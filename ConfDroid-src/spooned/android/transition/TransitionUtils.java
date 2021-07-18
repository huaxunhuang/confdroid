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
 * Static utility methods for Transitions.
 *
 * @unknown 
 */
public class TransitionUtils {
    private static int MAX_IMAGE_SIZE = 1024 * 1024;

    static android.animation.Animator mergeAnimators(android.animation.Animator animator1, android.animation.Animator animator2) {
        if (animator1 == null) {
            return animator2;
        } else
            if (animator2 == null) {
                return animator1;
            } else {
                android.animation.AnimatorSet animatorSet = new android.animation.AnimatorSet();
                animatorSet.playTogether(animator1, animator2);
                return animatorSet;
            }

    }

    public static android.transition.Transition mergeTransitions(android.transition.Transition... transitions) {
        int count = 0;
        int nonNullIndex = -1;
        for (int i = 0; i < transitions.length; i++) {
            if (transitions[i] != null) {
                count++;
                nonNullIndex = i;
            }
        }
        if (count == 0) {
            return null;
        }
        if (count == 1) {
            return transitions[nonNullIndex];
        }
        android.transition.TransitionSet transitionSet = new android.transition.TransitionSet();
        for (int i = 0; i < transitions.length; i++) {
            if (transitions[i] != null) {
                transitionSet.addTransition(transitions[i]);
            }
        }
        return transitionSet;
    }

    /**
     * Creates a View using the bitmap copy of <code>view</code>. If <code>view</code> is large,
     * the copy will use a scaled bitmap of the given view.
     *
     * @param sceneRoot
     * 		The ViewGroup in which the view copy will be displayed.
     * @param view
     * 		The view to create a copy of.
     * @param parent
     * 		The parent of view.
     */
    public static android.view.View copyViewImage(android.view.ViewGroup sceneRoot, android.view.View view, android.view.View parent) {
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setTranslate(-parent.getScrollX(), -parent.getScrollY());
        view.transformMatrixToGlobal(matrix);
        sceneRoot.transformMatrixToLocal(matrix);
        android.graphics.RectF bounds = new android.graphics.RectF(0, 0, view.getWidth(), view.getHeight());
        matrix.mapRect(bounds);
        int left = java.lang.Math.round(bounds.left);
        int top = java.lang.Math.round(bounds.top);
        int right = java.lang.Math.round(bounds.right);
        int bottom = java.lang.Math.round(bounds.bottom);
        android.widget.ImageView copy = new android.widget.ImageView(view.getContext());
        copy.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        android.graphics.Bitmap bitmap = android.transition.TransitionUtils.createViewBitmap(view, matrix, bounds, sceneRoot);
        if (bitmap != null) {
            copy.setImageBitmap(bitmap);
        }
        int widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(right - left, android.view.View.MeasureSpec.EXACTLY);
        int heightSpec = android.view.View.MeasureSpec.makeMeasureSpec(bottom - top, android.view.View.MeasureSpec.EXACTLY);
        copy.measure(widthSpec, heightSpec);
        copy.layout(left, top, right, bottom);
        return copy;
    }

