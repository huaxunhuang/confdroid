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


class TransitionSetKitKat extends android.support.transition.TransitionKitKat implements android.support.transition.TransitionSetImpl {
    private android.transition.TransitionSet mTransitionSet;

    public TransitionSetKitKat(android.support.transition.TransitionInterface transition) {
        mTransitionSet = new android.transition.TransitionSet();
        init(transition, mTransitionSet);
    }

    @java.lang.Override
    public int getOrdering() {
        return mTransitionSet.getOrdering();
    }

    @java.lang.Override
    public android.support.transition.TransitionSetKitKat setOrdering(int ordering) {
        mTransitionSet.setOrdering(ordering);
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionSetKitKat addTransition(android.support.transition.TransitionImpl transition) {
        mTransitionSet.addTransition(((android.support.transition.TransitionKitKat) (transition)).mTransition);
        return this;
    }

    @java.lang.Override
    public android.support.transition.TransitionSetKitKat removeTransition(android.support.transition.TransitionImpl transition) {
        mTransitionSet.removeTransition(((android.support.transition.TransitionKitKat) (transition)).mTransition);
        return this;
    }
}

