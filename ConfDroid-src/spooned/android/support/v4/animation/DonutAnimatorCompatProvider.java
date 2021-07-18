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
 * Provides similar functionality to Animators on platforms prior to Honeycomb.
 * <p>
 * This is not a fully implemented API which is why it is not public.
 *
 * @unknown 
 */
class DonutAnimatorCompatProvider implements android.support.v4.animation.AnimatorProvider {
    @java.lang.Override
    public android.support.v4.animation.ValueAnimatorCompat emptyValueAnimator() {
        return new android.support.v4.animation.DonutAnimatorCompatProvider.DonutFloatValueAnimator();
    }

    private static class DonutFloatValueAnimator implements android.support.v4.animation.ValueAnimatorCompat {
        java.util.List<android.support.v4.animation.AnimatorListenerCompat> mListeners = new java.util.ArrayList<android.support.v4.animation.AnimatorListenerCompat>();

        java.util.List<android.support.v4.animation.AnimatorUpdateListenerCompat> mUpdateListeners = new java.util.ArrayList<android.support.v4.animation.AnimatorUpdateListenerCompat>();

        android.view.View mTarget;

        private long mStartTime;

        private long mDuration = 200;

        private float mFraction = 0.0F;

        private boolean mStarted = false;

        private boolean mEnded = false;

        public DonutFloatValueAnimator() {
        }

        private java.lang.Runnable mLoopRunnable = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                long dt = getTime() - mStartTime;
                float fraction = (dt * 1.0F) / mDuration;
                if ((fraction > 1.0F) || (mTarget.getParent() == null)) {
                    fraction = 1.0F;
                }
                mFraction = fraction;
                notifyUpdateListeners();
                if (mFraction >= 1.0F) {
                    dispatchEnd();
                } else {
                    mTarget.postDelayed(mLoopRunnable, 16);
                }
            }
        };

        private void notifyUpdateListeners() {
            for (int i = mUpdateListeners.size() - 1; i >= 0; i--) {
                mUpdateListeners.get(i).onAnimationUpdate(this);
            }
        }

        @java.lang.Override
        public void setTarget(android.view.View view) {
            mTarget = view;
        }

        @java.lang.Override
        public void addListener(android.support.v4.animation.AnimatorListenerCompat listener) {
            mListeners.add(listener);
        }

        @java.lang.Override
        public void setDuration(long duration) {
            if (!mStarted) {
                mDuration = duration;
            }
        }

        @java.lang.Override
        public void start() {
            if (mStarted) {
                return;
            }
            mStarted = true;
            dispatchStart();
            mFraction = 0.0F;
            mStartTime = getTime();
            mTarget.postDelayed(mLoopRunnable, 16);
        }

        private long getTime() {
            return mTarget.getDrawingTime();
        }

        private void dispatchStart() {
            for (int i = mListeners.size() - 1; i >= 0; i--) {
                mListeners.get(i).onAnimationStart(this);
            }
        }

        private void dispatchEnd() {
            for (int i = mListeners.size() - 1; i >= 0; i--) {
                mListeners.get(i).onAnimationEnd(this);
            }
        }

        private void dispatchCancel() {
            for (int i = mListeners.size() - 1; i >= 0; i--) {
                mListeners.get(i).onAnimationCancel(this);
            }
        }

        @java.lang.Override
        public void cancel() {
            if (mEnded) {
                return;
            }
            mEnded = true;
            if (mStarted) {
                dispatchCancel();
            }
            dispatchEnd();
        }

        @java.lang.Override
        public void addUpdateListener(android.support.v4.animation.AnimatorUpdateListenerCompat animatorUpdateListener) {
            mUpdateListeners.add(animatorUpdateListener);
        }

        @java.lang.Override
        public float getAnimatedFraction() {
            return mFraction;
        }
    }

    @java.lang.Override
    public void clearInterpolator(android.view.View view) {
    }
}

