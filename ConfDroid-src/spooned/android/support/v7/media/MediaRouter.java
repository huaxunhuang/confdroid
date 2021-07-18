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
package android.support.v7.media;


/**
 * MediaRouter allows applications to control the routing of media channels
 * and streams from the current device to external speakers and destination devices.
 * <p>
 * A MediaRouter instance is retrieved through {@link #getInstance}.  Applications
 * can query the media router about the currently selected route and its capabilities
 * to determine how to send content to the route's destination.  Applications can
 * also {@link RouteInfo#sendControlRequest send control requests} to the route
 * to ask the route's destination to perform certain remote control functions
 * such as playing media.
 * </p><p>
 * See also {@link MediaRouteProvider} for information on how an application
 * can publish new media routes to the media router.
 * </p><p>
 * The media router API is not thread-safe; all interactions with it must be
 * done from the main thread of the process.
 * </p>
 */
public final class MediaRouter {
    static final java.lang.String TAG = "MediaRouter";

    static final boolean DEBUG = android.util.Log.isLoggable(android.support.v7.media.MediaRouter.TAG, android.util.Log.DEBUG);

    /**
     * Passed to {@link android.support.v7.media.MediaRouteProvider.RouteController#onUnselect(int)}
     * and {@link Callback#onRouteUnselected(MediaRouter, RouteInfo, int)} when the reason the route
     * was unselected is unknown.
     */
    public static final int UNSELECT_REASON_UNKNOWN = 0;

    /**
     * Passed to {@link android.support.v7.media.MediaRouteProvider.RouteController#onUnselect(int)}
     * and {@link Callback#onRouteUnselected(MediaRouter, RouteInfo, int)} when the user pressed
     * the disconnect button to disconnect and keep playing.
     * <p>
     *
     * @see {@link MediaRouteDescriptor#canDisconnectAndKeepPlaying()}.
     */
    public static final int UNSELECT_REASON_DISCONNECTED = 1;

    /**
     * Passed to {@link android.support.v7.media.MediaRouteProvider.RouteController#onUnselect(int)}
     * and {@link Callback#onRouteUnselected(MediaRouter, RouteInfo, int)} when the user pressed
     * the stop casting button.
     */
    public static final int UNSELECT_REASON_STOPPED = 2;

    /**
     * Passed to {@link android.support.v7.media.MediaRouteProvider.RouteController#onUnselect(int)}
     * and {@link Callback#onRouteUnselected(MediaRouter, RouteInfo, int)} when the user selected
     * a different route.
     */
    public static final int UNSELECT_REASON_ROUTE_CHANGED = 3;

    // Maintains global media router state for the process.
    // This field is initialized in MediaRouter.getInstance() before any
    // MediaRouter objects are instantiated so it is guaranteed to be
    // valid whenever any instance method is invoked.
    static android.support.v7.media.MediaRouter.GlobalMediaRouter sGlobal;

    // Context-bound state of the media router.
    final android.content.Context mContext;

    final java.util.ArrayList<android.support.v7.media.MediaRouter.CallbackRecord> mCallbackRecords = new java.util.ArrayList<android.support.v7.media.MediaRouter.CallbackRecord>();

    @android.support.annotation.IntDef(flag = true, value = { android.support.v7.media.MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN, android.support.v7.media.MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY, android.support.v7.media.MediaRouter.CALLBACK_FLAG_UNFILTERED_EVENTS })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface CallbackFlags {}

    /**
     * Flag for {@link #addCallback}: Actively scan for routes while this callback
     * is registered.
     * <p>
     * When this flag is specified, the media router will actively scan for new
     * routes.  Certain routes, such as wifi display routes, may not be discoverable
     * except when actively scanning.  This flag is typically used when the route picker
     * dialog has been opened by the user to ensure that the route information is
     * up to date.
     * </p><p>
     * Active scanning may consume a significant amount of power and may have intrusive
     * effects on wireless connectivity.  Therefore it is important that active scanning
     * only be requested when it is actually needed to satisfy a user request to
     * discover and select a new route.
     * </p><p>
     * This flag implies {@link #CALLBACK_FLAG_REQUEST_DISCOVERY} but performing
     * active scans is much more expensive than a normal discovery request.
     * </p>
     *
     * @see #CALLBACK_FLAG_REQUEST_DISCOVERY
     */
    public static final int CALLBACK_FLAG_PERFORM_ACTIVE_SCAN = 1 << 0;

    /**
     * Flag for {@link #addCallback}: Do not filter route events.
     * <p>
     * When this flag is specified, the callback will be invoked for events that affect any
     * route even if they do not match the callback's filter.
     * </p>
     */
    public static final int CALLBACK_FLAG_UNFILTERED_EVENTS = 1 << 1;

    /**
     * Flag for {@link #addCallback}: Request passive route discovery while this
     * callback is registered, except on {@link ActivityManager#isLowRamDevice low-RAM devices}.
     * <p>
     * When this flag is specified, the media router will try to discover routes.
     * Although route discovery is intended to be efficient, checking for new routes may
     * result in some network activity and could slowly drain the battery.  Therefore
     * applications should only specify {@link #CALLBACK_FLAG_REQUEST_DISCOVERY} when
     * they are running in the foreground and would like to provide the user with the
     * option of connecting to new routes.
     * </p><p>
     * Applications should typically add a callback using this flag in the
     * {@link android.app.Activity activity's} {@link android.app.Activity#onStart onStart}
     * method and remove it in the {@link android.app.Activity#onStop onStop} method.
     * The {@link android.support.v7.app.MediaRouteDiscoveryFragment} fragment may
     * also be used for this purpose.
     * </p><p class="note">
     * On {@link ActivityManager#isLowRamDevice low-RAM devices} this flag
     * will be ignored.  Refer to
     * {@link #addCallback(MediaRouteSelector, Callback, int) addCallback} for details.
     * </p>
     *
     * @see android.support.v7.app.MediaRouteDiscoveryFragment
     */
    public static final int CALLBACK_FLAG_REQUEST_DISCOVERY = 1 << 2;

    /**
     * Flag for {@link #addCallback}: Request passive route discovery while this
     * callback is registered, even on {@link ActivityManager#isLowRamDevice low-RAM devices}.
     * <p class="note">
     * This flag has a significant performance impact on low-RAM devices
     * since it may cause many media route providers to be started simultaneously.
     * It is much better to use {@link #CALLBACK_FLAG_REQUEST_DISCOVERY} instead to avoid
     * performing passive discovery on these devices altogether.  Refer to
     * {@link #addCallback(MediaRouteSelector, Callback, int) addCallback} for details.
     * </p>
     *
     * @see android.support.v7.app.MediaRouteDiscoveryFragment
     */
    public static final int CALLBACK_FLAG_FORCE_DISCOVERY = 1 << 3;

    /**
     * Flag for {@link #isRouteAvailable}: Ignore the default route.
     * <p>
     * This flag is used to determine whether a matching non-default route is available.
     * This constraint may be used to decide whether to offer the route chooser dialog
     * to the user.  There is no point offering the chooser if there are no
     * non-default choices.
     * </p>
     */
    public static final int AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE = 1 << 0;

    /**
     * Flag for {@link #isRouteAvailable}: Require an actual route to be matched.
     * <p>
     * If this flag is not set, then {@link #isRouteAvailable} will return true
     * if it is possible to discover a matching route even if discovery is not in
     * progress or if no matching route has yet been found.  This feature is used to
     * save resources by removing the need to perform passive route discovery on
     * {@link ActivityManager#isLowRamDevice low-RAM devices}.
     * </p><p>
     * If this flag is set, then {@link #isRouteAvailable} will only return true if
     * a matching route has actually been discovered.
     * </p>
     */
    public static final int AVAILABILITY_FLAG_REQUIRE_MATCH = 1 << 1;

    MediaRouter(android.content.Context context) {
        mContext = context;
    }

