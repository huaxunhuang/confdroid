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
 * This transition captures the scroll properties of targets before and after
 * the scene change and animates any changes.
 */
public class ChangeScroll extends android.transition.Transition {
    private static final java.lang.String PROPNAME_SCROLL_X = "android:changeScroll:x";

    private static final java.lang.String PROPNAME_SCROLL_Y = "android:changeScroll:y";

    private static final java.lang.String[] PROPERTIES = new java.lang.String[]{ android.transition.ChangeScroll.PROPNAME_SCROLL_X, android.transition.ChangeScroll.PROPNAME_SCROLL_Y };

    public ChangeScroll() {
    }

    public ChangeScroll(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public void captureEndValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public java.lang.String[] getTransitionProperties() {
        return android.transition.ChangeScroll.PROPERTIES;
    }

    private void captureValues(android.transition.TransitionValues transitionValues) {
        transitionValues.values.put(android.transition.ChangeScroll.PROPNAME_SCROLL_X, transitionValues.view.getScrollX());
        transitionValues.values.put(android.transition.ChangeScroll.PROPNAME_SCROLL_Y, transitionValues.view.getScrollY());
    }

    @java.lang.Override
    public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if ((startValues == null) || (endValues == null)) {
            return null;
        }
        final android.view.View view = endValues.view;
        int startX = ((java.lang.Integer) (startValues.values.get(android.transition.ChangeScroll.PROPNAME_SCROLL_X)));
        int endX = ((java.lang.Integer) (endValues.values.get(android.transition.ChangeScroll.PROPNAME_SCROLL_X)));
        int startY = ((java.lang.Integer) (startValues.values.get(android.transition.ChangeScroll.PROPNAME_SCROLL_Y)));
        int endY = ((java.lang.Integer) (endValues.values.get(android.transition.ChangeScroll.PROPNAME_SCROLL_Y)));
        android.animation.Animator scrollXAnimator = null;
        android.animation.Animator scrollYAnimator = null;
        if (startX != endX) {
            view.setScrollX(startX);
            scrollXAnimator = android.animation.ObjectAnimator.ofInt(view, "scrollX", startX, endX);
        }
        if (startY != endY) {
            view.setScrollY(startY);
            scrollYAnimator = android.animation.ObjectAnimator.ofInt(view, "scrollY", startY, endY);
        }
        return android.transition.TransitionUtils.mergeAnimators(scrollXAnimator, scrollYAnimator);
    }
}

