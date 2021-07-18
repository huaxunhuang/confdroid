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
 * This transition tracks changes during scene changes to the
 * {@link View#setBackground(android.graphics.drawable.Drawable) background}
 * property of its target views (when the background is a
 * {@link ColorDrawable}, as well as the
 * {@link TextView#setTextColor(android.content.res.ColorStateList)
 * color} of the text for target TextViews. If the color changes between
 * scenes, the color change is animated.
 *
 * @unknown 
 */
public class Recolor extends android.transition.Transition {
    private static final java.lang.String PROPNAME_BACKGROUND = "android:recolor:background";

    private static final java.lang.String PROPNAME_TEXT_COLOR = "android:recolor:textColor";

    public Recolor() {
    }

    public Recolor(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(android.transition.TransitionValues transitionValues) {
        transitionValues.values.put(android.transition.Recolor.PROPNAME_BACKGROUND, transitionValues.view.getBackground());
        if (transitionValues.view instanceof android.widget.TextView) {
            transitionValues.values.put(android.transition.Recolor.PROPNAME_TEXT_COLOR, ((android.widget.TextView) (transitionValues.view)).getCurrentTextColor());
        }
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
    public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if ((startValues == null) || (endValues == null)) {
            return null;
        }
        final android.view.View view = endValues.view;
        android.graphics.drawable.Drawable startBackground = ((android.graphics.drawable.Drawable) (startValues.values.get(android.transition.Recolor.PROPNAME_BACKGROUND)));
        android.graphics.drawable.Drawable endBackground = ((android.graphics.drawable.Drawable) (endValues.values.get(android.transition.Recolor.PROPNAME_BACKGROUND)));
        boolean changed = false;
        if ((startBackground instanceof android.graphics.drawable.ColorDrawable) && (endBackground instanceof android.graphics.drawable.ColorDrawable)) {
            android.graphics.drawable.ColorDrawable startColor = ((android.graphics.drawable.ColorDrawable) (startBackground));
            android.graphics.drawable.ColorDrawable endColor = ((android.graphics.drawable.ColorDrawable) (endBackground));
            if (startColor.getColor() != endColor.getColor()) {
                endColor.setColor(startColor.getColor());
                changed = true;
                return android.animation.ObjectAnimator.ofArgb(endBackground, "color", startColor.getColor(), endColor.getColor());
            }
        }
        if (view instanceof android.widget.TextView) {
            android.widget.TextView textView = ((android.widget.TextView) (view));
            int start = ((java.lang.Integer) (startValues.values.get(android.transition.Recolor.PROPNAME_TEXT_COLOR)));
            int end = ((java.lang.Integer) (endValues.values.get(android.transition.Recolor.PROPNAME_TEXT_COLOR)));
            if (start != end) {
                textView.setTextColor(end);
                changed = true;
                return android.animation.ObjectAnimator.ofArgb(textView, "textColor", start, end);
            }
        }
        return null;
    }
}

