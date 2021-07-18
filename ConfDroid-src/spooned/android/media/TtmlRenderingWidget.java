package android.media;


/**
 * Widget capable of rendering TTML captions.
 *
 * @unknown 
 */
class TtmlRenderingWidget extends android.widget.LinearLayout implements android.media.SubtitleTrack.RenderingWidget {
    /**
     * Callback for rendering changes.
     */
    private android.media.SubtitleTrack.RenderingWidget.OnChangedListener mListener;

    private final android.widget.TextView mTextView;

    public TtmlRenderingWidget(android.content.Context context) {
        this(context, null);
    }

    public TtmlRenderingWidget(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TtmlRenderingWidget(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TtmlRenderingWidget(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // Cannot render text over video when layer type is hardware.
        setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null);
        android.view.accessibility.CaptioningManager captionManager = ((android.view.accessibility.CaptioningManager) (context.getSystemService(android.content.Context.CAPTIONING_SERVICE)));
        mTextView = new android.widget.TextView(context);
        mTextView.setTextColor(captionManager.getUserStyle().foregroundColor);
        addView(mTextView, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
        mTextView.setGravity(android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL);
    }

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
    }

    @java.lang.Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setActiveCues(java.util.Vector<android.media.SubtitleTrack.Cue> activeCues) {
        final int count = activeCues.size();
        java.lang.String subtitleText = "";
        for (int i = 0; i < count; i++) {
            android.media.TtmlCue cue = ((android.media.TtmlCue) (activeCues.get(i)));
            subtitleText += cue.mText + "\n";
        }
        mTextView.setText(subtitleText);
        if (mListener != null) {
            mListener.onChanged(this);
        }
    }
}

