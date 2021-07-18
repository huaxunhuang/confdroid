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
 * Very simple drawable that draws a rounded rectangle background with arbitrary corners and also
 * reports proper outline for Lollipop.
 * <p>
 * Simpler and uses less resources compared to GradientDrawable or ShapeDrawable.
 */
class RoundRectDrawable extends android.graphics.drawable.Drawable {
    private float mRadius;

    private final android.graphics.Paint mPaint;

    private final android.graphics.RectF mBoundsF;

    private final android.graphics.Rect mBoundsI;

    private float mPadding;

    private boolean mInsetForPadding = false;

    private boolean mInsetForRadius = true;

    private android.content.res.ColorStateList mBackground;

    private android.graphics.PorterDuffColorFilter mTintFilter;

    private android.content.res.ColorStateList mTint;

    private android.graphics.PorterDuff.Mode mTintMode = android.graphics.PorterDuff.Mode.SRC_IN;

    public RoundRectDrawable(android.content.res.ColorStateList backgroundColor, float radius) {
        mRadius = radius;
        mPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.DITHER_FLAG);
        setBackground(backgroundColor);
        mBoundsF = new android.graphics.RectF();
        mBoundsI = new android.graphics.Rect();
    }

    private void setBackground(android.content.res.ColorStateList color) {
        mBackground = (color == null) ? android.content.res.ColorStateList.valueOf(android.graphics.Color.TRANSPARENT) : color;
        mPaint.setColor(mBackground.getColorForState(getState(), mBackground.getDefaultColor()));
    }

    void setPadding(float padding, boolean insetForPadding, boolean insetForRadius) {
        if (((padding == mPadding) && (mInsetForPadding == insetForPadding)) && (mInsetForRadius == insetForRadius)) {
            return;
        }
        mPadding = padding;
        mInsetForPadding = insetForPadding;
        mInsetForRadius = insetForRadius;
        updateBounds(null);
        invalidateSelf();
    }

    float getPadding() {
        return mPadding;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        final android.graphics.Paint paint = mPaint;
        final boolean clearColorFilter;
        if ((mTintFilter != null) && (paint.getColorFilter() == null)) {
            paint.setColorFilter(mTintFilter);
            clearColorFilter = true;
        } else {
            clearColorFilter = false;
        }
        canvas.drawRoundRect(mBoundsF, mRadius, mRadius, paint);
        if (clearColorFilter) {
            paint.setColorFilter(null);
        }
    }

    private void updateBounds(android.graphics.Rect bounds) {
        if (bounds == null) {
            bounds = getBounds();
        }
        mBoundsF.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
        mBoundsI.set(bounds);
        if (mInsetForPadding) {
            float vInset = android.support.v7.widget.RoundRectDrawableWithShadow.calculateVerticalPadding(mPadding, mRadius, mInsetForRadius);
            float hInset = android.support.v7.widget.RoundRectDrawableWithShadow.calculateHorizontalPadding(mPadding, mRadius, mInsetForRadius);
            mBoundsI.inset(((int) (java.lang.Math.ceil(hInset))), ((int) (java.lang.Math.ceil(vInset))));
            // to make sure they have same bounds.
            mBoundsF.set(mBoundsI);
        }
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        super.onBoundsChange(bounds);
        updateBounds(bounds);
    }

    @java.lang.Override
    public void getOutline(android.graphics.Outline outline) {
        outline.setRoundRect(mBoundsI, mRadius);
    }

    void setRadius(float radius) {
        if (radius == mRadius) {
            return;
        }
        mRadius = radius;
        updateBounds(null);
        invalidateSelf();
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @java.lang.Override
    public int getOpacity() {
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setColor(@android.support.annotation.Nullable
    android.content.res.ColorStateList color) {
        setBackground(color);
        invalidateSelf();
    }

    public android.content.res.ColorStateList getColor() {
        return mBackground;
    }

    @java.lang.Override
    public void setTintList(android.content.res.ColorStateList tint) {
        mTint = tint;
        mTintFilter = createTintFilter(mTint, mTintMode);
        invalidateSelf();
    }

    @java.lang.Override
    public void setTintMode(android.graphics.PorterDuff.Mode tintMode) {
        mTintMode = tintMode;
        mTintFilter = createTintFilter(mTint, mTintMode);
        invalidateSelf();
    }

    @java.lang.Override
    protected boolean onStateChange(int[] stateSet) {
        final int newColor = mBackground.getColorForState(stateSet, mBackground.getDefaultColor());
        final boolean colorChanged = newColor != mPaint.getColor();
        if (colorChanged) {
            mPaint.setColor(newColor);
        }
        if ((mTint != null) && (mTintMode != null)) {
            mTintFilter = createTintFilter(mTint, mTintMode);
            return true;
        }
        return colorChanged;
    }

    @java.lang.Override
    public boolean isStateful() {
        return (((mTint != null) && mTint.isStateful()) || ((mBackground != null) && mBackground.isStateful())) || super.isStateful();
    }

    /**
     * Ensures the tint filter is consistent with the current tint color and
     * mode.
     */
    private android.graphics.PorterDuffColorFilter createTintFilter(android.content.res.ColorStateList tint, android.graphics.PorterDuff.Mode tintMode) {
        if ((tint == null) || (tintMode == null)) {
            return null;
        }
        final int color = tint.getColorForState(getState(), android.graphics.Color.TRANSPARENT);
        return new android.graphics.PorterDuffColorFilter(color, tintMode);
    }
}

