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
 * DividerPresenter provides a default presentation for {@link DividerRow} in HeadersFragment.
 */
public class DividerPresenter extends android.support.v17.leanback.widget.Presenter {
    private final int mLayoutResourceId;

    public DividerPresenter() {
        this(R.layout.lb_divider);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public DividerPresenter(int layoutResourceId) {
        mLayoutResourceId = layoutResourceId;
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
        android.view.View headerView = android.view.LayoutInflater.from(parent.getContext()).inflate(mLayoutResourceId, parent, false);
        return new android.support.v17.leanback.widget.Presenter.ViewHolder(headerView);
    }

    @java.lang.Override
    public void onBindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder, java.lang.Object item) {
    }

    @java.lang.Override
    public void onUnbindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder) {
    }
}

