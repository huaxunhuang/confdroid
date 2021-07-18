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
 * A PlaybackControlsRowPresenter renders a {@link PlaybackControlsRow} to display a
 * series of playback control buttons. Typically this row will be the first row in a fragment
 * such as the {@link android.support.v17.leanback.app.PlaybackOverlayFragment}.
 *
 * <p>The detailed description is rendered using a {@link Presenter} passed in
 * {@link #PlaybackControlsRowPresenter(Presenter)}.  Typically this will be an instance of
 * {@link AbstractDetailsDescriptionPresenter}.  The application can access the
 * detailed description ViewHolder from {@link ViewHolder#mDescriptionViewHolder}.
 * </p>
 */
public class PlaybackControlsRowPresenter extends android.support.v17.leanback.widget.RowPresenter {
    static class BoundData extends android.support.v17.leanback.widget.PlaybackControlsPresenter.BoundData {
        android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder mRowViewHolder;
    }

    /**
     * A ViewHolder for the PlaybackControlsRow.
     */
    public class ViewHolder extends android.support.v17.leanback.widget.RowPresenter.ViewHolder {
        public final android.support.v17.leanback.widget.Presenter.ViewHolder mDescriptionViewHolder;

        final android.view.ViewGroup mCard;

        final android.view.ViewGroup mCardRightPanel;

        final android.widget.ImageView mImageView;

        final android.view.ViewGroup mDescriptionDock;

        final android.view.ViewGroup mControlsDock;

        final android.view.ViewGroup mSecondaryControlsDock;

        final android.view.View mSpacer;

        final android.view.View mBottomSpacer;

        android.view.View mBgView;

        int mControlsDockMarginStart;

        int mControlsDockMarginEnd;

        android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder mControlsVh;

        android.support.v17.leanback.widget.Presenter.ViewHolder mSecondaryControlsVh;

        android.support.v17.leanback.widget.PlaybackControlsRowPresenter.BoundData mControlsBoundData = new android.support.v17.leanback.widget.PlaybackControlsRowPresenter.BoundData();

        android.support.v17.leanback.widget.PlaybackControlsRowPresenter.BoundData mSecondaryBoundData = new android.support.v17.leanback.widget.PlaybackControlsRowPresenter.BoundData();

        android.support.v17.leanback.widget.Presenter.ViewHolder mSelectedViewHolder;

        java.lang.Object mSelectedItem;

        final android.support.v17.leanback.widget.PlaybackControlsRow.OnPlaybackStateChangedListener mListener = new android.support.v17.leanback.widget.PlaybackControlsRow.OnPlaybackStateChangedListener() {
            @java.lang.Override
            public void onCurrentTimeChanged(int ms) {
                mPlaybackControlsPresenter.setCurrentTime(mControlsVh, ms);
            }

            @java.lang.Override
            public void onBufferedProgressChanged(int ms) {
                mPlaybackControlsPresenter.setSecondaryProgress(mControlsVh, ms);
            }
        };

        ViewHolder(android.view.View rootView, android.support.v17.leanback.widget.Presenter descriptionPresenter) {
            super(rootView);
            mCard = ((android.view.ViewGroup) (rootView.findViewById(R.id.controls_card)));
            mCardRightPanel = ((android.view.ViewGroup) (rootView.findViewById(R.id.controls_card_right_panel)));
            mImageView = ((android.widget.ImageView) (rootView.findViewById(R.id.image)));
            mDescriptionDock = ((android.view.ViewGroup) (rootView.findViewById(R.id.description_dock)));
            mControlsDock = ((android.view.ViewGroup) (rootView.findViewById(R.id.controls_dock)));
            mSecondaryControlsDock = ((android.view.ViewGroup) (rootView.findViewById(R.id.secondary_controls_dock)));
            mSpacer = rootView.findViewById(R.id.spacer);
            mBottomSpacer = rootView.findViewById(R.id.bottom_spacer);
            mDescriptionViewHolder = (descriptionPresenter == null) ? null : descriptionPresenter.onCreateViewHolder(mDescriptionDock);
            if (mDescriptionViewHolder != null) {
                mDescriptionDock.addView(mDescriptionViewHolder.view);
            }
        }

        void dispatchItemSelection() {
            if (!isSelected()) {
                return;
            }
            if (mSelectedViewHolder == null) {
                if (getOnItemViewSelectedListener() != null) {
                    getOnItemViewSelectedListener().onItemSelected(null, null, this, getRow());
                }
            } else {
                if (getOnItemViewSelectedListener() != null) {
                    getOnItemViewSelectedListener().onItemSelected(mSelectedViewHolder, mSelectedItem, this, getRow());
                }
            }
        }

        android.support.v17.leanback.widget.Presenter getPresenter(boolean primary) {
            android.support.v17.leanback.widget.ObjectAdapter adapter = (primary) ? ((android.support.v17.leanback.widget.PlaybackControlsRow) (getRow())).getPrimaryActionsAdapter() : ((android.support.v17.leanback.widget.PlaybackControlsRow) (getRow())).getSecondaryActionsAdapter();
            if (adapter == null) {
                return null;
            }
            if (adapter.getPresenterSelector() instanceof android.support.v17.leanback.widget.ControlButtonPresenterSelector) {
                android.support.v17.leanback.widget.ControlButtonPresenterSelector selector = ((android.support.v17.leanback.widget.ControlButtonPresenterSelector) (adapter.getPresenterSelector()));
                return primary ? selector.getPrimaryPresenter() : selector.getSecondaryPresenter();
            }
            return adapter.getPresenter(adapter.size() > 0 ? adapter.get(0) : null);
        }

        void setOutline(android.view.View view) {
            if (mBgView != null) {
                android.support.v17.leanback.widget.RoundedRectHelper.getInstance().setClipToRoundedOutline(mBgView, false);
                android.support.v17.leanback.widget.ShadowHelper.getInstance().setZ(mBgView, 0);
            }
            mBgView = view;
            android.support.v17.leanback.widget.RoundedRectHelper.getInstance().setClipToRoundedOutline(view, true);
            if (android.support.v17.leanback.widget.PlaybackControlsRowPresenter.sShadowZ == 0) {
                android.support.v17.leanback.widget.PlaybackControlsRowPresenter.sShadowZ = view.getResources().getDimensionPixelSize(R.dimen.lb_playback_controls_z);
            }
            android.support.v17.leanback.widget.ShadowHelper.getInstance().setZ(view, android.support.v17.leanback.widget.PlaybackControlsRowPresenter.sShadowZ);
        }
    }

    private int mBackgroundColor = android.graphics.Color.TRANSPARENT;

    private boolean mBackgroundColorSet;

    private int mProgressColor = android.graphics.Color.TRANSPARENT;

    private boolean mProgressColorSet;

    private boolean mSecondaryActionsHidden;

    private android.support.v17.leanback.widget.Presenter mDescriptionPresenter;

    android.support.v17.leanback.widget.PlaybackControlsPresenter mPlaybackControlsPresenter;

    private android.support.v17.leanback.widget.ControlBarPresenter mSecondaryControlsPresenter;

    android.support.v17.leanback.widget.OnActionClickedListener mOnActionClickedListener;

    static float sShadowZ;

    private final android.support.v17.leanback.widget.ControlBarPresenter.OnControlSelectedListener mOnControlSelectedListener = new android.support.v17.leanback.widget.ControlBarPresenter.OnControlSelectedListener() {
        @java.lang.Override
        public void onControlSelected(android.support.v17.leanback.widget.Presenter.ViewHolder itemViewHolder, java.lang.Object item, android.support.v17.leanback.widget.ControlBarPresenter.BoundData data) {
            android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.PlaybackControlsRowPresenter.BoundData) (data)).mRowViewHolder;
            if ((vh.mSelectedViewHolder != itemViewHolder) || (vh.mSelectedItem != item)) {
                vh.mSelectedViewHolder = itemViewHolder;
                vh.mSelectedItem = item;
                vh.dispatchItemSelection();
            }
        }
    };

    private final android.support.v17.leanback.widget.ControlBarPresenter.OnControlClickedListener mOnControlClickedListener = new android.support.v17.leanback.widget.ControlBarPresenter.OnControlClickedListener() {
        @java.lang.Override
        public void onControlClicked(android.support.v17.leanback.widget.Presenter.ViewHolder itemViewHolder, java.lang.Object item, android.support.v17.leanback.widget.ControlBarPresenter.BoundData data) {
            android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.PlaybackControlsRowPresenter.BoundData) (data)).mRowViewHolder;
            if (vh.getOnItemViewClickedListener() != null) {
                vh.getOnItemViewClickedListener().onItemClicked(itemViewHolder, item, vh, vh.getRow());
            }
            if ((mOnActionClickedListener != null) && (item instanceof android.support.v17.leanback.widget.Action)) {
                mOnActionClickedListener.onActionClicked(((android.support.v17.leanback.widget.Action) (item)));
            }
        }
    };

    /**
     * Constructor for a PlaybackControlsRowPresenter.
     *
     * @param descriptionPresenter
     * 		Presenter for displaying item details.
     */
    public PlaybackControlsRowPresenter(android.support.v17.leanback.widget.Presenter descriptionPresenter) {
        setHeaderPresenter(null);
        setSelectEffectEnabled(false);
        mDescriptionPresenter = descriptionPresenter;
        mPlaybackControlsPresenter = new android.support.v17.leanback.widget.PlaybackControlsPresenter(R.layout.lb_playback_controls);
        mSecondaryControlsPresenter = new android.support.v17.leanback.widget.ControlBarPresenter(R.layout.lb_control_bar);
        mPlaybackControlsPresenter.setOnControlSelectedListener(mOnControlSelectedListener);
        mSecondaryControlsPresenter.setOnControlSelectedListener(mOnControlSelectedListener);
        mPlaybackControlsPresenter.setOnControlClickedListener(mOnControlClickedListener);
        mSecondaryControlsPresenter.setOnControlClickedListener(mOnControlClickedListener);
    }

    /**
     * Constructor for a PlaybackControlsRowPresenter.
     */
    public PlaybackControlsRowPresenter() {
        this(null);
    }

    /**
     * Sets the listener for {@link Action} click events.
     */
    public void setOnActionClickedListener(android.support.v17.leanback.widget.OnActionClickedListener listener) {
        mOnActionClickedListener = listener;
    }

    /**
     * Returns the listener for {@link Action} click events.
     */
    public android.support.v17.leanback.widget.OnActionClickedListener getOnActionClickedListener() {
        return mOnActionClickedListener;
    }

    /**
     * Sets the background color.  If not set, a default from the theme will be used.
     */
    public void setBackgroundColor(@android.support.annotation.ColorInt
    int color) {
        mBackgroundColor = color;
        mBackgroundColorSet = true;
    }

    /**
     * Returns the background color.  If no background color was set, transparent
     * is returned.
     */
    @android.support.annotation.ColorInt
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * Sets the primary color for the progress bar.  If not set, a default from
     * the theme will be used.
     */
    public void setProgressColor(@android.support.annotation.ColorInt
    int color) {
        mProgressColor = color;
        mProgressColorSet = true;
    }

    /**
     * Returns the primary color for the progress bar.  If no color was set, transparent
     * is returned.
     */
    @android.support.annotation.ColorInt
    public int getProgressColor() {
        return mProgressColor;
    }

    /**
     * Sets the secondary actions to be hidden behind a "more actions" button.
     * When "more actions" is selected, the primary actions are replaced with
     * the secondary actions.
     */
    public void setSecondaryActionsHidden(boolean hidden) {
        mSecondaryActionsHidden = hidden;
    }

    /**
     * Returns true if secondary actions are hidden.
     */
    public boolean areSecondaryActionsHidden() {
        return mSecondaryActionsHidden;
    }

    /**
     * Shows or hides space at the bottom of the playback controls row.
     * This allows the row to hug the bottom of the display when no
     * other rows are present.
     */
    public void showBottomSpace(android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder vh, boolean show) {
        vh.mBottomSpacer.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    /**
     * Displays the primary actions.  This will override the user having selected "more actions"
     * to display the secondary actions; see {@link #setSecondaryActionsHidden(boolean)}.
     */
    public void showPrimaryActions(android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder vh) {
        mPlaybackControlsPresenter.showPrimaryActions(vh.mControlsVh);
        mPlaybackControlsPresenter.resetFocus(vh.mControlsVh);
    }

    private int getDefaultBackgroundColor(android.content.Context context) {
        android.util.TypedValue outValue = new android.util.TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.defaultBrandColor, outValue, true)) {
            return context.getResources().getColor(outValue.resourceId);
        }
        return context.getResources().getColor(R.color.lb_default_brand_color);
    }

    private int getDefaultProgressColor(android.content.Context context) {
        android.util.TypedValue outValue = new android.util.TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.playbackProgressPrimaryColor, outValue, true)) {
            return context.getResources().getColor(outValue.resourceId);
        }
        return context.getResources().getColor(R.color.lb_playback_progress_color_no_theme);
    }

    @java.lang.Override
    protected android.support.v17.leanback.widget.RowPresenter.ViewHolder createRowViewHolder(android.view.ViewGroup parent) {
        android.view.View v = android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.lb_playback_controls_row, parent, false);
        android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder vh = new android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder(v, mDescriptionPresenter);
        initRow(vh);
        return vh;
    }

    private void initRow(final android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder vh) {
        android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (vh.mControlsDock.getLayoutParams()));
        vh.mControlsDockMarginStart = lp.getMarginStart();
        vh.mControlsDockMarginEnd = lp.getMarginEnd();
        vh.mControlsVh = ((android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder) (mPlaybackControlsPresenter.onCreateViewHolder(vh.mControlsDock)));
        mPlaybackControlsPresenter.setProgressColor(vh.mControlsVh, mProgressColorSet ? mProgressColor : getDefaultProgressColor(vh.mControlsDock.getContext()));
        mPlaybackControlsPresenter.setBackgroundColor(vh.mControlsVh, mBackgroundColorSet ? mBackgroundColor : getDefaultBackgroundColor(vh.view.getContext()));
        vh.mControlsDock.addView(vh.mControlsVh.view);
        vh.mSecondaryControlsVh = mSecondaryControlsPresenter.onCreateViewHolder(vh.mSecondaryControlsDock);
        if (!mSecondaryActionsHidden) {
            vh.mSecondaryControlsDock.addView(vh.mSecondaryControlsVh.view);
        }
        ((android.support.v17.leanback.widget.PlaybackControlsRowView) (vh.view)).setOnUnhandledKeyListener(new android.support.v17.leanback.widget.PlaybackControlsRowView.OnUnhandledKeyListener() {
            @java.lang.Override
            public boolean onUnhandledKey(android.view.KeyEvent event) {
                if (vh.getOnKeyListener() != null) {
                    if (vh.getOnKeyListener().onKey(vh.view, event.getKeyCode(), event)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @java.lang.Override
    protected void onBindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, java.lang.Object item) {
        super.onBindRowViewHolder(holder, item);
        android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder) (holder));
        android.support.v17.leanback.widget.PlaybackControlsRow row = ((android.support.v17.leanback.widget.PlaybackControlsRow) (vh.getRow()));
        mPlaybackControlsPresenter.enableSecondaryActions(mSecondaryActionsHidden);
        if (row.getItem() == null) {
            vh.mDescriptionDock.setVisibility(android.view.View.GONE);
            vh.mSpacer.setVisibility(android.view.View.GONE);
        } else {
            vh.mDescriptionDock.setVisibility(android.view.View.VISIBLE);
            if (vh.mDescriptionViewHolder != null) {
                mDescriptionPresenter.onBindViewHolder(vh.mDescriptionViewHolder, row.getItem());
            }
            vh.mSpacer.setVisibility(android.view.View.VISIBLE);
        }
        if ((row.getImageDrawable() == null) || (row.getItem() == null)) {
            vh.mImageView.setImageDrawable(null);
            updateCardLayout(vh, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            vh.mImageView.setImageDrawable(row.getImageDrawable());
            updateCardLayout(vh, vh.mImageView.getLayoutParams().height);
        }
        vh.mControlsBoundData.adapter = row.getPrimaryActionsAdapter();
        vh.mControlsBoundData.secondaryActionsAdapter = row.getSecondaryActionsAdapter();
        vh.mControlsBoundData.presenter = vh.getPresenter(true);
        vh.mControlsBoundData.mRowViewHolder = vh;
        mPlaybackControlsPresenter.onBindViewHolder(vh.mControlsVh, vh.mControlsBoundData);
        vh.mSecondaryBoundData.adapter = row.getSecondaryActionsAdapter();
        vh.mSecondaryBoundData.presenter = vh.getPresenter(false);
        vh.mSecondaryBoundData.mRowViewHolder = vh;
        mSecondaryControlsPresenter.onBindViewHolder(vh.mSecondaryControlsVh, vh.mSecondaryBoundData);
        mPlaybackControlsPresenter.setTotalTime(vh.mControlsVh, row.getTotalTime());
        mPlaybackControlsPresenter.setCurrentTime(vh.mControlsVh, row.getCurrentTime());
        mPlaybackControlsPresenter.setSecondaryProgress(vh.mControlsVh, row.getBufferedProgress());
        row.setOnPlaybackStateChangedListener(vh.mListener);
    }

    private void updateCardLayout(android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder vh, int height) {
        android.view.ViewGroup.LayoutParams lp = vh.mCardRightPanel.getLayoutParams();
        lp.height = height;
        vh.mCardRightPanel.setLayoutParams(lp);
        android.view.ViewGroup.MarginLayoutParams mlp = ((android.view.ViewGroup.MarginLayoutParams) (vh.mControlsDock.getLayoutParams()));
        android.widget.LinearLayout.LayoutParams llp = ((android.widget.LinearLayout.LayoutParams) (vh.mDescriptionDock.getLayoutParams()));
        if (height == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {
            llp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
            mlp.setMarginStart(0);
            mlp.setMarginEnd(0);
            vh.mCard.setBackground(null);
            vh.setOutline(vh.mControlsDock);
            mPlaybackControlsPresenter.enableTimeMargins(vh.mControlsVh, true);
        } else {
            llp.height = 0;
            llp.weight = 1;
            mlp.setMarginStart(vh.mControlsDockMarginStart);
            mlp.setMarginEnd(vh.mControlsDockMarginEnd);
            vh.mCard.setBackgroundColor(mBackgroundColorSet ? mBackgroundColor : getDefaultBackgroundColor(vh.mCard.getContext()));
            vh.setOutline(vh.mCard);
            mPlaybackControlsPresenter.enableTimeMargins(vh.mControlsVh, false);
        }
        vh.mDescriptionDock.setLayoutParams(llp);
        vh.mControlsDock.setLayoutParams(mlp);
    }

    @java.lang.Override
    protected void onUnbindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder) {
        android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder) (holder));
        android.support.v17.leanback.widget.PlaybackControlsRow row = ((android.support.v17.leanback.widget.PlaybackControlsRow) (vh.getRow()));
        if (vh.mDescriptionViewHolder != null) {
            mDescriptionPresenter.onUnbindViewHolder(vh.mDescriptionViewHolder);
        }
        mPlaybackControlsPresenter.onUnbindViewHolder(vh.mControlsVh);
        mSecondaryControlsPresenter.onUnbindViewHolder(vh.mSecondaryControlsVh);
        row.setOnPlaybackStateChangedListener(null);
        super.onUnbindRowViewHolder(holder);
    }

    @java.lang.Override
    protected void onRowViewSelected(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh, boolean selected) {
        super.onRowViewSelected(vh, selected);
        if (selected) {
            ((android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder) (vh)).dispatchItemSelection();
        }
    }

    @java.lang.Override
    protected void onRowViewAttachedToWindow(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        super.onRowViewAttachedToWindow(vh);
        if (mDescriptionPresenter != null) {
            mDescriptionPresenter.onViewAttachedToWindow(((android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder) (vh)).mDescriptionViewHolder);
        }
    }

    @java.lang.Override
    protected void onRowViewDetachedFromWindow(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        super.onRowViewDetachedFromWindow(vh);
        if (mDescriptionPresenter != null) {
            mDescriptionPresenter.onViewDetachedFromWindow(((android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder) (vh)).mDescriptionViewHolder);
        }
    }
}

