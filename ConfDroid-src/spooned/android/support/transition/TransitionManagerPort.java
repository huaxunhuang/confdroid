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


class TransitionManagerPort {
    // TODO: how to handle enter/exit?
    private static final java.lang.String[] EMPTY_STRINGS = new java.lang.String[0];

    private static java.lang.String LOG_TAG = "TransitionManager";

    private static android.support.transition.TransitionPort sDefaultTransition = new android.support.transition.AutoTransitionPort();

    private static java.lang.ThreadLocal<java.lang.ref.WeakReference<android.support.v4.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.support.transition.TransitionPort>>>> sRunningTransitions = new java.lang.ThreadLocal<>();

    static java.util.ArrayList<android.view.ViewGroup> sPendingTransitions = new java.util.ArrayList<>();

    android.support.v4.util.ArrayMap<android.support.transition.ScenePort, android.support.transition.TransitionPort> mSceneTransitions = new android.support.v4.util.ArrayMap<>();

    android.support.v4.util.ArrayMap<android.support.transition.ScenePort, android.support.v4.util.ArrayMap<android.support.transition.ScenePort, android.support.transition.TransitionPort>> mScenePairTransitions = new android.support.v4.util.ArrayMap<>();

    android.support.v4.util.ArrayMap<android.support.transition.ScenePort, android.support.v4.util.ArrayMap<java.lang.String, android.support.transition.TransitionPort>> mSceneNameTransitions = new android.support.v4.util.ArrayMap<>();

    android.support.v4.util.ArrayMap<java.lang.String, android.support.v4.util.ArrayMap<android.support.transition.ScenePort, android.support.transition.TransitionPort>> mNameSceneTransitions = new android.support.v4.util.ArrayMap<>();

    /**
     * Gets the current default transition. The initial value is an {@link AutoTransition} instance.
     *
     * @return The current default transition.
     * @unknown pending later changes
     * @see #setDefaultTransition(TransitionPort)
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static android.support.transition.TransitionPort getDefaultTransition() {
        return android.support.transition.TransitionManagerPort.sDefaultTransition;
    }

    /**
     * Sets the transition to be used for any scene change for which no
     * other transition is explicitly set. The initial value is
     * an {@link AutoTransition} instance.
     *
     * @param transition
     * 		The default transition to be used for scene changes.
     * @unknown pending later changes
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public void setDefaultTransition(android.support.transition.TransitionPort transition) {
        android.support.transition.TransitionManagerPort.sDefaultTransition = transition;
    }

    /**
     * This is where all of the work of a transition/scene-change is
     * orchestrated. This method captures the start values for the given
     * transition, exits the current Scene, enters the new scene, captures
     * the end values for the transition, and finally plays the
     * resulting values-populated transition.
     *
     * @param scene
     * 		The scene being entered
     * @param transition
     * 		The transition to play for this scene change
     */
    private static void changeScene(android.support.transition.ScenePort scene, android.support.transition.TransitionPort transition) {
        final android.view.ViewGroup sceneRoot = scene.getSceneRoot();
        android.support.transition.TransitionPort transitionClone = null;
        if (transition != null) {
            transitionClone = transition.clone();
            transitionClone.setSceneRoot(sceneRoot);
        }
        android.support.transition.ScenePort oldScene = android.support.transition.ScenePort.getCurrentScene(sceneRoot);
        if ((oldScene != null) && oldScene.isCreatedFromLayoutResource()) {
            transitionClone.setCanRemoveViews(true);
        }
        android.support.transition.TransitionManagerPort.sceneChangeSetup(sceneRoot, transitionClone);
        scene.enter();
        android.support.transition.TransitionManagerPort.sceneChangeRunTransition(sceneRoot, transitionClone);
    }

