package android.app;


class LoaderManagerImpl extends android.app.LoaderManager {
    static final java.lang.String TAG = "LoaderManager";

    static boolean DEBUG = false;

    // These are the currently active loaders.  A loader is here
    // from the time its load is started until it has been explicitly
    // stopped or restarted by the application.
    final android.util.SparseArray<android.app.LoaderManagerImpl.LoaderInfo> mLoaders = new android.util.SparseArray<android.app.LoaderManagerImpl.LoaderInfo>(0);

    // These are previously run loaders.  This list is maintained internally
    // to avoid destroying a loader while an application is still using it.
    // It allows an application to restart a loader, but continue using its
    // previously run loader until the new loader's data is available.
    final android.util.SparseArray<android.app.LoaderManagerImpl.LoaderInfo> mInactiveLoaders = new android.util.SparseArray<android.app.LoaderManagerImpl.LoaderInfo>(0);

    final java.lang.String mWho;

    boolean mStarted;

    boolean mRetaining;

    boolean mRetainingStarted;

    boolean mCreatingLoader;

    private android.app.FragmentHostCallback mHost;

    final class LoaderInfo implements android.content.Loader.OnLoadCanceledListener<java.lang.Object> , android.content.Loader.OnLoadCompleteListener<java.lang.Object> {
        final int mId;

        final android.os.Bundle mArgs;

        android.app.LoaderManager.LoaderCallbacks<java.lang.Object> mCallbacks;

        android.content.Loader<java.lang.Object> mLoader;

        boolean mHaveData;

        boolean mDeliveredData;

        java.lang.Object mData;

        boolean mStarted;

        boolean mRetaining;

        boolean mRetainingStarted;

        boolean mReportNextStart;

        boolean mDestroyed;

        boolean mListenerRegistered;

        android.app.LoaderManagerImpl.LoaderInfo mPendingLoader;

        public LoaderInfo(int id, android.os.Bundle args, android.app.LoaderManager.LoaderCallbacks<java.lang.Object> callbacks) {
            mId = id;
            mArgs = args;
            mCallbacks = callbacks;
        }

        void start() {
            if (mRetaining && mRetainingStarted) {
                // Our owner is started, but we were being retained from a
                // previous instance in the started state...  so there is really
                // nothing to do here, since the loaders are still started.
                mStarted = true;
                return;
            }
            if (mStarted) {
                // If loader already started, don't restart.
                return;
            }
            mStarted = true;
            if (android.app.LoaderManagerImpl.DEBUG)
                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Starting: " + this);

            if ((mLoader == null) && (mCallbacks != null)) {
                mLoader = mCallbacks.onCreateLoader(mId, mArgs);
            }
            if (mLoader != null) {
                if (mLoader.getClass().isMemberClass() && (!java.lang.reflect.Modifier.isStatic(mLoader.getClass().getModifiers()))) {
                    throw new java.lang.IllegalArgumentException("Object returned from onCreateLoader must not be a non-static inner member class: " + mLoader);
                }
                if (!mListenerRegistered) {
                    mLoader.registerListener(mId, this);
                    mLoader.registerOnLoadCanceledListener(this);
                    mListenerRegistered = true;
                }
                mLoader.startLoading();
            }
        }

        void retain() {
            if (android.app.LoaderManagerImpl.DEBUG)
                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Retaining: " + this);

            mRetaining = true;
            mRetainingStarted = mStarted;
            mStarted = false;
            mCallbacks = null;
        }

        void finishRetain() {
            if (mRetaining) {
                if (android.app.LoaderManagerImpl.DEBUG)
                    android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Finished Retaining: " + this);

                mRetaining = false;
                if (mStarted != mRetainingStarted) {
                    if (!mStarted) {
                        // This loader was retained in a started state, but
                        // at the end of retaining everything our owner is
                        // no longer started...  so make it stop.
                        stop();
                    }
                }
            }
            if ((mStarted && mHaveData) && (!mReportNextStart)) {
                // This loader has retained its data, either completely across
                // a configuration change or just whatever the last data set
                // was after being restarted from a stop, and now at the point of
                // finishing the retain we find we remain started, have
                // our data, and the owner has a new callback...  so
                // let's deliver the data now.
                callOnLoadFinished(mLoader, mData);
            }
        }

