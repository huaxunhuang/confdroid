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
 * Bridge from {@link Presenter} to {@link RecyclerView.Adapter}. Public to allow use by third
 * party Presenters.
 */
public class ItemBridgeAdapter extends android.support.v7.widget.RecyclerView.Adapter implements android.support.v17.leanback.widget.FacetProviderAdapter {
    static final java.lang.String TAG = "ItemBridgeAdapter";

    static final boolean DEBUG = false;

    /**
     * Interface for listening to ViewHolder operations.
     */
    public static class AdapterListener {
        public void onAddPresenter(android.support.v17.leanback.widget.Presenter presenter, int type) {
        }

        public void onCreate(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
        }

        public void onBind(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
        }

        public void onUnbind(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
        }

        public void onAttachedToWindow(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
        }

        public void onDetachedFromWindow(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
        }
    }

    /**
     * Interface for wrapping a view created by a Presenter into another view.
     * The wrapper must be the immediate parent of the wrapped view.
     */
    public static abstract class Wrapper {
        public abstract android.view.View createWrapper(android.view.View root);

        public abstract void wrap(android.view.View wrapper, android.view.View wrapped);
    }

    private android.support.v17.leanback.widget.ObjectAdapter mAdapter;

    android.support.v17.leanback.widget.ItemBridgeAdapter.Wrapper mWrapper;

    private android.support.v17.leanback.widget.PresenterSelector mPresenterSelector;

    android.support.v17.leanback.widget.FocusHighlightHandler mFocusHighlight;

    private android.support.v17.leanback.widget.ItemBridgeAdapter.AdapterListener mAdapterListener;

    private java.util.ArrayList<android.support.v17.leanback.widget.Presenter> mPresenters = new java.util.ArrayList<android.support.v17.leanback.widget.Presenter>();

    final class OnFocusChangeListener implements android.view.View.OnFocusChangeListener {
        android.view.View.OnFocusChangeListener mChainedListener;

        @java.lang.Override
        public void onFocusChange(android.view.View view, boolean hasFocus) {
            if (android.support.v17.leanback.widget.ItemBridgeAdapter.DEBUG)
                android.util.Log.v(android.support.v17.leanback.widget.ItemBridgeAdapter.TAG, (((("onFocusChange " + hasFocus) + " ") + view) + " mFocusHighlight") + mFocusHighlight);

            if (mWrapper != null) {
                view = ((android.view.View) (view.getParent()));
            }
            if (mFocusHighlight != null) {
                mFocusHighlight.onItemFocused(view, hasFocus);
            }
            if (mChainedListener != null) {
                mChainedListener.onFocusChange(view, hasFocus);
            }
        }
    }

    /**
     * ViewHolder for the ItemBridgeAdapter.
     */
    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements android.support.v17.leanback.widget.FacetProvider {
        final android.support.v17.leanback.widget.Presenter mPresenter;

        final android.support.v17.leanback.widget.Presenter.ViewHolder mHolder;

        final android.support.v17.leanback.widget.ItemBridgeAdapter.OnFocusChangeListener mFocusChangeListener = new android.support.v17.leanback.widget.ItemBridgeAdapter.OnFocusChangeListener();

        java.lang.Object mItem;

        java.lang.Object mExtraObject;

        /**
         * Get {@link Presenter}.
         */
        public final android.support.v17.leanback.widget.Presenter getPresenter() {
            return mPresenter;
        }

        /**
         * Get {@link Presenter.ViewHolder}.
         */
        public final android.support.v17.leanback.widget.Presenter.ViewHolder getViewHolder() {
            return mHolder;
        }

        /**
         * Get currently bound object.
         */
        public final java.lang.Object getItem() {
            return mItem;
        }

        /**
         * Get extra object associated with the view.  Developer can attach
         * any customized UI object in addition to {@link Presenter.ViewHolder}.
         * A typical use case is attaching an animator object.
         */
        public final java.lang.Object getExtraObject() {
            return mExtraObject;
        }

        /**
         * Set extra object associated with the view.  Developer can attach
         * any customized UI object in addition to {@link Presenter.ViewHolder}.
         * A typical use case is attaching an animator object.
         */
        public void setExtraObject(java.lang.Object object) {
            mExtraObject = object;
        }

        @java.lang.Override
        public java.lang.Object getFacet(java.lang.Class<?> facetClass) {
            return mHolder.getFacet(facetClass);
        }

        ViewHolder(android.support.v17.leanback.widget.Presenter presenter, android.view.View view, android.support.v17.leanback.widget.Presenter.ViewHolder holder) {
            super(view);
            mPresenter = presenter;
            mHolder = holder;
        }
    }

