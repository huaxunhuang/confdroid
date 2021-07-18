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
package android.appwidget;


/**
 * AppWidgetHost provides the interaction with the AppWidget service for apps,
 * like the home screen, that want to embed AppWidgets in their UI.
 */
public class AppWidgetHost {
    static final int HANDLE_UPDATE = 1;

    static final int HANDLE_PROVIDER_CHANGED = 2;

    static final int HANDLE_PROVIDERS_CHANGED = 3;

    static final int HANDLE_VIEW_DATA_CHANGED = 4;

    static final java.lang.Object sServiceLock = new java.lang.Object();

    static com.android.internal.appwidget.IAppWidgetService sService;

    private android.util.DisplayMetrics mDisplayMetrics;

    private java.lang.String mContextOpPackageName;

    private final android.os.Handler mHandler;

    private final int mHostId;

    private final android.appwidget.AppWidgetHost.Callbacks mCallbacks;

    private final android.util.SparseArray<android.appwidget.AppWidgetHostView> mViews = new android.util.SparseArray<>();

    private android.widget.RemoteViews.OnClickHandler mOnClickHandler;

    static class Callbacks extends com.android.internal.appwidget.IAppWidgetHost.Stub {
        private final java.lang.ref.WeakReference<android.os.Handler> mWeakHandler;

        public Callbacks(android.os.Handler handler) {
            mWeakHandler = new java.lang.ref.WeakReference<>(handler);
        }

        public void updateAppWidget(int appWidgetId, android.widget.RemoteViews views) {
            if (android.appwidget.AppWidgetHost.Callbacks.isLocalBinder() && (views != null)) {
                views = views.clone();
            }
            android.os.Handler handler = mWeakHandler.get();
            if (handler == null) {
                return;
            }
            android.os.Message msg = handler.obtainMessage(android.appwidget.AppWidgetHost.HANDLE_UPDATE, appWidgetId, 0, views);
            msg.sendToTarget();
        }

        public void providerChanged(int appWidgetId, android.appwidget.AppWidgetProviderInfo info) {
            if (android.appwidget.AppWidgetHost.Callbacks.isLocalBinder() && (info != null)) {
                info = info.clone();
            }
            android.os.Handler handler = mWeakHandler.get();
            if (handler == null) {
                return;
            }
            android.os.Message msg = handler.obtainMessage(android.appwidget.AppWidgetHost.HANDLE_PROVIDER_CHANGED, appWidgetId, 0, info);
            msg.sendToTarget();
        }

        public void providersChanged() {
            android.os.Handler handler = mWeakHandler.get();
            if (handler == null) {
                return;
            }
            handler.obtainMessage(android.appwidget.AppWidgetHost.HANDLE_PROVIDERS_CHANGED).sendToTarget();
        }

        public void viewDataChanged(int appWidgetId, int viewId) {
            android.os.Handler handler = mWeakHandler.get();
            if (handler == null) {
                return;
            }
            android.os.Message msg = handler.obtainMessage(android.appwidget.AppWidgetHost.HANDLE_VIEW_DATA_CHANGED, appWidgetId, viewId);
            msg.sendToTarget();
        }

        private static boolean isLocalBinder() {
            return android.os.Process.myPid() == android.os.Binder.getCallingPid();
        }
    }