    static android.support.v4.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.support.transition.TransitionPort>> getRunningTransitions() {
        java.lang.ref.WeakReference<android.support.v4.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.support.transition.TransitionPort>>> runningTransitions = android.support.transition.TransitionManagerPort.sRunningTransitions.get();
        if ((runningTransitions == null) || (runningTransitions.get() == null)) {
            android.support.v4.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.support.transition.TransitionPort>> transitions = new android.support.v4.util.ArrayMap<>();
            runningTransitions = new java.lang.ref.WeakReference<>(transitions);
            android.support.transition.TransitionManagerPort.sRunningTransitions.set(runningTransitions);
        }
        return runningTransitions.get();
    }

    private static void sceneChangeRunTransition(final android.view.ViewGroup sceneRoot, final android.support.transition.TransitionPort transition) {
        if ((transition != null) && (sceneRoot != null)) {
            android.support.transition.TransitionManagerPort.MultiListener listener = new android.support.transition.TransitionManagerPort.MultiListener(transition, sceneRoot);
            sceneRoot.addOnAttachStateChangeListener(listener);
            sceneRoot.getViewTreeObserver().addOnPreDrawListener(listener);
        }
    }

    private static void sceneChangeSetup(android.view.ViewGroup sceneRoot, android.support.transition.TransitionPort transition) {
        // Capture current values
        java.util.ArrayList<android.support.transition.TransitionPort> runningTransitions = android.support.transition.TransitionManagerPort.getRunningTransitions().get(sceneRoot);
        if ((runningTransitions != null) && (runningTransitions.size() > 0)) {
            for (android.support.transition.TransitionPort runningTransition : runningTransitions) {
                runningTransition.pause(sceneRoot);
            }
        }
        if (transition != null) {
            transition.captureValues(sceneRoot, true);
        }
        // Notify previous scene that it is being exited
        android.support.transition.ScenePort previousScene = android.support.transition.ScenePort.getCurrentScene(sceneRoot);
        if (previousScene != null) {
            previousScene.exit();
        }
    }

    public static void go(android.support.transition.ScenePort scene) {
        android.support.transition.TransitionManagerPort.changeScene(scene, android.support.transition.TransitionManagerPort.sDefaultTransition);
    }

    public static void go(android.support.transition.ScenePort scene, android.support.transition.TransitionPort transition) {
        android.support.transition.TransitionManagerPort.changeScene(scene, transition);
    }

    public static void beginDelayedTransition(final android.view.ViewGroup sceneRoot) {
        android.support.transition.TransitionManagerPort.beginDelayedTransition(sceneRoot, null);
    }

    public static void beginDelayedTransition(final android.view.ViewGroup sceneRoot, android.support.transition.TransitionPort transition) {
        if ((!android.support.transition.TransitionManagerPort.sPendingTransitions.contains(sceneRoot)) && android.support.v4.view.ViewCompat.isLaidOut(sceneRoot)) {
            if (android.support.transition.TransitionPort.DBG) {
                android.util.Log.d(android.support.transition.TransitionManagerPort.LOG_TAG, (("beginDelayedTransition: root, transition = " + sceneRoot) + ", ") + transition);
            }
            android.support.transition.TransitionManagerPort.sPendingTransitions.add(sceneRoot);
            if (transition == null) {
                transition = android.support.transition.TransitionManagerPort.sDefaultTransition;
            }
            final android.support.transition.TransitionPort transitionClone = transition.clone();
            android.support.transition.TransitionManagerPort.sceneChangeSetup(sceneRoot, transitionClone);
            android.support.transition.ScenePort.setCurrentScene(sceneRoot, null);
            android.support.transition.TransitionManagerPort.sceneChangeRunTransition(sceneRoot, transitionClone);
        }
    }

    public void setTransition(android.support.transition.ScenePort scene, android.support.transition.TransitionPort transition) {
        mSceneTransitions.put(scene, transition);
    }