        void reportStart() {
            if (mStarted) {
                if (mReportNextStart) {
                    mReportNextStart = false;
                    if (mHaveData && (!mRetaining)) {
                        callOnLoadFinished(mLoader, mData);
                    }
                }
            }
        }

        void stop() {
            if (android.app.LoaderManagerImpl.DEBUG)
                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Stopping: " + this);

            mStarted = false;
            if (!mRetaining) {
                if ((mLoader != null) && mListenerRegistered) {
                    // Let the loader know we're done with it
                    mListenerRegistered = false;
                    mLoader.unregisterListener(this);
                    mLoader.unregisterOnLoadCanceledListener(this);
                    mLoader.stopLoading();
                }
            }
        }

        boolean cancel() {
            if (android.app.LoaderManagerImpl.DEBUG)
                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Canceling: " + this);

            if ((mStarted && (mLoader != null)) && mListenerRegistered) {
                final boolean cancelLoadResult = mLoader.cancelLoad();
                if (!cancelLoadResult) {
                    onLoadCanceled(mLoader);
                }
                return cancelLoadResult;
            }
            return false;
        }

        void destroy() {
            if (android.app.LoaderManagerImpl.DEBUG)
                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Destroying: " + this);

            mDestroyed = true;
            boolean needReset = mDeliveredData;
            mDeliveredData = false;
            if ((((mCallbacks != null) && (mLoader != null)) && mHaveData) && needReset) {
                if (android.app.LoaderManagerImpl.DEBUG)
                    android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Reseting: " + this);

                java.lang.String lastBecause = null;
                if (mHost != null) {
                    lastBecause = mHost.mFragmentManager.mNoTransactionsBecause;
                    mHost.mFragmentManager.mNoTransactionsBecause = "onLoaderReset";
                }
                try {
                    mCallbacks.onLoaderReset(mLoader);
                } finally {
                    if (mHost != null) {
                        mHost.mFragmentManager.mNoTransactionsBecause = lastBecause;
                    }
                }
            }
            mCallbacks = null;
            mData = null;
            mHaveData = false;
            if (mLoader != null) {
                if (mListenerRegistered) {
                    mListenerRegistered = false;
                    mLoader.unregisterListener(this);
                    mLoader.unregisterOnLoadCanceledListener(this);
                }
                mLoader.reset();
            }
            if (mPendingLoader != null) {
                mPendingLoader.destroy();
            }
        }

        @java.lang.Override
        public void onLoadCanceled(android.content.Loader<java.lang.Object> loader) {
            if (android.app.LoaderManagerImpl.DEBUG)
                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "onLoadCanceled: " + this);

            if (mDestroyed) {
                if (android.app.LoaderManagerImpl.DEBUG)
                    android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Ignoring load canceled -- destroyed");

                return;
            }
            if (mLoaders.get(mId) != this) {
                // This cancellation message is not coming from the current active loader.
                // We don't care about it.
                if (android.app.LoaderManagerImpl.DEBUG)
                    android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Ignoring load canceled -- not active");

                return;
            }
            android.app.LoaderManagerImpl.LoaderInfo pending = mPendingLoader;
            if (pending != null) {
                // There is a new request pending and we were just
                // waiting for the old one to cancel or complete before starting
                // it.  So now it is time, switch over to the new loader.
                if (android.app.LoaderManagerImpl.DEBUG)
                    android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Switching to pending loader: " + pending);

                mPendingLoader = null;
                mLoaders.put(mId, null);
                destroy();
                installLoader(pending);
            }
        }

        @java.lang.Override
        public void onLoadComplete(android.content.Loader<java.lang.Object> loader, java.lang.Object data) {
            if (android.app.LoaderManagerImpl.DEBUG)
                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "onLoadComplete: " + this);

