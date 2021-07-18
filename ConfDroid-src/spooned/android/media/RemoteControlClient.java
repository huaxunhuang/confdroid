/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.media;


/**
 * RemoteControlClient enables exposing information meant to be consumed by remote controls
 * capable of displaying metadata, artwork and media transport control buttons.
 *
 * <p>A remote control client object is associated with a media button event receiver. This
 * event receiver must have been previously registered with
 * {@link AudioManager#registerMediaButtonEventReceiver(ComponentName)} before the
 * RemoteControlClient can be registered through
 * {@link AudioManager#registerRemoteControlClient(RemoteControlClient)}.
 *
 * <p>Here is an example of creating a RemoteControlClient instance after registering a media
 * button event receiver:
 * <pre>ComponentName myEventReceiver = new ComponentName(getPackageName(), MyRemoteControlEventReceiver.class.getName());
 * AudioManager myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
 * myAudioManager.registerMediaButtonEventReceiver(myEventReceiver);
 * // build the PendingIntent for the remote control client
 * Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
 * mediaButtonIntent.setComponent(myEventReceiver);
 * PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, 0);
 * // create and register the remote control client
 * RemoteControlClient myRemoteControlClient = new RemoteControlClient(mediaPendingIntent);
 * myAudioManager.registerRemoteControlClient(myRemoteControlClient);</pre>
 *
 * @deprecated Use {@link MediaSession} instead.
 */
@java.lang.Deprecated
public class RemoteControlClient {
    private static final java.lang.String TAG = "RemoteControlClient";

    private static final boolean DEBUG = false;

    /**
     * Playback state of a RemoteControlClient which is stopped.
     *
     * @see #setPlaybackState(int)
     */
    public static final int PLAYSTATE_STOPPED = 1;

    /**
     * Playback state of a RemoteControlClient which is paused.
     *
     * @see #setPlaybackState(int)
     */
    public static final int PLAYSTATE_PAUSED = 2;

    /**
     * Playback state of a RemoteControlClient which is playing media.
     *
     * @see #setPlaybackState(int)
     */
    public static final int PLAYSTATE_PLAYING = 3;

    /**
     * Playback state of a RemoteControlClient which is fast forwarding in the media
     *    it is currently playing.
     *
     * @see #setPlaybackState(int)
     */
    public static final int PLAYSTATE_FAST_FORWARDING = 4;

    /**
     * Playback state of a RemoteControlClient which is fast rewinding in the media
     *    it is currently playing.
     *
     * @see #setPlaybackState(int)
     */
    public static final int PLAYSTATE_REWINDING = 5;

    /**
     * Playback state of a RemoteControlClient which is skipping to the next
     *    logical chapter (such as a song in a playlist) in the media it is currently playing.
     *
     * @see #setPlaybackState(int)
     */
    public static final int PLAYSTATE_SKIPPING_FORWARDS = 6;

    /**
     * Playback state of a RemoteControlClient which is skipping back to the previous
     *    logical chapter (such as a song in a playlist) in the media it is currently playing.
     *
     * @see #setPlaybackState(int)
     */
    public static final int PLAYSTATE_SKIPPING_BACKWARDS = 7;

    /**
     * Playback state of a RemoteControlClient which is buffering data to play before it can
     *    start or resume playback.
     *
     * @see #setPlaybackState(int)
     */
    public static final int PLAYSTATE_BUFFERING = 8;

    /**
     * Playback state of a RemoteControlClient which cannot perform any playback related
     *    operation because of an internal error. Examples of such situations are no network
     *    connectivity when attempting to stream data from a server, or expired user credentials
     *    when trying to play subscription-based content.
     *
     * @see #setPlaybackState(int)
     */
    public static final int PLAYSTATE_ERROR = 9;

    /**
     *
     *
     * @unknown The value of a playback state when none has been declared.
    Intentionally hidden as an application shouldn't set such a playback state value.
     */
    public static final int PLAYSTATE_NONE = 0;

    /**
     *
     *
     * @unknown The default playback type, "local", indicating the presentation of the media is happening on
    the same device (e.g. a phone, a tablet) as where it is controlled from.
     */
    public static final int PLAYBACK_TYPE_LOCAL = 0;

    /**
     *
     *
     * @unknown A playback type indicating the presentation of the media is happening on
    a different device (i.e. the remote device) than where it is controlled from.
     */
    public static final int PLAYBACK_TYPE_REMOTE = 1;

    private static final int PLAYBACK_TYPE_MIN = android.media.RemoteControlClient.PLAYBACK_TYPE_LOCAL;

    private static final int PLAYBACK_TYPE_MAX = android.media.RemoteControlClient.PLAYBACK_TYPE_REMOTE;

    /**
     *
     *
     * @unknown Playback information indicating the playback volume is fixed, i.e. it cannot be controlled
    from this object. An example of fixed playback volume is a remote player, playing over HDMI
    where the user prefer to control the volume on the HDMI sink, rather than attenuate at the
    source.
     * @see #PLAYBACKINFO_VOLUME_HANDLING.
     */
    public static final int PLAYBACK_VOLUME_FIXED = 0;

