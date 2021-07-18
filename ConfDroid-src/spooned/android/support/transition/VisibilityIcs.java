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


class VisibilityIcs extends android.support.transition.TransitionIcs implements android.support.transition.VisibilityImpl {
    @java.lang.Override
    public void init(android.support.transition.TransitionInterface external, java.lang.Object internal) {
        mExternalTransition = external;
        if (internal == null) {
            mTransition = new android.support.transition.VisibilityIcs.VisibilityWrapper(((android.support.transition.VisibilityInterface) (external)));
        } else {
            mTransition = ((android.support.transition.VisibilityPort) (internal));
        }
    }

    @java.lang.Override
    public boolean isVisible(android.support.transition.TransitionValues values) {
        return ((android.support.transition.VisibilityPort) (mTransition)).isVisible(values);
    }

    @java.lang.Override
    public android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility) {
        return ((android.support.transition.VisibilityPort) (mTransition)).onAppear(sceneRoot, startValues, startVisibility, endValues, endVisibility);
    }

    @java.lang.Override
    public android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility) {
        return ((android.support.transition.VisibilityPort) (mTransition)).onDisappear(sceneRoot, startValues, startVisibility, endValues, endVisibility);
    }

    private static class VisibilityWrapper extends android.support.transition.VisibilityPort {
        private android.support.transition.VisibilityInterface mVisibility;

        VisibilityWrapper(android.support.transition.VisibilityInterface visibility) {
            mVisibility = visibility;
        }

        @java.lang.Override
        public void captureStartValues(android.support.transition.TransitionValues transitionValues) {
            mVisibility.captureStartValues(transitionValues);
        }

        @java.lang.Override
        public void captureEndValues(android.support.transition.TransitionValues transitionValues) {
            mVisibility.captureEndValues(transitionValues);
        }

        @java.lang.Override
        public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, android.support.transition.TransitionValues endValues) {
            return mVisibility.createAnimator(sceneRoot, startValues, endValues);
        }

        @java.lang.Override
        public boolean isVisible(android.support.transition.TransitionValues values) {
            return mVisibility.isVisible(values);
        }

        @java.lang.Override
        public android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility) {
            return mVisibility.onAppear(sceneRoot, startValues, startVisibility, endValues, endVisibility);
        }

        @java.lang.Override
        public android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility) {
            return mVisibility.onDisappear(sceneRoot, startValues, startVisibility, endValues, endVisibility);
        }
    }
}

