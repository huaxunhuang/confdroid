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
 * A presenter that assumes a LinearLayout container for a series
 * of control buttons backed by objects of type {@link Action}.
 *
 * Different layouts may be passed to the presenter constructor.
 * The layout must contain a view with id control_bar.
 */
class ControlBarPresenter extends android.support.v17.leanback.widget.Presenter {
    static final int MAX_CONTROLS = 7;

    /**
     * The data type expected by this presenter.
     */
    static class BoundData {
        /**
         * Adapter containing objects of type {@link Action}.
         */
        android.support.v17.leanback.widget.ObjectAdapter adapter;

        /**
         * The presenter to be used for the adapter objects.
         */
        android.support.v17.leanback.widget.Presenter presenter;
    }

    /**
     * Listener for control selected events.
     */
    interface OnControlSelectedListener {
        void onControlSelected(android.support.v17.leanback.widget.Presenter.ViewHolder controlViewHolder, java.lang.Object item, android.support.v17.leanback.widget.ControlBarPresenter.BoundData data);
    }

    /**
     * Listener for control clicked events.
     */
    interface OnControlClickedListener {
        void onControlClicked(android.support.v17.leanback.widget.Presenter.ViewHolder controlViewHolder, java.lang.Object item, android.support.v17.leanback.widget.ControlBarPresenter.BoundData data);
    }

    class ViewHolder extends android.support.v17.leanback.widget.Presenter.ViewHolder {
        android.support.v17.leanback.widget.ObjectAdapter mAdapter;

        android.support.v17.leanback.widget.ControlBarPresenter.BoundData mData;

        android.support.v17.leanback.widget.Presenter mPresenter;

        android.support.v17.leanback.widget.ControlBar mControlBar;

        android.view.View mControlsContainer;

        android.util.SparseArray<android.support.v17.leanback.widget.Presenter.ViewHolder> mViewHolders = new android.util.SparseArray<android.support.v17.leanback.widget.Presenter.ViewHolder>();

        android.support.v17.leanback.widget.ObjectAdapter.DataObserver mDataObserver;

        /**
         * Constructor for the ViewHolder.
         */
        ViewHolder(android.view.View rootView) {
            super(rootView);
            mControlsContainer = rootView.findViewById(R.id.controls_container);
            mControlBar = ((android.support.v17.leanback.widget.ControlBar) (rootView.findViewById(R.id.control_bar)));
            if (mControlBar == null) {
                throw new java.lang.IllegalStateException("Couldn't find control_bar");
            }
            mControlBar.setOnChildFocusedListener(new android.support.v17.leanback.widget.ControlBar.OnChildFocusedListener() {
                @java.lang.Override
                public void onChildFocusedListener(android.view.View child, android.view.View focused) {
                    if (mOnControlSelectedListener == null) {
                        return;
                    }
                    for (int position = 0; position < mViewHolders.size(); position++) {
                        if (mViewHolders.get(position).view == child) {
                            mOnControlSelectedListener.onControlSelected(mViewHolders.get(position), getDisplayedAdapter().get(position), mData);
                            break;
                        }
                    }
                }
            });
            mDataObserver = new android.support.v17.leanback.widget.ObjectAdapter.DataObserver() {
                @java.lang.Override
                public void onChanged() {
                    if (mAdapter == getDisplayedAdapter()) {
                        showControls(mPresenter);
                    }
                }

                @java.lang.Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    if (mAdapter == getDisplayedAdapter()) {
                        for (int i = 0; i < itemCount; i++) {
                            bindControlToAction(positionStart + i, mPresenter);
                        }
                    }
                }
            };
        }

        int getChildMarginFromCenter(android.content.Context context, int numControls) {
            // Includes margin between icons plus two times half the icon width.
            return getChildMarginDefault(context) + getControlIconWidth(context);
        }

        void showControls(android.support.v17.leanback.widget.Presenter presenter) {
            android.support.v17.leanback.widget.ObjectAdapter adapter = getDisplayedAdapter();
            int adapterSize = (adapter == null) ? 0 : adapter.size();
            // Shrink the number of attached views
            android.view.View focusedView = mControlBar.getFocusedChild();
            if (((focusedView != null) && (adapterSize > 0)) && (mControlBar.indexOfChild(focusedView) >= adapterSize)) {
                mControlBar.getChildAt(adapter.size() - 1).requestFocus();
            }
            for (int i = mControlBar.getChildCount() - 1; i >= adapterSize; i--) {
                mControlBar.removeViewAt(i);
            }
            for (int position = 0; (position < adapterSize) && (position < android.support.v17.leanback.widget.ControlBarPresenter.MAX_CONTROLS); position++) {
                bindControlToAction(position, adapter, presenter);
            }
            mControlBar.setChildMarginFromCenter(getChildMarginFromCenter(mControlBar.getContext(), adapterSize));
        }

        void bindControlToAction(int position, android.support.v17.leanback.widget.Presenter presenter) {
            bindControlToAction(position, getDisplayedAdapter(), presenter);
        }

