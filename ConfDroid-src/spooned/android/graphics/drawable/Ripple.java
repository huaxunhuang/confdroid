/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * Draws a Material ripple.
 */
class Ripple {
    private static final android.animation.TimeInterpolator LINEAR_INTERPOLATOR = new android.view.animation.LinearInterpolator();

    private static final android.animation.TimeInterpolator DECEL_INTERPOLATOR = new android.graphics.drawable.Ripple.LogInterpolator();

    private static final float GLOBAL_SPEED = 1.0F;

    private static final float WAVE_TOUCH_DOWN_ACCELERATION = 1024.0F * android.graphics.drawable.Ripple.GLOBAL_SPEED;

    private static final float WAVE_TOUCH_UP_ACCELERATION = 3400.0F * android.graphics.drawable.Ripple.GLOBAL_SPEED;

    private static final float WAVE_OPACITY_DECAY_VELOCITY = 3.0F / android.graphics.drawable.Ripple.GLOBAL_SPEED;

    private static final long RIPPLE_ENTER_DELAY = 80;

    // Hardware animators.
    private final java.util.ArrayList<android.view.RenderNodeAnimator> mRunningAnimations = new java.util.ArrayList<android.view.RenderNodeAnimator>();

    private final android.graphics.drawable.RippleDrawable mOwner;

    /**
     * Bounds used for computing max radius.
     */
    private final android.graphics.Rect mBounds;

    /**
     * Maximum ripple radius.
     */
    private float mOuterRadius;

    /**
     * Screen density used to adjust pixel-based velocities.
     */
    private float mDensity;

    private float mStartingX;

    private float mStartingY;

    private float mClampedStartingX;

    private float mClampedStartingY;

    // Hardware rendering properties.
    private android.graphics.CanvasProperty<android.graphics.Paint> mPropPaint;

    private android.graphics.CanvasProperty<java.lang.Float> mPropRadius;

    private android.graphics.CanvasProperty<java.lang.Float> mPropX;

    private android.graphics.CanvasProperty<java.lang.Float> mPropY;

    // Software animators.
    private android.animation.ObjectAnimator mAnimRadius;

    private android.animation.ObjectAnimator mAnimOpacity;

    private android.animation.ObjectAnimator mAnimX;

    private android.animation.ObjectAnimator mAnimY;

    // Temporary paint used for creating canvas properties.
    private android.graphics.Paint mTempPaint;

    // Software rendering properties.
    private float mOpacity = 1;

    private float mOuterX;

    private float mOuterY;

    // Values used to tween between the start and end positions.
    private float mTweenRadius = 0;

    private float mTweenX = 0;

    private float mTweenY = 0;

    /**
     * Whether we should be drawing hardware animations.
     */
    private boolean mHardwareAnimating;

    /**
     * Whether we can use hardware acceleration for the exit animation.
     */
    private boolean mCanUseHardware;

    /**
     * Whether we have an explicit maximum radius.
     */
    private boolean mHasMaxRadius;

    /**
     * Whether we were canceled externally and should avoid self-removal.
     */
    private boolean mCanceled;

    private boolean mHasPendingHardwareExit;

    private int mPendingRadiusDuration;

    private int mPendingOpacityDuration;

    /**
     * Creates a new ripple.
     */
    public Ripple(android.graphics.drawable.RippleDrawable owner, android.graphics.Rect bounds, float startingX, float startingY) {
        mOwner = owner;
        mBounds = bounds;
        mStartingX = startingX;
        mStartingY = startingY;
    }

    public void setup(int maxRadius, float density) {
        if (maxRadius != android.graphics.drawable.RippleDrawable.RADIUS_AUTO) {
            mHasMaxRadius = true;
            mOuterRadius = maxRadius;
        } else {
            final float halfWidth = mBounds.width() / 2.0F;
            final float halfHeight = mBounds.height() / 2.0F;
            mOuterRadius = ((float) (java.lang.Math.sqrt((halfWidth * halfWidth) + (halfHeight * halfHeight))));
        }
        mOuterX = 0;
        mOuterY = 0;
        mDensity = density;
        clampStartingPosition();
    }

