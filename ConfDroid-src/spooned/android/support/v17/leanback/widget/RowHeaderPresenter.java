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
 * RowHeaderPresenter provides a default presentation for {@link HeaderItem} using a
 * {@link RowHeaderView}. If a subclass creates its own view, the subclass must also override
 * {@link #onSelectLevelChanged(ViewHolder)}.
 */
public class RowHeaderPresenter extends android.support.v17.leanback.widget.Presenter {
    private final int mLayoutResourceId;

    private final android.graphics.Paint mFontMeasurePaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);

    private boolean mNullItemVisibilityGone;

    private final boolean mAnimateSelect;

    public RowHeaderPresenter() {
        this(R.layout.lb_row_header);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public RowHeaderPresenter(int layoutResourceId) {
        this(layoutResourceId, true);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public RowHeaderPresenter(int layoutResourceId, boolean animateSelect) {
        mLayoutResourceId = layoutResourceId;
        mAnimateSelect = animateSelect;
    }

    /**
     * Optionally sets the view visibility to {@link View#GONE} when bound to null.
     */
    public void setNullItemVisibilityGone(boolean nullItemVisibilityGone) {
        mNullItemVisibilityGone = nullItemVisibilityGone;
    }

    /**
     * Returns true if the view visibility is set to {@link View#GONE} when bound to null.
     */
    public boolean isNullItemVisibilityGone() {
        return mNullItemVisibilityGone;
    }

    /**
     * A ViewHolder for the RowHeaderPresenter.
     */
    public static class ViewHolder extends android.support.v17.leanback.widget.Presenter.ViewHolder {
        float mSelectLevel;

        int mOriginalTextColor;

        float mUnselectAlpha;

        public ViewHolder(android.view.View view) {
            super(view);
        }

        public final float getSelectLevel() {
            return mSelectLevel;
        }
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
        android.support.v17.leanback.widget.RowHeaderView headerView = ((android.support.v17.leanback.widget.RowHeaderView) (android.view.LayoutInflater.from(parent.getContext()).inflate(mLayoutResourceId, parent, false)));
        android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder viewHolder = new android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder(headerView);
        viewHolder.mOriginalTextColor = headerView.getCurrentTextColor();
        viewHolder.mUnselectAlpha = parent.getResources().getFraction(R.fraction.lb_browse_header_unselect_alpha, 1, 1);
        if (mAnimateSelect) {
            setSelectLevel(viewHolder, 0);
        }
        return viewHolder;
    }

    @java.lang.Override
    public void onBindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder, java.lang.Object item) {
        android.support.v17.leanback.widget.HeaderItem headerItem = (item == null) ? null : ((android.support.v17.leanback.widget.Row) (item)).getHeaderItem();
        if (headerItem == null) {
            ((android.support.v17.leanback.widget.RowHeaderView) (viewHolder.view)).setText(null);
            viewHolder.view.setContentDescription(null);
            if (mNullItemVisibilityGone) {
                viewHolder.view.setVisibility(android.view.View.GONE);
            }
        } else {
            viewHolder.view.setVisibility(android.view.View.VISIBLE);
            ((android.support.v17.leanback.widget.RowHeaderView) (viewHolder.view)).setText(headerItem.getName());
            viewHolder.view.setContentDescription(headerItem.getContentDescription());
        }
    }

    @java.lang.Override
    public void onUnbindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder) {
        ((android.support.v17.leanback.widget.RowHeaderView) (viewHolder.view)).setText(null);
        if (mAnimateSelect) {
            setSelectLevel(((android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder) (viewHolder)), 0);
        }
    }

    /**
     * Sets the select level.
     */
    public final void setSelectLevel(android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder holder, float selectLevel) {
        holder.mSelectLevel = selectLevel;
        onSelectLevelChanged(holder);
    }

    /**
     * Called when the select level changes.  The default implementation sets the alpha on the view.
     */
    protected void onSelectLevelChanged(android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder holder) {
        if (mAnimateSelect) {
            holder.view.setAlpha(holder.mUnselectAlpha + (holder.mSelectLevel * (1.0F - holder.mUnselectAlpha)));
        }
    }

    /**
     * Returns the space (distance in pixels) below the baseline of the
     * text view, if one exists; otherwise, returns 0.
     */
    public int getSpaceUnderBaseline(android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder holder) {
        int space = holder.view.getPaddingBottom();
        if (holder.view instanceof android.widget.TextView) {
            space += ((int) (android.support.v17.leanback.widget.RowHeaderPresenter.getFontDescent(((android.widget.TextView) (holder.view)), mFontMeasurePaint)));
        }
        return space;
    }

    protected static float getFontDescent(android.widget.TextView textView, android.graphics.Paint fontMeasurePaint) {
        if (fontMeasurePaint.getTextSize() != textView.getTextSize()) {
            fontMeasurePaint.setTextSize(textView.getTextSize());
        }
        if (fontMeasurePaint.getTypeface() != textView.getTypeface()) {
            fontMeasurePaint.setTypeface(textView.getTypeface());
        }
        return fontMeasurePaint.descent();
    }
}

