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
 * ListRowPresenter renders {@link ListRow} using a
 * {@link HorizontalGridView} hosted in a {@link ListRowView}.
 *
 * <h3>Hover card</h3>
 * Optionally, {@link #setHoverCardPresenterSelector(PresenterSelector)} can be used to
 * display a view for the currently focused list item below the rendered
 * list. This view is known as a hover card.
 *
 * <h3>Selection animation</h3>
 * ListRowPresenter disables {@link RowPresenter}'s default dimming effect and draws
 * a dim overlay on each view individually.  A subclass may override and disable
 * {@link #isUsingDefaultListSelectEffect()} and write its own dim effect in
 * {@link #onSelectLevelChanged(RowPresenter.ViewHolder)}.
 *
 * <h3>Shadow</h3>
 * ListRowPresenter applies a default shadow to each child view.  Call
 * {@link #setShadowEnabled(boolean)} to disable shadows.  A subclass may override and return
 * false in {@link #isUsingDefaultShadow()} and replace with its own shadow implementation.
 */
public class ListRowPresenter extends android.support.v17.leanback.widget.RowPresenter {
    private static final java.lang.String TAG = "ListRowPresenter";

    private static final boolean DEBUG = false;

    private static final int DEFAULT_RECYCLED_POOL_SIZE = 24;

    /**
     * ViewHolder for the ListRowPresenter.
     */
    public static class ViewHolder extends android.support.v17.leanback.widget.RowPresenter.ViewHolder {
        final android.support.v17.leanback.widget.ListRowPresenter mListRowPresenter;

        final android.support.v17.leanback.widget.HorizontalGridView mGridView;

        android.support.v17.leanback.widget.ItemBridgeAdapter mItemBridgeAdapter;

        final android.support.v17.leanback.widget.HorizontalHoverCardSwitcher mHoverCardViewSwitcher = new android.support.v17.leanback.widget.HorizontalHoverCardSwitcher();

        final int mPaddingTop;

        final int mPaddingBottom;

        final int mPaddingLeft;

        final int mPaddingRight;

        public ViewHolder(android.view.View rootView, android.support.v17.leanback.widget.HorizontalGridView gridView, android.support.v17.leanback.widget.ListRowPresenter p) {
            super(rootView);
            mGridView = gridView;
            mListRowPresenter = p;
            mPaddingTop = mGridView.getPaddingTop();
            mPaddingBottom = mGridView.getPaddingBottom();
            mPaddingLeft = mGridView.getPaddingLeft();
            mPaddingRight = mGridView.getPaddingRight();
        }

        /**
         * Gets ListRowPresenter that creates this ViewHolder.
         *
         * @return ListRowPresenter that creates this ViewHolder.
         */
        public final android.support.v17.leanback.widget.ListRowPresenter getListRowPresenter() {
            return mListRowPresenter;
        }

        /**
         * Gets HorizontalGridView that shows a list of items.
         *
         * @return HorizontalGridView that shows a list of items.
         */
        public final android.support.v17.leanback.widget.HorizontalGridView getGridView() {
            return mGridView;
        }

        /**
         * Gets ItemBridgeAdapter that creates the list of items.
         *
         * @return ItemBridgeAdapter that creates the list of items.
         */
        public final android.support.v17.leanback.widget.ItemBridgeAdapter getBridgeAdapter() {
            return mItemBridgeAdapter;
        }

        /**
         * Gets selected item position in adapter.
         *
         * @return Selected item position in adapter.
         */
        public int getSelectedPosition() {
            return mGridView.getSelectedPosition();
        }

        /**
         * Gets ViewHolder at a position in adapter.  Returns null if the item does not exist
         * or the item is not bound to a view.
         *
         * @param position
         * 		Position of the item in adapter.
         * @return ViewHolder bounds to the item.
         */
        public android.support.v17.leanback.widget.Presenter.ViewHolder getItemViewHolder(int position) {
            android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibvh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (mGridView.findViewHolderForAdapterPosition(position)));
            if (ibvh == null) {
                return null;
            }
            return ibvh.getViewHolder();
        }
    }

    /**
     * A task on the ListRowPresenter.ViewHolder that can select an item by position in the
     * HorizontalGridView and perform an optional item task on it.
     */
    public static class SelectItemViewHolderTask extends android.support.v17.leanback.widget.Presenter.ViewHolderTask {
        private int mItemPosition;

        private boolean mSmoothScroll = true;

        android.support.v17.leanback.widget.Presenter.ViewHolderTask mItemTask;

        public SelectItemViewHolderTask(int itemPosition) {
            setItemPosition(itemPosition);
        }

        /**
         * Sets the adapter position of item to select.
         *
         * @param itemPosition
         * 		Position of the item in adapter.
         */
        public void setItemPosition(int itemPosition) {
            mItemPosition = itemPosition;
        }

        /**
         * Returns the adapter position of item to select.
         *
         * @return The adapter position of item to select.
         */
        public int getItemPosition() {
            return mItemPosition;
        }

        /**
         * Sets smooth scrolling to the item or jump to the item without scrolling.  By default it is
         * true.
         *
         * @param smoothScroll
         * 		True for smooth scrolling to the item, false otherwise.
         */
        public void setSmoothScroll(boolean smoothScroll) {
            mSmoothScroll = smoothScroll;
        }

        /**
         * Returns true if smooth scrolling to the item false otherwise.  By default it is true.
         *
         * @return True for smooth scrolling to the item, false otherwise.
         */
        public boolean isSmoothScroll() {
            return mSmoothScroll;
        }

        /**
         * Returns optional task to run when the item is selected, null for no task.
         *
         * @return Optional task to run when the item is selected, null for no task.
         */
        public android.support.v17.leanback.widget.Presenter.ViewHolderTask getItemTask() {
            return mItemTask;
        }

        /**
         * Sets task to run when the item is selected, null for no task.
         *
         * @param itemTask
         * 		Optional task to run when the item is selected, null for no task.
         */
        public void setItemTask(android.support.v17.leanback.widget.Presenter.ViewHolderTask itemTask) {
            mItemTask = itemTask;
        }

        @java.lang.Override
        public void run(android.support.v17.leanback.widget.Presenter.ViewHolder holder) {
            if (holder instanceof android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) {
                android.support.v17.leanback.widget.HorizontalGridView gridView = ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (holder)).getGridView();
                android.support.v17.leanback.widget.ViewHolderTask task = null;
                if (mItemTask != null) {
                    task = new android.support.v17.leanback.widget.ViewHolderTask() {
                        final android.support.v17.leanback.widget.Presenter.ViewHolderTask itemTask = mItemTask;

                        @java.lang.Override
                        public void run(android.support.v7.widget.RecyclerView.ViewHolder rvh) {
                            android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibvh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (rvh));
                            itemTask.run(ibvh.getViewHolder());
                        }
                    };
                }
                if (isSmoothScroll()) {
                    gridView.setSelectedPositionSmooth(mItemPosition, task);
                } else {
                    gridView.setSelectedPosition(mItemPosition, task);
                }
            }
        }
    }

    class ListRowPresenterItemBridgeAdapter extends android.support.v17.leanback.widget.ItemBridgeAdapter {
        android.support.v17.leanback.widget.ListRowPresenter.ViewHolder mRowViewHolder;

        ListRowPresenterItemBridgeAdapter(android.support.v17.leanback.widget.ListRowPresenter.ViewHolder rowViewHolder) {
            mRowViewHolder = rowViewHolder;
        }

        @java.lang.Override
        protected void onCreate(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
            if (viewHolder.itemView instanceof android.view.ViewGroup) {
                android.support.v17.leanback.transition.TransitionHelper.setTransitionGroup(((android.view.ViewGroup) (viewHolder.itemView)), true);
            }
            if (mShadowOverlayHelper != null) {
                mShadowOverlayHelper.onViewCreated(viewHolder.itemView);
            }
        }

        @java.lang.Override
        public void onBind(final android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
            // Only when having an OnItemClickListner, we will attach the OnClickListener.
            if (mRowViewHolder.getOnItemViewClickedListener() != null) {
                viewHolder.mHolder.view.setOnClickListener(new android.view.View.OnClickListener() {
                    @java.lang.Override
                    public void onClick(android.view.View v) {
                        android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (mRowViewHolder.mGridView.getChildViewHolder(viewHolder.itemView)));
                        if (mRowViewHolder.getOnItemViewClickedListener() != null) {
                            mRowViewHolder.getOnItemViewClickedListener().onItemClicked(viewHolder.mHolder, ibh.mItem, mRowViewHolder, ((android.support.v17.leanback.widget.ListRow) (mRowViewHolder.mRow)));
                        }
                    }
                });
            }
        }

        @java.lang.Override
        public void onUnbind(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
            if (mRowViewHolder.getOnItemViewClickedListener() != null) {
                viewHolder.mHolder.view.setOnClickListener(null);
            }
        }

        @java.lang.Override
        public void onAttachedToWindow(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
            if ((mShadowOverlayHelper != null) && mShadowOverlayHelper.needsOverlay()) {
                int dimmedColor = mRowViewHolder.mColorDimmer.getPaint().getColor();
                mShadowOverlayHelper.setOverlayColor(viewHolder.itemView, dimmedColor);
            }
            mRowViewHolder.syncActivatedStatus(viewHolder.itemView);
        }

        @java.lang.Override
        public void onAddPresenter(android.support.v17.leanback.widget.Presenter presenter, int type) {
            mRowViewHolder.getGridView().getRecycledViewPool().setMaxRecycledViews(type, getRecycledPoolSize(presenter));
        }
    }

    private int mNumRows = 1;

    private int mRowHeight;

    private int mExpandedRowHeight;

    private android.support.v17.leanback.widget.PresenterSelector mHoverCardPresenterSelector;

    private int mFocusZoomFactor;

    private boolean mUseFocusDimmer;

    private boolean mShadowEnabled = true;

    private int mBrowseRowsFadingEdgeLength = -1;

    private boolean mRoundedCornersEnabled = true;

    private boolean mKeepChildForeground = true;

    private java.util.HashMap<android.support.v17.leanback.widget.Presenter, java.lang.Integer> mRecycledPoolSize = new java.util.HashMap<android.support.v17.leanback.widget.Presenter, java.lang.Integer>();

    android.support.v17.leanback.widget.ShadowOverlayHelper mShadowOverlayHelper;

    private android.support.v17.leanback.widget.ItemBridgeAdapter.Wrapper mShadowOverlayWrapper;

    private static int sSelectedRowTopPadding;

    private static int sExpandedSelectedRowTopPadding;

    private static int sExpandedRowNoHovercardBottomPadding;

    /**
     * Constructs a ListRowPresenter with defaults.
     * Uses {@link FocusHighlight#ZOOM_FACTOR_MEDIUM} for focus zooming and
     * disabled dimming on focus.
     */
    public ListRowPresenter() {
        this(android.support.v17.leanback.widget.FocusHighlight.ZOOM_FACTOR_MEDIUM);
    }

    /**
     * Constructs a ListRowPresenter with the given parameters.
     *
     * @param focusZoomFactor
     * 		Controls the zoom factor used when an item view is focused. One of
     * 		{@link FocusHighlight#ZOOM_FACTOR_NONE},
     * 		{@link FocusHighlight#ZOOM_FACTOR_SMALL},
     * 		{@link FocusHighlight#ZOOM_FACTOR_XSMALL},
     * 		{@link FocusHighlight#ZOOM_FACTOR_MEDIUM},
     * 		{@link FocusHighlight#ZOOM_FACTOR_LARGE}
     * 		Dimming on focus defaults to disabled.
     */
    public ListRowPresenter(int focusZoomFactor) {
        this(focusZoomFactor, false);
    }

    /**
     * Constructs a ListRowPresenter with the given parameters.
     *
     * @param focusZoomFactor
     * 		Controls the zoom factor used when an item view is focused. One of
     * 		{@link FocusHighlight#ZOOM_FACTOR_NONE},
     * 		{@link FocusHighlight#ZOOM_FACTOR_SMALL},
     * 		{@link FocusHighlight#ZOOM_FACTOR_XSMALL},
     * 		{@link FocusHighlight#ZOOM_FACTOR_MEDIUM},
     * 		{@link FocusHighlight#ZOOM_FACTOR_LARGE}
     * @param useFocusDimmer
     * 		determines if the FocusHighlighter will use the dimmer
     */
    public ListRowPresenter(int focusZoomFactor, boolean useFocusDimmer) {
        if (!android.support.v17.leanback.widget.FocusHighlightHelper.isValidZoomIndex(focusZoomFactor)) {
            throw new java.lang.IllegalArgumentException("Unhandled zoom factor");
        }
        mFocusZoomFactor = focusZoomFactor;
        mUseFocusDimmer = useFocusDimmer;
    }

    /**
     * Sets the row height for rows created by this Presenter. Rows
     * created before calling this method will not be updated.
     *
     * @param rowHeight
     * 		Row height in pixels, or WRAP_CONTENT, or 0
     * 		to use the default height.
     */
    public void setRowHeight(int rowHeight) {
        mRowHeight = rowHeight;
    }

    /**
     * Returns the row height for list rows created by this Presenter.
     */
    public int getRowHeight() {
        return mRowHeight;
    }

    /**
     * Sets the expanded row height for rows created by this Presenter.
     * If not set, expanded rows have the same height as unexpanded
     * rows.
     *
     * @param rowHeight
     * 		The row height in to use when the row is expanded,
     * 		in pixels, or WRAP_CONTENT, or 0 to use the default.
     */
    public void setExpandedRowHeight(int rowHeight) {
        mExpandedRowHeight = rowHeight;
    }

    /**
     * Returns the expanded row height for rows created by this Presenter.
     */
    public int getExpandedRowHeight() {
        return mExpandedRowHeight != 0 ? mExpandedRowHeight : mRowHeight;
    }

    /**
     * Returns the zoom factor used for focus highlighting.
     */
    public final int getFocusZoomFactor() {
        return mFocusZoomFactor;
    }

    /**
     * Returns the zoom factor used for focus highlighting.
     *
     * @deprecated use {@link #getFocusZoomFactor} instead.
     */
    @java.lang.Deprecated
    public final int getZoomFactor() {
        return mFocusZoomFactor;
    }

    /**
     * Returns true if the focus dimmer is used for focus highlighting; false otherwise.
     */
    public final boolean isFocusDimmerUsed() {
        return mUseFocusDimmer;
    }

    /**
     * Sets the numbers of rows for rendering the list of items. By default, it is
     * set to 1.
     */
    public void setNumRows(int numRows) {
        this.mNumRows = numRows;
    }

    @java.lang.Override
    protected void initializeRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder) {
        super.initializeRowViewHolder(holder);
        final android.support.v17.leanback.widget.ListRowPresenter.ViewHolder rowViewHolder = ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (holder));
        android.content.Context context = holder.view.getContext();
        if (mShadowOverlayHelper == null) {
            mShadowOverlayHelper = new android.support.v17.leanback.widget.ShadowOverlayHelper.Builder().needsOverlay(needsDefaultListSelectEffect()).needsShadow(needsDefaultShadow()).needsRoundedCorner(areChildRoundedCornersEnabled()).preferZOrder(isUsingZOrder(context)).keepForegroundDrawable(mKeepChildForeground).options(createShadowOverlayOptions()).build(context);
            if (mShadowOverlayHelper.needsWrapper()) {
                mShadowOverlayWrapper = new android.support.v17.leanback.widget.ItemBridgeAdapterShadowOverlayWrapper(mShadowOverlayHelper);
            }
        }
        rowViewHolder.mItemBridgeAdapter = new android.support.v17.leanback.widget.ListRowPresenter.ListRowPresenterItemBridgeAdapter(rowViewHolder);
        // set wrapper if needed
        rowViewHolder.mItemBridgeAdapter.setWrapper(mShadowOverlayWrapper);
        mShadowOverlayHelper.prepareParentForShadow(rowViewHolder.mGridView);
        android.support.v17.leanback.widget.FocusHighlightHelper.setupBrowseItemFocusHighlight(rowViewHolder.mItemBridgeAdapter, mFocusZoomFactor, mUseFocusDimmer);
        rowViewHolder.mGridView.setFocusDrawingOrderEnabled(mShadowOverlayHelper.getShadowType() != android.support.v17.leanback.widget.ShadowOverlayHelper.SHADOW_DYNAMIC);
        rowViewHolder.mGridView.setOnChildSelectedListener(new android.support.v17.leanback.widget.OnChildSelectedListener() {
            @java.lang.Override
            public void onChildSelected(android.view.ViewGroup parent, android.view.View view, int position, long id) {
                selectChildView(rowViewHolder, view, true);
            }
        });
        rowViewHolder.mGridView.setOnUnhandledKeyListener(new android.support.v17.leanback.widget.BaseGridView.OnUnhandledKeyListener() {
            @java.lang.Override
            public boolean onUnhandledKey(android.view.KeyEvent event) {
                return (rowViewHolder.getOnKeyListener() != null) && rowViewHolder.getOnKeyListener().onKey(rowViewHolder.view, event.getKeyCode(), event);
            }
        });
        rowViewHolder.mGridView.setNumRows(mNumRows);
    }

    final boolean needsDefaultListSelectEffect() {
        return isUsingDefaultListSelectEffect() && getSelectEffectEnabled();
    }

    /**
     * Sets the recycled pool size for the given presenter.
     */
    public void setRecycledPoolSize(android.support.v17.leanback.widget.Presenter presenter, int size) {
        mRecycledPoolSize.put(presenter, size);
    }

    /**
     * Returns the recycled pool size for the given presenter.
     */
    public int getRecycledPoolSize(android.support.v17.leanback.widget.Presenter presenter) {
        return mRecycledPoolSize.containsKey(presenter) ? mRecycledPoolSize.get(presenter) : android.support.v17.leanback.widget.ListRowPresenter.DEFAULT_RECYCLED_POOL_SIZE;
    }

    /**
     * Sets the {@link PresenterSelector} used for showing a select object in a hover card.
     */
    public final void setHoverCardPresenterSelector(android.support.v17.leanback.widget.PresenterSelector selector) {
        mHoverCardPresenterSelector = selector;
    }

    /**
     * Returns the {@link PresenterSelector} used for showing a select object in a hover card.
     */
    public final android.support.v17.leanback.widget.PresenterSelector getHoverCardPresenterSelector() {
        return mHoverCardPresenterSelector;
    }

    /* Perform operations when a child of horizontal grid view is selected. */
    void selectChildView(android.support.v17.leanback.widget.ListRowPresenter.ViewHolder rowViewHolder, android.view.View view, boolean fireEvent) {
        if (view != null) {
            if (rowViewHolder.mSelected) {
                android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (rowViewHolder.mGridView.getChildViewHolder(view)));
                if (mHoverCardPresenterSelector != null) {
                    rowViewHolder.mHoverCardViewSwitcher.select(rowViewHolder.mGridView, view, ibh.mItem);
                }
                if (fireEvent && (rowViewHolder.getOnItemViewSelectedListener() != null)) {
                    rowViewHolder.getOnItemViewSelectedListener().onItemSelected(ibh.mHolder, ibh.mItem, rowViewHolder, rowViewHolder.mRow);
                }
            }
        } else {
            if (mHoverCardPresenterSelector != null) {
                rowViewHolder.mHoverCardViewSwitcher.unselect();
            }
            if (fireEvent && (rowViewHolder.getOnItemViewSelectedListener() != null)) {
                rowViewHolder.getOnItemViewSelectedListener().onItemSelected(null, null, rowViewHolder, rowViewHolder.mRow);
            }
        }
    }

    private static void initStatics(android.content.Context context) {
        if (android.support.v17.leanback.widget.ListRowPresenter.sSelectedRowTopPadding == 0) {
            android.support.v17.leanback.widget.ListRowPresenter.sSelectedRowTopPadding = context.getResources().getDimensionPixelSize(R.dimen.lb_browse_selected_row_top_padding);
            android.support.v17.leanback.widget.ListRowPresenter.sExpandedSelectedRowTopPadding = context.getResources().getDimensionPixelSize(R.dimen.lb_browse_expanded_selected_row_top_padding);
            android.support.v17.leanback.widget.ListRowPresenter.sExpandedRowNoHovercardBottomPadding = context.getResources().getDimensionPixelSize(R.dimen.lb_browse_expanded_row_no_hovercard_bottom_padding);
        }
    }

    private int getSpaceUnderBaseline(android.support.v17.leanback.widget.ListRowPresenter.ViewHolder vh) {
        android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder headerViewHolder = vh.getHeaderViewHolder();
        if (headerViewHolder != null) {
            if (getHeaderPresenter() != null) {
                return getHeaderPresenter().getSpaceUnderBaseline(headerViewHolder);
            }
            return headerViewHolder.view.getPaddingBottom();
        }
        return 0;
    }

    private void setVerticalPadding(android.support.v17.leanback.widget.ListRowPresenter.ViewHolder vh) {
        int paddingTop;
        int paddingBottom;
        // Note: sufficient bottom padding needed for card shadows.
        if (vh.isExpanded()) {
            int headerSpaceUnderBaseline = getSpaceUnderBaseline(vh);
            if (android.support.v17.leanback.widget.ListRowPresenter.DEBUG)
                android.util.Log.v(android.support.v17.leanback.widget.ListRowPresenter.TAG, "headerSpaceUnderBaseline " + headerSpaceUnderBaseline);

            paddingTop = (vh.isSelected() ? android.support.v17.leanback.widget.ListRowPresenter.sExpandedSelectedRowTopPadding : vh.mPaddingTop) - headerSpaceUnderBaseline;
            paddingBottom = (mHoverCardPresenterSelector == null) ? android.support.v17.leanback.widget.ListRowPresenter.sExpandedRowNoHovercardBottomPadding : vh.mPaddingBottom;
        } else
            if (vh.isSelected()) {
                paddingTop = android.support.v17.leanback.widget.ListRowPresenter.sSelectedRowTopPadding - vh.mPaddingBottom;
                paddingBottom = android.support.v17.leanback.widget.ListRowPresenter.sSelectedRowTopPadding;
            } else {
                paddingTop = 0;
                paddingBottom = vh.mPaddingBottom;
            }

        vh.getGridView().setPadding(vh.mPaddingLeft, paddingTop, vh.mPaddingRight, paddingBottom);
    }

    @java.lang.Override
    protected android.support.v17.leanback.widget.RowPresenter.ViewHolder createRowViewHolder(android.view.ViewGroup parent) {
        android.support.v17.leanback.widget.ListRowPresenter.initStatics(parent.getContext());
        android.support.v17.leanback.widget.ListRowView rowView = new android.support.v17.leanback.widget.ListRowView(parent.getContext());
        setupFadingEffect(rowView);
        if (mRowHeight != 0) {
            rowView.getGridView().setRowHeight(mRowHeight);
        }
        return new android.support.v17.leanback.widget.ListRowPresenter.ViewHolder(rowView, rowView.getGridView(), this);
    }

    /**
     * Dispatch item selected event using current selected item in the {@link HorizontalGridView}.
     * The method should only be called from onRowViewSelected().
     */
    @java.lang.Override
    protected void dispatchItemSelectedListener(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, boolean selected) {
        android.support.v17.leanback.widget.ListRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (holder));
        android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder itemViewHolder = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (vh.mGridView.findViewHolderForPosition(vh.mGridView.getSelectedPosition())));
        if (itemViewHolder == null) {
            super.dispatchItemSelectedListener(holder, selected);
            return;
        }
        if (selected) {
            if (holder.getOnItemViewSelectedListener() != null) {
                holder.getOnItemViewSelectedListener().onItemSelected(itemViewHolder.getViewHolder(), itemViewHolder.mItem, vh, vh.getRow());
            }
        }
    }

    @java.lang.Override
    protected void onRowViewSelected(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, boolean selected) {
        super.onRowViewSelected(holder, selected);
        android.support.v17.leanback.widget.ListRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (holder));
        setVerticalPadding(vh);
        updateFooterViewSwitcher(vh);
    }

    /* Show or hide hover card when row selection or expanded state is changed. */
    private void updateFooterViewSwitcher(android.support.v17.leanback.widget.ListRowPresenter.ViewHolder vh) {
        if (vh.mExpanded && vh.mSelected) {
            if (mHoverCardPresenterSelector != null) {
                vh.mHoverCardViewSwitcher.init(((android.view.ViewGroup) (vh.view)), mHoverCardPresenterSelector);
            }
            android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (vh.mGridView.findViewHolderForPosition(vh.mGridView.getSelectedPosition())));
            selectChildView(vh, ibh == null ? null : ibh.itemView, false);
        } else {
            if (mHoverCardPresenterSelector != null) {
                vh.mHoverCardViewSwitcher.unselect();
            }
        }
    }

    private void setupFadingEffect(android.support.v17.leanback.widget.ListRowView rowView) {
        // content is completely faded at 1/2 padding of left, fading length is 1/2 of padding.
        android.support.v17.leanback.widget.HorizontalGridView gridView = rowView.getGridView();
        if (mBrowseRowsFadingEdgeLength < 0) {
            android.content.res.TypedArray ta = gridView.getContext().obtainStyledAttributes(R.styleable.LeanbackTheme);
            mBrowseRowsFadingEdgeLength = ((int) (ta.getDimension(R.styleable.LeanbackTheme_browseRowsFadingEdgeLength, 0)));
            ta.recycle();
        }
        gridView.setFadingLeftEdgeLength(mBrowseRowsFadingEdgeLength);
    }

    @java.lang.Override
    protected void onRowViewExpanded(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, boolean expanded) {
        super.onRowViewExpanded(holder, expanded);
        android.support.v17.leanback.widget.ListRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (holder));
        if (getRowHeight() != getExpandedRowHeight()) {
            int newHeight = (expanded) ? getExpandedRowHeight() : getRowHeight();
            vh.getGridView().setRowHeight(newHeight);
        }
        setVerticalPadding(vh);
        updateFooterViewSwitcher(vh);
    }

    @java.lang.Override
    protected void onBindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, java.lang.Object item) {
        super.onBindRowViewHolder(holder, item);
        android.support.v17.leanback.widget.ListRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (holder));
        android.support.v17.leanback.widget.ListRow rowItem = ((android.support.v17.leanback.widget.ListRow) (item));
        vh.mItemBridgeAdapter.setAdapter(rowItem.getAdapter());
        vh.mGridView.setAdapter(vh.mItemBridgeAdapter);
        vh.mGridView.setContentDescription(rowItem.getContentDescription());
    }

    @java.lang.Override
    protected void onUnbindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder) {
        android.support.v17.leanback.widget.ListRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (holder));
        vh.mGridView.setAdapter(null);
        vh.mItemBridgeAdapter.clear();
        super.onUnbindRowViewHolder(holder);
    }

    /**
     * ListRowPresenter overrides the default select effect of {@link RowPresenter}
     * and return false.
     */
    @java.lang.Override
    public final boolean isUsingDefaultSelectEffect() {
        return false;
    }

    /**
     * Returns true so that default select effect is applied to each individual
     * child of {@link HorizontalGridView}.  Subclass may return false to disable
     * the default implementation.
     *
     * @see #onSelectLevelChanged(RowPresenter.ViewHolder)
     */
    public boolean isUsingDefaultListSelectEffect() {
        return true;
    }

    /**
     * Default implementation returns true if SDK version >= 21, shadow (either static or z-order
     * based) will be applied to each individual child of {@link HorizontalGridView}.
     * Subclass may return false to disable default implementation of shadow and provide its own.
     */
    public boolean isUsingDefaultShadow() {
        return android.support.v17.leanback.widget.ShadowOverlayHelper.supportsShadow();
    }

    /**
     * Returns true if SDK >= L, where Z shadow is enabled so that Z order is enabled
     * on each child of horizontal list.   If subclass returns false in isUsingDefaultShadow()
     * and does not use Z-shadow on SDK >= L, it should override isUsingZOrder() return false.
     */
    public boolean isUsingZOrder(android.content.Context context) {
        return !android.support.v17.leanback.system.Settings.getInstance(context).preferStaticShadows();
    }

    /**
     * Enables or disables child shadow.
     * This is not only for enable/disable default shadow implementation but also subclass must
     * respect this flag.
     */
    public final void setShadowEnabled(boolean enabled) {
        mShadowEnabled = enabled;
    }

    /**
     * Returns true if child shadow is enabled.
     * This is not only for enable/disable default shadow implementation but also subclass must
     * respect this flag.
     */
    public final boolean getShadowEnabled() {
        return mShadowEnabled;
    }

    /**
     * Enables or disabled rounded corners on children of this row.
     * Supported on Android SDK >= L.
     */
    public final void enableChildRoundedCorners(boolean enable) {
        mRoundedCornersEnabled = enable;
    }

    /**
     * Returns true if rounded corners are enabled for children of this row.
     */
    public final boolean areChildRoundedCornersEnabled() {
        return mRoundedCornersEnabled;
    }

    final boolean needsDefaultShadow() {
        return isUsingDefaultShadow() && getShadowEnabled();
    }

    /**
     * When ListRowPresenter applies overlay color on the child,  it may change child's foreground
     * Drawable.  If application uses child's foreground for other purposes such as ripple effect,
     * it needs tell ListRowPresenter to keep the child's foreground.  The default value is true.
     *
     * @param keep
     * 		true if keep foreground of child of this row, false ListRowPresenter might change
     * 		the foreground of the child.
     */
    public final void setKeepChildForeground(boolean keep) {
        mKeepChildForeground = keep;
    }

    /**
     * Returns true if keeps foreground of child of this row, false otherwise.  When
     * ListRowPresenter applies overlay color on the child,  it may change child's foreground
     * Drawable.  If application uses child's foreground for other purposes such as ripple effect,
     * it needs tell ListRowPresenter to keep the child's foreground.  The default value is true.
     *
     * @return true if keeps foreground of child of this row, false otherwise.
     */
    public final boolean isKeepChildForeground() {
        return mKeepChildForeground;
    }

    /**
     * Create ShadowOverlayHelper Options.  Subclass may override.
     * e.g.
     * <code>
     * return new ShadowOverlayHelper.Options().roundedCornerRadius(10);
     * </code>
     *
     * @return The options to be used for shadow, overlay and rounded corner.
     */
    protected android.support.v17.leanback.widget.ShadowOverlayHelper.Options createShadowOverlayOptions() {
        return android.support.v17.leanback.widget.ShadowOverlayHelper.Options.DEFAULT;
    }

    /**
     * Applies select level to header and draw a default color dim over each child
     * of {@link HorizontalGridView}.
     * <p>
     * Subclass may override this method.  A subclass
     * needs to call super.onSelectLevelChanged() for applying header select level
     * and optionally applying a default select level to each child view of
     * {@link HorizontalGridView} if {@link #isUsingDefaultListSelectEffect()}
     * is true.  Subclass may override {@link #isUsingDefaultListSelectEffect()} to return
     * false and deal with the individual item select level by itself.
     * </p>
     */
    @java.lang.Override
    protected void onSelectLevelChanged(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder) {
        super.onSelectLevelChanged(holder);
        if ((mShadowOverlayHelper != null) && mShadowOverlayHelper.needsOverlay()) {
            android.support.v17.leanback.widget.ListRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (holder));
            int dimmedColor = vh.mColorDimmer.getPaint().getColor();
            for (int i = 0, count = vh.mGridView.getChildCount(); i < count; i++) {
                mShadowOverlayHelper.setOverlayColor(vh.mGridView.getChildAt(i), dimmedColor);
            }
            if (vh.mGridView.getFadingLeftEdge()) {
                vh.mGridView.invalidate();
            }
        }
    }

    @java.lang.Override
    public void freeze(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, boolean freeze) {
        android.support.v17.leanback.widget.ListRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (holder));
        vh.mGridView.setScrollEnabled(!freeze);
    }

    @java.lang.Override
    public void setEntranceTransitionState(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, boolean afterEntrance) {
        super.setEntranceTransitionState(holder, afterEntrance);
        ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (holder)).mGridView.setChildrenVisibility(afterEntrance ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
    }
}

