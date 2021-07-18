package android.support.v17.leanback.app;


/**
 * A helper class for implementing a glue layer between a
 * {@link PlaybackOverlayFragment} and a
 * {@link android.support.v4.media.session.MediaControllerCompat}.
 */
public abstract class MediaControllerGlue extends android.support.v17.leanback.app.PlaybackControlGlue {
    static final java.lang.String TAG = "MediaControllerGlue";

    static final boolean DEBUG = false;

    android.support.v4.media.session.MediaControllerCompat mMediaController;

    private final android.support.v4.media.session.MediaControllerCompat.Callback mCallback = new android.support.v4.media.session.MediaControllerCompat.Callback() {
        @java.lang.Override
        public void onMetadataChanged(android.support.v4.media.MediaMetadataCompat metadata) {
            if (android.support.v17.leanback.app.MediaControllerGlue.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.MediaControllerGlue.TAG, "onMetadataChanged");

            android.support.v17.leanback.app.MediaControllerGlue.this.onMetadataChanged();
        }

        @java.lang.Override
        public void onPlaybackStateChanged(android.support.v4.media.session.PlaybackStateCompat state) {
            if (android.support.v17.leanback.app.MediaControllerGlue.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.MediaControllerGlue.TAG, "onPlaybackStateChanged");

            onStateChanged();
        }

        @java.lang.Override
        public void onSessionDestroyed() {
            if (android.support.v17.leanback.app.MediaControllerGlue.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.MediaControllerGlue.TAG, "onSessionDestroyed");

            mMediaController = null;
        }

        @java.lang.Override
        public void onSessionEvent(java.lang.String event, android.os.Bundle extras) {
            if (android.support.v17.leanback.app.MediaControllerGlue.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.MediaControllerGlue.TAG, "onSessionEvent");

        }
    };

    /**
     * Constructor for the glue.
     *
     * <p>The {@link PlaybackOverlayFragment} must be passed in.
     * A {@link android.support.v17.leanback.widget.OnItemViewClickedListener} and
     * {@link PlaybackOverlayFragment.InputEventHandler}
     * will be set on the fragment.
     * </p>
     *
     * @param context
     * 		
     * @param fragment
     * 		
     * @param seekSpeeds
     * 		Array of seek speeds for fast forward and rewind.
     */
    public MediaControllerGlue(android.content.Context context, android.support.v17.leanback.app.PlaybackOverlayFragment fragment, int[] seekSpeeds) {
        super(context, fragment, seekSpeeds);
    }

    /**
     * Constructor for the glue.
     *
     * <p>The {@link PlaybackOverlayFragment} must be passed in.
     * A {@link android.support.v17.leanback.widget.OnItemViewClickedListener} and
     * {@link PlaybackOverlayFragment.InputEventHandler}
     * will be set on the fragment.
     * </p>
     *
     * @param context
     * 		
     * @param fragment
     * 		
     * @param fastForwardSpeeds
     * 		Array of seek speeds for fast forward.
     * @param rewindSpeeds
     * 		Array of seek speeds for rewind.
     */
    public MediaControllerGlue(android.content.Context context, android.support.v17.leanback.app.PlaybackOverlayFragment fragment, int[] fastForwardSpeeds, int[] rewindSpeeds) {
        super(context, fragment, fastForwardSpeeds, rewindSpeeds);
    }

    /**
     * Attaches to the given media controller.
     */
    public void attachToMediaController(android.support.v4.media.session.MediaControllerCompat mediaController) {
        if (mediaController != mMediaController) {
            if (android.support.v17.leanback.app.MediaControllerGlue.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.MediaControllerGlue.TAG, "New media controller " + mediaController);

            detach();
            mMediaController = mediaController;
            if (mMediaController != null) {
                mMediaController.registerCallback(mCallback);
            }
            onMetadataChanged();
            onStateChanged();
        }
    }

    /**
     * Detaches from the media controller.  Must be called when the object is no longer
     * needed.
     */
    public void detach() {
        if (mMediaController != null) {
            mMediaController.unregisterCallback(mCallback);
        }
        mMediaController = null;
    }

    /**
     * Returns the media controller currently attached.
     */
    public final android.support.v4.media.session.MediaControllerCompat getMediaController() {
        return mMediaController;
    }

    @java.lang.Override
    public boolean hasValidMedia() {
        return (mMediaController != null) && (mMediaController.getMetadata() != null);
    }

    @java.lang.Override
    public boolean isMediaPlaying() {
        return mMediaController.getPlaybackState().getState() == android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
    }

