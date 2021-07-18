/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * A {@link Row} of playback controls to be displayed by a {@link PlaybackControlsRowPresenter}.
 *
 * This row consists of some optional item detail, a series of primary actions,
 * and an optional series of secondary actions.
 *
 * <p>
 * Controls are specified via an {@link ObjectAdapter} containing one or more
 * {@link Action}s.
 * </p>
 * <p>
 * Adapters should have their {@link PresenterSelector} set to an instance of
 * {@link ControlButtonPresenterSelector}.
 * </p>
 */
public class PlaybackControlsRow extends android.support.v17.leanback.widget.Row {
    /**
     * Base class for an action comprised of a series of icons.
     */
    public static abstract class MultiAction extends android.support.v17.leanback.widget.Action {
        private int mIndex;

        private android.graphics.drawable.Drawable[] mDrawables;

        private java.lang.String[] mLabels;

        private java.lang.String[] mLabels2;

        /**
         * Constructor
         *
         * @param id
         * 		The id of the Action.
         */
        public MultiAction(int id) {
            super(id);
        }

        /**
         * Sets the array of drawables.  The size of the array defines the range
         * of valid indices for this action.
         */
        public void setDrawables(android.graphics.drawable.Drawable[] drawables) {
            mDrawables = drawables;
            setIndex(0);
        }

        /**
         * Sets the array of strings used as labels.  The size of the array defines the range
         * of valid indices for this action.  The labels are used to define the accessibility
         * content description unless secondary labels are provided.
         */
        public void setLabels(java.lang.String[] labels) {
            mLabels = labels;
            setIndex(0);
        }

        /**
         * Sets the array of strings used as secondary labels.  These labels are used
         * in place of the primary labels for accessibility content description only.
         */
        public void setSecondaryLabels(java.lang.String[] labels) {
            mLabels2 = labels;
            setIndex(0);
        }

        /**
         * Returns the number of actions.
         */
        public int getActionCount() {
            if (mDrawables != null) {
                return mDrawables.length;
            }
            if (mLabels != null) {
                return mLabels.length;
            }
            return 0;
        }

        /**
         * Returns the drawable at the given index.
         */
        public android.graphics.drawable.Drawable getDrawable(int index) {
            return mDrawables == null ? null : mDrawables[index];
        }

        /**
         * Returns the label at the given index.
         */
        public java.lang.String getLabel(int index) {
            return mLabels == null ? null : mLabels[index];
        }

        /**
         * Returns the secondary label at the given index.
         */
        public java.lang.String getSecondaryLabel(int index) {
            return mLabels2 == null ? null : mLabels2[index];
        }

        /**
         * Increments the index, wrapping to zero once the end is reached.
         */
        public void nextIndex() {
            setIndex(mIndex < (getActionCount() - 1) ? mIndex + 1 : 0);
        }

        /**
         * Sets the current index.
         */
        public void setIndex(int index) {
            mIndex = index;
            if (mDrawables != null) {
                setIcon(mDrawables[mIndex]);
            }
            if (mLabels != null) {
                setLabel1(mLabels[mIndex]);
            }
            if (mLabels2 != null) {
                setLabel2(mLabels2[mIndex]);
            }
        }

        /**
         * Returns the current index.
         */
        public int getIndex() {
            return mIndex;
        }
    }

    /**
     * An action displaying icons for play and pause.
     */
    public static class PlayPauseAction extends android.support.v17.leanback.widget.PlaybackControlsRow.MultiAction {
        /**
         * Action index for the play icon.
         */
        public static int PLAY = 0;

