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


class TransitionKitKat extends android.support.transition.TransitionImpl {
    /* package */
    android.transition.Transition mTransition;

    /* package */
    android.support.transition.TransitionInterface mExternalTransition;

    private android.support.transition.TransitionKitKat.CompatListener mCompatListener;

    static void copyValues(android.transition.TransitionValues source, android.support.transition.TransitionValues dest) {
        if (source == null) {
            return;
        }
        dest.view = source.view;
        if (source.values.size() > 0) {
            dest.values.putAll(source.values);
        }
    }

    static void copyValues(android.support.transition.TransitionValues source, android.transition.TransitionValues dest) {
        if (source == null) {
            return;
        }
        dest.view = source.view;
        if (source.values.size() > 0) {
            dest.values.putAll(source.values);
        }
    }

    static void wrapCaptureStartValues(android.support.transition.TransitionInterface transition, android.transition.TransitionValues transitionValues) {
        android.support.transition.TransitionValues externalValues = new android.support.transition.TransitionValues();
        android.support.transition.TransitionKitKat.copyValues(transitionValues, externalValues);
        transition.captureStartValues(externalValues);
        android.support.transition.TransitionKitKat.copyValues(externalValues, transitionValues);
    }

    static void wrapCaptureEndValues(android.support.transition.TransitionInterface transition, android.transition.TransitionValues transitionValues) {
        android.support.transition.TransitionValues externalValues = new android.support.transition.TransitionValues();
        android.support.transition.TransitionKitKat.copyValues(transitionValues, externalValues);
        transition.captureEndValues(externalValues);
        android.support.transition.TransitionKitKat.copyValues(externalValues, transitionValues);
    }

    static android.support.transition.TransitionValues convertToSupport(android.transition.TransitionValues values) {
        if (values == null) {
            return null;
        }
        android.support.transition.TransitionValues supportValues = new android.support.transition.TransitionValues();
        android.support.transition.TransitionKitKat.copyValues(values, supportValues);
        return supportValues;
    }

    static android.transition.TransitionValues convertToPlatform(android.support.transition.TransitionValues values) {
        if (values == null) {
            return null;
        }
        android.transition.TransitionValues platformValues = new android.transition.TransitionValues();
        android.support.transition.TransitionKitKat.copyValues(values, platformValues);
        return platformValues;
    }

