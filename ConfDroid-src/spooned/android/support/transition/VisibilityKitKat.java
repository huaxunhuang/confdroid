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


class VisibilityKitKat extends android.support.transition.TransitionKitKat implements android.support.transition.VisibilityImpl {
    @java.lang.Override
    public void init(android.support.transition.TransitionInterface external, java.lang.Object internal) {
        mExternalTransition = external;
        if (internal == null) {
            mTransition = new android.support.transition.VisibilityKitKat.VisibilityWrapper(((android.support.transition.VisibilityInterface) (external)));
        } else {
            mTransition = ((android.transition.Visibility) (internal));
        }
    }

    @java.lang.Override
    public boolean isVisible(android.support.transition.TransitionValues values) {
        return ((android.transition.Visibility) (mTransition)).isVisible(android.support.transition.TransitionKitKat.convertToPlatform(values));
    }

    @java.lang.Override
    public android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility) {
        return ((android.transition.Visibility) (mTransition)).onAppear(sceneRoot, android.support.transition.TransitionKitKat.convertToPlatform(startValues), startVisibility, android.support.transition.TransitionKitKat.convertToPlatform(endValues), endVisibility);
    }

    @java.lang.Override
    public android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility) {
        return ((android.transition.Visibility) (mTransition)).onDisappear(sceneRoot, android.support.transition.TransitionKitKat.convertToPlatform(startValues), startVisibility, android.support.transition.TransitionKitKat.convertToPlatform(endValues), endVisibility);
    }

    private static class VisibilityWrapper extends android.transition.Visibility {
        private final android.support.transition.VisibilityInterface mVisibility;

        VisibilityWrapper(android.support.transition.VisibilityInterface visibility) {
            mVisibility = visibility;
        }

        @java.lang.Override
        public void captureStartValues(android.transition.TransitionValues transitionValues) {
            android.support.transition.TransitionKitKat.wrapCaptureStartValues(mVisibility, transitionValues);
        }

        @java.lang.Override
        public void captureEndValues(android.transition.TransitionValues transitionValues) {
            android.support.transition.TransitionKitKat.wrapCaptureEndValues(mVisibility, transitionValues);
        }

        @java.lang.Override
        public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
            return mVisibility.createAnimator(sceneRoot, android.support.transition.TransitionKitKat.convertToSupport(startValues), android.support.transition.TransitionKitKat.convertToSupport(endValues));
        }

        @java.lang.Override
        public boolean isVisible(android.transition.TransitionValues values) {
            if (values == null) {
                return false;
            }
            android.support.transition.TransitionValues externalValues = new android.support.transition.TransitionValues();
            android.support.transition.TransitionKitKat.copyValues(values, externalValues);
            return mVisibility.isVisible(externalValues);
        }

        @java.lang.Override
        public android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, int startVisibility, android.transition.TransitionValues endValues, int endVisibility) {
            return mVisibility.onAppear(sceneRoot, android.support.transition.TransitionKitKat.convertToSupport(startValues), startVisibility, android.support.transition.TransitionKitKat.convertToSupport(endValues), endVisibility);
        }

        @java.lang.Override
        public android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, int startVisibility, android.transition.TransitionValues endValues, int endVisibility) {
            return mVisibility.onDisappear(sceneRoot, android.support.transition.TransitionKitKat.convertToSupport(startValues), startVisibility, android.support.transition.TransitionKitKat.convertToSupport(endValues), endVisibility);
        }
    }
}

