/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.support.design.widget;


final class CollapsingTextHelper {
    // Pre-JB-MR2 doesn't support HW accelerated canvas scaled text so we will workaround it
    // by using our own texture
    private static final boolean USE_SCALING_TEXTURE = android.os.Build.VERSION.SDK_INT < 18;

    private static final boolean DEBUG_DRAW = false;

    private static final android.graphics.Paint DEBUG_DRAW_PAINT;

    static {
        DEBUG_DRAW_PAINT = (android.support.design.widget.CollapsingTextHelper.DEBUG_DRAW) ? new android.graphics.Paint() : null;
        if (android.support.design.widget.CollapsingTextHelper.DEBUG_DRAW_PAINT != null) {
            android.support.design.widget.CollapsingTextHelper.DEBUG_DRAW_PAINT.setAntiAlias(true);
            android.support.design.widget.CollapsingTextHelper.DEBUG_DRAW_PAINT.setColor(android.graphics.Color.MAGENTA);
        }
    }

    private final android.view.View mView;

    private boolean mDrawTitle;

    private float mExpandedFraction;

    private final android.graphics.Rect mExpandedBounds;

    private final android.graphics.Rect mCollapsedBounds;

    private final android.graphics.RectF mCurrentBounds;

    private int mExpandedTextGravity = android.view.Gravity.CENTER_VERTICAL;

    private int mCollapsedTextGravity = android.view.Gravity.CENTER_VERTICAL;

    private float mExpandedTextSize = 15;

    private float mCollapsedTextSize = 15;

    private android.content.res.ColorStateList mExpandedTextColor;

    private android.content.res.ColorStateList mCollapsedTextColor;

    private float mExpandedDrawY;

    private float mCollapsedDrawY;

    private float mExpandedDrawX;

    private float mCollapsedDrawX;

    private float mCurrentDrawX;

    private float mCurrentDrawY;

    private android.graphics.Typeface mCollapsedTypeface;

    private android.graphics.Typeface mExpandedTypeface;

    private android.graphics.Typeface mCurrentTypeface;

    private java.lang.CharSequence mText;

    private java.lang.CharSequence mTextToDraw;

    private boolean mIsRtl;

    private boolean mUseTexture;

    private android.graphics.Bitmap mExpandedTitleTexture;

    private android.graphics.Paint mTexturePaint;

    private float mTextureAscent;

    private float mTextureDescent;

    private float mScale;

    private float mCurrentTextSize;

    private int[] mState;

    private boolean mBoundsChanged;

    private final android.text.TextPaint mTextPaint;

    private android.view.animation.Interpolator mPositionInterpolator;

    private android.view.animation.Interpolator mTextSizeInterpolator;

    private float mCollapsedShadowRadius;

    private float mCollapsedShadowDx;

    private float mCollapsedShadowDy;

    private int mCollapsedShadowColor;

    private float mExpandedShadowRadius;

    private float mExpandedShadowDx;

    private float mExpandedShadowDy;

    private int mExpandedShadowColor;

    public CollapsingTextHelper(android.view.View view) {
        mView = view;
        mTextPaint = new android.text.TextPaint(android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.SUBPIXEL_TEXT_FLAG);
        mCollapsedBounds = new android.graphics.Rect();
        mExpandedBounds = new android.graphics.Rect();
        mCurrentBounds = new android.graphics.RectF();
    }

    void setTextSizeInterpolator(android.view.animation.Interpolator interpolator) {
        mTextSizeInterpolator = interpolator;
        recalculate();
    }

    void setPositionInterpolator(android.view.animation.Interpolator interpolator) {
        mPositionInterpolator = interpolator;
        recalculate();
    }

    void setExpandedTextSize(float textSize) {
        if (mExpandedTextSize != textSize) {
            mExpandedTextSize = textSize;
            recalculate();
        }
    }

    void setCollapsedTextSize(float textSize) {
        if (mCollapsedTextSize != textSize) {
            mCollapsedTextSize = textSize;
            recalculate();
        }
    }

    void setCollapsedTextColor(android.content.res.ColorStateList textColor) {
        if (mCollapsedTextColor != textColor) {
            mCollapsedTextColor = textColor;
            recalculate();
        }
    }

