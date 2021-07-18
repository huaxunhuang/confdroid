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
 * The service to be connected to for a remote adapter to request RemoteViews.  Users should
 * extend the RemoteViewsService to provide the appropriate RemoteViewsFactory's used to
 * populate the remote collection view (ListView, GridView, etc).
 */
public abstract class RemoteViewsService extends android.app.Service {
    private static final java.lang.String LOG_TAG = "RemoteViewsService";

    // Used for reference counting of RemoteViewsFactories
    // Because we are now unbinding when we are not using the Service (to allow them to be
    // reclaimed), the references to the factories that are created need to be stored and used when
    // the service is restarted (in response to user input for example).  When the process is
    // destroyed, so is this static cache of RemoteViewsFactories.
    private static final java.util.HashMap<android.content.Intent.FilterComparison, android.widget.RemoteViewsService.RemoteViewsFactory> sRemoteViewFactories = new java.util.HashMap<android.content.Intent.FilterComparison, android.widget.RemoteViewsService.RemoteViewsFactory>();

    private static final java.lang.Object sLock = new java.lang.Object();

    /**
     * An interface for an adapter between a remote collection view (ListView, GridView, etc) and
     * the underlying data for that view.  The implementor is responsible for making a RemoteView
     * for each item in the data set. This interface is a thin wrapper around {@link Adapter}.
     *
     * @see android.widget.Adapter
     * @see android.appwidget.AppWidgetManager
     */
    public interface RemoteViewsFactory {
        /**
         * Called when your factory is first constructed. The same factory may be shared across
         * multiple RemoteViewAdapters depending on the intent passed.
         */
        public void onCreate();

        /**
         * Called when notifyDataSetChanged() is triggered on the remote adapter. This allows a
         * RemoteViewsFactory to respond to data changes by updating any internal references.
         *
         * Note: expensive tasks can be safely performed synchronously within this method. In the
         * interim, the old data will be displayed within the widget.
         *
         * @see android.appwidget.AppWidgetManager#notifyAppWidgetViewDataChanged(int[], int)
         */
        public void onDataSetChanged();

        /**
         * Called when the last RemoteViewsAdapter that is associated with this factory is
         * unbound.
         */
        public void onDestroy();

        /**
         * See {@link Adapter#getCount()}
         *
         * @return Count of items.
         */
        public int getCount();

        /**
         * See {@link Adapter#getView(int, android.view.View, android.view.ViewGroup)}.
         *
         * Note: expensive tasks can be safely performed synchronously within this method, and a
         * loading view will be displayed in the interim. See {@link #getLoadingView()}.
         *
         * @param position
         * 		The position of the item within the Factory's data set of the item whose
         * 		view we want.
         * @return A RemoteViews object corresponding to the data at the specified position.
         */
        public android.widget.RemoteViews getViewAt(int position);

        /**
         * This allows for the use of a custom loading view which appears between the time that
         * {@link #getViewAt(int)} is called and returns. If null is returned, a default loading
         * view will be used.
         *
         * @return The RemoteViews representing the desired loading view.
         */
        public android.widget.RemoteViews getLoadingView();

        /**
         * See {@link Adapter#getViewTypeCount()}.
         *
         * @return The number of types of Views that will be returned by this factory.
         */
        public int getViewTypeCount();

        /**
         * See {@link Adapter#getItemId(int)}.
         *
         * @param position
         * 		The position of the item within the data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        public long getItemId(int position);

        /**
         * See {@link Adapter#hasStableIds()}.
         *
         * @return True if the same id always refers to the same object.
         */
        public boolean hasStableIds();
    }

    /**
     * A private proxy class for the private IRemoteViewsFactory interface through the
     * public RemoteViewsFactory interface.
     */
    private static class RemoteViewsFactoryAdapter extends com.android.internal.widget.IRemoteViewsFactory.Stub {
        public RemoteViewsFactoryAdapter(android.widget.RemoteViewsService.RemoteViewsFactory factory, boolean isCreated) {
            mFactory = factory;
            mIsCreated = isCreated;
        }

        public synchronized boolean isCreated() {
            return mIsCreated;
        }

