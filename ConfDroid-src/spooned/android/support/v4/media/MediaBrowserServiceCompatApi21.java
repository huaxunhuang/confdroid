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


class MediaBrowserServiceCompatApi21 {
    public static java.lang.Object createService(android.content.Context context, android.support.v4.media.MediaBrowserServiceCompatApi21.ServiceCompatProxy serviceProxy) {
        return new android.support.v4.media.MediaBrowserServiceCompatApi21.MediaBrowserServiceAdaptor(context, serviceProxy);
    }

    public static void onCreate(java.lang.Object serviceObj) {
        ((android.service.media.MediaBrowserService) (serviceObj)).onCreate();
    }

    public static android.os.IBinder onBind(java.lang.Object serviceObj, android.content.Intent intent) {
        return ((android.service.media.MediaBrowserService) (serviceObj)).onBind(intent);
    }

    public static void setSessionToken(java.lang.Object serviceObj, java.lang.Object token) {
        ((android.service.media.MediaBrowserService) (serviceObj)).setSessionToken(((android.media.session.MediaSession.Token) (token)));
    }

    public static void notifyChildrenChanged(java.lang.Object serviceObj, java.lang.String parentId) {
        ((android.service.media.MediaBrowserService) (serviceObj)).notifyChildrenChanged(parentId);
    }

    public interface ServiceCompatProxy {
        android.support.v4.media.MediaBrowserServiceCompatApi21.BrowserRoot onGetRoot(java.lang.String clientPackageName, int clientUid, android.os.Bundle rootHints);

        void onLoadChildren(java.lang.String parentId, android.support.v4.media.MediaBrowserServiceCompatApi21.ResultWrapper<java.util.List<android.os.Parcel>> result);
    }

    static class ResultWrapper<T> {
        android.service.media.MediaBrowserService.Result mResultObj;

        ResultWrapper(android.service.media.MediaBrowserService.Result result) {
            mResultObj = result;
        }

        public void sendResult(T result) {
            if (result instanceof java.util.List) {
                mResultObj.sendResult(parcelListToItemList(((java.util.List<android.os.Parcel>) (result))));
            } else
                if (result instanceof android.os.Parcel) {
                    android.os.Parcel parcel = ((android.os.Parcel) (result));
                    mResultObj.sendResult(android.media.browse.MediaBrowser.MediaItem.CREATOR.createFromParcel(parcel));
                    parcel.recycle();
                } else {
                    // The result is null or an invalid instance.
                    mResultObj.sendResult(null);
                }

        }

        public void detach() {
            mResultObj.detach();
        }

        java.util.List<android.media.browse.MediaBrowser.MediaItem> parcelListToItemList(java.util.List<android.os.Parcel> parcelList) {
            if (parcelList == null) {
                return null;
            }
            java.util.List<android.media.browse.MediaBrowser.MediaItem> items = new java.util.ArrayList<>();
            for (android.os.Parcel parcel : parcelList) {
                parcel.setDataPosition(0);
                items.add(android.media.browse.MediaBrowser.MediaItem.CREATOR.createFromParcel(parcel));
                parcel.recycle();
            }
            return items;
        }
    }

    static class BrowserRoot {
        final java.lang.String mRootId;

        final android.os.Bundle mExtras;

        BrowserRoot(java.lang.String rootId, android.os.Bundle extras) {
            mRootId = rootId;
            mExtras = extras;
        }
    }

    static class MediaBrowserServiceAdaptor extends android.service.media.MediaBrowserService {
        final android.support.v4.media.MediaBrowserServiceCompatApi21.ServiceCompatProxy mServiceProxy;

        MediaBrowserServiceAdaptor(android.content.Context context, android.support.v4.media.MediaBrowserServiceCompatApi21.ServiceCompatProxy serviceWrapper) {
            attachBaseContext(context);
            mServiceProxy = serviceWrapper;
        }

        @java.lang.Override
        public android.service.media.MediaBrowserService.BrowserRoot onGetRoot(java.lang.String clientPackageName, int clientUid, android.os.Bundle rootHints) {
            android.support.v4.media.MediaBrowserServiceCompatApi21.BrowserRoot browserRoot = mServiceProxy.onGetRoot(clientPackageName, clientUid, rootHints);
            return browserRoot == null ? null : new android.service.media.MediaBrowserService.BrowserRoot(browserRoot.mRootId, browserRoot.mExtras);
        }

        @java.lang.Override
        public void onLoadChildren(java.lang.String parentId, android.service.media.MediaBrowserService.Result<java.util.List<android.media.browse.MediaBrowser.MediaItem>> result) {
            mServiceProxy.onLoadChildren(parentId, new android.support.v4.media.MediaBrowserServiceCompatApi21.ResultWrapper<java.util.List<android.os.Parcel>>(result));
        }
    }
}

