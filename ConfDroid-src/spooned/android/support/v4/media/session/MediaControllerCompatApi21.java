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


class MediaControllerCompatApi21 {
    public static java.lang.Object fromToken(android.content.Context context, java.lang.Object sessionToken) {
        return new android.media.session.MediaController(context, ((android.media.session.MediaSession.Token) (sessionToken)));
    }

    public static java.lang.Object createCallback(android.support.v4.media.session.MediaControllerCompatApi21.Callback callback) {
        return new android.support.v4.media.session.MediaControllerCompatApi21.CallbackProxy<android.support.v4.media.session.MediaControllerCompatApi21.Callback>(callback);
    }

    public static void registerCallback(java.lang.Object controllerObj, java.lang.Object callbackObj, android.os.Handler handler) {
        ((android.media.session.MediaController) (controllerObj)).registerCallback(((android.media.session.MediaController.Callback) (callbackObj)), handler);
    }

    public static void unregisterCallback(java.lang.Object controllerObj, java.lang.Object callbackObj) {
        ((android.media.session.MediaController) (controllerObj)).unregisterCallback(((android.media.session.MediaController.Callback) (callbackObj)));
    }

    public static java.lang.Object getTransportControls(java.lang.Object controllerObj) {
        return ((android.media.session.MediaController) (controllerObj)).getTransportControls();
    }

    public static java.lang.Object getPlaybackState(java.lang.Object controllerObj) {
        return ((android.media.session.MediaController) (controllerObj)).getPlaybackState();
    }

    public static java.lang.Object getMetadata(java.lang.Object controllerObj) {
        return ((android.media.session.MediaController) (controllerObj)).getMetadata();
    }

    public static java.util.List<java.lang.Object> getQueue(java.lang.Object controllerObj) {
        java.util.List<android.media.session.MediaSession.QueueItem> queue = ((android.media.session.MediaController) (controllerObj)).getQueue();
        if (queue == null) {
            return null;
        }
        java.util.List<java.lang.Object> queueObjs = new java.util.ArrayList<java.lang.Object>(queue);
        return queueObjs;
    }

    public static java.lang.CharSequence getQueueTitle(java.lang.Object controllerObj) {
        return ((android.media.session.MediaController) (controllerObj)).getQueueTitle();
    }

    public static android.os.Bundle getExtras(java.lang.Object controllerObj) {
        return ((android.media.session.MediaController) (controllerObj)).getExtras();
    }

    public static int getRatingType(java.lang.Object controllerObj) {
        return ((android.media.session.MediaController) (controllerObj)).getRatingType();
    }

    public static long getFlags(java.lang.Object controllerObj) {
        return ((android.media.session.MediaController) (controllerObj)).getFlags();
    }

    public static java.lang.Object getPlaybackInfo(java.lang.Object controllerObj) {
        return ((android.media.session.MediaController) (controllerObj)).getPlaybackInfo();
    }

    public static android.app.PendingIntent getSessionActivity(java.lang.Object controllerObj) {
        return ((android.media.session.MediaController) (controllerObj)).getSessionActivity();
    }

    public static boolean dispatchMediaButtonEvent(java.lang.Object controllerObj, android.view.KeyEvent event) {
        return ((android.media.session.MediaController) (controllerObj)).dispatchMediaButtonEvent(event);
    }

    public static void setVolumeTo(java.lang.Object controllerObj, int value, int flags) {
        ((android.media.session.MediaController) (controllerObj)).setVolumeTo(value, flags);
    }

    public static void adjustVolume(java.lang.Object controllerObj, int direction, int flags) {
        ((android.media.session.MediaController) (controllerObj)).adjustVolume(direction, flags);
    }

    public static void sendCommand(java.lang.Object controllerObj, java.lang.String command, android.os.Bundle params, android.os.ResultReceiver cb) {
        ((android.media.session.MediaController) (controllerObj)).sendCommand(command, params, cb);
    }

    public static java.lang.String getPackageName(java.lang.Object controllerObj) {
        return ((android.media.session.MediaController) (controllerObj)).getPackageName();
    }

