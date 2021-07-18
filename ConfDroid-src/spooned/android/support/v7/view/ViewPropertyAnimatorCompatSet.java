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
package android.support.v7.view;


/**
 * A very naive implementation of a set of
 * {@link android.support.v4.view.ViewPropertyAnimatorCompat}.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ViewPropertyAnimatorCompatSet {
    final java.util.ArrayList<android.support.v4.view.ViewPropertyAnimatorCompat> mAnimators;

    private long mDuration = -1;

    private android.view.animation.Interpolator mInterpolator;

    android.support.v4.view.ViewPropertyAnimatorListener mListener;

    private boolean mIsStarted;

    public ViewPropertyAnimatorCompatSet() {
        mAnimators = new java.util.ArrayList<android.support.v4.view.ViewPropertyAnimatorCompat>();
    }

    public android.support.v7.view.ViewPropertyAnimatorCompatSet play(android.support.v4.view.ViewPropertyAnimatorCompat animator) {
        if (!mIsStarted) {
            mAnimators.add(animator);
        }
        return this;
    }

    public android.support.v7.view.ViewPropertyAnimatorCompatSet playSequentially(android.support.v4.view.ViewPropertyAnimatorCompat anim1, android.support.v4.view.ViewPropertyAnimatorCompat anim2) {
        mAnimators.add(anim1);
        anim2.setStartDelay(anim1.getDuration());
        mAnimators.add(anim2);
        return this;
    }

    public void start() {
        if (mIsStarted)
            return;

        for (android.support.v4.view.ViewPropertyAnimatorCompat animator : mAnimators) {
            if (mDuration >= 0) {
                animator.setDuration(mDuration);
            }
            if (mInterpolator != null) {
                animator.setInterpolator(mInterpolator);
            }
            if (mListener != null) {
                animator.setListener(mProxyListener);
            }
            animator.start();
        }
        mIsStarted = true;
    }

    void onAnimationsEnded() {
        mIsStarted = false;
    }

    public void cancel() {
        if (!mIsStarted) {
            return;
        }
        for (android.support.v4.view.ViewPropertyAnimatorCompat animator : mAnimators) {
            animator.cancel();
        }
        mIsStarted = false;
    }

    public android.support.v7.view.ViewPropertyAnimatorCompatSet setDuration(long duration) {
        if (!mIsStarted) {
            mDuration = duration;
        }
        return this;
    }

    public android.support.v7.view.ViewPropertyAnimatorCompatSet setInterpolator(android.view.animation.Interpolator interpolator) {
        if (!mIsStarted) {
            mInterpolator = interpolator;
        }
        return this;
    }

    public android.support.v7.view.ViewPropertyAnimatorCompatSet setListener(android.support.v4.view.ViewPropertyAnimatorListener listener) {
        if (!mIsStarted) {
            mListener = listener;
        }
        return this;
    }

    private final android.support.v4.view.ViewPropertyAnimatorListenerAdapter mProxyListener = new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
        private boolean mProxyStarted = false;

        private int mProxyEndCount = 0;

        @java.lang.Override
        public void onAnimationStart(android.view.View view) {
            if (mProxyStarted) {
                return;
            }
            mProxyStarted = true;
            if (mListener != null) {
                mListener.onAnimationStart(null);
            }
        }

        void onEnd() {
            mProxyEndCount = 0;
            mProxyStarted = false;
            onAnimationsEnded();
        }

        @java.lang.Override
        public void onAnimationEnd(android.view.View view) {
            if ((++mProxyEndCount) == mAnimators.size()) {
                if (mListener != null) {
                    mListener.onAnimationEnd(null);
                }
                onEnd();
            }
        }
    };
}

