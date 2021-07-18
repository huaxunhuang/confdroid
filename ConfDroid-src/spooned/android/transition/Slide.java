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
package android.transition;


/**
 * This transition tracks changes to the visibility of target views in the
 * start and end scenes and moves views in or out from one of the edges of the
 * scene. Visibility is determined by both the
 * {@link View#setVisibility(int)} state of the view as well as whether it
 * is parented in the current view hierarchy. Disappearing Views are
 * limited as described in {@link Visibility#onDisappear(android.view.ViewGroup,
 * TransitionValues, int, TransitionValues, int)}.
 */
public class Slide extends android.transition.Visibility {
    private static final java.lang.String TAG = "Slide";

    private static final android.animation.TimeInterpolator sDecelerate = new android.view.animation.DecelerateInterpolator();

    private static final android.animation.TimeInterpolator sAccelerate = new android.view.animation.AccelerateInterpolator();

    private static final java.lang.String PROPNAME_SCREEN_POSITION = "android:slide:screenPosition";

    private android.transition.Slide.CalculateSlide mSlideCalculator = android.transition.Slide.sCalculateBottom;

    @android.transition.Slide.GravityFlag
    private int mSlideEdge = android.view.Gravity.BOTTOM;

    private float mSlideFraction = 1;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.view.Gravity.LEFT, android.view.Gravity.TOP, android.view.Gravity.RIGHT, android.view.Gravity.BOTTOM, android.view.Gravity.START, android.view.Gravity.END })
    public @interface GravityFlag {}

    private interface CalculateSlide {
        /**
         * Returns the translation value for view when it goes out of the scene
         */
        float getGoneX(android.view.ViewGroup sceneRoot, android.view.View view, float fraction);

        /**
         * Returns the translation value for view when it goes out of the scene
         */
        float getGoneY(android.view.ViewGroup sceneRoot, android.view.View view, float fraction);
    }

    private static abstract class CalculateSlideHorizontal implements android.transition.Slide.CalculateSlide {
        @java.lang.Override
        public float getGoneY(android.view.ViewGroup sceneRoot, android.view.View view, float fraction) {
            return view.getTranslationY();
        }
    }

    private static abstract class CalculateSlideVertical implements android.transition.Slide.CalculateSlide {
        @java.lang.Override
        public float getGoneX(android.view.ViewGroup sceneRoot, android.view.View view, float fraction) {
            return view.getTranslationX();
        }
    }

    private static final android.transition.Slide.CalculateSlide sCalculateLeft = new android.transition.Slide.CalculateSlideHorizontal() {
        @java.lang.Override
        public float getGoneX(android.view.ViewGroup sceneRoot, android.view.View view, float fraction) {
            return view.getTranslationX() - (sceneRoot.getWidth() * fraction);
        }
    };

    private static final android.transition.Slide.CalculateSlide sCalculateStart = new android.transition.Slide.CalculateSlideHorizontal() {
        @java.lang.Override
        public float getGoneX(android.view.ViewGroup sceneRoot, android.view.View view, float fraction) {
            final boolean isRtl = sceneRoot.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL;
            final float x;
            if (isRtl) {
                x = view.getTranslationX() + (sceneRoot.getWidth() * fraction);
            } else {
                x = view.getTranslationX() - (sceneRoot.getWidth() * fraction);
            }
            return x;
        }
    };

    private static final android.transition.Slide.CalculateSlide sCalculateTop = new android.transition.Slide.CalculateSlideVertical() {
        @java.lang.Override
        public float getGoneY(android.view.ViewGroup sceneRoot, android.view.View view, float fraction) {
            return view.getTranslationY() - (sceneRoot.getHeight() * fraction);
        }
    };

    private static final android.transition.Slide.CalculateSlide sCalculateRight = new android.transition.Slide.CalculateSlideHorizontal() {
        @java.lang.Override
        public float getGoneX(android.view.ViewGroup sceneRoot, android.view.View view, float fraction) {
            return view.getTranslationX() + (sceneRoot.getWidth() * fraction);
        }
    };

    private static final android.transition.Slide.CalculateSlide sCalculateEnd = new android.transition.Slide.CalculateSlideHorizontal() {
        @java.lang.Override
        public float getGoneX(android.view.ViewGroup sceneRoot, android.view.View view, float fraction) {
            final boolean isRtl = sceneRoot.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL;
            final float x;
            if (isRtl) {
                x = view.getTranslationX() - (sceneRoot.getWidth() * fraction);
            } else {
                x = view.getTranslationX() + (sceneRoot.getWidth() * fraction);
            }
            return x;
        }
    };

    private static final android.transition.Slide.CalculateSlide sCalculateBottom = new android.transition.Slide.CalculateSlideVertical() {
        @java.lang.Override
        public float getGoneY(android.view.ViewGroup sceneRoot, android.view.View view, float fraction) {
            return view.getTranslationY() + (sceneRoot.getHeight() * fraction);
        }
    };

    /**
     * Constructor using the default {@link Gravity#BOTTOM}
     * slide edge direction.
     */
    public Slide() {
        setSlideEdge(android.view.Gravity.BOTTOM);
    }

    /**
     * Constructor using the provided slide edge direction.
     */
    public Slide(@android.transition.Slide.GravityFlag
    int slideEdge) {
        setSlideEdge(slideEdge);
    }

    public Slide(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Slide);
        int edge = a.getInt(R.styleable.Slide_slideEdge, android.view.Gravity.BOTTOM);
        a.recycle();
        setSlideEdge(edge);
    }

    private void captureValues(android.transition.TransitionValues transitionValues) {
        android.view.View view = transitionValues.view;
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        transitionValues.values.put(android.transition.Slide.PROPNAME_SCREEN_POSITION, position);
    }

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    @java.lang.Override
    public void captureEndValues(android.transition.TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        captureValues(transitionValues);
    }

    /**
     * Change the edge that Views appear and disappear from.
     *
     * @param slideEdge
     * 		The edge of the scene to use for Views appearing and disappearing. One of
     * 		{@link android.view.Gravity#LEFT}, {@link android.view.Gravity#TOP},
     * 		{@link android.view.Gravity#RIGHT}, {@link android.view.Gravity#BOTTOM},
     * 		{@link android.view.Gravity#START}, {@link android.view.Gravity#END}.
     * @unknown ref android.R.styleable#Slide_slideEdge
     */
    public void setSlideEdge(@android.transition.Slide.GravityFlag
    int slideEdge) {
        switch (slideEdge) {
            case android.view.Gravity.LEFT :
                mSlideCalculator = android.transition.Slide.sCalculateLeft;
                break;
            case android.view.Gravity.TOP :
                mSlideCalculator = android.transition.Slide.sCalculateTop;
                break;
            case android.view.Gravity.RIGHT :
                mSlideCalculator = android.transition.Slide.sCalculateRight;
                break;
            case android.view.Gravity.BOTTOM :
                mSlideCalculator = android.transition.Slide.sCalculateBottom;
                break;
            case android.view.Gravity.START :
                mSlideCalculator = android.transition.Slide.sCalculateStart;
                break;
            case android.view.Gravity.END :
                mSlideCalculator = android.transition.Slide.sCalculateEnd;
                break;
            default :
                throw new java.lang.IllegalArgumentException("Invalid slide direction");
        }
        mSlideEdge = slideEdge;
        android.transition.SidePropagation propagation = new android.transition.SidePropagation();
        propagation.setSide(slideEdge);
        setPropagation(propagation);
    }

    /**
     * Returns the edge that Views appear and disappear from.
     *
     * @return the edge of the scene to use for Views appearing and disappearing. One of
    {@link android.view.Gravity#LEFT}, {@link android.view.Gravity#TOP},
    {@link android.view.Gravity#RIGHT}, {@link android.view.Gravity#BOTTOM},
    {@link android.view.Gravity#START}, {@link android.view.Gravity#END}.
     * @unknown ref android.R.styleable#Slide_slideEdge
     */
    @android.transition.Slide.GravityFlag
    public int getSlideEdge() {
        return mSlideEdge;
    }

    @java.lang.Override
    public android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.view.View view, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if (endValues == null) {
            return null;
        }
        int[] position = ((int[]) (endValues.values.get(android.transition.Slide.PROPNAME_SCREEN_POSITION)));
        float endX = view.getTranslationX();
        float endY = view.getTranslationY();
        float startX = mSlideCalculator.getGoneX(sceneRoot, view, mSlideFraction);
        float startY = mSlideCalculator.getGoneY(sceneRoot, view, mSlideFraction);
        return android.transition.TranslationAnimationCreator.createAnimation(view, endValues, position[0], position[1], startX, startY, endX, endY, android.transition.Slide.sDecelerate, this);
    }

    @java.lang.Override
    public android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, android.view.View view, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if (startValues == null) {
            return null;
        }
        int[] position = ((int[]) (startValues.values.get(android.transition.Slide.PROPNAME_SCREEN_POSITION)));
        float startX = view.getTranslationX();
        float startY = view.getTranslationY();
        float endX = mSlideCalculator.getGoneX(sceneRoot, view, mSlideFraction);
        float endY = mSlideCalculator.getGoneY(sceneRoot, view, mSlideFraction);
        return android.transition.TranslationAnimationCreator.createAnimation(view, startValues, position[0], position[1], startX, startY, endX, endY, android.transition.Slide.sAccelerate, this);
    }

    /**
     *
     *
     * @unknown 
     */
    public void setSlideFraction(float slideFraction) {
        mSlideFraction = slideFraction;
    }
}

