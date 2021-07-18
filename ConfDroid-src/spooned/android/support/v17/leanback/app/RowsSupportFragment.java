/**
 * This file is auto-generated from RowsFragment.java.  DO NOT MODIFY.
 */
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
package android.support.v17.leanback.app;


/**
 * An ordered set of rows of leanback widgets.
 * <p>
 * A RowsSupportFragment renders the elements of its
 * {@link android.support.v17.leanback.widget.ObjectAdapter} as a set
 * of rows in a vertical list. The Adapter's {@link PresenterSelector} must maintain subclasses
 * of {@link RowPresenter}.
 * </p>
 */
public class RowsSupportFragment extends android.support.v17.leanback.app.BaseRowSupportFragment implements android.support.v17.leanback.app.BrowseSupportFragment.MainFragmentAdapterProvider , android.support.v17.leanback.app.BrowseSupportFragment.MainFragmentRowsAdapterProvider {
    private android.support.v17.leanback.app.RowsSupportFragment.MainFragmentAdapter mMainFragmentAdapter;

    private android.support.v17.leanback.app.RowsSupportFragment.MainFragmentRowsAdapter mMainFragmentRowsAdapter;

    @java.lang.Override
    public android.support.v17.leanback.app.BrowseSupportFragment.MainFragmentAdapter getMainFragmentAdapter() {
        if (mMainFragmentAdapter == null) {
            mMainFragmentAdapter = new android.support.v17.leanback.app.RowsSupportFragment.MainFragmentAdapter(this);
        }
        return mMainFragmentAdapter;
    }

    @java.lang.Override
    public android.support.v17.leanback.app.BrowseSupportFragment.MainFragmentRowsAdapter getMainFragmentRowsAdapter() {
        if (mMainFragmentRowsAdapter == null) {
            mMainFragmentRowsAdapter = new android.support.v17.leanback.app.RowsSupportFragment.MainFragmentRowsAdapter(this);
        }
        return mMainFragmentRowsAdapter;
    }

    /**
     * Internal helper class that manages row select animation and apply a default
     * dim to each row.
     */
    final class RowViewHolderExtra implements android.animation.TimeAnimator.TimeListener {
        final android.support.v17.leanback.widget.RowPresenter mRowPresenter;

        final android.support.v17.leanback.widget.Presenter.ViewHolder mRowViewHolder;

        final android.animation.TimeAnimator mSelectAnimator = new android.animation.TimeAnimator();

        int mSelectAnimatorDurationInUse;

        android.view.animation.Interpolator mSelectAnimatorInterpolatorInUse;

        float mSelectLevelAnimStart;

        float mSelectLevelAnimDelta;

        RowViewHolderExtra(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibvh) {
            mRowPresenter = ((android.support.v17.leanback.widget.RowPresenter) (ibvh.getPresenter()));
            mRowViewHolder = ibvh.getViewHolder();
            mSelectAnimator.setTimeListener(this);
        }

        @java.lang.Override
        public void onTimeUpdate(android.animation.TimeAnimator animation, long totalTime, long deltaTime) {
            if (mSelectAnimator.isRunning()) {
                updateSelect(totalTime, deltaTime);
            }
        }

        void updateSelect(long totalTime, long deltaTime) {
            float fraction;
            if (totalTime >= mSelectAnimatorDurationInUse) {
                fraction = 1;
                mSelectAnimator.end();
            } else {
                fraction = ((float) (totalTime / ((double) (mSelectAnimatorDurationInUse))));
            }
            if (mSelectAnimatorInterpolatorInUse != null) {
                fraction = mSelectAnimatorInterpolatorInUse.getInterpolation(fraction);
            }
            float level = mSelectLevelAnimStart + (fraction * mSelectLevelAnimDelta);
            mRowPresenter.setSelectLevel(mRowViewHolder, level);
        }

        void animateSelect(boolean select, boolean immediate) {
            mSelectAnimator.end();
            final float end = (select) ? 1 : 0;
            if (immediate) {
                mRowPresenter.setSelectLevel(mRowViewHolder, end);
            } else
                if (mRowPresenter.getSelectLevel(mRowViewHolder) != end) {
                    mSelectAnimatorDurationInUse = mSelectAnimatorDuration;
                    mSelectAnimatorInterpolatorInUse = mSelectAnimatorInterpolator;
                    mSelectLevelAnimStart = mRowPresenter.getSelectLevel(mRowViewHolder);
                    mSelectLevelAnimDelta = end - mSelectLevelAnimStart;
                    mSelectAnimator.start();
                }

        }
    }

