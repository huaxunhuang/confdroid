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


class MediaSessionCompatApi14 {
    /**
     * *** RemoteControlClient States, we only need none as the others were public ******
     */
    static final int RCC_PLAYSTATE_NONE = 0;

    /**
     * *** MediaSession States ******
     */
    static final int STATE_NONE = 0;

    static final int STATE_STOPPED = 1;

    static final int STATE_PAUSED = 2;

    static final int STATE_PLAYING = 3;

    static final int STATE_FAST_FORWARDING = 4;

    static final int STATE_REWINDING = 5;

    static final int STATE_BUFFERING = 6;

    static final int STATE_ERROR = 7;

    static final int STATE_CONNECTING = 8;

    static final int STATE_SKIPPING_TO_PREVIOUS = 9;

    static final int STATE_SKIPPING_TO_NEXT = 10;

    static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;

    /**
     * *** PlaybackState actions ****
     */
    private static final long ACTION_STOP = 1 << 0;

    private static final long ACTION_PAUSE = 1 << 1;

    private static final long ACTION_PLAY = 1 << 2;

    private static final long ACTION_REWIND = 1 << 3;

    private static final long ACTION_SKIP_TO_PREVIOUS = 1 << 4;

    private static final long ACTION_SKIP_TO_NEXT = 1 << 5;

    private static final long ACTION_FAST_FORWARD = 1 << 6;

    private static final long ACTION_PLAY_PAUSE = 1 << 9;

    /**
     * *** MediaMetadata keys *******
     */
    private static final java.lang.String METADATA_KEY_ART = "android.media.metadata.ART";

    private static final java.lang.String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";

    private static final java.lang.String METADATA_KEY_TITLE = "android.media.metadata.TITLE";

    private static final java.lang.String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";

    private static final java.lang.String METADATA_KEY_DURATION = "android.media.metadata.DURATION";

    private static final java.lang.String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";

    private static final java.lang.String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";

    private static final java.lang.String METADATA_KEY_WRITER = "android.media.metadata.WRITER";

    private static final java.lang.String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";

    private static final java.lang.String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";

    private static final java.lang.String METADATA_KEY_DATE = "android.media.metadata.DATE";

    private static final java.lang.String METADATA_KEY_GENRE = "android.media.metadata.GENRE";

    private static final java.lang.String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";

    private static final java.lang.String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";

    private static final java.lang.String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";

    public static java.lang.Object createRemoteControlClient(android.app.PendingIntent mbIntent) {
        return new android.media.RemoteControlClient(mbIntent);
    }

    public static void setState(java.lang.Object rccObj, int state) {
        ((android.media.RemoteControlClient) (rccObj)).setPlaybackState(android.support.v4.media.session.MediaSessionCompatApi14.getRccStateFromState(state));
    }

    public static void setTransportControlFlags(java.lang.Object rccObj, long actions) {
        ((android.media.RemoteControlClient) (rccObj)).setTransportControlFlags(android.support.v4.media.session.MediaSessionCompatApi14.getRccTransportControlFlagsFromActions(actions));
    }

    public static void setMetadata(java.lang.Object rccObj, android.os.Bundle metadata) {
        android.media.RemoteControlClient.MetadataEditor editor = ((android.media.RemoteControlClient) (rccObj)).editMetadata(true);
        android.support.v4.media.session.MediaSessionCompatApi14.buildOldMetadata(metadata, editor);
        editor.apply();
    }

    public static void registerRemoteControlClient(android.content.Context context, java.lang.Object rccObj) {
        android.media.AudioManager am = ((android.media.AudioManager) (context.getSystemService(android.content.Context.AUDIO_SERVICE)));
        am.registerRemoteControlClient(((android.media.RemoteControlClient) (rccObj)));
    }

    public static void unregisterRemoteControlClient(android.content.Context context, java.lang.Object rccObj) {
        android.media.AudioManager am = ((android.media.AudioManager) (context.getSystemService(android.content.Context.AUDIO_SERVICE)));
        am.unregisterRemoteControlClient(((android.media.RemoteControlClient) (rccObj)));
    }

    static int getRccStateFromState(int state) {
        switch (state) {
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_CONNECTING :
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_BUFFERING :
                return android.media.RemoteControlClient.PLAYSTATE_BUFFERING;
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_ERROR :
                return android.media.RemoteControlClient.PLAYSTATE_ERROR;
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_FAST_FORWARDING :
                return android.media.RemoteControlClient.PLAYSTATE_FAST_FORWARDING;
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_NONE :
                return android.support.v4.media.session.MediaSessionCompatApi14.RCC_PLAYSTATE_NONE;
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_PAUSED :
                return android.media.RemoteControlClient.PLAYSTATE_PAUSED;
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_PLAYING :
                return android.media.RemoteControlClient.PLAYSTATE_PLAYING;
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_REWINDING :
                return android.media.RemoteControlClient.PLAYSTATE_REWINDING;
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_SKIPPING_TO_PREVIOUS :
                return android.media.RemoteControlClient.PLAYSTATE_SKIPPING_BACKWARDS;
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_SKIPPING_TO_NEXT :
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_SKIPPING_TO_QUEUE_ITEM :
                return android.media.RemoteControlClient.PLAYSTATE_SKIPPING_FORWARDS;
            case android.support.v4.media.session.MediaSessionCompatApi14.STATE_STOPPED :
                return android.media.RemoteControlClient.PLAYSTATE_STOPPED;
            default :
                return -1;
        }
    }

