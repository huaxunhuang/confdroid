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
 * create a {@link MediaControllerCompat} to interact with the session.
 * <p>
 * To receive commands, media keys, and other events a {@link Callback} must be
 * set with {@link #setCallback(Callback)}.
 * <p>
 * When an app is finished performing playback it must call {@link #release()}
 * to clean up the session and notify any controllers.
 * <p>
 * MediaSessionCompat objects are not thread safe and all calls should be made
 * from the same thread.
 * <p>
 * This is a helper for accessing features in
 * {@link android.media.session.MediaSession} introduced after API level 4 in a
 * backwards compatible fashion.
 */
public class MediaSessionCompat {
    static final java.lang.String TAG = "MediaSessionCompat";

    private final android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl mImpl;

    private final android.support.v4.media.session.MediaControllerCompat mController;

    private final java.util.ArrayList<android.support.v4.media.session.MediaSessionCompat.OnActiveChangeListener> mActiveListeners = new java.util.ArrayList<>();

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef(flag = true, value = { android.support.v4.media.session.MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS, android.support.v4.media.session.MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface SessionFlags {}

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
     * Custom action to invoke playFromUri() for the forward compatibility.
     */
    static final java.lang.String ACTION_PLAY_FROM_URI = "android.support.v4.media.session.action.PLAY_FROM_URI";

    /**
     * Custom action to invoke prepare() for the forward compatibility.
     */
    static final java.lang.String ACTION_PREPARE = "android.support.v4.media.session.action.PREPARE";

    /**
     * Custom action to invoke prepareFromMediaId() for the forward compatibility.
     */
    static final java.lang.String ACTION_PREPARE_FROM_MEDIA_ID = "android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID";

    /**
     * Custom action to invoke prepareFromSearch() for the forward compatibility.
     */
    static final java.lang.String ACTION_PREPARE_FROM_SEARCH = "android.support.v4.media.session.action.PREPARE_FROM_SEARCH";

    /**
     * Custom action to invoke prepareFromUri() for the forward compatibility.
     */
    static final java.lang.String ACTION_PREPARE_FROM_URI = "android.support.v4.media.session.action.PREPARE_FROM_URI";

    /**
     * Argument for use with {@link #ACTION_PREPARE_FROM_MEDIA_ID} indicating media id to play.
     */
    static final java.lang.String ACTION_ARGUMENT_MEDIA_ID = "android.support.v4.media.session.action.ARGUMENT_MEDIA_ID";

    /**
     * Argument for use with {@link #ACTION_PREPARE_FROM_SEARCH} indicating search query.
     */
    static final java.lang.String ACTION_ARGUMENT_QUERY = "android.support.v4.media.session.action.ARGUMENT_QUERY";

    /**
     * Argument for use with {@link #ACTION_PREPARE_FROM_URI} and {@link #ACTION_PLAY_FROM_URI}
     * indicating URI to play.
     */
    static final java.lang.String ACTION_ARGUMENT_URI = "android.support.v4.media.session.action.ARGUMENT_URI";

    /**
     * Argument for use with various actions indicating extra bundle.
     */
    static final java.lang.String ACTION_ARGUMENT_EXTRAS = "android.support.v4.media.session.action.ARGUMENT_EXTRAS";

    // Maximum size of the bitmap in dp.
    private static final int MAX_BITMAP_SIZE_IN_DP = 320;

    // Maximum size of the bitmap in px. It shouldn't be changed.
    static int sMaxBitmapSize;

    /**
     * Creates a new session. You must call {@link #release()} when finished with the session.
     * <p>
     * The session will automatically be registered with the system but will not be published
     * until {@link #setActive(boolean) setActive(true)} is called.
     * </p><p>
     * For API 20 or earlier, note that a media button receiver is required for handling
     * {@link Intent#ACTION_MEDIA_BUTTON}. This constructor will attempt to find an appropriate
     * {@link BroadcastReceiver} from your manifest. See {@link MediaButtonReceiver} for more
     * details.
     * </p>
     *
     * @param context
     * 		The context to use to create the session.
     * @param tag
     * 		A short name for debugging purposes.
     */
    public MediaSessionCompat(android.content.Context context, java.lang.String tag) {
        this(context, tag, null, null);
    }

    /**
     * Creates a new session with a specified media button receiver (a component name and/or
     * a pending intent). You must call {@link #release()} when finished with the session.
     * <p>
     * The session will automatically be registered with the system but will not be published
     * until {@link #setActive(boolean) setActive(true)} is called. Note that {@code mbrComponent}
     * and {@code mrbIntent} are only used for API 20 or earlier. If you  want to set a media button
     * receiver in API 21 or later, call {@link #setMediaButtonReceiver}.
     * </p><p>
     * For API 20 or earlier, the new session will use the given {@code mbrComponent}.
     * If null, this will attempt to find an appropriate {@link BroadcastReceiver} that handles
     * {@link Intent#ACTION_MEDIA_BUTTON} from your manifest. See {@link MediaButtonReceiver} for
     * more details.
     * </p>
     *
     * @param context
     * 		The context to use to create the session.
     * @param tag
     * 		A short name for debugging purposes.
     * @param mbrComponent
     * 		The component name for your media button receiver.
     * @param mbrIntent
     * 		The PendingIntent for your receiver component that handles
     * 		media button events. This is optional and will be used on between
     * 		{@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2} and
     * 		{@link android.os.Build.VERSION_CODES#KITKAT_WATCH} instead of the
     * 		component name.
     */
    public MediaSessionCompat(android.content.Context context, java.lang.String tag, android.content.ComponentName mbrComponent, android.app.PendingIntent mbrIntent) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("context must not be null");
        }
        if (android.text.TextUtils.isEmpty(tag)) {
            throw new java.lang.IllegalArgumentException("tag must not be null or empty");
        }
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            mImpl = new android.support.v4.media.session.MediaSessionCompat.MediaSessionImplApi21(context, tag);
        } else {
            mImpl = new android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase(context, tag, mbrComponent, mbrIntent);
        }
        mController = new android.support.v4.media.session.MediaControllerCompat(context, this);
        if (android.support.v4.media.session.MediaSessionCompat.sMaxBitmapSize == 0) {
            android.support.v4.media.session.MediaSessionCompat.sMaxBitmapSize = ((int) (android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, android.support.v4.media.session.MediaSessionCompat.MAX_BITMAP_SIZE_IN_DP, context.getResources().getDisplayMetrics())));
        }
    }

    private MediaSessionCompat(android.content.Context context, android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl impl) {
        mImpl = impl;
        mController = new android.support.v4.media.session.MediaControllerCompat(context, this);
    }

    /**
     * Add a callback to receive updates on for the MediaSession. This includes
     * media button and volume events. The caller's thread will be used to post
     * events.
     *
     * @param callback
     * 		The callback object
     */
    public void setCallback(android.support.v4.media.session.MediaSessionCompat.Callback callback) {
        setCallback(callback, null);
    }

    /**
     * Set the callback to receive updates for the MediaSession. This includes
     * media button and volume events. Set the callback to null to stop
     * receiving events.
     *
     * @param callback
     * 		The callback to receive updates on.
     * @param handler
     * 		The handler that events should be posted on.
     */
    public void setCallback(android.support.v4.media.session.MediaSessionCompat.Callback callback, android.os.Handler handler) {
        mImpl.setCallback(callback, handler != null ? handler : new android.os.Handler());
    }

    /**
     * Set an intent for launching UI for this Session. This can be used as a
     * quick link to an ongoing media screen. The intent should be for an
     * activity that may be started using
     * {@link Activity#startActivity(Intent)}.
     *
     * @param pi
     * 		The intent to launch to show UI for this Session.
     */
    public void setSessionActivity(android.app.PendingIntent pi) {
        mImpl.setSessionActivity(pi);
    }

    /**
     * Set a pending intent for your media button receiver to allow restarting
     * playback after the session has been stopped. If your app is started in
     * this way an {@link Intent#ACTION_MEDIA_BUTTON} intent will be sent via
     * the pending intent.
     * <p>
     * This method will only work on
     * {@link android.os.Build.VERSION_CODES#LOLLIPOP} and later. Earlier
     * platform versions must include the media button receiver in the
     * constructor.
     *
     * @param mbr
     * 		The {@link PendingIntent} to send the media button event to.
     */
    public void setMediaButtonReceiver(android.app.PendingIntent mbr) {
        mImpl.setMediaButtonReceiver(mbr);
    }

    /**
     * Set any flags for the session.
     *
     * @param flags
     * 		The flags to set for this session.
     */
    public void setFlags(@android.support.v4.media.session.MediaSessionCompat.SessionFlags
    int flags) {
        mImpl.setFlags(flags);
    }

    /**
     * Set the stream this session is playing on. This will affect the system's
     * volume handling for this session. If {@link #setPlaybackToRemote} was
     * previously called it will stop receiving volume commands and the system
     * will begin sending volume changes to the appropriate stream.
     * <p>
     * By default sessions are on {@link AudioManager#STREAM_MUSIC}.
     *
     * @param stream
     * 		The {@link AudioManager} stream this session is playing on.
     */
    public void setPlaybackToLocal(int stream) {
        mImpl.setPlaybackToLocal(stream);
    }

    /**
     * Configure this session to use remote volume handling. This must be called
     * to receive volume button events, otherwise the system will adjust the
     * current stream volume for this session. If {@link #setPlaybackToLocal}
     * was previously called that stream will stop receiving volume changes for
     * this session.
     * <p>
     * On platforms earlier than {@link android.os.Build.VERSION_CODES#LOLLIPOP}
     * this will only allow an app to handle volume commands sent directly to
     * the session by a {@link MediaControllerCompat}. System routing of volume
     * keys will not use the volume provider.
     *
     * @param volumeProvider
     * 		The provider that will handle volume changes. May
     * 		not be null.
     */
    public void setPlaybackToRemote(android.support.v4.media.VolumeProviderCompat volumeProvider) {
        if (volumeProvider == null) {
            throw new java.lang.IllegalArgumentException("volumeProvider may not be null!");
        }
        mImpl.setPlaybackToRemote(volumeProvider);
    }

    /**
     * Set if this session is currently active and ready to receive commands. If
     * set to false your session's controller may not be discoverable. You must
     * set the session to active before it can start receiving media button
     * events or transport commands.
     * <p>
     * On platforms earlier than
     * {@link android.os.Build.VERSION_CODES#LOLLIPOP},
     * a media button event receiver should be set via the constructor to
     * receive media button events.
     *
     * @param active
     * 		Whether this session is active or not.
     */
    public void setActive(boolean active) {
        mImpl.setActive(active);
        for (android.support.v4.media.session.MediaSessionCompat.OnActiveChangeListener listener : mActiveListeners) {
            listener.onActiveChanged();
        }
    }

    /**
     * Get the current active state of this session.
     *
     * @return True if the session is active, false otherwise.
     */
    public boolean isActive() {
        return mImpl.isActive();
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
    public void sendSessionEvent(java.lang.String event, android.os.Bundle extras) {
        if (android.text.TextUtils.isEmpty(event)) {
            throw new java.lang.IllegalArgumentException("event cannot be null or empty");
        }
        mImpl.sendSessionEvent(event, extras);
    }

    /**
     * This must be called when an app has finished performing playback. If
     * playback is expected to start again shortly the session can be left open,
     * but it must be released if your activity or service is being destroyed.
     */
    public void release() {
        mImpl.release();
    }

    /**
     * Retrieve a token object that can be used by apps to create a
     * {@link MediaControllerCompat} for interacting with this session. The
     * owner of the session is responsible for deciding how to distribute these
     * tokens.
     * <p>
     * On platform versions before
     * {@link android.os.Build.VERSION_CODES#LOLLIPOP} this token may only be
     * used within your app as there is no way to guarantee other apps are using
     * the same version of the support library.
     *
     * @return A token that can be used to create a media controller for this
    session.
     */
    public android.support.v4.media.session.MediaSessionCompat.Token getSessionToken() {
        return mImpl.getSessionToken();
    }

    /**
     * Get a controller for this session. This is a convenience method to avoid
     * having to cache your own controller in process.
     *
     * @return A controller for this session.
     */
    public android.support.v4.media.session.MediaControllerCompat getController() {
        return mController;
    }

    /**
     * Update the current playback state.
     *
     * @param state
     * 		The current state of playback
     */
    public void setPlaybackState(android.support.v4.media.session.PlaybackStateCompat state) {
        mImpl.setPlaybackState(state);
    }

    /**
     * Update the current metadata. New metadata can be created using
     * {@link android.support.v4.media.MediaMetadataCompat.Builder}. This operation may take time
     * proportional to the size of the bitmap to replace large bitmaps with a scaled down copy.
     *
     * @param metadata
     * 		The new metadata
     * @see android.support.v4.media.MediaMetadataCompat.Builder#putBitmap
     */
    public void setMetadata(android.support.v4.media.MediaMetadataCompat metadata) {
        mImpl.setMetadata(metadata);
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
    public void setQueue(java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> queue) {
        mImpl.setQueue(queue);
    }

    /**
     * Set the title of the play queue. The UI should display this title along
     * with the play queue itself. e.g. "Play Queue", "Now Playing", or an album
     * name.
     *
     * @param title
     * 		The title of the play queue.
     */
    public void setQueueTitle(java.lang.CharSequence title) {
        mImpl.setQueueTitle(title);
    }

    /**
     * Set the style of rating used by this session. Apps trying to set the
     * rating should use this style. Must be one of the following:
     * <ul>
     * <li>{@link RatingCompat#RATING_NONE}</li>
     * <li>{@link RatingCompat#RATING_3_STARS}</li>
     * <li>{@link RatingCompat#RATING_4_STARS}</li>
     * <li>{@link RatingCompat#RATING_5_STARS}</li>
     * <li>{@link RatingCompat#RATING_HEART}</li>
     * <li>{@link RatingCompat#RATING_PERCENTAGE}</li>
     * <li>{@link RatingCompat#RATING_THUMB_UP_DOWN}</li>
     * </ul>
     */
    public void setRatingType(@android.support.v4.media.RatingCompat.Style
    int type) {
        mImpl.setRatingType(type);
    }

    /**
     * Set some extras that can be associated with the
     * {@link MediaSessionCompat}. No assumptions should be made as to how a
     * {@link MediaControllerCompat} will handle these extras. Keys should be
     * fully qualified (e.g. com.example.MY_EXTRA) to avoid conflicts.
     *
     * @param extras
     * 		The extras associated with the session.
     */
    public void setExtras(android.os.Bundle extras) {
        mImpl.setExtras(extras);
    }

    /**
     * Gets the underlying framework {@link android.media.session.MediaSession}
     * object.
     * <p>
     * This method is only supported on API 21+.
     * </p>
     *
     * @return The underlying {@link android.media.session.MediaSession} object,
    or null if none.
     */
    public java.lang.Object getMediaSession() {
        return mImpl.getMediaSession();
    }

    /**
     * Gets the underlying framework {@link android.media.RemoteControlClient}
     * object.
     * <p>
     * This method is only supported on APIs 14-20. On API 21+
     * {@link #getMediaSession()} should be used instead.
     *
     * @return The underlying {@link android.media.RemoteControlClient} object,
    or null if none.
     */
    public java.lang.Object getRemoteControlClient() {
        return mImpl.getRemoteControlClient();
    }

    /**
     * Returns the name of the package that sent the last media button, transport control, or
     * command from controllers and the system. This is only valid while in a request callback, such
     * as {@link Callback#onPlay}. This method is not available and returns null on pre-N devices.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public java.lang.String getCallingPackage() {
        return mImpl.getCallingPackage();
    }

    /**
     * Adds a listener to be notified when the active status of this session
     * changes. This is primarily used by the support library and should not be
     * needed by apps.
     *
     * @param listener
     * 		The listener to add.
     */
    public void addOnActiveChangeListener(android.support.v4.media.session.MediaSessionCompat.OnActiveChangeListener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("Listener may not be null");
        }
        mActiveListeners.add(listener);
    }

    /**
     * Stops the listener from being notified when the active status of this
     * session changes.
     *
     * @param listener
     * 		The listener to remove.
     */
    public void removeOnActiveChangeListener(android.support.v4.media.session.MediaSessionCompat.OnActiveChangeListener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("Listener may not be null");
        }
        mActiveListeners.remove(listener);
    }

    /**
     * Creates an instance from a framework {@link android.media.session.MediaSession} object.
     * <p>
     * This method is only supported on API 21+. On API 20 and below, it returns null.
     * </p>
     *
     * @param context
     * 		The context to use to create the session.
     * @param mediaSession
     * 		A {@link android.media.session.MediaSession} object.
     * @return An equivalent {@link MediaSessionCompat} object, or null if none.
     * @deprecated Use {@link #fromMediaSession(Context, Object)} instead.
     */
    @java.lang.Deprecated
    public static android.support.v4.media.session.MediaSessionCompat obtain(android.content.Context context, java.lang.Object mediaSession) {
        return android.support.v4.media.session.MediaSessionCompat.fromMediaSession(context, mediaSession);
    }

    /**
     * Creates an instance from a framework {@link android.media.session.MediaSession} object.
     * <p>
     * This method is only supported on API 21+. On API 20 and below, it returns null.
     * </p>
     *
     * @param context
     * 		The context to use to create the session.
     * @param mediaSession
     * 		A {@link android.media.session.MediaSession} object.
     * @return An equivalent {@link MediaSessionCompat} object, or null if none.
     */
    public static android.support.v4.media.session.MediaSessionCompat fromMediaSession(android.content.Context context, java.lang.Object mediaSession) {
        if (((context == null) || (mediaSession == null)) || (android.os.Build.VERSION.SDK_INT < 21)) {
            return null;
        }
        return new android.support.v4.media.session.MediaSessionCompat(context, new android.support.v4.media.session.MediaSessionCompat.MediaSessionImplApi21(mediaSession));
    }

    /**
     * Receives transport controls, media buttons, and commands from controllers
     * and the system. The callback may be set using {@link #setCallback}.
     */
    public static abstract class Callback {
        final java.lang.Object mCallbackObj;

        public Callback() {
            if (android.os.Build.VERSION.SDK_INT >= 24) {
                mCallbackObj = android.support.v4.media.session.MediaSessionCompatApi24.createCallback(new android.support.v4.media.session.MediaSessionCompat.Callback.StubApi24());
            } else
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    mCallbackObj = android.support.v4.media.session.MediaSessionCompatApi23.createCallback(new android.support.v4.media.session.MediaSessionCompat.Callback.StubApi23());
                } else
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        mCallbackObj = android.support.v4.media.session.MediaSessionCompatApi21.createCallback(new android.support.v4.media.session.MediaSessionCompat.Callback.StubApi21());
                    } else {
                        mCallbackObj = null;
                    }


        }

        /**
         * Called when a controller has sent a custom command to this session.
         * The owner of the session may handle custom commands but is not
         * required to.
         *
         * @param command
         * 		The command name.
         * @param extras
         * 		Optional parameters for the command, may be null.
         * @param cb
         * 		A result receiver to which a result may be sent by the command, may be null.
         */
        public void onCommand(java.lang.String command, android.os.Bundle extras, android.os.ResultReceiver cb) {
        }

        /**
         * Override to handle media button events.
         *
         * @param mediaButtonEvent
         * 		The media button event intent.
         * @return True if the event was handled, false otherwise.
         */
        public boolean onMediaButtonEvent(android.content.Intent mediaButtonEvent) {
            return false;
        }

        /**
         * Override to handle requests to prepare playback. During the preparation, a session
         * should not hold audio focus in order to allow other session play seamlessly.
         * The state of playback should be updated to {@link PlaybackStateCompat#STATE_PAUSED}
         * after the preparation is done.
         */
        public void onPrepare() {
        }

        /**
         * Override to handle requests to prepare for playing a specific mediaId that was provided
         * by your app. During the preparation, a session should not hold audio focus in order to
         * allow other session play seamlessly. The state of playback should be updated to
         * {@link PlaybackStateCompat#STATE_PAUSED} after the preparation is done. The playback
         * of the prepared content should start in the implementation of {@link #onPlay}. Override
         * {@link #onPlayFromMediaId} to handle requests for starting playback without preparation.
         */
        public void onPrepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
        }

        /**
         * Override to handle requests to prepare playback from a search query. An
         * empty query indicates that the app may prepare any music. The
         * implementation should attempt to make a smart choice about what to
         * play. During the preparation, a session should not hold audio focus in order to allow
         * other session play seamlessly. The state of playback should be updated to
         * {@link PlaybackStateCompat#STATE_PAUSED} after the preparation is done.
         * The playback of the prepared content should start in the implementation of
         * {@link #onPlay}. Override {@link #onPlayFromSearch} to handle requests for
         * starting playback without preparation.
         */
        public void onPrepareFromSearch(java.lang.String query, android.os.Bundle extras) {
        }

        /**
         * Override to handle requests to prepare a specific media item represented by a URI.
         * During the preparation, a session should not hold audio focus in order to allow other
         * session play seamlessly. The state of playback should be updated to
         * {@link PlaybackStateCompat#STATE_PAUSED} after the preparation is done. The playback of
         * the prepared content should start in the implementation of {@link #onPlay}. Override
         * {@link #onPlayFromUri} to handle requests for starting playback without preparation.
         */
        public void onPrepareFromUri(android.net.Uri uri, android.os.Bundle extras) {
        }

        /**
         * Override to handle requests to begin playback.
         */
        public void onPlay() {
        }

        /**
         * Override to handle requests to play a specific mediaId that was
         * provided by your app.
         */
        public void onPlayFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
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
        public void onSetRating(android.support.v4.media.RatingCompat rating) {
        }

        /**
         * Called when a {@link MediaControllerCompat} wants a
         * {@link PlaybackStateCompat.CustomAction} to be performed.
         *
         * @param action
         * 		The action that was originally sent in the
         * 		{@link PlaybackStateCompat.CustomAction}.
         * @param extras
         * 		Optional extras specified by the
         * 		{@link MediaControllerCompat}.
         */
        public void onCustomAction(java.lang.String action, android.os.Bundle extras) {
        }

        private class StubApi21 implements android.support.v4.media.session.MediaSessionCompatApi21.Callback {
            StubApi21() {
            }

            @java.lang.Override
            public void onCommand(java.lang.String command, android.os.Bundle extras, android.os.ResultReceiver cb) {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onCommand(command, extras, cb);
            }

            @java.lang.Override
            public boolean onMediaButtonEvent(android.content.Intent mediaButtonIntent) {
                return android.support.v4.media.session.MediaSessionCompat.Callback.this.onMediaButtonEvent(mediaButtonIntent);
            }

            @java.lang.Override
            public void onPlay() {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onPlay();
            }

            @java.lang.Override
            public void onPlayFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onPlayFromMediaId(mediaId, extras);
            }

            @java.lang.Override
            public void onPlayFromSearch(java.lang.String search, android.os.Bundle extras) {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onPlayFromSearch(search, extras);
            }

            @java.lang.Override
            public void onSkipToQueueItem(long id) {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onSkipToQueueItem(id);
            }

            @java.lang.Override
            public void onPause() {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onPause();
            }

            @java.lang.Override
            public void onSkipToNext() {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onSkipToNext();
            }

            @java.lang.Override
            public void onSkipToPrevious() {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onSkipToPrevious();
            }

            @java.lang.Override
            public void onFastForward() {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onFastForward();
            }

            @java.lang.Override
            public void onRewind() {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onRewind();
            }

            @java.lang.Override
            public void onStop() {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onStop();
            }

            @java.lang.Override
            public void onSeekTo(long pos) {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onSeekTo(pos);
            }

            @java.lang.Override
            public void onSetRating(java.lang.Object ratingObj) {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onSetRating(android.support.v4.media.RatingCompat.fromRating(ratingObj));
            }

            @java.lang.Override
            public void onCustomAction(java.lang.String action, android.os.Bundle extras) {
                if (action.equals(android.support.v4.media.session.MediaSessionCompat.ACTION_PLAY_FROM_URI)) {
                    android.net.Uri uri = extras.getParcelable(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_URI);
                    android.os.Bundle bundle = extras.getParcelable(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_EXTRAS);
                    android.support.v4.media.session.MediaSessionCompat.Callback.this.onPlayFromUri(uri, bundle);
                } else
                    if (action.equals(android.support.v4.media.session.MediaSessionCompat.ACTION_PREPARE)) {
                        android.support.v4.media.session.MediaSessionCompat.Callback.this.onPrepare();
                    } else
                        if (action.equals(android.support.v4.media.session.MediaSessionCompat.ACTION_PREPARE_FROM_MEDIA_ID)) {
                            java.lang.String mediaId = extras.getString(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_MEDIA_ID);
                            android.os.Bundle bundle = extras.getBundle(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_EXTRAS);
                            android.support.v4.media.session.MediaSessionCompat.Callback.this.onPrepareFromMediaId(mediaId, bundle);
                        } else
                            if (action.equals(android.support.v4.media.session.MediaSessionCompat.ACTION_PREPARE_FROM_SEARCH)) {
                                java.lang.String query = extras.getString(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_QUERY);
                                android.os.Bundle bundle = extras.getBundle(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_EXTRAS);
                                android.support.v4.media.session.MediaSessionCompat.Callback.this.onPrepareFromSearch(query, bundle);
                            } else
                                if (action.equals(android.support.v4.media.session.MediaSessionCompat.ACTION_PREPARE_FROM_URI)) {
                                    android.net.Uri uri = extras.getParcelable(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_URI);
                                    android.os.Bundle bundle = extras.getBundle(android.support.v4.media.session.MediaSessionCompat.ACTION_ARGUMENT_EXTRAS);
                                    android.support.v4.media.session.MediaSessionCompat.Callback.this.onPrepareFromUri(uri, bundle);
                                } else {
                                    android.support.v4.media.session.MediaSessionCompat.Callback.this.onCustomAction(action, extras);
                                }




            }
        }

        private class StubApi23 extends android.support.v4.media.session.MediaSessionCompat.Callback.StubApi21 implements android.support.v4.media.session.MediaSessionCompatApi23.Callback {
            StubApi23() {
            }

            @java.lang.Override
            public void onPlayFromUri(android.net.Uri uri, android.os.Bundle extras) {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onPlayFromUri(uri, extras);
            }
        }

        private class StubApi24 extends android.support.v4.media.session.MediaSessionCompat.Callback.StubApi23 implements android.support.v4.media.session.MediaSessionCompatApi24.Callback {
            StubApi24() {
            }

            @java.lang.Override
            public void onPrepare() {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onPrepare();
            }

            @java.lang.Override
            public void onPrepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras) {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onPrepareFromMediaId(mediaId, extras);
            }

            @java.lang.Override
            public void onPrepareFromSearch(java.lang.String query, android.os.Bundle extras) {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onPrepareFromSearch(query, extras);
            }

            @java.lang.Override
            public void onPrepareFromUri(android.net.Uri uri, android.os.Bundle extras) {
                android.support.v4.media.session.MediaSessionCompat.Callback.this.onPrepareFromUri(uri, extras);
            }
        }
    }

    /**
     * Represents an ongoing session. This may be passed to apps by the session
     * owner to allow them to create a {@link MediaControllerCompat} to communicate with
     * the session.
     */
    public static final class Token implements android.os.Parcelable {
        private final java.lang.Object mInner;

        Token(java.lang.Object inner) {
            mInner = inner;
        }

        /**
         * Creates a compat Token from a framework
         * {@link android.media.session.MediaSession.Token} object.
         * <p>
         * This method is only supported on
         * {@link android.os.Build.VERSION_CODES#LOLLIPOP} and later.
         * </p>
         *
         * @param token
         * 		The framework token object.
         * @return A compat Token for use with {@link MediaControllerCompat}.
         */
        public static android.support.v4.media.session.MediaSessionCompat.Token fromToken(java.lang.Object token) {
            if ((token == null) || (android.os.Build.VERSION.SDK_INT < 21)) {
                return null;
            }
            return new android.support.v4.media.session.MediaSessionCompat.Token(android.support.v4.media.session.MediaSessionCompatApi21.verifyToken(token));
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                dest.writeParcelable(((android.os.Parcelable) (mInner)), flags);
            } else {
                dest.writeStrongBinder(((android.os.IBinder) (mInner)));
            }
        }

        @java.lang.Override
        public int hashCode() {
            if (mInner == null) {
                return 0;
            }
            return mInner.hashCode();
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof android.support.v4.media.session.MediaSessionCompat.Token)) {
                return false;
            }
            android.support.v4.media.session.MediaSessionCompat.Token other = ((android.support.v4.media.session.MediaSessionCompat.Token) (obj));
            if (mInner == null) {
                return other.mInner == null;
            }
            if (other.mInner == null) {
                return false;
            }
            return mInner.equals(other.mInner);
        }

        /**
         * Gets the underlying framework {@link android.media.session.MediaSession.Token} object.
         * <p>
         * This method is only supported on API 21+.
         * </p>
         *
         * @return The underlying {@link android.media.session.MediaSession.Token} object,
        or null if none.
         */
        public java.lang.Object getToken() {
            return mInner;
        }

        public static final android.os.Parcelable.Creator<android.support.v4.media.session.MediaSessionCompat.Token> CREATOR = new android.os.Parcelable.Creator<android.support.v4.media.session.MediaSessionCompat.Token>() {
            @java.lang.Override
            public android.support.v4.media.session.MediaSessionCompat.Token createFromParcel(android.os.Parcel in) {
                java.lang.Object inner;
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    inner = in.readParcelable(null);
                } else {
                    inner = in.readStrongBinder();
                }
                return new android.support.v4.media.session.MediaSessionCompat.Token(inner);
            }

            @java.lang.Override
            public android.support.v4.media.session.MediaSessionCompat.Token[] newArray(int size) {
                return new android.support.v4.media.session.MediaSessionCompat.Token[size];
            }
        };
    }

    /**
     * A single item that is part of the play queue. It contains a description
     * of the item and its id in the queue.
     */
    public static final class QueueItem implements android.os.Parcelable {
        /**
         * This id is reserved. No items can be explicitly assigned this id.
         */
        public static final int UNKNOWN_ID = -1;

        private final android.support.v4.media.MediaDescriptionCompat mDescription;

        private final long mId;

        private java.lang.Object mItem;

        /**
         * Create a new {@link MediaSessionCompat.QueueItem}.
         *
         * @param description
         * 		The {@link MediaDescriptionCompat} for this item.
         * @param id
         * 		An identifier for this item. It must be unique within the
         * 		play queue and cannot be {@link #UNKNOWN_ID}.
         */
        public QueueItem(android.support.v4.media.MediaDescriptionCompat description, long id) {
            this(null, description, id);
        }

        private QueueItem(java.lang.Object queueItem, android.support.v4.media.MediaDescriptionCompat description, long id) {
            if (description == null) {
                throw new java.lang.IllegalArgumentException("Description cannot be null.");
            }
            if (id == android.support.v4.media.session.MediaSessionCompat.QueueItem.UNKNOWN_ID) {
                throw new java.lang.IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
            mDescription = description;
            mId = id;
            mItem = queueItem;
        }

        QueueItem(android.os.Parcel in) {
            mDescription = android.support.v4.media.MediaDescriptionCompat.CREATOR.createFromParcel(in);
            mId = in.readLong();
        }

        /**
         * Get the description for this item.
         */
        public android.support.v4.media.MediaDescriptionCompat getDescription() {
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

        /**
         * Get the underlying
         * {@link android.media.session.MediaSession.QueueItem}.
         * <p>
         * On builds before {@link android.os.Build.VERSION_CODES#LOLLIPOP} null
         * is returned.
         *
         * @return The underlying
        {@link android.media.session.MediaSession.QueueItem} or null.
         */
        public java.lang.Object getQueueItem() {
            if ((mItem != null) || (android.os.Build.VERSION.SDK_INT < 21)) {
                return mItem;
            }
            mItem = android.support.v4.media.session.MediaSessionCompatApi21.QueueItem.createItem(mDescription.getMediaDescription(), mId);
            return mItem;
        }

        /**
         * Creates an instance from a framework {@link android.media.session.MediaSession.QueueItem}
         * object.
         * <p>
         * This method is only supported on API 21+. On API 20 and below, it returns null.
         * </p>
         *
         * @param queueItem
         * 		A {@link android.media.session.MediaSession.QueueItem} object.
         * @return An equivalent {@link QueueItem} object, or null if none.
         * @deprecated Use {@link #fromQueueItem(Object)} instead.
         */
        @java.lang.Deprecated
        public static android.support.v4.media.session.MediaSessionCompat.QueueItem obtain(java.lang.Object queueItem) {
            return android.support.v4.media.session.MediaSessionCompat.QueueItem.fromQueueItem(queueItem);
        }

        /**
         * Creates an instance from a framework {@link android.media.session.MediaSession.QueueItem}
         * object.
         * <p>
         * This method is only supported on API 21+. On API 20 and below, it returns null.
         * </p>
         *
         * @param queueItem
         * 		A {@link android.media.session.MediaSession.QueueItem} object.
         * @return An equivalent {@link QueueItem} object, or null if none.
         */
        public static android.support.v4.media.session.MediaSessionCompat.QueueItem fromQueueItem(java.lang.Object queueItem) {
            if ((queueItem == null) || (android.os.Build.VERSION.SDK_INT < 21)) {
                return null;
            }
            java.lang.Object descriptionObj = android.support.v4.media.session.MediaSessionCompatApi21.QueueItem.getDescription(queueItem);
            android.support.v4.media.MediaDescriptionCompat description = android.support.v4.media.MediaDescriptionCompat.fromMediaDescription(descriptionObj);
            long id = android.support.v4.media.session.MediaSessionCompatApi21.QueueItem.getQueueId(queueItem);
            return new android.support.v4.media.session.MediaSessionCompat.QueueItem(queueItem, description, id);
        }

        /**
         * Creates a list of {@link QueueItem} objects from a framework
         * {@link android.media.session.MediaSession.QueueItem} object list.
         * <p>
         * This method is only supported on API 21+. On API 20 and below, it returns null.
         * </p>
         *
         * @param itemList
         * 		A list of {@link android.media.session.MediaSession.QueueItem} objects.
         * @return An equivalent list of {@link QueueItem} objects, or null if none.
         */
        public static java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> fromQueueItemList(java.util.List<?> itemList) {
            if ((itemList == null) || (android.os.Build.VERSION.SDK_INT < 21)) {
                return null;
            }
            java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> items = new java.util.ArrayList<>();
            for (java.lang.Object itemObj : itemList) {
                items.add(android.support.v4.media.session.MediaSessionCompat.QueueItem.fromQueueItem(itemObj));
            }
            return items;
        }

        public static final android.os.Parcelable.Creator<android.support.v4.media.session.MediaSessionCompat.QueueItem> CREATOR = new android.os.Parcelable.Creator<android.support.v4.media.session.MediaSessionCompat.QueueItem>() {
            @java.lang.Override
            public android.support.v4.media.session.MediaSessionCompat.QueueItem createFromParcel(android.os.Parcel p) {
                return new android.support.v4.media.session.MediaSessionCompat.QueueItem(p);
            }

            @java.lang.Override
            public android.support.v4.media.session.MediaSessionCompat.QueueItem[] newArray(int size) {
                return new android.support.v4.media.session.MediaSessionCompat.QueueItem[size];
            }
        };

        @java.lang.Override
        public java.lang.String toString() {
            return (((("MediaSession.QueueItem {" + "Description=") + mDescription) + ", Id=") + mId) + " }";
        }
    }

    /**
     * This is a wrapper for {@link ResultReceiver} for sending over aidl
     * interfaces. The framework version was not exposed to aidls until
     * {@link android.os.Build.VERSION_CODES#LOLLIPOP}.
     */
    static final class ResultReceiverWrapper implements android.os.Parcelable {
        private android.os.ResultReceiver mResultReceiver;

        public ResultReceiverWrapper(android.os.ResultReceiver resultReceiver) {
            mResultReceiver = resultReceiver;
        }

        ResultReceiverWrapper(android.os.Parcel in) {
            mResultReceiver = android.os.ResultReceiver.CREATOR.createFromParcel(in);
        }

        public static final android.os.Parcelable.Creator<android.support.v4.media.session.MediaSessionCompat.ResultReceiverWrapper> CREATOR = new android.os.Parcelable.Creator<android.support.v4.media.session.MediaSessionCompat.ResultReceiverWrapper>() {
            @java.lang.Override
            public android.support.v4.media.session.MediaSessionCompat.ResultReceiverWrapper createFromParcel(android.os.Parcel p) {
                return new android.support.v4.media.session.MediaSessionCompat.ResultReceiverWrapper(p);
            }

            @java.lang.Override
            public android.support.v4.media.session.MediaSessionCompat.ResultReceiverWrapper[] newArray(int size) {
                return new android.support.v4.media.session.MediaSessionCompat.ResultReceiverWrapper[size];
            }
        };

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            mResultReceiver.writeToParcel(dest, flags);
        }
    }

    public interface OnActiveChangeListener {
        void onActiveChanged();
    }

    interface MediaSessionImpl {
        void setCallback(android.support.v4.media.session.MediaSessionCompat.Callback callback, android.os.Handler handler);

        void setFlags(@android.support.v4.media.session.MediaSessionCompat.SessionFlags
        int flags);

        void setPlaybackToLocal(int stream);

        void setPlaybackToRemote(android.support.v4.media.VolumeProviderCompat volumeProvider);

        void setActive(boolean active);

        boolean isActive();

        void sendSessionEvent(java.lang.String event, android.os.Bundle extras);

        void release();

        android.support.v4.media.session.MediaSessionCompat.Token getSessionToken();

        void setPlaybackState(android.support.v4.media.session.PlaybackStateCompat state);

        void setMetadata(android.support.v4.media.MediaMetadataCompat metadata);

        void setSessionActivity(android.app.PendingIntent pi);

        void setMediaButtonReceiver(android.app.PendingIntent mbr);

        void setQueue(java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> queue);

        void setQueueTitle(java.lang.CharSequence title);

        void setRatingType(@android.support.v4.media.RatingCompat.Style
        int type);

        void setExtras(android.os.Bundle extras);

        java.lang.Object getMediaSession();

        java.lang.Object getRemoteControlClient();

        java.lang.String getCallingPackage();
    }

    static class MediaSessionImplBase implements android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl {
        private final android.content.Context mContext;

        private final android.content.ComponentName mMediaButtonReceiverComponentName;

        private final android.app.PendingIntent mMediaButtonReceiverIntent;

        private final java.lang.Object mRccObj;

        private final android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MediaSessionStub mStub;

        private final android.support.v4.media.session.MediaSessionCompat.Token mToken;

        final java.lang.String mPackageName;

        final java.lang.String mTag;

        final android.media.AudioManager mAudioManager;

        final java.lang.Object mLock = new java.lang.Object();

        final android.os.RemoteCallbackList<android.support.v4.media.session.IMediaControllerCallback> mControllerCallbacks = new android.os.RemoteCallbackList();

        private android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler mHandler;

        boolean mDestroyed = false;

        private boolean mIsActive = false;

        private boolean mIsRccRegistered = false;

        private boolean mIsMbrRegistered = false;

        volatile android.support.v4.media.session.MediaSessionCompat.Callback mCallback;

        @android.support.v4.media.session.MediaSessionCompat.SessionFlags
        int mFlags;

        android.support.v4.media.MediaMetadataCompat mMetadata;

        android.support.v4.media.session.PlaybackStateCompat mState;

        android.app.PendingIntent mSessionActivity;

        java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> mQueue;

        java.lang.CharSequence mQueueTitle;

        @android.support.v4.media.RatingCompat.Style
        int mRatingType;

        android.os.Bundle mExtras;

        int mVolumeType;

        int mLocalStream;

        android.support.v4.media.VolumeProviderCompat mVolumeProvider;

        private android.support.v4.media.VolumeProviderCompat.Callback mVolumeCallback = new android.support.v4.media.VolumeProviderCompat.Callback() {
            @java.lang.Override
            public void onVolumeChanged(android.support.v4.media.VolumeProviderCompat volumeProvider) {
                if (mVolumeProvider != volumeProvider) {
                    return;
                }
                android.support.v4.media.session.ParcelableVolumeInfo info = new android.support.v4.media.session.ParcelableVolumeInfo(mVolumeType, mLocalStream, volumeProvider.getVolumeControl(), volumeProvider.getMaxVolume(), volumeProvider.getCurrentVolume());
                sendVolumeInfoChanged(info);
            }
        };

        public MediaSessionImplBase(android.content.Context context, java.lang.String tag, android.content.ComponentName mbrComponent, android.app.PendingIntent mbrIntent) {
            if (mbrComponent == null) {
                mbrComponent = android.support.v4.media.session.MediaButtonReceiver.getMediaButtonReceiverComponent(context);
                if (mbrComponent == null) {
                    android.util.Log.w(android.support.v4.media.session.MediaSessionCompat.TAG, "Couldn't find a unique registered media button receiver in the " + "given context.");
                }
            }
            if ((mbrComponent != null) && (mbrIntent == null)) {
                // construct a PendingIntent for the media button
                android.content.Intent mediaButtonIntent = new android.content.Intent(android.content.Intent.ACTION_MEDIA_BUTTON);
                // the associated intent will be handled by the component being registered
                mediaButtonIntent.setComponent(mbrComponent);
                mbrIntent = /* requestCode, ignored */
                /* flags */
                android.app.PendingIntent.getBroadcast(context, 0, mediaButtonIntent, 0);
            }
            if (mbrComponent == null) {
                throw new java.lang.IllegalArgumentException("MediaButtonReceiver component may not be null.");
            }
            mContext = context;
            mPackageName = context.getPackageName();
            mAudioManager = ((android.media.AudioManager) (context.getSystemService(android.content.Context.AUDIO_SERVICE)));
            mTag = tag;
            mMediaButtonReceiverComponentName = mbrComponent;
            mMediaButtonReceiverIntent = mbrIntent;
            mStub = new android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MediaSessionStub();
            mToken = new android.support.v4.media.session.MediaSessionCompat.Token(mStub);
            mRatingType = android.support.v4.media.RatingCompat.RATING_NONE;
            mVolumeType = android.support.v4.media.session.MediaControllerCompat.PlaybackInfo.PLAYBACK_TYPE_LOCAL;
            mLocalStream = android.media.AudioManager.STREAM_MUSIC;
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                mRccObj = android.support.v4.media.session.MediaSessionCompatApi14.createRemoteControlClient(mbrIntent);
            } else {
                mRccObj = null;
            }
        }

        @java.lang.Override
        public void setCallback(android.support.v4.media.session.MediaSessionCompat.Callback callback, android.os.Handler handler) {
            mCallback = callback;
            if (callback == null) {
                // There's nothing to unregister on API < 18 since media buttons
                // all go through the media button receiver
                if (android.os.Build.VERSION.SDK_INT >= 18) {
                    android.support.v4.media.session.MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(mRccObj, null);
                }
                if (android.os.Build.VERSION.SDK_INT >= 19) {
                    android.support.v4.media.session.MediaSessionCompatApi19.setOnMetadataUpdateListener(mRccObj, null);
                }
            } else {
                if (handler == null) {
                    handler = new android.os.Handler();
                }
                synchronized(mLock) {
                    mHandler = new android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler(handler.getLooper());
                }
                android.support.v4.media.session.MediaSessionCompatApi19.Callback cb19 = new android.support.v4.media.session.MediaSessionCompatApi19.Callback() {
                    @java.lang.Override
                    public void onSetRating(java.lang.Object ratingObj) {
                        postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_RATE, android.support.v4.media.RatingCompat.fromRating(ratingObj));
                    }

                    @java.lang.Override
                    public void onSeekTo(long pos) {
                        postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_SEEK_TO, pos);
                    }
                };
                if (android.os.Build.VERSION.SDK_INT >= 18) {
                    java.lang.Object onPositionUpdateObj = android.support.v4.media.session.MediaSessionCompatApi18.createPlaybackPositionUpdateListener(cb19);
                    android.support.v4.media.session.MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(mRccObj, onPositionUpdateObj);
                }
                if (android.os.Build.VERSION.SDK_INT >= 19) {
                    java.lang.Object onMetadataUpdateObj = android.support.v4.media.session.MediaSessionCompatApi19.createMetadataUpdateListener(cb19);
                    android.support.v4.media.session.MediaSessionCompatApi19.setOnMetadataUpdateListener(mRccObj, onMetadataUpdateObj);
                }
            }
        }

        void postToHandler(int what) {
            postToHandler(what, null);
        }

        void postToHandler(int what, java.lang.Object obj) {
            postToHandler(what, obj, null);
        }

        void postToHandler(int what, java.lang.Object obj, android.os.Bundle extras) {
            synchronized(mLock) {
                if (mHandler != null) {
                    mHandler.post(what, obj, extras);
                }
            }
        }

        @java.lang.Override
        public void setFlags(@android.support.v4.media.session.MediaSessionCompat.SessionFlags
        int flags) {
            synchronized(mLock) {
                mFlags = flags;
            }
            update();
        }

        @java.lang.Override
        public void setPlaybackToLocal(int stream) {
            if (mVolumeProvider != null) {
                mVolumeProvider.setCallback(null);
            }
            mVolumeType = android.support.v4.media.session.MediaControllerCompat.PlaybackInfo.PLAYBACK_TYPE_LOCAL;
            android.support.v4.media.session.ParcelableVolumeInfo info = new android.support.v4.media.session.ParcelableVolumeInfo(mVolumeType, mLocalStream, android.support.v4.media.VolumeProviderCompat.VOLUME_CONTROL_ABSOLUTE, mAudioManager.getStreamMaxVolume(mLocalStream), mAudioManager.getStreamVolume(mLocalStream));
            sendVolumeInfoChanged(info);
        }

        @java.lang.Override
        public void setPlaybackToRemote(android.support.v4.media.VolumeProviderCompat volumeProvider) {
            if (volumeProvider == null) {
                throw new java.lang.IllegalArgumentException("volumeProvider may not be null");
            }
            if (mVolumeProvider != null) {
                mVolumeProvider.setCallback(null);
            }
            mVolumeType = android.support.v4.media.session.MediaControllerCompat.PlaybackInfo.PLAYBACK_TYPE_REMOTE;
            mVolumeProvider = volumeProvider;
            android.support.v4.media.session.ParcelableVolumeInfo info = new android.support.v4.media.session.ParcelableVolumeInfo(mVolumeType, mLocalStream, mVolumeProvider.getVolumeControl(), mVolumeProvider.getMaxVolume(), mVolumeProvider.getCurrentVolume());
            sendVolumeInfoChanged(info);
            volumeProvider.setCallback(mVolumeCallback);
        }

        @java.lang.Override
        public void setActive(boolean active) {
            if (active == mIsActive) {
                return;
            }
            mIsActive = active;
            if (update()) {
                setMetadata(mMetadata);
                setPlaybackState(mState);
            }
        }

        @java.lang.Override
        public boolean isActive() {
            return mIsActive;
        }

        @java.lang.Override
        public void sendSessionEvent(java.lang.String event, android.os.Bundle extras) {
            sendEvent(event, extras);
        }

        @java.lang.Override
        public void release() {
            mIsActive = false;
            mDestroyed = true;
            update();
            sendSessionDestroyed();
        }

        @java.lang.Override
        public android.support.v4.media.session.MediaSessionCompat.Token getSessionToken() {
            return mToken;
        }

        @java.lang.Override
        public void setPlaybackState(android.support.v4.media.session.PlaybackStateCompat state) {
            synchronized(mLock) {
                mState = state;
            }
            sendState(state);
            if (!mIsActive) {
                // Don't set the state until after the RCC is registered
                return;
            }
            if (state == null) {
                if (android.os.Build.VERSION.SDK_INT >= 14) {
                    android.support.v4.media.session.MediaSessionCompatApi14.setState(mRccObj, android.support.v4.media.session.PlaybackStateCompat.STATE_NONE);
                    android.support.v4.media.session.MediaSessionCompatApi14.setTransportControlFlags(mRccObj, 0);
                }
            } else {
                // Set state
                if (android.os.Build.VERSION.SDK_INT >= 18) {
                    android.support.v4.media.session.MediaSessionCompatApi18.setState(mRccObj, state.getState(), state.getPosition(), state.getPlaybackSpeed(), state.getLastPositionUpdateTime());
                } else
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        android.support.v4.media.session.MediaSessionCompatApi14.setState(mRccObj, state.getState());
                    }

                // Set transport control flags
                if (android.os.Build.VERSION.SDK_INT >= 19) {
                    android.support.v4.media.session.MediaSessionCompatApi19.setTransportControlFlags(mRccObj, state.getActions());
                } else
                    if (android.os.Build.VERSION.SDK_INT >= 18) {
                        android.support.v4.media.session.MediaSessionCompatApi18.setTransportControlFlags(mRccObj, state.getActions());
                    } else
                        if (android.os.Build.VERSION.SDK_INT >= 14) {
                            android.support.v4.media.session.MediaSessionCompatApi14.setTransportControlFlags(mRccObj, state.getActions());
                        }


            }
        }

        @java.lang.Override
        public void setMetadata(android.support.v4.media.MediaMetadataCompat metadata) {
            if (metadata != null) {
                // Clones the given {@link MediaMetadataCompat}, deep-copying bitmaps in the
                // metadata if necessary. Bitmaps can be scaled down if they are large.
                metadata = new android.support.v4.media.MediaMetadataCompat.Builder(metadata, android.support.v4.media.session.MediaSessionCompat.sMaxBitmapSize).build();
            }
            synchronized(mLock) {
                mMetadata = metadata;
            }
            sendMetadata(metadata);
            if (!mIsActive) {
                // Don't set metadata until after the rcc has been registered
                return;
            }
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                android.support.v4.media.session.MediaSessionCompatApi19.setMetadata(mRccObj, metadata == null ? null : metadata.getBundle(), mState == null ? 0 : mState.getActions());
            } else
                if (android.os.Build.VERSION.SDK_INT >= 14) {
                    android.support.v4.media.session.MediaSessionCompatApi14.setMetadata(mRccObj, metadata == null ? null : metadata.getBundle());
                }

        }

        @java.lang.Override
        public void setSessionActivity(android.app.PendingIntent pi) {
            synchronized(mLock) {
                mSessionActivity = pi;
            }
        }

        @java.lang.Override
        public void setMediaButtonReceiver(android.app.PendingIntent mbr) {
            // Do nothing, changing this is not supported before API 21.
        }

        @java.lang.Override
        public void setQueue(java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> queue) {
            mQueue = queue;
            sendQueue(queue);
        }

        @java.lang.Override
        public void setQueueTitle(java.lang.CharSequence title) {
            mQueueTitle = title;
            sendQueueTitle(title);
        }

        @java.lang.Override
        public java.lang.Object getMediaSession() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getRemoteControlClient() {
            return mRccObj;
        }

        @java.lang.Override
        public java.lang.String getCallingPackage() {
            return null;
        }

        @java.lang.Override
        public void setRatingType(@android.support.v4.media.RatingCompat.Style
        int type) {
            mRatingType = type;
        }

        @java.lang.Override
        public void setExtras(android.os.Bundle extras) {
            mExtras = extras;
            sendExtras(extras);
        }

        // Registers/unregisters the RCC and MediaButtonEventReceiver as needed.
        private boolean update() {
            boolean registeredRcc = false;
            if (mIsActive) {
                // Register a MBR if it's supported, unregister it
                // if support was removed.
                if ((!mIsMbrRegistered) && ((mFlags & android.support.v4.media.session.MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS) != 0)) {
                    if (android.os.Build.VERSION.SDK_INT >= 18) {
                        android.support.v4.media.session.MediaSessionCompatApi18.registerMediaButtonEventReceiver(mContext, mMediaButtonReceiverIntent, mMediaButtonReceiverComponentName);
                    } else {
                        android.media.AudioManager am = ((android.media.AudioManager) (mContext.getSystemService(android.content.Context.AUDIO_SERVICE)));
                        am.registerMediaButtonEventReceiver(mMediaButtonReceiverComponentName);
                    }
                    mIsMbrRegistered = true;
                } else
                    if (mIsMbrRegistered && ((mFlags & android.support.v4.media.session.MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS) == 0)) {
                        if (android.os.Build.VERSION.SDK_INT >= 18) {
                            android.support.v4.media.session.MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(mContext, mMediaButtonReceiverIntent, mMediaButtonReceiverComponentName);
                        } else {
                            android.media.AudioManager am = ((android.media.AudioManager) (mContext.getSystemService(android.content.Context.AUDIO_SERVICE)));
                            am.unregisterMediaButtonEventReceiver(mMediaButtonReceiverComponentName);
                        }
                        mIsMbrRegistered = false;
                    }

                // On API 14+ register a RCC if it's supported, unregister it if
                // not.
                if (android.os.Build.VERSION.SDK_INT >= 14) {
                    if ((!mIsRccRegistered) && ((mFlags & android.support.v4.media.session.MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS) != 0)) {
                        android.support.v4.media.session.MediaSessionCompatApi14.registerRemoteControlClient(mContext, mRccObj);
                        mIsRccRegistered = true;
                        registeredRcc = true;
                    } else
                        if (mIsRccRegistered && ((mFlags & android.support.v4.media.session.MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS) == 0)) {
                            // RCC keeps the state while the system resets its state internally when
                            // we register RCC. Reset the state so that the states in RCC and the system
                            // are in sync when we re-register the RCC.
                            android.support.v4.media.session.MediaSessionCompatApi14.setState(mRccObj, android.support.v4.media.session.PlaybackStateCompat.STATE_NONE);
                            android.support.v4.media.session.MediaSessionCompatApi14.unregisterRemoteControlClient(mContext, mRccObj);
                            mIsRccRegistered = false;
                        }

                }
            } else {
                // When inactive remove any registered components.
                if (mIsMbrRegistered) {
                    if (android.os.Build.VERSION.SDK_INT >= 18) {
                        android.support.v4.media.session.MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(mContext, mMediaButtonReceiverIntent, mMediaButtonReceiverComponentName);
                    } else {
                        android.media.AudioManager am = ((android.media.AudioManager) (mContext.getSystemService(android.content.Context.AUDIO_SERVICE)));
                        am.unregisterMediaButtonEventReceiver(mMediaButtonReceiverComponentName);
                    }
                    mIsMbrRegistered = false;
                }
                if (mIsRccRegistered) {
                    // RCC keeps the state while the system resets its state internally when
                    // we register RCC. Reset the state so that the states in RCC and the system
                    // are in sync when we re-register the RCC.
                    android.support.v4.media.session.MediaSessionCompatApi14.setState(mRccObj, android.support.v4.media.session.PlaybackStateCompat.STATE_NONE);
                    android.support.v4.media.session.MediaSessionCompatApi14.unregisterRemoteControlClient(mContext, mRccObj);
                    mIsRccRegistered = false;
                }
            }
            return registeredRcc;
        }

        void adjustVolume(int direction, int flags) {
            if (mVolumeType == android.support.v4.media.session.MediaControllerCompat.PlaybackInfo.PLAYBACK_TYPE_REMOTE) {
                if (mVolumeProvider != null) {
                    mVolumeProvider.onAdjustVolume(direction);
                }
            } else {
                mAudioManager.adjustStreamVolume(mLocalStream, direction, flags);
            }
        }

        void setVolumeTo(int value, int flags) {
            if (mVolumeType == android.support.v4.media.session.MediaControllerCompat.PlaybackInfo.PLAYBACK_TYPE_REMOTE) {
                if (mVolumeProvider != null) {
                    mVolumeProvider.onSetVolumeTo(value);
                }
            } else {
                mAudioManager.setStreamVolume(mLocalStream, value, flags);
            }
        }

        android.support.v4.media.session.PlaybackStateCompat getStateWithUpdatedPosition() {
            android.support.v4.media.session.PlaybackStateCompat state;
            long duration = -1;
            synchronized(mLock) {
                state = mState;
                if ((mMetadata != null) && mMetadata.containsKey(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION)) {
                    duration = mMetadata.getLong(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION);
                }
            }
            android.support.v4.media.session.PlaybackStateCompat result = null;
            if (state != null) {
                if (((state.getState() == android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING) || (state.getState() == android.support.v4.media.session.PlaybackStateCompat.STATE_FAST_FORWARDING)) || (state.getState() == android.support.v4.media.session.PlaybackStateCompat.STATE_REWINDING)) {
                    long updateTime = state.getLastPositionUpdateTime();
                    long currentTime = android.os.SystemClock.elapsedRealtime();
                    if (updateTime > 0) {
                        long position = ((long) (state.getPlaybackSpeed() * (currentTime - updateTime))) + state.getPosition();
                        if ((duration >= 0) && (position > duration)) {
                            position = duration;
                        } else
                            if (position < 0) {
                                position = 0;
                            }

                        android.support.v4.media.session.PlaybackStateCompat.Builder builder = new android.support.v4.media.session.PlaybackStateCompat.Builder(state);
                        builder.setState(state.getState(), position, state.getPlaybackSpeed(), currentTime);
                        result = builder.build();
                    }
                }
            }
            return result == null ? state : result;
        }

        void sendVolumeInfoChanged(android.support.v4.media.session.ParcelableVolumeInfo info) {
            int size = mControllerCallbacks.beginBroadcast();
            for (int i = size - 1; i >= 0; i--) {
                android.support.v4.media.session.IMediaControllerCallback cb = mControllerCallbacks.getBroadcastItem(i);
                try {
                    cb.onVolumeInfoChanged(info);
                } catch (android.os.RemoteException e) {
                }
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void sendSessionDestroyed() {
            int size = mControllerCallbacks.beginBroadcast();
            for (int i = size - 1; i >= 0; i--) {
                android.support.v4.media.session.IMediaControllerCallback cb = mControllerCallbacks.getBroadcastItem(i);
                try {
                    cb.onSessionDestroyed();
                } catch (android.os.RemoteException e) {
                }
            }
            mControllerCallbacks.finishBroadcast();
            mControllerCallbacks.kill();
        }

        private void sendEvent(java.lang.String event, android.os.Bundle extras) {
            int size = mControllerCallbacks.beginBroadcast();
            for (int i = size - 1; i >= 0; i--) {
                android.support.v4.media.session.IMediaControllerCallback cb = mControllerCallbacks.getBroadcastItem(i);
                try {
                    cb.onEvent(event, extras);
                } catch (android.os.RemoteException e) {
                }
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void sendState(android.support.v4.media.session.PlaybackStateCompat state) {
            int size = mControllerCallbacks.beginBroadcast();
            for (int i = size - 1; i >= 0; i--) {
                android.support.v4.media.session.IMediaControllerCallback cb = mControllerCallbacks.getBroadcastItem(i);
                try {
                    cb.onPlaybackStateChanged(state);
                } catch (android.os.RemoteException e) {
                }
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void sendMetadata(android.support.v4.media.MediaMetadataCompat metadata) {
            int size = mControllerCallbacks.beginBroadcast();
            for (int i = size - 1; i >= 0; i--) {
                android.support.v4.media.session.IMediaControllerCallback cb = mControllerCallbacks.getBroadcastItem(i);
                try {
                    cb.onMetadataChanged(metadata);
                } catch (android.os.RemoteException e) {
                }
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void sendQueue(java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> queue) {
            int size = mControllerCallbacks.beginBroadcast();
            for (int i = size - 1; i >= 0; i--) {
                android.support.v4.media.session.IMediaControllerCallback cb = mControllerCallbacks.getBroadcastItem(i);
                try {
                    cb.onQueueChanged(queue);
                } catch (android.os.RemoteException e) {
                }
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void sendQueueTitle(java.lang.CharSequence queueTitle) {
            int size = mControllerCallbacks.beginBroadcast();
            for (int i = size - 1; i >= 0; i--) {
                android.support.v4.media.session.IMediaControllerCallback cb = mControllerCallbacks.getBroadcastItem(i);
                try {
                    cb.onQueueTitleChanged(queueTitle);
                } catch (android.os.RemoteException e) {
                }
            }
            mControllerCallbacks.finishBroadcast();
        }

        private void sendExtras(android.os.Bundle extras) {
            int size = mControllerCallbacks.beginBroadcast();
            for (int i = size - 1; i >= 0; i--) {
                android.support.v4.media.session.IMediaControllerCallback cb = mControllerCallbacks.getBroadcastItem(i);
                try {
                    cb.onExtrasChanged(extras);
                } catch (android.os.RemoteException e) {
                }
            }
            mControllerCallbacks.finishBroadcast();
        }

        class MediaSessionStub extends android.support.v4.media.session.IMediaSession.Stub {
            @java.lang.Override
            public void sendCommand(java.lang.String command, android.os.Bundle args, android.support.v4.media.session.MediaSessionCompat.ResultReceiverWrapper cb) {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_COMMAND, new android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.Command(command, args, cb.mResultReceiver));
            }

            @java.lang.Override
            public boolean sendMediaButton(android.view.KeyEvent mediaButton) {
                boolean handlesMediaButtons = (mFlags & android.support.v4.media.session.MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS) != 0;
                if (handlesMediaButtons) {
                    postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_MEDIA_BUTTON, mediaButton);
                }
                return handlesMediaButtons;
            }

            @java.lang.Override
            public void registerCallbackListener(android.support.v4.media.session.IMediaControllerCallback cb) {
                // If this session is already destroyed tell the caller and
                // don't add them.
                if (mDestroyed) {
                    try {
                        cb.onSessionDestroyed();
                    } catch (java.lang.Exception e) {
                        // ignored
                    }
                    return;
                }
                mControllerCallbacks.register(cb);
            }

            @java.lang.Override
            public void unregisterCallbackListener(android.support.v4.media.session.IMediaControllerCallback cb) {
                mControllerCallbacks.unregister(cb);
            }

            @java.lang.Override
            public java.lang.String getPackageName() {
                // mPackageName is final so doesn't need synchronize block
                return mPackageName;
            }

            @java.lang.Override
            public java.lang.String getTag() {
                // mTag is final so doesn't need synchronize block
                return mTag;
            }

            @java.lang.Override
            public android.app.PendingIntent getLaunchPendingIntent() {
                synchronized(mLock) {
                    return mSessionActivity;
                }
            }

            @java.lang.Override
            @android.support.v4.media.session.MediaSessionCompat.SessionFlags
            public long getFlags() {
                synchronized(mLock) {
                    return mFlags;
                }
            }

            @java.lang.Override
            public android.support.v4.media.session.ParcelableVolumeInfo getVolumeAttributes() {
                int controlType;
                int max;
                int current;
                int stream;
                int volumeType;
                synchronized(mLock) {
                    volumeType = mVolumeType;
                    stream = mLocalStream;
                    android.support.v4.media.VolumeProviderCompat vp = mVolumeProvider;
                    if (volumeType == android.support.v4.media.session.MediaControllerCompat.PlaybackInfo.PLAYBACK_TYPE_REMOTE) {
                        controlType = vp.getVolumeControl();
                        max = vp.getMaxVolume();
                        current = vp.getCurrentVolume();
                    } else {
                        controlType = android.support.v4.media.VolumeProviderCompat.VOLUME_CONTROL_ABSOLUTE;
                        max = mAudioManager.getStreamMaxVolume(stream);
                        current = mAudioManager.getStreamVolume(stream);
                    }
                }
                return new android.support.v4.media.session.ParcelableVolumeInfo(volumeType, stream, controlType, max, current);
            }

            @java.lang.Override
            public void adjustVolume(int direction, int flags, java.lang.String packageName) {
                android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.this.adjustVolume(direction, flags);
            }

            @java.lang.Override
            public void setVolumeTo(int value, int flags, java.lang.String packageName) {
                android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.this.setVolumeTo(value, flags);
            }

            @java.lang.Override
            public void prepare() throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PREPARE);
            }

            @java.lang.Override
            public void prepareFromMediaId(java.lang.String mediaId, android.os.Bundle extras) throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PREPARE_MEDIA_ID, mediaId, extras);
            }

            @java.lang.Override
            public void prepareFromSearch(java.lang.String query, android.os.Bundle extras) throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PREPARE_SEARCH, query, extras);
            }

            @java.lang.Override
            public void prepareFromUri(android.net.Uri uri, android.os.Bundle extras) throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PREPARE_URI, uri, extras);
            }

            @java.lang.Override
            public void play() throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PLAY);
            }

            @java.lang.Override
            public void playFromMediaId(java.lang.String mediaId, android.os.Bundle extras) throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PLAY_MEDIA_ID, mediaId, extras);
            }

            @java.lang.Override
            public void playFromSearch(java.lang.String query, android.os.Bundle extras) throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PLAY_SEARCH, query, extras);
            }

            @java.lang.Override
            public void playFromUri(android.net.Uri uri, android.os.Bundle extras) throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PLAY_URI, uri, extras);
            }

            @java.lang.Override
            public void skipToQueueItem(long id) {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_SKIP_TO_ITEM, id);
            }

            @java.lang.Override
            public void pause() throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PAUSE);
            }

            @java.lang.Override
            public void stop() throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_STOP);
            }

            @java.lang.Override
            public void next() throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_NEXT);
            }

            @java.lang.Override
            public void previous() throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PREVIOUS);
            }

            @java.lang.Override
            public void fastForward() throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_FAST_FORWARD);
            }

            @java.lang.Override
            public void rewind() throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_REWIND);
            }

            @java.lang.Override
            public void seekTo(long pos) throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_SEEK_TO, pos);
            }

            @java.lang.Override
            public void rate(android.support.v4.media.RatingCompat rating) throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_RATE, rating);
            }

            @java.lang.Override
            public void sendCustomAction(java.lang.String action, android.os.Bundle args) throws android.os.RemoteException {
                postToHandler(android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_CUSTOM_ACTION, action, args);
            }

            @java.lang.Override
            public android.support.v4.media.MediaMetadataCompat getMetadata() {
                return mMetadata;
            }

            @java.lang.Override
            public android.support.v4.media.session.PlaybackStateCompat getPlaybackState() {
                return getStateWithUpdatedPosition();
            }

            @java.lang.Override
            public java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> getQueue() {
                synchronized(mLock) {
                    return mQueue;
                }
            }

            @java.lang.Override
            public java.lang.CharSequence getQueueTitle() {
                return mQueueTitle;
            }

            @java.lang.Override
            public android.os.Bundle getExtras() {
                synchronized(mLock) {
                    return mExtras;
                }
            }

            @java.lang.Override
            @android.support.v4.media.RatingCompat.Style
            public int getRatingType() {
                return mRatingType;
            }

            @java.lang.Override
            public boolean isTransportControlEnabled() {
                return (mFlags & android.support.v4.media.session.MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS) != 0;
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

        private class MessageHandler extends android.os.Handler {
            private static final int MSG_COMMAND = 1;

            private static final int MSG_ADJUST_VOLUME = 2;

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

            private static final int MSG_MEDIA_BUTTON = 21;

            private static final int MSG_SET_VOLUME = 22;

            // KeyEvent constants only available on API 11+
            private static final int KEYCODE_MEDIA_PAUSE = 127;

            private static final int KEYCODE_MEDIA_PLAY = 126;

            public MessageHandler(android.os.Looper looper) {
                super(looper);
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
                android.support.v4.media.session.MediaSessionCompat.Callback cb = android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.this.mCallback;
                if (cb == null) {
                    return;
                }
                switch (msg.what) {
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_COMMAND :
                        android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.Command cmd = ((android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.Command) (msg.obj));
                        cb.onCommand(cmd.command, cmd.extras, cmd.stub);
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_MEDIA_BUTTON :
                        android.view.KeyEvent keyEvent = ((android.view.KeyEvent) (msg.obj));
                        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_MEDIA_BUTTON);
                        intent.putExtra(android.content.Intent.EXTRA_KEY_EVENT, keyEvent);
                        // Let the Callback handle events first before using the default behavior
                        if (!cb.onMediaButtonEvent(intent)) {
                            onMediaButtonEvent(keyEvent, cb);
                        }
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PREPARE :
                        cb.onPrepare();
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PREPARE_MEDIA_ID :
                        cb.onPrepareFromMediaId(((java.lang.String) (msg.obj)), msg.getData());
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PREPARE_SEARCH :
                        cb.onPrepareFromSearch(((java.lang.String) (msg.obj)), msg.getData());
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PREPARE_URI :
                        cb.onPrepareFromUri(((android.net.Uri) (msg.obj)), msg.getData());
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PLAY :
                        cb.onPlay();
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PLAY_MEDIA_ID :
                        cb.onPlayFromMediaId(((java.lang.String) (msg.obj)), msg.getData());
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PLAY_SEARCH :
                        cb.onPlayFromSearch(((java.lang.String) (msg.obj)), msg.getData());
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PLAY_URI :
                        cb.onPlayFromUri(((android.net.Uri) (msg.obj)), msg.getData());
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_SKIP_TO_ITEM :
                        cb.onSkipToQueueItem(((java.lang.Long) (msg.obj)));
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PAUSE :
                        cb.onPause();
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_STOP :
                        cb.onStop();
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_NEXT :
                        cb.onSkipToNext();
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_PREVIOUS :
                        cb.onSkipToPrevious();
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_FAST_FORWARD :
                        cb.onFastForward();
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_REWIND :
                        cb.onRewind();
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_SEEK_TO :
                        cb.onSeekTo(((java.lang.Long) (msg.obj)));
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_RATE :
                        cb.onSetRating(((android.support.v4.media.RatingCompat) (msg.obj)));
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_CUSTOM_ACTION :
                        cb.onCustomAction(((java.lang.String) (msg.obj)), msg.getData());
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_ADJUST_VOLUME :
                        adjustVolume(((int) (msg.obj)), 0);
                        break;
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.MSG_SET_VOLUME :
                        setVolumeTo(((int) (msg.obj)), 0);
                        break;
                }
            }

            private void onMediaButtonEvent(android.view.KeyEvent ke, android.support.v4.media.session.MediaSessionCompat.Callback cb) {
                if ((ke == null) || (ke.getAction() != android.view.KeyEvent.ACTION_DOWN)) {
                    return;
                }
                long validActions = (mState == null) ? 0 : mState.getActions();
                switch (ke.getKeyCode()) {
                    // Note KeyEvent.KEYCODE_MEDIA_PLAY is API 11+
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.KEYCODE_MEDIA_PLAY :
                        if ((validActions & android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY) != 0) {
                            cb.onPlay();
                        }
                        break;
                        // Note KeyEvent.KEYCODE_MEDIA_PAUSE is API 11+
                    case android.support.v4.media.session.MediaSessionCompat.MediaSessionImplBase.MessageHandler.KEYCODE_MEDIA_PAUSE :
                        if ((validActions & android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE) != 0) {
                            cb.onPause();
                        }
                        break;
                    case android.view.KeyEvent.KEYCODE_MEDIA_NEXT :
                        if ((validActions & android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
                            cb.onSkipToNext();
                        }
                        break;
                    case android.view.KeyEvent.KEYCODE_MEDIA_PREVIOUS :
                        if ((validActions & android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0) {
                            cb.onSkipToPrevious();
                        }
                        break;
                    case android.view.KeyEvent.KEYCODE_MEDIA_STOP :
                        if ((validActions & android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP) != 0) {
                            cb.onStop();
                        }
                        break;
                    case android.view.KeyEvent.KEYCODE_MEDIA_FAST_FORWARD :
                        if ((validActions & android.support.v4.media.session.PlaybackStateCompat.ACTION_FAST_FORWARD) != 0) {
                            cb.onFastForward();
                        }
                        break;
                    case android.view.KeyEvent.KEYCODE_MEDIA_REWIND :
                        if ((validActions & android.support.v4.media.session.PlaybackStateCompat.ACTION_REWIND) != 0) {
                            cb.onRewind();
                        }
                        break;
                    case android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE :
                    case android.view.KeyEvent.KEYCODE_HEADSETHOOK :
                        boolean isPlaying = (mState != null) && (mState.getState() == android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING);
                        boolean canPlay = (validActions & (android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE | android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY)) != 0;
                        boolean canPause = (validActions & (android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE | android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE)) != 0;
                        if (isPlaying && canPause) {
                            cb.onPause();
                        } else
                            if ((!isPlaying) && canPlay) {
                                cb.onPlay();
                            }

                        break;
                }
            }
        }
    }

    static class MediaSessionImplApi21 implements android.support.v4.media.session.MediaSessionCompat.MediaSessionImpl {
        private final java.lang.Object mSessionObj;

        private final android.support.v4.media.session.MediaSessionCompat.Token mToken;

        private android.app.PendingIntent mMediaButtonIntent;

        public MediaSessionImplApi21(android.content.Context context, java.lang.String tag) {
            mSessionObj = android.support.v4.media.session.MediaSessionCompatApi21.createSession(context, tag);
            mToken = new android.support.v4.media.session.MediaSessionCompat.Token(android.support.v4.media.session.MediaSessionCompatApi21.getSessionToken(mSessionObj));
        }

        public MediaSessionImplApi21(java.lang.Object mediaSession) {
            mSessionObj = android.support.v4.media.session.MediaSessionCompatApi21.verifySession(mediaSession);
            mToken = new android.support.v4.media.session.MediaSessionCompat.Token(android.support.v4.media.session.MediaSessionCompatApi21.getSessionToken(mSessionObj));
        }

        @java.lang.Override
        public void setCallback(android.support.v4.media.session.MediaSessionCompat.Callback callback, android.os.Handler handler) {
            android.support.v4.media.session.MediaSessionCompatApi21.setCallback(mSessionObj, callback == null ? null : callback.mCallbackObj, handler);
        }

        @java.lang.Override
        public void setFlags(@android.support.v4.media.session.MediaSessionCompat.SessionFlags
        int flags) {
            android.support.v4.media.session.MediaSessionCompatApi21.setFlags(mSessionObj, flags);
        }

        @java.lang.Override
        public void setPlaybackToLocal(int stream) {
            android.support.v4.media.session.MediaSessionCompatApi21.setPlaybackToLocal(mSessionObj, stream);
        }

        @java.lang.Override
        public void setPlaybackToRemote(android.support.v4.media.VolumeProviderCompat volumeProvider) {
            android.support.v4.media.session.MediaSessionCompatApi21.setPlaybackToRemote(mSessionObj, volumeProvider.getVolumeProvider());
        }

        @java.lang.Override
        public void setActive(boolean active) {
            android.support.v4.media.session.MediaSessionCompatApi21.setActive(mSessionObj, active);
        }

        @java.lang.Override
        public boolean isActive() {
            return android.support.v4.media.session.MediaSessionCompatApi21.isActive(mSessionObj);
        }

        @java.lang.Override
        public void sendSessionEvent(java.lang.String event, android.os.Bundle extras) {
            android.support.v4.media.session.MediaSessionCompatApi21.sendSessionEvent(mSessionObj, event, extras);
        }

        @java.lang.Override
        public void release() {
            android.support.v4.media.session.MediaSessionCompatApi21.release(mSessionObj);
        }

        @java.lang.Override
        public android.support.v4.media.session.MediaSessionCompat.Token getSessionToken() {
            return mToken;
        }

        @java.lang.Override
        public void setPlaybackState(android.support.v4.media.session.PlaybackStateCompat state) {
            android.support.v4.media.session.MediaSessionCompatApi21.setPlaybackState(mSessionObj, state == null ? null : state.getPlaybackState());
        }

        @java.lang.Override
        public void setMetadata(android.support.v4.media.MediaMetadataCompat metadata) {
            android.support.v4.media.session.MediaSessionCompatApi21.setMetadata(mSessionObj, metadata == null ? null : metadata.getMediaMetadata());
        }

        @java.lang.Override
        public void setSessionActivity(android.app.PendingIntent pi) {
            android.support.v4.media.session.MediaSessionCompatApi21.setSessionActivity(mSessionObj, pi);
        }

        @java.lang.Override
        public void setMediaButtonReceiver(android.app.PendingIntent mbr) {
            mMediaButtonIntent = mbr;
            android.support.v4.media.session.MediaSessionCompatApi21.setMediaButtonReceiver(mSessionObj, mbr);
        }

        @java.lang.Override
        public void setQueue(java.util.List<android.support.v4.media.session.MediaSessionCompat.QueueItem> queue) {
            java.util.List<java.lang.Object> queueObjs = null;
            if (queue != null) {
                queueObjs = new java.util.ArrayList<>();
                for (android.support.v4.media.session.MediaSessionCompat.QueueItem item : queue) {
                    queueObjs.add(item.getQueueItem());
                }
            }
            android.support.v4.media.session.MediaSessionCompatApi21.setQueue(mSessionObj, queueObjs);
        }

        @java.lang.Override
        public void setQueueTitle(java.lang.CharSequence title) {
            android.support.v4.media.session.MediaSessionCompatApi21.setQueueTitle(mSessionObj, title);
        }

        @java.lang.Override
        public void setRatingType(@android.support.v4.media.RatingCompat.Style
        int type) {
            if (android.os.Build.VERSION.SDK_INT < 22) {
                // TODO figure out 21 implementation
            } else {
                android.support.v4.media.session.MediaSessionCompatApi22.setRatingType(mSessionObj, type);
            }
        }

        @java.lang.Override
        public void setExtras(android.os.Bundle extras) {
            android.support.v4.media.session.MediaSessionCompatApi21.setExtras(mSessionObj, extras);
        }

        @java.lang.Override
        public java.lang.Object getMediaSession() {
            return mSessionObj;
        }

        @java.lang.Override
        public java.lang.Object getRemoteControlClient() {
            return null;
        }

        @java.lang.Override
        public java.lang.String getCallingPackage() {
            if (android.os.Build.VERSION.SDK_INT < 24) {
                return null;
            } else {
                return android.support.v4.media.session.MediaSessionCompatApi24.getCallingPackage(mSessionObj);
            }
        }
    }
}

