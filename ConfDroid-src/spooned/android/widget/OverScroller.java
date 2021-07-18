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
 * This class encapsulates scrolling with the ability to overshoot the bounds
 * of a scrolling operation. This class is a drop-in replacement for
 * {@link android.widget.Scroller} in most cases.
 */
public class OverScroller {
    private int mMode;

    private final android.widget.OverScroller.SplineOverScroller mScrollerX;

    @android.annotation.UnsupportedAppUsage
    private final android.widget.OverScroller.SplineOverScroller mScrollerY;

    @android.annotation.UnsupportedAppUsage
    private android.view.animation.Interpolator mInterpolator;

    private final boolean mFlywheel;

    private static final int DEFAULT_DURATION = 250;

    private static final int SCROLL_MODE = 0;

    private static final int FLING_MODE = 1;

    /**
     * Creates an OverScroller with a viscous fluid scroll interpolator and flywheel.
     *
     * @param context
     * 		
     */
    public OverScroller(android.content.Context context) {
        this(context, null);
    }

    /**
     * Creates an OverScroller with flywheel enabled.
     *
     * @param context
     * 		The context of this application.
     * @param interpolator
     * 		The scroll interpolator. If null, a default (viscous) interpolator will
     * 		be used.
     */
    public OverScroller(android.content.Context context, android.view.animation.Interpolator interpolator) {
        this(context, interpolator, true);
    }

    /**
     * Creates an OverScroller.
     *
     * @param context
     * 		The context of this application.
     * @param interpolator
     * 		The scroll interpolator. If null, a default (viscous) interpolator will
     * 		be used.
     * @param flywheel
     * 		If true, successive fling motions will keep on increasing scroll speed.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public OverScroller(android.content.Context context, android.view.animation.Interpolator interpolator, boolean flywheel) {
        if (interpolator == null) {
            mInterpolator = new android.widget.Scroller.ViscousFluidInterpolator();
        } else {
            mInterpolator = interpolator;
        }
        mFlywheel = flywheel;
        mScrollerX = new android.widget.OverScroller.SplineOverScroller(context);
        mScrollerY = new android.widget.OverScroller.SplineOverScroller(context);
    }

    /**
     * Creates an OverScroller with flywheel enabled.
     *
     * @param context
     * 		The context of this application.
     * @param interpolator
     * 		The scroll interpolator. If null, a default (viscous) interpolator will
     * 		be used.
     * @param bounceCoefficientX
     * 		A value between 0 and 1 that will determine the proportion of the
     * 		velocity which is preserved in the bounce when the horizontal edge is reached. A null value
     * 		means no bounce. This behavior is no longer supported and this coefficient has no effect.
     * @param bounceCoefficientY
     * 		Same as bounceCoefficientX but for the vertical direction. This
     * 		behavior is no longer supported and this coefficient has no effect.
     * @deprecated Use {@link #OverScroller(Context, Interpolator)} instead.
     */
    @java.lang.Deprecated
    public OverScroller(android.content.Context context, android.view.animation.Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY) {
        this(context, interpolator, true);
    }

    /**
     * Creates an OverScroller.
     *
     * @param context
     * 		The context of this application.
     * @param interpolator
     * 		The scroll interpolator. If null, a default (viscous) interpolator will
     * 		be used.
     * @param bounceCoefficientX
     * 		A value between 0 and 1 that will determine the proportion of the
     * 		velocity which is preserved in the bounce when the horizontal edge is reached. A null value
     * 		means no bounce. This behavior is no longer supported and this coefficient has no effect.
     * @param bounceCoefficientY
     * 		Same as bounceCoefficientX but for the vertical direction. This
     * 		behavior is no longer supported and this coefficient has no effect.
     * @param flywheel
     * 		If true, successive fling motions will keep on increasing scroll speed.
     * @deprecated Use {@link #OverScroller(Context, Interpolator)} instead.
     */
    @java.lang.Deprecated
    public OverScroller(android.content.Context context, android.view.animation.Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY, boolean flywheel) {
        this(context, interpolator, flywheel);
    }

    @android.annotation.UnsupportedAppUsage
    void setInterpolator(android.view.animation.Interpolator interpolator) {
        if (interpolator == null) {
            mInterpolator = new android.widget.Scroller.ViscousFluidInterpolator();
        } else {
            mInterpolator = interpolator;
        }
    }