    void setExpandedTextColor(android.content.res.ColorStateList textColor) {
        if (mExpandedTextColor != textColor) {
            mExpandedTextColor = textColor;
            recalculate();
        }
    }

    void setExpandedBounds(int left, int top, int right, int bottom) {
        if (!android.support.design.widget.CollapsingTextHelper.rectEquals(mExpandedBounds, left, top, right, bottom)) {
            mExpandedBounds.set(left, top, right, bottom);
            mBoundsChanged = true;
            onBoundsChanged();
        }
    }

    void setCollapsedBounds(int left, int top, int right, int bottom) {
        if (!android.support.design.widget.CollapsingTextHelper.rectEquals(mCollapsedBounds, left, top, right, bottom)) {
            mCollapsedBounds.set(left, top, right, bottom);
            mBoundsChanged = true;
            onBoundsChanged();
        }
    }

    void onBoundsChanged() {
        mDrawTitle = (((mCollapsedBounds.width() > 0) && (mCollapsedBounds.height() > 0)) && (mExpandedBounds.width() > 0)) && (mExpandedBounds.height() > 0);
    }

    void setExpandedTextGravity(int gravity) {
        if (mExpandedTextGravity != gravity) {
            mExpandedTextGravity = gravity;
            recalculate();
        }
    }

    int getExpandedTextGravity() {
        return mExpandedTextGravity;
    }

    void setCollapsedTextGravity(int gravity) {
        if (mCollapsedTextGravity != gravity) {
            mCollapsedTextGravity = gravity;
            recalculate();
        }
    }

    int getCollapsedTextGravity() {
        return mCollapsedTextGravity;
    }

