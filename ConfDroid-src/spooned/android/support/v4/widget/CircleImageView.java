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
package android.support.v4.widget;


/**
 * Private class created to work around issues with AnimationListeners being
 * called before the animation is actually complete and support shadows on older
 * platforms.
 */
class CircleImageView extends android.widget.ImageView {
    private static final int KEY_SHADOW_COLOR = 0x1e000000;

    private static final int FILL_SHADOW_COLOR = 0x3d000000;

    // PX
    private static final float X_OFFSET = 0.0F;

    private static final float Y_OFFSET = 1.75F;

    private static final float SHADOW_RADIUS = 3.5F;

    private static final int SHADOW_ELEVATION = 4;

    private android.view.animation.Animation.AnimationListener mListener;

    int mShadowRadius;

    CircleImageView(android.content.Context context, int color) {
        super(context);
        final float density = getContext().getResources().getDisplayMetrics().density;
        final int shadowYOffset = ((int) (density * android.support.v4.widget.CircleImageView.Y_OFFSET));
        final int shadowXOffset = ((int) (density * android.support.v4.widget.CircleImageView.X_OFFSET));
        mShadowRadius = ((int) (density * android.support.v4.widget.CircleImageView.SHADOW_RADIUS));
        android.graphics.drawable.ShapeDrawable circle;
        if (elevationSupported()) {
            circle = new android.graphics.drawable.ShapeDrawable(new android.graphics.drawable.shapes.OvalShape());
            android.support.v4.view.ViewCompat.setElevation(this, android.support.v4.widget.CircleImageView.SHADOW_ELEVATION * density);
        } else {
            android.graphics.drawable.shapes.OvalShape oval = new android.support.v4.widget.CircleImageView.OvalShadow(mShadowRadius);
            circle = new android.graphics.drawable.ShapeDrawable(oval);
            android.support.v4.view.ViewCompat.setLayerType(this, android.support.v4.view.ViewCompat.LAYER_TYPE_SOFTWARE, circle.getPaint());
            circle.getPaint().setShadowLayer(mShadowRadius, shadowXOffset, shadowYOffset, android.support.v4.widget.CircleImageView.KEY_SHADOW_COLOR);
            final int padding = mShadowRadius;
            // set padding so the inner image sits correctly within the shadow.
            setPadding(padding, padding, padding, padding);
        }
        circle.getPaint().setColor(color);
        android.support.v4.view.ViewCompat.setBackground(this, circle);
    }

    private boolean elevationSupported() {
        return android.os.Build.VERSION.SDK_INT >= 21;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!elevationSupported()) {
            setMeasuredDimension(getMeasuredWidth() + (mShadowRadius * 2), getMeasuredHeight() + (mShadowRadius * 2));
        }
    }

    public void setAnimationListener(android.view.animation.Animation.AnimationListener listener) {
        mListener = listener;
    }

    @java.lang.Override
    public void onAnimationStart() {
        super.onAnimationStart();
        if (mListener != null) {
            mListener.onAnimationStart(getAnimation());
        }
    }

    @java.lang.Override
    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (mListener != null) {
            mListener.onAnimationEnd(getAnimation());
        }
    }

    /**
     * Update the background color of the circle image view.
     *
     * @param colorRes
     * 		Id of a color resource.
     */
    public void setBackgroundColorRes(int colorRes) {
        setBackgroundColor(android.support.v4.content.ContextCompat.getColor(getContext(), colorRes));
    }

    @java.lang.Override
    public void setBackgroundColor(int color) {
        if (getBackground() instanceof android.graphics.drawable.ShapeDrawable) {
            ((android.graphics.drawable.ShapeDrawable) (getBackground())).getPaint().setColor(color);
        }
    }

    private class OvalShadow extends android.graphics.drawable.shapes.OvalShape {
        private android.graphics.RadialGradient mRadialGradient;

        private android.graphics.Paint mShadowPaint;

        OvalShadow(int shadowRadius) {
            super();
            mShadowPaint = new android.graphics.Paint();
            mShadowRadius = shadowRadius;
            updateRadialGradient(((int) (rect().width())));
        }

        @java.lang.Override
        protected void onResize(float width, float height) {
            super.onResize(width, height);
            updateRadialGradient(((int) (width)));
        }

        @java.lang.Override
        public void draw(android.graphics.Canvas canvas, android.graphics.Paint paint) {
            final int viewWidth = android.support.v4.widget.CircleImageView.this.getWidth();
            final int viewHeight = android.support.v4.widget.CircleImageView.this.getHeight();
            canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2, mShadowPaint);
            canvas.drawCircle(viewWidth / 2, viewHeight / 2, (viewWidth / 2) - mShadowRadius, paint);
        }

        private void updateRadialGradient(int diameter) {
            mRadialGradient = new android.graphics.RadialGradient(diameter / 2, diameter / 2, mShadowRadius, new int[]{ android.support.v4.widget.CircleImageView.FILL_SHADOW_COLOR, android.graphics.Color.TRANSPARENT }, null, android.graphics.Shader.TileMode.CLAMP);
            mShadowPaint.setShader(mRadialGradient);
        }
    }
}

