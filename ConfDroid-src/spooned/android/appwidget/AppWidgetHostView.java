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
package android.appwidget;


/**
 * Provides the glue to show AppWidget views. This class offers automatic animation
 * between updates, and will try recycling old views for each incoming
 * {@link RemoteViews}.
 */
public class AppWidgetHostView extends android.widget.FrameLayout {
    static final java.lang.String TAG = "AppWidgetHostView";

    static final boolean LOGD = false;

    static final boolean CROSSFADE = false;

    static final int VIEW_MODE_NOINIT = 0;

    static final int VIEW_MODE_CONTENT = 1;

    static final int VIEW_MODE_ERROR = 2;

    static final int VIEW_MODE_DEFAULT = 3;

    static final int FADE_DURATION = 1000;

    // When we're inflating the initialLayout for a AppWidget, we only allow
    // views that are allowed in RemoteViews.
    static final android.view.LayoutInflater.Filter sInflaterFilter = new android.view.LayoutInflater.Filter() {
        public boolean onLoadClass(java.lang.Class clazz) {
            return clazz.isAnnotationPresent(android.widget.RemoteViews.RemoteView.class);
        }
    };

    android.content.Context mContext;

    android.content.Context mRemoteContext;

    int mAppWidgetId;

    android.appwidget.AppWidgetProviderInfo mInfo;

    android.view.View mView;

    int mViewMode = android.appwidget.AppWidgetHostView.VIEW_MODE_NOINIT;

    int mLayoutId = -1;

    long mFadeStartTime = -1;

    android.graphics.Bitmap mOld;

    android.graphics.Paint mOldPaint = new android.graphics.Paint();

    private android.widget.RemoteViews.OnClickHandler mOnClickHandler;

    private java.util.concurrent.Executor mAsyncExecutor;

    private android.os.CancellationSignal mLastExecutionSignal;

