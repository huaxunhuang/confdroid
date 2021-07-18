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


class SceneKitKat extends android.support.transition.SceneWrapper {
    private static java.lang.reflect.Field sEnterAction;

    private static java.lang.reflect.Method sSetCurrentScene;

    private android.view.View mLayout;// alternative to layoutId


    @java.lang.Override
    public void init(android.view.ViewGroup sceneRoot) {
        mScene = new android.transition.Scene(sceneRoot);
    }

    @java.lang.Override
    public void init(android.view.ViewGroup sceneRoot, android.view.View layout) {
        if (layout instanceof android.view.ViewGroup) {
            mScene = new android.transition.Scene(sceneRoot, ((android.view.ViewGroup) (layout)));
        } else {
            mScene = new android.transition.Scene(sceneRoot);
            mLayout = layout;
        }
    }

    @java.lang.Override
    public void enter() {
        if (mLayout != null) {
            // empty out parent container before adding to it
            final android.view.ViewGroup root = getSceneRoot();
            root.removeAllViews();
            root.addView(mLayout);
            invokeEnterAction();
            updateCurrentScene(root);
        } else {
            mScene.enter();
        }
    }

    private void invokeEnterAction() {
        if (android.support.transition.SceneKitKat.sEnterAction == null) {
            try {
                android.support.transition.SceneKitKat.sEnterAction = android.transition.Scene.class.getDeclaredField("mEnterAction");
                android.support.transition.SceneKitKat.sEnterAction.setAccessible(true);
            } catch (java.lang.NoSuchFieldException e) {
                throw new java.lang.RuntimeException(e);
            }
        }
        try {
            final java.lang.Runnable enterAction = ((java.lang.Runnable) (android.support.transition.SceneKitKat.sEnterAction.get(mScene)));
            if (enterAction != null) {
                enterAction.run();
            }
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    /**
     * Sets this Scene as the current scene of the View.
     */
    private void updateCurrentScene(android.view.View view) {
        if (android.support.transition.SceneKitKat.sSetCurrentScene == null) {
            try {
                android.support.transition.SceneKitKat.sSetCurrentScene = android.transition.Scene.class.getDeclaredMethod("setCurrentScene", android.view.View.class, android.transition.Scene.class);
                android.support.transition.SceneKitKat.sSetCurrentScene.setAccessible(true);
            } catch (java.lang.NoSuchMethodException e) {
                throw new java.lang.RuntimeException(e);
            }
        }
        try {
            android.support.transition.SceneKitKat.sSetCurrentScene.invoke(null, view, mScene);
        } catch (java.lang.IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}

