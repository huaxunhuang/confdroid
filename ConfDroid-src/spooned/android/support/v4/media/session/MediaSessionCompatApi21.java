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


class MediaSessionCompatApi21 {
    public static java.lang.Object createSession(android.content.Context context, java.lang.String tag) {
        return new android.media.session.MediaSession(context, tag);
    }

    public static java.lang.Object verifySession(java.lang.Object mediaSession) {
        if (mediaSession instanceof android.media.session.MediaSession) {
            return mediaSession;
        }
        throw new java.lang.IllegalArgumentException("mediaSession is not a valid MediaSession object");
    }

    public static java.lang.Object verifyToken(java.lang.Object token) {
        if (token instanceof android.media.session.MediaSession.Token) {
            return token;
        }
        throw new java.lang.IllegalArgumentException("token is not a valid MediaSession.Token object");
    }

    public static java.lang.Object createCallback(android.support.v4.media.session.MediaSessionCompatApi21.Callback callback) {
        return new android.support.v4.media.session.MediaSessionCompatApi21.CallbackProxy<android.support.v4.media.session.MediaSessionCompatApi21.Callback>(callback);
    }

    public static void setCallback(java.lang.Object sessionObj, java.lang.Object callbackObj, android.os.Handler handler) {
        ((android.media.session.MediaSession) (sessionObj)).setCallback(((android.media.session.MediaSession.Callback) (callbackObj)), handler);
    }

    public static void setFlags(java.lang.Object sessionObj, int flags) {
        ((android.media.session.MediaSession) (sessionObj)).setFlags(flags);
    }

    public static void setPlaybackToLocal(java.lang.Object sessionObj, int stream) {
        // TODO update APIs to use support version of AudioAttributes
        android.media.AudioAttributes.Builder bob = new android.media.AudioAttributes.Builder();
        bob.setLegacyStreamType(stream);
        ((android.media.session.MediaSession) (sessionObj)).setPlaybackToLocal(bob.build());
    }

    public static void setPlaybackToRemote(java.lang.Object sessionObj, java.lang.Object volumeProviderObj) {
        ((android.media.session.MediaSession) (sessionObj)).setPlaybackToRemote(((android.media.VolumeProvider) (volumeProviderObj)));
    }

    public static void setActive(java.lang.Object sessionObj, boolean active) {
        ((android.media.session.MediaSession) (sessionObj)).setActive(active);
    }

    public static boolean isActive(java.lang.Object sessionObj) {
        return ((android.media.session.MediaSession) (sessionObj)).isActive();
    }

    public static void sendSessionEvent(java.lang.Object sessionObj, java.lang.String event, android.os.Bundle extras) {
        ((android.media.session.MediaSession) (sessionObj)).sendSessionEvent(event, extras);
    }

    public static void release(java.lang.Object sessionObj) {
        ((android.media.session.MediaSession) (sessionObj)).release();
    }

    public static android.os.Parcelable getSessionToken(java.lang.Object sessionObj) {
        return ((android.media.session.MediaSession) (sessionObj)).getSessionToken();
    }

    public static void setPlaybackState(java.lang.Object sessionObj, java.lang.Object stateObj) {
        ((android.media.session.MediaSession) (sessionObj)).setPlaybackState(((android.media.session.PlaybackState) (stateObj)));
    }

    public static void setMetadata(java.lang.Object sessionObj, java.lang.Object metadataObj) {
        ((android.media.session.MediaSession) (sessionObj)).setMetadata(((android.media.MediaMetadata) (metadataObj)));
    }

    public static void setSessionActivity(java.lang.Object sessionObj, android.app.PendingIntent pi) {
        ((android.media.session.MediaSession) (sessionObj)).setSessionActivity(pi);
    }

    public static void setMediaButtonReceiver(java.lang.Object sessionObj, android.app.PendingIntent pi) {
        ((android.media.session.MediaSession) (sessionObj)).setMediaButtonReceiver(pi);
    }

