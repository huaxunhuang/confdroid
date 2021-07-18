package android.media;


/**
 * Abstract widget class to render a closed caption track.
 *
 * @unknown 
 */
abstract class ClosedCaptionWidget extends android.view.ViewGroup implements android.media.SubtitleTrack.RenderingWidget {
    /**
     *
     *
     * @unknown 
     */
    interface ClosedCaptionLayout {
        void setCaptionStyle(android.view.accessibility.CaptioningManager.CaptionStyle captionStyle);

        void setFontScale(float scale);
    }

    private static final android.view.accessibility.CaptioningManager.CaptionStyle DEFAULT_CAPTION_STYLE = android.view.accessibility.CaptioningManager.CaptionStyle.DEFAULT;

    /**
     * Captioning manager, used to obtain and track caption properties.
     */
    private final android.view.accessibility.CaptioningManager mManager;

    /**
     * Current caption style.
     */
    protected android.view.accessibility.CaptioningManager.CaptionStyle mCaptionStyle;

    /**
     * Callback for rendering changes.
     */
    protected android.media.SubtitleTrack.RenderingWidget.OnChangedListener mListener;

    /**
     * Concrete layout of CC.
     */
    protected android.media.ClosedCaptionWidget.ClosedCaptionLayout mClosedCaptionLayout;

    /**
     * Whether a caption style change listener is registered.
     */
    private boolean mHasChangeListener;

    public ClosedCaptionWidget(android.content.Context context) {
        this(context, null);
    }

    public ClosedCaptionWidget(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClosedCaptionWidget(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public ClosedCaptionWidget(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // Cannot render text over video when layer type is hardware.
        setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null);
        mManager = ((android.view.accessibility.CaptioningManager) (context.getSystemService(android.content.Context.CAPTIONING_SERVICE)));
        mCaptionStyle = android.media.ClosedCaptionWidget.DEFAULT_CAPTION_STYLE.applyStyle(mManager.getUserStyle());
        mClosedCaptionLayout = createCaptionLayout(context);
        mClosedCaptionLayout.setCaptionStyle(mCaptionStyle);
        mClosedCaptionLayout.setFontScale(mManager.getFontScale());
        addView(((android.view.ViewGroup) (mClosedCaptionLayout)), android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        requestLayout();
    }

    public abstract android.media.ClosedCaptionWidget.ClosedCaptionLayout createCaptionLayout(android.content.Context context);

    @java.lang.Override
    public void setOnChangedListener(android.media.SubtitleTrack.RenderingWidget.OnChangedListener listener) {
        mListener = listener;
    }

    @java.lang.Override
    public void setSize(int width, int height) {
        final int widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY);
        final int heightSpec = android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.EXACTLY);
        measure(widthSpec, heightSpec);
        layout(0, 0, width, height);
    }

    @java.lang.Override
    public void setVisible(boolean visible) {
        if (visible) {
            setVisibility(android.view.View.VISIBLE);
        } else {
            setVisibility(android.view.View.GONE);
        }
        manageChangeListener();
    }

    @java.lang.Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        manageChangeListener();
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        manageChangeListener();
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ((android.view.ViewGroup) (mClosedCaptionLayout)).measure(widthMeasureSpec, heightMeasureSpec);
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ((android.view.ViewGroup) (mClosedCaptionLayout)).layout(l, t, r, b);
    }

    /**
     * Manages whether this renderer is listening for caption style changes.
     */
    private final android.view.accessibility.CaptioningManager.CaptioningChangeListener mCaptioningListener = new android.view.accessibility.CaptioningManager.CaptioningChangeListener() {
        @java.lang.Override
        public void onUserStyleChanged(android.view.accessibility.CaptioningManager.CaptionStyle userStyle) {
            mCaptionStyle = android.media.ClosedCaptionWidget.DEFAULT_CAPTION_STYLE.applyStyle(userStyle);
            mClosedCaptionLayout.setCaptionStyle(mCaptionStyle);
        }

        @java.lang.Override
        public void onFontScaleChanged(float fontScale) {
            mClosedCaptionLayout.setFontScale(fontScale);
        }
    };

    private void manageChangeListener() {
        final boolean needsListener = isAttachedToWindow() && (getVisibility() == android.view.View.VISIBLE);
        if (mHasChangeListener != needsListener) {
            mHasChangeListener = needsListener;
            if (needsListener) {
                mManager.addCaptioningChangeListener(mCaptioningListener);
            } else {
                mManager.removeCaptioningChangeListener(mCaptioningListener);
            }
        }
    }
}