    @java.lang.Override
    public int getCurrentSpeedId() {
        int speed = ((int) (mMediaController.getPlaybackState().getPlaybackSpeed()));
        if (speed == 0) {
            return android.support.v17.leanback.app.PlaybackControlGlue.PLAYBACK_SPEED_PAUSED;
        } else
            if (speed == 1) {
                return android.support.v17.leanback.app.PlaybackControlGlue.PLAYBACK_SPEED_NORMAL;
            } else
                if (speed > 0) {
                    int[] seekSpeeds = getFastForwardSpeeds();
                    for (int index = 0; index < seekSpeeds.length; index++) {
                        if (speed == seekSpeeds[index]) {
                            return android.support.v17.leanback.app.PlaybackControlGlue.PLAYBACK_SPEED_FAST_L0 + index;
                        }
                    }
                } else {
                    int[] seekSpeeds = getRewindSpeeds();
                    for (int index = 0; index < seekSpeeds.length; index++) {
                        if ((-speed) == seekSpeeds[index]) {
                            return (-android.support.v17.leanback.app.PlaybackControlGlue.PLAYBACK_SPEED_FAST_L0) - index;
                        }
                    }
                }


        android.util.Log.w(android.support.v17.leanback.app.MediaControllerGlue.TAG, "Couldn't find index for speed " + speed);
        return android.support.v17.leanback.app.PlaybackControlGlue.PLAYBACK_SPEED_INVALID;
    }

    @java.lang.Override
    public java.lang.CharSequence getMediaTitle() {
        return mMediaController.getMetadata().getDescription().getTitle();
    }

    @java.lang.Override
    public java.lang.CharSequence getMediaSubtitle() {
        return mMediaController.getMetadata().getDescription().getSubtitle();
    }

    @java.lang.Override
    public int getMediaDuration() {
        return ((int) (mMediaController.getMetadata().getLong(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION)));
    }

    @java.lang.Override
    public int getCurrentPosition() {
        return ((int) (mMediaController.getPlaybackState().getPosition()));
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getMediaArt() {
        android.graphics.Bitmap bitmap = mMediaController.getMetadata().getDescription().getIconBitmap();
        return bitmap == null ? null : new android.graphics.drawable.BitmapDrawable(getContext().getResources(), bitmap);
    }

    @java.lang.Override
    public long getSupportedActions() {
        long result = 0;
        long actions = mMediaController.getPlaybackState().getActions();
        if ((actions & android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE) != 0) {
            result |= android.support.v17.leanback.app.PlaybackControlGlue.ACTION_PLAY_PAUSE;
        }
        if ((actions & android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
            result |= android.support.v17.leanback.app.PlaybackControlGlue.ACTION_SKIP_TO_NEXT;
        }
        if ((actions & android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0) {
            result |= android.support.v17.leanback.app.PlaybackControlGlue.ACTION_SKIP_TO_PREVIOUS;
        }
        if ((actions & android.support.v4.media.session.PlaybackStateCompat.ACTION_FAST_FORWARD) != 0) {
            result |= android.support.v17.leanback.app.PlaybackControlGlue.ACTION_FAST_FORWARD;
        }
        if ((actions & android.support.v4.media.session.PlaybackStateCompat.ACTION_REWIND) != 0) {
            result |= android.support.v17.leanback.app.PlaybackControlGlue.ACTION_REWIND;
        }
        return result;
    }

    @java.lang.Override
    protected void startPlayback(int speed) {
        if (android.support.v17.leanback.app.MediaControllerGlue.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.MediaControllerGlue.TAG, "startPlayback speed " + speed);

        if (speed == android.support.v17.leanback.app.PlaybackControlGlue.PLAYBACK_SPEED_NORMAL) {
            mMediaController.getTransportControls().play();
        } else
            if (speed > 0) {
                mMediaController.getTransportControls().fastForward();
            } else {
                mMediaController.getTransportControls().rewind();
            }

    }

    @java.lang.Override
    protected void pausePlayback() {
        if (android.support.v17.leanback.app.MediaControllerGlue.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.MediaControllerGlue.TAG, "pausePlayback");

        mMediaController.getTransportControls().pause();
    }

    @java.lang.Override
    protected void skipToNext() {
        if (android.support.v17.leanback.app.MediaControllerGlue.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.MediaControllerGlue.TAG, "skipToNext");

        mMediaController.getTransportControls().skipToNext();
    }

    @java.lang.Override
    protected void skipToPrevious() {
        if (android.support.v17.leanback.app.MediaControllerGlue.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.MediaControllerGlue.TAG, "skipToPrevious");

        mMediaController.getTransportControls().skipToPrevious();
    }
}

