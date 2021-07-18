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
 * Playback state for a {@link MediaSession}. This includes a state like
 * {@link PlaybackState#STATE_PLAYING}, the current playback position,
 * and the current control capabilities.
 */
public final class PlaybackState implements android.os.Parcelable {
    private static final java.lang.String TAG = "PlaybackState";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, value = { android.media.session.PlaybackState.ACTION_STOP, android.media.session.PlaybackState.ACTION_PAUSE, android.media.session.PlaybackState.ACTION_PLAY, android.media.session.PlaybackState.ACTION_REWIND, android.media.session.PlaybackState.ACTION_SKIP_TO_PREVIOUS, android.media.session.PlaybackState.ACTION_SKIP_TO_NEXT, android.media.session.PlaybackState.ACTION_FAST_FORWARD, android.media.session.PlaybackState.ACTION_SET_RATING, android.media.session.PlaybackState.ACTION_SEEK_TO, android.media.session.PlaybackState.ACTION_PLAY_PAUSE, android.media.session.PlaybackState.ACTION_PLAY_FROM_MEDIA_ID, android.media.session.PlaybackState.ACTION_PLAY_FROM_SEARCH, android.media.session.PlaybackState.ACTION_SKIP_TO_QUEUE_ITEM, android.media.session.PlaybackState.ACTION_PLAY_FROM_URI, android.media.session.PlaybackState.ACTION_PREPARE, android.media.session.PlaybackState.ACTION_PREPARE_FROM_MEDIA_ID, android.media.session.PlaybackState.ACTION_PREPARE_FROM_SEARCH, android.media.session.PlaybackState.ACTION_PREPARE_FROM_URI })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Actions {}

    /**
     * Indicates this session supports the stop command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_STOP = 1 << 0;

    /**
     * Indicates this session supports the pause command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_PAUSE = 1 << 1;

    /**
     * Indicates this session supports the play command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_PLAY = 1 << 2;

    /**
     * Indicates this session supports the rewind command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_REWIND = 1 << 3;

    /**
     * Indicates this session supports the previous command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_SKIP_TO_PREVIOUS = 1 << 4;

    /**
     * Indicates this session supports the next command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_SKIP_TO_NEXT = 1 << 5;

    /**
     * Indicates this session supports the fast forward command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_FAST_FORWARD = 1 << 6;

    /**
     * Indicates this session supports the set rating command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_SET_RATING = 1 << 7;

    /**
     * Indicates this session supports the seek to command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_SEEK_TO = 1 << 8;

    /**
     * Indicates this session supports the play/pause toggle command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_PLAY_PAUSE = 1 << 9;

    /**
     * Indicates this session supports the play from media id command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_PLAY_FROM_MEDIA_ID = 1 << 10;

    /**
     * Indicates this session supports the play from search command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_PLAY_FROM_SEARCH = 1 << 11;

    /**
     * Indicates this session supports the skip to queue item command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_SKIP_TO_QUEUE_ITEM = 1 << 12;

    /**
     * Indicates this session supports the play from URI command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_PLAY_FROM_URI = 1 << 13;

    /**
     * Indicates this session supports the prepare command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_PREPARE = 1 << 14;

    /**
     * Indicates this session supports the prepare from media id command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_PREPARE_FROM_MEDIA_ID = 1 << 15;

    /**
     * Indicates this session supports the prepare from search command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_PREPARE_FROM_SEARCH = 1 << 16;

    /**
     * Indicates this session supports the prepare from URI command.
     *
     * @see Builder#setActions(long)
     */
    public static final long ACTION_PREPARE_FROM_URI = 1 << 17;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.session.PlaybackState.STATE_NONE, android.media.session.PlaybackState.STATE_STOPPED, android.media.session.PlaybackState.STATE_PAUSED, android.media.session.PlaybackState.STATE_PLAYING, android.media.session.PlaybackState.STATE_FAST_FORWARDING, android.media.session.PlaybackState.STATE_REWINDING, android.media.session.PlaybackState.STATE_BUFFERING, android.media.session.PlaybackState.STATE_ERROR, android.media.session.PlaybackState.STATE_CONNECTING, android.media.session.PlaybackState.STATE_SKIPPING_TO_PREVIOUS, android.media.session.PlaybackState.STATE_SKIPPING_TO_NEXT, android.media.session.PlaybackState.STATE_SKIPPING_TO_QUEUE_ITEM })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface State {}

    /**
     * This is the default playback state and indicates that no media has been
     * added yet, or the performer has been reset and has no content to play.
     *
     * @see Builder#setState(int, long, float)
     * @see Builder#setState(int, long, float, long)
     */
    public static final int STATE_NONE = 0;

    /**
     * State indicating this item is currently stopped.
     *
     * @see Builder#setState
     */
    public static final int STATE_STOPPED = 1;

    /**
     * State indicating this item is currently paused.
     *
     * @see Builder#setState
     */
    public static final int STATE_PAUSED = 2;

    /**
     * State indicating this item is currently playing.
     *
     * @see Builder#setState
     */
    public static final int STATE_PLAYING = 3;

    /**
     * State indicating this item is currently fast forwarding.
     *
     * @see Builder#setState
     */
    public static final int STATE_FAST_FORWARDING = 4;

    /**
     * State indicating this item is currently rewinding.
     *
     * @see Builder#setState
     */
    public static final int STATE_REWINDING = 5;

    /**
     * State indicating this item is currently buffering and will begin playing
     * when enough data has buffered.
     *
     * @see Builder#setState
     */
    public static final int STATE_BUFFERING = 6;

    /**
     * State indicating this item is currently in an error state. The error
     * message should also be set when entering this state.
     *
     * @see Builder#setState
     */
    public static final int STATE_ERROR = 7;

    /**
     * State indicating the class doing playback is currently connecting to a
     * new destination.  Depending on the implementation you may return to the previous
     * state when the connection finishes or enter {@link #STATE_NONE}.
     * If the connection failed {@link #STATE_ERROR} should be used.
     *
     * @see Builder#setState
     */
    public static final int STATE_CONNECTING = 8;

    /**
     * State indicating the player is currently skipping to the previous item.
     *
     * @see Builder#setState
     */
    public static final int STATE_SKIPPING_TO_PREVIOUS = 9;

    /**
     * State indicating the player is currently skipping to the next item.
     *
     * @see Builder#setState
     */
    public static final int STATE_SKIPPING_TO_NEXT = 10;

    /**
     * State indicating the player is currently skipping to a specific item in
     * the queue.
     *
     * @see Builder#setState
     */
    public static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;

    /**
     * Use this value for the position to indicate the position is not known.
     */
    public static final long PLAYBACK_POSITION_UNKNOWN = -1;

    private final int mState;

    private final long mPosition;

    private final long mBufferedPosition;

    private final float mSpeed;

    private final long mActions;

    private java.util.List<android.media.session.PlaybackState.CustomAction> mCustomActions;

    private final java.lang.CharSequence mErrorMessage;

    private final long mUpdateTime;

    private final long mActiveItemId;

    private final android.os.Bundle mExtras;

    private PlaybackState(int state, long position, long updateTime, float speed, long bufferedPosition, long transportControls, java.util.List<android.media.session.PlaybackState.CustomAction> customActions, long activeItemId, java.lang.CharSequence error, android.os.Bundle extras) {
        mState = state;
        mPosition = position;
        mSpeed = speed;
        mUpdateTime = updateTime;
        mBufferedPosition = bufferedPosition;
        mActions = transportControls;
        mCustomActions = new java.util.ArrayList<>(customActions);
        mActiveItemId = activeItemId;
        mErrorMessage = error;
        mExtras = extras;
    }

    private PlaybackState(android.os.Parcel in) {
        mState = in.readInt();
        mPosition = in.readLong();
        mSpeed = in.readFloat();
        mUpdateTime = in.readLong();
        mBufferedPosition = in.readLong();
        mActions = in.readLong();
        mCustomActions = in.createTypedArrayList(android.media.session.PlaybackState.CustomAction.CREATOR);
        mActiveItemId = in.readLong();
        mErrorMessage = in.readCharSequence();
        mExtras = in.readBundle();
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder bob = new java.lang.StringBuilder("PlaybackState {");
        bob.append("state=").append(mState);
        bob.append(", position=").append(mPosition);
        bob.append(", buffered position=").append(mBufferedPosition);
        bob.append(", speed=").append(mSpeed);
        bob.append(", updated=").append(mUpdateTime);
        bob.append(", actions=").append(mActions);
        bob.append(", custom actions=").append(mCustomActions);
        bob.append(", active item id=").append(mActiveItemId);
        bob.append(", error=").append(mErrorMessage);
        bob.append("}");
        return bob.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mState);
        dest.writeLong(mPosition);
        dest.writeFloat(mSpeed);
        dest.writeLong(mUpdateTime);
        dest.writeLong(mBufferedPosition);
        dest.writeLong(mActions);
        dest.writeTypedList(mCustomActions);
        dest.writeLong(mActiveItemId);
        dest.writeCharSequence(mErrorMessage);
        dest.writeBundle(mExtras);
    }

    /**
     * Get the current state of playback. One of the following:
     * <ul>
     * <li> {@link PlaybackState#STATE_NONE}</li>
     * <li> {@link PlaybackState#STATE_STOPPED}</li>
     * <li> {@link PlaybackState#STATE_PLAYING}</li>
     * <li> {@link PlaybackState#STATE_PAUSED}</li>
     * <li> {@link PlaybackState#STATE_FAST_FORWARDING}</li>
     * <li> {@link PlaybackState#STATE_REWINDING}</li>
     * <li> {@link PlaybackState#STATE_BUFFERING}</li>
     * <li> {@link PlaybackState#STATE_ERROR}</li>
     * <li> {@link PlaybackState#STATE_CONNECTING}</li>
     * <li> {@link PlaybackState#STATE_SKIPPING_TO_PREVIOUS}</li>
     * <li> {@link PlaybackState#STATE_SKIPPING_TO_NEXT}</li>
     * <li> {@link PlaybackState#STATE_SKIPPING_TO_QUEUE_ITEM}</li>
     * </ul>
     */
    @android.media.session.PlaybackState.State
    public int getState() {
        return mState;
    }

    /**
     * Get the current playback position in ms.
     */
    public long getPosition() {
        return mPosition;
    }

    /**
     * Get the current buffered position in ms. This is the farthest playback
     * point that can be reached from the current position using only buffered
     * content.
     */
    public long getBufferedPosition() {
        return mBufferedPosition;
    }

    /**
     * Get the current playback speed as a multiple of normal playback. This
     * should be negative when rewinding. A value of 1 means normal playback and
     * 0 means paused.
     *
     * @return The current speed of playback.
     */
    public float getPlaybackSpeed() {
        return mSpeed;
    }

    /**
     * Get the current actions available on this session. This should use a
     * bitmask of the available actions.
     * <ul>
     * <li> {@link PlaybackState#ACTION_SKIP_TO_PREVIOUS}</li>
     * <li> {@link PlaybackState#ACTION_REWIND}</li>
     * <li> {@link PlaybackState#ACTION_PLAY}</li>
     * <li> {@link PlaybackState#ACTION_PAUSE}</li>
     * <li> {@link PlaybackState#ACTION_STOP}</li>
     * <li> {@link PlaybackState#ACTION_FAST_FORWARD}</li>
     * <li> {@link PlaybackState#ACTION_SKIP_TO_NEXT}</li>
     * <li> {@link PlaybackState#ACTION_SEEK_TO}</li>
     * <li> {@link PlaybackState#ACTION_SET_RATING}</li>
     * <li> {@link PlaybackState#ACTION_PLAY_PAUSE}</li>
     * <li> {@link PlaybackState#ACTION_PLAY_FROM_MEDIA_ID}</li>
     * <li> {@link PlaybackState#ACTION_PLAY_FROM_SEARCH}</li>
     * <li> {@link PlaybackState#ACTION_SKIP_TO_QUEUE_ITEM}</li>
     * <li> {@link PlaybackState#ACTION_PLAY_FROM_URI}</li>
     * <li> {@link PlaybackState#ACTION_PREPARE}</li>
     * <li> {@link PlaybackState#ACTION_PREPARE_FROM_MEDIA_ID}</li>
     * <li> {@link PlaybackState#ACTION_PREPARE_FROM_SEARCH}</li>
     * <li> {@link PlaybackState#ACTION_PREPARE_FROM_URI}</li>
     * </ul>
     */
    @android.media.session.PlaybackState.Actions
    public long getActions() {
        return mActions;
    }

    /**
     * Get the list of custom actions.
     */
    public java.util.List<android.media.session.PlaybackState.CustomAction> getCustomActions() {
        return mCustomActions;
    }

    /**
     * Get a user readable error message. This should be set when the state is
     * {@link PlaybackState#STATE_ERROR}.
     */
    public java.lang.CharSequence getErrorMessage() {
        return mErrorMessage;
    }

    /**
     * Get the elapsed real time at which position was last updated. If the
     * position has never been set this will return 0;
     *
     * @return The last time the position was updated.
     */
    public long getLastPositionUpdateTime() {
        return mUpdateTime;
    }

    /**
     * Get the id of the currently active item in the queue. If there is no
     * queue or a queue is not supported by the session this will be
     * {@link MediaSession.QueueItem#UNKNOWN_ID}.
     *
     * @return The id of the currently active item in the queue or
    {@link MediaSession.QueueItem#UNKNOWN_ID}.
     */
    public long getActiveQueueItemId() {
        return mActiveItemId;
    }

    /**
     * Get any custom extras that were set on this playback state.
     *
     * @return The extras for this state or null.
     */
    @android.annotation.Nullable
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    /**
     * Get the {@link PlaybackState} state for the given
     * {@link RemoteControlClient} state.
     *
     * @param rccState
     * 		The state used by {@link RemoteControlClient}.
     * @return The equivalent state used by {@link PlaybackState}.
     * @unknown 
     */
    public static int getStateFromRccState(int rccState) {
        switch (rccState) {
            case android.media.RemoteControlClient.PLAYSTATE_BUFFERING :
                return android.media.session.PlaybackState.STATE_BUFFERING;
            case android.media.RemoteControlClient.PLAYSTATE_ERROR :
                return android.media.session.PlaybackState.STATE_ERROR;
            case android.media.RemoteControlClient.PLAYSTATE_FAST_FORWARDING :
                return android.media.session.PlaybackState.STATE_FAST_FORWARDING;
            case android.media.RemoteControlClient.PLAYSTATE_NONE :
                return android.media.session.PlaybackState.STATE_NONE;
            case android.media.RemoteControlClient.PLAYSTATE_PAUSED :
                return android.media.session.PlaybackState.STATE_PAUSED;
            case android.media.RemoteControlClient.PLAYSTATE_PLAYING :
                return android.media.session.PlaybackState.STATE_PLAYING;
            case android.media.RemoteControlClient.PLAYSTATE_REWINDING :
                return android.media.session.PlaybackState.STATE_REWINDING;
            case android.media.RemoteControlClient.PLAYSTATE_SKIPPING_BACKWARDS :
                return android.media.session.PlaybackState.STATE_SKIPPING_TO_PREVIOUS;
            case android.media.RemoteControlClient.PLAYSTATE_SKIPPING_FORWARDS :
                return android.media.session.PlaybackState.STATE_SKIPPING_TO_NEXT;
            case android.media.RemoteControlClient.PLAYSTATE_STOPPED :
                return android.media.session.PlaybackState.STATE_STOPPED;
            default :
                return -1;
        }
    }

    /**
     * Get the {@link RemoteControlClient} state for the given
     * {@link PlaybackState} state.
     *
     * @param state
     * 		The state used by {@link PlaybackState}.
     * @return The equivalent state used by {@link RemoteControlClient}.
     * @unknown 
     */
    public static int getRccStateFromState(int state) {
        switch (state) {
            case android.media.session.PlaybackState.STATE_BUFFERING :
                return android.media.RemoteControlClient.PLAYSTATE_BUFFERING;
            case android.media.session.PlaybackState.STATE_ERROR :
                return android.media.RemoteControlClient.PLAYSTATE_ERROR;
            case android.media.session.PlaybackState.STATE_FAST_FORWARDING :
                return android.media.RemoteControlClient.PLAYSTATE_FAST_FORWARDING;
            case android.media.session.PlaybackState.STATE_NONE :
                return android.media.RemoteControlClient.PLAYSTATE_NONE;
            case android.media.session.PlaybackState.STATE_PAUSED :
                return android.media.RemoteControlClient.PLAYSTATE_PAUSED;
            case android.media.session.PlaybackState.STATE_PLAYING :
                return android.media.RemoteControlClient.PLAYSTATE_PLAYING;
            case android.media.session.PlaybackState.STATE_REWINDING :
                return android.media.RemoteControlClient.PLAYSTATE_REWINDING;
            case android.media.session.PlaybackState.STATE_SKIPPING_TO_PREVIOUS :
                return android.media.RemoteControlClient.PLAYSTATE_SKIPPING_BACKWARDS;
            case android.media.session.PlaybackState.STATE_SKIPPING_TO_NEXT :
                return android.media.RemoteControlClient.PLAYSTATE_SKIPPING_FORWARDS;
            case android.media.session.PlaybackState.STATE_STOPPED :
                return android.media.RemoteControlClient.PLAYSTATE_STOPPED;
            default :
                return -1;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static long getActionsFromRccControlFlags(int rccFlags) {
        long actions = 0;
        long flag = 1;
        while (flag <= rccFlags) {
            if ((flag & rccFlags) != 0) {
                actions |= android.media.session.PlaybackState.getActionForRccFlag(((int) (flag)));
            }
            flag = flag << 1;
        } 
        return actions;
    }

    /**
     *
     *
     * @unknown 
     */
    public static int getRccControlFlagsFromActions(long actions) {
        int rccFlags = 0;
        long action = 1;
        while ((action <= actions) && (action < java.lang.Integer.MAX_VALUE)) {
            if ((action & actions) != 0) {
                rccFlags |= android.media.session.PlaybackState.getRccFlagForAction(action);
            }
            action = action << 1;
        } 
        return rccFlags;
    }

    private static long getActionForRccFlag(int flag) {
        switch (flag) {
            case android.media.RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS :
                return android.media.session.PlaybackState.ACTION_SKIP_TO_PREVIOUS;
            case android.media.RemoteControlClient.FLAG_KEY_MEDIA_REWIND :
                return android.media.session.PlaybackState.ACTION_REWIND;
            case android.media.RemoteControlClient.FLAG_KEY_MEDIA_PLAY :
                return android.media.session.PlaybackState.ACTION_PLAY;
            case android.media.RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE :
                return android.media.session.PlaybackState.ACTION_PLAY_PAUSE;
            case android.media.RemoteControlClient.FLAG_KEY_MEDIA_PAUSE :
                return android.media.session.PlaybackState.ACTION_PAUSE;
            case android.media.RemoteControlClient.FLAG_KEY_MEDIA_STOP :
                return android.media.session.PlaybackState.ACTION_STOP;
            case android.media.RemoteControlClient.FLAG_KEY_MEDIA_FAST_FORWARD :
                return android.media.session.PlaybackState.ACTION_FAST_FORWARD;
            case android.media.RemoteControlClient.FLAG_KEY_MEDIA_NEXT :
                return android.media.session.PlaybackState.ACTION_SKIP_TO_NEXT;
            case android.media.RemoteControlClient.FLAG_KEY_MEDIA_POSITION_UPDATE :
                return android.media.session.PlaybackState.ACTION_SEEK_TO;
            case android.media.RemoteControlClient.FLAG_KEY_MEDIA_RATING :
                return android.media.session.PlaybackState.ACTION_SET_RATING;
        }
        return 0;
    }

    private static int getRccFlagForAction(long action) {
        // We only care about the lower set of actions that can map to rcc
        // flags.
        int testAction = (action < java.lang.Integer.MAX_VALUE) ? ((int) (action)) : 0;
        switch (testAction) {
            case ((int) (android.media.session.PlaybackState.ACTION_SKIP_TO_PREVIOUS)) :
                return android.media.RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS;
            case ((int) (android.media.session.PlaybackState.ACTION_REWIND)) :
                return android.media.RemoteControlClient.FLAG_KEY_MEDIA_REWIND;
            case ((int) (android.media.session.PlaybackState.ACTION_PLAY)) :
                return android.media.RemoteControlClient.FLAG_KEY_MEDIA_PLAY;
            case ((int) (android.media.session.PlaybackState.ACTION_PLAY_PAUSE)) :
                return android.media.RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE;
            case ((int) (android.media.session.PlaybackState.ACTION_PAUSE)) :
                return android.media.RemoteControlClient.FLAG_KEY_MEDIA_PAUSE;
            case ((int) (android.media.session.PlaybackState.ACTION_STOP)) :
                return android.media.RemoteControlClient.FLAG_KEY_MEDIA_STOP;
            case ((int) (android.media.session.PlaybackState.ACTION_FAST_FORWARD)) :
                return android.media.RemoteControlClient.FLAG_KEY_MEDIA_FAST_FORWARD;
            case ((int) (android.media.session.PlaybackState.ACTION_SKIP_TO_NEXT)) :
                return android.media.RemoteControlClient.FLAG_KEY_MEDIA_NEXT;
            case ((int) (android.media.session.PlaybackState.ACTION_SEEK_TO)) :
                return android.media.RemoteControlClient.FLAG_KEY_MEDIA_POSITION_UPDATE;
            case ((int) (android.media.session.PlaybackState.ACTION_SET_RATING)) :
                return android.media.RemoteControlClient.FLAG_KEY_MEDIA_RATING;
        }
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.media.session.PlaybackState> CREATOR = new android.os.Parcelable.Creator<android.media.session.PlaybackState>() {
        @java.lang.Override
        public android.media.session.PlaybackState createFromParcel(android.os.Parcel in) {
            return new android.media.session.PlaybackState(in);
        }

        @java.lang.Override
        public android.media.session.PlaybackState[] newArray(int size) {
            return new android.media.session.PlaybackState[size];
        }
    };

    /**
     * {@link PlaybackState.CustomAction CustomActions} can be used to extend the capabilities of
     * the standard transport controls by exposing app specific actions to
     * {@link MediaController MediaControllers}.
     */
    public static final class CustomAction implements android.os.Parcelable {
        private final java.lang.String mAction;

        private final java.lang.CharSequence mName;

        private final int mIcon;

        private final android.os.Bundle mExtras;

        /**
         * Use {@link PlaybackState.CustomAction.Builder#build()}.
         */
        private CustomAction(java.lang.String action, java.lang.CharSequence name, int icon, android.os.Bundle extras) {
            mAction = action;
            mName = name;
            mIcon = icon;
            mExtras = extras;
        }

        private CustomAction(android.os.Parcel in) {
            mAction = in.readString();
            mName = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            mIcon = in.readInt();
            mExtras = in.readBundle();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(mAction);
            android.text.TextUtils.writeToParcel(mName, dest, flags);
            dest.writeInt(mIcon);
            dest.writeBundle(mExtras);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        public static final android.os.Parcelable.Creator<android.media.session.PlaybackState.CustomAction> CREATOR = new android.os.Parcelable.Creator<android.media.session.PlaybackState.CustomAction>() {
            @java.lang.Override
            public android.media.session.PlaybackState.CustomAction createFromParcel(android.os.Parcel p) {
                return new android.media.session.PlaybackState.CustomAction(p);
            }

            @java.lang.Override
            public android.media.session.PlaybackState.CustomAction[] newArray(int size) {
                return new android.media.session.PlaybackState.CustomAction[size];
            }
        };

        /**
         * Returns the action of the {@link CustomAction}.
         *
         * @return The action of the {@link CustomAction}.
         */
        public java.lang.String getAction() {
            return mAction;
        }

        /**
         * Returns the display name of this action. e.g. "Favorite"
         *
         * @return The display name of this {@link CustomAction}.
         */
        public java.lang.CharSequence getName() {
            return mName;
        }

        /**
         * Returns the resource id of the icon in the {@link MediaSession MediaSession's} package.
         *
         * @return The resource id of the icon in the {@link MediaSession MediaSession's} package.
         */
        public int getIcon() {
            return mIcon;
        }

        /**
         * Returns extras which provide additional application-specific information about the
         * action, or null if none. These arguments are meant to be consumed by a
         * {@link MediaController} if it knows how to handle them.
         *
         * @return Optional arguments for the {@link CustomAction}.
         */
        public android.os.Bundle getExtras() {
            return mExtras;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((("Action:" + "mName='") + mName) + ", mIcon=") + mIcon) + ", mExtras=") + mExtras;
        }

        /**
         * Builder for {@link CustomAction} objects.
         */
        public static final class Builder {
            private final java.lang.String mAction;

            private final java.lang.CharSequence mName;

            private final int mIcon;

            private android.os.Bundle mExtras;

            /**
             * Creates a {@link CustomAction} builder with the id, name, and icon set.
             *
             * @param action
             * 		The action of the {@link CustomAction}.
             * @param name
             * 		The display name of the {@link CustomAction}. This name will be displayed
             * 		along side the action if the UI supports it.
             * @param icon
             * 		The icon resource id of the {@link CustomAction}. This resource id
             * 		must be in the same package as the {@link MediaSession}. It will be
             * 		displayed with the custom action if the UI supports it.
             */
            public Builder(java.lang.String action, java.lang.CharSequence name, @android.annotation.DrawableRes
            int icon) {
                if (android.text.TextUtils.isEmpty(action)) {
                    throw new java.lang.IllegalArgumentException("You must specify an action to build a CustomAction.");
                }
                if (android.text.TextUtils.isEmpty(name)) {
                    throw new java.lang.IllegalArgumentException("You must specify a name to build a CustomAction.");
                }
                if (icon == 0) {
                    throw new java.lang.IllegalArgumentException("You must specify an icon resource id to build a CustomAction.");
                }
                mAction = action;
                mName = name;
                mIcon = icon;
            }

            /**
             * Set optional extras for the {@link CustomAction}. These extras are meant to be
             * consumed by a {@link MediaController} if it knows how to handle them.
             * Keys should be fully qualified (e.g. "com.example.MY_ARG") to avoid collisions.
             *
             * @param extras
             * 		Optional extras for the {@link CustomAction}.
             * @return this.
             */
            public android.media.session.PlaybackState.CustomAction.Builder setExtras(android.os.Bundle extras) {
                mExtras = extras;
                return this;
            }

            /**
             * Build and return the {@link CustomAction} instance with the specified values.
             *
             * @return A new {@link CustomAction} instance.
             */
            public android.media.session.PlaybackState.CustomAction build() {
                return new android.media.session.PlaybackState.CustomAction(mAction, mName, mIcon, mExtras);
            }
        }
    }

    /**
     * Builder for {@link PlaybackState} objects.
     */
    public static final class Builder {
        private final java.util.List<android.media.session.PlaybackState.CustomAction> mCustomActions = new java.util.ArrayList<>();

        private int mState;

        private long mPosition;

        private long mBufferedPosition;

        private float mSpeed;

        private long mActions;

        private java.lang.CharSequence mErrorMessage;

        private long mUpdateTime;

        private long mActiveItemId = android.media.session.MediaSession.QueueItem.UNKNOWN_ID;

        private android.os.Bundle mExtras;

        /**
         * Creates an initially empty state builder.
         */
        public Builder() {
        }

        /**
         * Creates a builder with the same initial values as those in the from
         * state.
         *
         * @param from
         * 		The state to use for initializing the builder.
         */
        public Builder(android.media.session.PlaybackState from) {
            if (from == null) {
                return;
            }
            mState = from.mState;
            mPosition = from.mPosition;
            mBufferedPosition = from.mBufferedPosition;
            mSpeed = from.mSpeed;
            mActions = from.mActions;
            if (from.mCustomActions != null) {
                mCustomActions.addAll(from.mCustomActions);
            }
            mErrorMessage = from.mErrorMessage;
            mUpdateTime = from.mUpdateTime;
            mActiveItemId = from.mActiveItemId;
            mExtras = from.mExtras;
        }

        /**
         * Set the current state of playback.
         * <p>
         * The position must be in ms and indicates the current playback
         * position within the item. If the position is unknown use
         * {@link #PLAYBACK_POSITION_UNKNOWN}. When not using an unknown
         * position the time at which the position was updated must be provided.
         * It is okay to use {@link SystemClock#elapsedRealtime()} if the
         * current position was just retrieved.
         * <p>
         * The speed is a multiple of normal playback and should be 0 when
         * paused and negative when rewinding. Normal playback speed is 1.0.
         * <p>
         * The state must be one of the following:
         * <ul>
         * <li> {@link PlaybackState#STATE_NONE}</li>
         * <li> {@link PlaybackState#STATE_STOPPED}</li>
         * <li> {@link PlaybackState#STATE_PLAYING}</li>
         * <li> {@link PlaybackState#STATE_PAUSED}</li>
         * <li> {@link PlaybackState#STATE_FAST_FORWARDING}</li>
         * <li> {@link PlaybackState#STATE_REWINDING}</li>
         * <li> {@link PlaybackState#STATE_BUFFERING}</li>
         * <li> {@link PlaybackState#STATE_ERROR}</li>
         * <li> {@link PlaybackState#STATE_CONNECTING}</li>
         * <li> {@link PlaybackState#STATE_SKIPPING_TO_PREVIOUS}</li>
         * <li> {@link PlaybackState#STATE_SKIPPING_TO_NEXT}</li>
         * <li> {@link PlaybackState#STATE_SKIPPING_TO_QUEUE_ITEM}</li>
         * </ul>
         *
         * @param state
         * 		The current state of playback.
         * @param position
         * 		The position in the current item in ms.
         * @param playbackSpeed
         * 		The current speed of playback as a multiple of
         * 		normal playback.
         * @param updateTime
         * 		The time in the {@link SystemClock#elapsedRealtime}
         * 		timebase that the position was updated at.
         * @return this
         */
        public android.media.session.PlaybackState.Builder setState(@android.media.session.PlaybackState.State
        int state, long position, float playbackSpeed, long updateTime) {
            mState = state;
            mPosition = position;
            mUpdateTime = updateTime;
            mSpeed = playbackSpeed;
            return this;
        }

        /**
         * Set the current state of playback.
         * <p>
         * The position must be in ms and indicates the current playback
         * position within the item. If the position is unknown use
         * {@link #PLAYBACK_POSITION_UNKNOWN}. The update time will be set to
         * the current {@link SystemClock#elapsedRealtime()}.
         * <p>
         * The speed is a multiple of normal playback and should be 0 when
         * paused and negative when rewinding. Normal playback speed is 1.0.
         * <p>
         * The state must be one of the following:
         * <ul>
         * <li> {@link PlaybackState#STATE_NONE}</li>
         * <li> {@link PlaybackState#STATE_STOPPED}</li>
         * <li> {@link PlaybackState#STATE_PLAYING}</li>
         * <li> {@link PlaybackState#STATE_PAUSED}</li>
         * <li> {@link PlaybackState#STATE_FAST_FORWARDING}</li>
         * <li> {@link PlaybackState#STATE_REWINDING}</li>
         * <li> {@link PlaybackState#STATE_BUFFERING}</li>
         * <li> {@link PlaybackState#STATE_ERROR}</li>
         * <li> {@link PlaybackState#STATE_CONNECTING}</li>
         * <li> {@link PlaybackState#STATE_SKIPPING_TO_PREVIOUS}</li>
         * <li> {@link PlaybackState#STATE_SKIPPING_TO_NEXT}</li>
         * <li> {@link PlaybackState#STATE_SKIPPING_TO_QUEUE_ITEM}</li>
         * </ul>
         *
         * @param state
         * 		The current state of playback.
         * @param position
         * 		The position in the current item in ms.
         * @param playbackSpeed
         * 		The current speed of playback as a multiple of
         * 		normal playback.
         * @return this
         */
        public android.media.session.PlaybackState.Builder setState(@android.media.session.PlaybackState.State
        int state, long position, float playbackSpeed) {
            return setState(state, position, playbackSpeed, android.os.SystemClock.elapsedRealtime());
        }

        /**
         * Set the current actions available on this session. This should use a
         * bitmask of possible actions.
         * <ul>
         * <li> {@link PlaybackState#ACTION_SKIP_TO_PREVIOUS}</li>
         * <li> {@link PlaybackState#ACTION_REWIND}</li>
         * <li> {@link PlaybackState#ACTION_PLAY}</li>
         * <li> {@link PlaybackState#ACTION_PAUSE}</li>
         * <li> {@link PlaybackState#ACTION_STOP}</li>
         * <li> {@link PlaybackState#ACTION_FAST_FORWARD}</li>
         * <li> {@link PlaybackState#ACTION_SKIP_TO_NEXT}</li>
         * <li> {@link PlaybackState#ACTION_SEEK_TO}</li>
         * <li> {@link PlaybackState#ACTION_SET_RATING}</li>
         * <li> {@link PlaybackState#ACTION_PLAY_PAUSE}</li>
         * <li> {@link PlaybackState#ACTION_PLAY_FROM_MEDIA_ID}</li>
         * <li> {@link PlaybackState#ACTION_PLAY_FROM_SEARCH}</li>
         * <li> {@link PlaybackState#ACTION_SKIP_TO_QUEUE_ITEM}</li>
         * <li> {@link PlaybackState#ACTION_PLAY_FROM_URI}</li>
         * <li> {@link PlaybackState#ACTION_PREPARE}</li>
         * <li> {@link PlaybackState#ACTION_PREPARE_FROM_MEDIA_ID}</li>
         * <li> {@link PlaybackState#ACTION_PREPARE_FROM_SEARCH}</li>
         * <li> {@link PlaybackState#ACTION_PREPARE_FROM_URI}</li>
         * </ul>
         *
         * @param actions
         * 		The set of actions allowed.
         * @return this
         */
        public android.media.session.PlaybackState.Builder setActions(@android.media.session.PlaybackState.Actions
        long actions) {
            mActions = actions;
            return this;
        }

        /**
         * Add a custom action to the playback state. Actions can be used to
         * expose additional functionality to {@link MediaController
         * MediaControllers} beyond what is offered by the standard transport
         * controls.
         * <p>
         * e.g. start a radio station based on the current item or skip ahead by
         * 30 seconds.
         *
         * @param action
         * 		An identifier for this action. It can be sent back to
         * 		the {@link MediaSession} through
         * 		{@link MediaController.TransportControls#sendCustomAction(String, Bundle)}.
         * @param name
         * 		The display name for the action. If text is shown with
         * 		the action or used for accessibility, this is what should
         * 		be used.
         * @param icon
         * 		The resource action of the icon that should be displayed
         * 		for the action. The resource should be in the package of
         * 		the {@link MediaSession}.
         * @return this
         */
        public android.media.session.PlaybackState.Builder addCustomAction(java.lang.String action, java.lang.String name, int icon) {
            return addCustomAction(new android.media.session.PlaybackState.CustomAction(action, name, icon, null));
        }

        /**
         * Add a custom action to the playback state. Actions can be used to expose additional
         * functionality to {@link MediaController MediaControllers} beyond what is offered by the
         * standard transport controls.
         * <p>
         * An example of an action would be to start a radio station based on the current item
         * or to skip ahead by 30 seconds.
         *
         * @param customAction
         * 		The custom action to add to the {@link PlaybackState}.
         * @return this
         */
        public android.media.session.PlaybackState.Builder addCustomAction(android.media.session.PlaybackState.CustomAction customAction) {
            if (customAction == null) {
                throw new java.lang.IllegalArgumentException("You may not add a null CustomAction to PlaybackState.");
            }
            mCustomActions.add(customAction);
            return this;
        }

        /**
         * Set the current buffered position in ms. This is the farthest
         * playback point that can be reached from the current position using
         * only buffered content.
         *
         * @param bufferedPosition
         * 		The position in ms that playback is buffered
         * 		to.
         * @return this
         */
        public android.media.session.PlaybackState.Builder setBufferedPosition(long bufferedPosition) {
            mBufferedPosition = bufferedPosition;
            return this;
        }

        /**
         * Set the active item in the play queue by specifying its id. The
         * default value is {@link MediaSession.QueueItem#UNKNOWN_ID}
         *
         * @param id
         * 		The id of the active item.
         * @return this
         */
        public android.media.session.PlaybackState.Builder setActiveQueueItemId(long id) {
            mActiveItemId = id;
            return this;
        }

        /**
         * Set a user readable error message. This should be set when the state
         * is {@link PlaybackState#STATE_ERROR}.
         *
         * @param error
         * 		The error message for display to the user.
         * @return this
         */
        public android.media.session.PlaybackState.Builder setErrorMessage(java.lang.CharSequence error) {
            mErrorMessage = error;
            return this;
        }

        /**
         * Set any custom extras to be included with the playback state.
         *
         * @param extras
         * 		The extras to include.
         * @return this
         */
        public android.media.session.PlaybackState.Builder setExtras(android.os.Bundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         * Build and return the {@link PlaybackState} instance with these
         * values.
         *
         * @return A new state instance.
         */
        public android.media.session.PlaybackState build() {
            return new android.media.session.PlaybackState(mState, mPosition, mUpdateTime, mSpeed, mBufferedPosition, mActions, mCustomActions, mActiveItemId, mErrorMessage, mExtras);
        }
    }
}

