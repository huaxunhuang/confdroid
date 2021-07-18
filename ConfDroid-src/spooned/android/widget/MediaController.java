/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.widget;


/**
 * A view containing controls for a MediaPlayer. Typically contains the
 * buttons like "Play/Pause", "Rewind", "Fast Forward" and a progress
 * slider. It takes care of synchronizing the controls with the state
 * of the MediaPlayer.
 * <p>
 * The way to use this class is to instantiate it programmatically.
 * The MediaController will create a default set of controls
 * and put them in a window floating above your application. Specifically,
 * the controls will float above the view specified with setAnchorView().
 * The window will disappear if left idle for three seconds and reappear
 * when the user touches the anchor view.
 * <p>
 * Functions like show() and hide() have no effect when MediaController
 * is created in an xml layout.
 *
 * MediaController will hide and
 * show the buttons according to these rules:
 * <ul>
 * <li> The "previous" and "next" buttons are hidden until setPrevNextListeners()
 *   has been called
 * <li> The "previous" and "next" buttons are visible but disabled if
 *   setPrevNextListeners() was called with null listeners
 * <li> The "rewind" and "fastforward" buttons are shown unless requested
 *   otherwise by using the MediaController(Context, boolean) constructor
 *   with the boolean set to false
 * </ul>
 */
public class MediaController extends android.widget.FrameLayout {
    @android.annotation.UnsupportedAppUsage
    private android.widget.MediaController.MediaPlayerControl mPlayer;

    @android.annotation.UnsupportedAppUsage
    private final android.content.Context mContext;

    @android.annotation.UnsupportedAppUsage
    private android.view.View mAnchor;

    @android.annotation.UnsupportedAppUsage
    private android.view.View mRoot;

    @android.annotation.UnsupportedAppUsage
    private android.view.WindowManager mWindowManager;

    @android.annotation.UnsupportedAppUsage
    private android.view.Window mWindow;

    @android.annotation.UnsupportedAppUsage
    private android.view.View mDecor;

    @android.annotation.UnsupportedAppUsage
    private android.view.WindowManager.LayoutParams mDecorLayoutParams;

    @android.annotation.UnsupportedAppUsage
    private android.widget.ProgressBar mProgress;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private android.widget.TextView mEndTime;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private android.widget.TextView mCurrentTime;

    @android.annotation.UnsupportedAppUsage
    private boolean mShowing;

    private boolean mDragging;

    private static final int sDefaultTimeout = 3000;

    private final boolean mUseFastForward;

    private boolean mFromXml;

    private boolean mListenersSet;

    private android.view.View.OnClickListener mNextListener;

    private android.view.View.OnClickListener mPrevListener;

    java.lang.StringBuilder mFormatBuilder;

    java.util.Formatter mFormatter;

    @android.annotation.UnsupportedAppUsage
    private android.widget.ImageButton mPauseButton;

    @android.annotation.UnsupportedAppUsage
    private android.widget.ImageButton mFfwdButton;

    @android.annotation.UnsupportedAppUsage
    private android.widget.ImageButton mRewButton;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private android.widget.ImageButton mNextButton;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private android.widget.ImageButton mPrevButton;

    private java.lang.CharSequence mPlayDescription;

    private java.lang.CharSequence mPauseDescription;

    private final android.view.accessibility.AccessibilityManager mAccessibilityManager;

    public MediaController(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        mRoot = this;
        mContext = context;
        mUseFastForward = true;
        mFromXml = true;
        mAccessibilityManager = android.view.accessibility.AccessibilityManager.getInstance(context);
    }

    @java.lang.Override
    public void onFinishInflate() {
        if (mRoot != null)
            initControllerView(mRoot);

    }

    public MediaController(android.content.Context context, boolean useFastForward) {
        super(context);
        mContext = context;
        mUseFastForward = useFastForward;
        initFloatingWindowLayout();
        initFloatingWindow();
        mAccessibilityManager = android.view.accessibility.AccessibilityManager.getInstance(context);
    }

    public MediaController(android.content.Context context) {
        this(context, true);
    }

