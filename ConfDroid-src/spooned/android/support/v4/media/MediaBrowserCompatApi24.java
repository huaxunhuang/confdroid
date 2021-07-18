/**
 * Copyright (C) 2016 The Android Open Source Project
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


class MediaBrowserCompatApi24 {
    public static java.lang.Object createSubscriptionCallback(android.support.v4.media.MediaBrowserCompatApi24.SubscriptionCallback callback) {
        return new android.support.v4.media.MediaBrowserCompatApi24.SubscriptionCallbackProxy<>(callback);
    }

    public static void subscribe(java.lang.Object browserObj, java.lang.String parentId, android.os.Bundle options, java.lang.Object subscriptionCallbackObj) {
        ((android.media.browse.MediaBrowser) (browserObj)).subscribe(parentId, options, ((android.media.browse.MediaBrowser.SubscriptionCallback) (subscriptionCallbackObj)));
    }

    public static void unsubscribe(java.lang.Object browserObj, java.lang.String parentId, java.lang.Object subscriptionCallbackObj) {
        ((android.media.browse.MediaBrowser) (browserObj)).unsubscribe(parentId, ((android.media.browse.MediaBrowser.SubscriptionCallback) (subscriptionCallbackObj)));
    }

    interface SubscriptionCallback extends android.support.v4.media.MediaBrowserCompatApi21.SubscriptionCallback {
        void onChildrenLoaded(@android.support.annotation.NonNull
        java.lang.String parentId, java.util.List<?> children, @android.support.annotation.NonNull
        android.os.Bundle options);

        void onError(@android.support.annotation.NonNull
        java.lang.String parentId, @android.support.annotation.NonNull
        android.os.Bundle options);
    }

    static class SubscriptionCallbackProxy<T extends android.support.v4.media.MediaBrowserCompatApi24.SubscriptionCallback> extends android.support.v4.media.MediaBrowserCompatApi21.SubscriptionCallbackProxy<T> {
        public SubscriptionCallbackProxy(T callback) {
            super(callback);
        }

        @java.lang.Override
        public void onChildrenLoaded(@android.support.annotation.NonNull
        java.lang.String parentId, java.util.List<android.media.browse.MediaBrowser.MediaItem> children, @android.support.annotation.NonNull
        android.os.Bundle options) {
            mSubscriptionCallback.onChildrenLoaded(parentId, children, options);
        }

        @java.lang.Override
        public void onError(@android.support.annotation.NonNull
        java.lang.String parentId, @android.support.annotation.NonNull
        android.os.Bundle options) {
            mSubscriptionCallback.onError(parentId, options);
        }
    }
}