    /**
     * Create a host view.  Uses default fade animations.
     */
    public AppWidgetHostView(android.content.Context context) {
        this(context, android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     *
     *
     * @unknown 
     */
    public AppWidgetHostView(android.content.Context context, android.widget.RemoteViews.OnClickHandler handler) {
        this(context, android.R.anim.fade_in, android.R.anim.fade_out);
        mOnClickHandler = handler;
    }

    /**
     * Create a host view. Uses specified animations when pushing
     * {@link #updateAppWidget(RemoteViews)}.
     *
     * @param animationIn
     * 		Resource ID of in animation to use
     * @param animationOut
     * 		Resource ID of out animation to use
     */
    @java.lang.SuppressWarnings({ "UnusedDeclaration" })
    public AppWidgetHostView(android.content.Context context, int animationIn, int animationOut) {
        super(context);
        mContext = context;
        // We want to segregate the view ids within AppWidgets to prevent
        // problems when those ids collide with view ids in the AppWidgetHost.
        setIsRootNamespace(true);
    }

    /**
     * Pass the given handler to RemoteViews when updating this widget. Unless this
     * is done immediatly after construction, a call to {@link #updateAppWidget(RemoteViews)}
     * should be made.
     *
     * @param handler
     * 		
     * @unknown 
     */
    public void setOnClickHandler(android.widget.RemoteViews.OnClickHandler handler) {
        mOnClickHandler = handler;
    }

    /**
     * Set the AppWidget that will be displayed by this view. This method also adds default padding
     * to widgets, as described in {@link #getDefaultPaddingForWidget(Context, ComponentName, Rect)}
     * and can be overridden in order to add custom padding.
     */
    public void setAppWidget(int appWidgetId, android.appwidget.AppWidgetProviderInfo info) {
        mAppWidgetId = appWidgetId;
        mInfo = info;
        // Sometimes the AppWidgetManager returns a null AppWidgetProviderInfo object for
        // a widget, eg. for some widgets in safe mode.
        if (info != null) {
            // We add padding to the AppWidgetHostView if necessary
            android.graphics.Rect padding = android.appwidget.AppWidgetHostView.getDefaultPaddingForWidget(mContext, info.provider, null);
            setPadding(padding.left, padding.top, padding.right, padding.bottom);
            updateContentDescription(info);
        }
    }

    /**
     * As of ICE_CREAM_SANDWICH we are automatically adding padding to widgets targeting
     * ICE_CREAM_SANDWICH and higher. The new widget design guidelines strongly recommend
     * that widget developers do not add extra padding to their widgets. This will help
     * achieve consistency among widgets.
     *
     * Note: this method is only needed by developers of AppWidgetHosts. The method is provided in
     * order for the AppWidgetHost to account for the automatic padding when computing the number
     * of cells to allocate to a particular widget.
     *
     * @param context
     * 		the current context
     * @param component
     * 		the component name of the widget
     * @param padding
     * 		Rect in which to place the output, if null, a new Rect will be allocated and
     * 		returned
     * @return default padding for this widget, in pixels
     */
    public static android.graphics.Rect getDefaultPaddingForWidget(android.content.Context context, android.content.ComponentName component, android.graphics.Rect padding) {
        android.content.pm.PackageManager packageManager = context.getPackageManager();
        android.content.pm.ApplicationInfo appInfo;
        if (padding == null) {
            padding = new android.graphics.Rect(0, 0, 0, 0);
        } else {
            padding.set(0, 0, 0, 0);
        }
        try {
            appInfo = packageManager.getApplicationInfo(component.getPackageName(), 0);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // if we can't find the package, return 0 padding
            return padding;
        }
        if (appInfo.targetSdkVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            android.content.res.Resources r = context.getResources();
            padding.left = r.getDimensionPixelSize(com.android.internal.R.dimen.default_app_widget_padding_left);
            padding.right = r.getDimensionPixelSize(com.android.internal.R.dimen.default_app_widget_padding_right);
            padding.top = r.getDimensionPixelSize(com.android.internal.R.dimen.default_app_widget_padding_top);
            padding.bottom = r.getDimensionPixelSize(com.android.internal.R.dimen.default_app_widget_padding_bottom);
        }
        return padding;
    }

    public int getAppWidgetId() {
        return mAppWidgetId;
    }

    public android.appwidget.AppWidgetProviderInfo getAppWidgetInfo() {
        return mInfo;
    }

    @java.lang.Override
    protected void dispatchSaveInstanceState(android.util.SparseArray<android.os.Parcelable> container) {
        final android.appwidget.AppWidgetHostView.ParcelableSparseArray jail = new android.appwidget.AppWidgetHostView.ParcelableSparseArray();
        super.dispatchSaveInstanceState(jail);
        container.put(generateId(), jail);
    }

    private int generateId() {
        final int id = getId();
        return id == android.view.View.NO_ID ? mAppWidgetId : id;
    }

    @java.lang.Override
    protected void dispatchRestoreInstanceState(android.util.SparseArray<android.os.Parcelable> container) {
        final android.os.Parcelable parcelable = container.get(generateId());
        android.appwidget.AppWidgetHostView.ParcelableSparseArray jail = null;
        if ((parcelable != null) && (parcelable instanceof android.appwidget.AppWidgetHostView.ParcelableSparseArray)) {
            jail = ((android.appwidget.AppWidgetHostView.ParcelableSparseArray) (parcelable));
        }
        if (jail == null)
            jail = new android.appwidget.AppWidgetHostView.ParcelableSparseArray();

        try {
            super.dispatchRestoreInstanceState(jail);
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.appwidget.AppWidgetHostView.TAG, (("failed to restoreInstanceState for widget id: " + mAppWidgetId) + ", ") + (mInfo == null ? "null" : mInfo.provider), e);
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        try {
            super.onLayout(changed, left, top, right, bottom);
        } catch (final java.lang.RuntimeException e) {
            android.util.Log.e(android.appwidget.AppWidgetHostView.TAG, "Remote provider threw runtime exception, using error view instead.", e);
            removeViewInLayout(mView);
            android.view.View child = getErrorView();
            prepareView(child);
            addViewInLayout(child, 0, child.getLayoutParams());
            measureChild(child, android.view.View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), android.view.View.MeasureSpec.EXACTLY));
            child.layout(0, 0, (child.getMeasuredWidth() + mPaddingLeft) + mPaddingRight, (child.getMeasuredHeight() + mPaddingTop) + mPaddingBottom);
            mView = child;
            mViewMode = android.appwidget.AppWidgetHostView.VIEW_MODE_ERROR;
        }
    }