    /**
     *
     *
     * @unknown Playback information indicating the playback volume is variable and can be controlled from
    this object.
     * @see #PLAYBACKINFO_VOLUME_HANDLING.
     */
    public static final int PLAYBACK_VOLUME_VARIABLE = 1;

    /**
     *
     *
     * @unknown (to be un-hidden)
    The playback information value indicating the value of a given information type is invalid.
     * @see #PLAYBACKINFO_VOLUME_HANDLING.
     */
    public static final int PLAYBACKINFO_INVALID_VALUE = java.lang.Integer.MIN_VALUE;

    /**
     *
     *
     * @unknown An unknown or invalid playback position value.
     */
    public static final long PLAYBACK_POSITION_INVALID = -1;

    /**
     *
     *
     * @unknown An invalid playback position value associated with the use of {@link #setPlaybackState(int)}
    used to indicate that playback position will remain unknown.
     */
    public static final long PLAYBACK_POSITION_ALWAYS_UNKNOWN = 0x8019771980198300L;

    /**
     *
     *
     * @unknown The default playback speed, 1x.
     */
    public static final float PLAYBACK_SPEED_1X = 1.0F;

    // ==========================================
    // Public keys for playback information
    /**
     *
     *
     * @unknown Playback information that defines the type of playback associated with this
    RemoteControlClient. See {@link #PLAYBACK_TYPE_LOCAL} and {@link #PLAYBACK_TYPE_REMOTE}.
     */
    public static final int PLAYBACKINFO_PLAYBACK_TYPE = 1;

    /**
     *
     *
     * @unknown Playback information that defines at what volume the playback associated with this
    RemoteControlClient is performed. This information is only used when the playback type is not
    local (see {@link #PLAYBACKINFO_PLAYBACK_TYPE}).
     */
    public static final int PLAYBACKINFO_VOLUME = 2;

    /**
     *
     *
     * @unknown Playback information that defines the maximum volume volume value that is supported
    by the playback associated with this RemoteControlClient. This information is only used
    when the playback type is not local (see {@link #PLAYBACKINFO_PLAYBACK_TYPE}).
     */
    public static final int PLAYBACKINFO_VOLUME_MAX = 3;

    /**
     *
     *
     * @unknown Playback information that defines how volume is handled for the presentation of the media.
     * @see #PLAYBACK_VOLUME_FIXED
     * @see #PLAYBACK_VOLUME_VARIABLE
     */
    public static final int PLAYBACKINFO_VOLUME_HANDLING = 4;

    /**
     *
     *
     * @unknown Playback information that defines over what stream type the media is presented.
     */
    public static final int PLAYBACKINFO_USES_STREAM = 5;

    // ==========================================
    // Public flags for the supported transport control capabilities
    /**
     * Flag indicating a RemoteControlClient makes use of the "previous" media key.
     *
     * @see #setTransportControlFlags(int)
     * @see android.view.KeyEvent#KEYCODE_MEDIA_PREVIOUS
     */
    public static final int FLAG_KEY_MEDIA_PREVIOUS = 1 << 0;

    /**
     * Flag indicating a RemoteControlClient makes use of the "rewind" media key.
     *
     * @see #setTransportControlFlags(int)
     * @see android.view.KeyEvent#KEYCODE_MEDIA_REWIND
     */
    public static final int FLAG_KEY_MEDIA_REWIND = 1 << 1;

    /**
     * Flag indicating a RemoteControlClient makes use of the "play" media key.
     *
     * @see #setTransportControlFlags(int)
     * @see android.view.KeyEvent#KEYCODE_MEDIA_PLAY
     */
    public static final int FLAG_KEY_MEDIA_PLAY = 1 << 2;

    /**
     * Flag indicating a RemoteControlClient makes use of the "play/pause" media key.
     *
     * @see #setTransportControlFlags(int)
     * @see android.view.KeyEvent#KEYCODE_MEDIA_PLAY_PAUSE
     */
    public static final int FLAG_KEY_MEDIA_PLAY_PAUSE = 1 << 3;

    /**
     * Flag indicating a RemoteControlClient makes use of the "pause" media key.
     *
     * @see #setTransportControlFlags(int)
     * @see android.view.KeyEvent#KEYCODE_MEDIA_PAUSE
     */
    public static final int FLAG_KEY_MEDIA_PAUSE = 1 << 4;

    /**
     * Flag indicating a RemoteControlClient makes use of the "stop" media key.
     *
     * @see #setTransportControlFlags(int)
     * @see android.view.KeyEvent#KEYCODE_MEDIA_STOP
     */
    public static final int FLAG_KEY_MEDIA_STOP = 1 << 5;

    /**
     * Flag indicating a RemoteControlClient makes use of the "fast forward" media key.
     *
     * @see #setTransportControlFlags(int)
     * @see android.view.KeyEvent#KEYCODE_MEDIA_FAST_FORWARD
     */
    public static final int FLAG_KEY_MEDIA_FAST_FORWARD = 1 << 6;

    /**
     * Flag indicating a RemoteControlClient makes use of the "next" media key.
     *
     * @see #setTransportControlFlags(int)
     * @see android.view.KeyEvent#KEYCODE_MEDIA_NEXT
     */
    public static final int FLAG_KEY_MEDIA_NEXT = 1 << 7;

