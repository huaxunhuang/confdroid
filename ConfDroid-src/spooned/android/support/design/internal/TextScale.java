/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.design.internal;


/**
 *
 *
 * @unknown 
 */
public class TextScale extends android.support.transition.Transition {
    private static final java.lang.String PROPNAME_SCALE = "android:textscale:scale";

    @java.lang.Override
    public void captureStartValues(android.support.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public void captureEndValues(android.support.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(android.support.transition.TransitionValues transitionValues) {
        if (transitionValues.view instanceof android.widget.TextView) {
            android.widget.TextView textview = ((android.widget.TextView) (transitionValues.view));
            transitionValues.values.put(android.support.design.internal.TextScale.PROPNAME_SCALE, textview.getScaleX());
        }
    }

    @java.lang.Override
    public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, android.support.transition.TransitionValues endValues) {
        if ((((startValues == null) || (endValues == null)) || (!(startValues.view instanceof android.widget.TextView))) || (!(endValues.view instanceof android.widget.TextView))) {
            return null;
        }
        final android.widget.TextView view = ((android.widget.TextView) (endValues.view));
        java.util.Map<java.lang.String, java.lang.Object> startVals = startValues.values;
        java.util.Map<java.lang.String, java.lang.Object> endVals = endValues.values;
        final float startSize = (startVals.get(android.support.design.internal.TextScale.PROPNAME_SCALE) != null) ? ((float) (startVals.get(android.support.design.internal.TextScale.PROPNAME_SCALE))) : 1.0F;
        final float endSize = (endVals.get(android.support.design.internal.TextScale.PROPNAME_SCALE) != null) ? ((float) (endVals.get(android.support.design.internal.TextScale.PROPNAME_SCALE))) : 1.0F;
        if (startSize == endSize) {
            return null;
        }
        android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofFloat(startSize, endSize);
        animator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
            @java.lang.Override
            public void onAnimationUpdate(android.animation.ValueAnimator valueAnimator) {
                float animatedValue = ((float) (valueAnimator.getAnimatedValue()));
                view.setScaleX(animatedValue);
                view.setScaleY(animatedValue);
            }
        });
        return animator;
    }
}

