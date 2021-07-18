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
package android.media.tv;


/**
 * Central system API to the overall TV input framework (TIF) architecture, which arbitrates
 * interaction between applications and the selected TV inputs. You can retrieve an instance of
 * this interface with {@link android.content.Context#getSystemService
 * Context.getSystemService(Context.TV_INPUT_SERVICE)}.
 *
 * <p>There are three primary parties involved in the TV input framework (TIF) architecture:
 *
 * <ul>
 * <li>The <strong>TV input manager</strong> as expressed by this class is the central point of the
 * system that manages interaction between all other parts. It is expressed as the client-side API
 * here which exists in each application context and communicates with a global system service that
 * manages the interaction across all processes.
 * <li>A <strong>TV input</strong> implemented by {@link TvInputService} represents an input source
 * of TV, which can be a pass-through input such as HDMI, or a tuner input which provides broadcast
 * TV programs. The system binds to the TV input per application’s request.
 * on implementing TV inputs.
 * <li><strong>Applications</strong> talk to the TV input manager to list TV inputs and check their
 * status. Once an application find the input to use, it uses {@link TvView} or
 * {@link TvRecordingClient} for further interaction such as watching and recording broadcast TV
 * programs.
 * </ul>
 */
public final class TvInputManager {
    private static final java.lang.String TAG = "TvInputManager";

    static final int DVB_DEVICE_START = 0;

    static final int DVB_DEVICE_END = 2;

    /**
     * A demux device of DVB API for controlling the filters of DVB hardware/software.
     *
     * @unknown 
     */
    public static final int DVB_DEVICE_DEMUX = android.media.tv.TvInputManager.DVB_DEVICE_START;

    /**
     * A DVR device of DVB API for reading transport streams.
     *
     * @unknown 
     */
    public static final int DVB_DEVICE_DVR = 1;