    void setCollapsedTextAppearance(int resId) {
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(mView.getContext(), resId, android.support.v7.appcompat.R.styleable.TextAppearance);
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor)) {
            mCollapsedTextColor = a.getColorStateList(android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor);
        }
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize)) {
            mCollapsedTextSize = a.getDimensionPixelSize(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize, ((int) (mCollapsedTextSize)));
        }
        mCollapsedShadowColor = a.getInt(android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowColor, 0);
        mCollapsedShadowDx = a.getFloat(android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDx, 0);
        mCollapsedShadowDy = a.getFloat(android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDy, 0);
        mCollapsedShadowRadius = a.getFloat(android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowRadius, 0);
        a.recycle();
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            mCollapsedTypeface = readFontFamilyTypeface(resId);
        }
        recalculate();
    }

    void setExpandedTextAppearance(int resId) {
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(mView.getContext(), resId, android.support.v7.appcompat.R.styleable.TextAppearance);
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor)) {
            mExpandedTextColor = a.getColorStateList(android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor);
        }
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize)) {
            mExpandedTextSize = a.getDimensionPixelSize(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize, ((int) (mExpandedTextSize)));
        }
        mExpandedShadowColor = a.getInt(android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowColor, 0);
        mExpandedShadowDx = a.getFloat(android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDx, 0);
        mExpandedShadowDy = a.getFloat(android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDy, 0);
        mExpandedShadowRadius = a.getFloat(android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowRadius, 0);
        a.recycle();
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            mExpandedTypeface = readFontFamilyTypeface(resId);
        }
        recalculate();
    }

    private android.graphics.Typeface readFontFamilyTypeface(int resId) {
        final android.content.res.TypedArray a = mView.getContext().obtainStyledAttributes(resId, new int[]{ android.R.attr.fontFamily });
        try {
            final java.lang.String family = a.getString(0);
            if (family != null) {
                return android.graphics.Typeface.create(family, android.graphics.Typeface.NORMAL);
            }
        } finally {
            a.recycle();
        }
        return null;
    }

    void setCollapsedTypeface(android.graphics.Typeface typeface) {
        if (mCollapsedTypeface != typeface) {
            mCollapsedTypeface = typeface;
            recalculate();
        }
    }

    void setExpandedTypeface(android.graphics.Typeface typeface) {
        if (mExpandedTypeface != typeface) {
            mExpandedTypeface = typeface;
            recalculate();
        }
    }

    void setTypefaces(android.graphics.Typeface typeface) {
        mCollapsedTypeface = mExpandedTypeface = typeface;
        recalculate();
    }

    android.graphics.Typeface getCollapsedTypeface() {
        return mCollapsedTypeface != null ? mCollapsedTypeface : android.graphics.Typeface.DEFAULT;
    }

    android.graphics.Typeface getExpandedTypeface() {
        return mExpandedTypeface != null ? mExpandedTypeface : android.graphics.Typeface.DEFAULT;
    }

    /**
     * Set the value indicating the current scroll value. This decides how much of the
     * background will be displayed, as well as the title metrics/positioning.
     *
     * A value of {@code 0.0} indicates that the layout is fully expanded.
     * A value of {@code 1.0} indicates that the layout is fully collapsed.
     */
    void setExpansionFraction(float fraction) {
        fraction = android.support.design.widget.MathUtils.constrain(fraction, 0.0F, 1.0F);
        if (fraction != mExpandedFraction) {
            mExpandedFraction = fraction;
            calculateCurrentOffsets();
        }
    }

    final boolean setState(final int[] state) {
        mState = state;
        if (isStateful()) {
            recalculate();
            return true;
        }
        return false;
    }

    final boolean isStateful() {
        return ((mCollapsedTextColor != null) && mCollapsedTextColor.isStateful()) || ((mExpandedTextColor != null) && mExpandedTextColor.isStateful());
    }

    float getExpansionFraction() {
        return mExpandedFraction;
    }

    float getCollapsedTextSize() {
        return mCollapsedTextSize;
    }

    float getExpandedTextSize() {
        return mExpandedTextSize;
    }

    private void calculateCurrentOffsets() {
        calculateOffsets(mExpandedFraction);
    }

    private void calculateOffsets(final float fraction) {
        interpolateBounds(fraction);
        mCurrentDrawX = android.support.design.widget.CollapsingTextHelper.lerp(mExpandedDrawX, mCollapsedDrawX, fraction, mPositionInterpolator);
        mCurrentDrawY = android.support.design.widget.CollapsingTextHelper.lerp(mExpandedDrawY, mCollapsedDrawY, fraction, mPositionInterpolator);
        setInterpolatedTextSize(android.support.design.widget.CollapsingTextHelper.lerp(mExpandedTextSize, mCollapsedTextSize, fraction, mTextSizeInterpolator));
        if (mCollapsedTextColor != mExpandedTextColor) {
            // If the collapsed and expanded text colors are different, blend them based on the
            // fraction
            mTextPaint.setColor(android.support.design.widget.CollapsingTextHelper.blendColors(getCurrentExpandedTextColor(), getCurrentCollapsedTextColor(), fraction));
        } else {
            mTextPaint.setColor(getCurrentCollapsedTextColor());
        }
        mTextPaint.setShadowLayer(android.support.design.widget.CollapsingTextHelper.lerp(mExpandedShadowRadius, mCollapsedShadowRadius, fraction, null), android.support.design.widget.CollapsingTextHelper.lerp(mExpandedShadowDx, mCollapsedShadowDx, fraction, null), android.support.design.widget.CollapsingTextHelper.lerp(mExpandedShadowDy, mCollapsedShadowDy, fraction, null), android.support.design.widget.CollapsingTextHelper.blendColors(mExpandedShadowColor, mCollapsedShadowColor, fraction));
        android.support.v4.view.ViewCompat.postInvalidateOnAnimation(mView);
    }

    @android.support.annotation.ColorInt
    private int getCurrentExpandedTextColor() {
        if (mState != null) {
            return mExpandedTextColor.getColorForState(mState, 0);
        } else {
            return mExpandedTextColor.getDefaultColor();
        }
    }

    @android.support.annotation.ColorInt
    private int getCurrentCollapsedTextColor() {
        if (mState != null) {
            return mCollapsedTextColor.getColorForState(mState, 0);
        } else {
            return mCollapsedTextColor.getDefaultColor();
        }
    }

    private void calculateBaseOffsets() {
        final float currentTextSize = mCurrentTextSize;
        // We then calculate the collapsed text size, using the same logic
        calculateUsingTextSize(mCollapsedTextSize);
        float width = (mTextToDraw != null) ? mTextPaint.measureText(mTextToDraw, 0, mTextToDraw.length()) : 0;
        final int collapsedAbsGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(mCollapsedTextGravity, mIsRtl ? android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL : android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR);
        switch (collapsedAbsGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) {
            case android.view.Gravity.BOTTOM :
                mCollapsedDrawY = mCollapsedBounds.bottom;
                break;
            case android.view.Gravity.TOP :
                mCollapsedDrawY = mCollapsedBounds.top - mTextPaint.ascent();
                break;
            case android.view.Gravity.CENTER_VERTICAL :
            default :
                float textHeight = mTextPaint.descent() - mTextPaint.ascent();
                float textOffset = (textHeight / 2) - mTextPaint.descent();
                mCollapsedDrawY = mCollapsedBounds.centerY() + textOffset;
                break;
        }
        switch (collapsedAbsGravity & android.support.v4.view.GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
            case android.view.Gravity.CENTER_HORIZONTAL :
                mCollapsedDrawX = mCollapsedBounds.centerX() - (width / 2);
                break;
            case android.view.Gravity.RIGHT :
                mCollapsedDrawX = mCollapsedBounds.right - width;
                break;
            case android.view.Gravity.LEFT :
            default :
                mCollapsedDrawX = mCollapsedBounds.left;
                break;
        }
        calculateUsingTextSize(mExpandedTextSize);
        width = (mTextToDraw != null) ? mTextPaint.measureText(mTextToDraw, 0, mTextToDraw.length()) : 0;
        final int expandedAbsGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(mExpandedTextGravity, mIsRtl ? android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL : android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR);
        switch (expandedAbsGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) {
            case android.view.Gravity.BOTTOM :
                mExpandedDrawY = mExpandedBounds.bottom;
                break;
            case android.view.Gravity.TOP :
                mExpandedDrawY = mExpandedBounds.top - mTextPaint.ascent();
                break;
            case android.view.Gravity.CENTER_VERTICAL :
            default :
                float textHeight = mTextPaint.descent() - mTextPaint.ascent();
                float textOffset = (textHeight / 2) - mTextPaint.descent();
                mExpandedDrawY = mExpandedBounds.centerY() + textOffset;
                break;
        }
        switch (expandedAbsGravity & android.support.v4.view.GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
            case android.view.Gravity.CENTER_HORIZONTAL :
                mExpandedDrawX = mExpandedBounds.centerX() - (width / 2);
                break;
            case android.view.Gravity.RIGHT :
                mExpandedDrawX = mExpandedBounds.right - width;
                break;
            case android.view.Gravity.LEFT :
            default :
                mExpandedDrawX = mExpandedBounds.left;
                break;
        }
        // The bounds have changed so we need to clear the texture
        clearTexture();
        // Now reset the text size back to the original
        setInterpolatedTextSize(currentTextSize);
    }

    private void interpolateBounds(float fraction) {
        mCurrentBounds.left = android.support.design.widget.CollapsingTextHelper.lerp(mExpandedBounds.left, mCollapsedBounds.left, fraction, mPositionInterpolator);
        mCurrentBounds.top = android.support.design.widget.CollapsingTextHelper.lerp(mExpandedDrawY, mCollapsedDrawY, fraction, mPositionInterpolator);
        mCurrentBounds.right = android.support.design.widget.CollapsingTextHelper.lerp(mExpandedBounds.right, mCollapsedBounds.right, fraction, mPositionInterpolator);
        mCurrentBounds.bottom = android.support.design.widget.CollapsingTextHelper.lerp(mExpandedBounds.bottom, mCollapsedBounds.bottom, fraction, mPositionInterpolator);
    }

    public void draw(android.graphics.Canvas canvas) {
        final int saveCount = canvas.save();
        if ((mTextToDraw != null) && mDrawTitle) {
            float x = mCurrentDrawX;
            float y = mCurrentDrawY;
            final boolean drawTexture = mUseTexture && (mExpandedTitleTexture != null);
            final float ascent;
            final float descent;
            if (drawTexture) {
                ascent = mTextureAscent * mScale;
                descent = mTextureDescent * mScale;
            } else {
                ascent = mTextPaint.ascent() * mScale;
                descent = mTextPaint.descent() * mScale;
            }
            if (android.support.design.widget.CollapsingTextHelper.DEBUG_DRAW) {
                // Just a debug tool, which drawn a magenta rect in the text bounds
                canvas.drawRect(mCurrentBounds.left, y + ascent, mCurrentBounds.right, y + descent, android.support.design.widget.CollapsingTextHelper.DEBUG_DRAW_PAINT);
            }
            if (drawTexture) {
                y += ascent;
            }
            if (mScale != 1.0F) {
                canvas.scale(mScale, mScale, x, y);
            }
            if (drawTexture) {
                // If we should use a texture, draw it instead of text
                canvas.drawBitmap(mExpandedTitleTexture, x, y, mTexturePaint);
            } else {
                canvas.drawText(mTextToDraw, 0, mTextToDraw.length(), x, y, mTextPaint);
            }
        }
        canvas.restoreToCount(saveCount);
    }

    private boolean calculateIsRtl(java.lang.CharSequence text) {
        final boolean defaultIsRtl = android.support.v4.view.ViewCompat.getLayoutDirection(mView) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
        return (defaultIsRtl ? android.support.v4.text.TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL : android.support.v4.text.TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR).isRtl(text, 0, text.length());
    }

    private void setInterpolatedTextSize(float textSize) {
        calculateUsingTextSize(textSize);
        // Use our texture if the scale isn't 1.0
        mUseTexture = android.support.design.widget.CollapsingTextHelper.USE_SCALING_TEXTURE && (mScale != 1.0F);
        if (mUseTexture) {
            // Make sure we have an expanded texture if needed
            ensureExpandedTexture();
        }
        android.support.v4.view.ViewCompat.postInvalidateOnAnimation(mView);
    }

    private void calculateUsingTextSize(final float textSize) {
        if (mText == null)
            return;

        final float collapsedWidth = mCollapsedBounds.width();
        final float expandedWidth = mExpandedBounds.width();
        final float availableWidth;
        final float newTextSize;
        boolean updateDrawText = false;
        if (android.support.design.widget.CollapsingTextHelper.isClose(textSize, mCollapsedTextSize)) {
            newTextSize = mCollapsedTextSize;
            mScale = 1.0F;
            if (mCurrentTypeface != mCollapsedTypeface) {
                mCurrentTypeface = mCollapsedTypeface;
                updateDrawText = true;
            }
            availableWidth = collapsedWidth;
        } else {
            newTextSize = mExpandedTextSize;
            if (mCurrentTypeface != mExpandedTypeface) {
                mCurrentTypeface = mExpandedTypeface;
                updateDrawText = true;
            }
            if (android.support.design.widget.CollapsingTextHelper.isClose(textSize, mExpandedTextSize)) {
                // If we're close to the expanded text size, snap to it and use a scale of 1
                mScale = 1.0F;
            } else {
                // Else, we'll scale down from the expanded text size
                mScale = textSize / mExpandedTextSize;
            }
            final float textSizeRatio = mCollapsedTextSize / mExpandedTextSize;
            // This is the size of the expanded bounds when it is scaled to match the
            // collapsed text size
            final float scaledDownWidth = expandedWidth * textSizeRatio;
            if (scaledDownWidth > collapsedWidth) {
                // If the scaled down size is larger than the actual collapsed width, we need to
                // cap the available width so that when the expanded text scales down, it matches
                // the collapsed width
                availableWidth = java.lang.Math.min(collapsedWidth / textSizeRatio, expandedWidth);
            } else {
                // Otherwise we'll just use the expanded width
                availableWidth = expandedWidth;
            }
        }
        if (availableWidth > 0) {
            updateDrawText = ((mCurrentTextSize != newTextSize) || mBoundsChanged) || updateDrawText;
            mCurrentTextSize = newTextSize;
            mBoundsChanged = false;
        }
        if ((mTextToDraw == null) || updateDrawText) {
            mTextPaint.setTextSize(mCurrentTextSize);
            mTextPaint.setTypeface(mCurrentTypeface);
            // Use linear text scaling if we're scaling the canvas
            mTextPaint.setLinearText(mScale != 1.0F);
            // If we don't currently have text to draw, or the text size has changed, ellipsize...
            final java.lang.CharSequence title = android.text.TextUtils.ellipsize(mText, mTextPaint, availableWidth, android.text.TextUtils.TruncateAt.END);
            if (!android.text.TextUtils.equals(title, mTextToDraw)) {
                mTextToDraw = title;
                mIsRtl = calculateIsRtl(mTextToDraw);
            }
        }
    }

    private void ensureExpandedTexture() {
        if (((mExpandedTitleTexture != null) || mExpandedBounds.isEmpty()) || android.text.TextUtils.isEmpty(mTextToDraw)) {
            return;
        }
        calculateOffsets(0.0F);
        mTextureAscent = mTextPaint.ascent();
        mTextureDescent = mTextPaint.descent();
        final int w = java.lang.Math.round(mTextPaint.measureText(mTextToDraw, 0, mTextToDraw.length()));
        final int h = java.lang.Math.round(mTextureDescent - mTextureAscent);
        if ((w <= 0) || (h <= 0)) {
            return;// If the width or height are 0, return

        }
        mExpandedTitleTexture = android.graphics.Bitmap.createBitmap(w, h, android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Canvas c = new android.graphics.Canvas(mExpandedTitleTexture);
        c.drawText(mTextToDraw, 0, mTextToDraw.length(), 0, h - mTextPaint.descent(), mTextPaint);
        if (mTexturePaint == null) {
            // Make sure we have a paint
            mTexturePaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.FILTER_BITMAP_FLAG);
        }
    }

    public void recalculate() {
        if ((mView.getHeight() > 0) && (mView.getWidth() > 0)) {
            // If we've already been laid out, calculate everything now otherwise we'll wait
            // until a layout
            calculateBaseOffsets();
            calculateCurrentOffsets();
        }
    }

    /**
     * Set the title to display
     *
     * @param text
     * 		
     */
    void setText(java.lang.CharSequence text) {
        if ((text == null) || (!text.equals(mText))) {
            mText = text;
            mTextToDraw = null;
            clearTexture();
            recalculate();
        }
    }

    java.lang.CharSequence getText() {
        return mText;
    }

    private void clearTexture() {
        if (mExpandedTitleTexture != null) {
            mExpandedTitleTexture.recycle();
            mExpandedTitleTexture = null;
        }
    }

    /**
     * Returns true if {@code value} is 'close' to it's closest decimal value. Close is currently
     * defined as it's difference being < 0.001.
     */
    private static boolean isClose(float value, float targetValue) {
        return java.lang.Math.abs(value - targetValue) < 0.001F;
    }

    android.content.res.ColorStateList getExpandedTextColor() {
        return mExpandedTextColor;
    }

    android.content.res.ColorStateList getCollapsedTextColor() {
        return mCollapsedTextColor;
    }

    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio
     * 		of which to blend. 0.0 will return {@code color1}, 0.5 will give an even blend,
     * 		1.0 will return {@code color2}.
     */
    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRatio = 1.0F - ratio;
        float a = (android.graphics.Color.alpha(color1) * inverseRatio) + (android.graphics.Color.alpha(color2) * ratio);
        float r = (android.graphics.Color.red(color1) * inverseRatio) + (android.graphics.Color.red(color2) * ratio);
        float g = (android.graphics.Color.green(color1) * inverseRatio) + (android.graphics.Color.green(color2) * ratio);
        float b = (android.graphics.Color.blue(color1) * inverseRatio) + (android.graphics.Color.blue(color2) * ratio);
        return android.graphics.Color.argb(((int) (a)), ((int) (r)), ((int) (g)), ((int) (b)));
    }

    private static float lerp(float startValue, float endValue, float fraction, android.view.animation.Interpolator interpolator) {
        if (interpolator != null) {
            fraction = interpolator.getInterpolation(fraction);
        }
        return android.support.design.widget.AnimationUtils.lerp(startValue, endValue, fraction);
    }

    private static boolean rectEquals(android.graphics.Rect r, int left, int top, int right, int bottom) {
        return !((((r.left != left) || (r.top != top)) || (r.right != right)) || (r.bottom != bottom));
    }
}

