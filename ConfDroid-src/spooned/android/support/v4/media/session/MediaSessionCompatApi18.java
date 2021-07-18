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


class MediaSessionCompatApi18 {
    private static final java.lang.String TAG = "MediaSessionCompatApi18";

    /**
     * *** PlaybackState actions ****
     */
    private static final long ACTION_SEEK_TO = 1 << 8;

    private static boolean sIsMbrPendingIntentSupported = true;

    public static java.lang.Object createPlaybackPositionUpdateListener(android.support.v4.media.session.MediaSessionCompatApi18.Callback callback) {
        return new android.support.v4.media.session.MediaSessionCompatApi18.OnPlaybackPositionUpdateListener<android.support.v4.media.session.MediaSessionCompatApi18.Callback>(callback);
    }

    public static void registerMediaButtonEventReceiver(android.content.Context context, android.app.PendingIntent pi, android.content.ComponentName cn) {
        android.media.AudioManager am = ((android.media.AudioManager) (context.getSystemService(android.content.Context.AUDIO_SERVICE)));
        // Some Android implementations are not able to register a media button event receiver
        // using a PendingIntent but need a ComponentName instead. These will raise a
        // NullPointerException.
        if (android.support.v4.media.session.MediaSessionCompatApi18.sIsMbrPendingIntentSupported) {
            try {
                am.registerMediaButtonEventReceiver(pi);
            } catch (java.lang.NullPointerException e) {
                android.util.Log.w(android.support.v4.media.session.MediaSessionCompatApi18.TAG, "Unable to register media button event receiver with " + "PendingIntent, falling back to ComponentName.");
                android.support.v4.media.session.MediaSessionCompatApi18.sIsMbrPendingIntentSupported = false;
            }
        }
        if (!android.support.v4.media.session.MediaSessionCompatApi18.sIsMbrPendingIntentSupported) {
            am.registerMediaButtonEventReceiver(cn);
        }
    }

    public static void unregisterMediaButtonEventReceiver(android.content.Context context, android.app.PendingIntent pi, android.content.ComponentName cn) {
        android.media.AudioManager am = ((android.media.AudioManager) (context.getSystemService(android.content.Context.AUDIO_SERVICE)));
        if (android.support.v4.media.session.MediaSessionCompatApi18.sIsMbrPendingIntentSupported) {
            am.unregisterMediaButtonEventReceiver(pi);
        } else {
            am.unregisterMediaButtonEventReceiver(cn);
        }
    }

    public static void setState(java.lang.Object rccObj, int state, long position, float speed, long updateTime) {
        long currTime = android.os.SystemClock.elapsedRealtime();
        if ((state == android.support.v4.media.session.MediaSessionCompatApi14.STATE_PLAYING) && (position > 0)) {
            long diff = 0;
            if (updateTime > 0) {
                diff = currTime - updateTime;
                if ((speed > 0) && (speed != 1.0F)) {
                    diff *= speed;
                }
            }
            position += diff;
        }
        state = android.support.v4.media.session.MediaSessionCompatApi14.getRccStateFromState(state);
        ((android.media.RemoteControlClient) (rccObj)).setPlaybackState(state, position, speed);
    }

    public static void setTransportControlFlags(java.lang.Object rccObj, long actions) {
        ((android.media.RemoteControlClient) (rccObj)).setTransportControlFlags(android.support.v4.media.session.MediaSessionCompatApi18.getRccTransportControlFlagsFromActions(actions));
    }

    public static void setOnPlaybackPositionUpdateListener(java.lang.Object rccObj, java.lang.Object onPositionUpdateObj) {
        ((android.media.RemoteControlClient) (rccObj)).setPlaybackPositionUpdateListener(((android.media.RemoteControlClient.OnPlaybackPositionUpdateListener) (onPositionUpdateObj)));
    }

    static int getRccTransportControlFlagsFromActions(long actions) {
        int transportControlFlags = android.support.v4.media.session.MediaSessionCompatApi14.getRccTransportControlFlagsFromActions(actions);
        if ((actions & android.support.v4.media.session.MediaSessionCompatApi18.ACTION_SEEK_TO) != 0) {
            transportControlFlags |= android.media.RemoteControlClient.FLAG_KEY_MEDIA_POSITION_UPDATE;
        }
        return transportControlFlags;
    }

    static class OnPlaybackPositionUpdateListener<T extends android.support.v4.media.session.MediaSessionCompatApi18.Callback> implements android.media.RemoteControlClient.OnPlaybackPositionUpdateListener {
        protected final T mCallback;

        public OnPlaybackPositionUpdateListener(T callback) {
            mCallback = callback;
        }

        @java.lang.Override
        public void onPlaybackPositionUpdate(long newPositionMs) {
            mCallback.onSeekTo(newPositionMs);
        }
    }

    interface Callback {
        public void onSeekTo(long pos);
    }
}

