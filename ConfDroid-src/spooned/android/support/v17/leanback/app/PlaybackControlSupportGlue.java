/**
 * This file is auto-generated from PlaybackControlGlue.java.  DO NOT MODIFY.
 */
package android.support.v17.leanback.app;


/**
 * A helper class for managing a {@link android.support.v17.leanback.widget.PlaybackControlsRow} and
 * {@link PlaybackOverlaySupportFragment} that implements a recommended approach to handling standard
 * playback control actions such as play/pause, fast forward/rewind at progressive speed levels,
 * and skip to next/previous.  This helper class is a glue layer in that it manages the
 * configuration of and interaction between the leanback UI components by defining a functional
 * interface to the media player.
 *
 * <p>You can instantiate a concrete subclass such as {@link MediaControllerGlue} or you must
 * subclass this abstract helper.  To create a subclass you must implement all of the
 * abstract methods and the subclass must invoke {@link #onMetadataChanged()} and
 * {@link #onStateChanged()} appropriately.
 * </p>
 *
 * <p>To use an instance of the glue layer, first construct an instance.  Constructor parameters
 * inform the glue what speed levels are supported for fast forward/rewind.  Providing a
 * {@link android.support.v17.leanback.app.PlaybackOverlaySupportFragment} is optional.
 * </p>
 *
 * <p>If you have your own controls row you must pass it to {@link #setControlsRow}.
 * The row will be updated by the glue layer based on the media metadata and playback state.
 * Alternatively, you may call {@link #createControlsRowAndPresenter()} which will set a controls
 * row and return a row presenter you can use to present the row.
 * </p>
 *
 * <p>The helper sets a {@link android.support.v17.leanback.widget.SparseArrayObjectAdapter}
 * on the controls row as the primary actions adapter, and adds actions to it.  You can provide
 * additional actions by overriding {@link #createPrimaryActionsAdapter}.  This helper does not
 * deal in secondary actions so those you may add separately.
 * </p>
 *
 * <p>Provide a click listener on your fragment and if an action is clicked, call
 * {@link #onActionClicked}.  There is no need to call {@link #setOnItemViewClickedListener}
 * but if you do a click listener will be installed on the fragment and recognized action clicks
 * will be handled.  Your listener will be called only for unhandled actions.
 * </p>
 *
 * <p>The helper implements a key event handler.  If you pass a
 * {@link android.support.v17.leanback.app.PlaybackOverlaySupportFragment} the fragment's input event
 * handler will be set.  Otherwise, you should set the glue object as key event handler to the
 * ViewHolder when bound by your row presenter; see
 * {@link RowPresenter.ViewHolder#setOnKeyListener(android.view.View.OnKeyListener)}.
 * </p>
 *
 * <p>To update the controls row progress during playback, override {@link #enableProgressUpdating}
 * to manage the lifecycle of a periodic callback to {@link #updateProgress()}.
 * {@link #getUpdatePeriod()} provides a recommended update period.
 * </p>
 */
public abstract class PlaybackControlSupportGlue implements android.support.v17.leanback.widget.OnActionClickedListener , android.view.View.OnKeyListener {
    /**
     * The adapter key for the first custom control on the left side
     * of the predefined primary controls.
     */
    public static final int ACTION_CUSTOM_LEFT_FIRST = 0x1;

    /**
     * The adapter key for the skip to previous control.
     */
    public static final int ACTION_SKIP_TO_PREVIOUS = 0x10;

    /**
     * The adapter key for the rewind control.
     */
    public static final int ACTION_REWIND = 0x20;

    /**
     * The adapter key for the play/pause control.
     */
    public static final int ACTION_PLAY_PAUSE = 0x40;

    /**
     * The adapter key for the fast forward control.
     */
    public static final int ACTION_FAST_FORWARD = 0x80;

    /**
     * The adapter key for the skip to next control.
     */
    public static final int ACTION_SKIP_TO_NEXT = 0x100;

    /**
     * The adapter key for the first custom control on the right side
     * of the predefined primary controls.
     */
    public static final int ACTION_CUSTOM_RIGHT_FIRST = 0x1000;

    /**
     * Invalid playback speed.
     */
    public static final int PLAYBACK_SPEED_INVALID = -1;

    /**
     * Speed representing playback state that is paused.
     */
    public static final int PLAYBACK_SPEED_PAUSED = 0;