    public void setTransition(android.support.transition.ScenePort fromScene, android.support.transition.ScenePort toScene, android.support.transition.TransitionPort transition) {
        android.support.v4.util.ArrayMap<android.support.transition.ScenePort, android.support.transition.TransitionPort> sceneTransitionMap = mScenePairTransitions.get(toScene);
        if (sceneTransitionMap == null) {
            sceneTransitionMap = new android.support.v4.util.ArrayMap<>();
            mScenePairTransitions.put(toScene, sceneTransitionMap);
        }
        sceneTransitionMap.put(fromScene, transition);
    }

    /**
     * Returns the Transition for the given scene being entered. The result
     * depends not only on the given scene, but also the scene which the
     * {@link ScenePort#getSceneRoot() sceneRoot} of the Scene is currently in.
     *
     * @param scene
     * 		The scene being entered
     * @return The Transition to be used for the given scene change. If no
    Transition was specified for this scene change, the default transition
    will be used instead.
     */
    private android.support.transition.TransitionPort getTransition(android.support.transition.ScenePort scene) {
        android.support.transition.TransitionPort transition;
        android.view.ViewGroup sceneRoot = scene.getSceneRoot();
        if (sceneRoot != null) {
            // TODO: cached in Scene instead? long-term, cache in View itself
            android.support.transition.ScenePort currScene = android.support.transition.ScenePort.getCurrentScene(sceneRoot);
            if (currScene != null) {
                android.support.v4.util.ArrayMap<android.support.transition.ScenePort, android.support.transition.TransitionPort> sceneTransitionMap = mScenePairTransitions.get(scene);
                if (sceneTransitionMap != null) {
                    transition = sceneTransitionMap.get(currScene);
                    if (transition != null) {
                        return transition;
                    }
                }
            }
        }
        transition = mSceneTransitions.get(scene);
        return transition != null ? transition : android.support.transition.TransitionManagerPort.sDefaultTransition;
    }

    /**
     * Retrieve the transition from a named scene to a target defined scene if one has been
     * associated with this TransitionManager.
     *
     * <p>A named scene is an indirect link for a transition. Fundamentally a named
     * scene represents a potentially arbitrary intersection point of two otherwise independent
     * transitions. Activity A may define a transition from scene X to "com.example.scene.FOO"
     * while activity B may define a transition from scene "com.example.scene.FOO" to scene Y.
     * In this way applications may define an API for more sophisticated transitions between
     * caller and called activities very similar to the way that <code>Intent</code> extras
     * define APIs for arguments and data propagation between activities.</p>
     *
     * @param fromName
     * 		Named scene that this transition corresponds to
     * @param toScene
     * 		Target scene that this transition will move to
     * @return Transition corresponding to the given fromName and toScene or null
    if no association exists in this TransitionManager
     * @see #setTransition(String, ScenePort, TransitionPort)
     */
    public android.support.transition.TransitionPort getNamedTransition(java.lang.String fromName, android.support.transition.ScenePort toScene) {
        android.support.v4.util.ArrayMap<android.support.transition.ScenePort, android.support.transition.TransitionPort> m = mNameSceneTransitions.get(fromName);
        if (m != null) {
            return m.get(toScene);
        }
        return null;
    }

    /**
     * Retrieve the transition from a defined scene to a target named scene if one has been
     * associated with this TransitionManager.
     *
     * <p>A named scene is an indirect link for a transition. Fundamentally a named
     * scene represents a potentially arbitrary intersection point of two otherwise independent
     * transitions. Activity A may define a transition from scene X to "com.example.scene.FOO"
     * while activity B may define a transition from scene "com.example.scene.FOO" to scene Y.
     * In this way applications may define an API for more sophisticated transitions between
     * caller and called activities very similar to the way that <code>Intent</code> extras
     * define APIs for arguments and data propagation between activities.</p>
     *
     * @param fromScene
     * 		Scene that this transition starts from
     * @param toName
     * 		Name of the target scene
     * @return Transition corresponding to the given fromScene and toName or null
    if no association exists in this TransitionManager
     */
    public android.support.transition.TransitionPort getNamedTransition(android.support.transition.ScenePort fromScene, java.lang.String toName) {
        android.support.v4.util.ArrayMap<java.lang.String, android.support.transition.TransitionPort> m = mSceneNameTransitions.get(fromScene);
        if (m != null) {
            return m.get(toName);
        }
        return null;
    }

