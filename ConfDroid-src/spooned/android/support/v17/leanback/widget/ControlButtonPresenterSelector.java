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
 * Displays primary and secondary controls for a {@link PlaybackControlsRow}.
 *
 * Binds to items of type {@link Action}.
 */
public class ControlButtonPresenterSelector extends android.support.v17.leanback.widget.PresenterSelector {
    private final android.support.v17.leanback.widget.Presenter mPrimaryPresenter = new android.support.v17.leanback.widget.ControlButtonPresenterSelector.ControlButtonPresenter(R.layout.lb_control_button_primary);

    private final android.support.v17.leanback.widget.Presenter mSecondaryPresenter = new android.support.v17.leanback.widget.ControlButtonPresenterSelector.ControlButtonPresenter(R.layout.lb_control_button_secondary);

    private final android.support.v17.leanback.widget.Presenter[] mPresenters = new android.support.v17.leanback.widget.Presenter[]{ mPrimaryPresenter };

    /**
     * Returns the presenter for primary controls.
     */
    public android.support.v17.leanback.widget.Presenter getPrimaryPresenter() {
        return mPrimaryPresenter;
    }

    /**
     * Returns the presenter for secondary controls.
     */
    public android.support.v17.leanback.widget.Presenter getSecondaryPresenter() {
        return mSecondaryPresenter;
    }

    /**
     * Always returns the presenter for primary controls.
     */
    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter getPresenter(java.lang.Object item) {
        return mPrimaryPresenter;
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter[] getPresenters() {
        return mPresenters;
    }

    static class ActionViewHolder extends android.support.v17.leanback.widget.Presenter.ViewHolder {
        android.widget.ImageView mIcon;

        android.widget.TextView mLabel;

        android.view.View mFocusableView;

        public ActionViewHolder(android.view.View view) {
            super(view);
            mIcon = ((android.widget.ImageView) (view.findViewById(R.id.icon)));
            mLabel = ((android.widget.TextView) (view.findViewById(R.id.label)));
            mFocusableView = view.findViewById(R.id.button);
        }
    }

    static class ControlButtonPresenter extends android.support.v17.leanback.widget.Presenter {
        private int mLayoutResourceId;

        ControlButtonPresenter(int layoutResourceId) {
            mLayoutResourceId = layoutResourceId;
        }

        @java.lang.Override
        public android.support.v17.leanback.widget.Presenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
            android.view.View v = android.view.LayoutInflater.from(parent.getContext()).inflate(mLayoutResourceId, parent, false);
            return new android.support.v17.leanback.widget.ControlButtonPresenterSelector.ActionViewHolder(v);
        }

        @java.lang.Override
        public void onBindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder, java.lang.Object item) {
            android.support.v17.leanback.widget.Action action = ((android.support.v17.leanback.widget.Action) (item));
            android.support.v17.leanback.widget.ControlButtonPresenterSelector.ActionViewHolder vh = ((android.support.v17.leanback.widget.ControlButtonPresenterSelector.ActionViewHolder) (viewHolder));
            vh.mIcon.setImageDrawable(action.getIcon());
            if (vh.mLabel != null) {
                if (action.getIcon() == null) {
                    vh.mLabel.setText(action.getLabel1());
                } else {
                    vh.mLabel.setText(null);
                }
            }
            java.lang.CharSequence contentDescription = (android.text.TextUtils.isEmpty(action.getLabel2())) ? action.getLabel1() : action.getLabel2();
            if (!android.text.TextUtils.equals(vh.mFocusableView.getContentDescription(), contentDescription)) {
                vh.mFocusableView.setContentDescription(contentDescription);
                vh.mFocusableView.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED);
            }
        }

        @java.lang.Override
        public void onUnbindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder) {
            android.support.v17.leanback.widget.ControlButtonPresenterSelector.ActionViewHolder vh = ((android.support.v17.leanback.widget.ControlButtonPresenterSelector.ActionViewHolder) (viewHolder));
            vh.mIcon.setImageDrawable(null);
            if (vh.mLabel != null) {
                vh.mLabel.setText(null);
            }
            vh.mFocusableView.setContentDescription(null);
        }

        @java.lang.Override
        public void setOnClickListener(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder, android.view.View.OnClickListener listener) {
            ((android.support.v17.leanback.widget.ControlButtonPresenterSelector.ActionViewHolder) (viewHolder)).mFocusableView.setOnClickListener(listener);
        }
    }
}

