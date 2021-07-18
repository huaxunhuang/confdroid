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
package android.graphics.drawable;


/**
 * Draws a ripple foreground.
 */
class RippleForeground extends android.graphics.drawable.RippleComponent {
    private static final android.animation.TimeInterpolator LINEAR_INTERPOLATOR = new android.view.animation.LinearInterpolator();

    // Matches R.interpolator.fast_out_slow_in but as we have no context we can't just import that
    private static final android.animation.TimeInterpolator DECELERATE_INTERPOLATOR = new android.view.animation.PathInterpolator(0.4F, 0.0F, 0.2F, 1.0F);

    // Time it takes for the ripple to expand
    private static final int RIPPLE_ENTER_DURATION = 225;

    // Time it takes for the ripple to slide from the touch to the center point
    private static final int RIPPLE_ORIGIN_DURATION = 225;

    private static final int OPACITY_ENTER_DURATION = 75;

    private static final int OPACITY_EXIT_DURATION = 150;

    private static final int OPACITY_HOLD_DURATION = android.graphics.drawable.RippleForeground.OPACITY_ENTER_DURATION + 150;

    // Parent-relative values for starting position.
    private float mStartingX;

    private float mStartingY;

    private float mClampedStartingX;

    private float mClampedStartingY;

    // Hardware rendering properties.
    private android.graphics.CanvasProperty<android.graphics.Paint> mPropPaint;

    private android.graphics.CanvasProperty<java.lang.Float> mPropRadius;

    private android.graphics.CanvasProperty<java.lang.Float> mPropX;

    private android.graphics.CanvasProperty<java.lang.Float> mPropY;

    // Target values for tween animations.
    private float mTargetX = 0;

    private float mTargetY = 0;

    // Software rendering properties.
    private float mOpacity = 0;

    // Values used to tween between the start and end positions.
    private float mTweenRadius = 0;

    private float mTweenX = 0;

    private float mTweenY = 0;

    /**
     * Whether this ripple has finished its exit animation.
     */
    private boolean mHasFinishedExit;

    /**
     * Whether we can use hardware acceleration for the exit animation.
     */
    private boolean mUsingProperties;

    private long mEnterStartedAtMillis;

    private java.util.ArrayList<android.view.RenderNodeAnimator> mPendingHwAnimators = new java.util.ArrayList<>();

    private java.util.ArrayList<android.view.RenderNodeAnimator> mRunningHwAnimators = new java.util.ArrayList<>();

    private java.util.ArrayList<android.animation.Animator> mRunningSwAnimators = new java.util.ArrayList<>();

    /**
     * If set, force all ripple animations to not run on RenderThread, even if it would be
     * available.
     */
    private final boolean mForceSoftware;

    /**
     * If we have a bound, don't start from 0. Start from 60% of the max out of width and height.
     */
    private float mStartRadius = 0;

    public RippleForeground(android.graphics.drawable.RippleDrawable owner, android.graphics.Rect bounds, float startingX, float startingY, boolean forceSoftware) {
        super(owner, bounds);
        mForceSoftware = forceSoftware;
        mStartingX = startingX;
        mStartingY = startingY;
        // Take 60% of the maximum of the width and height, then divided half to get the radius.
        mStartRadius = java.lang.Math.max(bounds.width(), bounds.height()) * 0.3F;
        clampStartingPosition();
    }

    @java.lang.Override
    protected void onTargetRadiusChanged(float targetRadius) {
        clampStartingPosition();
        switchToUiThreadAnimation();
    }

    private void drawSoftware(android.graphics.Canvas c, android.graphics.Paint p) {
        final int origAlpha = p.getAlpha();
        final int alpha = ((int) ((origAlpha * mOpacity) + 0.5F));
        final float radius = getCurrentRadius();
        if ((alpha > 0) && (radius > 0)) {
            final float x = getCurrentX();
            final float y = getCurrentY();
            p.setAlpha(alpha);
            c.drawCircle(x, y, radius, p);
            p.setAlpha(origAlpha);
        }
    }

    private void startPending(android.graphics.RecordingCanvas c) {
        if (!mPendingHwAnimators.isEmpty()) {
            for (int i = 0; i < mPendingHwAnimators.size(); i++) {
                android.view.RenderNodeAnimator animator = mPendingHwAnimators.get(i);
                animator.setTarget(c);
                animator.start();
                mRunningHwAnimators.add(animator);
            }
            mPendingHwAnimators.clear();
        }
    }

