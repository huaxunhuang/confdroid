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


class TransitionManagerStaticsIcs extends android.support.transition.TransitionManagerStaticsImpl {
    @java.lang.Override
    public void go(android.support.transition.SceneImpl scene) {
        android.support.transition.TransitionManagerPort.go(((android.support.transition.SceneIcs) (scene)).mScene);
    }

    @java.lang.Override
    public void go(android.support.transition.SceneImpl scene, android.support.transition.TransitionImpl transition) {
        android.support.transition.TransitionManagerPort.go(((android.support.transition.SceneIcs) (scene)).mScene, transition == null ? null : ((android.support.transition.TransitionIcs) (transition)).mTransition);
    }

    @java.lang.Override
    public void beginDelayedTransition(android.view.ViewGroup sceneRoot) {
        android.support.transition.TransitionManagerPort.beginDelayedTransition(sceneRoot);
    }

    @java.lang.Override
    public void beginDelayedTransition(android.view.ViewGroup sceneRoot, android.support.transition.TransitionImpl transition) {
        android.support.transition.TransitionManagerPort.beginDelayedTransition(sceneRoot, transition == null ? null : ((android.support.transition.TransitionIcs) (transition)).mTransition);
    }
}

