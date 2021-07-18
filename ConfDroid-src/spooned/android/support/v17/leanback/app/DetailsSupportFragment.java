/**
 * This file is auto-generated from DetailsFragment.java.  DO NOT MODIFY.
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
 * A fragment for creating Leanback details screens.
 *
 * <p>
 * A DetailsSupportFragment renders the elements of its {@link ObjectAdapter} as a set
 * of rows in a vertical list.The Adapter's {@link PresenterSelector} must maintain subclasses
 * of {@link RowPresenter}.
 * </p>
 *
 * When {@link FullWidthDetailsOverviewRowPresenter} is found in adapter,  DetailsSupportFragment will
 * setup default behavior of the DetailsOverviewRow:
 * <li>
 * The alignment of FullWidthDetailsOverviewRowPresenter is setup in
 * {@link #setupDetailsOverviewRowPresenter(FullWidthDetailsOverviewRowPresenter)}.
 * </li>
 * <li>
 * The view status switching of FullWidthDetailsOverviewRowPresenter is done in
 * {@link #onSetDetailsOverviewRowStatus(FullWidthDetailsOverviewRowPresenter,
 * FullWidthDetailsOverviewRowPresenter.ViewHolder, int, int, int)}.
 * </li>
 *
 * <p>
 * The recommended activity themes to use with a DetailsSupportFragment are
 * <li>
 * {@link android.support.v17.leanback.R.style#Theme_Leanback_Details} with activity
 * shared element transition for {@link FullWidthDetailsOverviewRowPresenter}.
 * </li>
 * <li>
 * {@link android.support.v17.leanback.R.style#Theme_Leanback_Details_NoSharedElementTransition}
 * if shared element transition is not needed, for example if first row is not rendered by
 * {@link FullWidthDetailsOverviewRowPresenter}.
 * </li>
 * </p>
 */
public class DetailsSupportFragment extends android.support.v17.leanback.app.BaseSupportFragment {
    static final java.lang.String TAG = "DetailsSupportFragment";

    static boolean DEBUG = false;

    private class SetSelectionRunnable implements java.lang.Runnable {
        int mPosition;

        boolean mSmooth = true;

        SetSelectionRunnable() {
        }

        @java.lang.Override
        public void run() {
            if (mRowsSupportFragment == null) {
                return;
            }
            mRowsSupportFragment.setSelectedPosition(mPosition, mSmooth);
        }
    }

    android.support.v17.leanback.app.RowsSupportFragment mRowsSupportFragment;

    private android.support.v17.leanback.widget.ObjectAdapter mAdapter;

    private int mContainerListAlignTop;

    android.support.v17.leanback.widget.BaseOnItemViewSelectedListener mExternalOnItemViewSelectedListener;

    private android.support.v17.leanback.widget.BaseOnItemViewClickedListener mOnItemViewClickedListener;

    private java.lang.Object mSceneAfterEntranceTransition;

    private final android.support.v17.leanback.app.DetailsSupportFragment.SetSelectionRunnable mSetSelectionRunnable = new android.support.v17.leanback.app.DetailsSupportFragment.SetSelectionRunnable();

    private final android.support.v17.leanback.widget.BaseOnItemViewSelectedListener<java.lang.Object> mOnItemViewSelectedListener = new android.support.v17.leanback.widget.BaseOnItemViewSelectedListener<java.lang.Object>() {
        @java.lang.Override
        public void onItemSelected(android.support.v17.leanback.widget.Presenter.ViewHolder itemViewHolder, java.lang.Object item, android.support.v17.leanback.widget.RowPresenter.ViewHolder rowViewHolder, java.lang.Object row) {
            int position = mRowsSupportFragment.getVerticalGridView().getSelectedPosition();
            int subposition = mRowsSupportFragment.getVerticalGridView().getSelectedSubPosition();
            if (android.support.v17.leanback.app.DetailsSupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.DetailsSupportFragment.TAG, (("row selected position " + position) + " subposition ") + subposition);

            onRowSelected(position, subposition);
            if (mExternalOnItemViewSelectedListener != null) {
                mExternalOnItemViewSelectedListener.onItemSelected(itemViewHolder, item, rowViewHolder, row);
            }
        }
    };