    private void initFloatingWindow() {
        mWindowManager = ((android.view.WindowManager) (mContext.getSystemService(android.content.Context.WINDOW_SERVICE)));
        mWindow = new com.android.internal.policy.PhoneWindow(mContext);
        mWindow.setWindowManager(mWindowManager, null, null);
        mWindow.requestFeature(android.view.Window.FEATURE_NO_TITLE);
        mDecor = mWindow.getDecorView();
        mDecor.setOnTouchListener(mTouchListener);
        mWindow.setContentView(this);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);
        // While the media controller is up, the volume control keys should
        // affect the media stream type
        mWindow.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS);
        requestFocus();
    }

    // Allocate and initialize the static parts of mDecorLayoutParams. Must
    // also call updateFloatingWindowLayout() to fill in the dynamic parts
    // (y and width) before mDecorLayoutParams can be used.
    private void initFloatingWindowLayout() {
        mDecorLayoutParams = new android.view.WindowManager.LayoutParams();
        android.view.WindowManager.LayoutParams p = mDecorLayoutParams;
        p.gravity = android.view.Gravity.TOP | android.view.Gravity.LEFT;
        p.height = android.widget.FrameLayout.LayoutParams.WRAP_CONTENT;
        p.x = 0;
        p.format = android.graphics.PixelFormat.TRANSLUCENT;
        p.type = android.view.WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        p.flags |= (android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL) | android.view.WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
        p.token = null;
        p.windowAnimations = 0;// android.R.style.DropDownAnimationDown;

    }

    // Update the dynamic parts of mDecorLayoutParams
    // Must be called with mAnchor != NULL.
    private void updateFloatingWindowLayout() {
        int[] anchorPos = new int[2];
        mAnchor.getLocationOnScreen(anchorPos);
        // we need to know the size of the controller so we can properly position it
        // within its space
        mDecor.measure(android.view.View.MeasureSpec.makeMeasureSpec(mAnchor.getWidth(), android.view.View.MeasureSpec.AT_MOST), android.view.View.MeasureSpec.makeMeasureSpec(mAnchor.getHeight(), android.view.View.MeasureSpec.AT_MOST));
        android.view.WindowManager.LayoutParams p = mDecorLayoutParams;
        p.width = mAnchor.getWidth();
        p.x = anchorPos[0] + ((mAnchor.getWidth() - p.width) / 2);
        p.y = (anchorPos[1] + mAnchor.getHeight()) - mDecor.getMeasuredHeight();
    }

    // This is called whenever mAnchor's layout bound changes
    private final android.view.View.OnLayoutChangeListener mLayoutChangeListener = new android.view.View.OnLayoutChangeListener() {
        @java.lang.Override
        public void onLayoutChange(android.view.View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            updateFloatingWindowLayout();
            if (mShowing) {
                mWindowManager.updateViewLayout(mDecor, mDecorLayoutParams);
            }
        }
    };

    private final android.view.View.OnTouchListener mTouchListener = new android.view.View.OnTouchListener() {
        @java.lang.Override
        public boolean onTouch(android.view.View v, android.view.MotionEvent event) {
            if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                if (mShowing) {
                    hide();
                }
            }
            return false;
        }
    };

    public void setMediaPlayer(android.widget.MediaController.MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    /**
     * Set the view that acts as the anchor for the control view.
     * This can for example be a VideoView, or your Activity's main view.
     * When VideoView calls this method, it will use the VideoView's parent
     * as the anchor.
     *
     * @param view
     * 		The view to which to anchor the controller when it is visible.
     */
    public void setAnchorView(android.view.View view) {
        if (mAnchor != null) {
            mAnchor.removeOnLayoutChangeListener(mLayoutChangeListener);
        }
        mAnchor = view;
        if (mAnchor != null) {
            mAnchor.addOnLayoutChangeListener(mLayoutChangeListener);
        }
        android.widget.FrameLayout.LayoutParams frameParams = new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        removeAllViews();
        android.view.View v = makeControllerView();
        addView(v, frameParams);
    }

    /**
     * Create the view that holds the widgets that control playback.
     * Derived classes can override this to create their own.
     *
     * @return The controller view.
     * @unknown This doesn't work as advertised
     */
    protected android.view.View makeControllerView() {
        android.view.LayoutInflater inflate = ((android.view.LayoutInflater) (mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        mRoot = inflate.inflate(com.android.internal.R.layout.media_controller, null);
        initControllerView(mRoot);
        return mRoot;
    }

    private void initControllerView(android.view.View v) {
        android.content.res.Resources res = mContext.getResources();
        mPlayDescription = res.getText(com.android.internal.R.string.lockscreen_transport_play_description);
        mPauseDescription = res.getText(com.android.internal.R.string.lockscreen_transport_pause_description);
        mPauseButton = v.findViewById(com.android.internal.R.id.pause);
        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }
        mFfwdButton = v.findViewById(com.android.internal.R.id.ffwd);
        if (mFfwdButton != null) {
            mFfwdButton.setOnClickListener(mFfwdListener);
            if (!mFromXml) {
                mFfwdButton.setVisibility(mUseFastForward ? android.view.View.VISIBLE : android.view.View.GONE);
            }
        }
        mRewButton = v.findViewById(com.android.internal.R.id.rew);
        if (mRewButton != null) {
            mRewButton.setOnClickListener(mRewListener);
            if (!mFromXml) {
                mRewButton.setVisibility(mUseFastForward ? android.view.View.VISIBLE : android.view.View.GONE);
            }
        }
        // By default these are hidden. They will be enabled when setPrevNextListeners() is called
        mNextButton = v.findViewById(com.android.internal.R.id.next);
        if (((mNextButton != null) && (!mFromXml)) && (!mListenersSet)) {
            mNextButton.setVisibility(android.view.View.GONE);
        }
        mPrevButton = v.findViewById(com.android.internal.R.id.prev);
        if (((mPrevButton != null) && (!mFromXml)) && (!mListenersSet)) {
            mPrevButton.setVisibility(android.view.View.GONE);
        }
        mProgress = v.findViewById(com.android.internal.R.id.mediacontroller_progress);
        if (mProgress != null) {
            if (mProgress instanceof android.widget.SeekBar) {
                android.widget.SeekBar seeker = ((android.widget.SeekBar) (mProgress));
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }
        mEndTime = v.findViewById(com.android.internal.R.id.time);
        mCurrentTime = v.findViewById(com.android.internal.R.id.time_current);
        mFormatBuilder = new java.lang.StringBuilder();
        mFormatter = new java.util.Formatter(mFormatBuilder, java.util.Locale.getDefault());
        installPrevNextListeners();
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 3 seconds of inactivity.
     */
    public void show() {
        show(android.widget.MediaController.sDefaultTimeout);
    }

    /**
     * Disable pause or seek buttons if the stream cannot be paused or seeked.
     * This requires the control interface to be a MediaPlayerControlExt
     */
    private void disableUnsupportedButtons() {
        try {
            if ((mPauseButton != null) && (!mPlayer.canPause())) {
                mPauseButton.setEnabled(false);
            }
            if ((mRewButton != null) && (!mPlayer.canSeekBackward())) {
                mRewButton.setEnabled(false);
            }
            if ((mFfwdButton != null) && (!mPlayer.canSeekForward())) {
                mFfwdButton.setEnabled(false);
            }
            // TODO What we really should do is add a canSeek to the MediaPlayerControl interface;
            // this scheme can break the case when applications want to allow seek through the
            // progress bar but disable forward/backward buttons.
            // 
            // However, currently the flags SEEK_BACKWARD_AVAILABLE, SEEK_FORWARD_AVAILABLE,
            // and SEEK_AVAILABLE are all (un)set together; as such the aforementioned issue
            // shouldn't arise in existing applications.
            if (((mProgress != null) && (!mPlayer.canSeekBackward())) && (!mPlayer.canSeekForward())) {
                mProgress.setEnabled(false);
            }
        } catch (java.lang.IncompatibleClassChangeError ex) {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't disable
            // the buttons.
        }
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     *
     * @param timeout
     * 		The timeout in milliseconds. Use 0 to show
     * 		the controller until hide() is called.
     */
    public void show(int timeout) {
        if ((!mShowing) && (mAnchor != null)) {
            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            disableUnsupportedButtons();
            updateFloatingWindowLayout();
            mWindowManager.addView(mDecor, mDecorLayoutParams);
            mShowing = true;
        }
        updatePausePlay();
        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        post(mShowProgress);
        if ((timeout != 0) && (!mAccessibilityManager.isTouchExplorationEnabled())) {
            removeCallbacks(mFadeOut);
            postDelayed(mFadeOut, timeout);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (mAnchor == null)
            return;

        if (mShowing) {
            try {
                removeCallbacks(mShowProgress);
                mWindowManager.removeView(mDecor);
            } catch (java.lang.IllegalArgumentException ex) {
                android.util.Log.w("MediaController", "already removed");
            }
            mShowing = false;
        }
    }

    private final java.lang.Runnable mFadeOut = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            hide();
        }
    };

    private final java.lang.Runnable mShowProgress = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            int pos = setProgress();
            if (((!mDragging) && mShowing) && mPlayer.isPlaying()) {
                postDelayed(mShowProgress, 1000 - (pos % 1000));
            }
        }
    };

    private java.lang.String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if ((mPlayer == null) || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = (1000L * position) / duration;
                mProgress.setProgress(((int) (pos)));
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }
        if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));

        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        switch (event.getAction()) {
            case android.view.MotionEvent.ACTION_DOWN :
                show(0);// show until hide is called

                break;
            case android.view.MotionEvent.ACTION_UP :
                show(android.widget.MediaController.sDefaultTimeout);// start timeout

                break;
            case android.view.MotionEvent.ACTION_CANCEL :
                hide();
                break;
            default :
                break;
        }
        return true;
    }

    @java.lang.Override
    public boolean onTrackballEvent(android.view.MotionEvent ev) {
        show(android.widget.MediaController.sDefaultTimeout);
        return false;
    }

    @java.lang.Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = (event.getRepeatCount() == 0) && (event.getAction() == android.view.KeyEvent.ACTION_DOWN);
        if (((keyCode == android.view.KeyEvent.KEYCODE_HEADSETHOOK) || (keyCode == android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)) || (keyCode == android.view.KeyEvent.KEYCODE_SPACE)) {
            if (uniqueDown) {
                doPauseResume();
                show(android.widget.MediaController.sDefaultTimeout);
                if (mPauseButton != null) {
                    mPauseButton.requestFocus();
                }
            }
            return true;
        } else
            if (keyCode == android.view.KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (uniqueDown && (!mPlayer.isPlaying())) {
                    mPlayer.start();
                    updatePausePlay();
                    show(android.widget.MediaController.sDefaultTimeout);
                }
                return true;
            } else
                if ((keyCode == android.view.KeyEvent.KEYCODE_MEDIA_STOP) || (keyCode == android.view.KeyEvent.KEYCODE_MEDIA_PAUSE)) {
                    if (uniqueDown && mPlayer.isPlaying()) {
                        mPlayer.pause();
                        updatePausePlay();
                        show(android.widget.MediaController.sDefaultTimeout);
                    }
                    return true;
                } else
                    if ((((keyCode == android.view.KeyEvent.KEYCODE_VOLUME_DOWN) || (keyCode == android.view.KeyEvent.KEYCODE_VOLUME_UP)) || (keyCode == android.view.KeyEvent.KEYCODE_VOLUME_MUTE)) || (keyCode == android.view.KeyEvent.KEYCODE_CAMERA)) {
                        // don't show the controls for volume adjustment
                        return super.dispatchKeyEvent(event);
                    } else
                        if ((keyCode == android.view.KeyEvent.KEYCODE_BACK) || (keyCode == android.view.KeyEvent.KEYCODE_MENU)) {
                            if (uniqueDown) {
                                hide();
                            }
                            return true;
                        }




        show(android.widget.MediaController.sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }

    private final android.view.View.OnClickListener mPauseListener = new android.view.View.OnClickListener() {
        @java.lang.Override
        public void onClick(android.view.View v) {
            doPauseResume();
            show(android.widget.MediaController.sDefaultTimeout);
        }
    };

    @android.annotation.UnsupportedAppUsage
    private void updatePausePlay() {
        if ((mRoot == null) || (mPauseButton == null))
            return;

        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(com.android.internal.R.drawable.ic_media_pause);
            mPauseButton.setContentDescription(mPauseDescription);
        } else {
            mPauseButton.setImageResource(com.android.internal.R.drawable.ic_media_play);
            mPauseButton.setContentDescription(mPlayDescription);
        }
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    // There are two scenarios that can trigger the seekbar listener to trigger:
    // 
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the dragging
    // session to avoid jumps in the position in case of ongoing playback.
    // 
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
    // we will simply apply the updated position without suspending regular updates.
    @android.annotation.UnsupportedAppUsage
    private final android.widget.SeekBar.OnSeekBarChangeListener mSeekListener = new android.widget.SeekBar.OnSeekBarChangeListener() {
        @java.lang.Override
        public void onStartTrackingTouch(android.widget.SeekBar bar) {
            show(3600000);
            mDragging = true;
            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            removeCallbacks(mShowProgress);
        }

        @java.lang.Override
        public void onProgressChanged(android.widget.SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }
            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo(((int) (newposition)));
            if (mCurrentTime != null)
                mCurrentTime.setText(stringForTime(((int) (newposition))));

        }

        @java.lang.Override
        public void onStopTrackingTouch(android.widget.SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(android.widget.MediaController.sDefaultTimeout);
            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            post(mShowProgress);
        }
    };

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mFfwdButton != null) {
            mFfwdButton.setEnabled(enabled);
        }
        if (mRewButton != null) {
            mRewButton.setEnabled(enabled);
        }
        if (mNextButton != null) {
            mNextButton.setEnabled(enabled && (mNextListener != null));
        }
        if (mPrevButton != null) {
            mPrevButton.setEnabled(enabled && (mPrevListener != null));
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.MediaController.class.getName();
    }

    @android.annotation.UnsupportedAppUsage
    private final android.view.View.OnClickListener mRewListener = new android.view.View.OnClickListener() {
        @java.lang.Override
        public void onClick(android.view.View v) {
            int pos = mPlayer.getCurrentPosition();
            pos -= 5000;// milliseconds

            mPlayer.seekTo(pos);
            setProgress();
            show(android.widget.MediaController.sDefaultTimeout);
        }
    };

    @android.annotation.UnsupportedAppUsage
    private final android.view.View.OnClickListener mFfwdListener = new android.view.View.OnClickListener() {
        @java.lang.Override
        public void onClick(android.view.View v) {
            int pos = mPlayer.getCurrentPosition();
            pos += 15000;// milliseconds

            mPlayer.seekTo(pos);
            setProgress();
            show(android.widget.MediaController.sDefaultTimeout);
        }
    };

    private void installPrevNextListeners() {
        if (mNextButton != null) {
            mNextButton.setOnClickListener(mNextListener);
            mNextButton.setEnabled(mNextListener != null);
        }
        if (mPrevButton != null) {
            mPrevButton.setOnClickListener(mPrevListener);
            mPrevButton.setEnabled(mPrevListener != null);
        }
    }

    public void setPrevNextListeners(android.view.View.OnClickListener next, android.view.View.OnClickListener prev) {
        mNextListener = next;
        mPrevListener = prev;
        mListenersSet = true;
        if (mRoot != null) {
            installPrevNextListeners();
            if ((mNextButton != null) && (!mFromXml)) {
                mNextButton.setVisibility(android.view.View.VISIBLE);
            }
            if ((mPrevButton != null) && (!mFromXml)) {
                mPrevButton.setVisibility(android.view.View.VISIBLE);
            }
        }
    }

    public interface MediaPlayerControl {
        void start();

        void pause();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int pos);

        boolean isPlaying();

        int getBufferPercentage();

        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();

        /**
         * Get the audio session id for the player used by this VideoView. This can be used to
         * apply audio effects to the audio track of a video.
         *
         * @return The audio session, or 0 if there was an error.
         */
        int getAudioSessionId();
    }
}

