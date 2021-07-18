package android.support.v17.leanback.app;


/**
 * Manager for showing/hiding progress bar widget. This class lets user specify an initial
 * delay after which the progress bar will be shown. This is currently being used in
 * {@link BrowseFragment} & {@link VerticalGridFragment} to show {@link ProgressBar}
 * while the data is being loaded.
 */
public final class ProgressBarManager {
    // Default delay for progress bar widget.
    private static final long DEFAULT_PROGRESS_BAR_DELAY = 1000;

    private long mInitialDelay = android.support.v17.leanback.app.ProgressBarManager.DEFAULT_PROGRESS_BAR_DELAY;

    android.view.ViewGroup rootView;

    android.view.View mProgressBarView;

    private android.os.Handler mHandler = new android.os.Handler();

    boolean mEnableProgressBar = true;

    boolean mUserProvidedProgressBar;

    boolean mIsShowing;

    private java.lang.Runnable runnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            if ((!mEnableProgressBar) || ((!mUserProvidedProgressBar) && (rootView == null))) {
                return;
            }
            if (mIsShowing) {
                if (mProgressBarView == null) {
                    mProgressBarView = new android.widget.ProgressBar(rootView.getContext(), null, android.R.attr.progressBarStyleLarge);
                    android.widget.FrameLayout.LayoutParams progressBarParams = new android.widget.FrameLayout.LayoutParams(android.widget.FrameLayout.LayoutParams.WRAP_CONTENT, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
                    progressBarParams.gravity = android.view.Gravity.CENTER;
                    rootView.addView(mProgressBarView, progressBarParams);
                } else
                    if (mUserProvidedProgressBar) {
                        mProgressBarView.setVisibility(android.view.View.VISIBLE);
                    }

            }
        }
    };

    /**
     * Sets the root view on which the progress bar will be attached. This class assumes the
     * root view to be {@link FrameLayout} in order to position the progress bar widget
     * in the center of the screen.
     *
     * @param rootView
     * 		view that will contain the progress bar.
     */
    public void setRootView(android.view.ViewGroup rootView) {
        this.rootView = rootView;
    }

    /**
     * Displays the progress bar.
     */
    public void show() {
        if (mEnableProgressBar) {
            mIsShowing = true;
            mHandler.postDelayed(runnable, mInitialDelay);
        }
    }

    /**
     * Hides the progress bar.
     */
    public void hide() {
        mIsShowing = false;
        if (mUserProvidedProgressBar) {
            mProgressBarView.setVisibility(android.view.View.INVISIBLE);
        } else
            if (mProgressBarView != null) {
                rootView.removeView(mProgressBarView);
            }

        mHandler.removeCallbacks(runnable);
    }

    /**
     * Sets a custom view to be shown in place of the default {@link ProgressBar}. This
     * view must have a parent. Once set, we maintain the visibility property of this view.
     *
     * @param progressBarView
     * 		custom view that will be shown to indicate progress.
     */
    public void setProgressBarView(android.view.View progressBarView) {
        if (progressBarView.getParent() == null) {
            throw new java.lang.IllegalArgumentException("Must have a parent");
        }
        this.mProgressBarView = progressBarView;
        this.mProgressBarView.setVisibility(android.view.View.INVISIBLE);
        mUserProvidedProgressBar = true;
    }

    /**
     * Returns the initial delay.
     */
    public long getInitialDelay() {
        return mInitialDelay;
    }

    /**
     * Sets the initial delay. Progress bar will be shown after this delay has elapsed.
     *
     * @param initialDelay
     * 		millisecond representing the initial delay.
     */
    public void setInitialDelay(long initialDelay) {
        this.mInitialDelay = initialDelay;
    }

    /**
     * Disables progress bar.
     */
    public void disableProgressBar() {
        mEnableProgressBar = false;
    }

    /**
     * Enables progress bar.
     */
    public void enableProgressBar() {
        mEnableProgressBar = true;
    }
}

