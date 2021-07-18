/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.widget;


/**
 * This class performs the graphical effect used at the edges of scrollable widgets
 * when the user scrolls beyond the content bounds in 2D space.
 *
 * <p>EdgeEffect is stateful. Custom widgets using EdgeEffect should create an
 * instance for each edge that should show the effect, feed it input data using
 * the methods {@link #onAbsorb(int)}, {@link #onPull(float)}, and {@link #onRelease()},
 * and draw the effect using {@link #draw(Canvas)} in the widget's overridden
 * {@link android.view.View#draw(Canvas)} method. If {@link #isFinished()} returns
 * false after drawing, the edge effect's animation is not yet complete and the widget
 * should schedule another drawing pass to continue the animation.</p>
 *
 * <p>When drawing, widgets should draw their main content and child views first,
 * usually by invoking <code>super.draw(canvas)</code> from an overridden <code>draw</code>
 * method. (This will invoke onDraw and dispatch drawing to child views as needed.)
 * The edge effect may then be drawn on top of the view's content using the
 * {@link #draw(Canvas)} method.</p>
 */
public class EdgeEffect {
    /**
     * The default blend mode used by {@link EdgeEffect}.
     */
    public static final android.graphics.BlendMode DEFAULT_BLEND_MODE = android.graphics.BlendMode.SRC_ATOP;

    @java.lang.SuppressWarnings("UnusedDeclaration")
    private static final java.lang.String TAG = "EdgeEffect";

    // Time it will take the effect to fully recede in ms
    private static final int RECEDE_TIME = 600;

    // Time it will take before a pulled glow begins receding in ms
    private static final int PULL_TIME = 167;

    // Time it will take in ms for a pulled glow to decay to partial strength before release
    private static final int PULL_DECAY_TIME = 2000;

    private static final float MAX_ALPHA = 0.15F;

    private static final float GLOW_ALPHA_START = 0.09F;

    private static final float MAX_GLOW_SCALE = 2.0F;

    private static final float PULL_GLOW_BEGIN = 0.0F;

    // Minimum velocity that will be absorbed
    private static final int MIN_VELOCITY = 100;

    // Maximum velocity, clamps at this value
    private static final int MAX_VELOCITY = 10000;

    private static final float EPSILON = 0.001F;

    private static final double ANGLE = java.lang.Math.PI / 6;

    private static final float SIN = ((float) (java.lang.Math.sin(android.widget.EdgeEffect.ANGLE)));

    private static final float COS = ((float) (java.lang.Math.cos(android.widget.EdgeEffect.ANGLE)));

    private static final float RADIUS_FACTOR = 0.6F;

    private float mGlowAlpha;

    @android.annotation.UnsupportedAppUsage
    private float mGlowScaleY;

    private float mGlowAlphaStart;

    private float mGlowAlphaFinish;

    private float mGlowScaleYStart;

    private float mGlowScaleYFinish;

    private long mStartTime;

    private float mDuration;

    private final android.view.animation.Interpolator mInterpolator;

    private static final int STATE_IDLE = 0;

    private static final int STATE_PULL = 1;

    private static final int STATE_ABSORB = 2;

    private static final int STATE_RECEDE = 3;

    private static final int STATE_PULL_DECAY = 4;

    private static final float PULL_DISTANCE_ALPHA_GLOW_FACTOR = 0.8F;

    private static final int VELOCITY_GLOW_FACTOR = 6;

    private int mState = android.widget.EdgeEffect.STATE_IDLE;

    private float mPullDistance;