    /**
     * Provide guidance about the size of this widget to the AppWidgetManager. The widths and
     * heights should correspond to the full area the AppWidgetHostView is given. Padding added by
     * the framework will be accounted for automatically. This information gets embedded into the
     * AppWidget options and causes a callback to the AppWidgetProvider.
     *
     * @see AppWidgetProvider#onAppWidgetOptionsChanged(Context, AppWidgetManager, int, Bundle)
     * @param newOptions
     * 		The bundle of options, in addition to the size information,
     * 		can be null.
     * @param minWidth
     * 		The minimum width in dips that the widget will be displayed at.
     * @param minHeight
     * 		The maximum height in dips that the widget will be displayed at.
     * @param maxWidth
     * 		The maximum width in dips that the widget will be displayed at.
     * @param maxHeight
     * 		The maximum height in dips that the widget will be displayed at.
     */
    public void updateAppWidgetSize(android.os.Bundle newOptions, int minWidth, int minHeight, int maxWidth, int maxHeight) {
        updateAppWidgetSize(newOptions, minWidth, minHeight, maxWidth, maxHeight, false);
    }

    /**
     *
     *
     * @unknown 
     */
    public void updateAppWidgetSize(android.os.Bundle newOptions, int minWidth, int minHeight, int maxWidth, int maxHeight, boolean ignorePadding) {
        if (newOptions == null) {
            newOptions = new android.os.Bundle();
        }
        android.graphics.Rect padding = new android.graphics.Rect();
        if (mInfo != null) {
            padding = android.appwidget.AppWidgetHostView.getDefaultPaddingForWidget(mContext, mInfo.provider, padding);
        }
        float density = getResources().getDisplayMetrics().density;
        int xPaddingDips = ((int) ((padding.left + padding.right) / density));
        int yPaddingDips = ((int) ((padding.top + padding.bottom) / density));
        int newMinWidth = minWidth - (ignorePadding ? 0 : xPaddingDips);
        int newMinHeight = minHeight - (ignorePadding ? 0 : yPaddingDips);
        int newMaxWidth = maxWidth - (ignorePadding ? 0 : xPaddingDips);
        int newMaxHeight = maxHeight - (ignorePadding ? 0 : yPaddingDips);
        android.appwidget.AppWidgetManager widgetManager = android.appwidget.AppWidgetManager.getInstance(mContext);
        // We get the old options to see if the sizes have changed
        android.os.Bundle oldOptions = widgetManager.getAppWidgetOptions(mAppWidgetId);
        boolean needsUpdate = false;
        if ((((newMinWidth != oldOptions.getInt(android.appwidget.AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) || (newMinHeight != oldOptions.getInt(android.appwidget.AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT))) || (newMaxWidth != oldOptions.getInt(android.appwidget.AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH))) || (newMaxHeight != oldOptions.getInt(android.appwidget.AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT))) {
            needsUpdate = true;
        }
        if (needsUpdate) {
            newOptions.putInt(android.appwidget.AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, newMinWidth);
            newOptions.putInt(android.appwidget.AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, newMinHeight);
            newOptions.putInt(android.appwidget.AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, newMaxWidth);
            newOptions.putInt(android.appwidget.AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, newMaxHeight);
            updateAppWidgetOptions(newOptions);
        }
    }

    /**
     * Specify some extra information for the widget provider. Causes a callback to the
     * AppWidgetProvider.
     *
     * @see AppWidgetProvider#onAppWidgetOptionsChanged(Context, AppWidgetManager, int, Bundle)
     * @param options
     * 		The bundle of options information.
     */
    public void updateAppWidgetOptions(android.os.Bundle options) {
        android.appwidget.AppWidgetManager.getInstance(mContext).updateAppWidgetOptions(mAppWidgetId, options);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public android.widget.FrameLayout.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        // We're being asked to inflate parameters, probably by a LayoutInflater
        // in a remote Context. To help resolve any remote references, we
        // inflate through our last mRemoteContext when it exists.
        final android.content.Context context = (mRemoteContext != null) ? mRemoteContext : mContext;
        return new android.widget.FrameLayout.LayoutParams(context, attrs);
    }

    /**
     * Sets an executor which can be used for asynchronously inflating and applying the remoteviews.
     *
     * @see {@link RemoteViews#applyAsync(Context, ViewGroup, RemoteViews.OnViewAppliedListener, Executor)}
     * @param executor
     * 		the executor to use or null.
     * @unknown 
     */
    public void setAsyncExecutor(java.util.concurrent.Executor executor) {
        if (mLastExecutionSignal != null) {
            mLastExecutionSignal.cancel();
            mLastExecutionSignal = null;
        }
        mAsyncExecutor = executor;
    }

    /**
     * Update the AppWidgetProviderInfo for this view, and reset it to the
     * initial layout.
     */
    void resetAppWidget(android.appwidget.AppWidgetProviderInfo info) {
        mInfo = info;
        mViewMode = android.appwidget.AppWidgetHostView.VIEW_MODE_NOINIT;
        updateAppWidget(null);
    }

    /**
     * Process a set of {@link RemoteViews} coming in as an update from the
     * AppWidget provider. Will animate into these new views as needed
     */
    public void updateAppWidget(android.widget.RemoteViews remoteViews) {
        applyRemoteViews(remoteViews);
    }

    /**
     *
     *
     * @unknown 
     */
    protected void applyRemoteViews(android.widget.RemoteViews remoteViews) {
        if (android.appwidget.AppWidgetHostView.LOGD)
            android.util.Log.d(android.appwidget.AppWidgetHostView.TAG, "updateAppWidget called mOld=" + mOld);

        boolean recycled = false;
        android.view.View content = null;
        java.lang.Exception exception = null;
        // Capture the old view into a bitmap so we can do the crossfade.
        if (android.appwidget.AppWidgetHostView.CROSSFADE) {
            if (mFadeStartTime < 0) {
                if (mView != null) {
                    final int width = mView.getWidth();
                    final int height = mView.getHeight();
                    try {
                        mOld = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
                    } catch (java.lang.OutOfMemoryError e) {
                        // we just won't do the fade
                        mOld = null;
                    }
                    if (mOld != null) {
                        // mView.drawIntoBitmap(mOld);
                    }
                }
            }
        }
        if (mLastExecutionSignal != null) {
            mLastExecutionSignal.cancel();
            mLastExecutionSignal = null;
        }
        if (remoteViews == null) {
            if (mViewMode == android.appwidget.AppWidgetHostView.VIEW_MODE_DEFAULT) {
                // We've already done this -- nothing to do.
                return;
            }
            content = getDefaultView();
            mLayoutId = -1;
            mViewMode = android.appwidget.AppWidgetHostView.VIEW_MODE_DEFAULT;
        } else {
            if (mAsyncExecutor != null) {
                inflateAsync(remoteViews);
                return;
            }
            // Prepare a local reference to the remote Context so we're ready to
            // inflate any requested LayoutParams.
            mRemoteContext = getRemoteContext();
            int layoutId = remoteViews.getLayoutId();
            // If our stale view has been prepared to match active, and the new
            // layout matches, try recycling it
            if ((content == null) && (layoutId == mLayoutId)) {
                try {
                    remoteViews.reapply(mContext, mView, mOnClickHandler);
                    content = mView;
                    recycled = true;
                    if (android.appwidget.AppWidgetHostView.LOGD)
                        android.util.Log.d(android.appwidget.AppWidgetHostView.TAG, "was able to recycle existing layout");

                } catch (java.lang.RuntimeException e) {
                    exception = e;
                }
            }
            // Try normal RemoteView inflation
            if (content == null) {
                try {
                    content = remoteViews.apply(mContext, this, mOnClickHandler);
                    if (android.appwidget.AppWidgetHostView.LOGD)
                        android.util.Log.d(android.appwidget.AppWidgetHostView.TAG, "had to inflate new layout");

                } catch (java.lang.RuntimeException e) {
                    exception = e;
                }
            }
            mLayoutId = layoutId;
            mViewMode = android.appwidget.AppWidgetHostView.VIEW_MODE_CONTENT;
        }
        applyContent(content, recycled, exception);
        updateContentDescription(mInfo);
    }

    private void applyContent(android.view.View content, boolean recycled, java.lang.Exception exception) {
        if (content == null) {
            if (mViewMode == android.appwidget.AppWidgetHostView.VIEW_MODE_ERROR) {
                // We've already done this -- nothing to do.
                return;
            }
            android.util.Log.w(android.appwidget.AppWidgetHostView.TAG, "updateAppWidget couldn't find any view, using error view", exception);
            content = getErrorView();
            mViewMode = android.appwidget.AppWidgetHostView.VIEW_MODE_ERROR;
        }
        if (!recycled) {
            prepareView(content);
            addView(content);
        }
        if (mView != content) {
            removeView(mView);
            mView = content;
        }
        if (android.appwidget.AppWidgetHostView.CROSSFADE) {
            if (mFadeStartTime < 0) {
                // if there is already an animation in progress, don't do anything --
                // the new view will pop in on top of the old one during the cross fade,
                // and that looks okay.
                mFadeStartTime = android.os.SystemClock.uptimeMillis();
                invalidate();
            }
        }
    }

    private void updateContentDescription(android.appwidget.AppWidgetProviderInfo info) {
        if (info != null) {
            android.content.pm.LauncherApps launcherApps = getContext().getSystemService(android.content.pm.LauncherApps.class);
            android.content.pm.ApplicationInfo appInfo = launcherApps.getApplicationInfo(info.provider.getPackageName(), 0, info.getProfile());
            if ((appInfo != null) && ((appInfo.flags & android.content.pm.ApplicationInfo.FLAG_SUSPENDED) != 0)) {
                setContentDescription(android.content.res.Resources.getSystem().getString(com.android.internal.R.string.suspended_widget_accessibility, info.label));
            } else {
                setContentDescription(info.label);
            }
        }
    }

    private void inflateAsync(android.widget.RemoteViews remoteViews) {
        // Prepare a local reference to the remote Context so we're ready to
        // inflate any requested LayoutParams.
        mRemoteContext = getRemoteContext();
        int layoutId = remoteViews.getLayoutId();
        // If our stale view has been prepared to match active, and the new
        // layout matches, try recycling it
        if ((layoutId == mLayoutId) && (mView != null)) {
            try {
                mLastExecutionSignal = remoteViews.reapplyAsync(mContext, mView, mAsyncExecutor, new android.appwidget.AppWidgetHostView.ViewApplyListener(remoteViews, layoutId, true), mOnClickHandler);
            } catch (java.lang.Exception e) {
                // Reapply failed. Try apply
            }
        }
        if (mLastExecutionSignal == null) {
            mLastExecutionSignal = remoteViews.applyAsync(mContext, this, mAsyncExecutor, new android.appwidget.AppWidgetHostView.ViewApplyListener(remoteViews, layoutId, false), mOnClickHandler);
        }
    }

    private class ViewApplyListener implements android.widget.RemoteViews.OnViewAppliedListener {
        private final android.widget.RemoteViews mViews;

        private final boolean mIsReapply;

        private final int mLayoutId;

        public ViewApplyListener(android.widget.RemoteViews views, int layoutId, boolean isReapply) {
            mViews = views;
            mLayoutId = layoutId;
            mIsReapply = isReapply;
        }

        @java.lang.Override
        public void onViewApplied(android.view.View v) {
            android.appwidget.AppWidgetHostView.this.mLayoutId = mLayoutId;
            mViewMode = android.appwidget.AppWidgetHostView.VIEW_MODE_CONTENT;
            applyContent(v, mIsReapply, null);
        }

        @java.lang.Override
        public void onError(java.lang.Exception e) {
            if (mIsReapply) {
                // Try a fresh replay
                mLastExecutionSignal = mViews.applyAsync(mContext, android.appwidget.AppWidgetHostView.this, mAsyncExecutor, new android.appwidget.AppWidgetHostView.ViewApplyListener(mViews, mLayoutId, false), mOnClickHandler);
            } else {
                applyContent(null, false, e);
            }
        }
    }

    /**
     * Process data-changed notifications for the specified view in the specified
     * set of {@link RemoteViews} views.
     */
    void viewDataChanged(int viewId) {
        android.view.View v = findViewById(viewId);
        if ((v != null) && (v instanceof android.widget.AdapterView<?>)) {
            android.widget.AdapterView<?> adapterView = ((android.widget.AdapterView<?>) (v));
            android.widget.Adapter adapter = adapterView.getAdapter();
            if (adapter instanceof android.widget.BaseAdapter) {
                android.widget.BaseAdapter baseAdapter = ((android.widget.BaseAdapter) (adapter));
                baseAdapter.notifyDataSetChanged();
            } else
                if ((adapter == null) && (adapterView instanceof android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback)) {
                    // If the adapter is null, it may mean that the RemoteViewsAapter has not yet
                    // connected to its associated service, and hence the adapter hasn't been set.
                    // In this case, we need to defer the notify call until it has been set.
                    ((android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback) (adapterView)).deferNotifyDataSetChanged();
                }

        }
    }

    /**
     * Build a {@link Context} cloned into another package name, usually for the
     * purposes of reading remote resources.
     *
     * @unknown 
     */
    protected android.content.Context getRemoteContext() {
        try {
            // Return if cloned successfully, otherwise default
            return mContext.createApplicationContext(mInfo.providerInfo.applicationInfo, android.content.Context.CONTEXT_RESTRICTED);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.e(android.appwidget.AppWidgetHostView.TAG, ("Package name " + mInfo.providerInfo.packageName) + " not found");
            return mContext;
        }
    }

    @java.lang.Override
    protected boolean drawChild(android.graphics.Canvas canvas, android.view.View child, long drawingTime) {
        if (android.appwidget.AppWidgetHostView.CROSSFADE) {
            int alpha;
            int l = child.getLeft();
            int t = child.getTop();
            if (mFadeStartTime > 0) {
                alpha = ((int) (((drawingTime - mFadeStartTime) * 255) / android.appwidget.AppWidgetHostView.FADE_DURATION));
                if (alpha > 255) {
                    alpha = 255;
                }
                android.util.Log.d(android.appwidget.AppWidgetHostView.TAG, (((((("drawChild alpha=" + alpha) + " l=") + l) + " t=") + t) + " w=") + child.getWidth());
                if ((alpha != 255) && (mOld != null)) {
                    mOldPaint.setAlpha(255 - alpha);
                    // canvas.drawBitmap(mOld, l, t, mOldPaint);
                }
            } else {
                alpha = 255;
            }
            int restoreTo = canvas.saveLayerAlpha(l, t, child.getWidth(), child.getHeight(), alpha, android.graphics.Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | android.graphics.Canvas.CLIP_TO_LAYER_SAVE_FLAG);
            boolean rv = super.drawChild(canvas, child, drawingTime);
            canvas.restoreToCount(restoreTo);
            if (alpha < 255) {
                invalidate();
            } else {
                mFadeStartTime = -1;
                if (mOld != null) {
                    mOld.recycle();
                    mOld = null;
                }
            }
            return rv;
        } else {
            return super.drawChild(canvas, child, drawingTime);
        }
    }

    /**
     * Prepare the given view to be shown. This might include adjusting
     * {@link FrameLayout.LayoutParams} before inserting.
     */
    protected void prepareView(android.view.View view) {
        // Take requested dimensions from child, but apply default gravity.
        android.widget.FrameLayout.LayoutParams requested = ((android.widget.FrameLayout.LayoutParams) (view.getLayoutParams()));
        if (requested == null) {
            requested = new android.widget.FrameLayout.LayoutParams(android.widget.FrameLayout.LayoutParams.MATCH_PARENT, android.widget.FrameLayout.LayoutParams.MATCH_PARENT);
        }
        requested.gravity = android.view.Gravity.CENTER;
        view.setLayoutParams(requested);
    }

    /**
     * Inflate and return the default layout requested by AppWidget provider.
     */
    protected android.view.View getDefaultView() {
        if (android.appwidget.AppWidgetHostView.LOGD) {
            android.util.Log.d(android.appwidget.AppWidgetHostView.TAG, "getDefaultView");
        }
        android.view.View defaultView = null;
        java.lang.Exception exception = null;
        try {
            if (mInfo != null) {
                android.content.Context theirContext = getRemoteContext();
                mRemoteContext = theirContext;
                android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (theirContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
                inflater = inflater.cloneInContext(theirContext);
                inflater.setFilter(android.appwidget.AppWidgetHostView.sInflaterFilter);
                android.appwidget.AppWidgetManager manager = android.appwidget.AppWidgetManager.getInstance(mContext);
                android.os.Bundle options = manager.getAppWidgetOptions(mAppWidgetId);
                int layoutId = mInfo.initialLayout;
                if (options.containsKey(android.appwidget.AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY)) {
                    int category = options.getInt(android.appwidget.AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY);
                    if (category == android.appwidget.AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD) {
                        int kgLayoutId = mInfo.initialKeyguardLayout;
                        // If a default keyguard layout is not specified, use the standard
                        // default layout.
                        layoutId = (kgLayoutId == 0) ? layoutId : kgLayoutId;
                    }
                }
                defaultView = inflater.inflate(layoutId, this, false);
            } else {
                android.util.Log.w(android.appwidget.AppWidgetHostView.TAG, "can't inflate defaultView because mInfo is missing");
            }
        } catch (java.lang.RuntimeException e) {
            exception = e;
        }
        if (exception != null) {
            android.util.Log.w(android.appwidget.AppWidgetHostView.TAG, (("Error inflating AppWidget " + mInfo) + ": ") + exception.toString());
        }
        if (defaultView == null) {
            if (android.appwidget.AppWidgetHostView.LOGD)
                android.util.Log.d(android.appwidget.AppWidgetHostView.TAG, "getDefaultView couldn't find any view, so inflating error");

            defaultView = getErrorView();
        }
        return defaultView;
    }

    /**
     * Inflate and return a view that represents an error state.
     */
    protected android.view.View getErrorView() {
        android.widget.TextView tv = new android.widget.TextView(mContext);
        tv.setText(com.android.internal.R.string.gadget_host_error_inflating);
        // TODO: get this color from somewhere.
        tv.setBackgroundColor(android.graphics.Color.argb(127, 0, 0, 0));
        return tv;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setClassName(android.appwidget.AppWidgetHostView.class.getName());
    }

    private static class ParcelableSparseArray extends android.util.SparseArray<android.os.Parcelable> implements android.os.Parcelable {
        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            final int count = size();
            dest.writeInt(count);
            for (int i = 0; i < count; i++) {
                dest.writeInt(keyAt(i));
                dest.writeParcelable(valueAt(i), 0);
            }
        }

        public static final android.os.Parcelable.Creator<android.appwidget.AppWidgetHostView.ParcelableSparseArray> CREATOR = new android.os.Parcelable.Creator<android.appwidget.AppWidgetHostView.ParcelableSparseArray>() {
            public android.appwidget.AppWidgetHostView.ParcelableSparseArray createFromParcel(android.os.Parcel source) {
                final android.appwidget.AppWidgetHostView.ParcelableSparseArray array = new android.appwidget.AppWidgetHostView.ParcelableSparseArray();
                final java.lang.ClassLoader loader = array.getClass().getClassLoader();
                final int count = source.readInt();
                for (int i = 0; i < count; i++) {
                    array.put(source.readInt(), source.readParcelable(loader));
                }
                return array;
            }

            public android.appwidget.AppWidgetHostView.ParcelableSparseArray[] newArray(int size) {
                return new android.appwidget.AppWidgetHostView.ParcelableSparseArray[size];
            }
        };
    }
}

