package android.media;


/**
 * Widget capable of rendering WebVTT captions.
 *
 * @unknown 
 */
class WebVttRenderingWidget extends android.view.ViewGroup implements android.media.SubtitleTrack.RenderingWidget {
    private static final boolean DEBUG = false;

    private static final android.view.accessibility.CaptioningManager.CaptionStyle DEFAULT_CAPTION_STYLE = android.view.accessibility.CaptioningManager.CaptionStyle.DEFAULT;

    private static final int DEBUG_REGION_BACKGROUND = 0x800000ff;

    private static final int DEBUG_CUE_BACKGROUND = 0x80ff0000;

    /**
     * WebVtt specifies line height as 5.3% of the viewport height.
     */
    private static final float LINE_HEIGHT_RATIO = 0.0533F;

    /**
     * Map of active regions, used to determine enter/exit.
     */
    private final android.util.ArrayMap<android.media.TextTrackRegion, android.media.WebVttRenderingWidget.RegionLayout> mRegionBoxes = new android.util.ArrayMap<android.media.TextTrackRegion, android.media.WebVttRenderingWidget.RegionLayout>();

    /**
     * Map of active cues, used to determine enter/exit.
     */
    private final android.util.ArrayMap<android.media.TextTrackCue, android.media.WebVttRenderingWidget.CueLayout> mCueBoxes = new android.util.ArrayMap<android.media.TextTrackCue, android.media.WebVttRenderingWidget.CueLayout>();

    /**
     * Captioning manager, used to obtain and track caption properties.
     */
    private final android.view.accessibility.CaptioningManager mManager;

    /**
     * Callback for rendering changes.
     */
    private android.media.SubtitleTrack.RenderingWidget.OnChangedListener mListener;

    /**
     * Current caption style.
     */
    private android.view.accessibility.CaptioningManager.CaptionStyle mCaptionStyle;

    /**
     * Current font size, computed from font scaling factor and height.
     */
    private float mFontSize;

    /**
     * Whether a caption style change listener is registered.
     */
    private boolean mHasChangeListener;

    public WebVttRenderingWidget(android.content.Context context) {
        this(context, null);
    }

