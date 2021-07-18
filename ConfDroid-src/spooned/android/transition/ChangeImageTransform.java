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
 * This Transition captures an ImageView's matrix before and after the
 * scene change and animates it during the transition.
 *
 * <p>In combination with ChangeBounds, ChangeImageTransform allows ImageViews
 * that change size, shape, or {@link android.widget.ImageView.ScaleType} to animate contents
 * smoothly.</p>
 */
public class ChangeImageTransform extends android.transition.Transition {
    private static final java.lang.String TAG = "ChangeImageTransform";

    private static final java.lang.String PROPNAME_MATRIX = "android:changeImageTransform:matrix";

    private static final java.lang.String PROPNAME_BOUNDS = "android:changeImageTransform:bounds";

    private static final java.lang.String[] sTransitionProperties = new java.lang.String[]{ android.transition.ChangeImageTransform.PROPNAME_MATRIX, android.transition.ChangeImageTransform.PROPNAME_BOUNDS };

    private static android.animation.TypeEvaluator<android.graphics.Matrix> NULL_MATRIX_EVALUATOR = new android.animation.TypeEvaluator<android.graphics.Matrix>() {
        @java.lang.Override
        public android.graphics.Matrix evaluate(float fraction, android.graphics.Matrix startValue, android.graphics.Matrix endValue) {
            return null;
        }
    };

    private static android.util.Property<android.widget.ImageView, android.graphics.Matrix> ANIMATED_TRANSFORM_PROPERTY = new android.util.Property<android.widget.ImageView, android.graphics.Matrix>(android.graphics.Matrix.class, "animatedTransform") {
        @java.lang.Override
        public void set(android.widget.ImageView object, android.graphics.Matrix value) {
            object.animateTransform(value);
        }

        @java.lang.Override
        public android.graphics.Matrix get(android.widget.ImageView object) {
            return null;
        }
    };

    public ChangeImageTransform() {
    }

    public ChangeImageTransform(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(android.transition.TransitionValues transitionValues) {
        android.view.View view = transitionValues.view;
        if ((!(view instanceof android.widget.ImageView)) || (view.getVisibility() != android.view.View.VISIBLE)) {
            return;
        }
        android.widget.ImageView imageView = ((android.widget.ImageView) (view));
        android.graphics.drawable.Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            return;
        }
        java.util.Map<java.lang.String, java.lang.Object> values = transitionValues.values;
        int left = view.getLeft();
        int top = view.getTop();
        int right = view.getRight();
        int bottom = view.getBottom();
        android.graphics.Rect bounds = new android.graphics.Rect(left, top, right, bottom);
        values.put(android.transition.ChangeImageTransform.PROPNAME_BOUNDS, bounds);
        android.graphics.Matrix matrix;
        android.widget.ImageView.ScaleType scaleType = imageView.getScaleType();
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        if (((scaleType == android.widget.ImageView.ScaleType.FIT_XY) && (drawableWidth > 0)) && (drawableHeight > 0)) {
            float scaleX = ((float) (bounds.width())) / drawableWidth;
            float scaleY = ((float) (bounds.height())) / drawableHeight;
            matrix = new android.graphics.Matrix();
            matrix.setScale(scaleX, scaleY);
        } else {
            matrix = new android.graphics.Matrix(imageView.getImageMatrix());
        }
        values.put(android.transition.ChangeImageTransform.PROPNAME_MATRIX, matrix);
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
        return android.transition.ChangeImageTransform.sTransitionProperties;
    }

    /**
     * Creates an Animator for ImageViews moving, changing dimensions, and/or changing
     * {@link android.widget.ImageView.ScaleType}.
     *
     * @param sceneRoot
     * 		The root of the transition hierarchy.
     * @param startValues
     * 		The values for a specific target in the start scene.
     * @param endValues
     * 		The values for the target in the end scene.
     * @return An Animator to move an ImageView or null if the View is not an ImageView,
    the Drawable changed, the View is not VISIBLE, or there was no change.
     */
    @java.lang.Override
    public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if ((startValues == null) || (endValues == null)) {
            return null;
        }
        android.graphics.Rect startBounds = ((android.graphics.Rect) (startValues.values.get(android.transition.ChangeImageTransform.PROPNAME_BOUNDS)));
        android.graphics.Rect endBounds = ((android.graphics.Rect) (endValues.values.get(android.transition.ChangeImageTransform.PROPNAME_BOUNDS)));
        android.graphics.Matrix startMatrix = ((android.graphics.Matrix) (startValues.values.get(android.transition.ChangeImageTransform.PROPNAME_MATRIX)));
        android.graphics.Matrix endMatrix = ((android.graphics.Matrix) (endValues.values.get(android.transition.ChangeImageTransform.PROPNAME_MATRIX)));
        if ((((startBounds == null) || (endBounds == null)) || (startMatrix == null)) || (endMatrix == null)) {
            return null;
        }
        if (startBounds.equals(endBounds) && startMatrix.equals(endMatrix)) {
            return null;
        }
        android.widget.ImageView imageView = ((android.widget.ImageView) (endValues.view));
        android.graphics.drawable.Drawable drawable = imageView.getDrawable();
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        android.animation.ObjectAnimator animator;
        if ((drawableWidth <= 0) || (drawableHeight <= 0)) {
            animator = createNullAnimator(imageView);
        } else {
            android.transition.ChangeImageTransform.ANIMATED_TRANSFORM_PROPERTY.set(imageView, startMatrix);
            animator = createMatrixAnimator(imageView, startMatrix, endMatrix);
        }
        return animator;
    }

    private android.animation.ObjectAnimator createNullAnimator(android.widget.ImageView imageView) {
        return android.animation.ObjectAnimator.ofObject(imageView, android.transition.ChangeImageTransform.ANIMATED_TRANSFORM_PROPERTY, android.transition.ChangeImageTransform.NULL_MATRIX_EVALUATOR, android.graphics.Matrix.IDENTITY_MATRIX, android.graphics.Matrix.IDENTITY_MATRIX);
    }

    private android.animation.ObjectAnimator createMatrixAnimator(final android.widget.ImageView imageView, android.graphics.Matrix startMatrix, final android.graphics.Matrix endMatrix) {
        return android.animation.ObjectAnimator.ofObject(imageView, android.transition.ChangeImageTransform.ANIMATED_TRANSFORM_PROPERTY, new android.transition.TransitionUtils.MatrixEvaluator(), startMatrix, endMatrix);
    }
}

