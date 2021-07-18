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
package android.transition;


/**
 * Base class for <code>TransitionPropagation</code>s that care about
 * View Visibility and the center position of the View.
 */
public abstract class VisibilityPropagation extends android.transition.TransitionPropagation {
    /**
     * The property key used for {@link android.view.View#getVisibility()}.
     */
    private static final java.lang.String PROPNAME_VISIBILITY = "android:visibilityPropagation:visibility";

    /**
     * The property key used for the center of the View in screen coordinates. This is an
     * int[2] with the index 0 taking the x coordinate and index 1 taking the y coordinate.
     */
    private static final java.lang.String PROPNAME_VIEW_CENTER = "android:visibilityPropagation:center";

    private static final java.lang.String[] VISIBILITY_PROPAGATION_VALUES = new java.lang.String[]{ android.transition.VisibilityPropagation.PROPNAME_VISIBILITY, android.transition.VisibilityPropagation.PROPNAME_VIEW_CENTER };

    @java.lang.Override
    public void captureValues(android.transition.TransitionValues values) {
        android.view.View view = values.view;
        java.lang.Integer visibility = ((java.lang.Integer) (values.values.get(android.transition.Visibility.PROPNAME_VISIBILITY)));
        if (visibility == null) {
            visibility = view.getVisibility();
        }
        values.values.put(android.transition.VisibilityPropagation.PROPNAME_VISIBILITY, visibility);
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        loc[0] += java.lang.Math.round(view.getTranslationX());
        loc[0] += view.getWidth() / 2;
        loc[1] += java.lang.Math.round(view.getTranslationY());
        loc[1] += view.getHeight() / 2;
        values.values.put(android.transition.VisibilityPropagation.PROPNAME_VIEW_CENTER, loc);
    }

    @java.lang.Override
    public java.lang.String[] getPropagationProperties() {
        return android.transition.VisibilityPropagation.VISIBILITY_PROPAGATION_VALUES;
    }

    /**
     * Returns {@link android.view.View#getVisibility()} for the View at the time the values
     * were captured.
     *
     * @param values
     * 		The TransitionValues captured at the start or end of the Transition.
     * @return {@link android.view.View#getVisibility()} for the View at the time the values
    were captured.
     */
    public int getViewVisibility(android.transition.TransitionValues values) {
        if (values == null) {
            return android.view.View.GONE;
        }
        java.lang.Integer visibility = ((java.lang.Integer) (values.values.get(android.transition.VisibilityPropagation.PROPNAME_VISIBILITY)));
        if (visibility == null) {
            return android.view.View.GONE;
        }
        return visibility;
    }

    /**
     * Returns the View's center x coordinate, relative to the screen, at the time the values
     * were captured.
     *
     * @param values
     * 		The TransitionValues captured at the start or end of the Transition.
     * @return the View's center x coordinate, relative to the screen, at the time the values
    were captured.
     */
    public int getViewX(android.transition.TransitionValues values) {
        return android.transition.VisibilityPropagation.getViewCoordinate(values, 0);
    }

    /**
     * Returns the View's center y coordinate, relative to the screen, at the time the values
     * were captured.
     *
     * @param values
     * 		The TransitionValues captured at the start or end of the Transition.
     * @return the View's center y coordinate, relative to the screen, at the time the values
    were captured.
     */
    public int getViewY(android.transition.TransitionValues values) {
        return android.transition.VisibilityPropagation.getViewCoordinate(values, 1);
    }

    private static int getViewCoordinate(android.transition.TransitionValues values, int coordinateIndex) {
        if (values == null) {
            return -1;
        }
        int[] coordinates = ((int[]) (values.values.get(android.transition.VisibilityPropagation.PROPNAME_VIEW_CENTER)));
        if (coordinates == null) {
            return -1;
        }
        return coordinates[coordinateIndex];
    }
}

