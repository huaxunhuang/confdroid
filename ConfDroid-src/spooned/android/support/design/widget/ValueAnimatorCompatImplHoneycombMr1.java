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


class ValueAnimatorCompatImplHoneycombMr1 extends android.support.design.widget.ValueAnimatorCompat.Impl {
    private final android.animation.ValueAnimator mValueAnimator;

    ValueAnimatorCompatImplHoneycombMr1() {
        mValueAnimator = new android.animation.ValueAnimator();
    }

    @java.lang.Override
    public void start() {
        mValueAnimator.start();
    }

    @java.lang.Override
    public boolean isRunning() {
        return mValueAnimator.isRunning();
    }

    @java.lang.Override
    public void setInterpolator(android.view.animation.Interpolator interpolator) {
        mValueAnimator.setInterpolator(interpolator);
    }

    @java.lang.Override
    public void addUpdateListener(final android.support.design.widget.ValueAnimatorCompat.Impl.AnimatorUpdateListenerProxy updateListener) {
        mValueAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
            @java.lang.Override
            public void onAnimationUpdate(android.animation.ValueAnimator valueAnimator) {
                updateListener.onAnimationUpdate();
            }
        });
    }

    @java.lang.Override
    public void addListener(final android.support.design.widget.ValueAnimatorCompat.Impl.AnimatorListenerProxy listener) {
        mValueAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
            @java.lang.Override
            public void onAnimationStart(android.animation.Animator animator) {
                listener.onAnimationStart();
            }

            @java.lang.Override
            public void onAnimationEnd(android.animation.Animator animator) {
                listener.onAnimationEnd();
            }

            @java.lang.Override
            public void onAnimationCancel(android.animation.Animator animator) {
                listener.onAnimationCancel();
            }
        });
    }

    @java.lang.Override
    public void setIntValues(int from, int to) {
        mValueAnimator.setIntValues(from, to);
    }

    @java.lang.Override
    public int getAnimatedIntValue() {
        return ((int) (mValueAnimator.getAnimatedValue()));
    }

    @java.lang.Override
    public void setFloatValues(float from, float to) {
        mValueAnimator.setFloatValues(from, to);
    }

    @java.lang.Override
    public float getAnimatedFloatValue() {
        return ((float) (mValueAnimator.getAnimatedValue()));
    }

    @java.lang.Override
    public void setDuration(long duration) {
        mValueAnimator.setDuration(duration);
    }

    @java.lang.Override
    public void cancel() {
        mValueAnimator.cancel();
    }

    @java.lang.Override
    public float getAnimatedFraction() {
        return mValueAnimator.getAnimatedFraction();
    }

    @java.lang.Override
    public void end() {
        mValueAnimator.end();
    }

    @java.lang.Override
    public long getDuration() {
        return mValueAnimator.getDuration();
    }
}

