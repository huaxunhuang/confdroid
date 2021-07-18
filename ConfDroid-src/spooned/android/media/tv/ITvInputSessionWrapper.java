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
 * Implements the internal ITvInputSession interface to convert incoming calls on to it back to
 * calls on the public TvInputSession interface, scheduling them on the main thread of the process.
 *
 * @unknown 
 */
public class ITvInputSessionWrapper extends android.media.tv.ITvInputSession.Stub implements com.android.internal.os.HandlerCaller.Callback {
    private static final java.lang.String TAG = "TvInputSessionWrapper";

    private static final int EXECUTE_MESSAGE_TIMEOUT_SHORT_MILLIS = 50;

    private static final int EXECUTE_MESSAGE_TUNE_TIMEOUT_MILLIS = 2000;

    private static final int EXECUTE_MESSAGE_TIMEOUT_LONG_MILLIS = 5 * 1000;

    private static final int DO_RELEASE = 1;

    private static final int DO_SET_MAIN = 2;

    private static final int DO_SET_SURFACE = 3;

    private static final int DO_DISPATCH_SURFACE_CHANGED = 4;

    private static final int DO_SET_STREAM_VOLUME = 5;

    private static final int DO_TUNE = 6;

    private static final int DO_SET_CAPTION_ENABLED = 7;

    private static final int DO_SELECT_TRACK = 8;

    private static final int DO_APP_PRIVATE_COMMAND = 9;

    private static final int DO_CREATE_OVERLAY_VIEW = 10;

    private static final int DO_RELAYOUT_OVERLAY_VIEW = 11;

    private static final int DO_REMOVE_OVERLAY_VIEW = 12;

    private static final int DO_UNBLOCK_CONTENT = 13;

    private static final int DO_TIME_SHIFT_PLAY = 14;

    private static final int DO_TIME_SHIFT_PAUSE = 15;

    private static final int DO_TIME_SHIFT_RESUME = 16;

    private static final int DO_TIME_SHIFT_SEEK_TO = 17;

    private static final int DO_TIME_SHIFT_SET_PLAYBACK_PARAMS = 18;

    private static final int DO_TIME_SHIFT_ENABLE_POSITION_TRACKING = 19;

    private static final int DO_START_RECORDING = 20;

    private static final int DO_STOP_RECORDING = 21;

    private final boolean mIsRecordingSession;

    private final com.android.internal.os.HandlerCaller mCaller;

    private android.media.tv.TvInputService.Session mTvInputSessionImpl;

    private android.media.tv.TvInputService.RecordingSession mTvInputRecordingSessionImpl;

    private android.view.InputChannel mChannel;

    private android.media.tv.ITvInputSessionWrapper.TvInputEventReceiver mReceiver;

    public ITvInputSessionWrapper(android.content.Context context, android.media.tv.TvInputService.Session sessionImpl, android.view.InputChannel channel) {
        mIsRecordingSession = false;
        mCaller = /* asyncHandler */
        new com.android.internal.os.HandlerCaller(context, null, this, true);
        mTvInputSessionImpl = sessionImpl;
        mChannel = channel;
        if (channel != null) {
            mReceiver = new android.media.tv.ITvInputSessionWrapper.TvInputEventReceiver(channel, context.getMainLooper());
        }
    }

    // For the recording session
    public ITvInputSessionWrapper(android.content.Context context, android.media.tv.TvInputService.RecordingSession recordingSessionImpl) {
        mIsRecordingSession = true;
        mCaller = /* asyncHandler */
        new com.android.internal.os.HandlerCaller(context, null, this, true);
        mTvInputRecordingSessionImpl = recordingSessionImpl;
    }