    /**
     * Get a copy of bitmap of given drawable, return null if intrinsic size is zero
     */
    public static android.graphics.Bitmap createDrawableBitmap(android.graphics.drawable.Drawable drawable, android.view.View hostView) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if ((width <= 0) || (height <= 0)) {
            return null;
        }
        float scale = java.lang.Math.min(1.0F, ((float) (android.transition.TransitionUtils.MAX_IMAGE_SIZE)) / (width * height));
        if ((drawable instanceof android.graphics.drawable.BitmapDrawable) && (scale == 1.0F)) {
            // return same bitmap if scale down not needed
            return ((android.graphics.drawable.BitmapDrawable) (drawable)).getBitmap();
        }
        int bitmapWidth = ((int) (width * scale));
        int bitmapHeight = ((int) (height * scale));
        final android.graphics.Picture picture = new android.graphics.Picture();
        final android.graphics.Canvas canvas = picture.beginRecording(width, height);
        // Do stuff with the canvas
        android.graphics.Rect existingBounds = drawable.getBounds();
        int left = existingBounds.left;
        int top = existingBounds.top;
        int right = existingBounds.right;
        int bottom = existingBounds.bottom;
        drawable.setBounds(0, 0, bitmapWidth, bitmapHeight);
        drawable.draw(canvas);
        drawable.setBounds(left, top, right, bottom);
        picture.endRecording();
        return android.graphics.Bitmap.createBitmap(picture);
    }

    /**
     * Creates a Bitmap of the given view, using the Matrix matrix to transform to the local
     * coordinates. <code>matrix</code> will be modified during the bitmap creation.
     *
     * <p>If the bitmap is large, it will be scaled uniformly down to at most 1MB size.</p>
     *
     * @param view
     * 		The view to create a bitmap for.
     * @param matrix
     * 		The matrix converting the view local coordinates to the coordinates that
     * 		the bitmap will be displayed in. <code>matrix</code> will be modified before
     * 		returning.
     * @param bounds
     * 		The bounds of the bitmap in the destination coordinate system (where the
     * 		view should be presented. Typically, this is matrix.mapRect(viewBounds);
     * @param sceneRoot
     * 		A ViewGroup that is attached to the window to temporarily contain the view
     * 		if it isn't attached to the window.
     * @return A bitmap of the given view or null if bounds has no width or height.
     */
    public static android.graphics.Bitmap createViewBitmap(android.view.View view, android.graphics.Matrix matrix, android.graphics.RectF bounds, android.view.ViewGroup sceneRoot) {
        final boolean addToOverlay = !view.isAttachedToWindow();
        android.view.ViewGroup parent = null;
        int indexInParent = 0;
        if (addToOverlay) {
            if ((sceneRoot == null) || (!sceneRoot.isAttachedToWindow())) {
                return null;
            }
            parent = ((android.view.ViewGroup) (view.getParent()));
            indexInParent = parent.indexOfChild(view);
            sceneRoot.getOverlay().add(view);
        }
        android.graphics.Bitmap bitmap = null;
        int bitmapWidth = java.lang.Math.round(bounds.width());
        int bitmapHeight = java.lang.Math.round(bounds.height());
        if ((bitmapWidth > 0) && (bitmapHeight > 0)) {
            float scale = java.lang.Math.min(1.0F, ((float) (android.transition.TransitionUtils.MAX_IMAGE_SIZE)) / (bitmapWidth * bitmapHeight));
            bitmapWidth *= scale;
            bitmapHeight *= scale;
            matrix.postTranslate(-bounds.left, -bounds.top);
            matrix.postScale(scale, scale);
            final android.graphics.Picture picture = new android.graphics.Picture();
            final android.graphics.Canvas canvas = picture.beginRecording(bitmapWidth, bitmapHeight);
            canvas.concat(matrix);
            view.draw(canvas);
            picture.endRecording();
            bitmap = android.graphics.Bitmap.createBitmap(picture);
        }
        if (addToOverlay) {
            sceneRoot.getOverlay().remove(view);
            parent.addView(view, indexInParent);
        }
        return bitmap;
    }

    public static class MatrixEvaluator implements android.animation.TypeEvaluator<android.graphics.Matrix> {
        float[] mTempStartValues = new float[9];

        float[] mTempEndValues = new float[9];

        android.graphics.Matrix mTempMatrix = new android.graphics.Matrix();

        @java.lang.Override
        public android.graphics.Matrix evaluate(float fraction, android.graphics.Matrix startValue, android.graphics.Matrix endValue) {
            startValue.getValues(mTempStartValues);
            endValue.getValues(mTempEndValues);
            for (int i = 0; i < 9; i++) {
                float diff = mTempEndValues[i] - mTempStartValues[i];
                mTempEndValues[i] = mTempStartValues[i] + (fraction * diff);
            }
            mTempMatrix.setValues(mTempEndValues);
            return mTempMatrix;
        }
    }
}

