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


/**
 * A drawable which draws an oval 'border'.
 */
class CircularBorderDrawable extends android.graphics.drawable.Drawable {
    /**
     * We actually draw the stroke wider than the border size given. This is to reduce any
     * potential transparent space caused by anti-aliasing and padding rounding.
     * This value defines the multiplier used to determine to draw stroke width.
     */
    private static final float DRAW_STROKE_WIDTH_MULTIPLE = 1.3333F;

    final android.graphics.Paint mPaint;

    final android.graphics.Rect mRect = new android.graphics.Rect();

    final android.graphics.RectF mRectF = new android.graphics.RectF();

    float mBorderWidth;

    private int mTopOuterStrokeColor;

    private int mTopInnerStrokeColor;

    private int mBottomOuterStrokeColor;

    private int mBottomInnerStrokeColor;

    private android.content.res.ColorStateList mBorderTint;

    private int mCurrentBorderTintColor;

    private boolean mInvalidateShader = true;

    private float mRotation;

    public CircularBorderDrawable() {
        mPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(android.graphics.Paint.Style.STROKE);
    }

    void setGradientColors(int topOuterStrokeColor, int topInnerStrokeColor, int bottomOuterStrokeColor, int bottomInnerStrokeColor) {
        mTopOuterStrokeColor = topOuterStrokeColor;
        mTopInnerStrokeColor = topInnerStrokeColor;
        mBottomOuterStrokeColor = bottomOuterStrokeColor;
        mBottomInnerStrokeColor = bottomInnerStrokeColor;
    }

    /**
     * Set the border width
     */
    void setBorderWidth(float width) {
        if (mBorderWidth != width) {
            mBorderWidth = width;
            mPaint.setStrokeWidth(width * android.support.design.widget.CircularBorderDrawable.DRAW_STROKE_WIDTH_MULTIPLE);
            mInvalidateShader = true;
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        if (mInvalidateShader) {
            mPaint.setShader(createGradientShader());
            mInvalidateShader = false;
        }
        final float halfBorderWidth = mPaint.getStrokeWidth() / 2.0F;
        final android.graphics.RectF rectF = mRectF;
        // We need to inset the oval bounds by half the border width. This is because stroke draws
        // the center of the border on the dimension. Whereas we want the stroke on the inside.
        copyBounds(mRect);
        rectF.set(mRect);
        rectF.left += halfBorderWidth;
        rectF.top += halfBorderWidth;
        rectF.right -= halfBorderWidth;
        rectF.bottom -= halfBorderWidth;
        canvas.save();
        canvas.rotate(mRotation, rectF.centerX(), rectF.centerY());
        // Draw the oval
        canvas.drawOval(rectF, mPaint);
        canvas.restore();
    }

    @java.lang.Override
    public boolean getPadding(android.graphics.Rect padding) {
        final int borderWidth = java.lang.Math.round(mBorderWidth);
        padding.set(borderWidth, borderWidth, borderWidth, borderWidth);
        return true;
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    void setBorderTint(android.content.res.ColorStateList tint) {
        if (tint != null) {
            mCurrentBorderTintColor = tint.getColorForState(getState(), mCurrentBorderTintColor);
        }
        mBorderTint = tint;
        mInvalidateShader = true;
        invalidateSelf();
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @java.lang.Override
    public int getOpacity() {
        return mBorderWidth > 0 ? android.graphics.PixelFormat.TRANSLUCENT : android.graphics.PixelFormat.TRANSPARENT;
    }

    final void setRotation(float rotation) {
        if (rotation != mRotation) {
            mRotation = rotation;
            invalidateSelf();
        }
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        mInvalidateShader = true;
    }

    @java.lang.Override
    public boolean isStateful() {
        return ((mBorderTint != null) && mBorderTint.isStateful()) || super.isStateful();
    }

    @java.lang.Override
    protected boolean onStateChange(int[] state) {
        if (mBorderTint != null) {
            final int newColor = mBorderTint.getColorForState(state, mCurrentBorderTintColor);
            if (newColor != mCurrentBorderTintColor) {
                mInvalidateShader = true;
                mCurrentBorderTintColor = newColor;
            }
        }
        if (mInvalidateShader) {
            invalidateSelf();
        }
        return mInvalidateShader;
    }

    /**
     * Creates a vertical {@link LinearGradient}
     *
     * @return 
     */
    private android.graphics.Shader createGradientShader() {
        final android.graphics.Rect rect = mRect;
        copyBounds(rect);
        final float borderRatio = mBorderWidth / rect.height();
        final int[] colors = new int[6];
        colors[0] = android.support.v4.graphics.ColorUtils.compositeColors(mTopOuterStrokeColor, mCurrentBorderTintColor);
        colors[1] = android.support.v4.graphics.ColorUtils.compositeColors(mTopInnerStrokeColor, mCurrentBorderTintColor);
        colors[2] = android.support.v4.graphics.ColorUtils.compositeColors(android.support.v4.graphics.ColorUtils.setAlphaComponent(mTopInnerStrokeColor, 0), mCurrentBorderTintColor);
        colors[3] = android.support.v4.graphics.ColorUtils.compositeColors(android.support.v4.graphics.ColorUtils.setAlphaComponent(mBottomInnerStrokeColor, 0), mCurrentBorderTintColor);
        colors[4] = android.support.v4.graphics.ColorUtils.compositeColors(mBottomInnerStrokeColor, mCurrentBorderTintColor);
        colors[5] = android.support.v4.graphics.ColorUtils.compositeColors(mBottomOuterStrokeColor, mCurrentBorderTintColor);
        final float[] positions = new float[6];
        positions[0] = 0.0F;
        positions[1] = borderRatio;
        positions[2] = 0.5F;
        positions[3] = 0.5F;
        positions[4] = 1.0F - borderRatio;
        positions[5] = 1.0F;
        return new android.graphics.LinearGradient(0, rect.top, 0, rect.bottom, colors, positions, android.graphics.Shader.TileMode.CLAMP);
    }
}

