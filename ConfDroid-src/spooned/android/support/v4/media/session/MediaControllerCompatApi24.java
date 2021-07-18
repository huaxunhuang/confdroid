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


class MediaControllerCompatApi24 {
    public static class TransportControls extends android.support.v4.media.session.MediaControllerCompatApi23.TransportControls {
        public static void prepare(java.lang.Object controlsObj) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).prepare();
        }

        public static void prepareFromMediaId(java.lang.Object controlsObj, java.lang.String mediaId, android.os.Bundle extras) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).prepareFromMediaId(mediaId, extras);
        }

        public static void prepareFromSearch(java.lang.Object controlsObj, java.lang.String query, android.os.Bundle extras) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).prepareFromSearch(query, extras);
        }

        public static void prepareFromUri(java.lang.Object controlsObj, android.net.Uri uri, android.os.Bundle extras) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).prepareFromUri(uri, extras);
        }
    }
}

