/**
 * This file is auto-generated from HeadersFragment.java.  DO NOT MODIFY.
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
 * An internal fragment containing a list of row headers.
 */
public class HeadersSupportFragment extends android.support.v17.leanback.app.BaseRowSupportFragment {
    /**
     * Interface definition for a callback to be invoked when a header item is clicked.
     */
    public interface OnHeaderClickedListener {
        /**
         * Called when a header item has been clicked.
         *
         * @param viewHolder
         * 		Row ViewHolder object corresponding to the selected Header.
         * @param row
         * 		Row object corresponding to the selected Header.
         */
        void onHeaderClicked(android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder viewHolder, android.support.v17.leanback.widget.Row row);
    }

    /**
     * Interface definition for a callback to be invoked when a header item is selected.
     */
    public interface OnHeaderViewSelectedListener {
        /**
         * Called when a header item has been selected.
         *
         * @param viewHolder
         * 		Row ViewHolder object corresponding to the selected Header.
         * @param row
         * 		Row object corresponding to the selected Header.
         */
        void onHeaderSelected(android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder viewHolder, android.support.v17.leanback.widget.Row row);
    }

    private android.support.v17.leanback.app.HeadersSupportFragment.OnHeaderViewSelectedListener mOnHeaderViewSelectedListener;

    android.support.v17.leanback.app.HeadersSupportFragment.OnHeaderClickedListener mOnHeaderClickedListener;

    private boolean mHeadersEnabled = true;

    private boolean mHeadersGone = false;

    private int mBackgroundColor;

    private boolean mBackgroundColorSet;

    private static final android.support.v17.leanback.widget.PresenterSelector sHeaderPresenter = new android.support.v17.leanback.widget.ClassPresenterSelector().addClassPresenter(android.support.v17.leanback.widget.DividerRow.class, new android.support.v17.leanback.widget.DividerPresenter()).addClassPresenter(android.support.v17.leanback.widget.SectionRow.class, new android.support.v17.leanback.widget.RowHeaderPresenter(R.layout.lb_section_header, false)).addClassPresenter(android.support.v17.leanback.widget.Row.class, new android.support.v17.leanback.widget.RowHeaderPresenter(R.layout.lb_header));

    public HeadersSupportFragment() {
        setPresenterSelector(android.support.v17.leanback.app.HeadersSupportFragment.sHeaderPresenter);
    }

    public void setOnHeaderClickedListener(android.support.v17.leanback.app.HeadersSupportFragment.OnHeaderClickedListener listener) {
        mOnHeaderClickedListener = listener;
    }

    public void setOnHeaderViewSelectedListener(android.support.v17.leanback.app.HeadersSupportFragment.OnHeaderViewSelectedListener listener) {
        mOnHeaderViewSelectedListener = listener;
    }

    @java.lang.Override
    android.support.v17.leanback.widget.VerticalGridView findGridViewFromRoot(android.view.View view) {
        return ((android.support.v17.leanback.widget.VerticalGridView) (view.findViewById(R.id.browse_headers)));
    }

