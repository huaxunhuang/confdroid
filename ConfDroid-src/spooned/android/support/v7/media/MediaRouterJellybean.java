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


final class MediaRouterJellybean {
    private static final java.lang.String TAG = "MediaRouterJellybean";

    public static final int ROUTE_TYPE_LIVE_AUDIO = 0x1;

    public static final int ROUTE_TYPE_LIVE_VIDEO = 0x2;

    public static final int ROUTE_TYPE_USER = 0x800000;

    public static final int ALL_ROUTE_TYPES = (android.support.v7.media.MediaRouterJellybean.ROUTE_TYPE_LIVE_AUDIO | android.support.v7.media.MediaRouterJellybean.ROUTE_TYPE_LIVE_VIDEO) | android.support.v7.media.MediaRouterJellybean.ROUTE_TYPE_USER;

    public static java.lang.Object getMediaRouter(android.content.Context context) {
        return context.getSystemService(android.content.Context.MEDIA_ROUTER_SERVICE);
    }

    @java.lang.SuppressWarnings({ "rawtypes", "unchecked" })
    public static java.util.List getRoutes(java.lang.Object routerObj) {
        final android.media.MediaRouter router = ((android.media.MediaRouter) (routerObj));
        final int count = router.getRouteCount();
        java.util.List out = new java.util.ArrayList(count);
        for (int i = 0; i < count; i++) {
            out.add(router.getRouteAt(i));
        }
        return out;
    }

    @java.lang.SuppressWarnings({ "rawtypes", "unchecked" })
    public static java.util.List getCategories(java.lang.Object routerObj) {
        final android.media.MediaRouter router = ((android.media.MediaRouter) (routerObj));
        final int count = router.getCategoryCount();
        java.util.List out = new java.util.ArrayList(count);
        for (int i = 0; i < count; i++) {
            out.add(router.getCategoryAt(i));
        }
        return out;
    }

    public static java.lang.Object getSelectedRoute(java.lang.Object routerObj, int type) {
        return ((android.media.MediaRouter) (routerObj)).getSelectedRoute(type);
    }

    public static void selectRoute(java.lang.Object routerObj, int types, java.lang.Object routeObj) {
        ((android.media.MediaRouter) (routerObj)).selectRoute(types, ((android.media.MediaRouter.RouteInfo) (routeObj)));
    }

    public static void addCallback(java.lang.Object routerObj, int types, java.lang.Object callbackObj) {
        ((android.media.MediaRouter) (routerObj)).addCallback(types, ((android.media.MediaRouter.Callback) (callbackObj)));
    }

    public static void removeCallback(java.lang.Object routerObj, java.lang.Object callbackObj) {
        ((android.media.MediaRouter) (routerObj)).removeCallback(((android.media.MediaRouter.Callback) (callbackObj)));
    }

    public static java.lang.Object createRouteCategory(java.lang.Object routerObj, java.lang.String name, boolean isGroupable) {
        return ((android.media.MediaRouter) (routerObj)).createRouteCategory(name, isGroupable);
    }

    public static java.lang.Object createUserRoute(java.lang.Object routerObj, java.lang.Object categoryObj) {
        return ((android.media.MediaRouter) (routerObj)).createUserRoute(((android.media.MediaRouter.RouteCategory) (categoryObj)));
    }

    public static void addUserRoute(java.lang.Object routerObj, java.lang.Object routeObj) {
        ((android.media.MediaRouter) (routerObj)).addUserRoute(((android.media.MediaRouter.UserRouteInfo) (routeObj)));
    }

    public static void removeUserRoute(java.lang.Object routerObj, java.lang.Object routeObj) {
        ((android.media.MediaRouter) (routerObj)).removeUserRoute(((android.media.MediaRouter.UserRouteInfo) (routeObj)));
    }

    public static java.lang.Object createCallback(android.support.v7.media.MediaRouterJellybean.Callback callback) {
        return new android.support.v7.media.MediaRouterJellybean.CallbackProxy<android.support.v7.media.MediaRouterJellybean.Callback>(callback);
    }

