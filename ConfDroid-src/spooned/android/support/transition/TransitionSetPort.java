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


class TransitionSetPort extends android.support.transition.TransitionPort {
    /**
     * A flag used to indicate that the child transitions of this set
     * should all start at the same time.
     */
    public static final int ORDERING_TOGETHER = 0;

    /**
     * A flag used to indicate that the child transitions of this set should
     * play in sequence; when one child transition ends, the next child
     * transition begins. Note that a transition does not end until all
     * instances of it (which are playing on all applicable targets of the
     * transition) end.
     */
    public static final int ORDERING_SEQUENTIAL = 1;

    java.util.ArrayList<android.support.transition.TransitionPort> mTransitions = new java.util.ArrayList<android.support.transition.TransitionPort>();

    int mCurrentListeners;

    boolean mStarted = false;

    private boolean mPlayTogether = true;

    public TransitionSetPort() {
    }

    public int getOrdering() {
        return mPlayTogether ? android.support.transition.TransitionSetPort.ORDERING_TOGETHER : android.support.transition.TransitionSetPort.ORDERING_SEQUENTIAL;
    }

    public android.support.transition.TransitionSetPort setOrdering(int ordering) {
        switch (ordering) {
            case android.support.transition.TransitionSetPort.ORDERING_SEQUENTIAL :
                mPlayTogether = false;
                break;
            case android.support.transition.TransitionSetPort.ORDERING_TOGETHER :
                mPlayTogether = true;
                break;
            default :
                throw new android.util.AndroidRuntimeException(("Invalid parameter for TransitionSet " + "ordering: ") + ordering);
        }
        return this;
    }

    public android.support.transition.TransitionSetPort addTransition(android.support.transition.TransitionPort transition) {
        if (transition != null) {
            mTransitions.add(transition);
            transition.mParent = this;
            if (mDuration >= 0) {
                transition.setDuration(mDuration);
            }
        }
        return this;
    }