    @java.lang.Override
    public void init(android.support.transition.TransitionInterface external, java.lang.Object internal) {
        mExternalTransition = external;
        if (internal == null) {
            mTransition = new android.support.transition.TransitionKitKat.TransitionWrapper(external);
        } else {
            mTransition = ((android.transition.Transition) (internal));
        }
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl addListener(android.support.transition.TransitionInterfaceListener listener) {
        if (mCompatListener == null) {
            mCompatListener = new android.support.transition.TransitionKitKat.CompatListener();
            mTransition.addListener(mCompatListener);
        }
        mCompatListener.addListener(listener);
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl removeListener(android.support.transition.TransitionInterfaceListener listener) {
        if (mCompatListener == null) {
            return this;
        }
        mCompatListener.removeListener(listener);
        if (mCompatListener.isEmpty()) {
            mTransition.removeListener(mCompatListener);
            mCompatListener = null;
        }
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl addTarget(android.view.View target) {
        mTransition.addTarget(target);
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl addTarget(int targetId) {
        mTransition.addTarget(targetId);
        return this;
    }

    @java.lang.Override
    public void captureEndValues(android.support.transition.TransitionValues transitionValues) {
        android.transition.TransitionValues internalValues = new android.transition.TransitionValues();
        android.support.transition.TransitionKitKat.copyValues(transitionValues, internalValues);
        mTransition.captureEndValues(internalValues);
        android.support.transition.TransitionKitKat.copyValues(internalValues, transitionValues);
    }

    @java.lang.Override
    public void captureStartValues(android.support.transition.TransitionValues transitionValues) {
        android.transition.TransitionValues internalValues = new android.transition.TransitionValues();
        android.support.transition.TransitionKitKat.copyValues(transitionValues, internalValues);
        mTransition.captureStartValues(internalValues);
        android.support.transition.TransitionKitKat.copyValues(internalValues, transitionValues);
    }

    @java.lang.Override
    public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValues startValues, android.support.transition.TransitionValues endValues) {
        android.transition.TransitionValues internalStartValues;
        android.transition.TransitionValues internalEndValues;
        if (startValues != null) {
            internalStartValues = new android.transition.TransitionValues();
            android.support.transition.TransitionKitKat.copyValues(startValues, internalStartValues);
        } else {
            internalStartValues = null;
        }
        if (endValues != null) {
            internalEndValues = new android.transition.TransitionValues();
            android.support.transition.TransitionKitKat.copyValues(endValues, internalEndValues);
        } else {
            internalEndValues = null;
        }
        return mTransition.createAnimator(sceneRoot, internalStartValues, internalEndValues);
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl excludeChildren(android.view.View target, boolean exclude) {
        mTransition.excludeChildren(target, exclude);
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl excludeChildren(int targetId, boolean exclude) {
        mTransition.excludeChildren(targetId, exclude);
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl excludeChildren(java.lang.Class type, boolean exclude) {
        mTransition.excludeChildren(type, exclude);
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl excludeTarget(android.view.View target, boolean exclude) {
        mTransition.excludeTarget(target, exclude);
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl excludeTarget(int targetId, boolean exclude) {
        mTransition.excludeTarget(targetId, exclude);
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl excludeTarget(java.lang.Class type, boolean exclude) {
        mTransition.excludeTarget(type, exclude);
        return this;
    }

    @java.lang.Override
    public long getDuration() {
        return mTransition.getDuration();
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl setDuration(long duration) {
        mTransition.setDuration(duration);
        return this;
    }

    @java.lang.Override
    public android.animation.TimeInterpolator getInterpolator() {
        return mTransition.getInterpolator();
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl setInterpolator(android.animation.TimeInterpolator interpolator) {
        mTransition.setInterpolator(interpolator);
        return this;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return mTransition.getName();
    }

    @java.lang.Override
    public long getStartDelay() {
        return mTransition.getStartDelay();
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl setStartDelay(long startDelay) {
        mTransition.setStartDelay(startDelay);
        return this;
    }

    @java.lang.Override
    public java.util.List<java.lang.Integer> getTargetIds() {
        return mTransition.getTargetIds();
    }

    @java.lang.Override
    public java.util.List<android.view.View> getTargets() {
        return mTransition.getTargets();
    }

    @java.lang.Override
    public java.lang.String[] getTransitionProperties() {
        return mTransition.getTransitionProperties();
    }

    @java.lang.Override
    public android.support.transition.TransitionValues getTransitionValues(android.view.View view, boolean start) {
        android.support.transition.TransitionValues values = new android.support.transition.TransitionValues();
        android.support.transition.TransitionKitKat.copyValues(mTransition.getTransitionValues(view, start), values);
        return values;
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl removeTarget(android.view.View target) {
        mTransition.removeTarget(target);
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionImpl removeTarget(int targetId) {
        if (targetId > 0) {
            // Workaround for the issue that the platform version calls remove(int)
            // when it should call remove(Integer)
            getTargetIds().remove(((java.lang.Integer) (targetId)));
        }
        return this;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return mTransition.toString();
    }

    private static class TransitionWrapper extends android.transition.Transition {
        private android.support.transition.TransitionInterface mTransition;

        public TransitionWrapper(android.support.transition.TransitionInterface transition) {
            mTransition = transition;
        }

        @java.lang.Override
        public void captureStartValues(android.transition.TransitionValues transitionValues) {
            android.support.transition.TransitionKitKat.wrapCaptureStartValues(mTransition, transitionValues);
        }

        @java.lang.Override
        public void captureEndValues(android.transition.TransitionValues transitionValues) {
            android.support.transition.TransitionKitKat.wrapCaptureEndValues(mTransition, transitionValues);
        }

        @java.lang.Override
        public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
            return mTransition.createAnimator(sceneRoot, android.support.transition.TransitionKitKat.convertToSupport(startValues), android.support.transition.TransitionKitKat.convertToSupport(endValues));
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    private class CompatListener implements android.transition.Transition.TransitionListener {
        private final java.util.ArrayList<android.support.transition.TransitionInterfaceListener> mListeners = new java.util.ArrayList<>();

        CompatListener() {
        }

        void addListener(android.support.transition.TransitionInterfaceListener listener) {
            mListeners.add(listener);
        }

        void removeListener(android.support.transition.TransitionInterfaceListener listener) {
            mListeners.remove(listener);
        }

        boolean isEmpty() {
            return mListeners.isEmpty();
        }

        @java.lang.Override
        public void onTransitionStart(android.transition.Transition transition) {
            for (android.support.transition.TransitionInterfaceListener listener : mListeners) {
                listener.onTransitionStart(mExternalTransition);
            }
        }

        @java.lang.Override
        public void onTransitionEnd(android.transition.Transition transition) {
            for (android.support.transition.TransitionInterfaceListener listener : mListeners) {
                listener.onTransitionEnd(mExternalTransition);
            }
        }

        @java.lang.Override
        public void onTransitionCancel(android.transition.Transition transition) {
            for (android.support.transition.TransitionInterfaceListener listener : mListeners) {
                listener.onTransitionCancel(mExternalTransition);
            }
        }

        @java.lang.Override
        public void onTransitionPause(android.transition.Transition transition) {
            for (android.support.transition.TransitionInterfaceListener listener : mListeners) {
                listener.onTransitionPause(mExternalTransition);
            }
        }

        @java.lang.Override
        public void onTransitionResume(android.transition.Transition transition) {
            for (android.support.transition.TransitionInterfaceListener listener : mListeners) {
                listener.onTransitionResume(mExternalTransition);
            }
        }
    }
}