    private void pruneHwFinished() {
        if (!mRunningHwAnimators.isEmpty()) {
            for (int i = mRunningHwAnimators.size() - 1; i >= 0; i--) {
                if (!mRunningHwAnimators.get(i).isRunning()) {
                    mRunningHwAnimators.remove(i);
                }
            }
        }
    }

    private void pruneSwFinished() {
        if (!mRunningSwAnimators.isEmpty()) {
            for (int i = mRunningSwAnimators.size() - 1; i >= 0; i--) {
                if (!mRunningSwAnimators.get(i).isRunning()) {
                    mRunningSwAnimators.remove(i);
                }
            }
        }
    }

    private void drawHardware(android.graphics.RecordingCanvas c, android.graphics.Paint p) {
        startPending(c);
        pruneHwFinished();
        if (mPropPaint != null) {
            mUsingProperties = true;
            c.drawCircle(mPropX, mPropY, mPropRadius, mPropPaint);
        } else {
            mUsingProperties = false;
            drawSoftware(c, p);
        }
    }

    /**
     * Returns the maximum bounds of the ripple relative to the ripple center.
     */
    public void getBounds(android.graphics.Rect bounds) {
        final int outerX = ((int) (mTargetX));
        final int outerY = ((int) (mTargetY));
        final int r = ((int) (mTargetRadius)) + 1;
        bounds.set(outerX - r, outerY - r, outerX + r, outerY + r);
    }

    /**
     * Specifies the starting position relative to the drawable bounds. No-op if
     * the ripple has already entered.
     */
    public void move(float x, float y) {
        mStartingX = x;
        mStartingY = y;
        clampStartingPosition();
    }

    /**
     *
     *
     * @return {@code true} if this ripple has finished its exit animation
     */
    public boolean hasFinishedExit() {
        return mHasFinishedExit;
    }

    private long computeFadeOutDelay() {
        long timeSinceEnter = android.view.animation.AnimationUtils.currentAnimationTimeMillis() - mEnterStartedAtMillis;
        if ((timeSinceEnter > 0) && (timeSinceEnter < android.graphics.drawable.RippleForeground.OPACITY_HOLD_DURATION)) {
            return android.graphics.drawable.RippleForeground.OPACITY_HOLD_DURATION - timeSinceEnter;
        }
        return 0;
    }

    private void startSoftwareEnter() {
        for (int i = 0; i < mRunningSwAnimators.size(); i++) {
            mRunningSwAnimators.get(i).cancel();
        }
        mRunningSwAnimators.clear();
        final android.animation.ObjectAnimator tweenRadius = android.animation.ObjectAnimator.ofFloat(this, android.graphics.drawable.RippleForeground.TWEEN_RADIUS, 1);
        tweenRadius.setDuration(android.graphics.drawable.RippleForeground.RIPPLE_ENTER_DURATION);
        tweenRadius.setInterpolator(android.graphics.drawable.RippleForeground.DECELERATE_INTERPOLATOR);
        tweenRadius.start();
        mRunningSwAnimators.add(tweenRadius);
        final android.animation.ObjectAnimator tweenOrigin = android.animation.ObjectAnimator.ofFloat(this, android.graphics.drawable.RippleForeground.TWEEN_ORIGIN, 1);
        tweenOrigin.setDuration(android.graphics.drawable.RippleForeground.RIPPLE_ORIGIN_DURATION);
        tweenOrigin.setInterpolator(android.graphics.drawable.RippleForeground.DECELERATE_INTERPOLATOR);
        tweenOrigin.start();
        mRunningSwAnimators.add(tweenOrigin);
        final android.animation.ObjectAnimator opacity = android.animation.ObjectAnimator.ofFloat(this, android.graphics.drawable.RippleForeground.OPACITY, 1);
        opacity.setDuration(android.graphics.drawable.RippleForeground.OPACITY_ENTER_DURATION);
        opacity.setInterpolator(android.graphics.drawable.RippleForeground.LINEAR_INTERPOLATOR);
        opacity.start();
        mRunningSwAnimators.add(opacity);
    }

    private void startSoftwareExit() {
        final android.animation.ObjectAnimator opacity = android.animation.ObjectAnimator.ofFloat(this, android.graphics.drawable.RippleForeground.OPACITY, 0);
        opacity.setDuration(android.graphics.drawable.RippleForeground.OPACITY_EXIT_DURATION);
        opacity.setInterpolator(android.graphics.drawable.RippleForeground.LINEAR_INTERPOLATOR);
        opacity.addListener(mAnimationListener);
        opacity.setStartDelay(computeFadeOutDelay());
        opacity.start();
        mRunningSwAnimators.add(opacity);
    }