    /**
     * Retrieve the supported target named scenes when transitioning away from the given scene.
     *
     * <p>A named scene is an indirect link for a transition. Fundamentally a named
     * scene represents a potentially arbitrary intersection point of two otherwise independent
     * transitions. Activity A may define a transition from scene X to "com.example.scene.FOO"
     * while activity B may define a transition from scene "com.example.scene.FOO" to scene Y.
     * In this way applications may define an API for more sophisticated transitions between
     * caller and called activities very similar to the way that <code>Intent</code> extras
     * define APIs for arguments and data propagation between activities.</p>
     *
     * @param fromScene
     * 		Scene to transition from
     * @return An array of Strings naming each supported transition starting from
    <code>fromScene</code>. If no transitions to a named scene from the given
    scene are supported this function will return a String[] of length 0.
     * @see #setTransition(ScenePort, String, TransitionPort)
     */
    public java.lang.String[] getTargetSceneNames(android.support.transition.ScenePort fromScene) {
        final android.support.v4.util.ArrayMap<java.lang.String, android.support.transition.TransitionPort> m = mSceneNameTransitions.get(fromScene);
        if (m == null) {
            return android.support.transition.TransitionManagerPort.EMPTY_STRINGS;
        }
        final int count = m.size();
        final java.lang.String[] result = new java.lang.String[count];
        for (int i = 0; i < count; i++) {
            result[i] = m.keyAt(i);
        }
        return result;
    }

    /**
     * Set a transition from a specific scene to a named scene.
     *
     * <p>A named scene is an indirect link for a transition. Fundamentally a named
     * scene represents a potentially arbitrary intersection point of two otherwise independent
     * transitions. Activity A may define a transition from scene X to "com.example.scene.FOO"
     * while activity B may define a transition from scene "com.example.scene.FOO" to scene Y.
     * In this way applications may define an API for more sophisticated transitions between
     * caller and called activities very similar to the way that <code>Intent</code> extras
     * define APIs for arguments and data propagation between activities.</p>
     *
     * @param fromScene
     * 		Scene to transition from
     * @param toName
     * 		Named scene to transition to
     * @param transition
     * 		Transition to use
     * @see #getTargetSceneNames(ScenePort)
     */
    public void setTransition(android.support.transition.ScenePort fromScene, java.lang.String toName, android.support.transition.TransitionPort transition) {
        android.support.v4.util.ArrayMap<java.lang.String, android.support.transition.TransitionPort> m = mSceneNameTransitions.get(fromScene);
        if (m == null) {
            m = new android.support.v4.util.ArrayMap<>();
            mSceneNameTransitions.put(fromScene, m);
        }
        m.put(toName, transition);
    }

    /**
     * Set a transition from a named scene to a concrete scene.
     *
     * <p>A named scene is an indirect link for a transition. Fundamentally a named
     * scene represents a potentially arbitrary intersection point of two otherwise independent
     * transitions. Activity A may define a transition from scene X to "com.example.scene.FOO"
     * while activity B may define a transition from scene "com.example.scene.FOO" to scene Y.
     * In this way applications may define an API for more sophisticated transitions between
     * caller and called activities very similar to the way that <code>Intent</code> extras
     * define APIs for arguments and data propagation between activities.</p>
     *
     * @param fromName
     * 		Named scene to transition from
     * @param toScene
     * 		Scene to transition to
     * @param transition
     * 		Transition to use
     * @see #getNamedTransition(String, ScenePort)
     */
    public void setTransition(java.lang.String fromName, android.support.transition.ScenePort toScene, android.support.transition.TransitionPort transition) {
        android.support.v4.util.ArrayMap<android.support.transition.ScenePort, android.support.transition.TransitionPort> m = mNameSceneTransitions.get(fromName);
        if (m == null) {
            m = new android.support.v4.util.ArrayMap<>();
            mNameSceneTransitions.put(fromName, m);
        }
        m.put(toScene, transition);
    }

