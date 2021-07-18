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


/**
 * Allows an app to interact with an ongoing media session. Media buttons and
 * other commands can be sent to the session. A callback may be registered to
 * receive updates from the session, such as metadata and play state changes.
 * <p>
 * A MediaController can be created if you have a {@link MediaSessionCompat.Token}
 * from the session owner.
 * <p>
 * MediaController objects are thread-safe.
 * <p>
 * This is a helper for accessing features in {@link android.media.session.MediaSession}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public final class MediaControllerCompat {
    static final java.lang.String TAG = "MediaControllerCompat";

    private final android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl mImpl;

    private final android.support.v4.media.session.MediaSessionCompat.Token mToken;

    /**
     * Creates a media controller from a session.
     *
     * @param session
     * 		The session to be controlled.
     */
    public MediaControllerCompat(android.content.Context context, android.support.v4.media.session.MediaSessionCompat session) {
        if (session == null) {
            throw new java.lang.IllegalArgumentException("session must not be null");
        }
        mToken = session.getSessionToken();
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            mImpl = new android.support.v4.media.session.MediaControllerCompat.MediaControllerImplApi24(context, session);
        } else
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                mImpl = new android.support.v4.media.session.MediaControllerCompat.MediaControllerImplApi23(context, session);
            } else
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    mImpl = new android.support.v4.media.session.MediaControllerCompat.MediaControllerImplApi21(context, session);
                } else {
                    mImpl = new android.support.v4.media.session.MediaControllerCompat.MediaControllerImplBase(mToken);
                }


    }

    /**
     * Creates a media controller from a session token which may have
     * been obtained from another process.
     *
     * @param sessionToken
     * 		The token of the session to be controlled.
     * @throws RemoteException
     * 		if the session is not accessible.
     */
    public MediaControllerCompat(android.content.Context context, android.support.v4.media.session.MediaSessionCompat.Token sessionToken) throws android.os.RemoteException {
        if (sessionToken == null) {
            throw new java.lang.IllegalArgumentException("sessionToken must not be null");
        }
        mToken = sessionToken;
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            mImpl = new android.support.v4.media.session.MediaControllerCompat.MediaControllerImplApi24(context, sessionToken);
        } else
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                mImpl = new android.support.v4.media.session.MediaControllerCompat.MediaControllerImplApi23(context, sessionToken);
            } else
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    mImpl = new android.support.v4.media.session.MediaControllerCompat.MediaControllerImplApi21(context, sessionToken);
                } else {
                    mImpl = new android.support.v4.media.session.MediaControllerCompat.MediaControllerImplBase(mToken);
                }


    }

    /**
     * Get a {@link TransportControls} instance for this session.
     *
     * @return A controls instance
     */
    public android.support.v4.media.session.MediaControllerCompat.TransportControls getTransportControls() {
        return mImpl.getTransportControls();
    }

    /**
     * Send the specified media button event to the session. Only media keys can
     * be sent by this method, other keys will be ignored.
     *
     * @param keyEvent
     * 		The media button event to dispatch.
     * @return true if the event was sent to the session, false otherwise.
     */
    public boolean dispatchMediaButtonEvent(android.view.KeyEvent keyEvent) {
        if (keyEvent == null) {
            throw new java.lang.IllegalArgumentException("KeyEvent may not be null");
        }
        return mImpl.dispatchMediaButtonEvent(keyEvent);
    }

    /**
     * Get the current playback state for this session.
     *
     * @return The current PlaybackState or null
     */
    public android.support.v4.media.session.PlaybackStateCompat getPlaybackState() {
        return mImpl.getPlaybackState();
    }

    /**
     * Get the current metadata for this session.
     *
     * @return The current MediaMetadata or null.
     */
    public android.support.v4.media.MediaMetadataCompat getMetadata() {
        return mImpl.getMetadata();
    }

    /**
     * Get the current play queue for this session if one is set. If you only
     * care about the current item {@link #getMetadata()} should be used.
     *
     * @return The current play queue or null.
     */
    public java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> getQueue() {
        return mImpl.getQueue();
    }

    /**
     * Get the queue title for this session.
     */
    public java.lang.CharSequence getQueueTitle() {
        return mImpl.getQueueTitle();
    }

    /**
     * Get the extras for this session.
     */
    public android.os.Bundle getExtras() {
        return mImpl.getExtras();
    }

    /**
     * Get the rating type supported by the session. One of:
     * <ul>
     * <li>{@link RatingCompat#RATING_NONE}</li>
     * <li>{@link RatingCompat#RATING_HEART}</li>
     * <li>{@link RatingCompat#RATING_THUMB_UP_DOWN}</li>
     * <li>{@link RatingCompat#RATING_3_STARS}</li>
     * <li>{@link RatingCompat#RATING_4_STARS}</li>
     * <li>{@link RatingCompat#RATING_5_STARS}</li>
     * <li>{@link RatingCompat#RATING_PERCENTAGE}</li>
     * </ul>
     *
     * @return The supported rating type
     */
    public int getRatingType() {
        return mImpl.getRatingType();
    }

    /**
     * Get the flags for this session. Flags are defined in
     * {@link MediaSessionCompat}.
     *
     * @return The current set of flags for the session.
     */
    public long getFlags() {
        return mImpl.getFlags();
    }

    /**
     * Get the current playback info for this session.
     *
     * @return The current playback info or null.
     */
    public android.support.v4.media.session.MediaControllerCompat.PlaybackInfo getPlaybackInfo() {
        return mImpl.getPlaybackInfo();
    }

    /**
     * Get an intent for launching UI associated with this session if one
     * exists.
     *
     * @return A {@link PendingIntent} to launch UI or null.
     */
    public android.app.PendingIntent getSessionActivity() {
        return mImpl.getSessionActivity();
    }

    /**
     * Get the token for the session this controller is connected to.
     *
     * @return The session's token.
     */
    public android.support.v4.media.session.MediaSessionCompat.Token getSessionToken() {
        return mToken;
    }

    /**
     * Set the volume of the output this session is playing on. The command will
     * be ignored if it does not support
     * {@link VolumeProviderCompat#VOLUME_CONTROL_ABSOLUTE}. The flags in
     * {@link AudioManager} may be used to affect the handling.
     *
     * @see #getPlaybackInfo()
     * @param value
     * 		The value to set it to, between 0 and the reported max.
     * @param flags
     * 		Flags from {@link AudioManager} to include with the volume
     * 		request.
     */
    public void setVolumeTo(int value, int flags) {
        mImpl.setVolumeTo(value, flags);
    }

    /**
     * Adjust the volume of the output this session is playing on. The direction
     * must be one of {@link AudioManager#ADJUST_LOWER},
     * {@link AudioManager#ADJUST_RAISE}, or {@link AudioManager#ADJUST_SAME}.
     * The command will be ignored if the session does not support
     * {@link VolumeProviderCompat#VOLUME_CONTROL_RELATIVE} or
     * {@link VolumeProviderCompat#VOLUME_CONTROL_ABSOLUTE}. The flags in
     * {@link AudioManager} may be used to affect the handling.
     *
     * @see #getPlaybackInfo()
     * @param direction
     * 		The direction to adjust the volume in.
     * @param flags
     * 		Any flags to pass with the command.
     */
    public void adjustVolume(int direction, int flags) {
        mImpl.adjustVolume(direction, flags);
    }

    /**
     * Adds a callback to receive updates from the Session. Updates will be
     * posted on the caller's thread.
     *
     * @param callback
     * 		The callback object, must not be null.
     */
    public void registerCallback(android.support.v4.media.session.MediaControllerCompat.Callback callback) {
        registerCallback(callback, null);
    }

    /**
     * Adds a callback to receive updates from the session. Updates will be
     * posted on the specified handler's thread.
     *
     * @param callback
     * 		The callback object, must not be null.
     * @param handler
     * 		The handler to post updates on. If null the callers thread
     * 		will be used.
     */
    public void registerCallback(android.support.v4.media.session.MediaControllerCompat.Callback callback, android.os.Handler handler) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback cannot be null");
        }
        if (handler == null) {
            handler = new android.os.Handler();
        }
        mImpl.registerCallback(callback, handler);
    }

    /**
     * Stop receiving updates on the specified callback. If an update has
     * already been posted you may still receive it after calling this method.
     *
     * @param callback
     * 		The callback to remove
     */
    public void unregisterCallback(android.support.v4.media.session.MediaControllerCompat.Callback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback cannot be null");
        }
        mImpl.unregisterCallback(callback);
    }

    /**
     * Sends a generic command to the session. It is up to the session creator
     * to decide what commands and parameters they will support. As such,
     * commands should only be sent to sessions that the controller owns.
     *
     * @param command
     * 		The command to send
     * @param params
     * 		Any parameters to include with the command
     * @param cb
     * 		The callback to receive the result on
     */
    public void sendCommand(java.lang.String command, android.os.Bundle params, android.os.ResultReceiver cb) {
        if (android.text.TextUtils.isEmpty(command)) {
            throw new java.lang.IllegalArgumentException("command cannot be null or empty");
        }
        mImpl.sendCommand(command, params, cb);
    }

    /**
     * Get the session owner's package name.
     *
     * @return The package name of of the session owner.
     */
    public java.lang.String getPackageName() {
        return mImpl.getPackageName();
    }

    /**
     * Gets the underlying framework
     * {@link android.media.session.MediaController} object.
     * <p>
     * This method is only supported on API 21+.
     * </p>
     *
     * @return The underlying {@link android.media.session.MediaController}
    object, or null if none.
     */
    public java.lang.Object getMediaController() {
        return mImpl.getMediaController();
    }

    /**
     * Callback for receiving updates on from the session. A Callback can be
     * registered using {@link #registerCallback}
     */
    public static abstract class Callback implements android.os.IBinder.DeathRecipient {
        private final java.lang.Object mCallbackObj;

        android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler mHandler;

        boolean mRegistered = false;

        public Callback() {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                mCallbackObj = android.support.v4.media.session.MediaControllerCompatApi21.createCallback(new android.support.v4.media.session.MediaControllerCompat.Callback.StubApi21());
            } else {
                mCallbackObj = new android.support.v4.media.session.MediaControllerCompat.Callback.StubCompat();
            }
        }

        /**
         * Override to handle the session being destroyed. The session is no
         * longer valid after this call and calls to it will be ignored.
         */
        public void onSessionDestroyed() {
        }

        /**
         * Override to handle custom events sent by the session owner without a
         * specified interface. Controllers should only handle these for
         * sessions they own.
         *
         * @param event
         * 		The event from the session.
         * @param extras
         * 		Optional parameters for the event.
         */
        public void onSessionEvent(java.lang.String event, android.os.Bundle extras) {
        }

        /**
         * Override to handle changes in playback state.
         *
         * @param state
         * 		The new playback state of the session
         */
        public void onPlaybackStateChanged(android.support.v4.media.session.PlaybackStateCompat state) {
        }

        /**
         * Override to handle changes to the current metadata.
         *
         * @param metadata
         * 		The current metadata for the session or null if none.
         * @see MediaMetadataCompat
         */
        public void onMetadataChanged(android.support.v4.media.MediaMetadataCompat metadata) {
        }

        /**
         * Override to handle changes to items in the queue.
         *
         * @see MediaSessionCompat.QueueItem
         * @param queue
         * 		A list of items in the current play queue. It should
         * 		include the currently playing item as well as previous and
         * 		upcoming items if applicable.
         */
        public void onQueueChanged(java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> queue) {
        }

        /**
         * Override to handle changes to the queue title.
         *
         * @param title
         * 		The title that should be displayed along with the play
         * 		queue such as "Now Playing". May be null if there is no
         * 		such title.
         */
        public void onQueueTitleChanged(java.lang.CharSequence title) {
        }

        /**
         * Override to handle chagnes to the {@link MediaSessionCompat} extras.
         *
         * @param extras
         * 		The extras that can include other information
         * 		associated with the {@link MediaSessionCompat}.
         */
        public void onExtrasChanged(android.os.Bundle extras) {
        }

        /**
         * Override to handle changes to the audio info.
         *
         * @param info
         * 		The current audio info for this session.
         */
        public void onAudioInfoChanged(android.support.v4.media.session.MediaControllerCompat.PlaybackInfo info) {
        }

        @java.lang.Override
        public void binderDied() {
            onSessionDestroyed();
        }

        /**
         * Set the handler to use for pre 21 callbacks.
         */
        private void setHandler(android.os.Handler handler) {
            mHandler = new android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler(handler.getLooper());
        }

        private class StubApi21 implements android.support.v4.media.session.MediaControllerCompatApi21.Callback {
            StubApi21() {
            }

            @java.lang.Override
            public void onSessionDestroyed() {
                android.support.v4.media.session.MediaControllerCompat.Callback.this.onSessionDestroyed();
            }

            @java.lang.Override
            public void onSessionEvent(java.lang.String event, android.os.Bundle extras) {
                android.support.v4.media.session.MediaControllerCompat.Callback.this.onSessionEvent(event, extras);
            }

            @java.lang.Override
            public void onPlaybackStateChanged(java.lang.Object stateObj) {
                android.support.v4.media.session.MediaControllerCompat.Callback.this.onPlaybackStateChanged(android.support.v4.media.session.PlaybackStateCompat.fromPlaybackState(stateObj));
            }

            @java.lang.Override
            public void onMetadataChanged(java.lang.Object metadataObj) {
                android.support.v4.media.session.MediaControllerCompat.Callback.this.onMetadataChanged(android.support.v4.media.MediaMetadataCompat.fromMediaMetadata(metadataObj));
            }

            @java.lang.Override
            public void onQueueChanged(java.util.List<?> queue) {
                android.support.v4.media.session.MediaControllerCompat.Callback.this.onQueueChanged(android.support.v4.media.session.MediaSessionCompat.QueueItem.fromQueueItemList(queue));
            }

            @java.lang.Override
            public void onQueueTitleChanged(java.lang.CharSequence title) {
                android.support.v4.media.session.MediaControllerCompat.Callback.this.onQueueTitleChanged(title);
            }

            @java.lang.Override
            public void onExtrasChanged(android.os.Bundle extras) {
                android.support.v4.media.session.MediaControllerCompat.Callback.this.onExtrasChanged(extras);
            }

            @java.lang.Override
            public void onAudioInfoChanged(int type, int stream, int control, int max, int current) {
                android.support.v4.media.session.MediaControllerCompat.Callback.this.onAudioInfoChanged(new android.support.v4.media.session.MediaControllerCompat.PlaybackInfo(type, stream, control, max, current));
            }
        }

        private class StubCompat extends android.support.v4.media.session.IMediaControllerCallback.Stub {
            StubCompat() {
            }

            @java.lang.Override
            public void onEvent(java.lang.String event, android.os.Bundle extras) throws android.os.RemoteException {
                mHandler.post(android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_EVENT, event, extras);
            }

            @java.lang.Override
            public void onSessionDestroyed() throws android.os.RemoteException {
                mHandler.post(android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_DESTROYED, null, null);
            }

            @java.lang.Override
            public void onPlaybackStateChanged(android.support.v4.media.session.PlaybackStateCompat state) throws android.os.RemoteException {
                mHandler.post(android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_PLAYBACK_STATE, state, null);
            }

            @java.lang.Override
            public void onMetadataChanged(android.support.v4.media.MediaMetadataCompat metadata) throws android.os.RemoteException {
                mHandler.post(android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_METADATA, metadata, null);
            }

            @java.lang.Override
            public void onQueueChanged(java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> queue) throws android.os.RemoteException {
                mHandler.post(android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_QUEUE, queue, null);
            }

            @java.lang.Override
            public void onQueueTitleChanged(java.lang.CharSequence title) throws android.os.RemoteException {
                mHandler.post(android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_QUEUE_TITLE, title, null);
            }

            @java.lang.Override
            public void onExtrasChanged(android.os.Bundle extras) throws android.os.RemoteException {
                mHandler.post(android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_EXTRAS, extras, null);
            }

            @java.lang.Override
            public void onVolumeInfoChanged(android.support.v4.media.session.ParcelableVolumeInfo info) throws android.os.RemoteException {
                android.support.v4.media.session.MediaControllerCompat.PlaybackInfo pi = null;
                if (info != null) {
                    pi = new android.support.v4.media.session.MediaControllerCompat.PlaybackInfo(info.volumeType, info.audioStream, info.controlType, info.maxVolume, info.currentVolume);
                }
                mHandler.post(android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_VOLUME, pi, null);
            }
        }

        private class MessageHandler extends android.os.Handler {
            private static final int MSG_EVENT = 1;

            private static final int MSG_UPDATE_PLAYBACK_STATE = 2;

            private static final int MSG_UPDATE_METADATA = 3;

            private static final int MSG_UPDATE_VOLUME = 4;

            private static final int MSG_UPDATE_QUEUE = 5;

            private static final int MSG_UPDATE_QUEUE_TITLE = 6;

            private static final int MSG_UPDATE_EXTRAS = 7;

            private static final int MSG_DESTROYED = 8;

            public MessageHandler(android.os.Looper looper) {
                super(looper);
            }

            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                if (!mRegistered) {
                    return;
                }
                switch (msg.what) {
                    case android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_EVENT :
                        onSessionEvent(((java.lang.String) (msg.obj)), msg.getData());
                        break;
                    case android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_PLAYBACK_STATE :
                        onPlaybackStateChanged(((android.support.v4.media.session.PlaybackStateCompat) (msg.obj)));
                        break;
                    case android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_METADATA :
                        onMetadataChanged(((android.support.v4.media.MediaMetadataCompat) (msg.obj)));
                        break;
                    case android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_QUEUE :
                        onQueueChanged(((java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem>) (msg.obj)));
                        break;
                    case android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_QUEUE_TITLE :
                        onQueueTitleChanged(((java.lang.CharSequence) (msg.obj)));
                        break;
                    case android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_EXTRAS :
                        onExtrasChanged(((android.os.Bundle) (msg.obj)));
                        break;
                    case android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_UPDATE_VOLUME :
                        onAudioInfoChanged(((android.support.v4.media.session.MediaControllerCompat.PlaybackInfo) (msg.obj)));
                        break;
                    case android.support.v4.media.session.MediaControllerCompat.Callback.MessageHandler.MSG_DESTROYED :
                        onSessionDestroyed();
                        break;
                }
            }

            public void post(int what, java.lang.Object obj, android.os.Bundle data) {
                android.os.Message msg = obtainMessage(what, obj);
                msg.setData(data);
                msg.sendToTarget();
            }
        }
    }

    /**
     * Interface for controlling media playback on a session. This allows an app
     * to send media transport commands to the session.
     */
    public static abstract class TransportControls {
        TransportControls() {
        }

        /**
         * Request that the player prepare its playback without audio focus. In other words, other
         * session can continue to play during the preparation of this session. This method can be
         * used to speed up the start of the playback. Once the preparation is done, the session
         * will change its playback state to {@link PlaybackStateCompat#STATE_PAUSED}. Afterwards,
         * {@link #play} can be called to start playback. If the preparation is not needed,
         * {@link #play} can be directly called without this method.
         */
        public abstract void prepare();

        /**
         * Request that the player prepare playback for a specific media id. In other words, other
         * session can continue to play during the preparation of this session. This method can be
         * used to speed up the start of the playback. Once the preparation is
         * done, the session will change its playback state to
         * {@link PlaybackStateCompat#STATE_PAUSED}. Afterwards, {@link #play} can be called to
         * start playback. If the preparation is not needed, {@link #playFromMediaId} can
         * be directly called without this method.
         *
         * @param mediaId
         * 		The id of the requested media.
         * @param extras
         * 		Optional extras that can include extra information about the media item
         * 		to be prepared.
         */
        public abstract void prepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras);

        /**
         * Request that the player prepare playback for a specific search query.
         * An empty or null query should be treated as a request to prepare any
         * music. In other words, other session can continue to play during
         * the preparation of this session. This method can be used to speed up the start of the
         * playback. Once the preparation is done, the session will change its playback state to
         * {@link PlaybackStateCompat#STATE_PAUSED}. Afterwards, {@link #play} can be called to
         * start playback. If the preparation is not needed, {@link #playFromSearch} can be directly
         * called without this method.
         *
         * @param query
         * 		The search query.
         * @param extras
         * 		Optional extras that can include extra information
         * 		about the query.
         */
        public abstract void prepareFromSearch(java.lang.String query, android.os.Bundle extras);

        /**
         * Request that the player prepare playback for a specific {@link Uri}.
         * In other words, other session can continue to play during the preparation of this
         * session. This method can be used to speed up the start of the playback.
         * Once the preparation is done, the session will change its playback state to
         * {@link PlaybackStateCompat#STATE_PAUSED}. Afterwards, {@link #play} can be called to
         * start playback. If the preparation is not needed, {@link #playFromUri} can be directly
         * called without this method.
         *
         * @param uri
         * 		The URI of the requested media.
         * @param extras
         * 		Optional extras that can include extra information about the media item
         * 		to be prepared.
         */
        public abstract void prepareFromUri(android.net.Uri uri, android.os.Bundle extras);

        /**
         * Request that the player start its playback at its current position.
         */
        public abstract void play();

        /**
         * Request that the player start playback for a specific {@link Uri}.
         *
         * @param mediaId
         * 		The uri of the requested media.
         * @param extras
         * 		Optional extras that can include extra information
         * 		about the media item to be played.
         */
        public abstract void playFromMediaId(java.lang.String mediaId, android.os.Bundle extras);

        /**
         * Request that the player start playback for a specific search query.
         * An empty or null query should be treated as a request to play any
         * music.
         *
         * @param query
         * 		The search query.
         * @param extras
         * 		Optional extras that can include extra information
         * 		about the query.
         */
        public abstract void playFromSearch(java.lang.String query, android.os.Bundle extras);

        /**
         * Request that the player start playback for a specific {@link Uri}.
         *
         * @param uri
         * 		The URI of the requested media.
         * @param extras
         * 		Optional extras that can include extra information about the media item
         * 		to be played.
         */
        public abstract void playFromUri(android.net.Uri uri, android.os.Bundle extras);

        /**
         * Play an item with a specific id in the play queue. If you specify an
         * id that is not in the play queue, the behavior is undefined.
         */
        public abstract void skipToQueueItem(long id);

        /**
         * Request that the player pause its playback and stay at its current
         * position.
         */
        public abstract void pause();

        /**
         * Request that the player stop its playback; it may clear its state in
         * whatever way is appropriate.
         */
        public abstract void stop();

        /**
         * Move to a new location in the media stream.
         *
         * @param pos
         * 		Position to move to, in milliseconds.
         */
        public abstract void seekTo(long pos);

        /**
         * Start fast forwarding. If playback is already fast forwarding this
         * may increase the rate.
         */
        public abstract void fastForward();

        /**
         * Skip to the next item.
         */
        public abstract void skipToNext();

        /**
         * Start rewinding. If playback is already rewinding this may increase
         * the rate.
         */
        public abstract void rewind();

        /**
         * Skip to the previous item.
         */
        public abstract void skipToPrevious();

        /**
         * Rate the current content. This will cause the rating to be set for
         * the current user. The Rating type must match the type returned by
         * {@link #getRatingType()}.
         *
         * @param rating
         * 		The rating to set for the current content
         */
        public abstract void setRating(android.support.v4.media.RatingCompat rating);

        /**
         * Send a custom action for the {@link MediaSessionCompat} to perform.
         *
         * @param customAction
         * 		The action to perform.
         * @param args
         * 		Optional arguments to supply to the
         * 		{@link MediaSessionCompat} for this custom action.
         */
        public abstract void sendCustomAction(android.support.v4.media.session.PlaybackStateCompat.CustomAction customAction, android.os.Bundle args);

        /**
         * Send the id and args from a custom action for the
         * {@link MediaSessionCompat} to perform.
         *
         * @see #sendCustomAction(PlaybackStateCompat.CustomAction action,
        Bundle args)
         * @param action
         * 		The action identifier of the
         * 		{@link PlaybackStateCompat.CustomAction} as specified by
         * 		the {@link MediaSessionCompat}.
         * @param args
         * 		Optional arguments to supply to the
         * 		{@link MediaSessionCompat} for this custom action.
         */
        public abstract void sendCustomAction(java.lang.String action, android.os.Bundle args);
    }

    /**
     * Holds information about the way volume is handled for this session.
     */
    public static final class PlaybackInfo {
        /**
         * The session uses local playback.
         */
        public static final int PLAYBACK_TYPE_LOCAL = 1;

        /**
         * The session uses remote playback.
         */
        public static final int PLAYBACK_TYPE_REMOTE = 2;

        private final int mPlaybackType;

        // TODO update audio stream with AudioAttributes support version
        private final int mAudioStream;

        private final int mVolumeControl;

        private final int mMaxVolume;

        private final int mCurrentVolume;

        PlaybackInfo(int type, int stream, int control, int max, int current) {
            mPlaybackType = type;
            mAudioStream = stream;
            mVolumeControl = control;
            mMaxVolume = max;
            mCurrentVolume = current;
        }

        /**
         * Get the type of volume handling, either local or remote. One of:
         * <ul>
         * <li>{@link PlaybackInfo#PLAYBACK_TYPE_LOCAL}</li>
         * <li>{@link PlaybackInfo#PLAYBACK_TYPE_REMOTE}</li>
         * </ul>
         *
         * @return The type of volume handling this session is using.
         */
        public int getPlaybackType() {
            return mPlaybackType;
        }

        /**
         * Get the stream this is currently controlling volume on. When the volume
         * type is {@link PlaybackInfo#PLAYBACK_TYPE_REMOTE} this value does not
         * have meaning and should be ignored.
         *
         * @return The stream this session is playing on.
         */
        public int getAudioStream() {
            // TODO switch to AudioAttributesCompat when it is added.
            return mAudioStream;
        }

        /**
         * Get the type of volume control that can be used. One of:
         * <ul>
         * <li>{@link VolumeProviderCompat#VOLUME_CONTROL_ABSOLUTE}</li>
         * <li>{@link VolumeProviderCompat#VOLUME_CONTROL_RELATIVE}</li>
         * <li>{@link VolumeProviderCompat#VOLUME_CONTROL_FIXED}</li>
         * </ul>
         *
         * @return The type of volume control that may be used with this
        session.
         */
        public int getVolumeControl() {
            return mVolumeControl;
        }

        /**
         * Get the maximum volume that may be set for this session.
         *
         * @return The maximum allowed volume where this session is playing.
         */
        public int getMaxVolume() {
            return mMaxVolume;
        }

        /**
         * Get the current volume for this session.
         *
         * @return The current volume where this session is playing.
         */
        public int getCurrentVolume() {
            return mCurrentVolume;
        }
    }

    interface MediaControllerImpl {
        void registerCallback(android.support.v4.media.session.MediaControllerCompat.Callback callback, android.os.Handler handler);

        void unregisterCallback(android.support.v4.media.session.MediaControllerCompat.Callback callback);

        boolean dispatchMediaButtonEvent(android.view.KeyEvent keyEvent);

        android.support.v4.media.session.MediaControllerCompat.TransportControls getTransportControls();

        android.support.v4.media.session.PlaybackStateCompat getPlaybackState();

        android.support.v4.media.MediaMetadataCompat getMetadata();

        java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> getQueue();

        java.lang.CharSequence getQueueTitle();

        android.os.Bundle getExtras();

        int getRatingType();

        long getFlags();

        android.support.v4.media.session.MediaControllerCompat.PlaybackInfo getPlaybackInfo();

        android.app.PendingIntent getSessionActivity();

        void setVolumeTo(int value, int flags);

        void adjustVolume(int direction, int flags);

        void sendCommand(java.lang.String command, android.os.Bundle params, android.os.ResultReceiver cb);

        java.lang.String getPackageName();

        java.lang.Object getMediaController();
    }

    static class MediaControllerImplBase implements android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl {
        private android.support.v4.media.session.MediaSessionCompat.Token mToken;

        private android.support.v4.media.session.IMediaSession mBinder;

        private android.support.v4.media.session.MediaControllerCompat.TransportControls mTransportControls;

        public MediaControllerImplBase(android.support.v4.media.session.MediaSessionCompat.Token token) {
            mToken = token;
            mBinder = IMediaSession.Stub.asInterface(((android.os.IBinder) (token.getToken())));
        }

        @java.lang.Override
        public void registerCallback(android.support.v4.media.session.MediaControllerCompat.Callback callback, android.os.Handler handler) {
            if (callback == null) {
                throw new java.lang.IllegalArgumentException("callback may not be null.");
            }
            try {
                mBinder.asBinder().linkToDeath(callback, 0);
                mBinder.registerCallbackListener(((android.support.v4.media.session.IMediaControllerCallback) (callback.mCallbackObj)));
                callback.setHandler(handler);
                callback.mRegistered = true;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in registerCallback. " + e);
                callback.onSessionDestroyed();
            }
        }

        @java.lang.Override
        public void unregisterCallback(android.support.v4.media.session.MediaControllerCompat.Callback callback) {
            if (callback == null) {
                throw new java.lang.IllegalArgumentException("callback may not be null.");
            }
            try {
                mBinder.unregisterCallbackListener(((android.support.v4.media.session.IMediaControllerCallback) (callback.mCallbackObj)));
                mBinder.asBinder().unlinkToDeath(callback, 0);
                callback.mRegistered = false;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in unregisterCallback. " + e);
            }
        }

        @java.lang.Override
        public boolean dispatchMediaButtonEvent(android.view.KeyEvent event) {
            if (event == null) {
                throw new java.lang.IllegalArgumentException("event may not be null.");
            }
            try {
                mBinder.sendMediaButton(event);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in dispatchMediaButtonEvent. " + e);
            }
            return false;
        }

        @java.lang.Override
        public android.support.v4.media.session.MediaControllerCompat.TransportControls getTransportControls() {
            if (mTransportControls == null) {
                mTransportControls = new android.support.v4.media.session.MediaControllerCompat.TransportControlsBase(mBinder);
            }
            return mTransportControls;
        }

        @java.lang.Override
        public android.support.v4.media.session.PlaybackStateCompat getPlaybackState() {
            try {
                return mBinder.getPlaybackState();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in getPlaybackState. " + e);
            }
            return null;
        }

        @java.lang.Override
        public android.support.v4.media.MediaMetadataCompat getMetadata() {
            try {
                return mBinder.getMetadata();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in getMetadata. " + e);
            }
            return null;
        }

        @java.lang.Override
        public java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> getQueue() {
            try {
                return mBinder.getQueue();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in getQueue. " + e);
            }
            return null;
        }

        @java.lang.Override
        public java.lang.CharSequence getQueueTitle() {
            try {
                return mBinder.getQueueTitle();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in getQueueTitle. " + e);
            }
            return null;
        }

        @java.lang.Override
        public android.os.Bundle getExtras() {
            try {
                return mBinder.getExtras();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in getExtras. " + e);
            }
            return null;
        }

        @java.lang.Override
        public int getRatingType() {
            try {
                return mBinder.getRatingType();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in getRatingType. " + e);
            }
            return 0;
        }

        @java.lang.Override
        public long getFlags() {
            try {
                return mBinder.getFlags();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in getFlags. " + e);
            }
            return 0;
        }

        @java.lang.Override
        public android.support.v4.media.session.MediaControllerCompat.PlaybackInfo getPlaybackInfo() {
            try {
                android.support.v4.media.session.ParcelableVolumeInfo info = mBinder.getVolumeAttributes();
                android.support.v4.media.session.MediaControllerCompat.PlaybackInfo pi = new android.support.v4.media.session.MediaControllerCompat.PlaybackInfo(info.volumeType, info.audioStream, info.controlType, info.maxVolume, info.currentVolume);
                return pi;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in getPlaybackInfo. " + e);
            }
            return null;
        }

        @java.lang.Override
        public android.app.PendingIntent getSessionActivity() {
            try {
                return mBinder.getLaunchPendingIntent();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in getSessionActivity. " + e);
            }
            return null;
        }

        @java.lang.Override
        public void setVolumeTo(int value, int flags) {
            try {
                mBinder.setVolumeTo(value, flags, null);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in setVolumeTo. " + e);
            }
        }

        @java.lang.Override
        public void adjustVolume(int direction, int flags) {
            try {
                mBinder.adjustVolume(direction, flags, null);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in adjustVolume. " + e);
            }
        }

        @java.lang.Override
        public void sendCommand(java.lang.String command, android.os.Bundle params, android.os.ResultReceiver cb) {
            try {
                mBinder.sendCommand(command, params, new android.support.v4.media.session.MediaSessionCompat.ResultReceiverWrapper(cb));
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in sendCommand. " + e);
            }
        }

        @java.lang.Override
        public java.lang.String getPackageName() {
            try {
                return mBinder.getPackageName();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in getPackageName. " + e);
            }
            return null;
        }

        @java.lang.Override
        public java.lang.Object getMediaController() {
            return null;
        }
    }

    static class TransportControlsBase extends android.support.v4.media.session.MediaControllerCompat.TransportControls {
        private android.support.v4.media.session.IMediaSession mBinder;

        public TransportControlsBase(android.support.v4.media.session.IMediaSession binder) {
            mBinder = binder;
        }

        @java.lang.Override
        public void prepare() {
            try {
                mBinder.prepare();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in prepare. " + e);
            }
        }

        @java.lang.Override
        public void prepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
            try {
                mBinder.prepareFromMediaId(mediaId, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in prepareFromMediaId. " + e);
            }
        }

        @java.lang.Override
        public void prepareFromSearch(java.lang.String query, android.os.Bundle extras) {
            try {
                mBinder.prepareFromSearch(query, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in prepareFromSearch. " + e);
            }
        }

        @java.lang.Override
        public void prepareFromUri(android.net.Uri uri, android.os.Bundle extras) {
            try {
                mBinder.prepareFromUri(uri, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in prepareFromUri. " + e);
            }
        }

        @java.lang.Override
        public void play() {
            try {
                mBinder.play();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in play. " + e);
            }
        }

        @java.lang.Override
        public void playFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
            try {
                mBinder.playFromMediaId(mediaId, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in playFromMediaId. " + e);
            }
        }

        @java.lang.Override
        public void playFromSearch(java.lang.String query, android.os.Bundle extras) {
            try {
                mBinder.playFromSearch(query, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in playFromSearch. " + e);
            }
        }

        @java.lang.Override
        public void playFromUri(android.net.Uri uri, android.os.Bundle extras) {
            try {
                mBinder.playFromUri(uri, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in playFromUri. " + e);
            }
        }

        @java.lang.Override
        public void skipToQueueItem(long id) {
            try {
                mBinder.skipToQueueItem(id);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in skipToQueueItem. " + e);
            }
        }

        @java.lang.Override
        public void pause() {
            try {
                mBinder.pause();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in pause. " + e);
            }
        }

        @java.lang.Override
        public void stop() {
            try {
                mBinder.stop();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in stop. " + e);
            }
        }

        @java.lang.Override
        public void seekTo(long pos) {
            try {
                mBinder.seekTo(pos);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in seekTo. " + e);
            }
        }

        @java.lang.Override
        public void fastForward() {
            try {
                mBinder.fastForward();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in fastForward. " + e);
            }
        }

        @java.lang.Override
        public void skipToNext() {
            try {
                mBinder.next();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in skipToNext. " + e);
            }
        }

        @java.lang.Override
        public void rewind() {
            try {
                mBinder.rewind();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in rewind. " + e);
            }
        }

        @java.lang.Override
        public void skipToPrevious() {
            try {
                mBinder.previous();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in skipToPrevious. " + e);
            }
        }

        @java.lang.Override
        public void setRating(android.support.v4.media.RatingCompat rating) {
            try {
                mBinder.rate(rating);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in setRating. " + e);
            }
        }

        @java.lang.Override
        public void sendCustomAction(android.support.v4.media.session.PlaybackStateCompat.CustomAction customAction, android.os.Bundle args) {
            sendCustomAction(customAction.getAction(), args);
        }

        @java.lang.Override
        public void sendCustomAction(java.lang.String action, android.os.Bundle args) {
            try {
                mBinder.sendCustomAction(action, args);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.support.v4.media.session.MediaControllerCompat.TAG, "Dead object in sendCustomAction. " + e);
            }
        }
    }

    static class MediaControllerImplApi21 implements android.support.v4.media.session.MediaControllerCompat.MediaControllerImpl {
        protected final java.lang.Object mControllerObj;

        public MediaControllerImplApi21(android.content.Context context, android.support.v4.media.session.MediaSessionCompat session) {
            mControllerObj = android.support.v4.media.session.MediaControllerCompatApi21.fromToken(context, session.getSessionToken().getToken());
        }

        public MediaControllerImplApi21(android.content.Context context, android.support.v4.media.session.MediaSessionCompat.Token sessionToken) throws android.os.RemoteException {
            mControllerObj = android.support.v4.media.session.MediaControllerCompatApi21.fromToken(context, sessionToken.getToken());
            if (mControllerObj == null)
                throw new android.os.RemoteException();

        }

        @java.lang.Override
        public void registerCallback(android.support.v4.media.session.MediaControllerCompat.Callback callback, android.os.Handler handler) {
            android.support.v4.media.session.MediaControllerCompatApi21.registerCallback(mControllerObj, callback.mCallbackObj, handler);
        }

        @java.lang.Override
        public void unregisterCallback(android.support.v4.media.session.MediaControllerCompat.Callback callback) {
            android.support.v4.media.session.MediaControllerCompatApi21.unregisterCallback(mControllerObj, callback.mCallbackObj);
        }

        @java.lang.Override
        public boolean dispatchMediaButtonEvent(android.view.KeyEvent event) {
            return android.support.v4.media.session.MediaControllerCompatApi21.dispatchMediaButtonEvent(mControllerObj, event);
        }

        @java.lang.Override
        public android.support.v4.media.session.MediaControllerCompat.TransportControls getTransportControls() {
            java.lang.Object controlsObj = android.support.v4.media.session.MediaControllerCompatApi21.getTransportControls(mControllerObj);
            return controlsObj != null ? new android.support.v4.media.session.MediaControllerCompat.TransportControlsApi21(controlsObj) : null;
        }

        @java.lang.Override
        public android.support.v4.media.session.PlaybackStateCompat getPlaybackState() {
            java.lang.Object stateObj = android.support.v4.media.session.MediaControllerCompatApi21.getPlaybackState(mControllerObj);
            return stateObj != null ? android.support.v4.media.session.PlaybackStateCompat.fromPlaybackState(stateObj) : null;
        }

        @java.lang.Override
        public android.support.v4.media.MediaMetadataCompat getMetadata() {
            java.lang.Object metadataObj = android.support.v4.media.session.MediaControllerCompatApi21.getMetadata(mControllerObj);
            return metadataObj != null ? android.support.v4.media.MediaMetadataCompat.fromMediaMetadata(metadataObj) : null;
        }

        @java.lang.Override
        public java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> getQueue() {
            java.util.List<java.lang.Object> queueObjs = android.support.v4.media.session.MediaControllerCompatApi21.getQueue(mControllerObj);
            return queueObjs != null ? android.support.v4.media.session.MediaSessionCompat.QueueItem.fromQueueItemList(queueObjs) : null;
        }

        @java.lang.Override
        public java.lang.CharSequence getQueueTitle() {
            return android.support.v4.media.session.MediaControllerCompatApi21.getQueueTitle(mControllerObj);
        }

        @java.lang.Override
        public android.os.Bundle getExtras() {
            return android.support.v4.media.session.MediaControllerCompatApi21.getExtras(mControllerObj);
        }

        @java.lang.Override
        public int getRatingType() {
            return android.support.v4.media.session.MediaControllerCompatApi21.getRatingType(mControllerObj);
        }

        @java.lang.Override
        public long getFlags() {
            return android.support.v4.media.session.MediaControllerCompatApi21.getFlags(mControllerObj);
        }

        @java.lang.Override
        public android.support.v4.media.session.MediaControllerCompat.PlaybackInfo getPlaybackInfo() {
            java.lang.Object volumeInfoObj = android.support.v4.media.session.MediaControllerCompatApi21.getPlaybackInfo(mControllerObj);
            return volumeInfoObj != null ? new android.support.v4.media.session.MediaControllerCompat.PlaybackInfo(android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.getPlaybackType(volumeInfoObj), android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.getLegacyAudioStream(volumeInfoObj), android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.getVolumeControl(volumeInfoObj), android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.getMaxVolume(volumeInfoObj), android.support.v4.media.session.MediaControllerCompatApi21.PlaybackInfo.getCurrentVolume(volumeInfoObj)) : null;
        }

        @java.lang.Override
        public android.app.PendingIntent getSessionActivity() {
            return android.support.v4.media.session.MediaControllerCompatApi21.getSessionActivity(mControllerObj);
        }

        @java.lang.Override
        public void setVolumeTo(int value, int flags) {
            android.support.v4.media.session.MediaControllerCompatApi21.setVolumeTo(mControllerObj, value, flags);
        }

        @java.lang.Override
        public void adjustVolume(int direction, int flags) {
            android.support.v4.media.session.MediaControllerCompatApi21.adjustVolume(mControllerObj, direction, flags);
        }

        @java.lang.Override
        public void sendCommand(java.lang.String command, android.os.Bundle params, android.os.ResultReceiver cb) {
            android.support.v4.media.session.MediaControllerCompatApi21.sendCommand(mControllerObj, command, params, cb);
        }

        @java.lang.Override
        public java.lang.String getPackageName() {
            return android.support.v4.media.session.MediaControllerCompatApi21.getPackageName(mControllerObj);
        }

        @java.lang.Override
        public java.lang.Object getMediaController() {
            return mControllerObj;
        }
    }

    static class TransportControlsApi21 extends android.support.v4.media.session.MediaControllerCompat.TransportControls {
        protected final java.lang.Object mControlsObj;

        public TransportControlsApi21(java.lang.Object controlsObj) {
            mControlsObj = controlsObj;
        }

        @java.lang.Override
        public void prepare() {
            sendCustomAction(android.support.v4.media.session.MediaSessionCompat.ACTION_PREPARE, null);
        }

        @java.lang.Override
        public void prepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putString(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_MEDIA_ID, mediaId);
            bundle.putBundle(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_EXTRAS, extras);
            sendCustomAction(android.support.v4.media.session.MediaSessionCompat.ACTION_PREPARE_FROM_MEDIA_ID, bundle);
        }

        @java.lang.Override
        public void prepareFromSearch(java.lang.String query, android.os.Bundle extras) {
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putString(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_QUERY, query);
            bundle.putBundle(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_EXTRAS, extras);
            sendCustomAction(android.support.v4.media.session.MediaSessionCompat.ACTION_PREPARE_FROM_SEARCH, bundle);
        }

        @java.lang.Override
        public void prepareFromUri(android.net.Uri uri, android.os.Bundle extras) {
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putParcelable(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_URI, uri);
            bundle.putBundle(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_EXTRAS, extras);
            sendCustomAction(android.support.v4.media.session.MediaSessionCompat.ACTION_PREPARE_FROM_URI, bundle);
        }

        @java.lang.Override
        public void play() {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.play(mControlsObj);
        }

        @java.lang.Override
        public void pause() {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.pause(mControlsObj);
        }

        @java.lang.Override
        public void stop() {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.stop(mControlsObj);
        }

        @java.lang.Override
        public void seekTo(long pos) {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.seekTo(mControlsObj, pos);
        }

        @java.lang.Override
        public void fastForward() {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.fastForward(mControlsObj);
        }

        @java.lang.Override
        public void rewind() {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.rewind(mControlsObj);
        }

        @java.lang.Override
        public void skipToNext() {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.skipToNext(mControlsObj);
        }

        @java.lang.Override
        public void skipToPrevious() {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.skipToPrevious(mControlsObj);
        }

        @java.lang.Override
        public void setRating(android.support.v4.media.RatingCompat rating) {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.setRating(mControlsObj, rating != null ? rating.getRating() : null);
        }

        @java.lang.Override
        public void playFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.playFromMediaId(mControlsObj, mediaId, extras);
        }

        @java.lang.Override
        public void playFromSearch(java.lang.String query, android.os.Bundle extras) {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.playFromSearch(mControlsObj, query, extras);
        }

        @java.lang.Override
        public void playFromUri(android.net.Uri uri, android.os.Bundle extras) {
            if ((uri == null) || android.net.Uri.EMPTY.equals(uri)) {
                throw new java.lang.IllegalArgumentException("You must specify a non-empty Uri for playFromUri.");
            }
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putParcelable(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_URI, uri);
            bundle.putParcelable(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_EXTRAS, extras);
            sendCustomAction(android.support.v4.media.session.MediaSessionCompat.ACTION_PLAY_FROM_URI, bundle);
        }

        @java.lang.Override
        public void skipToQueueItem(long id) {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.skipToQueueItem(mControlsObj, id);
        }

        @java.lang.Override
        public void sendCustomAction(android.support.v4.media.session.PlaybackStateCompat.CustomAction customAction, android.os.Bundle args) {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.sendCustomAction(mControlsObj, customAction.getAction(), args);
        }

        @java.lang.Override
        public void sendCustomAction(java.lang.String action, android.os.Bundle args) {
            android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.sendCustomAction(mControlsObj, action, args);
        }
    }

    static class MediaControllerImplApi23 extends android.support.v4.media.session.MediaControllerCompat.MediaControllerImplApi21 {
        public MediaControllerImplApi23(android.content.Context context, android.support.v4.media.session.MediaSessionCompat session) {
            super(context, session);
        }

        public MediaControllerImplApi23(android.content.Context context, android.support.v4.media.session.MediaSessionCompat.Token sessionToken) throws android.os.RemoteException {
            super(context, sessionToken);
        }

        @java.lang.Override
        public android.support.v4.media.session.MediaControllerCompat.TransportControls getTransportControls() {
            java.lang.Object controlsObj = android.support.v4.media.session.MediaControllerCompatApi21.getTransportControls(mControllerObj);
            return controlsObj != null ? new android.support.v4.media.session.MediaControllerCompat.TransportControlsApi23(controlsObj) : null;
        }
    }

    static class TransportControlsApi23 extends android.support.v4.media.session.MediaControllerCompat.TransportControlsApi21 {
        public TransportControlsApi23(java.lang.Object controlsObj) {
            super(controlsObj);
        }

        @java.lang.Override
        public void playFromUri(android.net.Uri uri, android.os.Bundle extras) {
            android.support.v4.media.session.MediaControllerCompatApi23.TransportControls.playFromUri(mControlsObj, uri, extras);
        }
    }

    static class MediaControllerImplApi24 extends android.support.v4.media.session.MediaControllerCompat.MediaControllerImplApi23 {
        public MediaControllerImplApi24(android.content.Context context, android.support.v4.media.session.MediaSessionCompat session) {
            super(context, session);
        }

        public MediaControllerImplApi24(android.content.Context context, android.support.v4.media.session.MediaSessionCompat.Token sessionToken) throws android.os.RemoteException {
            super(context, sessionToken);
        }

        @java.lang.Override
        public android.support.v4.media.session.MediaControllerCompat.TransportControls getTransportControls() {
            java.lang.Object controlsObj = android.support.v4.media.session.MediaControllerCompatApi21.getTransportControls(mControllerObj);
            return controlsObj != null ? new android.support.v4.media.session.MediaControllerCompat.TransportControlsApi24(controlsObj) : null;
        }
    }

    static class TransportControlsApi24 extends android.support.v4.media.session.MediaControllerCompat.TransportControlsApi23 {
        public TransportControlsApi24(java.lang.Object controlsObj) {
            super(controlsObj);
        }

        @java.lang.Override
        public void prepare() {
            android.support.v4.media.session.MediaControllerCompatApi24.TransportControls.prepare(mControlsObj);
        }

        @java.lang.Override
        public void prepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
            android.support.v4.media.session.MediaControllerCompatApi24.TransportControls.prepareFromMediaId(mControlsObj, mediaId, extras);
        }

        @java.lang.Override
        public void prepareFromSearch(java.lang.String query, android.os.Bundle extras) {
            android.support.v4.media.session.MediaControllerCompatApi24.TransportControls.prepareFromSearch(mControlsObj, query, extras);
        }

        @java.lang.Override
        public void prepareFromUri(android.net.Uri uri, android.os.Bundle extras) {
            android.support.v4.media.session.MediaControllerCompatApi24.TransportControls.prepareFromUri(mControlsObj, uri, extras);
        }
    }
}

