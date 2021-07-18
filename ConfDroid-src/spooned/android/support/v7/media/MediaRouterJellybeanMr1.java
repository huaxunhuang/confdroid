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


final class MediaRouterJellybeanMr1 {
    private static final java.lang.String TAG = "MediaRouterJellybeanMr1";

    public static java.lang.Object createCallback(android.support.v7.media.MediaRouterJellybeanMr1.Callback callback) {
        return new android.support.v7.media.MediaRouterJellybeanMr1.CallbackProxy<android.support.v7.media.MediaRouterJellybeanMr1.Callback>(callback);
    }

    public static final class RouteInfo {
        public static boolean isEnabled(java.lang.Object routeObj) {
            return ((android.media.MediaRouter.RouteInfo) (routeObj)).isEnabled();
        }

        public static android.view.Display getPresentationDisplay(java.lang.Object routeObj) {
            // android.media.MediaRouter.RouteInfo.getPresentationDisplay() was
            // added in API 17. However, some factory releases of JB MR1 missed it.
            try {
                return ((android.media.MediaRouter.RouteInfo) (routeObj)).getPresentationDisplay();
            } catch (java.lang.NoSuchMethodError ex) {
                android.util.Log.w(android.support.v7.media.MediaRouterJellybeanMr1.TAG, "Cannot get presentation display for the route.", ex);
            }
            return null;
        }
    }

    public static interface Callback extends android.support.v7.media.MediaRouterJellybean.Callback {
        public void onRoutePresentationDisplayChanged(java.lang.Object routeObj);
    }

    /**
     * Workaround the fact that the version of MediaRouter.addCallback() that accepts a
     * flag to perform an active scan does not exist in JB MR1 so we need to force
     * wifi display scans directly through the DisplayManager.
     * Do not use on JB MR2 and above.
     */
    public static final class ActiveScanWorkaround implements java.lang.Runnable {
        // Time between wifi display scans when actively scanning in milliseconds.
        private static final int WIFI_DISPLAY_SCAN_INTERVAL = 15000;

        private final android.hardware.display.DisplayManager mDisplayManager;

        private final android.os.Handler mHandler;

        private java.lang.reflect.Method mScanWifiDisplaysMethod;

        private boolean mActivelyScanningWifiDisplays;

        public ActiveScanWorkaround(android.content.Context context, android.os.Handler handler) {
            if (android.os.Build.VERSION.SDK_INT != 17) {
                throw new java.lang.UnsupportedOperationException();
            }
            mDisplayManager = ((android.hardware.display.DisplayManager) (context.getSystemService(android.content.Context.DISPLAY_SERVICE)));
            mHandler = handler;
            try {
                mScanWifiDisplaysMethod = android.hardware.display.DisplayManager.class.getMethod("scanWifiDisplays");
            } catch (java.lang.NoSuchMethodException ex) {
            }
        }

        public void setActiveScanRouteTypes(int routeTypes) {
            // On JB MR1, there is no API to scan wifi display routes.
            // Instead we must make a direct call into the DisplayManager to scan
            // wifi displays on this version but only when live video routes are requested.
            // See also the JellybeanMr2Impl implementation of this method.
            // This was fixed in JB MR2 by adding a new overload of addCallback() to
            // enable active scanning on request.
            if ((routeTypes & android.support.v7.media.MediaRouterJellybean.ROUTE_TYPE_LIVE_VIDEO) != 0) {
                if (!mActivelyScanningWifiDisplays) {
                    if (mScanWifiDisplaysMethod != null) {
                        mActivelyScanningWifiDisplays = true;
                        mHandler.post(this);
                    } else {
                        android.util.Log.w(android.support.v7.media.MediaRouterJellybeanMr1.TAG, "Cannot scan for wifi displays because the " + ("DisplayManager.scanWifiDisplays() method is " + "not available on this device."));
                    }
                }
            } else {
                if (mActivelyScanningWifiDisplays) {
                    mActivelyScanningWifiDisplays = false;
                    mHandler.removeCallbacks(this);
                }
            }
        }

        @java.lang.Override
        public void run() {
            if (mActivelyScanningWifiDisplays) {
                try {
                    mScanWifiDisplaysMethod.invoke(mDisplayManager);
                } catch (java.lang.IllegalAccessException ex) {
                    android.util.Log.w(android.support.v7.media.MediaRouterJellybeanMr1.TAG, "Cannot scan for wifi displays.", ex);
                } catch (java.lang.reflect.InvocationTargetException ex) {
                    android.util.Log.w(android.support.v7.media.MediaRouterJellybeanMr1.TAG, "Cannot scan for wifi displays.", ex);
                }
                mHandler.postDelayed(this, android.support.v7.media.MediaRouterJellybeanMr1.ActiveScanWorkaround.WIFI_DISPLAY_SCAN_INTERVAL);
            }
        }
    }

    /**
     * Workaround the fact that the isConnecting() method does not exist in JB MR1.
     * Do not use on JB MR2 and above.
     */
    public static final class IsConnectingWorkaround {
        private java.lang.reflect.Method mGetStatusCodeMethod;

        private int mStatusConnecting;

        public IsConnectingWorkaround() {
            if (android.os.Build.VERSION.SDK_INT != 17) {
                throw new java.lang.UnsupportedOperationException();
            }
            try {
                java.lang.reflect.Field statusConnectingField = android.media.MediaRouter.RouteInfo.class.getField("STATUS_CONNECTING");
                mStatusConnecting = statusConnectingField.getInt(null);
                mGetStatusCodeMethod = android.media.MediaRouter.RouteInfo.class.getMethod("getStatusCode");
            } catch (java.lang.NoSuchFieldException ex) {
            } catch (java.lang.NoSuchMethodException ex) {
            } catch (java.lang.IllegalAccessException ex) {
            }
        }

        public boolean isConnecting(java.lang.Object routeObj) {
            android.media.MediaRouter.RouteInfo route = ((android.media.MediaRouter.RouteInfo) (routeObj));
            if (mGetStatusCodeMethod != null) {
                try {
                    int statusCode = ((java.lang.Integer) (mGetStatusCodeMethod.invoke(route)));
                    return statusCode == mStatusConnecting;
                } catch (java.lang.IllegalAccessException ex) {
                } catch (java.lang.reflect.InvocationTargetException ex) {
                }
            }
            // Assume not connecting.
            return false;
        }
    }

    static class CallbackProxy<T extends android.support.v7.media.MediaRouterJellybeanMr1.Callback> extends android.support.v7.media.MediaRouterJellybean.CallbackProxy<T> {
        public CallbackProxy(T callback) {
            super(callback);
        }

        @java.lang.Override
        public void onRoutePresentationDisplayChanged(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo route) {
            mCallback.onRoutePresentationDisplayChanged(route);
        }
    }
}

