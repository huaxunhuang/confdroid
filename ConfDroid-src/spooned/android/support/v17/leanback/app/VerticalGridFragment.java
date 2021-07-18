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
 * A fragment for creating leanback vertical grids.
 *
 * <p>Renders a vertical grid of objects given a {@link VerticalGridPresenter} and
 * an {@link ObjectAdapter}.
 */
public class VerticalGridFragment extends android.support.v17.leanback.app.BaseFragment {
    static final java.lang.String TAG = "VerticalGridFragment";

    static boolean DEBUG = false;

    private android.support.v17.leanback.widget.ObjectAdapter mAdapter;

    private android.support.v17.leanback.widget.VerticalGridPresenter mGridPresenter;

    android.support.v17.leanback.widget.VerticalGridPresenter.ViewHolder mGridViewHolder;

    android.support.v17.leanback.widget.OnItemViewSelectedListener mOnItemViewSelectedListener;

    private android.support.v17.leanback.widget.OnItemViewClickedListener mOnItemViewClickedListener;

    private java.lang.Object mSceneAfterEntranceTransition;

    private int mSelectedPosition = -1;

    /**
     * Sets the grid presenter.
     */
    public void setGridPresenter(android.support.v17.leanback.widget.VerticalGridPresenter gridPresenter) {
        if (gridPresenter == null) {
            throw new java.lang.IllegalArgumentException("Grid presenter may not be null");
        }
        mGridPresenter = gridPresenter;
        mGridPresenter.setOnItemViewSelectedListener(mViewSelectedListener);
        if (mOnItemViewClickedListener != null) {
            mGridPresenter.setOnItemViewClickedListener(mOnItemViewClickedListener);
        }
    }

    /**
     * Returns the grid presenter.
     */
    public android.support.v17.leanback.widget.VerticalGridPresenter getGridPresenter() {
        return mGridPresenter;
    }

    /**
     * Sets the object adapter for the fragment.
     */
    public void setAdapter(android.support.v17.leanback.widget.ObjectAdapter adapter) {
        mAdapter = adapter;
        updateAdapter();
    }

    /**
     * Returns the object adapter.
     */
    public android.support.v17.leanback.widget.ObjectAdapter getAdapter() {
        return mAdapter;
    }

    private final android.support.v17.leanback.widget.OnItemViewSelectedListener mViewSelectedListener = new android.support.v17.leanback.widget.OnItemViewSelectedListener() {
        @java.lang.Override
        public void onItemSelected(android.support.v17.leanback.widget.Presenter.ViewHolder itemViewHolder, java.lang.Object item, android.support.v17.leanback.widget.RowPresenter.ViewHolder rowViewHolder, android.support.v17.leanback.widget.Row row) {
            int position = mGridViewHolder.getGridView().getSelectedPosition();
            if (android.support.v17.leanback.app.VerticalGridFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.VerticalGridFragment.TAG, "grid selected position " + position);

            gridOnItemSelected(position);
            if (mOnItemViewSelectedListener != null) {
                mOnItemViewSelectedListener.onItemSelected(itemViewHolder, item, rowViewHolder, row);
            }
        }
    };

    private final android.support.v17.leanback.widget.OnChildLaidOutListener mChildLaidOutListener = new android.support.v17.leanback.widget.OnChildLaidOutListener() {
        @java.lang.Override
        public void onChildLaidOut(android.view.ViewGroup parent, android.view.View view, int position, long id) {
            if (position == 0) {
                showOrHideTitle();
            }
        }
    };

    /**
     * Sets an item selection listener.
     */
    public void setOnItemViewSelectedListener(android.support.v17.leanback.widget.OnItemViewSelectedListener listener) {
        mOnItemViewSelectedListener = listener;
    }

    void gridOnItemSelected(int position) {
        if (position != mSelectedPosition) {
            mSelectedPosition = position;
            showOrHideTitle();
        }
    }

    void showOrHideTitle() {
        if (mGridViewHolder.getGridView().findViewHolderForAdapterPosition(mSelectedPosition) == null) {
            return;
        }
        if (!mGridViewHolder.getGridView().hasPreviousViewInSameRow(mSelectedPosition)) {
            showTitle(true);
        } else {
            showTitle(false);
        }
    }

    /**
     * Sets an item clicked listener.
     */
    public void setOnItemViewClickedListener(android.support.v17.leanback.widget.OnItemViewClickedListener listener) {
        mOnItemViewClickedListener = listener;
        if (mGridPresenter != null) {
            mGridPresenter.setOnItemViewClickedListener(mOnItemViewClickedListener);
        }
    }

    /**
     * Returns the item clicked listener.
     */
    public android.support.v17.leanback.widget.OnItemViewClickedListener getOnItemViewClickedListener() {
        return mOnItemViewClickedListener;
    }

    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        android.view.ViewGroup root = ((android.view.ViewGroup) (inflater.inflate(R.layout.lb_vertical_grid_fragment, container, false)));
        android.view.ViewGroup gridFrame = ((android.view.ViewGroup) (root.findViewById(R.id.grid_frame)));
        installTitleView(inflater, gridFrame, savedInstanceState);
        getProgressBarManager().setRootView(root);
        return root;
    }

    @java.lang.Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        android.view.ViewGroup gridDock = ((android.view.ViewGroup) (view.findViewById(R.id.browse_grid_dock)));
        mGridViewHolder = mGridPresenter.onCreateViewHolder(gridDock);
        gridDock.addView(mGridViewHolder.view);
        mGridViewHolder.getGridView().setOnChildLaidOutListener(mChildLaidOutListener);
        mSceneAfterEntranceTransition = android.support.v17.leanback.transition.TransitionHelper.createScene(gridDock, new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                setEntranceTransitionState(true);
            }
        });
        updateAdapter();
    }

    private void setupFocusSearchListener() {
        android.support.v17.leanback.widget.BrowseFrameLayout browseFrameLayout = ((android.support.v17.leanback.widget.BrowseFrameLayout) (getView().findViewById(R.id.grid_frame)));
        browseFrameLayout.setOnFocusSearchListener(getTitleHelper().getOnFocusSearchListener());
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        setupFocusSearchListener();
        if (isEntranceTransitionEnabled()) {
            setEntranceTransitionState(false);
        }
    }

    @java.lang.Override
    public void onDestroyView() {
        super.onDestroyView();
        mGridViewHolder = null;
    }

    /**
     * Sets the selected item position.
     */
    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        if ((mGridViewHolder != null) && (mGridViewHolder.getGridView().getAdapter() != null)) {
            mGridViewHolder.getGridView().setSelectedPositionSmooth(position);
        }
    }

    private void updateAdapter() {
        if (mGridViewHolder != null) {
            mGridPresenter.onBindViewHolder(mGridViewHolder, mAdapter);
            if (mSelectedPosition != (-1)) {
                mGridViewHolder.getGridView().setSelectedPosition(mSelectedPosition);
            }
        }
    }

    @java.lang.Override
    protected java.lang.Object createEntranceTransition() {
        return android.support.v17.leanback.transition.TransitionHelper.loadTransition(getActivity(), R.transition.lb_vertical_grid_entrance_transition);
    }

    @java.lang.Override
    protected void runEntranceTransition(java.lang.Object entranceTransition) {
        android.support.v17.leanback.transition.TransitionHelper.runTransition(mSceneAfterEntranceTransition, entranceTransition);
    }

    void setEntranceTransitionState(boolean afterTransition) {
        mGridPresenter.setEntranceTransitionState(mGridViewHolder, afterTransition);
    }
}

