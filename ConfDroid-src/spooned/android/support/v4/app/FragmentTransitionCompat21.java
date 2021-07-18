/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v4.app;


class FragmentTransitionCompat21 {
    public static java.lang.String getTransitionName(android.view.View view) {
        return view.getTransitionName();
    }

    public static java.lang.Object cloneTransition(java.lang.Object transition) {
        if (transition != null) {
            transition = ((android.transition.Transition) (transition)).clone();
        }
        return transition;
    }

    public static java.lang.Object captureExitingViews(java.lang.Object exitTransition, android.view.View root, java.util.ArrayList<android.view.View> viewList, java.util.Map<java.lang.String, android.view.View> namedViews, android.view.View nonExistentView) {
        if (exitTransition != null) {
            android.support.v4.app.FragmentTransitionCompat21.captureTransitioningViews(viewList, root);
            if (namedViews != null) {
                viewList.removeAll(namedViews.values());
            }
            if (viewList.isEmpty()) {
                exitTransition = null;
            } else {
                viewList.add(nonExistentView);
                android.support.v4.app.FragmentTransitionCompat21.addTargets(((android.transition.Transition) (exitTransition)), viewList);
            }
        }
        return exitTransition;
    }

    public static void excludeTarget(java.lang.Object transitionObject, android.view.View view, boolean exclude) {
        android.transition.Transition transition = ((android.transition.Transition) (transitionObject));
        transition.excludeTarget(view, exclude);
    }

    public static void beginDelayedTransition(android.view.ViewGroup sceneRoot, java.lang.Object transitionObject) {
        android.transition.Transition transition = ((android.transition.Transition) (transitionObject));
        android.transition.TransitionManager.beginDelayedTransition(sceneRoot, transition);
    }

    public static void setEpicenter(java.lang.Object transitionObject, android.view.View view) {
        android.transition.Transition transition = ((android.transition.Transition) (transitionObject));
        final android.graphics.Rect epicenter = android.support.v4.app.FragmentTransitionCompat21.getBoundsOnScreen(view);
        transition.setEpicenterCallback(new android.transition.Transition.EpicenterCallback() {
            @java.lang.Override
            public android.graphics.Rect onGetEpicenter(android.transition.Transition transition) {
                return epicenter;
            }
        });
    }

    public static java.lang.Object wrapSharedElementTransition(java.lang.Object transitionObj) {
        if (transitionObj == null) {
            return null;
        }
        android.transition.Transition transition = ((android.transition.Transition) (transitionObj));
        if (transition == null) {
            return null;
        }
        android.transition.TransitionSet transitionSet = new android.transition.TransitionSet();
        transitionSet.addTransition(transition);
        return transitionSet;
    }

    private static void excludeViews(android.transition.Transition transition, android.transition.Transition fromTransition, java.util.ArrayList<android.view.View> views, boolean exclude) {
        if (transition != null) {
            final int viewCount = (fromTransition == null) ? 0 : views.size();
            for (int i = 0; i < viewCount; i++) {
                transition.excludeTarget(views.get(i), exclude);
            }
        }
    }

    /**
     * Exclude (or remove the exclude) of shared element views from the enter and exit transitions.
     *
     * @param enterTransitionObj
     * 		The enter transition
     * @param exitTransitionObj
     * 		The exit transition
     * @param sharedElementTransitionObj
     * 		The shared element transition
     * @param views
     * 		The shared element target views.
     * @param exclude
     * 		<code>true</code> to exclude or <code>false</code> to remove the excluded
     * 		views.
     */
    public static void excludeSharedElementViews(java.lang.Object enterTransitionObj, java.lang.Object exitTransitionObj, java.lang.Object sharedElementTransitionObj, java.util.ArrayList<android.view.View> views, boolean exclude) {
        android.transition.Transition enterTransition = ((android.transition.Transition) (enterTransitionObj));
        android.transition.Transition exitTransition = ((android.transition.Transition) (exitTransitionObj));
        android.transition.Transition sharedElementTransition = ((android.transition.Transition) (sharedElementTransitionObj));
        android.support.v4.app.FragmentTransitionCompat21.excludeViews(enterTransition, sharedElementTransition, views, exclude);
        android.support.v4.app.FragmentTransitionCompat21.excludeViews(exitTransition, sharedElementTransition, views, exclude);
    }

