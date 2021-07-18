/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v7.app;


/**
 * This class implements the route chooser dialog for {@link MediaRouter}.
 * <p>
 * This dialog allows the user to choose a route that matches a given selector.
 * </p>
 *
 * @see MediaRouteButton
 * @see MediaRouteActionProvider
 */
public class MediaRouteChooserDialog extends android.support.v7.app.AppCompatDialog {
    static final java.lang.String TAG = "MediaRouteChooserDialog";

    // Do not update the route list immediately to avoid unnatural dialog change.
    private static final long UPDATE_ROUTES_DELAY_MS = 300L;

    static final int MSG_UPDATE_ROUTES = 1;

    private final android.support.v7.media.MediaRouter mRouter;

    private final android.support.v7.app.MediaRouteChooserDialog.MediaRouterCallback mCallback;

    private android.support.v7.media.MediaRouteSelector mSelector = android.support.v7.media.MediaRouteSelector.EMPTY;

    private java.util.ArrayList<android.support.v7.media.MediaRouter.RouteInfo> mRoutes;

    private android.support.v7.app.MediaRouteChooserDialog.RouteAdapter mAdapter;

    private android.widget.ListView mListView;

    private boolean mAttachedToWindow;

    private long mLastUpdateTime;

    private final android.os.Handler mHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case android.support.v7.app.MediaRouteChooserDialog.MSG_UPDATE_ROUTES :
                    updateRoutes(((java.util.List<android.support.v7.media.MediaRouter.RouteInfo>) (message.obj)));
                    break;
            }
        }
    };

    public MediaRouteChooserDialog(android.content.Context context) {
        this(context, 0);
    }

    public MediaRouteChooserDialog(android.content.Context context, int theme) {
        super(android.support.v7.app.MediaRouterThemeHelper.createThemedContext(context, theme), theme);
        context = getContext();
        mRouter = android.support.v7.media.MediaRouter.getInstance(context);
        mCallback = new android.support.v7.app.MediaRouteChooserDialog.MediaRouterCallback();
    }

    /**
     * Gets the media route selector for filtering the routes that the user can select.
     *
     * @return The selector, never null.
     */
    @android.support.annotation.NonNull
    public android.support.v7.media.MediaRouteSelector getRouteSelector() {
        return mSelector;
    }

    /**
     * Sets the media route selector for filtering the routes that the user can select.
     *
     * @param selector
     * 		The selector, must not be null.
     */
    public void setRouteSelector(@android.support.annotation.NonNull
    android.support.v7.media.MediaRouteSelector selector) {
        if (selector == null) {
            throw new java.lang.IllegalArgumentException("selector must not be null");
        }
        if (!mSelector.equals(selector)) {
            mSelector = selector;
            if (mAttachedToWindow) {
                mRouter.removeCallback(mCallback);
                mRouter.addCallback(selector, mCallback, android.support.v7.media.MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
            }
            refreshRoutes();
        }
    }

    /**
     * Called to filter the set of routes that should be included in the list.
     * <p>
     * The default implementation iterates over all routes in the provided list and
     * removes those for which {@link #onFilterRoute} returns false.
     * </p>
     *
     * @param routes
     * 		The list of routes to filter in-place, never null.
     */
    public void onFilterRoutes(@android.support.annotation.NonNull
    java.util.List<android.support.v7.media.MediaRouter.RouteInfo> routes) {
        for (int i = routes.size(); (i--) > 0;) {
            if (!onFilterRoute(routes.get(i))) {
                routes.remove(i);
            }
        }
    }

    /**
     * Returns true if the route should be included in the list.
     * <p>
     * The default implementation returns true for enabled non-default routes that
     * match the selector.  Subclasses can override this method to filter routes
     * differently.
     * </p>
     *
     * @param route
     * 		The route to consider, never null.
     * @return True if the route should be included in the chooser dialog.
     */
    public boolean onFilterRoute(@android.support.annotation.NonNull
    android.support.v7.media.MediaRouter.RouteInfo route) {
        return ((!route.isDefaultOrBluetooth()) && route.isEnabled()) && route.matchesSelector(mSelector);
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mr_chooser_dialog);
        setTitle(R.string.mr_chooser_title);
        mRoutes = new java.util.ArrayList<>();
        mAdapter = new android.support.v7.app.MediaRouteChooserDialog.RouteAdapter(getContext(), mRoutes);
        mListView = ((android.widget.ListView) (findViewById(R.id.mr_chooser_list)));
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
        mListView.setEmptyView(findViewById(android.R.id.empty));
        updateLayout();
    }

    /**
     * Sets the width of the dialog. Also called when configuration changes.
     */
    void updateLayout() {
        getWindow().setLayout(android.support.v7.app.MediaRouteDialogHelper.getDialogWidth(getContext()), android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @java.lang.Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
        mRouter.addCallback(mSelector, mCallback, android.support.v7.media.MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
        refreshRoutes();
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        mAttachedToWindow = false;
        mRouter.removeCallback(mCallback);
        mHandler.removeMessages(android.support.v7.app.MediaRouteChooserDialog.MSG_UPDATE_ROUTES);
        super.onDetachedFromWindow();
    }

    /**
     * Refreshes the list of routes that are shown in the chooser dialog.
     */
    public void refreshRoutes() {
        if (mAttachedToWindow) {
            java.util.ArrayList<android.support.v7.media.MediaRouter.RouteInfo> routes = new java.util.ArrayList<>(mRouter.getRoutes());
            onFilterRoutes(routes);
            java.util.Collections.sort(routes, android.support.v7.app.MediaRouteChooserDialog.RouteComparator.sInstance);
            if ((android.os.SystemClock.uptimeMillis() - mLastUpdateTime) >= android.support.v7.app.MediaRouteChooserDialog.UPDATE_ROUTES_DELAY_MS) {
                updateRoutes(routes);
            } else {
                mHandler.removeMessages(android.support.v7.app.MediaRouteChooserDialog.MSG_UPDATE_ROUTES);
                mHandler.sendMessageAtTime(mHandler.obtainMessage(android.support.v7.app.MediaRouteChooserDialog.MSG_UPDATE_ROUTES, routes), mLastUpdateTime + android.support.v7.app.MediaRouteChooserDialog.UPDATE_ROUTES_DELAY_MS);
            }
        }
    }

    void updateRoutes(java.util.List<android.support.v7.media.MediaRouter.RouteInfo> routes) {
        mLastUpdateTime = android.os.SystemClock.uptimeMillis();
        mRoutes.clear();
        mRoutes.addAll(routes);
        mAdapter.notifyDataSetChanged();
    }

    private final class RouteAdapter extends android.widget.ArrayAdapter<android.support.v7.media.MediaRouter.RouteInfo> implements android.widget.AdapterView.OnItemClickListener {
        private final android.view.LayoutInflater mInflater;

        private final android.graphics.drawable.Drawable mDefaultIcon;

        private final android.graphics.drawable.Drawable mTvIcon;

        private final android.graphics.drawable.Drawable mSpeakerIcon;

        private final android.graphics.drawable.Drawable mSpeakerGroupIcon;

        public RouteAdapter(android.content.Context context, java.util.List<android.support.v7.media.MediaRouter.RouteInfo> routes) {
            super(context, 0, routes);
            mInflater = android.view.LayoutInflater.from(context);
            android.content.res.TypedArray styledAttributes = getContext().obtainStyledAttributes(new int[]{ R.attr.mediaRouteDefaultIconDrawable, R.attr.mediaRouteTvIconDrawable, R.attr.mediaRouteSpeakerIconDrawable, R.attr.mediaRouteSpeakerGroupIconDrawable });
            mDefaultIcon = styledAttributes.getDrawable(0);
            mTvIcon = styledAttributes.getDrawable(1);
            mSpeakerIcon = styledAttributes.getDrawable(2);
            mSpeakerGroupIcon = styledAttributes.getDrawable(3);
            styledAttributes.recycle();
        }

        @java.lang.Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @java.lang.Override
        public boolean isEnabled(int position) {
            return getItem(position).isEnabled();
        }

        @java.lang.Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.view.View view = convertView;
            if (view == null) {
                view = mInflater.inflate(R.layout.mr_chooser_list_item, parent, false);
            }
            android.support.v7.media.MediaRouter.RouteInfo route = getItem(position);
            android.widget.TextView text1 = ((android.widget.TextView) (view.findViewById(R.id.mr_chooser_route_name)));
            android.widget.TextView text2 = ((android.widget.TextView) (view.findViewById(R.id.mr_chooser_route_desc)));
            text1.setText(route.getName());
            java.lang.String description = route.getDescription();
            boolean isConnectedOrConnecting = (route.getConnectionState() == android.support.v7.media.MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTED) || (route.getConnectionState() == android.support.v7.media.MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTING);
            if (isConnectedOrConnecting && (!android.text.TextUtils.isEmpty(description))) {
                text1.setGravity(android.view.Gravity.BOTTOM);
                text2.setVisibility(android.view.View.VISIBLE);
                text2.setText(description);
            } else {
                text1.setGravity(android.view.Gravity.CENTER_VERTICAL);
                text2.setVisibility(android.view.View.GONE);
                text2.setText("");
            }
            view.setEnabled(route.isEnabled());
            android.widget.ImageView iconView = ((android.widget.ImageView) (view.findViewById(R.id.mr_chooser_route_icon)));
            if (iconView != null) {
                iconView.setImageDrawable(getIconDrawable(route));
            }
            return view;
        }

        @java.lang.Override
        public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
            android.support.v7.media.MediaRouter.RouteInfo route = getItem(position);
            if (route.isEnabled()) {
                route.select();
                dismiss();
            }
        }

        private android.graphics.drawable.Drawable getIconDrawable(android.support.v7.media.MediaRouter.RouteInfo route) {
            android.net.Uri iconUri = route.getIconUri();
            if (iconUri != null) {
                try {
                    java.io.InputStream is = getContext().getContentResolver().openInputStream(iconUri);
                    android.graphics.drawable.Drawable drawable = android.graphics.drawable.Drawable.createFromStream(is, null);
                    if (drawable != null) {
                        return drawable;
                    }
                } catch (java.io.IOException e) {
                    android.util.Log.w(android.support.v7.app.MediaRouteChooserDialog.TAG, "Failed to load " + iconUri, e);
                    // Falls back.
                }
            }
            return getDefaultIconDrawable(route);
        }

        private android.graphics.drawable.Drawable getDefaultIconDrawable(android.support.v7.media.MediaRouter.RouteInfo route) {
            // If the type of the receiver device is specified, use it.
            switch (route.getDeviceType()) {
                case android.support.v7.media.MediaRouter.RouteInfo.DEVICE_TYPE_TV :
                    return mTvIcon;
                case android.support.v7.media.MediaRouter.RouteInfo.DEVICE_TYPE_SPEAKER :
                    return mSpeakerIcon;
            }
            // Otherwise, make the best guess based on other route information.
            if (route instanceof android.support.v7.media.MediaRouter.RouteGroup) {
                // Only speakers can be grouped for now.
                return mSpeakerGroupIcon;
            }
            return mDefaultIcon;
        }
    }

    private final class MediaRouterCallback extends android.support.v7.media.MediaRouter.Callback {
        MediaRouterCallback() {
        }

        @java.lang.Override
        public void onRouteAdded(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo info) {
            refreshRoutes();
        }

        @java.lang.Override
        public void onRouteRemoved(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo info) {
            refreshRoutes();
        }

        @java.lang.Override
        public void onRouteChanged(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo info) {
            refreshRoutes();
        }

        @java.lang.Override
        public void onRouteSelected(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
            dismiss();
        }
    }

    private static final class RouteComparator implements java.util.Comparator<android.support.v7.media.MediaRouter.RouteInfo> {
        public static final android.support.v7.app.MediaRouteChooserDialog.RouteComparator sInstance = new android.support.v7.app.MediaRouteChooserDialog.RouteComparator();

        @java.lang.Override
        public int compare(android.support.v7.media.MediaRouter.RouteInfo lhs, android.support.v7.media.MediaRouter.RouteInfo rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }
}

