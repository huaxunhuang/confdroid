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
package android.support.v17.leanback.transition;


/**
 * Slide distance toward/from a edge.
 * This is a limited Slide implementation for KitKat without propagation support.
 */
class SlideKitkat extends android.transition.Visibility {
    private static final java.lang.String TAG = "SlideKitkat";

    private static final android.animation.TimeInterpolator sDecelerate = new android.view.animation.DecelerateInterpolator();

    private static final android.animation.TimeInterpolator sAccelerate = new android.view.animation.AccelerateInterpolator();

    private int mSlideEdge;

    private android.support.v17.leanback.transition.SlideKitkat.CalculateSlide mSlideCalculator;

    private interface CalculateSlide {
        /**
         * Returns the translation value for view when it out of the scene
         */
        float getGone(android.view.View view);

        /**
         * Returns the translation value for view when it is in the scene
         */
        float getHere(android.view.View view);

        /**
         * Returns the property to animate translation
         */
        android.util.Property<android.view.View, java.lang.Float> getProperty();
    }

    private static abstract class CalculateSlideHorizontal implements android.support.v17.leanback.transition.SlideKitkat.CalculateSlide {
        CalculateSlideHorizontal() {
        }

        @java.lang.Override
        public float getHere(android.view.View view) {
            return view.getTranslationX();
        }

        @java.lang.Override
        public android.util.Property<android.view.View, java.lang.Float> getProperty() {
            return android.view.View.TRANSLATION_X;
        }
    }

    private static abstract class CalculateSlideVertical implements android.support.v17.leanback.transition.SlideKitkat.CalculateSlide {
        CalculateSlideVertical() {
        }

        @java.lang.Override
        public float getHere(android.view.View view) {
            return view.getTranslationY();
        }

        @java.lang.Override
        public android.util.Property<android.view.View, java.lang.Float> getProperty() {
            return android.view.View.TRANSLATION_Y;
        }
    }

    private static final android.support.v17.leanback.transition.SlideKitkat.CalculateSlide sCalculateLeft = new android.support.v17.leanback.transition.SlideKitkat.CalculateSlideHorizontal() {
        @java.lang.Override
        public float getGone(android.view.View view) {
            return view.getTranslationX() - view.getWidth();
        }
    };

    private static final android.support.v17.leanback.transition.SlideKitkat.CalculateSlide sCalculateTop = new android.support.v17.leanback.transition.SlideKitkat.CalculateSlideVertical() {
        @java.lang.Override
        public float getGone(android.view.View view) {
            return view.getTranslationY() - view.getHeight();
        }
    };

    private static final android.support.v17.leanback.transition.SlideKitkat.CalculateSlide sCalculateRight = new android.support.v17.leanback.transition.SlideKitkat.CalculateSlideHorizontal() {
        @java.lang.Override
        public float getGone(android.view.View view) {
            return view.getTranslationX() + view.getWidth();
        }
    };

    private static final android.support.v17.leanback.transition.SlideKitkat.CalculateSlide sCalculateBottom = new android.support.v17.leanback.transition.SlideKitkat.CalculateSlideVertical() {
        @java.lang.Override
        public float getGone(android.view.View view) {
            return view.getTranslationY() + view.getHeight();
        }
    };

    private static final android.support.v17.leanback.transition.SlideKitkat.CalculateSlide sCalculateStart = new android.support.v17.leanback.transition.SlideKitkat.CalculateSlideHorizontal() {
        @java.lang.Override
        public float getGone(android.view.View view) {
            if (view.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL) {
                return view.getTranslationX() + view.getWidth();
            } else {
                return view.getTranslationX() - view.getWidth();
            }
        }
    };

    private static final android.support.v17.leanback.transition.SlideKitkat.CalculateSlide sCalculateEnd = new android.support.v17.leanback.transition.SlideKitkat.CalculateSlideHorizontal() {
        @java.lang.Override
        public float getGone(android.view.View view) {
            if (view.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL) {
                return view.getTranslationX() - view.getWidth();
            } else {
                return view.getTranslationX() + view.getWidth();
            }
        }
    };

    public SlideKitkat() {
        setSlideEdge(android.view.Gravity.BOTTOM);
    }

