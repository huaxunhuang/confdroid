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


class MediaSessionCompatApi19 {
    /**
     * *** PlaybackState actions ****
     */
    private static final long ACTION_SET_RATING = 1 << 7;

    /**
     * *** MediaMetadata keys *******
     */
    private static final java.lang.String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";

    private static final java.lang.String METADATA_KEY_RATING = "android.media.metadata.RATING";

    private static final java.lang.String METADATA_KEY_YEAR = "android.media.metadata.YEAR";

    public static void setTransportControlFlags(java.lang.Object rccObj, long actions) {
        ((android.media.RemoteControlClient) (rccObj)).setTransportControlFlags(android.support.v4.media.session.MediaSessionCompatApi19.getRccTransportControlFlagsFromActions(actions));
    }

    public static java.lang.Object createMetadataUpdateListener(android.support.v4.media.session.MediaSessionCompatApi19.Callback callback) {
        return new android.support.v4.media.session.MediaSessionCompatApi19.OnMetadataUpdateListener<android.support.v4.media.session.MediaSessionCompatApi19.Callback>(callback);
    }

    public static void setMetadata(java.lang.Object rccObj, android.os.Bundle metadata, long actions) {
        android.media.RemoteControlClient.MetadataEditor editor = ((android.media.RemoteControlClient) (rccObj)).editMetadata(true);
        android.support.v4.media.session.MediaSessionCompatApi14.buildOldMetadata(metadata, editor);
        android.support.v4.media.session.MediaSessionCompatApi19.addNewMetadata(metadata, editor);
        if ((actions & android.support.v4.media.session.MediaSessionCompatApi19.ACTION_SET_RATING) != 0) {
            editor.addEditableKey(android.media.RemoteControlClient.MetadataEditor.RATING_KEY_BY_USER);
        }
        editor.apply();
    }

    public static void setOnMetadataUpdateListener(java.lang.Object rccObj, java.lang.Object onMetadataUpdateObj) {
        ((android.media.RemoteControlClient) (rccObj)).setMetadataUpdateListener(((android.media.RemoteControlClient.OnMetadataUpdateListener) (onMetadataUpdateObj)));
    }

    static int getRccTransportControlFlagsFromActions(long actions) {
        int transportControlFlags = android.support.v4.media.session.MediaSessionCompatApi18.getRccTransportControlFlagsFromActions(actions);
        if ((actions & android.support.v4.media.session.MediaSessionCompatApi19.ACTION_SET_RATING) != 0) {
            transportControlFlags |= android.media.RemoteControlClient.FLAG_KEY_MEDIA_RATING;
        }
        return transportControlFlags;
    }

    static void addNewMetadata(android.os.Bundle metadata, android.media.RemoteControlClient.MetadataEditor editor) {
        if (metadata == null) {
            return;
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi19.METADATA_KEY_YEAR)) {
            editor.putLong(android.media.MediaMetadataRetriever.METADATA_KEY_YEAR, metadata.getLong(android.support.v4.media.session.MediaSessionCompatApi19.METADATA_KEY_YEAR));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi19.METADATA_KEY_RATING)) {
            editor.putObject(android.media.MediaMetadataEditor.RATING_KEY_BY_OTHERS, metadata.getParcelable(android.support.v4.media.session.MediaSessionCompatApi19.METADATA_KEY_RATING));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi19.METADATA_KEY_USER_RATING)) {
            editor.putObject(android.media.MediaMetadataEditor.RATING_KEY_BY_USER, metadata.getParcelable(android.support.v4.media.session.MediaSessionCompatApi19.METADATA_KEY_USER_RATING));
        }
    }

    static class OnMetadataUpdateListener<T extends android.support.v4.media.session.MediaSessionCompatApi19.Callback> implements android.media.RemoteControlClient.OnMetadataUpdateListener {
        protected final T mCallback;

        public OnMetadataUpdateListener(T callback) {
            mCallback = callback;
        }

        @java.lang.Override
        public void onMetadataUpdate(int key, java.lang.Object newValue) {
            if ((key == android.media.MediaMetadataEditor.RATING_KEY_BY_USER) && (newValue instanceof android.media.Rating)) {
                mCallback.onSetRating(newValue);
            }
        }
    }

    interface Callback extends android.support.v4.media.session.MediaSessionCompatApi18.Callback {
        public void onSetRating(java.lang.Object ratingObj);
    }
}

