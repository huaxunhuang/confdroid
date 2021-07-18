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


class TransitionManagerIcs extends android.support.transition.TransitionManagerImpl {
    private final android.support.transition.TransitionManagerPort mTransitionManager = new android.support.transition.TransitionManagerPort();

    @java.lang.Override
    public void setTransition(android.support.transition.SceneImpl scene, android.support.transition.TransitionImpl transition) {
        mTransitionManager.setTransition(((android.support.transition.SceneIcs) (scene)).mScene, transition == null ? null : ((android.support.transition.TransitionIcs) (transition)).mTransition);
    }

    @java.lang.Override
    public void setTransition(android.support.transition.SceneImpl fromScene, android.support.transition.SceneImpl toScene, android.support.transition.TransitionImpl transition) {
        mTransitionManager.setTransition(((android.support.transition.SceneIcs) (fromScene)).mScene, ((android.support.transition.SceneIcs) (toScene)).mScene, transition == null ? null : ((android.support.transition.TransitionIcs) (transition)).mTransition);
    }

    @java.lang.Override
    public void transitionTo(android.support.transition.SceneImpl scene) {
        mTransitionManager.transitionTo(((android.support.transition.SceneIcs) (scene)).mScene);
    }
}

