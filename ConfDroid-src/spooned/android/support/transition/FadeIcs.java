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


class FadeIcs extends android.support.transition.TransitionIcs implements android.support.transition.VisibilityImpl {
    public FadeIcs(android.support.transition.TransitionInterface transition) {
        init(transition, new android.support.transition.FadePort());
    }

    public FadeIcs(android.support.transition.TransitionInterface transition, int fadingMode) {
        init(transition, new android.support.transition.FadePort(fadingMode));
    }

    @java.lang.Override
    public boolean isVisible(android.support.transition.TransitionValues values) {
        return ((android.support.transition.FadePort) (mTransition)).isVisible(values);
    }

    @java.lang.Override
    public android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility) {
        return ((android.support.transition.FadePort) (mTransition)).onAppear(sceneRoot, startValues, startVisibility, endValues, endVisibility);
    }

    @java.lang.Override
    public android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility) {
        return ((android.support.transition.FadePort) (mTransition)).onDisappear(sceneRoot, startValues, startVisibility, startValues, startVisibility);
    }
}