    class UpdateHandler extends android.os.Handler {
        public UpdateHandler(android.os.Looper looper) {
            super(looper);
        }

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.appwidget.AppWidgetHost.HANDLE_UPDATE :
                    {
                        updateAppWidgetView(msg.arg1, ((android.widget.RemoteViews) (msg.obj)));
                        break;
                    }
                case android.appwidget.AppWidgetHost.HANDLE_PROVIDER_CHANGED :
                    {
                        onProviderChanged(msg.arg1, ((android.appwidget.AppWidgetProviderInfo) (msg.obj)));
                        break;
                    }
                case android.appwidget.AppWidgetHost.HANDLE_PROVIDERS_CHANGED :
                    {
                        onProvidersChanged();
                        break;
                    }
                case android.appwidget.AppWidgetHost.HANDLE_VIEW_DATA_CHANGED :
                    {
                        viewDataChanged(msg.arg1, msg.arg2);
                        break;
                    }
            }
        }
    }

    public AppWidgetHost(android.content.Context context, int hostId) {
        this(context, hostId, null, context.getMainLooper());
    }

    /**
     *
     *
     * @unknown 
     */
    public AppWidgetHost(android.content.Context context, int hostId, android.widget.RemoteViews.OnClickHandler handler, android.os.Looper looper) {
        mContextOpPackageName = context.getOpPackageName();
        mHostId = hostId;
        mOnClickHandler = handler;
        mHandler = new android.appwidget.AppWidgetHost.UpdateHandler(looper);
        mCallbacks = new android.appwidget.AppWidgetHost.Callbacks(mHandler);
        mDisplayMetrics = context.getResources().getDisplayMetrics();
        android.appwidget.AppWidgetHost.bindService();
    }

    private static void bindService() {
        synchronized(android.appwidget.AppWidgetHost.sServiceLock) {
            if (android.appwidget.AppWidgetHost.sService == null) {
                android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.APPWIDGET_SERVICE);
                android.appwidget.AppWidgetHost.sService = IAppWidgetService.Stub.asInterface(b);
            }
        }
    }

    /**
     * Start receiving onAppWidgetChanged calls for your AppWidgets.  Call this when your activity
     * becomes visible, i.e. from onStart() in your Activity.
     */
    public void startListening() {
        final int[] idsToUpdate;
        synchronized(mViews) {
            int N = mViews.size();
            idsToUpdate = new int[N];
            for (int i = 0; i < N; i++) {
                idsToUpdate[i] = mViews.keyAt(i);
            }
        }
        java.util.List<android.appwidget.PendingHostUpdate> updates;
        try {
            updates = android.appwidget.AppWidgetHost.sService.startListening(mCallbacks, mContextOpPackageName, mHostId, idsToUpdate).getList();
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("system server dead?", e);
        }
        int N = updates.size();
        for (int i = 0; i < N; i++) {
            android.appwidget.PendingHostUpdate update = updates.get(i);
            switch (update.type) {
                case android.appwidget.PendingHostUpdate.TYPE_VIEWS_UPDATE :
                    updateAppWidgetView(update.appWidgetId, update.views);
                    break;
                case android.appwidget.PendingHostUpdate.TYPE_PROVIDER_CHANGED :
                    onProviderChanged(update.appWidgetId, update.widgetInfo);
                    break;
                case android.appwidget.PendingHostUpdate.TYPE_VIEW_DATA_CHANGED :
                    viewDataChanged(update.appWidgetId, update.viewId);
            }
        }
    }

    /**
     * Stop receiving onAppWidgetChanged calls for your AppWidgets.  Call this when your activity is
     * no longer visible, i.e. from onStop() in your Activity.
     */
    public void stopListening() {
        try {
            android.appwidget.AppWidgetHost.sService.stopListening(mContextOpPackageName, mHostId);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("system server dead?", e);
        }
    }

    /**
     * Get a appWidgetId for a host in the calling process.
     *
     * @return a appWidgetId
     */
    public int allocateAppWidgetId() {
        try {
            return android.appwidget.AppWidgetHost.sService.allocateAppWidgetId(mContextOpPackageName, mHostId);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("system server dead?", e);
        }
    }

    /**
     * Starts an app widget provider configure activity for result on behalf of the caller.
     * Use this method if the provider is in another profile as you are not allowed to start
     * an activity in another profile. You can optionally provide a request code that is
     * returned in {@link Activity#onActivityResult(int, int, android.content.Intent)} and
     * an options bundle to be passed to the started activity.
     * <p>
     * Note that the provided app widget has to be bound for this method to work.
     * </p>
     *
     * @param activity
     * 		The activity from which to start the configure one.
     * @param appWidgetId
     * 		The bound app widget whose provider's config activity to start.
     * @param requestCode
     * 		Optional request code retuned with the result.
     * @param intentFlags
     * 		Optional intent flags.
     * @throws android.content.ActivityNotFoundException
     * 		If the activity is not found.
     * @see AppWidgetProviderInfo#getProfile()
     */
    public final void startAppWidgetConfigureActivityForResult(@android.annotation.NonNull
    android.app.Activity activity, int appWidgetId, int intentFlags, int requestCode, @android.annotation.Nullable
    android.os.Bundle options) {
        try {
            android.content.IntentSender intentSender = android.appwidget.AppWidgetHost.sService.createAppWidgetConfigIntentSender(mContextOpPackageName, appWidgetId, intentFlags);
            if (intentSender != null) {
                activity.startIntentSenderForResult(intentSender, requestCode, null, 0, 0, 0, options);
            } else {
                throw new android.content.ActivityNotFoundException();
            }
        } catch (android.content.IntentSender.SendIntentException e) {
            throw new android.content.ActivityNotFoundException();
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("system server dead?", e);
        }
    }

    /**
     * Gets a list of all the appWidgetIds that are bound to the current host
     *
     * @unknown 
     */
    public int[] getAppWidgetIds() {
        try {
            if (android.appwidget.AppWidgetHost.sService == null) {
                android.appwidget.AppWidgetHost.bindService();
            }
            return android.appwidget.AppWidgetHost.sService.getAppWidgetIdsForHost(mContextOpPackageName, mHostId);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("system server dead?", e);
        }
    }

    /**
     * Stop listening to changes for this AppWidget.
     */
    public void deleteAppWidgetId(int appWidgetId) {
        synchronized(mViews) {
            mViews.remove(appWidgetId);
            try {
                android.appwidget.AppWidgetHost.sService.deleteAppWidgetId(mContextOpPackageName, appWidgetId);
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException("system server dead?", e);
            }
        }
    }

    /**
     * Remove all records about this host from the AppWidget manager.
     * <ul>
     *   <li>Call this when initializing your database, as it might be because of a data wipe.</li>
     *   <li>Call this to have the AppWidget manager release all resources associated with your
     *   host.  Any future calls about this host will cause the records to be re-allocated.</li>
     * </ul>
     */
    public void deleteHost() {
        try {
            android.appwidget.AppWidgetHost.sService.deleteHost(mContextOpPackageName, mHostId);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("system server dead?", e);
        }
    }

    /**
     * Remove all records about all hosts for your package.
     * <ul>
     *   <li>Call this when initializing your database, as it might be because of a data wipe.</li>
     *   <li>Call this to have the AppWidget manager release all resources associated with your
     *   host.  Any future calls about this host will cause the records to be re-allocated.</li>
     * </ul>
     */
    public static void deleteAllHosts() {
        try {
            android.appwidget.AppWidgetHost.sService.deleteAllHosts();
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("system server dead?", e);
        }
    }

    /**
     * Create the AppWidgetHostView for the given widget.
     * The AppWidgetHost retains a pointer to the newly-created View.
     */
    public final android.appwidget.AppWidgetHostView createView(android.content.Context context, int appWidgetId, android.appwidget.AppWidgetProviderInfo appWidget) {
        android.appwidget.AppWidgetHostView view = onCreateView(context, appWidgetId, appWidget);
        view.setOnClickHandler(mOnClickHandler);
        view.setAppWidget(appWidgetId, appWidget);
        synchronized(mViews) {
            mViews.put(appWidgetId, view);
        }
        android.widget.RemoteViews views;
        try {
            views = android.appwidget.AppWidgetHost.sService.getAppWidgetViews(mContextOpPackageName, appWidgetId);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("system server dead?", e);
        }
        view.updateAppWidget(views);
        return view;
    }

    /**
     * Called to create the AppWidgetHostView.  Override to return a custom subclass if you
     * need it.  {@more }
     */
    protected android.appwidget.AppWidgetHostView onCreateView(android.content.Context context, int appWidgetId, android.appwidget.AppWidgetProviderInfo appWidget) {
        return new android.appwidget.AppWidgetHostView(context, mOnClickHandler);
    }

    /**
     * Called when the AppWidget provider for a AppWidget has been upgraded to a new apk.
     */
    protected void onProviderChanged(int appWidgetId, android.appwidget.AppWidgetProviderInfo appWidget) {
        android.appwidget.AppWidgetHostView v;
        // Convert complex to dp -- we are getting the AppWidgetProviderInfo from the
        // AppWidgetService, which doesn't have our context, hence we need to do the
        // conversion here.
        appWidget.minWidth = android.util.TypedValue.complexToDimensionPixelSize(appWidget.minWidth, mDisplayMetrics);
        appWidget.minHeight = android.util.TypedValue.complexToDimensionPixelSize(appWidget.minHeight, mDisplayMetrics);
        appWidget.minResizeWidth = android.util.TypedValue.complexToDimensionPixelSize(appWidget.minResizeWidth, mDisplayMetrics);
        appWidget.minResizeHeight = android.util.TypedValue.complexToDimensionPixelSize(appWidget.minResizeHeight, mDisplayMetrics);
        synchronized(mViews) {
            v = mViews.get(appWidgetId);
        }
        if (v != null) {
            v.resetAppWidget(appWidget);
        }
    }

    /**
     * Called when the set of available widgets changes (ie. widget containing packages
     * are added, updated or removed, or widget components are enabled or disabled.)
     */
    protected void onProvidersChanged() {
        // Does nothing
    }

    void updateAppWidgetView(int appWidgetId, android.widget.RemoteViews views) {
        android.appwidget.AppWidgetHostView v;
        synchronized(mViews) {
            v = mViews.get(appWidgetId);
        }
        if (v != null) {
            v.updateAppWidget(views);
        }
    }

    void viewDataChanged(int appWidgetId, int viewId) {
        android.appwidget.AppWidgetHostView v;
        synchronized(mViews) {
            v = mViews.get(appWidgetId);
        }
        if (v != null) {
            v.viewDataChanged(viewId);
        }
    }

    /**
     * Clear the list of Views that have been created by this AppWidgetHost.
     */
    protected void clearViews() {
        synchronized(mViews) {
            mViews.clear();
        }
    }
}