    public static java.lang.Object createVolumeCallback(android.support.v7.media.MediaRouterJellybean.VolumeCallback callback) {
        return new android.support.v7.media.MediaRouterJellybean.VolumeCallbackProxy<android.support.v7.media.MediaRouterJellybean.VolumeCallback>(callback);
    }

    public static final class RouteInfo {
        public static java.lang.CharSequence getName(java.lang.Object routeObj, android.content.Context context) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getName(context);
        }

        public static java.lang.CharSequence getStatus(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getStatus();
        }

        public static int getSupportedTypes(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getSupportedTypes();
        }

        public static java.lang.Object getCategory(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getCategory();
        }

        public static android.graphics.drawable.Drawable getIconDrawable(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getIconDrawable();
        }

        public static int getPlaybackType(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getPlaybackType();
        }

        public static int getPlaybackStream(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getPlaybackStream();
        }

        public static int getVolume(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getVolume();
        }

        public static int getVolumeMax(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getVolumeMax();
        }

        public static int getVolumeHandling(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getVolumeHandling();
        }

        public static java.lang.Object getTag(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getTag();
        }

        public static void setTag(java.lang.Object routeObj, java.lang.Object tag) {
            ((android.media.MediaRouter.RouteInfo) (routeObj)).setTag(tag);
        }

        public static void requestSetVolume(java.lang.Object routeObj, int volume) {
            ((android.media.MediaRouter.RouteInfo) (routeObj)).requestSetVolume(volume);
        }

        public static void requestUpdateVolume(java.lang.Object routeObj, int direction) {
            ((android.media.MediaRouter.RouteInfo) (routeObj)).requestUpdateVolume(direction);
        }

        public static java.lang.Object getGroup(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getGroup();
        }

