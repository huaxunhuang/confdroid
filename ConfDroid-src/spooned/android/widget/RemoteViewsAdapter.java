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
 * An adapter to a RemoteViewsService which fetches and caches RemoteViews to be later inflated as
 * child views.
 *
 * The adapter runs in the host process, typically a Launcher app.
 *
 * It makes a service connection to the {@link RemoteViewsService} running in the
 * AppWidgetsProvider's process. This connection is made on a background thread (and proxied via
 * the platform to get the bind permissions) and all interaction with the service is done on the
 * background thread.
 *
 * On first bind, the adapter will load can cache the RemoteViews locally. Afterwards the
 * connection is only made when new RemoteViews are required.
 *
 * @unknown 
 */
public class RemoteViewsAdapter extends android.widget.BaseAdapter implements android.os.Handler.Callback {
    private static final java.lang.String TAG = "RemoteViewsAdapter";

    // The max number of items in the cache
    private static final int DEFAULT_CACHE_SIZE = 40;

    // The delay (in millis) to wait until attempting to unbind from a service after a request.
    // This ensures that we don't stay continually bound to the service and that it can be destroyed
    // if we need the memory elsewhere in the system.
    private static final int UNBIND_SERVICE_DELAY = 5000;

    // Default height for the default loading view, in case we cannot get inflate the first view
    private static final int DEFAULT_LOADING_VIEW_HEIGHT = 50;

    // We cache the FixedSizeRemoteViewsCaches across orientation. These are the related data
    // structures;
    private static final java.util.HashMap<android.widget.RemoteViewsAdapter.RemoteViewsCacheKey, android.widget.RemoteViewsAdapter.FixedSizeRemoteViewsCache> sCachedRemoteViewsCaches = new java.util.HashMap<>();

    private static final java.util.HashMap<android.widget.RemoteViewsAdapter.RemoteViewsCacheKey, java.lang.Runnable> sRemoteViewsCacheRemoveRunnables = new java.util.HashMap<>();

    private static android.os.HandlerThread sCacheRemovalThread;

    private static android.os.Handler sCacheRemovalQueue;

    // We keep the cache around for a duration after onSaveInstanceState for use on re-inflation.
    // If a new RemoteViewsAdapter with the same intent / widget id isn't constructed within this
    // duration, the cache is dropped.
    private static final int REMOTE_VIEWS_CACHE_DURATION = 5000;

    private final android.content.Context mContext;

    private final android.content.Intent mIntent;

    private final int mAppWidgetId;

    private final boolean mOnLightBackground;

    private final java.util.concurrent.Executor mAsyncViewLoadExecutor;

    private android.widget.RemoteViews.OnClickHandler mRemoteViewsOnClickHandler;

    @android.annotation.UnsupportedAppUsage
    private final android.widget.RemoteViewsAdapter.FixedSizeRemoteViewsCache mCache;

    private int mVisibleWindowLowerBound;

    private int mVisibleWindowUpperBound;

    // The set of requested views that are to be notified when the associated RemoteViews are
    // loaded.
    private android.widget.RemoteViewsAdapter.RemoteViewsFrameLayoutRefSet mRequestedViews;

    @android.annotation.UnsupportedAppUsage
    private final android.os.HandlerThread mWorkerThread;

    // items may be interrupted within the normally processed queues
    private final android.os.Handler mMainHandler;

    private final android.widget.RemoteViewsAdapter.RemoteServiceHandler mServiceHandler;

    private final android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback mCallback;

    // Used to indicate to the AdapterView that it can use this Adapter immediately after
    // construction (happens when we have a cached FixedSizeRemoteViewsCache).
    private boolean mDataReady = false;

    /**
     * USed to dedupe {@link RemoteViews#mApplication} so that we do not hold on to
     * multiple copies of the same ApplicationInfo object.
     */
    private android.content.pm.ApplicationInfo mLastRemoteViewAppInfo;

    /**
     * An interface for the RemoteAdapter to notify other classes when adapters
     * are actually connected to/disconnected from their actual services.
     */
    public interface RemoteAdapterConnectionCallback {
        /**
         *
         *
         * @return whether the adapter was set or not.
         */
        boolean onRemoteAdapterConnected();

        void onRemoteAdapterDisconnected();

        /**
         * This defers a notifyDataSetChanged on the pending RemoteViewsAdapter if it has not
         * connected yet.
         */
        void deferNotifyDataSetChanged();

        void setRemoteViewsAdapter(android.content.Intent intent, boolean isAsync);
    }

    public static class AsyncRemoteAdapterAction implements java.lang.Runnable {
        private final android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback mCallback;

        private final android.content.Intent mIntent;

        public AsyncRemoteAdapterAction(android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback callback, android.content.Intent intent) {
            mCallback = callback;
            mIntent = intent;
        }

        @java.lang.Override
        public void run() {
            mCallback.setRemoteViewsAdapter(mIntent, true);
        }
    }

    static final int MSG_REQUEST_BIND = 1;

    static final int MSG_NOTIFY_DATA_SET_CHANGED = 2;

    static final int MSG_LOAD_NEXT_ITEM = 3;

    static final int MSG_UNBIND_SERVICE = 4;

    private static final int MSG_MAIN_HANDLER_COMMIT_METADATA = 1;

    private static final int MSG_MAIN_HANDLER_SUPER_NOTIFY_DATA_SET_CHANGED = 2;

    private static final int MSG_MAIN_HANDLER_REMOTE_ADAPTER_CONNECTED = 3;

    private static final int MSG_MAIN_HANDLER_REMOTE_ADAPTER_DISCONNECTED = 4;

    private static final int MSG_MAIN_HANDLER_REMOTE_VIEWS_LOADED = 5;

    /**
     * Handler for various interactions with the {@link RemoteViewsService}.
     */
    private static class RemoteServiceHandler extends android.os.Handler implements android.content.ServiceConnection {
        private final java.lang.ref.WeakReference<android.widget.RemoteViewsAdapter> mAdapter;

        private final android.content.Context mContext;

        private com.android.internal.widget.IRemoteViewsFactory mRemoteViewsFactory;

        // The last call to notifyDataSetChanged didn't succeed, try again on next service bind.
        private boolean mNotifyDataSetChangedPending = false;

        private boolean mBindRequested = false;

