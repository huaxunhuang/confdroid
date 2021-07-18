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


class SceneIcs extends android.support.transition.SceneImpl {
    /* package */
    android.support.transition.ScenePort mScene;

    @java.lang.Override
    public void init(android.view.ViewGroup sceneRoot) {
        mScene = new android.support.transition.ScenePort(sceneRoot);
    }

    @java.lang.Override
    public void init(android.view.ViewGroup sceneRoot, android.view.View layout) {
        mScene = new android.support.transition.ScenePort(sceneRoot, layout);
    }

    @java.lang.Override
    public void enter() {
        mScene.enter();
    }

    @java.lang.Override
    public void exit() {
        mScene.exit();
    }

    @java.lang.Override
    public android.view.ViewGroup getSceneRoot() {
        return mScene.getSceneRoot();
    }

    @java.lang.Override
    public void setEnterAction(java.lang.Runnable action) {
        mScene.setEnterAction(action);
    }

    @java.lang.Override
    public void setExitAction(java.lang.Runnable action) {
        mScene.setExitAction(action);
    }
}

