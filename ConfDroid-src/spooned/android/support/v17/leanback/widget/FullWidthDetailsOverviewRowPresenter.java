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
 * Renders a {@link DetailsOverviewRow} to display an overview of an item. Typically this row will
 * be the first row in a fragment such as the
 * {@link android.support.v17.leanback.app.DetailsFragment}. The View created by the
 * FullWidthDetailsOverviewRowPresenter is made in three parts: logo view on the left, action list view on
 * the top and a customizable detailed description view on the right.
 *
 * <p>The detailed description is rendered using a {@link Presenter} passed in
 * {@link #FullWidthDetailsOverviewRowPresenter(Presenter)}. Typically this will be an instance of
 * {@link AbstractDetailsDescriptionPresenter}. The application can access the detailed description
 * ViewHolder from {@link ViewHolder#getDetailsDescriptionViewHolder()}.
 * </p>
 *
 * <p>The logo view is rendered using a customizable {@link DetailsOverviewLogoPresenter} passed in
 * {@link #FullWidthDetailsOverviewRowPresenter(Presenter, DetailsOverviewLogoPresenter)}. The application
 * can access the logo ViewHolder from {@link ViewHolder#getLogoViewHolder()}.
 * </p>
 *
 * <p>
 * To support activity shared element transition, call {@link #setListener(Listener)} with
 * {@link FullWidthDetailsOverviewSharedElementHelper} during Activity's onCreate(). Application is free to
 * create its own "shared element helper" class using the Listener for image binding.
 * Call {@link #setParticipatingEntranceTransition(boolean)} with false
 * </p>
 *
 * <p>
 * The view has three states: {@link #STATE_HALF} {@link #STATE_FULL} and {@link #STATE_SMALL}. See
 * {@link android.support.v17.leanback.app.DetailsFragment} where it switches states based on
 * selected row position.
 * </p>
 */
public class FullWidthDetailsOverviewRowPresenter extends android.support.v17.leanback.widget.RowPresenter {
    static final java.lang.String TAG = "FullWidthDetailsOverviewRowPresenter";

    static final boolean DEBUG = false;

    private static android.graphics.Rect sTmpRect = new android.graphics.Rect();

    static final android.os.Handler sHandler = new android.os.Handler();

    /**
     * This is the default state corresponding to layout file.  The view takes full width
     * of screen and covers bottom half of the screen.
     */
    public static final int STATE_HALF = 0;

    /**
     * This is the state when the view covers full width and height of screen.
     */
    public static final int STATE_FULL = 1;

    /**
     * This is the state where the view shrinks to a small banner.
     */
    public static final int STATE_SMALL = 2;

    /**
     * This is the alignment mode that the logo and description align to the starting edge of the
     * overview view.
     */
    public static final int ALIGN_MODE_START = 0;

    /**
     * This is the alignment mode that the ending edge of logo and the starting edge of description
     * align to the middle of the overview view. Note that this might not be the exact horizontal
     * center of the overview view.
     */
    public static final int ALIGN_MODE_MIDDLE = 1;

    /**
     * Listeners for events on ViewHolder.
     */
    public static abstract class Listener {
        /**
         * {@link FullWidthDetailsOverviewRowPresenter#notifyOnBindLogo(ViewHolder)} is called.
         *
         * @param vh
         * 		The ViewHolder that has bound logo view.
         */
        public void onBindLogo(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder vh) {
        }
    }

    class ActionsItemBridgeAdapter extends android.support.v17.leanback.widget.ItemBridgeAdapter {
        android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder mViewHolder;

        ActionsItemBridgeAdapter(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder viewHolder) {
            mViewHolder = viewHolder;
        }

        @java.lang.Override
        public void onBind(final android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibvh) {
            if ((mViewHolder.getOnItemViewClickedListener() != null) || (mActionClickedListener != null)) {
                ibvh.getPresenter().setOnClickListener(ibvh.getViewHolder(), new android.view.View.OnClickListener() {
                    @java.lang.Override
                    public void onClick(android.view.View v) {
                        if (mViewHolder.getOnItemViewClickedListener() != null) {
                            mViewHolder.getOnItemViewClickedListener().onItemClicked(ibvh.getViewHolder(), ibvh.getItem(), mViewHolder, mViewHolder.getRow());
                        }
                        if (mActionClickedListener != null) {
                            mActionClickedListener.onActionClicked(((android.support.v17.leanback.widget.Action) (ibvh.getItem())));
                        }
                    }
                });
            }
        }

        @java.lang.Override
        public void onUnbind(final android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibvh) {
            if ((mViewHolder.getOnItemViewClickedListener() != null) || (mActionClickedListener != null)) {
                ibvh.getPresenter().setOnClickListener(ibvh.getViewHolder(), null);
            }
        }

        @java.lang.Override
        public void onAttachedToWindow(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
            // Remove first to ensure we don't add ourselves more than once.
            viewHolder.itemView.removeOnLayoutChangeListener(mViewHolder.mLayoutChangeListener);
            viewHolder.itemView.addOnLayoutChangeListener(mViewHolder.mLayoutChangeListener);
        }

        @java.lang.Override
        public void onDetachedFromWindow(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
            viewHolder.itemView.removeOnLayoutChangeListener(mViewHolder.mLayoutChangeListener);
            mViewHolder.checkFirstAndLastPosition(false);
        }
    }

    /**
     * A ViewHolder for the DetailsOverviewRow.
     */
    public class ViewHolder extends android.support.v17.leanback.widget.RowPresenter.ViewHolder {
        protected final android.support.v17.leanback.widget.DetailsOverviewRow.Listener mRowListener = createRowListener();

        protected android.support.v17.leanback.widget.DetailsOverviewRow.Listener createRowListener() {
            return new android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder.DetailsOverviewRowListener();
        }

        public class DetailsOverviewRowListener extends android.support.v17.leanback.widget.DetailsOverviewRow.Listener {
            @java.lang.Override
            public void onImageDrawableChanged(android.support.v17.leanback.widget.DetailsOverviewRow row) {
                android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.sHandler.removeCallbacks(mUpdateDrawableCallback);
                android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.sHandler.post(mUpdateDrawableCallback);
            }

            @java.lang.Override
            public void onItemChanged(android.support.v17.leanback.widget.DetailsOverviewRow row) {
                if (mDetailsDescriptionViewHolder != null) {
                    mDetailsPresenter.onUnbindViewHolder(mDetailsDescriptionViewHolder);
                }
                mDetailsPresenter.onBindViewHolder(mDetailsDescriptionViewHolder, row.getItem());
            }

            @java.lang.Override
            public void onActionsAdapterChanged(android.support.v17.leanback.widget.DetailsOverviewRow row) {
                bindActions(row.getActionsAdapter());
            }
        }

        final android.view.ViewGroup mOverviewRoot;

        final android.widget.FrameLayout mOverviewFrame;

        final android.view.ViewGroup mDetailsDescriptionFrame;

        final android.support.v17.leanback.widget.HorizontalGridView mActionsRow;

        final android.support.v17.leanback.widget.Presenter.ViewHolder mDetailsDescriptionViewHolder;

        final android.support.v17.leanback.widget.DetailsOverviewLogoPresenter.ViewHolder mDetailsLogoViewHolder;

        int mNumItems;

        android.support.v17.leanback.widget.ItemBridgeAdapter mActionBridgeAdapter;

        int mState = android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.STATE_HALF;

        final java.lang.Runnable mUpdateDrawableCallback = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                android.support.v17.leanback.widget.Row row = getRow();
                if (row == null) {
                    return;
                }
                mDetailsOverviewLogoPresenter.onBindViewHolder(mDetailsLogoViewHolder, row);
            }
        };

        void bindActions(android.support.v17.leanback.widget.ObjectAdapter adapter) {
            mActionBridgeAdapter.setAdapter(adapter);
            mActionsRow.setAdapter(mActionBridgeAdapter);
            mNumItems = mActionBridgeAdapter.getItemCount();
        }

        void onBind() {
            android.support.v17.leanback.widget.DetailsOverviewRow row = ((android.support.v17.leanback.widget.DetailsOverviewRow) (getRow()));
            bindActions(row.getActionsAdapter());
            row.addListener(mRowListener);
        }

        void onUnbind() {
            android.support.v17.leanback.widget.DetailsOverviewRow row = ((android.support.v17.leanback.widget.DetailsOverviewRow) (getRow()));
            row.removeListener(mRowListener);
            android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.sHandler.removeCallbacks(mUpdateDrawableCallback);
        }

        final android.view.View.OnLayoutChangeListener mLayoutChangeListener = new android.view.View.OnLayoutChangeListener() {
            @java.lang.Override
            public void onLayoutChange(android.view.View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.TAG, "onLayoutChange " + v);

                checkFirstAndLastPosition(false);
            }
        };

        final android.support.v17.leanback.widget.OnChildSelectedListener mChildSelectedListener = new android.support.v17.leanback.widget.OnChildSelectedListener() {
            @java.lang.Override
            public void onChildSelected(android.view.ViewGroup parent, android.view.View view, int position, long id) {
                dispatchItemSelection(view);
            }
        };

        void dispatchItemSelection(android.view.View view) {
            if (!isSelected()) {
                return;
            }
            android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibvh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) ((view != null) ? mActionsRow.getChildViewHolder(view) : mActionsRow.findViewHolderForPosition(mActionsRow.getSelectedPosition())));
            if (ibvh == null) {
                if (getOnItemViewSelectedListener() != null) {
                    getOnItemViewSelectedListener().onItemSelected(null, null, this, getRow());
                }
            } else {
                if (getOnItemViewSelectedListener() != null) {
                    getOnItemViewSelectedListener().onItemSelected(ibvh.getViewHolder(), ibvh.getItem(), this, getRow());
                }
            }
        }

        final android.support.v7.widget.RecyclerView.OnScrollListener mScrollListener = new android.support.v7.widget.RecyclerView.OnScrollListener() {
            @java.lang.Override
            public void onScrollStateChanged(android.support.v7.widget.RecyclerView recyclerView, int newState) {
            }

            @java.lang.Override
            public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
                checkFirstAndLastPosition(true);
            }
        };

        private int getViewCenter(android.view.View view) {
            return (view.getRight() - view.getLeft()) / 2;
        }

        void checkFirstAndLastPosition(boolean fromScroll) {
            android.support.v7.widget.RecyclerView.ViewHolder viewHolder;
            viewHolder = mActionsRow.findViewHolderForPosition(mNumItems - 1);
            boolean showRight = (viewHolder == null) || (viewHolder.itemView.getRight() > mActionsRow.getWidth());
            viewHolder = mActionsRow.findViewHolderForPosition(0);
            boolean showLeft = (viewHolder == null) || (viewHolder.itemView.getLeft() < 0);
            if (android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.DEBUG)
                android.util.Log.v(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.TAG, (((("checkFirstAndLast fromScroll " + fromScroll) + " showRight ") + showRight) + " showLeft ") + showLeft);

        }

        /**
         * Constructor for the ViewHolder.
         *
         * @param rootView
         * 		The root View that this view holder will be attached
         * 		to.
         */
        public ViewHolder(android.view.View rootView, android.support.v17.leanback.widget.Presenter detailsPresenter, android.support.v17.leanback.widget.DetailsOverviewLogoPresenter logoPresenter) {
            super(rootView);
            mOverviewRoot = ((android.view.ViewGroup) (rootView.findViewById(R.id.details_root)));
            mOverviewFrame = ((android.widget.FrameLayout) (rootView.findViewById(R.id.details_frame)));
            mDetailsDescriptionFrame = ((android.view.ViewGroup) (rootView.findViewById(R.id.details_overview_description)));
            mActionsRow = ((android.support.v17.leanback.widget.HorizontalGridView) (mOverviewFrame.findViewById(R.id.details_overview_actions)));
            mActionsRow.setHasOverlappingRendering(false);
            mActionsRow.setOnScrollListener(mScrollListener);
            mActionsRow.setAdapter(mActionBridgeAdapter);
            mActionsRow.setOnChildSelectedListener(mChildSelectedListener);
            final int fadeLength = rootView.getResources().getDimensionPixelSize(R.dimen.lb_details_overview_actions_fade_size);
            mActionsRow.setFadingRightEdgeLength(fadeLength);
            mActionsRow.setFadingLeftEdgeLength(fadeLength);
            mDetailsDescriptionViewHolder = detailsPresenter.onCreateViewHolder(mDetailsDescriptionFrame);
            mDetailsDescriptionFrame.addView(mDetailsDescriptionViewHolder.view);
            mDetailsLogoViewHolder = ((android.support.v17.leanback.widget.DetailsOverviewLogoPresenter.ViewHolder) (logoPresenter.onCreateViewHolder(mOverviewRoot)));
            mOverviewRoot.addView(mDetailsLogoViewHolder.view);
        }

        /**
         * Returns the rectangle area with a color background.
         */
        public final android.view.ViewGroup getOverviewView() {
            return mOverviewFrame;
        }

        /**
         * Returns the ViewHolder for logo.
         */
        public final android.support.v17.leanback.widget.DetailsOverviewLogoPresenter.ViewHolder getLogoViewHolder() {
            return mDetailsLogoViewHolder;
        }

        /**
         * Returns the ViewHolder for DetailsDescription.
         */
        public final android.support.v17.leanback.widget.Presenter.ViewHolder getDetailsDescriptionViewHolder() {
            return mDetailsDescriptionViewHolder;
        }

        /**
         * Returns the root view for inserting details description.
         */
        public final android.view.ViewGroup getDetailsDescriptionFrame() {
            return mDetailsDescriptionFrame;
        }

        /**
         * Returns the view of actions row.
         */
        public final android.view.ViewGroup getActionsRow() {
            return mActionsRow;
        }

        /**
         * Returns current state of the ViewHolder set by
         * {@link FullWidthDetailsOverviewRowPresenter#setState(ViewHolder, int)}.
         */
        public final int getState() {
            return mState;
        }
    }

    protected int mInitialState = android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.STATE_HALF;

    final android.support.v17.leanback.widget.Presenter mDetailsPresenter;

    final android.support.v17.leanback.widget.DetailsOverviewLogoPresenter mDetailsOverviewLogoPresenter;

    android.support.v17.leanback.widget.OnActionClickedListener mActionClickedListener;

    private int mBackgroundColor = android.graphics.Color.TRANSPARENT;

    private int mActionsBackgroundColor = android.graphics.Color.TRANSPARENT;

    private boolean mBackgroundColorSet;

    private boolean mActionsBackgroundColorSet;

    private android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.Listener mListener;

    private boolean mParticipatingEntranceTransition;

    private int mAlignmentMode;

    /**
     * Constructor for a FullWidthDetailsOverviewRowPresenter.
     *
     * @param detailsPresenter
     * 		The {@link Presenter} used to render the detailed
     * 		description of the row.
     */
    public FullWidthDetailsOverviewRowPresenter(android.support.v17.leanback.widget.Presenter detailsPresenter) {
        this(detailsPresenter, new android.support.v17.leanback.widget.DetailsOverviewLogoPresenter());
    }

    /**
     * Constructor for a FullWidthDetailsOverviewRowPresenter.
     *
     * @param detailsPresenter
     * 		The {@link Presenter} used to render the detailed
     * 		description of the row.
     * @param logoPresenter
     * 		The {@link Presenter} used to render the logo view.
     */
    public FullWidthDetailsOverviewRowPresenter(android.support.v17.leanback.widget.Presenter detailsPresenter, android.support.v17.leanback.widget.DetailsOverviewLogoPresenter logoPresenter) {
        setHeaderPresenter(null);
        setSelectEffectEnabled(false);
        mDetailsPresenter = detailsPresenter;
        mDetailsOverviewLogoPresenter = logoPresenter;
    }

    /**
     * Sets the listener for Action click events.
     */
    public void setOnActionClickedListener(android.support.v17.leanback.widget.OnActionClickedListener listener) {
        mActionClickedListener = listener;
    }

    /**
     * Returns the listener for Action click events.
     */
    public android.support.v17.leanback.widget.OnActionClickedListener getOnActionClickedListener() {
        return mActionClickedListener;
    }

    /**
     * Sets the background color.  If not set, a default from the theme will be used.
     */
    public final void setBackgroundColor(int color) {
        mBackgroundColor = color;
        mBackgroundColorSet = true;
    }

    /**
     * Returns the background color.  If {@link #setBackgroundColor(int)}, transparent
     * is returned.
     */
    public final int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * Sets the background color for Action Bar.  If not set, a default from the theme will be
     * used.
     */
    public final void setActionsBackgroundColor(int color) {
        mActionsBackgroundColor = color;
        mActionsBackgroundColorSet = true;
    }

    /**
     * Returns the background color of actions.  If {@link #setActionsBackgroundColor(int)}
     * is not called,  transparent is returned.
     */
    public final int getActionsBackgroundColor() {
        return mActionsBackgroundColor;
    }

    /**
     * Returns true if the overview should be part of shared element transition.
     */
    public final boolean isParticipatingEntranceTransition() {
        return mParticipatingEntranceTransition;
    }

    /**
     * Sets if the overview should be part of shared element transition.
     */
    public final void setParticipatingEntranceTransition(boolean participating) {
        mParticipatingEntranceTransition = participating;
    }

    /**
     * Change the initial state used to create ViewHolder.
     */
    public final void setInitialState(int state) {
        mInitialState = state;
    }

    /**
     * Returns the initial state used to create ViewHolder.
     */
    public final int getInitialState() {
        return mInitialState;
    }

    /**
     * Set alignment mode of Description.
     *
     * @param alignmentMode
     * 		One of {@link #ALIGN_MODE_MIDDLE} or {@link #ALIGN_MODE_START}
     */
    public final void setAlignmentMode(int alignmentMode) {
        mAlignmentMode = alignmentMode;
    }

    /**
     * Returns alignment mode of Description.
     *
     * @return One of {@link #ALIGN_MODE_MIDDLE} or {@link #ALIGN_MODE_START}.
     */
    public final int getAlignmentMode() {
        return mAlignmentMode;
    }

    @java.lang.Override
    protected boolean isClippingChildren() {
        return true;
    }

    /**
     * Set listener for details overview presenter. Must be called before creating
     * ViewHolder.
     */
    public final void setListener(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.Listener listener) {
        mListener = listener;
    }

    /**
     * Get resource id to inflate the layout.  The layout must match {@link #STATE_HALF}
     */
    protected int getLayoutResourceId() {
        return R.layout.lb_fullwidth_details_overview;
    }

    @java.lang.Override
    protected android.support.v17.leanback.widget.RowPresenter.ViewHolder createRowViewHolder(android.view.ViewGroup parent) {
        android.view.View v = android.view.LayoutInflater.from(parent.getContext()).inflate(getLayoutResourceId(), parent, false);
        final android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder vh = new android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder(v, mDetailsPresenter, mDetailsOverviewLogoPresenter);
        mDetailsOverviewLogoPresenter.setContext(vh.mDetailsLogoViewHolder, vh, this);
        setState(vh, mInitialState);
        vh.mActionBridgeAdapter = new android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ActionsItemBridgeAdapter(vh);
        final android.view.View overview = vh.mOverviewFrame;
        if (mBackgroundColorSet) {
            overview.setBackgroundColor(mBackgroundColor);
        }
        if (mActionsBackgroundColorSet) {
            setBackgroundColor(mActionsBackgroundColor);
        }
        android.support.v17.leanback.widget.RoundedRectHelper.getInstance().setClipToRoundedOutline(overview, true);
        if (!getSelectEffectEnabled()) {
            vh.mOverviewFrame.setForeground(null);
        }
        vh.mActionsRow.setOnUnhandledKeyListener(new android.support.v17.leanback.widget.BaseGridView.OnUnhandledKeyListener() {
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
        return vh;
    }

    private static int getNonNegativeWidth(android.graphics.drawable.Drawable drawable) {
        final int width = (drawable == null) ? 0 : drawable.getIntrinsicWidth();
        return width > 0 ? width : 0;
    }

    private static int getNonNegativeHeight(android.graphics.drawable.Drawable drawable) {
        final int height = (drawable == null) ? 0 : drawable.getIntrinsicHeight();
        return height > 0 ? height : 0;
    }

    @java.lang.Override
    protected void onBindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, java.lang.Object item) {
        super.onBindRowViewHolder(holder, item);
        android.support.v17.leanback.widget.DetailsOverviewRow row = ((android.support.v17.leanback.widget.DetailsOverviewRow) (item));
        android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder) (holder));
        mDetailsOverviewLogoPresenter.onBindViewHolder(vh.mDetailsLogoViewHolder, row);
        mDetailsPresenter.onBindViewHolder(vh.mDetailsDescriptionViewHolder, row.getItem());
        vh.onBind();
    }

    @java.lang.Override
    protected void onUnbindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder) {
        android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder) (holder));
        vh.onUnbind();
        mDetailsPresenter.onUnbindViewHolder(vh.mDetailsDescriptionViewHolder);
        mDetailsOverviewLogoPresenter.onUnbindViewHolder(vh.mDetailsLogoViewHolder);
        super.onUnbindRowViewHolder(holder);
    }

    @java.lang.Override
    public final boolean isUsingDefaultSelectEffect() {
        return false;
    }

    @java.lang.Override
    protected void onSelectLevelChanged(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder) {
        super.onSelectLevelChanged(holder);
        if (getSelectEffectEnabled()) {
            android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder) (holder));
            int dimmedColor = vh.mColorDimmer.getPaint().getColor();
            ((android.graphics.drawable.ColorDrawable) (vh.mOverviewFrame.getForeground().mutate())).setColor(dimmedColor);
        }
    }

    @java.lang.Override
    protected void onRowViewAttachedToWindow(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        super.onRowViewAttachedToWindow(vh);
        android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder viewHolder = ((android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder) (vh));
        mDetailsPresenter.onViewAttachedToWindow(viewHolder.mDetailsDescriptionViewHolder);
        mDetailsOverviewLogoPresenter.onViewAttachedToWindow(viewHolder.mDetailsLogoViewHolder);
    }

    @java.lang.Override
    protected void onRowViewDetachedFromWindow(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        super.onRowViewDetachedFromWindow(vh);
        android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder viewHolder = ((android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder) (vh));
        mDetailsPresenter.onViewDetachedFromWindow(viewHolder.mDetailsDescriptionViewHolder);
        mDetailsOverviewLogoPresenter.onViewDetachedFromWindow(viewHolder.mDetailsLogoViewHolder);
    }

    /**
     * Called by {@link DetailsOverviewLogoPresenter} to notify logo was bound to view.
     * Application should not directly call this method.
     *
     * @param viewHolder
     * 		The row ViewHolder that has logo bound to view.
     */
    public final void notifyOnBindLogo(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder viewHolder) {
        onLayoutOverviewFrame(viewHolder, viewHolder.getState(), true);
        onLayoutLogo(viewHolder, viewHolder.getState(), true);
        if (mListener != null) {
            mListener.onBindLogo(viewHolder);
        }
    }

    /**
     * Layout logo position based on current state.  Subclass may override.
     * The method is called when a logo is bound to view or state changes.
     *
     * @param viewHolder
     * 		The row ViewHolder that contains the logo.
     * @param oldState
     * 		The old state,  can be same as current viewHolder.getState()
     * @param logoChanged
     * 		Whether logo was changed.
     */
    protected void onLayoutLogo(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder viewHolder, int oldState, boolean logoChanged) {
        android.view.View v = viewHolder.getLogoViewHolder().view;
        android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (v.getLayoutParams()));
        switch (mAlignmentMode) {
            case android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ALIGN_MODE_START :
            default :
                lp.setMarginStart(v.getResources().getDimensionPixelSize(R.dimen.lb_details_v2_logo_margin_start));
                break;
            case android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ALIGN_MODE_MIDDLE :
                lp.setMarginStart(v.getResources().getDimensionPixelSize(R.dimen.lb_details_v2_left) - lp.width);
                break;
        }
        switch (viewHolder.getState()) {
            case android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.STATE_FULL :
            default :
                lp.topMargin = v.getResources().getDimensionPixelSize(R.dimen.lb_details_v2_blank_height) - (lp.height / 2);
                break;
            case android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.STATE_HALF :
                lp.topMargin = (v.getResources().getDimensionPixelSize(R.dimen.lb_details_v2_blank_height) + v.getResources().getDimensionPixelSize(R.dimen.lb_details_v2_actions_height)) + v.getResources().getDimensionPixelSize(R.dimen.lb_details_v2_description_margin_top);
                break;
            case android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.STATE_SMALL :
                lp.topMargin = 0;
                break;
        }
        v.setLayoutParams(lp);
    }

    /**
     * Layout overview frame based on current state.  Subclass may override.
     * The method is called when a logo is bound to view or state changes.
     *
     * @param viewHolder
     * 		The row ViewHolder that contains the logo.
     * @param oldState
     * 		The old state,  can be same as current viewHolder.getState()
     * @param logoChanged
     * 		Whether logo was changed.
     */
    protected void onLayoutOverviewFrame(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder viewHolder, int oldState, boolean logoChanged) {
        boolean wasBanner = oldState == android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.STATE_SMALL;
        boolean isBanner = viewHolder.getState() == android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.STATE_SMALL;
        if ((wasBanner != isBanner) || logoChanged) {
            android.content.res.Resources res = viewHolder.view.getResources();
            int frameMarginStart;
            int descriptionMarginStart = 0;
            int logoWidth = 0;
            if (mDetailsOverviewLogoPresenter.isBoundToImage(viewHolder.getLogoViewHolder(), ((android.support.v17.leanback.widget.DetailsOverviewRow) (viewHolder.getRow())))) {
                logoWidth = viewHolder.getLogoViewHolder().view.getLayoutParams().width;
            }
            switch (mAlignmentMode) {
                case android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ALIGN_MODE_START :
                default :
                    if (isBanner) {
                        frameMarginStart = res.getDimensionPixelSize(R.dimen.lb_details_v2_logo_margin_start);
                        descriptionMarginStart = logoWidth;
                    } else {
                        frameMarginStart = 0;
                        descriptionMarginStart = logoWidth + res.getDimensionPixelSize(R.dimen.lb_details_v2_logo_margin_start);
                    }
                    break;
                case android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ALIGN_MODE_MIDDLE :
                    if (isBanner) {
                        frameMarginStart = res.getDimensionPixelSize(R.dimen.lb_details_v2_left) - logoWidth;
                        descriptionMarginStart = logoWidth;
                    } else {
                        frameMarginStart = 0;
                        descriptionMarginStart = res.getDimensionPixelSize(R.dimen.lb_details_v2_left);
                    }
                    break;
            }
            android.view.ViewGroup.MarginLayoutParams lpFrame = ((android.view.ViewGroup.MarginLayoutParams) (viewHolder.getOverviewView().getLayoutParams()));
            lpFrame.topMargin = (isBanner) ? 0 : res.getDimensionPixelSize(R.dimen.lb_details_v2_blank_height);
            lpFrame.leftMargin = lpFrame.rightMargin = frameMarginStart;
            viewHolder.getOverviewView().setLayoutParams(lpFrame);
            android.view.View description = viewHolder.getDetailsDescriptionFrame();
            android.view.ViewGroup.MarginLayoutParams lpDesc = ((android.view.ViewGroup.MarginLayoutParams) (description.getLayoutParams()));
            lpDesc.setMarginStart(descriptionMarginStart);
            description.setLayoutParams(lpDesc);
            android.view.View action = viewHolder.getActionsRow();
            android.view.ViewGroup.MarginLayoutParams lpActions = ((android.view.ViewGroup.MarginLayoutParams) (action.getLayoutParams()));
            lpActions.setMarginStart(descriptionMarginStart);
            lpActions.height = (isBanner) ? 0 : res.getDimensionPixelSize(R.dimen.lb_details_v2_actions_height);
            action.setLayoutParams(lpActions);
        }
    }

    /**
     * Switch state of a ViewHolder.
     *
     * @param viewHolder
     * 		The ViewHolder to change state.
     * @param state
     * 		New state, can be {@link #STATE_FULL}, {@link #STATE_HALF}
     * 		or {@link #STATE_SMALL}.
     */
    public final void setState(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder viewHolder, int state) {
        if (viewHolder.getState() != state) {
            int oldState = viewHolder.getState();
            viewHolder.mState = state;
            onStateChanged(viewHolder, oldState);
        }
    }

    /**
     * Called when {@link ViewHolder#getState()} changes.  Subclass may override.
     * The default implementation calls {@link #onLayoutLogo(ViewHolder, int, boolean)} and
     * {@link #onLayoutOverviewFrame(ViewHolder, int, boolean)}.
     *
     * @param viewHolder
     * 		The ViewHolder which state changed.
     * @param oldState
     * 		The old state.
     */
    protected void onStateChanged(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder viewHolder, int oldState) {
        onLayoutOverviewFrame(viewHolder, oldState, false);
        onLayoutLogo(viewHolder, oldState, false);
    }

    @java.lang.Override
    public void setEntranceTransitionState(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, boolean afterEntrance) {
        super.setEntranceTransitionState(holder, afterEntrance);
        if (mParticipatingEntranceTransition) {
            holder.view.setVisibility(afterEntrance ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
        }
    }
}