        RemoteServiceHandler(android.os.Looper workerLooper, android.widget.RemoteViewsAdapter adapter, android.content.Context context) {
            super(workerLooper);
            mAdapter = new java.lang.ref.WeakReference<>(adapter);
            mContext = context;
        }

        @java.lang.Override
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            // This is called on the same thread.
            mRemoteViewsFactory = IRemoteViewsFactory.Stub.asInterface(service);
            enqueueDeferredUnbindServiceMessage();
            android.widget.RemoteViewsAdapter adapter = mAdapter.get();
            if (adapter == null) {
                return;
            }
            if (mNotifyDataSetChangedPending) {
                mNotifyDataSetChangedPending = false;
                android.os.Message msg = android.os.Message.obtain(this, android.widget.RemoteViewsAdapter.MSG_NOTIFY_DATA_SET_CHANGED);
                handleMessage(msg);
                msg.recycle();
            } else {
                if (!sendNotifyDataSetChange(false)) {
                    return;
                }
                // Request meta data so that we have up to date data when calling back to
                // the remote adapter callback
                adapter.updateTemporaryMetaData(mRemoteViewsFactory);
                adapter.mMainHandler.sendEmptyMessage(android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_COMMIT_METADATA);
                adapter.mMainHandler.sendEmptyMessage(android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_REMOTE_ADAPTER_CONNECTED);
            }
        }

        @java.lang.Override
        public void onServiceDisconnected(android.content.ComponentName name) {
            mRemoteViewsFactory = null;
            android.widget.RemoteViewsAdapter adapter = mAdapter.get();
            if (adapter != null) {
                adapter.mMainHandler.sendEmptyMessage(android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_REMOTE_ADAPTER_DISCONNECTED);
            }
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.widget.RemoteViewsAdapter adapter = mAdapter.get();
            switch (msg.what) {
                case android.widget.RemoteViewsAdapter.MSG_REQUEST_BIND :
                    {
                        if ((adapter == null) || (mRemoteViewsFactory != null)) {
                            enqueueDeferredUnbindServiceMessage();
                        }
                        if (mBindRequested) {
                            return;
                        }
                        int flags = android.content.Context.BIND_AUTO_CREATE | android.content.Context.BIND_FOREGROUND_SERVICE_WHILE_AWAKE;
                        final android.app.IServiceConnection sd = mContext.getServiceDispatcher(this, this, flags);
                        android.content.Intent intent = ((android.content.Intent) (msg.obj));
                        int appWidgetId = msg.arg1;
                        try {
                            mBindRequested = android.appwidget.AppWidgetManager.getInstance(mContext).bindRemoteViewsService(mContext, appWidgetId, intent, sd, flags);
                        } catch (java.lang.Exception e) {
                            android.util.Log.e(android.widget.RemoteViewsAdapter.TAG, "Failed to bind remoteViewsService: " + e.getMessage());
                        }
                        return;
                    }
                case android.widget.RemoteViewsAdapter.MSG_NOTIFY_DATA_SET_CHANGED :
                    {
                        enqueueDeferredUnbindServiceMessage();
                        if (adapter == null) {
                            return;
                        }
                        if (mRemoteViewsFactory == null) {
                            mNotifyDataSetChangedPending = true;
                            adapter.requestBindService();
                            return;
                        }
                        if (!sendNotifyDataSetChange(true)) {
                            return;
                        }
                        // Flush the cache so that we can reload new items from the service
                        synchronized(adapter.mCache) {
                            adapter.mCache.reset();
                        }
                        // Re-request the new metadata (only after the notification to the factory)
                        adapter.updateTemporaryMetaData(mRemoteViewsFactory);
                        int newCount;
                        int[] visibleWindow;
                        synchronized(adapter.mCache.getTemporaryMetaData()) {
                            newCount = adapter.mCache.getTemporaryMetaData().count;
                            visibleWindow = adapter.getVisibleWindow(newCount);
                        }
                        // Pre-load (our best guess of) the views which are currently visible in the
                        // AdapterView. This mitigates flashing and flickering of loading views when a
                        // widget notifies that its data has changed.
                        for (int position : visibleWindow) {
                            // Because temporary meta data is only ever modified from this thread
                            // (ie. mWorkerThread), it is safe to assume that count is a valid
                            // representation.
                            if (position < newCount) {
                                adapter.updateRemoteViews(mRemoteViewsFactory, position, false);
                            }
                        }
                        // Propagate the notification back to the base adapter
                        adapter.mMainHandler.sendEmptyMessage(android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_COMMIT_METADATA);
                        adapter.mMainHandler.sendEmptyMessage(android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_SUPER_NOTIFY_DATA_SET_CHANGED);
                        return;
                    }
                case android.widget.RemoteViewsAdapter.MSG_LOAD_NEXT_ITEM :
                    {
                        if ((adapter == null) || (mRemoteViewsFactory == null)) {
                            return;
                        }
                        removeMessages(android.widget.RemoteViewsAdapter.MSG_UNBIND_SERVICE);
                        // Get the next index to load
                        final int position = adapter.mCache.getNextIndexToLoad();
                        if (position > (-1)) {
                            // Load the item, and notify any existing RemoteViewsFrameLayouts
                            adapter.updateRemoteViews(mRemoteViewsFactory, position, true);
                            // Queue up for the next one to load
                            sendEmptyMessage(android.widget.RemoteViewsAdapter.MSG_LOAD_NEXT_ITEM);
                        } else {
                            // No more items to load, so queue unbind
                            enqueueDeferredUnbindServiceMessage();
                        }
                        return;
                    }
                case android.widget.RemoteViewsAdapter.MSG_UNBIND_SERVICE :
                    {
                        unbindNow();
                        return;
                    }
            }
        }

        protected void unbindNow() {
            if (mBindRequested) {
                mBindRequested = false;
                mContext.unbindService(this);
            }
            mRemoteViewsFactory = null;
        }

        private boolean sendNotifyDataSetChange(boolean always) {
            try {
                if (always || (!mRemoteViewsFactory.isCreated())) {
                    mRemoteViewsFactory.onDataSetChanged();
                }
                return true;
            } catch (android.os.RemoteException | java.lang.RuntimeException e) {
                android.util.Log.e(android.widget.RemoteViewsAdapter.TAG, "Error in updateNotifyDataSetChanged(): " + e.getMessage());
                return false;
            }
        }