    public static class TransportControls {
        public static void play(java.lang.Object controlsObj) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).play();
        }

        public static void pause(java.lang.Object controlsObj) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).pause();
        }

        public static void stop(java.lang.Object controlsObj) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).stop();
        }

        public static void seekTo(java.lang.Object controlsObj, long pos) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).seekTo(pos);
        }

        public static void fastForward(java.lang.Object controlsObj) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).fastForward();
        }

        public static void rewind(java.lang.Object controlsObj) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).rewind();
        }

        public static void skipToNext(java.lang.Object controlsObj) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).skipToNext();
        }

        public static void skipToPrevious(java.lang.Object controlsObj) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).skipToPrevious();
        }

        public static void setRating(java.lang.Object controlsObj, java.lang.Object ratingObj) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).setRating(((android.media.Rating) (ratingObj)));
        }

        public static void playFromMediaId(java.lang.Object controlsObj, java.lang.String mediaId, android.os.Bundle extras) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).playFromMediaId(mediaId, extras);
        }

        public static void playFromSearch(java.lang.Object controlsObj, java.lang.String query, android.os.Bundle extras) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).playFromSearch(query, extras);
        }

        public static void skipToQueueItem(java.lang.Object controlsObj, long id) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).skipToQueueItem(id);
        }

        public static void sendCustomAction(java.lang.Object controlsObj, java.lang.String action, android.os.Bundle args) {
            ((android.media.session.MediaController.TransportControls) (controlsObj)).sendCustomAction(action, args);
        }
    }

    public static class PlaybackInfo {
        public static int getPlaybackType(java.lang.Object volumeInfoObj) {
            return ((android.media.session.MediaController.PlaybackInfo) (volumeInfoObj)).getPlaybackType();
        }

        public static android.media.AudioAttributes getAudioAttributes(java.lang.Object volumeInfoObj) {
            return ((android.media.session.MediaController.PlaybackInfo) (volumeInfoObj)).getAudioAttributes();
        }

        public static int getLegacyAudioStream(java.lang.Object volumeInfoObj) {
            android.media.AudioAttributes attrs = android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.getAudioAttributes(volumeInfoObj);
            return android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.toLegacyStreamType(attrs);
        }

        public static int getVolumeControl(java.lang.Object volumeInfoObj) {
            return ((android.media.session.MediaController.PlaybackInfo) (volumeInfoObj)).getVolumeControl();
        }

        public static int getMaxVolume(java.lang.Object volumeInfoObj) {
            return ((android.media.session.MediaController.PlaybackInfo) (volumeInfoObj)).getMaxVolume();
        }

        public static int getCurrentVolume(java.lang.Object volumeInfoObj) {
            return ((android.media.session.MediaController.PlaybackInfo) (volumeInfoObj)).getCurrentVolume();
        }

        // This is copied from AudioAttributes.toLegacyStreamType. TODO This
        // either needs to be kept in sync with that one or toLegacyStreamType
        // needs to be made public so it can be used by the support lib.
        private static final int FLAG_SCO = 0x1 << 2;

        private static final int STREAM_BLUETOOTH_SCO = 6;

        private static final int STREAM_SYSTEM_ENFORCED = 7;

        private static int toLegacyStreamType(android.media.AudioAttributes aa) {
            // flags to stream type mapping
            if ((aa.getFlags() & android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED) == android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED) {
                return android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.STREAM_SYSTEM_ENFORCED;
            }
            if ((aa.getFlags() & android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.FLAG_SCO) == android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.FLAG_SCO) {
                return android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.STREAM_BLUETOOTH_SCO;
            }
            // usage to stream type mapping
            switch (aa.getUsage()) {
                case android.media.AudioAttributes.USAGE_MEDIA :
                case android.media.AudioAttributes.USAGE_GAME :
                case android.media.AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY :
                case android.media.AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE :
                    return android.media.AudioManager.STREAM_MUSIC;
                case android.media.AudioAttributes.USAGE_ASSISTANCE_SONIFICATION :
                    return android.media.AudioManager.STREAM_SYSTEM;
                case android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION :
                    return android.media.AudioManager.STREAM_VOICE_CALL;
                case android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING :
                    return android.media.AudioManager.STREAM_DTMF;
                case android.media.AudioAttributes.USAGE_ALARM :
                    return android.media.AudioManager.STREAM_ALARM;
                case android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE :
                    return android.media.AudioManager.STREAM_RING;
                case android.media.AudioAttributes.USAGE_NOTIFICATION :
                case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST :
                case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT :
                case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED :
                case android.media.AudioAttributes.USAGE_NOTIFICATION_EVENT :
                    return android.media.AudioManager.STREAM_NOTIFICATION;
                case android.media.AudioAttributes.USAGE_UNKNOWN :
                default :
                    return android.media.AudioManager.STREAM_MUSIC;
            }
        }
    }

    public static interface Callback {
        public void onSessionDestroyed();

        public void onSessionEvent(java.lang.String event, android.os.Bundle extras);

        public void onPlaybackStateChanged(java.lang.Object stateObj);

        public void onMetadataChanged(java.lang.Object metadataObj);

        public void onQueueChanged(java.util.List<?> queue);

        public void onQueueTitleChanged(java.lang.CharSequence title);

        public void onExtrasChanged(android.os.Bundle extras);

        public void onAudioInfoChanged(int type, int stream, int control, int max, int current);
    }

    static class CallbackProxy<T extends android.support.v4.media.session.MediaControllerCompatApi21.Callback> extends android.media.session.MediaController.Callback {
        protected final T mCallback;

        public CallbackProxy(T callback) {
            mCallback = callback;
        }

        @java.lang.Override
        public void onSessionDestroyed() {
            mCallback.onSessionDestroyed();
        }

        @java.lang.Override
        public void onSessionEvent(java.lang.String event, android.os.Bundle extras) {
            mCallback.onSessionEvent(event, extras);
        }

        @java.lang.Override
        public void onPlaybackStateChanged(android.media.session.PlaybackState state) {
            mCallback.onPlaybackStateChanged(state);
        }

        @java.lang.Override
        public void onMetadataChanged(android.media.MediaMetadata metadata) {
            mCallback.onMetadataChanged(metadata);
        }

        @java.lang.Override
        public void onQueueChanged(java.util.List<android.media.session.MediaSession.QueueItem> queue) {
            mCallback.onQueueChanged(queue);
        }

        @java.lang.Override
        public void onQueueTitleChanged(java.lang.CharSequence title) {
            mCallback.onQueueTitleChanged(title);
        }

        @java.lang.Override
        public void onExtrasChanged(android.os.Bundle extras) {
            mCallback.onExtrasChanged(extras);
        }

        @java.lang.Override
        public void onAudioInfoChanged(android.media.session.MediaController.PlaybackInfo info) {
            mCallback.onAudioInfoChanged(info.getPlaybackType(), android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.getLegacyAudioStream(info), info.getVolumeControl(), info.getMaxVolume(), info.getCurrentVolume());
        }
    }
}

