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
 * Displays TV contents. The TvView class provides a high level interface for applications to show
 * TV programs from various TV sources that implement {@link TvInputService}. (Note that the list of
 * TV inputs available on the system can be obtained by calling
 * {@link TvInputManager#getTvInputList() TvInputManager.getTvInputList()}.)
 *
 * <p>Once the application supplies the URI for a specific TV channel to {@link #tune}
 * method, it takes care of underlying service binding (and unbinding if the current TvView is
 * already bound to a service) and automatically allocates/deallocates resources needed. In addition
 * to a few essential methods to control how the contents are presented, it also provides a way to
 * dispatch input events to the connected TvInputService in order to enable custom key actions for
 * the TV input.
 */
public class TvView extends android.view.ViewGroup {
    private static final java.lang.String TAG = "TvView";

    private static final boolean DEBUG = false;

    private static final int ZORDER_MEDIA = 0;

    private static final int ZORDER_MEDIA_OVERLAY = 1;

    private static final int ZORDER_ON_TOP = 2;

    private static final java.lang.ref.WeakReference<android.media.tv.TvView> NULL_TV_VIEW = new java.lang.ref.WeakReference<>(null);

    private static final java.lang.Object sMainTvViewLock = new java.lang.Object();

    private static java.lang.ref.WeakReference<android.media.tv.TvView> sMainTvView = android.media.tv.TvView.NULL_TV_VIEW;

    private final android.os.Handler mHandler = new android.os.Handler();

    private android.media.tv.TvInputManager.Session mSession;

    private android.view.SurfaceView mSurfaceView;

    private android.view.Surface mSurface;

    private boolean mOverlayViewCreated;

    private android.graphics.Rect mOverlayViewFrame;

    private final android.media.tv.TvInputManager mTvInputManager;

    private android.media.tv.TvView.MySessionCallback mSessionCallback;

    private android.media.tv.TvView.TvInputCallback mCallback;

    private android.media.tv.TvView.OnUnhandledInputEventListener mOnUnhandledInputEventListener;

    private java.lang.Float mStreamVolume;

    private java.lang.Boolean mCaptionEnabled;

    private final java.util.Queue<android.util.Pair<java.lang.String, android.os.Bundle>> mPendingAppPrivateCommands = new java.util.ArrayDeque<>();

    private boolean mSurfaceChanged;

    private int mSurfaceFormat;

    private int mSurfaceWidth;

    private int mSurfaceHeight;

    private final android.util.AttributeSet mAttrs;

    private final int mDefStyleAttr;

    private int mWindowZOrder;

    private boolean mUseRequestedSurfaceLayout;

    private int mSurfaceViewLeft;

    private int mSurfaceViewRight;

    private int mSurfaceViewTop;

    private int mSurfaceViewBottom;

    private android.media.tv.TvView.TimeShiftPositionCallback mTimeShiftPositionCallback;

    private final android.view.SurfaceHolder.Callback mSurfaceHolderCallback = new android.view.SurfaceHolder.Callback() {
        @java.lang.Override
        public void surfaceChanged(android.view.SurfaceHolder holder, int format, int width, int height) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, ((((((("surfaceChanged(holder=" + holder) + ", format=") + format) + ", width=") + width) + ", height=") + height) + ")");
            }
            mSurfaceFormat = format;
            mSurfaceWidth = width;
            mSurfaceHeight = height;
            mSurfaceChanged = true;
            dispatchSurfaceChanged(mSurfaceFormat, mSurfaceWidth, mSurfaceHeight);
        }

        @java.lang.Override
        public void surfaceCreated(android.view.SurfaceHolder holder) {
            mSurface = holder.getSurface();
            setSessionSurface(mSurface);
        }

        @java.lang.Override
        public void surfaceDestroyed(android.view.SurfaceHolder holder) {
            mSurface = null;
            mSurfaceChanged = false;
            setSessionSurface(null);
        }
    };

    private final android.media.tv.TvInputManager.Session.FinishedInputEventCallback mFinishedInputEventCallback = new android.media.tv.TvInputManager.Session.FinishedInputEventCallback() {
        @java.lang.Override
        public void onFinishedInputEvent(java.lang.Object token, boolean handled) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, ((("onFinishedInputEvent(token=" + token) + ", handled=") + handled) + ")");
            }
            if (handled) {
                return;
            }
            // TODO: Re-order unhandled events.
            android.view.InputEvent event = ((android.view.InputEvent) (token));
            if (dispatchUnhandledInputEvent(event)) {
                return;
            }
            android.view.ViewRootImpl viewRootImpl = getViewRootImpl();
            if (viewRootImpl != null) {
                viewRootImpl.dispatchUnhandledInputEvent(event);
            }
        }
    };

    public TvView(android.content.Context context) {
        this(context, null, 0);
    }

    public TvView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttrs = attrs;
        mDefStyleAttr = defStyleAttr;
        resetSurfaceView();
        mTvInputManager = ((android.media.tv.TvInputManager) (getContext().getSystemService(android.content.Context.TV_INPUT_SERVICE)));
    }

    /**
     * Sets the callback to be invoked when an event is dispatched to this TvView.
     *
     * @param callback
     * 		The callback to receive events. A value of {@code null} removes the existing
     * 		callback.
     */
    public void setCallback(@android.annotation.Nullable
    android.media.tv.TvView.TvInputCallback callback) {
        mCallback = callback;
    }

    /**
     * Sets this as the main {@link TvView}.
     *
     * <p>The main {@link TvView} is a {@link TvView} whose corresponding TV input determines the
     * HDMI-CEC active source device. For an HDMI port input, one of source devices that is
     * connected to that HDMI port becomes the active source. For an HDMI-CEC logical device input,
     * the corresponding HDMI-CEC logical device becomes the active source. For any non-HDMI input
     * (including the tuner, composite, S-Video, etc.), the internal device (= TV itself) becomes
     * the active source.
     *
     * <p>First tuned {@link TvView} becomes main automatically, and keeps to be main until either
     * {@link #reset} is called for the main {@link TvView} or {@code setMain()} is called for other
     * {@link TvView}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public void setMain() {
        synchronized(android.media.tv.TvView.sMainTvViewLock) {
            android.media.tv.TvView.sMainTvView = new java.lang.ref.WeakReference<>(this);
            if (hasWindowFocus() && (mSession != null)) {
                mSession.setMain();
            }
        }
    }

    /**
     * Controls whether the TvView's surface is placed on top of another regular surface view in the
     * window (but still behind the window itself).
     * This is typically used to place overlays on top of an underlying TvView.
     *
     * <p>Note that this must be set before the TvView's containing window is attached to the
     * window manager.
     *
     * <p>Calling this overrides any previous call to {@link #setZOrderOnTop}.
     *
     * @param isMediaOverlay
     * 		{@code true} to be on top of another regular surface, {@code false}
     * 		otherwise.
     */
    public void setZOrderMediaOverlay(boolean isMediaOverlay) {
        if (isMediaOverlay) {
            mWindowZOrder = android.media.tv.TvView.ZORDER_MEDIA_OVERLAY;
            removeSessionOverlayView();
        } else {
            mWindowZOrder = android.media.tv.TvView.ZORDER_MEDIA;
            createSessionOverlayView();
        }
        if (mSurfaceView != null) {
            // ZOrderOnTop(false) removes WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
            // from WindowLayoutParam as well as changes window type.
            mSurfaceView.setZOrderOnTop(false);
            mSurfaceView.setZOrderMediaOverlay(isMediaOverlay);
        }
    }

    /**
     * Controls whether the TvView's surface is placed on top of its window. Normally it is placed
     * behind the window, to allow it to (for the most part) appear to composite with the views in
     * the hierarchy.  By setting this, you cause it to be placed above the window. This means that
     * none of the contents of the window this TvView is in will be visible on top of its surface.
     *
     * <p>Note that this must be set before the TvView's containing window is attached to the window
     * manager.
     *
     * <p>Calling this overrides any previous call to {@link #setZOrderMediaOverlay}.
     *
     * @param onTop
     * 		{@code true} to be on top of its window, {@code false} otherwise.
     */
    public void setZOrderOnTop(boolean onTop) {
        if (onTop) {
            mWindowZOrder = android.media.tv.TvView.ZORDER_ON_TOP;
            removeSessionOverlayView();
        } else {
            mWindowZOrder = android.media.tv.TvView.ZORDER_MEDIA;
            createSessionOverlayView();
        }
        if (mSurfaceView != null) {
            mSurfaceView.setZOrderMediaOverlay(false);
            mSurfaceView.setZOrderOnTop(onTop);
        }
    }

    /**
     * Sets the relative stream volume of this TvView.
     *
     * <p>This method is primarily used to handle audio focus changes or mute a specific TvView when
     * multiple views are displayed. If the method has not yet been called, the TvView assumes the
     * default value of {@code 1.0f}.
     *
     * @param volume
     * 		A volume value between {@code 0.0f} to {@code 1.0f}.
     */
    public void setStreamVolume(@android.annotation.FloatRange(from = 0.0, to = 1.0)
    float volume) {
        if (android.media.tv.TvView.DEBUG)
            android.util.Log.d(android.media.tv.TvView.TAG, ("setStreamVolume(" + volume) + ")");

        mStreamVolume = volume;
        if (mSession == null) {
            // Volume will be set once the connection has been made.
            return;
        }
        mSession.setStreamVolume(volume);
    }

    /**
     * Tunes to a given channel.
     *
     * @param inputId
     * 		The ID of the TV input for the given channel.
     * @param channelUri
     * 		The URI of a channel.
     */
    public void tune(@android.annotation.NonNull
    java.lang.String inputId, android.net.Uri channelUri) {
        tune(inputId, channelUri, null);
    }

    /**
     * Tunes to a given channel. This can be used to provide domain-specific features that are only
     * known between certain clients and their TV inputs.
     *
     * @param inputId
     * 		The ID of TV input for the given channel.
     * @param channelUri
     * 		The URI of a channel.
     * @param params
     * 		Domain-specific data for this tune request. Keys <em>must</em> be a scoped
     * 		name, i.e. prefixed with a package name you own, so that different developers will
     * 		not create conflicting keys.
     */
    public void tune(java.lang.String inputId, android.net.Uri channelUri, android.os.Bundle params) {
        if (android.media.tv.TvView.DEBUG)
            android.util.Log.d(android.media.tv.TvView.TAG, ("tune(" + channelUri) + ")");

        if (android.text.TextUtils.isEmpty(inputId)) {
            throw new java.lang.IllegalArgumentException("inputId cannot be null or an empty string");
        }
        synchronized(android.media.tv.TvView.sMainTvViewLock) {
            if (android.media.tv.TvView.sMainTvView.get() == null) {
                android.media.tv.TvView.sMainTvView = new java.lang.ref.WeakReference<>(this);
            }
        }
        if ((mSessionCallback != null) && android.text.TextUtils.equals(mSessionCallback.mInputId, inputId)) {
            if (mSession != null) {
                mSession.tune(channelUri, params);
            } else {
                // createSession() was called but the actual session for the given inputId has not
                // yet been created. Just replace the existing tuning params in the callback with
                // the new ones and tune later in onSessionCreated(). It is not necessary to create
                // a new callback because this tuning request was made on the same inputId.
                mSessionCallback.mChannelUri = channelUri;
                mSessionCallback.mTuneParams = params;
            }
        } else {
            resetInternal();
            // In case createSession() is called multiple times across different inputId's before
            // any session is created (e.g. when quickly tuning to a channel from input A and then
            // to another channel from input B), only the callback for the last createSession()
            // should be invoked. (The previous callbacks are simply ignored.) To do that, we create
            // a new callback each time and keep mSessionCallback pointing to the last one. If
            // MySessionCallback.this is different from mSessionCallback, we know that this callback
            // is obsolete and should ignore it.
            mSessionCallback = new android.media.tv.TvView.MySessionCallback(inputId, channelUri, params);
            if (mTvInputManager != null) {
                mTvInputManager.createSession(inputId, mSessionCallback, mHandler);
            }
        }
    }

    /**
     * Resets this TvView.
     *
     * <p>This method is primarily used to un-tune the current TvView.
     */
    public void reset() {
        if (android.media.tv.TvView.DEBUG)
            android.util.Log.d(android.media.tv.TvView.TAG, "reset()");

        synchronized(android.media.tv.TvView.sMainTvViewLock) {
            if (this == android.media.tv.TvView.sMainTvView.get()) {
                android.media.tv.TvView.sMainTvView = android.media.tv.TvView.NULL_TV_VIEW;
            }
        }
        resetInternal();
    }

    private void resetInternal() {
        mSessionCallback = null;
        mPendingAppPrivateCommands.clear();
        if (mSession != null) {
            setSessionSurface(null);
            removeSessionOverlayView();
            mUseRequestedSurfaceLayout = false;
            mSession.release();
            mSession = null;
            resetSurfaceView();
        }
    }

    /**
     * Requests to unblock TV content according to the given rating.
     *
     * <p>This notifies TV input that blocked content is now OK to play.
     *
     * @param unblockedRating
     * 		A TvContentRating to unblock.
     * @see TvInputService.Session#notifyContentBlocked(TvContentRating)
     * @unknown 
     */
    public void requestUnblockContent(android.media.tv.TvContentRating unblockedRating) {
        unblockContent(unblockedRating);
    }

    /**
     * Requests to unblock TV content according to the given rating.
     *
     * <p>This notifies TV input that blocked content is now OK to play.
     *
     * @param unblockedRating
     * 		A TvContentRating to unblock.
     * @see TvInputService.Session#notifyContentBlocked(TvContentRating)
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(android.media.tv.android.Manifest.permission.MODIFY_PARENTAL_CONTROLS.class)
    public void unblockContent(android.media.tv.TvContentRating unblockedRating) {
        if (mSession != null) {
            mSession.unblockContent(unblockedRating);
        }
    }

    /**
     * Enables or disables the caption in this TvView.
     *
     * <p>Note that this method does not take any effect unless the current TvView is tuned.
     *
     * @param enabled
     * 		{@code true} to enable, {@code false} to disable.
     */
    public void setCaptionEnabled(boolean enabled) {
        if (android.media.tv.TvView.DEBUG)
            android.util.Log.d(android.media.tv.TvView.TAG, ("setCaptionEnabled(" + enabled) + ")");

        mCaptionEnabled = enabled;
        if (mSession != null) {
            mSession.setCaptionEnabled(enabled);
        }
    }

    /**
     * Selects a track.
     *
     * @param type
     * 		The type of the track to select. The type can be {@link TvTrackInfo#TYPE_AUDIO},
     * 		{@link TvTrackInfo#TYPE_VIDEO} or {@link TvTrackInfo#TYPE_SUBTITLE}.
     * @param trackId
     * 		The ID of the track to select. {@code null} means to unselect the current
     * 		track for a given type.
     * @see #getTracks
     * @see #getSelectedTrack
     */
    public void selectTrack(int type, java.lang.String trackId) {
        if (mSession != null) {
            mSession.selectTrack(type, trackId);
        }
    }

    /**
     * Returns the list of tracks. Returns {@code null} if the information is not available.
     *
     * @param type
     * 		The type of the tracks. The type can be {@link TvTrackInfo#TYPE_AUDIO},
     * 		{@link TvTrackInfo#TYPE_VIDEO} or {@link TvTrackInfo#TYPE_SUBTITLE}.
     * @see #selectTrack
     * @see #getSelectedTrack
     */
    public java.util.List<android.media.tv.TvTrackInfo> getTracks(int type) {
        if (mSession == null) {
            return null;
        }
        return mSession.getTracks(type);
    }

    /**
     * Returns the ID of the selected track for a given type. Returns {@code null} if the
     * information is not available or the track is not selected.
     *
     * @param type
     * 		The type of the selected tracks. The type can be {@link TvTrackInfo#TYPE_AUDIO},
     * 		{@link TvTrackInfo#TYPE_VIDEO} or {@link TvTrackInfo#TYPE_SUBTITLE}.
     * @see #selectTrack
     * @see #getTracks
     */
    public java.lang.String getSelectedTrack(int type) {
        if (mSession == null) {
            return null;
        }
        return mSession.getSelectedTrack(type);
    }

    /**
     * Plays a given recorded TV program.
     *
     * @param inputId
     * 		The ID of the TV input that created the given recorded program.
     * @param recordedProgramUri
     * 		The URI of a recorded program.
     */
    public void timeShiftPlay(java.lang.String inputId, android.net.Uri recordedProgramUri) {
        if (android.media.tv.TvView.DEBUG)
            android.util.Log.d(android.media.tv.TvView.TAG, ("timeShiftPlay(" + recordedProgramUri) + ")");

        if (android.text.TextUtils.isEmpty(inputId)) {
            throw new java.lang.IllegalArgumentException("inputId cannot be null or an empty string");
        }
        synchronized(android.media.tv.TvView.sMainTvViewLock) {
            if (android.media.tv.TvView.sMainTvView.get() == null) {
                android.media.tv.TvView.sMainTvView = new java.lang.ref.WeakReference<>(this);
            }
        }
        if ((mSessionCallback != null) && android.text.TextUtils.equals(mSessionCallback.mInputId, inputId)) {
            if (mSession != null) {
                mSession.timeShiftPlay(recordedProgramUri);
            } else {
                mSessionCallback.mRecordedProgramUri = recordedProgramUri;
            }
        } else {
            resetInternal();
            mSessionCallback = new android.media.tv.TvView.MySessionCallback(inputId, recordedProgramUri);
            if (mTvInputManager != null) {
                mTvInputManager.createSession(inputId, mSessionCallback, mHandler);
            }
        }
    }

    /**
     * Pauses playback. No-op if it is already paused. Call {@link #timeShiftResume} to resume.
     */
    public void timeShiftPause() {
        if (mSession != null) {
            mSession.timeShiftPause();
        }
    }

    /**
     * Resumes playback. No-op if it is already resumed. Call {@link #timeShiftPause} to pause.
     */
    public void timeShiftResume() {
        if (mSession != null) {
            mSession.timeShiftResume();
        }
    }

    /**
     * Seeks to a specified time position. {@code timeMs} must be equal to or greater than the start
     * position returned by {@link TimeShiftPositionCallback#onTimeShiftStartPositionChanged} and
     * equal to or less than the current time.
     *
     * @param timeMs
     * 		The time position to seek to, in milliseconds since the epoch.
     */
    public void timeShiftSeekTo(long timeMs) {
        if (mSession != null) {
            mSession.timeShiftSeekTo(timeMs);
        }
    }

    /**
     * Sets playback rate using {@link android.media.PlaybackParams}.
     *
     * @param params
     * 		The playback params.
     */
    public void timeShiftSetPlaybackParams(@android.annotation.NonNull
    android.media.PlaybackParams params) {
        if (mSession != null) {
            mSession.timeShiftSetPlaybackParams(params);
        }
    }

    /**
     * Sets the callback to be invoked when the time shift position is changed.
     *
     * @param callback
     * 		The callback to receive time shift position changes. A value of {@code null}
     * 		removes the existing callback.
     */
    public void setTimeShiftPositionCallback(@android.annotation.Nullable
    android.media.tv.TvView.TimeShiftPositionCallback callback) {
        mTimeShiftPositionCallback = callback;
        ensurePositionTracking();
    }

    private void ensurePositionTracking() {
        if (mSession == null) {
            return;
        }
        mSession.timeShiftEnablePositionTracking(mTimeShiftPositionCallback != null);
    }

    /**
     * Sends a private command to the underlying TV input. This can be used to provide
     * domain-specific features that are only known between certain clients and their TV inputs.
     *
     * @param action
     * 		The name of the private command to send. This <em>must</em> be a scoped name,
     * 		i.e. prefixed with a package name you own, so that different developers will not
     * 		create conflicting commands.
     * @param data
     * 		An optional bundle to send with the command.
     */
    public void sendAppPrivateCommand(@android.annotation.NonNull
    java.lang.String action, android.os.Bundle data) {
        if (android.text.TextUtils.isEmpty(action)) {
            throw new java.lang.IllegalArgumentException("action cannot be null or an empty string");
        }
        if (mSession != null) {
            mSession.sendAppPrivateCommand(action, data);
        } else {
            android.util.Log.w(android.media.tv.TvView.TAG, ("sendAppPrivateCommand - session not yet created (action \"" + action) + "\" pending)");
            mPendingAppPrivateCommands.add(android.util.Pair.create(action, data));
        }
    }

    /**
     * Dispatches an unhandled input event to the next receiver.
     *
     * <p>Except system keys, TvView always consumes input events in the normal flow. This is called
     * asynchronously from where the event is dispatched. It gives the host application a chance to
     * dispatch the unhandled input events.
     *
     * @param event
     * 		The input event.
     * @return {@code true} if the event was handled by the view, {@code false} otherwise.
     */
    public boolean dispatchUnhandledInputEvent(android.view.InputEvent event) {
        if (mOnUnhandledInputEventListener != null) {
            if (mOnUnhandledInputEventListener.onUnhandledInputEvent(event)) {
                return true;
            }
        }
        return onUnhandledInputEvent(event);
    }

    /**
     * Called when an unhandled input event also has not been handled by the user provided
     * callback. This is the last chance to handle the unhandled input event in the TvView.
     *
     * @param event
     * 		The input event.
     * @return If you handled the event, return {@code true}. If you want to allow the event to be
    handled by the next receiver, return {@code false}.
     */
    public boolean onUnhandledInputEvent(android.view.InputEvent event) {
        return false;
    }

    /**
     * Registers a callback to be invoked when an input event is not handled by the bound TV input.
     *
     * @param listener
     * 		The callback to be invoked when the unhandled input event is received.
     */
    public void setOnUnhandledInputEventListener(android.media.tv.TvView.OnUnhandledInputEventListener listener) {
        mOnUnhandledInputEventListener = listener;
    }

    @java.lang.Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        if (super.dispatchKeyEvent(event)) {
            return true;
        }
        if (android.media.tv.TvView.DEBUG)
            android.util.Log.d(android.media.tv.TvView.TAG, ("dispatchKeyEvent(" + event) + ")");

        if (mSession == null) {
            return false;
        }
        android.view.InputEvent copiedEvent = event.copy();
        int ret = mSession.dispatchInputEvent(copiedEvent, copiedEvent, mFinishedInputEventCallback, mHandler);
        return ret != android.media.tv.TvInputManager.Session.DISPATCH_NOT_HANDLED;
    }

    @java.lang.Override
    public boolean dispatchTouchEvent(android.view.MotionEvent event) {
        if (super.dispatchTouchEvent(event)) {
            return true;
        }
        if (android.media.tv.TvView.DEBUG)
            android.util.Log.d(android.media.tv.TvView.TAG, ("dispatchTouchEvent(" + event) + ")");

        if (mSession == null) {
            return false;
        }
        android.view.InputEvent copiedEvent = event.copy();
        int ret = mSession.dispatchInputEvent(copiedEvent, copiedEvent, mFinishedInputEventCallback, mHandler);
        return ret != android.media.tv.TvInputManager.Session.DISPATCH_NOT_HANDLED;
    }

    @java.lang.Override
    public boolean dispatchTrackballEvent(android.view.MotionEvent event) {
        if (super.dispatchTrackballEvent(event)) {
            return true;
        }
        if (android.media.tv.TvView.DEBUG)
            android.util.Log.d(android.media.tv.TvView.TAG, ("dispatchTrackballEvent(" + event) + ")");

        if (mSession == null) {
            return false;
        }
        android.view.InputEvent copiedEvent = event.copy();
        int ret = mSession.dispatchInputEvent(copiedEvent, copiedEvent, mFinishedInputEventCallback, mHandler);
        return ret != android.media.tv.TvInputManager.Session.DISPATCH_NOT_HANDLED;
    }

    @java.lang.Override
    public boolean dispatchGenericMotionEvent(android.view.MotionEvent event) {
        if (super.dispatchGenericMotionEvent(event)) {
            return true;
        }
        if (android.media.tv.TvView.DEBUG)
            android.util.Log.d(android.media.tv.TvView.TAG, ("dispatchGenericMotionEvent(" + event) + ")");

        if (mSession == null) {
            return false;
        }
        android.view.InputEvent copiedEvent = event.copy();
        int ret = mSession.dispatchInputEvent(copiedEvent, copiedEvent, mFinishedInputEventCallback, mHandler);
        return ret != android.media.tv.TvInputManager.Session.DISPATCH_NOT_HANDLED;
    }

    @java.lang.Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        super.dispatchWindowFocusChanged(hasFocus);
        // Other app may have shown its own main TvView.
        // Set main again to regain main session.
        synchronized(android.media.tv.TvView.sMainTvViewLock) {
            if ((hasFocus && (this == android.media.tv.TvView.sMainTvView.get())) && (mSession != null)) {
                mSession.setMain();
            }
        }
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        createSessionOverlayView();
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        removeSessionOverlayView();
        super.onDetachedFromWindow();
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (android.media.tv.TvView.DEBUG) {
            android.util.Log.d(android.media.tv.TvView.TAG, ((((((("onLayout (left=" + left) + ", top=") + top) + ", right=") + right) + ", bottom=") + bottom) + ",)");
        }
        if (mUseRequestedSurfaceLayout) {
            mSurfaceView.layout(mSurfaceViewLeft, mSurfaceViewTop, mSurfaceViewRight, mSurfaceViewBottom);
        } else {
            mSurfaceView.layout(0, 0, right - left, bottom - top);
        }
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mSurfaceView.measure(widthMeasureSpec, heightMeasureSpec);
        int width = mSurfaceView.getMeasuredWidth();
        int height = mSurfaceView.getMeasuredHeight();
        int childState = mSurfaceView.getMeasuredState();
        setMeasuredDimension(android.view.View.resolveSizeAndState(width, widthMeasureSpec, childState), android.view.View.resolveSizeAndState(height, heightMeasureSpec, childState << android.view.View.MEASURED_HEIGHT_STATE_SHIFT));
    }

    @java.lang.Override
    public boolean gatherTransparentRegion(android.graphics.Region region) {
        if (mWindowZOrder != android.media.tv.TvView.ZORDER_ON_TOP) {
            if (region != null) {
                int width = getWidth();
                int height = getHeight();
                if ((width > 0) && (height > 0)) {
                    int[] location = new int[2];
                    getLocationInWindow(location);
                    int left = location[0];
                    int top = location[1];
                    region.op(left, top, left + width, top + height, android.graphics.Region.Op.UNION);
                }
            }
        }
        return super.gatherTransparentRegion(region);
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        if (mWindowZOrder != android.media.tv.TvView.ZORDER_ON_TOP) {
            // Punch a hole so that the underlying overlay view and surface can be shown.
            canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
        }
        super.draw(canvas);
    }

    @java.lang.Override
    protected void dispatchDraw(android.graphics.Canvas canvas) {
        if (mWindowZOrder != android.media.tv.TvView.ZORDER_ON_TOP) {
            // Punch a hole so that the underlying overlay view and surface can be shown.
            canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
        }
        super.dispatchDraw(canvas);
    }

    @java.lang.Override
    protected void onVisibilityChanged(android.view.View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mSurfaceView.setVisibility(visibility);
        if (visibility == android.view.View.VISIBLE) {
            createSessionOverlayView();
        } else {
            removeSessionOverlayView();
        }
    }

    private void resetSurfaceView() {
        if (mSurfaceView != null) {
            mSurfaceView.getHolder().removeCallback(mSurfaceHolderCallback);
            removeView(mSurfaceView);
        }
        mSurface = null;
        mSurfaceView = new android.view.SurfaceView(getContext(), mAttrs, mDefStyleAttr) {
            @java.lang.Override
            protected void updateWindow(boolean force, boolean redrawNeeded) {
                super.updateWindow(force, redrawNeeded);
                relayoutSessionOverlayView();
            }
        };
        // The surface view's content should be treated as secure all the time.
        mSurfaceView.setSecure(true);
        mSurfaceView.getHolder().addCallback(mSurfaceHolderCallback);
        if (mWindowZOrder == android.media.tv.TvView.ZORDER_MEDIA_OVERLAY) {
            mSurfaceView.setZOrderMediaOverlay(true);
        } else
            if (mWindowZOrder == android.media.tv.TvView.ZORDER_ON_TOP) {
                mSurfaceView.setZOrderOnTop(true);
            }

        addView(mSurfaceView);
    }

    private void setSessionSurface(android.view.Surface surface) {
        if (mSession == null) {
            return;
        }
        mSession.setSurface(surface);
    }

    private void dispatchSurfaceChanged(int format, int width, int height) {
        if (mSession == null) {
            return;
        }
        mSession.dispatchSurfaceChanged(format, width, height);
    }

    private void createSessionOverlayView() {
        if ((((mSession == null) || (!isAttachedToWindow())) || mOverlayViewCreated) || (mWindowZOrder != android.media.tv.TvView.ZORDER_MEDIA)) {
            return;
        }
        mOverlayViewFrame = getViewFrameOnScreen();
        mSession.createOverlayView(this, mOverlayViewFrame);
        mOverlayViewCreated = true;
    }

    private void removeSessionOverlayView() {
        if ((mSession == null) || (!mOverlayViewCreated)) {
            return;
        }
        mSession.removeOverlayView();
        mOverlayViewCreated = false;
        mOverlayViewFrame = null;
    }

    private void relayoutSessionOverlayView() {
        if ((((mSession == null) || (!isAttachedToWindow())) || (!mOverlayViewCreated)) || (mWindowZOrder != android.media.tv.TvView.ZORDER_MEDIA)) {
            return;
        }
        android.graphics.Rect viewFrame = getViewFrameOnScreen();
        if (viewFrame.equals(mOverlayViewFrame)) {
            return;
        }
        mSession.relayoutOverlayView(viewFrame);
        mOverlayViewFrame = viewFrame;
    }

    private android.graphics.Rect getViewFrameOnScreen() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        return new android.graphics.Rect(location[0], location[1], location[0] + getWidth(), location[1] + getHeight());
    }

    /**
     * Callback used to receive time shift position changes.
     */
    public static abstract class TimeShiftPositionCallback {
        /**
         * This is called when the start position for time shifting has changed.
         *
         * <p>The start position for time shifting indicates the earliest possible time the user can
         * seek to. Initially this is equivalent to the time when the underlying TV input starts
         * recording. Later it may be adjusted because there is insufficient space or the duration
         * of recording is limited. The application must not allow the user to seek to a position
         * earlier than the start position.
         *
         * <p>For playback of a recorded program initiated by {@link #timeShiftPlay(String, Uri)},
         * the start position is the time when playback starts. It does not change.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         * @param timeMs
         * 		The start position for time shifting, in milliseconds since the epoch.
         */
        public void onTimeShiftStartPositionChanged(java.lang.String inputId, long timeMs) {
        }

        /**
         * This is called when the current position for time shifting has changed.
         *
         * <p>The current position for time shifting is the same as the current position of
         * playback. During playback, the current position changes continuously. When paused, it
         * does not change.
         *
         * <p>Note that {@code timeMs} is wall-clock time.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         * @param timeMs
         * 		The current position for time shifting, in milliseconds since the epoch.
         */
        public void onTimeShiftCurrentPositionChanged(java.lang.String inputId, long timeMs) {
        }
    }

    /**
     * Callback used to receive various status updates on the {@link TvView}.
     */
    public static abstract class TvInputCallback {
        /**
         * This is invoked when an error occurred while establishing a connection to the underlying
         * TV input.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         */
        public void onConnectionFailed(java.lang.String inputId) {
        }

        /**
         * This is invoked when the existing connection to the underlying TV input is lost.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         */
        public void onDisconnected(java.lang.String inputId) {
        }

        /**
         * This is invoked when the channel of this TvView is changed by the underlying TV input
         * without any {@link TvView#tune} request.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         * @param channelUri
         * 		The URI of a channel.
         */
        public void onChannelRetuned(java.lang.String inputId, android.net.Uri channelUri) {
        }

        /**
         * This is called when the track information has been changed.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         * @param tracks
         * 		A list which includes track information.
         */
        public void onTracksChanged(java.lang.String inputId, java.util.List<android.media.tv.TvTrackInfo> tracks) {
        }

        /**
         * This is called when there is a change on the selected tracks.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         * @param type
         * 		The type of the track selected. The type can be
         * 		{@link TvTrackInfo#TYPE_AUDIO}, {@link TvTrackInfo#TYPE_VIDEO} or
         * 		{@link TvTrackInfo#TYPE_SUBTITLE}.
         * @param trackId
         * 		The ID of the track selected.
         */
        public void onTrackSelected(java.lang.String inputId, int type, java.lang.String trackId) {
        }

        /**
         * This is invoked when the video size has been changed. It is also called when the first
         * time video size information becomes available after this view is tuned to a specific
         * channel.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         * @param width
         * 		The width of the video.
         * @param height
         * 		The height of the video.
         */
        public void onVideoSizeChanged(java.lang.String inputId, int width, int height) {
        }

        /**
         * This is called when the video is available, so the TV input starts the playback.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         */
        public void onVideoAvailable(java.lang.String inputId) {
        }

        /**
         * This is called when the video is not available, so the TV input stops the playback.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
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
        public void onVideoUnavailable(java.lang.String inputId, @android.media.tv.TvInputManager.VideoUnavailableReason
        int reason) {
        }

        /**
         * This is called when the current program content turns out to be allowed to watch since
         * its content rating is not blocked by parental controls.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         */
        public void onContentAllowed(java.lang.String inputId) {
        }

        /**
         * This is called when the current program content turns out to be not allowed to watch
         * since its content rating is blocked by parental controls.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         * @param rating
         * 		The content rating of the blocked program.
         */
        public void onContentBlocked(java.lang.String inputId, android.media.tv.TvContentRating rating) {
        }

        /**
         * This is invoked when a custom event from the bound TV input is sent to this view.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         * @param eventType
         * 		The type of the event.
         * @param eventArgs
         * 		Optional arguments of the event.
         * @unknown 
         */
        @android.annotation.SystemApi
        public void onEvent(java.lang.String inputId, java.lang.String eventType, android.os.Bundle eventArgs) {
        }

        /**
         * This is called when the time shift status is changed.
         *
         * @param inputId
         * 		The ID of the TV input bound to this view.
         * @param status
         * 		The current time shift status. Should be one of the followings.
         * 		<ul>
         * 		<li>{@link TvInputManager#TIME_SHIFT_STATUS_UNSUPPORTED}
         * 		<li>{@link TvInputManager#TIME_SHIFT_STATUS_UNAVAILABLE}
         * 		<li>{@link TvInputManager#TIME_SHIFT_STATUS_AVAILABLE}
         * 		</ul>
         */
        public void onTimeShiftStatusChanged(java.lang.String inputId, @android.media.tv.TvInputManager.TimeShiftStatus
        int status) {
        }
    }

    /**
     * Interface definition for a callback to be invoked when the unhandled input event is received.
     */
    public interface OnUnhandledInputEventListener {
        /**
         * Called when an input event was not handled by the bound TV input.
         *
         * <p>This is called asynchronously from where the event is dispatched. It gives the host
         * application a chance to handle the unhandled input events.
         *
         * @param event
         * 		The input event.
         * @return If you handled the event, return {@code true}. If you want to allow the event to
        be handled by the next receiver, return {@code false}.
         */
        boolean onUnhandledInputEvent(android.view.InputEvent event);
    }

    private class MySessionCallback extends android.media.tv.TvInputManager.SessionCallback {
        final java.lang.String mInputId;

        android.net.Uri mChannelUri;

        android.os.Bundle mTuneParams;

        android.net.Uri mRecordedProgramUri;

        MySessionCallback(java.lang.String inputId, android.net.Uri channelUri, android.os.Bundle tuneParams) {
            mInputId = inputId;
            mChannelUri = channelUri;
            mTuneParams = tuneParams;
        }

        MySessionCallback(java.lang.String inputId, android.net.Uri recordedProgramUri) {
            mInputId = inputId;
            mRecordedProgramUri = recordedProgramUri;
        }

        @java.lang.Override
        public void onSessionCreated(android.media.tv.TvInputManager.Session session) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, "onSessionCreated()");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onSessionCreated - session already created");
                // This callback is obsolete.
                if (session != null) {
                    session.release();
                }
                return;
            }
            mSession = session;
            if (session != null) {
                // Sends the pending app private commands first.
                for (android.util.Pair<java.lang.String, android.os.Bundle> command : mPendingAppPrivateCommands) {
                    mSession.sendAppPrivateCommand(command.first, command.second);
                }
                mPendingAppPrivateCommands.clear();
                synchronized(android.media.tv.TvView.sMainTvViewLock) {
                    if (hasWindowFocus() && (android.media.tv.TvView.this == android.media.tv.TvView.sMainTvView.get())) {
                        mSession.setMain();
                    }
                }
                // mSurface may not be ready yet as soon as starting an application.
                // In the case, we don't send Session.setSurface(null) unnecessarily.
                // setSessionSurface will be called in surfaceCreated.
                if (mSurface != null) {
                    setSessionSurface(mSurface);
                    if (mSurfaceChanged) {
                        dispatchSurfaceChanged(mSurfaceFormat, mSurfaceWidth, mSurfaceHeight);
                    }
                }
                createSessionOverlayView();
                if (mStreamVolume != null) {
                    mSession.setStreamVolume(mStreamVolume);
                }
                if (mCaptionEnabled != null) {
                    mSession.setCaptionEnabled(mCaptionEnabled);
                }
                if (mChannelUri != null) {
                    mSession.tune(mChannelUri, mTuneParams);
                } else {
                    mSession.timeShiftPlay(mRecordedProgramUri);
                }
                ensurePositionTracking();
            } else {
                mSessionCallback = null;
                if (mCallback != null) {
                    mCallback.onConnectionFailed(mInputId);
                }
            }
        }

        @java.lang.Override
        public void onSessionReleased(android.media.tv.TvInputManager.Session session) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, "onSessionReleased()");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onSessionReleased - session not created");
                return;
            }
            mOverlayViewCreated = false;
            mOverlayViewFrame = null;
            mSessionCallback = null;
            mSession = null;
            if (mCallback != null) {
                mCallback.onDisconnected(mInputId);
            }
        }

        @java.lang.Override
        public void onChannelRetuned(android.media.tv.TvInputManager.Session session, android.net.Uri channelUri) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, ("onChannelChangedByTvInput(" + channelUri) + ")");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onChannelRetuned - session not created");
                return;
            }
            if (mCallback != null) {
                mCallback.onChannelRetuned(mInputId, channelUri);
            }
        }

        @java.lang.Override
        public void onTracksChanged(android.media.tv.TvInputManager.Session session, java.util.List<android.media.tv.TvTrackInfo> tracks) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, ("onTracksChanged(" + tracks) + ")");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onTracksChanged - session not created");
                return;
            }
            if (mCallback != null) {
                mCallback.onTracksChanged(mInputId, tracks);
            }
        }

        @java.lang.Override
        public void onTrackSelected(android.media.tv.TvInputManager.Session session, int type, java.lang.String trackId) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, ((("onTrackSelected(type=" + type) + ", trackId=") + trackId) + ")");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onTrackSelected - session not created");
                return;
            }
            if (mCallback != null) {
                mCallback.onTrackSelected(mInputId, type, trackId);
            }
        }

        @java.lang.Override
        public void onVideoSizeChanged(android.media.tv.TvInputManager.Session session, int width, int height) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, "onVideoSizeChanged()");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onVideoSizeChanged - session not created");
                return;
            }
            if (mCallback != null) {
                mCallback.onVideoSizeChanged(mInputId, width, height);
            }
        }

        @java.lang.Override
        public void onVideoAvailable(android.media.tv.TvInputManager.Session session) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, "onVideoAvailable()");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onVideoAvailable - session not created");
                return;
            }
            if (mCallback != null) {
                mCallback.onVideoAvailable(mInputId);
            }
        }

        @java.lang.Override
        public void onVideoUnavailable(android.media.tv.TvInputManager.Session session, int reason) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, ("onVideoUnavailable(reason=" + reason) + ")");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onVideoUnavailable - session not created");
                return;
            }
            if (mCallback != null) {
                mCallback.onVideoUnavailable(mInputId, reason);
            }
        }

        @java.lang.Override
        public void onContentAllowed(android.media.tv.TvInputManager.Session session) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, "onContentAllowed()");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onContentAllowed - session not created");
                return;
            }
            if (mCallback != null) {
                mCallback.onContentAllowed(mInputId);
            }
        }

        @java.lang.Override
        public void onContentBlocked(android.media.tv.TvInputManager.Session session, android.media.tv.TvContentRating rating) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, ("onContentBlocked(rating=" + rating) + ")");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onContentBlocked - session not created");
                return;
            }
            if (mCallback != null) {
                mCallback.onContentBlocked(mInputId, rating);
            }
        }

        @java.lang.Override
        public void onLayoutSurface(android.media.tv.TvInputManager.Session session, int left, int top, int right, int bottom) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, ((((((("onLayoutSurface (left=" + left) + ", top=") + top) + ", right=") + right) + ", bottom=") + bottom) + ",)");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onLayoutSurface - session not created");
                return;
            }
            mSurfaceViewLeft = left;
            mSurfaceViewTop = top;
            mSurfaceViewRight = right;
            mSurfaceViewBottom = bottom;
            mUseRequestedSurfaceLayout = true;
            requestLayout();
        }

        @java.lang.Override
        public void onSessionEvent(android.media.tv.TvInputManager.Session session, java.lang.String eventType, android.os.Bundle eventArgs) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, ("onSessionEvent(" + eventType) + ")");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onSessionEvent - session not created");
                return;
            }
            if (mCallback != null) {
                mCallback.onEvent(mInputId, eventType, eventArgs);
            }
        }

        @java.lang.Override
        public void onTimeShiftStatusChanged(android.media.tv.TvInputManager.Session session, int status) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, "onTimeShiftStatusChanged()");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onTimeShiftStatusChanged - session not created");
                return;
            }
            if (mCallback != null) {
                mCallback.onTimeShiftStatusChanged(mInputId, status);
            }
        }

        @java.lang.Override
        public void onTimeShiftStartPositionChanged(android.media.tv.TvInputManager.Session session, long timeMs) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, "onTimeShiftStartPositionChanged()");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onTimeShiftStartPositionChanged - session not created");
                return;
            }
            if (mTimeShiftPositionCallback != null) {
                mTimeShiftPositionCallback.onTimeShiftStartPositionChanged(mInputId, timeMs);
            }
        }

        @java.lang.Override
        public void onTimeShiftCurrentPositionChanged(android.media.tv.TvInputManager.Session session, long timeMs) {
            if (android.media.tv.TvView.DEBUG) {
                android.util.Log.d(android.media.tv.TvView.TAG, "onTimeShiftCurrentPositionChanged()");
            }
            if (this != mSessionCallback) {
                android.util.Log.w(android.media.tv.TvView.TAG, "onTimeShiftCurrentPositionChanged - session not created");
                return;
            }
            if (mTimeShiftPositionCallback != null) {
                mTimeShiftPositionCallback.onTimeShiftCurrentPositionChanged(mInputId, timeMs);
            }
        }
    }
}