        private void enqueueDeferredUnbindServiceMessage() {
            removeMessages(android.widget.RemoteViewsAdapter.MSG_UNBIND_SERVICE);
            sendEmptyMessageDelayed(android.widget.RemoteViewsAdapter.MSG_UNBIND_SERVICE, android.widget.RemoteViewsAdapter.UNBIND_SERVICE_DELAY);
        }
    }

    /**
     * A FrameLayout which contains a loading view, and manages the re/applying of RemoteViews when
     * they are loaded.
     */
    static class RemoteViewsFrameLayout extends android.appwidget.AppWidgetHostView {
        private final android.widget.RemoteViewsAdapter.FixedSizeRemoteViewsCache mCache;

        public int cacheIndex = -1;

        public RemoteViewsFrameLayout(android.content.Context context, android.widget.RemoteViewsAdapter.FixedSizeRemoteViewsCache cache) {
            super(context);
            mCache = cache;
        }

        /**
         * Updates this RemoteViewsFrameLayout depending on the view that was loaded.
         *
         * @param view
         * 		the RemoteViews that was loaded. If null, the RemoteViews was not loaded
         * 		successfully.
         * @param forceApplyAsync
         * 		when true, the host will always try to inflate the view
         * 		asynchronously (for eg, when we are already showing the loading
         * 		view)
         */
        public void onRemoteViewsLoaded(android.widget.RemoteViews view, android.widget.RemoteViews.OnClickHandler handler, boolean forceApplyAsync) {
            setOnClickHandler(handler);
            applyRemoteViews(view, forceApplyAsync || ((view != null) && view.prefersAsyncApply()));
        }

        /**
         * Creates a default loading view. Uses the size of the first row as a guide for the
         * size of the loading view.
         */
        @java.lang.Override
        protected android.view.View getDefaultView() {
            int viewHeight = mCache.getMetaData().getLoadingTemplate(getContext()).defaultHeight;
            // Compose the loading view text
            android.widget.TextView loadingTextView = ((android.widget.TextView) (android.view.LayoutInflater.from(getContext()).inflate(com.android.internal.R.layout.remote_views_adapter_default_loading_view, this, false)));
            loadingTextView.setHeight(viewHeight);
            return loadingTextView;
        }

        @java.lang.Override
        protected android.content.Context getRemoteContext() {
            return null;
        }

        @java.lang.Override
        protected android.view.View getErrorView() {
            // Use the default loading view as the error view.
            return getDefaultView();
        }
    }

    /**
     * Stores the references of all the RemoteViewsFrameLayouts that have been returned by the
     * adapter that have not yet had their RemoteViews loaded.
     */
    private class RemoteViewsFrameLayoutRefSet extends android.util.SparseArray<java.util.LinkedList<android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout>> {
        /**
         * Adds a new reference to a RemoteViewsFrameLayout returned by the adapter.
         */
        public void add(int position, android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout layout) {
            java.util.LinkedList<android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout> refs = get(position);
            // Create the list if necessary
            if (refs == null) {
                refs = new java.util.LinkedList<>();
                put(position, refs);
            }
            // Add the references to the list
            layout.cacheIndex = position;
            refs.add(layout);
        }

        /**
         * Notifies each of the RemoteViewsFrameLayouts associated with a particular position that
         * the associated RemoteViews has loaded.
         */
        public void notifyOnRemoteViewsLoaded(int position, android.widget.RemoteViews view) {
            if (view == null)
                return;

            // Remove this set from the original mapping
            final java.util.LinkedList<android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout> refs = removeReturnOld(position);
            if (refs != null) {
                // Notify all the references for that position of the newly loaded RemoteViews
                for (final android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout ref : refs) {
                    ref.onRemoteViewsLoaded(view, mRemoteViewsOnClickHandler, true);
                }
            }
        }

        /**
         * We need to remove views from this set if they have been recycled by the AdapterView.
         */
        public void removeView(android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout rvfl) {
            if (rvfl.cacheIndex < 0) {
                return;
            }
            final java.util.LinkedList<android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout> refs = get(rvfl.cacheIndex);
            if (refs != null) {
                refs.remove(rvfl);
            }
            rvfl.cacheIndex = -1;
        }
    }

    /**
     * The meta-data associated with the cache in it's current state.
     */
    private static class RemoteViewsMetaData {
        int count;

        int viewTypeCount;

        boolean hasStableIds;

        // Used to determine how to construct loading views.  If a loading view is not specified
        // by the user, then we try and load the first view, and use its height as the height for
        // the default loading view.
        android.widget.RemoteViewsAdapter.LoadingViewTemplate loadingTemplate;

        // A mapping from type id to a set of unique type ids
        private final android.util.SparseIntArray mTypeIdIndexMap = new android.util.SparseIntArray();

        public RemoteViewsMetaData() {
            reset();
        }

        public void set(android.widget.RemoteViewsAdapter.RemoteViewsMetaData d) {
            synchronized(d) {
                count = d.count;
                viewTypeCount = d.viewTypeCount;
                hasStableIds = d.hasStableIds;
                loadingTemplate = d.loadingTemplate;
            }
        }

        public void reset() {
            count = 0;
            // by default there is at least one dummy view type
            viewTypeCount = 1;
            hasStableIds = true;
            loadingTemplate = null;
            mTypeIdIndexMap.clear();
        }

        public int getMappedViewType(int typeId) {
            int mappedTypeId = mTypeIdIndexMap.get(typeId, -1);
            if (mappedTypeId == (-1)) {
                // We +1 because the loading view always has view type id of 0
                mappedTypeId = mTypeIdIndexMap.size() + 1;
                mTypeIdIndexMap.put(typeId, mappedTypeId);
            }
            return mappedTypeId;
        }

        public boolean isViewTypeInRange(int typeId) {
            int mappedType = getMappedViewType(typeId);
            return mappedType < viewTypeCount;
        }

        public synchronized android.widget.RemoteViewsAdapter.LoadingViewTemplate getLoadingTemplate(android.content.Context context) {
            if (loadingTemplate == null) {
                loadingTemplate = new android.widget.RemoteViewsAdapter.LoadingViewTemplate(null, context);
            }
            return loadingTemplate;
        }
    }

    /**
     * The meta-data associated with a single item in the cache.
     */
    private static class RemoteViewsIndexMetaData {
        int typeId;

        long itemId;

        public RemoteViewsIndexMetaData(android.widget.RemoteViews v, long itemId) {
            set(v, itemId);
        }

        public void set(android.widget.RemoteViews v, long id) {
            itemId = id;
            if (v != null) {
                typeId = v.getLayoutId();
            } else {
                typeId = 0;
            }
        }
    }

    /**
     * Config diff flags for which the cache should be reset
     */
    private static final int CACHE_RESET_CONFIG_FLAGS = ((android.content.pm.ActivityInfo.CONFIG_FONT_SCALE | android.content.pm.ActivityInfo.CONFIG_UI_MODE) | android.content.pm.ActivityInfo.CONFIG_DENSITY) | android.content.pm.ActivityInfo.CONFIG_ASSETS_PATHS;

    /**
     *
     */
    private static class FixedSizeRemoteViewsCache {
        // The meta data related to all the RemoteViews, ie. count, is stable, etc.
        // The meta data objects are made final so that they can be locked on independently
        // of the FixedSizeRemoteViewsCache. If we ever lock on both meta data objects, it is in
        // the order mTemporaryMetaData followed by mMetaData.
        private final android.widget.RemoteViewsAdapter.RemoteViewsMetaData mMetaData = new android.widget.RemoteViewsAdapter.RemoteViewsMetaData();

        private final android.widget.RemoteViewsAdapter.RemoteViewsMetaData mTemporaryMetaData = new android.widget.RemoteViewsAdapter.RemoteViewsMetaData();

        // The cache/mapping of position to RemoteViewsMetaData.  This set is guaranteed to be
        // greater than or equal to the set of RemoteViews.
        // Note: The reason that we keep this separate from the RemoteViews cache below is that this
        // we still need to be able to access the mapping of position to meta data, without keeping
        // the heavy RemoteViews around.  The RemoteViews cache is trimmed to fixed constraints wrt.
        // memory and size, but this metadata cache will retain information until the data at the
        // position is guaranteed as not being necessary any more (usually on notifyDataSetChanged).
        private final android.util.SparseArray<android.widget.RemoteViewsAdapter.RemoteViewsIndexMetaData> mIndexMetaData = new android.util.SparseArray();

        // The cache of actual RemoteViews, which may be pruned if the cache gets too large, or uses
        // too much memory.
        private final android.util.SparseArray<android.widget.RemoteViews> mIndexRemoteViews = new android.util.SparseArray();

        // An array of indices to load, Indices which are explicitly requested are set to true,
        // and those determined by the preloading algorithm to prefetch are set to false.
        private final android.util.SparseBooleanArray mIndicesToLoad = new android.util.SparseBooleanArray();

        // We keep a reference of the last requested index to determine which item to prune the
        // farthest items from when we hit the memory limit
        private int mLastRequestedIndex;

        // The lower and upper bounds of the preloaded range
        private int mPreloadLowerBound;

        private int mPreloadUpperBound;

        // The bounds of this fixed cache, we will try and fill as many items into the cache up to
        // the maxCount number of items, or the maxSize memory usage.
        // The maxCountSlack is used to determine if a new position in the cache to be loaded is
        // sufficiently ouside the old set, prompting a shifting of the "window" of items to be
        // preloaded.
        private final int mMaxCount;

        private final int mMaxCountSlack;

        private static final float sMaxCountSlackPercent = 0.75F;

        private static final int sMaxMemoryLimitInBytes = (2 * 1024) * 1024;

        // Configuration for which the cache was created
        private final android.content.res.Configuration mConfiguration;

        FixedSizeRemoteViewsCache(int maxCacheSize, android.content.res.Configuration configuration) {
            mMaxCount = maxCacheSize;
            mMaxCountSlack = java.lang.Math.round(android.widget.RemoteViewsAdapter.FixedSizeRemoteViewsCache.sMaxCountSlackPercent * (mMaxCount / 2));
            mPreloadLowerBound = 0;
            mPreloadUpperBound = -1;
            mLastRequestedIndex = -1;
            mConfiguration = new android.content.res.Configuration(configuration);
        }

        public void insert(int position, android.widget.RemoteViews v, long itemId, int[] visibleWindow) {
            // Trim the cache if we go beyond the count
            if (mIndexRemoteViews.size() >= mMaxCount) {
                mIndexRemoteViews.remove(getFarthestPositionFrom(position, visibleWindow));
            }
            // Trim the cache if we go beyond the available memory size constraints
            int pruneFromPosition = (mLastRequestedIndex > (-1)) ? mLastRequestedIndex : position;
            while (getRemoteViewsBitmapMemoryUsage() >= android.widget.RemoteViewsAdapter.FixedSizeRemoteViewsCache.sMaxMemoryLimitInBytes) {
                // Note: This is currently the most naive mechanism for deciding what to prune when
                // we hit the memory limit.  In the future, we may want to calculate which index to
                // remove based on both its position as well as it's current memory usage, as well
                // as whether it was directly requested vs. whether it was preloaded by our caching
                // mechanism.
                int trimIndex = getFarthestPositionFrom(pruneFromPosition, visibleWindow);
                // Need to check that this is a valid index, to cover the case where you have only
                // a single view in the cache, but it's larger than the max memory limit
                if (trimIndex < 0) {
                    break;
                }
                mIndexRemoteViews.remove(trimIndex);
            } 
            // Update the metadata cache
            final android.widget.RemoteViewsAdapter.RemoteViewsIndexMetaData metaData = mIndexMetaData.get(position);
            if (metaData != null) {
                metaData.set(v, itemId);
            } else {
                mIndexMetaData.put(position, new android.widget.RemoteViewsAdapter.RemoteViewsIndexMetaData(v, itemId));
            }
            mIndexRemoteViews.put(position, v);
        }

        public android.widget.RemoteViewsAdapter.RemoteViewsMetaData getMetaData() {
            return mMetaData;
        }

        public android.widget.RemoteViewsAdapter.RemoteViewsMetaData getTemporaryMetaData() {
            return mTemporaryMetaData;
        }

        public android.widget.RemoteViews getRemoteViewsAt(int position) {
            return mIndexRemoteViews.get(position);
        }

        public android.widget.RemoteViewsAdapter.RemoteViewsIndexMetaData getMetaDataAt(int position) {
            return mIndexMetaData.get(position);
        }

        public void commitTemporaryMetaData() {
            synchronized(mTemporaryMetaData) {
                synchronized(mMetaData) {
                    mMetaData.set(mTemporaryMetaData);
                }
            }
        }

        private int getRemoteViewsBitmapMemoryUsage() {
            // Calculate the memory usage of all the RemoteViews bitmaps being cached
            int mem = 0;
            for (int i = mIndexRemoteViews.size() - 1; i >= 0; i--) {
                final android.widget.RemoteViews v = mIndexRemoteViews.valueAt(i);
                if (v != null) {
                    mem += v.estimateMemoryUsage();
                }
            }
            return mem;
        }

        private int getFarthestPositionFrom(int pos, int[] visibleWindow) {
            // Find the index farthest away and remove that
            int maxDist = 0;
            int maxDistIndex = -1;
            int maxDistNotVisible = 0;
            int maxDistIndexNotVisible = -1;
            for (int i = mIndexRemoteViews.size() - 1; i >= 0; i--) {
                int index = mIndexRemoteViews.keyAt(i);
                int dist = java.lang.Math.abs(index - pos);
                if ((dist > maxDistNotVisible) && (java.util.Arrays.binarySearch(visibleWindow, index) < 0)) {
                    // maxDistNotVisible/maxDistIndexNotVisible will store the index of the
                    // farthest non-visible position
                    maxDistIndexNotVisible = index;
                    maxDistNotVisible = dist;
                }
                if (dist >= maxDist) {
                    // maxDist/maxDistIndex will store the index of the farthest position
                    // regardless of whether it is visible or not
                    maxDistIndex = index;
                    maxDist = dist;
                }
            }
            if (maxDistIndexNotVisible > (-1)) {
                return maxDistIndexNotVisible;
            }
            return maxDistIndex;
        }

        public void queueRequestedPositionToLoad(int position) {
            mLastRequestedIndex = position;
            synchronized(mIndicesToLoad) {
                mIndicesToLoad.put(position, true);
            }
        }

        public boolean queuePositionsToBePreloadedFromRequestedPosition(int position) {
            // Check if we need to preload any items
            if ((mPreloadLowerBound <= position) && (position <= mPreloadUpperBound)) {
                int center = (mPreloadUpperBound + mPreloadLowerBound) / 2;
                if (java.lang.Math.abs(position - center) < mMaxCountSlack) {
                    return false;
                }
            }
            int count;
            synchronized(mMetaData) {
                count = mMetaData.count;
            }
            synchronized(mIndicesToLoad) {
                // Remove all indices which have not been previously requested.
                for (int i = mIndicesToLoad.size() - 1; i >= 0; i--) {
                    if (!mIndicesToLoad.valueAt(i)) {
                        mIndicesToLoad.removeAt(i);
                    }
                }
                // Add all the preload indices
                int halfMaxCount = mMaxCount / 2;
                mPreloadLowerBound = position - halfMaxCount;
                mPreloadUpperBound = position + halfMaxCount;
                int effectiveLowerBound = java.lang.Math.max(0, mPreloadLowerBound);
                int effectiveUpperBound = java.lang.Math.min(mPreloadUpperBound, count - 1);
                for (int i = effectiveLowerBound; i <= effectiveUpperBound; ++i) {
                    if ((mIndexRemoteViews.indexOfKey(i) < 0) && (!mIndicesToLoad.get(i))) {
                        // If the index has not been requested, and has not been loaded.
                        mIndicesToLoad.put(i, false);
                    }
                }
            }
            return true;
        }

        /**
         * Returns the next index to load
         */
        public int getNextIndexToLoad() {
            // We try and prioritize items that have been requested directly, instead
            // of items that are loaded as a result of the caching mechanism
            synchronized(mIndicesToLoad) {
                // Prioritize requested indices to be loaded first
                int index = mIndicesToLoad.indexOfValue(true);
                if (index < 0) {
                    // Otherwise, preload other indices as necessary
                    index = mIndicesToLoad.indexOfValue(false);
                }
                if (index < 0) {
                    return -1;
                } else {
                    int key = mIndicesToLoad.keyAt(index);
                    mIndicesToLoad.removeAt(index);
                    return key;
                }
            }
        }

        public boolean containsRemoteViewAt(int position) {
            return mIndexRemoteViews.indexOfKey(position) >= 0;
        }

        public boolean containsMetaDataAt(int position) {
            return mIndexMetaData.indexOfKey(position) >= 0;
        }

        public void reset() {
            // Note: We do not try and reset the meta data, since that information is still used by
            // collection views to validate it's own contents (and will be re-requested if the data
            // is invalidated through the notifyDataSetChanged() flow).
            mPreloadLowerBound = 0;
            mPreloadUpperBound = -1;
            mLastRequestedIndex = -1;
            mIndexRemoteViews.clear();
            mIndexMetaData.clear();
            synchronized(mIndicesToLoad) {
                mIndicesToLoad.clear();
            }
        }
    }

    static class RemoteViewsCacheKey {
        final android.content.Intent.FilterComparison filter;

        final int widgetId;

        RemoteViewsCacheKey(android.content.Intent.FilterComparison filter, int widgetId) {
            this.filter = filter;
            this.widgetId = widgetId;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (!(o instanceof android.widget.RemoteViewsAdapter.RemoteViewsCacheKey)) {
                return false;
            }
            android.widget.RemoteViewsAdapter.RemoteViewsCacheKey other = ((android.widget.RemoteViewsAdapter.RemoteViewsCacheKey) (o));
            return other.filter.equals(filter) && (other.widgetId == widgetId);
        }

        @java.lang.Override
        public int hashCode() {
            return (filter == null ? 0 : filter.hashCode()) ^ (widgetId << 2);
        }
    }

    public RemoteViewsAdapter(android.content.Context context, android.content.Intent intent, android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback callback, boolean useAsyncLoader) {
        mContext = context;
        mIntent = intent;
        if (mIntent == null) {
            throw new java.lang.IllegalArgumentException("Non-null Intent must be specified.");
        }
        mAppWidgetId = intent.getIntExtra(android.widget.RemoteViews.EXTRA_REMOTEADAPTER_APPWIDGET_ID, -1);
        mRequestedViews = new android.widget.RemoteViewsAdapter.RemoteViewsFrameLayoutRefSet();
        mOnLightBackground = intent.getBooleanExtra(android.widget.RemoteViews.EXTRA_REMOTEADAPTER_ON_LIGHT_BACKGROUND, false);
        // Strip the previously injected app widget id from service intent
        intent.removeExtra(android.widget.RemoteViews.EXTRA_REMOTEADAPTER_APPWIDGET_ID);
        intent.removeExtra(android.widget.RemoteViews.EXTRA_REMOTEADAPTER_ON_LIGHT_BACKGROUND);
        // Initialize the worker thread
        mWorkerThread = new android.os.HandlerThread("RemoteViewsCache-loader");
        mWorkerThread.start();
        mMainHandler = new android.os.Handler(android.os.Looper.myLooper(), this);
        mServiceHandler = new android.widget.RemoteViewsAdapter.RemoteServiceHandler(mWorkerThread.getLooper(), this, context.getApplicationContext());
        mAsyncViewLoadExecutor = (useAsyncLoader) ? new android.widget.RemoteViewsAdapter.HandlerThreadExecutor(mWorkerThread) : null;
        mCallback = callback;
        if (android.widget.RemoteViewsAdapter.sCacheRemovalThread == null) {
            android.widget.RemoteViewsAdapter.sCacheRemovalThread = new android.os.HandlerThread("RemoteViewsAdapter-cachePruner");
            android.widget.RemoteViewsAdapter.sCacheRemovalThread.start();
            android.widget.RemoteViewsAdapter.sCacheRemovalQueue = new android.os.Handler(android.widget.RemoteViewsAdapter.sCacheRemovalThread.getLooper());
        }
        android.widget.RemoteViewsAdapter.RemoteViewsCacheKey key = new android.widget.RemoteViewsAdapter.RemoteViewsCacheKey(new android.content.Intent.FilterComparison(mIntent), mAppWidgetId);
        synchronized(android.widget.RemoteViewsAdapter.sCachedRemoteViewsCaches) {
            android.widget.RemoteViewsAdapter.FixedSizeRemoteViewsCache cache = android.widget.RemoteViewsAdapter.sCachedRemoteViewsCaches.get(key);
            android.content.res.Configuration config = context.getResources().getConfiguration();
            if ((cache == null) || ((cache.mConfiguration.diff(config) & android.widget.RemoteViewsAdapter.CACHE_RESET_CONFIG_FLAGS) != 0)) {
                mCache = new android.widget.RemoteViewsAdapter.FixedSizeRemoteViewsCache(android.widget.RemoteViewsAdapter.DEFAULT_CACHE_SIZE, config);
            } else {
                mCache = android.widget.RemoteViewsAdapter.sCachedRemoteViewsCaches.get(key);
                synchronized(mCache.mMetaData) {
                    if (mCache.mMetaData.count > 0) {
                        // As a precautionary measure, we verify that the meta data indicates a
                        // non-zero count before declaring that data is ready.
                        mDataReady = true;
                    }
                }
            }
            if (!mDataReady) {
                requestBindService();
            }
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            mServiceHandler.unbindNow();
            mWorkerThread.quit();
        } finally {
            super.finalize();
        }
    }

    @android.annotation.UnsupportedAppUsage
    public boolean isDataReady() {
        return mDataReady;
    }

    @android.annotation.UnsupportedAppUsage
    public void setRemoteViewsOnClickHandler(android.widget.RemoteViews.OnClickHandler handler) {
        mRemoteViewsOnClickHandler = handler;
    }

    @android.annotation.UnsupportedAppUsage
    public void saveRemoteViewsCache() {
        final android.widget.RemoteViewsAdapter.RemoteViewsCacheKey key = new android.widget.RemoteViewsAdapter.RemoteViewsCacheKey(new android.content.Intent.FilterComparison(mIntent), mAppWidgetId);
        synchronized(android.widget.RemoteViewsAdapter.sCachedRemoteViewsCaches) {
            // If we already have a remove runnable posted for this key, remove it.
            if (android.widget.RemoteViewsAdapter.sRemoteViewsCacheRemoveRunnables.containsKey(key)) {
                android.widget.RemoteViewsAdapter.sCacheRemovalQueue.removeCallbacks(android.widget.RemoteViewsAdapter.sRemoteViewsCacheRemoveRunnables.get(key));
                android.widget.RemoteViewsAdapter.sRemoteViewsCacheRemoveRunnables.remove(key);
            }
            int metaDataCount = 0;
            int numRemoteViewsCached = 0;
            synchronized(mCache.mMetaData) {
                metaDataCount = mCache.mMetaData.count;
            }
            synchronized(mCache) {
                numRemoteViewsCached = mCache.mIndexRemoteViews.size();
            }
            if ((metaDataCount > 0) && (numRemoteViewsCached > 0)) {
                android.widget.RemoteViewsAdapter.sCachedRemoteViewsCaches.put(key, mCache);
            }
            java.lang.Runnable r = () -> {
                synchronized(android.widget.RemoteViewsAdapter.sCachedRemoteViewsCaches) {
                    if (android.widget.RemoteViewsAdapter.sCachedRemoteViewsCaches.containsKey(key)) {
                        android.widget.RemoteViewsAdapter.sCachedRemoteViewsCaches.remove(key);
                    }
                    if (android.widget.RemoteViewsAdapter.sRemoteViewsCacheRemoveRunnables.containsKey(key)) {
                        android.widget.RemoteViewsAdapter.sRemoteViewsCacheRemoveRunnables.remove(key);
                    }
                }
            };
            android.widget.RemoteViewsAdapter.sRemoteViewsCacheRemoveRunnables.put(key, r);
            android.widget.RemoteViewsAdapter.sCacheRemovalQueue.postDelayed(r, android.widget.RemoteViewsAdapter.REMOTE_VIEWS_CACHE_DURATION);
        }
    }

    @android.annotation.WorkerThread
    private void updateTemporaryMetaData(com.android.internal.widget.IRemoteViewsFactory factory) {
        try {
            // get the properties/first view (so that we can use it to
            // measure our dummy views)
            boolean hasStableIds = factory.hasStableIds();
            int viewTypeCount = factory.getViewTypeCount();
            int count = factory.getCount();
            android.widget.RemoteViewsAdapter.LoadingViewTemplate loadingTemplate = new android.widget.RemoteViewsAdapter.LoadingViewTemplate(factory.getLoadingView(), mContext);
            if ((count > 0) && (loadingTemplate.remoteViews == null)) {
                android.widget.RemoteViews firstView = factory.getViewAt(0);
                if (firstView != null) {
                    loadingTemplate.loadFirstViewHeight(firstView, mContext, new android.widget.RemoteViewsAdapter.HandlerThreadExecutor(mWorkerThread));
                }
            }
            final android.widget.RemoteViewsAdapter.RemoteViewsMetaData tmpMetaData = mCache.getTemporaryMetaData();
            synchronized(tmpMetaData) {
                tmpMetaData.hasStableIds = hasStableIds;
                // We +1 because the base view type is the loading view
                tmpMetaData.viewTypeCount = viewTypeCount + 1;
                tmpMetaData.count = count;
                tmpMetaData.loadingTemplate = loadingTemplate;
            }
        } catch (android.os.RemoteException | java.lang.RuntimeException e) {
            android.util.Log.e("RemoteViewsAdapter", "Error in updateMetaData: " + e.getMessage());
            // If we encounter a crash when updating, we should reset the metadata & cache
            // and trigger a notifyDataSetChanged to update the widget accordingly
            synchronized(mCache.getMetaData()) {
                mCache.getMetaData().reset();
            }
            synchronized(mCache) {
                mCache.reset();
            }
            mMainHandler.sendEmptyMessage(android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_SUPER_NOTIFY_DATA_SET_CHANGED);
        }
    }

    @android.annotation.WorkerThread
    private void updateRemoteViews(com.android.internal.widget.IRemoteViewsFactory factory, int position, boolean notifyWhenLoaded) {
        // Load the item information from the remote service
        final android.widget.RemoteViews remoteViews;
        final long itemId;
        try {
            remoteViews = factory.getViewAt(position);
            itemId = factory.getItemId(position);
            if (remoteViews == null) {
                throw new java.lang.RuntimeException("Null remoteViews");
            }
        } catch (android.os.RemoteException | java.lang.RuntimeException e) {
            android.util.Log.e(android.widget.RemoteViewsAdapter.TAG, (("Error in updateRemoteViews(" + position) + "): ") + e.getMessage());
            // Return early to prevent additional work in re-centering the view cache, and
            // swapping from the loading view
            return;
        }
        if (remoteViews.mApplication != null) {
            // We keep track of last application info. This helps when all the remoteViews have
            // same applicationInfo, which should be the case for a typical adapter. But if every
            // view has different application info, there will not be any optimization.
            if ((mLastRemoteViewAppInfo != null) && remoteViews.hasSameAppInfo(mLastRemoteViewAppInfo)) {
                // We should probably also update the remoteViews for nested ViewActions.
                // Hopefully, RemoteViews in an adapter would be less complicated.
                remoteViews.mApplication = mLastRemoteViewAppInfo;
            } else {
                mLastRemoteViewAppInfo = remoteViews.mApplication;
            }
        }
        int layoutId = remoteViews.getLayoutId();
        android.widget.RemoteViewsAdapter.RemoteViewsMetaData metaData = mCache.getMetaData();
        boolean viewTypeInRange;
        int cacheCount;
        synchronized(metaData) {
            viewTypeInRange = metaData.isViewTypeInRange(layoutId);
            cacheCount = mCache.mMetaData.count;
        }
        synchronized(mCache) {
            if (viewTypeInRange) {
                int[] visibleWindow = getVisibleWindow(cacheCount);
                // Cache the RemoteViews we loaded
                mCache.insert(position, remoteViews, itemId, visibleWindow);
                if (notifyWhenLoaded) {
                    // Notify all the views that we have previously returned for this index that
                    // there is new data for it.
                    android.os.Message.obtain(mMainHandler, android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_REMOTE_VIEWS_LOADED, position, 0, remoteViews).sendToTarget();
                }
            } else {
                // We need to log an error here, as the the view type count specified by the
                // factory is less than the number of view types returned. We don't return this
                // view to the AdapterView, as this will cause an exception in the hosting process,
                // which contains the associated AdapterView.
                android.util.Log.e(android.widget.RemoteViewsAdapter.TAG, "Error: widget's RemoteViewsFactory returns more view types than " + " indicated by getViewTypeCount() ");
            }
        }
    }

    @android.annotation.UnsupportedAppUsage
    public android.content.Intent getRemoteViewsServiceIntent() {
        return mIntent;
    }

    public int getCount() {
        final android.widget.RemoteViewsAdapter.RemoteViewsMetaData metaData = mCache.getMetaData();
        synchronized(metaData) {
            return metaData.count;
        }
    }

    public java.lang.Object getItem(int position) {
        // Disallow arbitrary object to be associated with an item for the time being
        return null;
    }

    public long getItemId(int position) {
        synchronized(mCache) {
            if (mCache.containsMetaDataAt(position)) {
                return mCache.getMetaDataAt(position).itemId;
            }
            return 0;
        }
    }

    public int getItemViewType(int position) {
        final int typeId;
        synchronized(mCache) {
            if (mCache.containsMetaDataAt(position)) {
                typeId = mCache.getMetaDataAt(position).typeId;
            } else {
                return 0;
            }
        }
        final android.widget.RemoteViewsAdapter.RemoteViewsMetaData metaData = mCache.getMetaData();
        synchronized(metaData) {
            return metaData.getMappedViewType(typeId);
        }
    }

    /**
     * This method allows an AdapterView using this Adapter to provide information about which
     * views are currently being displayed. This allows for certain optimizations and preloading
     * which  wouldn't otherwise be possible.
     */
    @android.annotation.UnsupportedAppUsage
    public void setVisibleRangeHint(int lowerBound, int upperBound) {
        mVisibleWindowLowerBound = lowerBound;
        mVisibleWindowUpperBound = upperBound;
    }

    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        // "Request" an index so that we can queue it for loading, initiate subsequent
        // preloading, etc.
        synchronized(mCache) {
            android.widget.RemoteViews rv = mCache.getRemoteViewsAt(position);
            boolean isInCache = rv != null;
            boolean hasNewItems = false;
            if ((convertView != null) && (convertView instanceof android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout)) {
                mRequestedViews.removeView(((android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout) (convertView)));
            }
            if (!isInCache) {
                // Requesting bind service will trigger a super.notifyDataSetChanged(), which will
                // in turn trigger another request to getView()
                requestBindService();
            } else {
                // Queue up other indices to be preloaded based on this position
                hasNewItems = mCache.queuePositionsToBePreloadedFromRequestedPosition(position);
            }
            final android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout layout;
            if (convertView instanceof android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout) {
                layout = ((android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout) (convertView));
            } else {
                layout = new android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout(parent.getContext(), mCache);
                layout.setExecutor(mAsyncViewLoadExecutor);
                layout.setOnLightBackground(mOnLightBackground);
            }
            if (isInCache) {
                // Apply the view synchronously if possible, to avoid flickering
                layout.onRemoteViewsLoaded(rv, mRemoteViewsOnClickHandler, false);
                if (hasNewItems) {
                    mServiceHandler.sendEmptyMessage(android.widget.RemoteViewsAdapter.MSG_LOAD_NEXT_ITEM);
                }
            } else {
                // If the views is not loaded, apply the loading view. If the loading view doesn't
                // exist, the layout will create a default view based on the firstView height.
                layout.onRemoteViewsLoaded(mCache.getMetaData().getLoadingTemplate(mContext).remoteViews, mRemoteViewsOnClickHandler, false);
                mRequestedViews.add(position, layout);
                mCache.queueRequestedPositionToLoad(position);
                mServiceHandler.sendEmptyMessage(android.widget.RemoteViewsAdapter.MSG_LOAD_NEXT_ITEM);
            }
            return layout;
        }
    }

    public int getViewTypeCount() {
        final android.widget.RemoteViewsAdapter.RemoteViewsMetaData metaData = mCache.getMetaData();
        synchronized(metaData) {
            return metaData.viewTypeCount;
        }
    }

    public boolean hasStableIds() {
        final android.widget.RemoteViewsAdapter.RemoteViewsMetaData metaData = mCache.getMetaData();
        synchronized(metaData) {
            return metaData.hasStableIds;
        }
    }

    public boolean isEmpty() {
        return getCount() <= 0;
    }

    /**
     * Returns a sorted array of all integers between lower and upper.
     */
    private int[] getVisibleWindow(int count) {
        int lower = mVisibleWindowLowerBound;
        int upper = mVisibleWindowUpperBound;
        // In the case that the window is invalid or uninitialized, return an empty window.
        if ((((lower == 0) && (upper == 0)) || (lower < 0)) || (upper < 0)) {
            return new int[0];
        }
        int[] window;
        if (lower <= upper) {
            window = new int[(upper + 1) - lower];
            for (int i = lower, j = 0; i <= upper; i++ , j++) {
                window[j] = i;
            }
        } else {
            // If the upper bound is less than the lower bound it means that the visible window
            // wraps around.
            count = java.lang.Math.max(count, lower);
            window = new int[((count - lower) + upper) + 1];
            int j = 0;
            // Add the entries in sorted order
            for (int i = 0; i <= upper; i++ , j++) {
                window[j] = i;
            }
            for (int i = lower; i < count; i++ , j++) {
                window[j] = i;
            }
        }
        return window;
    }

    public void notifyDataSetChanged() {
        mServiceHandler.removeMessages(android.widget.RemoteViewsAdapter.MSG_UNBIND_SERVICE);
        mServiceHandler.sendEmptyMessage(android.widget.RemoteViewsAdapter.MSG_NOTIFY_DATA_SET_CHANGED);
    }

    void superNotifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @java.lang.Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_COMMIT_METADATA :
                {
                    mCache.commitTemporaryMetaData();
                    return true;
                }
            case android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_SUPER_NOTIFY_DATA_SET_CHANGED :
                {
                    superNotifyDataSetChanged();
                    return true;
                }
            case android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_REMOTE_ADAPTER_CONNECTED :
                {
                    if (mCallback != null) {
                        mCallback.onRemoteAdapterConnected();
                    }
                    return true;
                }
            case android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_REMOTE_ADAPTER_DISCONNECTED :
                {
                    if (mCallback != null) {
                        mCallback.onRemoteAdapterDisconnected();
                    }
                    return true;
                }
            case android.widget.RemoteViewsAdapter.MSG_MAIN_HANDLER_REMOTE_VIEWS_LOADED :
                {
                    mRequestedViews.notifyOnRemoteViewsLoaded(msg.arg1, ((android.widget.RemoteViews) (msg.obj)));
                    return true;
                }
        }
        return false;
    }

    private void requestBindService() {
        mServiceHandler.removeMessages(android.widget.RemoteViewsAdapter.MSG_UNBIND_SERVICE);
        android.os.Message.obtain(mServiceHandler, android.widget.RemoteViewsAdapter.MSG_REQUEST_BIND, mAppWidgetId, 0, mIntent).sendToTarget();
    }

    private static class HandlerThreadExecutor implements java.util.concurrent.Executor {
        private final android.os.HandlerThread mThread;

        HandlerThreadExecutor(android.os.HandlerThread thread) {
            mThread = thread;
        }

        @java.lang.Override
        public void execute(java.lang.Runnable runnable) {
            if (java.lang.Thread.currentThread().getId() == mThread.getId()) {
                runnable.run();
            } else {
                new android.os.Handler(mThread.getLooper()).post(runnable);
            }
        }
    }

    private static class LoadingViewTemplate {
        public final android.widget.RemoteViews remoteViews;

        public int defaultHeight;

        LoadingViewTemplate(android.widget.RemoteViews views, android.content.Context context) {
            remoteViews = views;
            float density = context.getResources().getDisplayMetrics().density;
            defaultHeight = java.lang.Math.round(android.widget.RemoteViewsAdapter.DEFAULT_LOADING_VIEW_HEIGHT * density);
        }

        public void loadFirstViewHeight(android.widget.RemoteViews firstView, android.content.Context context, java.util.concurrent.Executor executor) {
            // Inflate the first view on the worker thread
            firstView.applyAsync(context, new android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout(context, null), executor, new android.widget.RemoteViews.OnViewAppliedListener() {
                @java.lang.Override
                public void onViewApplied(android.view.View v) {
                    try {
                        v.measure(android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED), android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED));
                        defaultHeight = v.getMeasuredHeight();
                    } catch (java.lang.Exception e) {
                        onError(e);
                    }
                }

                @java.lang.Override
                public void onError(java.lang.Exception e) {
                    // Do nothing. The default height will stay the same.
                    android.util.Log.w(android.widget.RemoteViewsAdapter.TAG, "Error inflating first RemoteViews", e);
                }
            });
        }
    }
}

