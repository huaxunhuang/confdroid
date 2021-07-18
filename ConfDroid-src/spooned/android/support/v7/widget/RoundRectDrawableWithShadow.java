/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v7.widget;


/**
 * A rounded rectangle drawable which also includes a shadow around.
 */
class RoundRectDrawableWithShadow extends android.graphics.drawable.Drawable {
    // used to calculate content padding
    static final double COS_45 = java.lang.Math.cos(java.lang.Math.toRadians(45));

    static final float SHADOW_MULTIPLIER = 1.5F;

    final int mInsetShadow;// extra shadow to avoid gaps between card and shadow


    /* This helper is set by CardView implementations.
    <p>
    Prior to API 17, canvas.drawRoundRect is expensive; which is why we need this interface
    to draw efficient rounded rectangles before 17.
     */
    static android.support.v7.widget.RoundRectDrawableWithShadow.RoundRectHelper sRoundRectHelper;

    android.graphics.Paint mPaint;

    android.graphics.Paint mCornerShadowPaint;

    android.graphics.Paint mEdgeShadowPaint;

    final android.graphics.RectF mCardBounds;

    float mCornerRadius;

    android.graphics.Path mCornerShadowPath;

    // updated value with inset
    float mMaxShadowSize;

    // actual value set by developer
    float mRawMaxShadowSize;

    // multiplied value to account for shadow offset
    float mShadowSize;

    // actual value set by developer
    float mRawShadowSize;

    private android.content.res.ColorStateList mBackground;

    private boolean mDirty = true;

    private final int mShadowStartColor;

    private final int mShadowEndColor;

    private boolean mAddPaddingForCorners = true;

    /**
     * If shadow size is set to a value above max shadow, we print a warning
     */
    private boolean mPrintedShadowClipWarning = false;