    /**
     * Setting a non-negative duration on a TransitionSet causes all of the child
     * transitions (current and future) to inherit this duration.
     *
     * @param duration
     * 		The length of the animation, in milliseconds.
     * @return This transitionSet object.
     */
    @java.lang.Override
    public android.support.transition.TransitionSetPort setDuration(long duration) {
        super.setDuration(duration);
        if (mDuration >= 0) {
            int numTransitions = mTransitions.size();
            for (int i = 0; i < numTransitions; ++i) {
                mTransitions.get(i).setDuration(duration);
            }
        }
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionSetPort setStartDelay(long startDelay) {
        return ((android.support.transition.TransitionSetPort) (super.setStartDelay(startDelay)));
    }

    @java.lang.Override
    public android.support.transition.TransitionSetPort setInterpolator(android.animation.TimeInterpolator interpolator) {
        return ((android.support.transition.TransitionSetPort) (super.setInterpolator(interpolator)));
    }

    @java.lang.Override
    public android.support.transition.TransitionSetPort addTarget(android.view.View target) {
        return ((android.support.transition.TransitionSetPort) (super.addTarget(target)));
    }

    @java.lang.Override
    public android.support.transition.TransitionSetPort addTarget(int targetId) {
        return ((android.support.transition.TransitionSetPort) (super.addTarget(targetId)));
    }

    @java.lang.Override
    public android.support.transition.TransitionSetPort addListener(android.support.transition.TransitionPort.TransitionListener listener) {
        return ((android.support.transition.TransitionSetPort) (super.addListener(listener)));
    }

    @java.lang.Override
    public android.support.transition.TransitionSetPort removeTarget(int targetId) {
        return ((android.support.transition.TransitionSetPort) (super.removeTarget(targetId)));
    }

    @java.lang.Override
    public android.support.transition.TransitionSetPort removeTarget(android.view.View target) {
        return ((android.support.transition.TransitionSetPort) (super.removeTarget(target)));
    }

    @java.lang.Override
    public android.support.transition.TransitionSetPort removeListener(android.support.transition.TransitionPort.TransitionListener listener) {
        return ((android.support.transition.TransitionSetPort) (super.removeListener(listener)));
    }

    public android.support.transition.TransitionSetPort removeTransition(android.support.transition.TransitionPort transition) {
        mTransitions.remove(transition);
        transition.mParent = null;
        return this;
    }

    /**
     * Sets up listeners for each of the child transitions. This is used to
     * determine when this transition set is finished (all child transitions
     * must finish first).
     */
    private void setupStartEndListeners() {
        android.support.transition.TransitionSetPort.TransitionSetListener listener = new android.support.transition.TransitionSetPort.TransitionSetListener(this);
        for (android.support.transition.TransitionPort childTransition : mTransitions) {
            childTransition.addListener(listener);
        }
        mCurrentListeners = mTransitions.size();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    protected void createAnimators(android.view.ViewGroup sceneRoot, android.support.transition.TransitionValuesMaps startValues, android.support.transition.TransitionValuesMaps endValues) {
        for (android.support.transition.TransitionPort childTransition : mTransitions) {
            childTransition.createAnimators(sceneRoot, startValues, endValues);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    protected void runAnimators() {
        if (mTransitions.isEmpty()) {
            start();
            end();
            return;
        }
        setupStartEndListeners();
        if (!mPlayTogether) {
            // Setup sequence with listeners
            // TODO: Need to add listeners in such a way that we can remove them later if canceled
            for (int i = 1; i < mTransitions.size(); ++i) {
                android.support.transition.TransitionPort previousTransition = mTransitions.get(i - 1);
                final android.support.transition.TransitionPort nextTransition = mTransitions.get(i);
                previousTransition.addListener(new android.support.transition.TransitionPort.TransitionListenerAdapter() {
                    @java.lang.Override
                    public void onTransitionEnd(android.support.transition.TransitionPort transition) {
                        nextTransition.runAnimators();
                        transition.removeListener(this);
                    }
                });
            }
            android.support.transition.TransitionPort firstTransition = mTransitions.get(0);
            if (firstTransition != null) {
                firstTransition.runAnimators();
            }
        } else {
            for (android.support.transition.TransitionPort childTransition : mTransitions) {
                childTransition.runAnimators();
            }
        }
    }

    @java.lang.Override
    public void captureStartValues(android.support.transition.TransitionValues transitionValues) {
        int targetId = transitionValues.view.getId();
        if (isValidTarget(transitionValues.view, targetId)) {
            for (android.support.transition.TransitionPort childTransition : mTransitions) {
                if (childTransition.isValidTarget(transitionValues.view, targetId)) {
                    childTransition.captureStartValues(transitionValues);
                }
            }
        }
    }

    @java.lang.Override
    public void captureEndValues(android.support.transition.TransitionValues transitionValues) {
        int targetId = transitionValues.view.getId();
        if (isValidTarget(transitionValues.view, targetId)) {
            for (android.support.transition.TransitionPort childTransition : mTransitions) {
                if (childTransition.isValidTarget(transitionValues.view, targetId)) {
                    childTransition.captureEndValues(transitionValues);
                }
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public void pause(android.view.View sceneRoot) {
        super.pause(sceneRoot);
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            mTransitions.get(i).pause(sceneRoot);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public void resume(android.view.View sceneRoot) {
        super.resume(sceneRoot);
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            mTransitions.get(i).resume(sceneRoot);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    protected void cancel() {
        super.cancel();
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            mTransitions.get(i).cancel();
        }
    }

    @java.lang.Override
    android.support.transition.TransitionSetPort setSceneRoot(android.view.ViewGroup sceneRoot) {
        super.setSceneRoot(sceneRoot);
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            mTransitions.get(i).setSceneRoot(sceneRoot);
        }
        return ((android.support.transition.TransitionSetPort) (this));
    }

    @java.lang.Override
    void setCanRemoveViews(boolean canRemoveViews) {
        super.setCanRemoveViews(canRemoveViews);
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            mTransitions.get(i).setCanRemoveViews(canRemoveViews);
        }
    }

    @java.lang.Override
    java.lang.String toString(java.lang.String indent) {
        java.lang.String result = super.toString(indent);
        for (int i = 0; i < mTransitions.size(); ++i) {
            result += "\n" + mTransitions.get(i).toString(indent + "  ");
        }
        return result;
    }

    @java.lang.Override
    public android.support.transition.TransitionSetPort clone() {
        android.support.transition.TransitionSetPort clone = ((android.support.transition.TransitionSetPort) (super.clone()));
        clone.mTransitions = new java.util.ArrayList<android.support.transition.TransitionPort>();
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            clone.addTransition(((android.support.transition.TransitionPort) (mTransitions.get(i).clone())));
        }
        return clone;
    }

    /**
     * This listener is used to detect when all child transitions are done, at
     * which point this transition set is also done.
     */
    static class TransitionSetListener extends android.support.transition.TransitionPort.TransitionListenerAdapter {
        android.support.transition.TransitionSetPort mTransitionSet;

        TransitionSetListener(android.support.transition.TransitionSetPort transitionSet) {
            mTransitionSet = transitionSet;
        }

        @java.lang.Override
        public void onTransitionStart(android.support.transition.TransitionPort transition) {
            if (!mTransitionSet.mStarted) {
                mTransitionSet.start();
                mTransitionSet.mStarted = true;
            }
        }

        @java.lang.Override
        public void onTransitionEnd(android.support.transition.TransitionPort transition) {
            --mTransitionSet.mCurrentListeners;
            if (mTransitionSet.mCurrentListeners == 0) {
                // All child trans
                mTransitionSet.mStarted = false;
                mTransitionSet.end();
            }
            transition.removeListener(this);
        }
    }
}

