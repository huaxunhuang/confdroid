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


class MediaBrowserServiceCompatApi23 {
    public static java.lang.Object createService(android.content.Context context, android.support.v4.media.MediaBrowserServiceCompatApi23.ServiceCompatProxy serviceProxy) {
        return new android.support.v4.media.MediaBrowserServiceCompatApi23.MediaBrowserServiceAdaptor(context, serviceProxy);
    }

    public interface ServiceCompatProxy extends android.support.v4.media.MediaBrowserServiceCompatApi21.ServiceCompatProxy {
        void onLoadItem(java.lang.String itemId, android.support.v4.media.MediaBrowserServiceCompatApi21.ResultWrapper<android.os.Parcel> result);
    }

    static class MediaBrowserServiceAdaptor extends android.support.v4.media.MediaBrowserServiceCompatApi21.MediaBrowserServiceAdaptor {
        MediaBrowserServiceAdaptor(android.content.Context context, android.support.v4.media.MediaBrowserServiceCompatApi23.ServiceCompatProxy serviceWrapper) {
            super(context, serviceWrapper);
        }

        @java.lang.Override
        public void onLoadItem(java.lang.String itemId, android.service.media.MediaBrowserService.Result<android.media.browse.MediaBrowser.MediaItem> result) {
            ((android.support.v4.media.MediaBrowserServiceCompatApi23.ServiceCompatProxy) (mServiceProxy)).onLoadItem(itemId, new android.support.v4.media.MediaBrowserServiceCompatApi21.ResultWrapper<android.os.Parcel>(result));
        }
    }
}