    public void transitionTo(android.support.transition.ScenePort scene) {
        // Auto transition if there is no transition declared for the Scene, but there is
        // a root or parent view
        android.support.transition.TransitionManagerPort.changeScene(scene, getTransition(scene));
    }

    /**
     * This private utility class is used to listen for both OnPreDraw and
     * OnAttachStateChange events. OnPreDraw events are the main ones we care
     * about since that's what triggers the transition to take place.
     * OnAttachStateChange events are also important in case the view is removed
     * from the hierarchy before the OnPreDraw event takes place; it's used to
     * clean up things since the OnPreDraw listener didn't get called in time.
     */
    private static class MultiListener implements android.view.View.OnAttachStateChangeListener , android.view.ViewTreeObserver.OnPreDrawListener {
        android.support.transition.TransitionPort mTransition;

        android.view.ViewGroup mSceneRoot;

        MultiListener(android.support.transition.TransitionPort transition, android.view.ViewGroup sceneRoot) {
            mTransition = transition;
            mSceneRoot = sceneRoot;
        }

        private void removeListeners() {
            mSceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            mSceneRoot.removeOnAttachStateChangeListener(this);
        }

        @java.lang.Override
        public void onViewAttachedToWindow(android.view.View v) {
        }

        @java.lang.Override
        public void onViewDetachedFromWindow(android.view.View v) {
            removeListeners();
            android.support.transition.TransitionManagerPort.sPendingTransitions.remove(mSceneRoot);
            java.util.ArrayList<android.support.transition.TransitionPort> runningTransitions = android.support.transition.TransitionManagerPort.getRunningTransitions().get(mSceneRoot);
            if ((runningTransitions != null) && (runningTransitions.size() > 0)) {
                for (android.support.transition.TransitionPort runningTransition : runningTransitions) {
                    runningTransition.resume(mSceneRoot);
                }
            }
            mTransition.clearValues(true);
        }

        @java.lang.Override
        public boolean onPreDraw() {
            removeListeners();
            android.support.transition.TransitionManagerPort.sPendingTransitions.remove(mSceneRoot);
            // Add to running list, handle end to remove it
            final android.support.v4.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.support.transition.TransitionPort>> runningTransitions = android.support.transition.TransitionManagerPort.getRunningTransitions();
            java.util.ArrayList<android.support.transition.TransitionPort> currentTransitions = runningTransitions.get(mSceneRoot);
            java.util.ArrayList<android.support.transition.TransitionPort> previousRunningTransitions = null;
            if (currentTransitions == null) {
                currentTransitions = new java.util.ArrayList<>();
                runningTransitions.put(mSceneRoot, currentTransitions);
            } else
                if (currentTransitions.size() > 0) {
                    previousRunningTransitions = new java.util.ArrayList<>(currentTransitions);
                }

            currentTransitions.add(mTransition);
            mTransition.addListener(new android.support.transition.TransitionPort.TransitionListenerAdapter() {
                @java.lang.Override
                public void onTransitionEnd(android.support.transition.TransitionPort transition) {
                    java.util.ArrayList<android.support.transition.TransitionPort> currentTransitions = runningTransitions.get(mSceneRoot);
                    currentTransitions.remove(transition);
                }
            });
            mTransition.captureValues(mSceneRoot, false);
            if (previousRunningTransitions != null) {
                for (android.support.transition.TransitionPort runningTransition : previousRunningTransitions) {
                    runningTransition.resume(mSceneRoot);
                }
            }
            mTransition.playTransition(mSceneRoot);
            return true;
        }
    }
}