    static int getRccTransportControlFlagsFromActions(long actions) {
        int transportControlFlags = 0;
        if ((actions & android.support.v4.media.session.MediaSessionCompatApi14.ACTION_STOP) != 0) {
            transportControlFlags |= android.media.RemoteControlClient.FLAG_KEY_MEDIA_STOP;
        }
        if ((actions & android.support.v4.media.session.MediaSessionCompatApi14.ACTION_PAUSE) != 0) {
            transportControlFlags |= android.media.RemoteControlClient.FLAG_KEY_MEDIA_PAUSE;
        }
        if ((actions & android.support.v4.media.session.MediaSessionCompatApi14.ACTION_PLAY) != 0) {
            transportControlFlags |= android.media.RemoteControlClient.FLAG_KEY_MEDIA_PLAY;
        }
        if ((actions & android.support.v4.media.session.MediaSessionCompatApi14.ACTION_REWIND) != 0) {
            transportControlFlags |= android.media.RemoteControlClient.FLAG_KEY_MEDIA_REWIND;
        }
        if ((actions & android.support.v4.media.session.MediaSessionCompatApi14.ACTION_SKIP_TO_PREVIOUS) != 0) {
            transportControlFlags |= android.media.RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS;
        }
        if ((actions & android.support.v4.media.session.MediaSessionCompatApi14.ACTION_SKIP_TO_NEXT) != 0) {
            transportControlFlags |= android.media.RemoteControlClient.FLAG_KEY_MEDIA_NEXT;
        }
        if ((actions & android.support.v4.media.session.MediaSessionCompatApi14.ACTION_FAST_FORWARD) != 0) {
            transportControlFlags |= android.media.RemoteControlClient.FLAG_KEY_MEDIA_FAST_FORWARD;
        }
        if ((actions & android.support.v4.media.session.MediaSessionCompatApi14.ACTION_PLAY_PAUSE) != 0) {
            transportControlFlags |= android.media.RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE;
        }
        return transportControlFlags;
    }

    static void buildOldMetadata(android.os.Bundle metadata, android.media.RemoteControlClient.MetadataEditor editor) {
        if (metadata == null) {
            return;
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_ART)) {
            android.graphics.Bitmap art = metadata.getParcelable(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_ART);
            editor.putBitmap(android.media.RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, art);
        } else
            if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_ALBUM_ART)) {
                // Fall back to album art if the track art wasn't available
                android.graphics.Bitmap art = metadata.getParcelable(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_ALBUM_ART);
                editor.putBitmap(android.media.RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, art);
            }

        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_ALBUM)) {
            editor.putString(android.media.MediaMetadataRetriever.METADATA_KEY_ALBUM, metadata.getString(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_ALBUM));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_ALBUM_ARTIST)) {
            editor.putString(android.media.MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST, metadata.getString(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_ALBUM_ARTIST));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_ARTIST)) {
            editor.putString(android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST, metadata.getString(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_ARTIST));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_AUTHOR)) {
            editor.putString(android.media.MediaMetadataRetriever.METADATA_KEY_AUTHOR, metadata.getString(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_AUTHOR));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_COMPILATION)) {
            editor.putString(android.media.MediaMetadataRetriever.METADATA_KEY_COMPILATION, metadata.getString(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_COMPILATION));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_COMPOSER)) {
            editor.putString(android.media.MediaMetadataRetriever.METADATA_KEY_COMPOSER, metadata.getString(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_COMPOSER));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_DATE)) {
            editor.putString(android.media.MediaMetadataRetriever.METADATA_KEY_DATE, metadata.getString(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_DATE));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_DISC_NUMBER)) {
            editor.putLong(android.media.MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER, metadata.getLong(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_DISC_NUMBER));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_DURATION)) {
            editor.putLong(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION, metadata.getLong(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_DURATION));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_GENRE)) {
            editor.putString(android.media.MediaMetadataRetriever.METADATA_KEY_GENRE, metadata.getString(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_GENRE));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_TITLE)) {
            editor.putString(android.media.MediaMetadataRetriever.METADATA_KEY_TITLE, metadata.getString(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_TITLE));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_TRACK_NUMBER)) {
            editor.putLong(android.media.MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER, metadata.getLong(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_TRACK_NUMBER));
        }
        if (metadata.containsKey(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_WRITER)) {
            editor.putString(android.media.MediaMetadataRetriever.METADATA_KEY_WRITER, metadata.getString(android.support.v4.media.session.MediaSessionCompatApi14.METADATA_KEY_WRITER));
        }
    }
}

