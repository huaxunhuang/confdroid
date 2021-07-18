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
 * Renders a {@link DetailsOverviewRow} to display an overview of an item.
 * Typically this row will be the first row in a fragment
 * such as the {@link android.support.v17.leanback.app.DetailsFragment
 * DetailsFragment}.  The View created by the DetailsOverviewRowPresenter is made in three parts:
 * ImageView on the left, action list view on the bottom and a customizable detailed
 * description view on the right.
 *
 * <p>The detailed description is rendered using a {@link Presenter} passed in
 * {@link #DetailsOverviewRowPresenter(Presenter)}.  Typically this will be an instance of
 * {@link AbstractDetailsDescriptionPresenter}.  The application can access the
 * detailed description ViewHolder from {@link ViewHolder#mDetailsDescriptionViewHolder}.
 * </p>
 *
 * <p>
 * To participate in activity transition, call {@link #setSharedElementEnterTransition(Activity,
 * String)} during Activity's onCreate().
 * </p>
 *
 * <p>
 * Because transition support and layout are fully controlled by DetailsOverviewRowPresenter,
 * developer can not override DetailsOverviewRowPresenter.ViewHolder for adding/replacing views
 * of DetailsOverviewRowPresenter.  If further customization is required beyond replacing
 * the detailed description, the application should create a new row presenter class.
 * </p>
 *
 * @deprecated Use {@link FullWidthDetailsOverviewRowPresenter}
 */
@java.lang.Deprecated
public class DetailsOverviewRowPresenter extends android.support.v17.leanback.widget.RowPresenter {
    static final java.lang.String TAG = "DetailsOverviewRowPresenter";

    static final boolean DEBUG = false;

    private static final int MORE_ACTIONS_FADE_MS = 100;

    private static final long DEFAULT_TIMEOUT = 5000;

    class ActionsItemBridgeAdapter extends android.support.v17.leanback.widget.ItemBridgeAdapter {
        android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder mViewHolder;

        ActionsItemBridgeAdapter(android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder viewHolder) {
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
    public final class ViewHolder extends android.support.v17.leanback.widget.RowPresenter.ViewHolder {
        final android.widget.FrameLayout mOverviewFrame;

        final android.view.ViewGroup mOverviewView;

        final android.widget.ImageView mImageView;

        final android.view.ViewGroup mRightPanel;

        final android.widget.FrameLayout mDetailsDescriptionFrame;

        final android.support.v17.leanback.widget.HorizontalGridView mActionsRow;

        public final android.support.v17.leanback.widget.Presenter.ViewHolder mDetailsDescriptionViewHolder;

        int mNumItems;

        boolean mShowMoreRight;

        boolean mShowMoreLeft;

        android.support.v17.leanback.widget.ItemBridgeAdapter mActionBridgeAdapter;

        final android.os.Handler mHandler = new android.os.Handler();

        final java.lang.Runnable mUpdateDrawableCallback = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                bindImageDrawable(android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder.this);
            }
        };

        final android.support.v17.leanback.widget.DetailsOverviewRow.Listener mListener = new android.support.v17.leanback.widget.DetailsOverviewRow.Listener() {
            @java.lang.Override
            public void onImageDrawableChanged(android.support.v17.leanback.widget.DetailsOverviewRow row) {
                mHandler.removeCallbacks(mUpdateDrawableCallback);
                mHandler.post(mUpdateDrawableCallback);
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
        };

        void bindActions(android.support.v17.leanback.widget.ObjectAdapter adapter) {
            mActionBridgeAdapter.setAdapter(adapter);
            mActionsRow.setAdapter(mActionBridgeAdapter);
            mNumItems = mActionBridgeAdapter.getItemCount();
            mShowMoreRight = false;
            mShowMoreLeft = true;
            showMoreLeft(false);
        }

        final android.view.View.OnLayoutChangeListener mLayoutChangeListener = new android.view.View.OnLayoutChangeListener() {
            @java.lang.Override
            public void onLayoutChange(android.view.View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (android.support.v17.leanback.widget.DetailsOverviewRowPresenter.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.widget.DetailsOverviewRowPresenter.TAG, "onLayoutChange " + v);

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
            if (android.support.v17.leanback.widget.DetailsOverviewRowPresenter.DEBUG)
                android.util.Log.v(android.support.v17.leanback.widget.DetailsOverviewRowPresenter.TAG, (((("checkFirstAndLast fromScroll " + fromScroll) + " showRight ") + showRight) + " showLeft ") + showLeft);

            showMoreRight(showRight);
            showMoreLeft(showLeft);
        }

        private void showMoreLeft(boolean show) {
            if (show != mShowMoreLeft) {
                mActionsRow.setFadingLeftEdge(show);
                mShowMoreLeft = show;
            }
        }

        private void showMoreRight(boolean show) {
            if (show != mShowMoreRight) {
                mActionsRow.setFadingRightEdge(show);
                mShowMoreRight = show;
            }
        }

        /**
         * Constructor for the ViewHolder.
         *
         * @param rootView
         * 		The root View that this view holder will be attached
         * 		to.
         */
        public ViewHolder(android.view.View rootView, android.support.v17.leanback.widget.Presenter detailsPresenter) {
            super(rootView);
            mOverviewFrame = ((android.widget.FrameLayout) (rootView.findViewById(R.id.details_frame)));
            mOverviewView = ((android.view.ViewGroup) (rootView.findViewById(R.id.details_overview)));
            mImageView = ((android.widget.ImageView) (rootView.findViewById(R.id.details_overview_image)));
            mRightPanel = ((android.view.ViewGroup) (rootView.findViewById(R.id.details_overview_right_panel)));
            mDetailsDescriptionFrame = ((android.widget.FrameLayout) (mRightPanel.findViewById(R.id.details_overview_description)));
            mActionsRow = ((android.support.v17.leanback.widget.HorizontalGridView) (mRightPanel.findViewById(R.id.details_overview_actions)));
            mActionsRow.setHasOverlappingRendering(false);
            mActionsRow.setOnScrollListener(mScrollListener);
            mActionsRow.setAdapter(mActionBridgeAdapter);
            mActionsRow.setOnChildSelectedListener(mChildSelectedListener);
            final int fadeLength = rootView.getResources().getDimensionPixelSize(R.dimen.lb_details_overview_actions_fade_size);
            mActionsRow.setFadingRightEdgeLength(fadeLength);
            mActionsRow.setFadingLeftEdgeLength(fadeLength);
            mDetailsDescriptionViewHolder = detailsPresenter.onCreateViewHolder(mDetailsDescriptionFrame);
            mDetailsDescriptionFrame.addView(mDetailsDescriptionViewHolder.view);
        }
    }

    final android.support.v17.leanback.widget.Presenter mDetailsPresenter;

    android.support.v17.leanback.widget.OnActionClickedListener mActionClickedListener;

    private int mBackgroundColor = android.graphics.Color.TRANSPARENT;

    private boolean mBackgroundColorSet;

    private boolean mIsStyleLarge = true;

    private android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper mSharedElementHelper;

    /**
     * Constructor for a DetailsOverviewRowPresenter.
     *
     * @param detailsPresenter
     * 		The {@link Presenter} used to render the detailed
     * 		description of the row.
     */
    public DetailsOverviewRowPresenter(android.support.v17.leanback.widget.Presenter detailsPresenter) {
        setHeaderPresenter(null);
        setSelectEffectEnabled(false);
        mDetailsPresenter = detailsPresenter;
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
     * Sets the layout style to be large or small. This affects the height of
     * the overview, including the text description. The default is large.
     */
    public void setStyleLarge(boolean large) {
        mIsStyleLarge = large;
    }

    /**
     * Returns true if the layout style is large.
     */
    public boolean isStyleLarge() {
        return mIsStyleLarge;
    }

    /**
     * Sets the enter transition of target activity to be
     * transiting into overview row created by this presenter.  The transition will
     * be cancelled if the overview image is not loaded in the timeout period.
     * <p>
     * It assumes shared element passed from calling activity is an ImageView;
     * the shared element transits to overview image on the starting edge of the detail
     * overview row, while bounds of overview row grows and reveals text
     * and action buttons.
     * <p>
     * The method must be invoked in target Activity's onCreate().
     */
    public final void setSharedElementEnterTransition(android.app.Activity activity, java.lang.String sharedElementName, long timeoutMs) {
        if (mSharedElementHelper == null) {
            mSharedElementHelper = new android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper();
        }
        mSharedElementHelper.setSharedElementEnterTransition(activity, sharedElementName, timeoutMs);
    }

    /**
     * Sets the enter transition of target activity to be
     * transiting into overview row created by this presenter.  The transition will
     * be cancelled if overview image is not loaded in a default timeout period.
     * <p>
     * It assumes shared element passed from calling activity is an ImageView;
     * the shared element transits to overview image on the starting edge of the detail
     * overview row, while bounds of overview row grows and reveals text
     * and action buttons.
     * <p>
     * The method must be invoked in target Activity's onCreate().
     */
    public final void setSharedElementEnterTransition(android.app.Activity activity, java.lang.String sharedElementName) {
        setSharedElementEnterTransition(activity, sharedElementName, android.support.v17.leanback.widget.DetailsOverviewRowPresenter.DEFAULT_TIMEOUT);
    }

    private int getDefaultBackgroundColor(android.content.Context context) {
        android.util.TypedValue outValue = new android.util.TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.defaultBrandColor, outValue, true)) {
            return context.getResources().getColor(outValue.resourceId);
        }
        return context.getResources().getColor(R.color.lb_default_brand_color);
    }

    @java.lang.Override
    protected void onRowViewSelected(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh, boolean selected) {
        super.onRowViewSelected(vh, selected);
        if (selected) {
            ((android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder) (vh)).dispatchItemSelection(null);
        }
    }

    @java.lang.Override
    protected android.support.v17.leanback.widget.RowPresenter.ViewHolder createRowViewHolder(android.view.ViewGroup parent) {
        android.view.View v = android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.lb_details_overview, parent, false);
        android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder vh = new android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder(v, mDetailsPresenter);
        initDetailsOverview(vh);
        return vh;
    }

    private int getCardHeight(android.content.Context context) {
        int resId = (mIsStyleLarge) ? R.dimen.lb_details_overview_height_large : R.dimen.lb_details_overview_height_small;
        return context.getResources().getDimensionPixelSize(resId);
    }

    private void initDetailsOverview(final android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder vh) {
        vh.mActionBridgeAdapter = new android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ActionsItemBridgeAdapter(vh);
        final android.view.View overview = vh.mOverviewFrame;
        android.view.ViewGroup.LayoutParams lp = overview.getLayoutParams();
        lp.height = getCardHeight(overview.getContext());
        overview.setLayoutParams(lp);
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
    }

    private static int getNonNegativeWidth(android.graphics.drawable.Drawable drawable) {
        final int width = (drawable == null) ? 0 : drawable.getIntrinsicWidth();
        return width > 0 ? width : 0;
    }

    private static int getNonNegativeHeight(android.graphics.drawable.Drawable drawable) {
        final int height = (drawable == null) ? 0 : drawable.getIntrinsicHeight();
        return height > 0 ? height : 0;
    }

    void bindImageDrawable(android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder vh) {
        android.support.v17.leanback.widget.DetailsOverviewRow row = ((android.support.v17.leanback.widget.DetailsOverviewRow) (vh.getRow()));
        android.view.ViewGroup.MarginLayoutParams layoutParams = ((android.view.ViewGroup.MarginLayoutParams) (vh.mImageView.getLayoutParams()));
        final int cardHeight = getCardHeight(vh.mImageView.getContext());
        final int verticalMargin = vh.mImageView.getResources().getDimensionPixelSize(R.dimen.lb_details_overview_image_margin_vertical);
        final int horizontalMargin = vh.mImageView.getResources().getDimensionPixelSize(R.dimen.lb_details_overview_image_margin_horizontal);
        final int drawableWidth = android.support.v17.leanback.widget.DetailsOverviewRowPresenter.getNonNegativeWidth(row.getImageDrawable());
        final int drawableHeight = android.support.v17.leanback.widget.DetailsOverviewRowPresenter.getNonNegativeHeight(row.getImageDrawable());
        boolean scaleImage = row.isImageScaleUpAllowed();
        boolean useMargin = false;
        if (row.getImageDrawable() != null) {
            boolean landscape = false;
            // If large style and landscape image we always use margin.
            if (drawableWidth > drawableHeight) {
                landscape = true;
                if (mIsStyleLarge) {
                    useMargin = true;
                }
            }
            // If long dimension bigger than the card height we scale down.
            if ((landscape && (drawableWidth > cardHeight)) || ((!landscape) && (drawableHeight > cardHeight))) {
                scaleImage = true;
            }
            // If we're not scaling to fit the card height then we always use margin.
            if (!scaleImage) {
                useMargin = true;
            }
            // If using margin than may need to scale down.
            if (useMargin && (!scaleImage)) {
                if (landscape && (drawableWidth > (cardHeight - horizontalMargin))) {
                    scaleImage = true;
                } else
                    if ((!landscape) && (drawableHeight > (cardHeight - (2 * verticalMargin)))) {
                        scaleImage = true;
                    }

            }
        }
        final int bgColor = (mBackgroundColorSet) ? mBackgroundColor : getDefaultBackgroundColor(vh.mOverviewView.getContext());
        if (useMargin) {
            layoutParams.setMarginStart(horizontalMargin);
            layoutParams.topMargin = layoutParams.bottomMargin = verticalMargin;
            vh.mOverviewFrame.setBackgroundColor(bgColor);
            vh.mRightPanel.setBackground(null);
            vh.mImageView.setBackground(null);
        } else {
            layoutParams.leftMargin = layoutParams.topMargin = layoutParams.bottomMargin = 0;
            vh.mRightPanel.setBackgroundColor(bgColor);
            vh.mImageView.setBackgroundColor(bgColor);
            vh.mOverviewFrame.setBackground(null);
        }
        android.support.v17.leanback.widget.RoundedRectHelper.getInstance().setClipToRoundedOutline(vh.mOverviewFrame, true);
        if (scaleImage) {
            vh.mImageView.setScaleType(android.widget.ImageView.ScaleType.FIT_START);
            vh.mImageView.setAdjustViewBounds(true);
            vh.mImageView.setMaxWidth(cardHeight);
            layoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            vh.mImageView.setScaleType(android.widget.ImageView.ScaleType.CENTER);
            vh.mImageView.setAdjustViewBounds(false);
            layoutParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
            // Limit width to the card height
            layoutParams.width = java.lang.Math.min(cardHeight, drawableWidth);
        }
        vh.mImageView.setLayoutParams(layoutParams);
        vh.mImageView.setImageDrawable(row.getImageDrawable());
        if ((row.getImageDrawable() != null) && (mSharedElementHelper != null)) {
            mSharedElementHelper.onBindToDrawable(vh);
        }
    }

    @java.lang.Override
    protected void onBindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder, java.lang.Object item) {
        super.onBindRowViewHolder(holder, item);
        android.support.v17.leanback.widget.DetailsOverviewRow row = ((android.support.v17.leanback.widget.DetailsOverviewRow) (item));
        android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder) (holder));
        bindImageDrawable(vh);
        mDetailsPresenter.onBindViewHolder(vh.mDetailsDescriptionViewHolder, row.getItem());
        vh.bindActions(row.getActionsAdapter());
        row.addListener(vh.mListener);
    }

    @java.lang.Override
    protected void onUnbindRowViewHolder(android.support.v17.leanback.widget.RowPresenter.ViewHolder holder) {
        android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder) (holder));
        android.support.v17.leanback.widget.DetailsOverviewRow dor = ((android.support.v17.leanback.widget.DetailsOverviewRow) (vh.getRow()));
        dor.removeListener(vh.mListener);
        if (vh.mDetailsDescriptionViewHolder != null) {
            mDetailsPresenter.onUnbindViewHolder(vh.mDetailsDescriptionViewHolder);
        }
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
            android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder) (holder));
            int dimmedColor = vh.mColorDimmer.getPaint().getColor();
            ((android.graphics.drawable.ColorDrawable) (vh.mOverviewFrame.getForeground().mutate())).setColor(dimmedColor);
        }
    }

    @java.lang.Override
    protected void onRowViewAttachedToWindow(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        super.onRowViewAttachedToWindow(vh);
        if (mDetailsPresenter != null) {
            mDetailsPresenter.onViewAttachedToWindow(((android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder) (vh)).mDetailsDescriptionViewHolder);
        }
    }

    @java.lang.Override
    protected void onRowViewDetachedFromWindow(android.support.v17.leanback.widget.RowPresenter.ViewHolder vh) {
        super.onRowViewDetachedFromWindow(vh);
        if (mDetailsPresenter != null) {
            mDetailsPresenter.onViewDetachedFromWindow(((android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder) (vh)).mDetailsDescriptionViewHolder);
        }
    }
}

