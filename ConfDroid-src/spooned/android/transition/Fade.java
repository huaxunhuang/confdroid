/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * start and end scenes and fades views in or out when they become visible
 * or non-visible. Visibility is determined by both the
 * {@link View#setVisibility(int)} state of the view as well as whether it
 * is parented in the current view hierarchy.
 *
 * <p>The ability of this transition to fade out a particular view, and the
 * way that that fading operation takes place, is based on
 * the situation of the view in the view hierarchy. For example, if a view was
 * simply removed from its parent, then the view will be added into a {@link android.view.ViewGroupOverlay} while fading. If a visible view is
 * changed to be {@link View#GONE} or {@link View#INVISIBLE}, then the
 * visibility will be changed to {@link View#VISIBLE} for the duration of
 * the animation. However, if a view is in a hierarchy which is also altering
 * its visibility, the situation can be more complicated. In general, if a
 * view that is no longer in the hierarchy in the end scene still has a
 * parent (so its parent hierarchy was removed, but it was not removed from
 * its parent), then it will be left alone to avoid side-effects from
 * improperly removing it from its parent. The only exception to this is if
 * the previous {@link Scene} was
 * {@link Scene#getSceneForLayout(android.view.ViewGroup, int, android.content.Context)
 * created from a layout resource file}, then it is considered safe to un-parent
 * the starting scene view in order to fade it out.</p>
 *
 * <p>A Fade transition can be described in a resource file by using the
 * tag <code>fade</code>, along with the standard
 * attributes of {@link android.R.styleable#Fade} and
 * {@link android.R.styleable#Transition}.</p>
 */
public class Fade extends android.transition.Visibility {
    static final java.lang.String PROPNAME_TRANSITION_ALPHA = "android:fade:transitionAlpha";

    private static boolean DBG = android.transition.Transition.DBG && false;

    private static final java.lang.String LOG_TAG = "Fade";

    /**
     * Fading mode used in {@link #Fade(int)} to make the transition
     * operate on targets that are appearing. Maybe be combined with
     * {@link #OUT} to fade both in and out. Equivalent to
     * {@link Visibility#MODE_IN}.
     */
    public static final int IN = android.transition.Visibility.MODE_IN;

    /**
     * Fading mode used in {@link #Fade(int)} to make the transition
     * operate on targets that are disappearing. Maybe be combined with
     * {@link #IN} to fade both in and out. Equivalent to
     * {@link Visibility#MODE_OUT}.
     */
    public static final int OUT = android.transition.Visibility.MODE_OUT;

    /**
     * Constructs a Fade transition that will fade targets in and out.
     */
    public Fade() {
    }

    /**
     * Constructs a Fade transition that will fade targets in
     * and/or out, according to the value of fadingMode.
     *
     * @param fadingMode
     * 		The behavior of this transition, a combination of
     * 		{@link #IN} and {@link #OUT}.
     */
    public Fade(@android.transition.Visibility.VisibilityMode
    int fadingMode) {
        setMode(fadingMode);
    }

    public Fade(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Fade);
        int fadingMode = a.getInt(R.styleable.Fade_fadingMode, getMode());
        setMode(fadingMode);
        a.recycle();
    }

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        transitionValues.values.put(android.transition.Fade.PROPNAME_TRANSITION_ALPHA, transitionValues.view.getTransitionAlpha());
    }

    /**
     * Utility method to handle creating and running the Animator.
     */
    private android.animation.Animator createAnimation(final android.view.View view, float startAlpha, final float endAlpha) {
        if (startAlpha == endAlpha) {
            return null;
        }
        view.setTransitionAlpha(startAlpha);
        final android.animation.ObjectAnimator anim = android.animation.ObjectAnimator.ofFloat(view, "transitionAlpha", endAlpha);
        if (android.transition.Fade.DBG) {
            android.util.Log.d(android.transition.Fade.LOG_TAG, "Created animator " + anim);
        }
        final android.transition.Fade.FadeAnimatorListener listener = new android.transition.Fade.FadeAnimatorListener(view);
        anim.addListener(listener);
        addListener(new android.transition.TransitionListenerAdapter() {
            @java.lang.Override
            public void onTransitionEnd(android.transition.Transition transition) {
                view.setTransitionAlpha(1);
                transition.removeListener(this);
            }
        });
        return anim;
    }

    @java.lang.Override
    public android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.view.View view, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if (android.transition.Fade.DBG) {
            android.view.View startView = (startValues != null) ? startValues.view : null;
            android.util.Log.d(android.transition.Fade.LOG_TAG, (("Fade.onAppear: startView, startVis, endView, endVis = " + startView) + ", ") + view);
        }
        float startAlpha = android.transition.Fade.getStartAlpha(startValues, 0);
        if (startAlpha == 1) {
            startAlpha = 0;
        }
        return createAnimation(view, startAlpha, 1);
    }

    @java.lang.Override
    public android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, final android.view.View view, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        float startAlpha = android.transition.Fade.getStartAlpha(startValues, 1);
        return createAnimation(view, startAlpha, 0);
    }

    private static float getStartAlpha(android.transition.TransitionValues startValues, float fallbackValue) {
        float startAlpha = fallbackValue;
        if (startValues != null) {
            java.lang.Float startAlphaFloat = ((java.lang.Float) (startValues.values.get(android.transition.Fade.PROPNAME_TRANSITION_ALPHA)));
            if (startAlphaFloat != null) {
                startAlpha = startAlphaFloat;
            }
        }
        return startAlpha;
    }

    private static class FadeAnimatorListener extends android.animation.AnimatorListenerAdapter {
        private final android.view.View mView;

        private boolean mLayerTypeChanged = false;

        public FadeAnimatorListener(android.view.View view) {
            mView = view;
        }

        @java.lang.Override
        public void onAnimationStart(android.animation.Animator animator) {
            if (mView.hasOverlappingRendering() && (mView.getLayerType() == android.view.View.LAYER_TYPE_NONE)) {
                mLayerTypeChanged = true;
                mView.setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null);
            }
        }

        @java.lang.Override
        public void onAnimationEnd(android.animation.Animator animator) {
            mView.setTransitionAlpha(1);
            if (mLayerTypeChanged) {
                mView.setLayerType(android.view.View.LAYER_TYPE_NONE, null);
            }
        }
    }
}

