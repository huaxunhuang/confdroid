/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.transition;


final class TransitionHelperApi21 {
    TransitionHelperApi21() {
    }

    public static void setEnterTransition(android.app.Fragment fragment, java.lang.Object transition) {
        fragment.setEnterTransition(((android.transition.Transition) (transition)));
    }

    public static void setExitTransition(android.app.Fragment fragment, java.lang.Object transition) {
        fragment.setExitTransition(((android.transition.Transition) (transition)));
    }

    public static void setSharedElementEnterTransition(android.app.Fragment fragment, java.lang.Object transition) {
        fragment.setSharedElementEnterTransition(((android.transition.Transition) (transition)));
    }

    public static void addSharedElement(android.app.FragmentTransaction ft, android.view.View view, java.lang.String transitionName) {
        ft.addSharedElement(view, transitionName);
    }

    public static java.lang.Object getSharedElementEnterTransition(android.view.Window window) {
        return window.getSharedElementEnterTransition();
    }

    public static java.lang.Object getSharedElementReturnTransition(android.view.Window window) {
        return window.getSharedElementReturnTransition();
    }

    public static java.lang.Object getSharedElementExitTransition(android.view.Window window) {
        return window.getSharedElementExitTransition();
    }

    public static java.lang.Object getSharedElementReenterTransition(android.view.Window window) {
        return window.getSharedElementReenterTransition();
    }

    public static java.lang.Object getEnterTransition(android.view.Window window) {
        return window.getEnterTransition();
    }

    public static java.lang.Object getReturnTransition(android.view.Window window) {
        return window.getReturnTransition();
    }

    public static java.lang.Object getExitTransition(android.view.Window window) {
        return window.getExitTransition();
    }

    public static java.lang.Object getReenterTransition(android.view.Window window) {
        return window.getReenterTransition();
    }

    public static java.lang.Object createScale() {
        return new android.transition.ChangeTransform();
    }

    public static java.lang.Object createDefaultInterpolator(android.content.Context context) {
        return android.view.animation.AnimationUtils.loadInterpolator(context, R.interpolator.fast_out_linear_in);
    }

    public static java.lang.Object createChangeTransform() {
        return new android.transition.ChangeTransform();
    }

    public static java.lang.Object createFadeAndShortSlide(int edge) {
        return new android.support.v17.leanback.transition.FadeAndShortSlide(edge);
    }

    public static java.lang.Object createFadeAndShortSlide(int edge, float distance) {
        android.support.v17.leanback.transition.FadeAndShortSlide slide = new android.support.v17.leanback.transition.FadeAndShortSlide(edge);
        slide.setDistance(distance);
        return slide;
    }

    public static void beginDelayedTransition(android.view.ViewGroup sceneRoot, java.lang.Object transitionObject) {
        android.transition.Transition transition = ((android.transition.Transition) (transitionObject));
        android.transition.TransitionManager.beginDelayedTransition(sceneRoot, transition);
    }

    public static void setTransitionGroup(android.view.ViewGroup viewGroup, boolean transitionGroup) {
        viewGroup.setTransitionGroup(transitionGroup);
    }
}