    /**
     * Sets the list of rows for the fragment.
     */
    public void setAdapter(android.support.v17.leanback.widget.ObjectAdapter adapter) {
        mAdapter = adapter;
        android.support.v17.leanback.widget.Presenter[] presenters = adapter.getPresenterSelector().getPresenters();
        if (presenters != null) {
            for (int i = 0; i < presenters.length; i++) {
                setupPresenter(presenters[i]);
            }
        } else {
            android.util.Log.e(android.support.v17.leanback.app.DetailsSupportFragment.TAG, "PresenterSelector.getPresenters() not implemented");
        }
        if (mRowsSupportFragment != null) {
            mRowsSupportFragment.setAdapter(adapter);
        }
    }

    /**
     * Returns the list of rows.
     */
    public android.support.v17.leanback.widget.ObjectAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * Sets an item selection listener.
     */
    public void setOnItemViewSelectedListener(android.support.v17.leanback.widget.BaseOnItemViewSelectedListener listener) {
        mExternalOnItemViewSelectedListener = listener;
    }

    /**
     * Sets an item clicked listener.
     */
    public void setOnItemViewClickedListener(android.support.v17.leanback.widget.BaseOnItemViewClickedListener listener) {
        if (mOnItemViewClickedListener != listener) {
            mOnItemViewClickedListener = listener;
            if (mRowsSupportFragment != null) {
                mRowsSupportFragment.setOnItemViewClickedListener(listener);
            }
        }
    }

