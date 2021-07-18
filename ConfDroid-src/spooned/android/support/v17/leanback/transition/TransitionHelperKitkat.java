/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.transition;


final class TransitionHelperKitkat {
    TransitionHelperKitkat() {
    }

    static java.lang.Object createScene(android.view.ViewGroup sceneRoot, java.lang.Runnable enterAction) {
        android.transition.Scene scene = new android.transition.Scene(sceneRoot);
        scene.setEnterAction(enterAction);
        return scene;
    }

    static java.lang.Object createTransitionSet(boolean sequential) {
        android.transition.TransitionSet set = new android.transition.TransitionSet();
        set.setOrdering(sequential ? android.transition.TransitionSet.ORDERING_SEQUENTIAL : android.transition.TransitionSet.ORDERING_TOGETHER);
        return set;
    }

    static void addTransition(java.lang.Object transitionSet, java.lang.Object transition) {
        ((android.transition.TransitionSet) (transitionSet)).addTransition(((android.transition.Transition) (transition)));
    }

    static java.lang.Object createAutoTransition() {
        return new android.transition.AutoTransition();
    }

    static java.lang.Object createSlide(int slideEdge) {
        android.support.v17.leanback.transition.SlideKitkat slide = new android.support.v17.leanback.transition.SlideKitkat();
        slide.setSlideEdge(slideEdge);
        return slide;
    }

    static java.lang.Object createScale() {
        android.support.v17.leanback.transition.Scale scale = new android.support.v17.leanback.transition.Scale();
        return scale;
    }

    static java.lang.Object createFadeTransition(int fadingMode) {
        android.transition.Fade fade = new android.transition.Fade(fadingMode);
        return fade;
    }

    /**
     * change bounds that support customized start delay.
     */
    static class CustomChangeBounds extends android.transition.ChangeBounds {
        int mDefaultStartDelay;

        // View -> delay
        final java.util.HashMap<android.view.View, java.lang.Integer> mViewStartDelays = new java.util.HashMap<android.view.View, java.lang.Integer>();

        // id -> delay
        final android.util.SparseIntArray mIdStartDelays = new android.util.SparseIntArray();

        // Class.getName() -> delay
        final java.util.HashMap<java.lang.String, java.lang.Integer> mClassStartDelays = new java.util.HashMap<java.lang.String, java.lang.Integer>();

        private int getDelay(android.view.View view) {
            java.lang.Integer delay = mViewStartDelays.get(view);
            if (delay != null) {
                return delay;
            }
            int idStartDelay = mIdStartDelays.get(view.getId(), -1);
            if (idStartDelay != (-1)) {
                return idStartDelay;
            }
            delay = mClassStartDelays.get(view.getClass().getName());
            if (delay != null) {
                return delay;
            }
            return mDefaultStartDelay;
        }

        @java.lang.Override
        public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
            android.animation.Animator animator = super.createAnimator(sceneRoot, startValues, endValues);
            if (((animator != null) && (endValues != null)) && (endValues.view != null)) {
                animator.setStartDelay(getDelay(endValues.view));
            }
            return animator;
        }

        public void setStartDelay(android.view.View view, int startDelay) {
            mViewStartDelays.put(view, startDelay);
        }

        public void setStartDelay(int viewId, int startDelay) {
            mIdStartDelays.put(viewId, startDelay);
        }

        public void setStartDelay(java.lang.String className, int startDelay) {
            mClassStartDelays.put(className, startDelay);
        }

