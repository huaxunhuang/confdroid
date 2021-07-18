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
 * The media route action provider displays a {@link MediaRouteButton media route button}
 * in the application's {@link ActionBar} to allow the user to select routes and
 * to control the currently selected route.
 * <p>
 * The application must specify the kinds of routes that the user should be allowed
 * to select by specifying a {@link MediaRouteSelector selector} with the
 * {@link #setRouteSelector} method.
 * </p><p>
 * Refer to {@link MediaRouteButton} for a description of the button that will
 * appear in the action bar menu.  Note that instead of disabling the button
 * when no routes are available, the action provider will instead make the
 * menu item invisible.  In this way, the button will only be visible when it
 * is possible for the user to discover and select a matching route.
 * </p>
 *
 * <h3>Prerequisites</h3>
 * <p>
 * To use the media route action provider, the activity must be a subclass of
 * {@link AppCompatActivity} from the <code>android.support.v7.appcompat</code>
 * support library.  Refer to support library documentation for details.
 * </p>
 *
 * <h3>Example</h3>
 * <p>
 * </p><p>
 * The application should define a menu resource to include the provider in the
 * action bar options menu.  Note that the support library action bar uses attributes
 * that are defined in the application's resource namespace rather than the framework's
 * resource namespace to configure each item.
 * </p><pre>
 * &lt;menu xmlns:android="http://schemas.android.com/apk/res/android"
 *         xmlns:app="http://schemas.android.com/apk/res-auto">
 *     &lt;item android:id="@+id/media_route_menu_item"
 *         android:title="@string/media_route_menu_title"
 *         app:showAsAction="always"
 *         app:actionProviderClass="android.support.v7.app.MediaRouteActionProvider"/>
 * &lt;/menu>
 * </pre><p>
 * Then configure the menu and set the route selector for the chooser.
 * </p><pre>
 * public class MyActivity extends ActionBarActivity {
 *     private MediaRouter mRouter;
 *     private MediaRouter.Callback mCallback;
 *     private MediaRouteSelector mSelector;
 *
 *     protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *
 *         mRouter = Mediarouter.getInstance(this);
 *         mSelector = new MediaRouteSelector.Builder()
 *                 .addControlCategory(MediaControlIntent.CATEGORY_LIVE_AUDIO)
 *                 .addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
 *                 .build();
 *         mCallback = new MyCallback();
 *     }
 *
 *     // Add the callback on start to tell the media router what kinds of routes
 *     // the application is interested in so that it can try to discover suitable ones.
 *     public void onStart() {
 *         super.onStart();
 *
 *         mediaRouter.addCallback(mSelector, mCallback,
 *                 MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
 *
 *         MediaRouter.RouteInfo route = mediaRouter.updateSelectedRoute(mSelector);
 *         // do something with the route...
 *     }
 *
 *     // Remove the selector on stop to tell the media router that it no longer
 *     // needs to invest effort trying to discover routes of these kinds for now.
 *     public void onStop() {
 *         super.onStop();
 *
 *         mediaRouter.removeCallback(mCallback);
 *     }
 *
 *     public boolean onCreateOptionsMenu(Menu menu) {
 *         super.onCreateOptionsMenu(menu);
 *
 *         getMenuInflater().inflate(R.menu.sample_media_router_menu, menu);
 *
 *         MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
 *         MediaRouteActionProvider mediaRouteActionProvider =
 *                 (MediaRouteActionProvider)MenuItemCompat.getActionProvider(mediaRouteMenuItem);
 *         mediaRouteActionProvider.setRouteSelector(mSelector);
 *         return true;
 *     }
 *
 *     private final class MyCallback extends MediaRouter.Callback {
 *         // Implement callback methods as needed.
 *     }
 * }
 * </pre>
 *
 * @see #setRouteSelector
 */
public class MediaRouteActionProvider extends android.support.v4.view.ActionProvider {
    private static final java.lang.String TAG = "MediaRouteActionProvider";

    private final android.support.v7.media.MediaRouter mRouter;

    private final android.support.v7.app.MediaRouteActionProvider.MediaRouterCallback mCallback;

    private android.support.v7.media.MediaRouteSelector mSelector = android.support.v7.media.MediaRouteSelector.EMPTY;

    private android.support.v7.app.MediaRouteDialogFactory mDialogFactory = android.support.v7.app.MediaRouteDialogFactory.getDefault();

    private android.support.v7.app.MediaRouteButton mButton;

    /**
     * Creates the action provider.
     *
     * @param context
     * 		The context.
     */
    public MediaRouteActionProvider(android.content.Context context) {
        super(context);
        mRouter = android.support.v7.media.MediaRouter.getInstance(context);
        mCallback = new android.support.v7.app.MediaRouteActionProvider.MediaRouterCallback(this);
    }

    /**
     * Gets the media route selector for filtering the routes that the user can
     * select using the media route chooser dialog.
     *
     * @return The selector, never null.
     */
    @android.support.annotation.NonNull
    public android.support.v7.media.MediaRouteSelector getRouteSelector() {
        return mSelector;
    }

    /**
     * Sets the media route selector for filtering the routes that the user can
     * select using the media route chooser dialog.
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
            // FIXME: We currently have no way of knowing whether the action provider
            // is still needed by the UI.  Unfortunately this means the action provider
            // may leak callbacks until garbage collection occurs.  This may result in
            // media route providers doing more work than necessary in the short term
            // while trying to discover routes that are no longer of interest to the
            // application.  To solve this problem, the action provider will need some
            // indication from the framework that it is being destroyed.
            if (!mSelector.isEmpty()) {
                mRouter.removeCallback(mCallback);
            }
            if (!selector.isEmpty()) {
                mRouter.addCallback(selector, mCallback);
            }
            mSelector = selector;
            refreshRoute();
            if (mButton != null) {
                mButton.setRouteSelector(selector);
            }
        }
    }

    /**
     * Gets the media route dialog factory to use when showing the route chooser
     * or controller dialog.
     *
     * @return The dialog factory, never null.
     */
    @android.support.annotation.NonNull
    public android.support.v7.app.MediaRouteDialogFactory getDialogFactory() {
        return mDialogFactory;
    }

    /**
     * Sets the media route dialog factory to use when showing the route chooser
     * or controller dialog.
     *
     * @param factory
     * 		The dialog factory, must not be null.
     */
    public void setDialogFactory(@android.support.annotation.NonNull
    android.support.v7.app.MediaRouteDialogFactory factory) {
        if (factory == null) {
            throw new java.lang.IllegalArgumentException("factory must not be null");
        }
        if (mDialogFactory != factory) {
            mDialogFactory = factory;
            if (mButton != null) {
                mButton.setDialogFactory(factory);
            }
        }
    }

    /**
     * Gets the associated media route button, or null if it has not yet been created.
     */
    @android.support.annotation.Nullable
    public android.support.v7.app.MediaRouteButton getMediaRouteButton() {
        return mButton;
    }

    /**
     * Called when the media route button is being created.
     * <p>
     * Subclasses may override this method to customize the button.
     * </p>
     */
    public android.support.v7.app.MediaRouteButton onCreateMediaRouteButton() {
        return new android.support.v7.app.MediaRouteButton(getContext());
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("deprecation")
    public android.view.View onCreateActionView() {
        if (mButton != null) {
            android.util.Log.e(android.support.v7.app.MediaRouteActionProvider.TAG, "onCreateActionView: this ActionProvider is already associated " + ("with a menu item. Don't reuse MediaRouteActionProvider instances! " + "Abandoning the old menu item..."));
        }
        mButton = onCreateMediaRouteButton();
        mButton.setCheatSheetEnabled(true);
        mButton.setRouteSelector(mSelector);
        mButton.setDialogFactory(mDialogFactory);
        mButton.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        return mButton;
    }

    @java.lang.Override
    public boolean onPerformDefaultAction() {
        if (mButton != null) {
            return mButton.showDialog();
        }
        return false;
    }

    @java.lang.Override
    public boolean overridesItemVisibility() {
        return true;
    }

    @java.lang.Override
    public boolean isVisible() {
        return mRouter.isRouteAvailable(mSelector, android.support.v7.media.MediaRouter.AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE);
    }

    void refreshRoute() {
        refreshVisibility();
    }

    private static final class MediaRouterCallback extends android.support.v7.media.MediaRouter.Callback {
        private final java.lang.ref.WeakReference<android.support.v7.app.MediaRouteActionProvider> mProviderWeak;

        public MediaRouterCallback(android.support.v7.app.MediaRouteActionProvider provider) {
            mProviderWeak = new java.lang.ref.WeakReference<android.support.v7.app.MediaRouteActionProvider>(provider);
        }

        @java.lang.Override
        public void onRouteAdded(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo info) {
            refreshRoute(router);
        }

        @java.lang.Override
        public void onRouteRemoved(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo info) {
            refreshRoute(router);
        }

        @java.lang.Override
        public void onRouteChanged(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo info) {
            refreshRoute(router);
        }

        @java.lang.Override
        public void onProviderAdded(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.ProviderInfo provider) {
            refreshRoute(router);
        }

        @java.lang.Override
        public void onProviderRemoved(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.ProviderInfo provider) {
            refreshRoute(router);
        }

        @java.lang.Override
        public void onProviderChanged(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.ProviderInfo provider) {
            refreshRoute(router);
        }

        private void refreshRoute(android.support.v7.media.MediaRouter router) {
            android.support.v7.app.MediaRouteActionProvider provider = mProviderWeak.get();
            if (provider != null) {
                provider.refreshRoute();
            } else {
                router.removeCallback(this);
            }
        }
    }
}