    public WebVttRenderingWidget(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebVttRenderingWidget(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WebVttRenderingWidget(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // Cannot render text over video when layer type is hardware.
        setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null);
        mManager = ((android.view.accessibility.CaptioningManager) (context.getSystemService(android.content.Context.CAPTIONING_SERVICE)));
        mCaptionStyle = mManager.getUserStyle();
        mFontSize = (mManager.getFontScale() * getHeight()) * android.media.WebVttRenderingWidget.LINE_HEIGHT_RATIO;
    }

    @java.lang.Override
    public void setSize(int width, int height) {
        final int widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY);
        final int heightSpec = android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.EXACTLY);
        measure(widthSpec, heightSpec);
        layout(0, 0, width, height);
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
    public void setOnChangedListener(android.media.SubtitleTrack.RenderingWidget.OnChangedListener listener) {
        mListener = listener;
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

    /**
     * Manages whether this renderer is listening for caption style changes.
     */
    private void manageChangeListener() {
        final boolean needsListener = isAttachedToWindow() && (getVisibility() == android.view.View.VISIBLE);
        if (mHasChangeListener != needsListener) {
            mHasChangeListener = needsListener;
            if (needsListener) {
                mManager.addCaptioningChangeListener(mCaptioningListener);
                final android.view.accessibility.CaptioningManager.CaptionStyle captionStyle = mManager.getUserStyle();
                final float fontSize = (mManager.getFontScale() * getHeight()) * android.media.WebVttRenderingWidget.LINE_HEIGHT_RATIO;
                setCaptionStyle(captionStyle, fontSize);
            } else {
                mManager.removeCaptioningChangeListener(mCaptioningListener);
            }
        }
    }

    public void setActiveCues(java.util.Vector<android.media.SubtitleTrack.Cue> activeCues) {
        final android.content.Context context = getContext();
        final android.view.accessibility.CaptioningManager.CaptionStyle captionStyle = mCaptionStyle;
        final float fontSize = mFontSize;
        prepForPrune();
        // Ensure we have all necessary cue and region boxes.
        final int count = activeCues.size();
        for (int i = 0; i < count; i++) {
            final android.media.TextTrackCue cue = ((android.media.TextTrackCue) (activeCues.get(i)));
            final android.media.TextTrackRegion region = cue.mRegion;
            if (region != null) {
                android.media.WebVttRenderingWidget.RegionLayout regionBox = mRegionBoxes.get(region);
                if (regionBox == null) {
                    regionBox = new android.media.WebVttRenderingWidget.RegionLayout(context, region, captionStyle, fontSize);
                    mRegionBoxes.put(region, regionBox);
                    addView(regionBox, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                regionBox.put(cue);
            } else {
                android.media.WebVttRenderingWidget.CueLayout cueBox = mCueBoxes.get(cue);
                if (cueBox == null) {
                    cueBox = new android.media.WebVttRenderingWidget.CueLayout(context, cue, captionStyle, fontSize);
                    mCueBoxes.put(cue, cueBox);
                    addView(cueBox, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                cueBox.update();
                cueBox.setOrder(i);
            }
        }
        prune();
        // Force measurement and layout.
        final int width = getWidth();
        final int height = getHeight();
        setSize(width, height);
        if (mListener != null) {
            mListener.onChanged(this);
        }
    }

    private void setCaptionStyle(android.view.accessibility.CaptioningManager.CaptionStyle captionStyle, float fontSize) {
        captionStyle = android.media.WebVttRenderingWidget.DEFAULT_CAPTION_STYLE.applyStyle(captionStyle);
        mCaptionStyle = captionStyle;
        mFontSize = fontSize;
        final int cueCount = mCueBoxes.size();
        for (int i = 0; i < cueCount; i++) {
            final android.media.WebVttRenderingWidget.CueLayout cueBox = mCueBoxes.valueAt(i);
            cueBox.setCaptionStyle(captionStyle, fontSize);
        }
        final int regionCount = mRegionBoxes.size();
        for (int i = 0; i < regionCount; i++) {
            final android.media.WebVttRenderingWidget.RegionLayout regionBox = mRegionBoxes.valueAt(i);
            regionBox.setCaptionStyle(captionStyle, fontSize);
        }
    }

    /**
     * Remove inactive cues and regions.
     */
    private void prune() {
        int regionCount = mRegionBoxes.size();
        for (int i = 0; i < regionCount; i++) {
            final android.media.WebVttRenderingWidget.RegionLayout regionBox = mRegionBoxes.valueAt(i);
            if (regionBox.prune()) {
                removeView(regionBox);
                mRegionBoxes.removeAt(i);
                regionCount--;
                i--;
            }
        }
        int cueCount = mCueBoxes.size();
        for (int i = 0; i < cueCount; i++) {
            final android.media.WebVttRenderingWidget.CueLayout cueBox = mCueBoxes.valueAt(i);
            if (!cueBox.isActive()) {
                removeView(cueBox);
                mCueBoxes.removeAt(i);
                cueCount--;
                i--;
            }
        }
    }

    /**
     * Reset active cues and regions.
     */
    private void prepForPrune() {
        final int regionCount = mRegionBoxes.size();
        for (int i = 0; i < regionCount; i++) {
            final android.media.WebVttRenderingWidget.RegionLayout regionBox = mRegionBoxes.valueAt(i);
            regionBox.prepForPrune();
        }
        final int cueCount = mCueBoxes.size();
        for (int i = 0; i < cueCount; i++) {
            final android.media.WebVttRenderingWidget.CueLayout cueBox = mCueBoxes.valueAt(i);
            cueBox.prepForPrune();
        }
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int regionCount = mRegionBoxes.size();
        for (int i = 0; i < regionCount; i++) {
            final android.media.WebVttRenderingWidget.RegionLayout regionBox = mRegionBoxes.valueAt(i);
            regionBox.measureForParent(widthMeasureSpec, heightMeasureSpec);
        }
        final int cueCount = mCueBoxes.size();
        for (int i = 0; i < cueCount; i++) {
            final android.media.WebVttRenderingWidget.CueLayout cueBox = mCueBoxes.valueAt(i);
            cueBox.measureForParent(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int viewportWidth = r - l;
        final int viewportHeight = b - t;
        setCaptionStyle(mCaptionStyle, (mManager.getFontScale() * android.media.WebVttRenderingWidget.LINE_HEIGHT_RATIO) * viewportHeight);
        final int regionCount = mRegionBoxes.size();
        for (int i = 0; i < regionCount; i++) {
            final android.media.WebVttRenderingWidget.RegionLayout regionBox = mRegionBoxes.valueAt(i);
            layoutRegion(viewportWidth, viewportHeight, regionBox);
        }
        final int cueCount = mCueBoxes.size();
        for (int i = 0; i < cueCount; i++) {
            final android.media.WebVttRenderingWidget.CueLayout cueBox = mCueBoxes.valueAt(i);
            layoutCue(viewportWidth, viewportHeight, cueBox);
        }
    }

    /**
     * Lays out a region within the viewport. The region handles layout for
     * contained cues.
     */
    private void layoutRegion(int viewportWidth, int viewportHeight, android.media.WebVttRenderingWidget.RegionLayout regionBox) {
        final android.media.TextTrackRegion region = regionBox.getRegion();
        final int regionHeight = regionBox.getMeasuredHeight();
        final int regionWidth = regionBox.getMeasuredWidth();
        // TODO: Account for region anchor point.
        final float x = region.mViewportAnchorPointX;
        final float y = region.mViewportAnchorPointY;
        final int left = ((int) ((x * (viewportWidth - regionWidth)) / 100));
        final int top = ((int) ((y * (viewportHeight - regionHeight)) / 100));
        regionBox.layout(left, top, left + regionWidth, top + regionHeight);
    }

    /**
     * Lays out a cue within the viewport.
     */
    private void layoutCue(int viewportWidth, int viewportHeight, android.media.WebVttRenderingWidget.CueLayout cueBox) {
        final android.media.TextTrackCue cue = cueBox.getCue();
        final int direction = getLayoutDirection();
        final int absAlignment = android.media.WebVttRenderingWidget.resolveCueAlignment(direction, cue.mAlignment);
        final boolean cueSnapToLines = cue.mSnapToLines;
        int size = (100 * cueBox.getMeasuredWidth()) / viewportWidth;
        // Determine raw x-position.
        int xPosition;
        switch (absAlignment) {
            case android.media.TextTrackCue.ALIGNMENT_LEFT :
                xPosition = cue.mTextPosition;
                break;
            case android.media.TextTrackCue.ALIGNMENT_RIGHT :
                xPosition = cue.mTextPosition - size;
                break;
            case android.media.TextTrackCue.ALIGNMENT_MIDDLE :
            default :
                xPosition = cue.mTextPosition - (size / 2);
                break;
        }
        // Adjust x-position for layout.
        if (direction == android.view.View.LAYOUT_DIRECTION_RTL) {
            xPosition = 100 - xPosition;
        }
        // If the text track cue snap-to-lines flag is set, adjust
        // x-position and size for padding. This is equivalent to placing the
        // cue within the title-safe area.
        if (cueSnapToLines) {
            final int paddingLeft = (100 * getPaddingLeft()) / viewportWidth;
            final int paddingRight = (100 * getPaddingRight()) / viewportWidth;
            if ((xPosition < paddingLeft) && ((xPosition + size) > paddingLeft)) {
                xPosition += paddingLeft;
                size -= paddingLeft;
            }
            final float rightEdge = 100 - paddingRight;
            if ((xPosition < rightEdge) && ((xPosition + size) > rightEdge)) {
                size -= paddingRight;
            }
        }
        // Compute absolute left position and width.
        final int left = (xPosition * viewportWidth) / 100;
        final int width = (size * viewportWidth) / 100;
        // Determine initial y-position.
        final int yPosition = calculateLinePosition(cueBox);
        // Compute absolute final top position and height.
        final int height = cueBox.getMeasuredHeight();
        final int top;
        if (yPosition < 0) {
            // TODO: This needs to use the actual height of prior boxes.
            top = viewportHeight + (yPosition * height);
        } else {
            top = (yPosition * (viewportHeight - height)) / 100;
        }
        // Layout cue in final position.
        cueBox.layout(left, top, left + width, top + height);
    }

    /**
     * Calculates the line position for a cue.
     * <p>
     * If the resulting position is negative, it represents a bottom-aligned
     * position relative to the number of active cues. Otherwise, it represents
     * a percentage [0-100] of the viewport height.
     */
    private int calculateLinePosition(android.media.WebVttRenderingWidget.CueLayout cueBox) {
        final android.media.TextTrackCue cue = cueBox.getCue();
        final java.lang.Integer linePosition = cue.mLinePosition;
        final boolean snapToLines = cue.mSnapToLines;
        final boolean autoPosition = linePosition == null;
        if (((!snapToLines) && (!autoPosition)) && ((linePosition < 0) || (linePosition > 100))) {
            // Invalid line position defaults to 100.
            return 100;
        } else
            if (!autoPosition) {
                // Use the valid, supplied line position.
                return linePosition;
            } else
                if (!snapToLines) {
                    // Automatic, non-snapped line position defaults to 100.
                    return 100;
                } else {
                    // Automatic snapped line position uses active cue order.
                    return -(cueBox.mOrder + 1);
                }


    }

    /**
     * Resolves cue alignment according to the specified layout direction.
     */
    private static int resolveCueAlignment(int layoutDirection, int alignment) {
        switch (alignment) {
            case android.media.TextTrackCue.ALIGNMENT_START :
                return layoutDirection == android.view.View.LAYOUT_DIRECTION_LTR ? android.media.TextTrackCue.ALIGNMENT_LEFT : android.media.TextTrackCue.ALIGNMENT_RIGHT;
            case android.media.TextTrackCue.ALIGNMENT_END :
                return layoutDirection == android.view.View.LAYOUT_DIRECTION_LTR ? android.media.TextTrackCue.ALIGNMENT_RIGHT : android.media.TextTrackCue.ALIGNMENT_LEFT;
        }
        return alignment;
    }

    private final android.view.accessibility.CaptioningManager.CaptioningChangeListener mCaptioningListener = new android.view.accessibility.CaptioningManager.CaptioningChangeListener() {
        @java.lang.Override
        public void onFontScaleChanged(float fontScale) {
            final float fontSize = (fontScale * getHeight()) * android.media.WebVttRenderingWidget.LINE_HEIGHT_RATIO;
            setCaptionStyle(mCaptionStyle, fontSize);
        }

        @java.lang.Override
        public void onUserStyleChanged(android.view.accessibility.CaptioningManager.CaptionStyle userStyle) {
            setCaptionStyle(userStyle, mFontSize);
        }
    };

    /**
     * A text track region represents a portion of the video viewport and
     * provides a rendering area for text track cues.
     */
    private static class RegionLayout extends android.widget.LinearLayout {
        private final java.util.ArrayList<android.media.WebVttRenderingWidget.CueLayout> mRegionCueBoxes = new java.util.ArrayList<android.media.WebVttRenderingWidget.CueLayout>();

        private final android.media.TextTrackRegion mRegion;

        private android.view.accessibility.CaptioningManager.CaptionStyle mCaptionStyle;

        private float mFontSize;

        public RegionLayout(android.content.Context context, android.media.TextTrackRegion region, android.view.accessibility.CaptioningManager.CaptionStyle captionStyle, float fontSize) {
            super(context);
            mRegion = region;
            mCaptionStyle = captionStyle;
            mFontSize = fontSize;
            // TODO: Add support for vertical text
            setOrientation(android.widget.LinearLayout.VERTICAL);
            if (android.media.WebVttRenderingWidget.DEBUG) {
                setBackgroundColor(android.media.WebVttRenderingWidget.DEBUG_REGION_BACKGROUND);
            } else {
                setBackgroundColor(captionStyle.windowColor);
            }
        }

        public void setCaptionStyle(android.view.accessibility.CaptioningManager.CaptionStyle captionStyle, float fontSize) {
            mCaptionStyle = captionStyle;
            mFontSize = fontSize;
            final int cueCount = mRegionCueBoxes.size();
            for (int i = 0; i < cueCount; i++) {
                final android.media.WebVttRenderingWidget.CueLayout cueBox = mRegionCueBoxes.get(i);
                cueBox.setCaptionStyle(captionStyle, fontSize);
            }
            setBackgroundColor(captionStyle.windowColor);
        }

        /**
         * Performs the parent's measurement responsibilities, then
         * automatically performs its own measurement.
         */
        public void measureForParent(int widthMeasureSpec, int heightMeasureSpec) {
            final android.media.TextTrackRegion region = mRegion;
            final int specWidth = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
            final int specHeight = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
            final int width = ((int) (region.mWidth));
            // Determine the absolute maximum region size as the requested size.
            final int size = (width * specWidth) / 100;
            widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(size, android.view.View.MeasureSpec.AT_MOST);
            heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(specHeight, android.view.View.MeasureSpec.AT_MOST);
            measure(widthMeasureSpec, heightMeasureSpec);
        }

        /**
         * Prepares this region for pruning by setting all tracks as inactive.
         * <p>
         * Tracks that are added or updated using {@link #put(TextTrackCue)}
         * after this calling this method will be marked as active.
         */
        public void prepForPrune() {
            final int cueCount = mRegionCueBoxes.size();
            for (int i = 0; i < cueCount; i++) {
                final android.media.WebVttRenderingWidget.CueLayout cueBox = mRegionCueBoxes.get(i);
                cueBox.prepForPrune();
            }
        }

        /**
         * Adds a {@link TextTrackCue} to this region. If the track had already
         * been added, updates its active state.
         *
         * @param cue
         * 		
         */
        public void put(android.media.TextTrackCue cue) {
            final int cueCount = mRegionCueBoxes.size();
            for (int i = 0; i < cueCount; i++) {
                final android.media.WebVttRenderingWidget.CueLayout cueBox = mRegionCueBoxes.get(i);
                if (cueBox.getCue() == cue) {
                    cueBox.update();
                    return;
                }
            }
            final android.media.WebVttRenderingWidget.CueLayout cueBox = new android.media.WebVttRenderingWidget.CueLayout(getContext(), cue, mCaptionStyle, mFontSize);
            mRegionCueBoxes.add(cueBox);
            addView(cueBox, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
            if (getChildCount() > mRegion.mLines) {
                removeViewAt(0);
            }
        }

        /**
         * Remove all inactive tracks from this region.
         *
         * @return true if this region is empty and should be pruned
         */
        public boolean prune() {
            int cueCount = mRegionCueBoxes.size();
            for (int i = 0; i < cueCount; i++) {
                final android.media.WebVttRenderingWidget.CueLayout cueBox = mRegionCueBoxes.get(i);
                if (!cueBox.isActive()) {
                    mRegionCueBoxes.remove(i);
                    removeView(cueBox);
                    cueCount--;
                    i--;
                }
            }
            return mRegionCueBoxes.isEmpty();
        }

        /**
         *
         *
         * @return the region data backing this layout
         */
        public android.media.TextTrackRegion getRegion() {
            return mRegion;
        }
    }

    /**
     * A text track cue is the unit of time-sensitive data in a text track,
     * corresponding for instance for subtitles and captions to the text that
     * appears at a particular time and disappears at another time.
     * <p>
     * A single cue may contain multiple {@link SpanLayout}s, each representing a
     * single line of text.
     */
    private static class CueLayout extends android.widget.LinearLayout {
        public final android.media.TextTrackCue mCue;

        private android.view.accessibility.CaptioningManager.CaptionStyle mCaptionStyle;

        private float mFontSize;

        private boolean mActive;

        private int mOrder;

        public CueLayout(android.content.Context context, android.media.TextTrackCue cue, android.view.accessibility.CaptioningManager.CaptionStyle captionStyle, float fontSize) {
            super(context);
            mCue = cue;
            mCaptionStyle = captionStyle;
            mFontSize = fontSize;
            // TODO: Add support for vertical text.
            final boolean horizontal = cue.mWritingDirection == android.media.TextTrackCue.WRITING_DIRECTION_HORIZONTAL;
            setOrientation(horizontal ? android.widget.LinearLayout.VERTICAL : android.widget.LinearLayout.HORIZONTAL);
            switch (cue.mAlignment) {
                case android.media.TextTrackCue.ALIGNMENT_END :
                    setGravity(android.view.Gravity.END);
                    break;
                case android.media.TextTrackCue.ALIGNMENT_LEFT :
                    setGravity(android.view.Gravity.LEFT);
                    break;
                case android.media.TextTrackCue.ALIGNMENT_MIDDLE :
                    setGravity(horizontal ? android.view.Gravity.CENTER_HORIZONTAL : android.view.Gravity.CENTER_VERTICAL);
                    break;
                case android.media.TextTrackCue.ALIGNMENT_RIGHT :
                    setGravity(android.view.Gravity.RIGHT);
                    break;
                case android.media.TextTrackCue.ALIGNMENT_START :
                    setGravity(android.view.Gravity.START);
                    break;
            }
            if (android.media.WebVttRenderingWidget.DEBUG) {
                setBackgroundColor(android.media.WebVttRenderingWidget.DEBUG_CUE_BACKGROUND);
            }
            update();
        }

        public void setCaptionStyle(android.view.accessibility.CaptioningManager.CaptionStyle style, float fontSize) {
            mCaptionStyle = style;
            mFontSize = fontSize;
            final int n = getChildCount();
            for (int i = 0; i < n; i++) {
                final android.view.View child = getChildAt(i);
                if (child instanceof android.media.WebVttRenderingWidget.SpanLayout) {
                    ((android.media.WebVttRenderingWidget.SpanLayout) (child)).setCaptionStyle(style, fontSize);
                }
            }
        }

        public void prepForPrune() {
            mActive = false;
        }

        public void update() {
            mActive = true;
            removeAllViews();
            final int cueAlignment = android.media.WebVttRenderingWidget.resolveCueAlignment(getLayoutDirection(), mCue.mAlignment);
            final android.text.Layout.Alignment alignment;
            switch (cueAlignment) {
                case android.media.TextTrackCue.ALIGNMENT_LEFT :
                    alignment = android.text.Layout.Alignment.ALIGN_LEFT;
                    break;
                case android.media.TextTrackCue.ALIGNMENT_RIGHT :
                    alignment = android.text.Layout.Alignment.ALIGN_RIGHT;
                    break;
                case android.media.TextTrackCue.ALIGNMENT_MIDDLE :
                default :
                    alignment = android.text.Layout.Alignment.ALIGN_CENTER;
            }
            final android.view.accessibility.CaptioningManager.CaptionStyle captionStyle = mCaptionStyle;
            final float fontSize = mFontSize;
            final android.media.TextTrackCueSpan[][] lines = mCue.mLines;
            final int lineCount = lines.length;
            for (int i = 0; i < lineCount; i++) {
                final android.media.WebVttRenderingWidget.SpanLayout lineBox = new android.media.WebVttRenderingWidget.SpanLayout(getContext(), lines[i]);
                lineBox.setAlignment(alignment);
                lineBox.setCaptionStyle(captionStyle, fontSize);
                addView(lineBox, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        }

        @java.lang.Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        /**
         * Performs the parent's measurement responsibilities, then
         * automatically performs its own measurement.
         */
        public void measureForParent(int widthMeasureSpec, int heightMeasureSpec) {
            final android.media.TextTrackCue cue = mCue;
            final int specWidth = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
            final int specHeight = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
            final int direction = getLayoutDirection();
            final int absAlignment = android.media.WebVttRenderingWidget.resolveCueAlignment(direction, cue.mAlignment);
            // Determine the maximum size of cue based on its starting position
            // and the direction in which it grows.
            final int maximumSize;
            switch (absAlignment) {
                case android.media.TextTrackCue.ALIGNMENT_LEFT :
                    maximumSize = 100 - cue.mTextPosition;
                    break;
                case android.media.TextTrackCue.ALIGNMENT_RIGHT :
                    maximumSize = cue.mTextPosition;
                    break;
                case android.media.TextTrackCue.ALIGNMENT_MIDDLE :
                    if (cue.mTextPosition <= 50) {
                        maximumSize = cue.mTextPosition * 2;
                    } else {
                        maximumSize = (100 - cue.mTextPosition) * 2;
                    }
                    break;
                default :
                    maximumSize = 0;
            }
            // Determine absolute maximum cue size as the smaller of the
            // requested size and the maximum theoretical size.
            final int size = (java.lang.Math.min(cue.mSize, maximumSize) * specWidth) / 100;
            widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(size, android.view.View.MeasureSpec.AT_MOST);
            heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(specHeight, android.view.View.MeasureSpec.AT_MOST);
            measure(widthMeasureSpec, heightMeasureSpec);
        }

        /**
         * Sets the order of this cue in the list of active cues.
         *
         * @param order
         * 		the order of this cue in the list of active cues
         */
        public void setOrder(int order) {
            mOrder = order;
        }

        /**
         *
         *
         * @return whether this cue is marked as active
         */
        public boolean isActive() {
            return mActive;
        }

        /**
         *
         *
         * @return the cue data backing this layout
         */
        public android.media.TextTrackCue getCue() {
            return mCue;
        }
    }

    /**
     * A text track line represents a single line of text within a cue.
     * <p>
     * A single line may contain multiple spans, each representing a section of
     * text that may be enabled or disabled at a particular time.
     */
    private static class SpanLayout extends com.android.internal.widget.SubtitleView {
        private final android.text.SpannableStringBuilder mBuilder = new android.text.SpannableStringBuilder();

        private final android.media.TextTrackCueSpan[] mSpans;

        public SpanLayout(android.content.Context context, android.media.TextTrackCueSpan[] spans) {
            super(context);
            mSpans = spans;
            update();
        }

        public void update() {
            final android.text.SpannableStringBuilder builder = mBuilder;
            final android.media.TextTrackCueSpan[] spans = mSpans;
            builder.clear();
            builder.clearSpans();
            final int spanCount = spans.length;
            for (int i = 0; i < spanCount; i++) {
                final android.media.TextTrackCueSpan span = spans[i];
                if (span.mEnabled) {
                    builder.append(spans[i].mText);
                }
            }
            setText(builder);
        }

        public void setCaptionStyle(android.view.accessibility.CaptioningManager.CaptionStyle captionStyle, float fontSize) {
            setBackgroundColor(captionStyle.backgroundColor);
            setForegroundColor(captionStyle.foregroundColor);
            setEdgeColor(captionStyle.edgeColor);
            setEdgeType(captionStyle.edgeType);
            setTypeface(captionStyle.getTypeface());
            setTextSize(fontSize);
        }
    }
}

