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
package android.support.design.widget;


/**
 * A {@link android.graphics.drawable.Drawable} which wraps another drawable and
 * draws a shadow around it.
 */
class ShadowDrawableWrapper extends android.support.v7.graphics.drawable.DrawableWrapper {
    // used to calculate content padding
    static final double COS_45 = java.lang.Math.cos(java.lang.Math.toRadians(45));

    static final float SHADOW_MULTIPLIER = 1.5F;

    static final float SHADOW_TOP_SCALE = 0.25F;

    static final float SHADOW_HORIZ_SCALE = 0.5F;

    static final float SHADOW_BOTTOM_SCALE = 1.0F;

    final android.graphics.Paint mCornerShadowPaint;

    final android.graphics.Paint mEdgeShadowPaint;

    final android.graphics.RectF mContentBounds;

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

    private boolean mDirty = true;

    private final int mShadowStartColor;

    private final int mShadowMiddleColor;

    private final int mShadowEndColor;

    private boolean mAddPaddingForCorners = true;

    private float mRotation;

    /**
     * If shadow size is set to a value above max shadow, we print a warning
     */
    private boolean mPrintedShadowClipWarning = false;

    public ShadowDrawableWrapper(android.content.Context context, android.graphics.drawable.Drawable content, float radius, float shadowSize, float maxShadowSize) {
        super(content);
        mShadowStartColor = android.support.v4.content.ContextCompat.getColor(context, R.color.design_fab_shadow_start_color);
        mShadowMiddleColor = android.support.v4.content.ContextCompat.getColor(context, R.color.design_fab_shadow_mid_color);
        mShadowEndColor = android.support.v4.content.ContextCompat.getColor(context, R.color.design_fab_shadow_end_color);
        mCornerShadowPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.DITHER_FLAG);
        mCornerShadowPaint.setStyle(android.graphics.Paint.Style.FILL);
        mCornerRadius = java.lang.Math.round(radius);
        mContentBounds = new android.graphics.RectF();
        mEdgeShadowPaint = new android.graphics.Paint(mCornerShadowPaint);
        mEdgeShadowPaint.setAntiAlias(false);
        setShadowSize(shadowSize, maxShadowSize);
    }

    /**
     * Casts the value to an even integer.
     */
    private static int toEven(float value) {
        int i = java.lang.Math.round(value);
        return (i % 2) == 1 ? i - 1 : i;
    }

    public void setAddPaddingForCorners(boolean addPaddingForCorners) {
        mAddPaddingForCorners = addPaddingForCorners;
        invalidateSelf();
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        super.setAlpha(alpha);
        mCornerShadowPaint.setAlpha(alpha);
        mEdgeShadowPaint.setAlpha(alpha);
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        mDirty = true;
    }

    void setShadowSize(float shadowSize, float maxShadowSize) {
        if ((shadowSize < 0) || (maxShadowSize < 0)) {
            throw new java.lang.IllegalArgumentException("invalid shadow size");
        }
        shadowSize = android.support.design.widget.ShadowDrawableWrapper.toEven(shadowSize);
        maxShadowSize = android.support.design.widget.ShadowDrawableWrapper.toEven(maxShadowSize);
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
        mShadowSize = java.lang.Math.round(shadowSize * android.support.design.widget.ShadowDrawableWrapper.SHADOW_MULTIPLIER);
        mMaxShadowSize = maxShadowSize;
        mDirty = true;
        invalidateSelf();
    }

    @java.lang.Override
    public boolean getPadding(android.graphics.Rect padding) {
        int vOffset = ((int) (java.lang.Math.ceil(android.support.design.widget.ShadowDrawableWrapper.calculateVerticalPadding(mRawMaxShadowSize, mCornerRadius, mAddPaddingForCorners))));
        int hOffset = ((int) (java.lang.Math.ceil(android.support.design.widget.ShadowDrawableWrapper.calculateHorizontalPadding(mRawMaxShadowSize, mCornerRadius, mAddPaddingForCorners))));
        padding.set(hOffset, vOffset, hOffset, vOffset);
        return true;
    }

    public static float calculateVerticalPadding(float maxShadowSize, float cornerRadius, boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return ((float) ((maxShadowSize * android.support.design.widget.ShadowDrawableWrapper.SHADOW_MULTIPLIER) + ((1 - android.support.design.widget.ShadowDrawableWrapper.COS_45) * cornerRadius)));
        } else {
            return maxShadowSize * android.support.design.widget.ShadowDrawableWrapper.SHADOW_MULTIPLIER;
        }
    }

    public static float calculateHorizontalPadding(float maxShadowSize, float cornerRadius, boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return ((float) (maxShadowSize + ((1 - android.support.design.widget.ShadowDrawableWrapper.COS_45) * cornerRadius)));
        } else {
            return maxShadowSize;
        }
    }

    @java.lang.Override
    public int getOpacity() {
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    public void setCornerRadius(float radius) {
        radius = java.lang.Math.round(radius);
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
        drawShadow(canvas);
        super.draw(canvas);
    }

    final void setRotation(float rotation) {
        if (mRotation != rotation) {
            mRotation = rotation;
            invalidateSelf();
        }
    }

    private void drawShadow(android.graphics.Canvas canvas) {
        final int rotateSaved = canvas.save();
        canvas.rotate(mRotation, mContentBounds.centerX(), mContentBounds.centerY());
        final float edgeShadowTop = (-mCornerRadius) - mShadowSize;
        final float shadowOffset = mCornerRadius;
        final boolean drawHorizontalEdges = (mContentBounds.width() - (2 * shadowOffset)) > 0;
        final boolean drawVerticalEdges = (mContentBounds.height() - (2 * shadowOffset)) > 0;
        final float shadowOffsetTop = mRawShadowSize - (mRawShadowSize * android.support.design.widget.ShadowDrawableWrapper.SHADOW_TOP_SCALE);
        final float shadowOffsetHorizontal = mRawShadowSize - (mRawShadowSize * android.support.design.widget.ShadowDrawableWrapper.SHADOW_HORIZ_SCALE);
        final float shadowOffsetBottom = mRawShadowSize - (mRawShadowSize * android.support.design.widget.ShadowDrawableWrapper.SHADOW_BOTTOM_SCALE);
        final float shadowScaleHorizontal = shadowOffset / (shadowOffset + shadowOffsetHorizontal);
        final float shadowScaleTop = shadowOffset / (shadowOffset + shadowOffsetTop);
        final float shadowScaleBottom = shadowOffset / (shadowOffset + shadowOffsetBottom);
        // LT
        int saved = canvas.save();
        canvas.translate(mContentBounds.left + shadowOffset, mContentBounds.top + shadowOffset);
        canvas.scale(shadowScaleHorizontal, shadowScaleTop);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawHorizontalEdges) {
            // TE
            canvas.scale(1.0F / shadowScaleHorizontal, 1.0F);
            canvas.drawRect(0, edgeShadowTop, mContentBounds.width() - (2 * shadowOffset), -mCornerRadius, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // RB
        saved = canvas.save();
        canvas.translate(mContentBounds.right - shadowOffset, mContentBounds.bottom - shadowOffset);
        canvas.scale(shadowScaleHorizontal, shadowScaleBottom);
        canvas.rotate(180.0F);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawHorizontalEdges) {
            // BE
            canvas.scale(1.0F / shadowScaleHorizontal, 1.0F);
            canvas.drawRect(0, edgeShadowTop, mContentBounds.width() - (2 * shadowOffset), (-mCornerRadius) + mShadowSize, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // LB
        saved = canvas.save();
        canvas.translate(mContentBounds.left + shadowOffset, mContentBounds.bottom - shadowOffset);
        canvas.scale(shadowScaleHorizontal, shadowScaleBottom);
        canvas.rotate(270.0F);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawVerticalEdges) {
            // LE
            canvas.scale(1.0F / shadowScaleBottom, 1.0F);
            canvas.drawRect(0, edgeShadowTop, mContentBounds.height() - (2 * shadowOffset), -mCornerRadius, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // RT
        saved = canvas.save();
        canvas.translate(mContentBounds.right - shadowOffset, mContentBounds.top + shadowOffset);
        canvas.scale(shadowScaleHorizontal, shadowScaleTop);
        canvas.rotate(90.0F);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawVerticalEdges) {
            // RE
            canvas.scale(1.0F / shadowScaleTop, 1.0F);
            canvas.drawRect(0, edgeShadowTop, mContentBounds.height() - (2 * shadowOffset), -mCornerRadius, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        canvas.restoreToCount(rotateSaved);
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
        float shadowRadius = -outerBounds.top;
        if (shadowRadius > 0.0F) {
            float startRatio = mCornerRadius / shadowRadius;
            float midRatio = startRatio + ((1.0F - startRatio) / 2.0F);
            mCornerShadowPaint.setShader(new android.graphics.RadialGradient(0, 0, shadowRadius, new int[]{ 0, mShadowStartColor, mShadowMiddleColor, mShadowEndColor }, new float[]{ 0.0F, startRatio, midRatio, 1.0F }, android.graphics.Shader.TileMode.CLAMP));
        }
        // we offset the content shadowSize/2 pixels up to make it more realistic.
        // this is why edge shadow shader has some extra space
        // When drawing bottom edge shadow, we use that extra space.
        mEdgeShadowPaint.setShader(new android.graphics.LinearGradient(0, innerBounds.top, 0, outerBounds.top, new int[]{ mShadowStartColor, mShadowMiddleColor, mShadowEndColor }, new float[]{ 0.0F, 0.5F, 1.0F }, android.graphics.Shader.TileMode.CLAMP));
        mEdgeShadowPaint.setAntiAlias(false);
    }

    private void buildComponents(android.graphics.Rect bounds) {
        // Card is offset SHADOW_MULTIPLIER * maxShadowSize to account for the shadow shift.
        // We could have different top-bottom offsets to avoid extra gap above but in that case
        // center aligning Views inside the CardView would be problematic.
        final float verticalOffset = mRawMaxShadowSize * android.support.design.widget.ShadowDrawableWrapper.SHADOW_MULTIPLIER;
        mContentBounds.set(bounds.left + mRawMaxShadowSize, bounds.top + verticalOffset, bounds.right - mRawMaxShadowSize, bounds.bottom - verticalOffset);
        getWrappedDrawable().setBounds(((int) (mContentBounds.left)), ((int) (mContentBounds.top)), ((int) (mContentBounds.right)), ((int) (mContentBounds.bottom)));
        buildShadowCorners();
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public void setShadowSize(float size) {
        setShadowSize(size, mRawMaxShadowSize);
    }

    public void setMaxShadowSize(float size) {
        setShadowSize(mRawShadowSize, size);
    }

    public float getShadowSize() {
        return mRawShadowSize;
    }

    public float getMaxShadowSize() {
        return mRawMaxShadowSize;
    }

    public float getMinWidth() {
        final float content = 2 * java.lang.Math.max(mRawMaxShadowSize, mCornerRadius + (mRawMaxShadowSize / 2));
        return content + (mRawMaxShadowSize * 2);
    }

    public float getMinHeight() {
        final float content = 2 * java.lang.Math.max(mRawMaxShadowSize, mCornerRadius + ((mRawMaxShadowSize * android.support.design.widget.ShadowDrawableWrapper.SHADOW_MULTIPLIER) / 2));
        return content + ((mRawMaxShadowSize * android.support.design.widget.ShadowDrawableWrapper.SHADOW_MULTIPLIER) * 2);
    }
}

