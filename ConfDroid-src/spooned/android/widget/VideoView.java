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
 * Displays a video file.  The VideoView class
 * can load images from various sources (such as resources or content
 * providers), takes care of computing its measurement from the video so that
 * it can be used in any layout manager, and provides various display options
 * such as scaling and tinting.<p>
 *
 * <em>Note: VideoView does not retain its full state when going into the
 * background.</em>  In particular, it does not restore the current play state,
 * play position, selected tracks, or any subtitle tracks added via
 * {@link #addSubtitleSource addSubtitleSource()}.  Applications should
 * save and restore these on their own in
 * {@link android.app.Activity#onSaveInstanceState} and
 * {@link android.app.Activity#onRestoreInstanceState}.<p>
 * Also note that the audio session id (from {@link #getAudioSessionId}) may
 * change from its previously returned value when the VideoView is restored.
 * <p>
 * By default, VideoView requests audio focus with {@link AudioManager#AUDIOFOCUS_GAIN}. Use
 * {@link #setAudioFocusRequest(int)} to change this behavior.
 * <p>
 * The default {@link AudioAttributes} used during playback have a usage of
 * {@link AudioAttributes#USAGE_MEDIA} and a content type of
 * {@link AudioAttributes#CONTENT_TYPE_MOVIE}, use {@link #setAudioAttributes(AudioAttributes)} to
 * modify them.
 */
public class VideoView extends android.view.SurfaceView implements android.media.SubtitleController.Anchor , android.widget.MediaController.MediaPlayerControl {
    private static final java.lang.String TAG = "VideoView";

    // all possible internal states
    private static final int STATE_ERROR = -1;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private static final int STATE_IDLE = 0;

    private static final int STATE_PREPARING = 1;

    private static final int STATE_PREPARED = 2;

    private static final int STATE_PLAYING = 3;

    private static final int STATE_PAUSED = 4;

    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private final java.util.Vector<android.util.Pair<java.io.InputStream, android.media.MediaFormat>> mPendingSubtitleTracks = new java.util.Vector<>();

    // settable by the client
    @android.annotation.UnsupportedAppUsage
    private android.net.Uri mUri;

    @android.annotation.UnsupportedAppUsage
    private java.util.Map<java.lang.String, java.lang.String> mHeaders;

    // mCurrentState is a VideoView object's current state.
    // mTargetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.
    @android.annotation.UnsupportedAppUsage
    private int mCurrentState = android.widget.VideoView.STATE_IDLE;

    @android.annotation.UnsupportedAppUsage
    private int mTargetState = android.widget.VideoView.STATE_IDLE;

    // All the stuff we need for playing and showing a video
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private android.view.SurfaceHolder mSurfaceHolder = null;

    @android.annotation.UnsupportedAppUsage
    private android.media.MediaPlayer mMediaPlayer = null;

    private int mAudioSession;

    @android.annotation.UnsupportedAppUsage
    private int mVideoWidth;

    @android.annotation.UnsupportedAppUsage
    private int mVideoHeight;

    private int mSurfaceWidth;

    private int mSurfaceHeight;

    @android.annotation.UnsupportedAppUsage
    private android.widget.MediaController mMediaController;

    private android.media.MediaPlayer.OnCompletionListener mOnCompletionListener;

    private MediaPlayer.OnPreparedListener mOnPreparedListener;

    @android.annotation.UnsupportedAppUsage
    private int mCurrentBufferPercentage;

    private android.media.MediaPlayer.OnErrorListener mOnErrorListener;

    private android.media.MediaPlayer.OnInfoListener mOnInfoListener;

    private int mSeekWhenPrepared;// recording the seek position while preparing


    private boolean mCanPause;

    private boolean mCanSeekBack;

    private boolean mCanSeekForward;

    private android.media.AudioManager mAudioManager;

    private int mAudioFocusType = android.media.AudioManager.AUDIOFOCUS_GAIN;// legacy focus gain


    private android.media.AudioAttributes mAudioAttributes;

    /**
     * Subtitle rendering widget overlaid on top of the video.
     */
    private android.media.SubtitleTrack.RenderingWidget mSubtitleWidget;

    /**
     * Listener for changes to subtitle data, used to redraw when needed.
     */
    private RenderingWidget.OnChangedListener mSubtitlesChangedListener;

    public VideoView(android.content.Context context) {
        this(context, null);
    }

    public VideoView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VideoView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mVideoWidth = 0;
        mVideoHeight = 0;
        mAudioManager = ((android.media.AudioManager) (mContext.getSystemService(android.content.Context.AUDIO_SERVICE)));
        mAudioAttributes = new android.media.AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MOVIE).build();
        getHolder().addCallback(mSHCallback);
        getHolder().setType(android.view.SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        mCurrentState = android.widget.VideoView.STATE_IDLE;
        mTargetState = android.widget.VideoView.STATE_IDLE;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Log.i("@@@@", "onMeasure(" + MeasureSpec.toString(widthMeasureSpec) + ", "
        // + MeasureSpec.toString(heightMeasureSpec) + ")");
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        if ((mVideoWidth > 0) && (mVideoHeight > 0)) {
            int widthSpecMode = android.widget.MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = android.widget.MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = android.widget.MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = android.widget.MeasureSpec.getSize(heightMeasureSpec);
            if ((widthSpecMode == MeasureSpec.EXACTLY) && (heightSpecMode == MeasureSpec.EXACTLY)) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;
                // for compatibility, we adjust size based on aspect ratio
                if ((mVideoWidth * height) < (width * mVideoHeight)) {
                    // Log.i("@@@", "image too wide, correcting");
                    width = (height * mVideoWidth) / mVideoHeight;
                } else
                    if ((mVideoWidth * height) > (width * mVideoHeight)) {
                        // Log.i("@@@", "image too tall, correcting");
                        height = (width * mVideoHeight) / mVideoWidth;
                    }

            } else
                if (widthSpecMode == MeasureSpec.EXACTLY) {
                    // only the width is fixed, adjust the height to match aspect ratio if possible
                    width = widthSpecSize;
                    height = (width * mVideoHeight) / mVideoWidth;
                    if ((heightSpecMode == MeasureSpec.AT_MOST) && (height > heightSpecSize)) {
                        // couldn't match aspect ratio within the constraints
                        height = heightSpecSize;
                    }
                } else
                    if (heightSpecMode == MeasureSpec.EXACTLY) {
                        // only the height is fixed, adjust the width to match aspect ratio if possible
                        height = heightSpecSize;
                        width = (height * mVideoWidth) / mVideoHeight;
                        if ((widthSpecMode == MeasureSpec.AT_MOST) && (width > widthSpecSize)) {
                            // couldn't match aspect ratio within the constraints
                            width = widthSpecSize;
                        }
                    } else {
                        // neither the width nor the height are fixed, try to use actual video size
                        width = mVideoWidth;
                        height = mVideoHeight;
                        if ((heightSpecMode == MeasureSpec.AT_MOST) && (height > heightSpecSize)) {
                            // too tall, decrease both width and height
                            height = heightSpecSize;
                            width = (height * mVideoWidth) / mVideoHeight;
                        }
                        if ((widthSpecMode == MeasureSpec.AT_MOST) && (width > widthSpecSize)) {
                            // too wide, decrease both width and height
                            width = widthSpecSize;
                            height = (width * mVideoHeight) / mVideoWidth;
                        }
                    }


        } else {
            // no size yet, just adopt the given spec sizes
        }
        setMeasuredDimension(width, height);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.VideoView.class.getName();
    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec) {
        return getDefaultSize(desiredSize, measureSpec);
    }

    /**
     * Sets video path.
     *
     * @param path
     * 		the path of the video.
     */
    public void setVideoPath(java.lang.String path) {
        setVideoURI(android.net.Uri.parse(path));
    }

    /**
     * Sets video URI.
     *
     * @param uri
     * 		the URI of the video.
     */
    public void setVideoURI(android.net.Uri uri) {
        setVideoURI(uri, null);
    }

    /**
     * Sets video URI using specific headers.
     *
     * @param uri
     * 		the URI of the video.
     * @param headers
     * 		the headers for the URI request.
     * 		Note that the cross domain redirection is allowed by default, but that can be
     * 		changed with key/value pairs through the headers parameter with
     * 		"android-allow-cross-domain-redirect" as the key and "0" or "1" as the value
     * 		to disallow or allow cross domain redirection.
     */
    public void setVideoURI(android.net.Uri uri, java.util.Map<java.lang.String, java.lang.String> headers) {
        mUri = uri;
        mHeaders = headers;
        mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    /**
     * Sets which type of audio focus will be requested during the playback, or configures playback
     * to not request audio focus. Valid values for focus requests are
     * {@link AudioManager#AUDIOFOCUS_GAIN}, {@link AudioManager#AUDIOFOCUS_GAIN_TRANSIENT},
     * {@link AudioManager#AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK}, and
     * {@link AudioManager#AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE}. Or use
     * {@link AudioManager#AUDIOFOCUS_NONE} to express that audio focus should not be
     * requested when playback starts. You can for instance use this when playing a silent animation
     * through this class, and you don't want to affect other audio applications playing in the
     * background.
     *
     * @param focusGain
     * 		the type of audio focus gain that will be requested, or
     * 		{@link AudioManager#AUDIOFOCUS_NONE} to disable the use audio focus during playback.
     */
    public void setAudioFocusRequest(int focusGain) {
        if (((((focusGain != android.media.AudioManager.AUDIOFOCUS_NONE) && (focusGain != android.media.AudioManager.AUDIOFOCUS_GAIN)) && (focusGain != android.media.AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)) && (focusGain != android.media.AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)) && (focusGain != android.media.AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE)) {
            throw new java.lang.IllegalArgumentException("Illegal audio focus type " + focusGain);
        }
        mAudioFocusType = focusGain;
    }

    /**
     * Sets the {@link AudioAttributes} to be used during the playback of the video.
     *
     * @param attributes
     * 		non-null <code>AudioAttributes</code>.
     */
    public void setAudioAttributes(@android.annotation.NonNull
    android.media.AudioAttributes attributes) {
        if (attributes == null) {
            throw new java.lang.IllegalArgumentException("Illegal null AudioAttributes");
        }
        mAudioAttributes = attributes;
    }

    /**
     * Adds an external subtitle source file (from the provided input stream.)
     *
     * Note that a single external subtitle source may contain multiple or no
     * supported tracks in it. If the source contained at least one track in
     * it, one will receive an {@link MediaPlayer#MEDIA_INFO_METADATA_UPDATE}
     * info message. Otherwise, if reading the source takes excessive time,
     * one will receive a {@link MediaPlayer#MEDIA_INFO_SUBTITLE_TIMED_OUT}
     * message. If the source contained no supported track (including an empty
     * source file or null input stream), one will receive a {@link MediaPlayer#MEDIA_INFO_UNSUPPORTED_SUBTITLE} message. One can find the
     * total number of available tracks using {@link MediaPlayer#getTrackInfo()}
     * to see what additional tracks become available after this method call.
     *
     * @param is
     * 		input stream containing the subtitle data.  It will be
     * 		closed by the media framework.
     * @param format
     * 		the format of the subtitle track(s).  Must contain at least
     * 		the mime type ({@link MediaFormat#KEY_MIME}) and the
     * 		language ({@link MediaFormat#KEY_LANGUAGE}) of the file.
     * 		If the file itself contains the language information,
     * 		specify "und" for the language.
     */
    public void addSubtitleSource(java.io.InputStream is, android.media.MediaFormat format) {
        if (mMediaPlayer == null) {
            mPendingSubtitleTracks.add(android.util.Pair.create(is, format));
        } else {
            try {
                mMediaPlayer.addSubtitleSource(is, format);
            } catch (java.lang.IllegalStateException e) {
                mInfoListener.onInfo(mMediaPlayer, MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE, 0);
            }
        }
    }

    public void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = android.widget.VideoView.STATE_IDLE;
            mTargetState = android.widget.VideoView.STATE_IDLE;
            mAudioManager.abandonAudioFocus(null);
        }
    }

    private void openVideo() {
        if ((mUri == null) || (mSurfaceHolder == null)) {
            // not ready for playback just yet, will try again later
            return;
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);
        if (mAudioFocusType != android.media.AudioManager.AUDIOFOCUS_NONE) {
            // TODO this should have a focus listener
            /* flags */
            mAudioManager.requestAudioFocus(null, mAudioAttributes, mAudioFocusType, 0);
        }
        try {
            mMediaPlayer = new android.media.MediaPlayer();
            // TODO: create SubtitleController in MediaPlayer, but we need
            // a context for the subtitle renderers
            final android.content.Context context = getContext();
            final android.media.SubtitleController controller = new android.media.SubtitleController(context, mMediaPlayer.getMediaTimeProvider(), mMediaPlayer);
            controller.registerRenderer(new android.media.WebVttRenderer(context));
            controller.registerRenderer(new android.media.TtmlRenderer(context));
            controller.registerRenderer(new android.media.Cea708CaptionRenderer(context));
            controller.registerRenderer(new android.media.ClosedCaptionRenderer(context));
            mMediaPlayer.setSubtitleAnchor(controller, this);
            if (mAudioSession != 0) {
                mMediaPlayer.setAudioSessionId(mAudioSession);
            } else {
                mAudioSession = mMediaPlayer.getAudioSessionId();
            }
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mCurrentBufferPercentage = 0;
            mMediaPlayer.setDataSource(mContext, mUri, mHeaders);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setAudioAttributes(mAudioAttributes);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
            for (android.util.Pair<java.io.InputStream, android.media.MediaFormat> pending : mPendingSubtitleTracks) {
                try {
                    mMediaPlayer.addSubtitleSource(pending.first, pending.second);
                } catch (java.lang.IllegalStateException e) {
                    mInfoListener.onInfo(mMediaPlayer, MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE, 0);
                }
            }
            // we don't set the target state here either, but preserve the
            // target state that was there before.
            mCurrentState = android.widget.VideoView.STATE_PREPARING;
            attachMediaController();
        } catch (java.io.IOException ex) {
            android.util.Log.w(android.widget.VideoView.TAG, "Unable to open content: " + mUri, ex);
            mCurrentState = android.widget.VideoView.STATE_ERROR;
            mTargetState = android.widget.VideoView.STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            return;
        } catch (java.lang.IllegalArgumentException ex) {
            android.util.Log.w(android.widget.VideoView.TAG, "Unable to open content: " + mUri, ex);
            mCurrentState = android.widget.VideoView.STATE_ERROR;
            mTargetState = android.widget.VideoView.STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            return;
        } finally {
            mPendingSubtitleTracks.clear();
        }
    }

    public void setMediaController(android.widget.MediaController controller) {
        if (mMediaController != null) {
            mMediaController.hide();
        }
        mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        if ((mMediaPlayer != null) && (mMediaController != null)) {
            mMediaController.setMediaPlayer(this);
            android.view.View anchorView = (this.getParent() instanceof android.view.View) ? ((android.view.View) (this.getParent())) : this;
            mMediaController.setAnchorView(anchorView);
            mMediaController.setEnabled(isInPlaybackState());
        }
    }

    MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new android.media.MediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(android.media.MediaPlayer mp, int width, int height) {
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            if ((mVideoWidth != 0) && (mVideoHeight != 0)) {
                getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                requestLayout();
            }
        }
    };

    @android.annotation.UnsupportedAppUsage
    MediaPlayer.OnPreparedListener mPreparedListener = new android.media.MediaPlayer.OnPreparedListener() {
        public void onPrepared(android.media.MediaPlayer mp) {
            mCurrentState = android.widget.VideoView.STATE_PREPARED;
            // Get the capabilities of the player for this stream
            android.media.Metadata data = mp.getMetadata(MediaPlayer.METADATA_ALL, MediaPlayer.BYPASS_METADATA_FILTER);
            if (data != null) {
                mCanPause = (!data.has(Metadata.PAUSE_AVAILABLE)) || data.getBoolean(Metadata.PAUSE_AVAILABLE);
                mCanSeekBack = (!data.has(Metadata.SEEK_BACKWARD_AVAILABLE)) || data.getBoolean(Metadata.SEEK_BACKWARD_AVAILABLE);
                mCanSeekForward = (!data.has(Metadata.SEEK_FORWARD_AVAILABLE)) || data.getBoolean(Metadata.SEEK_FORWARD_AVAILABLE);
            } else {
                mCanPause = mCanSeekBack = mCanSeekForward = true;
            }
            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }
            if (mMediaController != null) {
                mMediaController.setEnabled(true);
            }
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            int seekToPosition = mSeekWhenPrepared;// mSeekWhenPrepared may be changed after seekTo() call

            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }
            if ((mVideoWidth != 0) && (mVideoHeight != 0)) {
                // Log.i("@@@@", "video size: " + mVideoWidth +"/"+ mVideoHeight);
                getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                if ((mSurfaceWidth == mVideoWidth) && (mSurfaceHeight == mVideoHeight)) {
                    // We didn't actually change the size (it was already at the size
                    // we need), so we won't get a "surface changed" callback, so
                    // start the video here instead of in the callback.
                    if (mTargetState == android.widget.VideoView.STATE_PLAYING) {
                        start();
                        if (mMediaController != null) {
                            mMediaController.show();
                        }
                    } else
                        if ((!isPlaying()) && ((seekToPosition != 0) || (getCurrentPosition() > 0))) {
                            if (mMediaController != null) {
                                // Show the media controls when we're paused into a video and make 'em stick.
                                mMediaController.show(0);
                            }
                        }

                }
            } else {
                // We don't know the video size yet, but should start anyway.
                // The video size might be reported to us later.
                if (mTargetState == android.widget.VideoView.STATE_PLAYING) {
                    start();
                }
            }
        }
    };

    private android.media.MediaPlayer.OnCompletionListener mCompletionListener = new android.media.MediaPlayer.OnCompletionListener() {
        public void onCompletion(android.media.MediaPlayer mp) {
            mCurrentState = android.widget.VideoView.STATE_PLAYBACK_COMPLETED;
            mTargetState = android.widget.VideoView.STATE_PLAYBACK_COMPLETED;
            if (mMediaController != null) {
                mMediaController.hide();
            }
            if (mOnCompletionListener != null) {
                mOnCompletionListener.onCompletion(mMediaPlayer);
            }
            if (mAudioFocusType != android.media.AudioManager.AUDIOFOCUS_NONE) {
                mAudioManager.abandonAudioFocus(null);
            }
        }
    };

    private android.media.MediaPlayer.OnInfoListener mInfoListener = new android.media.MediaPlayer.OnInfoListener() {
        public boolean onInfo(android.media.MediaPlayer mp, int arg1, int arg2) {
            if (mOnInfoListener != null) {
                mOnInfoListener.onInfo(mp, arg1, arg2);
            }
            return true;
        }
    };

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private android.media.MediaPlayer.OnErrorListener mErrorListener = new android.media.MediaPlayer.OnErrorListener() {
        public boolean onError(android.media.MediaPlayer mp, int framework_err, int impl_err) {
            android.util.Log.d(android.widget.VideoView.TAG, (("Error: " + framework_err) + ",") + impl_err);
            mCurrentState = android.widget.VideoView.STATE_ERROR;
            mTargetState = android.widget.VideoView.STATE_ERROR;
            if (mMediaController != null) {
                mMediaController.hide();
            }
            /* If an error handler has been supplied, use it and finish. */
            if (mOnErrorListener != null) {
                if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                    return true;
                }
            }
            /* Otherwise, pop up an error dialog so the user knows that
            something bad has happened. Only try and pop up the dialog
            if we're attached to a window. When we're going away and no
            longer have a window, don't bother showing the user an error.
             */
            if (getWindowToken() != null) {
                android.content.res.Resources r = mContext.getResources();
                int messageId;
                if (framework_err == android.media.MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
                    messageId = com.android.internal.R.string.VideoView_error_text_invalid_progressive_playback;
                } else {
                    messageId = com.android.internal.R.string.VideoView_error_text_unknown;
                }
                new android.app.AlertDialog.Builder(mContext).setMessage(messageId).setPositiveButton(com.android.internal.R.string.VideoView_error_button, new android.content.DialogInterface.OnClickListener() {
                    public void onClick(android.content.DialogInterface dialog, int whichButton) {
                        /* If we get here, there is no onError listener, so
                        at least inform them that the video is over.
                         */
                        if (mOnCompletionListener != null) {
                            mOnCompletionListener.onCompletion(mMediaPlayer);
                        }
                    }
                }).setCancelable(false).show();
            }
            return true;
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new android.media.MediaPlayer.OnBufferingUpdateListener() {
        public void onBufferingUpdate(android.media.MediaPlayer mp, int percent) {
            mCurrentBufferPercentage = percent;
        }
    };

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l
     * 		The callback that will be run
     */
    public void setOnPreparedListener(android.media.MediaPlayer.OnPreparedListener l) {
        mOnPreparedListener = l;
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l
     * 		The callback that will be run
     */
    public void setOnCompletionListener(android.media.MediaPlayer.OnCompletionListener l) {
        mOnCompletionListener = l;
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l
     * 		The callback that will be run
     */
    public void setOnErrorListener(android.media.MediaPlayer.OnErrorListener l) {
        mOnErrorListener = l;
    }

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param l
     * 		The callback that will be run
     */
    public void setOnInfoListener(android.media.MediaPlayer.OnInfoListener l) {
        mOnInfoListener = l;
    }

    @android.annotation.UnsupportedAppUsage
    android.view.SurfaceHolder.Callback mSHCallback = new android.view.SurfaceHolder.Callback() {
        public void surfaceChanged(android.view.SurfaceHolder holder, int format, int w, int h) {
            mSurfaceWidth = w;
            mSurfaceHeight = h;
            boolean isValidState = mTargetState == android.widget.VideoView.STATE_PLAYING;
            boolean hasValidSize = (mVideoWidth == w) && (mVideoHeight == h);
            if (((mMediaPlayer != null) && isValidState) && hasValidSize) {
                if (mSeekWhenPrepared != 0) {
                    seekTo(mSeekWhenPrepared);
                }
                start();
            }
        }

        public void surfaceCreated(android.view.SurfaceHolder holder) {
            mSurfaceHolder = holder;
            openVideo();
        }

        public void surfaceDestroyed(android.view.SurfaceHolder holder) {
            // after we return from this we can't use the surface any more
            mSurfaceHolder = null;
            if (mMediaController != null)
                mMediaController.hide();

            release(true);
        }
    };

    /* release the media player in any state */
    @android.annotation.UnsupportedAppUsage
    private void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mPendingSubtitleTracks.clear();
            mCurrentState = android.widget.VideoView.STATE_IDLE;
            if (cleartargetstate) {
                mTargetState = android.widget.VideoView.STATE_IDLE;
            }
            if (mAudioFocusType != android.media.AudioManager.AUDIOFOCUS_NONE) {
                mAudioManager.abandonAudioFocus(null);
            }
        }
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        if (((ev.getAction() == android.view.MotionEvent.ACTION_DOWN) && isInPlaybackState()) && (mMediaController != null)) {
            toggleMediaControlsVisiblity();
        }
        return onTouchEvent(ev);
    }

    @java.lang.Override
    public boolean onTrackballEvent(android.view.MotionEvent ev) {
        if (((ev.getAction() == android.view.MotionEvent.ACTION_DOWN) && isInPlaybackState()) && (mMediaController != null)) {
            toggleMediaControlsVisiblity();
        }
        return onTrackballEvent(ev);
    }

    @java.lang.Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        boolean isKeyCodeSupported = ((((((keyCode != android.view.KeyEvent.KEYCODE_BACK) && (keyCode != android.view.KeyEvent.KEYCODE_VOLUME_UP)) && (keyCode != android.view.KeyEvent.KEYCODE_VOLUME_DOWN)) && (keyCode != android.view.KeyEvent.KEYCODE_VOLUME_MUTE)) && (keyCode != android.view.KeyEvent.KEYCODE_MENU)) && (keyCode != android.view.KeyEvent.KEYCODE_CALL)) && (keyCode != android.view.KeyEvent.KEYCODE_ENDCALL);
        if ((isInPlaybackState() && isKeyCodeSupported) && (mMediaController != null)) {
            if ((keyCode == android.view.KeyEvent.KEYCODE_HEADSETHOOK) || (keyCode == android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)) {
                if (mMediaPlayer.isPlaying()) {
                    pause();
                    mMediaController.show();
                } else {
                    start();
                    mMediaController.hide();
                }
                return true;
            } else
                if (keyCode == android.view.KeyEvent.KEYCODE_MEDIA_PLAY) {
                    if (!mMediaPlayer.isPlaying()) {
                        start();
                        mMediaController.hide();
                    }
                    return true;
                } else
                    if ((keyCode == android.view.KeyEvent.KEYCODE_MEDIA_STOP) || (keyCode == android.view.KeyEvent.KEYCODE_MEDIA_PAUSE)) {
                        if (mMediaPlayer.isPlaying()) {
                            pause();
                            mMediaController.show();
                        }
                        return true;
                    } else {
                        toggleMediaControlsVisiblity();
                    }


        }
        return onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
        if (mMediaController.isShowing()) {
            mMediaController.hide();
        } else {
            mMediaController.show();
        }
    }

    @java.lang.Override
    public void start() {
        if (isInPlaybackState()) {
            mMediaPlayer.start();
            mCurrentState = android.widget.VideoView.STATE_PLAYING;
        }
        mTargetState = android.widget.VideoView.STATE_PLAYING;
    }

    @java.lang.Override
    public void pause() {
        if (isInPlaybackState()) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentState = android.widget.VideoView.STATE_PAUSED;
            }
        }
        mTargetState = android.widget.VideoView.STATE_PAUSED;
    }

    public void suspend() {
        release(false);
    }

    public void resume() {
        openVideo();
    }

    @java.lang.Override
    public int getDuration() {
        if (isInPlaybackState()) {
            return mMediaPlayer.getDuration();
        }
        return -1;
    }

    @java.lang.Override
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @java.lang.Override
    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(msec);
            mSeekWhenPrepared = 0;
        } else {
            mSeekWhenPrepared = msec;
        }
    }

    @java.lang.Override
    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    @java.lang.Override
    public int getBufferPercentage() {
        if (mMediaPlayer != null) {
            return mCurrentBufferPercentage;
        }
        return 0;
    }

    private boolean isInPlaybackState() {
        return (((mMediaPlayer != null) && (mCurrentState != android.widget.VideoView.STATE_ERROR)) && (mCurrentState != android.widget.VideoView.STATE_IDLE)) && (mCurrentState != android.widget.VideoView.STATE_PREPARING);
    }

    @java.lang.Override
    public boolean canPause() {
        return mCanPause;
    }

    @java.lang.Override
    public boolean canSeekBackward() {
        return mCanSeekBack;
    }

    @java.lang.Override
    public boolean canSeekForward() {
        return mCanSeekForward;
    }

    @java.lang.Override
    public int getAudioSessionId() {
        if (mAudioSession == 0) {
            android.media.MediaPlayer foo = new android.media.MediaPlayer();
            mAudioSession = foo.getAudioSessionId();
            foo.release();
        }
        return mAudioSession;
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        onAttachedToWindow();
        if (mSubtitleWidget != null) {
            mSubtitleWidget.onAttachedToWindow();
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        onDetachedFromWindow();
        if (mSubtitleWidget != null) {
            mSubtitleWidget.onDetachedFromWindow();
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        onLayout(changed, left, top, right, bottom);
        if (mSubtitleWidget != null) {
            measureAndLayoutSubtitleWidget();
        }
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        draw(canvas);
        if (mSubtitleWidget != null) {
            final int saveCount = canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop());
            mSubtitleWidget.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    /**
     * Forces a measurement and layout pass for all overlaid views.
     *
     * @see #setSubtitleWidget(RenderingWidget)
     */
    private void measureAndLayoutSubtitleWidget() {
        final int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
        final int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
        mSubtitleWidget.setSize(width, height);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void setSubtitleWidget(android.media.SubtitleTrack.RenderingWidget subtitleWidget) {
        if (mSubtitleWidget == subtitleWidget) {
            return;
        }
        final boolean attachedToWindow = isAttachedToWindow();
        if (mSubtitleWidget != null) {
            if (attachedToWindow) {
                mSubtitleWidget.onDetachedFromWindow();
            }
            mSubtitleWidget.setOnChangedListener(null);
        }
        mSubtitleWidget = subtitleWidget;
        if (subtitleWidget != null) {
            if (mSubtitlesChangedListener == null) {
                mSubtitlesChangedListener = new android.media.SubtitleTrack.RenderingWidget.OnChangedListener() {
                    @java.lang.Override
                    public void onChanged(android.media.SubtitleTrack.RenderingWidget renderingWidget) {
                        invalidate();
                    }
                };
            }
            setWillNotDraw(false);
            subtitleWidget.setOnChangedListener(mSubtitlesChangedListener);
            if (attachedToWindow) {
                subtitleWidget.onAttachedToWindow();
                requestLayout();
            }
        } else {
            setWillNotDraw(true);
        }
        invalidate();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.os.Looper getSubtitleLooper() {
        return android.os.Looper.getMainLooper();
    }
}

