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
 * This class offers a very small subset of {@code ValueAnimator}'s API, but works pre-v11 too.
 * <p>
 * You shouldn't not instantiate this directly. Instead use {@code ViewUtils.createAnimator()}.
 */
class ValueAnimatorCompat {
    interface AnimatorUpdateListener {
        /**
         * <p>Notifies the occurrence of another frame of the animation.</p>
         *
         * @param animator
         * 		The animation which was repeated.
         */
        void onAnimationUpdate(android.support.design.widget.ValueAnimatorCompat animator);
    }

    /**
     * An animation listener receives notifications from an animation.
     * Notifications indicate animation related events, such as the end or the
     * repetition of the animation.
     */
    interface AnimatorListener {
        /**
         * <p>Notifies the start of the animation.</p>
         *
         * @param animator
         * 		The started animation.
         */
        void onAnimationStart(android.support.design.widget.ValueAnimatorCompat animator);

        /**
         * <p>Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         *
         * @param animator
         * 		The animation which reached its end.
         */
        void onAnimationEnd(android.support.design.widget.ValueAnimatorCompat animator);

        /**
         * <p>Notifies the cancellation of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         *
         * @param animator
         * 		The animation which was canceled.
         */
        void onAnimationCancel(android.support.design.widget.ValueAnimatorCompat animator);
    }

    static class AnimatorListenerAdapter implements android.support.design.widget.ValueAnimatorCompat.AnimatorListener {
        @java.lang.Override
        public void onAnimationStart(android.support.design.widget.ValueAnimatorCompat animator) {
        }

        @java.lang.Override
        public void onAnimationEnd(android.support.design.widget.ValueAnimatorCompat animator) {
        }

        @java.lang.Override
        public void onAnimationCancel(android.support.design.widget.ValueAnimatorCompat animator) {
        }
    }

    interface Creator {
        @android.support.annotation.NonNull
        android.support.design.widget.ValueAnimatorCompat createAnimator();
    }

    static abstract class Impl {
        interface AnimatorUpdateListenerProxy {
            void onAnimationUpdate();
        }

        interface AnimatorListenerProxy {
            void onAnimationStart();

            void onAnimationEnd();

            void onAnimationCancel();
        }

        abstract void start();

        abstract boolean isRunning();

        abstract void setInterpolator(android.view.animation.Interpolator interpolator);

        abstract void addListener(android.support.design.widget.ValueAnimatorCompat.Impl.AnimatorListenerProxy listener);

        abstract void addUpdateListener(android.support.design.widget.ValueAnimatorCompat.Impl.AnimatorUpdateListenerProxy updateListener);

        abstract void setIntValues(int from, int to);

        abstract int getAnimatedIntValue();

        abstract void setFloatValues(float from, float to);

        abstract float getAnimatedFloatValue();

        abstract void setDuration(long duration);

        abstract void cancel();

        abstract float getAnimatedFraction();

        abstract void end();

        abstract long getDuration();
    }

    private final android.support.design.widget.ValueAnimatorCompat.Impl mImpl;

    ValueAnimatorCompat(android.support.design.widget.ValueAnimatorCompat.Impl impl) {
        mImpl = impl;
    }

    public void start() {
        mImpl.start();
    }

    public boolean isRunning() {
        return mImpl.isRunning();
    }

    public void setInterpolator(android.view.animation.Interpolator interpolator) {
        mImpl.setInterpolator(interpolator);
    }

    public void addUpdateListener(final android.support.design.widget.ValueAnimatorCompat.AnimatorUpdateListener updateListener) {
        if (updateListener != null) {
            mImpl.addUpdateListener(new android.support.design.widget.ValueAnimatorCompat.Impl.AnimatorUpdateListenerProxy() {
                @java.lang.Override
                public void onAnimationUpdate() {
                    updateListener.onAnimationUpdate(android.support.design.widget.ValueAnimatorCompat.this);
                }
            });
        } else {
            mImpl.addUpdateListener(null);
        }
    }

    public void addListener(final android.support.design.widget.ValueAnimatorCompat.AnimatorListener listener) {
        if (listener != null) {
            mImpl.addListener(new android.support.design.widget.ValueAnimatorCompat.Impl.AnimatorListenerProxy() {
                @java.lang.Override
                public void onAnimationStart() {
                    listener.onAnimationStart(android.support.design.widget.ValueAnimatorCompat.this);
                }

                @java.lang.Override
                public void onAnimationEnd() {
                    listener.onAnimationEnd(android.support.design.widget.ValueAnimatorCompat.this);
                }

                @java.lang.Override
                public void onAnimationCancel() {
                    listener.onAnimationCancel(android.support.design.widget.ValueAnimatorCompat.this);
                }
            });
        } else {
            mImpl.addListener(null);
        }
    }

    public void setIntValues(int from, int to) {
        mImpl.setIntValues(from, to);
    }

    public int getAnimatedIntValue() {
        return mImpl.getAnimatedIntValue();
    }

    public void setFloatValues(float from, float to) {
        mImpl.setFloatValues(from, to);
    }

    public float getAnimatedFloatValue() {
        return mImpl.getAnimatedFloatValue();
    }

    public void setDuration(long duration) {
        mImpl.setDuration(duration);
    }

    public void cancel() {
        mImpl.cancel();
    }

    public float getAnimatedFraction() {
        return mImpl.getAnimatedFraction();
    }

    public void end() {
        mImpl.end();
    }

    public long getDuration() {
        return mImpl.getDuration();
    }
}

