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
package android.media;


/**
 * MediaRouter allows applications to control the routing of media channels
 * and streams from the current device to external speakers and destination devices.
 *
 * <p>A MediaRouter is retrieved through {@link Context#getSystemService(String)
 * Context.getSystemService()} of a {@link Context#MEDIA_ROUTER_SERVICE
 * Context.MEDIA_ROUTER_SERVICE}.
 *
 * <p>The media router API is not thread-safe; all interactions with it must be
 * done from the main thread of the process.</p>
 */
public class MediaRouter {
    private static final java.lang.String TAG = "MediaRouter";

    private static final boolean DEBUG = android.util.Log.isLoggable(android.media.MediaRouter.TAG, android.util.Log.DEBUG);

    static class Static implements android.hardware.display.DisplayManager.DisplayListener {
        final android.content.Context mAppContext;

        final android.content.res.Resources mResources;

        final android.media.IAudioService mAudioService;

        final android.hardware.display.DisplayManager mDisplayService;

        final android.media.IMediaRouterService mMediaRouterService;

        final android.os.Handler mHandler;

        final java.util.concurrent.CopyOnWriteArrayList<android.media.MediaRouter.CallbackInfo> mCallbacks = new java.util.concurrent.CopyOnWriteArrayList<android.media.MediaRouter.CallbackInfo>();

        final java.util.ArrayList<android.media.MediaRouter.RouteInfo> mRoutes = new java.util.ArrayList<android.media.MediaRouter.RouteInfo>();

        final java.util.ArrayList<android.media.MediaRouter.RouteCategory> mCategories = new java.util.ArrayList<android.media.MediaRouter.RouteCategory>();

        final android.media.MediaRouter.RouteCategory mSystemCategory;

        final android.media.AudioRoutesInfo mCurAudioRoutesInfo = new android.media.AudioRoutesInfo();

        android.media.MediaRouter.RouteInfo mDefaultAudioVideo;

        android.media.MediaRouter.RouteInfo mBluetoothA2dpRoute;

        android.media.MediaRouter.RouteInfo mSelectedRoute;

        final boolean mCanConfigureWifiDisplays;

        boolean mActivelyScanningWifiDisplays;

        java.lang.String mPreviousActiveWifiDisplayAddress;

        int mDiscoveryRequestRouteTypes;

        boolean mDiscoverRequestActiveScan;

        int mCurrentUserId = -1;

        android.media.IMediaRouterClient mClient;

        android.media.MediaRouterClientState mClientState;

