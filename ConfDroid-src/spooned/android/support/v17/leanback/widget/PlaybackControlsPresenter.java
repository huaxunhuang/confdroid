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
 * A presenter for a control bar that supports "more actions",
 * and toggling the set of controls between primary and secondary
 * sets of {@link Actions}.
 */
class PlaybackControlsPresenter extends android.support.v17.leanback.widget.ControlBarPresenter {
    /**
     * The data type expected by this presenter.
     */
    static class BoundData extends android.support.v17.leanback.widget.ControlBarPresenter.BoundData {
        /**
         * The adapter containing secondary actions.
         */
        android.support.v17.leanback.widget.ObjectAdapter secondaryActionsAdapter;
    }

    class ViewHolder extends android.support.v17.leanback.widget.ControlBarPresenter.ViewHolder {
        android.support.v17.leanback.widget.ObjectAdapter mMoreActionsAdapter;

        android.support.v17.leanback.widget.ObjectAdapter.DataObserver mMoreActionsObserver;

        final android.widget.FrameLayout mMoreActionsDock;

        android.support.v17.leanback.widget.Presenter.ViewHolder mMoreActionsViewHolder;

        boolean mMoreActionsShowing;

        final android.widget.TextView mCurrentTime;

        final android.widget.TextView mTotalTime;

        final android.widget.ProgressBar mProgressBar;

        int mCurrentTimeInSeconds = -1;

        java.lang.StringBuilder mTotalTimeStringBuilder = new java.lang.StringBuilder();

        java.lang.StringBuilder mCurrentTimeStringBuilder = new java.lang.StringBuilder();

        int mCurrentTimeMarginStart;

        int mTotalTimeMarginEnd;

        final android.support.v17.leanback.widget.PersistentFocusWrapper mControlsFocusWrapper;

        ViewHolder(android.view.View rootView) {
            super(rootView);
            mMoreActionsDock = ((android.widget.FrameLayout) (rootView.findViewById(R.id.more_actions_dock)));
            mCurrentTime = ((android.widget.TextView) (rootView.findViewById(R.id.current_time)));
            mTotalTime = ((android.widget.TextView) (rootView.findViewById(R.id.total_time)));
            mProgressBar = ((android.widget.ProgressBar) (rootView.findViewById(R.id.playback_progress)));
            mMoreActionsObserver = new android.support.v17.leanback.widget.ObjectAdapter.DataObserver() {
                @java.lang.Override
                public void onChanged() {
                    if (mMoreActionsShowing) {
                        showControls(mPresenter);
                    }
                }

                @java.lang.Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    if (mMoreActionsShowing) {
                        for (int i = 0; i < itemCount; i++) {
                            bindControlToAction(positionStart + i, mPresenter);
                        }
                    }
                }
            };
            mCurrentTimeMarginStart = ((android.view.ViewGroup.MarginLayoutParams) (mCurrentTime.getLayoutParams())).getMarginStart();
            mTotalTimeMarginEnd = ((android.view.ViewGroup.MarginLayoutParams) (mTotalTime.getLayoutParams())).getMarginEnd();
            mControlsFocusWrapper = ((android.support.v17.leanback.widget.PersistentFocusWrapper) (mControlBar.getParent()));
        }

        void showMoreActions(boolean show) {
            if (show) {
                if (mMoreActionsViewHolder == null) {
                    android.support.v17.leanback.widget.Action action = new android.support.v17.leanback.widget.PlaybackControlsRow.MoreActions(mMoreActionsDock.getContext());
                    mMoreActionsViewHolder = mPresenter.onCreateViewHolder(mMoreActionsDock);
                    mPresenter.onBindViewHolder(mMoreActionsViewHolder, action);
                    mPresenter.setOnClickListener(mMoreActionsViewHolder, new android.view.View.OnClickListener() {
                        @java.lang.Override
                        public void onClick(android.view.View v) {
                            toggleMoreActions();
                        }
                    });
                }
                if (mMoreActionsViewHolder.view.getParent() == null) {
                    mMoreActionsDock.addView(mMoreActionsViewHolder.view);
                }
            } else
                if ((mMoreActionsViewHolder != null) && (mMoreActionsViewHolder.view.getParent() != null)) {
                    mMoreActionsDock.removeView(mMoreActionsViewHolder.view);
                }

        }