        public static boolean isGroup(java.lang.Object routeObj) {
            return routeObj instanceof android.media.MediaRouter.RouteGroup;
        }
    }

    public static final class RouteGroup {
        @java.lang.SuppressWarnings({ "rawtypes", "unchecked" })
        public static java.util.List getGroupedRoutes(java.lang.Object groupObj) {
            final android.media.MediaRouter.RouteGroup group = ((android.media.MediaRouter.RouteGroup) (groupObj));
            final int count = group.getRouteCount();
            java.util.List out = new java.util.ArrayList(count);
            for (int i = 0; i < count; i++) {
                out.add(group.getRouteAt(i));
            }
            return out;
        }
    }

    public static final class UserRouteInfo {
        public static void setName(java.lang.Object routeObj, java.lang.CharSequence name) {
            ((android.media.MediaRouter.UserRouteInfo) (routeObj)).setName(name);
        }

        public static void setStatus(java.lang.Object routeObj, java.lang.CharSequence status) {
            ((android.media.MediaRouter.UserRouteInfo) (routeObj)).setStatus(status);
        }

        public static void setIconDrawable(java.lang.Object routeObj, android.graphics.drawable.Drawable icon) {
            ((android.media.MediaRouter.UserRouteInfo) (routeObj)).setIconDrawable(icon);
        }

        public static void setPlaybackType(java.lang.Object routeObj, int type) {
            ((android.media.MediaRouter.UserRouteInfo) (routeObj)).setPlaybackType(type);
        }

        public static void setPlaybackStream(java.lang.Object routeObj, int stream) {
            ((android.media.MediaRouter.UserRouteInfo) (routeObj)).setPlaybackStream(stream);
        }

        public static void setVolume(java.lang.Object routeObj, int volume) {
            ((android.media.MediaRouter.UserRouteInfo) (routeObj)).setVolume(volume);
        }

        public static void setVolumeMax(java.lang.Object routeObj, int volumeMax) {
            ((android.media.MediaRouter.UserRouteInfo) (routeObj)).setVolumeMax(volumeMax);
        }

        public static void setVolumeHandling(java.lang.Object routeObj, int volumeHandling) {
            ((android.media.MediaRouter.UserRouteInfo) (routeObj)).setVolumeHandling(volumeHandling);
        }

        public static void setVolumeCallback(java.lang.Object routeObj, java.lang.Object volumeCallbackObj) {
            ((android.media.MediaRouter.UserRouteInfo) (routeObj)).setVolumeCallback(((android.media.MediaRouter.VolumeCallback) (volumeCallbackObj)));
        }

        public static void setRemoteControlClient(java.lang.Object routeObj, java.lang.Object rccObj) {
            ((android.media.MediaRouter.UserRouteInfo) (routeObj)).setRemoteControlClient(((android.media.RemoteControlClient) (rccObj)));
        }
    }

    public static final class RouteCategory {
        public static java.lang.CharSequence getName(java.lang.Object categoryObj, android.content.Context context) {
            return ((android.media.MediaRouter.RouteCategory) (categoryObj)).getName(context);
        }

        @java.lang.SuppressWarnings({ "rawtypes", "unchecked" })
        public static java.util.List getRoutes(java.lang.Object categoryObj) {
            java.util.ArrayList out = new java.util.ArrayList();
            ((android.media.MediaRouter.RouteCategory) (categoryObj)).getRoutes(out);
            return out;
        }

        public static int getSupportedTypes(java.lang.Object categoryObj) {
            return ((android.media.MediaRouter.RouteCategory) (categoryObj)).getSupportedTypes();
        }

        public static boolean isGroupable(java.lang.Object categoryObj) {
            return ((android.media.MediaRouter.RouteCategory) (categoryObj)).isGroupable();
        }
    }

    public static interface Callback {
        public void onRouteSelected(int type, java.lang.Object routeObj);

        public void onRouteUnselected(int type, java.lang.Object routeObj);

        public void onRouteAdded(java.lang.Object routeObj);

        public void onRouteRemoved(java.lang.Object routeObj);

        public void onRouteChanged(java.lang.Object routeObj);

        public void onRouteGrouped(java.lang.Object routeObj, java.lang.Object groupObj, int index);

        public void onRouteUngrouped(java.lang.Object routeObj, java.lang.Object groupObj);

        public void onRouteVolumeChanged(java.lang.Object routeObj);
    }

    public static interface VolumeCallback {
        public void onVolumeSetRequest(java.lang.Object routeObj, int volume);

        public void onVolumeUpdateRequest(java.lang.Object routeObj, int direction);
    }

    /**
     * Workaround for limitations of selectRoute() on JB and JB MR1.
     * Do not use on JB MR2 and above.
     */
    public static final class SelectRouteWorkaround {
        private java.lang.reflect.Method mSelectRouteIntMethod;

        public SelectRouteWorkaround() {
            if ((android.os.Build.VERSION.SDK_INT < 16) || (android.os.Build.VERSION.SDK_INT > 17)) {
                throw new java.lang.UnsupportedOperationException();
            }
            try {
                mSelectRouteIntMethod = android.media.MediaRouter.class.getMethod("selectRouteInt", int.class, android.media.MediaRouter.RouteInfo.class);
            } catch (java.lang.NoSuchMethodException ex) {
            }
        }

        public void selectRoute(java.lang.Object routerObj, int types, java.lang.Object routeObj) {
            android.media.MediaRouter router = ((android.media.MediaRouter) (routerObj));
            android.media.MediaRouter.RouteInfo route = ((android.media.MediaRouter.RouteInfo) (routeObj));
            int routeTypes = route.getSupportedTypes();
            if ((routeTypes & android.support.v7.media.MediaRouterJellybean.ROUTE_TYPE_USER) == 0) {
                // Handle non-user routes.
                // On JB and JB MR1, the selectRoute() API only supports programmatically
                // selecting user routes.  So instead we rely on the hidden selectRouteInt()
                // method on these versions of the platform.
                // This limitation was removed in JB MR2.
                if (mSelectRouteIntMethod != null) {
                    try {
                        mSelectRouteIntMethod.invoke(router, types, route);
                        return;// success!

                    } catch (java.lang.IllegalAccessException ex) {
                        android.util.Log.w(android.support.v7.media.MediaRouterJellybean.TAG, "Cannot programmatically select non-user route.  " + "Media routing may not work.", ex);
                    } catch (java.lang.reflect.InvocationTargetException ex) {
                        android.util.Log.w(android.support.v7.media.MediaRouterJellybean.TAG, "Cannot programmatically select non-user route.  " + "Media routing may not work.", ex);
                    }
                } else {
                    android.util.Log.w(android.support.v7.media.MediaRouterJellybean.TAG, "Cannot programmatically select non-user route " + ("because the platform is missing the selectRouteInt() " + "method.  Media routing may not work."));
                }
            }
            // Default handling.
            router.selectRoute(types, route);
        }
    }

    /**
     * Workaround the fact that the getDefaultRoute() method does not exist in JB and JB MR1.
     * Do not use on JB MR2 and above.
     */
    public static final class GetDefaultRouteWorkaround {
        private java.lang.reflect.Method mGetSystemAudioRouteMethod;

        public GetDefaultRouteWorkaround() {
            if ((android.os.Build.VERSION.SDK_INT < 16) || (android.os.Build.VERSION.SDK_INT > 17)) {
                throw new java.lang.UnsupportedOperationException();
            }
            try {
                mGetSystemAudioRouteMethod = android.media.MediaRouter.class.getMethod("getSystemAudioRoute");
            } catch (java.lang.NoSuchMethodException ex) {
            }
        }

        public java.lang.Object getDefaultRoute(java.lang.Object routerObj) {
            android.media.MediaRouter router = ((android.media.MediaRouter) (routerObj));
            if (mGetSystemAudioRouteMethod != null) {
                try {
                    return mGetSystemAudioRouteMethod.invoke(router);
                } catch (java.lang.IllegalAccessException ex) {
                } catch (java.lang.reflect.InvocationTargetException ex) {
                }
            }
            // Could not find the method or it does not work.
            // Return the first route and hope for the best.
            return router.getRouteAt(0);
        }
    }

    static class CallbackProxy<T extends android.support.v7.media.MediaRouterJellybean.Callback> extends android.media.MediaRouter.Callback {
        protected final T mCallback;

        public CallbackProxy(T callback) {
            mCallback = callback;
        }

        @java.lang.Override
        public void onRouteSelected(android.media.MediaRouter router, int type, android.media.MediaRouter.RouteInfo route) {
            mCallback.onRouteSelected(type, route);
        }

        @java.lang.Override
        public void onRouteUnselected(android.media.MediaRouter router, int type, android.media.MediaRouter.RouteInfo route) {
            mCallback.onRouteUnselected(type, route);
        }

        @java.lang.Override
        public void onRouteAdded(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo route) {
            mCallback.onRouteAdded(route);
        }

        @java.lang.Override
        public void onRouteRemoved(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo route) {
            mCallback.onRouteRemoved(route);
        }

        @java.lang.Override
        public void onRouteChanged(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo route) {
            mCallback.onRouteChanged(route);
        }

        @java.lang.Override
        public void onRouteGrouped(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo route, android.media.MediaRouter.RouteGroup group, int index) {
            mCallback.onRouteGrouped(route, group, index);
        }

        @java.lang.Override
        public void onRouteUngrouped(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo route, android.media.MediaRouter.RouteGroup group) {
            mCallback.onRouteUngrouped(route, group);
        }

        @java.lang.Override
        public void onRouteVolumeChanged(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo route) {
            mCallback.onRouteVolumeChanged(route);
        }
    }

    static class VolumeCallbackProxy<T extends android.support.v7.media.MediaRouterJellybean.VolumeCallback> extends android.media.MediaRouter.VolumeCallback {
        protected final T mCallback;

        public VolumeCallbackProxy(T callback) {
            mCallback = callback;
        }

        @java.lang.Override
        public void onVolumeSetRequest(android.media.MediaRouter.RouteInfo route, int volume) {
            mCallback.onVolumeSetRequest(route, volume);
        }

        @java.lang.Override
        public void onVolumeUpdateRequest(android.media.MediaRouter.RouteInfo route, int direction) {
            mCallback.onVolumeUpdateRequest(route, direction);
        }
    }
}