    private final android.graphics.Rect mBounds = new android.graphics.Rect();

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 123769450)
    private final android.graphics.Paint mPaint = new android.graphics.Paint();

    private float mRadius;

    private float mBaseGlowScale;

    private float mDisplacement = 0.5F;

    private float mTargetDisplacement = 0.5F;

    /**
     * Construct a new EdgeEffect with a theme appropriate for the provided context.
     *
     * @param context
     * 		Context used to provide theming and resource information for the EdgeEffect
     */
    public EdgeEffect(android.content.Context context) {
        mPaint.setAntiAlias(true);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(com.android.internal.R.styleable.EdgeEffect);
        final int themeColor = a.getColor(com.android.internal.R.styleable.EdgeEffect_colorEdgeEffect, 0xff666666);
        a.recycle();
        mPaint.setColor((themeColor & 0xffffff) | 0x33000000);
        mPaint.setStyle(android.graphics.Paint.Style.FILL);
        mPaint.setBlendMode(android.widget.EdgeEffect.DEFAULT_BLEND_MODE);
        mInterpolator = new android.view.animation.DecelerateInterpolator();
    }

    /**
     * Set the size of this edge effect in pixels.
     *
     * @param width
     * 		Effect width in pixels
     * @param height
     * 		Effect height in pixels
     */
    public void setSize(int width, int height) {
        final float r = (width * android.widget.EdgeEffect.RADIUS_FACTOR) / android.widget.EdgeEffect.SIN;
        final float y = android.widget.EdgeEffect.COS * r;
        final float h = r - y;
        final float or = (height * android.widget.EdgeEffect.RADIUS_FACTOR) / android.widget.EdgeEffect.SIN;
        final float oy = android.widget.EdgeEffect.COS * or;
        final float oh = or - oy;
        mRadius = r;
        mBaseGlowScale = (h > 0) ? java.lang.Math.min(oh / h, 1.0F) : 1.0F;
        mBounds.set(mBounds.left, mBounds.top, width, ((int) (java.lang.Math.min(height, h))));
    }

    /**
     * Reports if this EdgeEffect's animation is finished. If this method returns false
     * after a call to {@link #draw(Canvas)} the host widget should schedule another
     * drawing pass to continue the animation.
     *
     * @return true if animation is finished, false if drawing should continue on the next frame.
     */
    public boolean isFinished() {
        return mState == android.widget.EdgeEffect.STATE_IDLE;
    }

    /**
     * Immediately finish the current animation.
     * After this call {@link #isFinished()} will return true.
     */
    public void finish() {
        mState = android.widget.EdgeEffect.STATE_IDLE;
    }

    /**
     * A view should call this when content is pulled away from an edge by the user.
     * This will update the state of the current visual effect and its associated animation.
     * The host view should always {@link android.view.View#invalidate()} after this
     * and draw the results accordingly.
     *
     * <p>Views using EdgeEffect should favor {@link #onPull(float, float)} when the displacement
     * of the pull point is known.</p>
     *
     * @param deltaDistance
     * 		Change in distance since the last call. Values may be 0 (no change) to
     * 		1.f (full length of the view) or negative values to express change
     * 		back toward the edge reached to initiate the effect.
     */
    public void onPull(float deltaDistance) {
        onPull(deltaDistance, 0.5F);
    }

    /**
     * A view should call this when content is pulled away from an edge by the user.
     * This will update the state of the current visual effect and its associated animation.
     * The host view should always {@link android.view.View#invalidate()} after this
     * and draw the results accordingly.
     *
     * @param deltaDistance
     * 		Change in distance since the last call. Values may be 0 (no change) to
     * 		1.f (full length of the view) or negative values to express change
     * 		back toward the edge reached to initiate the effect.
     * @param displacement
     * 		The displacement from the starting side of the effect of the point
     * 		initiating the pull. In the case of touch this is the finger position.
     * 		Values may be from 0-1.
     */
    public void onPull(float deltaDistance, float displacement) {
        final long now = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
        mTargetDisplacement = displacement;
        if ((mState == android.widget.EdgeEffect.STATE_PULL_DECAY) && ((now - mStartTime) < mDuration)) {
            return;
        }
        if (mState != android.widget.EdgeEffect.STATE_PULL) {
            mGlowScaleY = java.lang.Math.max(android.widget.EdgeEffect.PULL_GLOW_BEGIN, mGlowScaleY);
        }
        mState = android.widget.EdgeEffect.STATE_PULL;
        mStartTime = now;
        mDuration = android.widget.EdgeEffect.PULL_TIME;
        mPullDistance += deltaDistance;
        final float absdd = java.lang.Math.abs(deltaDistance);
        mGlowAlpha = mGlowAlphaStart = java.lang.Math.min(android.widget.EdgeEffect.MAX_ALPHA, mGlowAlpha + (absdd * android.widget.EdgeEffect.PULL_DISTANCE_ALPHA_GLOW_FACTOR));
        if (mPullDistance == 0) {
            mGlowScaleY = mGlowScaleYStart = 0;
        } else {
            final float scale = ((float) (java.lang.Math.max(0, (1 - (1 / java.lang.Math.sqrt(java.lang.Math.abs(mPullDistance) * mBounds.height()))) - 0.3) / 0.7));
            mGlowScaleY = mGlowScaleYStart = scale;
        }
        mGlowAlphaFinish = mGlowAlpha;
        mGlowScaleYFinish = mGlowScaleY;
    }

    /**
     * Call when the object is released after being pulled.
     * This will begin the "decay" phase of the effect. After calling this method
     * the host view should {@link android.view.View#invalidate()} and thereby
     * draw the results accordingly.
     */
    public void onRelease() {
        mPullDistance = 0;
        if ((mState != android.widget.EdgeEffect.STATE_PULL) && (mState != android.widget.EdgeEffect.STATE_PULL_DECAY)) {
            return;
        }
        mState = android.widget.EdgeEffect.STATE_RECEDE;
        mGlowAlphaStart = mGlowAlpha;
        mGlowScaleYStart = mGlowScaleY;
        mGlowAlphaFinish = 0.0F;
        mGlowScaleYFinish = 0.0F;
        mStartTime = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
        mDuration = android.widget.EdgeEffect.RECEDE_TIME;
    }

    /**
     * Call when the effect absorbs an impact at the given velocity.
     * Used when a fling reaches the scroll boundary.
     *
     * <p>When using a {@link android.widget.Scroller} or {@link android.widget.OverScroller},
     * the method <code>getCurrVelocity</code> will provide a reasonable approximation
     * to use here.</p>
     *
     * @param velocity
     * 		Velocity at impact in pixels per second.
     */
    public void onAbsorb(int velocity) {
        mState = android.widget.EdgeEffect.STATE_ABSORB;
        velocity = java.lang.Math.min(java.lang.Math.max(android.widget.EdgeEffect.MIN_VELOCITY, java.lang.Math.abs(velocity)), android.widget.EdgeEffect.MAX_VELOCITY);
        mStartTime = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
        mDuration = 0.15F + (velocity * 0.02F);
        // The glow depends more on the velocity, and therefore starts out
        // nearly invisible.
        mGlowAlphaStart = android.widget.EdgeEffect.GLOW_ALPHA_START;
        mGlowScaleYStart = java.lang.Math.max(mGlowScaleY, 0.0F);
        // Growth for the size of the glow should be quadratic to properly
        // respond
        // to a user's scrolling speed. The faster the scrolling speed, the more
        // intense the effect should be for both the size and the saturation.
        mGlowScaleYFinish = java.lang.Math.min(0.025F + (((velocity * (velocity / 100)) * 1.5E-4F) / 2), 1.0F);
        // Alpha should change for the glow as well as size.
        mGlowAlphaFinish = java.lang.Math.max(mGlowAlphaStart, java.lang.Math.min((velocity * android.widget.EdgeEffect.VELOCITY_GLOW_FACTOR) * 1.0E-5F, android.widget.EdgeEffect.MAX_ALPHA));
        mTargetDisplacement = 0.5F;
    }

    /**
     * Set the color of this edge effect in argb.
     *
     * @param color
     * 		Color in argb
     */
    public void setColor(@android.annotation.ColorInt
    int color) {
        mPaint.setColor(color);
    }

    /**
     * Set or clear the blend mode. A blend mode defines how source pixels
     * (generated by a drawing command) are composited with the destination pixels
     * (content of the render target).
     * <p />
     * Pass null to clear any previous blend mode.
     * <p />
     *
     * @see BlendMode
     * @param blendmode
     * 		May be null. The blend mode to be installed in the paint
     */
    public void setBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode blendmode) {
        mPaint.setBlendMode(blendmode);
    }

    /**
     * Return the color of this edge effect in argb.
     *
     * @return The color of this edge effect in argb
     */
    @android.annotation.ColorInt
    public int getColor() {
        return mPaint.getColor();
    }

    /**
     * Returns the blend mode. A blend mode defines how source pixels
     * (generated by a drawing command) are composited with the destination pixels
     * (content of the render target).
     * <p />
     *
     * @return BlendMode
     */
    @android.annotation.Nullable
    public android.graphics.BlendMode getBlendMode() {
        return mPaint.getBlendMode();
    }

    /**
     * Draw into the provided canvas. Assumes that the canvas has been rotated
     * accordingly and the size has been set. The effect will be drawn the full
     * width of X=0 to X=width, beginning from Y=0 and extending to some factor <
     * 1.f of height.
     *
     * @param canvas
     * 		Canvas to draw into
     * @return true if drawing should continue beyond this frame to continue the
    animation
     */
    public boolean draw(android.graphics.Canvas canvas) {
        update();
        final int count = canvas.save();
        final float centerX = mBounds.centerX();
        final float centerY = mBounds.height() - mRadius;
        canvas.scale(1.0F, java.lang.Math.min(mGlowScaleY, 1.0F) * mBaseGlowScale, centerX, 0);
        final float displacement = java.lang.Math.max(0, java.lang.Math.min(mDisplacement, 1.0F)) - 0.5F;
        float translateX = (mBounds.width() * displacement) / 2;
        canvas.clipRect(mBounds);
        canvas.translate(translateX, 0);
        mPaint.setAlpha(((int) (0xff * mGlowAlpha)));
        canvas.drawCircle(centerX, centerY, mRadius, mPaint);
        canvas.restoreToCount(count);
        boolean oneLastFrame = false;
        if ((mState == android.widget.EdgeEffect.STATE_RECEDE) && (mGlowScaleY == 0)) {
            mState = android.widget.EdgeEffect.STATE_IDLE;
            oneLastFrame = true;
        }
        return (mState != android.widget.EdgeEffect.STATE_IDLE) || oneLastFrame;
    }

    /**
     * Return the maximum height that the edge effect will be drawn at given the original
     * {@link #setSize(int, int) input size}.
     *
     * @return The maximum height of the edge effect
     */
    public int getMaxHeight() {
        return ((int) ((mBounds.height() * android.widget.EdgeEffect.MAX_GLOW_SCALE) + 0.5F));
    }

    private void update() {
        final long time = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
        final float t = java.lang.Math.min((time - mStartTime) / mDuration, 1.0F);
        final float interp = mInterpolator.getInterpolation(t);
        mGlowAlpha = mGlowAlphaStart + ((mGlowAlphaFinish - mGlowAlphaStart) * interp);
        mGlowScaleY = mGlowScaleYStart + ((mGlowScaleYFinish - mGlowScaleYStart) * interp);
        mDisplacement = (mDisplacement + mTargetDisplacement) / 2;
        if (t >= (1.0F - android.widget.EdgeEffect.EPSILON)) {
            switch (mState) {
                case android.widget.EdgeEffect.STATE_ABSORB :
                    mState = android.widget.EdgeEffect.STATE_RECEDE;
                    mStartTime = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
                    mDuration = android.widget.EdgeEffect.RECEDE_TIME;
                    mGlowAlphaStart = mGlowAlpha;
                    mGlowScaleYStart = mGlowScaleY;
                    // After absorb, the glow should fade to nothing.
                    mGlowAlphaFinish = 0.0F;
                    mGlowScaleYFinish = 0.0F;
                    break;
                case android.widget.EdgeEffect.STATE_PULL :
                    mState = android.widget.EdgeEffect.STATE_PULL_DECAY;
                    mStartTime = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
                    mDuration = android.widget.EdgeEffect.PULL_DECAY_TIME;
                    mGlowAlphaStart = mGlowAlpha;
                    mGlowScaleYStart = mGlowScaleY;
                    // After pull, the glow should fade to nothing.
                    mGlowAlphaFinish = 0.0F;
                    mGlowScaleYFinish = 0.0F;
                    break;
                case android.widget.EdgeEffect.STATE_PULL_DECAY :
                    mState = android.widget.EdgeEffect.STATE_RECEDE;
                    break;
                case android.widget.EdgeEffect.STATE_RECEDE :
                    mState = android.widget.EdgeEffect.STATE_IDLE;
                    break;
            }
        }
    }
}