    static final java.lang.String TAG = "RowsSupportFragment";

    static final boolean DEBUG = false;

    android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder mSelectedViewHolder;

    private int mSubPosition;

    boolean mExpand = true;

    boolean mViewsCreated;

    private int mAlignedTop;

    boolean mAfterEntranceTransition = true;

    android.support.v17.leanback.widget.BaseOnItemViewSelectedListener mOnItemViewSelectedListener;

    android.support.v17.leanback.widget.BaseOnItemViewClickedListener mOnItemViewClickedListener;

    // Select animation and interpolator are not intended to be
    // exposed at this moment. They might be synced with vertical scroll
    // animation later.
    int mSelectAnimatorDuration;

    android.view.animation.Interpolator mSelectAnimatorInterpolator = new android.view.animation.DecelerateInterpolator(2);

    private android.support.v7.widget.RecyclerView.RecycledViewPool mRecycledViewPool;

    private java.util.ArrayList<android.support.v17.leanback.widget.Presenter> mPresenterMapper;

    android.support.v17.leanback.widget.ItemBridgeAdapter.AdapterListener mExternalAdapterListener;

    @java.lang.Override
    protected android.support.v17.leanback.widget.VerticalGridView findGridViewFromRoot(android.view.View view) {
        return ((android.support.v17.leanback.widget.VerticalGridView) (view.findViewById(R.id.container_list)));
    }

    /**
     * Sets an item clicked listener on the fragment.
     * OnItemViewClickedListener will override {@link View.OnClickListener} that
     * item presenter sets during {@link Presenter#onCreateViewHolder(ViewGroup)}.
     * So in general, developer should choose one of the listeners but not both.
     */
    public void setOnItemViewClickedListener(android.support.v17.leanback.widget.BaseOnItemViewClickedListener listener) {
        mOnItemViewClickedListener = listener;
        if (mViewsCreated) {
            throw new java.lang.IllegalStateException("Item clicked listener must be set before views are created");
        }
    }

    /**
     * Returns the item clicked listener.
     */
    public android.support.v17.leanback.widget.BaseOnItemViewClickedListener getOnItemViewClickedListener() {
        return mOnItemViewClickedListener;
    }

    /**
     *
     *
     * @deprecated use {@link BrowseSupportFragment#enableRowScaling(boolean)} instead.
     * @param enable
     * 		true to enable row scaling
     */
    @java.lang.Deprecated
    public void enableRowScaling(boolean enable) {
    }