        void toggleMoreActions() {
            mMoreActionsShowing = !mMoreActionsShowing;
            showControls(mPresenter);
        }

        @java.lang.Override
        android.support.v17.leanback.widget.ObjectAdapter getDisplayedAdapter() {
            return mMoreActionsShowing ? mMoreActionsAdapter : mAdapter;
        }

        @java.lang.Override
        int getChildMarginFromCenter(android.content.Context context, int numControls) {
            int margin = getControlIconWidth(context);
            if (numControls < 4) {
                margin += getChildMarginBiggest(context);
            } else
                if (numControls < 6) {
                    margin += getChildMarginBigger(context);
                } else {
                    margin += getChildMarginDefault(context);
                }

            return margin;
        }

        void setTotalTime(int totalTimeMs) {
            if (totalTimeMs <= 0) {
                mTotalTime.setVisibility(android.view.View.GONE);
                mProgressBar.setVisibility(android.view.View.GONE);
            } else {
                mTotalTime.setVisibility(android.view.View.VISIBLE);
                mProgressBar.setVisibility(android.view.View.VISIBLE);
                android.support.v17.leanback.widget.PlaybackControlsPresenter.formatTime(totalTimeMs / 1000, mTotalTimeStringBuilder);
                mTotalTime.setText(mTotalTimeStringBuilder.toString());
                mProgressBar.setMax(totalTimeMs);
            }
        }

        int getTotalTime() {
            return mProgressBar.getMax();
        }

        void setCurrentTime(int currentTimeMs) {
            int seconds = currentTimeMs / 1000;
            if (seconds != mCurrentTimeInSeconds) {
                mCurrentTimeInSeconds = seconds;
                android.support.v17.leanback.widget.PlaybackControlsPresenter.formatTime(mCurrentTimeInSeconds, mCurrentTimeStringBuilder);
                mCurrentTime.setText(mCurrentTimeStringBuilder.toString());
            }
            mProgressBar.setProgress(currentTimeMs);
        }

        int getCurrentTime() {
            return mProgressBar.getProgress();
        }

        void setSecondaryProgress(int progressMs) {
            mProgressBar.setSecondaryProgress(progressMs);
        }

