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


/**
 * Utility class for creating a default transition that automatically fades,
 * moves, and resizes views during a scene change.
 *
 * <p>Unlike the platform version, this does not support use in XML resources.</p>
 */
public class AutoTransition extends android.support.transition.TransitionSet {
    /**
     * Constructs an AutoTransition object, which is a TransitionSet which
     * first fades out disappearing targets, then moves and resizes existing
     * targets, and finally fades in appearing targets.
     */
    public AutoTransition() {
        setOrdering(android.support.transition.TransitionSet.ORDERING_SEQUENTIAL);
        addTransition(new android.support.transition.Fade(android.support.transition.Fade.OUT)).addTransition(new android.support.transition.ChangeBounds()).addTransition(new android.support.transition.Fade(android.support.transition.Fade.IN));
    }
}

