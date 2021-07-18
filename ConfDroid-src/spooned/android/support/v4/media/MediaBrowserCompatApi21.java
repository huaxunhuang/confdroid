/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.v4.media;


class MediaBrowserCompatApi21 {
    static final java.lang.String NULL_MEDIA_ITEM_ID = "android.support.v4.media.MediaBrowserCompat.NULL_MEDIA_ITEM";

    public static java.lang.Object createConnectionCallback(android.support.v4.media.MediaBrowserCompatApi21.ConnectionCallback callback) {
        return new android.support.v4.media.MediaBrowserCompatApi21.ConnectionCallbackProxy<>(callback);
    }

    public static java.lang.Object createBrowser(android.content.Context context, android.content.ComponentName serviceComponent, java.lang.Object callback, android.os.Bundle rootHints) {
        return new android.media.browse.MediaBrowser(context, serviceComponent, ((android.media.browse.MediaBrowser.ConnectionCallback) (callback)), rootHints);
    }

    public static void connect(java.lang.Object browserObj) {
        ((android.media.browse.MediaBrowser) (browserObj)).connect();
    }

    public static void disconnect(java.lang.Object browserObj) {
        ((android.media.browse.MediaBrowser) (browserObj)).disconnect();
    }

    public static boolean isConnected(java.lang.Object browserObj) {
        return ((android.media.browse.MediaBrowser) (browserObj)).isConnected();
    }

    public static android.content.ComponentName getServiceComponent(java.lang.Object browserObj) {
        return ((android.media.browse.MediaBrowser) (browserObj)).getServiceComponent();
    }

    public static java.lang.String getRoot(java.lang.Object browserObj) {
        return ((android.media.browse.MediaBrowser) (browserObj)).getRoot();
    }

    public static android.os.Bundle getExtras(java.lang.Object browserObj) {
        return ((android.media.browse.MediaBrowser) (browserObj)).getExtras();
    }

    public static java.lang.Object getSessionToken(java.lang.Object browserObj) {
        return ((android.media.browse.MediaBrowser) (browserObj)).getSessionToken();
    }

    public static java.lang.Object createSubscriptionCallback(android.support.v4.media.MediaBrowserCompatApi21.SubscriptionCallback callback) {
        return new android.support.v4.media.MediaBrowserCompatApi21.SubscriptionCallbackProxy<>(callback);
    }

    public static void subscribe(java.lang.Object browserObj, java.lang.String parentId, java.lang.Object subscriptionCallbackObj) {
        ((android.media.browse.MediaBrowser) (browserObj)).subscribe(parentId, ((android.media.browse.MediaBrowser.SubscriptionCallback) (subscriptionCallbackObj)));
    }

    public static void unsubscribe(java.lang.Object browserObj, java.lang.String parentId) {
        ((android.media.browse.MediaBrowser) (browserObj)).unsubscribe(parentId);
    }

    interface ConnectionCallback {
        void onConnected();

        void onConnectionSuspended();

        void onConnectionFailed();
    }

    static class ConnectionCallbackProxy<T extends android.support.v4.media.MediaBrowserCompatApi21.ConnectionCallback> extends android.media.browse.MediaBrowser.ConnectionCallback {
        protected final T mConnectionCallback;

        public ConnectionCallbackProxy(T connectionCallback) {
            mConnectionCallback = connectionCallback;
        }

        @java.lang.Override
        public void onConnected() {
            mConnectionCallback.onConnected();
        }

        @java.lang.Override
        public void onConnectionSuspended() {
            mConnectionCallback.onConnectionSuspended();
        }

        @java.lang.Override
        public void onConnectionFailed() {
            mConnectionCallback.onConnectionFailed();
        }
    }

    interface SubscriptionCallback {
        void onChildrenLoaded(@android.support.annotation.NonNull
        java.lang.String parentId, java.util.List<?> children);

        void onError(@android.support.annotation.NonNull
        java.lang.String parentId);
    }

    static class SubscriptionCallbackProxy<T extends android.support.v4.media.MediaBrowserCompatApi21.SubscriptionCallback> extends android.media.browse.MediaBrowser.SubscriptionCallback {
        protected final T mSubscriptionCallback;

        public SubscriptionCallbackProxy(T callback) {
            mSubscriptionCallback = callback;
        }

        @java.lang.Override
        public void onChildrenLoaded(@android.support.annotation.NonNull
        java.lang.String parentId, java.util.List<android.media.browse.MediaBrowser.MediaItem> children) {
            mSubscriptionCallback.onChildrenLoaded(parentId, children);
        }

        @java.lang.Override
        public void onError(@android.support.annotation.NonNull
        java.lang.String parentId) {
            mSubscriptionCallback.onError(parentId);
        }
    }

    static class MediaItem {
        public static int getFlags(java.lang.Object itemObj) {
            return ((android.media.browse.MediaBrowser.MediaItem) (itemObj)).getFlags();
        }

        public static java.lang.Object getDescription(java.lang.Object itemObj) {
            return ((android.media.browse.MediaBrowser.MediaItem) (itemObj)).getDescription();
        }
    }
}

