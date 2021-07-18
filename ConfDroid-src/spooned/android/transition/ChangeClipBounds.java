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
 * ChangeClipBounds captures the {@link android.view.View#getClipBounds()} before and after the
 * scene change and animates those changes during the transition.
 */
public class ChangeClipBounds extends android.transition.Transition {
    private static final java.lang.String TAG = "ChangeTransform";

    private static final java.lang.String PROPNAME_CLIP = "android:clipBounds:clip";

    private static final java.lang.String PROPNAME_BOUNDS = "android:clipBounds:bounds";

    private static final java.lang.String[] sTransitionProperties = new java.lang.String[]{ android.transition.ChangeClipBounds.PROPNAME_CLIP };

    public ChangeClipBounds() {
    }

    public ChangeClipBounds(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    @java.lang.Override
    public java.lang.String[] getTransitionProperties() {
        return android.transition.ChangeClipBounds.sTransitionProperties;
    }

    private void captureValues(android.transition.TransitionValues values) {
        android.view.View view = values.view;
        if (view.getVisibility() == android.view.View.GONE) {
            return;
        }
        android.graphics.Rect clip = view.getClipBounds();
        values.values.put(android.transition.ChangeClipBounds.PROPNAME_CLIP, clip);
        if (clip == null) {
            android.graphics.Rect bounds = new android.graphics.Rect(0, 0, view.getWidth(), view.getHeight());
            values.values.put(android.transition.ChangeClipBounds.PROPNAME_BOUNDS, bounds);
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
    public android.animation.Animator createAnimator(final android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if ((((startValues == null) || (endValues == null)) || (!startValues.values.containsKey(android.transition.ChangeClipBounds.PROPNAME_CLIP))) || (!endValues.values.containsKey(android.transition.ChangeClipBounds.PROPNAME_CLIP))) {
            return null;
        }
        android.graphics.Rect start = ((android.graphics.Rect) (startValues.values.get(android.transition.ChangeClipBounds.PROPNAME_CLIP)));
        android.graphics.Rect end = ((android.graphics.Rect) (endValues.values.get(android.transition.ChangeClipBounds.PROPNAME_CLIP)));
        boolean endIsNull = end == null;
        if ((start == null) && (end == null)) {
            return null;// No animation required since there is no clip.

        }
        if (start == null) {
            start = ((android.graphics.Rect) (startValues.values.get(android.transition.ChangeClipBounds.PROPNAME_BOUNDS)));
        } else
            if (end == null) {
                end = ((android.graphics.Rect) (endValues.values.get(android.transition.ChangeClipBounds.PROPNAME_BOUNDS)));
            }

        if (start.equals(end)) {
            return null;
        }
        endValues.view.setClipBounds(start);
        android.animation.RectEvaluator evaluator = new android.animation.RectEvaluator(new android.graphics.Rect());
        android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofObject(endValues.view, "clipBounds", evaluator, start, end);
        if (endIsNull) {
            final android.view.View endView = endValues.view;
            animator.addListener(new android.animation.AnimatorListenerAdapter() {
                @java.lang.Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    endView.setClipBounds(null);
                }
            });
        }
        return animator;
    }
}