    RoundRectDrawableWithShadow(android.content.res.Resources resources, android.content.res.ColorStateList backgroundColor, float radius, float shadowSize, float maxShadowSize) {
        mShadowStartColor = resources.getColor(R.color.cardview_shadow_start_color);
        mShadowEndColor = resources.getColor(R.color.cardview_shadow_end_color);
        mInsetShadow = resources.getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
        mPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.DITHER_FLAG);
        setBackground(backgroundColor);
        mCornerShadowPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.DITHER_FLAG);
        mCornerShadowPaint.setStyle(android.graphics.Paint.Style.FILL);
        mCornerRadius = ((int) (radius + 0.5F));
        mCardBounds = new android.graphics.RectF();
        mEdgeShadowPaint = new android.graphics.Paint(mCornerShadowPaint);
        mEdgeShadowPaint.setAntiAlias(false);
        setShadowSize(shadowSize, maxShadowSize);
    }

    private void setBackground(android.content.res.ColorStateList color) {
        mBackground = (color == null) ? android.content.res.ColorStateList.valueOf(android.graphics.Color.TRANSPARENT) : color;
        mPaint.setColor(mBackground.getColorForState(getState(), mBackground.getDefaultColor()));
    }

    /**
     * Casts the value to an even integer.
     */
    private int toEven(float value) {
        int i = ((int) (value + 0.5F));
        if ((i % 2) == 1) {
            return i - 1;
        }
        return i;
    }

    public void setAddPaddingForCorners(boolean addPaddingForCorners) {
        mAddPaddingForCorners = addPaddingForCorners;
        invalidateSelf();
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        mCornerShadowPaint.setAlpha(alpha);
        mEdgeShadowPaint.setAlpha(alpha);
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        super.onBoundsChange(bounds);
        mDirty = true;
    }

    void setShadowSize(float shadowSize, float maxShadowSize) {
        if (shadowSize < 0.0F) {
            throw new java.lang.IllegalArgumentException(("Invalid shadow size " + shadowSize) + ". Must be >= 0");
        }
        if (maxShadowSize < 0.0F) {
            throw new java.lang.IllegalArgumentException(("Invalid max shadow size " + maxShadowSize) + ". Must be >= 0");
        }
        shadowSize = toEven(shadowSize);
        maxShadowSize = toEven(maxShadowSize);
        if (shadowSize > maxShadowSize) {
            shadowSize = maxShadowSize;
            if (!mPrintedShadowClipWarning) {
                mPrintedShadowClipWarning = true;
            }
        }
        if ((mRawShadowSize == shadowSize) && (mRawMaxShadowSize == maxShadowSize)) {
            return;
        }
        mRawShadowSize = shadowSize;
        mRawMaxShadowSize = maxShadowSize;
        mShadowSize = ((int) (((shadowSize * android.support.v7.widget.RoundRectDrawableWithShadow.SHADOW_MULTIPLIER) + mInsetShadow) + 0.5F));
        mMaxShadowSize = maxShadowSize + mInsetShadow;
        mDirty = true;
        invalidateSelf();
    }

    @java.lang.Override
    public boolean getPadding(android.graphics.Rect padding) {
        int vOffset = ((int) (java.lang.Math.ceil(android.support.v7.widget.RoundRectDrawableWithShadow.calculateVerticalPadding(mRawMaxShadowSize, mCornerRadius, mAddPaddingForCorners))));
        int hOffset = ((int) (java.lang.Math.ceil(android.support.v7.widget.RoundRectDrawableWithShadow.calculateHorizontalPadding(mRawMaxShadowSize, mCornerRadius, mAddPaddingForCorners))));
        padding.set(hOffset, vOffset, hOffset, vOffset);
        return true;
    }

    static float calculateVerticalPadding(float maxShadowSize, float cornerRadius, boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return ((float) ((maxShadowSize * android.support.v7.widget.RoundRectDrawableWithShadow.SHADOW_MULTIPLIER) + ((1 - android.support.v7.widget.RoundRectDrawableWithShadow.COS_45) * cornerRadius)));
        } else {
            return maxShadowSize * android.support.v7.widget.RoundRectDrawableWithShadow.SHADOW_MULTIPLIER;
        }
    }

    static float calculateHorizontalPadding(float maxShadowSize, float cornerRadius, boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return ((float) (maxShadowSize + ((1 - android.support.v7.widget.RoundRectDrawableWithShadow.COS_45) * cornerRadius)));
        } else {
            return maxShadowSize;
        }
    }

    @java.lang.Override
    protected boolean onStateChange(int[] stateSet) {
        final int newColor = mBackground.getColorForState(stateSet, mBackground.getDefaultColor());
        if (mPaint.getColor() == newColor) {
            return false;
        }
        mPaint.setColor(newColor);
        mDirty = true;
        invalidateSelf();
        return true;
    }

    @java.lang.Override
    public boolean isStateful() {
        return ((mBackground != null) && mBackground.isStateful()) || super.isStateful();
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @java.lang.Override
    public int getOpacity() {
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    void setCornerRadius(float radius) {
        if (radius < 0.0F) {
            throw new java.lang.IllegalArgumentException(("Invalid radius " + radius) + ". Must be >= 0");
        }
        radius = ((int) (radius + 0.5F));
        if (mCornerRadius == radius) {
            return;
        }
        mCornerRadius = radius;
        mDirty = true;
        invalidateSelf();
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        if (mDirty) {
            buildComponents(getBounds());
            mDirty = false;
        }
        canvas.translate(0, mRawShadowSize / 2);
        drawShadow(canvas);
        canvas.translate(0, (-mRawShadowSize) / 2);
        android.support.v7.widget.RoundRectDrawableWithShadow.sRoundRectHelper.drawRoundRect(canvas, mCardBounds, mCornerRadius, mPaint);
    }

    private void drawShadow(android.graphics.Canvas canvas) {
        final float edgeShadowTop = (-mCornerRadius) - mShadowSize;
        final float inset = (mCornerRadius + mInsetShadow) + (mRawShadowSize / 2);
        final boolean drawHorizontalEdges = (mCardBounds.width() - (2 * inset)) > 0;
        final boolean drawVerticalEdges = (mCardBounds.height() - (2 * inset)) > 0;
        // LT
        int saved = canvas.save();
        canvas.translate(mCardBounds.left + inset, mCardBounds.top + inset);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawHorizontalEdges) {
            canvas.drawRect(0, edgeShadowTop, mCardBounds.width() - (2 * inset), -mCornerRadius, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // RB
        saved = canvas.save();
        canvas.translate(mCardBounds.right - inset, mCardBounds.bottom - inset);
        canvas.rotate(180.0F);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawHorizontalEdges) {
            canvas.drawRect(0, edgeShadowTop, mCardBounds.width() - (2 * inset), (-mCornerRadius) + mShadowSize, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // LB
        saved = canvas.save();
        canvas.translate(mCardBounds.left + inset, mCardBounds.bottom - inset);
        canvas.rotate(270.0F);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawVerticalEdges) {
            canvas.drawRect(0, edgeShadowTop, mCardBounds.height() - (2 * inset), -mCornerRadius, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // RT
        saved = canvas.save();
        canvas.translate(mCardBounds.right - inset, mCardBounds.top + inset);
        canvas.rotate(90.0F);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawVerticalEdges) {
            canvas.drawRect(0, edgeShadowTop, mCardBounds.height() - (2 * inset), -mCornerRadius, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
    }

    private void buildShadowCorners() {
        android.graphics.RectF innerBounds = new android.graphics.RectF(-mCornerRadius, -mCornerRadius, mCornerRadius, mCornerRadius);
        android.graphics.RectF outerBounds = new android.graphics.RectF(innerBounds);
        outerBounds.inset(-mShadowSize, -mShadowSize);
        if (mCornerShadowPath == null) {
            mCornerShadowPath = new android.graphics.Path();
        } else {
            mCornerShadowPath.reset();
        }
        mCornerShadowPath.setFillType(android.graphics.Path.FillType.EVEN_ODD);
        mCornerShadowPath.moveTo(-mCornerRadius, 0);
        mCornerShadowPath.rLineTo(-mShadowSize, 0);
        // outer arc
        mCornerShadowPath.arcTo(outerBounds, 180.0F, 90.0F, false);
        // inner arc
        mCornerShadowPath.arcTo(innerBounds, 270.0F, -90.0F, false);
        mCornerShadowPath.close();
        float startRatio = mCornerRadius / (mCornerRadius + mShadowSize);
        mCornerShadowPaint.setShader(new android.graphics.RadialGradient(0, 0, mCornerRadius + mShadowSize, new int[]{ mShadowStartColor, mShadowStartColor, mShadowEndColor }, new float[]{ 0.0F, startRatio, 1.0F }, android.graphics.Shader.TileMode.CLAMP));
        // we offset the content shadowSize/2 pixels up to make it more realistic.
        // this is why edge shadow shader has some extra space
        // When drawing bottom edge shadow, we use that extra space.
        mEdgeShadowPaint.setShader(new android.graphics.LinearGradient(0, (-mCornerRadius) + mShadowSize, 0, (-mCornerRadius) - mShadowSize, new int[]{ mShadowStartColor, mShadowStartColor, mShadowEndColor }, new float[]{ 0.0F, 0.5F, 1.0F }, android.graphics.Shader.TileMode.CLAMP));
        mEdgeShadowPaint.setAntiAlias(false);
    }

    private void buildComponents(android.graphics.Rect bounds) {
        // Card is offset SHADOW_MULTIPLIER * maxShadowSize to account for the shadow shift.
        // We could have different top-bottom offsets to avoid extra gap above but in that case
        // center aligning Views inside the CardView would be problematic.
        final float verticalOffset = mRawMaxShadowSize * android.support.v7.widget.RoundRectDrawableWithShadow.SHADOW_MULTIPLIER;
        mCardBounds.set(bounds.left + mRawMaxShadowSize, bounds.top + verticalOffset, bounds.right - mRawMaxShadowSize, bounds.bottom - verticalOffset);
        buildShadowCorners();
    }

    float getCornerRadius() {
        return mCornerRadius;
    }

    void getMaxShadowAndCornerPadding(android.graphics.Rect into) {
        getPadding(into);
    }

    void setShadowSize(float size) {
        setShadowSize(size, mRawMaxShadowSize);
    }

    void setMaxShadowSize(float size) {
        setShadowSize(mRawShadowSize, size);
    }

    float getShadowSize() {
        return mRawShadowSize;
    }

    float getMaxShadowSize() {
        return mRawMaxShadowSize;
    }

    float getMinWidth() {
        final float content = 2 * java.lang.Math.max(mRawMaxShadowSize, (mCornerRadius + mInsetShadow) + (mRawMaxShadowSize / 2));
        return content + ((mRawMaxShadowSize + mInsetShadow) * 2);
    }

    float getMinHeight() {
        final float content = 2 * java.lang.Math.max(mRawMaxShadowSize, (mCornerRadius + mInsetShadow) + ((mRawMaxShadowSize * android.support.v7.widget.RoundRectDrawableWithShadow.SHADOW_MULTIPLIER) / 2));
        return content + (((mRawMaxShadowSize * android.support.v7.widget.RoundRectDrawableWithShadow.SHADOW_MULTIPLIER) + mInsetShadow) * 2);
    }

    void setColor(@android.support.annotation.Nullable
    android.content.res.ColorStateList color) {
        setBackground(color);
        invalidateSelf();
    }

    android.content.res.ColorStateList getColor() {
        return mBackground;
    }

    static interface RoundRectHelper {
        void drawRoundRect(android.graphics.Canvas canvas, android.graphics.RectF bounds, float cornerRadius, android.graphics.Paint paint);
    }
}

