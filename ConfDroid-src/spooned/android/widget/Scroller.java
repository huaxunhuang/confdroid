/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * <p>This class encapsulates scrolling. You can use scrollers ({@link Scroller}
 * or {@link OverScroller}) to collect the data you need to produce a scrolling
 * animation&mdash;for example, in response to a fling gesture. Scrollers track
 * scroll offsets for you over time, but they don't automatically apply those
 * positions to your view. It's your responsibility to get and apply new
 * coordinates at a rate that will make the scrolling animation look smooth.</p>
 *
 * <p>Here is a simple example:</p>
 *
 * <pre> private Scroller mScroller = new Scroller(context);
 * ...
 * public void zoomIn() {
 *     // Revert any animation currently in progress
 *     mScroller.forceFinished(true);
 *     // Start scrolling by providing a starting point and
 *     // the distance to travel
 *     mScroller.startScroll(0, 0, 100, 0);
 *     // Invalidate to request a redraw
 *     invalidate();
 * }</pre>
 *
 * <p>To track the changing positions of the x/y coordinates, use
 * {@link #computeScrollOffset}. The method returns a boolean to indicate
 * whether the scroller is finished. If it isn't, it means that a fling or
 * programmatic pan operation is still in progress. You can use this method to
 * find the current offsets of the x and y coordinates, for example:</p>
 *
 * <pre>if (mScroller.computeScrollOffset()) {
 *     // Get current x and y positions
 *     int currX = mScroller.getCurrX();
 *     int currY = mScroller.getCurrY();
 *    ...
 * }</pre>
 */
public class Scroller {
    @android.annotation.UnsupportedAppUsage
    private final android.view.animation.Interpolator mInterpolator;

    private int mMode;

    private int mStartX;

    private int mStartY;

    private int mFinalX;

    private int mFinalY;

    private int mMinX;

    private int mMaxX;

    private int mMinY;

    private int mMaxY;

    private int mCurrX;

    private int mCurrY;

    private long mStartTime;

    @android.annotation.UnsupportedAppUsage
    private int mDuration;

    private float mDurationReciprocal;

    private float mDeltaX;

    private float mDeltaY;

    private boolean mFinished;

    private boolean mFlywheel;

    private float mVelocity;

    private float mCurrVelocity;

    private int mDistance;

    private float mFlingFriction = android.view.ViewConfiguration.getScrollFriction();

    private static final int DEFAULT_DURATION = 250;

    private static final int SCROLL_MODE = 0;

    private static final int FLING_MODE = 1;

    @android.annotation.UnsupportedAppUsage
    private static float DECELERATION_RATE = ((float) (java.lang.Math.log(0.78) / java.lang.Math.log(0.9)));

    @android.annotation.UnsupportedAppUsage
    private static final float INFLEXION = 0.35F;// Tension lines cross at (INFLEXION, 1)


    private static final float START_TENSION = 0.5F;

    private static final float END_TENSION = 1.0F;

    private static final float P1 = android.widget.Scroller.START_TENSION * android.widget.Scroller.INFLEXION;

    private static final float P2 = 1.0F - (android.widget.Scroller.END_TENSION * (1.0F - android.widget.Scroller.INFLEXION));

    private static final int NB_SAMPLES = 100;

    private static final float[] SPLINE_POSITION = new float[android.widget.Scroller.NB_SAMPLES + 1];

    private static final float[] SPLINE_TIME = new float[android.widget.Scroller.NB_SAMPLES + 1];

    @android.annotation.UnsupportedAppUsage
    private float mDeceleration;

    private final float mPpi;

    // A context-specific coefficient adjusted to physical values.
    @android.annotation.UnsupportedAppUsage
    private float mPhysicalCoeff;

    static {
        float x_min = 0.0F;
        float y_min = 0.0F;
        for (int i = 0; i < android.widget.Scroller.NB_SAMPLES; i++) {
            final float alpha = ((float) (i)) / android.widget.Scroller.NB_SAMPLES;
            float x_max = 1.0F;
            float x;
            float tx;
            float coef;
            while (true) {
                x = x_min + ((x_max - x_min) / 2.0F);
                coef = (3.0F * x) * (1.0F - x);
                tx = (coef * (((1.0F - x) * android.widget.Scroller.P1) + (x * android.widget.Scroller.P2))) + ((x * x) * x);
                if (java.lang.Math.abs(tx - alpha) < 1.0E-5)
                    break;

                if (tx > alpha)
                    x_max = x;
                else
                    x_min = x;

            } 
            android.widget.Scroller.SPLINE_POSITION[i] = (coef * (((1.0F - x) * android.widget.Scroller.START_TENSION) + x)) + ((x * x) * x);
            float y_max = 1.0F;
            float y;
            float dy;
            while (true) {
                y = y_min + ((y_max - y_min) / 2.0F);
                coef = (3.0F * y) * (1.0F - y);
                dy = (coef * (((1.0F - y) * android.widget.Scroller.START_TENSION) + y)) + ((y * y) * y);
                if (java.lang.Math.abs(dy - alpha) < 1.0E-5)
                    break;

                if (dy > alpha)
                    y_max = y;
                else
                    y_min = y;

            } 
            android.widget.Scroller.SPLINE_TIME[i] = (coef * (((1.0F - y) * android.widget.Scroller.P1) + (y * android.widget.Scroller.P2))) + ((y * y) * y);
        }
        android.widget.Scroller.SPLINE_POSITION[android.widget.Scroller.NB_SAMPLES] = android.widget.Scroller.SPLINE_TIME[android.widget.Scroller.NB_SAMPLES] = 1.0F;
    }

    /**
     * Create a Scroller with the default duration and interpolator.
     */
    public Scroller(android.content.Context context) {
        this(context, null);
    }

    /**
     * Create a Scroller with the specified interpolator. If the interpolator is
     * null, the default (viscous) interpolator will be used. "Flywheel" behavior will
     * be in effect for apps targeting Honeycomb or newer.
     */
    public Scroller(android.content.Context context, android.view.animation.Interpolator interpolator) {
        this(context, interpolator, context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.HONEYCOMB);
    }

    /**
     * Create a Scroller with the specified interpolator. If the interpolator is
     * null, the default (viscous) interpolator will be used. Specify whether or
     * not to support progressive "flywheel" behavior in flinging.
     */
    public Scroller(android.content.Context context, android.view.animation.Interpolator interpolator, boolean flywheel) {
        mFinished = true;
        if (interpolator == null) {
            mInterpolator = new android.widget.Scroller.ViscousFluidInterpolator();
        } else {
            mInterpolator = interpolator;
        }
        mPpi = context.getResources().getDisplayMetrics().density * 160.0F;
        mDeceleration = computeDeceleration(android.view.ViewConfiguration.getScrollFriction());
        mFlywheel = flywheel;
        mPhysicalCoeff = computeDeceleration(0.84F);// look and feel tuning

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
        mDeceleration = computeDeceleration(friction);
        mFlingFriction = friction;
    }

    private float computeDeceleration(float friction) {
        return ((android.hardware.SensorManager.GRAVITY_EARTH// g (m/s^2)
         * 39.37F)// inch/meter
         * mPpi)// pixels per inch
         * friction;
    }

    /**
     * Returns whether the scroller has finished scrolling.
     *
     * @return True if the scroller has finished scrolling, false otherwise.
     */
    public final boolean isFinished() {
        return mFinished;
    }

    /**
     * Force the finished field to a particular value.
     *
     * @param finished
     * 		The new finished value.
     */
    public final void forceFinished(boolean finished) {
        mFinished = finished;
    }

    /**
     * Returns how long the scroll event will take, in milliseconds.
     *
     * @return The duration of the scroll in milliseconds.
     */
    public final int getDuration() {
        return mDuration;
    }

    /**
     * Returns the current X offset in the scroll.
     *
     * @return The new X offset as an absolute distance from the origin.
     */
    public final int getCurrX() {
        return mCurrX;
    }

    /**
     * Returns the current Y offset in the scroll.
     *
     * @return The new Y offset as an absolute distance from the origin.
     */
    public final int getCurrY() {
        return mCurrY;
    }

    /**
     * Returns the current velocity.
     *
     * @return The original velocity less the deceleration. Result may be
    negative.
     */
    public float getCurrVelocity() {
        return mMode == android.widget.Scroller.FLING_MODE ? mCurrVelocity : mVelocity - ((mDeceleration * timePassed()) / 2000.0F);
    }

    /**
     * Returns the start X offset in the scroll.
     *
     * @return The start X offset as an absolute distance from the origin.
     */
    public final int getStartX() {
        return mStartX;
    }

    /**
     * Returns the start Y offset in the scroll.
     *
     * @return The start Y offset as an absolute distance from the origin.
     */
    public final int getStartY() {
        return mStartY;
    }

    /**
     * Returns where the scroll will end. Valid only for "fling" scrolls.
     *
     * @return The final X offset as an absolute distance from the origin.
     */
    public final int getFinalX() {
        return mFinalX;
    }

    /**
     * Returns where the scroll will end. Valid only for "fling" scrolls.
     *
     * @return The final Y offset as an absolute distance from the origin.
     */
    public final int getFinalY() {
        return mFinalY;
    }

    /**
     * Call this when you want to know the new location.  If it returns true,
     * the animation is not yet finished.
     */
    public boolean computeScrollOffset() {
        if (mFinished) {
            return false;
        }
        int timePassed = ((int) (android.view.animation.AnimationUtils.currentAnimationTimeMillis() - mStartTime));
        if (timePassed < mDuration) {
            switch (mMode) {
                case android.widget.Scroller.SCROLL_MODE :
                    final float x = mInterpolator.getInterpolation(timePassed * mDurationReciprocal);
                    mCurrX = mStartX + java.lang.Math.round(x * mDeltaX);
                    mCurrY = mStartY + java.lang.Math.round(x * mDeltaY);
                    break;
                case android.widget.Scroller.FLING_MODE :
                    final float t = ((float) (timePassed)) / mDuration;
                    final int index = ((int) (android.widget.Scroller.NB_SAMPLES * t));
                    float distanceCoef = 1.0F;
                    float velocityCoef = 0.0F;
                    if (index < android.widget.Scroller.NB_SAMPLES) {
                        final float t_inf = ((float) (index)) / android.widget.Scroller.NB_SAMPLES;
                        final float t_sup = ((float) (index + 1)) / android.widget.Scroller.NB_SAMPLES;
                        final float d_inf = android.widget.Scroller.SPLINE_POSITION[index];
                        final float d_sup = android.widget.Scroller.SPLINE_POSITION[index + 1];
                        velocityCoef = (d_sup - d_inf) / (t_sup - t_inf);
                        distanceCoef = d_inf + ((t - t_inf) * velocityCoef);
                    }
                    mCurrVelocity = ((velocityCoef * mDistance) / mDuration) * 1000.0F;
                    mCurrX = mStartX + java.lang.Math.round(distanceCoef * (mFinalX - mStartX));
                    // Pin to mMinX <= mCurrX <= mMaxX
                    mCurrX = java.lang.Math.min(mCurrX, mMaxX);
                    mCurrX = java.lang.Math.max(mCurrX, mMinX);
                    mCurrY = mStartY + java.lang.Math.round(distanceCoef * (mFinalY - mStartY));
                    // Pin to mMinY <= mCurrY <= mMaxY
                    mCurrY = java.lang.Math.min(mCurrY, mMaxY);
                    mCurrY = java.lang.Math.max(mCurrY, mMinY);
                    if ((mCurrX == mFinalX) && (mCurrY == mFinalY)) {
                        mFinished = true;
                    }
                    break;
            }
        } else {
            mCurrX = mFinalX;
            mCurrY = mFinalY;
            mFinished = true;
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
        startScroll(startX, startY, dx, dy, android.widget.Scroller.DEFAULT_DURATION);
    }

    /**
     * Start scrolling by providing a starting point, the distance to travel,
     * and the duration of the scroll.
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
        mMode = android.widget.Scroller.SCROLL_MODE;
        mFinished = false;
        mDuration = duration;
        mStartTime = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
        mStartX = startX;
        mStartY = startY;
        mFinalX = startX + dx;
        mFinalY = startY + dy;
        mDeltaX = dx;
        mDeltaY = dy;
        mDurationReciprocal = 1.0F / ((float) (mDuration));
    }

    /**
     * Start scrolling based on a fling gesture. The distance travelled will
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
     * 		Minimum X value. The scroller will not scroll past this
     * 		point.
     * @param maxX
     * 		Maximum X value. The scroller will not scroll past this
     * 		point.
     * @param minY
     * 		Minimum Y value. The scroller will not scroll past this
     * 		point.
     * @param maxY
     * 		Maximum Y value. The scroller will not scroll past this
     * 		point.
     */
    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        // Continue a scroll or fling in progress
        if (mFlywheel && (!mFinished)) {
            float oldVel = getCurrVelocity();
            float dx = ((float) (mFinalX - mStartX));
            float dy = ((float) (mFinalY - mStartY));
            float hyp = ((float) (java.lang.Math.hypot(dx, dy)));
            float ndx = dx / hyp;
            float ndy = dy / hyp;
            float oldVelocityX = ndx * oldVel;
            float oldVelocityY = ndy * oldVel;
            if ((java.lang.Math.signum(velocityX) == java.lang.Math.signum(oldVelocityX)) && (java.lang.Math.signum(velocityY) == java.lang.Math.signum(oldVelocityY))) {
                velocityX += oldVelocityX;
                velocityY += oldVelocityY;
            }
        }
        mMode = android.widget.Scroller.FLING_MODE;
        mFinished = false;
        float velocity = ((float) (java.lang.Math.hypot(velocityX, velocityY)));
        mVelocity = velocity;
        mDuration = getSplineFlingDuration(velocity);
        mStartTime = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
        mStartX = startX;
        mStartY = startY;
        float coeffX = (velocity == 0) ? 1.0F : velocityX / velocity;
        float coeffY = (velocity == 0) ? 1.0F : velocityY / velocity;
        double totalDistance = getSplineFlingDistance(velocity);
        mDistance = ((int) (totalDistance * java.lang.Math.signum(velocity)));
        mMinX = minX;
        mMaxX = maxX;
        mMinY = minY;
        mMaxY = maxY;
        mFinalX = startX + ((int) (java.lang.Math.round(totalDistance * coeffX)));
        // Pin to mMinX <= mFinalX <= mMaxX
        mFinalX = java.lang.Math.min(mFinalX, mMaxX);
        mFinalX = java.lang.Math.max(mFinalX, mMinX);
        mFinalY = startY + ((int) (java.lang.Math.round(totalDistance * coeffY)));
        // Pin to mMinY <= mFinalY <= mMaxY
        mFinalY = java.lang.Math.min(mFinalY, mMaxY);
        mFinalY = java.lang.Math.max(mFinalY, mMinY);
    }

    private double getSplineDeceleration(float velocity) {
        return java.lang.Math.log((android.widget.Scroller.INFLEXION * java.lang.Math.abs(velocity)) / (mFlingFriction * mPhysicalCoeff));
    }

    private int getSplineFlingDuration(float velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = android.widget.Scroller.DECELERATION_RATE - 1.0;
        return ((int) (1000.0 * java.lang.Math.exp(l / decelMinusOne)));
    }

    private double getSplineFlingDistance(float velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = android.widget.Scroller.DECELERATION_RATE - 1.0;
        return (mFlingFriction * mPhysicalCoeff) * java.lang.Math.exp((android.widget.Scroller.DECELERATION_RATE / decelMinusOne) * l);
    }

    /**
     * Stops the animation. Contrary to {@link #forceFinished(boolean)},
     * aborting the animating cause the scroller to move to the final x and y
     * position
     *
     * @see #forceFinished(boolean)
     */
    public void abortAnimation() {
        mCurrX = mFinalX;
        mCurrY = mFinalY;
        mFinished = true;
    }

    /**
     * Extend the scroll animation. This allows a running animation to scroll
     * further and longer, when used with {@link #setFinalX(int)} or {@link #setFinalY(int)}.
     *
     * @param extend
     * 		Additional time to scroll in milliseconds.
     * @see #setFinalX(int)
     * @see #setFinalY(int)
     */
    public void extendDuration(int extend) {
        int passed = timePassed();
        mDuration = passed + extend;
        mDurationReciprocal = 1.0F / mDuration;
        mFinished = false;
    }

    /**
     * Returns the time elapsed since the beginning of the scrolling.
     *
     * @return The elapsed time in milliseconds.
     */
    public int timePassed() {
        return ((int) (android.view.animation.AnimationUtils.currentAnimationTimeMillis() - mStartTime));
    }

    /**
     * Sets the final position (X) for this scroller.
     *
     * @param newX
     * 		The new X offset as an absolute distance from the origin.
     * @see #extendDuration(int)
     * @see #setFinalY(int)
     */
    public void setFinalX(int newX) {
        mFinalX = newX;
        mDeltaX = mFinalX - mStartX;
        mFinished = false;
    }

    /**
     * Sets the final position (Y) for this scroller.
     *
     * @param newY
     * 		The new Y offset as an absolute distance from the origin.
     * @see #extendDuration(int)
     * @see #setFinalX(int)
     */
    public void setFinalY(int newY) {
        mFinalY = newY;
        mDeltaY = mFinalY - mStartY;
        mFinished = false;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isScrollingInDirection(float xvel, float yvel) {
        return ((!mFinished) && (java.lang.Math.signum(xvel) == java.lang.Math.signum(mFinalX - mStartX))) && (java.lang.Math.signum(yvel) == java.lang.Math.signum(mFinalY - mStartY));
    }

    static class ViscousFluidInterpolator implements android.view.animation.Interpolator {
        /**
         * Controls the viscous fluid effect (how much of it).
         */
        private static final float VISCOUS_FLUID_SCALE = 8.0F;

        private static final float VISCOUS_FLUID_NORMALIZE;

        private static final float VISCOUS_FLUID_OFFSET;

        static {
            // must be set to 1.0 (used in viscousFluid())
            VISCOUS_FLUID_NORMALIZE = 1.0F / android.widget.Scroller.ViscousFluidInterpolator.viscousFluid(1.0F);
            // account for very small floating-point error
            VISCOUS_FLUID_OFFSET = 1.0F - (android.widget.Scroller.ViscousFluidInterpolator.VISCOUS_FLUID_NORMALIZE * android.widget.Scroller.ViscousFluidInterpolator.viscousFluid(1.0F));
        }

        private static float viscousFluid(float x) {
            x *= android.widget.Scroller.ViscousFluidInterpolator.VISCOUS_FLUID_SCALE;
            if (x < 1.0F) {
                x -= 1.0F - ((float) (java.lang.Math.exp(-x)));
            } else {
                float start = 0.36787945F;// 1/e == exp(-1)

                x = 1.0F - ((float) (java.lang.Math.exp(1.0F - x)));
                x = start + (x * (1.0F - start));
            }
            return x;
        }

        @java.lang.Override
        public float getInterpolation(float input) {
            final float interpolated = android.widget.Scroller.ViscousFluidInterpolator.VISCOUS_FLUID_NORMALIZE * android.widget.Scroller.ViscousFluidInterpolator.viscousFluid(input);
            if (interpolated > 0) {
                return interpolated + android.widget.Scroller.ViscousFluidInterpolator.VISCOUS_FLUID_OFFSET;
            }
            return interpolated;
        }
    }
}

