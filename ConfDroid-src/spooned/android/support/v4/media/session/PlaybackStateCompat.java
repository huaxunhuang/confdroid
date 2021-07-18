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
 * Playback state for a {@link MediaSessionCompat}. This includes a state like
 * {@link PlaybackStateCompat#STATE_PLAYING}, the current playback position,
 * and the current control capabilities.
 */
public final class PlaybackStateCompat implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef(flag = true, value = { android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP, android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE, android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY, android.support.v4.media.session.PlaybackStateCompat.ACTION_REWIND, android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS, android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_NEXT, android.support.v4.media.session.PlaybackStateCompat.ACTION_FAST_FORWARD, android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_RATING, android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO, android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE, android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID, android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH, android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM, android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_URI, android.support.v4.media.session.PlaybackStateCompat.ACTION_PREPARE, android.support.v4.media.session.PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID, android.support.v4.media.session.PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH, android.support.v4.media.session.PlaybackStateCompat.ACTION_PREPARE_FROM_URI })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Actions {}

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef({ android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP, android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE, android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY, android.support.v4.media.session.PlaybackStateCompat.ACTION_REWIND, android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS, android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_NEXT, android.support.v4.media.session.PlaybackStateCompat.ACTION_FAST_FORWARD, android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface MediaKeyAction {}

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
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef({ android.support.v4.media.session.PlaybackStateCompat.STATE_NONE, android.support.v4.media.session.PlaybackStateCompat.STATE_STOPPED, android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED, android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING, android.support.v4.media.session.PlaybackStateCompat.STATE_FAST_FORWARDING, android.support.v4.media.session.PlaybackStateCompat.STATE_REWINDING, android.support.v4.media.session.PlaybackStateCompat.STATE_BUFFERING, android.support.v4.media.session.PlaybackStateCompat.STATE_ERROR, android.support.v4.media.session.PlaybackStateCompat.STATE_CONNECTING, android.support.v4.media.session.PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS, android.support.v4.media.session.PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, android.support.v4.media.session.PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface State {}

    /**
     * This is the default playback state and indicates that no media has been
     * added yet, or the performer has been reset and has no content to play.
     *
     * @see Builder#setState
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
     * route. Depending on the implementation you may return to the previous
     * state when the connection finishes or enter {@link #STATE_NONE}. If
     * the connection failed {@link #STATE_ERROR} should be used.
     * <p>
     * On devices earlier than API 21, this will appear as {@link #STATE_BUFFERING}
     * </p>
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
     * <p>
     * On devices earlier than API 21, this will appear as {@link #STATE_SKIPPING_TO_NEXT}
     * </p>
     *
     * @see Builder#setState
     */
    public static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;

    /**
     * Use this value for the position to indicate the position is not known.
     */
    public static final long PLAYBACK_POSITION_UNKNOWN = -1;

    // KeyEvent constants only available on API 11+
    private static final int KEYCODE_MEDIA_PAUSE = 127;

    private static final int KEYCODE_MEDIA_PLAY = 126;

    /**
     * Translates a given action into a matched key code defined in {@link KeyEvent}. The given
     * action should be one of the following:
     * <ul>
     * <li>{@link PlaybackStateCompat#ACTION_PLAY}</li>
     * <li>{@link PlaybackStateCompat#ACTION_PAUSE}</li>
     * <li>{@link PlaybackStateCompat#ACTION_SKIP_TO_NEXT}</li>
     * <li>{@link PlaybackStateCompat#ACTION_SKIP_TO_PREVIOUS}</li>
     * <li>{@link PlaybackStateCompat#ACTION_STOP}</li>
     * <li>{@link PlaybackStateCompat#ACTION_FAST_FORWARD}</li>
     * <li>{@link PlaybackStateCompat#ACTION_REWIND}</li>
     * <li>{@link PlaybackStateCompat#ACTION_PLAY_PAUSE}</li>
     * </ul>
     *
     * @param action
     * 		The action to be translated.
     * @return the key code matched to the given action.
     */
    public static int toKeyCode(@android.support.v4.media.session.PlaybackStateCompat.MediaKeyAction
    long action) {
        if (action == android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY) {
            return android.support.v4.media.session.PlaybackStateCompat.KEYCODE_MEDIA_PLAY;
        } else
            if (action == android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE) {
                return android.support.v4.media.session.PlaybackStateCompat.KEYCODE_MEDIA_PAUSE;
            } else
                if (action == android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_NEXT) {
                    return android.view.KeyEvent.KEYCODE_MEDIA_NEXT;
                } else
                    if (action == android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) {
                        return android.view.KeyEvent.KEYCODE_MEDIA_PREVIOUS;
                    } else
                        if (action == android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP) {
                            return android.view.KeyEvent.KEYCODE_MEDIA_STOP;
                        } else
                            if (action == android.support.v4.media.session.PlaybackStateCompat.ACTION_FAST_FORWARD) {
                                return android.view.KeyEvent.KEYCODE_MEDIA_FAST_FORWARD;
                            } else
                                if (action == android.support.v4.media.session.PlaybackStateCompat.ACTION_REWIND) {
                                    return android.view.KeyEvent.KEYCODE_MEDIA_REWIND;
                                } else
                                    if (action == android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE) {
                                        return android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
                                    }







        return android.view.KeyEvent.KEYCODE_UNKNOWN;
    }

    final int mState;

    final long mPosition;

    final long mBufferedPosition;

    final float mSpeed;

    final long mActions;

    final java.lang.CharSequence mErrorMessage;

    final long mUpdateTime;

    java.util.List<android.support.v4.media.session.PlaybackStateCompat.CustomAction> mCustomActions;

    final long mActiveItemId;

    final android.os.Bundle mExtras;

    private java.lang.Object mStateObj;

    PlaybackStateCompat(int state, long position, long bufferedPosition, float rate, long actions, java.lang.CharSequence errorMessage, long updateTime, java.util.List<android.support.v4.media.session.PlaybackStateCompat.CustomAction> customActions, long activeItemId, android.os.Bundle extras) {
        mState = state;
        mPosition = position;
        mBufferedPosition = bufferedPosition;
        mSpeed = rate;
        mActions = actions;
        mErrorMessage = errorMessage;
        mUpdateTime = updateTime;
        mCustomActions = new java.util.ArrayList<>(customActions);
        mActiveItemId = activeItemId;
        mExtras = extras;
    }

    PlaybackStateCompat(android.os.Parcel in) {
        mState = in.readInt();
        mPosition = in.readLong();
        mSpeed = in.readFloat();
        mUpdateTime = in.readLong();
        mBufferedPosition = in.readLong();
        mActions = in.readLong();
        mErrorMessage = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        mCustomActions = in.createTypedArrayList(android.support.v4.media.session.PlaybackStateCompat.CustomAction.CREATOR);
        mActiveItemId = in.readLong();
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
        bob.append(", error=").append(mErrorMessage);
        bob.append(", custom actions=").append(mCustomActions);
        bob.append(", active item id=").append(mActiveItemId);
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
        android.text.TextUtils.writeToParcel(mErrorMessage, dest, flags);
        dest.writeTypedList(mCustomActions);
        dest.writeLong(mActiveItemId);
        dest.writeBundle(mExtras);
    }

    /**
     * Get the current state of playback. One of the following:
     * <ul>
     * <li> {@link PlaybackStateCompat#STATE_NONE}</li>
     * <li> {@link PlaybackStateCompat#STATE_STOPPED}</li>
     * <li> {@link PlaybackStateCompat#STATE_PLAYING}</li>
     * <li> {@link PlaybackStateCompat#STATE_PAUSED}</li>
     * <li> {@link PlaybackStateCompat#STATE_FAST_FORWARDING}</li>
     * <li> {@link PlaybackStateCompat#STATE_REWINDING}</li>
     * <li> {@link PlaybackStateCompat#STATE_BUFFERING}</li>
     * <li> {@link PlaybackStateCompat#STATE_ERROR}</li>
     * <li> {@link PlaybackStateCompat#STATE_CONNECTING}</li>
     * <li> {@link PlaybackStateCompat#STATE_SKIPPING_TO_PREVIOUS}</li>
     * <li> {@link PlaybackStateCompat#STATE_SKIPPING_TO_NEXT}</li>
     * <li> {@link PlaybackStateCompat#STATE_SKIPPING_TO_QUEUE_ITEM}</li>
     */
    @android.support.v4.media.session.PlaybackStateCompat.State
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
     * <li> {@link PlaybackStateCompat#ACTION_SKIP_TO_PREVIOUS}</li>
     * <li> {@link PlaybackStateCompat#ACTION_REWIND}</li>
     * <li> {@link PlaybackStateCompat#ACTION_PLAY}</li>
     * <li> {@link PlaybackStateCompat#ACTION_PLAY_PAUSE}</li>
     * <li> {@link PlaybackStateCompat#ACTION_PAUSE}</li>
     * <li> {@link PlaybackStateCompat#ACTION_STOP}</li>
     * <li> {@link PlaybackStateCompat#ACTION_FAST_FORWARD}</li>
     * <li> {@link PlaybackStateCompat#ACTION_SKIP_TO_NEXT}</li>
     * <li> {@link PlaybackStateCompat#ACTION_SEEK_TO}</li>
     * <li> {@link PlaybackStateCompat#ACTION_SET_RATING}</li>
     * <li> {@link PlaybackStateCompat#ACTION_PLAY_FROM_MEDIA_ID}</li>
     * <li> {@link PlaybackStateCompat#ACTION_PLAY_FROM_SEARCH}</li>
     * <li> {@link PlaybackStateCompat#ACTION_SKIP_TO_QUEUE_ITEM}</li>
     * <li> {@link PlaybackStateCompat#ACTION_PLAY_FROM_URI}</li>
     * <li> {@link PlaybackStateCompat#ACTION_PREPARE}</li>
     * <li> {@link PlaybackStateCompat#ACTION_PREPARE_FROM_MEDIA_ID}</li>
     * <li> {@link PlaybackStateCompat#ACTION_PREPARE_FROM_SEARCH}</li>
     * <li> {@link PlaybackStateCompat#ACTION_PREPARE_FROM_URI}</li>
     * </ul>
     */
    @android.support.v4.media.session.PlaybackStateCompat.Actions
    public long getActions() {
        return mActions;
    }

    /**
     * Get the list of custom actions.
     */
    public java.util.List<android.support.v4.media.session.PlaybackStateCompat.CustomAction> getCustomActions() {
        return mCustomActions;
    }

    /**
     * Get a user readable error message. This should be set when the state is
     * {@link PlaybackStateCompat#STATE_ERROR}.
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
     * {@link MediaSessionCompat.QueueItem#UNKNOWN_ID}.
     *
     * @return The id of the currently active item in the queue or
    {@link MediaSessionCompat.QueueItem#UNKNOWN_ID}.
     */
    public long getActiveQueueItemId() {
        return mActiveItemId;
    }

    /**
     * Get any custom extras that were set on this playback state.
     *
     * @return The extras for this state or null.
     */
    @android.support.annotation.Nullable
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    /**
     * Creates an instance from a framework {@link android.media.session.PlaybackState} object.
     * <p>
     * This method is only supported on API 21+.
     * </p>
     *
     * @param stateObj
     * 		A {@link android.media.session.PlaybackState} object, or null if none.
     * @return An equivalent {@link PlaybackStateCompat} object, or null if none.
     */
    public static android.support.v4.media.session.PlaybackStateCompat fromPlaybackState(java.lang.Object stateObj) {
        if ((stateObj == null) || (android.os.Build.VERSION.SDK_INT < 21)) {
            return null;
        }
        java.util.List<java.lang.Object> customActionObjs = android.support.v4.media.session.PlaybackStateCompatApi21.getCustomActions(stateObj);
        java.util.List<android.support.v4.media.session.PlaybackStateCompat.CustomAction> customActions = null;
        if (customActionObjs != null) {
            customActions = new java.util.ArrayList<>(customActionObjs.size());
            for (java.lang.Object customActionObj : customActionObjs) {
                customActions.add(android.support.v4.media.session.PlaybackStateCompat.CustomAction.fromCustomAction(customActionObj));
            }
        }
        android.os.Bundle extras = (android.os.Build.VERSION.SDK_INT >= 22) ? android.support.v4.media.session.PlaybackStateCompatApi22.getExtras(stateObj) : null;
        android.support.v4.media.session.PlaybackStateCompat state = new android.support.v4.media.session.PlaybackStateCompat(android.support.v4.media.session.PlaybackStateCompatApi21.getState(stateObj), android.support.v4.media.session.PlaybackStateCompatApi21.getPosition(stateObj), android.support.v4.media.session.PlaybackStateCompatApi21.getBufferedPosition(stateObj), android.support.v4.media.session.PlaybackStateCompatApi21.getPlaybackSpeed(stateObj), android.support.v4.media.session.PlaybackStateCompatApi21.getActions(stateObj), android.support.v4.media.session.PlaybackStateCompatApi21.getErrorMessage(stateObj), android.support.v4.media.session.PlaybackStateCompatApi21.getLastPositionUpdateTime(stateObj), customActions, android.support.v4.media.session.PlaybackStateCompatApi21.getActiveQueueItemId(stateObj), extras);
        state.mStateObj = stateObj;
        return state;
    }

    /**
     * Gets the underlying framework {@link android.media.session.PlaybackState} object.
     * <p>
     * This method is only supported on API 21+.
     * </p>
     *
     * @return An equivalent {@link android.media.session.PlaybackState} object, or null if none.
     */
    public java.lang.Object getPlaybackState() {
        if ((mStateObj != null) || (android.os.Build.VERSION.SDK_INT < 21)) {
            return mStateObj;
        }
        java.util.List<java.lang.Object> customActions = null;
        if (mCustomActions != null) {
            customActions = new java.util.ArrayList<>(mCustomActions.size());
            for (android.support.v4.media.session.PlaybackStateCompat.CustomAction customAction : mCustomActions) {
                customActions.add(customAction.getCustomAction());
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= 22) {
            mStateObj = android.support.v4.media.session.PlaybackStateCompatApi22.newInstance(mState, mPosition, mBufferedPosition, mSpeed, mActions, mErrorMessage, mUpdateTime, customActions, mActiveItemId, mExtras);
        } else {
            mStateObj = android.support.v4.media.session.PlaybackStateCompatApi21.newInstance(mState, mPosition, mBufferedPosition, mSpeed, mActions, mErrorMessage, mUpdateTime, customActions, mActiveItemId);
        }
        return mStateObj;
    }

    public static final android.os.Parcelable.Creator<android.support.v4.media.session.PlaybackStateCompat> CREATOR = new android.os.Parcelable.Creator<android.support.v4.media.session.PlaybackStateCompat>() {
        @java.lang.Override
        public android.support.v4.media.session.PlaybackStateCompat createFromParcel(android.os.Parcel in) {
            return new android.support.v4.media.session.PlaybackStateCompat(in);
        }

        @java.lang.Override
        public android.support.v4.media.session.PlaybackStateCompat[] newArray(int size) {
            return new android.support.v4.media.session.PlaybackStateCompat[size];
        }
    };

    /**
     * {@link PlaybackStateCompat.CustomAction CustomActions} can be used to
     * extend the capabilities of the standard transport controls by exposing
     * app specific actions to {@link MediaControllerCompat Controllers}.
     */
    public static final class CustomAction implements android.os.Parcelable {
        private final java.lang.String mAction;

        private final java.lang.CharSequence mName;

        private final int mIcon;

        private final android.os.Bundle mExtras;

        private java.lang.Object mCustomActionObj;

        /**
         * Use {@link PlaybackStateCompat.CustomAction.Builder#build()}.
         */
        CustomAction(java.lang.String action, java.lang.CharSequence name, int icon, android.os.Bundle extras) {
            mAction = action;
            mName = name;
            mIcon = icon;
            mExtras = extras;
        }

        CustomAction(android.os.Parcel in) {
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

        /**
         * Creates an instance from a framework
         * {@link android.media.session.PlaybackState.CustomAction} object.
         * <p>
         * This method is only supported on API 21+.
         * </p>
         *
         * @param customActionObj
         * 		A {@link android.media.session.PlaybackState.CustomAction} object,
         * 		or null if none.
         * @return An equivalent {@link PlaybackStateCompat.CustomAction} object, or null if none.
         */
        public static android.support.v4.media.session.PlaybackStateCompat.CustomAction fromCustomAction(java.lang.Object customActionObj) {
            if ((customActionObj == null) || (android.os.Build.VERSION.SDK_INT < 21)) {
                return null;
            }
            android.support.v4.media.session.PlaybackStateCompat.CustomAction customAction = new android.support.v4.media.session.PlaybackStateCompat.CustomAction(android.support.v4.media.session.PlaybackStateCompatApi21.CustomAction.getAction(customActionObj), android.support.v4.media.session.PlaybackStateCompatApi21.CustomAction.getName(customActionObj), android.support.v4.media.session.PlaybackStateCompatApi21.CustomAction.getIcon(customActionObj), android.support.v4.media.session.PlaybackStateCompatApi21.CustomAction.getExtras(customActionObj));
            customAction.mCustomActionObj = customActionObj;
            return customAction;
        }

        /**
         * Gets the underlying framework {@link android.media.session.PlaybackState.CustomAction}
         * object.
         * <p>
         * This method is only supported on API 21+.
         * </p>
         *
         * @return An equivalent {@link android.media.session.PlaybackState.CustomAction} object,
        or null if none.
         */
        public java.lang.Object getCustomAction() {
            if ((mCustomActionObj != null) || (android.os.Build.VERSION.SDK_INT < 21)) {
                return mCustomActionObj;
            }
            mCustomActionObj = android.support.v4.media.session.PlaybackStateCompatApi21.CustomAction.newInstance(mAction, mName, mIcon, mExtras);
            return mCustomActionObj;
        }

        public static final android.os.Parcelable.Creator<android.support.v4.media.session.PlaybackStateCompat.CustomAction> CREATOR = new android.os.Parcelable.Creator<android.support.v4.media.session.PlaybackStateCompat.CustomAction>() {
            @java.lang.Override
            public android.support.v4.media.session.PlaybackStateCompat.CustomAction createFromParcel(android.os.Parcel p) {
                return new android.support.v4.media.session.PlaybackStateCompat.CustomAction(p);
            }

            @java.lang.Override
            public android.support.v4.media.session.PlaybackStateCompat.CustomAction[] newArray(int size) {
                return new android.support.v4.media.session.PlaybackStateCompat.CustomAction[size];
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
         * Returns the resource id of the icon in the {@link MediaSessionCompat
         * Session's} package.
         *
         * @return The resource id of the icon in the {@link MediaSessionCompat
        Session's} package.
         */
        public int getIcon() {
            return mIcon;
        }

        /**
         * Returns extras which provide additional application-specific
         * information about the action, or null if none. These arguments are
         * meant to be consumed by a {@link MediaControllerCompat} if it knows
         * how to handle them.
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
             * Creates a {@link CustomAction} builder with the id, name, and
             * icon set.
             *
             * @param action
             * 		The action of the {@link CustomAction}.
             * @param name
             * 		The display name of the {@link CustomAction}. This
             * 		name will be displayed along side the action if the UI
             * 		supports it.
             * @param icon
             * 		The icon resource id of the {@link CustomAction}.
             * 		This resource id must be in the same package as the
             * 		{@link MediaSessionCompat}. It will be displayed with
             * 		the custom action if the UI supports it.
             */
            public Builder(java.lang.String action, java.lang.CharSequence name, int icon) {
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
             * Set optional extras for the {@link CustomAction}. These extras
             * are meant to be consumed by a {@link MediaControllerCompat} if it
             * knows how to handle them. Keys should be fully qualified (e.g.
             * "com.example.MY_ARG") to avoid collisions.
             *
             * @param extras
             * 		Optional extras for the {@link CustomAction}.
             * @return this.
             */
            public android.support.v4.media.session.PlaybackStateCompat.CustomAction.Builder setExtras(android.os.Bundle extras) {
                mExtras = extras;
                return this;
            }

            /**
             * Build and return the {@link CustomAction} instance with the
             * specified values.
             *
             * @return A new {@link CustomAction} instance.
             */
            public android.support.v4.media.session.PlaybackStateCompat.CustomAction build() {
                return new android.support.v4.media.session.PlaybackStateCompat.CustomAction(mAction, mName, mIcon, mExtras);
            }
        }
    }

    /**
     * Builder for {@link PlaybackStateCompat} objects.
     */
    public static final class Builder {
        private final java.util.List<android.support.v4.media.session.PlaybackStateCompat.CustomAction> mCustomActions = new java.util.ArrayList<>();

        private int mState;

        private long mPosition;

        private long mBufferedPosition;

        private float mRate;

        private long mActions;

        private java.lang.CharSequence mErrorMessage;

        private long mUpdateTime;

        private long mActiveItemId = android.support.v4.media.session.MediaSessionCompat.QueueItem.UNKNOWN_ID;

        private android.os.Bundle mExtras;

        /**
         * Create an empty Builder.
         */
        public Builder() {
        }

        /**
         * Create a Builder using a {@link PlaybackStateCompat} instance to set the
         * initial values.
         *
         * @param source
         * 		The playback state to copy.
         */
        public Builder(android.support.v4.media.session.PlaybackStateCompat source) {
            mState = source.mState;
            mPosition = source.mPosition;
            mRate = source.mSpeed;
            mUpdateTime = source.mUpdateTime;
            mBufferedPosition = source.mBufferedPosition;
            mActions = source.mActions;
            mErrorMessage = source.mErrorMessage;
            if (source.mCustomActions != null) {
                mCustomActions.addAll(source.mCustomActions);
            }
            mActiveItemId = source.mActiveItemId;
            mExtras = source.mExtras;
        }

        /**
         * Set the current state of playback.
         * <p>
         * The position must be in ms and indicates the current playback
         * position within the track. If the position is unknown use
         * {@link #PLAYBACK_POSITION_UNKNOWN}.
         * <p>
         * The rate is a multiple of normal playback and should be 0 when paused
         * and negative when rewinding. Normal playback rate is 1.0.
         * <p>
         * The state must be one of the following:
         * <ul>
         * <li> {@link PlaybackStateCompat#STATE_NONE}</li>
         * <li> {@link PlaybackStateCompat#STATE_STOPPED}</li>
         * <li> {@link PlaybackStateCompat#STATE_PLAYING}</li>
         * <li> {@link PlaybackStateCompat#STATE_PAUSED}</li>
         * <li> {@link PlaybackStateCompat#STATE_FAST_FORWARDING}</li>
         * <li> {@link PlaybackStateCompat#STATE_REWINDING}</li>
         * <li> {@link PlaybackStateCompat#STATE_BUFFERING}</li>
         * <li> {@link PlaybackStateCompat#STATE_ERROR}</li>
         * <li> {@link PlaybackStateCompat#STATE_CONNECTING}</li>
         * <li> {@link PlaybackStateCompat#STATE_SKIPPING_TO_PREVIOUS}</li>
         * <li> {@link PlaybackStateCompat#STATE_SKIPPING_TO_NEXT}</li>
         * <li> {@link PlaybackStateCompat#STATE_SKIPPING_TO_QUEUE_ITEM}</li>
         * </ul>
         *
         * @param state
         * 		The current state of playback.
         * @param position
         * 		The position in the current track in ms.
         * @param playbackSpeed
         * 		The current rate of playback as a multiple of
         * 		normal playback.
         */
        public android.support.v4.media.session.PlaybackStateCompat.Builder setState(@android.support.v4.media.session.PlaybackStateCompat.State
        int state, long position, float playbackSpeed) {
            return setState(state, position, playbackSpeed, android.os.SystemClock.elapsedRealtime());
        }

        /**
         * Set the current state of playback.
         * <p>
         * The position must be in ms and indicates the current playback
         * position within the track. If the position is unknown use
         * {@link #PLAYBACK_POSITION_UNKNOWN}.
         * <p>
         * The rate is a multiple of normal playback and should be 0 when paused
         * and negative when rewinding. Normal playback rate is 1.0.
         * <p>
         * The state must be one of the following:
         * <ul>
         * <li> {@link PlaybackStateCompat#STATE_NONE}</li>
         * <li> {@link PlaybackStateCompat#STATE_STOPPED}</li>
         * <li> {@link PlaybackStateCompat#STATE_PLAYING}</li>
         * <li> {@link PlaybackStateCompat#STATE_PAUSED}</li>
         * <li> {@link PlaybackStateCompat#STATE_FAST_FORWARDING}</li>
         * <li> {@link PlaybackStateCompat#STATE_REWINDING}</li>
         * <li> {@link PlaybackStateCompat#STATE_BUFFERING}</li>
         * <li> {@link PlaybackStateCompat#STATE_ERROR}</li>
         * <li> {@link PlaybackStateCompat#STATE_CONNECTING}</li>
         * <li> {@link PlaybackStateCompat#STATE_SKIPPING_TO_PREVIOUS}</li>
         * <li> {@link PlaybackStateCompat#STATE_SKIPPING_TO_NEXT}</li>
         * <li> {@link PlaybackStateCompat#STATE_SKIPPING_TO_QUEUE_ITEM}</li>
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
        public android.support.v4.media.session.PlaybackStateCompat.Builder setState(@android.support.v4.media.session.PlaybackStateCompat.State
        int state, long position, float playbackSpeed, long updateTime) {
            mState = state;
            mPosition = position;
            mUpdateTime = updateTime;
            mRate = playbackSpeed;
            return this;
        }

        /**
         * Set the current buffered position in ms. This is the farthest
         * playback point that can be reached from the current position using
         * only buffered content.
         *
         * @return this
         */
        public android.support.v4.media.session.PlaybackStateCompat.Builder setBufferedPosition(long bufferPosition) {
            mBufferedPosition = bufferPosition;
            return this;
        }

        /**
         * Set the current capabilities available on this session. This should
         * use a bitmask of the available capabilities.
         * <ul>
         * <li> {@link PlaybackStateCompat#ACTION_SKIP_TO_PREVIOUS}</li>
         * <li> {@link PlaybackStateCompat#ACTION_REWIND}</li>
         * <li> {@link PlaybackStateCompat#ACTION_PLAY}</li>
         * <li> {@link PlaybackStateCompat#ACTION_PLAY_PAUSE}</li>
         * <li> {@link PlaybackStateCompat#ACTION_PAUSE}</li>
         * <li> {@link PlaybackStateCompat#ACTION_STOP}</li>
         * <li> {@link PlaybackStateCompat#ACTION_FAST_FORWARD}</li>
         * <li> {@link PlaybackStateCompat#ACTION_SKIP_TO_NEXT}</li>
         * <li> {@link PlaybackStateCompat#ACTION_SEEK_TO}</li>
         * <li> {@link PlaybackStateCompat#ACTION_SET_RATING}</li>
         * <li> {@link PlaybackStateCompat#ACTION_PLAY_FROM_MEDIA_ID}</li>
         * <li> {@link PlaybackStateCompat#ACTION_PLAY_FROM_SEARCH}</li>
         * <li> {@link PlaybackStateCompat#ACTION_SKIP_TO_QUEUE_ITEM}</li>
         * <li> {@link PlaybackStateCompat#ACTION_PLAY_FROM_URI}</li>
         * <li> {@link PlaybackStateCompat#ACTION_PREPARE}</li>
         * <li> {@link PlaybackStateCompat#ACTION_PREPARE_FROM_MEDIA_ID}</li>
         * <li> {@link PlaybackStateCompat#ACTION_PREPARE_FROM_SEARCH}</li>
         * <li> {@link PlaybackStateCompat#ACTION_PREPARE_FROM_URI}</li>
         * </ul>
         *
         * @return this
         */
        public android.support.v4.media.session.PlaybackStateCompat.Builder setActions(@android.support.v4.media.session.PlaybackStateCompat.Actions
        long capabilities) {
            mActions = capabilities;
            return this;
        }

        /**
         * Add a custom action to the playback state. Actions can be used to
         * expose additional functionality to {@link MediaControllerCompat
         * Controllers} beyond what is offered by the standard transport
         * controls.
         * <p>
         * e.g. start a radio station based on the current item or skip ahead by
         * 30 seconds.
         *
         * @param action
         * 		An identifier for this action. It can be sent back to
         * 		the {@link MediaSessionCompat} through
         * 		{@link MediaControllerCompat.TransportControls#sendCustomAction(String, Bundle)}.
         * @param name
         * 		The display name for the action. If text is shown with
         * 		the action or used for accessibility, this is what should
         * 		be used.
         * @param icon
         * 		The resource action of the icon that should be displayed
         * 		for the action. The resource should be in the package of
         * 		the {@link MediaSessionCompat}.
         * @return this
         */
        public android.support.v4.media.session.PlaybackStateCompat.Builder addCustomAction(java.lang.String action, java.lang.String name, int icon) {
            return addCustomAction(new android.support.v4.media.session.PlaybackStateCompat.CustomAction(action, name, icon, null));
        }

        /**
         * Add a custom action to the playback state. Actions can be used to expose additional
         * functionality to {@link MediaControllerCompat Controllers} beyond what is offered
         * by the standard transport controls.
         * <p>
         * An example of an action would be to start a radio station based on the current item
         * or to skip ahead by 30 seconds.
         *
         * @param customAction
         * 		The custom action to add to the {@link PlaybackStateCompat}.
         * @return this
         */
        public android.support.v4.media.session.PlaybackStateCompat.Builder addCustomAction(android.support.v4.media.session.PlaybackStateCompat.CustomAction customAction) {
            if (customAction == null) {
                throw new java.lang.IllegalArgumentException("You may not add a null CustomAction to PlaybackStateCompat.");
            }
            mCustomActions.add(customAction);
            return this;
        }

        /**
         * Set the active item in the play queue by specifying its id. The
         * default value is {@link MediaSessionCompat.QueueItem#UNKNOWN_ID}
         *
         * @param id
         * 		The id of the active item.
         * @return this
         */
        public android.support.v4.media.session.PlaybackStateCompat.Builder setActiveQueueItemId(long id) {
            mActiveItemId = id;
            return this;
        }

        /**
         * Set a user readable error message. This should be set when the state
         * is {@link PlaybackStateCompat#STATE_ERROR}.
         *
         * @return this
         */
        public android.support.v4.media.session.PlaybackStateCompat.Builder setErrorMessage(java.lang.CharSequence errorMessage) {
            mErrorMessage = errorMessage;
            return this;
        }

        /**
         * Set any custom extras to be included with the playback state.
         *
         * @param extras
         * 		The extras to include.
         * @return this
         */
        public android.support.v4.media.session.PlaybackStateCompat.Builder setExtras(android.os.Bundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         * Creates the playback state object.
         */
        public android.support.v4.media.session.PlaybackStateCompat build() {
            return new android.support.v4.media.session.PlaybackStateCompat(mState, mPosition, mBufferedPosition, mRate, mActions, mErrorMessage, mUpdateTime, mCustomActions, mActiveItemId, mExtras);
        }
    }
}

