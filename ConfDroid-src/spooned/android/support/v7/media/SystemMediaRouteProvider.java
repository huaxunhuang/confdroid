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
 * Provides routes for built-in system destinations such as the local display
 * and speaker.  On Jellybean and newer platform releases, queries the framework
 * MediaRouter for framework-provided routes and registers non-framework-provided
 * routes as user routes.
 */
abstract class SystemMediaRouteProvider extends android.support.v7.media.MediaRouteProvider {
    private static final java.lang.String TAG = "SystemMediaRouteProvider";

    public static final java.lang.String PACKAGE_NAME = "android";

    public static final java.lang.String DEFAULT_ROUTE_ID = "DEFAULT_ROUTE";

    protected SystemMediaRouteProvider(android.content.Context context) {
        super(context, new android.support.v7.media.MediaRouteProvider.ProviderMetadata(new android.content.ComponentName(android.support.v7.media.SystemMediaRouteProvider.PACKAGE_NAME, android.support.v7.media.SystemMediaRouteProvider.class.getName())));
    }

    public static android.support.v7.media.SystemMediaRouteProvider obtain(android.content.Context context, android.support.v7.media.SystemMediaRouteProvider.SyncCallback syncCallback) {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            return new android.support.v7.media.SystemMediaRouteProvider.Api24Impl(context, syncCallback);
        }
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            return new android.support.v7.media.SystemMediaRouteProvider.JellybeanMr2Impl(context, syncCallback);
        }
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            return new android.support.v7.media.SystemMediaRouteProvider.JellybeanMr1Impl(context, syncCallback);
        }
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            return new android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl(context, syncCallback);
        }
        return new android.support.v7.media.SystemMediaRouteProvider.LegacyImpl(context);
    }

    /**
     * Called by the media router when a route is added to synchronize state with
     * the framework media router.
     */
    public void onSyncRouteAdded(android.support.v7.media.MediaRouter.RouteInfo route) {
    }

    /**
     * Called by the media router when a route is removed to synchronize state with
     * the framework media router.
     */
    public void onSyncRouteRemoved(android.support.v7.media.MediaRouter.RouteInfo route) {
    }

    /**
     * Called by the media router when a route is changed to synchronize state with
     * the framework media router.
     */
    public void onSyncRouteChanged(android.support.v7.media.MediaRouter.RouteInfo route) {
    }

    /**
     * Called by the media router when a route is selected to synchronize state with
     * the framework media router.
     */
    public void onSyncRouteSelected(android.support.v7.media.MediaRouter.RouteInfo route) {
    }

    /**
     * Callbacks into the media router to synchronize state with the framework media router.
     */
    public interface SyncCallback {
        public android.support.v7.media.MediaRouter.RouteInfo getSystemRouteByDescriptorId(java.lang.String id);
    }

    /**
     * Legacy implementation for platform versions prior to Jellybean.
     */
    static class LegacyImpl extends android.support.v7.media.SystemMediaRouteProvider {
        static final int PLAYBACK_STREAM = android.media.AudioManager.STREAM_MUSIC;

        private static final java.util.ArrayList<android.content.IntentFilter> CONTROL_FILTERS;

        static {
            android.content.IntentFilter f = new android.content.IntentFilter();
            f.addCategory(android.support.v7.media.MediaControlIntent.CATEGORY_LIVE_AUDIO);
            f.addCategory(android.support.v7.media.MediaControlIntent.CATEGORY_LIVE_VIDEO);
            CONTROL_FILTERS = new java.util.ArrayList<android.content.IntentFilter>();
            android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.CONTROL_FILTERS.add(f);
        }

        final android.media.AudioManager mAudioManager;

        private final android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.VolumeChangeReceiver mVolumeChangeReceiver;

        int mLastReportedVolume = -1;

        public LegacyImpl(android.content.Context context) {
            super(context);
            mAudioManager = ((android.media.AudioManager) (context.getSystemService(android.content.Context.AUDIO_SERVICE)));
            mVolumeChangeReceiver = new android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.VolumeChangeReceiver();
            context.registerReceiver(mVolumeChangeReceiver, new android.content.IntentFilter(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.VolumeChangeReceiver.VOLUME_CHANGED_ACTION));
            publishRoutes();
        }

        void publishRoutes() {
            android.content.res.Resources r = getContext().getResources();
            int maxVolume = mAudioManager.getStreamMaxVolume(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.PLAYBACK_STREAM);
            mLastReportedVolume = mAudioManager.getStreamVolume(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.PLAYBACK_STREAM);
            android.support.v7.media.MediaRouteDescriptor defaultRoute = new android.support.v7.media.MediaRouteDescriptor.Builder(android.support.v7.media.SystemMediaRouteProvider.DEFAULT_ROUTE_ID, r.getString(R.string.mr_system_route_name)).addControlFilters(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.CONTROL_FILTERS).setPlaybackStream(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.PLAYBACK_STREAM).setPlaybackType(android.support.v7.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_LOCAL).setVolumeHandling(android.support.v7.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE).setVolumeMax(maxVolume).setVolume(mLastReportedVolume).build();
            android.support.v7.media.MediaRouteProviderDescriptor providerDescriptor = new android.support.v7.media.MediaRouteProviderDescriptor.Builder().addRoute(defaultRoute).build();
            setDescriptor(providerDescriptor);
        }

        @java.lang.Override
        public android.support.v7.media.MediaRouteProvider.RouteController onCreateRouteController(java.lang.String routeId) {
            if (routeId.equals(android.support.v7.media.SystemMediaRouteProvider.DEFAULT_ROUTE_ID)) {
                return new android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.DefaultRouteController();
            }
            return null;
        }

        final class DefaultRouteController extends android.support.v7.media.MediaRouteProvider.RouteController {
            @java.lang.Override
            public void onSetVolume(int volume) {
                mAudioManager.setStreamVolume(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.PLAYBACK_STREAM, volume, 0);
                publishRoutes();
            }

            @java.lang.Override
            public void onUpdateVolume(int delta) {
                int volume = mAudioManager.getStreamVolume(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.PLAYBACK_STREAM);
                int maxVolume = mAudioManager.getStreamMaxVolume(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.PLAYBACK_STREAM);
                int newVolume = java.lang.Math.min(maxVolume, java.lang.Math.max(0, volume + delta));
                if (newVolume != volume) {
                    mAudioManager.setStreamVolume(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.PLAYBACK_STREAM, volume, 0);
                }
                publishRoutes();
            }
        }

        final class VolumeChangeReceiver extends android.content.BroadcastReceiver {
            // These constants come from AudioManager.
            public static final java.lang.String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";

            public static final java.lang.String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";

            public static final java.lang.String EXTRA_VOLUME_STREAM_VALUE = "android.media.EXTRA_VOLUME_STREAM_VALUE";

            @java.lang.Override
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if (intent.getAction().equals(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.VolumeChangeReceiver.VOLUME_CHANGED_ACTION)) {
                    final int streamType = intent.getIntExtra(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.VolumeChangeReceiver.EXTRA_VOLUME_STREAM_TYPE, -1);
                    if (streamType == android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.PLAYBACK_STREAM) {
                        final int volume = intent.getIntExtra(android.support.v7.media.SystemMediaRouteProvider.LegacyImpl.VolumeChangeReceiver.EXTRA_VOLUME_STREAM_VALUE, -1);
                        if ((volume >= 0) && (volume != mLastReportedVolume)) {
                            publishRoutes();
                        }
                    }
                }
            }
        }
    }

    /**
     * Jellybean implementation.
     */
    static class JellybeanImpl extends android.support.v7.media.SystemMediaRouteProvider implements android.support.v7.media.MediaRouterJellybean.Callback , android.support.v7.media.MediaRouterJellybean.VolumeCallback {
        private static final java.util.ArrayList<android.content.IntentFilter> LIVE_AUDIO_CONTROL_FILTERS;

        static {
            android.content.IntentFilter f = new android.content.IntentFilter();
            f.addCategory(android.support.v7.media.MediaControlIntent.CATEGORY_LIVE_AUDIO);
            LIVE_AUDIO_CONTROL_FILTERS = new java.util.ArrayList<android.content.IntentFilter>();
            android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.LIVE_AUDIO_CONTROL_FILTERS.add(f);
        }

        private static final java.util.ArrayList<android.content.IntentFilter> LIVE_VIDEO_CONTROL_FILTERS;

        static {
            android.content.IntentFilter f = new android.content.IntentFilter();
            f.addCategory(android.support.v7.media.MediaControlIntent.CATEGORY_LIVE_VIDEO);
            LIVE_VIDEO_CONTROL_FILTERS = new java.util.ArrayList<android.content.IntentFilter>();
            android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.LIVE_VIDEO_CONTROL_FILTERS.add(f);
        }

        private final android.support.v7.media.SystemMediaRouteProvider.SyncCallback mSyncCallback;

        protected final java.lang.Object mRouterObj;

        protected final java.lang.Object mCallbackObj;

        protected final java.lang.Object mVolumeCallbackObj;

        protected final java.lang.Object mUserRouteCategoryObj;

        protected int mRouteTypes;

        protected boolean mActiveScan;

        protected boolean mCallbackRegistered;

        // Maintains an association from framework routes to support library routes.
        // Note that we cannot use the tag field for this because an application may
        // have published its own user routes to the framework media router and already
        // used the tag for its own purposes.
        protected final java.util.ArrayList<android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord> mSystemRouteRecords = new java.util.ArrayList<android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord>();

        // Maintains an association from support library routes to framework routes.
        protected final java.util.ArrayList<android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord> mUserRouteRecords = new java.util.ArrayList<android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord>();

        private android.support.v7.media.MediaRouterJellybean.SelectRouteWorkaround mSelectRouteWorkaround;

        private android.support.v7.media.MediaRouterJellybean.GetDefaultRouteWorkaround mGetDefaultRouteWorkaround;

        public JellybeanImpl(android.content.Context context, android.support.v7.media.SystemMediaRouteProvider.SyncCallback syncCallback) {
            super(context);
            mSyncCallback = syncCallback;
            mRouterObj = android.support.v7.media.MediaRouterJellybean.getMediaRouter(context);
            mCallbackObj = createCallbackObj();
            mVolumeCallbackObj = createVolumeCallbackObj();
            android.content.res.Resources r = context.getResources();
            mUserRouteCategoryObj = android.support.v7.media.MediaRouterJellybean.createRouteCategory(mRouterObj, r.getString(R.string.mr_user_route_category_name), false);
            updateSystemRoutes();
        }

        @java.lang.Override
        public android.support.v7.media.MediaRouteProvider.RouteController onCreateRouteController(java.lang.String routeId) {
            int index = findSystemRouteRecordByDescriptorId(routeId);
            if (index >= 0) {
                android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record = mSystemRouteRecords.get(index);
                return new android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteController(record.mRouteObj);
            }
            return null;
        }

        @java.lang.Override
        public void onDiscoveryRequestChanged(android.support.v7.media.MediaRouteDiscoveryRequest request) {
            int newRouteTypes = 0;
            boolean newActiveScan = false;
            if (request != null) {
                final android.support.v7.media.MediaRouteSelector selector = request.getSelector();
                final java.util.List<java.lang.String> categories = selector.getControlCategories();
                final int count = categories.size();
                for (int i = 0; i < count; i++) {
                    java.lang.String category = categories.get(i);
                    if (category.equals(android.support.v7.media.MediaControlIntent.CATEGORY_LIVE_AUDIO)) {
                        newRouteTypes |= android.support.v7.media.MediaRouterJellybean.ROUTE_TYPE_LIVE_AUDIO;
                    } else
                        if (category.equals(android.support.v7.media.MediaControlIntent.CATEGORY_LIVE_VIDEO)) {
                            newRouteTypes |= android.support.v7.media.MediaRouterJellybean.ROUTE_TYPE_LIVE_VIDEO;
                        } else {
                            newRouteTypes |= android.support.v7.media.MediaRouterJellybean.ROUTE_TYPE_USER;
                        }

                }
                newActiveScan = request.isActiveScan();
            }
            if ((mRouteTypes != newRouteTypes) || (mActiveScan != newActiveScan)) {
                mRouteTypes = newRouteTypes;
                mActiveScan = newActiveScan;
                updateSystemRoutes();
            }
        }

        @java.lang.Override
        public void onRouteAdded(java.lang.Object routeObj) {
            if (addSystemRouteNoPublish(routeObj)) {
                publishRoutes();
            }
        }

        private void updateSystemRoutes() {
            updateCallback();
            boolean changed = false;
            for (java.lang.Object routeObj : android.support.v7.media.MediaRouterJellybean.getRoutes(mRouterObj)) {
                changed |= addSystemRouteNoPublish(routeObj);
            }
            if (changed) {
                publishRoutes();
            }
        }

        private boolean addSystemRouteNoPublish(java.lang.Object routeObj) {
            if ((getUserRouteRecord(routeObj) == null) && (findSystemRouteRecord(routeObj) < 0)) {
                java.lang.String id = assignRouteId(routeObj);
                android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record = new android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord(routeObj, id);
                updateSystemRouteDescriptor(record);
                mSystemRouteRecords.add(record);
                return true;
            }
            return false;
        }

        private java.lang.String assignRouteId(java.lang.Object routeObj) {
            // TODO: The framework media router should supply a unique route id that
            // we can use here.  For now we use a hash of the route name and take care
            // to dedupe it.
            boolean isDefault = getDefaultRoute() == routeObj;
            java.lang.String id = (isDefault) ? android.support.v7.media.SystemMediaRouteProvider.DEFAULT_ROUTE_ID : java.lang.String.format(java.util.Locale.US, "ROUTE_%08x", getRouteName(routeObj).hashCode());
            if (findSystemRouteRecordByDescriptorId(id) < 0) {
                return id;
            }
            for (int i = 2; ; i++) {
                java.lang.String newId = java.lang.String.format(java.util.Locale.US, "%s_%d", id, i);
                if (findSystemRouteRecordByDescriptorId(newId) < 0) {
                    return newId;
                }
            }
        }

        @java.lang.Override
        public void onRouteRemoved(java.lang.Object routeObj) {
            if (getUserRouteRecord(routeObj) == null) {
                int index = findSystemRouteRecord(routeObj);
                if (index >= 0) {
                    mSystemRouteRecords.remove(index);
                    publishRoutes();
                }
            }
        }

        @java.lang.Override
        public void onRouteChanged(java.lang.Object routeObj) {
            if (getUserRouteRecord(routeObj) == null) {
                int index = findSystemRouteRecord(routeObj);
                if (index >= 0) {
                    android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record = mSystemRouteRecords.get(index);
                    updateSystemRouteDescriptor(record);
                    publishRoutes();
                }
            }
        }

        @java.lang.Override
        public void onRouteVolumeChanged(java.lang.Object routeObj) {
            if (getUserRouteRecord(routeObj) == null) {
                int index = findSystemRouteRecord(routeObj);
                if (index >= 0) {
                    android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record = mSystemRouteRecords.get(index);
                    int newVolume = android.support.v7.media.MediaRouterJellybean.RouteInfo.getVolume(routeObj);
                    if (newVolume != record.mRouteDescriptor.getVolume()) {
                        record.mRouteDescriptor = new android.support.v7.media.MediaRouteDescriptor.Builder(record.mRouteDescriptor).setVolume(newVolume).build();
                        publishRoutes();
                    }
                }
            }
        }

        @java.lang.Override
        public void onRouteSelected(int type, java.lang.Object routeObj) {
            if (routeObj != android.support.v7.media.MediaRouterJellybean.getSelectedRoute(mRouterObj, android.support.v7.media.MediaRouterJellybean.ALL_ROUTE_TYPES)) {
                // The currently selected route has already changed so this callback
                // is stale.  Drop it to prevent getting into sync loops.
                return;
            }
            android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord userRouteRecord = getUserRouteRecord(routeObj);
            if (userRouteRecord != null) {
                userRouteRecord.mRoute.select();
            } else {
                // Select the route if it already exists in the compat media router.
                // If not, we will select it instead when the route is added.
                int index = findSystemRouteRecord(routeObj);
                if (index >= 0) {
                    android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record = mSystemRouteRecords.get(index);
                    android.support.v7.media.MediaRouter.RouteInfo route = mSyncCallback.getSystemRouteByDescriptorId(record.mRouteDescriptorId);
                    if (route != null) {
                        route.select();
                    }
                }
            }
        }

        @java.lang.Override
        public void onRouteUnselected(int type, java.lang.Object routeObj) {
            // Nothing to do when a route is unselected.
            // We only need to handle when a route is selected.
        }

        @java.lang.Override
        public void onRouteGrouped(java.lang.Object routeObj, java.lang.Object groupObj, int index) {
            // Route grouping is deprecated and no longer supported.
        }

        @java.lang.Override
        public void onRouteUngrouped(java.lang.Object routeObj, java.lang.Object groupObj) {
            // Route grouping is deprecated and no longer supported.
        }

        @java.lang.Override
        public void onVolumeSetRequest(java.lang.Object routeObj, int volume) {
            android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord record = getUserRouteRecord(routeObj);
            if (record != null) {
                record.mRoute.requestSetVolume(volume);
            }
        }

        @java.lang.Override
        public void onVolumeUpdateRequest(java.lang.Object routeObj, int direction) {
            android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord record = getUserRouteRecord(routeObj);
            if (record != null) {
                record.mRoute.requestUpdateVolume(direction);
            }
        }

        @java.lang.Override
        public void onSyncRouteAdded(android.support.v7.media.MediaRouter.RouteInfo route) {
            if (route.getProviderInstance() != this) {
                java.lang.Object routeObj = android.support.v7.media.MediaRouterJellybean.createUserRoute(mRouterObj, mUserRouteCategoryObj);
                android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord record = new android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord(route, routeObj);
                android.support.v7.media.MediaRouterJellybean.RouteInfo.setTag(routeObj, record);
                android.support.v7.media.MediaRouterJellybean.UserRouteInfo.setVolumeCallback(routeObj, mVolumeCallbackObj);
                updateUserRouteProperties(record);
                mUserRouteRecords.add(record);
                android.support.v7.media.MediaRouterJellybean.addUserRoute(mRouterObj, routeObj);
            } else {
                // If the newly added route is the counterpart of the currently selected
                // route in the framework media router then ensure it is selected in
                // the compat media router.
                java.lang.Object routeObj = android.support.v7.media.MediaRouterJellybean.getSelectedRoute(mRouterObj, android.support.v7.media.MediaRouterJellybean.ALL_ROUTE_TYPES);
                int index = findSystemRouteRecord(routeObj);
                if (index >= 0) {
                    android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record = mSystemRouteRecords.get(index);
                    if (record.mRouteDescriptorId.equals(route.getDescriptorId())) {
                        route.select();
                    }
                }
            }
        }

        @java.lang.Override
        public void onSyncRouteRemoved(android.support.v7.media.MediaRouter.RouteInfo route) {
            if (route.getProviderInstance() != this) {
                int index = findUserRouteRecord(route);
                if (index >= 0) {
                    android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord record = mUserRouteRecords.remove(index);
                    android.support.v7.media.MediaRouterJellybean.RouteInfo.setTag(record.mRouteObj, null);
                    android.support.v7.media.MediaRouterJellybean.UserRouteInfo.setVolumeCallback(record.mRouteObj, null);
                    android.support.v7.media.MediaRouterJellybean.removeUserRoute(mRouterObj, record.mRouteObj);
                }
            }
        }

        @java.lang.Override
        public void onSyncRouteChanged(android.support.v7.media.MediaRouter.RouteInfo route) {
            if (route.getProviderInstance() != this) {
                int index = findUserRouteRecord(route);
                if (index >= 0) {
                    android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord record = mUserRouteRecords.get(index);
                    updateUserRouteProperties(record);
                }
            }
        }

        @java.lang.Override
        public void onSyncRouteSelected(android.support.v7.media.MediaRouter.RouteInfo route) {
            if (!route.isSelected()) {
                // The currently selected route has already changed so this callback
                // is stale.  Drop it to prevent getting into sync loops.
                return;
            }
            if (route.getProviderInstance() != this) {
                int index = findUserRouteRecord(route);
                if (index >= 0) {
                    android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord record = mUserRouteRecords.get(index);
                    selectRoute(record.mRouteObj);
                }
            } else {
                int index = findSystemRouteRecordByDescriptorId(route.getDescriptorId());
                if (index >= 0) {
                    android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record = mSystemRouteRecords.get(index);
                    selectRoute(record.mRouteObj);
                }
            }
        }

        protected void publishRoutes() {
            android.support.v7.media.MediaRouteProviderDescriptor.Builder builder = new android.support.v7.media.MediaRouteProviderDescriptor.Builder();
            int count = mSystemRouteRecords.size();
            for (int i = 0; i < count; i++) {
                builder.addRoute(mSystemRouteRecords.get(i).mRouteDescriptor);
            }
            setDescriptor(builder.build());
        }

        protected int findSystemRouteRecord(java.lang.Object routeObj) {
            final int count = mSystemRouteRecords.size();
            for (int i = 0; i < count; i++) {
                if (mSystemRouteRecords.get(i).mRouteObj == routeObj) {
                    return i;
                }
            }
            return -1;
        }

        protected int findSystemRouteRecordByDescriptorId(java.lang.String id) {
            final int count = mSystemRouteRecords.size();
            for (int i = 0; i < count; i++) {
                if (mSystemRouteRecords.get(i).mRouteDescriptorId.equals(id)) {
                    return i;
                }
            }
            return -1;
        }

        protected int findUserRouteRecord(android.support.v7.media.MediaRouter.RouteInfo route) {
            final int count = mUserRouteRecords.size();
            for (int i = 0; i < count; i++) {
                if (mUserRouteRecords.get(i).mRoute == route) {
                    return i;
                }
            }
            return -1;
        }

        protected android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord getUserRouteRecord(java.lang.Object routeObj) {
            java.lang.Object tag = android.support.v7.media.MediaRouterJellybean.RouteInfo.getTag(routeObj);
            return tag instanceof android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord ? ((android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord) (tag)) : null;
        }

        protected void updateSystemRouteDescriptor(android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record) {
            // We must always recreate the route descriptor when making any changes
            // because they are intended to be immutable once published.
            android.support.v7.media.MediaRouteDescriptor.Builder builder = new android.support.v7.media.MediaRouteDescriptor.Builder(record.mRouteDescriptorId, getRouteName(record.mRouteObj));
            onBuildSystemRouteDescriptor(record, builder);
            record.mRouteDescriptor = builder.build();
        }

        protected java.lang.String getRouteName(java.lang.Object routeObj) {
            // Routes should not have null names but it may happen for badly configured
            // user routes.  We tolerate this by using an empty name string here but
            // such unnamed routes will be discarded by the media router upstream
            // (with a log message so we can track down the problem).
            java.lang.CharSequence name = android.support.v7.media.MediaRouterJellybean.RouteInfo.getName(routeObj, getContext());
            return name != null ? name.toString() : "";
        }

        protected void onBuildSystemRouteDescriptor(android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record, android.support.v7.media.MediaRouteDescriptor.Builder builder) {
            int supportedTypes = android.support.v7.media.MediaRouterJellybean.RouteInfo.getSupportedTypes(record.mRouteObj);
            if ((supportedTypes & android.support.v7.media.MediaRouterJellybean.ROUTE_TYPE_LIVE_AUDIO) != 0) {
                builder.addControlFilters(android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.LIVE_AUDIO_CONTROL_FILTERS);
            }
            if ((supportedTypes & android.support.v7.media.MediaRouterJellybean.ROUTE_TYPE_LIVE_VIDEO) != 0) {
                builder.addControlFilters(android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.LIVE_VIDEO_CONTROL_FILTERS);
            }
            builder.setPlaybackType(android.support.v7.media.MediaRouterJellybean.RouteInfo.getPlaybackType(record.mRouteObj));
            builder.setPlaybackStream(android.support.v7.media.MediaRouterJellybean.RouteInfo.getPlaybackStream(record.mRouteObj));
            builder.setVolume(android.support.v7.media.MediaRouterJellybean.RouteInfo.getVolume(record.mRouteObj));
            builder.setVolumeMax(android.support.v7.media.MediaRouterJellybean.RouteInfo.getVolumeMax(record.mRouteObj));
            builder.setVolumeHandling(android.support.v7.media.MediaRouterJellybean.RouteInfo.getVolumeHandling(record.mRouteObj));
        }

        protected void updateUserRouteProperties(android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord record) {
            android.support.v7.media.MediaRouterJellybean.UserRouteInfo.setName(record.mRouteObj, record.mRoute.getName());
            android.support.v7.media.MediaRouterJellybean.UserRouteInfo.setPlaybackType(record.mRouteObj, record.mRoute.getPlaybackType());
            android.support.v7.media.MediaRouterJellybean.UserRouteInfo.setPlaybackStream(record.mRouteObj, record.mRoute.getPlaybackStream());
            android.support.v7.media.MediaRouterJellybean.UserRouteInfo.setVolume(record.mRouteObj, record.mRoute.getVolume());
            android.support.v7.media.MediaRouterJellybean.UserRouteInfo.setVolumeMax(record.mRouteObj, record.mRoute.getVolumeMax());
            android.support.v7.media.MediaRouterJellybean.UserRouteInfo.setVolumeHandling(record.mRouteObj, record.mRoute.getVolumeHandling());
        }

        protected void updateCallback() {
            if (mCallbackRegistered) {
                mCallbackRegistered = false;
                android.support.v7.media.MediaRouterJellybean.removeCallback(mRouterObj, mCallbackObj);
            }
            if (mRouteTypes != 0) {
                mCallbackRegistered = true;
                android.support.v7.media.MediaRouterJellybean.addCallback(mRouterObj, mRouteTypes, mCallbackObj);
            }
        }

        protected java.lang.Object createCallbackObj() {
            return android.support.v7.media.MediaRouterJellybean.createCallback(this);
        }

        protected java.lang.Object createVolumeCallbackObj() {
            return android.support.v7.media.MediaRouterJellybean.createVolumeCallback(this);
        }

        protected void selectRoute(java.lang.Object routeObj) {
            if (mSelectRouteWorkaround == null) {
                mSelectRouteWorkaround = new android.support.v7.media.MediaRouterJellybean.SelectRouteWorkaround();
            }
            mSelectRouteWorkaround.selectRoute(mRouterObj, android.support.v7.media.MediaRouterJellybean.ALL_ROUTE_TYPES, routeObj);
        }

        protected java.lang.Object getDefaultRoute() {
            if (mGetDefaultRouteWorkaround == null) {
                mGetDefaultRouteWorkaround = new android.support.v7.media.MediaRouterJellybean.GetDefaultRouteWorkaround();
            }
            return mGetDefaultRouteWorkaround.getDefaultRoute(mRouterObj);
        }

        /**
         * Represents a route that is provided by the framework media router
         * and published by this route provider to the support library media router.
         */
        protected static final class SystemRouteRecord {
            public final java.lang.Object mRouteObj;

            public final java.lang.String mRouteDescriptorId;

            public android.support.v7.media.MediaRouteDescriptor mRouteDescriptor;// assigned immediately after creation


            public SystemRouteRecord(java.lang.Object routeObj, java.lang.String id) {
                mRouteObj = routeObj;
                mRouteDescriptorId = id;
            }
        }

        /**
         * Represents a route that is provided by the support library media router
         * and published by this route provider to the framework media router.
         */
        protected static final class UserRouteRecord {
            public final android.support.v7.media.MediaRouter.RouteInfo mRoute;

            public final java.lang.Object mRouteObj;

            public UserRouteRecord(android.support.v7.media.MediaRouter.RouteInfo route, java.lang.Object routeObj) {
                mRoute = route;
                mRouteObj = routeObj;
            }
        }

        protected final class SystemRouteController extends android.support.v7.media.MediaRouteProvider.RouteController {
            private final java.lang.Object mRouteObj;

            public SystemRouteController(java.lang.Object routeObj) {
                mRouteObj = routeObj;
            }

            @java.lang.Override
            public void onSetVolume(int volume) {
                android.support.v7.media.MediaRouterJellybean.RouteInfo.requestSetVolume(mRouteObj, volume);
            }

            @java.lang.Override
            public void onUpdateVolume(int delta) {
                android.support.v7.media.MediaRouterJellybean.RouteInfo.requestUpdateVolume(mRouteObj, delta);
            }
        }
    }

    /**
     * Jellybean MR1 implementation.
     */
    private static class JellybeanMr1Impl extends android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl implements android.support.v7.media.MediaRouterJellybeanMr1.Callback {
        private android.support.v7.media.MediaRouterJellybeanMr1.ActiveScanWorkaround mActiveScanWorkaround;

        private android.support.v7.media.MediaRouterJellybeanMr1.IsConnectingWorkaround mIsConnectingWorkaround;

        public JellybeanMr1Impl(android.content.Context context, android.support.v7.media.SystemMediaRouteProvider.SyncCallback syncCallback) {
            super(context, syncCallback);
        }

        @java.lang.Override
        public void onRoutePresentationDisplayChanged(java.lang.Object routeObj) {
            int index = findSystemRouteRecord(routeObj);
            if (index >= 0) {
                android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record = mSystemRouteRecords.get(index);
                android.view.Display newPresentationDisplay = android.support.v7.media.MediaRouterJellybeanMr1.RouteInfo.getPresentationDisplay(routeObj);
                int newPresentationDisplayId = (newPresentationDisplay != null) ? newPresentationDisplay.getDisplayId() : -1;
                if (newPresentationDisplayId != record.mRouteDescriptor.getPresentationDisplayId()) {
                    record.mRouteDescriptor = new android.support.v7.media.MediaRouteDescriptor.Builder(record.mRouteDescriptor).setPresentationDisplayId(newPresentationDisplayId).build();
                    publishRoutes();
                }
            }
        }

        @java.lang.Override
        protected void onBuildSystemRouteDescriptor(android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record, android.support.v7.media.MediaRouteDescriptor.Builder builder) {
            super.onBuildSystemRouteDescriptor(record, builder);
            if (!android.support.v7.media.MediaRouterJellybeanMr1.RouteInfo.isEnabled(record.mRouteObj)) {
                builder.setEnabled(false);
            }
            if (isConnecting(record)) {
                builder.setConnecting(true);
            }
            android.view.Display presentationDisplay = android.support.v7.media.MediaRouterJellybeanMr1.RouteInfo.getPresentationDisplay(record.mRouteObj);
            if (presentationDisplay != null) {
                builder.setPresentationDisplayId(presentationDisplay.getDisplayId());
            }
        }

        @java.lang.Override
        protected void updateCallback() {
            super.updateCallback();
            if (mActiveScanWorkaround == null) {
                mActiveScanWorkaround = new android.support.v7.media.MediaRouterJellybeanMr1.ActiveScanWorkaround(getContext(), getHandler());
            }
            mActiveScanWorkaround.setActiveScanRouteTypes(mActiveScan ? mRouteTypes : 0);
        }

        @java.lang.Override
        protected java.lang.Object createCallbackObj() {
            return android.support.v7.media.MediaRouterJellybeanMr1.createCallback(this);
        }

        protected boolean isConnecting(android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record) {
            if (mIsConnectingWorkaround == null) {
                mIsConnectingWorkaround = new android.support.v7.media.MediaRouterJellybeanMr1.IsConnectingWorkaround();
            }
            return mIsConnectingWorkaround.isConnecting(record.mRouteObj);
        }
    }

    /**
     * Jellybean MR2 implementation.
     */
    private static class JellybeanMr2Impl extends android.support.v7.media.SystemMediaRouteProvider.JellybeanMr1Impl {
        public JellybeanMr2Impl(android.content.Context context, android.support.v7.media.SystemMediaRouteProvider.SyncCallback syncCallback) {
            super(context, syncCallback);
        }

        @java.lang.Override
        protected void onBuildSystemRouteDescriptor(android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record, android.support.v7.media.MediaRouteDescriptor.Builder builder) {
            super.onBuildSystemRouteDescriptor(record, builder);
            java.lang.CharSequence description = android.support.v7.media.MediaRouterJellybeanMr2.RouteInfo.getDescription(record.mRouteObj);
            if (description != null) {
                builder.setDescription(description.toString());
            }
        }

        @java.lang.Override
        protected void selectRoute(java.lang.Object routeObj) {
            android.support.v7.media.MediaRouterJellybean.selectRoute(mRouterObj, android.support.v7.media.MediaRouterJellybean.ALL_ROUTE_TYPES, routeObj);
        }

        @java.lang.Override
        protected java.lang.Object getDefaultRoute() {
            return android.support.v7.media.MediaRouterJellybeanMr2.getDefaultRoute(mRouterObj);
        }

        @java.lang.Override
        protected void updateUserRouteProperties(android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord record) {
            super.updateUserRouteProperties(record);
            android.support.v7.media.MediaRouterJellybeanMr2.UserRouteInfo.setDescription(record.mRouteObj, record.mRoute.getDescription());
        }

        @java.lang.Override
        protected void updateCallback() {
            if (mCallbackRegistered) {
                android.support.v7.media.MediaRouterJellybean.removeCallback(mRouterObj, mCallbackObj);
            }
            mCallbackRegistered = true;
            android.support.v7.media.MediaRouterJellybeanMr2.addCallback(mRouterObj, mRouteTypes, mCallbackObj, android.support.v7.media.MediaRouter.CALLBACK_FLAG_UNFILTERED_EVENTS | (mActiveScan ? android.support.v7.media.MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN : 0));
        }

        @java.lang.Override
        protected boolean isConnecting(android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record) {
            return android.support.v7.media.MediaRouterJellybeanMr2.RouteInfo.isConnecting(record.mRouteObj);
        }
    }

    /**
     * Api24 implementation.
     */
    private static class Api24Impl extends android.support.v7.media.SystemMediaRouteProvider.JellybeanMr2Impl {
        public Api24Impl(android.content.Context context, android.support.v7.media.SystemMediaRouteProvider.SyncCallback syncCallback) {
            super(context, syncCallback);
        }

        @java.lang.Override
        protected void onBuildSystemRouteDescriptor(android.support.v7.media.SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord record, android.support.v7.media.MediaRouteDescriptor.Builder builder) {
            super.onBuildSystemRouteDescriptor(record, builder);
            builder.setDeviceType(android.support.v7.media.MediaRouterApi24.RouteInfo.getDeviceType(record.mRouteObj));
        }
    }
}

