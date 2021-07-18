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


final class MediaRouterJellybeanMr2 {
    public static java.lang.Object getDefaultRoute(java.lang.Object routerObj) {
        return ((android.media.MediaRouter) (routerObj)).getDefaultRoute();
    }

    public static void addCallback(java.lang.Object routerObj, int types, java.lang.Object callbackObj, int flags) {
        ((android.media.MediaRouter) (routerObj)).addCallback(types, ((android.media.MediaRouter.Callback) (callbackObj)), flags);
    }

    public static final class RouteInfo {
        public static java.lang.CharSequence getDescription(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).getDescription();
        }

        public static boolean isConnecting(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).isConnecting();
        }
    }

    public static final class UserRouteInfo {
        public static void setDescription(java.lang.Object routeObj, java.lang.CharSequence description) {
            ((android.media.MediaRouter.UserRouteInfo) (routeObj)).setDescription(description);
        }
    }
}