    /**
     * Set the visibility of titles/hovercard of browse rows.
     */
    public void setExpand(boolean expand) {
        mExpand = expand;
        android.support.v17.leanback.widget.VerticalGridView listView = getVerticalGridView();
        if (listView != null) {
            final int count = listView.getChildCount();
            if (android.support.v17.leanback.app.RowsSupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.RowsSupportFragment.TAG, (("setExpand " + expand) + " count ") + count);

            for (int i = 0; i < count; i++) {
                android.view.View view = listView.getChildAt(i);
                android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (listView.getChildViewHolder(view)));
                android.support.v17.leanback.app.RowsSupportFragment.setRowViewExpanded(vh, mExpand);
            }
        }
    }

    /**
     * Sets an item selection listener.
     */
    public void setOnItemViewSelectedListener(android.support.v17.leanback.widget.BaseOnItemViewSelectedListener listener) {
        mOnItemViewSelectedListener = listener;
        android.support.v17.leanback.widget.VerticalGridView listView = getVerticalGridView();
        if (listView != null) {
            final int count = listView.getChildCount();
            for (int i = 0; i < count; i++) {
                android.view.View view = listView.getChildAt(i);
                android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibvh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (listView.getChildViewHolder(view)));
                android.support.v17.leanback.app.RowsSupportFragment.getRowViewHolder(ibvh).setOnItemViewSelectedListener(mOnItemViewSelectedListener);
            }
        }
    }

    /**
     * Returns an item selection listener.
     */
    public android.support.v17.leanback.widget.BaseOnItemViewSelectedListener getOnItemViewSelectedListener() {
        return mOnItemViewSelectedListener;
    }

    @java.lang.Override
    void onRowSelected(android.support.v7.widget.RecyclerView parent, android.support.v7.widget.RecyclerView.ViewHolder viewHolder, int position, int subposition) {
        if ((mSelectedViewHolder != viewHolder) || (mSubPosition != subposition)) {
            if (android.support.v17.leanback.app.RowsSupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.RowsSupportFragment.TAG, (((("new row selected position " + position) + " subposition ") + subposition) + " view ") + viewHolder.itemView);

            mSubPosition = subposition;
            if (mSelectedViewHolder != null) {
                android.support.v17.leanback.app.RowsSupportFragment.setRowViewSelected(mSelectedViewHolder, false, false);
            }
            mSelectedViewHolder = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (viewHolder));
            if (mSelectedViewHolder != null) {
                android.support.v17.leanback.app.RowsSupportFragment.setRowViewSelected(mSelectedViewHolder, true, false);
            }
        }
        // When RowsSupportFragment is embedded inside a page fragment, we want to show
        // the title view only when we're on the first row or there is no data.
        if (mMainFragmentAdapter != null) {
            mMainFragmentAdapter.getFragmentHost().showTitleView(position <= 0);
        }
    }

    /**
     * Get row ViewHolder at adapter position.  Returns null if the row object is not in adapter or
     * the row object has not been bound to a row view.
     *
     * @param position
     * 		Position of row in adapter.
     * @return Row ViewHolder at a given adapter position.
     */
    public android.support.v17.leanback.widget.RowPresenter.ViewHolder getRowViewHolder(int position) {
        android.support.v17.leanback.widget.VerticalGridView verticalView = getVerticalGridView();
        if (verticalView == null) {
            return null;
        }
        return android.support.v17.leanback.app.RowsSupportFragment.getRowViewHolder(((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (verticalView.findViewHolderForAdapterPosition(position))));
    }

    @java.lang.Override
    int getLayoutResourceId() {
        return R.layout.lb_rows_fragment;
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectAnimatorDuration = getResources().getInteger(R.integer.lb_browse_rows_anim_duration);
    }

    @java.lang.Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState) {
        if (android.support.v17.leanback.app.RowsSupportFragment.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.RowsSupportFragment.TAG, "onViewCreated");

        super.onViewCreated(view, savedInstanceState);
        // Align the top edge of child with id row_content.
        // Need set this for directly using RowsSupportFragment.
        getVerticalGridView().setItemAlignmentViewId(R.id.row_content);
        getVerticalGridView().setSaveChildrenPolicy(android.support.v17.leanback.widget.VerticalGridView.SAVE_LIMITED_CHILD);
        setAlignment(mAlignedTop);
        mRecycledViewPool = null;
        mPresenterMapper = null;
        if (mMainFragmentAdapter != null) {
            mMainFragmentAdapter.getFragmentHost().notifyViewCreated(mMainFragmentAdapter);
        }
    }

    @java.lang.Override
    public void onDestroyView() {
        mViewsCreated = false;
        super.onDestroyView();
    }

    void setExternalAdapterListener(android.support.v17.leanback.widget.ItemBridgeAdapter.AdapterListener listener) {
        mExternalAdapterListener = listener;
    }

    static void setRowViewExpanded(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh, boolean expanded) {
        ((android.support.v17.leanback.widget.RowPresenter) (vh.getPresenter())).setRowViewExpanded(vh.getViewHolder(), expanded);
    }

    static void setRowViewSelected(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh, boolean selected, boolean immediate) {
        android.support.v17.leanback.app.RowsSupportFragment.RowViewHolderExtra extra = ((android.support.v17.leanback.app.RowsSupportFragment.RowViewHolderExtra) (vh.getExtraObject()));
        extra.animateSelect(selected, immediate);
        ((android.support.v17.leanback.widget.RowPresenter) (vh.getPresenter())).setRowViewSelected(vh.getViewHolder(), selected);
    }

    private final android.support.v17.leanback.widget.ItemBridgeAdapter.AdapterListener mBridgeAdapterListener = new android.support.v17.leanback.widget.ItemBridgeAdapter.AdapterListener() {
        @java.lang.Override
        public void onAddPresenter(android.support.v17.leanback.widget.Presenter presenter, int type) {
            if (mExternalAdapterListener != null) {
                mExternalAdapterListener.onAddPresenter(presenter, type);
            }
        }

        @java.lang.Override
        public void onCreate(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh) {
            android.support.v17.leanback.widget.VerticalGridView listView = getVerticalGridView();
            if (listView != null) {
                // set clip children false for slide animation
                listView.setClipChildren(false);
            }
            setupSharedViewPool(vh);
            mViewsCreated = true;
            vh.setExtraObject(new android.support.v17.leanback.app.RowsSupportFragment.RowViewHolderExtra(vh));
            // selected state is initialized to false, then driven by grid view onChildSelected
            // events.  When there is rebind, grid view fires onChildSelected event properly.
            // So we don't need do anything special later in onBind or onAttachedToWindow.
            android.support.v17.leanback.app.RowsSupportFragment.setRowViewSelected(vh, false, true);
            if (mExternalAdapterListener != null) {
                mExternalAdapterListener.onCreate(vh);
            }
        }

        @java.lang.Override
        public void onAttachedToWindow(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh) {
            if (android.support.v17.leanback.app.RowsSupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.RowsSupportFragment.TAG, "onAttachToWindow");

            // All views share the same mExpand value.  When we attach a view to grid view,
            // we should make sure it pick up the latest mExpand value we set early on other
            // attached views.  For no-structure-change update,  the view is rebound to new data,
            // but again it should use the unchanged mExpand value,  so we don't need do any
            // thing in onBind.
            android.support.v17.leanback.app.RowsSupportFragment.setRowViewExpanded(vh, mExpand);
            android.support.v17.leanback.widget.RowPresenter rowPresenter = ((android.support.v17.leanback.widget.RowPresenter) (vh.getPresenter()));
            android.support.v17.leanback.widget.RowPresenter.ViewHolder rowVh = rowPresenter.getRowViewHolder(vh.getViewHolder());
            rowVh.setOnItemViewSelectedListener(mOnItemViewSelectedListener);
            rowVh.setOnItemViewClickedListener(mOnItemViewClickedListener);
            rowPresenter.setEntranceTransitionState(rowVh, mAfterEntranceTransition);
            if (mExternalAdapterListener != null) {
                mExternalAdapterListener.onAttachedToWindow(vh);
            }
        }

        @java.lang.Override
        public void onDetachedFromWindow(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh) {
            if (mSelectedViewHolder == vh) {
                android.support.v17.leanback.app.RowsSupportFragment.setRowViewSelected(mSelectedViewHolder, false, true);
                mSelectedViewHolder = null;
            }
            if (mExternalAdapterListener != null) {
                mExternalAdapterListener.onDetachedFromWindow(vh);
            }
        }

        @java.lang.Override
        public void onBind(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh) {
            if (mExternalAdapterListener != null) {
                mExternalAdapterListener.onBind(vh);
            }
        }

        @java.lang.Override
        public void onUnbind(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh) {
            android.support.v17.leanback.app.RowsSupportFragment.setRowViewSelected(vh, false, true);
            if (mExternalAdapterListener != null) {
                mExternalAdapterListener.onUnbind(vh);
            }
        }
    };

    void setupSharedViewPool(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder bridgeVh) {
        android.support.v17.leanback.widget.RowPresenter rowPresenter = ((android.support.v17.leanback.widget.RowPresenter) (bridgeVh.getPresenter()));
        android.support.v17.leanback.widget.RowPresenter.ViewHolder rowVh = rowPresenter.getRowViewHolder(bridgeVh.getViewHolder());
        if (rowVh instanceof android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) {
            android.support.v17.leanback.widget.HorizontalGridView view = ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (rowVh)).getGridView();
            // Recycled view pool is shared between all list rows
            if (mRecycledViewPool == null) {
                mRecycledViewPool = view.getRecycledViewPool();
            } else {
                view.setRecycledViewPool(mRecycledViewPool);
            }
            android.support.v17.leanback.widget.ItemBridgeAdapter bridgeAdapter = ((android.support.v17.leanback.widget.ListRowPresenter.ViewHolder) (rowVh)).getBridgeAdapter();
            if (mPresenterMapper == null) {
                mPresenterMapper = bridgeAdapter.getPresenterMapper();
            } else {
                bridgeAdapter.setPresenterMapper(mPresenterMapper);
            }
        }
    }

    @java.lang.Override
    void updateAdapter() {
        super.updateAdapter();
        mSelectedViewHolder = null;
        mViewsCreated = false;
        android.support.v17.leanback.widget.ItemBridgeAdapter adapter = getBridgeAdapter();
        if (adapter != null) {
            adapter.setAdapterListener(mBridgeAdapterListener);
        }
    }

    @java.lang.Override
    public boolean onTransitionPrepare() {
        boolean prepared = super.onTransitionPrepare();
        if (prepared) {
            freezeRows(true);
        }
        return prepared;
    }

    @java.lang.Override
    public void onTransitionEnd() {
        super.onTransitionEnd();
        freezeRows(false);
    }

    private void freezeRows(boolean freeze) {
        android.support.v17.leanback.widget.VerticalGridView verticalView = getVerticalGridView();
        if (verticalView != null) {
            final int count = verticalView.getChildCount();
            for (int i = 0; i < count; i++) {
                android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibvh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (verticalView.getChildViewHolder(verticalView.getChildAt(i))));
                android.support.v17.leanback.widget.RowPresenter rowPresenter = ((android.support.v17.leanback.widget.RowPresenter) (ibvh.getPresenter()));
                android.support.v17.leanback.widget.RowPresenter.ViewHolder vh = rowPresenter.getRowViewHolder(ibvh.getViewHolder());
                rowPresenter.freeze(vh, freeze);
            }
        }
    }

    /**
     * For rows that willing to participate entrance transition,  this function
     * hide views if afterTransition is true,  show views if afterTransition is false.
     */
    public void setEntranceTransitionState(boolean afterTransition) {
        mAfterEntranceTransition = afterTransition;
        android.support.v17.leanback.widget.VerticalGridView verticalView = getVerticalGridView();
        if (verticalView != null) {
            final int count = verticalView.getChildCount();
            for (int i = 0; i < count; i++) {
                android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibvh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (verticalView.getChildViewHolder(verticalView.getChildAt(i))));
                android.support.v17.leanback.widget.RowPresenter rowPresenter = ((android.support.v17.leanback.widget.RowPresenter) (ibvh.getPresenter()));
                android.support.v17.leanback.widget.RowPresenter.ViewHolder vh = rowPresenter.getRowViewHolder(ibvh.getViewHolder());
                rowPresenter.setEntranceTransitionState(vh, mAfterEntranceTransition);
            }
        }
    }

    /**
     * Selects a Row and perform an optional task on the Row. For example
     * <code>setSelectedPosition(10, true, new ListRowPresenterSelectItemViewHolderTask(5))</code>
     * Scroll to 11th row and selects 6th item on that row.  The method will be ignored if
     * RowsSupportFragment has not been created (i.e. before {@link #onCreateView(LayoutInflater,
     * ViewGroup, Bundle)}).
     *
     * @param rowPosition
     * 		Which row to select.
     * @param smooth
     * 		True to scroll to the row, false for no animation.
     * @param rowHolderTask
     * 		Task to perform on the Row.
     */
    public void setSelectedPosition(int rowPosition, boolean smooth, final android.support.v17.leanback.widget.Presenter.ViewHolderTask rowHolderTask) {
        android.support.v17.leanback.widget.VerticalGridView verticalView = getVerticalGridView();
        if (verticalView == null) {
            return;
        }
        android.support.v17.leanback.widget.ViewHolderTask task = null;
        if (rowHolderTask != null) {
            // This task will execute once the scroll completes. Once the scrolling finishes,
            // we will get a success callback to update selected row position. Since the
            // update to selected row position happens in a post, we want to ensure that this
            // gets called after that.
            task = new android.support.v17.leanback.widget.ViewHolderTask() {
                @java.lang.Override
                public void run(final android.support.v7.widget.RecyclerView.ViewHolder rvh) {
                    rvh.itemView.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            rowHolderTask.run(android.support.v17.leanback.app.RowsSupportFragment.getRowViewHolder(((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (rvh))));
                        }
                    });
                }
            };
        }
        if (smooth) {
            verticalView.setSelectedPositionSmooth(rowPosition, task);
        } else {
            verticalView.setSelectedPosition(rowPosition, task);
        }
    }

    static android.support.v17.leanback.widget.RowPresenter.ViewHolder getRowViewHolder(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder ibvh) {
        if (ibvh == null) {
            return null;
        }
        android.support.v17.leanback.widget.RowPresenter rowPresenter = ((android.support.v17.leanback.widget.RowPresenter) (ibvh.getPresenter()));
        return rowPresenter.getRowViewHolder(ibvh.getViewHolder());
    }

    public boolean isScrolling() {
        if (getVerticalGridView() == null) {
            return false;
        }
        return getVerticalGridView().getScrollState() != android.support.v17.leanback.widget.HorizontalGridView.SCROLL_STATE_IDLE;
    }

    @java.lang.Override
    public void setAlignment(int windowAlignOffsetFromTop) {
        mAlignedTop = windowAlignOffsetFromTop;
        final android.support.v17.leanback.widget.VerticalGridView gridView = getVerticalGridView();
        if (gridView != null) {
            gridView.setItemAlignmentOffset(0);
            gridView.setItemAlignmentOffsetPercent(android.support.v17.leanback.widget.VerticalGridView.ITEM_ALIGN_OFFSET_PERCENT_DISABLED);
            gridView.setItemAlignmentOffsetWithPadding(true);
            gridView.setWindowAlignmentOffset(mAlignedTop);
            // align to a fixed position from top
            gridView.setWindowAlignmentOffsetPercent(android.support.v17.leanback.widget.VerticalGridView.WINDOW_ALIGN_OFFSET_PERCENT_DISABLED);
            gridView.setWindowAlignment(android.support.v17.leanback.widget.VerticalGridView.WINDOW_ALIGN_NO_EDGE);
        }
    }

    public static class MainFragmentAdapter extends android.support.v17.leanback.app.BrowseSupportFragment.MainFragmentAdapter<android.support.v17.leanback.app.RowsSupportFragment> {
        public MainFragmentAdapter(android.support.v17.leanback.app.RowsSupportFragment fragment) {
            super(fragment);
            setScalingEnabled(true);
        }

        @java.lang.Override
        public boolean isScrolling() {
            return getFragment().isScrolling();
        }

        @java.lang.Override
        public void setExpand(boolean expand) {
            getFragment().setExpand(expand);
        }

        @java.lang.Override
        public void setEntranceTransitionState(boolean state) {
            getFragment().setEntranceTransitionState(state);
        }

        @java.lang.Override
        public void setAlignment(int windowAlignOffsetFromTop) {
            getFragment().setAlignment(windowAlignOffsetFromTop);
        }

        @java.lang.Override
        public boolean onTransitionPrepare() {
            return getFragment().onTransitionPrepare();
        }

        @java.lang.Override
        public void onTransitionStart() {
            getFragment().onTransitionStart();
        }

        @java.lang.Override
        public void onTransitionEnd() {
            getFragment().onTransitionEnd();
        }
    }

    public static class MainFragmentRowsAdapter extends android.support.v17.leanback.app.BrowseSupportFragment.MainFragmentRowsAdapter<android.support.v17.leanback.app.RowsSupportFragment> {
        public MainFragmentRowsAdapter(android.support.v17.leanback.app.RowsSupportFragment fragment) {
            super(fragment);
        }

        @java.lang.Override
        public void setAdapter(android.support.v17.leanback.widget.ObjectAdapter adapter) {
            getFragment().setAdapter(adapter);
        }

        /**
         * Sets an item clicked listener on the fragment.
         */
        @java.lang.Override
        public void setOnItemViewClickedListener(android.support.v17.leanback.widget.OnItemViewClickedListener listener) {
            getFragment().setOnItemViewClickedListener(listener);
        }

        @java.lang.Override
        public void setOnItemViewSelectedListener(android.support.v17.leanback.widget.OnItemViewSelectedListener listener) {
            getFragment().setOnItemViewSelectedListener(listener);
        }

        @java.lang.Override
        public void setSelectedPosition(int rowPosition, boolean smooth, final android.support.v17.leanback.widget.Presenter.ViewHolderTask rowHolderTask) {
            getFragment().setSelectedPosition(rowPosition, smooth, rowHolderTask);
        }

        @java.lang.Override
        public void setSelectedPosition(int rowPosition, boolean smooth) {
            getFragment().setSelectedPosition(rowPosition, smooth);
        }

        @java.lang.Override
        public int getSelectedPosition() {
            return getFragment().getSelectedPosition();
        }
    }
}