    /**
     * Speed representing playback state that is playing normally.
     */
    public static final int PLAYBACK_SPEED_NORMAL = 1;

    /**
     * The initial (level 0) fast forward playback speed.
     * The negative of this value is for rewind at the same speed.
     */
    public static final int PLAYBACK_SPEED_FAST_L0 = 10;

    /**
     * The level 1 fast forward playback speed.
     * The negative of this value is for rewind at the same speed.
     */
    public static final int PLAYBACK_SPEED_FAST_L1 = 11;

    /**
     * The level 2 fast forward playback speed.
     * The negative of this value is for rewind at the same speed.
     */
    public static final int PLAYBACK_SPEED_FAST_L2 = 12;

    /**
     * The level 3 fast forward playback speed.
     * The negative of this value is for rewind at the same speed.
     */
    public static final int PLAYBACK_SPEED_FAST_L3 = 13;

    /**
     * The level 4 fast forward playback speed.
     * The negative of this value is for rewind at the same speed.
     */
    public static final int PLAYBACK_SPEED_FAST_L4 = 14;

    static final java.lang.String TAG = "PlaybackControlSupportGlue";

    static final boolean DEBUG = false;

    static final int MSG_UPDATE_PLAYBACK_STATE = 100;

    private static final int UPDATE_PLAYBACK_STATE_DELAY_MS = 2000;

    private static final int NUMBER_OF_SEEK_SPEEDS = (android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L4 - android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0) + 1;

    private final android.support.v17.leanback.app.PlaybackOverlaySupportFragment mFragment;

    private final android.content.Context mContext;

    private final int[] mFastForwardSpeeds;

    private final int[] mRewindSpeeds;

    private android.support.v17.leanback.widget.PlaybackControlsRow mControlsRow;

    private android.support.v17.leanback.widget.SparseArrayObjectAdapter mPrimaryActionsAdapter;

    private android.support.v17.leanback.widget.PlaybackControlsRow.PlayPauseAction mPlayPauseAction;

    private android.support.v17.leanback.widget.PlaybackControlsRow.SkipNextAction mSkipNextAction;

    private android.support.v17.leanback.widget.PlaybackControlsRow.SkipPreviousAction mSkipPreviousAction;

    private android.support.v17.leanback.widget.PlaybackControlsRow.FastForwardAction mFastForwardAction;

    private android.support.v17.leanback.widget.PlaybackControlsRow.RewindAction mRewindAction;

    android.support.v17.leanback.widget.OnItemViewClickedListener mExternalOnItemViewClickedListener;

    private int mPlaybackSpeed = android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_NORMAL;

    private boolean mFadeWhenPlaying = true;

