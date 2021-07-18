/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.transition;


/**
 * This class manages the set of transitions that fire when there is a
 * change of {@link Scene}. To use the manager, add scenes along with
 * transition objects with calls to {@link #setTransition(Scene, Transition)}
 * or {@link #setTransition(Scene, Scene, Transition)}. Setting specific
 * transitions for scene changes is not required; by default, a Scene change
 * will use {@link AutoTransition} to do something reasonable for most
 * situations. Specifying other transitions for particular scene changes is
 * only necessary if the application wants different transition behavior
 * in these situations.
 *
 * <p>TransitionManagers can be declared in XML resource files inside the
 * <code>res/transition</code> directory. TransitionManager resources consist of
 * the <code>transitionManager</code>tag name, containing one or more
 * <code>transition</code> tags, each of which describe the relationship of
 * that transition to the from/to scene information in that tag.
 * For example, here is a resource file that declares several scene
 * transitions:</p>
 *
 * {@sample development/samples/ApiDemos/res/transition/transitions_mgr.xml TransitionManager}
 *
 * <p>For each of the <code>fromScene</code> and <code>toScene</code> attributes,
 * there is a reference to a standard XML layout file. This is equivalent to
 * creating a scene from a layout in code by calling
 * {@link Scene#getSceneForLayout(ViewGroup, int, Context)}. For the
 * <code>transition</code> attribute, there is a reference to a resource
 * file in the <code>res/transition</code> directory which describes that
 * transition.</p>
 *
 * Information on XML resource descriptions for transitions can be found for
 * {@link android.R.styleable#Transition}, {@link android.R.styleable#TransitionSet},
 * {@link android.R.styleable#TransitionTarget}, {@link android.R.styleable#Fade},
 * and {@link android.R.styleable#TransitionManager}.
 */
public class TransitionManager {
    // TODO: how to handle enter/exit?
    private static java.lang.String LOG_TAG = "TransitionManager";

    private static android.transition.Transition sDefaultTransition = new android.transition.AutoTransition();

    private static final java.lang.String[] EMPTY_STRINGS = new java.lang.String[0];

    android.util.ArrayMap<android.transition.Scene, android.transition.Transition> mSceneTransitions = new android.util.ArrayMap<android.transition.Scene, android.transition.Transition>();

    android.util.ArrayMap<android.transition.Scene, android.util.ArrayMap<android.transition.Scene, android.transition.Transition>> mScenePairTransitions = new android.util.ArrayMap<android.transition.Scene, android.util.ArrayMap<android.transition.Scene, android.transition.Transition>>();

    @android.annotation.UnsupportedAppUsage
    private static java.lang.ThreadLocal<java.lang.ref.WeakReference<android.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.transition.Transition>>>> sRunningTransitions = new java.lang.ThreadLocal<java.lang.ref.WeakReference<android.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.transition.Transition>>>>();

    @android.annotation.UnsupportedAppUsage
    private static java.util.ArrayList<android.view.ViewGroup> sPendingTransitions = new java.util.ArrayList<android.view.ViewGroup>();

    /**
     * Sets the transition to be used for any scene change for which no
     * other transition is explicitly set. The initial value is
     * an {@link AutoTransition} instance.
     *
     * @param transition
     * 		The default transition to be used for scene changes.
     * @unknown pending later changes
     */
    public void setDefaultTransition(android.transition.Transition transition) {
        android.transition.TransitionManager.sDefaultTransition = transition;
    }

    /**
     * Gets the current default transition. The initial value is an {@link AutoTransition} instance.
     *
     * @return The current default transition.
     * @see #setDefaultTransition(Transition)
     * @unknown pending later changes
     */
    public static android.transition.Transition getDefaultTransition() {
        return android.transition.TransitionManager.sDefaultTransition;
    }

    /**
     * Sets a specific transition to occur when the given scene is entered.
     *
     * @param scene
     * 		The scene which, when applied, will cause the given
     * 		transition to run.
     * @param transition
     * 		The transition that will play when the given scene is
     * 		entered. A value of null will result in the default behavior of
     * 		using the default transition instead.
     */
    public void setTransition(android.transition.Scene scene, android.transition.Transition transition) {
        mSceneTransitions.put(scene, transition);
    }