    /**
     * Gets an instance of the media router service associated with the context.
     * <p>
     * The application is responsible for holding a strong reference to the returned
     * {@link MediaRouter} instance, such as by storing the instance in a field of
     * the {@link android.app.Activity}, to ensure that the media router remains alive
     * as long as the application is using its features.
     * </p><p>
     * In other words, the support library only holds a {@link WeakReference weak reference}
     * to each media router instance.  When there are no remaining strong references to the
     * media router instance, all of its callbacks will be removed and route discovery
     * will no longer be performed on its behalf.
     * </p>
     *
     * @return The media router instance for the context.  The application must hold
    a strong reference to this object as long as it is in use.
     */
    public static android.support.v7.media.MediaRouter getInstance(@android.support.annotation.NonNull
    android.content.Context context) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("context must not be null");
        }
        android.support.v7.media.MediaRouter.checkCallingThread();
        if (android.support.v7.media.MediaRouter.sGlobal == null) {
            android.support.v7.media.MediaRouter.sGlobal = new android.support.v7.media.MediaRouter.GlobalMediaRouter(context.getApplicationContext());
            android.support.v7.media.MediaRouter.sGlobal.start();
        }
        return android.support.v7.media.MediaRouter.sGlobal.getRouter(context);
    }

    /**
     * Gets information about the {@link MediaRouter.RouteInfo routes} currently known to
     * this media router.
     */
    public java.util.List<android.support.v7.media.MediaRouter.RouteInfo> getRoutes() {
        android.support.v7.media.MediaRouter.checkCallingThread();
        return android.support.v7.media.MediaRouter.sGlobal.getRoutes();
    }

    /**
     * Gets information about the {@link MediaRouter.ProviderInfo route providers}
     * currently known to this media router.
     */
    public java.util.List<android.support.v7.media.MediaRouter.ProviderInfo> getProviders() {
        android.support.v7.media.MediaRouter.checkCallingThread();
        return android.support.v7.media.MediaRouter.sGlobal.getProviders();
    }

    /**
     * Gets the default route for playing media content on the system.
     * <p>
     * The system always provides a default route.
     * </p>
     *
     * @return The default route, which is guaranteed to never be null.
     */
    @android.support.annotation.NonNull
    public android.support.v7.media.MediaRouter.RouteInfo getDefaultRoute() {
        android.support.v7.media.MediaRouter.checkCallingThread();
        return android.support.v7.media.MediaRouter.sGlobal.getDefaultRoute();
    }

    /**
     * Gets the currently selected route.
     * <p>
     * The application should examine the route's
     * {@link RouteInfo#getControlFilters media control intent filters} to assess the
     * capabilities of the route before attempting to use it.
     * </p>
     *
     * <h3>Example</h3>
     * <pre>
     * public boolean playMovie() {
     *     MediaRouter mediaRouter = MediaRouter.getInstance(context);
     *     MediaRouter.RouteInfo route = mediaRouter.getSelectedRoute();
     *
     *     // First try using the remote playback interface, if supported.
     *     if (route.supportsControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)) {
     *         // The route supports remote playback.
     *         // Try to send it the Uri of the movie to play.
     *         Intent intent = new Intent(MediaControlIntent.ACTION_PLAY);
     *         intent.addCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK);
     *         intent.setDataAndType("http://example.com/videos/movie.mp4", "video/mp4");
     *         if (route.supportsControlRequest(intent)) {
     *             route.sendControlRequest(intent, null);
     *             return true; // sent the request to play the movie
     *         }
     *     }
     *
     *     // If remote playback was not possible, then play locally.
     *     if (route.supportsControlCategory(MediaControlIntent.CATEGORY_LIVE_VIDEO)) {
     *         // The route supports live video streaming.
     *         // Prepare to play content locally in a window or in a presentation.
     *         return playMovieInWindow();
     *     }
     *
     *     // Neither interface is supported, so we can't play the movie to this route.
     *     return false;
     * }
     * </pre>
     *
     * @return The selected route, which is guaranteed to never be null.
     * @see RouteInfo#getControlFilters
     * @see RouteInfo#supportsControlCategory
     * @see RouteInfo#supportsControlRequest
     */
    @android.support.annotation.NonNull
    public android.support.v7.media.MediaRouter.RouteInfo getSelectedRoute() {
        android.support.v7.media.MediaRouter.checkCallingThread();
        return android.support.v7.media.MediaRouter.sGlobal.getSelectedRoute();
    }

    /**
     * Returns the selected route if it matches the specified selector, otherwise
     * selects the default route and returns it. If there is one live audio route
     * (usually Bluetooth A2DP), it will be selected instead of default route.
     *
     * @param selector
     * 		The selector to match.
     * @return The previously selected route if it matched the selector, otherwise the
    newly selected default route which is guaranteed to never be null.
     * @see MediaRouteSelector
     * @see RouteInfo#matchesSelector
     */
    @android.support.annotation.NonNull
    public android.support.v7.media.MediaRouter.RouteInfo updateSelectedRoute(@android.support.annotation.NonNull
    android.support.v7.media.MediaRouteSelector selector) {
        if (selector == null) {
            throw new java.lang.IllegalArgumentException("selector must not be null");
        }
        android.support.v7.media.MediaRouter.checkCallingThread();
        if (android.support.v7.media.MediaRouter.DEBUG) {
            android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "updateSelectedRoute: " + selector);
        }
        android.support.v7.media.MediaRouter.RouteInfo route = android.support.v7.media.MediaRouter.sGlobal.getSelectedRoute();
        if ((!route.isDefaultOrBluetooth()) && (!route.matchesSelector(selector))) {
            route = android.support.v7.media.MediaRouter.sGlobal.chooseFallbackRoute();
            android.support.v7.media.MediaRouter.sGlobal.selectRoute(route);
        }
        return route;
    }

    /**
     * Selects the specified route.
     *
     * @param route
     * 		The route to select.
     */
    public void selectRoute(@android.support.annotation.NonNull
    android.support.v7.media.MediaRouter.RouteInfo route) {
        if (route == null) {
            throw new java.lang.IllegalArgumentException("route must not be null");
        }
        android.support.v7.media.MediaRouter.checkCallingThread();
        if (android.support.v7.media.MediaRouter.DEBUG) {
            android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "selectRoute: " + route);
        }
        android.support.v7.media.MediaRouter.sGlobal.selectRoute(route);
    }

    /**
     * Unselects the current round and selects the default route instead.
     * <p>
     * The reason given must be one of:
     * <ul>
     * <li>{@link MediaRouter#UNSELECT_REASON_UNKNOWN}</li>
     * <li>{@link MediaRouter#UNSELECT_REASON_DISCONNECTED}</li>
     * <li>{@link MediaRouter#UNSELECT_REASON_STOPPED}</li>
     * <li>{@link MediaRouter#UNSELECT_REASON_ROUTE_CHANGED}</li>
     * </ul>
     *
     * @param reason
     * 		The reason for disconnecting the current route.
     */
    public void unselect(int reason) {
        if ((reason < android.support.v7.media.MediaRouter.UNSELECT_REASON_UNKNOWN) || (reason > android.support.v7.media.MediaRouter.UNSELECT_REASON_ROUTE_CHANGED)) {
            throw new java.lang.IllegalArgumentException("Unsupported reason to unselect route");
        }
        android.support.v7.media.MediaRouter.checkCallingThread();
        // Choose the fallback route if it's not already selected.
        // Otherwise, select the default route.
        android.support.v7.media.MediaRouter.RouteInfo fallbackRoute = android.support.v7.media.MediaRouter.sGlobal.chooseFallbackRoute();
        if (android.support.v7.media.MediaRouter.sGlobal.getSelectedRoute() != fallbackRoute) {
            android.support.v7.media.MediaRouter.sGlobal.selectRoute(fallbackRoute, reason);
        } else {
            android.support.v7.media.MediaRouter.sGlobal.selectRoute(android.support.v7.media.MediaRouter.sGlobal.getDefaultRoute(), reason);
        }
    }

    /**
     * Returns true if there is a route that matches the specified selector.
     * <p>
     * This method returns true if there are any available routes that match the
     * selector regardless of whether they are enabled or disabled. If the
     * {@link #AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE} flag is specified, then
     * the method will only consider non-default routes.
     * </p>
     * <p class="note">
     * On {@link ActivityManager#isLowRamDevice low-RAM devices} this method
     * will return true if it is possible to discover a matching route even if
     * discovery is not in progress or if no matching route has yet been found.
     * Use {@link #AVAILABILITY_FLAG_REQUIRE_MATCH} to require an actual match.
     * </p>
     *
     * @param selector
     * 		The selector to match.
     * @param flags
     * 		Flags to control the determination of whether a route may be
     * 		available. May be zero or some combination of
     * 		{@link #AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE} and
     * 		{@link #AVAILABILITY_FLAG_REQUIRE_MATCH}.
     * @return True if a matching route may be available.
     */
    public boolean isRouteAvailable(@android.support.annotation.NonNull
    android.support.v7.media.MediaRouteSelector selector, int flags) {
        if (selector == null) {
            throw new java.lang.IllegalArgumentException("selector must not be null");
        }
        android.support.v7.media.MediaRouter.checkCallingThread();
        return android.support.v7.media.MediaRouter.sGlobal.isRouteAvailable(selector, flags);
    }

    /**
     * Registers a callback to discover routes that match the selector and to receive
     * events when they change.
     * <p>
     * This is a convenience method that has the same effect as calling
     * {@link #addCallback(MediaRouteSelector, Callback, int)} without flags.
     * </p>
     *
     * @param selector
     * 		A route selector that indicates the kinds of routes that the
     * 		callback would like to discover.
     * @param callback
     * 		The callback to add.
     * @see #removeCallback
     */
    public void addCallback(android.support.v7.media.MediaRouteSelector selector, android.support.v7.media.MediaRouter.Callback callback) {
        addCallback(selector, callback, 0);
    }

    /**
     * Registers a callback to discover routes that match the selector and to receive
     * events when they change.
     * <p>
     * The selector describes the kinds of routes that the application wants to
     * discover.  For example, if the application wants to use
     * live audio routes then it should include the
     * {@link MediaControlIntent#CATEGORY_LIVE_AUDIO live audio media control intent category}
     * in its selector when it adds a callback to the media router.
     * The selector may include any number of categories.
     * </p><p>
     * If the callback has already been registered, then the selector is added to
     * the set of selectors being monitored by the callback.
     * </p><p>
     * By default, the callback will only be invoked for events that affect routes
     * that match the specified selector.  Event filtering may be disabled by specifying
     * the {@link #CALLBACK_FLAG_UNFILTERED_EVENTS} flag when the callback is registered.
     * </p><p>
     * Applications should use the {@link #isRouteAvailable} method to determine
     * whether is it possible to discover a route with the desired capabilities
     * and therefore whether the media route button should be shown to the user.
     * </p><p>
     * The {@link #CALLBACK_FLAG_REQUEST_DISCOVERY} flag should be used while the application
     * is in the foreground to request that passive discovery be performed if there are
     * sufficient resources to allow continuous passive discovery.
     * On {@link ActivityManager#isLowRamDevice low-RAM devices} this flag will be
     * ignored to conserve resources.
     * </p><p>
     * The {@link #CALLBACK_FLAG_FORCE_DISCOVERY} flag should be used when
     * passive discovery absolutely must be performed, even on low-RAM devices.
     * This flag has a significant performance impact on low-RAM devices
     * since it may cause many media route providers to be started simultaneously.
     * It is much better to use {@link #CALLBACK_FLAG_REQUEST_DISCOVERY} instead to avoid
     * performing passive discovery on these devices altogether.
     * </p><p>
     * The {@link #CALLBACK_FLAG_PERFORM_ACTIVE_SCAN} flag should be used when the
     * media route chooser dialog is showing to confirm the presence of available
     * routes that the user may connect to.  This flag may use substantially more
     * power.
     * </p>
     *
     * <h3>Example</h3>
     * <pre>
     * public class MyActivity extends Activity {
     *     private MediaRouter mRouter;
     *     private MediaRouter.Callback mCallback;
     *     private MediaRouteSelector mSelector;
     *
     *     protected void onCreate(Bundle savedInstanceState) {
     *         super.onCreate(savedInstanceState);
     *
     *         mRouter = Mediarouter.getInstance(this);
     *         mCallback = new MyCallback();
     *         mSelector = new MediaRouteSelector.Builder()
     *                 .addControlCategory(MediaControlIntent.CATEGORY_LIVE_AUDIO)
     *                 .addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
     *                 .build();
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
     *     private final class MyCallback extends MediaRouter.Callback {
     *         // Implement callback methods as needed.
     *     }
     * }
     * </pre>
     *
     * @param selector
     * 		A route selector that indicates the kinds of routes that the
     * 		callback would like to discover.
     * @param callback
     * 		The callback to add.
     * @param flags
     * 		Flags to control the behavior of the callback.
     * 		May be zero or a combination of {@link #CALLBACK_FLAG_PERFORM_ACTIVE_SCAN} and
     * 		{@link #CALLBACK_FLAG_UNFILTERED_EVENTS}.
     * @see #removeCallback
     */
    public void addCallback(@android.support.annotation.NonNull
    android.support.v7.media.MediaRouteSelector selector, @android.support.annotation.NonNull
    android.support.v7.media.MediaRouter.Callback callback, @android.support.v7.media.MediaRouter.CallbackFlags
    int flags) {
        if (selector == null) {
            throw new java.lang.IllegalArgumentException("selector must not be null");
        }
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback must not be null");
        }
        android.support.v7.media.MediaRouter.checkCallingThread();
        if (android.support.v7.media.MediaRouter.DEBUG) {
            android.util.Log.d(android.support.v7.media.MediaRouter.TAG, (((("addCallback: selector=" + selector) + ", callback=") + callback) + ", flags=") + java.lang.Integer.toHexString(flags));
        }
        android.support.v7.media.MediaRouter.CallbackRecord record;
        int index = findCallbackRecord(callback);
        if (index < 0) {
            record = new android.support.v7.media.MediaRouter.CallbackRecord(this, callback);
            mCallbackRecords.add(record);
        } else {
            record = mCallbackRecords.get(index);
        }
        boolean updateNeeded = false;
        if ((flags & (~record.mFlags)) != 0) {
            record.mFlags |= flags;
            updateNeeded = true;
        }
        if (!record.mSelector.contains(selector)) {
            record.mSelector = new android.support.v7.media.MediaRouteSelector.Builder(record.mSelector).addSelector(selector).build();
            updateNeeded = true;
        }
        if (updateNeeded) {
            android.support.v7.media.MediaRouter.sGlobal.updateDiscoveryRequest();
        }
    }

    /**
     * Removes the specified callback.  It will no longer receive events about
     * changes to media routes.
     *
     * @param callback
     * 		The callback to remove.
     * @see #addCallback
     */
    public void removeCallback(@android.support.annotation.NonNull
    android.support.v7.media.MediaRouter.Callback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback must not be null");
        }
        android.support.v7.media.MediaRouter.checkCallingThread();
        if (android.support.v7.media.MediaRouter.DEBUG) {
            android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "removeCallback: callback=" + callback);
        }
        int index = findCallbackRecord(callback);
        if (index >= 0) {
            mCallbackRecords.remove(index);
            android.support.v7.media.MediaRouter.sGlobal.updateDiscoveryRequest();
        }
    }

    private int findCallbackRecord(android.support.v7.media.MediaRouter.Callback callback) {
        final int count = mCallbackRecords.size();
        for (int i = 0; i < count; i++) {
            if (mCallbackRecords.get(i).mCallback == callback) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Registers a media route provider within this application process.
     * <p>
     * The provider will be added to the list of providers that all {@link MediaRouter}
     * instances within this process can use to discover routes.
     * </p>
     *
     * @param providerInstance
     * 		The media route provider instance to add.
     * @see MediaRouteProvider
     * @see #removeCallback
     */
    public void addProvider(@android.support.annotation.NonNull
    android.support.v7.media.MediaRouteProvider providerInstance) {
        if (providerInstance == null) {
            throw new java.lang.IllegalArgumentException("providerInstance must not be null");
        }
        android.support.v7.media.MediaRouter.checkCallingThread();
        if (android.support.v7.media.MediaRouter.DEBUG) {
            android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "addProvider: " + providerInstance);
        }
        android.support.v7.media.MediaRouter.sGlobal.addProvider(providerInstance);
    }

    /**
     * Unregisters a media route provider within this application process.
     * <p>
     * The provider will be removed from the list of providers that all {@link MediaRouter}
     * instances within this process can use to discover routes.
     * </p>
     *
     * @param providerInstance
     * 		The media route provider instance to remove.
     * @see MediaRouteProvider
     * @see #addCallback
     */
    public void removeProvider(@android.support.annotation.NonNull
    android.support.v7.media.MediaRouteProvider providerInstance) {
        if (providerInstance == null) {
            throw new java.lang.IllegalArgumentException("providerInstance must not be null");
        }
        android.support.v7.media.MediaRouter.checkCallingThread();
        if (android.support.v7.media.MediaRouter.DEBUG) {
            android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "removeProvider: " + providerInstance);
        }
        android.support.v7.media.MediaRouter.sGlobal.removeProvider(providerInstance);
    }

    /**
     * Adds a remote control client to enable remote control of the volume
     * of the selected route.
     * <p>
     * The remote control client must have previously been registered with
     * the audio manager using the {@link android.media.AudioManager#registerRemoteControlClient
     * AudioManager.registerRemoteControlClient} method.
     * </p>
     *
     * @param remoteControlClient
     * 		The {@link android.media.RemoteControlClient} to register.
     */
    public void addRemoteControlClient(@android.support.annotation.NonNull
    java.lang.Object remoteControlClient) {
        if (remoteControlClient == null) {
            throw new java.lang.IllegalArgumentException("remoteControlClient must not be null");
        }
        android.support.v7.media.MediaRouter.checkCallingThread();
        if (android.support.v7.media.MediaRouter.DEBUG) {
            android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "addRemoteControlClient: " + remoteControlClient);
        }
        android.support.v7.media.MediaRouter.sGlobal.addRemoteControlClient(remoteControlClient);
    }

    /**
     * Removes a remote control client.
     *
     * @param remoteControlClient
     * 		The {@link android.media.RemoteControlClient}
     * 		to unregister.
     */
    public void removeRemoteControlClient(@android.support.annotation.NonNull
    java.lang.Object remoteControlClient) {
        if (remoteControlClient == null) {
            throw new java.lang.IllegalArgumentException("remoteControlClient must not be null");
        }
        if (android.support.v7.media.MediaRouter.DEBUG) {
            android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "removeRemoteControlClient: " + remoteControlClient);
        }
        android.support.v7.media.MediaRouter.sGlobal.removeRemoteControlClient(remoteControlClient);
    }

    /**
     * Sets the media session to enable remote control of the volume of the
     * selected route. This should be used instead of
     * {@link #addRemoteControlClient} when using media sessions. Set the
     * session to null to clear it.
     *
     * @param mediaSession
     * 		The {@link android.media.session.MediaSession} to
     * 		use.
     */
    public void setMediaSession(java.lang.Object mediaSession) {
        if (android.support.v7.media.MediaRouter.DEBUG) {
            android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "addMediaSession: " + mediaSession);
        }
        android.support.v7.media.MediaRouter.sGlobal.setMediaSession(mediaSession);
    }

    /**
     * Sets a compat media session to enable remote control of the volume of the
     * selected route. This should be used instead of
     * {@link #addRemoteControlClient} when using {@link MediaSessionCompat}.
     * Set the session to null to clear it.
     *
     * @param mediaSession
     * 		
     */
    public void setMediaSessionCompat(android.support.v4.media.session.MediaSessionCompat mediaSession) {
        if (android.support.v7.media.MediaRouter.DEBUG) {
            android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "addMediaSessionCompat: " + mediaSession);
        }
        android.support.v7.media.MediaRouter.sGlobal.setMediaSessionCompat(mediaSession);
    }

    public android.support.v4.media.session.MediaSessionCompat.Token getMediaSessionToken() {
        return android.support.v7.media.MediaRouter.sGlobal.getMediaSessionToken();
    }

    /**
     * Ensures that calls into the media router are on the correct thread.
     * It pays to be a little paranoid when global state invariants are at risk.
     */
    static void checkCallingThread() {
        if (android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
            throw new java.lang.IllegalStateException("The media router service must only be " + "accessed on the application's main thread.");
        }
    }

    static <T> boolean equal(T a, T b) {
        return (a == b) || (((a != null) && (b != null)) && a.equals(b));
    }

    /**
     * Provides information about a media route.
     * <p>
     * Each media route has a list of {@link MediaControlIntent media control}
     * {@link #getControlFilters intent filters} that describe the capabilities of the
     * route and the manner in which it is used and controlled.
     * </p>
     */
    public static class RouteInfo {
        private final android.support.v7.media.MediaRouter.ProviderInfo mProvider;

        private final java.lang.String mDescriptorId;

        private final java.lang.String mUniqueId;

        private java.lang.String mName;

        private java.lang.String mDescription;

        private android.net.Uri mIconUri;

        private boolean mEnabled;

        private boolean mConnecting;

        private int mConnectionState;

        private boolean mCanDisconnect;

        private final java.util.ArrayList<android.content.IntentFilter> mControlFilters = new java.util.ArrayList<>();

        private int mPlaybackType;

        private int mPlaybackStream;

        private int mDeviceType;

        private int mVolumeHandling;

        private int mVolume;

        private int mVolumeMax;

        private android.view.Display mPresentationDisplay;

        private int mPresentationDisplayId = android.support.v7.media.MediaRouter.RouteInfo.PRESENTATION_DISPLAY_ID_NONE;

        private android.os.Bundle mExtras;

        private android.content.IntentSender mSettingsIntent;

        android.support.v7.media.MediaRouteDescriptor mDescriptor;

        @android.support.annotation.IntDef({ android.support.v7.media.MediaRouter.RouteInfo.CONNECTION_STATE_DISCONNECTED, android.support.v7.media.MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTING, android.support.v7.media.MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTED })
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        private @interface ConnectionState {}

        /**
         * The default connection state indicating the route is disconnected.
         *
         * @see #getConnectionState
         */
        public static final int CONNECTION_STATE_DISCONNECTED = 0;

        /**
         * A connection state indicating the route is in the process of connecting and is not yet
         * ready for use.
         *
         * @see #getConnectionState
         */
        public static final int CONNECTION_STATE_CONNECTING = 1;

        /**
         * A connection state indicating the route is connected.
         *
         * @see #getConnectionState
         */
        public static final int CONNECTION_STATE_CONNECTED = 2;

        @android.support.annotation.IntDef({ android.support.v7.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_LOCAL, android.support.v7.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE })
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        private @interface PlaybackType {}

        /**
         * The default playback type, "local", indicating the presentation of the media
         * is happening on the same device (e.g. a phone, a tablet) as where it is
         * controlled from.
         *
         * @see #getPlaybackType
         */
        public static final int PLAYBACK_TYPE_LOCAL = 0;

        /**
         * A playback type indicating the presentation of the media is happening on
         * a different device (i.e. the remote device) than where it is controlled from.
         *
         * @see #getPlaybackType
         */
        public static final int PLAYBACK_TYPE_REMOTE = 1;

        @android.support.annotation.IntDef({ android.support.v7.media.MediaRouter.RouteInfo.DEVICE_TYPE_UNKNOWN, android.support.v7.media.MediaRouter.RouteInfo.DEVICE_TYPE_TV, android.support.v7.media.MediaRouter.RouteInfo.DEVICE_TYPE_SPEAKER, android.support.v7.media.MediaRouter.RouteInfo.DEVICE_TYPE_BLUETOOTH })
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        private @interface DeviceType {}

        /**
         * The default receiver device type of the route indicating the type is unknown.
         *
         * @see #getDeviceType
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public static final int DEVICE_TYPE_UNKNOWN = 0;

        /**
         * A receiver device type of the route indicating the presentation of the media is happening
         * on a TV.
         *
         * @see #getDeviceType
         */
        public static final int DEVICE_TYPE_TV = 1;

        /**
         * A receiver device type of the route indicating the presentation of the media is happening
         * on a speaker.
         *
         * @see #getDeviceType
         */
        public static final int DEVICE_TYPE_SPEAKER = 2;

        /**
         * A receiver device type of the route indicating the presentation of the media is happening
         * on a bluetooth device such as a bluetooth speaker.
         *
         * @see #getDeviceType
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public static final int DEVICE_TYPE_BLUETOOTH = 3;

        @android.support.annotation.IntDef({ android.support.v7.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED, android.support.v7.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE })
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        private @interface PlaybackVolume {}

        /**
         * Playback information indicating the playback volume is fixed, i.e. it cannot be
         * controlled from this object. An example of fixed playback volume is a remote player,
         * playing over HDMI where the user prefers to control the volume on the HDMI sink, rather
         * than attenuate at the source.
         *
         * @see #getVolumeHandling
         */
        public static final int PLAYBACK_VOLUME_FIXED = 0;

        /**
         * Playback information indicating the playback volume is variable and can be controlled
         * from this object.
         *
         * @see #getVolumeHandling
         */
        public static final int PLAYBACK_VOLUME_VARIABLE = 1;

        /**
         * The default presentation display id indicating no presentation display is associated
         * with the route.
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public static final int PRESENTATION_DISPLAY_ID_NONE = -1;

        static final int CHANGE_GENERAL = 1 << 0;

        static final int CHANGE_VOLUME = 1 << 1;

        static final int CHANGE_PRESENTATION_DISPLAY = 1 << 2;

        // Should match to SystemMediaRouteProvider.PACKAGE_NAME.
        static final java.lang.String SYSTEM_MEDIA_ROUTE_PROVIDER_PACKAGE_NAME = "android";

        RouteInfo(android.support.v7.media.MediaRouter.ProviderInfo provider, java.lang.String descriptorId, java.lang.String uniqueId) {
            mProvider = provider;
            mDescriptorId = descriptorId;
            mUniqueId = uniqueId;
        }

        /**
         * Gets information about the provider of this media route.
         */
        public android.support.v7.media.MediaRouter.ProviderInfo getProvider() {
            return mProvider;
        }

        /**
         * Gets the unique id of the route.
         * <p>
         * The route unique id functions as a stable identifier by which the route is known.
         * For example, an application can use this id as a token to remember the
         * selected route across restarts or to communicate its identity to a service.
         * </p>
         *
         * @return The unique id of the route, never null.
         */
        @android.support.annotation.NonNull
        public java.lang.String getId() {
            return mUniqueId;
        }

        /**
         * Gets the user-visible name of the route.
         * <p>
         * The route name identifies the destination represented by the route.
         * It may be a user-supplied name, an alias, or device serial number.
         * </p>
         *
         * @return The user-visible name of a media route.  This is the string presented
        to users who may select this as the active route.
         */
        public java.lang.String getName() {
            return mName;
        }

        /**
         * Gets the user-visible description of the route.
         * <p>
         * The route description describes the kind of destination represented by the route.
         * It may be a user-supplied string, a model number or brand of device.
         * </p>
         *
         * @return The description of the route, or null if none.
         */
        @android.support.annotation.Nullable
        public java.lang.String getDescription() {
            return mDescription;
        }

        /**
         * Gets the URI of the icon representing this route.
         * <p>
         * This icon will be used in picker UIs if available.
         * </p>
         *
         * @return The URI of the icon representing this route, or null if none.
         */
        public android.net.Uri getIconUri() {
            return mIconUri;
        }

        /**
         * Returns true if this route is enabled and may be selected.
         *
         * @return True if this route is enabled.
         */
        public boolean isEnabled() {
            return mEnabled;
        }

        /**
         * Returns true if the route is in the process of connecting and is not
         * yet ready for use.
         *
         * @return True if this route is in the process of connecting.
         */
        public boolean isConnecting() {
            return mConnecting;
        }

        /**
         * Gets the connection state of the route.
         *
         * @return The connection state of this route: {@link #CONNECTION_STATE_DISCONNECTED},
        {@link #CONNECTION_STATE_CONNECTING}, or {@link #CONNECTION_STATE_CONNECTED}.
         */
        @android.support.v7.media.MediaRouter.RouteInfo.ConnectionState
        public int getConnectionState() {
            return mConnectionState;
        }

        /**
         * Returns true if this route is currently selected.
         *
         * @return True if this route is currently selected.
         * @see MediaRouter#getSelectedRoute
         */
        public boolean isSelected() {
            android.support.v7.media.MediaRouter.checkCallingThread();
            return android.support.v7.media.MediaRouter.sGlobal.getSelectedRoute() == this;
        }

        /**
         * Returns true if this route is the default route.
         *
         * @return True if this route is the default route.
         * @see MediaRouter#getDefaultRoute
         */
        public boolean isDefault() {
            android.support.v7.media.MediaRouter.checkCallingThread();
            return android.support.v7.media.MediaRouter.sGlobal.getDefaultRoute() == this;
        }

        /**
         * Gets a list of {@link MediaControlIntent media control intent} filters that
         * describe the capabilities of this route and the media control actions that
         * it supports.
         *
         * @return A list of intent filters that specifies the media control intents that
        this route supports.
         * @see MediaControlIntent
         * @see #supportsControlCategory
         * @see #supportsControlRequest
         */
        public java.util.List<android.content.IntentFilter> getControlFilters() {
            return mControlFilters;
        }

        /**
         * Returns true if the route supports at least one of the capabilities
         * described by a media route selector.
         *
         * @param selector
         * 		The selector that specifies the capabilities to check.
         * @return True if the route supports at least one of the capabilities
        described in the media route selector.
         */
        public boolean matchesSelector(@android.support.annotation.NonNull
        android.support.v7.media.MediaRouteSelector selector) {
            if (selector == null) {
                throw new java.lang.IllegalArgumentException("selector must not be null");
            }
            android.support.v7.media.MediaRouter.checkCallingThread();
            return selector.matchesControlFilters(mControlFilters);
        }

        /**
         * Returns true if the route supports the specified
         * {@link MediaControlIntent media control} category.
         * <p>
         * Media control categories describe the capabilities of this route
         * such as whether it supports live audio streaming or remote playback.
         * </p>
         *
         * @param category
         * 		A {@link MediaControlIntent media control} category
         * 		such as {@link MediaControlIntent#CATEGORY_LIVE_AUDIO},
         * 		{@link MediaControlIntent#CATEGORY_LIVE_VIDEO},
         * 		{@link MediaControlIntent#CATEGORY_REMOTE_PLAYBACK}, or a provider-defined
         * 		media control category.
         * @return True if the route supports the specified intent category.
         * @see MediaControlIntent
         * @see #getControlFilters
         */
        public boolean supportsControlCategory(@android.support.annotation.NonNull
        java.lang.String category) {
            if (category == null) {
                throw new java.lang.IllegalArgumentException("category must not be null");
            }
            android.support.v7.media.MediaRouter.checkCallingThread();
            int count = mControlFilters.size();
            for (int i = 0; i < count; i++) {
                if (mControlFilters.get(i).hasCategory(category)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns true if the route supports the specified
         * {@link MediaControlIntent media control} category and action.
         * <p>
         * Media control actions describe specific requests that an application
         * can ask a route to perform.
         * </p>
         *
         * @param category
         * 		A {@link MediaControlIntent media control} category
         * 		such as {@link MediaControlIntent#CATEGORY_LIVE_AUDIO},
         * 		{@link MediaControlIntent#CATEGORY_LIVE_VIDEO},
         * 		{@link MediaControlIntent#CATEGORY_REMOTE_PLAYBACK}, or a provider-defined
         * 		media control category.
         * @param action
         * 		A {@link MediaControlIntent media control} action
         * 		such as {@link MediaControlIntent#ACTION_PLAY}.
         * @return True if the route supports the specified intent action.
         * @see MediaControlIntent
         * @see #getControlFilters
         */
        public boolean supportsControlAction(@android.support.annotation.NonNull
        java.lang.String category, @android.support.annotation.NonNull
        java.lang.String action) {
            if (category == null) {
                throw new java.lang.IllegalArgumentException("category must not be null");
            }
            if (action == null) {
                throw new java.lang.IllegalArgumentException("action must not be null");
            }
            android.support.v7.media.MediaRouter.checkCallingThread();
            int count = mControlFilters.size();
            for (int i = 0; i < count; i++) {
                android.content.IntentFilter filter = mControlFilters.get(i);
                if (filter.hasCategory(category) && filter.hasAction(action)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns true if the route supports the specified
         * {@link MediaControlIntent media control} request.
         * <p>
         * Media control requests are used to request the route to perform
         * actions such as starting remote playback of a media item.
         * </p>
         *
         * @param intent
         * 		A {@link MediaControlIntent media control intent}.
         * @return True if the route can handle the specified intent.
         * @see MediaControlIntent
         * @see #getControlFilters
         */
        public boolean supportsControlRequest(@android.support.annotation.NonNull
        android.content.Intent intent) {
            if (intent == null) {
                throw new java.lang.IllegalArgumentException("intent must not be null");
            }
            android.support.v7.media.MediaRouter.checkCallingThread();
            android.content.ContentResolver contentResolver = android.support.v7.media.MediaRouter.sGlobal.getContentResolver();
            int count = mControlFilters.size();
            for (int i = 0; i < count; i++) {
                if (mControlFilters.get(i).match(contentResolver, intent, true, android.support.v7.media.MediaRouter.TAG) >= 0) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Sends a {@link MediaControlIntent media control} request to be performed
         * asynchronously by the route's destination.
         * <p>
         * Media control requests are used to request the route to perform
         * actions such as starting remote playback of a media item.
         * </p><p>
         * This function may only be called on a selected route.  Control requests
         * sent to unselected routes will fail.
         * </p>
         *
         * @param intent
         * 		A {@link MediaControlIntent media control intent}.
         * @param callback
         * 		A {@link ControlRequestCallback} to invoke with the result
         * 		of the request, or null if no result is required.
         * @see MediaControlIntent
         */
        public void sendControlRequest(@android.support.annotation.NonNull
        android.content.Intent intent, @android.support.annotation.Nullable
        android.support.v7.media.MediaRouter.ControlRequestCallback callback) {
            if (intent == null) {
                throw new java.lang.IllegalArgumentException("intent must not be null");
            }
            android.support.v7.media.MediaRouter.checkCallingThread();
            android.support.v7.media.MediaRouter.sGlobal.sendControlRequest(this, intent, callback);
        }

        /**
         * Gets the type of playback associated with this route.
         *
         * @return The type of playback associated with this route: {@link #PLAYBACK_TYPE_LOCAL}
        or {@link #PLAYBACK_TYPE_REMOTE}.
         */
        @android.support.v7.media.MediaRouter.RouteInfo.PlaybackType
        public int getPlaybackType() {
            return mPlaybackType;
        }

        /**
         * Gets the audio stream over which the playback associated with this route is performed.
         *
         * @return The stream over which the playback associated with this route is performed.
         */
        public int getPlaybackStream() {
            return mPlaybackStream;
        }

        /**
         * Gets the type of the receiver device associated with this route.
         *
         * @return The type of the receiver device associated with this route:
        {@link #DEVICE_TYPE_TV} or {@link #DEVICE_TYPE_SPEAKER}.
         */
        public int getDeviceType() {
            return mDeviceType;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public boolean isDefaultOrBluetooth() {
            if (isDefault() || (mDeviceType == android.support.v7.media.MediaRouter.RouteInfo.DEVICE_TYPE_BLUETOOTH)) {
                return true;
            }
            // This is a workaround for platform version 23 or below where the system route
            // provider doesn't specify device type for bluetooth media routes.
            return (android.support.v7.media.MediaRouter.RouteInfo.isSystemMediaRouteProvider(this) && supportsControlCategory(android.support.v7.media.MediaControlIntent.CATEGORY_LIVE_AUDIO)) && (!supportsControlCategory(android.support.v7.media.MediaControlIntent.CATEGORY_LIVE_VIDEO));
        }

        private static boolean isSystemMediaRouteProvider(android.support.v7.media.MediaRouter.RouteInfo route) {
            return android.text.TextUtils.equals(route.getProviderInstance().getMetadata().getPackageName(), android.support.v7.media.MediaRouter.RouteInfo.SYSTEM_MEDIA_ROUTE_PROVIDER_PACKAGE_NAME);
        }

        /**
         * Gets information about how volume is handled on the route.
         *
         * @return How volume is handled on the route: {@link #PLAYBACK_VOLUME_FIXED}
        or {@link #PLAYBACK_VOLUME_VARIABLE}.
         */
        @android.support.v7.media.MediaRouter.RouteInfo.PlaybackVolume
        public int getVolumeHandling() {
            return mVolumeHandling;
        }

        /**
         * Gets the current volume for this route. Depending on the route, this may only
         * be valid if the route is currently selected.
         *
         * @return The volume at which the playback associated with this route is performed.
         */
        public int getVolume() {
            return mVolume;
        }

        /**
         * Gets the maximum volume at which the playback associated with this route is performed.
         *
         * @return The maximum volume at which the playback associated with
        this route is performed.
         */
        public int getVolumeMax() {
            return mVolumeMax;
        }

        /**
         * Gets whether this route supports disconnecting without interrupting
         * playback.
         *
         * @return True if this route can disconnect without stopping playback,
        false otherwise.
         */
        public boolean canDisconnect() {
            return mCanDisconnect;
        }

        /**
         * Requests a volume change for this route asynchronously.
         * <p>
         * This function may only be called on a selected route.  It will have
         * no effect if the route is currently unselected.
         * </p>
         *
         * @param volume
         * 		The new volume value between 0 and {@link #getVolumeMax}.
         */
        public void requestSetVolume(int volume) {
            android.support.v7.media.MediaRouter.checkCallingThread();
            android.support.v7.media.MediaRouter.sGlobal.requestSetVolume(this, java.lang.Math.min(mVolumeMax, java.lang.Math.max(0, volume)));
        }

        /**
         * Requests an incremental volume update for this route asynchronously.
         * <p>
         * This function may only be called on a selected route.  It will have
         * no effect if the route is currently unselected.
         * </p>
         *
         * @param delta
         * 		The delta to add to the current volume.
         */
        public void requestUpdateVolume(int delta) {
            android.support.v7.media.MediaRouter.checkCallingThread();
            if (delta != 0) {
                android.support.v7.media.MediaRouter.sGlobal.requestUpdateVolume(this, delta);
            }
        }

        /**
         * Gets the {@link Display} that should be used by the application to show
         * a {@link android.app.Presentation} on an external display when this route is selected.
         * Depending on the route, this may only be valid if the route is currently
         * selected.
         * <p>
         * The preferred presentation display may change independently of the route
         * being selected or unselected.  For example, the presentation display
         * of the default system route may change when an external HDMI display is connected
         * or disconnected even though the route itself has not changed.
         * </p><p>
         * This method may return null if there is no external display associated with
         * the route or if the display is not ready to show UI yet.
         * </p><p>
         * The application should listen for changes to the presentation display
         * using the {@link Callback#onRoutePresentationDisplayChanged} callback and
         * show or dismiss its {@link android.app.Presentation} accordingly when the display
         * becomes available or is removed.
         * </p><p>
         * This method only makes sense for
         * {@link MediaControlIntent#CATEGORY_LIVE_VIDEO live video} routes.
         * </p>
         *
         * @return The preferred presentation display to use when this route is
        selected or null if none.
         * @see MediaControlIntent#CATEGORY_LIVE_VIDEO
         * @see android.app.Presentation
         */
        @android.support.annotation.Nullable
        public android.view.Display getPresentationDisplay() {
            android.support.v7.media.MediaRouter.checkCallingThread();
            if ((mPresentationDisplayId >= 0) && (mPresentationDisplay == null)) {
                mPresentationDisplay = android.support.v7.media.MediaRouter.sGlobal.getDisplay(mPresentationDisplayId);
            }
            return mPresentationDisplay;
        }

        /**
         * Gets the route's presentation display id, or -1 if none.
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public int getPresentationDisplayId() {
            return mPresentationDisplayId;
        }

        /**
         * Gets a collection of extra properties about this route that were supplied
         * by its media route provider, or null if none.
         */
        @android.support.annotation.Nullable
        public android.os.Bundle getExtras() {
            return mExtras;
        }

        /**
         * Gets an intent sender for launching a settings activity for this
         * route.
         */
        @android.support.annotation.Nullable
        public android.content.IntentSender getSettingsIntent() {
            return mSettingsIntent;
        }

        /**
         * Selects this media route.
         */
        public void select() {
            android.support.v7.media.MediaRouter.checkCallingThread();
            android.support.v7.media.MediaRouter.sGlobal.selectRoute(this);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((((((((((((((((((((((((((((("MediaRouter.RouteInfo{ uniqueId=" + mUniqueId) + ", name=") + mName) + ", description=") + mDescription) + ", iconUri=") + mIconUri) + ", enabled=") + mEnabled) + ", connecting=") + mConnecting) + ", connectionState=") + mConnectionState) + ", canDisconnect=") + mCanDisconnect) + ", playbackType=") + mPlaybackType) + ", playbackStream=") + mPlaybackStream) + ", deviceType=") + mDeviceType) + ", volumeHandling=") + mVolumeHandling) + ", volume=") + mVolume) + ", volumeMax=") + mVolumeMax) + ", presentationDisplayId=") + mPresentationDisplayId) + ", extras=") + mExtras) + ", settingsIntent=") + mSettingsIntent) + ", providerPackageName=") + mProvider.getPackageName()) + " }";
        }

        int maybeUpdateDescriptor(android.support.v7.media.MediaRouteDescriptor descriptor) {
            int changes = 0;
            if (mDescriptor != descriptor) {
                changes = updateDescriptor(descriptor);
            }
            return changes;
        }

        int updateDescriptor(android.support.v7.media.MediaRouteDescriptor descriptor) {
            int changes = 0;
            mDescriptor = descriptor;
            if (descriptor != null) {
                if (!android.support.v7.media.MediaRouter.equal(mName, descriptor.getName())) {
                    mName = descriptor.getName();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (!android.support.v7.media.MediaRouter.equal(mDescription, descriptor.getDescription())) {
                    mDescription = descriptor.getDescription();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (!android.support.v7.media.MediaRouter.equal(mIconUri, descriptor.getIconUri())) {
                    mIconUri = descriptor.getIconUri();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (mEnabled != descriptor.isEnabled()) {
                    mEnabled = descriptor.isEnabled();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (mConnecting != descriptor.isConnecting()) {
                    mConnecting = descriptor.isConnecting();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (mConnectionState != descriptor.getConnectionState()) {
                    mConnectionState = descriptor.getConnectionState();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (!mControlFilters.equals(descriptor.getControlFilters())) {
                    mControlFilters.clear();
                    mControlFilters.addAll(descriptor.getControlFilters());
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (mPlaybackType != descriptor.getPlaybackType()) {
                    mPlaybackType = descriptor.getPlaybackType();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (mPlaybackStream != descriptor.getPlaybackStream()) {
                    mPlaybackStream = descriptor.getPlaybackStream();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (mDeviceType != descriptor.getDeviceType()) {
                    mDeviceType = descriptor.getDeviceType();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (mVolumeHandling != descriptor.getVolumeHandling()) {
                    mVolumeHandling = descriptor.getVolumeHandling();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL | android.support.v7.media.MediaRouter.RouteInfo.CHANGE_VOLUME;
                }
                if (mVolume != descriptor.getVolume()) {
                    mVolume = descriptor.getVolume();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL | android.support.v7.media.MediaRouter.RouteInfo.CHANGE_VOLUME;
                }
                if (mVolumeMax != descriptor.getVolumeMax()) {
                    mVolumeMax = descriptor.getVolumeMax();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL | android.support.v7.media.MediaRouter.RouteInfo.CHANGE_VOLUME;
                }
                if (mPresentationDisplayId != descriptor.getPresentationDisplayId()) {
                    mPresentationDisplayId = descriptor.getPresentationDisplayId();
                    mPresentationDisplay = null;
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL | android.support.v7.media.MediaRouter.RouteInfo.CHANGE_PRESENTATION_DISPLAY;
                }
                if (!android.support.v7.media.MediaRouter.equal(mExtras, descriptor.getExtras())) {
                    mExtras = descriptor.getExtras();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (!android.support.v7.media.MediaRouter.equal(mSettingsIntent, descriptor.getSettingsActivity())) {
                    mSettingsIntent = descriptor.getSettingsActivity();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL;
                }
                if (mCanDisconnect != descriptor.canDisconnectAndKeepPlaying()) {
                    mCanDisconnect = descriptor.canDisconnectAndKeepPlaying();
                    changes |= android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL | android.support.v7.media.MediaRouter.RouteInfo.CHANGE_PRESENTATION_DISPLAY;
                }
            }
            return changes;
        }

        java.lang.String getDescriptorId() {
            return mDescriptorId;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.support.v7.media.MediaRouteProvider getProviderInstance() {
            return mProvider.getProviderInstance();
        }
    }

    /**
     * Information about a route that consists of multiple other routes in a group.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static class RouteGroup extends android.support.v7.media.MediaRouter.RouteInfo {
        private java.util.List<android.support.v7.media.MediaRouter.RouteInfo> mRoutes = new java.util.ArrayList<>();

        RouteGroup(android.support.v7.media.MediaRouter.ProviderInfo provider, java.lang.String descriptorId, java.lang.String uniqueId) {
            super(provider, descriptorId, uniqueId);
        }

        /**
         *
         *
         * @return The number of routes in this group
         */
        public int getRouteCount() {
            return mRoutes.size();
        }

        /**
         * Returns the route in this group at the specified index
         *
         * @param index
         * 		Index to fetch
         * @return The route at index
         */
        public android.support.v7.media.MediaRouter.RouteInfo getRouteAt(int index) {
            return mRoutes.get(index);
        }

        /**
         * Returns the routes in this group
         *
         * @return The list of the routes in this group
         */
        public java.util.List<android.support.v7.media.MediaRouter.RouteInfo> getRoutes() {
            return mRoutes;
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(super.toString());
            sb.append('[');
            final int count = mRoutes.size();
            for (int i = 0; i < count; i++) {
                if (i > 0)
                    sb.append(", ");

                sb.append(mRoutes.get(i));
            }
            sb.append(']');
            return sb.toString();
        }

        @java.lang.Override
        int maybeUpdateDescriptor(android.support.v7.media.MediaRouteDescriptor descriptor) {
            boolean changed = false;
            if (mDescriptor != descriptor) {
                mDescriptor = descriptor;
                if (descriptor != null) {
                    java.util.List<java.lang.String> groupMemberIds = descriptor.getGroupMemberIds();
                    java.util.List<android.support.v7.media.MediaRouter.RouteInfo> routes = new java.util.ArrayList<>();
                    changed = groupMemberIds.size() != mRoutes.size();
                    for (java.lang.String groupMemberId : groupMemberIds) {
                        java.lang.String uniqueId = android.support.v7.media.MediaRouter.sGlobal.getUniqueId(getProvider(), groupMemberId);
                        android.support.v7.media.MediaRouter.RouteInfo groupMember = android.support.v7.media.MediaRouter.sGlobal.getRoute(uniqueId);
                        if (groupMember != null) {
                            routes.add(groupMember);
                            if ((!changed) && (!mRoutes.contains(groupMember))) {
                                changed = true;
                            }
                        }
                    }
                    if (changed) {
                        mRoutes = routes;
                    }
                }
            }
            return (changed ? android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL : 0) | super.updateDescriptor(descriptor);
        }
    }

    /**
     * Provides information about a media route provider.
     * <p>
     * This object may be used to determine which media route provider has
     * published a particular route.
     * </p>
     */
    public static final class ProviderInfo {
        private final android.support.v7.media.MediaRouteProvider mProviderInstance;

        private final java.util.List<android.support.v7.media.MediaRouter.RouteInfo> mRoutes = new java.util.ArrayList<>();

        private final android.support.v7.media.MediaRouteProvider.ProviderMetadata mMetadata;

        private android.support.v7.media.MediaRouteProviderDescriptor mDescriptor;

        private android.content.res.Resources mResources;

        private boolean mResourcesNotAvailable;

        ProviderInfo(android.support.v7.media.MediaRouteProvider provider) {
            mProviderInstance = provider;
            mMetadata = provider.getMetadata();
        }

        /**
         * Gets the provider's underlying {@link MediaRouteProvider} instance.
         */
        public android.support.v7.media.MediaRouteProvider getProviderInstance() {
            android.support.v7.media.MediaRouter.checkCallingThread();
            return mProviderInstance;
        }

        /**
         * Gets the package name of the media route provider.
         */
        public java.lang.String getPackageName() {
            return mMetadata.getPackageName();
        }

        /**
         * Gets the component name of the media route provider.
         */
        public android.content.ComponentName getComponentName() {
            return mMetadata.getComponentName();
        }

        /**
         * Gets the {@link MediaRouter.RouteInfo routes} published by this route provider.
         */
        public java.util.List<android.support.v7.media.MediaRouter.RouteInfo> getRoutes() {
            android.support.v7.media.MediaRouter.checkCallingThread();
            return mRoutes;
        }

        android.content.res.Resources getResources() {
            if ((mResources == null) && (!mResourcesNotAvailable)) {
                java.lang.String packageName = getPackageName();
                android.content.Context context = android.support.v7.media.MediaRouter.sGlobal.getProviderContext(packageName);
                if (context != null) {
                    mResources = context.getResources();
                } else {
                    android.util.Log.w(android.support.v7.media.MediaRouter.TAG, "Unable to obtain resources for route provider package: " + packageName);
                    mResourcesNotAvailable = true;
                }
            }
            return mResources;
        }

        boolean updateDescriptor(android.support.v7.media.MediaRouteProviderDescriptor descriptor) {
            if (mDescriptor != descriptor) {
                mDescriptor = descriptor;
                return true;
            }
            return false;
        }

        int findRouteByDescriptorId(java.lang.String id) {
            final int count = mRoutes.size();
            for (int i = 0; i < count; i++) {
                if (mRoutes.get(i).mDescriptorId.equals(id)) {
                    return i;
                }
            }
            return -1;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ("MediaRouter.RouteProviderInfo{ packageName=" + getPackageName()) + " }";
        }
    }

    /**
     * Interface for receiving events about media routing changes.
     * All methods of this interface will be called from the application's main thread.
     * <p>
     * A Callback will only receive events relevant to routes that the callback
     * was registered for unless the {@link MediaRouter#CALLBACK_FLAG_UNFILTERED_EVENTS}
     * flag was specified in {@link MediaRouter#addCallback(MediaRouteSelector, Callback, int)}.
     * </p>
     *
     * @see MediaRouter#addCallback(MediaRouteSelector, Callback, int)
     * @see MediaRouter#removeCallback(Callback)
     */
    public static abstract class Callback {
        /**
         * Called when the supplied media route becomes selected as the active route.
         *
         * @param router
         * 		The media router reporting the event.
         * @param route
         * 		The route that has been selected.
         */
        public void onRouteSelected(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
        }

        /**
         * Called when the supplied media route becomes unselected as the active route.
         * For detailed reason, override {@link #onRouteUnselected(MediaRouter, RouteInfo, int)}
         * instead.
         *
         * @param router
         * 		The media router reporting the event.
         * @param route
         * 		The route that has been unselected.
         */
        public void onRouteUnselected(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
        }

        /**
         * Called when the supplied media route becomes unselected as the active route.
         * The default implementation calls {@link #onRouteUnselected}.
         * <p>
         * The reason provided will be one of the following:
         * <ul>
         * <li>{@link MediaRouter#UNSELECT_REASON_UNKNOWN}</li>
         * <li>{@link MediaRouter#UNSELECT_REASON_DISCONNECTED}</li>
         * <li>{@link MediaRouter#UNSELECT_REASON_STOPPED}</li>
         * <li>{@link MediaRouter#UNSELECT_REASON_ROUTE_CHANGED}</li>
         * </ul>
         *
         * @param router
         * 		The media router reporting the event.
         * @param route
         * 		The route that has been unselected.
         * @param reason
         * 		The reason for unselecting the route.
         */
        public void onRouteUnselected(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route, int reason) {
            onRouteUnselected(router, route);
        }

        /**
         * Called when a media route has been added.
         *
         * @param router
         * 		The media router reporting the event.
         * @param route
         * 		The route that has become available for use.
         */
        public void onRouteAdded(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
        }

        /**
         * Called when a media route has been removed.
         *
         * @param router
         * 		The media router reporting the event.
         * @param route
         * 		The route that has been removed from availability.
         */
        public void onRouteRemoved(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
        }

        /**
         * Called when a property of the indicated media route has changed.
         *
         * @param router
         * 		The media router reporting the event.
         * @param route
         * 		The route that was changed.
         */
        public void onRouteChanged(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
        }

        /**
         * Called when a media route's volume changes.
         *
         * @param router
         * 		The media router reporting the event.
         * @param route
         * 		The route whose volume changed.
         */
        public void onRouteVolumeChanged(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
        }

        /**
         * Called when a media route's presentation display changes.
         * <p>
         * This method is called whenever the route's presentation display becomes
         * available, is removed or has changes to some of its properties (such as its size).
         * </p>
         *
         * @param router
         * 		The media router reporting the event.
         * @param route
         * 		The route whose presentation display changed.
         * @see RouteInfo#getPresentationDisplay()
         */
        public void onRoutePresentationDisplayChanged(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
        }

        /**
         * Called when a media route provider has been added.
         *
         * @param router
         * 		The media router reporting the event.
         * @param provider
         * 		The provider that has become available for use.
         */
        public void onProviderAdded(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.ProviderInfo provider) {
        }

        /**
         * Called when a media route provider has been removed.
         *
         * @param router
         * 		The media router reporting the event.
         * @param provider
         * 		The provider that has been removed from availability.
         */
        public void onProviderRemoved(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.ProviderInfo provider) {
        }

        /**
         * Called when a property of the indicated media route provider has changed.
         *
         * @param router
         * 		The media router reporting the event.
         * @param provider
         * 		The provider that was changed.
         */
        public void onProviderChanged(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.ProviderInfo provider) {
        }
    }

    /**
     * Callback which is invoked with the result of a media control request.
     *
     * @see RouteInfo#sendControlRequest
     */
    public static abstract class ControlRequestCallback {
        /**
         * Called when a media control request succeeds.
         *
         * @param data
         * 		Result data, or null if none.
         * 		Contents depend on the {@link MediaControlIntent media control action}.
         */
        public void onResult(android.os.Bundle data) {
        }

        /**
         * Called when a media control request fails.
         *
         * @param error
         * 		A localized error message which may be shown to the user, or null
         * 		if the cause of the error is unclear.
         * @param data
         * 		Error data, or null if none.
         * 		Contents depend on the {@link MediaControlIntent media control action}.
         */
        public void onError(java.lang.String error, android.os.Bundle data) {
        }
    }

    private static final class CallbackRecord {
        public final android.support.v7.media.MediaRouter mRouter;

        public final android.support.v7.media.MediaRouter.Callback mCallback;

        public android.support.v7.media.MediaRouteSelector mSelector;

        public int mFlags;

        public CallbackRecord(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.Callback callback) {
            mRouter = router;
            mCallback = callback;
            mSelector = android.support.v7.media.MediaRouteSelector.EMPTY;
        }

        public boolean filterRouteEvent(android.support.v7.media.MediaRouter.RouteInfo route) {
            return ((mFlags & android.support.v7.media.MediaRouter.CALLBACK_FLAG_UNFILTERED_EVENTS) != 0) || route.matchesSelector(mSelector);
        }
    }

    /**
     * Global state for the media router.
     * <p>
     * Media routes and media route providers are global to the process; their
     * state and the bulk of the media router implementation lives here.
     * </p>
     */
    private static final class GlobalMediaRouter implements android.support.v7.media.RegisteredMediaRouteProviderWatcher.Callback , android.support.v7.media.SystemMediaRouteProvider.SyncCallback {
        final android.content.Context mApplicationContext;

        final java.util.ArrayList<java.lang.ref.WeakReference<android.support.v7.media.MediaRouter>> mRouters = new java.util.ArrayList<>();

        private final java.util.ArrayList<android.support.v7.media.MediaRouter.RouteInfo> mRoutes = new java.util.ArrayList<>();

        private final java.util.Map<android.support.v4.util.Pair<java.lang.String, java.lang.String>, java.lang.String> mUniqueIdMap = new java.util.HashMap<>();

        private final java.util.ArrayList<android.support.v7.media.MediaRouter.ProviderInfo> mProviders = new java.util.ArrayList<>();

        private final java.util.ArrayList<android.support.v7.media.MediaRouter.GlobalMediaRouter.RemoteControlClientRecord> mRemoteControlClients = new java.util.ArrayList<>();

        final android.support.v7.media.RemoteControlClientCompat.PlaybackInfo mPlaybackInfo = new android.support.v7.media.RemoteControlClientCompat.PlaybackInfo();

        private final android.support.v7.media.MediaRouter.GlobalMediaRouter.ProviderCallback mProviderCallback = new android.support.v7.media.MediaRouter.GlobalMediaRouter.ProviderCallback();

        final android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler mCallbackHandler = new android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler();

        private final android.support.v4.hardware.display.DisplayManagerCompat mDisplayManager;

        final android.support.v7.media.SystemMediaRouteProvider mSystemProvider;

        private final boolean mLowRam;

        private android.support.v7.media.RegisteredMediaRouteProviderWatcher mRegisteredProviderWatcher;

        private android.support.v7.media.MediaRouter.RouteInfo mDefaultRoute;

        android.support.v7.media.MediaRouter.RouteInfo mSelectedRoute;

        private android.support.v7.media.MediaRouteProvider.RouteController mSelectedRouteController;

        // A map from route descriptor ID to RouteController for the member routes in the currently
        // selected route group.
        private final java.util.Map<java.lang.String, android.support.v7.media.MediaRouteProvider.RouteController> mRouteControllerMap = new java.util.HashMap<>();

        private android.support.v7.media.MediaRouteDiscoveryRequest mDiscoveryRequest;

        private android.support.v7.media.MediaRouter.GlobalMediaRouter.MediaSessionRecord mMediaSession;

        android.support.v4.media.session.MediaSessionCompat mRccMediaSession;

        private android.support.v4.media.session.MediaSessionCompat mCompatSession;

        private android.support.v4.media.session.MediaSessionCompat.OnActiveChangeListener mSessionActiveListener = new android.support.v4.media.session.MediaSessionCompat.OnActiveChangeListener() {
            @java.lang.Override
            public void onActiveChanged() {
                if (mRccMediaSession != null) {
                    if (mRccMediaSession.isActive()) {
                        addRemoteControlClient(mRccMediaSession.getRemoteControlClient());
                    } else {
                        removeRemoteControlClient(mRccMediaSession.getRemoteControlClient());
                    }
                }
            }
        };

        GlobalMediaRouter(android.content.Context applicationContext) {
            mApplicationContext = applicationContext;
            mDisplayManager = android.support.v4.hardware.display.DisplayManagerCompat.getInstance(applicationContext);
            mLowRam = android.support.v4.app.ActivityManagerCompat.isLowRamDevice(((android.app.ActivityManager) (applicationContext.getSystemService(android.content.Context.ACTIVITY_SERVICE))));
            // Add the system media route provider for interoperating with
            // the framework media router.  This one is special and receives
            // synchronization messages from the media router.
            mSystemProvider = android.support.v7.media.SystemMediaRouteProvider.obtain(applicationContext, this);
            addProvider(mSystemProvider);
        }

        public void start() {
            // Start watching for routes published by registered media route
            // provider services.
            mRegisteredProviderWatcher = new android.support.v7.media.RegisteredMediaRouteProviderWatcher(mApplicationContext, this);
            mRegisteredProviderWatcher.start();
        }

        public android.support.v7.media.MediaRouter getRouter(android.content.Context context) {
            android.support.v7.media.MediaRouter router;
            for (int i = mRouters.size(); (--i) >= 0;) {
                router = mRouters.get(i).get();
                if (router == null) {
                    mRouters.remove(i);
                } else
                    if (router.mContext == context) {
                        return router;
                    }

            }
            router = new android.support.v7.media.MediaRouter(context);
            mRouters.add(new java.lang.ref.WeakReference<android.support.v7.media.MediaRouter>(router));
            return router;
        }

        public android.content.ContentResolver getContentResolver() {
            return mApplicationContext.getContentResolver();
        }

        public android.content.Context getProviderContext(java.lang.String packageName) {
            if (packageName.equals(android.support.v7.media.SystemMediaRouteProvider.PACKAGE_NAME)) {
                return mApplicationContext;
            }
            try {
                return mApplicationContext.createPackageContext(packageName, android.content.Context.CONTEXT_RESTRICTED);
            } catch (android.content.pm.PackageManager.NameNotFoundException ex) {
                return null;
            }
        }

        public android.view.Display getDisplay(int displayId) {
            return mDisplayManager.getDisplay(displayId);
        }

        public void sendControlRequest(android.support.v7.media.MediaRouter.RouteInfo route, android.content.Intent intent, android.support.v7.media.MediaRouter.ControlRequestCallback callback) {
            if ((route == mSelectedRoute) && (mSelectedRouteController != null)) {
                if (mSelectedRouteController.onControlRequest(intent, callback)) {
                    return;
                }
            }
            if (callback != null) {
                callback.onError(null, null);
            }
        }

        public void requestSetVolume(android.support.v7.media.MediaRouter.RouteInfo route, int volume) {
            if ((route == mSelectedRoute) && (mSelectedRouteController != null)) {
                mSelectedRouteController.onSetVolume(volume);
            } else
                if (!mRouteControllerMap.isEmpty()) {
                    android.support.v7.media.MediaRouteProvider.RouteController controller = mRouteControllerMap.get(route.mDescriptorId);
                    if (controller != null) {
                        controller.onSetVolume(volume);
                    }
                }

        }

        public void requestUpdateVolume(android.support.v7.media.MediaRouter.RouteInfo route, int delta) {
            if ((route == mSelectedRoute) && (mSelectedRouteController != null)) {
                mSelectedRouteController.onUpdateVolume(delta);
            }
        }

        public android.support.v7.media.MediaRouter.RouteInfo getRoute(java.lang.String uniqueId) {
            for (android.support.v7.media.MediaRouter.RouteInfo info : mRoutes) {
                if (info.mUniqueId.equals(uniqueId)) {
                    return info;
                }
            }
            return null;
        }

        public java.util.List<android.support.v7.media.MediaRouter.RouteInfo> getRoutes() {
            return mRoutes;
        }

        public java.util.List<android.support.v7.media.MediaRouter.ProviderInfo> getProviders() {
            return mProviders;
        }

        public android.support.v7.media.MediaRouter.RouteInfo getDefaultRoute() {
            if (mDefaultRoute == null) {
                // This should never happen once the media router has been fully
                // initialized but it is good to check for the error in case there
                // is a bug in provider initialization.
                throw new java.lang.IllegalStateException("There is no default route.  " + "The media router has not yet been fully initialized.");
            }
            return mDefaultRoute;
        }

        public android.support.v7.media.MediaRouter.RouteInfo getSelectedRoute() {
            if (mSelectedRoute == null) {
                // This should never happen once the media router has been fully
                // initialized but it is good to check for the error in case there
                // is a bug in provider initialization.
                throw new java.lang.IllegalStateException("There is no currently selected route.  " + "The media router has not yet been fully initialized.");
            }
            return mSelectedRoute;
        }

        public void selectRoute(android.support.v7.media.MediaRouter.RouteInfo route) {
            selectRoute(route, android.support.v7.media.MediaRouter.UNSELECT_REASON_ROUTE_CHANGED);
        }

        public void selectRoute(android.support.v7.media.MediaRouter.RouteInfo route, int unselectReason) {
            if (!mRoutes.contains(route)) {
                android.util.Log.w(android.support.v7.media.MediaRouter.TAG, "Ignoring attempt to select removed route: " + route);
                return;
            }
            if (!route.mEnabled) {
                android.util.Log.w(android.support.v7.media.MediaRouter.TAG, "Ignoring attempt to select disabled route: " + route);
                return;
            }
            setSelectedRouteInternal(route, unselectReason);
        }

        public boolean isRouteAvailable(android.support.v7.media.MediaRouteSelector selector, int flags) {
            if (selector.isEmpty()) {
                return false;
            }
            // On low-RAM devices, do not rely on actual discovery results unless asked to.
            if (((flags & android.support.v7.media.MediaRouter.AVAILABILITY_FLAG_REQUIRE_MATCH) == 0) && mLowRam) {
                return true;
            }
            // Check whether any existing routes match the selector.
            final int routeCount = mRoutes.size();
            for (int i = 0; i < routeCount; i++) {
                android.support.v7.media.MediaRouter.RouteInfo route = mRoutes.get(i);
                if (((flags & android.support.v7.media.MediaRouter.AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE) != 0) && route.isDefaultOrBluetooth()) {
                    continue;
                }
                if (route.matchesSelector(selector)) {
                    return true;
                }
            }
            // It doesn't look like we can find a matching route right now.
            return false;
        }

        public void updateDiscoveryRequest() {
            // Combine all of the callback selectors and active scan flags.
            boolean discover = false;
            boolean activeScan = false;
            android.support.v7.media.MediaRouteSelector.Builder builder = new android.support.v7.media.MediaRouteSelector.Builder();
            for (int i = mRouters.size(); (--i) >= 0;) {
                android.support.v7.media.MediaRouter router = mRouters.get(i).get();
                if (router == null) {
                    mRouters.remove(i);
                } else {
                    final int count = router.mCallbackRecords.size();
                    for (int j = 0; j < count; j++) {
                        android.support.v7.media.MediaRouter.CallbackRecord callback = router.mCallbackRecords.get(j);
                        builder.addSelector(callback.mSelector);
                        if ((callback.mFlags & android.support.v7.media.MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN) != 0) {
                            activeScan = true;
                            discover = true;// perform active scan implies request discovery

                        }
                        if ((callback.mFlags & android.support.v7.media.MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY) != 0) {
                            if (!mLowRam) {
                                discover = true;
                            }
                        }
                        if ((callback.mFlags & android.support.v7.media.MediaRouter.CALLBACK_FLAG_FORCE_DISCOVERY) != 0) {
                            discover = true;
                        }
                    }
                }
            }
            android.support.v7.media.MediaRouteSelector selector = (discover) ? builder.build() : android.support.v7.media.MediaRouteSelector.EMPTY;
            // Create a new discovery request.
            if (((mDiscoveryRequest != null) && mDiscoveryRequest.getSelector().equals(selector)) && (mDiscoveryRequest.isActiveScan() == activeScan)) {
                return;// no change

            }
            if (selector.isEmpty() && (!activeScan)) {
                // Discovery is not needed.
                if (mDiscoveryRequest == null) {
                    return;// no change

                }
                mDiscoveryRequest = null;
            } else {
                // Discovery is needed.
                mDiscoveryRequest = new android.support.v7.media.MediaRouteDiscoveryRequest(selector, activeScan);
            }
            if (android.support.v7.media.MediaRouter.DEBUG) {
                android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "Updated discovery request: " + mDiscoveryRequest);
            }
            if ((discover && (!activeScan)) && mLowRam) {
                android.util.Log.i(android.support.v7.media.MediaRouter.TAG, "Forcing passive route discovery on a low-RAM device, " + (("system performance may be affected.  Please consider using " + "CALLBACK_FLAG_REQUEST_DISCOVERY instead of ") + "CALLBACK_FLAG_FORCE_DISCOVERY."));
            }
            // Notify providers.
            final int providerCount = mProviders.size();
            for (int i = 0; i < providerCount; i++) {
                mProviders.get(i).mProviderInstance.setDiscoveryRequest(mDiscoveryRequest);
            }
        }

        @java.lang.Override
        public void addProvider(android.support.v7.media.MediaRouteProvider providerInstance) {
            int index = findProviderInfo(providerInstance);
            if (index < 0) {
                // 1. Add the provider to the list.
                android.support.v7.media.MediaRouter.ProviderInfo provider = new android.support.v7.media.MediaRouter.ProviderInfo(providerInstance);
                mProviders.add(provider);
                if (android.support.v7.media.MediaRouter.DEBUG) {
                    android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "Provider added: " + provider);
                }
                mCallbackHandler.post(android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_PROVIDER_ADDED, provider);
                // 2. Create the provider's contents.
                updateProviderContents(provider, providerInstance.getDescriptor());
                // 3. Register the provider callback.
                providerInstance.setCallback(mProviderCallback);
                // 4. Set the discovery request.
                providerInstance.setDiscoveryRequest(mDiscoveryRequest);
            }
        }

        @java.lang.Override
        public void removeProvider(android.support.v7.media.MediaRouteProvider providerInstance) {
            int index = findProviderInfo(providerInstance);
            if (index >= 0) {
                // 1. Unregister the provider callback.
                providerInstance.setCallback(null);
                // 2. Clear the discovery request.
                providerInstance.setDiscoveryRequest(null);
                // 3. Delete the provider's contents.
                android.support.v7.media.MediaRouter.ProviderInfo provider = mProviders.get(index);
                updateProviderContents(provider, null);
                // 4. Remove the provider from the list.
                if (android.support.v7.media.MediaRouter.DEBUG) {
                    android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "Provider removed: " + provider);
                }
                mCallbackHandler.post(android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_PROVIDER_REMOVED, provider);
                mProviders.remove(index);
            }
        }

        void updateProviderDescriptor(android.support.v7.media.MediaRouteProvider providerInstance, android.support.v7.media.MediaRouteProviderDescriptor descriptor) {
            int index = findProviderInfo(providerInstance);
            if (index >= 0) {
                // Update the provider's contents.
                android.support.v7.media.MediaRouter.ProviderInfo provider = mProviders.get(index);
                updateProviderContents(provider, descriptor);
            }
        }

        private int findProviderInfo(android.support.v7.media.MediaRouteProvider providerInstance) {
            final int count = mProviders.size();
            for (int i = 0; i < count; i++) {
                if (mProviders.get(i).mProviderInstance == providerInstance) {
                    return i;
                }
            }
            return -1;
        }

        private void updateProviderContents(android.support.v7.media.MediaRouter.ProviderInfo provider, android.support.v7.media.MediaRouteProviderDescriptor providerDescriptor) {
            if (provider.updateDescriptor(providerDescriptor)) {
                // Update all existing routes and reorder them to match
                // the order of their descriptors.
                int targetIndex = 0;
                boolean selectedRouteDescriptorChanged = false;
                if (providerDescriptor != null) {
                    if (providerDescriptor.isValid()) {
                        final java.util.List<android.support.v7.media.MediaRouteDescriptor> routeDescriptors = providerDescriptor.getRoutes();
                        final int routeCount = routeDescriptors.size();
                        // Updating route group's contents requires all member routes' information.
                        // Add the groups to the lists and update them later.
                        java.util.List<android.support.v4.util.Pair<android.support.v7.media.MediaRouter.RouteInfo, android.support.v7.media.MediaRouteDescriptor>> addedGroups = new java.util.ArrayList<>();
                        java.util.List<android.support.v4.util.Pair<android.support.v7.media.MediaRouter.RouteInfo, android.support.v7.media.MediaRouteDescriptor>> updatedGroups = new java.util.ArrayList<>();
                        for (int i = 0; i < routeCount; i++) {
                            final android.support.v7.media.MediaRouteDescriptor routeDescriptor = routeDescriptors.get(i);
                            final java.lang.String id = routeDescriptor.getId();
                            final int sourceIndex = provider.findRouteByDescriptorId(id);
                            if (sourceIndex < 0) {
                                // 1. Add the route to the list.
                                java.lang.String uniqueId = assignRouteUniqueId(provider, id);
                                boolean isGroup = routeDescriptor.getGroupMemberIds() != null;
                                android.support.v7.media.MediaRouter.RouteInfo route = (isGroup) ? new android.support.v7.media.MediaRouter.RouteGroup(provider, id, uniqueId) : new android.support.v7.media.MediaRouter.RouteInfo(provider, id, uniqueId);
                                provider.mRoutes.add(targetIndex++, route);
                                mRoutes.add(route);
                                // 2. Create the route's contents.
                                if (isGroup) {
                                    addedGroups.add(new android.support.v4.util.Pair(route, routeDescriptor));
                                } else {
                                    route.maybeUpdateDescriptor(routeDescriptor);
                                    // 3. Notify clients about addition.
                                    if (android.support.v7.media.MediaRouter.DEBUG) {
                                        android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "Route added: " + route);
                                    }
                                    mCallbackHandler.post(android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_ADDED, route);
                                }
                            } else
                                if (sourceIndex < targetIndex) {
                                    android.util.Log.w(android.support.v7.media.MediaRouter.TAG, "Ignoring route descriptor with duplicate id: " + routeDescriptor);
                                } else {
                                    // 1. Reorder the route within the list.
                                    android.support.v7.media.MediaRouter.RouteInfo route = provider.mRoutes.get(sourceIndex);
                                    java.util.Collections.swap(provider.mRoutes, sourceIndex, targetIndex++);
                                    // 2. Update the route's contents.
                                    if (route instanceof android.support.v7.media.MediaRouter.RouteGroup) {
                                        updatedGroups.add(new android.support.v4.util.Pair(route, routeDescriptor));
                                    } else {
                                        // 3. Notify clients about changes.
                                        if (updateRouteDescriptorAndNotify(route, routeDescriptor) != 0) {
                                            if (route == mSelectedRoute) {
                                                selectedRouteDescriptorChanged = true;
                                            }
                                        }
                                    }
                                }

                        }
                        // Update the new and/or existing groups.
                        for (android.support.v4.util.Pair<android.support.v7.media.MediaRouter.RouteInfo, android.support.v7.media.MediaRouteDescriptor> pair : addedGroups) {
                            android.support.v7.media.MediaRouter.RouteInfo route = pair.first;
                            route.maybeUpdateDescriptor(pair.second);
                            if (android.support.v7.media.MediaRouter.DEBUG) {
                                android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "Route added: " + route);
                            }
                            mCallbackHandler.post(android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_ADDED, route);
                        }
                        for (android.support.v4.util.Pair<android.support.v7.media.MediaRouter.RouteInfo, android.support.v7.media.MediaRouteDescriptor> pair : updatedGroups) {
                            android.support.v7.media.MediaRouter.RouteInfo route = pair.first;
                            if (updateRouteDescriptorAndNotify(route, pair.second) != 0) {
                                if (route == mSelectedRoute) {
                                    selectedRouteDescriptorChanged = true;
                                }
                            }
                        }
                    } else {
                        android.util.Log.w(android.support.v7.media.MediaRouter.TAG, "Ignoring invalid provider descriptor: " + providerDescriptor);
                    }
                }
                // Dispose all remaining routes that do not have matching descriptors.
                for (int i = provider.mRoutes.size() - 1; i >= targetIndex; i--) {
                    // 1. Delete the route's contents.
                    android.support.v7.media.MediaRouter.RouteInfo route = provider.mRoutes.get(i);
                    route.maybeUpdateDescriptor(null);
                    // 2. Remove the route from the list.
                    mRoutes.remove(route);
                }
                // Update the selected route if needed.
                updateSelectedRouteIfNeeded(selectedRouteDescriptorChanged);
                // Now notify clients about routes that were removed.
                // We do this after updating the selected route to ensure
                // that the framework media router observes the new route
                // selection before the removal since removing the currently
                // selected route may have side-effects.
                for (int i = provider.mRoutes.size() - 1; i >= targetIndex; i--) {
                    android.support.v7.media.MediaRouter.RouteInfo route = provider.mRoutes.remove(i);
                    if (android.support.v7.media.MediaRouter.DEBUG) {
                        android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "Route removed: " + route);
                    }
                    mCallbackHandler.post(android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_REMOVED, route);
                }
                // Notify provider changed.
                if (android.support.v7.media.MediaRouter.DEBUG) {
                    android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "Provider changed: " + provider);
                }
                mCallbackHandler.post(android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_PROVIDER_CHANGED, provider);
            }
        }

        private int updateRouteDescriptorAndNotify(android.support.v7.media.MediaRouter.RouteInfo route, android.support.v7.media.MediaRouteDescriptor routeDescriptor) {
            int changes = route.maybeUpdateDescriptor(routeDescriptor);
            if (changes != 0) {
                if ((changes & android.support.v7.media.MediaRouter.RouteInfo.CHANGE_GENERAL) != 0) {
                    if (android.support.v7.media.MediaRouter.DEBUG) {
                        android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "Route changed: " + route);
                    }
                    mCallbackHandler.post(android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_CHANGED, route);
                }
                if ((changes & android.support.v7.media.MediaRouter.RouteInfo.CHANGE_VOLUME) != 0) {
                    if (android.support.v7.media.MediaRouter.DEBUG) {
                        android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "Route volume changed: " + route);
                    }
                    mCallbackHandler.post(android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_VOLUME_CHANGED, route);
                }
                if ((changes & android.support.v7.media.MediaRouter.RouteInfo.CHANGE_PRESENTATION_DISPLAY) != 0) {
                    if (android.support.v7.media.MediaRouter.DEBUG) {
                        android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "Route presentation display changed: " + route);
                    }
                    mCallbackHandler.post(android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_PRESENTATION_DISPLAY_CHANGED, route);
                }
            }
            return changes;
        }

        private java.lang.String assignRouteUniqueId(android.support.v7.media.MediaRouter.ProviderInfo provider, java.lang.String routeDescriptorId) {
            // Although route descriptor ids are unique within a provider, it's
            // possible for there to be two providers with the same package name.
            // Therefore we must dedupe the composite id.
            java.lang.String componentName = provider.getComponentName().flattenToShortString();
            java.lang.String uniqueId = (componentName + ":") + routeDescriptorId;
            if (findRouteByUniqueId(uniqueId) < 0) {
                mUniqueIdMap.put(new android.support.v4.util.Pair(componentName, routeDescriptorId), uniqueId);
                return uniqueId;
            }
            android.util.Log.w(android.support.v7.media.MediaRouter.TAG, ((("Either " + routeDescriptorId) + " isn't unique in ") + componentName) + " or we're trying to assign a unique ID for an already added route");
            for (int i = 2; ; i++) {
                java.lang.String newUniqueId = java.lang.String.format(java.util.Locale.US, "%s_%d", uniqueId, i);
                if (findRouteByUniqueId(newUniqueId) < 0) {
                    mUniqueIdMap.put(new android.support.v4.util.Pair(componentName, routeDescriptorId), newUniqueId);
                    return newUniqueId;
                }
            }
        }

        private int findRouteByUniqueId(java.lang.String uniqueId) {
            final int count = mRoutes.size();
            for (int i = 0; i < count; i++) {
                if (mRoutes.get(i).mUniqueId.equals(uniqueId)) {
                    return i;
                }
            }
            return -1;
        }

        private java.lang.String getUniqueId(android.support.v7.media.MediaRouter.ProviderInfo provider, java.lang.String routeDescriptorId) {
            java.lang.String componentName = provider.getComponentName().flattenToShortString();
            return mUniqueIdMap.get(new android.support.v4.util.Pair(componentName, routeDescriptorId));
        }

        private void updateSelectedRouteIfNeeded(boolean selectedRouteDescriptorChanged) {
            // Update default route.
            if ((mDefaultRoute != null) && (!isRouteSelectable(mDefaultRoute))) {
                android.util.Log.i(android.support.v7.media.MediaRouter.TAG, ("Clearing the default route because it " + "is no longer selectable: ") + mDefaultRoute);
                mDefaultRoute = null;
            }
            if ((mDefaultRoute == null) && (!mRoutes.isEmpty())) {
                for (android.support.v7.media.MediaRouter.RouteInfo route : mRoutes) {
                    if (isSystemDefaultRoute(route) && isRouteSelectable(route)) {
                        mDefaultRoute = route;
                        android.util.Log.i(android.support.v7.media.MediaRouter.TAG, "Found default route: " + mDefaultRoute);
                        break;
                    }
                }
            }
            // Update selected route.
            if ((mSelectedRoute != null) && (!isRouteSelectable(mSelectedRoute))) {
                android.util.Log.i(android.support.v7.media.MediaRouter.TAG, ("Unselecting the current route because it " + "is no longer selectable: ") + mSelectedRoute);
                setSelectedRouteInternal(null, android.support.v7.media.MediaRouter.UNSELECT_REASON_UNKNOWN);
            }
            if (mSelectedRoute == null) {
                // Choose a new route.
                // This will have the side-effect of updating the playback info when
                // the new route is selected.
                setSelectedRouteInternal(chooseFallbackRoute(), android.support.v7.media.MediaRouter.UNSELECT_REASON_UNKNOWN);
            } else
                if (selectedRouteDescriptorChanged) {
                    // In case the selected route is a route group, select/unselect route controllers
                    // for the added/removed route members.
                    if (mSelectedRoute instanceof android.support.v7.media.MediaRouter.RouteGroup) {
                        java.util.List<android.support.v7.media.MediaRouter.RouteInfo> routes = ((android.support.v7.media.MediaRouter.RouteGroup) (mSelectedRoute)).getRoutes();
                        // Build a set of descriptor IDs for the new route group.
                        java.util.Set idSet = new java.util.HashSet<java.lang.String>();
                        for (android.support.v7.media.MediaRouter.RouteInfo route : routes) {
                            idSet.add(route.mDescriptorId);
                        }
                        // Unselect route controllers for the removed routes.
                        java.util.Iterator<java.util.Map.Entry<java.lang.String, android.support.v7.media.MediaRouteProvider.RouteController>> iter = mRouteControllerMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            java.util.Map.Entry<java.lang.String, android.support.v7.media.MediaRouteProvider.RouteController> entry = iter.next();
                            if (!idSet.contains(entry.getKey())) {
                                android.support.v7.media.MediaRouteProvider.RouteController controller = entry.getValue();
                                controller.onUnselect();
                                controller.onRelease();
                                iter.remove();
                            }
                        } 
                        // Select route controllers for the added routes.
                        for (android.support.v7.media.MediaRouter.RouteInfo route : routes) {
                            if (!mRouteControllerMap.containsKey(route.mDescriptorId)) {
                                android.support.v7.media.MediaRouteProvider.RouteController controller = route.getProviderInstance().onCreateRouteController(route.mDescriptorId, mSelectedRoute.mDescriptorId);
                                controller.onSelect();
                                mRouteControllerMap.put(route.mDescriptorId, controller);
                            }
                        }
                    }
                    // Update the playback info because the properties of the route have changed.
                    updatePlaybackInfoFromSelectedRoute();
                }

        }

        android.support.v7.media.MediaRouter.RouteInfo chooseFallbackRoute() {
            // When the current route is removed or no longer selectable,
            // we want to revert to a live audio route if there is
            // one (usually Bluetooth A2DP).  Failing that, use
            // the default route.
            for (android.support.v7.media.MediaRouter.RouteInfo route : mRoutes) {
                if (((route != mDefaultRoute) && isSystemLiveAudioOnlyRoute(route)) && isRouteSelectable(route)) {
                    return route;
                }
            }
            return mDefaultRoute;
        }

        private boolean isSystemLiveAudioOnlyRoute(android.support.v7.media.MediaRouter.RouteInfo route) {
            return ((route.getProviderInstance() == mSystemProvider) && route.supportsControlCategory(android.support.v7.media.MediaControlIntent.CATEGORY_LIVE_AUDIO)) && (!route.supportsControlCategory(android.support.v7.media.MediaControlIntent.CATEGORY_LIVE_VIDEO));
        }

        private boolean isRouteSelectable(android.support.v7.media.MediaRouter.RouteInfo route) {
            // This tests whether the route is still valid and enabled.
            // The route descriptor field is set to null when the route is removed.
            return (route.mDescriptor != null) && route.mEnabled;
        }

        private boolean isSystemDefaultRoute(android.support.v7.media.MediaRouter.RouteInfo route) {
            return (route.getProviderInstance() == mSystemProvider) && route.mDescriptorId.equals(android.support.v7.media.SystemMediaRouteProvider.DEFAULT_ROUTE_ID);
        }

        private void setSelectedRouteInternal(android.support.v7.media.MediaRouter.RouteInfo route, int unselectReason) {
            if (mSelectedRoute != route) {
                if (mSelectedRoute != null) {
                    if (android.support.v7.media.MediaRouter.DEBUG) {
                        android.util.Log.d(android.support.v7.media.MediaRouter.TAG, (("Route unselected: " + mSelectedRoute) + " reason: ") + unselectReason);
                    }
                    mCallbackHandler.post(android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_UNSELECTED, mSelectedRoute, unselectReason);
                    if (mSelectedRouteController != null) {
                        mSelectedRouteController.onUnselect(unselectReason);
                        mSelectedRouteController.onRelease();
                        mSelectedRouteController = null;
                    }
                    if (!mRouteControllerMap.isEmpty()) {
                        for (android.support.v7.media.MediaRouteProvider.RouteController controller : mRouteControllerMap.values()) {
                            controller.onUnselect(unselectReason);
                            controller.onRelease();
                        }
                        mRouteControllerMap.clear();
                    }
                }
                mSelectedRoute = route;
                if (mSelectedRoute != null) {
                    mSelectedRouteController = route.getProviderInstance().onCreateRouteController(route.mDescriptorId);
                    if (mSelectedRouteController != null) {
                        mSelectedRouteController.onSelect();
                    }
                    if (android.support.v7.media.MediaRouter.DEBUG) {
                        android.util.Log.d(android.support.v7.media.MediaRouter.TAG, "Route selected: " + mSelectedRoute);
                    }
                    mCallbackHandler.post(android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_SELECTED, mSelectedRoute);
                    if (mSelectedRoute instanceof android.support.v7.media.MediaRouter.RouteGroup) {
                        java.util.List<android.support.v7.media.MediaRouter.RouteInfo> routes = ((android.support.v7.media.MediaRouter.RouteGroup) (mSelectedRoute)).getRoutes();
                        mRouteControllerMap.clear();
                        for (android.support.v7.media.MediaRouter.RouteInfo r : routes) {
                            android.support.v7.media.MediaRouteProvider.RouteController controller = r.getProviderInstance().onCreateRouteController(r.mDescriptorId, mSelectedRoute.mDescriptorId);
                            controller.onSelect();
                            mRouteControllerMap.put(r.mDescriptorId, controller);
                        }
                    }
                }
                updatePlaybackInfoFromSelectedRoute();
            }
        }

        @java.lang.Override
        public android.support.v7.media.MediaRouter.RouteInfo getSystemRouteByDescriptorId(java.lang.String id) {
            int providerIndex = findProviderInfo(mSystemProvider);
            if (providerIndex >= 0) {
                android.support.v7.media.MediaRouter.ProviderInfo provider = mProviders.get(providerIndex);
                int routeIndex = provider.findRouteByDescriptorId(id);
                if (routeIndex >= 0) {
                    return provider.mRoutes.get(routeIndex);
                }
            }
            return null;
        }

        public void addRemoteControlClient(java.lang.Object rcc) {
            int index = findRemoteControlClientRecord(rcc);
            if (index < 0) {
                android.support.v7.media.MediaRouter.GlobalMediaRouter.RemoteControlClientRecord record = new android.support.v7.media.MediaRouter.GlobalMediaRouter.RemoteControlClientRecord(rcc);
                mRemoteControlClients.add(record);
            }
        }

        public void removeRemoteControlClient(java.lang.Object rcc) {
            int index = findRemoteControlClientRecord(rcc);
            if (index >= 0) {
                android.support.v7.media.MediaRouter.GlobalMediaRouter.RemoteControlClientRecord record = mRemoteControlClients.remove(index);
                record.disconnect();
            }
        }

        public void setMediaSession(java.lang.Object session) {
            if (mMediaSession != null) {
                mMediaSession.clearVolumeHandling();
            }
            if (session == null) {
                mMediaSession = null;
            } else {
                mMediaSession = new android.support.v7.media.MediaRouter.GlobalMediaRouter.MediaSessionRecord(session);
                updatePlaybackInfoFromSelectedRoute();
            }
        }

        public void setMediaSessionCompat(final android.support.v4.media.session.MediaSessionCompat session) {
            mCompatSession = session;
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                setMediaSession(session != null ? session.getMediaSession() : null);
            } else
                if (android.os.Build.VERSION.SDK_INT >= 14) {
                    if (mRccMediaSession != null) {
                        removeRemoteControlClient(mRccMediaSession.getRemoteControlClient());
                        mRccMediaSession.removeOnActiveChangeListener(mSessionActiveListener);
                    }
                    mRccMediaSession = session;
                    if (session != null) {
                        session.addOnActiveChangeListener(mSessionActiveListener);
                        if (session.isActive()) {
                            addRemoteControlClient(session.getRemoteControlClient());
                        }
                    }
                }

        }

        public android.support.v4.media.session.MediaSessionCompat.Token getMediaSessionToken() {
            if (mMediaSession != null) {
                return mMediaSession.getToken();
            } else
                if (mCompatSession != null) {
                    return mCompatSession.getSessionToken();
                }

            return null;
        }

        private int findRemoteControlClientRecord(java.lang.Object rcc) {
            final int count = mRemoteControlClients.size();
            for (int i = 0; i < count; i++) {
                android.support.v7.media.MediaRouter.GlobalMediaRouter.RemoteControlClientRecord record = mRemoteControlClients.get(i);
                if (record.getRemoteControlClient() == rcc) {
                    return i;
                }
            }
            return -1;
        }

        private void updatePlaybackInfoFromSelectedRoute() {
            if (mSelectedRoute != null) {
                mPlaybackInfo.volume = mSelectedRoute.getVolume();
                mPlaybackInfo.volumeMax = mSelectedRoute.getVolumeMax();
                mPlaybackInfo.volumeHandling = mSelectedRoute.getVolumeHandling();
                mPlaybackInfo.playbackStream = mSelectedRoute.getPlaybackStream();
                mPlaybackInfo.playbackType = mSelectedRoute.getPlaybackType();
                final int count = mRemoteControlClients.size();
                for (int i = 0; i < count; i++) {
                    android.support.v7.media.MediaRouter.GlobalMediaRouter.RemoteControlClientRecord record = mRemoteControlClients.get(i);
                    record.updatePlaybackInfo();
                }
                if (mMediaSession != null) {
                    if (mSelectedRoute == getDefaultRoute()) {
                        // Local route
                        mMediaSession.clearVolumeHandling();
                    } else {
                        @android.support.v4.media.VolumeProviderCompat.ControlType
                        int controlType = android.support.v4.media.VolumeProviderCompat.VOLUME_CONTROL_FIXED;
                        if (mPlaybackInfo.volumeHandling == android.support.v7.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE) {
                            controlType = android.support.v4.media.VolumeProviderCompat.VOLUME_CONTROL_ABSOLUTE;
                        }
                        mMediaSession.configureVolume(controlType, mPlaybackInfo.volumeMax, mPlaybackInfo.volume);
                    }
                }
            } else {
                if (mMediaSession != null) {
                    mMediaSession.clearVolumeHandling();
                }
            }
        }

        private final class ProviderCallback extends android.support.v7.media.MediaRouteProvider.Callback {
            ProviderCallback() {
            }

            @java.lang.Override
            public void onDescriptorChanged(android.support.v7.media.MediaRouteProvider provider, android.support.v7.media.MediaRouteProviderDescriptor descriptor) {
                updateProviderDescriptor(provider, descriptor);
            }
        }

        private final class MediaSessionRecord {
            private final android.support.v4.media.session.MediaSessionCompat mMsCompat;

            @android.support.v4.media.VolumeProviderCompat.ControlType
            private int mControlType;

            private int mMaxVolume;

            private android.support.v4.media.VolumeProviderCompat mVpCompat;

            public MediaSessionRecord(java.lang.Object mediaSession) {
                mMsCompat = android.support.v4.media.session.MediaSessionCompat.fromMediaSession(mApplicationContext, mediaSession);
            }

            public void configureVolume(@android.support.v4.media.VolumeProviderCompat.ControlType
            int controlType, int max, int current) {
                if (((mVpCompat != null) && (controlType == mControlType)) && (max == mMaxVolume)) {
                    // If we haven't changed control type or max just set the
                    // new current volume
                    mVpCompat.setCurrentVolume(current);
                } else {
                    // Otherwise create a new provider and update
                    mVpCompat = new android.support.v4.media.VolumeProviderCompat(controlType, max, current) {
                        @java.lang.Override
                        public void onSetVolumeTo(final int volume) {
                            mCallbackHandler.post(new java.lang.Runnable() {
                                @java.lang.Override
                                public void run() {
                                    if (mSelectedRoute != null) {
                                        mSelectedRoute.requestSetVolume(volume);
                                    }
                                }
                            });
                        }

                        @java.lang.Override
                        public void onAdjustVolume(final int direction) {
                            mCallbackHandler.post(new java.lang.Runnable() {
                                @java.lang.Override
                                public void run() {
                                    if (mSelectedRoute != null) {
                                        mSelectedRoute.requestUpdateVolume(direction);
                                    }
                                }
                            });
                        }
                    };
                    mMsCompat.setPlaybackToRemote(mVpCompat);
                }
            }

            public void clearVolumeHandling() {
                mMsCompat.setPlaybackToLocal(mPlaybackInfo.playbackStream);
                mVpCompat = null;
            }

            public android.support.v4.media.session.MediaSessionCompat.Token getToken() {
                return mMsCompat.getSessionToken();
            }
        }

        private final class RemoteControlClientRecord implements android.support.v7.media.RemoteControlClientCompat.VolumeCallback {
            private final android.support.v7.media.RemoteControlClientCompat mRccCompat;

            private boolean mDisconnected;

            public RemoteControlClientRecord(java.lang.Object rcc) {
                mRccCompat = android.support.v7.media.RemoteControlClientCompat.obtain(mApplicationContext, rcc);
                mRccCompat.setVolumeCallback(this);
                updatePlaybackInfo();
            }

            public java.lang.Object getRemoteControlClient() {
                return mRccCompat.getRemoteControlClient();
            }

            public void disconnect() {
                mDisconnected = true;
                mRccCompat.setVolumeCallback(null);
            }

            public void updatePlaybackInfo() {
                mRccCompat.setPlaybackInfo(mPlaybackInfo);
            }

            @java.lang.Override
            public void onVolumeSetRequest(int volume) {
                if ((!mDisconnected) && (mSelectedRoute != null)) {
                    mSelectedRoute.requestSetVolume(volume);
                }
            }

            @java.lang.Override
            public void onVolumeUpdateRequest(int direction) {
                if ((!mDisconnected) && (mSelectedRoute != null)) {
                    mSelectedRoute.requestUpdateVolume(direction);
                }
            }
        }

        private final class CallbackHandler extends android.os.Handler {
            private final java.util.ArrayList<android.support.v7.media.MediaRouter.CallbackRecord> mTempCallbackRecords = new java.util.ArrayList<android.support.v7.media.MediaRouter.CallbackRecord>();

            private static final int MSG_TYPE_MASK = 0xff00;

            private static final int MSG_TYPE_ROUTE = 0x100;

            private static final int MSG_TYPE_PROVIDER = 0x200;

            public static final int MSG_ROUTE_ADDED = android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_ROUTE | 1;

            public static final int MSG_ROUTE_REMOVED = android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_ROUTE | 2;

            public static final int MSG_ROUTE_CHANGED = android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_ROUTE | 3;

            public static final int MSG_ROUTE_VOLUME_CHANGED = android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_ROUTE | 4;

            public static final int MSG_ROUTE_PRESENTATION_DISPLAY_CHANGED = android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_ROUTE | 5;

            public static final int MSG_ROUTE_SELECTED = android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_ROUTE | 6;

            public static final int MSG_ROUTE_UNSELECTED = android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_ROUTE | 7;

            public static final int MSG_PROVIDER_ADDED = android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_PROVIDER | 1;

            public static final int MSG_PROVIDER_REMOVED = android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_PROVIDER | 2;

            public static final int MSG_PROVIDER_CHANGED = android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_PROVIDER | 3;

            CallbackHandler() {
            }

            public void post(int msg, java.lang.Object obj) {
                obtainMessage(msg, obj).sendToTarget();
            }

            public void post(int msg, java.lang.Object obj, int arg) {
                android.os.Message message = obtainMessage(msg, obj);
                message.arg1 = arg;
                message.sendToTarget();
            }

            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                final int what = msg.what;
                final java.lang.Object obj = msg.obj;
                final int arg = msg.arg1;
                if ((what == android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_CHANGED) && getSelectedRoute().getId().equals(((android.support.v7.media.MediaRouter.RouteInfo) (obj)).getId())) {
                    updateSelectedRouteIfNeeded(true);
                }
                // Synchronize state with the system media router.
                syncWithSystemProvider(what, obj);
                // Invoke all registered callbacks.
                // Build a list of callbacks before invoking them in case callbacks
                // are added or removed during dispatch.
                try {
                    for (int i = mRouters.size(); (--i) >= 0;) {
                        android.support.v7.media.MediaRouter router = mRouters.get(i).get();
                        if (router == null) {
                            mRouters.remove(i);
                        } else {
                            mTempCallbackRecords.addAll(router.mCallbackRecords);
                        }
                    }
                    final int callbackCount = mTempCallbackRecords.size();
                    for (int i = 0; i < callbackCount; i++) {
                        invokeCallback(mTempCallbackRecords.get(i), what, obj, arg);
                    }
                } finally {
                    mTempCallbackRecords.clear();
                }
            }

            private void syncWithSystemProvider(int what, java.lang.Object obj) {
                switch (what) {
                    case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_ADDED :
                        mSystemProvider.onSyncRouteAdded(((android.support.v7.media.MediaRouter.RouteInfo) (obj)));
                        break;
                    case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_REMOVED :
                        mSystemProvider.onSyncRouteRemoved(((android.support.v7.media.MediaRouter.RouteInfo) (obj)));
                        break;
                    case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_CHANGED :
                        mSystemProvider.onSyncRouteChanged(((android.support.v7.media.MediaRouter.RouteInfo) (obj)));
                        break;
                    case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_SELECTED :
                        mSystemProvider.onSyncRouteSelected(((android.support.v7.media.MediaRouter.RouteInfo) (obj)));
                        break;
                }
            }

            private void invokeCallback(android.support.v7.media.MediaRouter.CallbackRecord record, int what, java.lang.Object obj, int arg) {
                final android.support.v7.media.MediaRouter router = record.mRouter;
                final android.support.v7.media.MediaRouter.Callback callback = record.mCallback;
                switch (what & android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_MASK) {
                    case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_ROUTE :
                        {
                            final android.support.v7.media.MediaRouter.RouteInfo route = ((android.support.v7.media.MediaRouter.RouteInfo) (obj));
                            if (!record.filterRouteEvent(route)) {
                                break;
                            }
                            switch (what) {
                                case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_ADDED :
                                    callback.onRouteAdded(router, route);
                                    break;
                                case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_REMOVED :
                                    callback.onRouteRemoved(router, route);
                                    break;
                                case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_CHANGED :
                                    callback.onRouteChanged(router, route);
                                    break;
                                case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_VOLUME_CHANGED :
                                    callback.onRouteVolumeChanged(router, route);
                                    break;
                                case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_PRESENTATION_DISPLAY_CHANGED :
                                    callback.onRoutePresentationDisplayChanged(router, route);
                                    break;
                                case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_SELECTED :
                                    callback.onRouteSelected(router, route);
                                    break;
                                case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_ROUTE_UNSELECTED :
                                    callback.onRouteUnselected(router, route, arg);
                                    break;
                            }
                            break;
                        }
                    case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_TYPE_PROVIDER :
                        {
                            final android.support.v7.media.MediaRouter.ProviderInfo provider = ((android.support.v7.media.MediaRouter.ProviderInfo) (obj));
                            switch (what) {
                                case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_PROVIDER_ADDED :
                                    callback.onProviderAdded(router, provider);
                                    break;
                                case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_PROVIDER_REMOVED :
                                    callback.onProviderRemoved(router, provider);
                                    break;
                                case android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler.MSG_PROVIDER_CHANGED :
                                    callback.onProviderChanged(router, provider);
                                    break;
                            }
                        }
                }
            }
        }
    }
}

