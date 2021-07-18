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
 * <p>A {@link android.widget.TextView} that adjusts text size automatically in response
 * to certain trigger conditions, such as text that wraps over multiple lines.</p>
 */
class ResizingTextView extends android.widget.TextView {
    /**
     * Trigger text resize when text flows into the last line of a multi-line text view.
     */
    public static final int TRIGGER_MAX_LINES = 0x1;

    private int mTriggerConditions;// Union of trigger conditions


    private int mResizedTextSize;

    // Note: Maintaining line spacing turned out not to be useful, and will be removed in
    // the next round of design for this class (b/18736630). For now it simply defaults to false.
    private boolean mMaintainLineSpacing;

    private int mResizedPaddingAdjustmentTop;

    private int mResizedPaddingAdjustmentBottom;

    private boolean mIsResized = false;

    // Remember default properties in case we need to restore them
    private boolean mDefaultsInitialized = false;

    private int mDefaultTextSize;

    private float mDefaultLineSpacingExtra;

    private int mDefaultPaddingTop;

    private int mDefaultPaddingBottom;

    public ResizingTextView(android.content.Context ctx, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(ctx, attrs, defStyleAttr);
        android.content.res.TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.lbResizingTextView, defStyleAttr, defStyleRes);
        try {
            mTriggerConditions = a.getInt(R.styleable.lbResizingTextView_resizeTrigger, android.support.v17.leanback.widget.ResizingTextView.TRIGGER_MAX_LINES);
            mResizedTextSize = a.getDimensionPixelSize(R.styleable.lbResizingTextView_resizedTextSize, -1);
            mMaintainLineSpacing = a.getBoolean(R.styleable.lbResizingTextView_maintainLineSpacing, false);
            mResizedPaddingAdjustmentTop = a.getDimensionPixelOffset(R.styleable.lbResizingTextView_resizedPaddingAdjustmentTop, 0);
            mResizedPaddingAdjustmentBottom = a.getDimensionPixelOffset(R.styleable.lbResizingTextView_resizedPaddingAdjustmentBottom, 0);
        } finally {
            a.recycle();
        }
    }

    public ResizingTextView(android.content.Context ctx, android.util.AttributeSet attrs, int defStyleAttr) {
        this(ctx, attrs, defStyleAttr, 0);
    }

    public ResizingTextView(android.content.Context ctx, android.util.AttributeSet attrs) {
        // TODO We should define our own style that inherits from TextViewStyle, to set defaults
        // for new styleables,  We then pass the appropriate R.attr up the constructor chain here.
        this(ctx, attrs, android.R.attr.textViewStyle);
    }

    public ResizingTextView(android.content.Context ctx) {
        this(ctx, null);
    }

    /**
     *
     *
     * @return the trigger conditions used to determine whether resize occurs
     */
    public int getTriggerConditions() {
        return mTriggerConditions;
    }

    /**
     * Set the trigger conditions used to determine whether resize occurs. Pass
     * a union of trigger condition constants, such as {@link ResizingTextView#TRIGGER_MAX_LINES}.
     *
     * @param conditions
     * 		A union of trigger condition constants
     */
    public void setTriggerConditions(int conditions) {
        if (mTriggerConditions != conditions) {
            mTriggerConditions = conditions;
            // Always request a layout when trigger conditions change
            requestLayout();
        }
    }

    /**
     *
     *
     * @return the resized text size
     */
    public int getResizedTextSize() {
        return mResizedTextSize;
    }

    /**
     * Set the text size for resized text.
     *
     * @param size
     * 		The text size for resized text
     */
    public void setResizedTextSize(int size) {
        if (mResizedTextSize != size) {
            mResizedTextSize = size;
            resizeParamsChanged();
        }
    }

    /**
     *
     *
     * @return whether or not to maintain line spacing when resizing text.
    The default is true.
     */
    public boolean getMaintainLineSpacing() {
        return mMaintainLineSpacing;
    }

    /**
     * Set whether or not to maintain line spacing when resizing text.
     * The default is true.
     *
     * @param maintain
     * 		Whether or not to maintain line spacing
     */
    public void setMaintainLineSpacing(boolean maintain) {
        if (mMaintainLineSpacing != maintain) {
            mMaintainLineSpacing = maintain;
            resizeParamsChanged();
        }
    }

    /**
     *
     *
     * @return desired adjustment to top padding for resized text
     */
    public int getResizedPaddingAdjustmentTop() {
        return mResizedPaddingAdjustmentTop;
    }

    /**
     * Set the desired adjustment to top padding for resized text.
     *
     * @param adjustment
     * 		The adjustment to top padding, in pixels
     */
    public void setResizedPaddingAdjustmentTop(int adjustment) {
        if (mResizedPaddingAdjustmentTop != adjustment) {
            mResizedPaddingAdjustmentTop = adjustment;
            resizeParamsChanged();
        }
    }

    /**
     *
     *
     * @return desired adjustment to bottom padding for resized text
     */
    public int getResizedPaddingAdjustmentBottom() {
        return mResizedPaddingAdjustmentBottom;
    }

    /**
     * Set the desired adjustment to bottom padding for resized text.
     *
     * @param adjustment
     * 		The adjustment to bottom padding, in pixels
     */
    public void setResizedPaddingAdjustmentBottom(int adjustment) {
        if (mResizedPaddingAdjustmentBottom != adjustment) {
            mResizedPaddingAdjustmentBottom = adjustment;
            resizeParamsChanged();
        }
    }

    private void resizeParamsChanged() {
        // If we're not resized, then changing resize parameters doesn't
        // affect layout, so don't bother requesting.
        if (mIsResized) {
            requestLayout();
        }
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!mDefaultsInitialized) {
            mDefaultTextSize = ((int) (getTextSize()));
            mDefaultLineSpacingExtra = getLineSpacingExtra();
            mDefaultPaddingTop = getPaddingTop();
            mDefaultPaddingBottom = getPaddingBottom();
            mDefaultsInitialized = true;
        }
        // Always try first to measure with defaults. Otherwise, we may think we can get away
        // with larger text sizes later when we actually can't.
        setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDefaultTextSize);
        setLineSpacing(mDefaultLineSpacingExtra, getLineSpacingMultiplier());
        setPaddingTopAndBottom(mDefaultPaddingTop, mDefaultPaddingBottom);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        boolean resizeText = false;
        final android.text.Layout layout = getLayout();
        if (layout != null) {
            if ((mTriggerConditions & android.support.v17.leanback.widget.ResizingTextView.TRIGGER_MAX_LINES) > 0) {
                final int lineCount = layout.getLineCount();
                final int maxLines = getMaxLines();
                if (maxLines > 1) {
                    resizeText = lineCount == maxLines;
                }
            }
        }
        final int currentSizePx = ((int) (getTextSize()));
        boolean remeasure = false;
        if (resizeText) {
            if ((mResizedTextSize != (-1)) && (currentSizePx != mResizedTextSize)) {
                setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mResizedTextSize);
                remeasure = true;
            }
            // Check for other desired adjustments in addition to the text size
            final float targetLineSpacingExtra = (mDefaultLineSpacingExtra + mDefaultTextSize) - mResizedTextSize;
            if (mMaintainLineSpacing && (getLineSpacingExtra() != targetLineSpacingExtra)) {
                setLineSpacing(targetLineSpacingExtra, getLineSpacingMultiplier());
                remeasure = true;
            }
            final int paddingTop = mDefaultPaddingTop + mResizedPaddingAdjustmentTop;
            final int paddingBottom = mDefaultPaddingBottom + mResizedPaddingAdjustmentBottom;
            if ((getPaddingTop() != paddingTop) || (getPaddingBottom() != paddingBottom)) {
                setPaddingTopAndBottom(paddingTop, paddingBottom);
                remeasure = true;
            }
        } else {
            // Use default size, line spacing, and padding
            if ((mResizedTextSize != (-1)) && (currentSizePx != mDefaultTextSize)) {
                setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mDefaultTextSize);
                remeasure = true;
            }
            if (mMaintainLineSpacing && (getLineSpacingExtra() != mDefaultLineSpacingExtra)) {
                setLineSpacing(mDefaultLineSpacingExtra, getLineSpacingMultiplier());
                remeasure = true;
            }
            if ((getPaddingTop() != mDefaultPaddingTop) || (getPaddingBottom() != mDefaultPaddingBottom)) {
                setPaddingTopAndBottom(mDefaultPaddingTop, mDefaultPaddingBottom);
                remeasure = true;
            }
        }
        mIsResized = resizeText;
        if (remeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void setPaddingTopAndBottom(int paddingTop, int paddingBottom) {
        if (isPaddingRelative()) {
            setPaddingRelative(getPaddingStart(), paddingTop, getPaddingEnd(), paddingBottom);
        } else {
            setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
        }
    }
}