    static class UpdatePlaybackStateHandler extends android.os.Handler {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == android.support.v17.leanback.app.PlaybackControlSupportGlue.MSG_UPDATE_PLAYBACK_STATE) {
                android.support.v17.leanback.app.PlaybackControlSupportGlue glue = ((java.lang.ref.WeakReference<android.support.v17.leanback.app.PlaybackControlSupportGlue>) (msg.obj)).get();
                if (glue != null) {
                    glue.updatePlaybackState();
                }
            }
        }
    }

    static final android.os.Handler sHandler = new android.support.v17.leanback.app.PlaybackControlSupportGlue.UpdatePlaybackStateHandler();

    final java.lang.ref.WeakReference<android.support.v17.leanback.app.PlaybackControlSupportGlue> mGlueWeakReference = new java.lang.ref.WeakReference(this);

    private final android.support.v17.leanback.widget.OnItemViewClickedListener mOnItemViewClickedListener = new android.support.v17.leanback.widget.OnItemViewClickedListener() {
        @java.lang.Override
        public void onItemClicked(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder, java.lang.Object object, android.support.v17.leanback.widget.RowPresenter.ViewHolder viewHolder2, android.support.v17.leanback.widget.Row row) {
            if (android.support.v17.leanback.app.PlaybackControlSupportGlue.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.PlaybackControlSupportGlue.TAG, "onItemClicked " + object);

            boolean handled = false;
            if (object instanceof android.support.v17.leanback.widget.Action) {
                handled = dispatchAction(((android.support.v17.leanback.widget.Action) (object)), null);
            }
            if ((!handled) && (mExternalOnItemViewClickedListener != null)) {
                mExternalOnItemViewClickedListener.onItemClicked(viewHolder, object, viewHolder2, row);
            }
        }
    };

    /**
     * Constructor for the glue.
     *
     * @param context
     * 		
     * @param seekSpeeds
     * 		Array of seek speeds for fast forward and rewind.
     */
    public PlaybackControlSupportGlue(android.content.Context context, int[] seekSpeeds) {
        this(context, null, seekSpeeds, seekSpeeds);
    }

    /**
     * Constructor for the glue.
     *
     * @param context
     * 		
     * @param fastForwardSpeeds
     * 		Array of seek speeds for fast forward.
     * @param rewindSpeeds
     * 		Array of seek speeds for rewind.
     */
    public PlaybackControlSupportGlue(android.content.Context context, int[] fastForwardSpeeds, int[] rewindSpeeds) {
        this(context, null, fastForwardSpeeds, rewindSpeeds);
    }

    /**
     * Constructor for the glue.
     *
     * @param context
     * 		
     * @param fragment
     * 		Optional; if using a {@link PlaybackOverlaySupportFragment}, pass it in.
     * @param seekSpeeds
     * 		Array of seek speeds for fast forward and rewind.
     */
    public PlaybackControlSupportGlue(android.content.Context context, android.support.v17.leanback.app.PlaybackOverlaySupportFragment fragment, int[] seekSpeeds) {
        this(context, fragment, seekSpeeds, seekSpeeds);
    }

    /**
     * Constructor for the glue.
     *
     * @param context
     * 		
     * @param fragment
     * 		Optional; if using a {@link PlaybackOverlaySupportFragment}, pass it in.
     * @param fastForwardSpeeds
     * 		Array of seek speeds for fast forward.
     * @param rewindSpeeds
     * 		Array of seek speeds for rewind.
     */
    public PlaybackControlSupportGlue(android.content.Context context, android.support.v17.leanback.app.PlaybackOverlaySupportFragment fragment, int[] fastForwardSpeeds, int[] rewindSpeeds) {
        mContext = context;
        mFragment = fragment;
        if (fragment != null) {
            attachToFragment();
        }
        if ((fastForwardSpeeds.length == 0) || (fastForwardSpeeds.length > android.support.v17.leanback.app.PlaybackControlSupportGlue.NUMBER_OF_SEEK_SPEEDS)) {
            throw new java.lang.IllegalStateException("invalid fastForwardSpeeds array size");
        }
        mFastForwardSpeeds = fastForwardSpeeds;
        if ((rewindSpeeds.length == 0) || (rewindSpeeds.length > android.support.v17.leanback.app.PlaybackControlSupportGlue.NUMBER_OF_SEEK_SPEEDS)) {
            throw new java.lang.IllegalStateException("invalid rewindSpeeds array size");
        }
        mRewindSpeeds = rewindSpeeds;
    }

    private final android.support.v17.leanback.app.PlaybackOverlaySupportFragment.InputEventHandler mOnInputEventHandler = new android.support.v17.leanback.app.PlaybackOverlaySupportFragment.InputEventHandler() {
        @java.lang.Override
        public boolean handleInputEvent(android.view.InputEvent event) {
            if (event instanceof android.view.KeyEvent) {
                android.view.KeyEvent keyEvent = ((android.view.KeyEvent) (event));
                return onKey(null, keyEvent.getKeyCode(), keyEvent);
            }
            return false;
        }
    };

    private void attachToFragment() {
        mFragment.setInputEventHandler(mOnInputEventHandler);
    }

    /**
     * Helper method for instantiating a
     * {@link android.support.v17.leanback.widget.PlaybackControlsRow} and corresponding
     * {@link android.support.v17.leanback.widget.PlaybackControlsRowPresenter}.
     */
    public android.support.v17.leanback.widget.PlaybackControlsRowPresenter createControlsRowAndPresenter() {
        android.support.v17.leanback.widget.PlaybackControlsRow controlsRow = new android.support.v17.leanback.widget.PlaybackControlsRow(this);
        setControlsRow(controlsRow);
        android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter detailsPresenter = new android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter() {
            @java.lang.Override
            protected void onBindDescription(android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter.ViewHolder viewHolder, java.lang.Object object) {
                android.support.v17.leanback.app.PlaybackControlSupportGlue glue = ((android.support.v17.leanback.app.PlaybackControlSupportGlue) (object));
                if (glue.hasValidMedia()) {
                    viewHolder.getTitle().setText(glue.getMediaTitle());
                    viewHolder.getSubtitle().setText(glue.getMediaSubtitle());
                } else {
                    viewHolder.getTitle().setText("");
                    viewHolder.getSubtitle().setText("");
                }
            }
        };
        return new android.support.v17.leanback.widget.PlaybackControlsRowPresenter(detailsPresenter) {
            @java.lang.Override
            protected void onBindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh, java.lang.Object item) {
                super.onBindRowViewHolder(vh, item);
                vh.setOnKeyListener(android.support.v17.leanback.app.PlaybackControlSupportGlue.this);
            }

            @java.lang.Override
            protected void onUnbindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
                super.onUnbindRowViewHolder(vh);
                vh.setOnKeyListener(null);
            }
        };
    }

    /**
     * Returns the fragment.
     */
    public android.support.v17.leanback.app.PlaybackOverlaySupportFragment getFragment() {
        return mFragment;
    }

    /**
     * Returns the context.
     */
    public android.content.Context getContext() {
        return mContext;
    }

    /**
     * Returns the fast forward speeds.
     */
    public int[] getFastForwardSpeeds() {
        return mFastForwardSpeeds;
    }

    /**
     * Returns the rewind speeds.
     */
    public int[] getRewindSpeeds() {
        return mRewindSpeeds;
    }

    /**
     * Sets the controls to fade after a timeout when media is playing.
     */
    public void setFadingEnabled(boolean enable) {
        mFadeWhenPlaying = enable;
        if ((!mFadeWhenPlaying) && (mFragment != null)) {
            mFragment.setFadingEnabled(false);
        }
    }

    /**
     * Returns true if controls are set to fade when media is playing.
     */
    public boolean isFadingEnabled() {
        return mFadeWhenPlaying;
    }

    /**
     * Set the {@link OnItemViewClickedListener} to be called if the click event
     * is not handled internally.
     *
     * @param listener
     * 		
     * @deprecated Don't call this.  Instead set the listener on the fragment yourself,
    and call {@link #onActionClicked} to handle clicks.
     */
    @java.lang.Deprecated
    public void setOnItemViewClickedListener(android.support.v17.leanback.widget.OnItemViewClickedListener listener) {
        mExternalOnItemViewClickedListener = listener;
        if (mFragment != null) {
            mFragment.setOnItemViewClickedListener(mOnItemViewClickedListener);
        }
    }

    /**
     * Returns the {@link OnItemViewClickedListener}.
     */
    public android.support.v17.leanback.widget.OnItemViewClickedListener getOnItemViewClickedListener() {
        return mExternalOnItemViewClickedListener;
    }

    /**
     * Sets the controls row to be managed by the glue layer.
     * The primary actions and playback state related aspects of the row
     * are updated by the glue.
     */
    public void setControlsRow(android.support.v17.leanback.widget.PlaybackControlsRow controlsRow) {
        mControlsRow = controlsRow;
        mPrimaryActionsAdapter = createPrimaryActionsAdapter(new android.support.v17.leanback.widget.ControlButtonPresenterSelector());
        mControlsRow.setPrimaryActionsAdapter(mPrimaryActionsAdapter);
        updateControlsRow();
    }

    /**
     * Returns the playback controls row managed by the glue layer.
     */
    public android.support.v17.leanback.widget.PlaybackControlsRow getControlsRow() {
        return mControlsRow;
    }

    /**
     * Override this to start/stop a runnable to call {@link #updateProgress} at
     * an interval such as {@link #getUpdatePeriod}.
     */
    public void enableProgressUpdating(boolean enable) {
    }

    /**
     * Returns the time period in milliseconds that should be used
     * to update the progress.  See {@link #updateProgress()}.
     */
    public int getUpdatePeriod() {
        // TODO: calculate a better update period based on total duration and screen size
        return 500;
    }

    /**
     * Updates the progress bar based on the current media playback position.
     */
    public void updateProgress() {
        int position = getCurrentPosition();
        if (android.support.v17.leanback.app.PlaybackControlSupportGlue.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.PlaybackControlSupportGlue.TAG, "updateProgress " + position);

        mControlsRow.setCurrentTime(position);
    }

    /**
     * Handles action clicks.  A subclass may override this add support for additional actions.
     */
    @java.lang.Override
    public void onActionClicked(android.support.v17.leanback.widget.Action action) {
        dispatchAction(action, null);
    }

    /**
     * Handles key events and returns true if handled.  A subclass may override this to provide
     * additional support.
     */
    @java.lang.Override
    public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_UP :
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
            case android.view.KeyEvent.KEYCODE_BACK :
            case android.view.KeyEvent.KEYCODE_ESCAPE :
                boolean abortSeek = (mPlaybackSpeed >= android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0) || (mPlaybackSpeed <= (-android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0));
                if (abortSeek) {
                    mPlaybackSpeed = android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_NORMAL;
                    startPlayback(mPlaybackSpeed);
                    updatePlaybackStatusAfterUserAction();
                    return (keyCode == android.view.KeyEvent.KEYCODE_BACK) || (keyCode == android.view.KeyEvent.KEYCODE_ESCAPE);
                }
                return false;
        }
        android.support.v17.leanback.widget.Action action = mControlsRow.getActionForKeyCode(mPrimaryActionsAdapter, keyCode);
        if (action != null) {
            if (((((action == mPrimaryActionsAdapter.lookup(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_PLAY_PAUSE)) || (action == mPrimaryActionsAdapter.lookup(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_REWIND))) || (action == mPrimaryActionsAdapter.lookup(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_FAST_FORWARD))) || (action == mPrimaryActionsAdapter.lookup(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_SKIP_TO_PREVIOUS))) || (action == mPrimaryActionsAdapter.lookup(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_SKIP_TO_NEXT))) {
                if (((android.view.KeyEvent) (event)).getAction() == android.view.KeyEvent.ACTION_DOWN) {
                    dispatchAction(action, ((android.view.KeyEvent) (event)));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Called when the given action is invoked, either by click or keyevent.
     */
    boolean dispatchAction(android.support.v17.leanback.widget.Action action, android.view.KeyEvent keyEvent) {
        boolean handled = false;
        if (action == mPlayPauseAction) {
            boolean canPlay = ((keyEvent == null) || (keyEvent.getKeyCode() == android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)) || (keyEvent.getKeyCode() == android.view.KeyEvent.KEYCODE_MEDIA_PLAY);
            boolean canPause = ((keyEvent == null) || (keyEvent.getKeyCode() == android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)) || (keyEvent.getKeyCode() == android.view.KeyEvent.KEYCODE_MEDIA_PAUSE);
            // PLAY_PAUSE    PLAY      PAUSE
            // playing    paused                  paused
            // paused     playing       playing
            // ff/rw      playing       playing   paused
            if (canPause && (canPlay ? mPlaybackSpeed == android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_NORMAL : mPlaybackSpeed != android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_PAUSED)) {
                mPlaybackSpeed = android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_PAUSED;
                pausePlayback();
            } else
                if (canPlay && (mPlaybackSpeed != android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_NORMAL)) {
                    mPlaybackSpeed = android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_NORMAL;
                    startPlayback(mPlaybackSpeed);
                }

            updatePlaybackStatusAfterUserAction();
            handled = true;
        } else
            if (action == mSkipNextAction) {
                skipToNext();
                handled = true;
            } else
                if (action == mSkipPreviousAction) {
                    skipToPrevious();
                    handled = true;
                } else
                    if (action == mFastForwardAction) {
                        if (mPlaybackSpeed < getMaxForwardSpeedId()) {
                            switch (mPlaybackSpeed) {
                                case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0 :
                                case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L1 :
                                case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L2 :
                                case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L3 :
                                    mPlaybackSpeed++;
                                    break;
                                default :
                                    mPlaybackSpeed = android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0;
                                    break;
                            }
                            startPlayback(mPlaybackSpeed);
                            updatePlaybackStatusAfterUserAction();
                        }
                        handled = true;
                    } else
                        if (action == mRewindAction) {
                            if (mPlaybackSpeed > (-getMaxRewindSpeedId())) {
                                switch (mPlaybackSpeed) {
                                    case -android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0 :
                                    case -android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L1 :
                                    case -android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L2 :
                                    case -android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L3 :
                                        mPlaybackSpeed--;
                                        break;
                                    default :
                                        mPlaybackSpeed = -android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0;
                                        break;
                                }
                                startPlayback(mPlaybackSpeed);
                                updatePlaybackStatusAfterUserAction();
                            }
                            handled = true;
                        }




        return handled;
    }

    private int getMaxForwardSpeedId() {
        return android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0 + (mFastForwardSpeeds.length - 1);
    }

    private int getMaxRewindSpeedId() {
        return android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0 + (mRewindSpeeds.length - 1);
    }

    private void updateControlsRow() {
        updateRowMetadata();
        android.support.v17.leanback.app.PlaybackControlSupportGlue.sHandler.removeMessages(android.support.v17.leanback.app.PlaybackControlSupportGlue.MSG_UPDATE_PLAYBACK_STATE, mGlueWeakReference);
        updatePlaybackState();
    }

    private void updatePlaybackStatusAfterUserAction() {
        updatePlaybackState(mPlaybackSpeed);
        // Sync playback state after a delay
        android.support.v17.leanback.app.PlaybackControlSupportGlue.sHandler.removeMessages(android.support.v17.leanback.app.PlaybackControlSupportGlue.MSG_UPDATE_PLAYBACK_STATE, mGlueWeakReference);
        android.support.v17.leanback.app.PlaybackControlSupportGlue.sHandler.sendMessageDelayed(android.support.v17.leanback.app.PlaybackControlSupportGlue.sHandler.obtainMessage(android.support.v17.leanback.app.PlaybackControlSupportGlue.MSG_UPDATE_PLAYBACK_STATE, mGlueWeakReference), android.support.v17.leanback.app.PlaybackControlSupportGlue.UPDATE_PLAYBACK_STATE_DELAY_MS);
    }

    private void updateRowMetadata() {
        if (mControlsRow == null) {
            return;
        }
        if (android.support.v17.leanback.app.PlaybackControlSupportGlue.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.PlaybackControlSupportGlue.TAG, "updateRowMetadata hasValidMedia " + hasValidMedia());

        if (!hasValidMedia()) {
            mControlsRow.setImageDrawable(null);
            mControlsRow.setTotalTime(0);
            mControlsRow.setCurrentTime(0);
        } else {
            mControlsRow.setImageDrawable(getMediaArt());
            mControlsRow.setTotalTime(getMediaDuration());
            mControlsRow.setCurrentTime(getCurrentPosition());
        }
        onRowChanged(mControlsRow);
    }

    void updatePlaybackState() {
        if (hasValidMedia()) {
            mPlaybackSpeed = getCurrentSpeedId();
            updatePlaybackState(mPlaybackSpeed);
        }
    }

    private void updatePlaybackState(int playbackSpeed) {
        if (mControlsRow == null) {
            return;
        }
        final long actions = getSupportedActions();
        if ((actions & android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_SKIP_TO_PREVIOUS) != 0) {
            if (mSkipPreviousAction == null) {
                mSkipPreviousAction = new android.support.v17.leanback.widget.PlaybackControlsRow.SkipPreviousAction(mContext);
            }
            mPrimaryActionsAdapter.set(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_SKIP_TO_PREVIOUS, mSkipPreviousAction);
        } else {
            mPrimaryActionsAdapter.clear(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_SKIP_TO_PREVIOUS);
            mSkipPreviousAction = null;
        }
        if ((actions & android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_REWIND) != 0) {
            if (mRewindAction == null) {
                mRewindAction = new android.support.v17.leanback.widget.PlaybackControlsRow.RewindAction(mContext, mRewindSpeeds.length);
            }
            mPrimaryActionsAdapter.set(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_REWIND, mRewindAction);
        } else {
            mPrimaryActionsAdapter.clear(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_REWIND);
            mRewindAction = null;
        }
        if ((actions & android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_PLAY_PAUSE) != 0) {
            if (mPlayPauseAction == null) {
                mPlayPauseAction = new android.support.v17.leanback.widget.PlaybackControlsRow.PlayPauseAction(mContext);
            }
            mPrimaryActionsAdapter.set(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_PLAY_PAUSE, mPlayPauseAction);
        } else {
            mPrimaryActionsAdapter.clear(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_PLAY_PAUSE);
            mPlayPauseAction = null;
        }
        if ((actions & android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_FAST_FORWARD) != 0) {
            if (mFastForwardAction == null) {
                mFastForwardAction = new android.support.v17.leanback.widget.PlaybackControlsRow.FastForwardAction(mContext, mFastForwardSpeeds.length);
            }
            mPrimaryActionsAdapter.set(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_FAST_FORWARD, mFastForwardAction);
        } else {
            mPrimaryActionsAdapter.clear(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_FAST_FORWARD);
            mFastForwardAction = null;
        }
        if ((actions & android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_SKIP_TO_NEXT) != 0) {
            if (mSkipNextAction == null) {
                mSkipNextAction = new android.support.v17.leanback.widget.PlaybackControlsRow.SkipNextAction(mContext);
            }
            mPrimaryActionsAdapter.set(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_SKIP_TO_NEXT, mSkipNextAction);
        } else {
            mPrimaryActionsAdapter.clear(android.support.v17.leanback.app.PlaybackControlSupportGlue.ACTION_SKIP_TO_NEXT);
            mSkipNextAction = null;
        }
        if (mFastForwardAction != null) {
            int index = 0;
            if (playbackSpeed >= android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0) {
                index = (playbackSpeed - android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0) + 1;
            }
            if (mFastForwardAction.getIndex() != index) {
                mFastForwardAction.setIndex(index);
                android.support.v17.leanback.app.PlaybackControlSupportGlue.notifyItemChanged(mPrimaryActionsAdapter, mFastForwardAction);
            }
        }
        if (mRewindAction != null) {
            int index = 0;
            if (playbackSpeed <= (-android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0)) {
                index = ((-playbackSpeed) - android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0) + 1;
            }
            if (mRewindAction.getIndex() != index) {
                mRewindAction.setIndex(index);
                android.support.v17.leanback.app.PlaybackControlSupportGlue.notifyItemChanged(mPrimaryActionsAdapter, mRewindAction);
            }
        }
        if (playbackSpeed == android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_PAUSED) {
            updateProgress();
            enableProgressUpdating(false);
        } else {
            enableProgressUpdating(true);
        }
        if (mFadeWhenPlaying && (mFragment != null)) {
            mFragment.setFadingEnabled(playbackSpeed == android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_NORMAL);
        }
        if (mPlayPauseAction != null) {
            int index = (playbackSpeed == android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_PAUSED) ? android.support.v17.leanback.widget.PlaybackControlsRow.PlayPauseAction.PLAY : android.support.v17.leanback.widget.PlaybackControlsRow.PlayPauseAction.PAUSE;
            if (mPlayPauseAction.getIndex() != index) {
                mPlayPauseAction.setIndex(index);
                android.support.v17.leanback.app.PlaybackControlSupportGlue.notifyItemChanged(mPrimaryActionsAdapter, mPlayPauseAction);
            }
        }
    }

    private static void notifyItemChanged(android.support.v17.leanback.widget.SparseArrayObjectAdapter adapter, java.lang.Object object) {
        int index = adapter.indexOf(object);
        if (index >= 0) {
            adapter.notifyArrayItemRangeChanged(index, 1);
        }
    }

    private static java.lang.String getSpeedString(int speed) {
        switch (speed) {
            case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_INVALID :
                return "PLAYBACK_SPEED_INVALID";
            case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_PAUSED :
                return "PLAYBACK_SPEED_PAUSED";
            case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_NORMAL :
                return "PLAYBACK_SPEED_NORMAL";
            case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0 :
                return "PLAYBACK_SPEED_FAST_L0";
            case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L1 :
                return "PLAYBACK_SPEED_FAST_L1";
            case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L2 :
                return "PLAYBACK_SPEED_FAST_L2";
            case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L3 :
                return "PLAYBACK_SPEED_FAST_L3";
            case android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L4 :
                return "PLAYBACK_SPEED_FAST_L4";
            case -android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L0 :
                return "-PLAYBACK_SPEED_FAST_L0";
            case -android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L1 :
                return "-PLAYBACK_SPEED_FAST_L1";
            case -android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L2 :
                return "-PLAYBACK_SPEED_FAST_L2";
            case -android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L3 :
                return "-PLAYBACK_SPEED_FAST_L3";
            case -android.support.v17.leanback.app.PlaybackControlSupportGlue.PLAYBACK_SPEED_FAST_L4 :
                return "-PLAYBACK_SPEED_FAST_L4";
        }
        return null;
    }

    /**
     * Returns true if there is a valid media item.
     */
    public abstract boolean hasValidMedia();

    /**
     * Returns true if media is currently playing.
     */
    public abstract boolean isMediaPlaying();

    /**
     * Returns the title of the media item.
     */
    public abstract java.lang.CharSequence getMediaTitle();

    /**
     * Returns the subtitle of the media item.
     */
    public abstract java.lang.CharSequence getMediaSubtitle();

    /**
     * Returns the duration of the media item in milliseconds.
     */
    public abstract int getMediaDuration();

    /**
     * Returns a bitmap of the art for the media item.
     */
    public abstract android.graphics.drawable.Drawable getMediaArt();

    /**
     * Returns a bitmask of actions supported by the media player.
     */
    public abstract long getSupportedActions();

    /**
     * Returns the current playback speed.  When playing normally,
     * {@link #PLAYBACK_SPEED_NORMAL} should be returned.
     */
    public abstract int getCurrentSpeedId();

    /**
     * Returns the current position of the media item in milliseconds.
     */
    public abstract int getCurrentPosition();

    /**
     * Start playback at the given speed.
     *
     * @param speed
     * 		The desired playback speed.  For normal playback this will be
     * 		{@link #PLAYBACK_SPEED_NORMAL}; higher positive values for fast forward,
     * 		and negative values for rewind.
     */
    protected abstract void startPlayback(int speed);

    /**
     * Pause playback.
     */
    protected abstract void pausePlayback();

    /**
     * Skip to the next track.
     */
    protected abstract void skipToNext();

    /**
     * Skip to the previous track.
     */
    protected abstract void skipToPrevious();

    /**
     * Invoked when the playback controls row has changed.  The adapter containing this row
     * should be notified.
     */
    protected abstract void onRowChanged(android.support.v17.leanback.widget.PlaybackControlsRow row);

    /**
     * Creates the primary action adapter.  May be overridden to add additional primary
     * actions to the adapter.
     */
    protected android.support.v17.leanback.widget.SparseArrayObjectAdapter createPrimaryActionsAdapter(android.support.v17.leanback.widget.PresenterSelector presenterSelector) {
        return new android.support.v17.leanback.widget.SparseArrayObjectAdapter(presenterSelector);
    }

    /**
     * Must be called appropriately by a subclass when the playback state has changed.
     * It updates the playback state displayed on the media player.
     */
    protected void onStateChanged() {
        if (android.support.v17.leanback.app.PlaybackControlSupportGlue.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.PlaybackControlSupportGlue.TAG, "onStateChanged");

        // If a pending control button update is present, delay
        // the update until the state settles.
        if (!hasValidMedia()) {
            return;
        }
        if (android.support.v17.leanback.app.PlaybackControlSupportGlue.sHandler.hasMessages(android.support.v17.leanback.app.PlaybackControlSupportGlue.MSG_UPDATE_PLAYBACK_STATE, mGlueWeakReference)) {
            android.support.v17.leanback.app.PlaybackControlSupportGlue.sHandler.removeMessages(android.support.v17.leanback.app.PlaybackControlSupportGlue.MSG_UPDATE_PLAYBACK_STATE, mGlueWeakReference);
            if (getCurrentSpeedId() != mPlaybackSpeed) {
                if (android.support.v17.leanback.app.PlaybackControlSupportGlue.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.PlaybackControlSupportGlue.TAG, "Status expectation mismatch, delaying update");

                android.support.v17.leanback.app.PlaybackControlSupportGlue.sHandler.sendMessageDelayed(android.support.v17.leanback.app.PlaybackControlSupportGlue.sHandler.obtainMessage(android.support.v17.leanback.app.PlaybackControlSupportGlue.MSG_UPDATE_PLAYBACK_STATE, mGlueWeakReference), android.support.v17.leanback.app.PlaybackControlSupportGlue.UPDATE_PLAYBACK_STATE_DELAY_MS);
            } else {
                if (android.support.v17.leanback.app.PlaybackControlSupportGlue.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.PlaybackControlSupportGlue.TAG, "Update state matches expectation");

                updatePlaybackState();
            }
        } else {
            updatePlaybackState();
        }
    }

    /**
     * Must be called appropriately by a subclass when the metadata state has changed.
     */
    protected void onMetadataChanged() {
        if (android.support.v17.leanback.app.PlaybackControlSupportGlue.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.PlaybackControlSupportGlue.TAG, "onMetadataChanged");

        updateRowMetadata();
    }
}