    /**
     * Prepares the enter transition by adding a non-existent view to the transition's target list
     * and setting it epicenter callback. By adding a non-existent view to the target list,
     * we can prevent any view from being targeted at the beginning of the transition.
     * We will add to the views before the end state of the transition is captured so that the
     * views will appear. At the start of the transition, we clear the list of targets so that
     * we can restore the state of the transition and use it again.
     *
     * <p>The shared element transition maps its shared elements immediately prior to
     *  capturing the final state of the Transition.</p>
     */
    public static void addTransitionTargets(java.lang.Object enterTransitionObject, java.lang.Object sharedElementTransitionObject, java.lang.Object exitTransitionObject, final android.view.View container, final android.support.v4.app.FragmentTransitionCompat21.ViewRetriever inFragment, final android.view.View nonExistentView, android.support.v4.app.FragmentTransitionCompat21.EpicenterView epicenterView, final java.util.Map<java.lang.String, java.lang.String> nameOverrides, final java.util.ArrayList<android.view.View> enteringViews, final java.util.ArrayList<android.view.View> exitingViews, final java.util.Map<java.lang.String, android.view.View> namedViews, final java.util.Map<java.lang.String, android.view.View> renamedViews, final java.util.ArrayList<android.view.View> sharedElementTargets) {
        final android.transition.Transition enterTransition = ((android.transition.Transition) (enterTransitionObject));
        final android.transition.Transition exitTransition = ((android.transition.Transition) (exitTransitionObject));
        final android.transition.Transition sharedElementTransition = ((android.transition.Transition) (sharedElementTransitionObject));
        android.support.v4.app.FragmentTransitionCompat21.excludeViews(enterTransition, exitTransition, exitingViews, true);
        if ((enterTransitionObject != null) || (sharedElementTransitionObject != null)) {
            if (enterTransition != null) {
                enterTransition.addTarget(nonExistentView);
            }
            if (sharedElementTransitionObject != null) {
                android.support.v4.app.FragmentTransitionCompat21.setSharedElementTargets(sharedElementTransition, nonExistentView, namedViews, sharedElementTargets);
                android.support.v4.app.FragmentTransitionCompat21.excludeViews(enterTransition, sharedElementTransition, sharedElementTargets, true);
                android.support.v4.app.FragmentTransitionCompat21.excludeViews(exitTransition, sharedElementTransition, sharedElementTargets, true);
            }
            container.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                @java.lang.Override
                public boolean onPreDraw() {
                    container.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (enterTransition != null) {
                        enterTransition.removeTarget(nonExistentView);
                    }
                    if (inFragment != null) {
                        android.view.View fragmentView = inFragment.getView();
                        if (fragmentView != null) {
                            if (!nameOverrides.isEmpty()) {
                                android.support.v4.app.FragmentTransitionCompat21.findNamedViews(renamedViews, fragmentView);
                                renamedViews.keySet().retainAll(nameOverrides.values());
                                for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : nameOverrides.entrySet()) {
                                    java.lang.String to = entry.getValue();
                                    android.view.View view = renamedViews.get(to);
                                    if (view != null) {
                                        java.lang.String from = entry.getKey();
                                        view.setTransitionName(from);
                                    }
                                }
                            }
                            if (enterTransition != null) {
                                android.support.v4.app.FragmentTransitionCompat21.captureTransitioningViews(enteringViews, fragmentView);
                                enteringViews.removeAll(renamedViews.values());
                                enteringViews.add(nonExistentView);
                                android.support.v4.app.FragmentTransitionCompat21.addTargets(enterTransition, enteringViews);
                            }
                        }
                    }
                    android.support.v4.app.FragmentTransitionCompat21.excludeViews(exitTransition, enterTransition, enteringViews, true);
                    return true;
                }
            });
            android.support.v4.app.FragmentTransitionCompat21.setSharedElementEpicenter(enterTransition, epicenterView);
        }
    }

    public static java.lang.Object mergeTransitions(java.lang.Object enterTransitionObject, java.lang.Object exitTransitionObject, java.lang.Object sharedElementTransitionObject, boolean allowOverlap) {
        boolean overlap = true;
        android.transition.Transition enterTransition = ((android.transition.Transition) (enterTransitionObject));
        android.transition.Transition exitTransition = ((android.transition.Transition) (exitTransitionObject));
        android.transition.Transition sharedElementTransition = ((android.transition.Transition) (sharedElementTransitionObject));
        if ((enterTransition != null) && (exitTransition != null)) {
            overlap = allowOverlap;
        }
        // Wrap the transitions. Explicit targets like in enter and exit will cause the
        // views to be targeted regardless of excluded views. If that happens, then the
        // excluded fragments views (hidden fragments) will still be in the transition.
        android.transition.Transition transition;
        if (overlap) {
            // Regular transition -- do it all together
            android.transition.TransitionSet transitionSet = new android.transition.TransitionSet();
            if (enterTransition != null) {
                transitionSet.addTransition(enterTransition);
            }
            if (exitTransition != null) {
                transitionSet.addTransition(exitTransition);
            }
            if (sharedElementTransition != null) {
                transitionSet.addTransition(sharedElementTransition);
            }
            transition = transitionSet;
        } else {
            // First do exit, then enter, but allow shared element transition to happen
            // during both.
            android.transition.Transition staggered = null;
            if ((exitTransition != null) && (enterTransition != null)) {
                staggered = new android.transition.TransitionSet().addTransition(exitTransition).addTransition(enterTransition).setOrdering(android.transition.TransitionSet.ORDERING_SEQUENTIAL);
            } else
                if (exitTransition != null) {
                    staggered = exitTransition;
                } else
                    if (enterTransition != null) {
                        staggered = enterTransition;
                    }


            if (sharedElementTransition != null) {
                android.transition.TransitionSet together = new android.transition.TransitionSet();
                if (staggered != null) {
                    together.addTransition(staggered);
                }
                together.addTransition(sharedElementTransition);
                transition = together;
            } else {
                transition = staggered;
            }
        }
        return transition;
    }

    /**
     * Finds all children of the shared elements and sets the wrapping TransitionSet
     * targets to point to those. It also limits transitions that have no targets to the
     * specific shared elements. This allows developers to target child views of the
     * shared elements specifically, but this doesn't happen by default.
     */
    public static void setSharedElementTargets(java.lang.Object transitionObj, android.view.View nonExistentView, java.util.Map<java.lang.String, android.view.View> namedViews, java.util.ArrayList<android.view.View> sharedElementTargets) {
        android.transition.TransitionSet transition = ((android.transition.TransitionSet) (transitionObj));
        sharedElementTargets.clear();
        sharedElementTargets.addAll(namedViews.values());
        final java.util.List<android.view.View> views = transition.getTargets();
        views.clear();
        final int count = sharedElementTargets.size();
        for (int i = 0; i < count; i++) {
            final android.view.View view = sharedElementTargets.get(i);
            android.support.v4.app.FragmentTransitionCompat21.bfsAddViewChildren(views, view);
        }
        sharedElementTargets.add(nonExistentView);
        android.support.v4.app.FragmentTransitionCompat21.addTargets(transition, sharedElementTargets);
    }

    /**
     * Uses a breadth-first scheme to add startView and all of its children to views.
     * It won't add a child if it is already in views.
     */
    private static void bfsAddViewChildren(final java.util.List<android.view.View> views, final android.view.View startView) {
        final int startIndex = views.size();
        if (android.support.v4.app.FragmentTransitionCompat21.containedBeforeIndex(views, startView, startIndex)) {
            return;// This child is already in the list, so all its children are also.

        }
        views.add(startView);
        for (int index = startIndex; index < views.size(); index++) {
            final android.view.View view = views.get(index);
            if (view instanceof android.view.ViewGroup) {
                android.view.ViewGroup viewGroup = ((android.view.ViewGroup) (view));
                final int childCount = viewGroup.getChildCount();
                for (int childIndex = 0; childIndex < childCount; childIndex++) {
                    final android.view.View child = viewGroup.getChildAt(childIndex);
                    if (!android.support.v4.app.FragmentTransitionCompat21.containedBeforeIndex(views, child, startIndex)) {
                        views.add(child);
                    }
                }
            }
        }
    }

    /**
     * Does a linear search through views for view, limited to maxIndex.
     */
    private static boolean containedBeforeIndex(final java.util.List<android.view.View> views, final android.view.View view, final int maxIndex) {
        for (int i = 0; i < maxIndex; i++) {
            if (views.get(i) == view) {
                return true;
            }
        }
        return false;
    }

    private static void setSharedElementEpicenter(android.transition.Transition transition, final android.support.v4.app.FragmentTransitionCompat21.EpicenterView epicenterView) {
        if (transition != null) {
            transition.setEpicenterCallback(new android.transition.Transition.EpicenterCallback() {
                private android.graphics.Rect mEpicenter;

                @java.lang.Override
                public android.graphics.Rect onGetEpicenter(android.transition.Transition transition) {
                    if ((mEpicenter == null) && (epicenterView.epicenter != null)) {
                        mEpicenter = android.support.v4.app.FragmentTransitionCompat21.getBoundsOnScreen(epicenterView.epicenter);
                    }
                    return mEpicenter;
                }
            });
        }
    }

    private static android.graphics.Rect getBoundsOnScreen(android.view.View view) {
        android.graphics.Rect epicenter = new android.graphics.Rect();
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        // not as good as View.getBoundsOnScreen, but that's not public
        epicenter.set(loc[0], loc[1], loc[0] + view.getWidth(), loc[1] + view.getHeight());
        return epicenter;
    }

    private static void captureTransitioningViews(java.util.ArrayList<android.view.View> transitioningViews, android.view.View view) {
        if (view.getVisibility() == android.view.View.VISIBLE) {
            if (view instanceof android.view.ViewGroup) {
                android.view.ViewGroup viewGroup = ((android.view.ViewGroup) (view));
                if (viewGroup.isTransitionGroup()) {
                    transitioningViews.add(viewGroup);
                } else {
                    int count = viewGroup.getChildCount();
                    for (int i = 0; i < count; i++) {
                        android.view.View child = viewGroup.getChildAt(i);
                        android.support.v4.app.FragmentTransitionCompat21.captureTransitioningViews(transitioningViews, child);
                    }
                }
            } else {
                transitioningViews.add(view);
            }
        }
    }

    public static void findNamedViews(java.util.Map<java.lang.String, android.view.View> namedViews, android.view.View view) {
        if (view.getVisibility() == android.view.View.VISIBLE) {
            java.lang.String transitionName = view.getTransitionName();
            if (transitionName != null) {
                namedViews.put(transitionName, view);
            }
            if (view instanceof android.view.ViewGroup) {
                android.view.ViewGroup viewGroup = ((android.view.ViewGroup) (view));
                int count = viewGroup.getChildCount();
                for (int i = 0; i < count; i++) {
                    android.view.View child = viewGroup.getChildAt(i);
                    android.support.v4.app.FragmentTransitionCompat21.findNamedViews(namedViews, child);
                }
            }
        }
    }

    public static void cleanupTransitions(final android.view.View sceneRoot, final android.view.View nonExistentView, java.lang.Object enterTransitionObject, final java.util.ArrayList<android.view.View> enteringViews, java.lang.Object exitTransitionObject, final java.util.ArrayList<android.view.View> exitingViews, java.lang.Object sharedElementTransitionObject, final java.util.ArrayList<android.view.View> sharedElementTargets, java.lang.Object overallTransitionObject, final java.util.ArrayList<android.view.View> hiddenViews, final java.util.Map<java.lang.String, android.view.View> renamedViews) {
        final android.transition.Transition enterTransition = ((android.transition.Transition) (enterTransitionObject));
        final android.transition.Transition exitTransition = ((android.transition.Transition) (exitTransitionObject));
        final android.transition.Transition sharedElementTransition = ((android.transition.Transition) (sharedElementTransitionObject));
        final android.transition.Transition overallTransition = ((android.transition.Transition) (overallTransitionObject));
        if (overallTransition != null) {
            sceneRoot.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                @java.lang.Override
                public boolean onPreDraw() {
                    sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (enterTransition != null) {
                        android.support.v4.app.FragmentTransitionCompat21.removeTargets(enterTransition, enteringViews);
                        android.support.v4.app.FragmentTransitionCompat21.excludeViews(enterTransition, exitTransition, exitingViews, false);
                        android.support.v4.app.FragmentTransitionCompat21.excludeViews(enterTransition, sharedElementTransition, sharedElementTargets, false);
                    }
                    if (exitTransition != null) {
                        android.support.v4.app.FragmentTransitionCompat21.removeTargets(exitTransition, exitingViews);
                        android.support.v4.app.FragmentTransitionCompat21.excludeViews(exitTransition, enterTransition, enteringViews, false);
                        android.support.v4.app.FragmentTransitionCompat21.excludeViews(exitTransition, sharedElementTransition, sharedElementTargets, false);
                    }
                    if (sharedElementTransition != null) {
                        android.support.v4.app.FragmentTransitionCompat21.removeTargets(sharedElementTransition, sharedElementTargets);
                    }
                    for (java.util.Map.Entry<java.lang.String, android.view.View> entry : renamedViews.entrySet()) {
                        android.view.View view = entry.getValue();
                        java.lang.String name = entry.getKey();
                        view.setTransitionName(name);
                    }
                    int numViews = hiddenViews.size();
                    for (int i = 0; i < numViews; i++) {
                        overallTransition.excludeTarget(hiddenViews.get(i), false);
                    }
                    overallTransition.excludeTarget(nonExistentView, false);
                    return true;
                }
            });
        }
    }

    /**
     * This method removes the views from transitions that target ONLY those views.
     * The views list should match those added in addTargets and should contain
     * one view that is not in the view hierarchy (state.nonExistentView).
     */
    public static void removeTargets(java.lang.Object transitionObject, java.util.ArrayList<android.view.View> views) {
        android.transition.Transition transition = ((android.transition.Transition) (transitionObject));
        if (transition instanceof android.transition.TransitionSet) {
            android.transition.TransitionSet set = ((android.transition.TransitionSet) (transition));
            int numTransitions = set.getTransitionCount();
            for (int i = 0; i < numTransitions; i++) {
                android.transition.Transition child = set.getTransitionAt(i);
                android.support.v4.app.FragmentTransitionCompat21.removeTargets(child, views);
            }
        } else
            if (!android.support.v4.app.FragmentTransitionCompat21.hasSimpleTarget(transition)) {
                java.util.List<android.view.View> targets = transition.getTargets();
                if (((targets != null) && (targets.size() == views.size())) && targets.containsAll(views)) {
                    // We have an exact match. We must have added these earlier in addTargets
                    for (int i = views.size() - 1; i >= 0; i--) {
                        transition.removeTarget(views.get(i));
                    }
                }
            }

    }

    /**
     * This method adds views as targets to the transition, but only if the transition
     * doesn't already have a target. It is best for views to contain one View object
     * that does not exist in the view hierarchy (state.nonExistentView) so that
     * when they are removed later, a list match will suffice to remove the targets.
     * Otherwise, if you happened to have targeted the exact views for the transition,
     * the removeTargets call will remove them unexpectedly.
     */
    public static void addTargets(java.lang.Object transitionObject, java.util.ArrayList<android.view.View> views) {
        android.transition.Transition transition = ((android.transition.Transition) (transitionObject));
        if (transition instanceof android.transition.TransitionSet) {
            android.transition.TransitionSet set = ((android.transition.TransitionSet) (transition));
            int numTransitions = set.getTransitionCount();
            for (int i = 0; i < numTransitions; i++) {
                android.transition.Transition child = set.getTransitionAt(i);
                android.support.v4.app.FragmentTransitionCompat21.addTargets(child, views);
            }
        } else
            if (!android.support.v4.app.FragmentTransitionCompat21.hasSimpleTarget(transition)) {
                java.util.List<android.view.View> targets = transition.getTargets();
                if (android.support.v4.app.FragmentTransitionCompat21.isNullOrEmpty(targets)) {
                    // We can just add the target views
                    int numViews = views.size();
                    for (int i = 0; i < numViews; i++) {
                        transition.addTarget(views.get(i));
                    }
                }
            }

    }

    private static boolean hasSimpleTarget(android.transition.Transition transition) {
        return ((!android.support.v4.app.FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetIds())) || (!android.support.v4.app.FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetNames()))) || (!android.support.v4.app.FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetTypes()));
    }

    private static boolean isNullOrEmpty(java.util.List list) {
        return (list == null) || list.isEmpty();
    }

    public interface ViewRetriever {
        android.view.View getView();
    }

    public static class EpicenterView {
        public android.view.View epicenter;
    }
}

