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
 * Data structure which holds cached values for the transition.
 * The view field is the target which all of the values pertain to.
 * The values field is a map which holds information for fields
 * according to names selected by the transitions. These names should
 * be unique to avoid clobbering values stored by other transitions,
 * such as the convention project:transition_name:property_name. For
 * example, the platform might store a property "alpha" in a transition
 * "Fader" as "android:fader:alpha".
 *
 * <p>These values are cached during the
 * {@link android.support.transition.Transition#captureStartValues(TransitionValues)}
 * capture} phases of a scene change, once when the start values are captured
 * and again when the end values are captured. These start/end values are then
 * passed into the transitions via the
 * for {@link android.support.transition.Transition#createAnimator(android.view.ViewGroup,
 * TransitionValues, TransitionValues)} method.</p>
 */
public class TransitionValues {
    /**
     * The set of values tracked by transitions for this scene
     */
    public final java.util.Map<java.lang.String, java.lang.Object> values = new java.util.HashMap<>();

    /**
     * The View with these values
     */
    public android.view.View view;

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (other instanceof android.support.transition.TransitionValues) {
            if (view == ((android.support.transition.TransitionValues) (other)).view) {
                if (values.equals(((android.support.transition.TransitionValues) (other)).values)) {
                    return true;
                }
            }
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return (31 * view.hashCode()) + values.hashCode();
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String returnValue = ("TransitionValues@" + java.lang.Integer.toHexString(hashCode())) + ":\n";
        returnValue += ("    view = " + view) + "\n";
        returnValue += "    values:";
        for (java.lang.String s : values.keySet()) {
            returnValue += ((("    " + s) + ": ") + values.get(s)) + "\n";
        }
        return returnValue;
    }
}

