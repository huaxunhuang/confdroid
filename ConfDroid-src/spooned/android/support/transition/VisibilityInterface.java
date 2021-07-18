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
 * Used to reference android.support.transition.Visibility in a backward compatible manner.
 */
interface VisibilityInterface extends android.support.transition.TransitionInterface {
    boolean isVisible(android.support.transition.TransitionValues values);

    android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility);

    android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, int startVisibility, android.support.transition.TransitionValues endValues, int endVisibility);
}

