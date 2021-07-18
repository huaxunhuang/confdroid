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
package android.support.design.internal;


class BottomNavigationAnimationHelperIcs extends android.support.design.internal.BottomNavigationAnimationHelperBase {
    private static final long ACTIVE_ANIMATION_DURATION_MS = 115L;

    private final android.support.transition.TransitionSet mSet;

    BottomNavigationAnimationHelperIcs() {
        mSet = new android.support.transition.AutoTransition();
        mSet.setOrdering(android.support.transition.TransitionSet.ORDERING_TOGETHER);
        mSet.setDuration(android.support.design.internal.BottomNavigationAnimationHelperIcs.ACTIVE_ANIMATION_DURATION_MS);
        mSet.setInterpolator(new android.support.v4.view.animation.FastOutSlowInInterpolator());
        android.support.design.internal.TextScale textScale = new android.support.design.internal.TextScale();
        mSet.addTransition(textScale);
    }

    void beginDelayedTransition(android.view.ViewGroup view) {
        android.support.transition.TransitionManager.beginDelayedTransition(view, mSet);
    }
}