        final IAudioRoutesObserver.Stub mAudioRoutesObserver = new android.media.IAudioRoutesObserver.Stub() {
            @java.lang.Override
            public void dispatchAudioRoutesChanged(final android.media.AudioRoutesInfo newRoutes) {
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        updateAudioRoutes(newRoutes);
                    }
                });
            }
        };

        Static(android.content.Context appContext) {
            mAppContext = appContext;
            mResources = android.content.res.Resources.getSystem();
            mHandler = new android.os.Handler(appContext.getMainLooper());
            android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.AUDIO_SERVICE);
            mAudioService = IAudioService.Stub.asInterface(b);
            mDisplayService = ((android.hardware.display.DisplayManager) (appContext.getSystemService(android.content.Context.DISPLAY_SERVICE)));
            mMediaRouterService = IMediaRouterService.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.MEDIA_ROUTER_SERVICE));
            mSystemCategory = new android.media.MediaRouter.RouteCategory(com.android.internal.R.string.default_audio_route_category_name, android.media.MediaRouter.ROUTE_TYPE_LIVE_AUDIO | android.media.MediaRouter.ROUTE_TYPE_LIVE_VIDEO, false);
            mSystemCategory.mIsSystem = true;
            // Only the system can configure wifi displays.  The display manager
            // enforces this with a permission check.  Set a flag here so that we
            // know whether this process is actually allowed to scan and connect.
            mCanConfigureWifiDisplays = appContext.checkPermission(Manifest.permission.CONFIGURE_WIFI_DISPLAY, android.os.Process.myPid(), android.os.Process.myUid()) == android.content.pm.PackageManager.PERMISSION_GRANTED;
        }

        // Called after sStatic is initialized
        void startMonitoringRoutes(android.content.Context appContext) {
            mDefaultAudioVideo = new android.media.MediaRouter.RouteInfo(mSystemCategory);
            mDefaultAudioVideo.mNameResId = com.android.internal.R.string.default_audio_route_name;
            mDefaultAudioVideo.mSupportedTypes = android.media.MediaRouter.ROUTE_TYPE_LIVE_AUDIO | android.media.MediaRouter.ROUTE_TYPE_LIVE_VIDEO;
            mDefaultAudioVideo.updatePresentationDisplay();
            android.media.MediaRouter.addRouteStatic(mDefaultAudioVideo);
            // This will select the active wifi display route if there is one.
            android.media.MediaRouter.updateWifiDisplayStatus(mDisplayService.getWifiDisplayStatus());
            appContext.registerReceiver(new android.media.MediaRouter.WifiDisplayStatusChangedReceiver(), new android.content.IntentFilter(android.hardware.display.DisplayManager.ACTION_WIFI_DISPLAY_STATUS_CHANGED));
            appContext.registerReceiver(new android.media.MediaRouter.VolumeChangeReceiver(), new android.content.IntentFilter(android.media.AudioManager.VOLUME_CHANGED_ACTION));
            mDisplayService.registerDisplayListener(this, mHandler);
            android.media.AudioRoutesInfo newAudioRoutes = null;
            try {
                newAudioRoutes = mAudioService.startWatchingRoutes(mAudioRoutesObserver);
            } catch (android.os.RemoteException e) {
            }
            if (newAudioRoutes != null) {
                // This will select the active BT route if there is one and the current
                // selected route is the default system route, or if there is no selected
                // route yet.
                updateAudioRoutes(newAudioRoutes);
            }
            // Bind to the media router service.
            rebindAsUser(android.os.UserHandle.myUserId());
            // Select the default route if the above didn't sync us up
            // appropriately with relevant system state.
            if (mSelectedRoute == null) {
                android.media.MediaRouter.selectDefaultRouteStatic();
            }
        }

        void updateAudioRoutes(android.media.AudioRoutesInfo newRoutes) {
            android.util.Log.v(android.media.MediaRouter.TAG, "Updating audio routes: " + newRoutes);
            if (newRoutes.mainType != mCurAudioRoutesInfo.mainType) {
                mCurAudioRoutesInfo.mainType = newRoutes.mainType;
                int name;
                if (((newRoutes.mainType & android.media.AudioRoutesInfo.MAIN_HEADPHONES) != 0) || ((newRoutes.mainType & android.media.AudioRoutesInfo.MAIN_HEADSET) != 0)) {
                    name = com.android.internal.R.string.default_audio_route_name_headphones;
                } else
                    if ((newRoutes.mainType & android.media.AudioRoutesInfo.MAIN_DOCK_SPEAKERS) != 0) {
                        name = com.android.internal.R.string.default_audio_route_name_dock_speakers;
                    } else
                        if ((newRoutes.mainType & android.media.AudioRoutesInfo.MAIN_HDMI) != 0) {
                            name = com.android.internal.R.string.default_media_route_name_hdmi;
                        } else {
                            name = com.android.internal.R.string.default_audio_route_name;
                        }


                android.media.MediaRouter.sStatic.mDefaultAudioVideo.mNameResId = name;
                android.media.MediaRouter.dispatchRouteChanged(android.media.MediaRouter.sStatic.mDefaultAudioVideo);
            }
            final int mainType = mCurAudioRoutesInfo.mainType;
            if (!android.text.TextUtils.equals(newRoutes.bluetoothName, mCurAudioRoutesInfo.bluetoothName)) {
                mCurAudioRoutesInfo.bluetoothName = newRoutes.bluetoothName;
                if (mCurAudioRoutesInfo.bluetoothName != null) {
                    if (android.media.MediaRouter.sStatic.mBluetoothA2dpRoute == null) {
                        final android.media.MediaRouter.RouteInfo info = new android.media.MediaRouter.RouteInfo(android.media.MediaRouter.sStatic.mSystemCategory);
                        info.mName = mCurAudioRoutesInfo.bluetoothName;
                        info.mDescription = android.media.MediaRouter.sStatic.mResources.getText(com.android.internal.R.string.bluetooth_a2dp_audio_route_name);
                        info.mSupportedTypes = android.media.MediaRouter.ROUTE_TYPE_LIVE_AUDIO;
                        info.mDeviceType = android.media.MediaRouter.RouteInfo.DEVICE_TYPE_BLUETOOTH;
                        android.media.MediaRouter.sStatic.mBluetoothA2dpRoute = info;
                        android.media.MediaRouter.addRouteStatic(android.media.MediaRouter.sStatic.mBluetoothA2dpRoute);
                    } else {
                        android.media.MediaRouter.sStatic.mBluetoothA2dpRoute.mName = mCurAudioRoutesInfo.bluetoothName;
                        android.media.MediaRouter.dispatchRouteChanged(android.media.MediaRouter.sStatic.mBluetoothA2dpRoute);
                    }
                } else
                    if (android.media.MediaRouter.sStatic.mBluetoothA2dpRoute != null) {
                        android.media.MediaRouter.removeRouteStatic(android.media.MediaRouter.sStatic.mBluetoothA2dpRoute);
                        android.media.MediaRouter.sStatic.mBluetoothA2dpRoute = null;
                    }

            }
            if (mBluetoothA2dpRoute != null) {
                final boolean a2dpEnabled = isBluetoothA2dpOn();
                if ((mSelectedRoute == mBluetoothA2dpRoute) && (!a2dpEnabled)) {
                    android.media.MediaRouter.selectRouteStatic(android.media.MediaRouter.ROUTE_TYPE_LIVE_AUDIO, mDefaultAudioVideo, false);
                } else
                    if (((mSelectedRoute == mDefaultAudioVideo) || (mSelectedRoute == null)) && a2dpEnabled) {
                        android.media.MediaRouter.selectRouteStatic(android.media.MediaRouter.ROUTE_TYPE_LIVE_AUDIO, mBluetoothA2dpRoute, false);
                    }

            }
        }

        boolean isBluetoothA2dpOn() {
            try {
                return mAudioService.isBluetoothA2dpOn();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.media.MediaRouter.TAG, "Error querying Bluetooth A2DP state", e);
                return false;
            }
        }

        void updateDiscoveryRequest() {
            // What are we looking for today?
            int routeTypes = 0;
            int passiveRouteTypes = 0;
            boolean activeScan = false;
            boolean activeScanWifiDisplay = false;
            final int count = mCallbacks.size();
            for (int i = 0; i < count; i++) {
                android.media.MediaRouter.CallbackInfo cbi = mCallbacks.get(i);
                if ((cbi.flags & (android.media.MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN | android.media.MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY)) != 0) {
                    // Discovery explicitly requested.
                    routeTypes |= cbi.type;
                } else
                    if ((cbi.flags & android.media.MediaRouter.CALLBACK_FLAG_PASSIVE_DISCOVERY) != 0) {
                        // Discovery only passively requested.
                        passiveRouteTypes |= cbi.type;
                    } else {
                        // Legacy case since applications don't specify the discovery flag.
                        // Unfortunately we just have to assume they always need discovery
                        // whenever they have a callback registered.
                        routeTypes |= cbi.type;
                    }

                if ((cbi.flags & android.media.MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN) != 0) {
                    activeScan = true;
                    if ((cbi.type & android.media.MediaRouter.ROUTE_TYPE_REMOTE_DISPLAY) != 0) {
                        activeScanWifiDisplay = true;
                    }
                }
            }
            if ((routeTypes != 0) || activeScan) {
                // If someone else requests discovery then enable the passive listeners.
                // This is used by the MediaRouteButton and MediaRouteActionProvider since
                // they don't receive lifecycle callbacks from the Activity.
                routeTypes |= passiveRouteTypes;
            }
            // Update wifi display scanning.
            // TODO: All of this should be managed by the media router service.
            if (mCanConfigureWifiDisplays) {
                if ((mSelectedRoute != null) && mSelectedRoute.matchesTypes(android.media.MediaRouter.ROUTE_TYPE_REMOTE_DISPLAY)) {
                    // Don't scan while already connected to a remote display since
                    // it may interfere with the ongoing transmission.
                    activeScanWifiDisplay = false;
                }
                if (activeScanWifiDisplay) {
                    if (!mActivelyScanningWifiDisplays) {
                        mActivelyScanningWifiDisplays = true;
                        mDisplayService.startWifiDisplayScan();
                    }
                } else {
                    if (mActivelyScanningWifiDisplays) {
                        mActivelyScanningWifiDisplays = false;
                        mDisplayService.stopWifiDisplayScan();
                    }
                }
            }
            // Tell the media router service all about it.
            if ((routeTypes != mDiscoveryRequestRouteTypes) || (activeScan != mDiscoverRequestActiveScan)) {
                mDiscoveryRequestRouteTypes = routeTypes;
                mDiscoverRequestActiveScan = activeScan;
                publishClientDiscoveryRequest();
            }
        }

        @java.lang.Override
        public void onDisplayAdded(int displayId) {
            updatePresentationDisplays(displayId);
        }

        @java.lang.Override
        public void onDisplayChanged(int displayId) {
            updatePresentationDisplays(displayId);
        }

        @java.lang.Override
        public void onDisplayRemoved(int displayId) {
            updatePresentationDisplays(displayId);
        }

        public android.view.Display[] getAllPresentationDisplays() {
            return mDisplayService.getDisplays(android.hardware.display.DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        }

        private void updatePresentationDisplays(int changedDisplayId) {
            final int count = mRoutes.size();
            for (int i = 0; i < count; i++) {
                final android.media.MediaRouter.RouteInfo route = mRoutes.get(i);
                if (route.updatePresentationDisplay() || ((route.mPresentationDisplay != null) && (route.mPresentationDisplay.getDisplayId() == changedDisplayId))) {
                    android.media.MediaRouter.dispatchRoutePresentationDisplayChanged(route);
                }
            }
        }

        void setSelectedRoute(android.media.MediaRouter.RouteInfo info, boolean explicit) {
            // Must be non-reentrant.
            mSelectedRoute = info;
            publishClientSelectedRoute(explicit);
        }

        void rebindAsUser(int userId) {
            if (((mCurrentUserId != userId) || (userId < 0)) || (mClient == null)) {
                if (mClient != null) {
                    try {
                        mMediaRouterService.unregisterClient(mClient);
                    } catch (android.os.RemoteException ex) {
                        android.util.Log.e(android.media.MediaRouter.TAG, "Unable to unregister media router client.", ex);
                    }
                    mClient = null;
                }
                mCurrentUserId = userId;
                try {
                    android.media.MediaRouter.Static.Client client = new android.media.MediaRouter.Static.Client();
                    mMediaRouterService.registerClientAsUser(client, mAppContext.getPackageName(), userId);
                    mClient = client;
                } catch (android.os.RemoteException ex) {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Unable to register media router client.", ex);
                }
                publishClientDiscoveryRequest();
                publishClientSelectedRoute(false);
                updateClientState();
            }
        }

        void publishClientDiscoveryRequest() {
            if (mClient != null) {
                try {
                    mMediaRouterService.setDiscoveryRequest(mClient, mDiscoveryRequestRouteTypes, mDiscoverRequestActiveScan);
                } catch (android.os.RemoteException ex) {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Unable to publish media router client discovery request.", ex);
                }
            }
        }

        void publishClientSelectedRoute(boolean explicit) {
            if (mClient != null) {
                try {
                    mMediaRouterService.setSelectedRoute(mClient, mSelectedRoute != null ? mSelectedRoute.mGlobalRouteId : null, explicit);
                } catch (android.os.RemoteException ex) {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Unable to publish media router client selected route.", ex);
                }
            }
        }

        void updateClientState() {
            // Update the client state.
            mClientState = null;
            if (mClient != null) {
                try {
                    mClientState = mMediaRouterService.getState(mClient);
                } catch (android.os.RemoteException ex) {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Unable to retrieve media router client state.", ex);
                }
            }
            final java.util.ArrayList<android.media.MediaRouterClientState.RouteInfo> globalRoutes = (mClientState != null) ? mClientState.routes : null;
            final java.lang.String globallySelectedRouteId = (mClientState != null) ? mClientState.globallySelectedRouteId : null;
            // Add or update routes.
            final int globalRouteCount = (globalRoutes != null) ? globalRoutes.size() : 0;
            for (int i = 0; i < globalRouteCount; i++) {
                final android.media.MediaRouterClientState.RouteInfo globalRoute = globalRoutes.get(i);
                android.media.MediaRouter.RouteInfo route = findGlobalRoute(globalRoute.id);
                if (route == null) {
                    route = makeGlobalRoute(globalRoute);
                    android.media.MediaRouter.addRouteStatic(route);
                } else {
                    updateGlobalRoute(route, globalRoute);
                }
            }
            // Synchronize state with the globally selected route.
            if (globallySelectedRouteId != null) {
                final android.media.MediaRouter.RouteInfo route = findGlobalRoute(globallySelectedRouteId);
                if (route == null) {
                    android.util.Log.w(android.media.MediaRouter.TAG, "Could not find new globally selected route: " + globallySelectedRouteId);
                } else
                    if (route != mSelectedRoute) {
                        if (android.media.MediaRouter.DEBUG) {
                            android.util.Log.d(android.media.MediaRouter.TAG, "Selecting new globally selected route: " + route);
                        }
                        android.media.MediaRouter.selectRouteStatic(route.mSupportedTypes, route, false);
                    }

            } else
                if ((mSelectedRoute != null) && (mSelectedRoute.mGlobalRouteId != null)) {
                    if (android.media.MediaRouter.DEBUG) {
                        android.util.Log.d(android.media.MediaRouter.TAG, "Unselecting previous globally selected route: " + mSelectedRoute);
                    }
                    android.media.MediaRouter.selectDefaultRouteStatic();
                }

            // Remove defunct routes.
            outer : for (int i = mRoutes.size(); (i--) > 0;) {
                final android.media.MediaRouter.RouteInfo route = mRoutes.get(i);
                final java.lang.String globalRouteId = route.mGlobalRouteId;
                if (globalRouteId != null) {
                    for (int j = 0; j < globalRouteCount; j++) {
                        android.media.MediaRouterClientState.RouteInfo globalRoute = globalRoutes.get(j);
                        if (globalRouteId.equals(globalRoute.id)) {
                            continue outer;// found

                        }
                    }
                    // not found
                    android.media.MediaRouter.removeRouteStatic(route);
                }
            }
        }

        void requestSetVolume(android.media.MediaRouter.RouteInfo route, int volume) {
            if ((route.mGlobalRouteId != null) && (mClient != null)) {
                try {
                    mMediaRouterService.requestSetVolume(mClient, route.mGlobalRouteId, volume);
                } catch (android.os.RemoteException ex) {
                    android.util.Log.w(android.media.MediaRouter.TAG, "Unable to request volume change.", ex);
                }
            }
        }

        void requestUpdateVolume(android.media.MediaRouter.RouteInfo route, int direction) {
            if ((route.mGlobalRouteId != null) && (mClient != null)) {
                try {
                    mMediaRouterService.requestUpdateVolume(mClient, route.mGlobalRouteId, direction);
                } catch (android.os.RemoteException ex) {
                    android.util.Log.w(android.media.MediaRouter.TAG, "Unable to request volume change.", ex);
                }
            }
        }

        android.media.MediaRouter.RouteInfo makeGlobalRoute(android.media.MediaRouterClientState.RouteInfo globalRoute) {
            android.media.MediaRouter.RouteInfo route = new android.media.MediaRouter.RouteInfo(android.media.MediaRouter.sStatic.mSystemCategory);
            route.mGlobalRouteId = globalRoute.id;
            route.mName = globalRoute.name;
            route.mDescription = globalRoute.description;
            route.mSupportedTypes = globalRoute.supportedTypes;
            route.mDeviceType = globalRoute.deviceType;
            route.mEnabled = globalRoute.enabled;
            route.setRealStatusCode(globalRoute.statusCode);
            route.mPlaybackType = globalRoute.playbackType;
            route.mPlaybackStream = globalRoute.playbackStream;
            route.mVolume = globalRoute.volume;
            route.mVolumeMax = globalRoute.volumeMax;
            route.mVolumeHandling = globalRoute.volumeHandling;
            route.mPresentationDisplayId = globalRoute.presentationDisplayId;
            route.updatePresentationDisplay();
            return route;
        }

        void updateGlobalRoute(android.media.MediaRouter.RouteInfo route, android.media.MediaRouterClientState.RouteInfo globalRoute) {
            boolean changed = false;
            boolean volumeChanged = false;
            boolean presentationDisplayChanged = false;
            if (!java.util.Objects.equals(route.mName, globalRoute.name)) {
                route.mName = globalRoute.name;
                changed = true;
            }
            if (!java.util.Objects.equals(route.mDescription, globalRoute.description)) {
                route.mDescription = globalRoute.description;
                changed = true;
            }
            final int oldSupportedTypes = route.mSupportedTypes;
            if (oldSupportedTypes != globalRoute.supportedTypes) {
                route.mSupportedTypes = globalRoute.supportedTypes;
                changed = true;
            }
            if (route.mEnabled != globalRoute.enabled) {
                route.mEnabled = globalRoute.enabled;
                changed = true;
            }
            if (route.mRealStatusCode != globalRoute.statusCode) {
                route.setRealStatusCode(globalRoute.statusCode);
                changed = true;
            }
            if (route.mPlaybackType != globalRoute.playbackType) {
                route.mPlaybackType = globalRoute.playbackType;
                changed = true;
            }
            if (route.mPlaybackStream != globalRoute.playbackStream) {
                route.mPlaybackStream = globalRoute.playbackStream;
                changed = true;
            }
            if (route.mVolume != globalRoute.volume) {
                route.mVolume = globalRoute.volume;
                changed = true;
                volumeChanged = true;
            }
            if (route.mVolumeMax != globalRoute.volumeMax) {
                route.mVolumeMax = globalRoute.volumeMax;
                changed = true;
                volumeChanged = true;
            }
            if (route.mVolumeHandling != globalRoute.volumeHandling) {
                route.mVolumeHandling = globalRoute.volumeHandling;
                changed = true;
                volumeChanged = true;
            }
            if (route.mPresentationDisplayId != globalRoute.presentationDisplayId) {
                route.mPresentationDisplayId = globalRoute.presentationDisplayId;
                route.updatePresentationDisplay();
                changed = true;
                presentationDisplayChanged = true;
            }
            if (changed) {
                android.media.MediaRouter.dispatchRouteChanged(route, oldSupportedTypes);
            }
            if (volumeChanged) {
                android.media.MediaRouter.dispatchRouteVolumeChanged(route);
            }
            if (presentationDisplayChanged) {
                android.media.MediaRouter.dispatchRoutePresentationDisplayChanged(route);
            }
        }

        android.media.MediaRouter.RouteInfo findGlobalRoute(java.lang.String globalRouteId) {
            final int count = mRoutes.size();
            for (int i = 0; i < count; i++) {
                final android.media.MediaRouter.RouteInfo route = mRoutes.get(i);
                if (globalRouteId.equals(route.mGlobalRouteId)) {
                    return route;
                }
            }
            return null;
        }

        final class Client extends android.media.IMediaRouterClient.Stub {
            @java.lang.Override
            public void onStateChanged() {
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (android.media.MediaRouter.Static.Client.this == mClient) {
                            updateClientState();
                        }
                    }
                });
            }
        }
    }

    static android.media.MediaRouter.Static sStatic;

    /**
     * Route type flag for live audio.
     *
     * <p>A device that supports live audio routing will allow the media audio stream
     * to be routed to supported destinations. This can include internal speakers or
     * audio jacks on the device itself, A2DP devices, and more.</p>
     *
     * <p>Once initiated this routing is transparent to the application. All audio
     * played on the media stream will be routed to the selected destination.</p>
     */
    public static final int ROUTE_TYPE_LIVE_AUDIO = 1 << 0;

    /**
     * Route type flag for live video.
     *
     * <p>A device that supports live video routing will allow a mirrored version
     * of the device's primary display or a customized
     * {@link android.app.Presentation Presentation} to be routed to supported destinations.</p>
     *
     * <p>Once initiated, display mirroring is transparent to the application.
     * While remote routing is active the application may use a
     * {@link android.app.Presentation Presentation} to replace the mirrored view
     * on the external display with different content.</p>
     *
     * @see RouteInfo#getPresentationDisplay()
     * @see android.app.Presentation
     */
    public static final int ROUTE_TYPE_LIVE_VIDEO = 1 << 1;

    /**
     * Temporary interop constant to identify remote displays.
     *
     * @unknown To be removed when media router API is updated.
     */
    public static final int ROUTE_TYPE_REMOTE_DISPLAY = 1 << 2;

    /**
     * Route type flag for application-specific usage.
     *
     * <p>Unlike other media route types, user routes are managed by the application.
     * The MediaRouter will manage and dispatch events for user routes, but the application
     * is expected to interpret the meaning of these events and perform the requested
     * routing tasks.</p>
     */
    public static final int ROUTE_TYPE_USER = 1 << 23;

    static final int ROUTE_TYPE_ANY = ((android.media.MediaRouter.ROUTE_TYPE_LIVE_AUDIO | android.media.MediaRouter.ROUTE_TYPE_LIVE_VIDEO) | android.media.MediaRouter.ROUTE_TYPE_REMOTE_DISPLAY) | android.media.MediaRouter.ROUTE_TYPE_USER;

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
     * </p>
     */
    public static final int CALLBACK_FLAG_PERFORM_ACTIVE_SCAN = 1 << 0;

    /**
     * Flag for {@link #addCallback}: Do not filter route events.
     * <p>
     * When this flag is specified, the callback will be invoked for event that affect any
     * route even if they do not match the callback's filter.
     * </p>
     */
    public static final int CALLBACK_FLAG_UNFILTERED_EVENTS = 1 << 1;

    /**
     * Explicitly requests discovery.
     *
     * @unknown Future API ported from support library.  Revisit this later.
     */
    public static final int CALLBACK_FLAG_REQUEST_DISCOVERY = 1 << 2;

    /**
     * Requests that discovery be performed but only if there is some other active
     * callback already registered.
     *
     * @unknown Compatibility workaround for the fact that applications do not currently
    request discovery explicitly (except when using the support library API).
     */
    public static final int CALLBACK_FLAG_PASSIVE_DISCOVERY = 1 << 3;

    /**
     * Flag for {@link #isRouteAvailable}: Ignore the default route.
     * <p>
     * This flag is used to determine whether a matching non-default route is available.
     * This constraint may be used to decide whether to offer the route chooser dialog
     * to the user.  There is no point offering the chooser if there are no
     * non-default choices.
     * </p>
     *
     * @unknown Future API ported from support library.  Revisit this later.
     */
    public static final int AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE = 1 << 0;

    // Maps application contexts
    static final java.util.HashMap<android.content.Context, android.media.MediaRouter> sRouters = new java.util.HashMap<android.content.Context, android.media.MediaRouter>();

    static java.lang.String typesToString(int types) {
        final java.lang.StringBuilder result = new java.lang.StringBuilder();
        if ((types & android.media.MediaRouter.ROUTE_TYPE_LIVE_AUDIO) != 0) {
            result.append("ROUTE_TYPE_LIVE_AUDIO ");
        }
        if ((types & android.media.MediaRouter.ROUTE_TYPE_LIVE_VIDEO) != 0) {
            result.append("ROUTE_TYPE_LIVE_VIDEO ");
        }
        if ((types & android.media.MediaRouter.ROUTE_TYPE_REMOTE_DISPLAY) != 0) {
            result.append("ROUTE_TYPE_REMOTE_DISPLAY ");
        }
        if ((types & android.media.MediaRouter.ROUTE_TYPE_USER) != 0) {
            result.append("ROUTE_TYPE_USER ");
        }
        return result.toString();
    }

    /**
     *
     *
     * @unknown 
     */
    public MediaRouter(android.content.Context context) {
        synchronized(android.media.MediaRouter.Static.class) {
            if (android.media.MediaRouter.sStatic == null) {
                final android.content.Context appContext = context.getApplicationContext();
                android.media.MediaRouter.sStatic = new android.media.MediaRouter.Static(appContext);
                android.media.MediaRouter.sStatic.startMonitoringRoutes(appContext);
            }
        }
    }

    /**
     * Gets the default route for playing media content on the system.
     * <p>
     * The system always provides a default route.
     * </p>
     *
     * @return The default route, which is guaranteed to never be null.
     */
    public android.media.MediaRouter.RouteInfo getDefaultRoute() {
        return android.media.MediaRouter.sStatic.mDefaultAudioVideo;
    }

    /**
     *
     *
     * @unknown for use by framework routing UI
     */
    public android.media.MediaRouter.RouteCategory getSystemCategory() {
        return android.media.MediaRouter.sStatic.mSystemCategory;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.media.MediaRouter.RouteInfo getSelectedRoute() {
        return getSelectedRoute(android.media.MediaRouter.ROUTE_TYPE_ANY);
    }

    /**
     * Return the currently selected route for any of the given types
     *
     * @param type
     * 		route types
     * @return the selected route
     */
    public android.media.MediaRouter.RouteInfo getSelectedRoute(int type) {
        if ((android.media.MediaRouter.sStatic.mSelectedRoute != null) && ((android.media.MediaRouter.sStatic.mSelectedRoute.mSupportedTypes & type) != 0)) {
            // If the selected route supports any of the types supplied, it's still considered
            // 'selected' for that type.
            return android.media.MediaRouter.sStatic.mSelectedRoute;
        } else
            if (type == android.media.MediaRouter.ROUTE_TYPE_USER) {
                // The caller specifically asked for a user route and the currently selected route
                // doesn't qualify.
                return null;
            }

        // If the above didn't match and we're not specifically asking for a user route,
        // consider the default selected.
        return android.media.MediaRouter.sStatic.mDefaultAudioVideo;
    }

    /**
     * Returns true if there is a route that matches the specified types.
     * <p>
     * This method returns true if there are any available routes that match the types
     * regardless of whether they are enabled or disabled.  If the
     * {@link #AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE} flag is specified, then
     * the method will only consider non-default routes.
     * </p>
     *
     * @param types
     * 		The types to match.
     * @param flags
     * 		Flags to control the determination of whether a route may be available.
     * 		May be zero or {@link #AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE}.
     * @return True if a matching route may be available.
     * @unknown Future API ported from support library.  Revisit this later.
     */
    public boolean isRouteAvailable(int types, int flags) {
        final int count = android.media.MediaRouter.sStatic.mRoutes.size();
        for (int i = 0; i < count; i++) {
            android.media.MediaRouter.RouteInfo route = android.media.MediaRouter.sStatic.mRoutes.get(i);
            if (route.matchesTypes(types)) {
                if (((flags & android.media.MediaRouter.AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE) == 0) || (route != android.media.MediaRouter.sStatic.mDefaultAudioVideo)) {
                    return true;
                }
            }
        }
        // It doesn't look like we can find a matching route right now.
        return false;
    }

    /**
     * Add a callback to listen to events about specific kinds of media routes.
     * If the specified callback is already registered, its registration will be updated for any
     * additional route types specified.
     * <p>
     * This is a convenience method that has the same effect as calling
     * {@link #addCallback(int, Callback, int)} without flags.
     * </p>
     *
     * @param types
     * 		Types of routes this callback is interested in
     * @param cb
     * 		Callback to add
     */
    public void addCallback(int types, android.media.MediaRouter.Callback cb) {
        addCallback(types, cb, 0);
    }

    /**
     * Add a callback to listen to events about specific kinds of media routes.
     * If the specified callback is already registered, its registration will be updated for any
     * additional route types specified.
     * <p>
     * By default, the callback will only be invoked for events that affect routes
     * that match the specified selector.  The filtering may be disabled by specifying
     * the {@link #CALLBACK_FLAG_UNFILTERED_EVENTS} flag.
     * </p>
     *
     * @param types
     * 		Types of routes this callback is interested in
     * @param cb
     * 		Callback to add
     * @param flags
     * 		Flags to control the behavior of the callback.
     * 		May be zero or a combination of {@link #CALLBACK_FLAG_PERFORM_ACTIVE_SCAN} and
     * 		{@link #CALLBACK_FLAG_UNFILTERED_EVENTS}.
     */
    public void addCallback(int types, android.media.MediaRouter.Callback cb, int flags) {
        android.media.MediaRouter.CallbackInfo info;
        int index = findCallbackInfo(cb);
        if (index >= 0) {
            info = android.media.MediaRouter.sStatic.mCallbacks.get(index);
            info.type |= types;
            info.flags |= flags;
        } else {
            info = new android.media.MediaRouter.CallbackInfo(cb, types, flags, this);
            android.media.MediaRouter.sStatic.mCallbacks.add(info);
        }
        android.media.MediaRouter.sStatic.updateDiscoveryRequest();
    }

    /**
     * Remove the specified callback. It will no longer receive events about media routing.
     *
     * @param cb
     * 		Callback to remove
     */
    public void removeCallback(android.media.MediaRouter.Callback cb) {
        int index = findCallbackInfo(cb);
        if (index >= 0) {
            android.media.MediaRouter.sStatic.mCallbacks.remove(index);
            android.media.MediaRouter.sStatic.updateDiscoveryRequest();
        } else {
            android.util.Log.w(android.media.MediaRouter.TAG, ("removeCallback(" + cb) + "): callback not registered");
        }
    }

    private int findCallbackInfo(android.media.MediaRouter.Callback cb) {
        final int count = android.media.MediaRouter.sStatic.mCallbacks.size();
        for (int i = 0; i < count; i++) {
            final android.media.MediaRouter.CallbackInfo info = android.media.MediaRouter.sStatic.mCallbacks.get(i);
            if (info.cb == cb) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Select the specified route to use for output of the given media types.
     * <p class="note">
     * As API version 18, this function may be used to select any route.
     * In prior versions, this function could only be used to select user
     * routes and would ignore any attempt to select a system route.
     * </p>
     *
     * @param types
     * 		type flags indicating which types this route should be used for.
     * 		The route must support at least a subset.
     * @param route
     * 		Route to select
     * @throws IllegalArgumentException
     * 		if the given route is {@code null}
     */
    public void selectRoute(int types, @android.annotation.NonNull
    android.media.MediaRouter.RouteInfo route) {
        if (route == null) {
            throw new java.lang.IllegalArgumentException("Route cannot be null.");
        }
        android.media.MediaRouter.selectRouteStatic(types, route, true);
    }

    /**
     *
     *
     * @unknown internal use
     */
    public void selectRouteInt(int types, android.media.MediaRouter.RouteInfo route, boolean explicit) {
        android.media.MediaRouter.selectRouteStatic(types, route, explicit);
    }

    static void selectRouteStatic(int types, @android.annotation.NonNull
    android.media.MediaRouter.RouteInfo route, boolean explicit) {
        android.util.Log.v(android.media.MediaRouter.TAG, "Selecting route: " + route);
        assert route != null;
        final android.media.MediaRouter.RouteInfo oldRoute = android.media.MediaRouter.sStatic.mSelectedRoute;
        if (oldRoute == route)
            return;

        if (!route.matchesTypes(types)) {
            android.util.Log.w(android.media.MediaRouter.TAG, (("selectRoute ignored; cannot select route with supported types " + android.media.MediaRouter.typesToString(route.getSupportedTypes())) + " into route types ") + android.media.MediaRouter.typesToString(types));
            return;
        }
        final android.media.MediaRouter.RouteInfo btRoute = android.media.MediaRouter.sStatic.mBluetoothA2dpRoute;
        if (((btRoute != null) && ((types & android.media.MediaRouter.ROUTE_TYPE_LIVE_AUDIO) != 0)) && ((route == btRoute) || (route == android.media.MediaRouter.sStatic.mDefaultAudioVideo))) {
            try {
                android.media.MediaRouter.sStatic.mAudioService.setBluetoothA2dpOn(route == btRoute);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.media.MediaRouter.TAG, "Error changing Bluetooth A2DP state", e);
            }
        }
        final android.hardware.display.WifiDisplay activeDisplay = android.media.MediaRouter.sStatic.mDisplayService.getWifiDisplayStatus().getActiveDisplay();
        final boolean oldRouteHasAddress = (oldRoute != null) && (oldRoute.mDeviceAddress != null);
        final boolean newRouteHasAddress = route.mDeviceAddress != null;
        if (((activeDisplay != null) || oldRouteHasAddress) || newRouteHasAddress) {
            if (newRouteHasAddress && (!android.media.MediaRouter.matchesDeviceAddress(activeDisplay, route))) {
                if (android.media.MediaRouter.sStatic.mCanConfigureWifiDisplays) {
                    android.media.MediaRouter.sStatic.mDisplayService.connectWifiDisplay(route.mDeviceAddress);
                } else {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Cannot connect to wifi displays because this process " + "is not allowed to do so.");
                }
            } else
                if ((activeDisplay != null) && (!newRouteHasAddress)) {
                    android.media.MediaRouter.sStatic.mDisplayService.disconnectWifiDisplay();
                }

        }
        android.media.MediaRouter.sStatic.setSelectedRoute(route, explicit);
        if (oldRoute != null) {
            android.media.MediaRouter.dispatchRouteUnselected(types & oldRoute.getSupportedTypes(), oldRoute);
            if (oldRoute.resolveStatusCode()) {
                android.media.MediaRouter.dispatchRouteChanged(oldRoute);
            }
        }
        if (route != null) {
            if (route.resolveStatusCode()) {
                android.media.MediaRouter.dispatchRouteChanged(route);
            }
            android.media.MediaRouter.dispatchRouteSelected(types & route.getSupportedTypes(), route);
        }
        // The behavior of active scans may depend on the currently selected route.
        android.media.MediaRouter.sStatic.updateDiscoveryRequest();
    }

    static void selectDefaultRouteStatic() {
        // TODO: Be smarter about the route types here; this selects for all valid.
        if (((android.media.MediaRouter.sStatic.mSelectedRoute != android.media.MediaRouter.sStatic.mBluetoothA2dpRoute) && (android.media.MediaRouter.sStatic.mBluetoothA2dpRoute != null)) && android.media.MediaRouter.sStatic.isBluetoothA2dpOn()) {
            android.media.MediaRouter.selectRouteStatic(android.media.MediaRouter.ROUTE_TYPE_ANY, android.media.MediaRouter.sStatic.mBluetoothA2dpRoute, false);
        } else {
            android.media.MediaRouter.selectRouteStatic(android.media.MediaRouter.ROUTE_TYPE_ANY, android.media.MediaRouter.sStatic.mDefaultAudioVideo, false);
        }
    }

    /**
     * Compare the device address of a display and a route.
     * Nulls/no device address will match another null/no address.
     */
    static boolean matchesDeviceAddress(android.hardware.display.WifiDisplay display, android.media.MediaRouter.RouteInfo info) {
        final boolean routeHasAddress = (info != null) && (info.mDeviceAddress != null);
        if ((display == null) && (!routeHasAddress)) {
            return true;
        }
        if ((display != null) && routeHasAddress) {
            return display.getDeviceAddress().equals(info.mDeviceAddress);
        }
        return false;
    }

    /**
     * Add an app-specified route for media to the MediaRouter.
     * App-specified route definitions are created using {@link #createUserRoute(RouteCategory)}
     *
     * @param info
     * 		Definition of the route to add
     * @see #createUserRoute(RouteCategory)
     * @see #removeUserRoute(UserRouteInfo)
     */
    public void addUserRoute(android.media.MediaRouter.UserRouteInfo info) {
        android.media.MediaRouter.addRouteStatic(info);
    }

    /**
     *
     *
     * @unknown Framework use only
     */
    public void addRouteInt(android.media.MediaRouter.RouteInfo info) {
        android.media.MediaRouter.addRouteStatic(info);
    }

    static void addRouteStatic(android.media.MediaRouter.RouteInfo info) {
        android.util.Log.v(android.media.MediaRouter.TAG, "Adding route: " + info);
        final android.media.MediaRouter.RouteCategory cat = info.getCategory();
        if (!android.media.MediaRouter.sStatic.mCategories.contains(cat)) {
            android.media.MediaRouter.sStatic.mCategories.add(cat);
        }
        if (cat.isGroupable() && (!(info instanceof android.media.MediaRouter.RouteGroup))) {
            // Enforce that any added route in a groupable category must be in a group.
            final android.media.MediaRouter.RouteGroup group = new android.media.MediaRouter.RouteGroup(info.getCategory());
            group.mSupportedTypes = info.mSupportedTypes;
            android.media.MediaRouter.sStatic.mRoutes.add(group);
            android.media.MediaRouter.dispatchRouteAdded(group);
            group.addRoute(info);
            info = group;
        } else {
            android.media.MediaRouter.sStatic.mRoutes.add(info);
            android.media.MediaRouter.dispatchRouteAdded(info);
        }
    }

    /**
     * Remove an app-specified route for media from the MediaRouter.
     *
     * @param info
     * 		Definition of the route to remove
     * @see #addUserRoute(UserRouteInfo)
     */
    public void removeUserRoute(android.media.MediaRouter.UserRouteInfo info) {
        android.media.MediaRouter.removeRouteStatic(info);
    }

    /**
     * Remove all app-specified routes from the MediaRouter.
     *
     * @see #removeUserRoute(UserRouteInfo)
     */
    public void clearUserRoutes() {
        for (int i = 0; i < android.media.MediaRouter.sStatic.mRoutes.size(); i++) {
            final android.media.MediaRouter.RouteInfo info = android.media.MediaRouter.sStatic.mRoutes.get(i);
            // TODO Right now, RouteGroups only ever contain user routes.
            // The code below will need to change if this assumption does.
            if ((info instanceof android.media.MediaRouter.UserRouteInfo) || (info instanceof android.media.MediaRouter.RouteGroup)) {
                android.media.MediaRouter.removeRouteStatic(info);
                i--;
            }
        }
    }

    /**
     *
     *
     * @unknown internal use only
     */
    public void removeRouteInt(android.media.MediaRouter.RouteInfo info) {
        android.media.MediaRouter.removeRouteStatic(info);
    }

    static void removeRouteStatic(android.media.MediaRouter.RouteInfo info) {
        android.util.Log.v(android.media.MediaRouter.TAG, "Removing route: " + info);
        if (android.media.MediaRouter.sStatic.mRoutes.remove(info)) {
            final android.media.MediaRouter.RouteCategory removingCat = info.getCategory();
            final int count = android.media.MediaRouter.sStatic.mRoutes.size();
            boolean found = false;
            for (int i = 0; i < count; i++) {
                final android.media.MediaRouter.RouteCategory cat = android.media.MediaRouter.sStatic.mRoutes.get(i).getCategory();
                if (removingCat == cat) {
                    found = true;
                    break;
                }
            }
            if (info.isSelected()) {
                // Removing the currently selected route? Select the default before we remove it.
                android.media.MediaRouter.selectDefaultRouteStatic();
            }
            if (!found) {
                android.media.MediaRouter.sStatic.mCategories.remove(removingCat);
            }
            android.media.MediaRouter.dispatchRouteRemoved(info);
        }
    }

    /**
     * Return the number of {@link MediaRouter.RouteCategory categories} currently
     * represented by routes known to this MediaRouter.
     *
     * @return the number of unique categories represented by this MediaRouter's known routes
     */
    public int getCategoryCount() {
        return android.media.MediaRouter.sStatic.mCategories.size();
    }

    /**
     * Return the {@link MediaRouter.RouteCategory category} at the given index.
     * Valid indices are in the range [0-getCategoryCount).
     *
     * @param index
     * 		which category to return
     * @return the category at index
     */
    public android.media.MediaRouter.RouteCategory getCategoryAt(int index) {
        return android.media.MediaRouter.sStatic.mCategories.get(index);
    }

    /**
     * Return the number of {@link MediaRouter.RouteInfo routes} currently known
     * to this MediaRouter.
     *
     * @return the number of routes tracked by this router
     */
    public int getRouteCount() {
        return android.media.MediaRouter.sStatic.mRoutes.size();
    }

    /**
     * Return the route at the specified index.
     *
     * @param index
     * 		index of the route to return
     * @return the route at index
     */
    public android.media.MediaRouter.RouteInfo getRouteAt(int index) {
        return android.media.MediaRouter.sStatic.mRoutes.get(index);
    }

    static int getRouteCountStatic() {
        return android.media.MediaRouter.sStatic.mRoutes.size();
    }

    static android.media.MediaRouter.RouteInfo getRouteAtStatic(int index) {
        return android.media.MediaRouter.sStatic.mRoutes.get(index);
    }

    /**
     * Create a new user route that may be modified and registered for use by the application.
     *
     * @param category
     * 		The category the new route will belong to
     * @return A new UserRouteInfo for use by the application
     * @see #addUserRoute(UserRouteInfo)
     * @see #removeUserRoute(UserRouteInfo)
     * @see #createRouteCategory(CharSequence, boolean)
     */
    public android.media.MediaRouter.UserRouteInfo createUserRoute(android.media.MediaRouter.RouteCategory category) {
        return new android.media.MediaRouter.UserRouteInfo(category);
    }

    /**
     * Create a new route category. Each route must belong to a category.
     *
     * @param name
     * 		Name of the new category
     * @param isGroupable
     * 		true if routes in this category may be grouped with one another
     * @return the new RouteCategory
     */
    public android.media.MediaRouter.RouteCategory createRouteCategory(java.lang.CharSequence name, boolean isGroupable) {
        return new android.media.MediaRouter.RouteCategory(name, android.media.MediaRouter.ROUTE_TYPE_USER, isGroupable);
    }

    /**
     * Create a new route category. Each route must belong to a category.
     *
     * @param nameResId
     * 		Resource ID of the name of the new category
     * @param isGroupable
     * 		true if routes in this category may be grouped with one another
     * @return the new RouteCategory
     */
    public android.media.MediaRouter.RouteCategory createRouteCategory(int nameResId, boolean isGroupable) {
        return new android.media.MediaRouter.RouteCategory(nameResId, android.media.MediaRouter.ROUTE_TYPE_USER, isGroupable);
    }

    /**
     * Rebinds the media router to handle routes that belong to the specified user.
     * Requires the interact across users permission to access the routes of another user.
     * <p>
     * This method is a complete hack to work around the singleton nature of the
     * media router when running inside of singleton processes like QuickSettings.
     * This mechanism should be burned to the ground when MediaRouter is redesigned.
     * Ideally the current user would be pulled from the Context but we need to break
     * down MediaRouter.Static before we can get there.
     * </p>
     *
     * @unknown 
     */
    public void rebindAsUser(int userId) {
        android.media.MediaRouter.sStatic.rebindAsUser(userId);
    }

    static void updateRoute(final android.media.MediaRouter.RouteInfo info) {
        android.media.MediaRouter.dispatchRouteChanged(info);
    }

    static void dispatchRouteSelected(int type, android.media.MediaRouter.RouteInfo info) {
        for (android.media.MediaRouter.CallbackInfo cbi : android.media.MediaRouter.sStatic.mCallbacks) {
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRouteSelected(cbi.router, type, info);
            }
        }
    }

    static void dispatchRouteUnselected(int type, android.media.MediaRouter.RouteInfo info) {
        for (android.media.MediaRouter.CallbackInfo cbi : android.media.MediaRouter.sStatic.mCallbacks) {
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRouteUnselected(cbi.router, type, info);
            }
        }
    }

    static void dispatchRouteChanged(android.media.MediaRouter.RouteInfo info) {
        android.media.MediaRouter.dispatchRouteChanged(info, info.mSupportedTypes);
    }

    static void dispatchRouteChanged(android.media.MediaRouter.RouteInfo info, int oldSupportedTypes) {
        android.util.Log.v(android.media.MediaRouter.TAG, "Dispatching route change: " + info);
        final int newSupportedTypes = info.mSupportedTypes;
        for (android.media.MediaRouter.CallbackInfo cbi : android.media.MediaRouter.sStatic.mCallbacks) {
            // Reconstruct some of the history for callbacks that may not have observed
            // all of the events needed to correctly interpret the current state.
            // FIXME: This is a strong signal that we should deprecate route type filtering
            // completely in the future because it can lead to inconsistencies in
            // applications.
            final boolean oldVisibility = cbi.filterRouteEvent(oldSupportedTypes);
            final boolean newVisibility = cbi.filterRouteEvent(newSupportedTypes);
            if ((!oldVisibility) && newVisibility) {
                cbi.cb.onRouteAdded(cbi.router, info);
                if (info.isSelected()) {
                    cbi.cb.onRouteSelected(cbi.router, newSupportedTypes, info);
                }
            }
            if (oldVisibility || newVisibility) {
                cbi.cb.onRouteChanged(cbi.router, info);
            }
            if (oldVisibility && (!newVisibility)) {
                if (info.isSelected()) {
                    cbi.cb.onRouteUnselected(cbi.router, oldSupportedTypes, info);
                }
                cbi.cb.onRouteRemoved(cbi.router, info);
            }
        }
    }

    static void dispatchRouteAdded(android.media.MediaRouter.RouteInfo info) {
        for (android.media.MediaRouter.CallbackInfo cbi : android.media.MediaRouter.sStatic.mCallbacks) {
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRouteAdded(cbi.router, info);
            }
        }
    }

    static void dispatchRouteRemoved(android.media.MediaRouter.RouteInfo info) {
        for (android.media.MediaRouter.CallbackInfo cbi : android.media.MediaRouter.sStatic.mCallbacks) {
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRouteRemoved(cbi.router, info);
            }
        }
    }

    static void dispatchRouteGrouped(android.media.MediaRouter.RouteInfo info, android.media.MediaRouter.RouteGroup group, int index) {
        for (android.media.MediaRouter.CallbackInfo cbi : android.media.MediaRouter.sStatic.mCallbacks) {
            if (cbi.filterRouteEvent(group)) {
                cbi.cb.onRouteGrouped(cbi.router, info, group, index);
            }
        }
    }

    static void dispatchRouteUngrouped(android.media.MediaRouter.RouteInfo info, android.media.MediaRouter.RouteGroup group) {
        for (android.media.MediaRouter.CallbackInfo cbi : android.media.MediaRouter.sStatic.mCallbacks) {
            if (cbi.filterRouteEvent(group)) {
                cbi.cb.onRouteUngrouped(cbi.router, info, group);
            }
        }
    }

    static void dispatchRouteVolumeChanged(android.media.MediaRouter.RouteInfo info) {
        for (android.media.MediaRouter.CallbackInfo cbi : android.media.MediaRouter.sStatic.mCallbacks) {
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRouteVolumeChanged(cbi.router, info);
            }
        }
    }

    static void dispatchRoutePresentationDisplayChanged(android.media.MediaRouter.RouteInfo info) {
        for (android.media.MediaRouter.CallbackInfo cbi : android.media.MediaRouter.sStatic.mCallbacks) {
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRoutePresentationDisplayChanged(cbi.router, info);
            }
        }
    }

    static void systemVolumeChanged(int newValue) {
        final android.media.MediaRouter.RouteInfo selectedRoute = android.media.MediaRouter.sStatic.mSelectedRoute;
        if (selectedRoute == null)
            return;

        if ((selectedRoute == android.media.MediaRouter.sStatic.mBluetoothA2dpRoute) || (selectedRoute == android.media.MediaRouter.sStatic.mDefaultAudioVideo)) {
            android.media.MediaRouter.dispatchRouteVolumeChanged(selectedRoute);
        } else
            if (android.media.MediaRouter.sStatic.mBluetoothA2dpRoute != null) {
                try {
                    android.media.MediaRouter.dispatchRouteVolumeChanged(android.media.MediaRouter.sStatic.mAudioService.isBluetoothA2dpOn() ? android.media.MediaRouter.sStatic.mBluetoothA2dpRoute : android.media.MediaRouter.sStatic.mDefaultAudioVideo);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Error checking Bluetooth A2DP state to report volume change", e);
                }
            } else {
                android.media.MediaRouter.dispatchRouteVolumeChanged(android.media.MediaRouter.sStatic.mDefaultAudioVideo);
            }

    }

    static void updateWifiDisplayStatus(android.hardware.display.WifiDisplayStatus status) {
        android.hardware.display.WifiDisplay[] displays;
        android.hardware.display.WifiDisplay activeDisplay;
        if (status.getFeatureState() == android.hardware.display.WifiDisplayStatus.FEATURE_STATE_ON) {
            displays = status.getDisplays();
            activeDisplay = status.getActiveDisplay();
            // Only the system is able to connect to wifi display routes.
            // The display manager will enforce this with a permission check but it
            // still publishes information about all available displays.
            // Filter the list down to just the active display.
            if (!android.media.MediaRouter.sStatic.mCanConfigureWifiDisplays) {
                if (activeDisplay != null) {
                    displays = new android.hardware.display.WifiDisplay[]{ activeDisplay };
                } else {
                    displays = android.hardware.display.WifiDisplay.EMPTY_ARRAY;
                }
            }
        } else {
            displays = android.hardware.display.WifiDisplay.EMPTY_ARRAY;
            activeDisplay = null;
        }
        java.lang.String activeDisplayAddress = (activeDisplay != null) ? activeDisplay.getDeviceAddress() : null;
        // Add or update routes.
        for (int i = 0; i < displays.length; i++) {
            final android.hardware.display.WifiDisplay d = displays[i];
            if (android.media.MediaRouter.shouldShowWifiDisplay(d, activeDisplay)) {
                android.media.MediaRouter.RouteInfo route = android.media.MediaRouter.findWifiDisplayRoute(d);
                if (route == null) {
                    route = android.media.MediaRouter.makeWifiDisplayRoute(d, status);
                    android.media.MediaRouter.addRouteStatic(route);
                } else {
                    java.lang.String address = d.getDeviceAddress();
                    boolean disconnected = (!address.equals(activeDisplayAddress)) && address.equals(android.media.MediaRouter.sStatic.mPreviousActiveWifiDisplayAddress);
                    android.media.MediaRouter.updateWifiDisplayRoute(route, d, status, disconnected);
                }
                if (d.equals(activeDisplay)) {
                    android.media.MediaRouter.selectRouteStatic(route.getSupportedTypes(), route, false);
                }
            }
        }
        // Remove stale routes.
        for (int i = android.media.MediaRouter.sStatic.mRoutes.size(); (i--) > 0;) {
            android.media.MediaRouter.RouteInfo route = android.media.MediaRouter.sStatic.mRoutes.get(i);
            if (route.mDeviceAddress != null) {
                android.hardware.display.WifiDisplay d = android.media.MediaRouter.findWifiDisplay(displays, route.mDeviceAddress);
                if ((d == null) || (!android.media.MediaRouter.shouldShowWifiDisplay(d, activeDisplay))) {
                    android.media.MediaRouter.removeRouteStatic(route);
                }
            }
        }
        // Remember the current active wifi display address so that we can infer disconnections.
        // TODO: This hack will go away once all of this is moved into the media router service.
        android.media.MediaRouter.sStatic.mPreviousActiveWifiDisplayAddress = activeDisplayAddress;
    }

    private static boolean shouldShowWifiDisplay(android.hardware.display.WifiDisplay d, android.hardware.display.WifiDisplay activeDisplay) {
        return d.isRemembered() || d.equals(activeDisplay);
    }

    static int getWifiDisplayStatusCode(android.hardware.display.WifiDisplay d, android.hardware.display.WifiDisplayStatus wfdStatus) {
        int newStatus;
        if (wfdStatus.getScanState() == android.hardware.display.WifiDisplayStatus.SCAN_STATE_SCANNING) {
            newStatus = android.media.MediaRouter.RouteInfo.STATUS_SCANNING;
        } else
            if (d.isAvailable()) {
                newStatus = (d.canConnect()) ? android.media.MediaRouter.RouteInfo.STATUS_AVAILABLE : android.media.MediaRouter.RouteInfo.STATUS_IN_USE;
            } else {
                newStatus = android.media.MediaRouter.RouteInfo.STATUS_NOT_AVAILABLE;
            }

        if (d.equals(wfdStatus.getActiveDisplay())) {
            final int activeState = wfdStatus.getActiveDisplayState();
            switch (activeState) {
                case android.hardware.display.WifiDisplayStatus.DISPLAY_STATE_CONNECTED :
                    newStatus = android.media.MediaRouter.RouteInfo.STATUS_CONNECTED;
                    break;
                case android.hardware.display.WifiDisplayStatus.DISPLAY_STATE_CONNECTING :
                    newStatus = android.media.MediaRouter.RouteInfo.STATUS_CONNECTING;
                    break;
                case android.hardware.display.WifiDisplayStatus.DISPLAY_STATE_NOT_CONNECTED :
                    android.util.Log.e(android.media.MediaRouter.TAG, "Active display is not connected!");
                    break;
            }
        }
        return newStatus;
    }

    static boolean isWifiDisplayEnabled(android.hardware.display.WifiDisplay d, android.hardware.display.WifiDisplayStatus wfdStatus) {
        return d.isAvailable() && (d.canConnect() || d.equals(wfdStatus.getActiveDisplay()));
    }

    static android.media.MediaRouter.RouteInfo makeWifiDisplayRoute(android.hardware.display.WifiDisplay display, android.hardware.display.WifiDisplayStatus wfdStatus) {
        final android.media.MediaRouter.RouteInfo newRoute = new android.media.MediaRouter.RouteInfo(android.media.MediaRouter.sStatic.mSystemCategory);
        newRoute.mDeviceAddress = display.getDeviceAddress();
        newRoute.mSupportedTypes = (android.media.MediaRouter.ROUTE_TYPE_LIVE_AUDIO | android.media.MediaRouter.ROUTE_TYPE_LIVE_VIDEO) | android.media.MediaRouter.ROUTE_TYPE_REMOTE_DISPLAY;
        newRoute.mVolumeHandling = android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED;
        newRoute.mPlaybackType = android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE;
        newRoute.setRealStatusCode(android.media.MediaRouter.getWifiDisplayStatusCode(display, wfdStatus));
        newRoute.mEnabled = android.media.MediaRouter.isWifiDisplayEnabled(display, wfdStatus);
        newRoute.mName = display.getFriendlyDisplayName();
        newRoute.mDescription = android.media.MediaRouter.sStatic.mResources.getText(com.android.internal.R.string.wireless_display_route_description);
        newRoute.updatePresentationDisplay();
        newRoute.mDeviceType = android.media.MediaRouter.RouteInfo.DEVICE_TYPE_TV;
        return newRoute;
    }

    private static void updateWifiDisplayRoute(android.media.MediaRouter.RouteInfo route, android.hardware.display.WifiDisplay display, android.hardware.display.WifiDisplayStatus wfdStatus, boolean disconnected) {
        boolean changed = false;
        final java.lang.String newName = display.getFriendlyDisplayName();
        if (!route.getName().equals(newName)) {
            route.mName = newName;
            changed = true;
        }
        boolean enabled = android.media.MediaRouter.isWifiDisplayEnabled(display, wfdStatus);
        changed |= route.mEnabled != enabled;
        route.mEnabled = enabled;
        changed |= route.setRealStatusCode(android.media.MediaRouter.getWifiDisplayStatusCode(display, wfdStatus));
        if (changed) {
            android.media.MediaRouter.dispatchRouteChanged(route);
        }
        if (((!enabled) || disconnected) && route.isSelected()) {
            // Oops, no longer available. Reselect the default.
            android.media.MediaRouter.selectDefaultRouteStatic();
        }
    }

    private static android.hardware.display.WifiDisplay findWifiDisplay(android.hardware.display.WifiDisplay[] displays, java.lang.String deviceAddress) {
        for (int i = 0; i < displays.length; i++) {
            final android.hardware.display.WifiDisplay d = displays[i];
            if (d.getDeviceAddress().equals(deviceAddress)) {
                return d;
            }
        }
        return null;
    }

    private static android.media.MediaRouter.RouteInfo findWifiDisplayRoute(android.hardware.display.WifiDisplay d) {
        final int count = android.media.MediaRouter.sStatic.mRoutes.size();
        for (int i = 0; i < count; i++) {
            final android.media.MediaRouter.RouteInfo info = android.media.MediaRouter.sStatic.mRoutes.get(i);
            if (d.getDeviceAddress().equals(info.mDeviceAddress)) {
                return info;
            }
        }
        return null;
    }

    /**
     * Information about a media route.
     */
    public static class RouteInfo {
        java.lang.CharSequence mName;

        int mNameResId;

        java.lang.CharSequence mDescription;

        private java.lang.CharSequence mStatus;

        int mSupportedTypes;

        int mDeviceType;

        android.media.MediaRouter.RouteGroup mGroup;

        final android.media.MediaRouter.RouteCategory mCategory;

        android.graphics.drawable.Drawable mIcon;

        // playback information
        int mPlaybackType = android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_LOCAL;

        int mVolumeMax = android.media.RemoteControlClient.DEFAULT_PLAYBACK_VOLUME;

        int mVolume = android.media.RemoteControlClient.DEFAULT_PLAYBACK_VOLUME;

        int mVolumeHandling = android.media.RemoteControlClient.DEFAULT_PLAYBACK_VOLUME_HANDLING;

        int mPlaybackStream = android.media.AudioManager.STREAM_MUSIC;

        android.media.MediaRouter.VolumeCallbackInfo mVcb;

        android.view.Display mPresentationDisplay;

        int mPresentationDisplayId = -1;

        java.lang.String mDeviceAddress;

        boolean mEnabled = true;

        // An id by which the route is known to the media router service.
        // Null if this route only exists as an artifact within this process.
        java.lang.String mGlobalRouteId;

        // A predetermined connection status that can override mStatus
        private int mRealStatusCode;

        private int mResolvedStatusCode;

        /**
         *
         *
         * @unknown 
         */
        public static final int STATUS_NONE = 0;

        /**
         *
         *
         * @unknown 
         */
        public static final int STATUS_SCANNING = 1;

        /**
         *
         *
         * @unknown 
         */
        public static final int STATUS_CONNECTING = 2;

        /**
         *
         *
         * @unknown 
         */
        public static final int STATUS_AVAILABLE = 3;

        /**
         *
         *
         * @unknown 
         */
        public static final int STATUS_NOT_AVAILABLE = 4;

        /**
         *
         *
         * @unknown 
         */
        public static final int STATUS_IN_USE = 5;

        /**
         *
         *
         * @unknown 
         */
        public static final int STATUS_CONNECTED = 6;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.IntDef({ android.media.MediaRouter.RouteInfo.DEVICE_TYPE_UNKNOWN, android.media.MediaRouter.RouteInfo.DEVICE_TYPE_TV, android.media.MediaRouter.RouteInfo.DEVICE_TYPE_SPEAKER, android.media.MediaRouter.RouteInfo.DEVICE_TYPE_BLUETOOTH })
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface DeviceType {}

        /**
         * The default receiver device type of the route indicating the type is unknown.
         *
         * @see #getDeviceType
         */
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
         */
        public static final int DEVICE_TYPE_BLUETOOTH = 3;

        private java.lang.Object mTag;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.IntDef({ android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_LOCAL, android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE })
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface PlaybackType {}

        /**
         * The default playback type, "local", indicating the presentation of the media is happening
         * on the same device (e&#46;g&#46; a phone, a tablet) as where it is controlled from.
         *
         * @see #getPlaybackType()
         */
        public static final int PLAYBACK_TYPE_LOCAL = 0;

        /**
         * A playback type indicating the presentation of the media is happening on
         * a different device (i&#46;e&#46; the remote device) than where it is controlled from.
         *
         * @see #getPlaybackType()
         */
        public static final int PLAYBACK_TYPE_REMOTE = 1;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.IntDef({ android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED, android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE })
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        private @interface PlaybackVolume {}

        /**
         * Playback information indicating the playback volume is fixed, i&#46;e&#46; it cannot be
         * controlled from this object. An example of fixed playback volume is a remote player,
         * playing over HDMI where the user prefers to control the volume on the HDMI sink, rather
         * than attenuate at the source.
         *
         * @see #getVolumeHandling()
         */
        public static final int PLAYBACK_VOLUME_FIXED = 0;

        /**
         * Playback information indicating the playback volume is variable and can be controlled
         * from this object.
         *
         * @see #getVolumeHandling()
         */
        public static final int PLAYBACK_VOLUME_VARIABLE = 1;

        RouteInfo(android.media.MediaRouter.RouteCategory category) {
            mCategory = category;
            mDeviceType = android.media.MediaRouter.RouteInfo.DEVICE_TYPE_UNKNOWN;
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
        public java.lang.CharSequence getName() {
            return getName(android.media.MediaRouter.sStatic.mResources);
        }

        /**
         * Return the properly localized/resource user-visible name of this route.
         * <p>
         * The route name identifies the destination represented by the route.
         * It may be a user-supplied name, an alias, or device serial number.
         * </p>
         *
         * @param context
         * 		Context used to resolve the correct configuration to load
         * @return The user-visible name of a media route.  This is the string presented
        to users who may select this as the active route.
         */
        public java.lang.CharSequence getName(android.content.Context context) {
            return getName(context.getResources());
        }

        java.lang.CharSequence getName(android.content.res.Resources res) {
            if (mNameResId != 0) {
                return mName = res.getText(mNameResId);
            }
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
        public java.lang.CharSequence getDescription() {
            return mDescription;
        }

        /**
         *
         *
         * @return The user-visible status for a media route. This may include a description
        of the currently playing media, if available.
         */
        public java.lang.CharSequence getStatus() {
            return mStatus;
        }

        /**
         * Set this route's status by predetermined status code. If the caller
         * should dispatch a route changed event this call will return true;
         */
        boolean setRealStatusCode(int statusCode) {
            if (mRealStatusCode != statusCode) {
                mRealStatusCode = statusCode;
                return resolveStatusCode();
            }
            return false;
        }

        /**
         * Resolves the status code whenever the real status code or selection state
         * changes.
         */
        boolean resolveStatusCode() {
            int statusCode = mRealStatusCode;
            if (isSelected()) {
                switch (statusCode) {
                    // If the route is selected and its status appears to be between states
                    // then report it as connecting even though it has not yet had a chance
                    // to officially move into the CONNECTING state.  Note that routes in
                    // the NONE state are assumed to not require an explicit connection
                    // lifecycle whereas those that are AVAILABLE are assumed to have
                    // to eventually proceed to CONNECTED.
                    case android.media.MediaRouter.RouteInfo.STATUS_AVAILABLE :
                    case android.media.MediaRouter.RouteInfo.STATUS_SCANNING :
                        statusCode = android.media.MediaRouter.RouteInfo.STATUS_CONNECTING;
                        break;
                }
            }
            if (mResolvedStatusCode == statusCode) {
                return false;
            }
            mResolvedStatusCode = statusCode;
            int resId;
            switch (statusCode) {
                case android.media.MediaRouter.RouteInfo.STATUS_SCANNING :
                    resId = com.android.internal.R.string.media_route_status_scanning;
                    break;
                case android.media.MediaRouter.RouteInfo.STATUS_CONNECTING :
                    resId = com.android.internal.R.string.media_route_status_connecting;
                    break;
                case android.media.MediaRouter.RouteInfo.STATUS_AVAILABLE :
                    resId = com.android.internal.R.string.media_route_status_available;
                    break;
                case android.media.MediaRouter.RouteInfo.STATUS_NOT_AVAILABLE :
                    resId = com.android.internal.R.string.media_route_status_not_available;
                    break;
                case android.media.MediaRouter.RouteInfo.STATUS_IN_USE :
                    resId = com.android.internal.R.string.media_route_status_in_use;
                    break;
                case android.media.MediaRouter.RouteInfo.STATUS_CONNECTED :
                case android.media.MediaRouter.RouteInfo.STATUS_NONE :
                default :
                    resId = 0;
                    break;
            }
            mStatus = (resId != 0) ? android.media.MediaRouter.sStatic.mResources.getText(resId) : null;
            return true;
        }

        /**
         *
         *
         * @unknown 
         */
        public int getStatusCode() {
            return mResolvedStatusCode;
        }

        /**
         *
         *
         * @return A media type flag set describing which types this route supports.
         */
        public int getSupportedTypes() {
            return mSupportedTypes;
        }

        /**
         * Gets the type of the receiver device associated with this route.
         *
         * @return The type of the receiver device associated with this route:
        {@link #DEVICE_TYPE_BLUETOOTH}, {@link #DEVICE_TYPE_TV}, {@link #DEVICE_TYPE_SPEAKER},
        or {@link #DEVICE_TYPE_UNKNOWN}.
         */
        @android.media.MediaRouter.RouteInfo.DeviceType
        public int getDeviceType() {
            return mDeviceType;
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean matchesTypes(int types) {
            return (mSupportedTypes & types) != 0;
        }

        /**
         *
         *
         * @return The group that this route belongs to.
         */
        public android.media.MediaRouter.RouteGroup getGroup() {
            return mGroup;
        }

        /**
         *
         *
         * @return the category this route belongs to.
         */
        public android.media.MediaRouter.RouteCategory getCategory() {
            return mCategory;
        }

        /**
         * Get the icon representing this route.
         * This icon will be used in picker UIs if available.
         *
         * @return the icon representing this route or null if no icon is available
         */
        public android.graphics.drawable.Drawable getIconDrawable() {
            return mIcon;
        }

        /**
         * Set an application-specific tag object for this route.
         * The application may use this to store arbitrary data associated with the
         * route for internal tracking.
         *
         * <p>Note that the lifespan of a route may be well past the lifespan of
         * an Activity or other Context; take care that objects you store here
         * will not keep more data in memory alive than you intend.</p>
         *
         * @param tag
         * 		Arbitrary, app-specific data for this route to hold for later use
         */
        public void setTag(java.lang.Object tag) {
            mTag = tag;
            routeUpdated();
        }

        /**
         *
         *
         * @return The tag object previously set by the application
         * @see #setTag(Object)
         */
        public java.lang.Object getTag() {
            return mTag;
        }

        /**
         *
         *
         * @return the type of playback associated with this route
         * @see UserRouteInfo#setPlaybackType(int)
         */
        @android.media.MediaRouter.RouteInfo.PlaybackType
        public int getPlaybackType() {
            return mPlaybackType;
        }

        /**
         *
         *
         * @return the stream over which the playback associated with this route is performed
         * @see UserRouteInfo#setPlaybackStream(int)
         */
        public int getPlaybackStream() {
            return mPlaybackStream;
        }

        /**
         * Return the current volume for this route. Depending on the route, this may only
         * be valid if the route is currently selected.
         *
         * @return the volume at which the playback associated with this route is performed
         * @see UserRouteInfo#setVolume(int)
         */
        public int getVolume() {
            if (mPlaybackType == android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_LOCAL) {
                int vol = 0;
                try {
                    vol = android.media.MediaRouter.sStatic.mAudioService.getStreamVolume(mPlaybackStream);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Error getting local stream volume", e);
                }
                return vol;
            } else {
                return mVolume;
            }
        }

        /**
         * Request a volume change for this route.
         *
         * @param volume
         * 		value between 0 and getVolumeMax
         */
        public void requestSetVolume(int volume) {
            if (mPlaybackType == android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_LOCAL) {
                try {
                    android.media.MediaRouter.sStatic.mAudioService.setStreamVolume(mPlaybackStream, volume, 0, android.app.ActivityThread.currentPackageName());
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Error setting local stream volume", e);
                }
            } else {
                android.media.MediaRouter.sStatic.requestSetVolume(this, volume);
            }
        }

        /**
         * Request an incremental volume update for this route.
         *
         * @param direction
         * 		Delta to apply to the current volume
         */
        public void requestUpdateVolume(int direction) {
            if (mPlaybackType == android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_LOCAL) {
                try {
                    final int volume = java.lang.Math.max(0, java.lang.Math.min(getVolume() + direction, getVolumeMax()));
                    android.media.MediaRouter.sStatic.mAudioService.setStreamVolume(mPlaybackStream, volume, 0, android.app.ActivityThread.currentPackageName());
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Error setting local stream volume", e);
                }
            } else {
                android.media.MediaRouter.sStatic.requestUpdateVolume(this, direction);
            }
        }

        /**
         *
         *
         * @return the maximum volume at which the playback associated with this route is performed
         * @see UserRouteInfo#setVolumeMax(int)
         */
        public int getVolumeMax() {
            if (mPlaybackType == android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_LOCAL) {
                int volMax = 0;
                try {
                    volMax = android.media.MediaRouter.sStatic.mAudioService.getStreamMaxVolume(mPlaybackStream);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Error getting local stream volume", e);
                }
                return volMax;
            } else {
                return mVolumeMax;
            }
        }

        /**
         *
         *
         * @return how volume is handling on the route
         * @see UserRouteInfo#setVolumeHandling(int)
         */
        @android.media.MediaRouter.RouteInfo.PlaybackVolume
        public int getVolumeHandling() {
            return mVolumeHandling;
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
         * This method only makes sense for {@link #ROUTE_TYPE_LIVE_VIDEO live video} routes.
         * </p>
         *
         * @return The preferred presentation display to use when this route is
        selected or null if none.
         * @see #ROUTE_TYPE_LIVE_VIDEO
         * @see android.app.Presentation
         */
        public android.view.Display getPresentationDisplay() {
            return mPresentationDisplay;
        }

        boolean updatePresentationDisplay() {
            android.view.Display display = choosePresentationDisplay();
            if (mPresentationDisplay != display) {
                mPresentationDisplay = display;
                return true;
            }
            return false;
        }

        private android.view.Display choosePresentationDisplay() {
            if ((mSupportedTypes & android.media.MediaRouter.ROUTE_TYPE_LIVE_VIDEO) != 0) {
                android.view.Display[] displays = android.media.MediaRouter.sStatic.getAllPresentationDisplays();
                // Ensure that the specified display is valid for presentations.
                // This check will normally disallow the default display unless it was
                // configured as a presentation display for some reason.
                if (mPresentationDisplayId >= 0) {
                    for (android.view.Display display : displays) {
                        if (display.getDisplayId() == mPresentationDisplayId) {
                            return display;
                        }
                    }
                    return null;
                }
                // Find the indicated Wifi display by its address.
                if (mDeviceAddress != null) {
                    for (android.view.Display display : displays) {
                        if ((display.getType() == android.view.Display.TYPE_WIFI) && mDeviceAddress.equals(display.getAddress())) {
                            return display;
                        }
                    }
                    return null;
                }
                // For the default route, choose the first presentation display from the list.
                if ((this == android.media.MediaRouter.sStatic.mDefaultAudioVideo) && (displays.length > 0)) {
                    return displays[0];
                }
            }
            return null;
        }

        /**
         *
         *
         * @unknown 
         */
        public java.lang.String getDeviceAddress() {
            return mDeviceAddress;
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
            return mResolvedStatusCode == android.media.MediaRouter.RouteInfo.STATUS_CONNECTING;
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isSelected() {
            return this == android.media.MediaRouter.sStatic.mSelectedRoute;
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean isDefault() {
            return this == android.media.MediaRouter.sStatic.mDefaultAudioVideo;
        }

        /**
         *
         *
         * @unknown 
         */
        public void select() {
            android.media.MediaRouter.selectRouteStatic(mSupportedTypes, this, true);
        }

        void setStatusInt(java.lang.CharSequence status) {
            if (!status.equals(mStatus)) {
                mStatus = status;
                if (mGroup != null) {
                    mGroup.memberStatusChanged(this, status);
                }
                routeUpdated();
            }
        }

        final IRemoteVolumeObserver.Stub mRemoteVolObserver = new android.media.IRemoteVolumeObserver.Stub() {
            @java.lang.Override
            public void dispatchRemoteVolumeUpdate(final int direction, final int value) {
                android.media.MediaRouter.sStatic.mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (mVcb != null) {
                            if (direction != 0) {
                                mVcb.vcb.onVolumeUpdateRequest(mVcb.route, direction);
                            } else {
                                mVcb.vcb.onVolumeSetRequest(mVcb.route, value);
                            }
                        }
                    }
                });
            }
        };

        void routeUpdated() {
            android.media.MediaRouter.updateRoute(this);
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.String supportedTypes = android.media.MediaRouter.typesToString(getSupportedTypes());
            return ((((((((((((getClass().getSimpleName() + "{ name=") + getName()) + ", description=") + getDescription()) + ", status=") + getStatus()) + ", category=") + getCategory()) + ", supportedTypes=") + supportedTypes) + ", presentationDisplay=") + mPresentationDisplay) + " }";
        }
    }

    /**
     * Information about a route that the application may define and modify.
     * A user route defaults to {@link RouteInfo#PLAYBACK_TYPE_REMOTE} and
     * {@link RouteInfo#PLAYBACK_VOLUME_FIXED}.
     *
     * @see MediaRouter.RouteInfo
     */
    public static class UserRouteInfo extends android.media.MediaRouter.RouteInfo {
        android.media.RemoteControlClient mRcc;

        android.media.MediaRouter.UserRouteInfo.SessionVolumeProvider mSvp;

        UserRouteInfo(android.media.MediaRouter.RouteCategory category) {
            super(category);
            mSupportedTypes = android.media.MediaRouter.ROUTE_TYPE_USER;
            mPlaybackType = android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE;
            mVolumeHandling = android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED;
        }

        /**
         * Set the user-visible name of this route.
         *
         * @param name
         * 		Name to display to the user to describe this route
         */
        public void setName(java.lang.CharSequence name) {
            mName = name;
            routeUpdated();
        }

        /**
         * Set the user-visible name of this route.
         * <p>
         * The route name identifies the destination represented by the route.
         * It may be a user-supplied name, an alias, or device serial number.
         * </p>
         *
         * @param resId
         * 		Resource ID of the name to display to the user to describe this route
         */
        public void setName(int resId) {
            mNameResId = resId;
            mName = null;
            routeUpdated();
        }

        /**
         * Set the user-visible description of this route.
         * <p>
         * The route description describes the kind of destination represented by the route.
         * It may be a user-supplied string, a model number or brand of device.
         * </p>
         *
         * @param description
         * 		The description of the route, or null if none.
         */
        public void setDescription(java.lang.CharSequence description) {
            mDescription = description;
            routeUpdated();
        }

        /**
         * Set the current user-visible status for this route.
         *
         * @param status
         * 		Status to display to the user to describe what the endpoint
         * 		of this route is currently doing
         */
        public void setStatus(java.lang.CharSequence status) {
            setStatusInt(status);
        }

        /**
         * Set the RemoteControlClient responsible for reporting playback info for this
         * user route.
         *
         * <p>If this route manages remote playback, the data exposed by this
         * RemoteControlClient will be used to reflect and update information
         * such as route volume info in related UIs.</p>
         *
         * <p>The RemoteControlClient must have been previously registered with
         * {@link AudioManager#registerRemoteControlClient(RemoteControlClient)}.</p>
         *
         * @param rcc
         * 		RemoteControlClient associated with this route
         */
        public void setRemoteControlClient(android.media.RemoteControlClient rcc) {
            mRcc = rcc;
            updatePlaybackInfoOnRcc();
        }

        /**
         * Retrieve the RemoteControlClient associated with this route, if one has been set.
         *
         * @return the RemoteControlClient associated with this route
         * @see #setRemoteControlClient(RemoteControlClient)
         */
        public android.media.RemoteControlClient getRemoteControlClient() {
            return mRcc;
        }

        /**
         * Set an icon that will be used to represent this route.
         * The system may use this icon in picker UIs or similar.
         *
         * @param icon
         * 		icon drawable to use to represent this route
         */
        public void setIconDrawable(android.graphics.drawable.Drawable icon) {
            mIcon = icon;
        }

        /**
         * Set an icon that will be used to represent this route.
         * The system may use this icon in picker UIs or similar.
         *
         * @param resId
         * 		Resource ID of an icon drawable to use to represent this route
         */
        public void setIconResource(@android.annotation.DrawableRes
        int resId) {
            setIconDrawable(android.media.MediaRouter.sStatic.mResources.getDrawable(resId));
        }

        /**
         * Set a callback to be notified of volume update requests
         *
         * @param vcb
         * 		
         */
        public void setVolumeCallback(android.media.MediaRouter.VolumeCallback vcb) {
            mVcb = new android.media.MediaRouter.VolumeCallbackInfo(vcb, this);
        }

        /**
         * Defines whether playback associated with this route is "local"
         *    ({@link RouteInfo#PLAYBACK_TYPE_LOCAL}) or "remote"
         *    ({@link RouteInfo#PLAYBACK_TYPE_REMOTE}).
         *
         * @param type
         * 		
         */
        public void setPlaybackType(@android.media.MediaRouter.RouteInfo.PlaybackType
        int type) {
            if (mPlaybackType != type) {
                mPlaybackType = type;
                configureSessionVolume();
            }
        }

        /**
         * Defines whether volume for the playback associated with this route is fixed
         * ({@link RouteInfo#PLAYBACK_VOLUME_FIXED}) or can modified
         * ({@link RouteInfo#PLAYBACK_VOLUME_VARIABLE}).
         *
         * @param volumeHandling
         * 		
         */
        public void setVolumeHandling(@android.media.MediaRouter.RouteInfo.PlaybackVolume
        int volumeHandling) {
            if (mVolumeHandling != volumeHandling) {
                mVolumeHandling = volumeHandling;
                configureSessionVolume();
            }
        }

        /**
         * Defines at what volume the playback associated with this route is performed (for user
         * feedback purposes). This information is only used when the playback is not local.
         *
         * @param volume
         * 		
         */
        public void setVolume(int volume) {
            volume = java.lang.Math.max(0, java.lang.Math.min(volume, getVolumeMax()));
            if (mVolume != volume) {
                mVolume = volume;
                if (mSvp != null) {
                    mSvp.setCurrentVolume(mVolume);
                }
                android.media.MediaRouter.dispatchRouteVolumeChanged(this);
                if (mGroup != null) {
                    mGroup.memberVolumeChanged(this);
                }
            }
        }

        @java.lang.Override
        public void requestSetVolume(int volume) {
            if (mVolumeHandling == android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE) {
                if (mVcb == null) {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Cannot requestSetVolume on user route - no volume callback set");
                    return;
                }
                mVcb.vcb.onVolumeSetRequest(this, volume);
            }
        }

        @java.lang.Override
        public void requestUpdateVolume(int direction) {
            if (mVolumeHandling == android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE) {
                if (mVcb == null) {
                    android.util.Log.e(android.media.MediaRouter.TAG, "Cannot requestChangeVolume on user route - no volumec callback set");
                    return;
                }
                mVcb.vcb.onVolumeUpdateRequest(this, direction);
            }
        }

        /**
         * Defines the maximum volume at which the playback associated with this route is performed
         * (for user feedback purposes). This information is only used when the playback is not
         * local.
         *
         * @param volumeMax
         * 		
         */
        public void setVolumeMax(int volumeMax) {
            if (mVolumeMax != volumeMax) {
                mVolumeMax = volumeMax;
                configureSessionVolume();
            }
        }

        /**
         * Defines over what stream type the media is presented.
         *
         * @param stream
         * 		
         */
        public void setPlaybackStream(int stream) {
            if (mPlaybackStream != stream) {
                mPlaybackStream = stream;
                configureSessionVolume();
            }
        }

        private void updatePlaybackInfoOnRcc() {
            configureSessionVolume();
        }

        private void configureSessionVolume() {
            if (mRcc == null) {
                if (android.media.MediaRouter.DEBUG) {
                    android.util.Log.d(android.media.MediaRouter.TAG, "No Rcc to configure volume for route " + mName);
                }
                return;
            }
            android.media.session.MediaSession session = mRcc.getMediaSession();
            if (session == null) {
                if (android.media.MediaRouter.DEBUG) {
                    android.util.Log.d(android.media.MediaRouter.TAG, "Rcc has no session to configure volume");
                }
                return;
            }
            if (mPlaybackType == android.media.RemoteControlClient.PLAYBACK_TYPE_REMOTE) {
                @android.media.VolumeProvider.ControlType
                int volumeControl = android.media.VolumeProvider.VOLUME_CONTROL_FIXED;
                switch (mVolumeHandling) {
                    case android.media.RemoteControlClient.PLAYBACK_VOLUME_VARIABLE :
                        volumeControl = android.media.VolumeProvider.VOLUME_CONTROL_ABSOLUTE;
                        break;
                    case android.media.RemoteControlClient.PLAYBACK_VOLUME_FIXED :
                    default :
                        break;
                }
                // Only register a new listener if necessary
                if (((mSvp == null) || (mSvp.getVolumeControl() != volumeControl)) || (mSvp.getMaxVolume() != mVolumeMax)) {
                    mSvp = new android.media.MediaRouter.UserRouteInfo.SessionVolumeProvider(volumeControl, mVolumeMax, mVolume);
                    session.setPlaybackToRemote(mSvp);
                }
            } else {
                // We only know how to handle local and remote, fall back to local if not remote.
                android.media.AudioAttributes.Builder bob = new android.media.AudioAttributes.Builder();
                bob.setLegacyStreamType(mPlaybackStream);
                session.setPlaybackToLocal(bob.build());
                mSvp = null;
            }
        }

        class SessionVolumeProvider extends android.media.VolumeProvider {
            public SessionVolumeProvider(@android.media.VolumeProvider.ControlType
            int volumeControl, int maxVolume, int currentVolume) {
                super(volumeControl, maxVolume, currentVolume);
            }

            @java.lang.Override
            public void onSetVolumeTo(final int volume) {
                android.media.MediaRouter.sStatic.mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (mVcb != null) {
                            mVcb.vcb.onVolumeSetRequest(mVcb.route, volume);
                        }
                    }
                });
            }

            @java.lang.Override
            public void onAdjustVolume(final int direction) {
                android.media.MediaRouter.sStatic.mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (mVcb != null) {
                            mVcb.vcb.onVolumeUpdateRequest(mVcb.route, direction);
                        }
                    }
                });
            }
        }
    }

    /**
     * Information about a route that consists of multiple other routes in a group.
     */
    public static class RouteGroup extends android.media.MediaRouter.RouteInfo {
        final java.util.ArrayList<android.media.MediaRouter.RouteInfo> mRoutes = new java.util.ArrayList<android.media.MediaRouter.RouteInfo>();

        private boolean mUpdateName;

        RouteGroup(android.media.MediaRouter.RouteCategory category) {
            super(category);
            mGroup = this;
            mVolumeHandling = android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED;
        }

        @java.lang.Override
        java.lang.CharSequence getName(android.content.res.Resources res) {
            if (mUpdateName)
                updateName();

            return super.getName(res);
        }

        /**
         * Add a route to this group. The route must not currently belong to another group.
         *
         * @param route
         * 		route to add to this group
         */
        public void addRoute(android.media.MediaRouter.RouteInfo route) {
            if (route.getGroup() != null) {
                throw new java.lang.IllegalStateException(("Route " + route) + " is already part of a group.");
            }
            if (route.getCategory() != mCategory) {
                throw new java.lang.IllegalArgumentException((((("Route cannot be added to a group with a different category. " + "(Route category=") + route.getCategory()) + " group category=") + mCategory) + ")");
            }
            final int at = mRoutes.size();
            mRoutes.add(route);
            route.mGroup = this;
            mUpdateName = true;
            updateVolume();
            routeUpdated();
            android.media.MediaRouter.dispatchRouteGrouped(route, this, at);
        }

        /**
         * Add a route to this group before the specified index.
         *
         * @param route
         * 		route to add
         * @param insertAt
         * 		insert the new route before this index
         */
        public void addRoute(android.media.MediaRouter.RouteInfo route, int insertAt) {
            if (route.getGroup() != null) {
                throw new java.lang.IllegalStateException(("Route " + route) + " is already part of a group.");
            }
            if (route.getCategory() != mCategory) {
                throw new java.lang.IllegalArgumentException((((("Route cannot be added to a group with a different category. " + "(Route category=") + route.getCategory()) + " group category=") + mCategory) + ")");
            }
            mRoutes.add(insertAt, route);
            route.mGroup = this;
            mUpdateName = true;
            updateVolume();
            routeUpdated();
            android.media.MediaRouter.dispatchRouteGrouped(route, this, insertAt);
        }

        /**
         * Remove a route from this group.
         *
         * @param route
         * 		route to remove
         */
        public void removeRoute(android.media.MediaRouter.RouteInfo route) {
            if (route.getGroup() != this) {
                throw new java.lang.IllegalArgumentException(("Route " + route) + " is not a member of this group.");
            }
            mRoutes.remove(route);
            route.mGroup = null;
            mUpdateName = true;
            updateVolume();
            android.media.MediaRouter.dispatchRouteUngrouped(route, this);
            routeUpdated();
        }

        /**
         * Remove the route at the specified index from this group.
         *
         * @param index
         * 		index of the route to remove
         */
        public void removeRoute(int index) {
            android.media.MediaRouter.RouteInfo route = mRoutes.remove(index);
            route.mGroup = null;
            mUpdateName = true;
            updateVolume();
            android.media.MediaRouter.dispatchRouteUngrouped(route, this);
            routeUpdated();
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
         * Return the route in this group at the specified index
         *
         * @param index
         * 		Index to fetch
         * @return The route at index
         */
        public android.media.MediaRouter.RouteInfo getRouteAt(int index) {
            return mRoutes.get(index);
        }

        /**
         * Set an icon that will be used to represent this group.
         * The system may use this icon in picker UIs or similar.
         *
         * @param icon
         * 		icon drawable to use to represent this group
         */
        public void setIconDrawable(android.graphics.drawable.Drawable icon) {
            mIcon = icon;
        }

        /**
         * Set an icon that will be used to represent this group.
         * The system may use this icon in picker UIs or similar.
         *
         * @param resId
         * 		Resource ID of an icon drawable to use to represent this group
         */
        public void setIconResource(@android.annotation.DrawableRes
        int resId) {
            setIconDrawable(android.media.MediaRouter.sStatic.mResources.getDrawable(resId));
        }

        @java.lang.Override
        public void requestSetVolume(int volume) {
            final int maxVol = getVolumeMax();
            if (maxVol == 0) {
                return;
            }
            final float scaledVolume = ((float) (volume)) / maxVol;
            final int routeCount = getRouteCount();
            for (int i = 0; i < routeCount; i++) {
                final android.media.MediaRouter.RouteInfo route = getRouteAt(i);
                final int routeVol = ((int) (scaledVolume * route.getVolumeMax()));
                route.requestSetVolume(routeVol);
            }
            if (volume != mVolume) {
                mVolume = volume;
                android.media.MediaRouter.dispatchRouteVolumeChanged(this);
            }
        }

        @java.lang.Override
        public void requestUpdateVolume(int direction) {
            final int maxVol = getVolumeMax();
            if (maxVol == 0) {
                return;
            }
            final int routeCount = getRouteCount();
            int volume = 0;
            for (int i = 0; i < routeCount; i++) {
                final android.media.MediaRouter.RouteInfo route = getRouteAt(i);
                route.requestUpdateVolume(direction);
                final int routeVol = route.getVolume();
                if (routeVol > volume) {
                    volume = routeVol;
                }
            }
            if (volume != mVolume) {
                mVolume = volume;
                android.media.MediaRouter.dispatchRouteVolumeChanged(this);
            }
        }

        void memberNameChanged(android.media.MediaRouter.RouteInfo info, java.lang.CharSequence name) {
            mUpdateName = true;
            routeUpdated();
        }

        void memberStatusChanged(android.media.MediaRouter.RouteInfo info, java.lang.CharSequence status) {
            setStatusInt(status);
        }

        void memberVolumeChanged(android.media.MediaRouter.RouteInfo info) {
            updateVolume();
        }

        void updateVolume() {
            // A group always represents the highest component volume value.
            final int routeCount = getRouteCount();
            int volume = 0;
            for (int i = 0; i < routeCount; i++) {
                final int routeVol = getRouteAt(i).getVolume();
                if (routeVol > volume) {
                    volume = routeVol;
                }
            }
            if (volume != mVolume) {
                mVolume = volume;
                android.media.MediaRouter.dispatchRouteVolumeChanged(this);
            }
        }

        @java.lang.Override
        void routeUpdated() {
            int types = 0;
            final int count = mRoutes.size();
            if (count == 0) {
                // Don't keep empty groups in the router.
                android.media.MediaRouter.removeRouteStatic(this);
                return;
            }
            int maxVolume = 0;
            boolean isLocal = true;
            boolean isFixedVolume = true;
            for (int i = 0; i < count; i++) {
                final android.media.MediaRouter.RouteInfo route = mRoutes.get(i);
                types |= route.mSupportedTypes;
                final int routeMaxVolume = route.getVolumeMax();
                if (routeMaxVolume > maxVolume) {
                    maxVolume = routeMaxVolume;
                }
                isLocal &= route.getPlaybackType() == android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_LOCAL;
                isFixedVolume &= route.getVolumeHandling() == android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED;
            }
            mPlaybackType = (isLocal) ? android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_LOCAL : android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE;
            mVolumeHandling = (isFixedVolume) ? android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED : android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE;
            mSupportedTypes = types;
            mVolumeMax = maxVolume;
            mIcon = (count == 1) ? mRoutes.get(0).getIconDrawable() : null;
            super.routeUpdated();
        }

        void updateName() {
            final java.lang.StringBuilder sb = new java.lang.StringBuilder();
            final int count = mRoutes.size();
            for (int i = 0; i < count; i++) {
                final android.media.MediaRouter.RouteInfo info = mRoutes.get(i);
                // TODO: There's probably a much more correct way to localize this.
                if (i > 0)
                    sb.append(", ");

                sb.append(info.mName);
            }
            mName = sb.toString();
            mUpdateName = false;
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
    }

    /**
     * Definition of a category of routes. All routes belong to a category.
     */
    public static class RouteCategory {
        java.lang.CharSequence mName;

        int mNameResId;

        int mTypes;

        final boolean mGroupable;

        boolean mIsSystem;

        RouteCategory(java.lang.CharSequence name, int types, boolean groupable) {
            mName = name;
            mTypes = types;
            mGroupable = groupable;
        }

        RouteCategory(int nameResId, int types, boolean groupable) {
            mNameResId = nameResId;
            mTypes = types;
            mGroupable = groupable;
        }

        /**
         *
         *
         * @return the name of this route category
         */
        public java.lang.CharSequence getName() {
            return getName(android.media.MediaRouter.sStatic.mResources);
        }

        /**
         * Return the properly localized/configuration dependent name of this RouteCategory.
         *
         * @param context
         * 		Context to resolve name resources
         * @return the name of this route category
         */
        public java.lang.CharSequence getName(android.content.Context context) {
            return getName(context.getResources());
        }

        java.lang.CharSequence getName(android.content.res.Resources res) {
            if (mNameResId != 0) {
                return res.getText(mNameResId);
            }
            return mName;
        }

        /**
         * Return the current list of routes in this category that have been added
         * to the MediaRouter.
         *
         * <p>This list will not include routes that are nested within RouteGroups.
         * A RouteGroup is treated as a single route within its category.</p>
         *
         * @param out
         * 		a List to fill with the routes in this category. If this parameter is
         * 		non-null, it will be cleared, filled with the current routes with this
         * 		category, and returned. If this parameter is null, a new List will be
         * 		allocated to report the category's current routes.
         * @return A list with the routes in this category that have been added to the MediaRouter.
         */
        public java.util.List<android.media.MediaRouter.RouteInfo> getRoutes(java.util.List<android.media.MediaRouter.RouteInfo> out) {
            if (out == null) {
                out = new java.util.ArrayList<android.media.MediaRouter.RouteInfo>();
            } else {
                out.clear();
            }
            final int count = android.media.MediaRouter.getRouteCountStatic();
            for (int i = 0; i < count; i++) {
                final android.media.MediaRouter.RouteInfo route = android.media.MediaRouter.getRouteAtStatic(i);
                if (route.mCategory == this) {
                    out.add(route);
                }
            }
            return out;
        }

        /**
         *
         *
         * @return Flag set describing the route types supported by this category
         */
        public int getSupportedTypes() {
            return mTypes;
        }

        /**
         * Return whether or not this category supports grouping.
         *
         * <p>If this method returns true, all routes obtained from this category
         * via calls to {@link #getRouteAt(int)} will be {@link MediaRouter.RouteGroup}s.</p>
         *
         * @return true if this category supports
         */
        public boolean isGroupable() {
            return mGroupable;
        }

        /**
         *
         *
         * @return true if this is the category reserved for system routes.
         * @unknown 
         */
        public boolean isSystem() {
            return mIsSystem;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((("RouteCategory{ name=" + mName) + " types=") + android.media.MediaRouter.typesToString(mTypes)) + " groupable=") + mGroupable) + " }";
        }
    }

    static class CallbackInfo {
        public int type;

        public int flags;

        public final android.media.MediaRouter.Callback cb;

        public final android.media.MediaRouter router;

        public CallbackInfo(android.media.MediaRouter.Callback cb, int type, int flags, android.media.MediaRouter router) {
            this.cb = cb;
            this.type = type;
            this.flags = flags;
            this.router = router;
        }

        public boolean filterRouteEvent(android.media.MediaRouter.RouteInfo route) {
            return filterRouteEvent(route.mSupportedTypes);
        }

        public boolean filterRouteEvent(int supportedTypes) {
            return ((flags & android.media.MediaRouter.CALLBACK_FLAG_UNFILTERED_EVENTS) != 0) || ((type & supportedTypes) != 0);
        }
    }

    /**
     * Interface for receiving events about media routing changes.
     * All methods of this interface will be called from the application's main thread.
     * <p>
     * A Callback will only receive events relevant to routes that the callback
     * was registered for unless the {@link MediaRouter#CALLBACK_FLAG_UNFILTERED_EVENTS}
     * flag was specified in {@link MediaRouter#addCallback(int, Callback, int)}.
     * </p>
     *
     * @see MediaRouter#addCallback(int, Callback, int)
     * @see MediaRouter#removeCallback(Callback)
     */
    public static abstract class Callback {
        /**
         * Called when the supplied route becomes selected as the active route
         * for the given route type.
         *
         * @param router
         * 		the MediaRouter reporting the event
         * @param type
         * 		Type flag set indicating the routes that have been selected
         * @param info
         * 		Route that has been selected for the given route types
         */
        public abstract void onRouteSelected(android.media.MediaRouter router, int type, android.media.MediaRouter.RouteInfo info);

        /**
         * Called when the supplied route becomes unselected as the active route
         * for the given route type.
         *
         * @param router
         * 		the MediaRouter reporting the event
         * @param type
         * 		Type flag set indicating the routes that have been unselected
         * @param info
         * 		Route that has been unselected for the given route types
         */
        public abstract void onRouteUnselected(android.media.MediaRouter router, int type, android.media.MediaRouter.RouteInfo info);

        /**
         * Called when a route for the specified type was added.
         *
         * @param router
         * 		the MediaRouter reporting the event
         * @param info
         * 		Route that has become available for use
         */
        public abstract void onRouteAdded(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info);

        /**
         * Called when a route for the specified type was removed.
         *
         * @param router
         * 		the MediaRouter reporting the event
         * @param info
         * 		Route that has been removed from availability
         */
        public abstract void onRouteRemoved(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info);

        /**
         * Called when an aspect of the indicated route has changed.
         *
         * <p>This will not indicate that the types supported by this route have
         * changed, only that cosmetic info such as name or status have been updated.</p>
         *
         * @param router
         * 		the MediaRouter reporting the event
         * @param info
         * 		The route that was changed
         */
        public abstract void onRouteChanged(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info);

        /**
         * Called when a route is added to a group.
         *
         * @param router
         * 		the MediaRouter reporting the event
         * @param info
         * 		The route that was added
         * @param group
         * 		The group the route was added to
         * @param index
         * 		The route index within group that info was added at
         */
        public abstract void onRouteGrouped(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info, android.media.MediaRouter.RouteGroup group, int index);

        /**
         * Called when a route is removed from a group.
         *
         * @param router
         * 		the MediaRouter reporting the event
         * @param info
         * 		The route that was removed
         * @param group
         * 		The group the route was removed from
         */
        public abstract void onRouteUngrouped(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info, android.media.MediaRouter.RouteGroup group);

        /**
         * Called when a route's volume changes.
         *
         * @param router
         * 		the MediaRouter reporting the event
         * @param info
         * 		The route with altered volume
         */
        public abstract void onRouteVolumeChanged(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info);

        /**
         * Called when a route's presentation display changes.
         * <p>
         * This method is called whenever the route's presentation display becomes
         * available, is removes or has changes to some of its properties (such as its size).
         * </p>
         *
         * @param router
         * 		the MediaRouter reporting the event
         * @param info
         * 		The route whose presentation display changed
         * @see RouteInfo#getPresentationDisplay()
         */
        public void onRoutePresentationDisplayChanged(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info) {
        }
    }

    /**
     * Stub implementation of {@link MediaRouter.Callback}.
     * Each abstract method is defined as a no-op. Override just the ones
     * you need.
     */
    public static class SimpleCallback extends android.media.MediaRouter.Callback {
        @java.lang.Override
        public void onRouteSelected(android.media.MediaRouter router, int type, android.media.MediaRouter.RouteInfo info) {
        }

        @java.lang.Override
        public void onRouteUnselected(android.media.MediaRouter router, int type, android.media.MediaRouter.RouteInfo info) {
        }

        @java.lang.Override
        public void onRouteAdded(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info) {
        }

        @java.lang.Override
        public void onRouteRemoved(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info) {
        }

        @java.lang.Override
        public void onRouteChanged(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info) {
        }

        @java.lang.Override
        public void onRouteGrouped(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info, android.media.MediaRouter.RouteGroup group, int index) {
        }

        @java.lang.Override
        public void onRouteUngrouped(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info, android.media.MediaRouter.RouteGroup group) {
        }

        @java.lang.Override
        public void onRouteVolumeChanged(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info) {
        }
    }

    static class VolumeCallbackInfo {
        public final android.media.MediaRouter.VolumeCallback vcb;

        public final android.media.MediaRouter.RouteInfo route;

        public VolumeCallbackInfo(android.media.MediaRouter.VolumeCallback vcb, android.media.MediaRouter.RouteInfo route) {
            this.vcb = vcb;
            this.route = route;
        }
    }

    /**
     * Interface for receiving events about volume changes.
     * All methods of this interface will be called from the application's main thread.
     *
     * <p>A VolumeCallback will only receive events relevant to routes that the callback
     * was registered for.</p>
     *
     * @see UserRouteInfo#setVolumeCallback(VolumeCallback)
     */
    public static abstract class VolumeCallback {
        /**
         * Called when the volume for the route should be increased or decreased.
         *
         * @param info
         * 		the route affected by this event
         * @param direction
         * 		an integer indicating whether the volume is to be increased
         * 		(positive value) or decreased (negative value).
         * 		For bundled changes, the absolute value indicates the number of changes
         * 		in the same direction, e.g. +3 corresponds to three "volume up" changes.
         */
        public abstract void onVolumeUpdateRequest(android.media.MediaRouter.RouteInfo info, int direction);

        /**
         * Called when the volume for the route should be set to the given value
         *
         * @param info
         * 		the route affected by this event
         * @param volume
         * 		an integer indicating the new volume value that should be used, always
         * 		between 0 and the value set by {@link UserRouteInfo#setVolumeMax(int)}.
         */
        public abstract void onVolumeSetRequest(android.media.MediaRouter.RouteInfo info, int volume);
    }

    static class VolumeChangeReceiver extends android.content.BroadcastReceiver {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (intent.getAction().equals(android.media.AudioManager.VOLUME_CHANGED_ACTION)) {
                final int streamType = intent.getIntExtra(android.media.AudioManager.EXTRA_VOLUME_STREAM_TYPE, -1);
                if (streamType != android.media.AudioManager.STREAM_MUSIC) {
                    return;
                }
                final int newVolume = intent.getIntExtra(android.media.AudioManager.EXTRA_VOLUME_STREAM_VALUE, 0);
                final int oldVolume = intent.getIntExtra(android.media.AudioManager.EXTRA_PREV_VOLUME_STREAM_VALUE, 0);
                if (newVolume != oldVolume) {
                    android.media.MediaRouter.systemVolumeChanged(newVolume);
                }
            }
        }
    }

    static class WifiDisplayStatusChangedReceiver extends android.content.BroadcastReceiver {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (intent.getAction().equals(android.hardware.display.DisplayManager.ACTION_WIFI_DISPLAY_STATUS_CHANGED)) {
                android.media.MediaRouter.updateWifiDisplayStatus(((android.hardware.display.WifiDisplayStatus) (intent.getParcelableExtra(android.hardware.display.DisplayManager.EXTRA_WIFI_DISPLAY_STATUS))));
            }
        }
    }
}

