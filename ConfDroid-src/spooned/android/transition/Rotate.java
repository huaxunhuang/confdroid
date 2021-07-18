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
 * This transition captures the rotation property of targets before and after
 * the scene change and animates any changes.
 *
 * @unknown 
 */
public class Rotate extends android.transition.Transition {
    private static final java.lang.String PROPNAME_ROTATION = "android:rotate:rotation";

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        transitionValues.values.put(android.transition.Rotate.PROPNAME_ROTATION, transitionValues.view.getRotation());
    }

    @java.lang.Override
    public void captureEndValues(android.transition.TransitionValues transitionValues) {
        transitionValues.values.put(android.transition.Rotate.PROPNAME_ROTATION, transitionValues.view.getRotation());
    }

    @java.lang.Override
    public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if ((startValues == null) || (endValues == null)) {
            return null;
        }
        final android.view.View view = endValues.view;
        float startRotation = ((java.lang.Float) (startValues.values.get(android.transition.Rotate.PROPNAME_ROTATION)));
        float endRotation = ((java.lang.Float) (endValues.values.get(android.transition.Rotate.PROPNAME_ROTATION)));
        if (startRotation != endRotation) {
            view.setRotation(startRotation);
            return android.animation.ObjectAnimator.ofFloat(view, android.view.View.ROTATION, startRotation, endRotation);
        }
        return null;
    }
}