    public static void setQueue(java.lang.Object sessionObj, java.util.List<java.lang.Object> queueObjs) {
        if (queueObjs == null) {
            ((android.media.session.MediaSession) (sessionObj)).setQueue(null);
            return;
        }
        java.util.ArrayList<android.media.session.MediaSession.QueueItem> queue = new java.util.ArrayList<android.media.session.MediaSession.QueueItem>();
        for (java.lang.Object itemObj : queueObjs) {
            queue.add(((android.media.session.MediaSession.QueueItem) (itemObj)));
        }
        ((android.media.session.MediaSession) (sessionObj)).setQueue(queue);
    }

    public static void setQueueTitle(java.lang.Object sessionObj, java.lang.CharSequence title) {
        ((android.media.session.MediaSession) (sessionObj)).setQueueTitle(title);
    }

    public static void setExtras(java.lang.Object sessionObj, android.os.Bundle extras) {
        ((android.media.session.MediaSession) (sessionObj)).setExtras(extras);
    }

    interface Callback extends android.support.v4.media.session.MediaSessionCompatApi19.Callback {
        public void onCommand(java.lang.String command, android.os.Bundle extras, android.os.ResultReceiver cb);

        public boolean onMediaButtonEvent(android.content.Intent mediaButtonIntent);

        public void onPlay();

        public void onPlayFromMediaId(java.lang.String mediaId, android.os.Bundle extras);

        public void onPlayFromSearch(java.lang.String search, android.os.Bundle extras);

        public void onSkipToQueueItem(long id);

        public void onPause();

        public void onSkipToNext();

        public void onSkipToPrevious();

        public void onFastForward();

        public void onRewind();

        public void onStop();

        public void onCustomAction(java.lang.String action, android.os.Bundle extras);
    }

    static class CallbackProxy<T extends android.support.v4.media.session.MediaSessionCompatApi21.Callback> extends android.media.session.MediaSession.Callback {
        protected final T mCallback;

        public CallbackProxy(T callback) {
            mCallback = callback;
        }

        @java.lang.Override
        public void onCommand(java.lang.String command, android.os.Bundle args, android.os.ResultReceiver cb) {
            mCallback.onCommand(command, args, cb);
        }

        @java.lang.Override
        public boolean onMediaButtonEvent(android.content.Intent mediaButtonIntent) {
            return mCallback.onMediaButtonEvent(mediaButtonIntent) || super.onMediaButtonEvent(mediaButtonIntent);
        }

        @java.lang.Override
        public void onPlay() {
            mCallback.onPlay();
        }

        @java.lang.Override
        public void onPlayFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
            mCallback.onPlayFromMediaId(mediaId, extras);
        }

        @java.lang.Override
        public void onPlayFromSearch(java.lang.String search, android.os.Bundle extras) {
            mCallback.onPlayFromSearch(search, extras);
        }

        @java.lang.Override
        public void onSkipToQueueItem(long id) {
            mCallback.onSkipToQueueItem(id);
        }

        @java.lang.Override
        public void onPause() {
            mCallback.onPause();
        }

        @java.lang.Override
        public void onSkipToNext() {
            mCallback.onSkipToNext();
        }

        @java.lang.Override
        public void onSkipToPrevious() {
            mCallback.onSkipToPrevious();
        }

        @java.lang.Override
        public void onFastForward() {
            mCallback.onFastForward();
        }

        @java.lang.Override
        public void onRewind() {
            mCallback.onRewind();
        }

        @java.lang.Override
        public void onStop() {
            mCallback.onStop();
        }

        @java.lang.Override
        public void onSeekTo(long pos) {
            mCallback.onSeekTo(pos);
        }

        @java.lang.Override
        public void onSetRating(android.media.Rating rating) {
            mCallback.onSetRating(rating);
        }

        @java.lang.Override
        public void onCustomAction(java.lang.String action, android.os.Bundle extras) {
            mCallback.onCustomAction(action, extras);
        }
    }

    static class QueueItem {
        public static java.lang.Object createItem(java.lang.Object mediaDescription, long id) {
            return new android.media.session.MediaSession.QueueItem(((android.media.MediaDescription) (mediaDescription)), id);
        }

        public static java.lang.Object getDescription(java.lang.Object queueItem) {
            return ((android.media.session.MediaSession.QueueItem) (queueItem)).getDescription();
        }

        public static long getQueueId(java.lang.Object queueItem) {
            return ((android.media.session.MediaSession.QueueItem) (queueItem)).getQueueId();
        }
    }
}

