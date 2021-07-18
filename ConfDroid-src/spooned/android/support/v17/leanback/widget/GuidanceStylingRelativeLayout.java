package android.support.v17.leanback.widget;


/**
 * Relative layout implementation that lays out child views based on provided keyline percent(
 * distance of TitleView baseline from the top).
 *
 * Repositioning child views in PreDraw callback in {@link GuidanceStylist} was interfering with
 * fragment transition. To avoid that, we do that in the onLayout pass.
 */
class GuidanceStylingRelativeLayout extends android.widget.RelativeLayout {
    private float mTitleKeylinePercent;

    public GuidanceStylingRelativeLayout(android.content.Context context) {
        this(context, null);
    }

    public GuidanceStylingRelativeLayout(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuidanceStylingRelativeLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        android.content.res.TypedArray ta = getContext().getTheme().obtainStyledAttributes(R.styleable.LeanbackGuidedStepTheme);
        mTitleKeylinePercent = ta.getFloat(R.styleable.LeanbackGuidedStepTheme_guidedStepKeyline, 40);
        ta.recycle();
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        android.widget.TextView mTitleView = ((android.widget.TextView) (getRootView().findViewById(R.id.guidance_title)));
        android.widget.TextView mBreadcrumbView = ((android.widget.TextView) (getRootView().findViewById(R.id.guidance_breadcrumb)));
        android.widget.TextView mDescriptionView = ((android.widget.TextView) (getRootView().findViewById(R.id.guidance_description)));
        android.widget.ImageView mIconView = ((android.widget.ImageView) (getRootView().findViewById(R.id.guidance_icon)));
        int mTitleKeylinePixels = ((int) ((getMeasuredHeight() * mTitleKeylinePercent) / 100));
        if ((mTitleView != null) && (mTitleView.getParent() == this)) {
            android.graphics.Paint textPaint = mTitleView.getPaint();
            int titleViewTextHeight = -textPaint.getFontMetricsInt().top;
            int mBreadcrumbViewHeight = mBreadcrumbView.getMeasuredHeight();
            int guidanceTextContainerTop = ((mTitleKeylinePixels - titleViewTextHeight) - mBreadcrumbViewHeight) - mTitleView.getPaddingTop();
            int offset = guidanceTextContainerTop - mBreadcrumbView.getTop();
            if ((mBreadcrumbView != null) && (mBreadcrumbView.getParent() == this)) {
                mBreadcrumbView.offsetTopAndBottom(offset);
            }
            mTitleView.offsetTopAndBottom(offset);
            if ((mDescriptionView != null) && (mDescriptionView.getParent() == this)) {
                mDescriptionView.offsetTopAndBottom(offset);
            }
        }
        if ((mIconView != null) && (mIconView.getParent() == this)) {
            android.graphics.drawable.Drawable drawable = mIconView.getDrawable();
            if (drawable != null) {
                mIconView.offsetTopAndBottom(mTitleKeylinePixels - (mIconView.getMeasuredHeight() / 2));
            }
        }
    }
}