    /**
     * Returns the item clicked listener.
     */
    public android.support.v17.leanback.widget.BaseOnItemViewClickedListener getOnItemViewClickedListener() {
        return mOnItemViewClickedListener;
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainerListAlignTop = getResources().getDimensionPixelSize(R.dimen.lb_details_rows_align_top);
    }

    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.lb_details_fragment, container, false);
        android.view.ViewGroup fragment_root = ((android.view.ViewGroup) (view.findViewById(R.id.details_fragment_root)));
        installTitleView(inflater, fragment_root, savedInstanceState);
        mRowsSupportFragment = ((android.support.v17.leanback.app.RowsSupportFragment) (getChildFragmentManager().findFragmentById(R.id.details_rows_dock)));
        if (mRowsSupportFragment == null) {
            mRowsSupportFragment = new android.support.v17.leanback.app.RowsSupportFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.details_rows_dock, mRowsSupportFragment).commit();
        }
        mRowsSupportFragment.setAdapter(mAdapter);
        mRowsSupportFragment.setOnItemViewSelectedListener(mOnItemViewSelectedListener);
        mRowsSupportFragment.setOnItemViewClickedListener(mOnItemViewClickedListener);
        mSceneAfterEntranceTransition = android.support.v17.leanback.transition.TransitionHelper.createScene(((android.view.ViewGroup) (view)), new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mRowsSupportFragment.setEntranceTransitionState(true);
            }
        });
        return view;
    }

    /**
     *
     *
     * @deprecated override {@link #onInflateTitleView(LayoutInflater,ViewGroup,Bundle)} instead.
     */
    @java.lang.Deprecated
    protected android.view.View inflateTitle(android.view.LayoutInflater inflater, android.view.ViewGroup parent, android.os.Bundle savedInstanceState) {
        return super.onInflateTitleView(inflater, parent, savedInstanceState);
    }

    @java.lang.Override
    public android.view.View onInflateTitleView(android.view.LayoutInflater inflater, android.view.ViewGroup parent, android.os.Bundle savedInstanceState) {
        return inflateTitle(inflater, parent, savedInstanceState);
    }

    void setVerticalGridViewLayout(android.support.v17.leanback.widget.VerticalGridView listview) {
        // align the top edge of item to a fixed position
        listview.setItemAlignmentOffset(-mContainerListAlignTop);
        listview.setItemAlignmentOffsetPercent(android.support.v17.leanback.widget.VerticalGridView.ITEM_ALIGN_OFFSET_PERCENT_DISABLED);
        listview.setWindowAlignmentOffset(0);
        listview.setWindowAlignmentOffsetPercent(android.support.v17.leanback.widget.VerticalGridView.WINDOW_ALIGN_OFFSET_PERCENT_DISABLED);
        listview.setWindowAlignment(android.support.v17.leanback.widget.VerticalGridView.WINDOW_ALIGN_NO_EDGE);
    }

    /**
     * Called to setup each Presenter of Adapter passed in {@link #setAdapter(ObjectAdapter)}.  Note
     * that setup should only change the Presenter behavior that is meaningful in DetailsSupportFragment.  For
     * example how a row is aligned in details Fragment.   The default implementation invokes
     * {@link #setupDetailsOverviewRowPresenter(FullWidthDetailsOverviewRowPresenter)}
     */
    protected void setupPresenter(android.support.v17.leanback.widget.Presenter rowPresenter) {
        if (rowPresenter instanceof android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter) {
            setupDetailsOverviewRowPresenter(((android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter) (rowPresenter)));
        }
    }

    /**
     * Called to setup {@link FullWidthDetailsOverviewRowPresenter}.  The default implementation
     * adds two alignment positions({@link ItemAlignmentFacet}) for ViewHolder of
     * FullWidthDetailsOverviewRowPresenter to align in fragment.
     */
    protected void setupDetailsOverviewRowPresenter(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter presenter) {
        android.support.v17.leanback.widget.ItemAlignmentFacet facet = new android.support.v17.leanback.widget.ItemAlignmentFacet();
        // by default align details_frame to half window height
        android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef alignDef1 = new android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef();
        alignDef1.setItemAlignmentViewId(R.id.details_frame);
        alignDef1.setItemAlignmentOffset(-getResources().getDimensionPixelSize(R.dimen.lb_details_v2_align_pos_for_actions));
        alignDef1.setItemAlignmentOffsetPercent(0);
        // when description is selected, align details_frame to top edge
        android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef alignDef2 = new android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef();
        alignDef2.setItemAlignmentViewId(R.id.details_frame);
        alignDef2.setItemAlignmentFocusViewId(R.id.details_overview_description);
        alignDef2.setItemAlignmentOffset(-getResources().getDimensionPixelSize(R.dimen.lb_details_v2_align_pos_for_description));
        alignDef2.setItemAlignmentOffsetPercent(0);
        android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef[] defs = new android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef[]{ alignDef1, alignDef2 };
        facet.setAlignmentDefs(defs);
        presenter.setFacet(android.support.v17.leanback.widget.ItemAlignmentFacet.class, facet);
    }

    android.support.v17.leanback.widget.VerticalGridView getVerticalGridView() {
        return mRowsSupportFragment == null ? null : mRowsSupportFragment.getVerticalGridView();
    }

    /**
     * Gets embedded RowsSupportFragment showing multiple rows for DetailsSupportFragment.  If view of
     * DetailsSupportFragment is not created, the method returns null.
     *
     * @return Embedded RowsSupportFragment showing multiple rows for DetailsSupportFragment.
     */
    public android.support.v17.leanback.app.RowsSupportFragment getRowsSupportFragment() {
        return mRowsSupportFragment;
    }

    /**
     * Setup dimensions that are only meaningful when the child Fragments are inside
     * DetailsSupportFragment.
     */
    private void setupChildFragmentLayout() {
        setVerticalGridViewLayout(mRowsSupportFragment.getVerticalGridView());
    }

    private void setupFocusSearchListener() {
        android.support.v17.leanback.widget.TitleHelper titleHelper = getTitleHelper();
        if (titleHelper != null) {
            android.support.v17.leanback.widget.BrowseFrameLayout browseFrameLayout = ((android.support.v17.leanback.widget.BrowseFrameLayout) (getView().findViewById(R.id.details_fragment_root)));
            browseFrameLayout.setOnFocusSearchListener(titleHelper.getOnFocusSearchListener());
        }
    }

    /**
     * Sets the selected row position with smooth animation.
     */
    public void setSelectedPosition(int position) {
        setSelectedPosition(position, true);
    }

    /**
     * Sets the selected row position.
     */
    public void setSelectedPosition(int position, boolean smooth) {
        mSetSelectionRunnable.mPosition = position;
        mSetSelectionRunnable.mSmooth = smooth;
        if ((getView() != null) && (getView().getHandler() != null)) {
            getView().getHandler().post(mSetSelectionRunnable);
        }
    }

    void onRowSelected(int selectedPosition, int selectedSubPosition) {
        android.support.v17.leanback.widget.ObjectAdapter adapter = getAdapter();
        if (((adapter == null) || (adapter.size() == 0)) || ((selectedPosition == 0) && (selectedSubPosition == 0))) {
            showTitle(true);
        } else {
            showTitle(false);
        }
        if ((adapter != null) && (adapter.size() > selectedPosition)) {
            final android.support.v17.leanback.widget.VerticalGridView gridView = getVerticalGridView();
            final int count = gridView.getChildCount();
            for (int i = 0; i < count; i++) {
                android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder bridgeViewHolder = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (gridView.getChildViewHolder(gridView.getChildAt(i))));
                android.support.v17.leanback.widget.RowPresenter rowPresenter = ((android.support.v17.leanback.widget.RowPresenter) (bridgeViewHolder.getPresenter()));
                onSetRowStatus(rowPresenter, rowPresenter.getRowViewHolder(bridgeViewHolder.getViewHolder()), bridgeViewHolder.getAdapterPosition(), selectedPosition, selectedSubPosition);
            }
        }
    }

    /**
     * Called on every visible row to change view status when current selected row position
     * or selected sub position changed.  Subclass may override.   The default
     * implementation calls {@link #onSetDetailsOverviewRowStatus(FullWidthDetailsOverviewRowPresenter,
     * FullWidthDetailsOverviewRowPresenter.ViewHolder, int, int, int)} if presenter is
     * instance of {@link FullWidthDetailsOverviewRowPresenter}.
     *
     * @param presenter
     * 		The presenter used to create row ViewHolder.
     * @param viewHolder
     * 		The visible (attached) row ViewHolder, note that it may or may not
     * 		be selected.
     * @param adapterPosition
     * 		The adapter position of viewHolder inside adapter.
     * @param selectedPosition
     * 		The adapter position of currently selected row.
     * @param selectedSubPosition
     * 		The sub position within currently selected row.  This is used
     * 		When a row has multiple alignment positions.
     */
    protected void onSetRowStatus(android.support.v17.leanback.widget.RowPresenter presenter, android.support.v17.leanback.widget.RowPresenter.ViewHolder viewHolder, int adapterPosition, int selectedPosition, int selectedSubPosition) {
        if (presenter instanceof android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter) {
            onSetDetailsOverviewRowStatus(((android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter) (presenter)), ((android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder) (viewHolder)), adapterPosition, selectedPosition, selectedSubPosition);
        }
    }

    /**
     * Called to change DetailsOverviewRow view status when current selected row position
     * or selected sub position changed.  Subclass may override.   The default
     * implementation switches between three states based on the positions:
     * {@link FullWidthDetailsOverviewRowPresenter#STATE_HALF},
     * {@link FullWidthDetailsOverviewRowPresenter#STATE_FULL} and
     * {@link FullWidthDetailsOverviewRowPresenter#STATE_SMALL}.
     *
     * @param presenter
     * 		The presenter used to create row ViewHolder.
     * @param viewHolder
     * 		The visible (attached) row ViewHolder, note that it may or may not
     * 		be selected.
     * @param adapterPosition
     * 		The adapter position of viewHolder inside adapter.
     * @param selectedPosition
     * 		The adapter position of currently selected row.
     * @param selectedSubPosition
     * 		The sub position within currently selected row.  This is used
     * 		When a row has multiple alignment positions.
     */
    protected void onSetDetailsOverviewRowStatus(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter presenter, android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder viewHolder, int adapterPosition, int selectedPosition, int selectedSubPosition) {
        if (selectedPosition > adapterPosition) {
            presenter.setState(viewHolder, android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.STATE_HALF);
        } else
            if ((selectedPosition == adapterPosition) && (selectedSubPosition == 1)) {
                presenter.setState(viewHolder, android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.STATE_HALF);
            } else
                if ((selectedPosition == adapterPosition) && (selectedSubPosition == 0)) {
                    presenter.setState(viewHolder, android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.STATE_FULL);
                } else {
                    presenter.setState(viewHolder, android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.STATE_SMALL);
                }


    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        setupChildFragmentLayout();
        setupFocusSearchListener();
        if (isEntranceTransitionEnabled()) {
            mRowsSupportFragment.setEntranceTransitionState(false);
        }
    }

    @java.lang.Override
    protected java.lang.Object createEntranceTransition() {
        return android.support.v17.leanback.transition.TransitionHelper.loadTransition(getActivity(), R.transition.lb_details_enter_transition);
    }

    @java.lang.Override
    protected void runEntranceTransition(java.lang.Object entranceTransition) {
        android.support.v17.leanback.transition.TransitionHelper.runTransition(mSceneAfterEntranceTransition, entranceTransition);
    }

    @java.lang.Override
    protected void onEntranceTransitionEnd() {
        mRowsSupportFragment.onTransitionEnd();
    }

    @java.lang.Override
    protected void onEntranceTransitionPrepare() {
        mRowsSupportFragment.onTransitionPrepare();
    }

    @java.lang.Override
    protected void onEntranceTransitionStart() {
        mRowsSupportFragment.onTransitionStart();
    }
}