    @java.lang.Override
    void onRowSelected(android.support.v7.widget.RecyclerView parent, android.support.v7.widget.RecyclerView.ViewHolder viewHolder, int position, int subposition) {
        if (mOnHeaderViewSelectedListener != null) {
            if ((viewHolder != null) && (position >= 0)) {
                android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (viewHolder));
                mOnHeaderViewSelectedListener.onHeaderSelected(((android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder) (vh.getViewHolder())), ((android.support.v17.leanback.widget.Row) (vh.getItem())));
            } else {
                mOnHeaderViewSelectedListener.onHeaderSelected(null, null);
            }
        }
    }

    private final android.support.v17.leanback.widget.ItemBridgeAdapter.AdapterListener mAdapterListener = new android.support.v17.leanback.widget.ItemBridgeAdapter.AdapterListener() {
        @java.lang.Override
        public void onCreate(final android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
            android.view.View headerView = viewHolder.getViewHolder().view;
            headerView.setOnClickListener(new android.view.View.OnClickListener() {
                @java.lang.Override
                public void onClick(android.view.View v) {
                    if (mOnHeaderClickedListener != null) {
                        mOnHeaderClickedListener.onHeaderClicked(((android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder) (viewHolder.getViewHolder())), ((android.support.v17.leanback.widget.Row) (viewHolder.getItem())));
                    }
                }
            });
            if (mWrapper != null) {
                viewHolder.itemView.addOnLayoutChangeListener(android.support.v17.leanback.app.HeadersSupportFragment.sLayoutChangeListener);
            } else {
                headerView.addOnLayoutChangeListener(android.support.v17.leanback.app.HeadersSupportFragment.sLayoutChangeListener);
            }
        }
    };

    static android.view.View.OnLayoutChangeListener sLayoutChangeListener = new android.view.View.OnLayoutChangeListener() {
        @java.lang.Override
        public void onLayoutChange(android.view.View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            v.setPivotX(v.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL ? v.getWidth() : 0);
            v.setPivotY(v.getMeasuredHeight() / 2);
        }
    };

    @java.lang.Override
    int getLayoutResourceId() {
        return R.layout.lb_headers_fragment;
    }

    @java.lang.Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final android.support.v17.leanback.widget.VerticalGridView listView = getVerticalGridView();
        if (listView == null) {
            return;
        }
        if (getBridgeAdapter() != null) {
            android.support.v17.leanback.widget.FocusHighlightHelper.setupHeaderItemFocusHighlight(listView);
        }
        if (mBackgroundColorSet) {
            listView.setBackgroundColor(mBackgroundColor);
            updateFadingEdgeToBrandColor(mBackgroundColor);
        } else {
            android.graphics.drawable.Drawable d = listView.getBackground();
            if (d instanceof android.graphics.drawable.ColorDrawable) {
                updateFadingEdgeToBrandColor(((android.graphics.drawable.ColorDrawable) (d)).getColor());
            }
        }
        updateListViewVisibility();
    }

    private void updateListViewVisibility() {
        final android.support.v17.leanback.widget.VerticalGridView listView = getVerticalGridView();
        if (listView != null) {
            getView().setVisibility(mHeadersGone ? android.view.View.GONE : android.view.View.VISIBLE);
            if (!mHeadersGone) {
                if (mHeadersEnabled) {
                    listView.setChildrenVisibility(android.view.View.VISIBLE);
                } else {
                    listView.setChildrenVisibility(android.view.View.INVISIBLE);
                }
            }
        }
    }

    void setHeadersEnabled(boolean enabled) {
        mHeadersEnabled = enabled;
        updateListViewVisibility();
    }

    void setHeadersGone(boolean gone) {
        mHeadersGone = gone;
        updateListViewVisibility();
    }

    static class NoOverlappingFrameLayout extends android.widget.FrameLayout {
        public NoOverlappingFrameLayout(android.content.Context context) {
            super(context);
        }

        /**
         * Avoid creating hardware layer for header dock.
         */
        @java.lang.Override
        public boolean hasOverlappingRendering() {
            return false;
        }
    }

    // Wrapper needed because of conflict between RecyclerView's use of alpha
    // for ADD animations, and RowHeaderPresenter's use of alpha for selected level.
    final android.support.v17.leanback.widget.ItemBridgeAdapter.Wrapper mWrapper = new android.support.v17.leanback.widget.ItemBridgeAdapter.Wrapper() {
        @java.lang.Override
        public void wrap(android.view.View wrapper, android.view.View wrapped) {
            ((android.widget.FrameLayout) (wrapper)).addView(wrapped);
        }

        @java.lang.Override
        public android.view.View createWrapper(android.view.View root) {
            return new android.support.v17.leanback.app.HeadersSupportFragment.NoOverlappingFrameLayout(root.getContext());
        }
    };

    @java.lang.Override
    void updateAdapter() {
        super.updateAdapter();
        android.support.v17.leanback.widget.ItemBridgeAdapter adapter = getBridgeAdapter();
        if (adapter != null) {
            adapter.setAdapterListener(mAdapterListener);
            adapter.setWrapper(mWrapper);
        }
        if ((adapter != null) && (getVerticalGridView() != null)) {
            android.support.v17.leanback.widget.FocusHighlightHelper.setupHeaderItemFocusHighlight(getVerticalGridView());
        }
    }

    void setBackgroundColor(int color) {
        mBackgroundColor = color;
        mBackgroundColorSet = true;
        if (getVerticalGridView() != null) {
            getVerticalGridView().setBackgroundColor(mBackgroundColor);
            updateFadingEdgeToBrandColor(mBackgroundColor);
        }
    }

    private void updateFadingEdgeToBrandColor(int backgroundColor) {
        android.view.View fadingView = getView().findViewById(R.id.fade_out_edge);
        android.graphics.drawable.Drawable background = fadingView.getBackground();
        if (background instanceof android.graphics.drawable.GradientDrawable) {
            background.mutate();
            ((android.graphics.drawable.GradientDrawable) (background)).setColors(new int[]{ android.graphics.Color.TRANSPARENT, backgroundColor });
        }
    }

    @java.lang.Override
    public void onTransitionStart() {
        super.onTransitionStart();
        if (!mHeadersEnabled) {
            // When enabling headers fragment,  the RowHeaderView gets a focus but
            // isShown() is still false because its parent is INVISIBLE, accessibility
            // event is not sent.
            // Workaround is: prevent focus to a child view during transition and put
            // focus on it after transition is done.
            final android.support.v17.leanback.widget.VerticalGridView listView = getVerticalGridView();
            if (listView != null) {
                listView.setDescendantFocusability(android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                if (listView.hasFocus()) {
                    listView.requestFocus();
                }
            }
        }
    }

    @java.lang.Override
    public void onTransitionEnd() {
        if (mHeadersEnabled) {
            final android.support.v17.leanback.widget.VerticalGridView listView = getVerticalGridView();
            if (listView != null) {
                listView.setDescendantFocusability(android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS);
                if (listView.hasFocus()) {
                    listView.requestFocus();
                }
            }
        }
        super.onTransitionEnd();
    }

    public boolean isScrolling() {
        return getVerticalGridView().getScrollState() != android.support.v17.leanback.widget.HorizontalGridView.SCROLL_STATE_IDLE;
    }
}

