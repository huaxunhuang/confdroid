/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * The presenter displaying a custom action in {@link AbstractMediaItemPresenter}.
 * This is the default presenter for actions in media rows if no action presenter is provided by the
 * user.
 *
 * Binds to items of type {@link MultiActionsProvider.MultiAction}.
 */
class MediaItemActionPresenter extends android.support.v17.leanback.widget.Presenter {
    MediaItemActionPresenter() {
    }

    static class ViewHolder extends android.support.v17.leanback.widget.Presenter.ViewHolder {
        final android.widget.ImageView mIcon;

        public ViewHolder(android.view.View view) {
            super(view);
            mIcon = ((android.widget.ImageView) (view.findViewById(R.id.actionIcon)));
        }

        public android.widget.ImageView getIcon() {
            return mIcon;
        }
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
        android.content.Context context = parent.getContext();
        android.view.View actionView = android.view.LayoutInflater.from(context).inflate(R.layout.lb_row_media_item_action, parent, false);
        return new android.support.v17.leanback.widget.MediaItemActionPresenter.ViewHolder(actionView);
    }

    @java.lang.Override
    public void onBindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder, java.lang.Object item) {
        android.support.v17.leanback.widget.MediaItemActionPresenter.ViewHolder actionViewHolder = ((android.support.v17.leanback.widget.MediaItemActionPresenter.ViewHolder) (viewHolder));
        android.support.v17.leanback.widget.MultiActionsProvider.MultiAction action = ((android.support.v17.leanback.widget.MultiActionsProvider.MultiAction) (item));
        actionViewHolder.getIcon().setImageDrawable(action.getCurrentDrawable());
    }

    @java.lang.Override
    public void onUnbindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder) {
    }
}

