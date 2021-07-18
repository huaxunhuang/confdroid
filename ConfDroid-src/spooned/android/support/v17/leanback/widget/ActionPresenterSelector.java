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


class ActionPresenterSelector extends android.support.v17.leanback.widget.PresenterSelector {
    private final android.support.v17.leanback.widget.Presenter mOneLineActionPresenter = new android.support.v17.leanback.widget.ActionPresenterSelector.OneLineActionPresenter();

    private final android.support.v17.leanback.widget.Presenter mTwoLineActionPresenter = new android.support.v17.leanback.widget.ActionPresenterSelector.TwoLineActionPresenter();

    private final android.support.v17.leanback.widget.Presenter[] mPresenters = new android.support.v17.leanback.widget.Presenter[]{ mOneLineActionPresenter, mTwoLineActionPresenter };

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter getPresenter(java.lang.Object item) {
        android.support.v17.leanback.widget.Action action = ((android.support.v17.leanback.widget.Action) (item));
        if (android.text.TextUtils.isEmpty(action.getLabel2())) {
            return mOneLineActionPresenter;
        } else {
            return mTwoLineActionPresenter;
        }
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter[] getPresenters() {
        return mPresenters;
    }

    static class ActionViewHolder extends android.support.v17.leanback.widget.Presenter.ViewHolder {
        android.support.v17.leanback.widget.Action mAction;

        android.widget.Button mButton;

        int mLayoutDirection;

        public ActionViewHolder(android.view.View view, int layoutDirection) {
            super(view);
            mButton = ((android.widget.Button) (view.findViewById(R.id.lb_action_button)));
            mLayoutDirection = layoutDirection;
        }
    }

    class OneLineActionPresenter extends android.support.v17.leanback.widget.Presenter {
        @java.lang.Override
        public android.support.v17.leanback.widget.Presenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
            android.view.View v = android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.lb_action_1_line, parent, false);
            return new android.support.v17.leanback.widget.ActionPresenterSelector.ActionViewHolder(v, parent.getLayoutDirection());
        }

        @java.lang.Override
        public void onBindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder, java.lang.Object item) {
            android.support.v17.leanback.widget.Action action = ((android.support.v17.leanback.widget.Action) (item));
            android.support.v17.leanback.widget.ActionPresenterSelector.ActionViewHolder vh = ((android.support.v17.leanback.widget.ActionPresenterSelector.ActionViewHolder) (viewHolder));
            vh.mAction = action;
            vh.mButton.setText(action.getLabel1());
        }

        @java.lang.Override
        public void onUnbindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder) {
            ((android.support.v17.leanback.widget.ActionPresenterSelector.ActionViewHolder) (viewHolder)).mAction = null;
        }
    }

    class TwoLineActionPresenter extends android.support.v17.leanback.widget.Presenter {
        @java.lang.Override
        public android.support.v17.leanback.widget.Presenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
            android.view.View v = android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.lb_action_2_lines, parent, false);
            return new android.support.v17.leanback.widget.ActionPresenterSelector.ActionViewHolder(v, parent.getLayoutDirection());
        }

        @java.lang.Override
        public void onBindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder, java.lang.Object item) {
            android.support.v17.leanback.widget.Action action = ((android.support.v17.leanback.widget.Action) (item));
            android.support.v17.leanback.widget.ActionPresenterSelector.ActionViewHolder vh = ((android.support.v17.leanback.widget.ActionPresenterSelector.ActionViewHolder) (viewHolder));
            android.graphics.drawable.Drawable icon = action.getIcon();
            vh.mAction = action;
            if (icon != null) {
                final int startPadding = vh.view.getResources().getDimensionPixelSize(R.dimen.lb_action_with_icon_padding_start);
                final int endPadding = vh.view.getResources().getDimensionPixelSize(R.dimen.lb_action_with_icon_padding_end);
                vh.view.setPaddingRelative(startPadding, 0, endPadding, 0);
            } else {
                final int padding = vh.view.getResources().getDimensionPixelSize(R.dimen.lb_action_padding_horizontal);
                vh.view.setPaddingRelative(padding, 0, padding, 0);
            }
            if (vh.mLayoutDirection == android.view.View.LAYOUT_DIRECTION_RTL) {
                vh.mButton.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
            } else {
                vh.mButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
            }
            java.lang.CharSequence line1 = action.getLabel1();
            java.lang.CharSequence line2 = action.getLabel2();
            if (android.text.TextUtils.isEmpty(line1)) {
                vh.mButton.setText(line2);
            } else
                if (android.text.TextUtils.isEmpty(line2)) {
                    vh.mButton.setText(line1);
                } else {
                    vh.mButton.setText((line1 + "\n") + line2);
                }

        }

        @java.lang.Override
        public void onUnbindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder) {
            android.support.v17.leanback.widget.ActionPresenterSelector.ActionViewHolder vh = ((android.support.v17.leanback.widget.ActionPresenterSelector.ActionViewHolder) (viewHolder));
            vh.mButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            vh.view.setPadding(0, 0, 0, 0);
            vh.mAction = null;
        }
    }
}