            if (mDestroyed) {
                if (android.app.LoaderManagerImpl.DEBUG)
                    android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Ignoring load complete -- destroyed");

                return;
            }
            if (mLoaders.get(mId) != this) {
                // This data is not coming from the current active loader.
                // We don't care about it.
                if (android.app.LoaderManagerImpl.DEBUG)
                    android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Ignoring load complete -- not active");

                return;
            }
            android.app.LoaderManagerImpl.LoaderInfo pending = mPendingLoader;
            if (pending != null) {
                // There is a new request pending and we were just
                // waiting for the old one to complete before starting
                // it.  So now it is time, switch over to the new loader.
                if (android.app.LoaderManagerImpl.DEBUG)
                    android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Switching to pending loader: " + pending);

                mPendingLoader = null;
                mLoaders.put(mId, null);
                destroy();
                installLoader(pending);
                return;
            }
            // Notify of the new data so the app can switch out the old data before
            // we try to destroy it.
            if ((mData != data) || (!mHaveData)) {
                mData = data;
                mHaveData = true;
                if (mStarted) {
                    callOnLoadFinished(loader, data);
                }
            }
            // if (DEBUG) Log.v(TAG, "  onLoadFinished returned: " + this);
            // We have now given the application the new loader with its
            // loaded data, so it should have stopped using the previous
            // loader.  If there is a previous loader on the inactive list,
            // clean it up.
            android.app.LoaderManagerImpl.LoaderInfo info = mInactiveLoaders.get(mId);
            if ((info != null) && (info != this)) {
                info.mDeliveredData = false;
                info.destroy();
                mInactiveLoaders.remove(mId);
            }
            if ((mHost != null) && (!hasRunningLoaders())) {
                mHost.mFragmentManager.startPendingDeferredFragments();
            }
        }

        void callOnLoadFinished(android.content.Loader<java.lang.Object> loader, java.lang.Object data) {
            if (mCallbacks != null) {
                java.lang.String lastBecause = null;
                if (mHost != null) {
                    lastBecause = mHost.mFragmentManager.mNoTransactionsBecause;
                    mHost.mFragmentManager.mNoTransactionsBecause = "onLoadFinished";
                }
                try {
                    if (android.app.LoaderManagerImpl.DEBUG)
                        android.util.Log.v(android.app.LoaderManagerImpl.TAG, (("  onLoadFinished in " + loader) + ": ") + loader.dataToString(data));

                    mCallbacks.onLoadFinished(loader, data);
                } finally {
                    if (mHost != null) {
                        mHost.mFragmentManager.mNoTransactionsBecause = lastBecause;
                    }
                }
                mDeliveredData = true;
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(64);
            sb.append("LoaderInfo{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(" #");
            sb.append(mId);
            sb.append(" : ");
            android.util.DebugUtils.buildShortClassTag(mLoader, sb);
            sb.append("}}");
            return sb.toString();
        }

        public void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            writer.print(prefix);
            writer.print("mId=");
            writer.print(mId);
            writer.print(" mArgs=");
            writer.println(mArgs);
            writer.print(prefix);
            writer.print("mCallbacks=");
            writer.println(mCallbacks);
            writer.print(prefix);
            writer.print("mLoader=");
            writer.println(mLoader);
            if (mLoader != null) {
                mLoader.dump(prefix + "  ", fd, writer, args);
            }
            if (mHaveData || mDeliveredData) {
                writer.print(prefix);
                writer.print("mHaveData=");
                writer.print(mHaveData);
                writer.print("  mDeliveredData=");
                writer.println(mDeliveredData);
                writer.print(prefix);
                writer.print("mData=");
                writer.println(mData);
            }
            writer.print(prefix);
            writer.print("mStarted=");
            writer.print(mStarted);
            writer.print(" mReportNextStart=");
            writer.print(mReportNextStart);
            writer.print(" mDestroyed=");
            writer.println(mDestroyed);
            writer.print(prefix);
            writer.print("mRetaining=");
            writer.print(mRetaining);
            writer.print(" mRetainingStarted=");
            writer.print(mRetainingStarted);
            writer.print(" mListenerRegistered=");
            writer.println(mListenerRegistered);
            if (mPendingLoader != null) {
                writer.print(prefix);
                writer.println("Pending Loader ");
                writer.print(mPendingLoader);
                writer.println(":");
                mPendingLoader.dump(prefix + "  ", fd, writer, args);
            }
        }
    }

    LoaderManagerImpl(java.lang.String who, android.app.FragmentHostCallback host, boolean started) {
        mWho = who;
        mHost = host;
        mStarted = started;
    }

    void updateHostController(android.app.FragmentHostCallback host) {
        mHost = host;
    }

    public android.app.FragmentHostCallback getFragmentHostCallback() {
        return mHost;
    }

    private android.app.LoaderManagerImpl.LoaderInfo createLoader(int id, android.os.Bundle args, android.app.LoaderManager.LoaderCallbacks<java.lang.Object> callback) {
        android.app.LoaderManagerImpl.LoaderInfo info = new android.app.LoaderManagerImpl.LoaderInfo(id, args, ((android.app.LoaderManager.LoaderCallbacks<java.lang.Object>) (callback)));
        android.content.Loader<java.lang.Object> loader = callback.onCreateLoader(id, args);
        info.mLoader = ((android.content.Loader<java.lang.Object>) (loader));
        return info;
    }

    private android.app.LoaderManagerImpl.LoaderInfo createAndInstallLoader(int id, android.os.Bundle args, android.app.LoaderManager.LoaderCallbacks<java.lang.Object> callback) {
        try {
            mCreatingLoader = true;
            android.app.LoaderManagerImpl.LoaderInfo info = createLoader(id, args, callback);
            installLoader(info);
            return info;
        } finally {
            mCreatingLoader = false;
        }
    }

    void installLoader(android.app.LoaderManagerImpl.LoaderInfo info) {
        mLoaders.put(info.mId, info);
        if (mStarted) {
            // The activity will start all existing loaders in it's onStart(),
            // so only start them here if we're past that point of the activitiy's
            // life cycle
            info.start();
        }
    }

    /**
     * Call to initialize a particular ID with a Loader.  If this ID already
     * has a Loader associated with it, it is left unchanged and any previous
     * callbacks replaced with the newly provided ones.  If there is not currently
     * a Loader for the ID, a new one is created and started.
     *
     * <p>This function should generally be used when a component is initializing,
     * to ensure that a Loader it relies on is created.  This allows it to re-use
     * an existing Loader's data if there already is one, so that for example
     * when an {@link Activity} is re-created after a configuration change it
     * does not need to re-create its loaders.
     *
     * <p>Note that in the case where an existing Loader is re-used, the
     * <var>args</var> given here <em>will be ignored</em> because you will
     * continue using the previous Loader.
     *
     * @param id
     * 		A unique (to this LoaderManager instance) identifier under
     * 		which to manage the new Loader.
     * @param args
     * 		Optional arguments that will be propagated to
     * 		{@link LoaderCallbacks#onCreateLoader(int, Bundle) LoaderCallbacks.onCreateLoader()}.
     * @param callback
     * 		Interface implementing management of this Loader.  Required.
     * 		Its onCreateLoader() method will be called while inside of the function to
     * 		instantiate the Loader object.
     */
    @java.lang.SuppressWarnings("unchecked")
    public <D> android.content.Loader<D> initLoader(int id, android.os.Bundle args, android.app.LoaderManager.LoaderCallbacks<D> callback) {
        if (mCreatingLoader) {
            throw new java.lang.IllegalStateException("Called while creating a loader");
        }
        android.app.LoaderManagerImpl.LoaderInfo info = mLoaders.get(id);
        if (android.app.LoaderManagerImpl.DEBUG)
            android.util.Log.v(android.app.LoaderManagerImpl.TAG, (("initLoader in " + this) + ": args=") + args);

        if (info == null) {
            // Loader doesn't already exist; create.
            info = createAndInstallLoader(id, args, ((android.app.LoaderManager.LoaderCallbacks<java.lang.Object>) (callback)));
            if (android.app.LoaderManagerImpl.DEBUG)
                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Created new loader " + info);

        } else {
            if (android.app.LoaderManagerImpl.DEBUG)
                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Re-using existing loader " + info);

            info.mCallbacks = ((android.app.LoaderManager.LoaderCallbacks<java.lang.Object>) (callback));
        }
        if (info.mHaveData && mStarted) {
            // If the loader has already generated its data, report it now.
            info.callOnLoadFinished(info.mLoader, info.mData);
        }
        return ((android.content.Loader<D>) (info.mLoader));
    }

    /**
     * Call to re-create the Loader associated with a particular ID.  If there
     * is currently a Loader associated with this ID, it will be
     * canceled/stopped/destroyed as appropriate.  A new Loader with the given
     * arguments will be created and its data delivered to you once available.
     *
     * <p>This function does some throttling of Loaders.  If too many Loaders
     * have been created for the given ID but not yet generated their data,
     * new calls to this function will create and return a new Loader but not
     * actually start it until some previous loaders have completed.
     *
     * <p>After calling this function, any previous Loaders associated with
     * this ID will be considered invalid, and you will receive no further
     * data updates from them.
     *
     * @param id
     * 		A unique (to this LoaderManager instance) identifier under
     * 		which to manage the new Loader.
     * @param args
     * 		Optional arguments that will be propagated to
     * 		{@link LoaderCallbacks#onCreateLoader(int, Bundle) LoaderCallbacks.onCreateLoader()}.
     * @param callback
     * 		Interface implementing management of this Loader.  Required.
     * 		Its onCreateLoader() method will be called while inside of the function to
     * 		instantiate the Loader object.
     */
    @java.lang.SuppressWarnings("unchecked")
    public <D> android.content.Loader<D> restartLoader(int id, android.os.Bundle args, android.app.LoaderManager.LoaderCallbacks<D> callback) {
        if (mCreatingLoader) {
            throw new java.lang.IllegalStateException("Called while creating a loader");
        }
        android.app.LoaderManagerImpl.LoaderInfo info = mLoaders.get(id);
        if (android.app.LoaderManagerImpl.DEBUG)
            android.util.Log.v(android.app.LoaderManagerImpl.TAG, (("restartLoader in " + this) + ": args=") + args);

        if (info != null) {
            android.app.LoaderManagerImpl.LoaderInfo inactive = mInactiveLoaders.get(id);
            if (inactive != null) {
                if (info.mHaveData) {
                    // This loader now has data...  we are probably being
                    // called from within onLoadComplete, where we haven't
                    // yet destroyed the last inactive loader.  So just do
                    // that now.
                    if (android.app.LoaderManagerImpl.DEBUG)
                        android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Removing last inactive loader: " + info);

                    inactive.mDeliveredData = false;
                    inactive.destroy();
                    info.mLoader.abandon();
                    mInactiveLoaders.put(id, info);
                } else {
                    // We already have an inactive loader for this ID that we are
                    // waiting for! Try to cancel; if this returns true then the task is still
                    // running and we have more work to do.
                    if (!info.cancel()) {
                        // The current Loader has not been started or was successfully canceled,
                        // we thus have no reason to keep it around. Remove it and a new
                        // LoaderInfo will be created below.
                        if (android.app.LoaderManagerImpl.DEBUG)
                            android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Current loader is stopped; replacing");

                        mLoaders.put(id, null);
                        info.destroy();
                    } else {
                        // Now we have three active loaders... we'll queue
                        // up this request to be processed once one of the other loaders
                        // finishes.
                        if (android.app.LoaderManagerImpl.DEBUG)
                            android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Current loader is running; configuring pending loader");

                        if (info.mPendingLoader != null) {
                            if (android.app.LoaderManagerImpl.DEBUG)
                                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Removing pending loader: " + info.mPendingLoader);

                            info.mPendingLoader.destroy();
                            info.mPendingLoader = null;
                        }
                        if (android.app.LoaderManagerImpl.DEBUG)
                            android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Enqueuing as new pending loader");

                        info.mPendingLoader = createLoader(id, args, ((android.app.LoaderManager.LoaderCallbacks<java.lang.Object>) (callback)));
                        return ((android.content.Loader<D>) (info.mPendingLoader.mLoader));
                    }
                }
            } else {
                // Keep track of the previous instance of this loader so we can destroy
                // it when the new one completes.
                if (android.app.LoaderManagerImpl.DEBUG)
                    android.util.Log.v(android.app.LoaderManagerImpl.TAG, "  Making last loader inactive: " + info);

                info.mLoader.abandon();
                mInactiveLoaders.put(id, info);
            }
        }
        info = createAndInstallLoader(id, args, ((android.app.LoaderManager.LoaderCallbacks<java.lang.Object>) (callback)));
        return ((android.content.Loader<D>) (info.mLoader));
    }

    /**
     * Rip down, tear apart, shred to pieces a current Loader ID.  After returning
     * from this function, any Loader objects associated with this ID are
     * destroyed.  Any data associated with them is destroyed.  You better not
     * be using it when you do this.
     *
     * @param id
     * 		Identifier of the Loader to be destroyed.
     */
    public void destroyLoader(int id) {
        if (mCreatingLoader) {
            throw new java.lang.IllegalStateException("Called while creating a loader");
        }
        if (android.app.LoaderManagerImpl.DEBUG)
            android.util.Log.v(android.app.LoaderManagerImpl.TAG, (("destroyLoader in " + this) + " of ") + id);

        int idx = mLoaders.indexOfKey(id);
        if (idx >= 0) {
            android.app.LoaderManagerImpl.LoaderInfo info = mLoaders.valueAt(idx);
            mLoaders.removeAt(idx);
            info.destroy();
        }
        idx = mInactiveLoaders.indexOfKey(id);
        if (idx >= 0) {
            android.app.LoaderManagerImpl.LoaderInfo info = mInactiveLoaders.valueAt(idx);
            mInactiveLoaders.removeAt(idx);
            info.destroy();
        }
        if ((mHost != null) && (!hasRunningLoaders())) {
            mHost.mFragmentManager.startPendingDeferredFragments();
        }
    }

    /**
     * Return the most recent Loader object associated with the
     * given ID.
     */
    @java.lang.SuppressWarnings("unchecked")
    public <D> android.content.Loader<D> getLoader(int id) {
        if (mCreatingLoader) {
            throw new java.lang.IllegalStateException("Called while creating a loader");
        }
        android.app.LoaderManagerImpl.LoaderInfo loaderInfo = mLoaders.get(id);
        if (loaderInfo != null) {
            if (loaderInfo.mPendingLoader != null) {
                return ((android.content.Loader<D>) (loaderInfo.mPendingLoader.mLoader));
            }
            return ((android.content.Loader<D>) (loaderInfo.mLoader));
        }
        return null;
    }

    void doStart() {
        if (android.app.LoaderManagerImpl.DEBUG)
            android.util.Log.v(android.app.LoaderManagerImpl.TAG, "Starting in " + this);

        if (mStarted) {
            java.lang.RuntimeException e = new java.lang.RuntimeException("here");
            e.fillInStackTrace();
            android.util.Log.w(android.app.LoaderManagerImpl.TAG, "Called doStart when already started: " + this, e);
            return;
        }
        mStarted = true;
        // Call out to sub classes so they can start their loaders
        // Let the existing loaders know that we want to be notified when a load is complete
        for (int i = mLoaders.size() - 1; i >= 0; i--) {
            mLoaders.valueAt(i).start();
        }
    }

    void doStop() {
        if (android.app.LoaderManagerImpl.DEBUG)
            android.util.Log.v(android.app.LoaderManagerImpl.TAG, "Stopping in " + this);

        if (!mStarted) {
            java.lang.RuntimeException e = new java.lang.RuntimeException("here");
            e.fillInStackTrace();
            android.util.Log.w(android.app.LoaderManagerImpl.TAG, "Called doStop when not started: " + this, e);
            return;
        }
        for (int i = mLoaders.size() - 1; i >= 0; i--) {
            mLoaders.valueAt(i).stop();
        }
        mStarted = false;
    }

    void doRetain() {
        if (android.app.LoaderManagerImpl.DEBUG)
            android.util.Log.v(android.app.LoaderManagerImpl.TAG, "Retaining in " + this);

        if (!mStarted) {
            java.lang.RuntimeException e = new java.lang.RuntimeException("here");
            e.fillInStackTrace();
            android.util.Log.w(android.app.LoaderManagerImpl.TAG, "Called doRetain when not started: " + this, e);
            return;
        }
        mRetaining = true;
        mStarted = false;
        for (int i = mLoaders.size() - 1; i >= 0; i--) {
            mLoaders.valueAt(i).retain();
        }
    }

    void finishRetain() {
        if (mRetaining) {
            if (android.app.LoaderManagerImpl.DEBUG)
                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "Finished Retaining in " + this);

            mRetaining = false;
            for (int i = mLoaders.size() - 1; i >= 0; i--) {
                mLoaders.valueAt(i).finishRetain();
            }
        }
    }

    void doReportNextStart() {
        for (int i = mLoaders.size() - 1; i >= 0; i--) {
            mLoaders.valueAt(i).mReportNextStart = true;
        }
    }

    void doReportStart() {
        for (int i = mLoaders.size() - 1; i >= 0; i--) {
            mLoaders.valueAt(i).reportStart();
        }
    }

    void doDestroy() {
        if (!mRetaining) {
            if (android.app.LoaderManagerImpl.DEBUG)
                android.util.Log.v(android.app.LoaderManagerImpl.TAG, "Destroying Active in " + this);

            for (int i = mLoaders.size() - 1; i >= 0; i--) {
                mLoaders.valueAt(i).destroy();
            }
            mLoaders.clear();
        }
        if (android.app.LoaderManagerImpl.DEBUG)
            android.util.Log.v(android.app.LoaderManagerImpl.TAG, "Destroying Inactive in " + this);

        for (int i = mInactiveLoaders.size() - 1; i >= 0; i--) {
            mInactiveLoaders.valueAt(i).destroy();
        }
        mInactiveLoaders.clear();
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("LoaderManager{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(" in ");
        android.util.DebugUtils.buildShortClassTag(mHost, sb);
        sb.append("}}");
        return sb.toString();
    }

    @java.lang.Override
    public void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (mLoaders.size() > 0) {
            writer.print(prefix);
            writer.println("Active Loaders:");
            java.lang.String innerPrefix = prefix + "    ";
            for (int i = 0; i < mLoaders.size(); i++) {
                android.app.LoaderManagerImpl.LoaderInfo li = mLoaders.valueAt(i);
                writer.print(prefix);
                writer.print("  #");
                writer.print(mLoaders.keyAt(i));
                writer.print(": ");
                writer.println(li.toString());
                li.dump(innerPrefix, fd, writer, args);
            }
        }
        if (mInactiveLoaders.size() > 0) {
            writer.print(prefix);
            writer.println("Inactive Loaders:");
            java.lang.String innerPrefix = prefix + "    ";
            for (int i = 0; i < mInactiveLoaders.size(); i++) {
                android.app.LoaderManagerImpl.LoaderInfo li = mInactiveLoaders.valueAt(i);
                writer.print(prefix);
                writer.print("  #");
                writer.print(mInactiveLoaders.keyAt(i));
                writer.print(": ");
                writer.println(li.toString());
                li.dump(innerPrefix, fd, writer, args);
            }
        }
    }

    public boolean hasRunningLoaders() {
        boolean loadersRunning = false;
        final int count = mLoaders.size();
        for (int i = 0; i < count; i++) {
            final android.app.LoaderManagerImpl.LoaderInfo li = mLoaders.valueAt(i);
            loadersRunning |= li.mStarted && (!li.mDeliveredData);
        }
        return loadersRunning;
    }
}