        public synchronized void onDataSetChanged() {
            try {
                mFactory.onDataSetChanged();
            } catch (java.lang.Exception ex) {
                java.lang.Thread t = java.lang.Thread.currentThread();
                java.lang.Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
        }

        public synchronized void onDataSetChangedAsync() {
            onDataSetChanged();
        }

        public synchronized int getCount() {
            int count = 0;
            try {
                count = mFactory.getCount();
            } catch (java.lang.Exception ex) {
                java.lang.Thread t = java.lang.Thread.currentThread();
                java.lang.Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return count;
        }

        public synchronized android.widget.RemoteViews getViewAt(int position) {
            android.widget.RemoteViews rv = null;
            try {
                rv = mFactory.getViewAt(position);
                if (rv != null) {
                    rv.addFlags(android.widget.RemoteViews.FLAG_WIDGET_IS_COLLECTION_CHILD);
                }
            } catch (java.lang.Exception ex) {
                java.lang.Thread t = java.lang.Thread.currentThread();
                java.lang.Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return rv;
        }

        public synchronized android.widget.RemoteViews getLoadingView() {
            android.widget.RemoteViews rv = null;
            try {
                rv = mFactory.getLoadingView();
            } catch (java.lang.Exception ex) {
                java.lang.Thread t = java.lang.Thread.currentThread();
                java.lang.Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return rv;
        }

        public synchronized int getViewTypeCount() {
            int count = 0;
            try {
                count = mFactory.getViewTypeCount();
            } catch (java.lang.Exception ex) {
                java.lang.Thread t = java.lang.Thread.currentThread();
                java.lang.Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return count;
        }

        public synchronized long getItemId(int position) {
            long id = 0;
            try {
                id = mFactory.getItemId(position);
            } catch (java.lang.Exception ex) {
                java.lang.Thread t = java.lang.Thread.currentThread();
                java.lang.Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return id;
        }

        public synchronized boolean hasStableIds() {
            boolean hasStableIds = false;
            try {
                hasStableIds = mFactory.hasStableIds();
            } catch (java.lang.Exception ex) {
                java.lang.Thread t = java.lang.Thread.currentThread();
                java.lang.Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return hasStableIds;
        }

        public void onDestroy(android.content.Intent intent) {
            synchronized(android.widget.RemoteViewsService.sLock) {
                android.content.Intent.FilterComparison fc = new android.content.Intent.FilterComparison(intent);
                if (android.widget.RemoteViewsService.sRemoteViewFactories.containsKey(fc)) {
                    android.widget.RemoteViewsService.RemoteViewsFactory factory = android.widget.RemoteViewsService.sRemoteViewFactories.get(fc);
                    try {
                        factory.onDestroy();
                    } catch (java.lang.Exception ex) {
                        java.lang.Thread t = java.lang.Thread.currentThread();
                        java.lang.Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
                    }
                    android.widget.RemoteViewsService.sRemoteViewFactories.remove(fc);
                }
            }
        }

        private android.widget.RemoteViewsService.RemoteViewsFactory mFactory;

        private boolean mIsCreated;
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        synchronized(android.widget.RemoteViewsService.sLock) {
            android.content.Intent.FilterComparison fc = new android.content.Intent.FilterComparison(intent);
            android.widget.RemoteViewsService.RemoteViewsFactory factory = null;
            boolean isCreated = false;
            if (!android.widget.RemoteViewsService.sRemoteViewFactories.containsKey(fc)) {
                factory = onGetViewFactory(intent);
                android.widget.RemoteViewsService.sRemoteViewFactories.put(fc, factory);
                factory.onCreate();
                isCreated = false;
            } else {
                factory = android.widget.RemoteViewsService.sRemoteViewFactories.get(fc);
                isCreated = true;
            }
            return new android.widget.RemoteViewsService.RemoteViewsFactoryAdapter(factory, isCreated);
        }
    }

    /**
     * To be implemented by the derived service to generate appropriate factories for
     * the data.
     */
    public abstract android.widget.RemoteViewsService.RemoteViewsFactory onGetViewFactory(android.content.Intent intent);
}