    /**
     * Flag indicating a RemoteControlClient can receive changes in the media playback position
     * through the {@link OnPlaybackPositionUpdateListener} interface. This flag must be set
     * in order for components that display the RemoteControlClient information, to display and
     * let the user control media playback position.
     *
     * @see #setTransportControlFlags(int)
     * @see #setOnGetPlaybackPositionListener(OnGetPlaybackPositionListener)
     * @see #setPlaybackPositionUpdateListener(OnPlaybackPositionUpdateListener)
     */
    public static final int FLAG_KEY_MEDIA_POSITION_UPDATE = 1 << 8;

    /**
     * Flag indicating a RemoteControlClient supports ratings.
     * This flag must be set in order for components that display the RemoteControlClient
     * information, to display ratings information, and, if ratings are declared editable
     * (by calling {@link MediaMetadataEditor#addEditableKey(int)} with the
     * {@link MediaMetadataEditor#RATING_KEY_BY_USER} key), it will enable the user to rate
     * the media, with values being received through the interface set with
     * {@link #setMetadataUpdateListener(OnMetadataUpdateListener)}.
     *
     * @see #setTransportControlFlags(int)
     */
    public static final int FLAG_KEY_MEDIA_RATING = 1 << 9;

    /**
     *
     *
     * @unknown The flags for when no media keys are declared supported.
    Intentionally hidden as an application shouldn't set the transport control flags
    to this value.
     */
    public static final int FLAGS_KEY_MEDIA_NONE = 0;

    /**
     *
     *
     * @unknown Flag used to signal some type of metadata exposed by the RemoteControlClient is requested.
     */
    public static final int FLAG_INFORMATION_REQUEST_METADATA = 1 << 0;

    /**
     *
     *
     * @unknown Flag used to signal that the transport control buttons supported by the
    RemoteControlClient are requested.
    This can for instance happen when playback is at the end of a playlist, and the "next"
    operation is not supported anymore.
     */
    public static final int FLAG_INFORMATION_REQUEST_KEY_MEDIA = 1 << 1;

    /**
     *
     *
     * @unknown Flag used to signal that the playback state of the RemoteControlClient is requested.
     */
    public static final int FLAG_INFORMATION_REQUEST_PLAYSTATE = 1 << 2;

    /**
     *
     *
     * @unknown Flag used to signal that the album art for the RemoteControlClient is requested.
     */
    public static final int FLAG_INFORMATION_REQUEST_ALBUM_ART = 1 << 3;

    private android.media.session.MediaSession mSession;

    /**
     * Class constructor.
     *
     * @param mediaButtonIntent
     * 		The intent that will be sent for the media button events sent
     * 		by remote controls.
     * 		This intent needs to have been constructed with the {@link Intent#ACTION_MEDIA_BUTTON}
     * 		action, and have a component that will handle the intent (set with
     * 		{@link Intent#setComponent(ComponentName)}) registered with
     * 		{@link AudioManager#registerMediaButtonEventReceiver(ComponentName)}
     * 		before this new RemoteControlClient can itself be registered with
     * 		{@link AudioManager#registerRemoteControlClient(RemoteControlClient)}.
     * @see AudioManager#registerMediaButtonEventReceiver(ComponentName)
     * @see AudioManager#registerRemoteControlClient(RemoteControlClient)
     */
    public RemoteControlClient(android.app.PendingIntent mediaButtonIntent) {
        mRcMediaIntent = mediaButtonIntent;
    }

    /**
     * Class constructor for a remote control client whose internal event handling
     * happens on a user-provided Looper.
     *
     * @param mediaButtonIntent
     * 		The intent that will be sent for the media button events sent
     * 		by remote controls.
     * 		This intent needs to have been constructed with the {@link Intent#ACTION_MEDIA_BUTTON}
     * 		action, and have a component that will handle the intent (set with
     * 		{@link Intent#setComponent(ComponentName)}) registered with
     * 		{@link AudioManager#registerMediaButtonEventReceiver(ComponentName)}
     * 		before this new RemoteControlClient can itself be registered with
     * 		{@link AudioManager#registerRemoteControlClient(RemoteControlClient)}.
     * @param looper
     * 		The Looper running the event loop.
     * @see AudioManager#registerMediaButtonEventReceiver(ComponentName)
     * @see AudioManager#registerRemoteControlClient(RemoteControlClient)
     */
    public RemoteControlClient(android.app.PendingIntent mediaButtonIntent, android.os.Looper looper) {
        mRcMediaIntent = mediaButtonIntent;
    }

    /**
     *
     *
     * @unknown 
     */
    public void registerWithSession(android.media.session.MediaSessionLegacyHelper helper) {
        helper.addRccListener(mRcMediaIntent, mTransportListener);
        mSession = helper.getSession(mRcMediaIntent);
        setTransportControlFlags(mTransportControlFlags);
    }

    /**
     *
     *
     * @unknown 
     */
    public void unregisterWithSession(android.media.session.MediaSessionLegacyHelper helper) {
        helper.removeRccListener(mRcMediaIntent);
        mSession = null;
    }

    /**
     * Get a {@link MediaSession} associated with this RCC. It will only have a
     * session while it is registered with
     * {@link AudioManager#registerRemoteControlClient}. The session returned
     * should not be modified directly by the application but may be used with
     * other APIs that require a session.
     *
     * @return A media session object or null.
     */
    public android.media.session.MediaSession getMediaSession() {
        return mSession;
    }