    private void startHardwareEnter() {
        if (mForceSoftware) {
            return;
        }
        mPropX = android.graphics.CanvasProperty.createFloat(getCurrentX());
        mPropY = android.graphics.CanvasProperty.createFloat(getCurrentY());
        mPropRadius = android.graphics.CanvasProperty.createFloat(getCurrentRadius());
        final android.graphics.Paint paint = mOwner.getRipplePaint();
        mPropPaint = android.graphics.CanvasProperty.createPaint(paint);
        final android.view.RenderNodeAnimator radius = new android.view.RenderNodeAnimator(mPropRadius, mTargetRadius);
        radius.setDuration(android.graphics.drawable.RippleForeground.RIPPLE_ORIGIN_DURATION);
        radius.setInterpolator(android.graphics.drawable.RippleForeground.DECELERATE_INTERPOLATOR);
        mPendingHwAnimators.add(radius);
        final android.view.RenderNodeAnimator x = new android.view.RenderNodeAnimator(mPropX, mTargetX);
        x.setDuration(android.graphics.drawable.RippleForeground.RIPPLE_ORIGIN_DURATION);
        x.setInterpolator(android.graphics.drawable.RippleForeground.DECELERATE_INTERPOLATOR);
        mPendingHwAnimators.add(x);
        final android.view.RenderNodeAnimator y = new android.view.RenderNodeAnimator(mPropY, mTargetY);
        y.setDuration(android.graphics.drawable.RippleForeground.RIPPLE_ORIGIN_DURATION);
        y.setInterpolator(android.graphics.drawable.RippleForeground.DECELERATE_INTERPOLATOR);
        mPendingHwAnimators.add(y);
        final android.view.RenderNodeAnimator opacity = new android.view.RenderNodeAnimator(mPropPaint, android.view.RenderNodeAnimator.PAINT_ALPHA, paint.getAlpha());
        opacity.setDuration(android.graphics.drawable.RippleForeground.OPACITY_ENTER_DURATION);
        opacity.setInterpolator(android.graphics.drawable.RippleForeground.LINEAR_INTERPOLATOR);
        opacity.setStartValue(0);
        mPendingHwAnimators.add(opacity);
        invalidateSelf();
    }

    private void startHardwareExit() {
        // Only run a hardware exit if we had a hardware enter to continue from
        if (mForceSoftware || (mPropPaint == null))
            return;

        final android.view.RenderNodeAnimator opacity = new android.view.RenderNodeAnimator(mPropPaint, android.view.RenderNodeAnimator.PAINT_ALPHA, 0);
        opacity.setDuration(android.graphics.drawable.RippleForeground.OPACITY_EXIT_DURATION);
        opacity.setInterpolator(android.graphics.drawable.RippleForeground.LINEAR_INTERPOLATOR);
        opacity.addListener(mAnimationListener);
        opacity.setStartDelay(computeFadeOutDelay());
        opacity.setStartValue(mOwner.getRipplePaint().getAlpha());
        mPendingHwAnimators.add(opacity);
        invalidateSelf();
    }

    /**
     * Starts a ripple enter animation.
     */
    public final void enter() {
        mEnterStartedAtMillis = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
        startSoftwareEnter();
        startHardwareEnter();
    }

    /**
     * Starts a ripple exit animation.
     */
    public final void exit() {
        startSoftwareExit();
        startHardwareExit();
    }

    private float getCurrentX() {
        return android.util.MathUtils.lerp(mClampedStartingX - mBounds.exactCenterX(), mTargetX, mTweenX);
    }

    private float getCurrentY() {
        return android.util.MathUtils.lerp(mClampedStartingY - mBounds.exactCenterY(), mTargetY, mTweenY);
    }

    private float getCurrentRadius() {
        return android.util.MathUtils.lerp(mStartRadius, mTargetRadius, mTweenRadius);
    }

    /**
     * Draws the ripple to the canvas, inheriting the paint's color and alpha
     * properties.
     *
     * @param c
     * 		the canvas to which the ripple should be drawn
     * @param p
     * 		the paint used to draw the ripple
     */
    public void draw(android.graphics.Canvas c, android.graphics.Paint p) {
        final boolean hasDisplayListCanvas = (!mForceSoftware) && (c instanceof android.graphics.RecordingCanvas);
        pruneSwFinished();
        if (hasDisplayListCanvas) {
            final android.graphics.RecordingCanvas hw = ((android.graphics.RecordingCanvas) (c));
            drawHardware(hw, p);
        } else {
            drawSoftware(c, p);
        }
    }

