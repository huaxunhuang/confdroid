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
package android.support.v4.animation;


/**
 * Uses framework Animators to provide ValueAnimatorCompat interface.
 * <p>
 * This is not a fully implemented API which is why it is not public.
 */
class HoneycombMr1AnimatorCompatProvider implements android.support.v4.animation.AnimatorProvider {
    private android.animation.TimeInterpolator mDefaultInterpolator;

    @java.lang.Override
    public android.support.v4.animation.ValueAnimatorCompat emptyValueAnimator() {
        return new android.support.v4.animation.HoneycombMr1AnimatorCompatProvider.HoneycombValueAnimatorCompat(android.animation.ValueAnimator.ofFloat(0.0F, 1.0F));
    }

    static class HoneycombValueAnimatorCompat implements android.support.v4.animation.ValueAnimatorCompat {
        final android.animation.Animator mWrapped;

        public HoneycombValueAnimatorCompat(android.animation.Animator wrapped) {
            mWrapped = wrapped;
        }

        @java.lang.Override
        public void setTarget(android.view.View view) {
            mWrapped.setTarget(view);
        }

        @java.lang.Override
        public void addListener(android.support.v4.animation.AnimatorListenerCompat listener) {
            mWrapped.addListener(new android.support.v4.animation.HoneycombMr1AnimatorCompatProvider.AnimatorListenerCompatWrapper(listener, this));
        }

        @java.lang.Override
        public void setDuration(long duration) {
            mWrapped.setDuration(duration);
        }

        @java.lang.Override
        public void start() {
            mWrapped.start();
        }

        @java.lang.Override
        public void cancel() {
            mWrapped.cancel();
        }

        @java.lang.Override
        public void addUpdateListener(final android.support.v4.animation.AnimatorUpdateListenerCompat animatorUpdateListener) {
            if (mWrapped instanceof android.animation.ValueAnimator) {
                ((android.animation.ValueAnimator) (mWrapped)).addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
                    @java.lang.Override
                    public void onAnimationUpdate(android.animation.ValueAnimator animation) {
                        animatorUpdateListener.onAnimationUpdate(android.support.v4.animation.HoneycombMr1AnimatorCompatProvider.HoneycombValueAnimatorCompat.this);
                    }
                });
            }
        }

        @java.lang.Override
        public float getAnimatedFraction() {
            return ((android.animation.ValueAnimator) (mWrapped)).getAnimatedFraction();
        }
    }

    static class AnimatorListenerCompatWrapper implements android.animation.Animator.AnimatorListener {
        final android.support.v4.animation.AnimatorListenerCompat mWrapped;

        final android.support.v4.animation.ValueAnimatorCompat mValueAnimatorCompat;

        public AnimatorListenerCompatWrapper(android.support.v4.animation.AnimatorListenerCompat wrapped, android.support.v4.animation.ValueAnimatorCompat valueAnimatorCompat) {
            mWrapped = wrapped;
            mValueAnimatorCompat = valueAnimatorCompat;
        }

        @java.lang.Override
        public void onAnimationStart(android.animation.Animator animation) {
            mWrapped.onAnimationStart(mValueAnimatorCompat);
        }

        @java.lang.Override
        public void onAnimationEnd(android.animation.Animator animation) {
            mWrapped.onAnimationEnd(mValueAnimatorCompat);
        }

        @java.lang.Override
        public void onAnimationCancel(android.animation.Animator animation) {
            mWrapped.onAnimationCancel(mValueAnimatorCompat);
        }

        @java.lang.Override
        public void onAnimationRepeat(android.animation.Animator animation) {
            mWrapped.onAnimationRepeat(mValueAnimatorCompat);
        }
    }

    @java.lang.Override
    public void clearInterpolator(android.view.View view) {
        if (mDefaultInterpolator == null) {
            mDefaultInterpolator = new android.animation.ValueAnimator().getInterpolator();
        }
        view.animate().setInterpolator(mDefaultInterpolator);
    }
}