    /**
     * Sets a specific transition to occur when the given pair of scenes is
     * exited/entered.
     *
     * @param fromScene
     * 		The scene being exited when the given transition will
     * 		be run
     * @param toScene
     * 		The scene being entered when the given transition will
     * 		be run
     * @param transition
     * 		The transition that will play when the given scene is
     * 		entered. A value of null will result in the default behavior of
     * 		using the default transition instead.
     */
    public void setTransition(android.transition.Scene fromScene, android.transition.Scene toScene, android.transition.Transition transition) {
        android.util.ArrayMap<android.transition.Scene, android.transition.Transition> sceneTransitionMap = mScenePairTransitions.get(toScene);
        if (sceneTransitionMap == null) {
            sceneTransitionMap = new android.util.ArrayMap<android.transition.Scene, android.transition.Transition>();
            mScenePairTransitions.put(toScene, sceneTransitionMap);
        }
        sceneTransitionMap.put(fromScene, transition);
    }

    /**
     * Returns the Transition for the given scene being entered. The result
     * depends not only on the given scene, but also the scene which the
     * {@link Scene#getSceneRoot() sceneRoot} of the Scene is currently in.
     *
     * @param scene
     * 		The scene being entered
     * @return The Transition to be used for the given scene change. If no
    Transition was specified for this scene change, the default transition
    will be used instead.
     * @unknown 
     */
    @android.annotation.TestApi
    public android.transition.Transition getTransition(android.transition.Scene scene) {
        android.transition.Transition transition = null;
        android.view.ViewGroup sceneRoot = scene.getSceneRoot();
        if (sceneRoot != null) {
            // TODO: cached in Scene instead? long-term, cache in View itself
            android.transition.Scene currScene = android.transition.Scene.getCurrentScene(sceneRoot);
            if (currScene != null) {
                android.util.ArrayMap<android.transition.Scene, android.transition.Transition> sceneTransitionMap = mScenePairTransitions.get(scene);
                if (sceneTransitionMap != null) {
                    transition = sceneTransitionMap.get(currScene);
                    if (transition != null) {
                        return transition;
                    }
                }
            }
        }
        transition = mSceneTransitions.get(scene);
        return transition != null ? transition : android.transition.TransitionManager.sDefaultTransition;
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
    private static void changeScene(android.transition.Scene scene, android.transition.Transition transition) {
        final android.view.ViewGroup sceneRoot = scene.getSceneRoot();
        if (!android.transition.TransitionManager.sPendingTransitions.contains(sceneRoot)) {
            android.transition.Scene oldScene = android.transition.Scene.getCurrentScene(sceneRoot);
            if (transition == null) {
                // Notify old scene that it is being exited
                if (oldScene != null) {
                    oldScene.exit();
                }
                scene.enter();
            } else {
                android.transition.TransitionManager.sPendingTransitions.add(sceneRoot);
                android.transition.Transition transitionClone = transition.clone();
                transitionClone.setSceneRoot(sceneRoot);
                if ((oldScene != null) && oldScene.isCreatedFromLayoutResource()) {
                    transitionClone.setCanRemoveViews(true);
                }
                android.transition.TransitionManager.sceneChangeSetup(sceneRoot, transitionClone);
                scene.enter();
                android.transition.TransitionManager.sceneChangeRunTransition(sceneRoot, transitionClone);
            }
        }
    }

    @android.annotation.UnsupportedAppUsage
    private static android.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.transition.Transition>> getRunningTransitions() {
        java.lang.ref.WeakReference<android.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.transition.Transition>>> runningTransitions = android.transition.TransitionManager.sRunningTransitions.get();
        if (runningTransitions != null) {
            android.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.transition.Transition>> transitions = runningTransitions.get();
            if (transitions != null) {
                return transitions;
            }
        }
        android.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.transition.Transition>> transitions = new android.util.ArrayMap();
        runningTransitions = new java.lang.ref.WeakReference(transitions);
        android.transition.TransitionManager.sRunningTransitions.set(runningTransitions);
        return transitions;
    }

    private static void sceneChangeRunTransition(final android.view.ViewGroup sceneRoot, final android.transition.Transition transition) {
        if ((transition != null) && (sceneRoot != null)) {
            android.transition.TransitionManager.MultiListener listener = new android.transition.TransitionManager.MultiListener(transition, sceneRoot);
            sceneRoot.addOnAttachStateChangeListener(listener);
            sceneRoot.getViewTreeObserver().addOnPreDrawListener(listener);
        }
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
        android.transition.Transition mTransition;

        android.view.ViewGroup mSceneRoot;

        final android.view.ViewTreeObserver mViewTreeObserver;

        MultiListener(android.transition.Transition transition, android.view.ViewGroup sceneRoot) {
            mTransition = transition;
            mSceneRoot = sceneRoot;
            mViewTreeObserver = mSceneRoot.getViewTreeObserver();
        }

        private void removeListeners() {
            if (mViewTreeObserver.isAlive()) {
                mViewTreeObserver.removeOnPreDrawListener(this);
            } else {
                mSceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            }
            mSceneRoot.removeOnAttachStateChangeListener(this);
        }

        @java.lang.Override
        public void onViewAttachedToWindow(android.view.View v) {
        }

        @java.lang.Override
        public void onViewDetachedFromWindow(android.view.View v) {
            removeListeners();
            android.transition.TransitionManager.sPendingTransitions.remove(mSceneRoot);
            java.util.ArrayList<android.transition.Transition> runningTransitions = android.transition.TransitionManager.getRunningTransitions().get(mSceneRoot);
            if ((runningTransitions != null) && (runningTransitions.size() > 0)) {
                for (android.transition.Transition runningTransition : runningTransitions) {
                    runningTransition.resume(mSceneRoot);
                }
            }
            mTransition.clearValues(true);
        }

        @java.lang.Override
        public boolean onPreDraw() {
            removeListeners();
            // Don't start the transition if it's no longer pending.
            if (!android.transition.TransitionManager.sPendingTransitions.remove(mSceneRoot)) {
                return true;
            }
            // Add to running list, handle end to remove it
            final android.util.ArrayMap<android.view.ViewGroup, java.util.ArrayList<android.transition.Transition>> runningTransitions = android.transition.TransitionManager.getRunningTransitions();
            java.util.ArrayList<android.transition.Transition> currentTransitions = runningTransitions.get(mSceneRoot);
            java.util.ArrayList<android.transition.Transition> previousRunningTransitions = null;
            if (currentTransitions == null) {
                currentTransitions = new java.util.ArrayList<android.transition.Transition>();
                runningTransitions.put(mSceneRoot, currentTransitions);
            } else
                if (currentTransitions.size() > 0) {
                    previousRunningTransitions = new java.util.ArrayList<android.transition.Transition>(currentTransitions);
                }

            currentTransitions.add(mTransition);
            mTransition.addListener(new android.transition.TransitionListenerAdapter() {
                @java.lang.Override
                public void onTransitionEnd(android.transition.Transition transition) {
                    java.util.ArrayList<android.transition.Transition> currentTransitions = runningTransitions.get(mSceneRoot);
                    currentTransitions.remove(transition);
                    transition.removeListener(this);
                }
            });
            mTransition.captureValues(mSceneRoot, false);
            if (previousRunningTransitions != null) {
                for (android.transition.Transition runningTransition : previousRunningTransitions) {
                    runningTransition.resume(mSceneRoot);
                }
            }
            mTransition.playTransition(mSceneRoot);
            return true;
        }
    }

    private static void sceneChangeSetup(android.view.ViewGroup sceneRoot, android.transition.Transition transition) {
        // Capture current values
        java.util.ArrayList<android.transition.Transition> runningTransitions = android.transition.TransitionManager.getRunningTransitions().get(sceneRoot);
        if ((runningTransitions != null) && (runningTransitions.size() > 0)) {
            for (android.transition.Transition runningTransition : runningTransitions) {
                runningTransition.pause(sceneRoot);
            }
        }
        if (transition != null) {
            transition.captureValues(sceneRoot, true);
        }
        // Notify previous scene that it is being exited
        android.transition.Scene previousScene = android.transition.Scene.getCurrentScene(sceneRoot);
        if (previousScene != null) {
            previousScene.exit();
        }
    }

    /**
     * Change to the given scene, using the
     * appropriate transition for this particular scene change
     * (as specified to the TransitionManager, or the default
     * if no such transition exists).
     *
     * @param scene
     * 		The Scene to change to
     */
    public void transitionTo(android.transition.Scene scene) {
        // Auto transition if there is no transition declared for the Scene, but there is
        // a root or parent view
        android.transition.TransitionManager.changeScene(scene, getTransition(scene));
    }

    /**
     * Convenience method to simply change to the given scene using
     * the default transition for TransitionManager.
     *
     * @param scene
     * 		The Scene to change to
     */
    public static void go(android.transition.Scene scene) {
        android.transition.TransitionManager.changeScene(scene, android.transition.TransitionManager.sDefaultTransition);
    }

    /**
     * Convenience method to simply change to the given scene using
     * the given transition.
     *
     * <p>Passing in <code>null</code> for the transition parameter will
     * result in the scene changing without any transition running, and is
     * equivalent to calling {@link Scene#exit()} on the scene root's
     * current scene, followed by {@link Scene#enter()} on the scene
     * specified by the <code>scene</code> parameter.</p>
     *
     * @param scene
     * 		The Scene to change to
     * @param transition
     * 		The transition to use for this scene change. A
     * 		value of null causes the scene change to happen with no transition.
     */
    public static void go(android.transition.Scene scene, android.transition.Transition transition) {
        android.transition.TransitionManager.changeScene(scene, transition);
    }

    /**
     * Convenience method to animate, using the default transition,
     * to a new scene defined by all changes within the given scene root between
     * calling this method and the next rendering frame.
     * Equivalent to calling {@link #beginDelayedTransition(ViewGroup, Transition)}
     * with a value of <code>null</code> for the <code>transition</code> parameter.
     *
     * @param sceneRoot
     * 		The root of the View hierarchy to run the transition on.
     */
    public static void beginDelayedTransition(final android.view.ViewGroup sceneRoot) {
        android.transition.TransitionManager.beginDelayedTransition(sceneRoot, null);
    }

    /**
     * Convenience method to animate to a new scene defined by all changes within
     * the given scene root between calling this method and the next rendering frame.
     * Calling this method causes TransitionManager to capture current values in the
     * scene root and then post a request to run a transition on the next frame.
     * At that time, the new values in the scene root will be captured and changes
     * will be animated. There is no need to create a Scene; it is implied by
     * changes which take place between calling this method and the next frame when
     * the transition begins.
     *
     * <p>Calling this method several times before the next frame (for example, if
     * unrelated code also wants to make dynamic changes and run a transition on
     * the same scene root), only the first call will trigger capturing values
     * and exiting the current scene. Subsequent calls to the method with the
     * same scene root during the same frame will be ignored.</p>
     *
     * <p>Passing in <code>null</code> for the transition parameter will
     * cause the TransitionManager to use its default transition.</p>
     *
     * @param sceneRoot
     * 		The root of the View hierarchy to run the transition on.
     * @param transition
     * 		The transition to use for this change. A
     * 		value of null causes the TransitionManager to use the default transition.
     */
    public static void beginDelayedTransition(final android.view.ViewGroup sceneRoot, android.transition.Transition transition) {
        if ((!android.transition.TransitionManager.sPendingTransitions.contains(sceneRoot)) && sceneRoot.isLaidOut()) {
            if (android.transition.Transition.DBG) {
                android.util.Log.d(android.transition.TransitionManager.LOG_TAG, (("beginDelayedTransition: root, transition = " + sceneRoot) + ", ") + transition);
            }
            android.transition.TransitionManager.sPendingTransitions.add(sceneRoot);
            if (transition == null) {
                transition = android.transition.TransitionManager.sDefaultTransition;
            }
            final android.transition.Transition transitionClone = transition.clone();
            android.transition.TransitionManager.sceneChangeSetup(sceneRoot, transitionClone);
            android.transition.Scene.setCurrentScene(sceneRoot, null);
            android.transition.TransitionManager.sceneChangeRunTransition(sceneRoot, transitionClone);
        }
    }

    /**
     * Ends all pending and ongoing transitions on the specified scene root.
     *
     * @param sceneRoot
     * 		The root of the View hierarchy to end transitions on.
     */
    public static void endTransitions(final android.view.ViewGroup sceneRoot) {
        android.transition.TransitionManager.sPendingTransitions.remove(sceneRoot);
        final java.util.ArrayList<android.transition.Transition> runningTransitions = android.transition.TransitionManager.getRunningTransitions().get(sceneRoot);
        if ((runningTransitions != null) && (!runningTransitions.isEmpty())) {
            // Make a copy in case this is called by an onTransitionEnd listener
            java.util.ArrayList<android.transition.Transition> copy = new java.util.ArrayList(runningTransitions);
            for (int i = copy.size() - 1; i >= 0; i--) {
                final android.transition.Transition transition = copy.get(i);
                transition.forceToEnd(sceneRoot);
            }
        }
    }
}

