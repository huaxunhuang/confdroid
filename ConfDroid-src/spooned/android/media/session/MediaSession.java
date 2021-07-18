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
 * Allows interaction with media controllers, volume keys, media buttons, and
 * transport controls.
 * <p>
 * A MediaSession should be created when an app wants to publish media playback
 * information or handle media keys. In general an app only needs one session
 * for all playback, though multiple sessions can be created to provide finer
 * grain controls of media.
 * <p>
 * Once a session is created the owner of the session may pass its
 * {@link #getSessionToken() session token} to other processes to allow them to
 * create a {@link MediaController} to interact with the session.
 * <p>
 * To receive commands, media keys, and other events a {@link Callback} must be
 * set with {@link #setCallback(Callback)} and {@link #setActive(boolean)
 * setActive(true)} must be called.
 * <p>
 * When an app is finished performing playback it must call {@link #release()}
 * to clean up the session and notify any controllers.
 * <p>
 * MediaSession objects are thread safe.
 */
public final class MediaSession {
    private static final java.lang.String TAG = "MediaSession";

    /**
     * Set this flag on the session to indicate that it can handle media button
     * events.
     */
    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1 << 0;

    /**
     * Set this flag on the session to indicate that it handles transport
     * control commands through its {@link Callback}.
     */
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 1 << 1;

    /**
     * System only flag for a session that needs to have priority over all other
     * sessions. This flag ensures this session will receive media button events
     * regardless of the current ordering in the system.
     *
     * @unknown 
     */
    public static final int FLAG_EXCLUSIVE_GLOBAL_PRIORITY = 1 << 16;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(flag = true, value = { android.media.session.MediaSession.FLAG_HANDLES_MEDIA_BUTTONS, android.media.session.MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS, android.media.session.MediaSession.FLAG_EXCLUSIVE_GLOBAL_PRIORITY })
    public @interface SessionFlags {}

    private final java.lang.Object mLock = new java.lang.Object();

    private final int mMaxBitmapSize;

    private final android.media.session.MediaSession.Token mSessionToken;

    private final android.media.session.MediaController mController;

    private final android.media.session.ISession mBinder;

    private final android.media.session.MediaSession.CallbackStub mCbStub;

    private android.media.session.MediaSession.CallbackMessageHandler mCallback;

    private android.media.VolumeProvider mVolumeProvider;

    private android.media.session.PlaybackState mPlaybackState;

    private boolean mActive = false;

    /**
     * Creates a new session. The session will automatically be registered with
     * the system but will not be published until {@link #setActive(boolean)
     * setActive(true)} is called. You must call {@link #release()} when
     * finished with the session.
     *
     * @param context
     * 		The context to use to create the session.
     * @param tag
     * 		A short name for debugging purposes.
     */
    public MediaSession(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    java.lang.String tag) {
        this(context, tag, android.os.UserHandle.myUserId());
    }

    /**
     * Creates a new session as the specified user. To create a session as a
     * user other than your own you must hold the
     * {@link android.Manifest.permission#INTERACT_ACROSS_USERS_FULL}
     * permission.
     *
     * @param context
     * 		The context to use to create the session.
     * @param tag
     * 		A short name for debugging purposes.
     * @param userId
     * 		The user id to create the session as.
     * @unknown 
     */
    public MediaSession(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    java.lang.String tag, int userId) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("context cannot be null.");
        }
        if (android.text.TextUtils.isEmpty(tag)) {
            throw new java.lang.IllegalArgumentException("tag cannot be null or empty");
        }
        mMaxBitmapSize = context.getResources().getDimensionPixelSize(com.android.internal.R.dimen.config_mediaMetadataBitmapMaxSize);
        mCbStub = new android.media.session.MediaSession.CallbackStub(this);
        android.media.session.MediaSessionManager manager = ((android.media.session.MediaSessionManager) (context.getSystemService(android.content.Context.MEDIA_SESSION_SERVICE)));
        try {
            mBinder = manager.createSession(mCbStub, tag, userId);
            mSessionToken = new android.media.session.MediaSession.Token(mBinder.getController());
            mController = new android.media.session.MediaController(context, mSessionToken);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Remote error creating session.", e);
        }
    }

    /**
     * Set the callback to receive updates for the MediaSession. This includes
     * media button events and transport controls. The caller's thread will be
     * used to post updates.
     * <p>
     * Set the callback to null to stop receiving updates.
     *
     * @param callback
     * 		The callback object
     */
    public void setCallback(@android.annotation.Nullable
    android.media.session.MediaSession.Callback callback) {
        setCallback(callback, null);
    }

    /**
     * Set the callback to receive updates for the MediaSession. This includes
     * media button events and transport controls.
     * <p>
     * Set the callback to null to stop receiving updates.
     *
     * @param callback
     * 		The callback to receive updates on.
     * @param handler
     * 		The handler that events should be posted on.
     */
    public void setCallback(@android.annotation.Nullable
    android.media.session.MediaSession.Callback callback, @android.annotation.Nullable
    android.os.Handler handler) {
        synchronized(mLock) {
            if (callback == null) {
                if (mCallback != null) {
                    mCallback.mCallback.mSession = null;
                }
                mCallback = null;
                return;
            }
            if (mCallback != null) {
                // We're updating the callback, clear the session from the old
                // one.
                mCallback.mCallback.mSession = null;
            }
            if (handler == null) {
                handler = new android.os.Handler();
            }
            callback.mSession = this;
            android.media.session.MediaSession.CallbackMessageHandler msgHandler = new android.media.session.MediaSession.CallbackMessageHandler(handler.getLooper(), callback);
            mCallback = msgHandler;
        }
    }

    /**
     * Set an intent for launching UI for this Session. This can be used as a
     * quick link to an ongoing media screen. The intent should be for an
     * activity that may be started using {@link Activity#startActivity(Intent)}.
     *
     * @param pi
     * 		The intent to launch to show UI for this Session.
     */
    public void setSessionActivity(@android.annotation.Nullable
    android.app.PendingIntent pi) {
        try {
            mBinder.setLaunchPendingIntent(pi);
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaSession.TAG, "Failure in setLaunchPendingIntent.", e);
        }
    }

    /**
     * Set a pending intent for your media button receiver to allow restarting
     * playback after the session has been stopped. If your app is started in
     * this way an {@link Intent#ACTION_MEDIA_BUTTON} intent will be sent via
     * the pending intent.
     *
     * @param mbr
     * 		The {@link PendingIntent} to send the media button event to.
     */
    public void setMediaButtonReceiver(@android.annotation.Nullable
    android.app.PendingIntent mbr) {
        try {
            mBinder.setMediaButtonReceiver(mbr);
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaSession.TAG, "Failure in setMediaButtonReceiver.", e);
        }
    }

    /**
     * Set any flags for the session.
     *
     * @param flags
     * 		The flags to set for this session.
     */
    public void setFlags(@android.media.session.MediaSession.SessionFlags
    int flags) {
        try {
            mBinder.setFlags(flags);
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaSession.TAG, "Failure in setFlags.", e);
        }
    }

    /**
     * Set the attributes for this session's audio. This will affect the
     * system's volume handling for this session. If
     * {@link #setPlaybackToRemote} was previously called it will stop receiving
     * volume commands and the system will begin sending volume changes to the
     * appropriate stream.
     * <p>
     * By default sessions use attributes for media.
     *
     * @param attributes
     * 		The {@link AudioAttributes} for this session's audio.
     */
    public void setPlaybackToLocal(android.media.AudioAttributes attributes) {
        if (attributes == null) {
            throw new java.lang.IllegalArgumentException("Attributes cannot be null for local playback.");
        }
        try {
            mBinder.setPlaybackToLocal(attributes);
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaSession.TAG, "Failure in setPlaybackToLocal.", e);
        }
    }

    /**
     * Configure this session to use remote volume handling. This must be called
     * to receive volume button events, otherwise the system will adjust the
     * appropriate stream volume for this session. If
     * {@link #setPlaybackToLocal} was previously called the system will stop
     * handling volume changes for this session and pass them to the volume
     * provider instead.
     *
     * @param volumeProvider
     * 		The provider that will handle volume changes. May
     * 		not be null.
     */
    public void setPlaybackToRemote(@android.annotation.NonNull
    android.media.VolumeProvider volumeProvider) {
        if (volumeProvider == null) {
            throw new java.lang.IllegalArgumentException("volumeProvider may not be null!");
        }
        synchronized(mLock) {
            mVolumeProvider = volumeProvider;
        }
        volumeProvider.setCallback(new android.media.VolumeProvider.Callback() {
            @java.lang.Override
            public void onVolumeChanged(android.media.VolumeProvider volumeProvider) {
                notifyRemoteVolumeChanged(volumeProvider);
            }
        });
        try {
            mBinder.setPlaybackToRemote(volumeProvider.getVolumeControl(), volumeProvider.getMaxVolume());
            mBinder.setCurrentVolume(volumeProvider.getCurrentVolume());
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaSession.TAG, "Failure in setPlaybackToRemote.", e);
        }
    }

    /**
     * Set if this session is currently active and ready to receive commands. If
     * set to false your session's controller may not be discoverable. You must
     * set the session to active before it can start receiving media button
     * events or transport commands.
     *
     * @param active
     * 		Whether this session is active or not.
     */
    public void setActive(boolean active) {
        if (mActive == active) {
            return;
        }
        try {
            mBinder.setActive(active);
            mActive = active;
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaSession.TAG, "Failure in setActive.", e);
        }
    }

    /**
     * Get the current active state of this session.
     *
     * @return True if the session is active, false otherwise.
     */
    public boolean isActive() {
        return mActive;
    }

    /**
     * Send a proprietary event to all MediaControllers listening to this
     * Session. It's up to the Controller/Session owner to determine the meaning
     * of any events.
     *
     * @param event
     * 		The name of the event to send
     * @param extras
     * 		Any extras included with the event
     */
    public void sendSessionEvent(@android.annotation.NonNull
    java.lang.String event, @android.annotation.Nullable
    android.os.Bundle extras) {
        if (android.text.TextUtils.isEmpty(event)) {
            throw new java.lang.IllegalArgumentException("event cannot be null or empty");
        }
        try {
            mBinder.sendEvent(event, extras);
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaSession.TAG, "Error sending event", e);
        }
    }

    /**
     * This must be called when an app has finished performing playback. If
     * playback is expected to start again shortly the session can be left open,
     * but it must be released if your activity or service is being destroyed.
     */
    public void release() {
        try {
            mBinder.destroy();
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaSession.TAG, "Error releasing session: ", e);
        }
    }

    /**
     * Retrieve a token object that can be used by apps to create a
     * {@link MediaController} for interacting with this session. The owner of
     * the session is responsible for deciding how to distribute these tokens.
     *
     * @return A token that can be used to create a MediaController for this
    session
     */
    @android.annotation.NonNull
    public android.media.session.MediaSession.Token getSessionToken() {
        return mSessionToken;
    }

    /**
     * Get a controller for this session. This is a convenience method to avoid
     * having to cache your own controller in process.
     *
     * @return A controller for this session.
     */
    @android.annotation.NonNull
    public android.media.session.MediaController getController() {
        return mController;
    }

    /**
     * Update the current playback state.
     *
     * @param state
     * 		The current state of playback
     */
    public void setPlaybackState(@android.annotation.Nullable
    android.media.session.PlaybackState state) {
        mPlaybackState = state;
        try {
            mBinder.setPlaybackState(state);
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaSession.TAG, "Dead object in setPlaybackState.", e);
        }
    }

    /**
     * Update the current metadata. New metadata can be created using
     * {@link android.media.MediaMetadata.Builder}.
     *
     * @param metadata
     * 		The new metadata
     */
    public void setMetadata(@android.annotation.Nullable
    android.media.MediaMetadata metadata) {
        if (metadata != null) {
            metadata = new android.media.MediaMetadata.Builder(metadata, mMaxBitmapSize).build();
        }
        try {
            mBinder.setMetadata(metadata);
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaSession.TAG, "Dead object in setPlaybackState.", e);
        }
    }

    /**
     * Update the list of items in the play queue. It is an ordered list and
     * should contain the current item, and previous or upcoming items if they
     * exist. Specify null if there is no current play queue.
     * <p>
     * The queue should be of reasonable size. If the play queue is unbounded
     * within your app, it is better to send a reasonable amount in a sliding
     * window instead.
     *
     * @param queue
     * 		A list of items in the play queue.
     */
    public void setQueue(@android.annotation.Nullable
    java.util.List<android.media.session.MediaSession.QueueItem> queue) {
        try {
            mBinder.setQueue(queue == null ? null : new android.content.pm.ParceledListSlice<android.media.session.MediaSession.QueueItem>(queue));
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf("Dead object in setQueue.", e);
        }
    }

    /**
     * Set the title of the play queue. The UI should display this title along
     * with the play queue itself.
     * e.g. "Play Queue", "Now Playing", or an album name.
     *
     * @param title
     * 		The title of the play queue.
     */
    public void setQueueTitle(@android.annotation.Nullable
    java.lang.CharSequence title) {
        try {
            mBinder.setQueueTitle(title);
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf("Dead object in setQueueTitle.", e);
        }
    }

    /**
     * Set the style of rating used by this session. Apps trying to set the
     * rating should use this style. Must be one of the following:
     * <ul>
     * <li>{@link Rating#RATING_NONE}</li>
     * <li>{@link Rating#RATING_3_STARS}</li>
     * <li>{@link Rating#RATING_4_STARS}</li>
     * <li>{@link Rating#RATING_5_STARS}</li>
     * <li>{@link Rating#RATING_HEART}</li>
     * <li>{@link Rating#RATING_PERCENTAGE}</li>
     * <li>{@link Rating#RATING_THUMB_UP_DOWN}</li>
     * </ul>
     */
    public void setRatingType(@android.media.Rating.Style
    int type) {
        try {
            mBinder.setRatingType(type);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.session.MediaSession.TAG, "Error in setRatingType.", e);
        }
    }

    /**
     * Set some extras that can be associated with the {@link MediaSession}. No assumptions should
     * be made as to how a {@link MediaController} will handle these extras.
     * Keys should be fully qualified (e.g. com.example.MY_EXTRA) to avoid conflicts.
     *
     * @param extras
     * 		The extras associated with the {@link MediaSession}.
     */
    public void setExtras(@android.annotation.Nullable
    android.os.Bundle extras) {
        try {
            mBinder.setExtras(extras);
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf("Dead object in setExtras.", e);
        }
    }

    /**
     * Notify the system that the remote volume changed.
     *
     * @param provider
     * 		The provider that is handling volume changes.
     * @unknown 
     */
    public void notifyRemoteVolumeChanged(android.media.VolumeProvider provider) {
        synchronized(mLock) {
            if ((provider == null) || (provider != mVolumeProvider)) {
                android.util.Log.w(android.media.session.MediaSession.TAG, "Received update from stale volume provider");
                return;
            }
        }
        try {
            mBinder.setCurrentVolume(provider.getCurrentVolume());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.session.MediaSession.TAG, "Error in notifyVolumeChanged", e);
        }
    }

    /**
     * Returns the name of the package that sent the last media button, transport control, or
     * command from controllers and the system. This is only valid while in a request callback, such
     * as {@link Callback#onPlay}.
     *
     * @unknown 
     */
    public java.lang.String getCallingPackage() {
        try {
            return mBinder.getCallingPackage();
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(android.media.session.MediaSession.TAG, "Dead object in getCallingPackage.", e);
        }
        return null;
    }

    private void dispatchPrepare() {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_PREPARE);
    }

    private void dispatchPrepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_PREPARE_MEDIA_ID, mediaId, extras);
    }

    private void dispatchPrepareFromSearch(java.lang.String query, android.os.Bundle extras) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_PREPARE_SEARCH, query, extras);
    }

    private void dispatchPrepareFromUri(android.net.Uri uri, android.os.Bundle extras) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_PREPARE_URI, uri, extras);
    }

    private void dispatchPlay() {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_PLAY);
    }

    private void dispatchPlayFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_PLAY_MEDIA_ID, mediaId, extras);
    }

    private void dispatchPlayFromSearch(java.lang.String query, android.os.Bundle extras) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_PLAY_SEARCH, query, extras);
    }

    private void dispatchPlayFromUri(android.net.Uri uri, android.os.Bundle extras) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_PLAY_URI, uri, extras);
    }

    private void dispatchSkipToItem(long id) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_SKIP_TO_ITEM, id);
    }

    private void dispatchPause() {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_PAUSE);
    }

    private void dispatchStop() {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_STOP);
    }

    private void dispatchNext() {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_NEXT);
    }

    private void dispatchPrevious() {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_PREVIOUS);
    }

    private void dispatchFastForward() {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_FAST_FORWARD);
    }

    private void dispatchRewind() {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_REWIND);
    }

    private void dispatchSeekTo(long pos) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_SEEK_TO, pos);
    }

    private void dispatchRate(android.media.Rating rating) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_RATE, rating);
    }

    private void dispatchCustomAction(java.lang.String action, android.os.Bundle args) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_CUSTOM_ACTION, action, args);
    }

    private void dispatchMediaButton(android.content.Intent mediaButtonIntent) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_MEDIA_BUTTON, mediaButtonIntent);
    }

    private void dispatchAdjustVolume(int direction) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_ADJUST_VOLUME, direction);
    }

    private void dispatchSetVolumeTo(int volume) {
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_SET_VOLUME, volume);
    }

    private void postToCallback(int what) {
        postToCallback(what, null);
    }

    private void postCommand(java.lang.String command, android.os.Bundle args, android.os.ResultReceiver resultCb) {
        android.media.session.MediaSession.Command cmd = new android.media.session.MediaSession.Command(command, args, resultCb);
        postToCallback(android.media.session.MediaSession.CallbackMessageHandler.MSG_COMMAND, cmd);
    }

    private void postToCallback(int what, java.lang.Object obj) {
        postToCallback(what, obj, null);
    }

    private void postToCallback(int what, java.lang.Object obj, android.os.Bundle extras) {
        synchronized(mLock) {
            if (mCallback != null) {
                mCallback.post(what, obj, extras);
            }
        }
    }

    /**
     * Return true if this is considered an active playback state.
     *
     * @unknown 
     */
    public static boolean isActiveState(int state) {
        switch (state) {
            case android.media.session.PlaybackState.STATE_FAST_FORWARDING :
            case android.media.session.PlaybackState.STATE_REWINDING :
            case android.media.session.PlaybackState.STATE_SKIPPING_TO_PREVIOUS :
            case android.media.session.PlaybackState.STATE_SKIPPING_TO_NEXT :
            case android.media.session.PlaybackState.STATE_BUFFERING :
            case android.media.session.PlaybackState.STATE_CONNECTING :
            case android.media.session.PlaybackState.STATE_PLAYING :
                return true;
        }
        return false;
    }

    /**
     * Represents an ongoing session. This may be passed to apps by the session
     * owner to allow them to create a {@link MediaController} to communicate with
     * the session.
     */
    public static final class Token implements android.os.Parcelable {
        private android.media.session.ISessionController mBinder;

        /**
         *
         *
         * @unknown 
         */
        public Token(android.media.session.ISessionController binder) {
            mBinder = binder;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeStrongBinder(mBinder.asBinder());
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + (mBinder == null ? 0 : hashCode());
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;

            if (obj == null)
                return false;

            if (getClass() != obj.getClass())
                return false;

            android.media.session.MediaSession.Token other = ((android.media.session.MediaSession.Token) (obj));
            if (mBinder == null) {
                if (other.mBinder != null)
                    return false;

            } else
                if (!equals(other.mBinder.asBinder()))
                    return false;


            return true;
        }

        android.media.session.ISessionController getBinder() {
            return mBinder;
        }

        public static final android.os.Parcelable.Creator<android.media.session.MediaSession.Token> CREATOR = new android.os.Parcelable.Creator<android.media.session.MediaSession.Token>() {
            @java.lang.Override
            public android.media.session.MediaSession.Token createFromParcel(android.os.Parcel in) {
                return new android.media.session.MediaSession.Token(ISessionController.Stub.asInterface(in.readStrongBinder()));
            }

            @java.lang.Override
            public android.media.session.MediaSession.Token[] newArray(int size) {
                return new android.media.session.MediaSession.Token[size];
            }
        };
    }

    /**
     * Receives media buttons, transport controls, and commands from controllers
     * and the system. A callback may be set using {@link #setCallback}.
     */
    public static abstract class Callback {
        private android.media.session.MediaSession mSession;

        public Callback() {
        }

        /**
         * Called when a controller has sent a command to this session.
         * The owner of the session may handle custom commands but is not
         * required to.
         *
         * @param command
         * 		The command name.
         * @param args
         * 		Optional parameters for the command, may be null.
         * @param cb
         * 		A result receiver to which a result may be sent by the command, may be null.
         */
        public void onCommand(@android.annotation.NonNull
        java.lang.String command, @android.annotation.Nullable
        android.os.Bundle args, @android.annotation.Nullable
        android.os.ResultReceiver cb) {
        }

        /**
         * Called when a media button is pressed and this session has the
         * highest priority or a controller sends a media button event to the
         * session. The default behavior will call the relevant method if the
         * action for it was set.
         * <p>
         * The intent will be of type {@link Intent#ACTION_MEDIA_BUTTON} with a
         * KeyEvent in {@link Intent#EXTRA_KEY_EVENT}
         *
         * @param mediaButtonIntent
         * 		an intent containing the KeyEvent as an
         * 		extra
         * @return True if the event was handled, false otherwise.
         */
        public boolean onMediaButtonEvent(@android.annotation.NonNull
        android.content.Intent mediaButtonIntent) {
            if ((mSession != null) && android.content.Intent.ACTION_MEDIA_BUTTON.equals(mediaButtonIntent.getAction())) {
                android.view.KeyEvent ke = mediaButtonIntent.getParcelableExtra(android.content.Intent.EXTRA_KEY_EVENT);
                if ((ke != null) && (ke.getAction() == android.view.KeyEvent.ACTION_DOWN)) {
                    android.media.session.PlaybackState state = mSession.mPlaybackState;
                    long validActions = (state == null) ? 0 : state.getActions();
                    switch (ke.getKeyCode()) {
                        case android.view.KeyEvent.KEYCODE_MEDIA_PLAY :
                            if ((validActions & android.media.session.PlaybackState.ACTION_PLAY) != 0) {
                                onPlay();
                                return true;
                            }
                            break;
                        case android.view.KeyEvent.KEYCODE_MEDIA_PAUSE :
                            if ((validActions & android.media.session.PlaybackState.ACTION_PAUSE) != 0) {
                                onPause();
                                return true;
                            }
                            break;
                        case android.view.KeyEvent.KEYCODE_MEDIA_NEXT :
                            if ((validActions & android.media.session.PlaybackState.ACTION_SKIP_TO_NEXT) != 0) {
                                onSkipToNext();
                                return true;
                            }
                            break;
                        case android.view.KeyEvent.KEYCODE_MEDIA_PREVIOUS :
                            if ((validActions & android.media.session.PlaybackState.ACTION_SKIP_TO_PREVIOUS) != 0) {
                                onSkipToPrevious();
                                return true;
                            }
                            break;
                        case android.view.KeyEvent.KEYCODE_MEDIA_STOP :
                            if ((validActions & android.media.session.PlaybackState.ACTION_STOP) != 0) {
                                onStop();
                                return true;
                            }
                            break;
                        case android.view.KeyEvent.KEYCODE_MEDIA_FAST_FORWARD :
                            if ((validActions & android.media.session.PlaybackState.ACTION_FAST_FORWARD) != 0) {
                                onFastForward();
                                return true;
                            }
                            break;
                        case android.view.KeyEvent.KEYCODE_MEDIA_REWIND :
                            if ((validActions & android.media.session.PlaybackState.ACTION_REWIND) != 0) {
                                onRewind();
                                return true;
                            }
                            break;
                        case android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE :
                        case android.view.KeyEvent.KEYCODE_HEADSETHOOK :
                            boolean isPlaying = (state == null) ? false : state.getState() == android.media.session.PlaybackState.STATE_PLAYING;
                            boolean canPlay = (validActions & (android.media.session.PlaybackState.ACTION_PLAY_PAUSE | android.media.session.PlaybackState.ACTION_PLAY)) != 0;
                            boolean canPause = (validActions & (android.media.session.PlaybackState.ACTION_PLAY_PAUSE | android.media.session.PlaybackState.ACTION_PAUSE)) != 0;
                            if (isPlaying && canPause) {
                                onPause();
                                return true;
                            } else
                                if ((!isPlaying) && canPlay) {
                                    onPlay();
                                    return true;
                                }

                            break;
                    }
                }
            }
            return false;
        }

        /**
         * Override to handle requests to prepare playback. During the preparation, a session should
         * not hold audio focus in order to allow other sessions play seamlessly. The state of
         * playback should be updated to {@link PlaybackState#STATE_PAUSED} after the preparation is
         * done.
         */
        public void onPrepare() {
        }

        /**
         * Override to handle requests to prepare for playing a specific mediaId that was provided
         * by your app's {@link MediaBrowserService}. During the preparation, a session should not
         * hold audio focus in order to allow other sessions play seamlessly. The state of playback
         * should be updated to {@link PlaybackState#STATE_PAUSED} after the preparation is done.
         * The playback of the prepared content should start in the implementation of
         * {@link #onPlay}. Override {@link #onPlayFromMediaId} to handle requests for starting
         * playback without preparation.
         */
        public void onPrepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
        }

        /**
         * Override to handle requests to prepare playback from a search query. An empty query
         * indicates that the app may prepare any music. The implementation should attempt to make a
         * smart choice about what to play. During the preparation, a session should not hold audio
         * focus in order to allow other sessions play seamlessly. The state of playback should be
         * updated to {@link PlaybackState#STATE_PAUSED} after the preparation is done. The playback
         * of the prepared content should start in the implementation of {@link #onPlay}. Override
         * {@link #onPlayFromSearch} to handle requests for starting playback without preparation.
         */
        public void onPrepareFromSearch(java.lang.String query, android.os.Bundle extras) {
        }

        /**
         * Override to handle requests to prepare a specific media item represented by a URI.
         * During the preparation, a session should not hold audio focus in order to allow
         * other sessions play seamlessly. The state of playback should be updated to
         * {@link PlaybackState#STATE_PAUSED} after the preparation is done.
         * The playback of the prepared content should start in the implementation of
         * {@link #onPlay}. Override {@link #onPlayFromUri} to handle requests
         * for starting playback without preparation.
         */
        public void onPrepareFromUri(android.net.Uri uri, android.os.Bundle extras) {
        }

        /**
         * Override to handle requests to begin playback.
         */
        public void onPlay() {
        }

        /**
         * Override to handle requests to begin playback from a search query. An
         * empty query indicates that the app may play any music. The
         * implementation should attempt to make a smart choice about what to
         * play.
         */
        public void onPlayFromSearch(java.lang.String query, android.os.Bundle extras) {
        }

        /**
         * Override to handle requests to play a specific mediaId that was
         * provided by your app's {@link MediaBrowserService}.
         */
        public void onPlayFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
        }

        /**
         * Override to handle requests to play a specific media item represented by a URI.
         */
        public void onPlayFromUri(android.net.Uri uri, android.os.Bundle extras) {
        }

        /**
         * Override to handle requests to play an item with a given id from the
         * play queue.
         */
        public void onSkipToQueueItem(long id) {
        }

        /**
         * Override to handle requests to pause playback.
         */
        public void onPause() {
        }

        /**
         * Override to handle requests to skip to the next media item.
         */
        public void onSkipToNext() {
        }

        /**
         * Override to handle requests to skip to the previous media item.
         */
        public void onSkipToPrevious() {
        }

        /**
         * Override to handle requests to fast forward.
         */
        public void onFastForward() {
        }

        /**
         * Override to handle requests to rewind.
         */
        public void onRewind() {
        }

        /**
         * Override to handle requests to stop playback.
         */
        public void onStop() {
        }

        /**
         * Override to handle requests to seek to a specific position in ms.
         *
         * @param pos
         * 		New position to move to, in milliseconds.
         */
        public void onSeekTo(long pos) {
        }

        /**
         * Override to handle the item being rated.
         *
         * @param rating
         * 		
         */
        public void onSetRating(@android.annotation.NonNull
        android.media.Rating rating) {
        }

        /**
         * Called when a {@link MediaController} wants a {@link PlaybackState.CustomAction} to be
         * performed.
         *
         * @param action
         * 		The action that was originally sent in the
         * 		{@link PlaybackState.CustomAction}.
         * @param extras
         * 		Optional extras specified by the {@link MediaController}.
         */
        public void onCustomAction(@android.annotation.NonNull
        java.lang.String action, @android.annotation.Nullable
        android.os.Bundle extras) {
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static class CallbackStub extends android.media.session.ISessionCallback.Stub {
        private java.lang.ref.WeakReference<android.media.session.MediaSession> mMediaSession;

        public CallbackStub(android.media.session.MediaSession session) {
            mMediaSession = new java.lang.ref.WeakReference<android.media.session.MediaSession>(session);
        }

        @java.lang.Override
        public void onCommand(java.lang.String command, android.os.Bundle args, android.os.ResultReceiver cb) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.postCommand(command, args, cb);
            }
        }

        @java.lang.Override
        public void onMediaButton(android.content.Intent mediaButtonIntent, int sequenceNumber, android.os.ResultReceiver cb) {
            android.media.session.MediaSession session = mMediaSession.get();
            try {
                if (session != null) {
                    session.dispatchMediaButton(mediaButtonIntent);
                }
            } finally {
                if (cb != null) {
                    cb.send(sequenceNumber, null);
                }
            }
        }

        @java.lang.Override
        public void onPrepare() {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchPrepare();
            }
        }

        @java.lang.Override
        public void onPrepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchPrepareFromMediaId(mediaId, extras);
            }
        }

        @java.lang.Override
        public void onPrepareFromSearch(java.lang.String query, android.os.Bundle extras) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchPrepareFromSearch(query, extras);
            }
        }

        @java.lang.Override
        public void onPrepareFromUri(android.net.Uri uri, android.os.Bundle extras) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchPrepareFromUri(uri, extras);
            }
        }

        @java.lang.Override
        public void onPlay() {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchPlay();
            }
        }

        @java.lang.Override
        public void onPlayFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchPlayFromMediaId(mediaId, extras);
            }
        }

        @java.lang.Override
        public void onPlayFromSearch(java.lang.String query, android.os.Bundle extras) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchPlayFromSearch(query, extras);
            }
        }

        @java.lang.Override
        public void onPlayFromUri(android.net.Uri uri, android.os.Bundle extras) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchPlayFromUri(uri, extras);
            }
        }

        @java.lang.Override
        public void onSkipToTrack(long id) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchSkipToItem(id);
            }
        }

        @java.lang.Override
        public void onPause() {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchPause();
            }
        }

        @java.lang.Override
        public void onStop() {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchStop();
            }
        }

        @java.lang.Override
        public void onNext() {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchNext();
            }
        }

        @java.lang.Override
        public void onPrevious() {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchPrevious();
            }
        }

        @java.lang.Override
        public void onFastForward() {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchFastForward();
            }
        }

        @java.lang.Override
        public void onRewind() {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchRewind();
            }
        }

        @java.lang.Override
        public void onSeekTo(long pos) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchSeekTo(pos);
            }
        }

        @java.lang.Override
        public void onRate(android.media.Rating rating) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchRate(rating);
            }
        }

        @java.lang.Override
        public void onCustomAction(java.lang.String action, android.os.Bundle args) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchCustomAction(action, args);
            }
        }

        @java.lang.Override
        public void onAdjustVolume(int direction) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchAdjustVolume(direction);
            }
        }

        @java.lang.Override
        public void onSetVolumeTo(int value) {
            android.media.session.MediaSession session = mMediaSession.get();
            if (session != null) {
                session.dispatchSetVolumeTo(value);
            }
        }
    }

    /**
     * A single item that is part of the play queue. It contains a description
     * of the item and its id in the queue.
     */
    public static final class QueueItem implements android.os.Parcelable {
        /**
         * This id is reserved. No items can be explicitly asigned this id.
         */
        public static final int UNKNOWN_ID = -1;

        private final android.media.MediaDescription mDescription;

        private final long mId;

        /**
         * Create a new {@link MediaSession.QueueItem}.
         *
         * @param description
         * 		The {@link MediaDescription} for this item.
         * @param id
         * 		An identifier for this item. It must be unique within the
         * 		play queue and cannot be {@link #UNKNOWN_ID}.
         */
        public QueueItem(android.media.MediaDescription description, long id) {
            if (description == null) {
                throw new java.lang.IllegalArgumentException("Description cannot be null.");
            }
            if (id == android.media.session.MediaSession.QueueItem.UNKNOWN_ID) {
                throw new java.lang.IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
            mDescription = description;
            mId = id;
        }

        private QueueItem(android.os.Parcel in) {
            mDescription = android.media.MediaDescription.CREATOR.createFromParcel(in);
            mId = in.readLong();
        }

        /**
         * Get the description for this item.
         */
        public android.media.MediaDescription getDescription() {
            return mDescription;
        }

        /**
         * Get the queue id for this item.
         */
        public long getQueueId() {
            return mId;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            mDescription.writeToParcel(dest, flags);
            dest.writeLong(mId);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        public static final android.os.Parcelable.Creator<android.media.session.MediaSession.QueueItem> CREATOR = new android.os.Parcelable.Creator<android.media.session.MediaSession.QueueItem>() {
            @java.lang.Override
            public android.media.session.MediaSession.QueueItem createFromParcel(android.os.Parcel p) {
                return new android.media.session.MediaSession.QueueItem(p);
            }

            @java.lang.Override
            public android.media.session.MediaSession.QueueItem[] newArray(int size) {
                return new android.media.session.MediaSession.QueueItem[size];
            }
        };

        @java.lang.Override
        public java.lang.String toString() {
            return (((("MediaSession.QueueItem {" + "Description=") + mDescription) + ", Id=") + mId) + " }";
        }
    }

    private static final class Command {
        public final java.lang.String command;

        public final android.os.Bundle extras;

        public final android.os.ResultReceiver stub;

        public Command(java.lang.String command, android.os.Bundle extras, android.os.ResultReceiver stub) {
            this.command = command;
            this.extras = extras;
            this.stub = stub;
        }
    }

    private class CallbackMessageHandler extends android.os.Handler {
        private static final int MSG_COMMAND = 1;

        private static final int MSG_MEDIA_BUTTON = 2;

        private static final int MSG_PREPARE = 3;

        private static final int MSG_PREPARE_MEDIA_ID = 4;

        private static final int MSG_PREPARE_SEARCH = 5;

        private static final int MSG_PREPARE_URI = 6;

        private static final int MSG_PLAY = 7;

        private static final int MSG_PLAY_MEDIA_ID = 8;

        private static final int MSG_PLAY_SEARCH = 9;

        private static final int MSG_PLAY_URI = 10;

        private static final int MSG_SKIP_TO_ITEM = 11;

        private static final int MSG_PAUSE = 12;

        private static final int MSG_STOP = 13;

        private static final int MSG_NEXT = 14;

        private static final int MSG_PREVIOUS = 15;

        private static final int MSG_FAST_FORWARD = 16;

        private static final int MSG_REWIND = 17;

        private static final int MSG_SEEK_TO = 18;

        private static final int MSG_RATE = 19;

        private static final int MSG_CUSTOM_ACTION = 20;

        private static final int MSG_ADJUST_VOLUME = 21;

        private static final int MSG_SET_VOLUME = 22;

        private android.media.session.MediaSession.Callback mCallback;

        public CallbackMessageHandler(android.os.Looper looper, android.media.session.MediaSession.Callback callback) {
            super(looper, null, true);
            mCallback = callback;
        }

        public void post(int what, java.lang.Object obj, android.os.Bundle bundle) {
            android.os.Message msg = obtainMessage(what, obj);
            msg.setData(bundle);
            msg.sendToTarget();
        }

        public void post(int what, java.lang.Object obj) {
            obtainMessage(what, obj).sendToTarget();
        }

        public void post(int what) {
            post(what, null);
        }

        public void post(int what, java.lang.Object obj, int arg1) {
            obtainMessage(what, arg1, 0, obj).sendToTarget();
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.media.VolumeProvider vp;
            switch (msg.what) {
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_COMMAND :
                    android.media.session.MediaSession.Command cmd = ((android.media.session.MediaSession.Command) (msg.obj));
                    mCallback.onCommand(cmd.command, cmd.extras, cmd.stub);
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_MEDIA_BUTTON :
                    mCallback.onMediaButtonEvent(((android.content.Intent) (msg.obj)));
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_PREPARE :
                    mCallback.onPrepare();
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_PREPARE_MEDIA_ID :
                    mCallback.onPrepareFromMediaId(((java.lang.String) (msg.obj)), msg.getData());
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_PREPARE_SEARCH :
                    mCallback.onPrepareFromSearch(((java.lang.String) (msg.obj)), msg.getData());
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_PREPARE_URI :
                    mCallback.onPrepareFromUri(((android.net.Uri) (msg.obj)), msg.getData());
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_PLAY :
                    mCallback.onPlay();
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_PLAY_MEDIA_ID :
                    mCallback.onPlayFromMediaId(((java.lang.String) (msg.obj)), msg.getData());
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_PLAY_SEARCH :
                    mCallback.onPlayFromSearch(((java.lang.String) (msg.obj)), msg.getData());
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_PLAY_URI :
                    mCallback.onPlayFromUri(((android.net.Uri) (msg.obj)), msg.getData());
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_SKIP_TO_ITEM :
                    mCallback.onSkipToQueueItem(((java.lang.Long) (msg.obj)));
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_PAUSE :
                    mCallback.onPause();
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_STOP :
                    mCallback.onStop();
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_NEXT :
                    mCallback.onSkipToNext();
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_PREVIOUS :
                    mCallback.onSkipToPrevious();
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_FAST_FORWARD :
                    mCallback.onFastForward();
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_REWIND :
                    mCallback.onRewind();
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_SEEK_TO :
                    mCallback.onSeekTo(((java.lang.Long) (msg.obj)));
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_RATE :
                    mCallback.onSetRating(((android.media.Rating) (msg.obj)));
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_CUSTOM_ACTION :
                    mCallback.onCustomAction(((java.lang.String) (msg.obj)), msg.getData());
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_ADJUST_VOLUME :
                    synchronized(mLock) {
                        vp = mVolumeProvider;
                    }
                    if (vp != null) {
                        vp.onAdjustVolume(((int) (msg.obj)));
                    }
                    break;
                case android.media.session.MediaSession.CallbackMessageHandler.MSG_SET_VOLUME :
                    synchronized(mLock) {
                        vp = mVolumeProvider;
                    }
                    if (vp != null) {
                        vp.onSetVolumeTo(((int) (msg.obj)));
                    }
                    break;
            }
        }
    }
}

