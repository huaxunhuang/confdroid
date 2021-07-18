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
package android.media.session;


/**
 * Allows an app to interact with an ongoing media session. Media buttons and
 * other commands can be sent to the session. A callback may be registered to
 * receive updates from the session, such as metadata and play state changes.
 * <p>
 * A MediaController can be created through {@link MediaSessionManager} if you
 * hold the "android.permission.MEDIA_CONTENT_CONTROL" permission or are an
 * enabled notification listener or by getting a {@link MediaSession.Token}
 * directly from the session owner.
 * <p>
 * MediaController objects are thread-safe.
 */
public final class MediaController {
    private static final java.lang.String TAG = "MediaController";

    private static final int MSG_EVENT = 1;

    private static final int MSG_UPDATE_PLAYBACK_STATE = 2;

    private static final int MSG_UPDATE_METADATA = 3;

    private static final int MSG_UPDATE_VOLUME = 4;

    private static final int MSG_UPDATE_QUEUE = 5;

    private static final int MSG_UPDATE_QUEUE_TITLE = 6;

    private static final int MSG_UPDATE_EXTRAS = 7;

    private static final int MSG_DESTROYED = 8;

    private final android.media.session.ISessionController mSessionBinder;

    private final android.media.session.MediaSession.Token mToken;

    private final android.content.Context mContext;

    private final android.media.session.MediaController.CallbackStub mCbStub = new android.media.session.MediaController.CallbackStub(this);

    private final java.util.ArrayList<android.media.session.MediaController.MessageHandler> mCallbacks = new java.util.ArrayList<android.media.session.MediaController.MessageHandler>();

    private final java.lang.Object mLock = new java.lang.Object();

    private boolean mCbRegistered = false;

    private java.lang.String mPackageName;

    private java.lang.String mTag;

    private final android.media.session.MediaController.TransportControls mTransportControls;

    /**
     * Call for creating a MediaController directly from a binder. Should only
     * be used by framework code.
     *
     * @unknown 
     */
    public MediaController(android.content.Context context, android.media.session.ISessionController sessionBinder) {
        if (sessionBinder == null) {
            throw new java.lang.IllegalArgumentException("Session token cannot be null");
        }
        if (context == null) {
            throw new java.lang.IllegalArgumentException("Context cannot be null");
        }
        mSessionBinder = sessionBinder;
        mTransportControls = new android.media.session.MediaController.TransportControls();
        mToken = new android.media.session.MediaSession.Token(sessionBinder);
        mContext = context;
    }

    /**
     * Create a new MediaController from a session's token.
     *
     * @param context
     * 		The caller's context.
     * @param token
     * 		The token for the session.
     */
    public MediaController(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.media.session.MediaSession.Token token) {
        this(context, token.getBinder());
    }

    /**
     * Get a {@link TransportControls} instance to send transport actions to
     * the associated session.
     *
     * @return A transport controls instance.
     */
    @android.annotation.NonNull
    public android.media.session.MediaController.TransportControls getTransportControls() {
        return mTransportControls;
    }

    /**
     * Send the specified media button event to the session. Only media keys can
     * be sent by this method, other keys will be ignored.
     *
     * @param keyEvent
     * 		The media button event to dispatch.
     * @return true if the event was sent to the session, false otherwise.
     */
    public boolean dispatchMediaButtonEvent(@android.annotation.NonNull
    android.view.KeyEvent keyEvent) {
        if (keyEvent == null) {
            throw new java.lang.IllegalArgumentException("KeyEvent may not be null");
        }
        if (!android.view.KeyEvent.isMediaKey(keyEvent.getKeyCode())) {
            return false;
        }
        try {
            return mSessionBinder.sendMediaButton(keyEvent);
        } catch (android.os.RemoteException e) {
            // System is dead. =(
        }
        return false;
    }