        int getSecondaryProgress() {
            return mProgressBar.getSecondaryProgress();
        }
    }

    static void formatTime(int seconds, java.lang.StringBuilder sb) {
        int minutes = seconds / 60;
        int hours = minutes / 60;
        seconds -= minutes * 60;
        minutes -= hours * 60;
        sb.setLength(0);
        if (hours > 0) {
            sb.append(hours).append(':');
            if (minutes < 10) {
                sb.append('0');
            }
        }
        sb.append(minutes).append(':');
        if (seconds < 10) {
            sb.append('0');
        }
        sb.append(seconds);
    }

    private boolean mMoreActionsEnabled = true;

    private static int sChildMarginBigger;

    private static int sChildMarginBiggest;

    /**
     * Constructor for a PlaybackControlsRowPresenter.
     *
     * @param layoutResourceId
     * 		The resource id of the layout for this presenter.
     */
    public PlaybackControlsPresenter(int layoutResourceId) {
        super(layoutResourceId);
    }

    /**
     * Enables the display of secondary actions.
     * A "more actions" button will be displayed.  When "more actions" is selected,
     * the primary actions are replaced with the secondary actions.
     */
    public void enableSecondaryActions(boolean enable) {
        mMoreActionsEnabled = enable;
    }

    /**
     * Returns true if secondary actions are enabled.
     */
    public boolean areMoreActionsEnabled() {
        return mMoreActionsEnabled;
    }

    public void setProgressColor(android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh, @android.support.annotation.ColorInt
    int color) {
        android.graphics.drawable.Drawable drawable = new android.graphics.drawable.ClipDrawable(new android.graphics.drawable.ColorDrawable(color), android.view.Gravity.LEFT, android.graphics.drawable.ClipDrawable.HORIZONTAL);
        ((android.graphics.drawable.LayerDrawable) (vh.mProgressBar.getProgressDrawable())).setDrawableByLayerId(android.R.id.progress, drawable);
    }

    public void setTotalTime(android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh, int ms) {
        vh.setTotalTime(ms);
    }

    public int getTotalTime(android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh) {
        return vh.getTotalTime();
    }

    public void setCurrentTime(android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh, int ms) {
        vh.setCurrentTime(ms);
    }

    public int getCurrentTime(android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh) {
        return vh.getCurrentTime();
    }

    public void setSecondaryProgress(android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh, int progressMs) {
        vh.setSecondaryProgress(progressMs);
    }

    public int getSecondaryProgress(android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh) {
        return vh.getSecondaryProgress();
    }

    public void showPrimaryActions(android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh) {
        if (vh.mMoreActionsShowing) {
            vh.toggleMoreActions();
        }
    }

    public void resetFocus(android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh) {
        vh.mControlsFocusWrapper.clearSelection();
        vh.mControlBar.requestFocus();
    }

    public void enableTimeMargins(android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh, boolean enable) {
        android.view.ViewGroup.MarginLayoutParams lp;
        lp = ((android.view.ViewGroup.MarginLayoutParams) (vh.mCurrentTime.getLayoutParams()));
        lp.setMarginStart(enable ? vh.mCurrentTimeMarginStart : 0);
        vh.mCurrentTime.setLayoutParams(lp);
        lp = ((android.view.ViewGroup.MarginLayoutParams) (vh.mTotalTime.getLayoutParams()));
        lp.setMarginEnd(enable ? vh.mTotalTimeMarginEnd : 0);
        vh.mTotalTime.setLayoutParams(lp);
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
        android.view.View v = android.view.LayoutInflater.from(parent.getContext()).inflate(getLayoutResourceId(), parent, false);
        return new android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder(v);
    }

    @java.lang.Override
    public void onBindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder holder, java.lang.Object item) {
        android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder) (holder));
        android.support.v17.leanback.widget.PlaybackControlsPresenter.BoundData data = ((android.support.v17.leanback.widget.PlaybackControlsPresenter.BoundData) (item));
        // If binding to a new adapter, display primary actions.
        if (vh.mMoreActionsAdapter != data.secondaryActionsAdapter) {
            vh.mMoreActionsAdapter = data.secondaryActionsAdapter;
            vh.mMoreActionsAdapter.registerObserver(vh.mMoreActionsObserver);
            vh.mMoreActionsShowing = false;
        }
        super.onBindViewHolder(holder, item);
        vh.showMoreActions(mMoreActionsEnabled);
    }

    @java.lang.Override
    public void onUnbindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder holder) {
        super.onUnbindViewHolder(holder);
        android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.PlaybackControlsPresenter.ViewHolder) (holder));
        if (vh.mMoreActionsAdapter != null) {
            vh.mMoreActionsAdapter.unregisterObserver(vh.mMoreActionsObserver);
            vh.mMoreActionsAdapter = null;
        }
    }

    int getChildMarginBigger(android.content.Context context) {
        if (android.support.v17.leanback.widget.PlaybackControlsPresenter.sChildMarginBigger == 0) {
            android.support.v17.leanback.widget.PlaybackControlsPresenter.sChildMarginBigger = context.getResources().getDimensionPixelSize(R.dimen.lb_playback_controls_child_margin_bigger);
        }
        return android.support.v17.leanback.widget.PlaybackControlsPresenter.sChildMarginBigger;
    }

    int getChildMarginBiggest(android.content.Context context) {
        if (android.support.v17.leanback.widget.PlaybackControlsPresenter.sChildMarginBiggest == 0) {
            android.support.v17.leanback.widget.PlaybackControlsPresenter.sChildMarginBiggest = context.getResources().getDimensionPixelSize(R.dimen.lb_playback_controls_child_margin_biggest);
        }
        return android.support.v17.leanback.widget.PlaybackControlsPresenter.sChildMarginBiggest;
    }
}