    /**
     * Class used to modify metadata in a {@link RemoteControlClient} object.
     * Use {@link RemoteControlClient#editMetadata(boolean)} to create an instance of an editor,
     * on which you set the metadata for the RemoteControlClient instance. Once all the information
     * has been set, use {@link #apply()} to make it the new metadata that should be displayed
     * for the associated client. Once the metadata has been "applied", you cannot reuse this
     * instance of the MetadataEditor.
     *
     * @deprecated Use {@link MediaMetadata} and {@link MediaSession} instead.
     */
    @java.lang.Deprecated
    public class MetadataEditor extends android.media.MediaMetadataEditor {
        // only use RemoteControlClient.editMetadata() to get a MetadataEditor instance
        private MetadataEditor() {
        }

        /**
         *
         *
         * @unknown 
         */
        public java.lang.Object clone() throws java.lang.CloneNotSupportedException {
            throw new java.lang.CloneNotSupportedException();
        }

        /**
         * The metadata key for the content artwork / album art.
         */
        public static final int BITMAP_KEY_ARTWORK = 100;

        /**
         *
         *
         * @unknown TODO(jmtrivi) have lockscreen move to the new key name and remove
         */
        public static final int METADATA_KEY_ARTWORK = android.media.RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK;

        /**
         * Adds textual information to be displayed.
         * Note that none of the information added after {@link #apply()} has been called,
         * will be displayed.
         *
         * @param key
         * 		The identifier of a the metadata field to set. Valid values are
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_ALBUM},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_ALBUMARTIST},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_TITLE},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_ARTIST},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_AUTHOR},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_COMPILATION},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_COMPOSER},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_DATE},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_GENRE},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_TITLE},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_WRITER}.
         * @param value
         * 		The text for the given key, or {@code null} to signify there is no valid
         * 		information for the field.
         * @return Returns a reference to the same MetadataEditor object, so you can chain put
        calls together.
         */
        public synchronized android.media.RemoteControlClient.MetadataEditor putString(int key, java.lang.String value) throws java.lang.IllegalArgumentException {
            super.putString(key, value);
            if (mMetadataBuilder != null) {
                // MediaMetadata supports all the same fields as MetadataEditor
                java.lang.String metadataKey = android.media.MediaMetadata.getKeyFromMetadataEditorKey(key);
                // But just in case, don't add things we don't understand
                if (metadataKey != null) {
                    mMetadataBuilder.putText(metadataKey, value);
                }
            }
            return this;
        }

        /**
         * Adds numerical information to be displayed.
         * Note that none of the information added after {@link #apply()} has been called,
         * will be displayed.
         *
         * @param key
         * 		the identifier of a the metadata field to set. Valid values are
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_CD_TRACK_NUMBER},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_DISC_NUMBER},
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_DURATION} (with a value
         * 		expressed in milliseconds),
         * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_YEAR}.
         * @param value
         * 		The long value for the given key
         * @return Returns a reference to the same MetadataEditor object, so you can chain put
        calls together.
         * @throws IllegalArgumentException
         * 		
         */
        public synchronized android.media.RemoteControlClient.MetadataEditor putLong(int key, long value) throws java.lang.IllegalArgumentException {
            super.putLong(key, value);
            if (mMetadataBuilder != null) {
                // MediaMetadata supports all the same fields as MetadataEditor
                java.lang.String metadataKey = android.media.MediaMetadata.getKeyFromMetadataEditorKey(key);
                // But just in case, don't add things we don't understand
                if (metadataKey != null) {
                    mMetadataBuilder.putLong(metadataKey, value);
                }
            }
            return this;
        }

        /**
         * Sets the album / artwork picture to be displayed on the remote control.
         *
         * @param key
         * 		the identifier of the bitmap to set. The only valid value is
         * 		{@link #BITMAP_KEY_ARTWORK}
         * @param bitmap
         * 		The bitmap for the artwork, or null if there isn't any.
         * @return Returns a reference to the same MetadataEditor object, so you can chain put
        calls together.
         * @throws IllegalArgumentException
         * 		
         * @see android.graphics.Bitmap
         */
        @java.lang.Override
        public synchronized android.media.RemoteControlClient.MetadataEditor putBitmap(int key, android.graphics.Bitmap bitmap) throws java.lang.IllegalArgumentException {
            super.putBitmap(key, bitmap);
            if (mMetadataBuilder != null) {
                // MediaMetadata supports all the same fields as MetadataEditor
                java.lang.String metadataKey = android.media.MediaMetadata.getKeyFromMetadataEditorKey(key);
                // But just in case, don't add things we don't understand
                if (metadataKey != null) {
                    mMetadataBuilder.putBitmap(metadataKey, bitmap);
                }
            }
            return this;
        }

