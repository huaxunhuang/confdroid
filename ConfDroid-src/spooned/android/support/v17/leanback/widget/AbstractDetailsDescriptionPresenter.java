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
 * An abstract {@link Presenter} for rendering a detailed description of an
 * item. Typically this Presenter will be used in a {@link DetailsOverviewRowPresenter}
 * or {@link PlaybackControlsRowPresenter}.
 *
 * <p>Subclasses must override {@link #onBindDescription} to implement the data
 * binding for this Presenter.
 */
public abstract class AbstractDetailsDescriptionPresenter extends android.support.v17.leanback.widget.Presenter {
    /**
     * The ViewHolder for the {@link AbstractDetailsDescriptionPresenter}.
     */
    public static class ViewHolder extends android.support.v17.leanback.widget.Presenter.ViewHolder {
        final android.widget.TextView mTitle;

        final android.widget.TextView mSubtitle;

        final android.widget.TextView mBody;

        final int mTitleMargin;

        final int mUnderTitleBaselineMargin;

        final int mUnderSubtitleBaselineMargin;

        final int mTitleLineSpacing;

        final int mBodyLineSpacing;

        final int mBodyMaxLines;

        final int mBodyMinLines;

        final android.graphics.Paint.FontMetricsInt mTitleFontMetricsInt;

        final android.graphics.Paint.FontMetricsInt mSubtitleFontMetricsInt;

        final android.graphics.Paint.FontMetricsInt mBodyFontMetricsInt;

        final int mTitleMaxLines;

        private android.view.ViewTreeObserver.OnPreDrawListener mPreDrawListener;

        public ViewHolder(final android.view.View view) {
            super(view);
            mTitle = ((android.widget.TextView) (view.findViewById(R.id.lb_details_description_title)));
            mSubtitle = ((android.widget.TextView) (view.findViewById(R.id.lb_details_description_subtitle)));
            mBody = ((android.widget.TextView) (view.findViewById(R.id.lb_details_description_body)));
            android.graphics.Paint.FontMetricsInt titleFontMetricsInt = getFontMetricsInt(mTitle);
            final int titleAscent = view.getResources().getDimensionPixelSize(R.dimen.lb_details_description_title_baseline);
            // Ascent is negative
            mTitleMargin = titleAscent + titleFontMetricsInt.ascent;
            mUnderTitleBaselineMargin = view.getResources().getDimensionPixelSize(R.dimen.lb_details_description_under_title_baseline_margin);
            mUnderSubtitleBaselineMargin = view.getResources().getDimensionPixelSize(R.dimen.lb_details_description_under_subtitle_baseline_margin);
            mTitleLineSpacing = view.getResources().getDimensionPixelSize(R.dimen.lb_details_description_title_line_spacing);
            mBodyLineSpacing = view.getResources().getDimensionPixelSize(R.dimen.lb_details_description_body_line_spacing);
            mBodyMaxLines = view.getResources().getInteger(R.integer.lb_details_description_body_max_lines);
            mBodyMinLines = view.getResources().getInteger(R.integer.lb_details_description_body_min_lines);
            mTitleMaxLines = mTitle.getMaxLines();
            mTitleFontMetricsInt = getFontMetricsInt(mTitle);
            mSubtitleFontMetricsInt = getFontMetricsInt(mSubtitle);
            mBodyFontMetricsInt = getFontMetricsInt(mBody);
            mTitle.addOnLayoutChangeListener(new android.view.View.OnLayoutChangeListener() {
                @java.lang.Override
                public void onLayoutChange(android.view.View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    addPreDrawListener();
                }
            });
        }

        void addPreDrawListener() {
            if (mPreDrawListener != null) {
                return;
            }
            mPreDrawListener = new android.view.ViewTreeObserver.OnPreDrawListener() {
                @java.lang.Override
                public boolean onPreDraw() {
                    if (((mSubtitle.getVisibility() == android.view.View.VISIBLE) && (mSubtitle.getTop() > view.getHeight())) && (mTitle.getLineCount() > 1)) {
                        mTitle.setMaxLines(mTitle.getLineCount() - 1);
                        return false;
                    }
                    final int titleLines = mTitle.getLineCount();
                    final int maxLines = (titleLines > 1) ? mBodyMinLines : mBodyMaxLines;
                    if (mBody.getMaxLines() != maxLines) {
                        mBody.setMaxLines(maxLines);
                        return false;
                    } else {
                        removePreDrawListener();
                        return true;
                    }
                }
            };
            view.getViewTreeObserver().addOnPreDrawListener(mPreDrawListener);
        }

        void removePreDrawListener() {
            if (mPreDrawListener != null) {
                view.getViewTreeObserver().removeOnPreDrawListener(mPreDrawListener);
                mPreDrawListener = null;
            }
        }

        public android.widget.TextView getTitle() {
            return mTitle;
        }

        public android.widget.TextView getSubtitle() {
            return mSubtitle;
        }

        public android.widget.TextView getBody() {
            return mBody;
        }

        private android.graphics.Paint.FontMetricsInt getFontMetricsInt(android.widget.TextView textView) {
            android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(textView.getTextSize());
            paint.setTypeface(textView.getTypeface());
            return paint.getFontMetricsInt();
        }
    }

    @java.lang.Override
    public final android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
        android.view.View v = android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.lb_details_description, parent, false);
        return new android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter.ViewHolder(v);
    }

    @java.lang.Override
    public final void onBindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder, java.lang.Object item) {
        android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter.ViewHolder) (viewHolder));
        onBindDescription(vh, item);
        boolean hasTitle = true;
        if (android.text.TextUtils.isEmpty(vh.mTitle.getText())) {
            vh.mTitle.setVisibility(android.view.View.GONE);
            hasTitle = false;
        } else {
            vh.mTitle.setVisibility(android.view.View.VISIBLE);
            vh.mTitle.setLineSpacing((vh.mTitleLineSpacing - vh.mTitle.getLineHeight()) + vh.mTitle.getLineSpacingExtra(), vh.mTitle.getLineSpacingMultiplier());
            vh.mTitle.setMaxLines(vh.mTitleMaxLines);
        }
        setTopMargin(vh.mTitle, vh.mTitleMargin);
        boolean hasSubtitle = true;
        if (android.text.TextUtils.isEmpty(vh.mSubtitle.getText())) {
            vh.mSubtitle.setVisibility(android.view.View.GONE);
            hasSubtitle = false;
        } else {
            vh.mSubtitle.setVisibility(android.view.View.VISIBLE);
            if (hasTitle) {
                setTopMargin(vh.mSubtitle, (vh.mUnderTitleBaselineMargin + vh.mSubtitleFontMetricsInt.ascent) - vh.mTitleFontMetricsInt.descent);
            } else {
                setTopMargin(vh.mSubtitle, 0);
            }
        }
        if (android.text.TextUtils.isEmpty(vh.mBody.getText())) {
            vh.mBody.setVisibility(android.view.View.GONE);
        } else {
            vh.mBody.setVisibility(android.view.View.VISIBLE);
            vh.mBody.setLineSpacing((vh.mBodyLineSpacing - vh.mBody.getLineHeight()) + vh.mBody.getLineSpacingExtra(), vh.mBody.getLineSpacingMultiplier());
            if (hasSubtitle) {
                setTopMargin(vh.mBody, (vh.mUnderSubtitleBaselineMargin + vh.mBodyFontMetricsInt.ascent) - vh.mSubtitleFontMetricsInt.descent);
            } else
                if (hasTitle) {
                    setTopMargin(vh.mBody, (vh.mUnderTitleBaselineMargin + vh.mBodyFontMetricsInt.ascent) - vh.mTitleFontMetricsInt.descent);
                } else {
                    setTopMargin(vh.mBody, 0);
                }

        }
    }

    /**
     * Binds the data from the item to the ViewHolder.  The item is typically associated with
     * a {@link DetailsOverviewRow} or {@link PlaybackControlsRow}.
     *
     * @param vh
     * 		The ViewHolder for this details description view.
     * @param item
     * 		The item being presented.
     */
    protected abstract void onBindDescription(android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter.ViewHolder vh, java.lang.Object item);

    @java.lang.Override
    public void onUnbindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder) {
    }

    @java.lang.Override
    public void onViewAttachedToWindow(android.support.v17.leanback.widget.Presenter.ViewHolder holder) {
        // In case predraw listener was removed in detach, make sure
        // we have the proper layout.
        android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter.ViewHolder) (holder));
        vh.addPreDrawListener();
        super.onViewAttachedToWindow(holder);
    }

    @java.lang.Override
    public void onViewDetachedFromWindow(android.support.v17.leanback.widget.Presenter.ViewHolder holder) {
        android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter.ViewHolder) (holder));
        vh.removePreDrawListener();
        super.onViewDetachedFromWindow(holder);
    }

    private void setTopMargin(android.widget.TextView textView, int topMargin) {
        android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (textView.getLayoutParams()));
        lp.topMargin = topMargin;
        textView.setLayoutParams(lp);
    }
}

