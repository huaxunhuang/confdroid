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
package android.support.v17.leanback.transition;


/**
 * Execute horizontal slide of 1/4 width and fade (to workaround bug 23718734)
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class FadeAndShortSlide extends android.transition.Visibility {
    private static final android.animation.TimeInterpolator sDecelerate = new android.view.animation.DecelerateInterpolator();

    // private static final TimeInterpolator sAccelerate = new AccelerateInterpolator();
    private static final java.lang.String PROPNAME_SCREEN_POSITION = "android:fadeAndShortSlideTransition:screenPosition";

    private android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide mSlideCalculator;

    private android.transition.Visibility mFade = new android.transition.Fade();

    private float mDistance = -1;

    private static abstract class CalculateSlide {
        CalculateSlide() {
        }

        /**
         * Returns the translation X value for view when it goes out of the scene
         */
        float getGoneX(android.support.v17.leanback.transition.FadeAndShortSlide t, android.view.ViewGroup sceneRoot, android.view.View view, int[] position) {
            return view.getTranslationX();
        }

        /**
         * Returns the translation Y value for view when it goes out of the scene
         */
        float getGoneY(android.support.v17.leanback.transition.FadeAndShortSlide t, android.view.ViewGroup sceneRoot, android.view.View view, int[] position) {
            return view.getTranslationY();
        }
    }

    float getHorizontalDistance(android.view.ViewGroup sceneRoot) {
        return mDistance >= 0 ? mDistance : sceneRoot.getWidth() / 4;
    }

    float getVerticalDistance(android.view.ViewGroup sceneRoot) {
        return mDistance >= 0 ? mDistance : sceneRoot.getHeight() / 4;
    }

    static final android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide sCalculateStart = new android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide() {
        @java.lang.Override
        public float getGoneX(android.support.v17.leanback.transition.FadeAndShortSlide t, android.view.ViewGroup sceneRoot, android.view.View view, int[] position) {
            final boolean isRtl = sceneRoot.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL;
            final float x;
            if (isRtl) {
                x = view.getTranslationX() + t.getHorizontalDistance(sceneRoot);
            } else {
                x = view.getTranslationX() - t.getHorizontalDistance(sceneRoot);
            }
            return x;
        }
    };

    static final android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide sCalculateEnd = new android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide() {
        @java.lang.Override
        public float getGoneX(android.support.v17.leanback.transition.FadeAndShortSlide t, android.view.ViewGroup sceneRoot, android.view.View view, int[] position) {
            final boolean isRtl = sceneRoot.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL;
            final float x;
            if (isRtl) {
                x = view.getTranslationX() - t.getHorizontalDistance(sceneRoot);
            } else {
                x = view.getTranslationX() + t.getHorizontalDistance(sceneRoot);
            }
            return x;
        }
    };

    static final android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide sCalculateStartEnd = new android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide() {
        @java.lang.Override
        public float getGoneX(android.support.v17.leanback.transition.FadeAndShortSlide t, android.view.ViewGroup sceneRoot, android.view.View view, int[] position) {
            final int viewCenter = position[0] + (view.getWidth() / 2);
            sceneRoot.getLocationOnScreen(position);
            android.graphics.Rect center = t.getEpicenter();
            final int sceneRootCenter = (center == null) ? position[0] + (sceneRoot.getWidth() / 2) : center.centerX();
            if (viewCenter < sceneRootCenter) {
                return view.getTranslationX() - t.getHorizontalDistance(sceneRoot);
            } else {
                return view.getTranslationX() + t.getHorizontalDistance(sceneRoot);
            }
        }
    };

    static final android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide sCalculateBottom = new android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide() {
        @java.lang.Override
        public float getGoneY(android.support.v17.leanback.transition.FadeAndShortSlide t, android.view.ViewGroup sceneRoot, android.view.View view, int[] position) {
            return view.getTranslationY() + t.getVerticalDistance(sceneRoot);
        }
    };

    static final android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide sCalculateTop = new android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide() {
        @java.lang.Override
        public float getGoneY(android.support.v17.leanback.transition.FadeAndShortSlide t, android.view.ViewGroup sceneRoot, android.view.View view, int[] position) {
            return view.getTranslationY() - t.getVerticalDistance(sceneRoot);
        }
    };

    final android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide sCalculateTopBottom = new android.support.v17.leanback.transition.FadeAndShortSlide.CalculateSlide() {
        @java.lang.Override
        public float getGoneY(android.support.v17.leanback.transition.FadeAndShortSlide t, android.view.ViewGroup sceneRoot, android.view.View view, int[] position) {
            final int viewCenter = position[1] + (view.getHeight() / 2);
            sceneRoot.getLocationOnScreen(position);
            android.graphics.Rect center = getEpicenter();
            final int sceneRootCenter = (center == null) ? position[1] + (sceneRoot.getHeight() / 2) : center.centerY();
            if (viewCenter < sceneRootCenter) {
                return view.getTranslationY() - t.getVerticalDistance(sceneRoot);
            } else {
                return view.getTranslationY() + t.getVerticalDistance(sceneRoot);
            }
        }
    };

    public FadeAndShortSlide() {
        this(android.view.Gravity.START);
    }

    public FadeAndShortSlide(int slideEdge) {
        setSlideEdge(slideEdge);
    }

    public FadeAndShortSlide(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lbSlide);
        int edge = a.getInt(R.styleable.lbSlide_lb_slideEdge, android.view.Gravity.START);
        setSlideEdge(edge);
        a.recycle();
    }

    @java.lang.Override
    public void setEpicenterCallback(android.transition.Transition.EpicenterCallback epicenterCallback) {
        mFade.setEpicenterCallback(epicenterCallback);
        super.setEpicenterCallback(epicenterCallback);
    }

    private void captureValues(android.transition.TransitionValues transitionValues) {
        android.view.View view = transitionValues.view;
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        transitionValues.values.put(android.support.v17.leanback.transition.FadeAndShortSlide.PROPNAME_SCREEN_POSITION, position);
    }

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        mFade.captureStartValues(transitionValues);
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    @java.lang.Override
    public void captureEndValues(android.transition.TransitionValues transitionValues) {
        mFade.captureEndValues(transitionValues);
        super.captureEndValues(transitionValues);
        captureValues(transitionValues);
    }

    public void setSlideEdge(int slideEdge) {
        switch (slideEdge) {
            case android.view.Gravity.START :
                mSlideCalculator = android.support.v17.leanback.transition.FadeAndShortSlide.sCalculateStart;
                break;
            case android.view.Gravity.END :
                mSlideCalculator = android.support.v17.leanback.transition.FadeAndShortSlide.sCalculateEnd;
                break;
            case android.view.Gravity.START | android.view.Gravity.END :
                mSlideCalculator = android.support.v17.leanback.transition.FadeAndShortSlide.sCalculateStartEnd;
                break;
            case android.view.Gravity.TOP :
                mSlideCalculator = android.support.v17.leanback.transition.FadeAndShortSlide.sCalculateTop;
                break;
            case android.view.Gravity.BOTTOM :
                mSlideCalculator = android.support.v17.leanback.transition.FadeAndShortSlide.sCalculateBottom;
                break;
            case android.view.Gravity.TOP | android.view.Gravity.BOTTOM :
                mSlideCalculator = sCalculateTopBottom;
                break;
            default :
                throw new java.lang.IllegalArgumentException("Invalid slide direction");
        }
    }

    @java.lang.Override
    public android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.view.View view, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if (endValues == null) {
            return null;
        }
        if (sceneRoot == view) {
            // workaround b/25375640, avoid run animation on sceneRoot
            return null;
        }
        int[] position = ((int[]) (endValues.values.get(android.support.v17.leanback.transition.FadeAndShortSlide.PROPNAME_SCREEN_POSITION)));
        int left = position[0];
        int top = position[1];
        float endX = view.getTranslationX();
        float startX = mSlideCalculator.getGoneX(this, sceneRoot, view, position);
        float endY = view.getTranslationY();
        float startY = mSlideCalculator.getGoneY(this, sceneRoot, view, position);
        final android.animation.Animator slideAnimator = android.support.v17.leanback.transition.TranslationAnimationCreator.createAnimation(view, endValues, left, top, startX, startY, endX, endY, android.support.v17.leanback.transition.FadeAndShortSlide.sDecelerate, this);
        final android.animation.Animator fadeAnimator = mFade.onAppear(sceneRoot, view, startValues, endValues);
        if (slideAnimator == null) {
            return fadeAnimator;
        } else
            if (fadeAnimator == null) {
                return slideAnimator;
            }

        final android.animation.AnimatorSet set = new android.animation.AnimatorSet();
        set.play(slideAnimator).with(fadeAnimator);
        return set;
    }

    @java.lang.Override
    public android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, android.view.View view, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if (startValues == null) {
            return null;
        }
        if (sceneRoot == view) {
            // workaround b/25375640, avoid run animation on sceneRoot
            return null;
        }
        int[] position = ((int[]) (startValues.values.get(android.support.v17.leanback.transition.FadeAndShortSlide.PROPNAME_SCREEN_POSITION)));
        int left = position[0];
        int top = position[1];
        float startX = view.getTranslationX();
        float endX = mSlideCalculator.getGoneX(this, sceneRoot, view, position);
        float startY = view.getTranslationY();
        float endY = mSlideCalculator.getGoneY(this, sceneRoot, view, position);
        final android.animation.Animator slideAnimator = /* sAccelerate */
        android.support.v17.leanback.transition.TranslationAnimationCreator.createAnimation(view, startValues, left, top, startX, startY, endX, endY, android.support.v17.leanback.transition.FadeAndShortSlide.sDecelerate, this);
        final android.animation.Animator fadeAnimator = mFade.onDisappear(sceneRoot, view, startValues, endValues);
        if (slideAnimator == null) {
            return fadeAnimator;
        } else
            if (fadeAnimator == null) {
                return slideAnimator;
            }

        final android.animation.AnimatorSet set = new android.animation.AnimatorSet();
        set.play(slideAnimator).with(fadeAnimator);
        return set;
    }

    @java.lang.Override
    public android.transition.Transition addListener(android.transition.Transition.TransitionListener listener) {
        mFade.addListener(listener);
        return super.addListener(listener);
    }

    @java.lang.Override
    public android.transition.Transition removeListener(android.transition.Transition.TransitionListener listener) {
        mFade.removeListener(listener);
        return super.removeListener(listener);
    }

    /**
     * Returns distance to slide.  When negative value is returned, it will use 1/4 of
     * sceneRoot dimension.
     */
    public float getDistance() {
        return mDistance;
    }

    /**
     * Set distance to slide, default value is -1.  when negative value is set, it will use 1/4 of
     * sceneRoot dimension.
     *
     * @param distance
     * 		Pixels to slide.
     */
    public void setDistance(float distance) {
        mDistance = distance;
    }

    @java.lang.Override
    public android.transition.Transition clone() {
        android.support.v17.leanback.transition.FadeAndShortSlide clone = null;
        clone = ((android.support.v17.leanback.transition.FadeAndShortSlide) (super.clone()));
        clone.mFade = ((android.transition.Visibility) (mFade.clone()));
        return clone;
    }
}