    /**
     * Clamps the starting position to fit within the ripple bounds.
     */
    private void clampStartingPosition() {
        final float cX = mBounds.exactCenterX();
        final float cY = mBounds.exactCenterY();
        final float dX = mStartingX - cX;
        final float dY = mStartingY - cY;
        final float r = mTargetRadius - mStartRadius;
        if (((dX * dX) + (dY * dY)) > (r * r)) {
            // Point is outside the circle, clamp to the perimeter.
            final double angle = java.lang.Math.atan2(dY, dX);
            mClampedStartingX = cX + ((float) (java.lang.Math.cos(angle) * r));
            mClampedStartingY = cY + ((float) (java.lang.Math.sin(angle) * r));
        } else {
            mClampedStartingX = mStartingX;
            mClampedStartingY = mStartingY;
        }
    }

    /**
     * Ends all animations, jumping values to the end state.
     */
    public void end() {
        for (int i = 0; i < mRunningSwAnimators.size(); i++) {
            end();
        }
        mRunningSwAnimators.clear();
        for (int i = 0; i < mRunningHwAnimators.size(); i++) {
            mRunningHwAnimators.get(i).end();
        }
        mRunningHwAnimators.clear();
    }

    private void onAnimationPropertyChanged() {
        if (!mUsingProperties) {
            invalidateSelf();
        }
    }

    private void clearHwProps() {
        mPropPaint = null;
        mPropRadius = null;
        mPropX = null;
        mPropY = null;
        mUsingProperties = false;
    }

    private final android.animation.AnimatorListenerAdapter mAnimationListener = new android.animation.AnimatorListenerAdapter() {
        @java.lang.Override
        public void onAnimationEnd(android.animation.Animator animator) {
            mHasFinishedExit = true;
            pruneHwFinished();
            pruneSwFinished();
            if (mRunningHwAnimators.isEmpty()) {
                clearHwProps();
            }
        }
    };

    private void switchToUiThreadAnimation() {
        for (int i = 0; i < mRunningHwAnimators.size(); i++) {
            android.animation.Animator animator = mRunningHwAnimators.get(i);
            animator.removeListener(mAnimationListener);
            animator.end();
        }
        mRunningHwAnimators.clear();
        clearHwProps();
        invalidateSelf();
    }

    /**
     * Property for animating radius between its initial and target values.
     */
    private static final android.util.FloatProperty<android.graphics.drawable.RippleForeground> TWEEN_RADIUS = new android.util.FloatProperty<android.graphics.drawable.RippleForeground>("tweenRadius") {
        @java.lang.Override
        public void setValue(android.graphics.drawable.RippleForeground object, float value) {
            object.mTweenRadius = value;
            object.onAnimationPropertyChanged();
        }

        @java.lang.Override
        public java.lang.Float get(android.graphics.drawable.RippleForeground object) {
            return object.mTweenRadius;
        }
    };

    /**
     * Property for animating origin between its initial and target values.
     */
    private static final android.util.FloatProperty<android.graphics.drawable.RippleForeground> TWEEN_ORIGIN = new android.util.FloatProperty<android.graphics.drawable.RippleForeground>("tweenOrigin") {
        @java.lang.Override
        public void setValue(android.graphics.drawable.RippleForeground object, float value) {
            object.mTweenX = value;
            object.mTweenY = value;
            object.onAnimationPropertyChanged();
        }

        @java.lang.Override
        public java.lang.Float get(android.graphics.drawable.RippleForeground object) {
            return object.mTweenX;
        }
    };

    /**
     * Property for animating opacity between 0 and its target value.
     */
    private static final android.util.FloatProperty<android.graphics.drawable.RippleForeground> OPACITY = new android.util.FloatProperty<android.graphics.drawable.RippleForeground>("opacity") {
        @java.lang.Override
        public void setValue(android.graphics.drawable.RippleForeground object, float value) {
            object.mOpacity = value;
            object.onAnimationPropertyChanged();
        }

        @java.lang.Override
        public java.lang.Float get(android.graphics.drawable.RippleForeground object) {
            return object.mOpacity;
        }
    };
}

