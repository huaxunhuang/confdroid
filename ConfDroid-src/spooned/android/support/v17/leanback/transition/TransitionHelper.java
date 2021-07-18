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


/**
 * Helper for view transitions.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class TransitionHelper {
    public static final int FADE_IN = 0x1;

    public static final int FADE_OUT = 0x2;

    public static final int SLIDE_LEFT = android.view.Gravity.LEFT;

    public static final int SLIDE_TOP = android.view.Gravity.TOP;

    public static final int SLIDE_RIGHT = android.view.Gravity.RIGHT;

    public static final int SLIDE_BOTTOM = android.view.Gravity.BOTTOM;

    private static android.support.v17.leanback.transition.TransitionHelper.TransitionHelperVersionImpl sImpl;

    /**
     * Gets whether the system supports Transition animations.
     *
     * @return True if Transition animations are supported.
     */
    public static boolean systemSupportsTransitions() {
        // Supported on Android 4.4 or later.
        return android.os.Build.VERSION.SDK_INT >= 19;
    }

    /**
     * Returns true if system supports entrance Transition animations.
     */
    public static boolean systemSupportsEntranceTransitions() {
        return android.os.Build.VERSION.SDK_INT >= 21;
    }

    /**
     * Interface implemented by classes that support Transition animations.
     */
    static interface TransitionHelperVersionImpl {
        public void setEnterTransition(android.app.Fragment fragment, java.lang.Object transition);

        public void setExitTransition(android.app.Fragment fragment, java.lang.Object transition);

        public void setSharedElementEnterTransition(android.app.Fragment fragment, java.lang.Object transition);

        public void addSharedElement(android.app.FragmentTransaction ft, android.view.View view, java.lang.String transitionName);

        public java.lang.Object getSharedElementEnterTransition(android.view.Window window);

        public java.lang.Object getSharedElementReturnTransition(android.view.Window window);

        public java.lang.Object getSharedElementExitTransition(android.view.Window window);

        public java.lang.Object getSharedElementReenterTransition(android.view.Window window);

        public java.lang.Object getEnterTransition(android.view.Window window);

        public java.lang.Object getReturnTransition(android.view.Window window);

        public java.lang.Object getExitTransition(android.view.Window window);

        public java.lang.Object getReenterTransition(android.view.Window window);

        public java.lang.Object createScene(android.view.ViewGroup sceneRoot, java.lang.Runnable r);

        public java.lang.Object createAutoTransition();

        public java.lang.Object createSlide(int slideEdge);

        public java.lang.Object createScale();

        public java.lang.Object createFadeTransition(int fadingMode);

        public java.lang.Object createChangeTransform();

        public java.lang.Object createChangeBounds(boolean reparent);

        public java.lang.Object createFadeAndShortSlide(int edge);

        public java.lang.Object createFadeAndShortSlide(int edge, float distance);

        public void setChangeBoundsStartDelay(java.lang.Object changeBounds, android.view.View view, int startDelay);

        public void setChangeBoundsStartDelay(java.lang.Object changeBounds, int viewId, int startDelay);

        public void setChangeBoundsStartDelay(java.lang.Object changeBounds, java.lang.String className, int startDelay);

        public void setChangeBoundsDefaultStartDelay(java.lang.Object changeBounds, int startDelay);

        public java.lang.Object createTransitionSet(boolean sequential);

        public void addTransition(java.lang.Object transitionSet, java.lang.Object transition);

        public void addTransitionListener(java.lang.Object transition, android.support.v17.leanback.transition.TransitionListener listener);

        public void removeTransitionListener(java.lang.Object transition, android.support.v17.leanback.transition.TransitionListener listener);

        public void runTransition(java.lang.Object scene, java.lang.Object transition);

        public void exclude(java.lang.Object transition, int targetId, boolean exclude);

        public void exclude(java.lang.Object transition, android.view.View targetView, boolean exclude);

        public void excludeChildren(java.lang.Object transition, int targetId, boolean exclude);

        public void excludeChildren(java.lang.Object transition, android.view.View target, boolean exclude);

        public void include(java.lang.Object transition, int targetId);

        public void include(java.lang.Object transition, android.view.View targetView);

        public void setStartDelay(java.lang.Object transition, long startDelay);

        public void setDuration(java.lang.Object transition, long duration);

        public void setInterpolator(java.lang.Object transition, java.lang.Object timeInterpolator);

        public void addTarget(java.lang.Object transition, android.view.View view);

        public java.lang.Object createDefaultInterpolator(android.content.Context context);

        public java.lang.Object loadTransition(android.content.Context context, int resId);

        public void beginDelayedTransition(android.view.ViewGroup sceneRoot, java.lang.Object transitionObject);

        public void setTransitionGroup(android.view.ViewGroup viewGroup, boolean transitionGroup);
    }

    /**
     * Interface used when we do not support Transition animations.
     */
    static class TransitionHelperStubImpl implements android.support.v17.leanback.transition.TransitionHelper.TransitionHelperVersionImpl {
        private static class TransitionStub {
            java.util.ArrayList<android.support.v17.leanback.transition.TransitionListener> mTransitionListeners;

            TransitionStub() {
            }
        }

        @java.lang.Override
        public void setEnterTransition(android.app.Fragment fragment, java.lang.Object transition) {
        }

        @java.lang.Override
        public void setExitTransition(android.app.Fragment fragment, java.lang.Object transition) {
        }

        @java.lang.Override
        public void setSharedElementEnterTransition(android.app.Fragment fragment, java.lang.Object transition) {
        }

        @java.lang.Override
        public void addSharedElement(android.app.FragmentTransaction ft, android.view.View view, java.lang.String transitionName) {
        }

        @java.lang.Override
        public java.lang.Object getSharedElementEnterTransition(android.view.Window window) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getSharedElementReturnTransition(android.view.Window window) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getSharedElementExitTransition(android.view.Window window) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getSharedElementReenterTransition(android.view.Window window) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getEnterTransition(android.view.Window window) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getReturnTransition(android.view.Window window) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getExitTransition(android.view.Window window) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getReenterTransition(android.view.Window window) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object createScene(android.view.ViewGroup sceneRoot, java.lang.Runnable r) {
            return r;
        }

        @java.lang.Override
        public java.lang.Object createAutoTransition() {
            return new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub();
        }

        @java.lang.Override
        public java.lang.Object createFadeTransition(int fadingMode) {
            return new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub();
        }

        @java.lang.Override
        public java.lang.Object createChangeBounds(boolean reparent) {
            return new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub();
        }

        @java.lang.Override
        public java.lang.Object createChangeTransform() {
            return new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub();
        }

        @java.lang.Override
        public java.lang.Object createFadeAndShortSlide(int edge) {
            return new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub();
        }

        @java.lang.Override
        public java.lang.Object createFadeAndShortSlide(int edge, float distance) {
            return new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub();
        }

        @java.lang.Override
        public java.lang.Object createSlide(int slideEdge) {
            return new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub();
        }

        @java.lang.Override
        public java.lang.Object createScale() {
            return new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub();
        }

        @java.lang.Override
        public void setChangeBoundsStartDelay(java.lang.Object changeBounds, android.view.View view, int startDelay) {
        }

        @java.lang.Override
        public void setChangeBoundsStartDelay(java.lang.Object changeBounds, int viewId, int startDelay) {
        }

        @java.lang.Override
        public void setChangeBoundsStartDelay(java.lang.Object changeBounds, java.lang.String className, int startDelay) {
        }

        @java.lang.Override
        public void setChangeBoundsDefaultStartDelay(java.lang.Object changeBounds, int startDelay) {
        }

        @java.lang.Override
        public java.lang.Object createTransitionSet(boolean sequential) {
            return new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub();
        }

        @java.lang.Override
        public void addTransition(java.lang.Object transitionSet, java.lang.Object transition) {
        }

        @java.lang.Override
        public void exclude(java.lang.Object transition, int targetId, boolean exclude) {
        }

        @java.lang.Override
        public void exclude(java.lang.Object transition, android.view.View targetView, boolean exclude) {
        }

        @java.lang.Override
        public void excludeChildren(java.lang.Object transition, int targetId, boolean exclude) {
        }

        @java.lang.Override
        public void excludeChildren(java.lang.Object transition, android.view.View targetView, boolean exclude) {
        }

        @java.lang.Override
        public void include(java.lang.Object transition, int targetId) {
        }

        @java.lang.Override
        public void include(java.lang.Object transition, android.view.View targetView) {
        }

        @java.lang.Override
        public void setStartDelay(java.lang.Object transition, long startDelay) {
        }

        @java.lang.Override
        public void setDuration(java.lang.Object transition, long duration) {
        }

        @java.lang.Override
        public void addTransitionListener(java.lang.Object transition, android.support.v17.leanback.transition.TransitionListener listener) {
            android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub stub = ((android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub) (transition));
            if (stub.mTransitionListeners == null) {
                stub.mTransitionListeners = new java.util.ArrayList<android.support.v17.leanback.transition.TransitionListener>();
            }
            stub.mTransitionListeners.add(listener);
        }

        @java.lang.Override
        public void removeTransitionListener(java.lang.Object transition, android.support.v17.leanback.transition.TransitionListener listener) {
            android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub stub = ((android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub) (transition));
            if (stub.mTransitionListeners != null) {
                stub.mTransitionListeners.remove(listener);
            }
        }

        @java.lang.Override
        public void runTransition(java.lang.Object scene, java.lang.Object transition) {
            android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub transitionStub = ((android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub) (transition));
            if ((transitionStub != null) && (transitionStub.mTransitionListeners != null)) {
                for (int i = 0, size = transitionStub.mTransitionListeners.size(); i < size; i++) {
                    transitionStub.mTransitionListeners.get(i).onTransitionStart(transition);
                }
            }
            java.lang.Runnable r = ((java.lang.Runnable) (scene));
            if (r != null) {
                r.run();
            }
            if ((transitionStub != null) && (transitionStub.mTransitionListeners != null)) {
                for (int i = 0, size = transitionStub.mTransitionListeners.size(); i < size; i++) {
                    transitionStub.mTransitionListeners.get(i).onTransitionEnd(transition);
                }
            }
        }

        @java.lang.Override
        public void setInterpolator(java.lang.Object transition, java.lang.Object timeInterpolator) {
        }

        @java.lang.Override
        public void addTarget(java.lang.Object transition, android.view.View view) {
        }

        @java.lang.Override
        public java.lang.Object createDefaultInterpolator(android.content.Context context) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object loadTransition(android.content.Context context, int resId) {
            return new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl.TransitionStub();
        }

        @java.lang.Override
        public void beginDelayedTransition(android.view.ViewGroup sceneRoot, java.lang.Object transitionObject) {
        }

        @java.lang.Override
        public void setTransitionGroup(android.view.ViewGroup viewGroup, boolean transitionGroup) {
        }
    }

    /**
     * Implementation used on KitKat (and above).
     */
    static class TransitionHelperKitkatImpl extends android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl {
        @java.lang.Override
        public java.lang.Object createScene(android.view.ViewGroup sceneRoot, java.lang.Runnable r) {
            return android.support.v17.leanback.transition.TransitionHelperKitkat.createScene(sceneRoot, r);
        }

        @java.lang.Override
        public java.lang.Object createAutoTransition() {
            return android.support.v17.leanback.transition.TransitionHelperKitkat.createAutoTransition();
        }

        @java.lang.Override
        public java.lang.Object createFadeTransition(int fadingMode) {
            return android.support.v17.leanback.transition.TransitionHelperKitkat.createFadeTransition(fadingMode);
        }

        @java.lang.Override
        public java.lang.Object createChangeBounds(boolean reparent) {
            return android.support.v17.leanback.transition.TransitionHelperKitkat.createChangeBounds(reparent);
        }

        @java.lang.Override
        public java.lang.Object createSlide(int slideEdge) {
            return android.support.v17.leanback.transition.TransitionHelperKitkat.createSlide(slideEdge);
        }

        @java.lang.Override
        public java.lang.Object createScale() {
            return android.support.v17.leanback.transition.TransitionHelperKitkat.createScale();
        }

        @java.lang.Override
        public void setChangeBoundsStartDelay(java.lang.Object changeBounds, android.view.View view, int startDelay) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.setChangeBoundsStartDelay(changeBounds, view, startDelay);
        }

        @java.lang.Override
        public void setChangeBoundsStartDelay(java.lang.Object changeBounds, int viewId, int startDelay) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.setChangeBoundsStartDelay(changeBounds, viewId, startDelay);
        }

        @java.lang.Override
        public void setChangeBoundsStartDelay(java.lang.Object changeBounds, java.lang.String className, int startDelay) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.setChangeBoundsStartDelay(changeBounds, className, startDelay);
        }

        @java.lang.Override
        public void setChangeBoundsDefaultStartDelay(java.lang.Object changeBounds, int startDelay) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.setChangeBoundsDefaultStartDelay(changeBounds, startDelay);
        }

        @java.lang.Override
        public java.lang.Object createTransitionSet(boolean sequential) {
            return android.support.v17.leanback.transition.TransitionHelperKitkat.createTransitionSet(sequential);
        }

        @java.lang.Override
        public void addTransition(java.lang.Object transitionSet, java.lang.Object transition) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.addTransition(transitionSet, transition);
        }

        @java.lang.Override
        public void exclude(java.lang.Object transition, int targetId, boolean exclude) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.exclude(transition, targetId, exclude);
        }

        @java.lang.Override
        public void exclude(java.lang.Object transition, android.view.View targetView, boolean exclude) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.exclude(transition, targetView, exclude);
        }

        @java.lang.Override
        public void excludeChildren(java.lang.Object transition, int targetId, boolean exclude) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.excludeChildren(transition, targetId, exclude);
        }

        @java.lang.Override
        public void excludeChildren(java.lang.Object transition, android.view.View targetView, boolean exclude) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.excludeChildren(transition, targetView, exclude);
        }

        @java.lang.Override
        public void include(java.lang.Object transition, int targetId) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.include(transition, targetId);
        }

        @java.lang.Override
        public void include(java.lang.Object transition, android.view.View targetView) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.include(transition, targetView);
        }

        @java.lang.Override
        public void setStartDelay(java.lang.Object transition, long startDelay) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.setStartDelay(transition, startDelay);
        }

        @java.lang.Override
        public void setDuration(java.lang.Object transition, long duration) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.setDuration(transition, duration);
        }

        @java.lang.Override
        public void addTransitionListener(java.lang.Object transition, android.support.v17.leanback.transition.TransitionListener listener) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.addTransitionListener(transition, listener);
        }

        @java.lang.Override
        public void removeTransitionListener(java.lang.Object transition, android.support.v17.leanback.transition.TransitionListener listener) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.removeTransitionListener(transition, listener);
        }

        @java.lang.Override
        public void runTransition(java.lang.Object scene, java.lang.Object transition) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.runTransition(scene, transition);
        }

        @java.lang.Override
        public void setInterpolator(java.lang.Object transition, java.lang.Object timeInterpolator) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.setInterpolator(transition, timeInterpolator);
        }

        @java.lang.Override
        public void addTarget(java.lang.Object transition, android.view.View view) {
            android.support.v17.leanback.transition.TransitionHelperKitkat.addTarget(transition, view);
        }

        @java.lang.Override
        public java.lang.Object createDefaultInterpolator(android.content.Context context) {
            return null;
        }

        @java.lang.Override
        public java.lang.Object loadTransition(android.content.Context context, int resId) {
            return android.support.v17.leanback.transition.TransitionHelperKitkat.loadTransition(context, resId);
        }
    }

    static final class TransitionHelperApi21Impl extends android.support.v17.leanback.transition.TransitionHelper.TransitionHelperKitkatImpl {
        @java.lang.Override
        public void setEnterTransition(android.app.Fragment fragment, java.lang.Object transition) {
            android.support.v17.leanback.transition.TransitionHelperApi21.setEnterTransition(fragment, transition);
        }

        @java.lang.Override
        public void setExitTransition(android.app.Fragment fragment, java.lang.Object transition) {
            android.support.v17.leanback.transition.TransitionHelperApi21.setExitTransition(fragment, transition);
        }

        @java.lang.Override
        public void setSharedElementEnterTransition(android.app.Fragment fragment, java.lang.Object transition) {
            android.support.v17.leanback.transition.TransitionHelperApi21.setSharedElementEnterTransition(fragment, transition);
        }

        @java.lang.Override
        public void addSharedElement(android.app.FragmentTransaction ft, android.view.View view, java.lang.String transitionName) {
            android.support.v17.leanback.transition.TransitionHelperApi21.addSharedElement(ft, view, transitionName);
        }

        @java.lang.Override
        public java.lang.Object getSharedElementEnterTransition(android.view.Window window) {
            return android.support.v17.leanback.transition.TransitionHelperApi21.getSharedElementEnterTransition(window);
        }

        @java.lang.Override
        public java.lang.Object getSharedElementReturnTransition(android.view.Window window) {
            return android.support.v17.leanback.transition.TransitionHelperApi21.getSharedElementReturnTransition(window);
        }

        @java.lang.Override
        public java.lang.Object getSharedElementExitTransition(android.view.Window window) {
            return android.support.v17.leanback.transition.TransitionHelperApi21.getSharedElementExitTransition(window);
        }

        @java.lang.Override
        public java.lang.Object getSharedElementReenterTransition(android.view.Window window) {
            return android.support.v17.leanback.transition.TransitionHelperApi21.getSharedElementReenterTransition(window);
        }

        @java.lang.Override
        public java.lang.Object createFadeAndShortSlide(int edge) {
            return android.support.v17.leanback.transition.TransitionHelperApi21.createFadeAndShortSlide(edge);
        }

        @java.lang.Override
        public java.lang.Object createFadeAndShortSlide(int edge, float distance) {
            return android.support.v17.leanback.transition.TransitionHelperApi21.createFadeAndShortSlide(edge, distance);
        }

        @java.lang.Override
        public void beginDelayedTransition(android.view.ViewGroup sceneRoot, java.lang.Object transition) {
            android.support.v17.leanback.transition.TransitionHelperApi21.beginDelayedTransition(sceneRoot, transition);
        }

        @java.lang.Override
        public java.lang.Object getEnterTransition(android.view.Window window) {
            return android.support.v17.leanback.transition.TransitionHelperApi21.getEnterTransition(window);
        }

        @java.lang.Override
        public java.lang.Object getReturnTransition(android.view.Window window) {
            return android.support.v17.leanback.transition.TransitionHelperApi21.getReturnTransition(window);
        }

        @java.lang.Override
        public java.lang.Object getExitTransition(android.view.Window window) {
            return android.support.v17.leanback.transition.TransitionHelperApi21.getExitTransition(window);
        }

        @java.lang.Override
        public java.lang.Object getReenterTransition(android.view.Window window) {
            return android.support.v17.leanback.transition.TransitionHelperApi21.getReenterTransition(window);
        }

        @java.lang.Override
        public java.lang.Object createScale() {
            return android.support.v17.leanback.transition.TransitionHelperApi21.createScale();
        }

        @java.lang.Override
        public java.lang.Object createDefaultInterpolator(android.content.Context context) {
            return android.support.v17.leanback.transition.TransitionHelperApi21.createDefaultInterpolator(context);
        }

        @java.lang.Override
        public void setTransitionGroup(android.view.ViewGroup viewGroup, boolean transitionGroup) {
            android.support.v17.leanback.transition.TransitionHelperApi21.setTransitionGroup(viewGroup, transitionGroup);
        }

        @java.lang.Override
        public java.lang.Object createChangeTransform() {
            return android.support.v17.leanback.transition.TransitionHelperApi21.createChangeTransform();
        }
    }

    static {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            android.support.v17.leanback.transition.TransitionHelper.sImpl = new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperApi21Impl();
        } else
            if (android.support.v17.leanback.transition.TransitionHelper.systemSupportsTransitions()) {
                android.support.v17.leanback.transition.TransitionHelper.sImpl = new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperKitkatImpl();
            } else {
                android.support.v17.leanback.transition.TransitionHelper.sImpl = new android.support.v17.leanback.transition.TransitionHelper.TransitionHelperStubImpl();
            }

    }

    public static java.lang.Object getSharedElementEnterTransition(android.view.Window window) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.getSharedElementEnterTransition(window);
    }

    public static java.lang.Object getSharedElementReturnTransition(android.view.Window window) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.getSharedElementReturnTransition(window);
    }

    public static java.lang.Object getSharedElementExitTransition(android.view.Window window) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.getSharedElementExitTransition(window);
    }

    public static java.lang.Object getSharedElementReenterTransition(android.view.Window window) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.getSharedElementReenterTransition(window);
    }

    public static java.lang.Object getEnterTransition(android.view.Window window) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.getEnterTransition(window);
    }

    public static java.lang.Object getReturnTransition(android.view.Window window) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.getReturnTransition(window);
    }

    public static java.lang.Object getExitTransition(android.view.Window window) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.getExitTransition(window);
    }

    public static java.lang.Object getReenterTransition(android.view.Window window) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.getReenterTransition(window);
    }

    public static java.lang.Object createScene(android.view.ViewGroup sceneRoot, java.lang.Runnable r) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.createScene(sceneRoot, r);
    }

    public static java.lang.Object createChangeBounds(boolean reparent) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.createChangeBounds(reparent);
    }

    public static java.lang.Object createChangeTransform() {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.createChangeTransform();
    }

    public static void setChangeBoundsStartDelay(java.lang.Object changeBounds, android.view.View view, int startDelay) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.setChangeBoundsStartDelay(changeBounds, view, startDelay);
    }

    public static void setChangeBoundsStartDelay(java.lang.Object changeBounds, int viewId, int startDelay) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.setChangeBoundsStartDelay(changeBounds, viewId, startDelay);
    }

    public static void setChangeBoundsStartDelay(java.lang.Object changeBounds, java.lang.String className, int startDelay) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.setChangeBoundsStartDelay(changeBounds, className, startDelay);
    }

    public static void setChangeBoundsDefaultStartDelay(java.lang.Object changeBounds, int startDelay) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.setChangeBoundsDefaultStartDelay(changeBounds, startDelay);
    }

    public static java.lang.Object createTransitionSet(boolean sequential) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.createTransitionSet(sequential);
    }

    public static java.lang.Object createSlide(int slideEdge) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.createSlide(slideEdge);
    }

    public static java.lang.Object createScale() {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.createScale();
    }

    public static void addTransition(java.lang.Object transitionSet, java.lang.Object transition) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.addTransition(transitionSet, transition);
    }

    public static void exclude(java.lang.Object transition, int targetId, boolean exclude) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.exclude(transition, targetId, exclude);
    }

    public static void exclude(java.lang.Object transition, android.view.View targetView, boolean exclude) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.exclude(transition, targetView, exclude);
    }

    public static void excludeChildren(java.lang.Object transition, int targetId, boolean exclude) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.excludeChildren(transition, targetId, exclude);
    }

    public static void excludeChildren(java.lang.Object transition, android.view.View targetView, boolean exclude) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.excludeChildren(transition, targetView, exclude);
    }

    public static void include(java.lang.Object transition, int targetId) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.include(transition, targetId);
    }

    public static void include(java.lang.Object transition, android.view.View targetView) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.include(transition, targetView);
    }

    public static void setStartDelay(java.lang.Object transition, long startDelay) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.setStartDelay(transition, startDelay);
    }

    public static void setDuration(java.lang.Object transition, long duration) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.setDuration(transition, duration);
    }

    public static java.lang.Object createAutoTransition() {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.createAutoTransition();
    }

    public static java.lang.Object createFadeTransition(int fadeMode) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.createFadeTransition(fadeMode);
    }

    public static void addTransitionListener(java.lang.Object transition, android.support.v17.leanback.transition.TransitionListener listener) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.addTransitionListener(transition, listener);
    }

    public static void removeTransitionListener(java.lang.Object transition, android.support.v17.leanback.transition.TransitionListener listener) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.removeTransitionListener(transition, listener);
    }

    public static void runTransition(java.lang.Object scene, java.lang.Object transition) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.runTransition(scene, transition);
    }

    public static void setInterpolator(java.lang.Object transition, java.lang.Object timeInterpolator) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.setInterpolator(transition, timeInterpolator);
    }

    public static void addTarget(java.lang.Object transition, android.view.View view) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.addTarget(transition, view);
    }

    public static java.lang.Object createDefaultInterpolator(android.content.Context context) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.createDefaultInterpolator(context);
    }

    public static java.lang.Object loadTransition(android.content.Context context, int resId) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.loadTransition(context, resId);
    }

    public static void setEnterTransition(android.app.Fragment fragment, java.lang.Object transition) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.setEnterTransition(fragment, transition);
    }

    public static void setExitTransition(android.app.Fragment fragment, java.lang.Object transition) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.setExitTransition(fragment, transition);
    }

    public static void setSharedElementEnterTransition(android.app.Fragment fragment, java.lang.Object transition) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.setSharedElementEnterTransition(fragment, transition);
    }

    public static void addSharedElement(android.app.FragmentTransaction ft, android.view.View view, java.lang.String transitionName) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.addSharedElement(ft, view, transitionName);
    }

    public static void setEnterTransition(android.support.v4.app.Fragment fragment, java.lang.Object transition) {
        fragment.setEnterTransition(transition);
    }

    public static void setExitTransition(android.support.v4.app.Fragment fragment, java.lang.Object transition) {
        fragment.setExitTransition(transition);
    }

    public static void setSharedElementEnterTransition(android.support.v4.app.Fragment fragment, java.lang.Object transition) {
        fragment.setSharedElementEnterTransition(transition);
    }

    public static void addSharedElement(android.support.v4.app.FragmentTransaction ft, android.view.View view, java.lang.String transitionName) {
        ft.addSharedElement(view, transitionName);
    }

    public static java.lang.Object createFadeAndShortSlide(int edge) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.createFadeAndShortSlide(edge);
    }

    public static java.lang.Object createFadeAndShortSlide(int edge, float distance) {
        return android.support.v17.leanback.transition.TransitionHelper.sImpl.createFadeAndShortSlide(edge, distance);
    }

    public static void beginDelayedTransition(android.view.ViewGroup sceneRoot, java.lang.Object transitionObject) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.beginDelayedTransition(sceneRoot, transitionObject);
    }

    public static void setTransitionGroup(android.view.ViewGroup viewGroup, boolean transitionGroup) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.setTransitionGroup(viewGroup, transitionGroup);
    }

    /**
     *
     *
     * @deprecated Use static calls.
     */
    @java.lang.Deprecated
    public static android.support.v17.leanback.transition.TransitionHelper getInstance() {
        return new android.support.v17.leanback.transition.TransitionHelper();
    }

    /**
     *
     *
     * @deprecated Use {@link #addTransitionListener(Object, TransitionListener)}
     */
    @java.lang.Deprecated
    public static void setTransitionListener(java.lang.Object transition, android.support.v17.leanback.transition.TransitionListener listener) {
        android.support.v17.leanback.transition.TransitionHelper.sImpl.addTransitionListener(transition, listener);
    }
}