        private void bindControlToAction(final int position, android.support.v17.leanback.widget.ObjectAdapter adapter, android.support.v17.leanback.widget.Presenter presenter) {
            android.support.v17.leanback.widget.Presenter.ViewHolder vh = mViewHolders.get(position);
            java.lang.Object item = adapter.get(position);
            if (vh == null) {
                vh = presenter.onCreateViewHolder(mControlBar);
                mViewHolders.put(position, vh);
                final android.support.v17.leanback.widget.Presenter.ViewHolder itemViewHolder = vh;
                presenter.setOnClickListener(vh, new android.view.View.OnClickListener() {
                    @java.lang.Override
                    public void onClick(android.view.View v) {
                        java.lang.Object item = getDisplayedAdapter().get(position);
                        if (mOnControlClickedListener != null) {
                            mOnControlClickedListener.onControlClicked(itemViewHolder, item, mData);
                        }
                    }
                });
            }
            if (vh.view.getParent() == null) {
                mControlBar.addView(vh.view);
            }
            presenter.onBindViewHolder(vh, item);
        }

        /**
         * Returns the adapter currently bound to the displayed controls.
         * May be overridden in a subclass.
         */
        android.support.v17.leanback.widget.ObjectAdapter getDisplayedAdapter() {
            return mAdapter;
        }
    }

    android.support.v17.leanback.widget.ControlBarPresenter.OnControlClickedListener mOnControlClickedListener;

    android.support.v17.leanback.widget.ControlBarPresenter.OnControlSelectedListener mOnControlSelectedListener;

    private int mLayoutResourceId;

    private static int sChildMarginDefault;

    private static int sControlIconWidth;

    /**
     * Constructor for a ControlBarPresenter.
     *
     * @param layoutResourceId
     * 		The resource id of the layout for this presenter.
     */
    public ControlBarPresenter(int layoutResourceId) {
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the layout resource id.
     */
    public int getLayoutResourceId() {
        return mLayoutResourceId;
    }

    /**
     * Sets the listener for control clicked events.
     */
    public void setOnControlClickedListener(android.support.v17.leanback.widget.ControlBarPresenter.OnControlClickedListener listener) {
        mOnControlClickedListener = listener;
    }

    /**
     * Returns the listener for control clicked events.
     */
    public android.support.v17.leanback.widget.ControlBarPresenter.OnControlClickedListener getOnItemViewClickedListener() {
        return mOnControlClickedListener;
    }

    /**
     * Sets the listener for control selection.
     */
    public void setOnControlSelectedListener(android.support.v17.leanback.widget.ControlBarPresenter.OnControlSelectedListener listener) {
        mOnControlSelectedListener = listener;
    }

    /**
     * Returns the listener for control selection.
     */
    public android.support.v17.leanback.widget.ControlBarPresenter.OnControlSelectedListener getOnItemControlListener() {
        return mOnControlSelectedListener;
    }

    public void setBackgroundColor(android.support.v17.leanback.widget.ControlBarPresenter.ViewHolder vh, int color) {
        vh.mControlsContainer.setBackgroundColor(color);
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
        android.view.View v = android.view.LayoutInflater.from(parent.getContext()).inflate(getLayoutResourceId(), parent, false);
        return new android.support.v17.leanback.widget.ControlBarPresenter.ViewHolder(v);
    }

    @java.lang.Override
    public void onBindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder holder, java.lang.Object item) {
        android.support.v17.leanback.widget.ControlBarPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.ControlBarPresenter.ViewHolder) (holder));
        android.support.v17.leanback.widget.ControlBarPresenter.BoundData data = ((android.support.v17.leanback.widget.ControlBarPresenter.BoundData) (item));
        if (vh.mAdapter != data.adapter) {
            vh.mAdapter = data.adapter;
            if (vh.mAdapter != null) {
                vh.mAdapter.registerObserver(vh.mDataObserver);
            }
        }
        vh.mPresenter = data.presenter;
        vh.mData = data;
        vh.showControls(vh.mPresenter);
    }

    @java.lang.Override
    public void onUnbindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder holder) {
        android.support.v17.leanback.widget.ControlBarPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.ControlBarPresenter.ViewHolder) (holder));
        if (vh.mAdapter != null) {
            vh.mAdapter.unregisterObserver(vh.mDataObserver);
            vh.mAdapter = null;
        }
        vh.mData = null;
    }

    int getChildMarginDefault(android.content.Context context) {
        if (android.support.v17.leanback.widget.ControlBarPresenter.sChildMarginDefault == 0) {
            android.support.v17.leanback.widget.ControlBarPresenter.sChildMarginDefault = context.getResources().getDimensionPixelSize(R.dimen.lb_playback_controls_child_margin_default);
        }
        return android.support.v17.leanback.widget.ControlBarPresenter.sChildMarginDefault;
    }

    int getControlIconWidth(android.content.Context context) {
        if (android.support.v17.leanback.widget.ControlBarPresenter.sControlIconWidth == 0) {
            android.support.v17.leanback.widget.ControlBarPresenter.sControlIconWidth = context.getResources().getDimensionPixelSize(R.dimen.lb_control_icon_width);
        }
        return android.support.v17.leanback.widget.ControlBarPresenter.sControlIconWidth;
    }
}