    @java.lang.Override
    public void executeMessage(android.os.Message msg) {
        if ((mIsRecordingSession && (mTvInputRecordingSessionImpl == null)) || ((!mIsRecordingSession) && (mTvInputSessionImpl == null))) {
            return;
        }
        long startTime = java.lang.System.nanoTime();
        switch (msg.what) {
            case android.media.tv.ITvInputSessionWrapper.DO_RELEASE :
                {
                    if (mIsRecordingSession) {
                        mTvInputRecordingSessionImpl.release();
                        mTvInputRecordingSessionImpl = null;
                    } else {
                        mTvInputSessionImpl.release();
                        mTvInputSessionImpl = null;
                        if (mReceiver != null) {
                            mReceiver.dispose();
                            mReceiver = null;
                        }
                        if (mChannel != null) {
                            mChannel.dispose();
                            mChannel = null;
                        }
                    }
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_SET_MAIN :
                {
                    mTvInputSessionImpl.setMain(((java.lang.Boolean) (msg.obj)));
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_SET_SURFACE :
                {
                    mTvInputSessionImpl.setSurface(((android.view.Surface) (msg.obj)));
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_DISPATCH_SURFACE_CHANGED :
                {
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    mTvInputSessionImpl.dispatchSurfaceChanged(args.argi1, args.argi2, args.argi3);
                    args.recycle();
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_SET_STREAM_VOLUME :
                {
                    mTvInputSessionImpl.setStreamVolume(((java.lang.Float) (msg.obj)));
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_TUNE :
                {
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    if (mIsRecordingSession) {
                        mTvInputRecordingSessionImpl.tune(((android.net.Uri) (args.arg1)), ((android.os.Bundle) (args.arg2)));
                    } else {
                        mTvInputSessionImpl.tune(((android.net.Uri) (args.arg1)), ((android.os.Bundle) (args.arg2)));
                    }
                    args.recycle();
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_SET_CAPTION_ENABLED :
                {
                    mTvInputSessionImpl.setCaptionEnabled(((java.lang.Boolean) (msg.obj)));
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_SELECT_TRACK :
                {
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    mTvInputSessionImpl.selectTrack(((java.lang.Integer) (args.arg1)), ((java.lang.String) (args.arg2)));
                    args.recycle();
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_APP_PRIVATE_COMMAND :
                {
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    if (mIsRecordingSession) {
                        mTvInputRecordingSessionImpl.appPrivateCommand(((java.lang.String) (args.arg1)), ((android.os.Bundle) (args.arg2)));
                    } else {
                        mTvInputSessionImpl.appPrivateCommand(((java.lang.String) (args.arg1)), ((android.os.Bundle) (args.arg2)));
                    }
                    args.recycle();
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_CREATE_OVERLAY_VIEW :
                {
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    mTvInputSessionImpl.createOverlayView(((android.os.IBinder) (args.arg1)), ((android.graphics.Rect) (args.arg2)));
                    args.recycle();
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_RELAYOUT_OVERLAY_VIEW :
                {
                    mTvInputSessionImpl.relayoutOverlayView(((android.graphics.Rect) (msg.obj)));
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_REMOVE_OVERLAY_VIEW :
                {
                    mTvInputSessionImpl.removeOverlayView(true);
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_UNBLOCK_CONTENT :
                {
                    mTvInputSessionImpl.unblockContent(((java.lang.String) (msg.obj)));
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_PLAY :
                {
                    mTvInputSessionImpl.timeShiftPlay(((android.net.Uri) (msg.obj)));
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_PAUSE :
                {
                    mTvInputSessionImpl.timeShiftPause();
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_RESUME :
                {
                    mTvInputSessionImpl.timeShiftResume();
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_SEEK_TO :
                {
                    mTvInputSessionImpl.timeShiftSeekTo(((java.lang.Long) (msg.obj)));
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_SET_PLAYBACK_PARAMS :
                {
                    mTvInputSessionImpl.timeShiftSetPlaybackParams(((android.media.PlaybackParams) (msg.obj)));
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_ENABLE_POSITION_TRACKING :
                {
                    mTvInputSessionImpl.timeShiftEnablePositionTracking(((java.lang.Boolean) (msg.obj)));
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_START_RECORDING :
                {
                    mTvInputRecordingSessionImpl.startRecording(((android.net.Uri) (msg.obj)));
                    break;
                }
            case android.media.tv.ITvInputSessionWrapper.DO_STOP_RECORDING :
                {
                    mTvInputRecordingSessionImpl.stopRecording();
                    break;
                }
            default :
                {
                    android.util.Log.w(android.media.tv.ITvInputSessionWrapper.TAG, "Unhandled message code: " + msg.what);
                    break;
                }
        }
        long durationMs = (java.lang.System.nanoTime() - startTime) / (1000 * 1000);
        if (durationMs > android.media.tv.ITvInputSessionWrapper.EXECUTE_MESSAGE_TIMEOUT_SHORT_MILLIS) {
            android.util.Log.w(android.media.tv.ITvInputSessionWrapper.TAG, ((("Handling message (" + msg.what) + ") took too long time (duration=") + durationMs) + "ms)");
            if ((msg.what == android.media.tv.ITvInputSessionWrapper.DO_TUNE) && (durationMs > android.media.tv.ITvInputSessionWrapper.EXECUTE_MESSAGE_TUNE_TIMEOUT_MILLIS)) {
                throw new java.lang.RuntimeException((((("Too much time to handle tune request. (" + durationMs) + "ms > ") + android.media.tv.ITvInputSessionWrapper.EXECUTE_MESSAGE_TUNE_TIMEOUT_MILLIS) + "ms) ") + "Consider handling the tune request in a separate thread.");
            }
            if (durationMs > android.media.tv.ITvInputSessionWrapper.EXECUTE_MESSAGE_TIMEOUT_LONG_MILLIS) {
                throw new java.lang.RuntimeException(((((("Too much time to handle a request. (type=" + msg.what) + ", ") + durationMs) + "ms > ") + android.media.tv.ITvInputSessionWrapper.EXECUTE_MESSAGE_TIMEOUT_LONG_MILLIS) + "ms).");
            }
        }
    }

    @java.lang.Override
    public void release() {
        if (!mIsRecordingSession) {
            mTvInputSessionImpl.scheduleOverlayViewCleanup();
        }
        mCaller.executeOrSendMessage(mCaller.obtainMessage(android.media.tv.ITvInputSessionWrapper.DO_RELEASE));
    }

    @java.lang.Override
    public void setMain(boolean isMain) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.media.tv.ITvInputSessionWrapper.DO_SET_MAIN, isMain));
    }

    @java.lang.Override
    public void setSurface(android.view.Surface surface) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.media.tv.ITvInputSessionWrapper.DO_SET_SURFACE, surface));
    }

    @java.lang.Override
    public void dispatchSurfaceChanged(int format, int width, int height) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageIIII(android.media.tv.ITvInputSessionWrapper.DO_DISPATCH_SURFACE_CHANGED, format, width, height, 0));
    }

    @java.lang.Override
    public final void setVolume(float volume) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.media.tv.ITvInputSessionWrapper.DO_SET_STREAM_VOLUME, volume));
    }

    @java.lang.Override
    public void tune(android.net.Uri channelUri, android.os.Bundle params) {
        // Clear the pending tune requests.
        mCaller.removeMessages(android.media.tv.ITvInputSessionWrapper.DO_TUNE);
        mCaller.executeOrSendMessage(mCaller.obtainMessageOO(android.media.tv.ITvInputSessionWrapper.DO_TUNE, channelUri, params));
    }

    @java.lang.Override
    public void setCaptionEnabled(boolean enabled) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.media.tv.ITvInputSessionWrapper.DO_SET_CAPTION_ENABLED, enabled));
    }

    @java.lang.Override
    public void selectTrack(int type, java.lang.String trackId) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageOO(android.media.tv.ITvInputSessionWrapper.DO_SELECT_TRACK, type, trackId));
    }

    @java.lang.Override
    public void appPrivateCommand(java.lang.String action, android.os.Bundle data) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageOO(android.media.tv.ITvInputSessionWrapper.DO_APP_PRIVATE_COMMAND, action, data));
    }

    @java.lang.Override
    public void createOverlayView(android.os.IBinder windowToken, android.graphics.Rect frame) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageOO(android.media.tv.ITvInputSessionWrapper.DO_CREATE_OVERLAY_VIEW, windowToken, frame));
    }

    @java.lang.Override
    public void relayoutOverlayView(android.graphics.Rect frame) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.media.tv.ITvInputSessionWrapper.DO_RELAYOUT_OVERLAY_VIEW, frame));
    }

    @java.lang.Override
    public void removeOverlayView() {
        mCaller.executeOrSendMessage(mCaller.obtainMessage(android.media.tv.ITvInputSessionWrapper.DO_REMOVE_OVERLAY_VIEW));
    }

    @java.lang.Override
    public void unblockContent(java.lang.String unblockedRating) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.media.tv.ITvInputSessionWrapper.DO_UNBLOCK_CONTENT, unblockedRating));
    }

    @java.lang.Override
    public void timeShiftPlay(android.net.Uri recordedProgramUri) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_PLAY, recordedProgramUri));
    }

    @java.lang.Override
    public void timeShiftPause() {
        mCaller.executeOrSendMessage(mCaller.obtainMessage(android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_PAUSE));
    }

    @java.lang.Override
    public void timeShiftResume() {
        mCaller.executeOrSendMessage(mCaller.obtainMessage(android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_RESUME));
    }

    @java.lang.Override
    public void timeShiftSeekTo(long timeMs) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_SEEK_TO, timeMs));
    }

    @java.lang.Override
    public void timeShiftSetPlaybackParams(android.media.PlaybackParams params) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_SET_PLAYBACK_PARAMS, params));
    }

    @java.lang.Override
    public void timeShiftEnablePositionTracking(boolean enable) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.media.tv.ITvInputSessionWrapper.DO_TIME_SHIFT_ENABLE_POSITION_TRACKING, enable));
    }

    @java.lang.Override
    public void startRecording(@android.annotation.Nullable
    android.net.Uri programUri) {
        mCaller.executeOrSendMessage(mCaller.obtainMessageO(android.media.tv.ITvInputSessionWrapper.DO_START_RECORDING, programUri));
    }

    @java.lang.Override
    public void stopRecording() {
        mCaller.executeOrSendMessage(mCaller.obtainMessage(android.media.tv.ITvInputSessionWrapper.DO_STOP_RECORDING));
    }

    private final class TvInputEventReceiver extends android.view.InputEventReceiver {
        public TvInputEventReceiver(android.view.InputChannel inputChannel, android.os.Looper looper) {
            super(inputChannel, looper);
        }

        @java.lang.Override
        public void onInputEvent(android.view.InputEvent event) {
            if (mTvInputSessionImpl == null) {
                // The session has been finished.
                finishInputEvent(event, false);
                return;
            }
            int handled = mTvInputSessionImpl.dispatchInputEvent(event, this);
            if (handled != android.media.tv.TvInputManager.Session.DISPATCH_IN_PROGRESS) {
                finishInputEvent(event, handled == android.media.tv.TvInputManager.Session.DISPATCH_HANDLED);
            }
        }
    }
}

