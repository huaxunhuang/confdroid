/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v7.media;


/**
 * Describes the playback status of a media session.
 * <p>
 * This class is part of the remote playback protocol described by the
 * {@link MediaControlIntent MediaControlIntent} class.
 * </p><p>
 * When a media session is created, it is initially in the
 * {@link #SESSION_STATE_ACTIVE active} state.  When the media session ends
 * normally, it transitions to the {@link #SESSION_STATE_ENDED ended} state.
 * If the media session is invalidated due to another session forcibly taking
 * control of the route, then it transitions to the
 * {@link #SESSION_STATE_INVALIDATED invalidated} state.
 * Refer to the documentation of each state for an explanation of its meaning.
 * </p><p>
 * To monitor session status, the application should supply a {@link PendingIntent} to use as the
 * {@link MediaControlIntent#EXTRA_SESSION_STATUS_UPDATE_RECEIVER session status update receiver}
 * for a given {@link MediaControlIntent#ACTION_START_SESSION session start request}.
 * </p><p>
 * This object is immutable once created using a {@link Builder} instance.
 * </p>
 */
public final class MediaSessionStatus {
    static final java.lang.String KEY_TIMESTAMP = "timestamp";

    static final java.lang.String KEY_SESSION_STATE = "sessionState";

    static final java.lang.String KEY_QUEUE_PAUSED = "queuePaused";

    static final java.lang.String KEY_EXTRAS = "extras";

    final android.os.Bundle mBundle;

    /**
     * Session state: Active.
     * <p>
     * Indicates that the media session is active and in control of the route.
     * </p>
     */
    public static final int SESSION_STATE_ACTIVE = 0;

    /**
     * Session state: Ended.
     * <p>
     * Indicates that the media session was ended normally using the
     * {@link MediaControlIntent#ACTION_END_SESSION end session} action.
     * </p><p>
     * A terminated media session cannot be used anymore.  To play more media, the
     * application must start a new session.
     * </p>
     */
    public static final int SESSION_STATE_ENDED = 1;

    /**
     * Session state: Invalidated.
     * <p>
     * Indicates that the media session was invalidated involuntarily due to
     * another session taking control of the route.
     * </p><p>
     * An invalidated media session cannot be used anymore.  To play more media, the
     * application must start a new session.
     * </p>
     */
    public static final int SESSION_STATE_INVALIDATED = 2;

    MediaSessionStatus(android.os.Bundle bundle) {
        mBundle = bundle;
    }

    /**
     * Gets the timestamp associated with the status information in
     * milliseconds since boot in the {@link SystemClock#elapsedRealtime} time base.
     *
     * @return The status timestamp in the {@link SystemClock#elapsedRealtime()} time base.
     */
    public long getTimestamp() {
        return mBundle.getLong(android.support.v7.media.MediaSessionStatus.KEY_TIMESTAMP);
    }

    /**
     * Gets the session state.
     *
     * @return The session state.  One of {@link #SESSION_STATE_ACTIVE},
    {@link #SESSION_STATE_ENDED}, or {@link #SESSION_STATE_INVALIDATED}.
     */
    public int getSessionState() {
        return mBundle.getInt(android.support.v7.media.MediaSessionStatus.KEY_SESSION_STATE, android.support.v7.media.MediaSessionStatus.SESSION_STATE_INVALIDATED);
    }

    /**
     * Returns true if the session's queue is paused.
     *
     * @return True if the session's queue is paused.
     */
    public boolean isQueuePaused() {
        return mBundle.getBoolean(android.support.v7.media.MediaSessionStatus.KEY_QUEUE_PAUSED);
    }

