/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * Abstract {@link Presenter} class for rendering media items in a playlist format.
 * Media item data provided for this presenter can implement the interface
 * {@link MultiActionsProvider}, if the media rows wish to contain custom actions.
 * Media items in the playlist are arranged as a vertical list with each row holding each media
 * item's details provided by the user of this class and a set of optional custom actions.
 * Each media item's details and actions are separately focusable.
 * The appearance of each one of the media row components can be controlled through setting
 * theme's attributes.
 * Each media item row provides a view flipper for switching between different views depending on
 * the playback state.
 * A default layout is provided by this presenter for rendering different playback states, or a
 * custom layout can be provided by the user by overriding the
 * playbackMediaItemNumberViewFlipperLayout attribute in the currently specified theme.
 * Subclasses should also override {@link #getMediaPlayState(Object)} to provide the current play
 * state of their media item model in case they wish to use different views depending on the
 * playback state.
 * The presenter can optionally provide line separators between media rows by setting
 * {@link #setHasMediaRowSeparator(boolean)} to true.
 * <p>
 *     Subclasses must override {@link #onBindMediaDetails} to implement their media item model
 *     data binding to each row view.
 * </p>
 * <p>
 *     The {@link OnItemViewClickedListener} and {@link OnItemViewSelectedListener}
 *     can be used in the same fashion to handle selection or click events on either of
 *     media details or each individual action views.
 * </p>
 * <p>
 *     {@link AbstractMediaListHeaderPresenter} can be used in conjunction with this presenter in
 *     order to display a playlist with a header view.
 * </p>
 */
public abstract class AbstractMediaItemPresenter extends android.support.v17.leanback.widget.RowPresenter {
    /**
     * Different playback states of a media item
     */
    /**
     * Indicating that the media item is currently neither playing nor paused.
     */
    public static final int PLAY_STATE_INITIAL = 0;

    /**
     * Indicating that the media item is currently paused.
     */
    public static final int PLAY_STATE_PAUSED = 1;

    /**
     * Indicating that the media item is currently playing
     */
    public static final int PLAY_STATE_PLAYING = 2;

    static final android.graphics.Rect sTempRect = new android.graphics.Rect();

    private int mBackgroundColor = android.graphics.Color.TRANSPARENT;

    private boolean mBackgroundColorSet;

    private boolean mMediaRowSeparator;

    private int mThemeId;

    private android.support.v17.leanback.widget.Presenter mMediaItemActionPresenter = new android.support.v17.leanback.widget.MediaItemActionPresenter();

    /**
     * Constructor used for creating an abstract media item presenter.
     */
    public AbstractMediaItemPresenter() {
        this(0);
    }

    /**
     * Constructor used for creating an abstract media item presenter.
     *
     * @param themeId
     * 		The resource id of the theme that defines attributes controlling the
     * 		appearance of different widgets in a media item row.
     */
    public AbstractMediaItemPresenter(int themeId) {
        mThemeId = themeId;
        setHeaderPresenter(null);
    }

    /**
     * Sets the theme used to style a media item row components.
     *
     * @param themeId
     * 		The resource id of the theme that defines attributes controlling the
     * 		appearance of different widgets in a media item row.
     */
    public void setThemeId(int themeId) {
        mThemeId = themeId;
    }

    /**
     * Return The resource id of the theme that defines attributes controlling the appearance of
     * different widgets in a media item row.
     *
     * @return The resource id of the theme that defines attributes controlling the appearance of
    different widgets in a media item row.
     */
    public int getThemeId() {
        return mThemeId;
    }

    /**
     * Sets the action presenter rendering each optional custom action within each media item row.
     *
     * @param actionPresenter
     * 		the presenter to be used for rendering a media item row actions.
     */
    public void setActionPresenter(android.support.v17.leanback.widget.Presenter actionPresenter) {
        mMediaItemActionPresenter = actionPresenter;
    }

    /**
     * Return the presenter used to render a media item row actions.
     *
     * @return the presenter used to render a media item row actions.
     */
    public android.support.v17.leanback.widget.Presenter getActionPresenter() {
        return mMediaItemActionPresenter;
    }

    /**
     * The ViewHolder for the {@link AbstractMediaItemPresenter}. It references different views
     * that place different meta-data corresponding to a media item details, actions, selector,
     * listeners, and presenters,
     */
    public static class ViewHolder extends android.support.v17.leanback.widget.RowPresenter.ViewHolder {
        final android.view.View mMediaRowView;

        final android.view.View mSelectorView;

        private final android.view.View mMediaItemDetailsView;

        final android.widget.ViewFlipper mMediaItemNumberViewFlipper;

        final android.widget.TextView mMediaItemNumberView;

        final android.view.View mMediaItemPausedView;

        final android.view.View mMediaItemPlayingView;

        private final android.widget.TextView mMediaItemNameView;

        private final android.widget.TextView mMediaItemDurationView;

        private final android.view.View mMediaItemRowSeparator;

        private final android.view.ViewGroup mMediaItemActionsContainer;

        private final java.util.List<android.support.v17.leanback.widget.Presenter.ViewHolder> mActionViewHolders;

        android.support.v17.leanback.widget.MultiActionsProvider.MultiAction[] mMediaItemRowActions;

        android.support.v17.leanback.widget.AbstractMediaItemPresenter mRowPresenter;

        android.animation.ValueAnimator mFocusViewAnimator;

        public ViewHolder(android.view.View view) {
            super(view);
            mSelectorView = view.findViewById(R.id.mediaRowSelector);
            mMediaRowView = view.findViewById(R.id.mediaItemRow);
            mMediaItemDetailsView = view.findViewById(R.id.mediaItemDetails);
            mMediaItemNameView = ((android.widget.TextView) (view.findViewById(R.id.mediaItemName)));
            mMediaItemDurationView = ((android.widget.TextView) (view.findViewById(R.id.mediaItemDuration)));
            mMediaItemRowSeparator = view.findViewById(R.id.mediaRowSeparator);
            mMediaItemActionsContainer = ((android.view.ViewGroup) (view.findViewById(R.id.mediaItemActionsContainer)));
            mActionViewHolders = new java.util.ArrayList<android.support.v17.leanback.widget.Presenter.ViewHolder>();
            getMediaItemDetailsView().setOnClickListener(new android.view.View.OnClickListener() {
                @java.lang.Override
                public void onClick(android.view.View view) {
                    if (getOnItemViewClickedListener() != null) {
                        getOnItemViewClickedListener().onItemClicked(null, null, android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder.this, getRowObject());
                    }
                }
            });
            getMediaItemDetailsView().setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                @java.lang.Override
                public void onFocusChange(android.view.View view, boolean hasFocus) {
                    mFocusViewAnimator = android.support.v17.leanback.widget.AbstractMediaItemPresenter.updateSelector(mSelectorView, view, mFocusViewAnimator, true);
                }
            });
            mMediaItemNumberViewFlipper = ((android.widget.ViewFlipper) (view.findViewById(R.id.mediaItemNumberViewFlipper)));
            android.util.TypedValue typedValue = new android.util.TypedValue();
            boolean found = view.getContext().getTheme().resolveAttribute(R.attr.playbackMediaItemNumberViewFlipperLayout, typedValue, true);
            android.view.View mergeView = android.view.LayoutInflater.from(view.getContext()).inflate(found ? typedValue.resourceId : R.layout.lb_media_item_number_view_flipper, mMediaItemNumberViewFlipper, true);
            mMediaItemNumberView = ((android.widget.TextView) (mergeView.findViewById(R.id.initial)));
            mMediaItemPausedView = mergeView.findViewById(R.id.paused);
            mMediaItemPlayingView = mergeView.findViewById(R.id.playing);
        }

        /**
         * Binds the actions in a media item row object to their views. This consists of creating
         * (or reusing the existing) action view holders, and populating them with the actions'
         * icons.
         */
        public void onBindRowActions() {
            for (int i = getMediaItemActionsContainer().getChildCount() - 1; i >= mActionViewHolders.size(); i--) {
                getMediaItemActionsContainer().removeViewAt(i);
                mActionViewHolders.remove(i);
            }
            mMediaItemRowActions = null;
            java.lang.Object rowObject = getRowObject();
            final android.support.v17.leanback.widget.MultiActionsProvider.MultiAction[] actionList;
            if (rowObject instanceof android.support.v17.leanback.widget.MultiActionsProvider) {
                actionList = ((android.support.v17.leanback.widget.MultiActionsProvider) (rowObject)).getActions();
            } else {
                return;
            }
            android.support.v17.leanback.widget.Presenter actionPresenter = mRowPresenter.getActionPresenter();
            if (actionPresenter == null) {
                return;
            }
            mMediaItemRowActions = actionList;
            for (int i = mActionViewHolders.size(); i < actionList.length; i++) {
                final int actionIndex = i;
                final android.support.v17.leanback.widget.Presenter.ViewHolder actionViewHolder = actionPresenter.onCreateViewHolder(getMediaItemActionsContainer());
                getMediaItemActionsContainer().addView(actionViewHolder.view);
                mActionViewHolders.add(actionViewHolder);
                actionViewHolder.view.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                    @java.lang.Override
                    public void onFocusChange(android.view.View view, boolean hasFocus) {
                        mFocusViewAnimator = android.support.v17.leanback.widget.AbstractMediaItemPresenter.updateSelector(mSelectorView, view, mFocusViewAnimator, false);
                    }
                });
                actionViewHolder.view.setOnClickListener(new android.view.View.OnClickListener() {
                    @java.lang.Override
                    public void onClick(android.view.View view) {
                        if (getOnItemViewClickedListener() != null) {
                            getOnItemViewClickedListener().onItemClicked(actionViewHolder, mMediaItemRowActions[actionIndex], android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder.this, getRowObject());
                        }
                    }
                });
            }
            if (mMediaItemActionsContainer != null) {
                for (int i = 0; i < actionList.length; i++) {
                    android.support.v17.leanback.widget.Presenter.ViewHolder avh = mActionViewHolders.get(i);
                    actionPresenter.onUnbindViewHolder(avh);
                    actionPresenter.onBindViewHolder(avh, mMediaItemRowActions[i]);
                }
            }
        }

        int findActionIndex(android.support.v17.leanback.widget.MultiActionsProvider.MultiAction action) {
            if (mMediaItemRowActions != null) {
                for (int i = 0; i < mMediaItemRowActions.length; i++) {
                    if (mMediaItemRowActions[i] == action) {
                        return i;
                    }
                }
            }
            return -1;
        }

        /**
         * Notifies an action has changed in this media row and the UI needs to be updated
         *
         * @param action
         * 		The action whose state has changed
         */
        public void notifyActionChanged(android.support.v17.leanback.widget.MultiActionsProvider.MultiAction action) {
            android.support.v17.leanback.widget.Presenter actionPresenter = mRowPresenter.getActionPresenter();
            if (actionPresenter == null) {
                return;
            }
            int actionIndex = findActionIndex(action);
            if (actionIndex >= 0) {
                android.support.v17.leanback.widget.Presenter.ViewHolder actionViewHolder = mActionViewHolders.get(actionIndex);
                actionPresenter.onUnbindViewHolder(actionViewHolder);
                actionPresenter.onBindViewHolder(actionViewHolder, action);
            }
        }

        /**
         * Notifies the content of the media item details in a row has changed and triggers updating
         * the UI. This causes {@link #onBindMediaDetails(ViewHolder, Object)}
         * on the user's provided presenter to be called back, allowing them to update UI
         * accordingly.
         */
        public void notifyDetailsChanged() {
            mRowPresenter.onUnbindMediaDetails(this);
            mRowPresenter.onBindMediaDetails(this, getRowObject());
        }

        /**
         * Notifies the playback state of the media item row has changed. This in turn triggers
         * updating of the UI for that media item row if corresponding views are specified for each
         * playback state.
         * By default, 3 views are provided for each playback state, or these views can be provided
         * by the user.
         */
        public void notifyPlayStateChanged() {
            mRowPresenter.onBindMediaPlayState(this);
        }

        /**
         *
         *
         * @return The SelectorView responsible for highlighting the in-focus view within each
        media item row
         */
        public android.view.View getSelectorView() {
            return mSelectorView;
        }

        /**
         *
         *
         * @return The FlipperView responsible for flipping between different media item number
        views depending on the playback state
         */
        public android.widget.ViewFlipper getMediaItemNumberViewFlipper() {
            return mMediaItemNumberViewFlipper;
        }

        /**
         *
         *
         * @return The TextView responsible for rendering the media item number.
        This view is rendered when the media item row is neither playing nor paused.
         */
        public android.widget.TextView getMediaItemNumberView() {
            return mMediaItemNumberView;
        }

        /**
         *
         *
         * @return The view rendered when the media item row is paused.
         */
        public android.view.View getMediaItemPausedView() {
            return mMediaItemPausedView;
        }

        /**
         *
         *
         * @return The view rendered when the media item row is playing.
         */
        public android.view.View getMediaItemPlayingView() {
            return mMediaItemPlayingView;
        }

        /**
         * Flips to the view at index 'position'. This position corresponds to the index of a
         * particular view within the ViewFlipper layout specified for the MediaItemNumberView
         * (see playbackMediaItemNumberViewFlipperLayout attribute).
         *
         * @param position
         * 		The index of the child view to display.
         */
        public void setSelectedMediaItemNumberView(int position) {
            if ((position >= 0) & (position < mMediaItemNumberViewFlipper.getChildCount())) {
                mMediaItemNumberViewFlipper.setDisplayedChild(position);
            }
        }

        /**
         * Returns the view displayed when the media item is neither playing nor paused,
         * corresponding to the playback state of PLAY_STATE_INITIAL.
         *
         * @return The TextView responsible for rendering the media item name.
         */
        public android.widget.TextView getMediaItemNameView() {
            return mMediaItemNameView;
        }

        /**
         *
         *
         * @return The TextView responsible for rendering the media item duration
         */
        public android.widget.TextView getMediaItemDurationView() {
            return mMediaItemDurationView;
        }

        /**
         *
         *
         * @return The view container of media item details
         */
        public android.view.View getMediaItemDetailsView() {
            return mMediaItemDetailsView;
        }

        /**
         *
         *
         * @return The view responsible for rendering the separator line between media rows
         */
        public android.view.View getMediaItemRowSeparator() {
            return mMediaItemRowSeparator;
        }

        /**
         *
         *
         * @return The view containing the set of custom actions
         */
        public android.view.ViewGroup getMediaItemActionsContainer() {
            return mMediaItemActionsContainer;
        }

        /**
         *
         *
         * @return Array of MultiActions displayed for this media item row
         */
        public android.support.v17.leanback.widget.MultiActionsProvider.MultiAction[] getMediaItemRowActions() {
            return mMediaItemRowActions;
        }
    }

    @java.lang.Override
    protected android.support.v17.leanback.widget.RowPresenter.ViewHolder createRowViewHolder(android.view.ViewGroup parent) {
        android.content.Context context = parent.getContext();
        if (mThemeId != 0) {
            context = new android.view.ContextThemeWrapper(context, mThemeId);
        }
        android.view.View view = android.view.LayoutInflater.from(context).inflate(R.layout.lb_row_media_item, parent, false);
        final android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder vh = new android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder(view);
        vh.mRowPresenter = this;
        if (mBackgroundColorSet) {
            vh.mMediaRowView.setBackgroundColor(mBackgroundColor);
        }
        return vh;
    }

    @java.lang.Override
    public boolean isUsingDefaultSelectEffect() {
        return false;
    }

    @java.lang.Override
    protected boolean isClippingChildren() {
        return true;
    }

    @java.lang.Override
    protected void onBindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh, java.lang.Object item) {
        super.onBindRowViewHolder(vh, item);
        final android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder mvh = ((android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder) (vh));
        onBindRowActions(mvh);
        mvh.getMediaItemRowSeparator().setVisibility(hasMediaRowSeparator() ? android.view.View.VISIBLE : android.view.View.GONE);
        onBindMediaPlayState(mvh);
        onBindMediaDetails(((android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder) (vh)), item);
    }

    /**
     * Binds the given media item object action to the given ViewHolder's action views.
     *
     * @param vh
     * 		ViewHolder for the media item.
     */
    protected void onBindRowActions(android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder vh) {
        vh.onBindRowActions();
    }

    /**
     * Sets the background color for the row views within the playlist.
     * If this is not set, a default color, defaultBrandColor, from theme is used.
     * This defaultBrandColor defaults to android:attr/colorPrimary on v21, if it's specified.
     *
     * @param color
     * 		The ARGB color used to set as the media list background color.
     */
    public void setBackgroundColor(int color) {
        mBackgroundColorSet = true;
        mBackgroundColor = color;
    }

    /**
     * Specifies whether a line separator should be used between media item rows.
     *
     * @param hasSeparator
     * 		true if a separator should be displayed, false otherwise.
     */
    public void setHasMediaRowSeparator(boolean hasSeparator) {
        mMediaRowSeparator = hasSeparator;
    }

    public boolean hasMediaRowSeparator() {
        return mMediaRowSeparator;
    }

    /**
     * Binds the media item details to their views provided by the
     * {@link AbstractMediaItemPresenter}.
     * This method is to be overridden by the users of this presenter.
     * The subclasses of this presenter can access and bind individual views for either of the
     * media item number, name, or duration (depending on whichever views are visible according to
     * the providing theme attributes), by calling {@link ViewHolder#getMediaItemNumberView()},
     * {@link ViewHolder#getMediaItemNameView()}, and {@link ViewHolder#getMediaItemDurationView()},
     * on the {@link ViewHolder} provided as the argument {@code vh} of this presenter.
     *
     * @param vh
     * 		The ViewHolder for this {@link AbstractMediaItemPresenter}.
     * @param item
     * 		The media item row object being presented.
     */
    protected abstract void onBindMediaDetails(android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder vh, java.lang.Object item);

    /**
     * Unbinds the media item details from their views provided by the
     * {@link AbstractMediaItemPresenter}.
     * This method can be overridden by the subclasses of this presenter if required.
     *
     * @param vh
     * 		ViewHolder to unbind from.
     */
    protected void onUnbindMediaDetails(android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder vh) {
    }

    /**
     * Binds the media item number view to the appropriate play state view of the media item.
     * The play state of the media item is extracted by calling {@link #getMediaPlayState(Object)} for
     * the media item embedded within this view.
     * This method triggers updating of the playback state UI if corresponding views are specified
     * for the current playback state.
     * By default, 3 views are provided for each playback state, or these views can be provided
     * by the user.
     */
    public void onBindMediaPlayState(android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder vh) {
        int childIndex = android.support.v17.leanback.widget.AbstractMediaItemPresenter.calculateMediaItemNumberFlipperIndex(vh);
        if ((childIndex != (-1)) && (vh.mMediaItemNumberViewFlipper.getDisplayedChild() != childIndex)) {
            vh.mMediaItemNumberViewFlipper.setDisplayedChild(childIndex);
        }
    }

    static int calculateMediaItemNumberFlipperIndex(android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder vh) {
        int childIndex = -1;
        int newPlayState = vh.mRowPresenter.getMediaPlayState(vh.getRowObject());
        switch (newPlayState) {
            case android.support.v17.leanback.widget.AbstractMediaItemPresenter.PLAY_STATE_INITIAL :
                childIndex = (vh.mMediaItemNumberView == null) ? -1 : vh.mMediaItemNumberViewFlipper.indexOfChild(vh.mMediaItemNumberView);
                break;
            case android.support.v17.leanback.widget.AbstractMediaItemPresenter.PLAY_STATE_PAUSED :
                childIndex = (vh.mMediaItemPausedView == null) ? -1 : vh.mMediaItemNumberViewFlipper.indexOfChild(vh.mMediaItemPausedView);
                break;
            case android.support.v17.leanback.widget.AbstractMediaItemPresenter.PLAY_STATE_PLAYING :
                childIndex = (vh.mMediaItemPlayingView == null) ? -1 : vh.mMediaItemNumberViewFlipper.indexOfChild(vh.mMediaItemPlayingView);
        }
        return childIndex;
    }

    /**
     * Called when the given ViewHolder wants to unbind the play state view.
     *
     * @param vh
     * 		The ViewHolder to unbind from.
     */
    public void onUnbindMediaPlayState(android.support.v17.leanback.widget.AbstractMediaItemPresenter.ViewHolder vh) {
    }

    /**
     * Returns the current play state of the given media item. By default, this method returns
     * PLAY_STATE_INITIAL which causes the media item number
     * {@link ViewHolder#getMediaItemNameView()} to be displayed for different
     * playback states. Users of this class should override this method in order to provide the
     * play state of their custom media item data model.
     *
     * @param item
     * 		The media item
     * @return The current play state of this media item
     */
    protected int getMediaPlayState(java.lang.Object item) {
        return android.support.v17.leanback.widget.AbstractMediaItemPresenter.PLAY_STATE_INITIAL;
    }

    /**
     * Each media item row can have multiple focusable elements; the details on the left and a set
     * of optional custom actions on the right.
     * The selector is a highlight that moves to highlight to cover whichever views is in focus.
     *
     * @param selectorView
     * 		the selector view used to highlight an individual element within a row.
     * @param focusChangedView
     * 		The component within the media row whose focus got changed.
     * @param layoutAnimator
     * 		the ValueAnimator producing animation frames for the selector's width
     * 		and x-translation, generated by this method and stored for the each
     * 		{@link ViewHolder}.
     * @param isDetails
     * 		Whether the changed-focused view is for a media item details (true) or
     * 		an action (false).
     */
    static android.animation.ValueAnimator updateSelector(final android.view.View selectorView, android.view.View focusChangedView, android.animation.ValueAnimator layoutAnimator, boolean isDetails) {
        int animationDuration = focusChangedView.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
        android.view.animation.DecelerateInterpolator interpolator = new android.view.animation.DecelerateInterpolator();
        int layoutDirection = android.support.v4.view.ViewCompat.getLayoutDirection(selectorView);
        if (!focusChangedView.hasFocus()) {
            // if neither of the details or action views are in focus (ie. another row is in focus),
            // animate the selector out.
            selectorView.animate().cancel();
            selectorView.animate().alpha(0.0F).setDuration(animationDuration).setInterpolator(interpolator).start();
            // keep existing layout animator
            return layoutAnimator;
        } else {
            // cancel existing layout animator
            if (layoutAnimator != null) {
                layoutAnimator.cancel();
                layoutAnimator = null;
            }
            float currentAlpha = selectorView.getAlpha();
            selectorView.animate().alpha(1.0F).setDuration(animationDuration).setInterpolator(interpolator).start();
            final android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (selectorView.getLayoutParams()));
            android.view.ViewGroup rootView = ((android.view.ViewGroup) (selectorView.getParent()));
            android.support.v17.leanback.widget.AbstractMediaItemPresenter.sTempRect.set(0, 0, focusChangedView.getWidth(), focusChangedView.getHeight());
            rootView.offsetDescendantRectToMyCoords(focusChangedView, android.support.v17.leanback.widget.AbstractMediaItemPresenter.sTempRect);
            if (isDetails) {
                if (layoutDirection == android.view.View.LAYOUT_DIRECTION_RTL) {
                    android.support.v17.leanback.widget.AbstractMediaItemPresenter.sTempRect.right += rootView.getHeight();
                    android.support.v17.leanback.widget.AbstractMediaItemPresenter.sTempRect.left -= rootView.getHeight() / 2;
                } else {
                    android.support.v17.leanback.widget.AbstractMediaItemPresenter.sTempRect.left -= rootView.getHeight();
                    android.support.v17.leanback.widget.AbstractMediaItemPresenter.sTempRect.right += rootView.getHeight() / 2;
                }
            }
            final int targetLeft = android.support.v17.leanback.widget.AbstractMediaItemPresenter.sTempRect.left;
            final int targetWidth = android.support.v17.leanback.widget.AbstractMediaItemPresenter.sTempRect.width();
            final float deltaWidth = lp.width - targetWidth;
            final float deltaLeft = lp.leftMargin - targetLeft;
            if ((deltaLeft == 0.0F) && (deltaWidth == 0.0F)) {
                // no change needed
            } else
                if (currentAlpha == 0.0F) {
                    // change selector to the proper width and marginLeft without animation.
                    lp.width = targetWidth;
                    lp.leftMargin = targetLeft;
                    selectorView.requestLayout();
                } else {
                    // animate the selector to the proper width and marginLeft.
                    layoutAnimator = android.animation.ValueAnimator.ofFloat(0.0F, 1.0F);
                    layoutAnimator.setDuration(animationDuration);
                    layoutAnimator.setInterpolator(interpolator);
                    layoutAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
                        @java.lang.Override
                        public void onAnimationUpdate(android.animation.ValueAnimator valueAnimator) {
                            // Set width to the proper width for this animation step.
                            float fractionToEnd = 1.0F - valueAnimator.getAnimatedFraction();
                            lp.leftMargin = java.lang.Math.round(targetLeft + (deltaLeft * fractionToEnd));
                            lp.width = java.lang.Math.round(targetWidth + (deltaWidth * fractionToEnd));
                            selectorView.requestLayout();
                        }
                    });
                    layoutAnimator.start();
                }

            return layoutAnimator;
        }
    }
}