        public void setDefaultStartDelay(int startDelay) {
            mDefaultStartDelay = startDelay;
        }
    }

    static java.lang.Object createChangeBounds(boolean reparent) {
        android.support.v17.leanback.transition.TransitionHelperKitkat.CustomChangeBounds changeBounds = new android.support.v17.leanback.transition.TransitionHelperKitkat.CustomChangeBounds();
        changeBounds.setReparent(reparent);
        return changeBounds;
    }

    static void setChangeBoundsStartDelay(java.lang.Object changeBounds, int viewId, int startDelay) {
        ((android.support.v17.leanback.transition.TransitionHelperKitkat.CustomChangeBounds) (changeBounds)).setStartDelay(viewId, startDelay);
    }

    static void setChangeBoundsStartDelay(java.lang.Object changeBounds, android.view.View view, int startDelay) {
        ((android.support.v17.leanback.transition.TransitionHelperKitkat.CustomChangeBounds) (changeBounds)).setStartDelay(view, startDelay);
    }

    static void setChangeBoundsStartDelay(java.lang.Object changeBounds, java.lang.String className, int startDelay) {
        ((android.support.v17.leanback.transition.TransitionHelperKitkat.CustomChangeBounds) (changeBounds)).setStartDelay(className, startDelay);
    }

    static void setChangeBoundsDefaultStartDelay(java.lang.Object changeBounds, int startDelay) {
        ((android.support.v17.leanback.transition.TransitionHelperKitkat.CustomChangeBounds) (changeBounds)).setDefaultStartDelay(startDelay);
    }

    static void setStartDelay(java.lang.Object transition, long startDelay) {
        ((android.transition.Transition) (transition)).setStartDelay(startDelay);
    }

    static void setDuration(java.lang.Object transition, long duration) {
        ((android.transition.Transition) (transition)).setDuration(duration);
    }

    static void exclude(java.lang.Object transition, int targetId, boolean exclude) {
        ((android.transition.Transition) (transition)).excludeTarget(targetId, exclude);
    }

    static void exclude(java.lang.Object transition, android.view.View targetView, boolean exclude) {
        ((android.transition.Transition) (transition)).excludeTarget(targetView, exclude);
    }

    static void excludeChildren(java.lang.Object transition, int targetId, boolean exclude) {
        ((android.transition.Transition) (transition)).excludeChildren(targetId, exclude);
    }

    static void excludeChildren(java.lang.Object transition, android.view.View targetView, boolean exclude) {
        ((android.transition.Transition) (transition)).excludeChildren(targetView, exclude);
    }

    static void include(java.lang.Object transition, int targetId) {
        ((android.transition.Transition) (transition)).addTarget(targetId);
    }

    static void include(java.lang.Object transition, android.view.View targetView) {
        ((android.transition.Transition) (transition)).addTarget(targetView);
    }

    static void addTransitionListener(java.lang.Object transition, final android.support.v17.leanback.transition.TransitionListener listener) {
        if (listener == null) {
            return;
        }
        android.transition.Transition t = ((android.transition.Transition) (transition));
        listener.mImpl = new android.transition.Transition.TransitionListener() {
            @java.lang.Override
            public void onTransitionStart(android.transition.Transition transition) {
                listener.onTransitionStart(transition);
            }

            @java.lang.Override
            public void onTransitionResume(android.transition.Transition transition) {
                listener.onTransitionResume(transition);
            }

            @java.lang.Override
            public void onTransitionPause(android.transition.Transition transition) {
                listener.onTransitionPause(transition);
            }

            @java.lang.Override
            public void onTransitionEnd(android.transition.Transition transition) {
                listener.onTransitionEnd(transition);
            }

            @java.lang.Override
            public void onTransitionCancel(android.transition.Transition transition) {
                listener.onTransitionCancel(transition);
            }
        };
        t.addListener(((android.transition.Transition.TransitionListener) (listener.mImpl)));
    }

    static void removeTransitionListener(java.lang.Object transition, final android.support.v17.leanback.transition.TransitionListener listener) {
        if ((listener == null) || (listener.mImpl == null)) {
            return;
        }
        android.transition.Transition t = ((android.transition.Transition) (transition));
        t.removeListener(((android.transition.Transition.TransitionListener) (listener.mImpl)));
        listener.mImpl = null;
    }

    static void runTransition(java.lang.Object scene, java.lang.Object transition) {
        android.transition.TransitionManager.go(((android.transition.Scene) (scene)), ((android.transition.Transition) (transition)));
    }

    static void setInterpolator(java.lang.Object transition, java.lang.Object timeInterpolator) {
        ((android.transition.Transition) (transition)).setInterpolator(((android.animation.TimeInterpolator) (timeInterpolator)));
    }

    static void addTarget(java.lang.Object transition, android.view.View view) {
        ((android.transition.Transition) (transition)).addTarget(view);
    }

    static java.lang.Object loadTransition(android.content.Context context, int resId) {
        return android.transition.TransitionInflater.from(context).inflateTransition(resId);
    }
}