    /**
     * The amount of friction applied to flings. The default value
     * is {@link ViewConfiguration#getScrollFriction}.
     *
     * @param friction
     * 		A scalar dimension-less value representing the coefficient of
     * 		friction.
     */
    public final void setFriction(float friction) {
        mScrollerX.setFriction(friction);
        mScrollerY.setFriction(friction);
    }

    /**
     * Returns whether the scroller has finished scrolling.
     *
     * @return True if the scroller has finished scrolling, false otherwise.
     */
    public final boolean isFinished() {
        return mScrollerX.mFinished && mScrollerY.mFinished;
    }

    /**
     * Force the finished field to a particular value. Contrary to
     * {@link #abortAnimation()}, forcing the animation to finished
     * does NOT cause the scroller to move to the final x and y
     * position.
     *
     * @param finished
     * 		The new finished value.
     */
    public final void forceFinished(boolean finished) {
        mScrollerX.mFinished = mScrollerY.mFinished = finished;
    }

    /**
     * Returns the current X offset in the scroll.
     *
     * @return The new X offset as an absolute distance from the origin.
     */
    public final int getCurrX() {
        return mScrollerX.mCurrentPosition;
    }

    /**
     * Returns the current Y offset in the scroll.
     *
     * @return The new Y offset as an absolute distance from the origin.
     */
    public final int getCurrY() {
        return mScrollerY.mCurrentPosition;
    }

    /**
     * Returns the absolute value of the current velocity.
     *
     * @return The original velocity less the deceleration, norm of the X and Y velocity vector.
     */
    public float getCurrVelocity() {
        return ((float) (java.lang.Math.hypot(mScrollerX.mCurrVelocity, mScrollerY.mCurrVelocity)));
    }

    /**
     * Returns the start X offset in the scroll.
     *
     * @return The start X offset as an absolute distance from the origin.
     */
    public final int getStartX() {
        return mScrollerX.mStart;
    }

    /**
     * Returns the start Y offset in the scroll.
     *
     * @return The start Y offset as an absolute distance from the origin.
     */
    public final int getStartY() {
        return mScrollerY.mStart;
    }

    /**
     * Returns where the scroll will end. Valid only for "fling" scrolls.
     *
     * @return The final X offset as an absolute distance from the origin.
     */
    public final int getFinalX() {
        return mScrollerX.mFinal;
    }

    /**
     * Returns where the scroll will end. Valid only for "fling" scrolls.
     *
     * @return The final Y offset as an absolute distance from the origin.
     */
    public final int getFinalY() {
        return mScrollerY.mFinal;
    }

    /**
     * Returns how long the scroll event will take, in milliseconds.
     *
     * @return The duration of the scroll in milliseconds.
     * @unknown Pending removal once nothing depends on it
     * @deprecated OverScrollers don't necessarily have a fixed duration.
    This function will lie to the best of its ability.
     */
    @java.lang.Deprecated
    public final int getDuration() {
        return java.lang.Math.max(mScrollerX.mDuration, mScrollerY.mDuration);
    }

    /**
     * Extend the scroll animation. This allows a running animation to scroll
     * further and longer, when used with {@link #setFinalX(int)} or {@link #setFinalY(int)}.
     *
     * @param extend
     * 		Additional time to scroll in milliseconds.
     * @see #setFinalX(int)
     * @see #setFinalY(int)
     * @unknown Pending removal once nothing depends on it
     * @deprecated OverScrollers don't necessarily have a fixed duration.
    Instead of setting a new final position and extending
    the duration of an existing scroll, use startScroll
    to begin a new animation.
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    public void extendDuration(int extend) {
        mScrollerX.extendDuration(extend);
        mScrollerY.extendDuration(extend);
    }

    /**
     * Sets the final position (X) for this scroller.
     *
     * @param newX
     * 		The new X offset as an absolute distance from the origin.
     * @see #extendDuration(int)
     * @see #setFinalY(int)
     * @unknown Pending removal once nothing depends on it
     * @deprecated OverScroller's final position may change during an animation.
    Instead of setting a new final position and extending
    the duration of an existing scroll, use startScroll
    to begin a new animation.
     */
    @java.lang.Deprecated
    public void setFinalX(int newX) {
        mScrollerX.setFinalPosition(newX);
    }

    /**
     * Sets the final position (Y) for this scroller.
     *
     * @param newY
     * 		The new Y offset as an absolute distance from the origin.
     * @see #extendDuration(int)
     * @see #setFinalX(int)
     * @unknown Pending removal once nothing depends on it
     * @deprecated OverScroller's final position may change during an animation.
    Instead of setting a new final position and extending
    the duration of an existing scroll, use startScroll
    to begin a new animation.
     */
    @java.lang.Deprecated
    public void setFinalY(int newY) {
        mScrollerY.setFinalPosition(newY);
    }

