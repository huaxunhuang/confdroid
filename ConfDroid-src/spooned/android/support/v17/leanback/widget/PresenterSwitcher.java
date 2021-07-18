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
 * An abstract helper class that switches a view in its parent view using a
 * {@link PresenterSelector}.  A subclass should implement {@link #insertView(View)} to define
 * how to add the view in parent, and may optionally override {@link #onViewSelected(View)}.
 */
public abstract class PresenterSwitcher {
    private android.view.ViewGroup mParent;

    private android.support.v17.leanback.widget.PresenterSelector mPresenterSelector;

    private android.support.v17.leanback.widget.Presenter mCurrentPresenter;

    private android.support.v17.leanback.widget.Presenter.ViewHolder mCurrentViewHolder;

    /**
     * Initializes the switcher with a parent view to insert view into and a
     * {@link PresenterSelector} for choosing a {@link Presenter} for a given object.
     * This will destroy any existing views.
     */
    public void init(android.view.ViewGroup parent, android.support.v17.leanback.widget.PresenterSelector presenterSelector) {
        clear();
        mParent = parent;
        mPresenterSelector = presenterSelector;
    }

    /**
     * Selects a view based on the given object and shows that view.
     */
    public void select(java.lang.Object object) {
        switchView(object);
        showView(true);
    }

    /**
     * Hides the view.
     */
    public void unselect() {
        showView(false);
    }

    /**
     * Returns the parent.
     */
    public final android.view.ViewGroup getParentViewGroup() {
        return mParent;
    }

    private void showView(boolean show) {
        if (mCurrentViewHolder != null) {
            showView(mCurrentViewHolder.view, show);
        }
    }

    private void switchView(java.lang.Object object) {
        android.support.v17.leanback.widget.Presenter presenter = mPresenterSelector.getPresenter(object);
        if (presenter != mCurrentPresenter) {
            showView(false);
            clear();
            mCurrentPresenter = presenter;
            if (mCurrentPresenter == null) {
                return;
            }
            mCurrentViewHolder = mCurrentPresenter.onCreateViewHolder(mParent);
            insertView(mCurrentViewHolder.view);
        } else {
            if (mCurrentPresenter == null) {
                return;
            }
            mCurrentPresenter.onUnbindViewHolder(mCurrentViewHolder);
        }
        mCurrentPresenter.onBindViewHolder(mCurrentViewHolder, object);
        onViewSelected(mCurrentViewHolder.view);
    }

    protected abstract void insertView(android.view.View view);

    /**
     * Called when a view is bound to the object of {@link #select(Object)}.
     */
    protected void onViewSelected(android.view.View view) {
    }

    protected void showView(android.view.View view, boolean visible) {
        view.setVisibility(visible ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    /**
     * Destroys created views.
     */
    public void clear() {
        if (mCurrentPresenter != null) {
            mCurrentPresenter.onUnbindViewHolder(mCurrentViewHolder);
            mParent.removeView(mCurrentViewHolder.view);
            mCurrentViewHolder = null;
            mCurrentPresenter = null;
        }
    }
}