    private android.support.v17.leanback.widget.ObjectAdapter.DataObserver mDataObserver = new android.support.v17.leanback.widget.ObjectAdapter.DataObserver() {
        @java.lang.Override
        public void onChanged() {
            android.support.v17.leanback.widget.ItemBridgeAdapter.this.notifyDataSetChanged();
        }

        @java.lang.Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            android.support.v17.leanback.widget.ItemBridgeAdapter.this.notifyItemRangeChanged(positionStart, itemCount);
        }

        @java.lang.Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            android.support.v17.leanback.widget.ItemBridgeAdapter.this.notifyItemRangeInserted(positionStart, itemCount);
        }

        @java.lang.Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            android.support.v17.leanback.widget.ItemBridgeAdapter.this.notifyItemRangeRemoved(positionStart, itemCount);
        }
    };

    public ItemBridgeAdapter(android.support.v17.leanback.widget.ObjectAdapter adapter, android.support.v17.leanback.widget.PresenterSelector presenterSelector) {
        setAdapter(adapter);
        mPresenterSelector = presenterSelector;
    }

    public ItemBridgeAdapter(android.support.v17.leanback.widget.ObjectAdapter adapter) {
        this(adapter, null);
    }

    public ItemBridgeAdapter() {
    }

    /**
     * Sets the {@link ObjectAdapter}.
     */
    public void setAdapter(android.support.v17.leanback.widget.ObjectAdapter adapter) {
        if (adapter == mAdapter) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.unregisterObserver(mDataObserver);
        }
        mAdapter = adapter;
        if (mAdapter == null) {
            notifyDataSetChanged();
            return;
        }
        mAdapter.registerObserver(mDataObserver);
        if (hasStableIds() != mAdapter.hasStableIds()) {
            setHasStableIds(mAdapter.hasStableIds());
        }
        notifyDataSetChanged();
    }

    /**
     * Sets the {@link Wrapper}.
     */
    public void setWrapper(android.support.v17.leanback.widget.ItemBridgeAdapter.Wrapper wrapper) {
        mWrapper = wrapper;
    }

    /**
     * Returns the {@link Wrapper}.
     */
    public android.support.v17.leanback.widget.ItemBridgeAdapter.Wrapper getWrapper() {
        return mWrapper;
    }

    void setFocusHighlight(android.support.v17.leanback.widget.FocusHighlightHandler listener) {
        mFocusHighlight = listener;
        if (android.support.v17.leanback.widget.ItemBridgeAdapter.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.ItemBridgeAdapter.TAG, "setFocusHighlight " + mFocusHighlight);

    }

    /**
     * Clears the adapter.
     */
    public void clear() {
        setAdapter(null);
    }

    /**
     * Sets the presenter mapper array.
     */
    public void setPresenterMapper(java.util.ArrayList<android.support.v17.leanback.widget.Presenter> presenters) {
        mPresenters = presenters;
    }

    /**
     * Returns the presenter mapper array.
     */
    public java.util.ArrayList<android.support.v17.leanback.widget.Presenter> getPresenterMapper() {
        return mPresenters;
    }

    @java.lang.Override
    public int getItemCount() {
        return mAdapter != null ? mAdapter.size() : 0;
    }

    @java.lang.Override
    public int getItemViewType(int position) {
        android.support.v17.leanback.widget.PresenterSelector presenterSelector = (mPresenterSelector != null) ? mPresenterSelector : mAdapter.getPresenterSelector();
        java.lang.Object item = mAdapter.get(position);
        android.support.v17.leanback.widget.Presenter presenter = presenterSelector.getPresenter(item);
        int type = mPresenters.indexOf(presenter);
        if (type < 0) {
            mPresenters.add(presenter);
            type = mPresenters.indexOf(presenter);
            if (android.support.v17.leanback.widget.ItemBridgeAdapter.DEBUG)
                android.util.Log.v(android.support.v17.leanback.widget.ItemBridgeAdapter.TAG, (("getItemViewType added presenter " + presenter) + " type ") + type);

            onAddPresenter(presenter, type);
            if (mAdapterListener != null) {
                mAdapterListener.onAddPresenter(presenter, type);
            }
        }
        return type;
    }

    /**
     * Called when presenter is added to Adapter.
     */
    protected void onAddPresenter(android.support.v17.leanback.widget.Presenter presenter, int type) {
    }

    /**
     * Called when ViewHolder is created.
     */
    protected void onCreate(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
    }

    /**
     * Called when ViewHolder has been bound to data.
     */
    protected void onBind(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
    }

    /**
     * Called when ViewHolder has been unbound from data.
     */
    protected void onUnbind(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
    }

    /**
     * Called when ViewHolder has been attached to window.
     */
    protected void onAttachedToWindow(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
    }

    /**
     * Called when ViewHolder has been detached from window.
     */
    protected void onDetachedFromWindow(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder) {
    }

    /**
     * {@link View.OnFocusChangeListener} that assigned in
     * {@link Presenter#onCreateViewHolder(ViewGroup)} may be chained, user should never change
     * {@link View.OnFocusChangeListener} after that.
     */
    @java.lang.Override
    public final android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        if (android.support.v17.leanback.widget.ItemBridgeAdapter.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.ItemBridgeAdapter.TAG, "onCreateViewHolder viewType " + viewType);

        android.support.v17.leanback.widget.Presenter presenter = mPresenters.get(viewType);
        android.support.v17.leanback.widget.Presenter.ViewHolder presenterVh;
        android.view.View view;
        if (mWrapper != null) {
            view = mWrapper.createWrapper(parent);
            presenterVh = presenter.onCreateViewHolder(parent);
            mWrapper.wrap(view, presenterVh.view);
        } else {
            presenterVh = presenter.onCreateViewHolder(parent);
            view = presenterVh.view;
        }
        android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder = new android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder(presenter, view, presenterVh);
        onCreate(viewHolder);
        if (mAdapterListener != null) {
            mAdapterListener.onCreate(viewHolder);
        }
        android.view.View presenterView = viewHolder.mHolder.view;
        if (presenterView != null) {
            viewHolder.mFocusChangeListener.mChainedListener = presenterView.getOnFocusChangeListener();
            presenterView.setOnFocusChangeListener(viewHolder.mFocusChangeListener);
        }
        if (mFocusHighlight != null) {
            mFocusHighlight.onInitializeView(view);
        }
        return viewHolder;
    }

    /**
     * Sets the AdapterListener.
     */
    public void setAdapterListener(android.support.v17.leanback.widget.ItemBridgeAdapter.AdapterListener listener) {
        mAdapterListener = listener;
    }

    @java.lang.Override
    public final void onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder holder, int position) {
        if (android.support.v17.leanback.widget.ItemBridgeAdapter.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.ItemBridgeAdapter.TAG, "onBindViewHolder position " + position);

        android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (holder));
        viewHolder.mItem = mAdapter.get(position);
        viewHolder.mPresenter.onBindViewHolder(viewHolder.mHolder, viewHolder.mItem);
        onBind(viewHolder);
        if (mAdapterListener != null) {
            mAdapterListener.onBind(viewHolder);
        }
    }

    @java.lang.Override
    public final void onViewRecycled(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (holder));
        viewHolder.mPresenter.onUnbindViewHolder(viewHolder.mHolder);
        onUnbind(viewHolder);
        if (mAdapterListener != null) {
            mAdapterListener.onUnbind(viewHolder);
        }
        viewHolder.mItem = null;
    }

    @java.lang.Override
    public final void onViewAttachedToWindow(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (holder));
        onAttachedToWindow(viewHolder);
        if (mAdapterListener != null) {
            mAdapterListener.onAttachedToWindow(viewHolder);
        }
        viewHolder.mPresenter.onViewAttachedToWindow(viewHolder.mHolder);
    }

    @java.lang.Override
    public final void onViewDetachedFromWindow(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder viewHolder = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (holder));
        viewHolder.mPresenter.onViewDetachedFromWindow(viewHolder.mHolder);
        onDetachedFromWindow(viewHolder);
        if (mAdapterListener != null) {
            mAdapterListener.onDetachedFromWindow(viewHolder);
        }
    }

    @java.lang.Override
    public long getItemId(int position) {
        return mAdapter.getId(position);
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.FacetProvider getFacetProvider(int type) {
        return mPresenters.get(type);
    }
}

