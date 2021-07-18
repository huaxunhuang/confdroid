package android.support.v17.leanback.widget;


/**
 * Presenter that responsible to create a ImageView and bind to DetailsOverviewRow. The default
 * implementation uses {@link DetailsOverviewRow#getImageDrawable()} and binds to {@link ImageView}.
 * <p>
 * Default implementation assumes no scaleType on ImageView and uses intrinsic width and height of
 * {@link DetailsOverviewRow#getImageDrawable()} to initialize ImageView's layout params.  To
 * specify a fixed size and/or specify a scapeType, subclass should change ImageView's layout params
 * and scaleType in {@link #onCreateView(ViewGroup)}.
 * <p>
 * Subclass may override and has its own image view. Subclass may also download image from URL
 * instead of using {@link DetailsOverviewRow#getImageDrawable()}. It's subclass's responsibility to
 * call {@link FullWidthDetailsOverviewRowPresenter#notifyOnBindLogo(FullWidthDetailsOverviewRowPresenter.ViewHolder)}
 * whenever {@link #isBoundToImage(ViewHolder, DetailsOverviewRow)} turned to true so that activity
 * transition can be started.
 */
public class DetailsOverviewLogoPresenter extends android.support.v17.leanback.widget.Presenter {
    /**
     * ViewHolder for Logo view of DetailsOverviewRow.
     */
    public static class ViewHolder extends android.support.v17.leanback.widget.Presenter.ViewHolder {
        protected android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter mParentPresenter;

        protected android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder mParentViewHolder;

        private boolean mSizeFromDrawableIntrinsic;

        public ViewHolder(android.view.View view) {
            super(view);
        }

        public android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter getParentPresenter() {
            return mParentPresenter;
        }

        public android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder getParentViewHolder() {
            return mParentViewHolder;
        }

        /**
         *
         *
         * @return True if layout size of ImageView should be changed to intrinsic size of Drawable,
        false otherwise. Used by
        {@link DetailsOverviewLogoPresenter#onBindViewHolder(Presenter.ViewHolder, Object)}
        .
         * @see DetailsOverviewLogoPresenter#onCreateView(ViewGroup)
         * @see DetailsOverviewLogoPresenter#onBindViewHolder(Presenter.ViewHolder, Object)
         */
        public boolean isSizeFromDrawableIntrinsic() {
            return mSizeFromDrawableIntrinsic;
        }

        /**
         * Change if the ImageView layout size should be synchronized to Drawable intrinsic size.
         * Used by
         * {@link DetailsOverviewLogoPresenter#onBindViewHolder(Presenter.ViewHolder, Object)}.
         *
         * @param sizeFromDrawableIntrinsic
         * 		True if layout size of ImageView should be changed to
         * 		intrinsic size of Drawable, false otherwise.
         * @see DetailsOverviewLogoPresenter#onCreateView(ViewGroup)
         * @see DetailsOverviewLogoPresenter#onBindViewHolder(Presenter.ViewHolder, Object)
         */
        public void setSizeFromDrawableIntrinsic(boolean sizeFromDrawableIntrinsic) {
            mSizeFromDrawableIntrinsic = sizeFromDrawableIntrinsic;
        }
    }