        @java.lang.Override
        public synchronized android.media.RemoteControlClient.MetadataEditor putObject(int key, java.lang.Object object) throws java.lang.IllegalArgumentException {
            super.putObject(key, object);
            if ((mMetadataBuilder != null) && ((key == android.media.MediaMetadataEditor.RATING_KEY_BY_USER) || (key == android.media.MediaMetadataEditor.RATING_KEY_BY_OTHERS))) {
                java.lang.String metadataKey = android.media.MediaMetadata.getKeyFromMetadataEditorKey(key);
                if (metadataKey != null) {
                    mMetadataBuilder.putRating(metadataKey, ((android.media.Rating) (object)));
                }
            }
            return this;
        }

        /**
         * Clears all the metadata that has been set since the MetadataEditor instance was created
         * (with {@link RemoteControlClient#editMetadata(boolean)}).
         * Note that clearing the metadata doesn't reset the editable keys
         * (use {@link MediaMetadataEditor#removeEditableKeys()} instead).
         */
        @java.lang.Override
        public synchronized void clear() {
            super.clear();
        }

        /**
         * Associates all the metadata that has been set since the MetadataEditor instance was
         *     created with {@link RemoteControlClient#editMetadata(boolean)}, or since
         *     {@link #clear()} was called, with the RemoteControlClient. Once "applied",
         *     this MetadataEditor cannot be reused to edit the RemoteControlClient's metadata.
         */
        public synchronized void apply() {
            if (mApplied) {
                android.util.Log.e(android.media.RemoteControlClient.TAG, "Can't apply a previously applied MetadataEditor");
                return;
            }
            synchronized(mCacheLock) {
                // Still build the old metadata so when creating a new editor
                // you get the expected values.
                // assign the edited data
                mMetadata = new android.os.Bundle(mEditorMetadata);
                // add the information about editable keys
                mMetadata.putLong(java.lang.String.valueOf(android.media.MediaMetadataEditor.KEY_EDITABLE_MASK), mEditableKeys);
                if ((mOriginalArtwork != null) && (!mOriginalArtwork.equals(mEditorArtwork))) {
                    mOriginalArtwork.recycle();
                }
                mOriginalArtwork = mEditorArtwork;
                mEditorArtwork = null;
                // USE_SESSIONS
                if ((mSession != null) && (mMetadataBuilder != null)) {
                    mMediaMetadata = mMetadataBuilder.build();
                    mSession.setMetadata(mMediaMetadata);
                }
                mApplied = true;
            }
        }
    }

    /**
     * Creates a {@link MetadataEditor}.
     *
     * @param startEmpty
     * 		Set to false if you want the MetadataEditor to contain the metadata that
     * 		was previously applied to the RemoteControlClient, or true if it is to be created empty.
     * @return a new MetadataEditor instance.
     */
    public android.media.RemoteControlClient.MetadataEditor editMetadata(boolean startEmpty) {
        android.media.RemoteControlClient.MetadataEditor editor = new android.media.RemoteControlClient.MetadataEditor();
        if (startEmpty) {
            editor.mEditorMetadata = new android.os.Bundle();
            editor.mEditorArtwork = null;
            editor.mMetadataChanged = true;
            editor.mArtworkChanged = true;
            editor.mEditableKeys = 0;
        } else {
            editor.mEditorMetadata = new android.os.Bundle(mMetadata);
            editor.mEditorArtwork = mOriginalArtwork;
            editor.mMetadataChanged = false;
            editor.mArtworkChanged = false;
        }
        // USE_SESSIONS
        if (startEmpty || (mMediaMetadata == null)) {
            editor.mMetadataBuilder = new android.media.MediaMetadata.Builder();
        } else {
            editor.mMetadataBuilder = new android.media.MediaMetadata.Builder(mMediaMetadata);
        }
        return editor;
    }

    /**
     * Sets the current playback state.
     *
     * @param state
     * 		The current playback state, one of the following values:
     * 		{@link #PLAYSTATE_STOPPED},
     * 		{@link #PLAYSTATE_PAUSED},
     * 		{@link #PLAYSTATE_PLAYING},
     * 		{@link #PLAYSTATE_FAST_FORWARDING},
     * 		{@link #PLAYSTATE_REWINDING},
     * 		{@link #PLAYSTATE_SKIPPING_FORWARDS},
     * 		{@link #PLAYSTATE_SKIPPING_BACKWARDS},
     * 		{@link #PLAYSTATE_BUFFERING},
     * 		{@link #PLAYSTATE_ERROR}.
     */
    public void setPlaybackState(int state) {
        /* legacy API, converting to method with position and speed */
        setPlaybackStateInt(state, android.media.RemoteControlClient.PLAYBACK_POSITION_ALWAYS_UNKNOWN, android.media.RemoteControlClient.PLAYBACK_SPEED_1X, false);
    }

