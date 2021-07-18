/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.app;


/**
 * The media route action provider displays a {@link MediaRouteButton media route button}
 * in the application's {@link ActionBar} to allow the user to select routes and
 * to control the currently selected route.
 * <p>
 * The application must specify the kinds of routes that the user should be allowed
 * to select by specifying the route types with the {@link #setRouteTypes} method.
 * </p><p>
 * Refer to {@link MediaRouteButton} for a description of the button that will
 * appear in the action bar menu.  Note that instead of disabling the button
 * when no routes are available, the action provider will instead make the
 * menu item invisible.  In this way, the button will only be visible when it
 * is possible for the user to discover and select a matching route.
 * </p>
 */
public class MediaRouteActionProvider extends android.view.ActionProvider {
    private static final java.lang.String TAG = "MediaRouteActionProvider";

    private final android.content.Context mContext;

    private final android.media.MediaRouter mRouter;

    private final android.app.MediaRouteActionProvider.MediaRouterCallback mCallback;

    private int mRouteTypes;

    private android.app.MediaRouteButton mButton;

    private android.view.View.OnClickListener mExtendedSettingsListener;

    public MediaRouteActionProvider(android.content.Context context) {
        super(context);
        mContext = context;
        mRouter = ((android.media.MediaRouter) (context.getSystemService(android.content.Context.MEDIA_ROUTER_SERVICE)));
        mCallback = new android.app.MediaRouteActionProvider.MediaRouterCallback(this);
        // Start with live audio by default.
        // TODO Update this when new route types are added; segment by API level
        // when different route types were added.
        setRouteTypes(android.media.MediaRouter.ROUTE_TYPE_LIVE_AUDIO);
    }

    /**
     * Sets the types of routes that will be shown in the media route chooser dialog
     * launched by this button.
     *
     * @param types
     * 		The route types to match.
     */
    public void setRouteTypes(int types) {
        if (mRouteTypes != types) {
            // FIXME: We currently have no way of knowing whether the action provider
            // is still needed by the UI.  Unfortunately this means the action provider
            // may leak callbacks until garbage collection occurs.  This may result in
            // media route providers doing more work than necessary in the short term
            // while trying to discover routes that are no longer of interest to the
            // application.  To solve this problem, the action provider will need some
            // indication from the framework that it is being destroyed.
            if (mRouteTypes != 0) {
                mRouter.removeCallback(mCallback);
            }
            mRouteTypes = types;
            if (types != 0) {
                mRouter.addCallback(types, mCallback, android.media.MediaRouter.CALLBACK_FLAG_PASSIVE_DISCOVERY);
            }
            refreshRoute();
            if (mButton != null) {
                mButton.setRouteTypes(mRouteTypes);
            }
        }
    }

    public void setExtendedSettingsClickListener(android.view.View.OnClickListener listener) {
        mExtendedSettingsListener = listener;
        if (mButton != null) {
            mButton.setExtendedSettingsClickListener(listener);
        }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("deprecation")
    public android.view.View onCreateActionView() {
        throw new java.lang.UnsupportedOperationException("Use onCreateActionView(MenuItem) instead.");
    }

    @java.lang.Override
    public android.view.View onCreateActionView(android.view.MenuItem item) {
        if (mButton != null) {
            android.util.Log.e(android.app.MediaRouteActionProvider.TAG, "onCreateActionView: this ActionProvider is already associated " + ("with a menu item. Don't reuse MediaRouteActionProvider instances! " + "Abandoning the old one..."));
        }
        mButton = new android.app.MediaRouteButton(mContext);
        mButton.setCheatSheetEnabled(true);
        mButton.setRouteTypes(mRouteTypes);
        mButton.setExtendedSettingsClickListener(mExtendedSettingsListener);
        mButton.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        return mButton;
    }

    @java.lang.Override
    public boolean onPerformDefaultAction() {
        if (mButton != null) {
            return mButton.showDialogInternal();
        }
        return false;
    }

    @java.lang.Override
    public boolean overridesItemVisibility() {
        return true;
    }

    @java.lang.Override
    public boolean isVisible() {
        return mRouter.isRouteAvailable(mRouteTypes, android.media.MediaRouter.AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE);
    }

    private void refreshRoute() {
        refreshVisibility();
    }

    private static class MediaRouterCallback extends android.media.MediaRouter.SimpleCallback {
        private final java.lang.ref.WeakReference<android.app.MediaRouteActionProvider> mProviderWeak;

        public MediaRouterCallback(android.app.MediaRouteActionProvider provider) {
            mProviderWeak = new java.lang.ref.WeakReference<android.app.MediaRouteActionProvider>(provider);
        }

        @java.lang.Override
        public void onRouteAdded(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info) {
            refreshRoute(router);
        }

        @java.lang.Override
        public void onRouteRemoved(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info) {
            refreshRoute(router);
        }

        @java.lang.Override
        public void onRouteChanged(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info) {
            refreshRoute(router);
        }

        private void refreshRoute(android.media.MediaRouter router) {
            android.app.MediaRouteActionProvider provider = mProviderWeak.get();
            if (provider != null) {
                provider.refreshRoute();
            } else {
                router.removeCallback(this);
            }
        }
    }
}