    public SlideKitkat(android.content.Context context, android.util.AttributeSet attrs) {
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lbSlide);
        int edge = a.getInt(R.styleable.lbSlide_lb_slideEdge, android.view.Gravity.BOTTOM);
        setSlideEdge(edge);
        long duration = a.getInt(R.styleable.lbSlide_android_duration, -1);
        if (duration >= 0) {
            setDuration(duration);
        }
        long startDelay = a.getInt(R.styleable.lbSlide_android_startDelay, -1);
        if (startDelay > 0) {
            setStartDelay(startDelay);
        }
        final int resID = a.getResourceId(R.styleable.lbSlide_android_interpolator, 0);
        if (resID > 0) {
            setInterpolator(android.view.animation.AnimationUtils.loadInterpolator(context, resID));
        }
        a.recycle();
    }

    /**
     * Change the edge that Views appear and disappear from.
     *
     * @param slideEdge
     * 		The edge of the scene to use for Views appearing and disappearing. One of
     * 		{@link android.view.Gravity#LEFT}, {@link android.view.Gravity#TOP},
     * 		{@link android.view.Gravity#RIGHT}, {@link android.view.Gravity#BOTTOM},
     * 		{@link android.view.Gravity#START}, {@link android.view.Gravity#END}.
     */
    public void setSlideEdge(int slideEdge) {
        switch (slideEdge) {
            case android.view.Gravity.LEFT :
                mSlideCalculator = android.support.v17.leanback.transition.SlideKitkat.sCalculateLeft;
                break;
            case android.view.Gravity.TOP :
                mSlideCalculator = android.support.v17.leanback.transition.SlideKitkat.sCalculateTop;
                break;
            case android.view.Gravity.RIGHT :
                mSlideCalculator = android.support.v17.leanback.transition.SlideKitkat.sCalculateRight;
                break;
            case android.view.Gravity.BOTTOM :
                mSlideCalculator = android.support.v17.leanback.transition.SlideKitkat.sCalculateBottom;
                break;
            case android.view.Gravity.START :
                mSlideCalculator = android.support.v17.leanback.transition.SlideKitkat.sCalculateStart;
                break;
            case android.view.Gravity.END :
                mSlideCalculator = android.support.v17.leanback.transition.SlideKitkat.sCalculateEnd;
                break;
            default :
                throw new java.lang.IllegalArgumentException("Invalid slide direction");
        }
        mSlideEdge = slideEdge;
    }

    /**
     * Returns the edge that Views appear and disappear from.
     *
     * @return the edge of the scene to use for Views appearing and disappearing. One of
    {@link android.view.Gravity#LEFT}, {@link android.view.Gravity#TOP},
    {@link android.view.Gravity#RIGHT}, {@link android.view.Gravity#BOTTOM},
    {@link android.view.Gravity#START}, {@link android.view.Gravity#END}.
     */
    public int getSlideEdge() {
        return mSlideEdge;
    }

    private android.animation.Animator createAnimation(final android.view.View view, android.util.Property<android.view.View, java.lang.Float> property, float start, float end, float terminalValue, android.animation.TimeInterpolator interpolator, int finalVisibility) {
        float[] startPosition = ((float[]) (view.getTag(R.id.lb_slide_transition_value)));
        if (startPosition != null) {
            start = (android.view.View.TRANSLATION_Y == property) ? startPosition[1] : startPosition[0];
            view.setTag(R.id.lb_slide_transition_value, null);
        }
        final android.animation.ObjectAnimator anim = android.animation.ObjectAnimator.ofFloat(view, property, start, end);
        android.support.v17.leanback.transition.SlideKitkat.SlideAnimatorListener listener = new android.support.v17.leanback.transition.SlideKitkat.SlideAnimatorListener(view, property, terminalValue, end, finalVisibility);
        anim.addListener(listener);
        anim.addPauseListener(listener);
        anim.setInterpolator(interpolator);
        return anim;
    }

    @java.lang.Override
    public android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, int startVisibility, android.transition.TransitionValues endValues, int endVisibility) {
        android.view.View view = (endValues != null) ? endValues.view : null;
        if (view == null) {
            return null;
        }
        float end = mSlideCalculator.getHere(view);
        float start = mSlideCalculator.getGone(view);
        return createAnimation(view, mSlideCalculator.getProperty(), start, end, end, android.support.v17.leanback.transition.SlideKitkat.sDecelerate, android.view.View.VISIBLE);
    }

    @java.lang.Override
    public android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, int startVisibility, android.transition.TransitionValues endValues, int endVisibility) {
        android.view.View view = (startValues != null) ? startValues.view : null;
        if (view == null) {
            return null;
        }
        float start = mSlideCalculator.getHere(view);
        float end = mSlideCalculator.getGone(view);
        return createAnimation(view, mSlideCalculator.getProperty(), start, end, start, android.support.v17.leanback.transition.SlideKitkat.sAccelerate, android.view.View.INVISIBLE);
    }

    private static class SlideAnimatorListener extends android.animation.AnimatorListenerAdapter {
        private boolean mCanceled = false;

        private float mPausedValue;

        private final android.view.View mView;

        private final float mEndValue;

        private final float mTerminalValue;

        private final int mFinalVisibility;

        private final android.util.Property<android.view.View, java.lang.Float> mProp;

        public SlideAnimatorListener(android.view.View view, android.util.Property<android.view.View, java.lang.Float> prop, float terminalValue, float endValue, int finalVisibility) {
            mProp = prop;
            mView = view;
            mTerminalValue = terminalValue;
            mEndValue = endValue;
            mFinalVisibility = finalVisibility;
            view.setVisibility(android.view.View.VISIBLE);
        }

        @java.lang.Override
        public void onAnimationCancel(android.animation.Animator animator) {
            float[] transitionPosition = new float[2];
            transitionPosition[0] = mView.getTranslationX();
            transitionPosition[1] = mView.getTranslationY();
            mView.setTag(R.id.lb_slide_transition_value, transitionPosition);
            mProp.set(mView, mTerminalValue);
            mCanceled = true;
        }

        @java.lang.Override
        public void onAnimationEnd(android.animation.Animator animator) {
            if (!mCanceled) {
                mProp.set(mView, mTerminalValue);
            }
            mView.setVisibility(mFinalVisibility);
        }

        @java.lang.Override
        public void onAnimationPause(android.animation.Animator animator) {
            mPausedValue = mProp.get(mView);
            mProp.set(mView, mEndValue);
            mView.setVisibility(mFinalVisibility);
        }

        @java.lang.Override
        public void onAnimationResume(android.animation.Animator animator) {
            mProp.set(mView, mPausedValue);
            mView.setVisibility(android.view.View.VISIBLE);
        }
    }
}

