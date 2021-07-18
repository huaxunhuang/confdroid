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
package android.support.v4.media.session;


class MediaSessionCompatApi24 {
    private static final java.lang.String TAG = "MediaSessionCompatApi24";

    public static java.lang.Object createCallback(android.support.v4.media.session.MediaSessionCompatApi24.Callback callback) {
        return new android.support.v4.media.session.MediaSessionCompatApi24.CallbackProxy<android.support.v4.media.session.MediaSessionCompatApi24.Callback>(callback);
    }

    public static java.lang.String getCallingPackage(java.lang.Object sessionObj) {
        android.media.session.MediaSession session = ((android.media.session.MediaSession) (sessionObj));
        try {
            java.lang.reflect.Method getCallingPackageMethod = session.getClass().getMethod("getCallingPackage");
            return ((java.lang.String) (getCallingPackageMethod.invoke(session)));
        } catch (java.lang.NoSuchMethodException | java.lang.reflect.InvocationTargetException | java.lang.IllegalAccessException e) {
            android.util.Log.e(android.support.v4.media.session.MediaSessionCompatApi24.TAG, "Cannot execute MediaSession.getCallingPackage()", e);
        }
        return null;
    }

    public interface Callback extends android.support.v4.media.session.MediaSessionCompatApi23.Callback {
        public void onPrepare();

        public void onPrepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras);

        public void onPrepareFromSearch(java.lang.String query, android.os.Bundle extras);

        public void onPrepareFromUri(android.net.Uri uri, android.os.Bundle extras);
    }

    static class CallbackProxy<T extends android.support.v4.media.session.MediaSessionCompatApi24.Callback> extends android.support.v4.media.session.MediaSessionCompatApi23.CallbackProxy<T> {
        public CallbackProxy(T callback) {
            super(callback);
        }

        @java.lang.Override
        public void onPrepare() {
            mCallback.onPrepare();
        }

        @java.lang.Override
        public void onPrepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
            mCallback.onPrepareFromMediaId(mediaId, extras);
        }

        @java.lang.Override
        public void onPrepareFromSearch(java.lang.String query, android.os.Bundle extras) {
            mCallback.onPrepareFromSearch(query, extras);
        }

        @java.lang.Override
        public void onPrepareFromUri(android.net.Uri uri, android.os.Bundle extras) {
            mCallback.onPrepareFromUri(uri, extras);
        }
    }
}

