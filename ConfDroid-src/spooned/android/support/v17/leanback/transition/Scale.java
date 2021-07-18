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


class Scale extends android.transition.Transition {
    private static final java.lang.String PROPNAME_SCALE = "android:leanback:scale";

    public Scale() {
    }

    private void captureValues(android.transition.TransitionValues values) {
        android.view.View view = values.view;
        values.values.put(android.support.v17.leanback.transition.Scale.PROPNAME_SCALE, view.getScaleX());
    }

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public void captureEndValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public android.animation.Animator createAnimator(final android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if ((startValues == null) || (endValues == null)) {
            return null;
        }
        final float startScale = ((java.lang.Float) (startValues.values.get(android.support.v17.leanback.transition.Scale.PROPNAME_SCALE)));
        final float endScale = ((java.lang.Float) (endValues.values.get(android.support.v17.leanback.transition.Scale.PROPNAME_SCALE)));
        final android.view.View view = startValues.view;
        view.setScaleX(startScale);
        view.setScaleY(startScale);
        android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofFloat(startScale, endScale);
        animator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
            @java.lang.Override
            public void onAnimationUpdate(android.animation.ValueAnimator animation) {
                final float scale = ((java.lang.Float) (animation.getAnimatedValue()));
                view.setScaleX(scale);
                view.setScaleY(scale);
            }
        });
        return animator;
    }
}

