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
package android.support.transition;


/**
 * Base class for platform specific Transition implementations.
 */
abstract class TransitionImpl {
    public abstract void init(android.support.transition.TransitionInterface external, java.lang.Object internal);

    public void init(android.support.transition.TransitionInterface external) {
        init(external, null);
    }

    public abstract android.support.transition.TransitionImpl addListener(android.support.transition.TransitionInterfaceListener listener);

    public abstract android.support.transition.TransitionImpl removeListener(android.support.transition.TransitionInterfaceListener listener);

    public abstract android.support.transition.TransitionImpl addTarget(android.view.View target);

    public abstract android.support.transition.TransitionImpl addTarget(int targetId);

    public abstract void captureEndValues(android.support.transition.TransitionValues transitionValues);

    public abstract void captureStartValues(android.support.transition.TransitionValues transitionValues);

    public abstract android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, android.support.transition.TransitionValues endValues);

    public abstract android.support.transition.TransitionImpl excludeChildren(android.view.View target, boolean exclude);

    public abstract android.support.transition.TransitionImpl excludeChildren(int targetId, boolean exclude);

    public abstract android.support.transition.TransitionImpl excludeChildren(java.lang.Class type, boolean exclude);

    public abstract android.support.transition.TransitionImpl excludeTarget(android.view.View target, boolean exclude);

    public abstract android.support.transition.TransitionImpl excludeTarget(int targetId, boolean exclude);

    public abstract android.support.transition.TransitionImpl excludeTarget(java.lang.Class type, boolean exclude);

    public abstract long getDuration();

    public abstract android.support.transition.TransitionImpl setDuration(long duration);

    public abstract android.animation.TimeInterpolator getInterpolator();

    public abstract android.support.transition.TransitionImpl setInterpolator(android.animation.TimeInterpolator interpolator);

    public abstract java.lang.String getName();

    public abstract long getStartDelay();

    public abstract android.support.transition.TransitionImpl setStartDelay(long startDelay);

    public abstract java.util.List<java.lang.Integer> getTargetIds();

    public abstract java.util.List<android.view.View> getTargets();

    public abstract java.lang.String[] getTransitionProperties();

    public abstract android.support.transition.TransitionValues getTransitionValues(android.view.View view, boolean start);

    public abstract android.support.transition.TransitionImpl removeTarget(android.view.View target);

    public abstract android.support.transition.TransitionImpl removeTarget(int targetId);
}