    /**
     * Gets a bundle of extras for this status object.
     * The extras will be ignored by the media router but they may be used
     * by applications.
     */
    public android.os.Bundle getExtras() {
        return mBundle.getBundle(android.support.v7.media.MediaSessionStatus.KEY_EXTRAS);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        result.append("MediaSessionStatus{ ");
        result.append("timestamp=");
        android.support.v4.util.TimeUtils.formatDuration(android.os.SystemClock.elapsedRealtime() - getTimestamp(), result);
        result.append(" ms ago");
        result.append(", sessionState=").append(android.support.v7.media.MediaSessionStatus.sessionStateToString(getSessionState()));
        result.append(", queuePaused=").append(isQueuePaused());
        result.append(", extras=").append(getExtras());
        result.append(" }");
        return result.toString();
    }

    private static java.lang.String sessionStateToString(int sessionState) {
        switch (sessionState) {
            case android.support.v7.media.MediaSessionStatus.SESSION_STATE_ACTIVE :
                return "active";
            case android.support.v7.media.MediaSessionStatus.SESSION_STATE_ENDED :
                return "ended";
            case android.support.v7.media.MediaSessionStatus.SESSION_STATE_INVALIDATED :
                return "invalidated";
        }
        return java.lang.Integer.toString(sessionState);
    }

    /**
     * Converts this object to a bundle for serialization.
     *
     * @return The contents of the object represented as a bundle.
     */
    public android.os.Bundle asBundle() {
        return mBundle;
    }

    /**
     * Creates an instance from a bundle.
     *
     * @param bundle
     * 		The bundle, or null if none.
     * @return The new instance, or null if the bundle was null.
     */
    public static android.support.v7.media.MediaSessionStatus fromBundle(android.os.Bundle bundle) {
        return bundle != null ? new android.support.v7.media.MediaSessionStatus(bundle) : null;
    }

    /**
     * Builder for {@link MediaSessionStatus media session status objects}.
     */
    public static final class Builder {
        private final android.os.Bundle mBundle;

        /**
         * Creates a media session status builder using the current time as the
         * reference timestamp.
         *
         * @param sessionState
         * 		The session state.
         */
        public Builder(int sessionState) {
            mBundle = new android.os.Bundle();
            setTimestamp(android.os.SystemClock.elapsedRealtime());
            setSessionState(sessionState);
        }

        /**
         * Creates a media session status builder whose initial contents are
         * copied from an existing status.
         */
        public Builder(android.support.v7.media.MediaSessionStatus status) {
            if (status == null) {
                throw new java.lang.IllegalArgumentException("status must not be null");
            }
            mBundle = new android.os.Bundle(status.mBundle);
        }

        /**
         * Sets the timestamp associated with the status information in
         * milliseconds since boot in the {@link SystemClock#elapsedRealtime} time base.
         */
        public android.support.v7.media.MediaSessionStatus.Builder setTimestamp(long elapsedRealtimeTimestamp) {
            mBundle.putLong(android.support.v7.media.MediaSessionStatus.KEY_TIMESTAMP, elapsedRealtimeTimestamp);
            return this;
        }

        /**
         * Sets the session state.
         */
        public android.support.v7.media.MediaSessionStatus.Builder setSessionState(int sessionState) {
            mBundle.putInt(android.support.v7.media.MediaSessionStatus.KEY_SESSION_STATE, sessionState);
            return this;
        }

        /**
         * Sets whether the queue is paused.
         */
        public android.support.v7.media.MediaSessionStatus.Builder setQueuePaused(boolean queuePaused) {
            mBundle.putBoolean(android.support.v7.media.MediaSessionStatus.KEY_QUEUE_PAUSED, queuePaused);
            return this;
        }

        /**
         * Sets a bundle of extras for this status object.
         * The extras will be ignored by the media router but they may be used
         * by applications.
         */
        public android.support.v7.media.MediaSessionStatus.Builder setExtras(android.os.Bundle extras) {
            mBundle.putBundle(android.support.v7.media.MediaSessionStatus.KEY_EXTRAS, extras);
            return this;
        }

        /**
         * Builds the {@link MediaSessionStatus media session status object}.
         */
        public android.support.v7.media.MediaSessionStatus build() {
            return new android.support.v7.media.MediaSessionStatus(mBundle);
        }
    }
}