    /**
     * Create a View for the Logo, default implementation loads from
     * {@link R.layout#lb_fullwidth_details_overview_logo}. Subclass may override this method to use
     * a fixed layout size and change ImageView scaleType. If the layout params is WRAP_CONTENT for
     * both width and size, the ViewHolder would be using intrinsic size of Drawable in
     * {@link #onBindViewHolder(Presenter.ViewHolder, Object)}.
     *
     * @param parent
     * 		Parent view.
     * @return View created for the logo.
     */
    public android.view.View onCreateView(android.view.ViewGroup parent) {
        return android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.lb_fullwidth_details_overview_logo, parent, false);
    }

    @java.lang.Override
    public android.support.v17.leanback.widget.Presenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
        android.view.View view = onCreateView(parent);
        android.support.v17.leanback.widget.DetailsOverviewLogoPresenter.ViewHolder vh = new android.support.v17.leanback.widget.DetailsOverviewLogoPresenter.ViewHolder(view);
        android.view.ViewGroup.LayoutParams lp = view.getLayoutParams();
        vh.setSizeFromDrawableIntrinsic((lp.width == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) && (lp.width == android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        return vh;
    }

    /**
     * Called from {@link FullWidthDetailsOverviewRowPresenter} to setup FullWidthDetailsOverviewRowPresenter
     * and FullWidthDetailsOverviewRowPresenter.ViewHolder that hosts the logo.
     *
     * @param viewHolder
     * 		
     * @param parentViewHolder
     * 		
     * @param parentPresenter
     * 		
     */
    public void setContext(android.support.v17.leanback.widget.DetailsOverviewLogoPresenter.ViewHolder viewHolder, android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder parentViewHolder, android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter parentPresenter) {
        viewHolder.mParentViewHolder = parentViewHolder;
        viewHolder.mParentPresenter = parentPresenter;
    }

    /**
     * Returns true if the logo view is bound to image. Subclass may override. The default
     * implementation returns true when {@link DetailsOverviewRow#getImageDrawable()} is not null.
     * If subclass of DetailsOverviewLogoPresenter manages its own image drawable, it should
     * override this function to report status correctly and invoke
     * {@link FullWidthDetailsOverviewRowPresenter#notifyOnBindLogo(FullWidthDetailsOverviewRowPresenter.ViewHolder)}
     * when image view is bound to the drawable.
     */
    public boolean isBoundToImage(android.support.v17.leanback.widget.DetailsOverviewLogoPresenter.ViewHolder viewHolder, android.support.v17.leanback.widget.DetailsOverviewRow row) {
        return (row != null) && (row.getImageDrawable() != null);
    }

    /**
     * Bind logo View to drawable of DetailsOverviewRow and call notifyOnBindLogo().  The
     * default implementation assumes the Logo View is an ImageView and change layout size to
     * intrinsic size of ImageDrawable if {@link ViewHolder#isSizeFromDrawableIntrinsic()} is true.
     *
     * @param viewHolder
     * 		ViewHolder to bind.
     * @param item
     * 		DetailsOverviewRow object to bind.
     */
    @java.lang.Override
    public void onBindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder, java.lang.Object item) {
        android.support.v17.leanback.widget.DetailsOverviewRow row = ((android.support.v17.leanback.widget.DetailsOverviewRow) (item));
        android.widget.ImageView imageView = ((android.widget.ImageView) (viewHolder.view));
        imageView.setImageDrawable(row.getImageDrawable());
        if (isBoundToImage(((android.support.v17.leanback.widget.DetailsOverviewLogoPresenter.ViewHolder) (viewHolder)), row)) {
            android.support.v17.leanback.widget.DetailsOverviewLogoPresenter.ViewHolder vh = ((android.support.v17.leanback.widget.DetailsOverviewLogoPresenter.ViewHolder) (viewHolder));
            if (vh.isSizeFromDrawableIntrinsic()) {
                android.view.ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.width = row.getImageDrawable().getIntrinsicWidth();
                lp.height = row.getImageDrawable().getIntrinsicHeight();
                if ((imageView.getMaxWidth() > 0) || (imageView.getMaxHeight() > 0)) {
                    float maxScaleWidth = 1.0F;
                    if (imageView.getMaxWidth() > 0) {
                        if (lp.width > imageView.getMaxWidth()) {
                            maxScaleWidth = imageView.getMaxWidth() / ((float) (lp.width));
                        }
                    }
                    float maxScaleHeight = 1.0F;
                    if (imageView.getMaxHeight() > 0) {
                        if (lp.height > imageView.getMaxHeight()) {
                            maxScaleHeight = imageView.getMaxHeight() / ((float) (lp.height));
                        }
                    }
                    float scale = java.lang.Math.min(maxScaleWidth, maxScaleHeight);
                    lp.width = ((int) (lp.width * scale));
                    lp.height = ((int) (lp.height * scale));
                }
                imageView.setLayoutParams(lp);
            }
            vh.mParentPresenter.notifyOnBindLogo(vh.mParentViewHolder);
        }
    }

    @java.lang.Override
    public void onUnbindViewHolder(android.support.v17.leanback.widget.Presenter.ViewHolder viewHolder) {
    }
}

