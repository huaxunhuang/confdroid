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
 * A TransitionSet is a parent of child transitions (including other
 * TransitionSets). Using TransitionSets enables more complex
 * choreography of transitions, where some sets play {@link #ORDERING_TOGETHER} and
 * others play {@link #ORDERING_SEQUENTIAL}. For example, {@link AutoTransition}
 * uses a TransitionSet to sequentially play a Fade(Fade.OUT), followed by
 * a {@link ChangeBounds}, followed by a Fade(Fade.OUT) transition.
 *
 * <p>A TransitionSet can be described in a resource file by using the
 * tag <code>transitionSet</code>, along with the standard
 * attributes of {@link android.R.styleable#TransitionSet} and
 * {@link android.R.styleable#Transition}. Child transitions of the
 * TransitionSet object can be loaded by adding those child tags inside the
 * enclosing <code>transitionSet</code> tag. For example, the following xml
 * describes a TransitionSet that plays a Fade and then a ChangeBounds
 * transition on the affected view targets:</p>
 * <pre>
 *     &lt;transitionSet xmlns:android="http://schemas.android.com/apk/res/android"
 *             android:transitionOrdering="sequential"&gt;
 *         &lt;fade/&gt;
 *         &lt;changeBounds/&gt;
 *     &lt;/transitionSet&gt;
 * </pre>
 */
public class TransitionSet extends android.transition.Transition {
    /**
     * Flag indicating the the interpolator changed.
     */
    private static final int FLAG_CHANGE_INTERPOLATOR = 0x1;

    /**
     * Flag indicating the the propagation changed.
     */
    private static final int FLAG_CHANGE_PROPAGATION = 0x2;

    /**
     * Flag indicating the the path motion changed.
     */
    private static final int FLAG_CHANGE_PATH_MOTION = 0x4;

    /**
     * Flag indicating the the epicentera callback changed.
     */
    static final int FLAG_CHANGE_EPICENTER = 0x8;

    java.util.ArrayList<android.transition.Transition> mTransitions = new java.util.ArrayList<android.transition.Transition>();

    private boolean mPlayTogether = true;

    int mCurrentListeners;

    boolean mStarted = false;

    // Flags to know whether or not the interpolator, path motion, epicenter, propagation
    // have changed
    private int mChangeFlags = 0;

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

    /**
     * Constructs an empty transition set. Add child transitions to the
     * set by calling {@link #addTransition(Transition)} )}. By default,
     * child transitions will play {@link #ORDERING_TOGETHER together}.
     */
    public TransitionSet() {
    }

    public TransitionSet(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TransitionSet);
        int ordering = a.getInt(R.styleable.TransitionSet_transitionOrdering, android.transition.TransitionSet.ORDERING_TOGETHER);
        setOrdering(ordering);
        a.recycle();
    }

    /**
     * Sets the play order of this set's child transitions.
     *
     * @param ordering
     * 		{@link #ORDERING_TOGETHER} to play this set's child
     * 		transitions together, {@link #ORDERING_SEQUENTIAL} to play the child
     * 		transitions in sequence.
     * @return This transitionSet object.
     */
    public android.transition.TransitionSet setOrdering(int ordering) {
        switch (ordering) {
            case android.transition.TransitionSet.ORDERING_SEQUENTIAL :
                mPlayTogether = false;
                break;
            case android.transition.TransitionSet.ORDERING_TOGETHER :
                mPlayTogether = true;
                break;
            default :
                throw new android.util.AndroidRuntimeException(("Invalid parameter for TransitionSet " + "ordering: ") + ordering);
        }
        return this;
    }

    /**
     * Returns the ordering of this TransitionSet. By default, the value is
     * {@link #ORDERING_TOGETHER}.
     *
     * @return {@link #ORDERING_TOGETHER} if child transitions will play at the same
    time, {@link #ORDERING_SEQUENTIAL} if they will play in sequence.
     * @see #setOrdering(int)
     */
    public int getOrdering() {
        return mPlayTogether ? android.transition.TransitionSet.ORDERING_TOGETHER : android.transition.TransitionSet.ORDERING_SEQUENTIAL;
    }

    /**
     * Adds child transition to this set. The order in which this child transition
     * is added relative to other child transitions that are added, in addition to
     * the {@link #getOrdering() ordering} property, determines the
     * order in which the transitions are started.
     *
     * <p>If this transitionSet has a {@link #getDuration() duration},
     * {@link #getInterpolator() interpolator}, {@link #getPropagation() propagation delay},
     * {@link #getPathMotion() path motion}, or
     * {@link #setEpicenterCallback(EpicenterCallback) epicenter callback}
     * set on it, the child transition will inherit the values that are set.
     * Transitions are assumed to have a maximum of one transitionSet parent.</p>
     *
     * @param transition
     * 		A non-null child transition to be added to this set.
     * @return This transitionSet object.
     */
    public android.transition.TransitionSet addTransition(android.transition.Transition transition) {
        if (transition != null) {
            addTransitionInternal(transition);
            if (mDuration >= 0) {
                transition.setDuration(mDuration);
            }
            if ((mChangeFlags & android.transition.TransitionSet.FLAG_CHANGE_INTERPOLATOR) != 0) {
                transition.setInterpolator(getInterpolator());
            }
            if ((mChangeFlags & android.transition.TransitionSet.FLAG_CHANGE_PROPAGATION) != 0) {
                transition.setPropagation(getPropagation());
            }
            if ((mChangeFlags & android.transition.TransitionSet.FLAG_CHANGE_PATH_MOTION) != 0) {
                transition.setPathMotion(getPathMotion());
            }
            if ((mChangeFlags & android.transition.TransitionSet.FLAG_CHANGE_EPICENTER) != 0) {
                transition.setEpicenterCallback(getEpicenterCallback());
            }
        }
        return this;
    }

    private void addTransitionInternal(android.transition.Transition transition) {
        mTransitions.add(transition);
        transition.mParent = this;
    }

    /**
     * Returns the number of child transitions in the TransitionSet.
     *
     * @return The number of child transitions in the TransitionSet.
     * @see #addTransition(Transition)
     * @see #getTransitionAt(int)
     */
    public int getTransitionCount() {
        return mTransitions.size();
    }

    /**
     * Returns the child Transition at the specified position in the TransitionSet.
     *
     * @param index
     * 		The position of the Transition to retrieve.
     * @see #addTransition(Transition)
     * @see #getTransitionCount()
     */
    public android.transition.Transition getTransitionAt(int index) {
        if ((index < 0) || (index >= mTransitions.size())) {
            return null;
        }
        return mTransitions.get(index);
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
    public android.transition.TransitionSet setDuration(long duration) {
        super.setDuration(duration);
        if ((mDuration >= 0) && (mTransitions != null)) {
            int numTransitions = mTransitions.size();
            for (int i = 0; i < numTransitions; ++i) {
                mTransitions.get(i).setDuration(duration);
            }
        }
        return this;
    }

    @java.lang.Override
    public android.transition.TransitionSet setStartDelay(long startDelay) {
        return ((android.transition.TransitionSet) (super.setStartDelay(startDelay)));
    }

    @java.lang.Override
    public android.transition.TransitionSet setInterpolator(android.animation.TimeInterpolator interpolator) {
        mChangeFlags |= android.transition.TransitionSet.FLAG_CHANGE_INTERPOLATOR;
        if (mTransitions != null) {
            int numTransitions = mTransitions.size();
            for (int i = 0; i < numTransitions; ++i) {
                mTransitions.get(i).setInterpolator(interpolator);
            }
        }
        return ((android.transition.TransitionSet) (super.setInterpolator(interpolator)));
    }

    @java.lang.Override
    public android.transition.TransitionSet addTarget(android.view.View target) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).addTarget(target);
        }
        return ((android.transition.TransitionSet) (super.addTarget(target)));
    }

    @java.lang.Override
    public android.transition.TransitionSet addTarget(int targetId) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).addTarget(targetId);
        }
        return ((android.transition.TransitionSet) (super.addTarget(targetId)));
    }

    @java.lang.Override
    public android.transition.TransitionSet addTarget(java.lang.String targetName) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).addTarget(targetName);
        }
        return ((android.transition.TransitionSet) (super.addTarget(targetName)));
    }

    @java.lang.Override
    public android.transition.TransitionSet addTarget(java.lang.Class targetType) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).addTarget(targetType);
        }
        return ((android.transition.TransitionSet) (super.addTarget(targetType)));
    }

    @java.lang.Override
    public android.transition.TransitionSet addListener(android.transition.Transition.TransitionListener listener) {
        return ((android.transition.TransitionSet) (super.addListener(listener)));
    }

    @java.lang.Override
    public android.transition.TransitionSet removeTarget(int targetId) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).removeTarget(targetId);
        }
        return ((android.transition.TransitionSet) (super.removeTarget(targetId)));
    }

    @java.lang.Override
    public android.transition.TransitionSet removeTarget(android.view.View target) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).removeTarget(target);
        }
        return ((android.transition.TransitionSet) (super.removeTarget(target)));
    }

    @java.lang.Override
    public android.transition.TransitionSet removeTarget(java.lang.Class target) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).removeTarget(target);
        }
        return ((android.transition.TransitionSet) (super.removeTarget(target)));
    }

    @java.lang.Override
    public android.transition.TransitionSet removeTarget(java.lang.String target) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).removeTarget(target);
        }
        return ((android.transition.TransitionSet) (super.removeTarget(target)));
    }

    @java.lang.Override
    public android.transition.Transition excludeTarget(android.view.View target, boolean exclude) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).excludeTarget(target, exclude);
        }
        return super.excludeTarget(target, exclude);
    }

    @java.lang.Override
    public android.transition.Transition excludeTarget(java.lang.String targetName, boolean exclude) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).excludeTarget(targetName, exclude);
        }
        return super.excludeTarget(targetName, exclude);
    }

    @java.lang.Override
    public android.transition.Transition excludeTarget(int targetId, boolean exclude) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).excludeTarget(targetId, exclude);
        }
        return super.excludeTarget(targetId, exclude);
    }

    @java.lang.Override
    public android.transition.Transition excludeTarget(java.lang.Class type, boolean exclude) {
        for (int i = 0; i < mTransitions.size(); i++) {
            mTransitions.get(i).excludeTarget(type, exclude);
        }
        return super.excludeTarget(type, exclude);
    }

    @java.lang.Override
    public android.transition.TransitionSet removeListener(android.transition.Transition.TransitionListener listener) {
        return ((android.transition.TransitionSet) (super.removeListener(listener)));
    }

    @java.lang.Override
    public void setPathMotion(android.transition.PathMotion pathMotion) {
        super.setPathMotion(pathMotion);
        mChangeFlags |= android.transition.TransitionSet.FLAG_CHANGE_PATH_MOTION;
        if (mTransitions != null) {
            for (int i = 0; i < mTransitions.size(); i++) {
                mTransitions.get(i).setPathMotion(pathMotion);
            }
        }
    }

    /**
     * Removes the specified child transition from this set.
     *
     * @param transition
     * 		The transition to be removed.
     * @return This transitionSet object.
     */
    public android.transition.TransitionSet removeTransition(android.transition.Transition transition) {
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
        android.transition.TransitionSet.TransitionSetListener listener = new android.transition.TransitionSet.TransitionSetListener(this);
        for (android.transition.Transition childTransition : mTransitions) {
            childTransition.addListener(listener);
        }
        mCurrentListeners = mTransitions.size();
    }

    /**
     * This listener is used to detect when all child transitions are done, at
     * which point this transition set is also done.
     */
    static class TransitionSetListener extends android.transition.TransitionListenerAdapter {
        android.transition.TransitionSet mTransitionSet;

        TransitionSetListener(android.transition.TransitionSet transitionSet) {
            mTransitionSet = transitionSet;
        }

        @java.lang.Override
        public void onTransitionStart(android.transition.Transition transition) {
            if (!mTransitionSet.mStarted) {
                mTransitionSet.start();
                mTransitionSet.mStarted = true;
            }
        }

        @java.lang.Override
        public void onTransitionEnd(android.transition.Transition transition) {
            --mTransitionSet.mCurrentListeners;
            if (mTransitionSet.mCurrentListeners == 0) {
                // All child trans
                mTransitionSet.mStarted = false;
                mTransitionSet.end();
            }
            transition.removeListener(this);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void createAnimators(android.view.ViewGroup sceneRoot, android.transition.TransitionValuesMaps startValues, android.transition.TransitionValuesMaps endValues, java.util.ArrayList<android.transition.TransitionValues> startValuesList, java.util.ArrayList<android.transition.TransitionValues> endValuesList) {
        long startDelay = getStartDelay();
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            android.transition.Transition childTransition = mTransitions.get(i);
            // We only set the start delay on the first transition if we are playing
            // the transitions sequentially.
            if ((startDelay > 0) && (mPlayTogether || (i == 0))) {
                long childStartDelay = childTransition.getStartDelay();
                if (childStartDelay > 0) {
                    childTransition.setStartDelay(startDelay + childStartDelay);
                } else {
                    childTransition.setStartDelay(startDelay);
                }
            }
            childTransition.createAnimators(sceneRoot, startValues, endValues, startValuesList, endValuesList);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void runAnimators() {
        if (mTransitions.isEmpty()) {
            start();
            end();
            return;
        }
        setupStartEndListeners();
        int numTransitions = mTransitions.size();
        if (!mPlayTogether) {
            // Setup sequence with listeners
            // TODO: Need to add listeners in such a way that we can remove them later if canceled
            for (int i = 1; i < numTransitions; ++i) {
                android.transition.Transition previousTransition = mTransitions.get(i - 1);
                final android.transition.Transition nextTransition = mTransitions.get(i);
                previousTransition.addListener(new android.transition.TransitionListenerAdapter() {
                    @java.lang.Override
                    public void onTransitionEnd(android.transition.Transition transition) {
                        nextTransition.runAnimators();
                        transition.removeListener(this);
                    }
                });
            }
            android.transition.Transition firstTransition = mTransitions.get(0);
            if (firstTransition != null) {
                firstTransition.runAnimators();
            }
        } else {
            for (int i = 0; i < numTransitions; ++i) {
                mTransitions.get(i).runAnimators();
            }
        }
    }

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        if (isValidTarget(transitionValues.view)) {
            for (android.transition.Transition childTransition : mTransitions) {
                if (childTransition.isValidTarget(transitionValues.view)) {
                    childTransition.captureStartValues(transitionValues);
                    transitionValues.targetedTransitions.add(childTransition);
                }
            }
        }
    }

    @java.lang.Override
    public void captureEndValues(android.transition.TransitionValues transitionValues) {
        if (isValidTarget(transitionValues.view)) {
            for (android.transition.Transition childTransition : mTransitions) {
                if (childTransition.isValidTarget(transitionValues.view)) {
                    childTransition.captureEndValues(transitionValues);
                    transitionValues.targetedTransitions.add(childTransition);
                }
            }
        }
    }

    @java.lang.Override
    void capturePropagationValues(android.transition.TransitionValues transitionValues) {
        super.capturePropagationValues(transitionValues);
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            mTransitions.get(i).capturePropagationValues(transitionValues);
        }
    }

    /**
     *
     *
     * @unknown 
     */
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
    @java.lang.Override
    protected void cancel() {
        super.cancel();
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            mTransitions.get(i).cancel();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    void forceToEnd(android.view.ViewGroup sceneRoot) {
        super.forceToEnd(sceneRoot);
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            mTransitions.get(i).forceToEnd(sceneRoot);
        }
    }

    @java.lang.Override
    android.transition.TransitionSet setSceneRoot(android.view.ViewGroup sceneRoot) {
        super.setSceneRoot(sceneRoot);
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            mTransitions.get(i).setSceneRoot(sceneRoot);
        }
        return ((android.transition.TransitionSet) (this));
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
    public void setPropagation(android.transition.TransitionPropagation propagation) {
        super.setPropagation(propagation);
        mChangeFlags |= android.transition.TransitionSet.FLAG_CHANGE_PROPAGATION;
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            mTransitions.get(i).setPropagation(propagation);
        }
    }

    @java.lang.Override
    public void setEpicenterCallback(android.transition.Transition.EpicenterCallback epicenterCallback) {
        super.setEpicenterCallback(epicenterCallback);
        mChangeFlags |= android.transition.TransitionSet.FLAG_CHANGE_EPICENTER;
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            mTransitions.get(i).setEpicenterCallback(epicenterCallback);
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
    public android.transition.TransitionSet clone() {
        android.transition.TransitionSet clone = ((android.transition.TransitionSet) (super.clone()));
        clone.mTransitions = new java.util.ArrayList<android.transition.Transition>();
        int numTransitions = mTransitions.size();
        for (int i = 0; i < numTransitions; ++i) {
            clone.addTransitionInternal(((android.transition.Transition) (mTransitions.get(i).clone())));
        }
        return clone;
    }
}

