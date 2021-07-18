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


class MediaBrowserCompatApi23 {
    public static java.lang.Object createItemCallback(android.support.v4.media.MediaBrowserCompatApi23.ItemCallback callback) {
        return new android.support.v4.media.MediaBrowserCompatApi23.ItemCallbackProxy<>(callback);
    }

    public static void getItem(java.lang.Object browserObj, java.lang.String mediaId, java.lang.Object itemCallbackObj) {
        ((android.media.browse.MediaBrowser) (browserObj)).getItem(mediaId, ((android.media.browse.MediaBrowser.ItemCallback) (itemCallbackObj)));
    }

    interface ItemCallback {
        void onItemLoaded(android.os.Parcel itemParcel);

        void onError(@android.support.annotation.NonNull
        java.lang.String itemId);
    }

    static class ItemCallbackProxy<T extends android.support.v4.media.MediaBrowserCompatApi23.ItemCallback> extends android.media.browse.MediaBrowser.ItemCallback {
        protected final T mItemCallback;

        public ItemCallbackProxy(T callback) {
            mItemCallback = callback;
        }

        @java.lang.Override
        public void onItemLoaded(android.media.browse.MediaBrowser.MediaItem item) {
            android.os.Parcel parcel = android.os.Parcel.obtain();
            item.writeToParcel(parcel, 0);
            mItemCallback.onItemLoaded(parcel);
        }

        @java.lang.Override
        public void onError(@android.support.annotation.NonNull
        java.lang.String itemId) {
            mItemCallback.onError(itemId);
        }
    }
}