    public boolean isHardwareAnimating() {
        return mHardwareAnimating;
    }

    private void clampStartingPosition() {
        final float cX = mBounds.exactCenterX();
        final float cY = mBounds.exactCenterY();
        final float dX = mStartingX - cX;
        final float dY = mStartingY - cY;
        final float r = mOuterRadius;
        if (((dX * dX) + (dY * dY)) > (r * r)) {
            // Point is outside the circle, clamp to the circumference.
            final double angle = java.lang.Math.atan2(dY, dX);
            mClampedStartingX = cX + ((float) (java.lang.Math.cos(angle) * r));
            mClampedStartingY = cY + ((float) (java.lang.Math.sin(angle) * r));
        } else {
            mClampedStartingX = mStartingX;
            mClampedStartingY = mStartingY;
        }
    }

    public void onHotspotBoundsChanged() {
        if (!mHasMaxRadius) {
            final float halfWidth = mBounds.width() / 2.0F;
            final float halfHeight = mBounds.height() / 2.0F;
            mOuterRadius = ((float) (java.lang.Math.sqrt((halfWidth * halfWidth) + (halfHeight * halfHeight))));
            clampStartingPosition();
        }
    }

    public void setOpacity(float a) {
        mOpacity = a;
        invalidateSelf();
    }

    public float getOpacity() {
        return mOpacity;
    }

    @java.lang.SuppressWarnings("unused")
    public void setRadiusGravity(float r) {
        mTweenRadius = r;
        invalidateSelf();
    }

    @java.lang.SuppressWarnings("unused")
    public float getRadiusGravity() {
        return mTweenRadius;
    }

    @java.lang.SuppressWarnings("unused")
    public void setXGravity(float x) {
        mTweenX = x;
        invalidateSelf();
    }

    @java.lang.SuppressWarnings("unused")
    public float getXGravity() {
        return mTweenX;
    }

    @java.lang.SuppressWarnings("unused")
    public void setYGravity(float y) {
        mTweenY = y;
        invalidateSelf();
    }

    @java.lang.SuppressWarnings("unused")
    public float getYGravity() {
        return mTweenY;
    }

    /**
     * Draws the ripple centered at (0,0) using the specified paint.
     */
    public boolean draw(android.graphics.Canvas c, android.graphics.Paint p) {
        final boolean canUseHardware = c.isHardwareAccelerated();
        if ((mCanUseHardware != canUseHardware) && mCanUseHardware) {
            // We've switched from hardware to non-hardware mode. Panic.
            cancelHardwareAnimations(true);
        }
        mCanUseHardware = canUseHardware;
        final boolean hasContent;
        if (canUseHardware && (mHardwareAnimating || mHasPendingHardwareExit)) {
            hasContent = drawHardware(((android.view.HardwareCanvas) (c)), p);
        } else {
            hasContent = drawSoftware(c, p);
        }
        return hasContent;
    }

    private boolean drawHardware(android.view.HardwareCanvas c, android.graphics.Paint p) {
        if (mHasPendingHardwareExit) {
            cancelHardwareAnimations(false);
            startPendingHardwareExit(c, p);
        }
        c.drawCircle(mPropX, mPropY, mPropRadius, mPropPaint);
        return true;
    }

    private boolean drawSoftware(android.graphics.Canvas c, android.graphics.Paint p) {
        boolean hasContent = false;
        final int paintAlpha = p.getAlpha();
        final int alpha = ((int) ((paintAlpha * mOpacity) + 0.5F));
        final float radius = android.util.MathUtils.lerp(0, mOuterRadius, mTweenRadius);
        if ((alpha > 0) && (radius > 0)) {
            final float x = android.util.MathUtils.lerp(mClampedStartingX - mBounds.exactCenterX(), mOuterX, mTweenX);
            final float y = android.util.MathUtils.lerp(mClampedStartingY - mBounds.exactCenterY(), mOuterY, mTweenY);
            p.setAlpha(alpha);
            c.drawCircle(x, y, radius, p);
            p.setAlpha(paintAlpha);
            hasContent = true;
        }
        return hasContent;
    }