    /**
     * A frontend device of DVB API for controlling the tuner and DVB demodulator hardware.
     *
     * @unknown 
     */
    public static final int DVB_DEVICE_FRONTEND = android.media.tv.TvInputManager.DVB_DEVICE_END;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.media.tv.TvInputManager.VIDEO_UNAVAILABLE_REASON_UNKNOWN, android.media.tv.TvInputManager.VIDEO_UNAVAILABLE_REASON_TUNING, android.media.tv.TvInputManager.VIDEO_UNAVAILABLE_REASON_WEAK_SIGNAL, android.media.tv.TvInputManager.VIDEO_UNAVAILABLE_REASON_BUFFERING, android.media.tv.TvInputManager.VIDEO_UNAVAILABLE_REASON_AUDIO_ONLY })
    public @interface VideoUnavailableReason {}

    static final int VIDEO_UNAVAILABLE_REASON_START = 0;

    static final int VIDEO_UNAVAILABLE_REASON_END = 4;

    /**
     * Reason for {@link TvInputService.Session#notifyVideoUnavailable(int)} and
     * {@link TvView.TvInputCallback#onVideoUnavailable(String, int)}: Video is unavailable due to
     * an unspecified error.
     */
    public static final int VIDEO_UNAVAILABLE_REASON_UNKNOWN = android.media.tv.TvInputManager.VIDEO_UNAVAILABLE_REASON_START;

    /**
     * Reason for {@link TvInputService.Session#notifyVideoUnavailable(int)} and
     * {@link TvView.TvInputCallback#onVideoUnavailable(String, int)}: Video is unavailable because
     * the corresponding TV input is in the middle of tuning to a new channel.
     */
    public static final int VIDEO_UNAVAILABLE_REASON_TUNING = 1;

    /**
     * Reason for {@link TvInputService.Session#notifyVideoUnavailable(int)} and
     * {@link TvView.TvInputCallback#onVideoUnavailable(String, int)}: Video is unavailable due to
     * weak TV signal.
     */
    public static final int VIDEO_UNAVAILABLE_REASON_WEAK_SIGNAL = 2;

    /**
     * Reason for {@link TvInputService.Session#notifyVideoUnavailable(int)} and
     * {@link TvView.TvInputCallback#onVideoUnavailable(String, int)}: Video is unavailable because
     * the corresponding TV input has stopped playback temporarily to buffer more data.
     */
    public static final int VIDEO_UNAVAILABLE_REASON_BUFFERING = 3;

    /**
     * Reason for {@link TvInputService.Session#notifyVideoUnavailable(int)} and
     * {@link TvView.TvInputCallback#onVideoUnavailable(String, int)}: Video is unavailable because
     * the current TV program is audio-only.
     */
    public static final int VIDEO_UNAVAILABLE_REASON_AUDIO_ONLY = android.media.tv.TvInputManager.VIDEO_UNAVAILABLE_REASON_END;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.media.tv.TvInputManager.TIME_SHIFT_STATUS_UNKNOWN, android.media.tv.TvInputManager.TIME_SHIFT_STATUS_UNSUPPORTED, android.media.tv.TvInputManager.TIME_SHIFT_STATUS_UNAVAILABLE, android.media.tv.TvInputManager.TIME_SHIFT_STATUS_AVAILABLE })
    public @interface TimeShiftStatus {}

    /**
     * Status for {@link TvInputService.Session#notifyTimeShiftStatusChanged(int)} and
     * {@link TvView.TvInputCallback#onTimeShiftStatusChanged(String, int)}: Unknown status. Also
     * the status prior to calling {@code notifyTimeShiftStatusChanged}.
     */
    public static final int TIME_SHIFT_STATUS_UNKNOWN = 0;

    /**
     * Status for {@link TvInputService.Session#notifyTimeShiftStatusChanged(int)} and
     * {@link TvView.TvInputCallback#onTimeShiftStatusChanged(String, int)}: The current TV input
     * does not support time shifting.
     */
    public static final int TIME_SHIFT_STATUS_UNSUPPORTED = 1;

    /**
     * Status for {@link TvInputService.Session#notifyTimeShiftStatusChanged(int)} and
     * {@link TvView.TvInputCallback#onTimeShiftStatusChanged(String, int)}: Time shifting is
     * currently unavailable but might work again later.
     */
    public static final int TIME_SHIFT_STATUS_UNAVAILABLE = 2;

    /**
     * Status for {@link TvInputService.Session#notifyTimeShiftStatusChanged(int)} and
     * {@link TvView.TvInputCallback#onTimeShiftStatusChanged(String, int)}: Time shifting is
     * currently available. In this status, the application assumes it can pause/resume playback,
     * seek to a specified time position and set playback rate and audio mode.
     */
    public static final int TIME_SHIFT_STATUS_AVAILABLE = 3;

    /**
     * Value returned by {@link TvInputService.Session#onTimeShiftGetCurrentPosition()} and
     * {@link TvInputService.Session#onTimeShiftGetStartPosition()} when time shifting has not
     * yet started.
     */
    public static final long TIME_SHIFT_INVALID_TIME = java.lang.Long.MIN_VALUE;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.media.tv.TvInputManager.RECORDING_ERROR_UNKNOWN, android.media.tv.TvInputManager.RECORDING_ERROR_INSUFFICIENT_SPACE, android.media.tv.TvInputManager.RECORDING_ERROR_RESOURCE_BUSY })
    public @interface RecordingError {}

    static final int RECORDING_ERROR_START = 0;

    static final int RECORDING_ERROR_END = 2;

    /**
     * Error for {@link TvInputService.RecordingSession#notifyError(int)} and
     * {@link TvRecordingClient.RecordingCallback#onError(int)}: The requested operation cannot be
     * completed due to a problem that does not fit under any other error codes, or the error code
     * for the problem is defined on the higher version than application's
     * <code>android:targetSdkVersion</code>.
     */
    public static final int RECORDING_ERROR_UNKNOWN = android.media.tv.TvInputManager.RECORDING_ERROR_START;

    /**
     * Error for {@link TvInputService.RecordingSession#notifyError(int)} and
     * {@link TvRecordingClient.RecordingCallback#onError(int)}: Recording cannot proceed due to
     * insufficient storage space.
     */
    public static final int RECORDING_ERROR_INSUFFICIENT_SPACE = 1;

    /**
     * Error for {@link TvInputService.RecordingSession#notifyError(int)} and
     * {@link TvRecordingClient.RecordingCallback#onError(int)}: Recording cannot proceed because
     * a required recording resource was not able to be allocated.
     */
    public static final int RECORDING_ERROR_RESOURCE_BUSY = android.media.tv.TvInputManager.RECORDING_ERROR_END;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.media.tv.TvInputManager.INPUT_STATE_CONNECTED, android.media.tv.TvInputManager.INPUT_STATE_CONNECTED_STANDBY, android.media.tv.TvInputManager.INPUT_STATE_DISCONNECTED })
    public @interface InputState {}

    /**
     * State for {@link #getInputState(String)} and
     * {@link TvInputCallback#onInputStateChanged(String, int)}: The input source is connected.
     *
     * <p>This state indicates that a source device is connected to the input port and is in the
     * normal operation mode. It is mostly relevant to hardware inputs such as HDMI input. This is
     * the default state for any hardware inputs where their states are unknown. Non-hardware inputs
     * are considered connected all the time.
     */
    public static final int INPUT_STATE_CONNECTED = 0;

    /**
     * State for {@link #getInputState(String)} and
     * {@link TvInputCallback#onInputStateChanged(String, int)}: The input source is connected but
     * in standby mode.
     *
     * <p>This state indicates that a source device is connected to the input port but is in standby
     * mode. It is mostly relevant to hardware inputs such as HDMI input.
     */
    public static final int INPUT_STATE_CONNECTED_STANDBY = 1;

    /**
     * State for {@link #getInputState(String)} and
     * {@link TvInputCallback#onInputStateChanged(String, int)}: The input source is disconnected.
     *
     * <p>This state indicates that a source device is disconnected from the input port. It is
     * mostly relevant to hardware inputs such as HDMI input.
     */
    public static final int INPUT_STATE_DISCONNECTED = 2;

    /**
     * Broadcast intent action when the user blocked content ratings change. For use with the
     * {@link #isRatingBlocked}.
     */
    public static final java.lang.String ACTION_BLOCKED_RATINGS_CHANGED = "android.media.tv.action.BLOCKED_RATINGS_CHANGED";

    /**
     * Broadcast intent action when the parental controls enabled state changes. For use with the
     * {@link #isParentalControlsEnabled}.
     */
    public static final java.lang.String ACTION_PARENTAL_CONTROLS_ENABLED_CHANGED = "android.media.tv.action.PARENTAL_CONTROLS_ENABLED_CHANGED";

    /**
     * Broadcast intent action used to query available content rating systems.
     *
     * <p>The TV input manager service locates available content rating systems by querying
     * broadcast receivers that are registered for this action. An application can offer additional
     * content rating systems to the user by declaring a suitable broadcast receiver in its
     * manifest.
     *
     * <p>Here is an example broadcast receiver declaration that an application might include in its
     * AndroidManifest.xml to advertise custom content rating systems. The meta-data specifies a
     * resource that contains a description of each content rating system that is provided by the
     * application.
     *
     * <p><pre class="prettyprint">
     * {@literal <receiver android:name=".TvInputReceiver">
     *     <intent-filter>
     *         <action android:name=
     *                 "android.media.tv.action.QUERY_CONTENT_RATING_SYSTEMS" />
     *     </intent-filter>
     *     <meta-data
     *             android:name="android.media.tv.metadata.CONTENT_RATING_SYSTEMS"
     *             android:resource="@xml/tv_content_rating_systems" />
     * </receiver>}</pre>
     *
     * <p>In the above example, the <code>@xml/tv_content_rating_systems</code> resource refers to an
     * XML resource whose root element is <code>&lt;rating-system-definitions&gt;</code> that
     * contains zero or more <code>&lt;rating-system-definition&gt;</code> elements. Each <code>
     * &lt;rating-system-definition&gt;</code> element specifies the ratings, sub-ratings and rating
     * orders of a particular content rating system.
     *
     * @see TvContentRating
     */
    public static final java.lang.String ACTION_QUERY_CONTENT_RATING_SYSTEMS = "android.media.tv.action.QUERY_CONTENT_RATING_SYSTEMS";

    /**
     * Content rating systems metadata associated with {@link #ACTION_QUERY_CONTENT_RATING_SYSTEMS}.
     *
     * <p>Specifies the resource ID of an XML resource that describes the content rating systems
     * that are provided by the application.
     */
    public static final java.lang.String META_DATA_CONTENT_RATING_SYSTEMS = "android.media.tv.metadata.CONTENT_RATING_SYSTEMS";

    /**
     * Activity action to set up channel sources i.e.&nbsp;TV inputs of type
     * {@link TvInputInfo#TYPE_TUNER}. When invoked, the system will display an appropriate UI for
     * the user to initiate the individual setup flow provided by
     * {@link android.R.attr#setupActivity} of each TV input service.
     */
    public static final java.lang.String ACTION_SETUP_INPUTS = "android.media.tv.action.SETUP_INPUTS";

    private final android.media.tv.ITvInputManager mService;

    private final java.lang.Object mLock = new java.lang.Object();

    // @GuardedBy("mLock")
    private final java.util.List<android.media.tv.TvInputManager.TvInputCallbackRecord> mCallbackRecords = new java.util.LinkedList<>();

    // A mapping from TV input ID to the state of corresponding input.
    // @GuardedBy("mLock")
    private final java.util.Map<java.lang.String, java.lang.Integer> mStateMap = new android.util.ArrayMap<>();

    // A mapping from the sequence number of a session to its SessionCallbackRecord.
    private final android.util.SparseArray<android.media.tv.TvInputManager.SessionCallbackRecord> mSessionCallbackRecordMap = new android.util.SparseArray<>();

    // A sequence number for the next session to be created. Should be protected by a lock
    // {@code mSessionCallbackRecordMap}.
    private int mNextSeq;

    private final android.media.tv.ITvInputClient mClient;

    private final int mUserId;

    /**
     * Interface used to receive the created session.
     *
     * @unknown 
     */
    public static abstract class SessionCallback {
        /**
         * This is called after {@link TvInputManager#createSession} has been processed.
         *
         * @param session
         * 		A {@link TvInputManager.Session} instance created. This can be
         * 		{@code null} if the creation request failed.
         */
        public void onSessionCreated(@android.annotation.Nullable
        android.media.tv.TvInputManager.Session session) {
        }

        /**
         * This is called when {@link TvInputManager.Session} is released.
         * This typically happens when the process hosting the session has crashed or been killed.
         *
         * @param session
         * 		A {@link TvInputManager.Session} instance released.
         */
        public void onSessionReleased(android.media.tv.TvInputManager.Session session) {
        }

        /**
         * This is called when the channel of this session is changed by the underlying TV input
         * without any {@link TvInputManager.Session#tune(Uri)} request.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         * @param channelUri
         * 		The URI of a channel.
         */
        public void onChannelRetuned(android.media.tv.TvInputManager.Session session, android.net.Uri channelUri) {
        }

        /**
         * This is called when the track information of the session has been changed.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         * @param tracks
         * 		A list which includes track information.
         */
        public void onTracksChanged(android.media.tv.TvInputManager.Session session, java.util.List<android.media.tv.TvTrackInfo> tracks) {
        }

        /**
         * This is called when a track for a given type is selected.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         * @param type
         * 		The type of the selected track. The type can be
         * 		{@link TvTrackInfo#TYPE_AUDIO}, {@link TvTrackInfo#TYPE_VIDEO} or
         * 		{@link TvTrackInfo#TYPE_SUBTITLE}.
         * @param trackId
         * 		The ID of the selected track. When {@code null} the currently selected
         * 		track for a given type should be unselected.
         */
        public void onTrackSelected(android.media.tv.TvInputManager.Session session, int type, @android.annotation.Nullable
        java.lang.String trackId) {
        }

        /**
         * This is invoked when the video size has been changed. It is also called when the first
         * time video size information becomes available after the session is tuned to a specific
         * channel.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         * @param width
         * 		The width of the video.
         * @param height
         * 		The height of the video.
         */
        public void onVideoSizeChanged(android.media.tv.TvInputManager.Session session, int width, int height) {
        }

        /**
         * This is called when the video is available, so the TV input starts the playback.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         */
        public void onVideoAvailable(android.media.tv.TvInputManager.Session session) {
        }

        /**
         * This is called when the video is not available, so the TV input stops the playback.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         * @param reason
         * 		The reason why the TV input stopped the playback:
         * 		<ul>
         * 		<li>{@link TvInputManager#VIDEO_UNAVAILABLE_REASON_UNKNOWN}
         * 		<li>{@link TvInputManager#VIDEO_UNAVAILABLE_REASON_TUNING}
         * 		<li>{@link TvInputManager#VIDEO_UNAVAILABLE_REASON_WEAK_SIGNAL}
         * 		<li>{@link TvInputManager#VIDEO_UNAVAILABLE_REASON_BUFFERING}
         * 		<li>{@link TvInputManager#VIDEO_UNAVAILABLE_REASON_AUDIO_ONLY}
         * 		</ul>
         */
        public void onVideoUnavailable(android.media.tv.TvInputManager.Session session, int reason) {
        }

        /**
         * This is called when the current program content turns out to be allowed to watch since
         * its content rating is not blocked by parental controls.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         */
        public void onContentAllowed(android.media.tv.TvInputManager.Session session) {
        }

        /**
         * This is called when the current program content turns out to be not allowed to watch
         * since its content rating is blocked by parental controls.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         * @param rating
         * 		The content ration of the blocked program.
         */
        public void onContentBlocked(android.media.tv.TvInputManager.Session session, android.media.tv.TvContentRating rating) {
        }

        /**
         * This is called when {@link TvInputService.Session#layoutSurface} is called to change the
         * layout of surface.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         * @param left
         * 		Left position.
         * @param top
         * 		Top position.
         * @param right
         * 		Right position.
         * @param bottom
         * 		Bottom position.
         */
        public void onLayoutSurface(android.media.tv.TvInputManager.Session session, int left, int top, int right, int bottom) {
        }

        /**
         * This is called when a custom event has been sent from this session.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback
         * @param eventType
         * 		The type of the event.
         * @param eventArgs
         * 		Optional arguments of the event.
         */
        public void onSessionEvent(android.media.tv.TvInputManager.Session session, java.lang.String eventType, android.os.Bundle eventArgs) {
        }

        /**
         * This is called when the time shift status is changed.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         * @param status
         * 		The current time shift status. Should be one of the followings.
         * 		<ul>
         * 		<li>{@link TvInputManager#TIME_SHIFT_STATUS_UNSUPPORTED}
         * 		<li>{@link TvInputManager#TIME_SHIFT_STATUS_UNAVAILABLE}
         * 		<li>{@link TvInputManager#TIME_SHIFT_STATUS_AVAILABLE}
         * 		</ul>
         */
        public void onTimeShiftStatusChanged(android.media.tv.TvInputManager.Session session, int status) {
        }

        /**
         * This is called when the start position for time shifting has changed.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         * @param timeMs
         * 		The start position for time shifting, in milliseconds since the epoch.
         */
        public void onTimeShiftStartPositionChanged(android.media.tv.TvInputManager.Session session, long timeMs) {
        }

        /**
         * This is called when the current position for time shifting is changed.
         *
         * @param session
         * 		A {@link TvInputManager.Session} associated with this callback.
         * @param timeMs
         * 		The current position for time shifting, in milliseconds since the epoch.
         */
        public void onTimeShiftCurrentPositionChanged(android.media.tv.TvInputManager.Session session, long timeMs) {
        }

        // For the recording session only
        /**
         * This is called when the recording session has been tuned to the given channel and is
         * ready to start recording.
         *
         * @param channelUri
         * 		The URI of a channel.
         */
        void onTuned(android.media.tv.TvInputManager.Session session, android.net.Uri channelUri) {
        }

        // For the recording session only
        /**
         * This is called when the current recording session has stopped recording and created a
         * new data entry in the {@link TvContract.RecordedPrograms} table that describes the newly
         * recorded program.
         *
         * @param recordedProgramUri
         * 		The URI for the newly recorded program.
         */
        void onRecordingStopped(android.media.tv.TvInputManager.Session session, android.net.Uri recordedProgramUri) {
        }

        // For the recording session only
        /**
         * This is called when an issue has occurred. It may be called at any time after the current
         * recording session is created until it is released.
         *
         * @param error
         * 		The error code.
         */
        void onError(android.media.tv.TvInputManager.Session session, @android.media.tv.TvInputManager.RecordingError
        int error) {
        }
    }

    private static final class SessionCallbackRecord {
        private final android.media.tv.TvInputManager.SessionCallback mSessionCallback;

        private final android.os.Handler mHandler;

        private android.media.tv.TvInputManager.Session mSession;

        SessionCallbackRecord(android.media.tv.TvInputManager.SessionCallback sessionCallback, android.os.Handler handler) {
            mSessionCallback = sessionCallback;
            mHandler = handler;
        }

        void postSessionCreated(final android.media.tv.TvInputManager.Session session) {
            mSession = session;
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onSessionCreated(session);
                }
            });
        }

        void postSessionReleased() {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onSessionReleased(mSession);
                }
            });
        }

        void postChannelRetuned(final android.net.Uri channelUri) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onChannelRetuned(mSession, channelUri);
                }
            });
        }

        void postTracksChanged(final java.util.List<android.media.tv.TvTrackInfo> tracks) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onTracksChanged(mSession, tracks);
                }
            });
        }

        void postTrackSelected(final int type, final java.lang.String trackId) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onTrackSelected(mSession, type, trackId);
                }
            });
        }

        void postVideoSizeChanged(final int width, final int height) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onVideoSizeChanged(mSession, width, height);
                }
            });
        }

        void postVideoAvailable() {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onVideoAvailable(mSession);
                }
            });
        }

        void postVideoUnavailable(final int reason) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onVideoUnavailable(mSession, reason);
                }
            });
        }

        void postContentAllowed() {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onContentAllowed(mSession);
                }
            });
        }

        void postContentBlocked(final android.media.tv.TvContentRating rating) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onContentBlocked(mSession, rating);
                }
            });
        }

        void postLayoutSurface(final int left, final int top, final int right, final int bottom) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onLayoutSurface(mSession, left, top, right, bottom);
                }
            });
        }

        void postSessionEvent(final java.lang.String eventType, final android.os.Bundle eventArgs) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onSessionEvent(mSession, eventType, eventArgs);
                }
            });
        }

        void postTimeShiftStatusChanged(final int status) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onTimeShiftStatusChanged(mSession, status);
                }
            });
        }

        void postTimeShiftStartPositionChanged(final long timeMs) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onTimeShiftStartPositionChanged(mSession, timeMs);
                }
            });
        }

        void postTimeShiftCurrentPositionChanged(final long timeMs) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onTimeShiftCurrentPositionChanged(mSession, timeMs);
                }
            });
        }

        // For the recording session only
        void postTuned(final android.net.Uri channelUri) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onTuned(mSession, channelUri);
                }
            });
        }

        // For the recording session only
        void postRecordingStopped(final android.net.Uri recordedProgramUri) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onRecordingStopped(mSession, recordedProgramUri);
                }
            });
        }

        // For the recording session only
        void postError(final int error) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mSessionCallback.onError(mSession, error);
                }
            });
        }
    }

    /**
     * Callback used to monitor status of the TV inputs.
     */
    public static abstract class TvInputCallback {
        /**
         * This is called when the state of a given TV input is changed.
         *
         * @param inputId
         * 		The ID of the TV input.
         * @param state
         * 		State of the TV input. The value is one of the following:
         * 		<ul>
         * 		<li>{@link TvInputManager#INPUT_STATE_CONNECTED}
         * 		<li>{@link TvInputManager#INPUT_STATE_CONNECTED_STANDBY}
         * 		<li>{@link TvInputManager#INPUT_STATE_DISCONNECTED}
         * 		</ul>
         */
        public void onInputStateChanged(java.lang.String inputId, @android.media.tv.TvInputManager.InputState
        int state) {
        }

        /**
         * This is called when a TV input is added to the system.
         *
         * <p>Normally it happens when the user installs a new TV input package that implements
         * {@link TvInputService} interface.
         *
         * @param inputId
         * 		The ID of the TV input.
         */
        public void onInputAdded(java.lang.String inputId) {
        }

        /**
         * This is called when a TV input is removed from the system.
         *
         * <p>Normally it happens when the user uninstalls the previously installed TV input
         * package.
         *
         * @param inputId
         * 		The ID of the TV input.
         */
        public void onInputRemoved(java.lang.String inputId) {
        }

        /**
         * This is called when a TV input is updated on the system.
         *
         * <p>Normally it happens when a previously installed TV input package is re-installed or
         * the media on which a newer version of the package exists becomes available/unavailable.
         *
         * @param inputId
         * 		The ID of the TV input.
         */
        public void onInputUpdated(java.lang.String inputId) {
        }

        /**
         * This is called when the information about an existing TV input has been updated.
         *
         * <p>Because the system automatically creates a <code>TvInputInfo</code> object for each TV
         * input based on the information collected from the <code>AndroidManifest.xml</code>, this
         * method is only called back when such information has changed dynamically.
         *
         * @param inputInfo
         * 		The <code>TvInputInfo</code> object that contains new information.
         */
        public void onTvInputInfoUpdated(android.media.tv.TvInputInfo inputInfo) {
        }
    }

    private static final class TvInputCallbackRecord {
        private final android.media.tv.TvInputManager.TvInputCallback mCallback;

        private final android.os.Handler mHandler;

        public TvInputCallbackRecord(android.media.tv.TvInputManager.TvInputCallback callback, android.os.Handler handler) {
            mCallback = callback;
            mHandler = handler;
        }

        public android.media.tv.TvInputManager.TvInputCallback getCallback() {
            return mCallback;
        }

        public void postInputAdded(final java.lang.String inputId) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mCallback.onInputAdded(inputId);
                }
            });
        }

        public void postInputRemoved(final java.lang.String inputId) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mCallback.onInputRemoved(inputId);
                }
            });
        }

        public void postInputUpdated(final java.lang.String inputId) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mCallback.onInputUpdated(inputId);
                }
            });
        }

        public void postInputStateChanged(final java.lang.String inputId, final int state) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mCallback.onInputStateChanged(inputId, state);
                }
            });
        }

        public void postTvInputInfoUpdated(final android.media.tv.TvInputInfo inputInfo) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mCallback.onTvInputInfoUpdated(inputInfo);
                }
            });
        }
    }

    /**
     * Interface used to receive events from Hardware objects.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static abstract class HardwareCallback {
        /**
         * This is called when {@link Hardware} is no longer available for the client.
         */
        public abstract void onReleased();

        /**
         * This is called when the underlying {@link TvStreamConfig} has been changed.
         *
         * @param configs
         * 		The new {@link TvStreamConfig}s.
         */
        public abstract void onStreamConfigChanged(android.media.tv.TvStreamConfig[] configs);
    }

    /**
     *
     *
     * @unknown 
     */
    public TvInputManager(android.media.tv.ITvInputManager service, int userId) {
        mService = service;
        mUserId = userId;
        mClient = new android.media.tv.ITvInputClient.Stub() {
            @java.lang.Override
            public void onSessionCreated(java.lang.String inputId, android.os.IBinder token, android.view.InputChannel channel, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for " + token);
                        return;
                    }
                    android.media.tv.TvInputManager.Session session = null;
                    if (token != null) {
                        session = new android.media.tv.TvInputManager.Session(token, channel, mService, mUserId, seq, mSessionCallbackRecordMap);
                    }
                    record.postSessionCreated(session);
                }
            }

            @java.lang.Override
            public void onSessionReleased(int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    mSessionCallbackRecordMap.delete(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq:" + seq);
                        return;
                    }
                    record.mSession.releaseInternal();
                    record.postSessionReleased();
                }
            }

            @java.lang.Override
            public void onChannelRetuned(android.net.Uri channelUri, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postChannelRetuned(channelUri);
                }
            }

            @java.lang.Override
            public void onTracksChanged(java.util.List<android.media.tv.TvTrackInfo> tracks, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    if (record.mSession.updateTracks(tracks)) {
                        record.postTracksChanged(tracks);
                        postVideoSizeChangedIfNeededLocked(record);
                    }
                }
            }

            @java.lang.Override
            public void onTrackSelected(int type, java.lang.String trackId, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    if (record.mSession.updateTrackSelection(type, trackId)) {
                        record.postTrackSelected(type, trackId);
                        postVideoSizeChangedIfNeededLocked(record);
                    }
                }
            }

            private void postVideoSizeChangedIfNeededLocked(android.media.tv.TvInputManager.SessionCallbackRecord record) {
                android.media.tv.TvTrackInfo track = record.mSession.getVideoTrackToNotify();
                if (track != null) {
                    record.postVideoSizeChanged(track.getVideoWidth(), track.getVideoHeight());
                }
            }

            @java.lang.Override
            public void onVideoAvailable(int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postVideoAvailable();
                }
            }

            @java.lang.Override
            public void onVideoUnavailable(int reason, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postVideoUnavailable(reason);
                }
            }

            @java.lang.Override
            public void onContentAllowed(int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postContentAllowed();
                }
            }

            @java.lang.Override
            public void onContentBlocked(java.lang.String rating, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postContentBlocked(android.media.tv.TvContentRating.unflattenFromString(rating));
                }
            }

            @java.lang.Override
            public void onLayoutSurface(int left, int top, int right, int bottom, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postLayoutSurface(left, top, right, bottom);
                }
            }

            @java.lang.Override
            public void onSessionEvent(java.lang.String eventType, android.os.Bundle eventArgs, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postSessionEvent(eventType, eventArgs);
                }
            }

            @java.lang.Override
            public void onTimeShiftStatusChanged(int status, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postTimeShiftStatusChanged(status);
                }
            }

            @java.lang.Override
            public void onTimeShiftStartPositionChanged(long timeMs, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postTimeShiftStartPositionChanged(timeMs);
                }
            }

            @java.lang.Override
            public void onTimeShiftCurrentPositionChanged(long timeMs, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postTimeShiftCurrentPositionChanged(timeMs);
                }
            }

            @java.lang.Override
            public void onTuned(int seq, android.net.Uri channelUri) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postTuned(channelUri);
                }
            }

            @java.lang.Override
            public void onRecordingStopped(android.net.Uri recordedProgramUri, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postRecordingStopped(recordedProgramUri);
                }
            }

            @java.lang.Override
            public void onError(int error, int seq) {
                synchronized(mSessionCallbackRecordMap) {
                    android.media.tv.TvInputManager.SessionCallbackRecord record = mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        android.util.Log.e(android.media.tv.TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postError(error);
                }
            }
        };
        android.media.tv.ITvInputManagerCallback managerCallback = new android.media.tv.ITvInputManagerCallback.Stub() {
            @java.lang.Override
            public void onInputAdded(java.lang.String inputId) {
                synchronized(mLock) {
                    mStateMap.put(inputId, android.media.tv.TvInputManager.INPUT_STATE_CONNECTED);
                    for (android.media.tv.TvInputManager.TvInputCallbackRecord record : mCallbackRecords) {
                        record.postInputAdded(inputId);
                    }
                }
            }

            @java.lang.Override
            public void onInputRemoved(java.lang.String inputId) {
                synchronized(mLock) {
                    mStateMap.remove(inputId);
                    for (android.media.tv.TvInputManager.TvInputCallbackRecord record : mCallbackRecords) {
                        record.postInputRemoved(inputId);
                    }
                }
            }

            @java.lang.Override
            public void onInputUpdated(java.lang.String inputId) {
                synchronized(mLock) {
                    for (android.media.tv.TvInputManager.TvInputCallbackRecord record : mCallbackRecords) {
                        record.postInputUpdated(inputId);
                    }
                }
            }

            @java.lang.Override
            public void onInputStateChanged(java.lang.String inputId, int state) {
                synchronized(mLock) {
                    mStateMap.put(inputId, state);
                    for (android.media.tv.TvInputManager.TvInputCallbackRecord record : mCallbackRecords) {
                        record.postInputStateChanged(inputId, state);
                    }
                }
            }

            @java.lang.Override
            public void onTvInputInfoUpdated(android.media.tv.TvInputInfo inputInfo) {
                synchronized(mLock) {
                    for (android.media.tv.TvInputManager.TvInputCallbackRecord record : mCallbackRecords) {
                        record.postTvInputInfoUpdated(inputInfo);
                    }
                }
            }
        };
        try {
            if (mService != null) {
                mService.registerCallback(managerCallback, mUserId);
                java.util.List<android.media.tv.TvInputInfo> infos = mService.getTvInputList(mUserId);
                synchronized(mLock) {
                    for (android.media.tv.TvInputInfo info : infos) {
                        java.lang.String inputId = info.getId();
                        mStateMap.put(inputId, mService.getTvInputState(inputId, mUserId));
                    }
                }
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns the complete list of TV inputs on the system.
     *
     * @return List of {@link TvInputInfo} for each TV input that describes its meta information.
     */
    public java.util.List<android.media.tv.TvInputInfo> getTvInputList() {
        try {
            return mService.getTvInputList(mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns the {@link TvInputInfo} for a given TV input.
     *
     * @param inputId
     * 		The ID of the TV input.
     * @return the {@link TvInputInfo} for a given TV input. {@code null} if not found.
     */
    @android.annotation.Nullable
    public android.media.tv.TvInputInfo getTvInputInfo(@android.annotation.NonNull
    java.lang.String inputId) {
        com.android.internal.util.Preconditions.checkNotNull(inputId);
        try {
            return mService.getTvInputInfo(inputId, mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Updates the <code>TvInputInfo</code> for an existing TV input. A TV input service
     * implementation may call this method to pass the application and system an up-to-date
     * <code>TvInputInfo</code> object that describes itself.
     *
     * <p>The system automatically creates a <code>TvInputInfo</code> object for each TV input,
     * based on the information collected from the <code>AndroidManifest.xml</code>, thus it is not
     * necessary to call this method unless such information has changed dynamically.
     * Use {@link TvInputInfo.Builder} to build a new <code>TvInputInfo</code> object.
     *
     * <p>Attempting to change information about a TV input that the calling package does not own
     * does nothing.
     *
     * @param inputInfo
     * 		The <code>TvInputInfo</code> object that contains new information.
     * @throws IllegalArgumentException
     * 		if the argument is {@code null}.
     * @see TvInputCallback#onTvInputInfoUpdated(TvInputInfo)
     */
    public void updateTvInputInfo(@android.annotation.NonNull
    android.media.tv.TvInputInfo inputInfo) {
        com.android.internal.util.Preconditions.checkNotNull(inputInfo);
        try {
            mService.updateTvInputInfo(inputInfo, mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns the state of a given TV input.
     *
     * <p>The state is one of the following:
     * <ul>
     * <li>{@link #INPUT_STATE_CONNECTED}
     * <li>{@link #INPUT_STATE_CONNECTED_STANDBY}
     * <li>{@link #INPUT_STATE_DISCONNECTED}
     * </ul>
     *
     * @param inputId
     * 		The ID of the TV input.
     * @throws IllegalArgumentException
     * 		if the argument is {@code null}.
     */
    @android.media.tv.TvInputManager.InputState
    public int getInputState(@android.annotation.NonNull
    java.lang.String inputId) {
        com.android.internal.util.Preconditions.checkNotNull(inputId);
        synchronized(mLock) {
            java.lang.Integer state = mStateMap.get(inputId);
            if (state == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "Unrecognized input ID: " + inputId);
                return android.media.tv.TvInputManager.INPUT_STATE_DISCONNECTED;
            }
            return state;
        }
    }

    /**
     * Registers a {@link TvInputCallback}.
     *
     * @param callback
     * 		A callback used to monitor status of the TV inputs.
     * @param handler
     * 		A {@link Handler} that the status change will be delivered to.
     */
    public void registerCallback(@android.annotation.NonNull
    android.media.tv.TvInputManager.TvInputCallback callback, @android.annotation.NonNull
    android.os.Handler handler) {
        com.android.internal.util.Preconditions.checkNotNull(callback);
        com.android.internal.util.Preconditions.checkNotNull(handler);
        synchronized(mLock) {
            mCallbackRecords.add(new android.media.tv.TvInputManager.TvInputCallbackRecord(callback, handler));
        }
    }

    /**
     * Unregisters the existing {@link TvInputCallback}.
     *
     * @param callback
     * 		The existing callback to remove.
     */
    public void unregisterCallback(@android.annotation.NonNull
    final android.media.tv.TvInputManager.TvInputCallback callback) {
        com.android.internal.util.Preconditions.checkNotNull(callback);
        synchronized(mLock) {
            for (java.util.Iterator<android.media.tv.TvInputManager.TvInputCallbackRecord> it = mCallbackRecords.iterator(); it.hasNext();) {
                android.media.tv.TvInputManager.TvInputCallbackRecord record = it.next();
                if (record.getCallback() == callback) {
                    it.remove();
                    break;
                }
            }
        }
    }

    /**
     * Returns the user's parental controls enabled state.
     *
     * @return {@code true} if the user enabled the parental controls, {@code false} otherwise.
     */
    public boolean isParentalControlsEnabled() {
        try {
            return mService.isParentalControlsEnabled(mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Sets the user's parental controls enabled state.
     *
     * @param enabled
     * 		The user's parental controls enabled state. {@code true} if the user enabled
     * 		the parental controls, {@code false} otherwise.
     * @see #isParentalControlsEnabled
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(android.media.tv.android.Manifest.permission.MODIFY_PARENTAL_CONTROLS.class)
    public void setParentalControlsEnabled(boolean enabled) {
        try {
            mService.setParentalControlsEnabled(enabled, mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Checks whether a given TV content rating is blocked by the user.
     *
     * @param rating
     * 		The TV content rating to check. Can be {@link TvContentRating#UNRATED}.
     * @return {@code true} if the given TV content rating is blocked, {@code false} otherwise.
     */
    public boolean isRatingBlocked(@android.annotation.NonNull
    android.media.tv.TvContentRating rating) {
        com.android.internal.util.Preconditions.checkNotNull(rating);
        try {
            return mService.isRatingBlocked(rating.flattenToString(), mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns the list of blocked content ratings.
     *
     * @return the list of content ratings blocked by the user.
     * @unknown 
     */
    @android.annotation.SystemApi
    public java.util.List<android.media.tv.TvContentRating> getBlockedRatings() {
        try {
            java.util.List<android.media.tv.TvContentRating> ratings = new java.util.ArrayList<>();
            for (java.lang.String rating : mService.getBlockedRatings(mUserId)) {
                ratings.add(android.media.tv.TvContentRating.unflattenFromString(rating));
            }
            return ratings;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Adds a user blocked content rating.
     *
     * @param rating
     * 		The content rating to block.
     * @see #isRatingBlocked
     * @see #removeBlockedRating
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(android.media.tv.android.Manifest.permission.MODIFY_PARENTAL_CONTROLS.class)
    public void addBlockedRating(@android.annotation.NonNull
    android.media.tv.TvContentRating rating) {
        com.android.internal.util.Preconditions.checkNotNull(rating);
        try {
            mService.addBlockedRating(rating.flattenToString(), mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Removes a user blocked content rating.
     *
     * @param rating
     * 		The content rating to unblock.
     * @see #isRatingBlocked
     * @see #addBlockedRating
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(android.media.tv.android.Manifest.permission.MODIFY_PARENTAL_CONTROLS.class)
    public void removeBlockedRating(@android.annotation.NonNull
    android.media.tv.TvContentRating rating) {
        com.android.internal.util.Preconditions.checkNotNull(rating);
        try {
            mService.removeBlockedRating(rating.flattenToString(), mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns the list of all TV content rating systems defined.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public java.util.List<android.media.tv.TvContentRatingSystemInfo> getTvContentRatingSystemList() {
        try {
            return mService.getTvContentRatingSystemList(mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Creates a {@link Session} for a given TV input.
     *
     * <p>The number of sessions that can be created at the same time is limited by the capability
     * of the given TV input.
     *
     * @param inputId
     * 		The ID of the TV input.
     * @param callback
     * 		A callback used to receive the created session.
     * @param handler
     * 		A {@link Handler} that the session creation will be delivered to.
     * @unknown 
     */
    public void createSession(@android.annotation.NonNull
    java.lang.String inputId, @android.annotation.NonNull
    final android.media.tv.TvInputManager.SessionCallback callback, @android.annotation.NonNull
    android.os.Handler handler) {
        createSessionInternal(inputId, false, callback, handler);
    }

    /**
     * Creates a recording {@link Session} for a given TV input.
     *
     * <p>The number of sessions that can be created at the same time is limited by the capability
     * of the given TV input.
     *
     * @param inputId
     * 		The ID of the TV input.
     * @param callback
     * 		A callback used to receive the created session.
     * @param handler
     * 		A {@link Handler} that the session creation will be delivered to.
     * @unknown 
     */
    public void createRecordingSession(@android.annotation.NonNull
    java.lang.String inputId, @android.annotation.NonNull
    final android.media.tv.TvInputManager.SessionCallback callback, @android.annotation.NonNull
    android.os.Handler handler) {
        createSessionInternal(inputId, true, callback, handler);
    }

    private void createSessionInternal(java.lang.String inputId, boolean isRecordingSession, android.media.tv.TvInputManager.SessionCallback callback, android.os.Handler handler) {
        com.android.internal.util.Preconditions.checkNotNull(inputId);
        com.android.internal.util.Preconditions.checkNotNull(callback);
        com.android.internal.util.Preconditions.checkNotNull(handler);
        android.media.tv.TvInputManager.SessionCallbackRecord record = new android.media.tv.TvInputManager.SessionCallbackRecord(callback, handler);
        synchronized(mSessionCallbackRecordMap) {
            int seq = mNextSeq++;
            mSessionCallbackRecordMap.put(seq, record);
            try {
                mService.createSession(mClient, inputId, isRecordingSession, seq, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /**
     * Returns the TvStreamConfig list of the given TV input.
     *
     * If you are using {@link Hardware} object from {@link #acquireTvInputHardware}, you should get the list of available streams
     * from {@link HardwareCallback#onStreamConfigChanged} method, not from
     * here. This method is designed to be used with {@link #captureFrame} in
     * capture scenarios specifically and not suitable for any other use.
     *
     * @param inputId
     * 		The ID of the TV input.
     * @return List of {@link TvStreamConfig} which is available for capturing
    of the given TV input.
     * @unknown 
     */
    @android.annotation.SystemApi
    public java.util.List<android.media.tv.TvStreamConfig> getAvailableTvStreamConfigList(java.lang.String inputId) {
        try {
            return mService.getAvailableTvStreamConfigList(inputId, mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Take a snapshot of the given TV input into the provided Surface.
     *
     * @param inputId
     * 		The ID of the TV input.
     * @param surface
     * 		the {@link Surface} to which the snapshot is captured.
     * @param config
     * 		the {@link TvStreamConfig} which is used for capturing.
     * @return true when the {@link Surface} is ready to be captured.
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean captureFrame(java.lang.String inputId, android.view.Surface surface, android.media.tv.TvStreamConfig config) {
        try {
            return mService.captureFrame(inputId, surface, config, mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns true if there is only a single TV input session.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean isSingleSessionActive() {
        try {
            return mService.isSingleSessionActive(mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns a list of TvInputHardwareInfo objects representing available hardware.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(android.media.tv.android.Manifest.permission.TV_INPUT_HARDWARE.class)
    public java.util.List<android.media.tv.TvInputHardwareInfo> getHardwareList() {
        try {
            return mService.getHardwareList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Acquires {@link Hardware} object for the given device ID.
     *
     * <p>A subsequent call to this method on the same {@code deviceId} will release the currently
     * acquired Hardware.
     *
     * @param deviceId
     * 		The device ID to acquire Hardware for.
     * @param callback
     * 		A callback to receive updates on Hardware.
     * @param info
     * 		The TV input which will use the acquired Hardware.
     * @return Hardware on success, {@code null} otherwise.
     * @unknown 
     */
    @android.annotation.RequiresPermission(android.media.tv.android.Manifest.permission.TV_INPUT_HARDWARE.class)
    public android.media.tv.TvInputManager.Hardware acquireTvInputHardware(int deviceId, final android.media.tv.TvInputManager.HardwareCallback callback, android.media.tv.TvInputInfo info) {
        return acquireTvInputHardware(deviceId, info, callback);
    }

    /**
     * Acquires {@link Hardware} object for the given device ID.
     *
     * <p>A subsequent call to this method on the same {@code deviceId} will release the currently
     * acquired Hardware.
     *
     * @param deviceId
     * 		The device ID to acquire Hardware for.
     * @param callback
     * 		A callback to receive updates on Hardware.
     * @param info
     * 		The TV input which will use the acquired Hardware.
     * @return Hardware on success, {@code null} otherwise.
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(android.media.tv.android.Manifest.permission.TV_INPUT_HARDWARE.class)
    public android.media.tv.TvInputManager.Hardware acquireTvInputHardware(int deviceId, android.media.tv.TvInputInfo info, final android.media.tv.TvInputManager.HardwareCallback callback) {
        try {
            return new android.media.tv.TvInputManager.Hardware(mService.acquireTvInputHardware(deviceId, new android.media.tv.ITvInputHardwareCallback.Stub() {
                @java.lang.Override
                public void onReleased() {
                    callback.onReleased();
                }

                @java.lang.Override
                public void onStreamConfigChanged(android.media.tv.TvStreamConfig[] configs) {
                    callback.onStreamConfigChanged(configs);
                }
            }, info, mUserId));
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Releases previously acquired hardware object.
     *
     * @param deviceId
     * 		The device ID this Hardware was acquired for
     * @param hardware
     * 		Hardware to release.
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(android.media.tv.android.Manifest.permission.TV_INPUT_HARDWARE.class)
    public void releaseTvInputHardware(int deviceId, android.media.tv.TvInputManager.Hardware hardware) {
        try {
            mService.releaseTvInputHardware(deviceId, hardware.getInterface(), mUserId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns the list of currently available DVB devices on the system.
     *
     * @return the list of {@link DvbDeviceInfo} objects representing available DVB devices.
     * @unknown 
     */
    public java.util.List<android.media.tv.DvbDeviceInfo> getDvbDeviceList() {
        try {
            return mService.getDvbDeviceList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns a {@link ParcelFileDescriptor} of a specified DVB device for a given
     * {@link DvbDeviceInfo}
     *
     * @param info
     * 		A {@link DvbDeviceInfo} to open a DVB device.
     * @param device
     * 		A DVB device. The DVB device can be {@link #DVB_DEVICE_DEMUX},
     * 		{@link #DVB_DEVICE_DVR} or {@link #DVB_DEVICE_FRONTEND}.
     * @return a {@link ParcelFileDescriptor} of a specified DVB device for a given
    {@link DvbDeviceInfo}, or {@code null} if the given {@link DvbDeviceInfo} was invalid
    or the specified DVB device was busy with a previous request.
     * @unknown 
     */
    public android.os.ParcelFileDescriptor openDvbDevice(android.media.tv.DvbDeviceInfo info, int device) {
        try {
            if ((android.media.tv.TvInputManager.DVB_DEVICE_START > device) || (android.media.tv.TvInputManager.DVB_DEVICE_END < device)) {
                throw new java.lang.IllegalArgumentException("Invalid DVB device: " + device);
            }
            return mService.openDvbDevice(info, device);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * The Session provides the per-session functionality of TV inputs.
     *
     * @unknown 
     */
    public static final class Session {
        static final int DISPATCH_IN_PROGRESS = -1;

        static final int DISPATCH_NOT_HANDLED = 0;

        static final int DISPATCH_HANDLED = 1;

        private static final long INPUT_SESSION_NOT_RESPONDING_TIMEOUT = 2500;

        private final android.media.tv.ITvInputManager mService;

        private final int mUserId;

        private final int mSeq;

        // For scheduling input event handling on the main thread. This also serves as a lock to
        // protect pending input events and the input channel.
        private final android.media.tv.TvInputManager.Session.InputEventHandler mHandler = new android.media.tv.TvInputManager.Session.InputEventHandler(android.os.Looper.getMainLooper());

        private final android.util.Pools.Pool<android.media.tv.TvInputManager.Session.PendingEvent> mPendingEventPool = new android.util.Pools.SimplePool<>(20);

        private final android.util.SparseArray<android.media.tv.TvInputManager.Session.PendingEvent> mPendingEvents = new android.util.SparseArray<>(20);

        private final android.util.SparseArray<android.media.tv.TvInputManager.SessionCallbackRecord> mSessionCallbackRecordMap;

        private android.os.IBinder mToken;

        private android.media.tv.TvInputManager.Session.TvInputEventSender mSender;

        private android.view.InputChannel mChannel;

        private final java.lang.Object mMetadataLock = new java.lang.Object();

        // @GuardedBy("mMetadataLock")
        private final java.util.List<android.media.tv.TvTrackInfo> mAudioTracks = new java.util.ArrayList<>();

        // @GuardedBy("mMetadataLock")
        private final java.util.List<android.media.tv.TvTrackInfo> mVideoTracks = new java.util.ArrayList<>();

        // @GuardedBy("mMetadataLock")
        private final java.util.List<android.media.tv.TvTrackInfo> mSubtitleTracks = new java.util.ArrayList<>();

        // @GuardedBy("mMetadataLock")
        private java.lang.String mSelectedAudioTrackId;

        // @GuardedBy("mMetadataLock")
        private java.lang.String mSelectedVideoTrackId;

        // @GuardedBy("mMetadataLock")
        private java.lang.String mSelectedSubtitleTrackId;

        // @GuardedBy("mMetadataLock")
        private int mVideoWidth;

        // @GuardedBy("mMetadataLock")
        private int mVideoHeight;

        private Session(android.os.IBinder token, android.view.InputChannel channel, android.media.tv.ITvInputManager service, int userId, int seq, android.util.SparseArray<android.media.tv.TvInputManager.SessionCallbackRecord> sessionCallbackRecordMap) {
            mToken = token;
            mChannel = channel;
            mService = service;
            mUserId = userId;
            mSeq = seq;
            mSessionCallbackRecordMap = sessionCallbackRecordMap;
        }

        /**
         * Releases this session.
         */
        public void release() {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.releaseSession(mToken, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
            releaseInternal();
        }

        /**
         * Sets this as the main session. The main session is a session whose corresponding TV
         * input determines the HDMI-CEC active source device.
         *
         * @see TvView#setMain
         */
        void setMain() {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.setMainSession(mToken, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Sets the {@link android.view.Surface} for this session.
         *
         * @param surface
         * 		A {@link android.view.Surface} used to render video.
         */
        public void setSurface(android.view.Surface surface) {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            // surface can be null.
            try {
                mService.setSurface(mToken, surface, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Notifies of any structural changes (format or size) of the surface passed in
         * {@link #setSurface}.
         *
         * @param format
         * 		The new PixelFormat of the surface.
         * @param width
         * 		The new width of the surface.
         * @param height
         * 		The new height of the surface.
         */
        public void dispatchSurfaceChanged(int format, int width, int height) {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.dispatchSurfaceChanged(mToken, format, width, height, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Sets the relative stream volume of this session to handle a change of audio focus.
         *
         * @param volume
         * 		A volume value between 0.0f to 1.0f.
         * @throws IllegalArgumentException
         * 		if the volume value is out of range.
         */
        public void setStreamVolume(float volume) {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                if ((volume < 0.0F) || (volume > 1.0F)) {
                    throw new java.lang.IllegalArgumentException("volume should be between 0.0f and 1.0f");
                }
                mService.setVolume(mToken, volume, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Tunes to a given channel.
         *
         * @param channelUri
         * 		The URI of a channel.
         */
        public void tune(android.net.Uri channelUri) {
            tune(channelUri, null);
        }

        /**
         * Tunes to a given channel.
         *
         * @param channelUri
         * 		The URI of a channel.
         * @param params
         * 		A set of extra parameters which might be handled with this tune event.
         */
        public void tune(@android.annotation.NonNull
        android.net.Uri channelUri, android.os.Bundle params) {
            com.android.internal.util.Preconditions.checkNotNull(channelUri);
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            synchronized(mMetadataLock) {
                mAudioTracks.clear();
                mVideoTracks.clear();
                mSubtitleTracks.clear();
                mSelectedAudioTrackId = null;
                mSelectedVideoTrackId = null;
                mSelectedSubtitleTrackId = null;
                mVideoWidth = 0;
                mVideoHeight = 0;
            }
            try {
                mService.tune(mToken, channelUri, params, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Enables or disables the caption for this session.
         *
         * @param enabled
         * 		{@code true} to enable, {@code false} to disable.
         */
        public void setCaptionEnabled(boolean enabled) {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.setCaptionEnabled(mToken, enabled, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Selects a track.
         *
         * @param type
         * 		The type of the track to select. The type can be
         * 		{@link TvTrackInfo#TYPE_AUDIO}, {@link TvTrackInfo#TYPE_VIDEO} or
         * 		{@link TvTrackInfo#TYPE_SUBTITLE}.
         * @param trackId
         * 		The ID of the track to select. When {@code null}, the currently selected
         * 		track of the given type will be unselected.
         * @see #getTracks
         */
        public void selectTrack(int type, @android.annotation.Nullable
        java.lang.String trackId) {
            synchronized(mMetadataLock) {
                if (type == android.media.tv.TvTrackInfo.TYPE_AUDIO) {
                    if ((trackId != null) && (!containsTrack(mAudioTracks, trackId))) {
                        android.util.Log.w(android.media.tv.TvInputManager.TAG, "Invalid audio trackId: " + trackId);
                        return;
                    }
                } else
                    if (type == android.media.tv.TvTrackInfo.TYPE_VIDEO) {
                        if ((trackId != null) && (!containsTrack(mVideoTracks, trackId))) {
                            android.util.Log.w(android.media.tv.TvInputManager.TAG, "Invalid video trackId: " + trackId);
                            return;
                        }
                    } else
                        if (type == android.media.tv.TvTrackInfo.TYPE_SUBTITLE) {
                            if ((trackId != null) && (!containsTrack(mSubtitleTracks, trackId))) {
                                android.util.Log.w(android.media.tv.TvInputManager.TAG, "Invalid subtitle trackId: " + trackId);
                                return;
                            }
                        } else {
                            throw new java.lang.IllegalArgumentException("invalid type: " + type);
                        }


            }
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.selectTrack(mToken, type, trackId, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        private boolean containsTrack(java.util.List<android.media.tv.TvTrackInfo> tracks, java.lang.String trackId) {
            for (android.media.tv.TvTrackInfo track : tracks) {
                if (track.getId().equals(trackId)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns the list of tracks for a given type. Returns {@code null} if the information is
         * not available.
         *
         * @param type
         * 		The type of the tracks. The type can be {@link TvTrackInfo#TYPE_AUDIO},
         * 		{@link TvTrackInfo#TYPE_VIDEO} or {@link TvTrackInfo#TYPE_SUBTITLE}.
         * @return the list of tracks for the given type.
         */
        @android.annotation.Nullable
        public java.util.List<android.media.tv.TvTrackInfo> getTracks(int type) {
            synchronized(mMetadataLock) {
                if (type == android.media.tv.TvTrackInfo.TYPE_AUDIO) {
                    if (mAudioTracks == null) {
                        return null;
                    }
                    return new java.util.ArrayList<>(mAudioTracks);
                } else
                    if (type == android.media.tv.TvTrackInfo.TYPE_VIDEO) {
                        if (mVideoTracks == null) {
                            return null;
                        }
                        return new java.util.ArrayList<>(mVideoTracks);
                    } else
                        if (type == android.media.tv.TvTrackInfo.TYPE_SUBTITLE) {
                            if (mSubtitleTracks == null) {
                                return null;
                            }
                            return new java.util.ArrayList<>(mSubtitleTracks);
                        }


            }
            throw new java.lang.IllegalArgumentException("invalid type: " + type);
        }

        /**
         * Returns the selected track for a given type. Returns {@code null} if the information is
         * not available or any of the tracks for the given type is not selected.
         *
         * @return The ID of the selected track.
         * @see #selectTrack
         */
        @android.annotation.Nullable
        public java.lang.String getSelectedTrack(int type) {
            synchronized(mMetadataLock) {
                if (type == android.media.tv.TvTrackInfo.TYPE_AUDIO) {
                    return mSelectedAudioTrackId;
                } else
                    if (type == android.media.tv.TvTrackInfo.TYPE_VIDEO) {
                        return mSelectedVideoTrackId;
                    } else
                        if (type == android.media.tv.TvTrackInfo.TYPE_SUBTITLE) {
                            return mSelectedSubtitleTrackId;
                        }


            }
            throw new java.lang.IllegalArgumentException("invalid type: " + type);
        }

        /**
         * Responds to onTracksChanged() and updates the internal track information. Returns true if
         * there is an update.
         */
        boolean updateTracks(java.util.List<android.media.tv.TvTrackInfo> tracks) {
            synchronized(mMetadataLock) {
                mAudioTracks.clear();
                mVideoTracks.clear();
                mSubtitleTracks.clear();
                for (android.media.tv.TvTrackInfo track : tracks) {
                    if (track.getType() == android.media.tv.TvTrackInfo.TYPE_AUDIO) {
                        mAudioTracks.add(track);
                    } else
                        if (track.getType() == android.media.tv.TvTrackInfo.TYPE_VIDEO) {
                            mVideoTracks.add(track);
                        } else
                            if (track.getType() == android.media.tv.TvTrackInfo.TYPE_SUBTITLE) {
                                mSubtitleTracks.add(track);
                            }


                }
                return ((!mAudioTracks.isEmpty()) || (!mVideoTracks.isEmpty())) || (!mSubtitleTracks.isEmpty());
            }
        }

        /**
         * Responds to onTrackSelected() and updates the internal track selection information.
         * Returns true if there is an update.
         */
        boolean updateTrackSelection(int type, java.lang.String trackId) {
            synchronized(mMetadataLock) {
                if ((type == android.media.tv.TvTrackInfo.TYPE_AUDIO) && (!android.text.TextUtils.equals(trackId, mSelectedAudioTrackId))) {
                    mSelectedAudioTrackId = trackId;
                    return true;
                } else
                    if ((type == android.media.tv.TvTrackInfo.TYPE_VIDEO) && (!android.text.TextUtils.equals(trackId, mSelectedVideoTrackId))) {
                        mSelectedVideoTrackId = trackId;
                        return true;
                    } else
                        if ((type == android.media.tv.TvTrackInfo.TYPE_SUBTITLE) && (!android.text.TextUtils.equals(trackId, mSelectedSubtitleTrackId))) {
                            mSelectedSubtitleTrackId = trackId;
                            return true;
                        }


            }
            return false;
        }

        /**
         * Returns the new/updated video track that contains new video size information. Returns
         * null if there is no video track to notify. Subsequent calls of this method results in a
         * non-null video track returned only by the first call and null returned by following
         * calls. The caller should immediately notify of the video size change upon receiving the
         * track.
         */
        android.media.tv.TvTrackInfo getVideoTrackToNotify() {
            synchronized(mMetadataLock) {
                if ((!mVideoTracks.isEmpty()) && (mSelectedVideoTrackId != null)) {
                    for (android.media.tv.TvTrackInfo track : mVideoTracks) {
                        if (track.getId().equals(mSelectedVideoTrackId)) {
                            int videoWidth = track.getVideoWidth();
                            int videoHeight = track.getVideoHeight();
                            if ((mVideoWidth != videoWidth) || (mVideoHeight != videoHeight)) {
                                mVideoWidth = videoWidth;
                                mVideoHeight = videoHeight;
                                return track;
                            }
                        }
                    }
                }
            }
            return null;
        }

        /**
         * Plays a given recorded TV program.
         */
        void timeShiftPlay(android.net.Uri recordedProgramUri) {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.timeShiftPlay(mToken, recordedProgramUri, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Pauses the playback. Call {@link #timeShiftResume()} to restart the playback.
         */
        void timeShiftPause() {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.timeShiftPause(mToken, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Resumes the playback. No-op if it is already playing the channel.
         */
        void timeShiftResume() {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.timeShiftResume(mToken, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Seeks to a specified time position.
         *
         * <p>Normally, the position is given within range between the start and the current time,
         * inclusively.
         *
         * @param timeMs
         * 		The time position to seek to, in milliseconds since the epoch.
         * @see TvView.TimeShiftPositionCallback#onTimeShiftStartPositionChanged
         */
        void timeShiftSeekTo(long timeMs) {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.timeShiftSeekTo(mToken, timeMs, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Sets playback rate using {@link android.media.PlaybackParams}.
         *
         * @param params
         * 		The playback params.
         */
        void timeShiftSetPlaybackParams(android.media.PlaybackParams params) {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.timeShiftSetPlaybackParams(mToken, params, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Enable/disable position tracking.
         *
         * @param enable
         * 		{@code true} to enable tracking, {@code false} otherwise.
         */
        void timeShiftEnablePositionTracking(boolean enable) {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.timeShiftEnablePositionTracking(mToken, enable, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Starts TV program recording in the current recording session.
         *
         * @param programUri
         * 		The URI for the TV program to record as a hint, built by
         * 		{@link TvContract#buildProgramUri(long)}. Can be {@code null}.
         */
        void startRecording(@android.annotation.Nullable
        android.net.Uri programUri) {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.startRecording(mToken, programUri, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Stops TV program recording in the current recording session.
         */
        void stopRecording() {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.stopRecording(mToken, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Calls {@link TvInputService.Session#appPrivateCommand(String, Bundle)
         * TvInputService.Session.appPrivateCommand()} on the current TvView.
         *
         * @param action
         * 		Name of the command to be performed. This <em>must</em> be a scoped name,
         * 		i.e. prefixed with a package name you own, so that different developers will
         * 		not create conflicting commands.
         * @param data
         * 		Any data to include with the command.
         */
        public void sendAppPrivateCommand(java.lang.String action, android.os.Bundle data) {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.sendAppPrivateCommand(mToken, action, data, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Creates an overlay view. Once the overlay view is created, {@link #relayoutOverlayView}
         * should be called whenever the layout of its containing view is changed.
         * {@link #removeOverlayView()} should be called to remove the overlay view.
         * Since a session can have only one overlay view, this method should be called only once
         * or it can be called again after calling {@link #removeOverlayView()}.
         *
         * @param view
         * 		A view playing TV.
         * @param frame
         * 		A position of the overlay view.
         * @throws IllegalStateException
         * 		if {@code view} is not attached to a window.
         */
        void createOverlayView(@android.annotation.NonNull
        android.view.View view, @android.annotation.NonNull
        android.graphics.Rect frame) {
            com.android.internal.util.Preconditions.checkNotNull(view);
            com.android.internal.util.Preconditions.checkNotNull(frame);
            if (view.getWindowToken() == null) {
                throw new java.lang.IllegalStateException("view must be attached to a window");
            }
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.createOverlayView(mToken, view.getWindowToken(), frame, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Relayouts the current overlay view.
         *
         * @param frame
         * 		A new position of the overlay view.
         */
        void relayoutOverlayView(@android.annotation.NonNull
        android.graphics.Rect frame) {
            com.android.internal.util.Preconditions.checkNotNull(frame);
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.relayoutOverlayView(mToken, frame, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Removes the current overlay view.
         */
        void removeOverlayView() {
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.removeOverlayView(mToken, mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Requests to unblock content blocked by parental controls.
         */
        void unblockContent(@android.annotation.NonNull
        android.media.tv.TvContentRating unblockedRating) {
            com.android.internal.util.Preconditions.checkNotNull(unblockedRating);
            if (mToken == null) {
                android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                mService.unblockContent(mToken, unblockedRating.flattenToString(), mUserId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Dispatches an input event to this session.
         *
         * @param event
         * 		An {@link InputEvent} to dispatch. Cannot be {@code null}.
         * @param token
         * 		A token used to identify the input event later in the callback.
         * @param callback
         * 		A callback used to receive the dispatch result. Cannot be {@code null}.
         * @param handler
         * 		A {@link Handler} that the dispatch result will be delivered to. Cannot be
         * 		{@code null}.
         * @return Returns {@link #DISPATCH_HANDLED} if the event was handled. Returns
        {@link #DISPATCH_NOT_HANDLED} if the event was not handled. Returns
        {@link #DISPATCH_IN_PROGRESS} if the event is in progress and the callback will
        be invoked later.
         * @unknown 
         */
        public int dispatchInputEvent(@android.annotation.NonNull
        android.view.InputEvent event, java.lang.Object token, @android.annotation.NonNull
        android.media.tv.TvInputManager.Session.FinishedInputEventCallback callback, @android.annotation.NonNull
        android.os.Handler handler) {
            com.android.internal.util.Preconditions.checkNotNull(event);
            com.android.internal.util.Preconditions.checkNotNull(callback);
            com.android.internal.util.Preconditions.checkNotNull(handler);
            synchronized(mHandler) {
                if (mChannel == null) {
                    return android.media.tv.TvInputManager.Session.DISPATCH_NOT_HANDLED;
                }
                android.media.tv.TvInputManager.Session.PendingEvent p = obtainPendingEventLocked(event, token, callback, handler);
                if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
                    // Already running on the main thread so we can send the event immediately.
                    return sendInputEventOnMainLooperLocked(p);
                }
                // Post the event to the main thread.
                android.os.Message msg = mHandler.obtainMessage(android.media.tv.TvInputManager.Session.InputEventHandler.MSG_SEND_INPUT_EVENT, p);
                msg.setAsynchronous(true);
                mHandler.sendMessage(msg);
                return android.media.tv.TvInputManager.Session.DISPATCH_IN_PROGRESS;
            }
        }

        /**
         * Callback that is invoked when an input event that was dispatched to this session has been
         * finished.
         *
         * @unknown 
         */
        public interface FinishedInputEventCallback {
            /**
             * Called when the dispatched input event is finished.
             *
             * @param token
             * 		A token passed to {@link #dispatchInputEvent}.
             * @param handled
             * 		{@code true} if the dispatched input event was handled properly.
             * 		{@code false} otherwise.
             */
            void onFinishedInputEvent(java.lang.Object token, boolean handled);
        }

        // Must be called on the main looper
        private void sendInputEventAndReportResultOnMainLooper(android.media.tv.TvInputManager.Session.PendingEvent p) {
            synchronized(mHandler) {
                int result = sendInputEventOnMainLooperLocked(p);
                if (result == android.media.tv.TvInputManager.Session.DISPATCH_IN_PROGRESS) {
                    return;
                }
            }
            invokeFinishedInputEventCallback(p, false);
        }

        private int sendInputEventOnMainLooperLocked(android.media.tv.TvInputManager.Session.PendingEvent p) {
            if (mChannel != null) {
                if (mSender == null) {
                    mSender = new android.media.tv.TvInputManager.Session.TvInputEventSender(mChannel, mHandler.getLooper());
                }
                final android.view.InputEvent event = p.mEvent;
                final int seq = event.getSequenceNumber();
                if (mSender.sendInputEvent(seq, event)) {
                    mPendingEvents.put(seq, p);
                    android.os.Message msg = mHandler.obtainMessage(android.media.tv.TvInputManager.Session.InputEventHandler.MSG_TIMEOUT_INPUT_EVENT, p);
                    msg.setAsynchronous(true);
                    mHandler.sendMessageDelayed(msg, android.media.tv.TvInputManager.Session.INPUT_SESSION_NOT_RESPONDING_TIMEOUT);
                    return android.media.tv.TvInputManager.Session.DISPATCH_IN_PROGRESS;
                }
                android.util.Log.w(android.media.tv.TvInputManager.TAG, (("Unable to send input event to session: " + mToken) + " dropping:") + event);
            }
            return android.media.tv.TvInputManager.Session.DISPATCH_NOT_HANDLED;
        }

        void finishedInputEvent(int seq, boolean handled, boolean timeout) {
            final android.media.tv.TvInputManager.Session.PendingEvent p;
            synchronized(mHandler) {
                int index = mPendingEvents.indexOfKey(seq);
                if (index < 0) {
                    return;// spurious, event already finished or timed out

                }
                p = mPendingEvents.valueAt(index);
                mPendingEvents.removeAt(index);
                if (timeout) {
                    android.util.Log.w(android.media.tv.TvInputManager.TAG, (("Timeout waiting for session to handle input event after " + android.media.tv.TvInputManager.Session.INPUT_SESSION_NOT_RESPONDING_TIMEOUT) + " ms: ") + mToken);
                } else {
                    mHandler.removeMessages(android.media.tv.TvInputManager.Session.InputEventHandler.MSG_TIMEOUT_INPUT_EVENT, p);
                }
            }
            invokeFinishedInputEventCallback(p, handled);
        }

        // Assumes the event has already been removed from the queue.
        void invokeFinishedInputEventCallback(android.media.tv.TvInputManager.Session.PendingEvent p, boolean handled) {
            p.mHandled = handled;
            if (p.mEventHandler.getLooper().isCurrentThread()) {
                // Already running on the callback handler thread so we can send the callback
                // immediately.
                p.run();
            } else {
                // Post the event to the callback handler thread.
                // In this case, the callback will be responsible for recycling the event.
                android.os.Message msg = android.os.Message.obtain(p.mEventHandler, p);
                msg.setAsynchronous(true);
                msg.sendToTarget();
            }
        }

        private void flushPendingEventsLocked() {
            mHandler.removeMessages(android.media.tv.TvInputManager.Session.InputEventHandler.MSG_FLUSH_INPUT_EVENT);
            final int count = mPendingEvents.size();
            for (int i = 0; i < count; i++) {
                int seq = mPendingEvents.keyAt(i);
                android.os.Message msg = mHandler.obtainMessage(android.media.tv.TvInputManager.Session.InputEventHandler.MSG_FLUSH_INPUT_EVENT, seq, 0);
                msg.setAsynchronous(true);
                msg.sendToTarget();
            }
        }

        private android.media.tv.TvInputManager.Session.PendingEvent obtainPendingEventLocked(android.view.InputEvent event, java.lang.Object token, android.media.tv.TvInputManager.Session.FinishedInputEventCallback callback, android.os.Handler handler) {
            android.media.tv.TvInputManager.Session.PendingEvent p = mPendingEventPool.acquire();
            if (p == null) {
                p = new android.media.tv.TvInputManager.Session.PendingEvent();
            }
            p.mEvent = event;
            p.mEventToken = token;
            p.mCallback = callback;
            p.mEventHandler = handler;
            return p;
        }

        private void recyclePendingEventLocked(android.media.tv.TvInputManager.Session.PendingEvent p) {
            p.recycle();
            mPendingEventPool.release(p);
        }

        android.os.IBinder getToken() {
            return mToken;
        }

        private void releaseInternal() {
            mToken = null;
            synchronized(mHandler) {
                if (mChannel != null) {
                    if (mSender != null) {
                        flushPendingEventsLocked();
                        mSender.dispose();
                        mSender = null;
                    }
                    mChannel.dispose();
                    mChannel = null;
                }
            }
            synchronized(mSessionCallbackRecordMap) {
                mSessionCallbackRecordMap.remove(mSeq);
            }
        }

        private final class InputEventHandler extends android.os.Handler {
            public static final int MSG_SEND_INPUT_EVENT = 1;

            public static final int MSG_TIMEOUT_INPUT_EVENT = 2;

            public static final int MSG_FLUSH_INPUT_EVENT = 3;

            InputEventHandler(android.os.Looper looper) {
                super(looper, null, true);
            }

            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case android.media.tv.TvInputManager.Session.InputEventHandler.MSG_SEND_INPUT_EVENT :
                        {
                            sendInputEventAndReportResultOnMainLooper(((android.media.tv.TvInputManager.Session.PendingEvent) (msg.obj)));
                            return;
                        }
                    case android.media.tv.TvInputManager.Session.InputEventHandler.MSG_TIMEOUT_INPUT_EVENT :
                        {
                            finishedInputEvent(msg.arg1, false, true);
                            return;
                        }
                    case android.media.tv.TvInputManager.Session.InputEventHandler.MSG_FLUSH_INPUT_EVENT :
                        {
                            finishedInputEvent(msg.arg1, false, false);
                            return;
                        }
                }
            }
        }

        private final class TvInputEventSender extends android.view.InputEventSender {
            public TvInputEventSender(android.view.InputChannel inputChannel, android.os.Looper looper) {
                super(inputChannel, looper);
            }

            @java.lang.Override
            public void onInputEventFinished(int seq, boolean handled) {
                finishedInputEvent(seq, handled, false);
            }
        }

        private final class PendingEvent implements java.lang.Runnable {
            public android.view.InputEvent mEvent;

            public java.lang.Object mEventToken;

            public android.media.tv.TvInputManager.Session.FinishedInputEventCallback mCallback;

            public android.os.Handler mEventHandler;

            public boolean mHandled;

            public void recycle() {
                mEvent = null;
                mEventToken = null;
                mCallback = null;
                mEventHandler = null;
                mHandled = false;
            }

            @java.lang.Override
            public void run() {
                mCallback.onFinishedInputEvent(mEventToken, mHandled);
                synchronized(mEventHandler) {
                    recyclePendingEventLocked(this);
                }
            }
        }
    }

    /**
     * The Hardware provides the per-hardware functionality of TV hardware.
     *
     * <p>TV hardware is physical hardware attached to the Android device; for example, HDMI ports,
     * Component/Composite ports, etc. Specifically, logical devices such as HDMI CEC logical
     * devices don't fall into this category.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final class Hardware {
        private final android.media.tv.ITvInputHardware mInterface;

        private Hardware(android.media.tv.ITvInputHardware hardwareInterface) {
            mInterface = hardwareInterface;
        }

        private android.media.tv.ITvInputHardware getInterface() {
            return mInterface;
        }

        public boolean setSurface(android.view.Surface surface, android.media.tv.TvStreamConfig config) {
            try {
                return mInterface.setSurface(surface, config);
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException(e);
            }
        }

        public void setStreamVolume(float volume) {
            try {
                mInterface.setStreamVolume(volume);
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException(e);
            }
        }

        public boolean dispatchKeyEventToHdmi(android.view.KeyEvent event) {
            try {
                return mInterface.dispatchKeyEventToHdmi(event);
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException(e);
            }
        }

        public void overrideAudioSink(int audioType, java.lang.String audioAddress, int samplingRate, int channelMask, int format) {
            try {
                mInterface.overrideAudioSink(audioType, audioAddress, samplingRate, channelMask, format);
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException(e);
            }
        }
    }
}