    /**
     * Sets the current playback state and the matching media position for the current playback
     *   speed.
     *
     * @param state
     * 		The current playback state, one of the following values:
     * 		{@link #PLAYSTATE_STOPPED},
     * 		{@link #PLAYSTATE_PAUSED},
     * 		{@link #PLAYSTATE_PLAYING},
     * 		{@link #PLAYSTATE_FAST_FORWARDING},
     * 		{@link #PLAYSTATE_REWINDING},
     * 		{@link #PLAYSTATE_SKIPPING_FORWARDS},
     * 		{@link #PLAYSTATE_SKIPPING_BACKWARDS},
     * 		{@link #PLAYSTATE_BUFFERING},
     * 		{@link #PLAYSTATE_ERROR}.
     * @param timeInMs
     * 		a 0 or positive value for the current media position expressed in ms
     * 		(same unit as for when sending the media duration, if applicable, with
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_DURATION} in the
     * 		{@link RemoteControlClient.MetadataEditor}). Negative values imply that position is not
     * 		known (e.g. listening to a live stream of a radio) or not applicable (e.g. when state
     * 		is {@link #PLAYSTATE_BUFFERING} and nothing had played yet).
     * @param playbackSpeed
     * 		a value expressed as a ratio of 1x playback: 1.0f is normal playback,
     * 		2.0f is 2x, 0.5f is half-speed, -2.0f is rewind at 2x speed. 0.0f means nothing is
     * 		playing (e.g. when state is {@link #PLAYSTATE_ERROR}).
     */
    public void setPlaybackState(int state, long timeInMs, float playbackSpeed) {
        setPlaybackStateInt(state, timeInMs, playbackSpeed, true);
    }

    private void setPlaybackStateInt(int state, long timeInMs, float playbackSpeed, boolean hasPosition) {
        synchronized(mCacheLock) {
            if (((mPlaybackState != state) || (mPlaybackPositionMs != timeInMs)) || (mPlaybackSpeed != playbackSpeed)) {
                // store locally
                mPlaybackState = state;
                // distinguish between an application not knowing the current playback position
                // at the moment and an application using the API where only the playback state
                // is passed, not the playback position.
                if (hasPosition) {
                    if (timeInMs < 0) {
                        mPlaybackPositionMs = android.media.RemoteControlClient.PLAYBACK_POSITION_INVALID;
                    } else {
                        mPlaybackPositionMs = timeInMs;
                    }
                } else {
                    mPlaybackPositionMs = android.media.RemoteControlClient.PLAYBACK_POSITION_ALWAYS_UNKNOWN;
                }
                mPlaybackSpeed = playbackSpeed;
                // keep track of when the state change occurred
                mPlaybackStateChangeTimeMs = android.os.SystemClock.elapsedRealtime();
                // USE_SESSIONS
                if (mSession != null) {
                    int pbState = android.media.session.PlaybackState.getStateFromRccState(state);
                    long position = (hasPosition) ? mPlaybackPositionMs : android.media.session.PlaybackState.PLAYBACK_POSITION_UNKNOWN;
                    android.media.session.PlaybackState.Builder bob = new android.media.session.PlaybackState.Builder(mSessionPlaybackState);
                    bob.setState(pbState, position, playbackSpeed, android.os.SystemClock.elapsedRealtime());
                    bob.setErrorMessage(null);
                    mSessionPlaybackState = bob.build();
                    mSession.setPlaybackState(mSessionPlaybackState);
                }
            }
        }
    }

    /**
     * Sets the flags for the media transport control buttons that this client supports.
     *
     * @param transportControlFlags
     * 		A combination of the following flags:
     * 		{@link #FLAG_KEY_MEDIA_PREVIOUS},
     * 		{@link #FLAG_KEY_MEDIA_REWIND},
     * 		{@link #FLAG_KEY_MEDIA_PLAY},
     * 		{@link #FLAG_KEY_MEDIA_PLAY_PAUSE},
     * 		{@link #FLAG_KEY_MEDIA_PAUSE},
     * 		{@link #FLAG_KEY_MEDIA_STOP},
     * 		{@link #FLAG_KEY_MEDIA_FAST_FORWARD},
     * 		{@link #FLAG_KEY_MEDIA_NEXT},
     * 		{@link #FLAG_KEY_MEDIA_POSITION_UPDATE},
     * 		{@link #FLAG_KEY_MEDIA_RATING}.
     */
    public void setTransportControlFlags(int transportControlFlags) {
        synchronized(mCacheLock) {
            // store locally
            mTransportControlFlags = transportControlFlags;
            // USE_SESSIONS
            if (mSession != null) {
                android.media.session.PlaybackState.Builder bob = new android.media.session.PlaybackState.Builder(mSessionPlaybackState);
                bob.setActions(android.media.session.PlaybackState.getActionsFromRccControlFlags(transportControlFlags));
                mSessionPlaybackState = bob.build();
                mSession.setPlaybackState(mSessionPlaybackState);
            }
        }
    }

    /**
     * Interface definition for a callback to be invoked when one of the metadata values has
     * been updated.
     * Implement this interface to receive metadata updates after registering your listener
     * through {@link RemoteControlClient#setMetadataUpdateListener(OnMetadataUpdateListener)}.
     */
    public interface OnMetadataUpdateListener {
        /**
         * Called on the implementer to notify that the metadata field for the given key has
         * been updated to the new value.
         *
         * @param key
         * 		the identifier of the updated metadata field.
         * @param newValue
         * 		the Object storing the new value for the key.
         */
        public abstract void onMetadataUpdate(int key, java.lang.Object newValue);
    }

    /**
     * Sets the listener to be called whenever the metadata is updated.
     * New metadata values will be received in the same thread as the one in which
     * RemoteControlClient was created.
     *
     * @param l
     * 		the metadata update listener
     */
    public void setMetadataUpdateListener(android.media.RemoteControlClient.OnMetadataUpdateListener l) {
        synchronized(mCacheLock) {
            mMetadataUpdateListener = l;
        }
    }