    /**
     * Returns the maximum bounds of the ripple relative to the ripple center.
     */
    public void getBounds(android.graphics.Rect bounds) {
        final int outerX = ((int) (mOuterX));
        final int outerY = ((int) (mOuterY));
        final int r = ((int) (mOuterRadius)) + 1;
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
     * Starts the enter animation.
     */
    public void enter() {
        cancel();
        final int radiusDuration = ((int) ((1000 * java.lang.Math.sqrt((mOuterRadius / android.graphics.drawable.Ripple.WAVE_TOUCH_DOWN_ACCELERATION) * mDensity)) + 0.5));
        final android.animation.ObjectAnimator radius = android.animation.ObjectAnimator.ofFloat(this, "radiusGravity", 1);
        radius.setAutoCancel(true);
        radius.setDuration(radiusDuration);
        radius.setInterpolator(android.graphics.drawable.Ripple.LINEAR_INTERPOLATOR);
        radius.setStartDelay(android.graphics.drawable.Ripple.RIPPLE_ENTER_DELAY);
        final android.animation.ObjectAnimator cX = android.animation.ObjectAnimator.ofFloat(this, "xGravity", 1);
        cX.setAutoCancel(true);
        cX.setDuration(radiusDuration);
        cX.setInterpolator(android.graphics.drawable.Ripple.LINEAR_INTERPOLATOR);
        cX.setStartDelay(android.graphics.drawable.Ripple.RIPPLE_ENTER_DELAY);
        final android.animation.ObjectAnimator cY = android.animation.ObjectAnimator.ofFloat(this, "yGravity", 1);
        cY.setAutoCancel(true);
        cY.setDuration(radiusDuration);
        cY.setInterpolator(android.graphics.drawable.Ripple.LINEAR_INTERPOLATOR);
        cY.setStartDelay(android.graphics.drawable.Ripple.RIPPLE_ENTER_DELAY);
        mAnimRadius = radius;
        mAnimX = cX;
        mAnimY = cY;
        // Enter animations always run on the UI thread, since it's unlikely
        // that anything interesting is happening until the user lifts their
        // finger.
        radius.start();
        cX.start();
        cY.start();
    }

    /**
     * Starts the exit animation.
     */
    public void exit() {
        final float radius = android.util.MathUtils.lerp(0, mOuterRadius, mTweenRadius);
        final float remaining;
        if ((mAnimRadius != null) && mAnimRadius.isRunning()) {
            remaining = mOuterRadius - radius;
        } else {
            remaining = mOuterRadius;
        }
        cancel();
        final int radiusDuration = ((int) ((1000 * java.lang.Math.sqrt((remaining / (android.graphics.drawable.Ripple.WAVE_TOUCH_UP_ACCELERATION + android.graphics.drawable.Ripple.WAVE_TOUCH_DOWN_ACCELERATION)) * mDensity)) + 0.5));
        final int opacityDuration = ((int) (((1000 * mOpacity) / android.graphics.drawable.Ripple.WAVE_OPACITY_DECAY_VELOCITY) + 0.5F));
        if (mCanUseHardware) {
            createPendingHardwareExit(radiusDuration, opacityDuration);
        } else {
            exitSoftware(radiusDuration, opacityDuration);
        }
    }

    private void createPendingHardwareExit(int radiusDuration, int opacityDuration) {
        mHasPendingHardwareExit = true;
        mPendingRadiusDuration = radiusDuration;
        mPendingOpacityDuration = opacityDuration;
        // The animation will start on the next draw().
        invalidateSelf();
    }

    private void startPendingHardwareExit(android.view.HardwareCanvas c, android.graphics.Paint p) {
        mHasPendingHardwareExit = false;
        final int radiusDuration = mPendingRadiusDuration;
        final int opacityDuration = mPendingOpacityDuration;
        final float startX = android.util.MathUtils.lerp(mClampedStartingX - mBounds.exactCenterX(), mOuterX, mTweenX);
        final float startY = android.util.MathUtils.lerp(mClampedStartingY - mBounds.exactCenterY(), mOuterY, mTweenY);
        final float startRadius = android.util.MathUtils.lerp(0, mOuterRadius, mTweenRadius);
        final android.graphics.Paint paint = getTempPaint(p);
        paint.setAlpha(((int) ((paint.getAlpha() * mOpacity) + 0.5F)));
        mPropPaint = android.graphics.CanvasProperty.createPaint(paint);
        mPropRadius = android.graphics.CanvasProperty.createFloat(startRadius);
        mPropX = android.graphics.CanvasProperty.createFloat(startX);
        mPropY = android.graphics.CanvasProperty.createFloat(startY);
        final android.view.RenderNodeAnimator radiusAnim = new android.view.RenderNodeAnimator(mPropRadius, mOuterRadius);
        radiusAnim.setDuration(radiusDuration);
        radiusAnim.setInterpolator(android.graphics.drawable.Ripple.DECEL_INTERPOLATOR);
        radiusAnim.setTarget(c);
        radiusAnim.start();
        final android.view.RenderNodeAnimator xAnim = new android.view.RenderNodeAnimator(mPropX, mOuterX);
        xAnim.setDuration(radiusDuration);
        xAnim.setInterpolator(android.graphics.drawable.Ripple.DECEL_INTERPOLATOR);
        xAnim.setTarget(c);
        xAnim.start();
        final android.view.RenderNodeAnimator yAnim = new android.view.RenderNodeAnimator(mPropY, mOuterY);
        yAnim.setDuration(radiusDuration);
        yAnim.setInterpolator(android.graphics.drawable.Ripple.DECEL_INTERPOLATOR);
        yAnim.setTarget(c);
        yAnim.start();
        final android.view.RenderNodeAnimator opacityAnim = new android.view.RenderNodeAnimator(mPropPaint, android.view.RenderNodeAnimator.PAINT_ALPHA, 0);
        opacityAnim.setDuration(opacityDuration);
        opacityAnim.setInterpolator(android.graphics.drawable.Ripple.LINEAR_INTERPOLATOR);
        opacityAnim.addListener(mAnimationListener);
        opacityAnim.setTarget(c);
        opacityAnim.start();
        mRunningAnimations.add(radiusAnim);
        mRunningAnimations.add(opacityAnim);
        mRunningAnimations.add(xAnim);
        mRunningAnimations.add(yAnim);
        mHardwareAnimating = true;
        // Set up the software values to match the hardware end values.
        mOpacity = 0;
        mTweenX = 1;
        mTweenY = 1;
        mTweenRadius = 1;
    }

    /**
     * Jump all animations to their end state. The caller is responsible for
     * removing the ripple from the list of animating ripples.
     */
    public void jump() {
        mCanceled = true;
        endSoftwareAnimations();
        cancelHardwareAnimations(true);
        mCanceled = false;
    }

    private void endSoftwareAnimations() {
        if (mAnimRadius != null) {
            mAnimRadius.end();
            mAnimRadius = null;
        }
        if (mAnimOpacity != null) {
            mAnimOpacity.end();
            mAnimOpacity = null;
        }
        if (mAnimX != null) {
            mAnimX.end();
            mAnimX = null;
        }
        if (mAnimY != null) {
            mAnimY.end();
            mAnimY = null;
        }
    }

    private android.graphics.Paint getTempPaint(android.graphics.Paint original) {
        if (mTempPaint == null) {
            mTempPaint = new android.graphics.Paint();
        }
        mTempPaint.set(original);
        return mTempPaint;
    }

    private void exitSoftware(int radiusDuration, int opacityDuration) {
        final android.animation.ObjectAnimator radiusAnim = android.animation.ObjectAnimator.ofFloat(this, "radiusGravity", 1);
        radiusAnim.setAutoCancel(true);
        radiusAnim.setDuration(radiusDuration);
        radiusAnim.setInterpolator(android.graphics.drawable.Ripple.DECEL_INTERPOLATOR);
        final android.animation.ObjectAnimator xAnim = android.animation.ObjectAnimator.ofFloat(this, "xGravity", 1);
        xAnim.setAutoCancel(true);
        xAnim.setDuration(radiusDuration);
        xAnim.setInterpolator(android.graphics.drawable.Ripple.DECEL_INTERPOLATOR);
        final android.animation.ObjectAnimator yAnim = android.animation.ObjectAnimator.ofFloat(this, "yGravity", 1);
        yAnim.setAutoCancel(true);
        yAnim.setDuration(radiusDuration);
        yAnim.setInterpolator(android.graphics.drawable.Ripple.DECEL_INTERPOLATOR);
        final android.animation.ObjectAnimator opacityAnim = android.animation.ObjectAnimator.ofFloat(this, "opacity", 0);
        opacityAnim.setAutoCancel(true);
        opacityAnim.setDuration(opacityDuration);
        opacityAnim.setInterpolator(android.graphics.drawable.Ripple.LINEAR_INTERPOLATOR);
        opacityAnim.addListener(mAnimationListener);
        mAnimRadius = radiusAnim;
        mAnimOpacity = opacityAnim;
        mAnimX = xAnim;
        mAnimY = yAnim;
        radiusAnim.start();
        opacityAnim.start();
        xAnim.start();
        yAnim.start();
    }

    /**
     * Cancels all animations. The caller is responsible for removing
     * the ripple from the list of animating ripples.
     */
    public void cancel() {
        mCanceled = true;
        cancelSoftwareAnimations();
        cancelHardwareAnimations(false);
        mCanceled = false;
    }

    private void cancelSoftwareAnimations() {
        if (mAnimRadius != null) {
            mAnimRadius.cancel();
            mAnimRadius = null;
        }
        if (mAnimOpacity != null) {
            mAnimOpacity.cancel();
            mAnimOpacity = null;
        }
        if (mAnimX != null) {
            mAnimX.cancel();
            mAnimX = null;
        }
        if (mAnimY != null) {
            mAnimY.cancel();
            mAnimY = null;
        }
    }

    /**
     * Cancels any running hardware animations.
     */
    private void cancelHardwareAnimations(boolean jumpToEnd) {
        final java.util.ArrayList<android.view.RenderNodeAnimator> runningAnimations = mRunningAnimations;
        final int N = runningAnimations.size();
        for (int i = 0; i < N; i++) {
            if (jumpToEnd) {
                runningAnimations.get(i).end();
            } else {
                runningAnimations.get(i).cancel();
            }
        }
        runningAnimations.clear();
        if (mHasPendingHardwareExit) {
            // If we had a pending hardware exit, jump to the end state.
            mHasPendingHardwareExit = false;
            if (jumpToEnd) {
                mOpacity = 0;
                mTweenX = 1;
                mTweenY = 1;
                mTweenRadius = 1;
            }
        }
        mHardwareAnimating = false;
    }

    private void removeSelf() {
        // The owner will invalidate itself.
        if (!mCanceled) {
            mOwner.removeRipple(this);
        }
    }

    private void invalidateSelf() {
        mOwner.invalidateSelf();
    }

    private final android.animation.AnimatorListenerAdapter mAnimationListener = new android.animation.AnimatorListenerAdapter() {
        @java.lang.Override
        public void onAnimationEnd(android.animation.Animator animation) {
            removeSelf();
        }
    };

    /**
     * Interpolator with a smooth log deceleration
     */
    private static final class LogInterpolator implements android.animation.TimeInterpolator {
        @java.lang.Override
        public float getInterpolation(float input) {
            return 1 - ((float) (java.lang.Math.pow(400, (-input) * 1.4)));
        }
    }
}

