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
 * A 'fake' ValueAnimator implementation which uses a Runnable.
 */
class ValueAnimatorCompatImplEclairMr1 extends android.support.design.widget.ValueAnimatorCompat.Impl {
    private static final int HANDLER_DELAY = 10;

    private static final int DEFAULT_DURATION = 200;

    private static final android.os.Handler sHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    private long mStartTime;

    private boolean mIsRunning;

    private final int[] mIntValues = new int[2];

    private final float[] mFloatValues = new float[2];

    private long mDuration = android.support.design.widget.ValueAnimatorCompatImplEclairMr1.DEFAULT_DURATION;

    private android.view.animation.Interpolator mInterpolator;

    private android.support.design.widget.ValueAnimatorCompat.Impl.AnimatorListenerProxy mListener;

    private android.support.design.widget.ValueAnimatorCompat.Impl.AnimatorUpdateListenerProxy mUpdateListener;

    private float mAnimatedFraction;

    @java.lang.Override
    public void start() {
        if (mIsRunning) {
            // If we're already running, ignore
            return;
        }
        if (mInterpolator == null) {
            mInterpolator = new android.view.animation.AccelerateDecelerateInterpolator();
        }
        mStartTime = android.os.SystemClock.uptimeMillis();
        mIsRunning = true;
        // Reset the animated fraction
        mAnimatedFraction = 0.0F;
        if (mListener != null) {
            mListener.onAnimationStart();
        }
        android.support.design.widget.ValueAnimatorCompatImplEclairMr1.sHandler.postDelayed(mRunnable, android.support.design.widget.ValueAnimatorCompatImplEclairMr1.HANDLER_DELAY);
    }

    @java.lang.Override
    public boolean isRunning() {
        return mIsRunning;
    }

    @java.lang.Override
    public void setInterpolator(android.view.animation.Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @java.lang.Override
    public void setListener(android.support.design.widget.ValueAnimatorCompat.Impl.AnimatorListenerProxy listener) {
        mListener = listener;
    }

    @java.lang.Override
    public void setUpdateListener(android.support.design.widget.ValueAnimatorCompat.Impl.AnimatorUpdateListenerProxy updateListener) {
        mUpdateListener = updateListener;
    }

    @java.lang.Override
    public void setIntValues(int from, int to) {
        mIntValues[0] = from;
        mIntValues[1] = to;
    }

    @java.lang.Override
    public int getAnimatedIntValue() {
        return android.support.design.widget.AnimationUtils.lerp(mIntValues[0], mIntValues[1], getAnimatedFraction());
    }

    @java.lang.Override
    public void setFloatValues(float from, float to) {
        mFloatValues[0] = from;
        mFloatValues[1] = to;
    }

    @java.lang.Override
    public float getAnimatedFloatValue() {
        return android.support.design.widget.AnimationUtils.lerp(mFloatValues[0], mFloatValues[1], getAnimatedFraction());
    }

    @java.lang.Override
    public void setDuration(long duration) {
        mDuration = duration;
    }

    @java.lang.Override
    public void cancel() {
        mIsRunning = false;
        android.support.design.widget.ValueAnimatorCompatImplEclairMr1.sHandler.removeCallbacks(mRunnable);
        if (mListener != null) {
            mListener.onAnimationCancel();
            mListener.onAnimationEnd();
        }
    }

    @java.lang.Override
    public float getAnimatedFraction() {
        return mAnimatedFraction;
    }

    @java.lang.Override
    public void end() {
        if (mIsRunning) {
            mIsRunning = false;
            android.support.design.widget.ValueAnimatorCompatImplEclairMr1.sHandler.removeCallbacks(mRunnable);
            // Set our animated fraction to 1
            mAnimatedFraction = 1.0F;
            if (mUpdateListener != null) {
                mUpdateListener.onAnimationUpdate();
            }
            if (mListener != null) {
                mListener.onAnimationEnd();
            }
        }
    }

    @java.lang.Override
    public long getDuration() {
        return mDuration;
    }

    private void update() {
        if (mIsRunning) {
            // Update the animated fraction
            final long elapsed = android.os.SystemClock.uptimeMillis() - mStartTime;
            final float linearFraction = android.support.design.widget.MathUtils.constrain(elapsed / ((float) (mDuration)), 0.0F, 1.0F);
            mAnimatedFraction = (mInterpolator != null) ? mInterpolator.getInterpolation(linearFraction) : linearFraction;
            // If we're running, dispatch tp the listener
            if (mUpdateListener != null) {
                mUpdateListener.onAnimationUpdate();
            }
            // Check to see if we've passed the animation duration
            if (android.os.SystemClock.uptimeMillis() >= (mStartTime + mDuration)) {
                mIsRunning = false;
                if (mListener != null) {
                    mListener.onAnimationEnd();
                }
            }
        }
        if (mIsRunning) {
            // If we're still running, post another delayed runnable
            android.support.design.widget.ValueAnimatorCompatImplEclairMr1.sHandler.postDelayed(mRunnable, android.support.design.widget.ValueAnimatorCompatImplEclairMr1.HANDLER_DELAY);
        }
    }

    private final java.lang.Runnable mRunnable = new java.lang.Runnable() {
        public void run() {
            update();
        }
    };
}