    /**
     * Interface definition for a callback to be invoked when the media playback position is
     * requested to be updated.
     *
     * @see RemoteControlClient#FLAG_KEY_MEDIA_POSITION_UPDATE
     */
    public interface OnPlaybackPositionUpdateListener {
        /**
         * Called on the implementer to notify it that the playback head should be set at the given
         * position. If the position can be changed from its current value, the implementor of
         * the interface must also update the playback position using
         * {@link #setPlaybackState(int, long, float)} to reflect the actual new
         * position being used, regardless of whether it differs from the requested position.
         * Failure to do so would cause the system to not know the new actual playback position,
         * and user interface components would fail to show the user where playback resumed after
         * the position was updated.
         *
         * @param newPositionMs
         * 		the new requested position in the current media, expressed in ms.
         */
        void onPlaybackPositionUpdate(long newPositionMs);
    }

    /**
     * Interface definition for a callback to be invoked when the media playback position is
     * queried.
     *
     * @see RemoteControlClient#FLAG_KEY_MEDIA_POSITION_UPDATE
     */
    public interface OnGetPlaybackPositionListener {
        /**
         * Called on the implementer of the interface to query the current playback position.
         *
         * @return a negative value if the current playback position (or the last valid playback
        position) is not known, or a zero or positive value expressed in ms indicating the
        current position, or the last valid known position.
         */
        long onGetPlaybackPosition();
    }

    /**
     * Sets the listener to be called whenever the media playback position is requested
     * to be updated.
     * Notifications will be received in the same thread as the one in which RemoteControlClient
     * was created.
     *
     * @param l
     * 		the position update listener to be called
     */
    public void setPlaybackPositionUpdateListener(android.media.RemoteControlClient.OnPlaybackPositionUpdateListener l) {
        synchronized(mCacheLock) {
            mPositionUpdateListener = l;
        }
    }

    /**
     * Sets the listener to be called whenever the media current playback position is needed.
     * Queries will be received in the same thread as the one in which RemoteControlClient
     * was created.
     *
     * @param l
     * 		the listener to be called to retrieve the playback position
     */
    public void setOnGetPlaybackPositionListener(android.media.RemoteControlClient.OnGetPlaybackPositionListener l) {
        synchronized(mCacheLock) {
            mPositionProvider = l;
        }
    }

    /**
     *
     *
     * @unknown Flag to reflect that the application controlling this RemoteControlClient sends playback
    position updates. The playback position being "readable" is considered from the application's
    point of view.
     */
    public static int MEDIA_POSITION_READABLE = 1 << 0;

    /**
     *
     *
     * @unknown Flag to reflect that the application controlling this RemoteControlClient can receive
    playback position updates. The playback position being "writable"
    is considered from the application's point of view.
     */
    public static int MEDIA_POSITION_WRITABLE = 1 << 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int DEFAULT_PLAYBACK_VOLUME_HANDLING = android.media.RemoteControlClient.PLAYBACK_VOLUME_VARIABLE;

    /**
     *
     *
     * @unknown 
     */
    // hard-coded to the same number of steps as AudioService.MAX_STREAM_VOLUME[STREAM_MUSIC]
    public static final int DEFAULT_PLAYBACK_VOLUME = 15;

    /**
     * Lock for all cached data
     */
    private final java.lang.Object mCacheLock = new java.lang.Object();

    /**
     * Cache for the playback state.
     * Access synchronized on mCacheLock
     */
    private int mPlaybackState = android.media.RemoteControlClient.PLAYSTATE_NONE;

    /**
     * Time of last play state change
     * Access synchronized on mCacheLock
     */
    private long mPlaybackStateChangeTimeMs = 0;

    /**
     * Last playback position in ms reported by the user
     */
    private long mPlaybackPositionMs = android.media.RemoteControlClient.PLAYBACK_POSITION_INVALID;

    /**
     * Last playback speed reported by the user
     */
    private float mPlaybackSpeed = android.media.RemoteControlClient.PLAYBACK_SPEED_1X;

    /**
     * Cache for the artwork bitmap.
     * Access synchronized on mCacheLock
     * Artwork and metadata are not kept in one Bundle because the bitmap sometimes needs to be
     * accessed to be resized, in which case a copy will be made. This would add overhead in
     * Bundle operations.
     */
    private android.graphics.Bitmap mOriginalArtwork;

    /**
     * Cache for the transport control mask.
     * Access synchronized on mCacheLock
     */
    private int mTransportControlFlags = android.media.RemoteControlClient.FLAGS_KEY_MEDIA_NONE;

    /**
     * Cache for the metadata strings.
     * Access synchronized on mCacheLock
     * This is re-initialized in apply() and so cannot be final.
     */
    private android.os.Bundle mMetadata = new android.os.Bundle();

    /**
     * Listener registered by user of RemoteControlClient to receive requests for playback position
     * update requests.
     */
    private android.media.RemoteControlClient.OnPlaybackPositionUpdateListener mPositionUpdateListener;

    /**
     * Provider registered by user of RemoteControlClient to provide the current playback position.
     */
    private android.media.RemoteControlClient.OnGetPlaybackPositionListener mPositionProvider;