    /**
     * Get the current playback state for this session.
     *
     * @return The current PlaybackState or null
     */
    @android.annotation.Nullable
    public android.media.session.PlaybackState getPlaybackState() {
        try {
            return mSessionBinder.getPlaybackState();
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaController.TAG, "Error calling getPlaybackState.", e);
            return null;
        }
    }

    /**
     * Get the current metadata for this session.
     *
     * @return The current MediaMetadata or null.
     */
    @android.annotation.Nullable
    public android.media.MediaMetadata getMetadata() {
        try {
            return mSessionBinder.getMetadata();
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaController.TAG, "Error calling getMetadata.", e);
            return null;
        }
    }

    /**
     * Get the current play queue for this session if one is set. If you only
     * care about the current item {@link #getMetadata()} should be used.
     *
     * @return The current play queue or null.
     */
    @android.annotation.Nullable
    public java.util.List<android.media.session.MediaSession.QueueItem> getQueue() {
        try {
            android.content.pm.ParceledListSlice queue = mSessionBinder.getQueue();
            if (queue != null) {
                return queue.getList();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaController.TAG, "Error calling getQueue.", e);
        }
        return null;
    }

    /**
     * Get the queue title for this session.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getQueueTitle() {
        try {
            return mSessionBinder.getQueueTitle();
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaController.TAG, "Error calling getQueueTitle", e);
        }
        return null;
    }

    /**
     * Get the extras for this session.
     */
    @android.annotation.Nullable
    public android.os.Bundle getExtras() {
        try {
            return mSessionBinder.getExtras();
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaController.TAG, "Error calling getExtras", e);
        }
        return null;
    }

    /**
     * Get the rating type supported by the session. One of:
     * <ul>
     * <li>{@link Rating#RATING_NONE}</li>
     * <li>{@link Rating#RATING_HEART}</li>
     * <li>{@link Rating#RATING_THUMB_UP_DOWN}</li>
     * <li>{@link Rating#RATING_3_STARS}</li>
     * <li>{@link Rating#RATING_4_STARS}</li>
     * <li>{@link Rating#RATING_5_STARS}</li>
     * <li>{@link Rating#RATING_PERCENTAGE}</li>
     * </ul>
     *
     * @return The supported rating type
     */
    public int getRatingType() {
        try {
            return mSessionBinder.getRatingType();
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaController.TAG, "Error calling getRatingType.", e);
            return android.media.Rating.RATING_NONE;
        }
    }

    /**
     * Get the flags for this session. Flags are defined in {@link MediaSession}.
     *
     * @return The current set of flags for the session.
     */
    @android.media.session.MediaSession.SessionFlags
    public long getFlags() {
        try {
            return mSessionBinder.getFlags();
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaController.TAG, "Error calling getFlags.", e);
        }
        return 0;
    }

    /**
     * Get the current playback info for this session.
     *
     * @return The current playback info or null.
     */
    @android.annotation.Nullable
    public android.media.session.MediaController.PlaybackInfo getPlaybackInfo() {
        try {
            android.media.session.ParcelableVolumeInfo result = mSessionBinder.getVolumeAttributes();
            return new android.media.session.MediaController.PlaybackInfo(result.volumeType, result.audioAttrs, result.controlType, result.maxVolume, result.currentVolume);
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaController.TAG, "Error calling getAudioInfo.", e);
        }
        return null;
    }

    /**
     * Get an intent for launching UI associated with this session if one
     * exists.
     *
     * @return A {@link PendingIntent} to launch UI or null.
     */
    @android.annotation.Nullable
    public android.app.PendingIntent getSessionActivity() {
        try {
            return mSessionBinder.getLaunchPendingIntent();
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaController.TAG, "Error calling getPendingIntent.", e);
        }
        return null;
    }

    /**
     * Get the token for the session this is connected to.
     *
     * @return The token for the connected session.
     */
    @android.annotation.NonNull
    public android.media.session.MediaSession.Token getSessionToken() {
        return mToken;
    }

    /**
     * Set the volume of the output this session is playing on. The command will
     * be ignored if it does not support
     * {@link VolumeProvider#VOLUME_CONTROL_ABSOLUTE}. The flags in
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
        try {
            mSessionBinder.setVolumeTo(value, flags, mContext.getPackageName());
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaController.TAG, "Error calling setVolumeTo.", e);
        }
    }

    /**
     * Adjust the volume of the output this session is playing on. The direction
     * must be one of {@link AudioManager#ADJUST_LOWER},
     * {@link AudioManager#ADJUST_RAISE}, or {@link AudioManager#ADJUST_SAME}.
     * The command will be ignored if the session does not support
     * {@link VolumeProvider#VOLUME_CONTROL_RELATIVE} or
     * {@link VolumeProvider#VOLUME_CONTROL_ABSOLUTE}. The flags in
     * {@link AudioManager} may be used to affect the handling.
     *
     * @see #getPlaybackInfo()
     * @param direction
     * 		The direction to adjust the volume in.
     * @param flags
     * 		Any flags to pass with the command.
     */
    public void adjustVolume(int direction, int flags) {
        try {
            mSessionBinder.adjustVolume(direction, flags, mContext.getPackageName());
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaController.TAG, "Error calling adjustVolumeBy.", e);
        }
    }

    /**
     * Registers a callback to receive updates from the Session. Updates will be
     * posted on the caller's thread.
     *
     * @param callback
     * 		The callback object, must not be null.
     */
    public void registerCallback(@android.annotation.NonNull
    android.media.session.MediaController.Callback callback) {
        registerCallback(callback, null);
    }

    /**
     * Registers a callback to receive updates from the session. Updates will be
     * posted on the specified handler's thread.
     *
     * @param callback
     * 		The callback object, must not be null.
     * @param handler
     * 		The handler to post updates on. If null the callers thread
     * 		will be used.
     */
    public void registerCallback(@android.annotation.NonNull
    android.media.session.MediaController.Callback callback, @android.annotation.Nullable
    android.os.Handler handler) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback must not be null");
        }
        if (handler == null) {
            handler = new android.os.Handler();
        }
        synchronized(mLock) {
            addCallbackLocked(callback, handler);
        }
    }

    /**
     * Unregisters the specified callback. If an update has already been posted
     * you may still receive it after calling this method.
     *
     * @param callback
     * 		The callback to remove.
     */
    public void unregisterCallback(@android.annotation.NonNull
    android.media.session.MediaController.Callback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback must not be null");
        }
        synchronized(mLock) {
            removeCallbackLocked(callback);
        }
    }

    /**
     * Sends a generic command to the session. It is up to the session creator
     * to decide what commands and parameters they will support. As such,
     * commands should only be sent to sessions that the controller owns.
     *
     * @param command
     * 		The command to send
     * @param args
     * 		Any parameters to include with the command
     * @param cb
     * 		The callback to receive the result on
     */
    public void sendCommand(@android.annotation.NonNull
    java.lang.String command, @android.annotation.Nullable
    android.os.Bundle args, @android.annotation.Nullable
    android.os.ResultReceiver cb) {
        if (android.text.TextUtils.isEmpty(command)) {
            throw new java.lang.IllegalArgumentException("command cannot be null or empty");
        }
        try {
            mSessionBinder.sendCommand(command, args, cb);
        } catch (android.os.RemoteException e) {
            android.util.Log.d(android.media.session.MediaController.TAG, "Dead object in sendCommand.", e);
        }
    }

    /**
     * Get the session owner's package name.
     *
     * @return The package name of of the session owner.
     */
    public java.lang.String getPackageName() {
        if (mPackageName == null) {
            try {
                mPackageName = mSessionBinder.getPackageName();
            } catch (android.os.RemoteException e) {
                android.util.Log.d(android.media.session.MediaController.TAG, "Dead object in getPackageName.", e);
            }
        }
        return mPackageName;
    }

    /**
     * Get the session's tag for debugging purposes.
     *
     * @return The session's tag.
     * @unknown 
     */
    public java.lang.String getTag() {
        if (mTag == null) {
            try {
                mTag = mSessionBinder.getTag();
            } catch (android.os.RemoteException e) {
                android.util.Log.d(android.media.session.MediaController.TAG, "Dead object in getTag.", e);
            }
        }
        return mTag;
    }

    /* @hide */
    android.media.session.ISessionController getSessionBinder() {
        return mSessionBinder;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean controlsSameSession(android.media.session.MediaController other) {
        if (other == null)
            return false;

        return mSessionBinder.asBinder() == other.getSessionBinder().asBinder();
    }

    private void addCallbackLocked(android.media.session.MediaController.Callback cb, android.os.Handler handler) {
        if (getHandlerForCallbackLocked(cb) != null) {
            android.util.Log.w(android.media.session.MediaController.TAG, "Callback is already added, ignoring");
            return;
        }
        android.media.session.MediaController.MessageHandler holder = new android.media.session.MediaController.MessageHandler(handler.getLooper(), cb);
        mCallbacks.add(holder);
        holder.mRegistered = true;
        if (!mCbRegistered) {
            try {
                mSessionBinder.registerCallbackListener(mCbStub);
                mCbRegistered = true;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.media.session.MediaController.TAG, "Dead object in registerCallback", e);
            }
        }
    }

    private boolean removeCallbackLocked(android.media.session.MediaController.Callback cb) {
        boolean success = false;
        for (int i = mCallbacks.size() - 1; i >= 0; i--) {
            android.media.session.MediaController.MessageHandler handler = mCallbacks.get(i);
            if (cb == handler.mCallback) {
                mCallbacks.remove(i);
                success = true;
                handler.mRegistered = false;
            }
        }
        if (mCbRegistered && (mCallbacks.size() == 0)) {
            try {
                mSessionBinder.unregisterCallbackListener(mCbStub);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.media.session.MediaController.TAG, "Dead object in removeCallbackLocked");
            }
            mCbRegistered = false;
        }
        return success;
    }

    private android.media.session.MediaController.MessageHandler getHandlerForCallbackLocked(android.media.session.MediaController.Callback cb) {
        if (cb == null) {
            throw new java.lang.IllegalArgumentException("Callback cannot be null");
        }
        for (int i = mCallbacks.size() - 1; i >= 0; i--) {
            android.media.session.MediaController.MessageHandler handler = mCallbacks.get(i);
            if (cb == handler.mCallback) {
                return handler;
            }
        }
        return null;
    }

    private final void postMessage(int what, java.lang.Object obj, android.os.Bundle data) {
        synchronized(mLock) {
            for (int i = mCallbacks.size() - 1; i >= 0; i--) {
                mCallbacks.get(i).post(what, obj, data);
            }
        }
    }

    /**
     * Callback for receiving updates from the session. A Callback can be
     * registered using {@link #registerCallback}.
     */
    public static abstract class Callback {
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
         * 		Optional parameters for the event, may be null.
         */
        public void onSessionEvent(@android.annotation.NonNull
        java.lang.String event, @android.annotation.Nullable
        android.os.Bundle extras) {
        }

        /**
         * Override to handle changes in playback state.
         *
         * @param state
         * 		The new playback state of the session
         */
        public void onPlaybackStateChanged(@android.annotation.NonNull
        android.media.session.PlaybackState state) {
        }

        /**
         * Override to handle changes to the current metadata.
         *
         * @param metadata
         * 		The current metadata for the session or null if none.
         * @see MediaMetadata
         */
        public void onMetadataChanged(@android.annotation.Nullable
        android.media.MediaMetadata metadata) {
        }

        /**
         * Override to handle changes to items in the queue.
         *
         * @param queue
         * 		A list of items in the current play queue. It should
         * 		include the currently playing item as well as previous and
         * 		upcoming items if applicable.
         * @see MediaSession.QueueItem
         */
        public void onQueueChanged(@android.annotation.Nullable
        java.util.List<android.media.session.MediaSession.QueueItem> queue) {
        }

        /**
         * Override to handle changes to the queue title.
         *
         * @param title
         * 		The title that should be displayed along with the play queue such as
         * 		"Now Playing". May be null if there is no such title.
         */
        public void onQueueTitleChanged(@android.annotation.Nullable
        java.lang.CharSequence title) {
        }

        /**
         * Override to handle changes to the {@link MediaSession} extras.
         *
         * @param extras
         * 		The extras that can include other information associated with the
         * 		{@link MediaSession}.
         */
        public void onExtrasChanged(@android.annotation.Nullable
        android.os.Bundle extras) {
        }

        /**
         * Override to handle changes to the audio info.
         *
         * @param info
         * 		The current audio info for this session.
         */
        public void onAudioInfoChanged(android.media.session.MediaController.PlaybackInfo info) {
        }
    }

    /**
     * Interface for controlling media playback on a session. This allows an app
     * to send media transport commands to the session.
     */
    public final class TransportControls {
        private static final java.lang.String TAG = "TransportController";

        private TransportControls() {
        }

        /**
         * Request that the player prepare its playback. In other words, other sessions can continue
         * to play during the preparation of this session. This method can be used to speed up the
         * start of the playback. Once the preparation is done, the session will change its playback
         * state to {@link PlaybackState#STATE_PAUSED}. Afterwards, {@link #play} can be called to
         * start playback.
         */
        public void prepare() {
            try {
                mSessionBinder.prepare();
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, "Error calling prepare.", e);
            }
        }

        /**
         * Request that the player prepare playback for a specific media id. In other words, other
         * sessions can continue to play during the preparation of this session. This method can be
         * used to speed up the start of the playback. Once the preparation is done, the session
         * will change its playback state to {@link PlaybackState#STATE_PAUSED}. Afterwards,
         * {@link #play} can be called to start playback. If the preparation is not needed,
         * {@link #playFromMediaId} can be directly called without this method.
         *
         * @param mediaId
         * 		The id of the requested media.
         * @param extras
         * 		Optional extras that can include extra information about the media item
         * 		to be prepared.
         */
        public void prepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
            if (android.text.TextUtils.isEmpty(mediaId)) {
                throw new java.lang.IllegalArgumentException("You must specify a non-empty String for prepareFromMediaId.");
            }
            try {
                mSessionBinder.prepareFromMediaId(mediaId, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, ("Error calling prepare(" + mediaId) + ").", e);
            }
        }

        /**
         * Request that the player prepare playback for a specific search query. An empty or null
         * query should be treated as a request to prepare any music. In other words, other sessions
         * can continue to play during the preparation of this session. This method can be used to
         * speed up the start of the playback. Once the preparation is done, the session will
         * change its playback state to {@link PlaybackState#STATE_PAUSED}. Afterwards,
         * {@link #play} can be called to start playback. If the preparation is not needed,
         * {@link #playFromSearch} can be directly called without this method.
         *
         * @param query
         * 		The search query.
         * @param extras
         * 		Optional extras that can include extra information
         * 		about the query.
         */
        public void prepareFromSearch(java.lang.String query, android.os.Bundle extras) {
            if (query == null) {
                // This is to remain compatible with
                // INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH
                query = "";
            }
            try {
                mSessionBinder.prepareFromSearch(query, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, ("Error calling prepare(" + query) + ").", e);
            }
        }

        /**
         * Request that the player prepare playback for a specific {@link Uri}. In other words,
         * other sessions can continue to play during the preparation of this session. This method
         * can be used to speed up the start of the playback. Once the preparation is done, the
         * session will change its playback state to {@link PlaybackState#STATE_PAUSED}. Afterwards,
         * {@link #play} can be called to start playback. If the preparation is not needed,
         * {@link #playFromUri} can be directly called without this method.
         *
         * @param uri
         * 		The URI of the requested media.
         * @param extras
         * 		Optional extras that can include extra information about the media item
         * 		to be prepared.
         */
        public void prepareFromUri(android.net.Uri uri, android.os.Bundle extras) {
            if ((uri == null) || android.net.Uri.EMPTY.equals(uri)) {
                throw new java.lang.IllegalArgumentException("You must specify a non-empty Uri for prepareFromUri.");
            }
            try {
                mSessionBinder.prepareFromUri(uri, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, ("Error calling prepare(" + uri) + ").", e);
            }
        }

        /**
         * Request that the player start its playback at its current position.
         */
        public void play() {
            try {
                mSessionBinder.play();
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, "Error calling play.", e);
            }
        }

        /**
         * Request that the player start playback for a specific media id.
         *
         * @param mediaId
         * 		The id of the requested media.
         * @param extras
         * 		Optional extras that can include extra information about the media item
         * 		to be played.
         */
        public void playFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
            if (android.text.TextUtils.isEmpty(mediaId)) {
                throw new java.lang.IllegalArgumentException("You must specify a non-empty String for playFromMediaId.");
            }
            try {
                mSessionBinder.playFromMediaId(mediaId, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, ("Error calling play(" + mediaId) + ").", e);
            }
        }

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
        public void playFromSearch(java.lang.String query, android.os.Bundle extras) {
            if (query == null) {
                // This is to remain compatible with
                // INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH
                query = "";
            }
            try {
                mSessionBinder.playFromSearch(query, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, ("Error calling play(" + query) + ").", e);
            }
        }

        /**
         * Request that the player start playback for a specific {@link Uri}.
         *
         * @param uri
         * 		The URI of the requested media.
         * @param extras
         * 		Optional extras that can include extra information about the media item
         * 		to be played.
         */
        public void playFromUri(android.net.Uri uri, android.os.Bundle extras) {
            if ((uri == null) || android.net.Uri.EMPTY.equals(uri)) {
                throw new java.lang.IllegalArgumentException("You must specify a non-empty Uri for playFromUri.");
            }
            try {
                mSessionBinder.playFromUri(uri, extras);
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, ("Error calling play(" + uri) + ").", e);
            }
        }

        /**
         * Play an item with a specific id in the play queue. If you specify an
         * id that is not in the play queue, the behavior is undefined.
         */
        public void skipToQueueItem(long id) {
            try {
                mSessionBinder.skipToQueueItem(id);
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, ("Error calling skipToItem(" + id) + ").", e);
            }
        }

        /**
         * Request that the player pause its playback and stay at its current
         * position.
         */
        public void pause() {
            try {
                mSessionBinder.pause();
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, "Error calling pause.", e);
            }
        }

        /**
         * Request that the player stop its playback; it may clear its state in
         * whatever way is appropriate.
         */
        public void stop() {
            try {
                mSessionBinder.stop();
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, "Error calling stop.", e);
            }
        }

        /**
         * Move to a new location in the media stream.
         *
         * @param pos
         * 		Position to move to, in milliseconds.
         */
        public void seekTo(long pos) {
            try {
                mSessionBinder.seekTo(pos);
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, "Error calling seekTo.", e);
            }
        }

        /**
         * Start fast forwarding. If playback is already fast forwarding this
         * may increase the rate.
         */
        public void fastForward() {
            try {
                mSessionBinder.fastForward();
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, "Error calling fastForward.", e);
            }
        }

        /**
         * Skip to the next item.
         */
        public void skipToNext() {
            try {
                mSessionBinder.next();
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, "Error calling next.", e);
            }
        }

        /**
         * Start rewinding. If playback is already rewinding this may increase
         * the rate.
         */
        public void rewind() {
            try {
                mSessionBinder.rewind();
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, "Error calling rewind.", e);
            }
        }

        /**
         * Skip to the previous item.
         */
        public void skipToPrevious() {
            try {
                mSessionBinder.previous();
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, "Error calling previous.", e);
            }
        }

        /**
         * Rate the current content. This will cause the rating to be set for
         * the current user. The Rating type must match the type returned by
         * {@link #getRatingType()}.
         *
         * @param rating
         * 		The rating to set for the current content
         */
        public void setRating(android.media.Rating rating) {
            try {
                mSessionBinder.rate(rating);
            } catch (android.os.RemoteException e) {
                android.util.Log.wtf(android.media.session.MediaController.TransportControls.TAG, "Error calling rate.", e);
            }
        }

        /**
         * Send a custom action back for the {@link MediaSession} to perform.
         *
         * @param customAction
         * 		The action to perform.
         * @param args
         * 		Optional arguments to supply to the {@link MediaSession} for this
         * 		custom action.
         */
        public void sendCustomAction(@android.annotation.NonNull
        android.media.session.PlaybackState.CustomAction customAction, @android.annotation.Nullable
        android.os.Bundle args) {
            if (customAction == null) {
                throw new java.lang.IllegalArgumentException("CustomAction cannot be null.");
            }
            sendCustomAction(customAction.getAction(), args);
        }

        /**
         * Send the id and args from a custom action back for the {@link MediaSession} to perform.
         *
         * @see #sendCustomAction(PlaybackState.CustomAction action, Bundle args)
         * @param action
         * 		The action identifier of the {@link PlaybackState.CustomAction} as
         * 		specified by the {@link MediaSession}.
         * @param args
         * 		Optional arguments to supply to the {@link MediaSession} for this
         * 		custom action.
         */
        public void sendCustomAction(@android.annotation.NonNull
        java.lang.String action, @android.annotation.Nullable
        android.os.Bundle args) {
            if (android.text.TextUtils.isEmpty(action)) {
                throw new java.lang.IllegalArgumentException("CustomAction cannot be null.");
            }
            try {
                mSessionBinder.sendCustomAction(action, args);
            } catch (android.os.RemoteException e) {
                android.util.Log.d(android.media.session.MediaController.TransportControls.TAG, "Dead object in sendCustomAction.", e);
            }
        }
    }

    /**
     * Holds information about the current playback and how audio is handled for
     * this session.
     */
    public static final class PlaybackInfo {
        /**
         * The session uses remote playback.
         */
        public static final int PLAYBACK_TYPE_REMOTE = 2;

        /**
         * The session uses local playback.
         */
        public static final int PLAYBACK_TYPE_LOCAL = 1;

        private final int mVolumeType;

        private final int mVolumeControl;

        private final int mMaxVolume;

        private final int mCurrentVolume;

        private final android.media.AudioAttributes mAudioAttrs;

        /**
         *
         *
         * @unknown 
         */
        public PlaybackInfo(int type, android.media.AudioAttributes attrs, int control, int max, int current) {
            mVolumeType = type;
            mAudioAttrs = attrs;
            mVolumeControl = control;
            mMaxVolume = max;
            mCurrentVolume = current;
        }

        /**
         * Get the type of playback which affects volume handling. One of:
         * <ul>
         * <li>{@link #PLAYBACK_TYPE_LOCAL}</li>
         * <li>{@link #PLAYBACK_TYPE_REMOTE}</li>
         * </ul>
         *
         * @return The type of playback this session is using.
         */
        public int getPlaybackType() {
            return mVolumeType;
        }

        /**
         * Get the audio attributes for this session. The attributes will affect
         * volume handling for the session. When the volume type is
         * {@link PlaybackInfo#PLAYBACK_TYPE_REMOTE} these may be ignored by the
         * remote volume handler.
         *
         * @return The attributes for this session.
         */
        public android.media.AudioAttributes getAudioAttributes() {
            return mAudioAttrs;
        }

        /**
         * Get the type of volume control that can be used. One of:
         * <ul>
         * <li>{@link VolumeProvider#VOLUME_CONTROL_ABSOLUTE}</li>
         * <li>{@link VolumeProvider#VOLUME_CONTROL_RELATIVE}</li>
         * <li>{@link VolumeProvider#VOLUME_CONTROL_FIXED}</li>
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

    private static final class CallbackStub extends android.media.session.ISessionControllerCallback.Stub {
        private final java.lang.ref.WeakReference<android.media.session.MediaController> mController;

        public CallbackStub(android.media.session.MediaController controller) {
            mController = new java.lang.ref.WeakReference<android.media.session.MediaController>(controller);
        }

        @java.lang.Override
        public void onSessionDestroyed() {
            android.media.session.MediaController controller = mController.get();
            if (controller != null) {
                controller.postMessage(android.media.session.MediaController.MSG_DESTROYED, null, null);
            }
        }

        @java.lang.Override
        public void onEvent(java.lang.String event, android.os.Bundle extras) {
            android.media.session.MediaController controller = mController.get();
            if (controller != null) {
                controller.postMessage(android.media.session.MediaController.MSG_EVENT, event, extras);
            }
        }

        @java.lang.Override
        public void onPlaybackStateChanged(android.media.session.PlaybackState state) {
            android.media.session.MediaController controller = mController.get();
            if (controller != null) {
                controller.postMessage(android.media.session.MediaController.MSG_UPDATE_PLAYBACK_STATE, state, null);
            }
        }

        @java.lang.Override
        public void onMetadataChanged(android.media.MediaMetadata metadata) {
            android.media.session.MediaController controller = mController.get();
            if (controller != null) {
                controller.postMessage(android.media.session.MediaController.MSG_UPDATE_METADATA, metadata, null);
            }
        }

        @java.lang.Override
        public void onQueueChanged(android.content.pm.ParceledListSlice parceledQueue) {
            java.util.List<android.media.session.MediaSession.QueueItem> queue = (parceledQueue == null) ? null : parceledQueue.getList();
            android.media.session.MediaController controller = mController.get();
            if (controller != null) {
                controller.postMessage(android.media.session.MediaController.MSG_UPDATE_QUEUE, queue, null);
            }
        }

        @java.lang.Override
        public void onQueueTitleChanged(java.lang.CharSequence title) {
            android.media.session.MediaController controller = mController.get();
            if (controller != null) {
                controller.postMessage(android.media.session.MediaController.MSG_UPDATE_QUEUE_TITLE, title, null);
            }
        }

        @java.lang.Override
        public void onExtrasChanged(android.os.Bundle extras) {
            android.media.session.MediaController controller = mController.get();
            if (controller != null) {
                controller.postMessage(android.media.session.MediaController.MSG_UPDATE_EXTRAS, extras, null);
            }
        }

        @java.lang.Override
        public void onVolumeInfoChanged(android.media.session.ParcelableVolumeInfo pvi) {
            android.media.session.MediaController controller = mController.get();
            if (controller != null) {
                android.media.session.MediaController.PlaybackInfo info = new android.media.session.MediaController.PlaybackInfo(pvi.volumeType, pvi.audioAttrs, pvi.controlType, pvi.maxVolume, pvi.currentVolume);
                controller.postMessage(android.media.session.MediaController.MSG_UPDATE_VOLUME, info, null);
            }
        }
    }

    private static final class MessageHandler extends android.os.Handler {
        private final android.media.session.MediaController.Callback mCallback;

        private boolean mRegistered = false;

        public MessageHandler(android.os.Looper looper, android.media.session.MediaController.Callback cb) {
            super(looper, null, true);
            mCallback = cb;
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            if (!mRegistered) {
                return;
            }
            switch (msg.what) {
                case android.media.session.MediaController.MSG_EVENT :
                    mCallback.onSessionEvent(((java.lang.String) (msg.obj)), msg.getData());
                    break;
                case android.media.session.MediaController.MSG_UPDATE_PLAYBACK_STATE :
                    mCallback.onPlaybackStateChanged(((android.media.session.PlaybackState) (msg.obj)));
                    break;
                case android.media.session.MediaController.MSG_UPDATE_METADATA :
                    mCallback.onMetadataChanged(((android.media.MediaMetadata) (msg.obj)));
                    break;
                case android.media.session.MediaController.MSG_UPDATE_QUEUE :
                    mCallback.onQueueChanged(((java.util.List<android.media.session.MediaSession.QueueItem>) (msg.obj)));
                    break;
                case android.media.session.MediaController.MSG_UPDATE_QUEUE_TITLE :
                    mCallback.onQueueTitleChanged(((java.lang.CharSequence) (msg.obj)));
                    break;
                case android.media.session.MediaController.MSG_UPDATE_EXTRAS :
                    mCallback.onExtrasChanged(((android.os.Bundle) (msg.obj)));
                    break;
                case android.media.session.MediaController.MSG_UPDATE_VOLUME :
                    mCallback.onAudioInfoChanged(((android.media.session.MediaController.PlaybackInfo) (msg.obj)));
                    break;
                case android.media.session.MediaController.MSG_DESTROYED :
                    mCallback.onSessionDestroyed();
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

