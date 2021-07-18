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


class SceneStaticsApi21 extends android.support.transition.SceneStaticsImpl {
    @java.lang.Override
    public android.support.transition.SceneImpl getSceneForLayout(android.view.ViewGroup sceneRoot, int layoutId, android.content.Context context) {
        android.support.transition.SceneApi21 scene = new android.support.transition.SceneApi21();
        scene.mScene = android.transition.Scene.getSceneForLayout(sceneRoot, layoutId, context);
        return scene;
    }
}

