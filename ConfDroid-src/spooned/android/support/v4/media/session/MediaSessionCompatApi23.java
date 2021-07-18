/**
 * Copyright (C) 2014 The Android Open Source Project
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


class MediaSessionCompatApi23 {
    public static java.lang.Object createCallback(android.support.v4.media.session.MediaSessionCompatApi23.Callback callback) {
        return new android.support.v4.media.session.MediaSessionCompatApi23.CallbackProxy<android.support.v4.media.session.MediaSessionCompatApi23.Callback>(callback);
    }

    public interface Callback extends android.support.v4.media.session.MediaSessionCompatApi21.Callback {
        public void onPlayFromUri(android.net.Uri uri, android.os.Bundle extras);
    }

    static class CallbackProxy<T extends android.support.v4.media.session.MediaSessionCompatApi23.Callback> extends android.support.v4.media.session.MediaSessionCompatApi21.CallbackProxy<T> {
        public CallbackProxy(T callback) {
            super(callback);
        }

        @java.lang.Override
        public void onPlayFromUri(android.net.Uri uri, android.os.Bundle extras) {
            mCallback.onPlayFromUri(uri, extras);
        }
    }
}

