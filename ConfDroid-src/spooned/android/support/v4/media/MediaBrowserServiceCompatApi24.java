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


class MediaBrowserServiceCompatApi24 {
    private static final java.lang.String TAG = "MBSCompatApi24";

    private static java.lang.reflect.Field sResultFlags;

    static {
        try {
            android.support.v4.media.MediaBrowserServiceCompatApi24.sResultFlags = android.service.media.MediaBrowserService.Result.class.getDeclaredField("mFlags");
            android.support.v4.media.MediaBrowserServiceCompatApi24.sResultFlags.setAccessible(true);
        } catch (java.lang.NoSuchFieldException e) {
            android.util.Log.w(android.support.v4.media.MediaBrowserServiceCompatApi24.TAG, e);
        }
    }

    public static java.lang.Object createService(android.content.Context context, android.support.v4.media.MediaBrowserServiceCompatApi24.ServiceCompatProxy serviceProxy) {
        return new android.support.v4.media.MediaBrowserServiceCompatApi24.MediaBrowserServiceAdaptor(context, serviceProxy);
    }

    public static void notifyChildrenChanged(java.lang.Object serviceObj, java.lang.String parentId, android.os.Bundle options) {
        ((android.service.media.MediaBrowserService) (serviceObj)).notifyChildrenChanged(parentId, options);
    }

    public static android.os.Bundle getBrowserRootHints(java.lang.Object serviceObj) {
        return ((android.service.media.MediaBrowserService) (serviceObj)).getBrowserRootHints();
    }

    public interface ServiceCompatProxy extends android.support.v4.media.MediaBrowserServiceCompatApi23.ServiceCompatProxy {
        void onLoadChildren(java.lang.String parentId, android.support.v4.media.MediaBrowserServiceCompatApi24.ResultWrapper result, android.os.Bundle options);
    }

    static class ResultWrapper {
        android.service.media.MediaBrowserService.Result mResultObj;

        ResultWrapper(android.service.media.MediaBrowserService.Result result) {
            mResultObj = result;
        }

        public void sendResult(java.util.List<android.os.Parcel> result, int flags) {
            try {
                android.support.v4.media.MediaBrowserServiceCompatApi24.sResultFlags.setInt(mResultObj, flags);
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.w(android.support.v4.media.MediaBrowserServiceCompatApi24.TAG, e);
            }
            mResultObj.sendResult(parcelListToItemList(result));
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

    static class MediaBrowserServiceAdaptor extends android.support.v4.media.MediaBrowserServiceCompatApi23.MediaBrowserServiceAdaptor {
        MediaBrowserServiceAdaptor(android.content.Context context, android.support.v4.media.MediaBrowserServiceCompatApi24.ServiceCompatProxy serviceWrapper) {
            super(context, serviceWrapper);
        }

        @java.lang.Override
        public void onLoadChildren(java.lang.String parentId, android.service.media.MediaBrowserService.Result<java.util.List<android.media.browse.MediaBrowser.MediaItem>> result, android.os.Bundle options) {
            ((android.support.v4.media.MediaBrowserServiceCompatApi24.ServiceCompatProxy) (mServiceProxy)).onLoadChildren(parentId, new android.support.v4.media.MediaBrowserServiceCompatApi24.ResultWrapper(result), options);
        }
    }
}