    /**
     * Listener registered by user of RemoteControlClient to receive edit changes to metadata
     * it exposes.
     */
    private android.media.RemoteControlClient.OnMetadataUpdateListener mMetadataUpdateListener;

    /**
     * The current remote control client generation ID across the system, as known by this object
     */
    private int mCurrentClientGenId = -1;

    /**
     * The media button intent description associated with this remote control client
     * (can / should include target component for intent handling, used when persisting media
     *    button event receiver across reboots).
     */
    private final android.app.PendingIntent mRcMediaIntent;

    /**
     * Reflects whether any "plugged in" IRemoteControlDisplay has mWantsPositonSync set to true.
     */
    // TODO consider using a ref count for IRemoteControlDisplay requiring sync instead
    private boolean mNeedsPositionSync = false;

    /**
     * Cache for the current playback state using Session APIs.
     */
    private android.media.session.PlaybackState mSessionPlaybackState = null;

    /**
     * Cache for metadata using Session APIs. This is re-initialized in apply().
     */
    private android.media.MediaMetadata mMediaMetadata;

    /**
     *
     *
     * @unknown Accessor to media button intent description (includes target component)
     */
    public android.app.PendingIntent getRcMediaIntent() {
        return mRcMediaIntent;
    }

    /**
     *
     *
     * @unknown Default value for the unique identifier
     */
    public static final int RCSE_ID_UNREGISTERED = -1;

    // USE_SESSIONS
    private android.media.session.MediaSession.Callback mTransportListener = new android.media.session.MediaSession.Callback() {
        @java.lang.Override
        public void onSeekTo(long pos) {
            android.media.RemoteControlClient.this.onSeekTo(mCurrentClientGenId, pos);
        }

        @java.lang.Override
        public void onSetRating(android.media.Rating rating) {
            if ((mTransportControlFlags & android.media.RemoteControlClient.FLAG_KEY_MEDIA_RATING) != 0) {
                onUpdateMetadata(mCurrentClientGenId, android.media.RemoteControlClient.MetadataEditor.RATING_KEY_BY_USER, rating);
            }
        }
    };

    // ===========================================================
    // Message handlers
    private void onSeekTo(int generationId, long timeMs) {
        synchronized(mCacheLock) {
            if ((mCurrentClientGenId == generationId) && (mPositionUpdateListener != null)) {
                mPositionUpdateListener.onPlaybackPositionUpdate(timeMs);
            }
        }
    }

    private void onUpdateMetadata(int generationId, int key, java.lang.Object value) {
        synchronized(mCacheLock) {
            if ((mCurrentClientGenId == generationId) && (mMetadataUpdateListener != null)) {
                mMetadataUpdateListener.onMetadataUpdate(key, value);
            }
        }
    }

    // ===========================================================
    // Internal utilities
    /**
     * Returns whether, for the given playback state, the playback position is expected to
     * be changing.
     *
     * @param playstate
     * 		the playback state to evaluate
     * @return true during any form of playback, false if it's not playing anything while in this
    playback state
     */
    static boolean playbackPositionShouldMove(int playstate) {
        switch (playstate) {
            case android.media.RemoteControlClient.PLAYSTATE_STOPPED :
            case android.media.RemoteControlClient.PLAYSTATE_PAUSED :
            case android.media.RemoteControlClient.PLAYSTATE_BUFFERING :
            case android.media.RemoteControlClient.PLAYSTATE_ERROR :
            case android.media.RemoteControlClient.PLAYSTATE_SKIPPING_FORWARDS :
            case android.media.RemoteControlClient.PLAYSTATE_SKIPPING_BACKWARDS :
                return false;
            case android.media.RemoteControlClient.PLAYSTATE_PLAYING :
            case android.media.RemoteControlClient.PLAYSTATE_FAST_FORWARDING :
            case android.media.RemoteControlClient.PLAYSTATE_REWINDING :
            default :
                return true;
        }
    }

    /**
     * Period for playback position drift checks, 15s when playing at 1x or slower.
     */
    private static final long POSITION_REFRESH_PERIOD_PLAYING_MS = 15000;

    /**
     * Minimum period for playback position drift checks, never more often when every 2s, when
     * fast forwarding or rewinding.
     */
    private static final long POSITION_REFRESH_PERIOD_MIN_MS = 2000;

    /**
     * The value above which the difference between client-reported playback position and
     * estimated position is considered a drift.
     */
    private static final long POSITION_DRIFT_MAX_MS = 500;

    /**
     * Compute the period at which the estimated playback position should be compared against the
     * actual playback position. Is a funciton of playback speed.
     *
     * @param speed
     * 		1.0f is normal playback speed
     * @return the period in ms
     */
    private static long getCheckPeriodFromSpeed(float speed) {
        if (java.lang.Math.abs(speed) <= 1.0F) {
            return android.media.RemoteControlClient.POSITION_REFRESH_PERIOD_PLAYING_MS;
        } else {
            return java.lang.Math.max(((long) (android.media.RemoteControlClient.POSITION_REFRESH_PERIOD_PLAYING_MS / java.lang.Math.abs(speed))), android.media.RemoteControlClient.POSITION_REFRESH_PERIOD_MIN_MS);
        }
    }
}