    /**
     * Call this when you want to know the new location. If it returns true, the
     * animation is not yet finished.
     */
    public boolean computeScrollOffset() {
        if (isFinished()) {
            return false;
        }
        switch (mMode) {
            case android.widget.OverScroller.SCROLL_MODE :
                long time = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
                // Any scroller can be used for time, since they were started
                // together in scroll mode. We use X here.
                final long elapsedTime = time - mScrollerX.mStartTime;
                final int duration = mScrollerX.mDuration;
                if (elapsedTime < duration) {
                    final float q = mInterpolator.getInterpolation(elapsedTime / ((float) (duration)));
                    mScrollerX.updateScroll(q);
                    mScrollerY.updateScroll(q);
                } else {
                    abortAnimation();
                }
                break;
            case android.widget.OverScroller.FLING_MODE :
                if (!mScrollerX.mFinished) {
                    if (!mScrollerX.update()) {
                        if (!mScrollerX.continueWhenFinished()) {
                            mScrollerX.finish();
                        }
                    }
                }
                if (!mScrollerY.mFinished) {
                    if (!mScrollerY.update()) {
                        if (!mScrollerY.continueWhenFinished()) {
                            mScrollerY.finish();
                        }
                    }
                }
                break;
        }
        return true;
    }

    /**
     * Start scrolling by providing a starting point and the distance to travel.
     * The scroll will use the default value of 250 milliseconds for the
     * duration.
     *
     * @param startX
     * 		Starting horizontal scroll offset in pixels. Positive
     * 		numbers will scroll the content to the left.
     * @param startY
     * 		Starting vertical scroll offset in pixels. Positive numbers
     * 		will scroll the content up.
     * @param dx
     * 		Horizontal distance to travel. Positive numbers will scroll the
     * 		content to the left.
     * @param dy
     * 		Vertical distance to travel. Positive numbers will scroll the
     * 		content up.
     */
    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, android.widget.OverScroller.DEFAULT_DURATION);
    }

    /**
     * Start scrolling by providing a starting point and the distance to travel.
     *
     * @param startX
     * 		Starting horizontal scroll offset in pixels. Positive
     * 		numbers will scroll the content to the left.
     * @param startY
     * 		Starting vertical scroll offset in pixels. Positive numbers
     * 		will scroll the content up.
     * @param dx
     * 		Horizontal distance to travel. Positive numbers will scroll the
     * 		content to the left.
     * @param dy
     * 		Vertical distance to travel. Positive numbers will scroll the
     * 		content up.
     * @param duration
     * 		Duration of the scroll in milliseconds.
     */
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        mMode = android.widget.OverScroller.SCROLL_MODE;
        mScrollerX.startScroll(startX, dx, duration);
        mScrollerY.startScroll(startY, dy, duration);
    }

    /**
     * Call this when you want to 'spring back' into a valid coordinate range.
     *
     * @param startX
     * 		Starting X coordinate
     * @param startY
     * 		Starting Y coordinate
     * @param minX
     * 		Minimum valid X value
     * @param maxX
     * 		Maximum valid X value
     * @param minY
     * 		Minimum valid Y value
     * @param maxY
     * 		Minimum valid Y value
     * @return true if a springback was initiated, false if startX and startY were
    already within the valid range.
     */
    public boolean springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) {
        mMode = android.widget.OverScroller.FLING_MODE;
        // Make sure both methods are called.
        final boolean spingbackX = mScrollerX.springback(startX, minX, maxX);
        final boolean spingbackY = mScrollerY.springback(startY, minY, maxY);
        return spingbackX || spingbackY;
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
    }

    /**
     * Start scrolling based on a fling gesture. The distance traveled will
     * depend on the initial velocity of the fling.
     *
     * @param startX
     * 		Starting point of the scroll (X)
     * @param startY
     * 		Starting point of the scroll (Y)
     * @param velocityX
     * 		Initial velocity of the fling (X) measured in pixels per
     * 		second.
     * @param velocityY
     * 		Initial velocity of the fling (Y) measured in pixels per
     * 		second
     * @param minX
     * 		Minimum X value. The scroller will not scroll past this point
     * 		unless overX > 0. If overfling is allowed, it will use minX as
     * 		a springback boundary.
     * @param maxX
     * 		Maximum X value. The scroller will not scroll past this point
     * 		unless overX > 0. If overfling is allowed, it will use maxX as
     * 		a springback boundary.
     * @param minY
     * 		Minimum Y value. The scroller will not scroll past this point
     * 		unless overY > 0. If overfling is allowed, it will use minY as
     * 		a springback boundary.
     * @param maxY
     * 		Maximum Y value. The scroller will not scroll past this point
     * 		unless overY > 0. If overfling is allowed, it will use maxY as
     * 		a springback boundary.
     * @param overX
     * 		Overfling range. If > 0, horizontal overfling in either
     * 		direction will be possible.
     * @param overY
     * 		Overfling range. If > 0, vertical overfling in either
     * 		direction will be possible.
     */
    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        // Continue a scroll or fling in progress
        if (mFlywheel && (!isFinished())) {
            float oldVelocityX = mScrollerX.mCurrVelocity;
            float oldVelocityY = mScrollerY.mCurrVelocity;
            if ((java.lang.Math.signum(velocityX) == java.lang.Math.signum(oldVelocityX)) && (java.lang.Math.signum(velocityY) == java.lang.Math.signum(oldVelocityY))) {
                velocityX += oldVelocityX;
                velocityY += oldVelocityY;
            }
        }
        mMode = android.widget.OverScroller.FLING_MODE;
        mScrollerX.fling(startX, velocityX, minX, maxX, overX);
        mScrollerY.fling(startY, velocityY, minY, maxY, overY);
    }

    /**
     * Notify the scroller that we've reached a horizontal boundary.
     * Normally the information to handle this will already be known
     * when the animation is started, such as in a call to one of the
     * fling functions. However there are cases where this cannot be known
     * in advance. This function will transition the current motion and
     * animate from startX to finalX as appropriate.
     *
     * @param startX
     * 		Starting/current X position
     * @param finalX
     * 		Desired final X position
     * @param overX
     * 		Magnitude of overscroll allowed. This should be the maximum
     * 		desired distance from finalX. Absolute value - must be positive.
     */
    public void notifyHorizontalEdgeReached(int startX, int finalX, int overX) {
        mScrollerX.notifyEdgeReached(startX, finalX, overX);
    }

    /**
     * Notify the scroller that we've reached a vertical boundary.
     * Normally the information to handle this will already be known
     * when the animation is started, such as in a call to one of the
     * fling functions. However there are cases where this cannot be known
     * in advance. This function will animate a parabolic motion from
     * startY to finalY.
     *
     * @param startY
     * 		Starting/current Y position
     * @param finalY
     * 		Desired final Y position
     * @param overY
     * 		Magnitude of overscroll allowed. This should be the maximum
     * 		desired distance from finalY. Absolute value - must be positive.
     */
    public void notifyVerticalEdgeReached(int startY, int finalY, int overY) {
        mScrollerY.notifyEdgeReached(startY, finalY, overY);
    }

    /**
     * Returns whether the current Scroller is currently returning to a valid position.
     * Valid bounds were provided by the
     * {@link #fling(int, int, int, int, int, int, int, int, int, int)} method.
     *
     * One should check this value before calling
     * {@link #startScroll(int, int, int, int)} as the interpolation currently in progress
     * to restore a valid position will then be stopped. The caller has to take into account
     * the fact that the started scroll will start from an overscrolled position.
     *
     * @return true when the current position is overscrolled and in the process of
    interpolating back to a valid value.
     */
    public boolean isOverScrolled() {
        return ((!mScrollerX.mFinished) && (mScrollerX.mState != android.widget.OverScroller.SplineOverScroller.SPLINE)) || ((!mScrollerY.mFinished) && (mScrollerY.mState != android.widget.OverScroller.SplineOverScroller.SPLINE));
    }

    /**
     * Stops the animation. Contrary to {@link #forceFinished(boolean)},
     * aborting the animating causes the scroller to move to the final x and y
     * positions.
     *
     * @see #forceFinished(boolean)
     */
    public void abortAnimation() {
        mScrollerX.finish();
        mScrollerY.finish();
    }

    /**
     * Returns the time elapsed since the beginning of the scrolling.
     *
     * @return The elapsed time in milliseconds.
     * @unknown 
     */
    public int timePassed() {
        final long time = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
        final long startTime = java.lang.Math.min(mScrollerX.mStartTime, mScrollerY.mStartTime);
        return ((int) (time - startTime));
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public boolean isScrollingInDirection(float xvel, float yvel) {
        final int dx = mScrollerX.mFinal - mScrollerX.mStart;
        final int dy = mScrollerY.mFinal - mScrollerY.mStart;
        return ((!isFinished()) && (java.lang.Math.signum(xvel) == java.lang.Math.signum(dx))) && (java.lang.Math.signum(yvel) == java.lang.Math.signum(dy));
    }

    static class SplineOverScroller {
        // Initial position
        private int mStart;

        // Current position
        private int mCurrentPosition;

        // Final position
        private int mFinal;

        // Initial velocity
        private int mVelocity;

        // Current velocity
        @android.annotation.UnsupportedAppUsage
        private float mCurrVelocity;

        // Constant current deceleration
        private float mDeceleration;

        // Animation starting time, in system milliseconds
        private long mStartTime;

        // Animation duration, in milliseconds
        private int mDuration;

        // Duration to complete spline component of animation
        private int mSplineDuration;

        // Distance to travel along spline animation
        private int mSplineDistance;

        // Whether the animation is currently in progress
        private boolean mFinished;

        // The allowed overshot distance before boundary is reached.
        private int mOver;

        // Fling friction
        private float mFlingFriction = android.view.ViewConfiguration.getScrollFriction();

        // Current state of the animation.
        private int mState = android.widget.OverScroller.SplineOverScroller.SPLINE;

        // Constant gravity value, used in the deceleration phase.
        private static final float GRAVITY = 2000.0F;

        // A context-specific coefficient adjusted to physical values.
        private float mPhysicalCoeff;

        private static float DECELERATION_RATE = ((float) (java.lang.Math.log(0.78) / java.lang.Math.log(0.9)));

        private static final float INFLEXION = 0.35F;// Tension lines cross at (INFLEXION, 1)


        private static final float START_TENSION = 0.5F;

        private static final float END_TENSION = 1.0F;

        private static final float P1 = android.widget.OverScroller.SplineOverScroller.START_TENSION * android.widget.OverScroller.SplineOverScroller.INFLEXION;

        private static final float P2 = 1.0F - (android.widget.OverScroller.SplineOverScroller.END_TENSION * (1.0F - android.widget.OverScroller.SplineOverScroller.INFLEXION));

        private static final int NB_SAMPLES = 100;

        private static final float[] SPLINE_POSITION = new float[android.widget.OverScroller.SplineOverScroller.NB_SAMPLES + 1];

        private static final float[] SPLINE_TIME = new float[android.widget.OverScroller.SplineOverScroller.NB_SAMPLES + 1];

        private static final int SPLINE = 0;

        private static final int CUBIC = 1;

        private static final int BALLISTIC = 2;

        static {
            float x_min = 0.0F;
            float y_min = 0.0F;
            for (int i = 0; i < android.widget.OverScroller.SplineOverScroller.NB_SAMPLES; i++) {
                final float alpha = ((float) (i)) / android.widget.OverScroller.SplineOverScroller.NB_SAMPLES;
                float x_max = 1.0F;
                float x;
                float tx;
                float coef;
                while (true) {
                    x = x_min + ((x_max - x_min) / 2.0F);
                    coef = (3.0F * x) * (1.0F - x);
                    tx = (coef * (((1.0F - x) * android.widget.OverScroller.SplineOverScroller.P1) + (x * android.widget.OverScroller.SplineOverScroller.P2))) + ((x * x) * x);
                    if (java.lang.Math.abs(tx - alpha) < 1.0E-5)
                        break;

                    if (tx > alpha)
                        x_max = x;
                    else
                        x_min = x;

                } 
                android.widget.OverScroller.SplineOverScroller.SPLINE_POSITION[i] = (coef * (((1.0F - x) * android.widget.OverScroller.SplineOverScroller.START_TENSION) + x)) + ((x * x) * x);
                float y_max = 1.0F;
                float y;
                float dy;
                while (true) {
                    y = y_min + ((y_max - y_min) / 2.0F);
                    coef = (3.0F * y) * (1.0F - y);
                    dy = (coef * (((1.0F - y) * android.widget.OverScroller.SplineOverScroller.START_TENSION) + y)) + ((y * y) * y);
                    if (java.lang.Math.abs(dy - alpha) < 1.0E-5)
                        break;

                    if (dy > alpha)
                        y_max = y;
                    else
                        y_min = y;

                } 
                android.widget.OverScroller.SplineOverScroller.SPLINE_TIME[i] = (coef * (((1.0F - y) * android.widget.OverScroller.SplineOverScroller.P1) + (y * android.widget.OverScroller.SplineOverScroller.P2))) + ((y * y) * y);
            }
            android.widget.OverScroller.SplineOverScroller.SPLINE_POSITION[android.widget.OverScroller.SplineOverScroller.NB_SAMPLES] = android.widget.OverScroller.SplineOverScroller.SPLINE_TIME[android.widget.OverScroller.SplineOverScroller.NB_SAMPLES] = 1.0F;
        }

        void setFriction(float friction) {
            mFlingFriction = friction;
        }

        SplineOverScroller(android.content.Context context) {
            mFinished = true;
            final float ppi = context.getResources().getDisplayMetrics().density * 160.0F;
            mPhysicalCoeff = ((android.hardware.SensorManager.GRAVITY_EARTH// g (m/s^2)
             * 39.37F)// inch/meter
             * ppi) * 0.84F;// look and feel tuning

        }

        void updateScroll(float q) {
            mCurrentPosition = mStart + java.lang.Math.round(q * (mFinal - mStart));
        }

        /* Get a signed deceleration that will reduce the velocity. */
        private static float getDeceleration(int velocity) {
            return velocity > 0 ? -android.widget.OverScroller.SplineOverScroller.GRAVITY : android.widget.OverScroller.SplineOverScroller.GRAVITY;
        }

        /* Modifies mDuration to the duration it takes to get from start to newFinal using the
        spline interpolation. The previous duration was needed to get to oldFinal.
         */
        private void adjustDuration(int start, int oldFinal, int newFinal) {
            final int oldDistance = oldFinal - start;
            final int newDistance = newFinal - start;
            final float x = java.lang.Math.abs(((float) (newDistance)) / oldDistance);
            final int index = ((int) (android.widget.OverScroller.SplineOverScroller.NB_SAMPLES * x));
            if (index < android.widget.OverScroller.SplineOverScroller.NB_SAMPLES) {
                final float x_inf = ((float) (index)) / android.widget.OverScroller.SplineOverScroller.NB_SAMPLES;
                final float x_sup = ((float) (index + 1)) / android.widget.OverScroller.SplineOverScroller.NB_SAMPLES;
                final float t_inf = android.widget.OverScroller.SplineOverScroller.SPLINE_TIME[index];
                final float t_sup = android.widget.OverScroller.SplineOverScroller.SPLINE_TIME[index + 1];
                final float timeCoef = t_inf + (((x - x_inf) / (x_sup - x_inf)) * (t_sup - t_inf));
                mDuration *= timeCoef;
            }
        }

        void startScroll(int start, int distance, int duration) {
            mFinished = false;
            mCurrentPosition = mStart = start;
            mFinal = start + distance;
            mStartTime = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
            mDuration = duration;
            // Unused
            mDeceleration = 0.0F;
            mVelocity = 0;
        }

        void finish() {
            mCurrentPosition = mFinal;
            // Not reset since WebView relies on this value for fast fling.
            // TODO: restore when WebView uses the fast fling implemented in this class.
            // mCurrVelocity = 0.0f;
            mFinished = true;
        }

        void setFinalPosition(int position) {
            mFinal = position;
            mFinished = false;
        }

        void extendDuration(int extend) {
            final long time = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
            final int elapsedTime = ((int) (time - mStartTime));
            mDuration = elapsedTime + extend;
            mFinished = false;
        }

        boolean springback(int start, int min, int max) {
            mFinished = true;
            mCurrentPosition = mStart = mFinal = start;
            mVelocity = 0;
            mStartTime = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
            mDuration = 0;
            if (start < min) {
                startSpringback(start, min, 0);
            } else
                if (start > max) {
                    startSpringback(start, max, 0);
                }

            return !mFinished;
        }

        private void startSpringback(int start, int end, int velocity) {
            // mStartTime has been set
            mFinished = false;
            mState = android.widget.OverScroller.SplineOverScroller.CUBIC;
            mCurrentPosition = mStart = start;
            mFinal = end;
            final int delta = start - end;
            mDeceleration = android.widget.OverScroller.SplineOverScroller.getDeceleration(delta);
            // TODO take velocity into account
            mVelocity = -delta;// only sign is used

            mOver = java.lang.Math.abs(delta);
            mDuration = ((int) (1000.0 * java.lang.Math.sqrt(((-2.0) * delta) / mDeceleration)));
        }

        void fling(int start, int velocity, int min, int max, int over) {
            mOver = over;
            mFinished = false;
            mCurrVelocity = mVelocity = velocity;
            mDuration = mSplineDuration = 0;
            mStartTime = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
            mCurrentPosition = mStart = start;
            if ((start > max) || (start < min)) {
                startAfterEdge(start, min, max, velocity);
                return;
            }
            mState = android.widget.OverScroller.SplineOverScroller.SPLINE;
            double totalDistance = 0.0;
            if (velocity != 0) {
                mDuration = mSplineDuration = getSplineFlingDuration(velocity);
                totalDistance = getSplineFlingDistance(velocity);
            }
            mSplineDistance = ((int) (totalDistance * java.lang.Math.signum(velocity)));
            mFinal = start + mSplineDistance;
            // Clamp to a valid final position
            if (mFinal < min) {
                adjustDuration(mStart, mFinal, min);
                mFinal = min;
            }
            if (mFinal > max) {
                adjustDuration(mStart, mFinal, max);
                mFinal = max;
            }
        }

        private double getSplineDeceleration(int velocity) {
            return java.lang.Math.log((android.widget.OverScroller.SplineOverScroller.INFLEXION * java.lang.Math.abs(velocity)) / (mFlingFriction * mPhysicalCoeff));
        }

        private double getSplineFlingDistance(int velocity) {
            final double l = getSplineDeceleration(velocity);
            final double decelMinusOne = android.widget.OverScroller.SplineOverScroller.DECELERATION_RATE - 1.0;
            return (mFlingFriction * mPhysicalCoeff) * java.lang.Math.exp((android.widget.OverScroller.SplineOverScroller.DECELERATION_RATE / decelMinusOne) * l);
        }

        /* Returns the duration, expressed in milliseconds */
        private int getSplineFlingDuration(int velocity) {
            final double l = getSplineDeceleration(velocity);
            final double decelMinusOne = android.widget.OverScroller.SplineOverScroller.DECELERATION_RATE - 1.0;
            return ((int) (1000.0 * java.lang.Math.exp(l / decelMinusOne)));
        }

        private void fitOnBounceCurve(int start, int end, int velocity) {
            // Simulate a bounce that started from edge
            final float durationToApex = (-velocity) / mDeceleration;
            // The float cast below is necessary to avoid integer overflow.
            final float velocitySquared = ((float) (velocity)) * velocity;
            final float distanceToApex = (velocitySquared / 2.0F) / java.lang.Math.abs(mDeceleration);
            final float distanceToEdge = java.lang.Math.abs(end - start);
            final float totalDuration = ((float) (java.lang.Math.sqrt((2.0 * (distanceToApex + distanceToEdge)) / java.lang.Math.abs(mDeceleration))));
            mStartTime -= ((int) (1000.0F * (totalDuration - durationToApex)));
            mCurrentPosition = mStart = end;
            mVelocity = ((int) ((-mDeceleration) * totalDuration));
        }

        private void startBounceAfterEdge(int start, int end, int velocity) {
            mDeceleration = android.widget.OverScroller.SplineOverScroller.getDeceleration(velocity == 0 ? start - end : velocity);
            fitOnBounceCurve(start, end, velocity);
            onEdgeReached();
        }

        private void startAfterEdge(int start, int min, int max, int velocity) {
            if ((start > min) && (start < max)) {
                android.util.Log.e("OverScroller", "startAfterEdge called from a valid position");
                mFinished = true;
                return;
            }
            final boolean positive = start > max;
            final int edge = (positive) ? max : min;
            final int overDistance = start - edge;
            boolean keepIncreasing = (overDistance * velocity) >= 0;
            if (keepIncreasing) {
                // Will result in a bounce or a to_boundary depending on velocity.
                startBounceAfterEdge(start, edge, velocity);
            } else {
                final double totalDistance = getSplineFlingDistance(velocity);
                if (totalDistance > java.lang.Math.abs(overDistance)) {
                    fling(start, velocity, positive ? min : start, positive ? start : max, mOver);
                } else {
                    startSpringback(start, edge, velocity);
                }
            }
        }

        void notifyEdgeReached(int start, int end, int over) {
            // mState is used to detect successive notifications
            if (mState == android.widget.OverScroller.SplineOverScroller.SPLINE) {
                mOver = over;
                mStartTime = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
                // We were in fling/scroll mode before: current velocity is such that distance to
                // edge is increasing. This ensures that startAfterEdge will not start a new fling.
                startAfterEdge(start, end, end, ((int) (mCurrVelocity)));
            }
        }

        private void onEdgeReached() {
            // mStart, mVelocity and mStartTime were adjusted to their values when edge was reached.
            // The float cast below is necessary to avoid integer overflow.
            final float velocitySquared = ((float) (mVelocity)) * mVelocity;
            float distance = velocitySquared / (2.0F * java.lang.Math.abs(mDeceleration));
            final float sign = java.lang.Math.signum(mVelocity);
            if (distance > mOver) {
                // Default deceleration is not sufficient to slow us down before boundary
                mDeceleration = ((-sign) * velocitySquared) / (2.0F * mOver);
                distance = mOver;
            }
            mOver = ((int) (distance));
            mState = android.widget.OverScroller.SplineOverScroller.BALLISTIC;
            mFinal = mStart + ((int) (mVelocity > 0 ? distance : -distance));
            mDuration = -((int) ((1000.0F * mVelocity) / mDeceleration));
        }

        boolean continueWhenFinished() {
            switch (mState) {
                case android.widget.OverScroller.SplineOverScroller.SPLINE :
                    // Duration from start to null velocity
                    if (mDuration < mSplineDuration) {
                        // If the animation was clamped, we reached the edge
                        mCurrentPosition = mStart = mFinal;
                        // TODO Better compute speed when edge was reached
                        mVelocity = ((int) (mCurrVelocity));
                        mDeceleration = android.widget.OverScroller.SplineOverScroller.getDeceleration(mVelocity);
                        mStartTime += mDuration;
                        onEdgeReached();
                    } else {
                        // Normal stop, no need to continue
                        return false;
                    }
                    break;
                case android.widget.OverScroller.SplineOverScroller.BALLISTIC :
                    mStartTime += mDuration;
                    startSpringback(mFinal, mStart, 0);
                    break;
                case android.widget.OverScroller.SplineOverScroller.CUBIC :
                    return false;
            }
            update();
            return true;
        }

        /* Update the current position and velocity for current time. Returns
        true if update has been done and false if animation duration has been
        reached.
         */
        boolean update() {
            final long time = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
            final long currentTime = time - mStartTime;
            if (currentTime == 0) {
                // Skip work but report that we're still going if we have a nonzero duration.
                return mDuration > 0;
            }
            if (currentTime > mDuration) {
                return false;
            }
            double distance = 0.0;
            switch (mState) {
                case android.widget.OverScroller.SplineOverScroller.SPLINE :
                    {
                        final float t = ((float) (currentTime)) / mSplineDuration;
                        final int index = ((int) (android.widget.OverScroller.SplineOverScroller.NB_SAMPLES * t));
                        float distanceCoef = 1.0F;
                        float velocityCoef = 0.0F;
                        if (index < android.widget.OverScroller.SplineOverScroller.NB_SAMPLES) {
                            final float t_inf = ((float) (index)) / android.widget.OverScroller.SplineOverScroller.NB_SAMPLES;
                            final float t_sup = ((float) (index + 1)) / android.widget.OverScroller.SplineOverScroller.NB_SAMPLES;
                            final float d_inf = android.widget.OverScroller.SplineOverScroller.SPLINE_POSITION[index];
                            final float d_sup = android.widget.OverScroller.SplineOverScroller.SPLINE_POSITION[index + 1];
                            velocityCoef = (d_sup - d_inf) / (t_sup - t_inf);
                            distanceCoef = d_inf + ((t - t_inf) * velocityCoef);
                        }
                        distance = distanceCoef * mSplineDistance;
                        mCurrVelocity = ((velocityCoef * mSplineDistance) / mSplineDuration) * 1000.0F;
                        break;
                    }
                case android.widget.OverScroller.SplineOverScroller.BALLISTIC :
                    {
                        final float t = currentTime / 1000.0F;
                        mCurrVelocity = mVelocity + (mDeceleration * t);
                        distance = (mVelocity * t) + (((mDeceleration * t) * t) / 2.0F);
                        break;
                    }
                case android.widget.OverScroller.SplineOverScroller.CUBIC :
                    {
                        final float t = ((float) (currentTime)) / mDuration;
                        final float t2 = t * t;
                        final float sign = java.lang.Math.signum(mVelocity);
                        distance = (sign * mOver) * ((3.0F * t2) - ((2.0F * t) * t2));
                        mCurrVelocity = ((sign * mOver) * 6.0F) * ((-t) + t2);
                        break;
                    }
            }
            mCurrentPosition = mStart + ((int) (java.lang.Math.round(distance)));
            return true;
        }
    }
}