        /**
         * Action index for the pause icon.
         */
        public static int PAUSE = 1;

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public PlayPauseAction(android.content.Context context) {
            super(R.id.lb_control_play_pause);
            android.graphics.drawable.Drawable[] drawables = new android.graphics.drawable.Drawable[2];
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.PlayPauseAction.PLAY] = android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_play);
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.PlayPauseAction.PAUSE] = android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_pause);
            setDrawables(drawables);
            java.lang.String[] labels = new java.lang.String[drawables.length];
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.PlayPauseAction.PLAY] = context.getString(R.string.lb_playback_controls_play);
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.PlayPauseAction.PAUSE] = context.getString(R.string.lb_playback_controls_pause);
            setLabels(labels);
            addKeyCode(android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
            addKeyCode(android.view.KeyEvent.KEYCODE_MEDIA_PLAY);
            addKeyCode(android.view.KeyEvent.KEYCODE_MEDIA_PAUSE);
        }
    }

    /**
     * An action displaying an icon for fast forward.
     */
    public static class FastForwardAction extends android.support.v17.leanback.widget.PlaybackControlsRow.MultiAction {
        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public FastForwardAction(android.content.Context context) {
            this(context, 1);
        }

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         * @param numSpeeds
         * 		Number of supported fast forward speeds.
         */
        public FastForwardAction(android.content.Context context, int numSpeeds) {
            super(R.id.lb_control_fast_forward);
            if (numSpeeds < 1) {
                throw new java.lang.IllegalArgumentException("numSpeeds must be > 0");
            }
            android.graphics.drawable.Drawable[] drawables = new android.graphics.drawable.Drawable[numSpeeds + 1];
            drawables[0] = android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_fast_forward);
            setDrawables(drawables);
            java.lang.String[] labels = new java.lang.String[getActionCount()];
            labels[0] = context.getString(R.string.lb_playback_controls_fast_forward);
            java.lang.String[] labels2 = new java.lang.String[getActionCount()];
            labels2[0] = labels[0];
            for (int i = 1; i <= numSpeeds; i++) {
                int multiplier = i + 1;
                labels[i] = context.getResources().getString(R.string.lb_control_display_fast_forward_multiplier, multiplier);
                labels2[i] = context.getResources().getString(R.string.lb_playback_controls_fast_forward_multiplier, multiplier);
            }
            setLabels(labels);
            setSecondaryLabels(labels2);
            addKeyCode(android.view.KeyEvent.KEYCODE_MEDIA_FAST_FORWARD);
        }
    }

    /**
     * An action displaying an icon for rewind.
     */
    public static class RewindAction extends android.support.v17.leanback.widget.PlaybackControlsRow.MultiAction {
        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public RewindAction(android.content.Context context) {
            this(context, 1);
        }

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         * @param numSpeeds
         * 		Number of supported fast forward speeds.
         */
        public RewindAction(android.content.Context context, int numSpeeds) {
            super(R.id.lb_control_fast_rewind);
            if (numSpeeds < 1) {
                throw new java.lang.IllegalArgumentException("numSpeeds must be > 0");
            }
            android.graphics.drawable.Drawable[] drawables = new android.graphics.drawable.Drawable[numSpeeds + 1];
            drawables[0] = android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_rewind);
            setDrawables(drawables);
            java.lang.String[] labels = new java.lang.String[getActionCount()];
            labels[0] = context.getString(R.string.lb_playback_controls_rewind);
            java.lang.String[] labels2 = new java.lang.String[getActionCount()];
            labels2[0] = labels[0];
            for (int i = 1; i <= numSpeeds; i++) {
                int multiplier = i + 1;
                labels[i] = labels[i] = context.getResources().getString(R.string.lb_control_display_rewind_multiplier, multiplier);
                labels2[i] = context.getResources().getString(R.string.lb_playback_controls_rewind_multiplier, multiplier);
            }
            setLabels(labels);
            setSecondaryLabels(labels2);
            addKeyCode(android.view.KeyEvent.KEYCODE_MEDIA_REWIND);
        }
    }

    /**
     * An action displaying an icon for skip next.
     */
    public static class SkipNextAction extends android.support.v17.leanback.widget.Action {
        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public SkipNextAction(android.content.Context context) {
            super(R.id.lb_control_skip_next);
            setIcon(android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_skip_next));
            setLabel1(context.getString(R.string.lb_playback_controls_skip_next));
            addKeyCode(android.view.KeyEvent.KEYCODE_MEDIA_NEXT);
        }
    }

    /**
     * An action displaying an icon for skip previous.
     */
    public static class SkipPreviousAction extends android.support.v17.leanback.widget.Action {
        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public SkipPreviousAction(android.content.Context context) {
            super(R.id.lb_control_skip_previous);
            setIcon(android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_skip_previous));
            setLabel1(context.getString(R.string.lb_playback_controls_skip_previous));
            addKeyCode(android.view.KeyEvent.KEYCODE_MEDIA_PREVIOUS);
        }
    }

    /**
     * An action displaying an icon for picture-in-picture.
     */
    public static class PictureInPictureAction extends android.support.v17.leanback.widget.Action {
        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public PictureInPictureAction(android.content.Context context) {
            super(R.id.lb_control_picture_in_picture);
            setIcon(android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_picture_in_picture));
            setLabel1(context.getString(R.string.lb_playback_controls_picture_in_picture));
            addKeyCode(android.view.KeyEvent.KEYCODE_WINDOW);
        }
    }

    /**
     * An action displaying an icon for "more actions".
     */
    public static class MoreActions extends android.support.v17.leanback.widget.Action {
        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public MoreActions(android.content.Context context) {
            super(R.id.lb_control_more_actions);
            setIcon(context.getResources().getDrawable(R.drawable.lb_ic_more));
            setLabel1(context.getString(R.string.lb_playback_controls_more_actions));
        }
    }

    /**
     * A base class for displaying a thumbs action.
     */
    public static abstract class ThumbsAction extends android.support.v17.leanback.widget.PlaybackControlsRow.MultiAction {
        /**
         * Action index for the solid thumb icon.
         */
        public static int SOLID = 0;

        /**
         * Action index for the outline thumb icon.
         */
        public static int OUTLINE = 1;

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public ThumbsAction(int id, android.content.Context context, int solidIconIndex, int outlineIconIndex) {
            super(id);
            android.graphics.drawable.Drawable[] drawables = new android.graphics.drawable.Drawable[2];
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.ThumbsAction.SOLID] = android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, solidIconIndex);
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.ThumbsAction.OUTLINE] = android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, outlineIconIndex);
            setDrawables(drawables);
        }
    }

    /**
     * An action displaying an icon for thumbs up.
     */
    public static class ThumbsUpAction extends android.support.v17.leanback.widget.PlaybackControlsRow.ThumbsAction {
        public ThumbsUpAction(android.content.Context context) {
            super(R.id.lb_control_thumbs_up, context, R.styleable.lbPlaybackControlsActionIcons_thumb_up, R.styleable.lbPlaybackControlsActionIcons_thumb_up_outline);
            java.lang.String[] labels = new java.lang.String[getActionCount()];
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.ThumbsAction.SOLID] = context.getString(R.string.lb_playback_controls_thumb_up);
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.ThumbsAction.OUTLINE] = context.getString(R.string.lb_playback_controls_thumb_up_outline);
            setLabels(labels);
        }
    }

    /**
     * An action displaying an icon for thumbs down.
     */
    public static class ThumbsDownAction extends android.support.v17.leanback.widget.PlaybackControlsRow.ThumbsAction {
        public ThumbsDownAction(android.content.Context context) {
            super(R.id.lb_control_thumbs_down, context, R.styleable.lbPlaybackControlsActionIcons_thumb_down, R.styleable.lbPlaybackControlsActionIcons_thumb_down_outline);
            java.lang.String[] labels = new java.lang.String[getActionCount()];
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.ThumbsAction.SOLID] = context.getString(R.string.lb_playback_controls_thumb_down);
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.ThumbsAction.OUTLINE] = context.getString(R.string.lb_playback_controls_thumb_down_outline);
            setLabels(labels);
        }
    }

    /**
     * An action for displaying three repeat states: none, one, or all.
     */
    public static class RepeatAction extends android.support.v17.leanback.widget.PlaybackControlsRow.MultiAction {
        /**
         * Action index for the repeat-none icon.
         */
        public static int NONE = 0;

        /**
         * Action index for the repeat-all icon.
         */
        public static int ALL = 1;

        /**
         * Action index for the repeat-one icon.
         */
        public static int ONE = 2;

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public RepeatAction(android.content.Context context) {
            this(context, android.support.v17.leanback.widget.PlaybackControlsRow.getIconHighlightColor(context));
        }

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources
         * @param highlightColor
         * 		Color to display the repeat-all and repeat0one icons.
         */
        public RepeatAction(android.content.Context context, int highlightColor) {
            this(context, highlightColor, highlightColor);
        }

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources
         * @param repeatAllColor
         * 		Color to display the repeat-all icon.
         * @param repeatOneColor
         * 		Color to display the repeat-one icon.
         */
        public RepeatAction(android.content.Context context, int repeatAllColor, int repeatOneColor) {
            super(R.id.lb_control_repeat);
            android.graphics.drawable.Drawable[] drawables = new android.graphics.drawable.Drawable[3];
            android.graphics.drawable.BitmapDrawable repeatDrawable = ((android.graphics.drawable.BitmapDrawable) (android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_repeat)));
            android.graphics.drawable.BitmapDrawable repeatOneDrawable = ((android.graphics.drawable.BitmapDrawable) (android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_repeat_one)));
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.RepeatAction.NONE] = repeatDrawable;
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.RepeatAction.ALL] = (repeatDrawable == null) ? null : new android.graphics.drawable.BitmapDrawable(context.getResources(), android.support.v17.leanback.widget.PlaybackControlsRow.createBitmap(repeatDrawable.getBitmap(), repeatAllColor));
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.RepeatAction.ONE] = (repeatOneDrawable == null) ? null : new android.graphics.drawable.BitmapDrawable(context.getResources(), android.support.v17.leanback.widget.PlaybackControlsRow.createBitmap(repeatOneDrawable.getBitmap(), repeatOneColor));
            setDrawables(drawables);
            java.lang.String[] labels = new java.lang.String[drawables.length];
            // Note, labels denote the action taken when clicked
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.RepeatAction.NONE] = context.getString(R.string.lb_playback_controls_repeat_all);
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.RepeatAction.ALL] = context.getString(R.string.lb_playback_controls_repeat_one);
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.RepeatAction.ONE] = context.getString(R.string.lb_playback_controls_repeat_none);
            setLabels(labels);
        }
    }

    /**
     * An action for displaying a shuffle icon.
     */
    public static class ShuffleAction extends android.support.v17.leanback.widget.PlaybackControlsRow.MultiAction {
        public static int OFF = 0;

        public static int ON = 1;

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public ShuffleAction(android.content.Context context) {
            this(context, android.support.v17.leanback.widget.PlaybackControlsRow.getIconHighlightColor(context));
        }

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         * @param highlightColor
         * 		Color for the highlighted icon state.
         */
        public ShuffleAction(android.content.Context context, int highlightColor) {
            super(R.id.lb_control_shuffle);
            android.graphics.drawable.BitmapDrawable uncoloredDrawable = ((android.graphics.drawable.BitmapDrawable) (android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_shuffle)));
            android.graphics.drawable.Drawable[] drawables = new android.graphics.drawable.Drawable[2];
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.ShuffleAction.OFF] = uncoloredDrawable;
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.ShuffleAction.ON] = new android.graphics.drawable.BitmapDrawable(context.getResources(), android.support.v17.leanback.widget.PlaybackControlsRow.createBitmap(uncoloredDrawable.getBitmap(), highlightColor));
            setDrawables(drawables);
            java.lang.String[] labels = new java.lang.String[drawables.length];
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.ShuffleAction.OFF] = context.getString(R.string.lb_playback_controls_shuffle_enable);
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.ShuffleAction.ON] = context.getString(R.string.lb_playback_controls_shuffle_disable);
            setLabels(labels);
        }
    }

    /**
     * An action for displaying a HQ (High Quality) icon.
     */
    public static class HighQualityAction extends android.support.v17.leanback.widget.PlaybackControlsRow.MultiAction {
        public static int OFF = 0;

        public static int ON = 1;

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public HighQualityAction(android.content.Context context) {
            this(context, android.support.v17.leanback.widget.PlaybackControlsRow.getIconHighlightColor(context));
        }

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         * @param highlightColor
         * 		Color for the highlighted icon state.
         */
        public HighQualityAction(android.content.Context context, int highlightColor) {
            super(R.id.lb_control_high_quality);
            android.graphics.drawable.BitmapDrawable uncoloredDrawable = ((android.graphics.drawable.BitmapDrawable) (android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_high_quality)));
            android.graphics.drawable.Drawable[] drawables = new android.graphics.drawable.Drawable[2];
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.HighQualityAction.OFF] = uncoloredDrawable;
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.HighQualityAction.ON] = new android.graphics.drawable.BitmapDrawable(context.getResources(), android.support.v17.leanback.widget.PlaybackControlsRow.createBitmap(uncoloredDrawable.getBitmap(), highlightColor));
            setDrawables(drawables);
            java.lang.String[] labels = new java.lang.String[drawables.length];
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.HighQualityAction.OFF] = context.getString(R.string.lb_playback_controls_high_quality_enable);
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.HighQualityAction.ON] = context.getString(R.string.lb_playback_controls_high_quality_disable);
            setLabels(labels);
        }
    }

    /**
     * An action for displaying a CC (Closed Captioning) icon.
     */
    public static class ClosedCaptioningAction extends android.support.v17.leanback.widget.PlaybackControlsRow.MultiAction {
        public static int OFF = 0;

        public static int ON = 1;

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         */
        public ClosedCaptioningAction(android.content.Context context) {
            this(context, android.support.v17.leanback.widget.PlaybackControlsRow.getIconHighlightColor(context));
        }

        /**
         * Constructor
         *
         * @param context
         * 		Context used for loading resources.
         * @param highlightColor
         * 		Color for the highlighted icon state.
         */
        public ClosedCaptioningAction(android.content.Context context, int highlightColor) {
            super(R.id.lb_control_closed_captioning);
            android.graphics.drawable.BitmapDrawable uncoloredDrawable = ((android.graphics.drawable.BitmapDrawable) (android.support.v17.leanback.widget.PlaybackControlsRow.getStyledDrawable(context, R.styleable.lbPlaybackControlsActionIcons_closed_captioning)));
            android.graphics.drawable.Drawable[] drawables = new android.graphics.drawable.Drawable[2];
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.ClosedCaptioningAction.OFF] = uncoloredDrawable;
            drawables[android.support.v17.leanback.widget.PlaybackControlsRow.ClosedCaptioningAction.ON] = new android.graphics.drawable.BitmapDrawable(context.getResources(), android.support.v17.leanback.widget.PlaybackControlsRow.createBitmap(uncoloredDrawable.getBitmap(), highlightColor));
            setDrawables(drawables);
            java.lang.String[] labels = new java.lang.String[drawables.length];
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.ClosedCaptioningAction.OFF] = context.getString(R.string.lb_playback_controls_closed_captioning_enable);
            labels[android.support.v17.leanback.widget.PlaybackControlsRow.ClosedCaptioningAction.ON] = context.getString(R.string.lb_playback_controls_closed_captioning_disable);
            setLabels(labels);
        }
    }

    static android.graphics.Bitmap createBitmap(android.graphics.Bitmap bitmap, int color) {
        android.graphics.Bitmap dst = bitmap.copy(bitmap.getConfig(), true);
        android.graphics.Canvas canvas = new android.graphics.Canvas(dst);
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setColorFilter(new android.graphics.PorterDuffColorFilter(color, android.graphics.PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return dst;
    }

    static int getIconHighlightColor(android.content.Context context) {
        android.util.TypedValue outValue = new android.util.TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.playbackControlsIconHighlightColor, outValue, true)) {
            return outValue.data;
        }
        return context.getResources().getColor(R.color.lb_playback_icon_highlight_no_theme);
    }

    static android.graphics.drawable.Drawable getStyledDrawable(android.content.Context context, int index) {
        android.util.TypedValue outValue = new android.util.TypedValue();
        if (!context.getTheme().resolveAttribute(R.attr.playbackControlsActionIcons, outValue, false)) {
            return null;
        }
        android.content.res.TypedArray array = context.getTheme().obtainStyledAttributes(outValue.data, R.styleable.lbPlaybackControlsActionIcons);
        android.graphics.drawable.Drawable drawable = array.getDrawable(index);
        array.recycle();
        return drawable;
    }

    private java.lang.Object mItem;

    private android.graphics.drawable.Drawable mImageDrawable;

    private android.support.v17.leanback.widget.ObjectAdapter mPrimaryActionsAdapter;

    private android.support.v17.leanback.widget.ObjectAdapter mSecondaryActionsAdapter;

    private int mTotalTimeMs;

    private int mCurrentTimeMs;

    private int mBufferedProgressMs;

    private android.support.v17.leanback.widget.PlaybackControlsRow.OnPlaybackStateChangedListener mListener;

    /**
     * Constructor for a PlaybackControlsRow that displays some details from
     * the given item.
     *
     * @param item
     * 		The main item for the row.
     */
    public PlaybackControlsRow(java.lang.Object item) {
        mItem = item;
    }

    /**
     * Constructor for a PlaybackControlsRow that has no item details.
     */
    public PlaybackControlsRow() {
    }

    /**
     * Returns the main item for the details page.
     */
    public final java.lang.Object getItem() {
        return mItem;
    }

    /**
     * Sets a {link @Drawable} image for this row.
     * <p>If set after the row has been bound to a view, the adapter must be notified that
     * this row has changed.</p>
     *
     * @param drawable
     * 		The drawable to set.
     */
    public final void setImageDrawable(android.graphics.drawable.Drawable drawable) {
        mImageDrawable = drawable;
    }

    /**
     * Sets a {@link Bitmap} for this row.
     * <p>If set after the row has been bound to a view, the adapter must be notified that
     * this row has changed.</p>
     *
     * @param context
     * 		The context to retrieve display metrics from.
     * @param bm
     * 		The bitmap to set.
     */
    public final void setImageBitmap(android.content.Context context, android.graphics.Bitmap bm) {
        mImageDrawable = new android.graphics.drawable.BitmapDrawable(context.getResources(), bm);
    }

    /**
     * Returns the image {@link Drawable} of this row.
     *
     * @return The overview's image drawable, or null if no drawable has been
    assigned.
     */
    public final android.graphics.drawable.Drawable getImageDrawable() {
        return mImageDrawable;
    }

    /**
     * Sets the primary actions {@link ObjectAdapter}.
     * <p>If set after the row has been bound to a view, the adapter must be notified that
     * this row has changed.</p>
     */
    public final void setPrimaryActionsAdapter(android.support.v17.leanback.widget.ObjectAdapter adapter) {
        mPrimaryActionsAdapter = adapter;
    }

    /**
     * Sets the secondary actions {@link ObjectAdapter}.
     * <p>If set after the row has been bound to a view, the adapter must be notified that
     * this row has changed.</p>
     */
    public final void setSecondaryActionsAdapter(android.support.v17.leanback.widget.ObjectAdapter adapter) {
        mSecondaryActionsAdapter = adapter;
    }

    /**
     * Returns the primary actions {@link ObjectAdapter}.
     */
    public final android.support.v17.leanback.widget.ObjectAdapter getPrimaryActionsAdapter() {
        return mPrimaryActionsAdapter;
    }

    /**
     * Returns the secondary actions {@link ObjectAdapter}.
     */
    public final android.support.v17.leanback.widget.ObjectAdapter getSecondaryActionsAdapter() {
        return mSecondaryActionsAdapter;
    }

    /**
     * Sets the total time in milliseconds for the playback controls row.
     * <p>If set after the row has been bound to a view, the adapter must be notified that
     * this row has changed.</p>
     */
    public void setTotalTime(int ms) {
        mTotalTimeMs = ms;
    }

    /**
     * Returns the total time in milliseconds for the playback controls row.
     */
    public int getTotalTime() {
        return mTotalTimeMs;
    }

    /**
     * Sets the current time in milliseconds for the playback controls row.
     * If this row is bound to a view, the view will automatically
     * be updated to reflect the new value.
     */
    public void setCurrentTime(int ms) {
        if (mCurrentTimeMs != ms) {
            mCurrentTimeMs = ms;
            currentTimeChanged();
        }
    }

    /**
     * Returns the current time in milliseconds for the playback controls row.
     */
    public int getCurrentTime() {
        return mCurrentTimeMs;
    }

    /**
     * Sets the buffered progress for the playback controls row.
     * If this row is bound to a view, the view will automatically
     * be updated to reflect the new value.
     */
    public void setBufferedProgress(int ms) {
        if (mBufferedProgressMs != ms) {
            mBufferedProgressMs = ms;
            bufferedProgressChanged();
        }
    }

    /**
     * Returns the buffered progress for the playback controls row.
     */
    public int getBufferedProgress() {
        return mBufferedProgressMs;
    }

    /**
     * Returns the Action associated with the given keycode, or null if no associated action exists.
     * Searches the primary adapter first, then the secondary adapter.
     */
    public android.support.v17.leanback.widget.Action getActionForKeyCode(int keyCode) {
        android.support.v17.leanback.widget.Action action = getActionForKeyCode(getPrimaryActionsAdapter(), keyCode);
        if (action != null) {
            return action;
        }
        return getActionForKeyCode(getSecondaryActionsAdapter(), keyCode);
    }

    /**
     * Returns the Action associated with the given keycode, or null if no associated action exists.
     */
    public android.support.v17.leanback.widget.Action getActionForKeyCode(android.support.v17.leanback.widget.ObjectAdapter adapter, int keyCode) {
        if ((adapter != mPrimaryActionsAdapter) && (adapter != mSecondaryActionsAdapter)) {
            throw new java.lang.IllegalArgumentException("Invalid adapter");
        }
        for (int i = 0; i < adapter.size(); i++) {
            android.support.v17.leanback.widget.Action action = ((android.support.v17.leanback.widget.Action) (adapter.get(i)));
            if (action.respondsToKeyCode(keyCode)) {
                return action;
            }
        }
        return null;
    }

    interface OnPlaybackStateChangedListener {
        public void onCurrentTimeChanged(int currentTimeMs);

        public void onBufferedProgressChanged(int bufferedProgressMs);
    }

    /**
     * Sets a listener to be called when the playback state changes.
     */
    void setOnPlaybackStateChangedListener(android.support.v17.leanback.widget.PlaybackControlsRow.OnPlaybackStateChangedListener listener) {
        mListener = listener;
    }

    /**
     * Returns the playback state listener.
     */
    android.support.v17.leanback.widget.PlaybackControlsRow.OnPlaybackStateChangedListener getOnPlaybackStateChangedListener() {
        return mListener;
    }

    private void currentTimeChanged() {
        if (mListener != null) {
            mListener.onCurrentTimeChanged(mCurrentTimeMs);
        }
    }

    private void bufferedProgressChanged() {
        if (mListener != null) {
            mListener.onBufferedProgressChanged(mBufferedProgressMs);
        }
    }
}

