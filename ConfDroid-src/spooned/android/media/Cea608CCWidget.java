package android.media;


/**
 * Widget capable of rendering CEA-608 closed captions.
 *
 * @unknown 
 */
class Cea608CCWidget extends android.media.ClosedCaptionWidget implements android.media.Cea608CCParser.DisplayListener {
    private static final android.graphics.Rect mTextBounds = new android.graphics.Rect();

    private static final java.lang.String mDummyText = "1234567890123456789012345678901234";

    public Cea608CCWidget(android.content.Context context) {
        this(context, null);
    }

    public Cea608CCWidget(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Cea608CCWidget(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public Cea608CCWidget(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @java.lang.Override
    public android.media.ClosedCaptionWidget.ClosedCaptionLayout createCaptionLayout(android.content.Context context) {
        return new android.media.Cea608CCWidget.CCLayout(context);
    }

    @java.lang.Override
    public void onDisplayChanged(android.text.SpannableStringBuilder[] styledTexts) {
        ((android.media.Cea608CCWidget.CCLayout) (mClosedCaptionLayout)).update(styledTexts);
        if (mListener != null) {
            mListener.onChanged(this);
        }
    }

    @java.lang.Override
    public android.view.accessibility.CaptioningManager.CaptionStyle getCaptionStyle() {
        return mCaptionStyle;
    }

    private static class CCLineBox extends android.widget.TextView {
        private static final float FONT_PADDING_RATIO = 0.75F;

        private static final float EDGE_OUTLINE_RATIO = 0.1F;

        private static final float EDGE_SHADOW_RATIO = 0.05F;

        private float mOutlineWidth;

        private float mShadowRadius;

        private float mShadowOffset;

        private int mTextColor = android.graphics.Color.WHITE;

        private int mBgColor = android.graphics.Color.BLACK;

        private int mEdgeType = android.view.accessibility.CaptioningManager.CaptionStyle.EDGE_TYPE_NONE;

        private int mEdgeColor = android.graphics.Color.TRANSPARENT;

        CCLineBox(android.content.Context context) {
            super(context);
            setGravity(android.view.Gravity.CENTER);
            setBackgroundColor(android.graphics.Color.TRANSPARENT);
            setTextColor(android.graphics.Color.WHITE);
            setTypeface(android.graphics.Typeface.MONOSPACE);
            setVisibility(android.view.View.INVISIBLE);
            final android.content.res.Resources res = getContext().getResources();
            // get the default (will be updated later during measure)
            mOutlineWidth = res.getDimensionPixelSize(com.android.internal.R.dimen.subtitle_outline_width);
            mShadowRadius = res.getDimensionPixelSize(com.android.internal.R.dimen.subtitle_shadow_radius);
            mShadowOffset = res.getDimensionPixelSize(com.android.internal.R.dimen.subtitle_shadow_offset);
        }

        void setCaptionStyle(android.view.accessibility.CaptioningManager.CaptionStyle captionStyle) {
            mTextColor = captionStyle.foregroundColor;
            mBgColor = captionStyle.backgroundColor;
            mEdgeType = captionStyle.edgeType;
            mEdgeColor = captionStyle.edgeColor;
            setTextColor(mTextColor);
            if (mEdgeType == android.view.accessibility.CaptioningManager.CaptionStyle.EDGE_TYPE_DROP_SHADOW) {
                setShadowLayer(mShadowRadius, mShadowOffset, mShadowOffset, mEdgeColor);
            } else {
                setShadowLayer(0, 0, 0, 0);
            }
            invalidate();
        }

        @java.lang.Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            float fontSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec) * android.media.Cea608CCWidget.CCLineBox.FONT_PADDING_RATIO;
            setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, fontSize);
            mOutlineWidth = (android.media.Cea608CCWidget.CCLineBox.EDGE_OUTLINE_RATIO * fontSize) + 1.0F;
            mShadowRadius = (android.media.Cea608CCWidget.CCLineBox.EDGE_SHADOW_RATIO * fontSize) + 1.0F;
            mShadowOffset = mShadowRadius;
            // set font scale in the X direction to match the required width
            setScaleX(1.0F);
            getPaint().getTextBounds(android.media.Cea608CCWidget.mDummyText, 0, android.media.Cea608CCWidget.mDummyText.length(), android.media.Cea608CCWidget.mTextBounds);
            float actualTextWidth = android.media.Cea608CCWidget.mTextBounds.width();
            float requiredTextWidth = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
            setScaleX(requiredTextWidth / actualTextWidth);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @java.lang.Override
        protected void onDraw(android.graphics.Canvas c) {
            if (((mEdgeType == android.view.accessibility.CaptioningManager.CaptionStyle.EDGE_TYPE_UNSPECIFIED) || (mEdgeType == android.view.accessibility.CaptioningManager.CaptionStyle.EDGE_TYPE_NONE)) || (mEdgeType == android.view.accessibility.CaptioningManager.CaptionStyle.EDGE_TYPE_DROP_SHADOW)) {
                // these edge styles don't require a second pass
                super.onDraw(c);
                return;
            }
            if (mEdgeType == android.view.accessibility.CaptioningManager.CaptionStyle.EDGE_TYPE_OUTLINE) {
                drawEdgeOutline(c);
            } else {
                // Raised or depressed
                drawEdgeRaisedOrDepressed(c);
            }
        }

        private void drawEdgeOutline(android.graphics.Canvas c) {
            android.text.TextPaint textPaint = getPaint();
            android.graphics.Paint.Style previousStyle = textPaint.getStyle();
            android.graphics.Paint.Join previousJoin = textPaint.getStrokeJoin();
            float previousWidth = textPaint.getStrokeWidth();
            setTextColor(mEdgeColor);
            textPaint.setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
            textPaint.setStrokeJoin(android.graphics.Paint.Join.ROUND);
            textPaint.setStrokeWidth(mOutlineWidth);
            // Draw outline and background only.
            super.onDraw(c);
            // Restore original settings.
            setTextColor(mTextColor);
            textPaint.setStyle(previousStyle);
            textPaint.setStrokeJoin(previousJoin);
            textPaint.setStrokeWidth(previousWidth);
            // Remove the background.
            setBackgroundSpans(android.graphics.Color.TRANSPARENT);
            // Draw foreground only.
            super.onDraw(c);
            // Restore the background.
            setBackgroundSpans(mBgColor);
        }

        private void drawEdgeRaisedOrDepressed(android.graphics.Canvas c) {
            android.text.TextPaint textPaint = getPaint();
            android.graphics.Paint.Style previousStyle = textPaint.getStyle();
            textPaint.setStyle(android.graphics.Paint.Style.FILL);
            final boolean raised = mEdgeType == android.view.accessibility.CaptioningManager.CaptionStyle.EDGE_TYPE_RAISED;
            final int colorUp = (raised) ? android.graphics.Color.WHITE : mEdgeColor;
            final int colorDown = (raised) ? mEdgeColor : android.graphics.Color.WHITE;
            final float offset = mShadowRadius / 2.0F;
            // Draw background and text with shadow up
            setShadowLayer(mShadowRadius, -offset, -offset, colorUp);
            super.onDraw(c);
            // Remove the background.
            setBackgroundSpans(android.graphics.Color.TRANSPARENT);
            // Draw text with shadow down
            setShadowLayer(mShadowRadius, +offset, +offset, colorDown);
            super.onDraw(c);
            // Restore settings
            textPaint.setStyle(previousStyle);
            // Restore the background.
            setBackgroundSpans(mBgColor);
        }

        private void setBackgroundSpans(int color) {
            java.lang.CharSequence text = getText();
            if (text instanceof android.text.Spannable) {
                android.text.Spannable spannable = ((android.text.Spannable) (text));
                android.media.Cea608CCParser.MutableBackgroundColorSpan[] bgSpans = spannable.getSpans(0, spannable.length(), android.media.Cea608CCParser.MutableBackgroundColorSpan.class);
                for (int i = 0; i < bgSpans.length; i++) {
                    bgSpans[i].setBackgroundColor(color);
                }
            }
        }
    }

    private static class CCLayout extends android.widget.LinearLayout implements android.media.ClosedCaptionWidget.ClosedCaptionLayout {
        private static final int MAX_ROWS = android.media.Cea608CCParser.MAX_ROWS;

        private static final float SAFE_AREA_RATIO = 0.9F;

        private final android.media.Cea608CCWidget.CCLineBox[] mLineBoxes = new android.media.Cea608CCWidget.CCLineBox[android.media.Cea608CCWidget.CCLayout.MAX_ROWS];

        CCLayout(android.content.Context context) {
            super(context);
            setGravity(android.view.Gravity.START);
            setOrientation(android.widget.LinearLayout.VERTICAL);
            for (int i = 0; i < android.media.Cea608CCWidget.CCLayout.MAX_ROWS; i++) {
                mLineBoxes[i] = new android.media.Cea608CCWidget.CCLineBox(getContext());
                addView(mLineBoxes[i], android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        }

        @java.lang.Override
        public void setCaptionStyle(android.view.accessibility.CaptioningManager.CaptionStyle captionStyle) {
            for (int i = 0; i < android.media.Cea608CCWidget.CCLayout.MAX_ROWS; i++) {
                mLineBoxes[i].setCaptionStyle(captionStyle);
            }
        }

        @java.lang.Override
        public void setFontScale(float fontScale) {
            // Ignores the font scale changes of the system wide CC preference.
        }

        void update(android.text.SpannableStringBuilder[] textBuffer) {
            for (int i = 0; i < android.media.Cea608CCWidget.CCLayout.MAX_ROWS; i++) {
                if (textBuffer[i] != null) {
                    mLineBoxes[i].setText(textBuffer[i], android.widget.TextView.BufferType.SPANNABLE);
                    mLineBoxes[i].setVisibility(android.view.View.VISIBLE);
                } else {
                    mLineBoxes[i].setVisibility(android.view.View.INVISIBLE);
                }
            }
        }

        @java.lang.Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int safeWidth = getMeasuredWidth();
            int safeHeight = getMeasuredHeight();
            // CEA-608 assumes 4:3 video
            if ((safeWidth * 3) >= (safeHeight * 4)) {
                safeWidth = (safeHeight * 4) / 3;
            } else {
                safeHeight = (safeWidth * 3) / 4;
            }
            safeWidth *= android.media.Cea608CCWidget.CCLayout.SAFE_AREA_RATIO;
            safeHeight *= android.media.Cea608CCWidget.CCLayout.SAFE_AREA_RATIO;
            int lineHeight = safeHeight / android.media.Cea608CCWidget.CCLayout.MAX_ROWS;
            int lineHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(lineHeight, android.view.View.MeasureSpec.EXACTLY);
            int lineWidthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(safeWidth, android.view.View.MeasureSpec.EXACTLY);
            for (int i = 0; i < android.media.Cea608CCWidget.CCLayout.MAX_ROWS; i++) {
                mLineBoxes[i].measure(lineWidthMeasureSpec, lineHeightMeasureSpec);
            }
        }

        @java.lang.Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            // safe caption area
            int viewPortWidth = r - l;
            int viewPortHeight = b - t;
            int safeWidth;
            int safeHeight;
            // CEA-608 assumes 4:3 video
            if ((viewPortWidth * 3) >= (viewPortHeight * 4)) {
                safeWidth = (viewPortHeight * 4) / 3;
                safeHeight = viewPortHeight;
            } else {
                safeWidth = viewPortWidth;
                safeHeight = (viewPortWidth * 3) / 4;
            }
            safeWidth *= android.media.Cea608CCWidget.CCLayout.SAFE_AREA_RATIO;
            safeHeight *= android.media.Cea608CCWidget.CCLayout.SAFE_AREA_RATIO;
            int left = (viewPortWidth - safeWidth) / 2;
            int top = (viewPortHeight - safeHeight) / 2;
            for (int i = 0; i < android.media.Cea608CCWidget.CCLayout.MAX_ROWS; i++) {
                mLineBoxes[i].layout(left, top + ((safeHeight * i) / android.media.Cea608CCWidget.CCLayout.MAX_ROWS), left + safeWidth, top + ((safeHeight * (i + 1)) / android.media.Cea608CCWidget.CCLayout.MAX_ROWS));
            }
        }
    }
}

