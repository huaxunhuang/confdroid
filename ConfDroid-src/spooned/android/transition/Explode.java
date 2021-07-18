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
 * This transition tracks changes to the visibility of target views in the
 * start and end scenes and moves views in or out from the edges of the
 * scene. Visibility is determined by both the
 * {@link View#setVisibility(int)} state of the view as well as whether it
 * is parented in the current view hierarchy. Disappearing Views are
 * limited as described in {@link Visibility#onDisappear(android.view.ViewGroup,
 * TransitionValues, int, TransitionValues, int)}.
 * <p>Views move away from the focal View or the center of the Scene if
 * no epicenter was provided.</p>
 */
public class Explode extends android.transition.Visibility {
    private static final android.animation.TimeInterpolator sDecelerate = new android.view.animation.DecelerateInterpolator();

    private static final android.animation.TimeInterpolator sAccelerate = new android.view.animation.AccelerateInterpolator();

    private static final java.lang.String TAG = "Explode";

    private static final java.lang.String PROPNAME_SCREEN_BOUNDS = "android:explode:screenBounds";

    private int[] mTempLoc = new int[2];

    public Explode() {
        setPropagation(new android.transition.CircularPropagation());
    }

    public Explode(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        setPropagation(new android.transition.CircularPropagation());
    }

    private void captureValues(android.transition.TransitionValues transitionValues) {
        android.view.View view = transitionValues.view;
        view.getLocationOnScreen(mTempLoc);
        int left = mTempLoc[0];
        int top = mTempLoc[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();
        transitionValues.values.put(android.transition.Explode.PROPNAME_SCREEN_BOUNDS, new android.graphics.Rect(left, top, right, bottom));
    }

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    @java.lang.Override
    public void captureEndValues(android.transition.TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        captureValues(transitionValues);
    }

    @java.lang.Override
    public android.animation.Animator onAppear(android.view.ViewGroup sceneRoot, android.view.View view, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if (endValues == null) {
            return null;
        }
        android.graphics.Rect bounds = ((android.graphics.Rect) (endValues.values.get(android.transition.Explode.PROPNAME_SCREEN_BOUNDS)));
        float endX = view.getTranslationX();
        float endY = view.getTranslationY();
        calculateOut(sceneRoot, bounds, mTempLoc);
        float startX = endX + mTempLoc[0];
        float startY = endY + mTempLoc[1];
        return android.transition.TranslationAnimationCreator.createAnimation(view, endValues, bounds.left, bounds.top, startX, startY, endX, endY, android.transition.Explode.sDecelerate, this);
    }

    @java.lang.Override
    public android.animation.Animator onDisappear(android.view.ViewGroup sceneRoot, android.view.View view, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if (startValues == null) {
            return null;
        }
        android.graphics.Rect bounds = ((android.graphics.Rect) (startValues.values.get(android.transition.Explode.PROPNAME_SCREEN_BOUNDS)));
        int viewPosX = bounds.left;
        int viewPosY = bounds.top;
        float startX = view.getTranslationX();
        float startY = view.getTranslationY();
        float endX = startX;
        float endY = startY;
        int[] interruptedPosition = ((int[]) (startValues.view.getTag(R.id.transitionPosition)));
        if (interruptedPosition != null) {
            // We want to have the end position relative to the interrupted position, not
            // the position it was supposed to start at.
            endX += interruptedPosition[0] - bounds.left;
            endY += interruptedPosition[1] - bounds.top;
            bounds.offsetTo(interruptedPosition[0], interruptedPosition[1]);
        }
        calculateOut(sceneRoot, bounds, mTempLoc);
        endX += mTempLoc[0];
        endY += mTempLoc[1];
        return android.transition.TranslationAnimationCreator.createAnimation(view, startValues, viewPosX, viewPosY, startX, startY, endX, endY, android.transition.Explode.sAccelerate, this);
    }

    private void calculateOut(android.view.View sceneRoot, android.graphics.Rect bounds, int[] outVector) {
        sceneRoot.getLocationOnScreen(mTempLoc);
        int sceneRootX = mTempLoc[0];
        int sceneRootY = mTempLoc[1];
        int focalX;
        int focalY;
        android.graphics.Rect epicenter = getEpicenter();
        if (epicenter == null) {
            focalX = (sceneRootX + (sceneRoot.getWidth() / 2)) + java.lang.Math.round(sceneRoot.getTranslationX());
            focalY = (sceneRootY + (sceneRoot.getHeight() / 2)) + java.lang.Math.round(sceneRoot.getTranslationY());
        } else {
            focalX = epicenter.centerX();
            focalY = epicenter.centerY();
        }
        int centerX = bounds.centerX();
        int centerY = bounds.centerY();
        double xVector = centerX - focalX;
        double yVector = centerY - focalY;
        if ((xVector == 0) && (yVector == 0)) {
            // Random direction when View is centered on focal View.
            xVector = (java.lang.Math.random() * 2) - 1;
            yVector = (java.lang.Math.random() * 2) - 1;
        }
        double vectorSize = java.lang.Math.hypot(xVector, yVector);
        xVector /= vectorSize;
        yVector /= vectorSize;
        double maxDistance = android.transition.Explode.calculateMaxDistance(sceneRoot, focalX - sceneRootX, focalY - sceneRootY);
        outVector[0] = ((int) (java.lang.Math.round(maxDistance * xVector)));
        outVector[1] = ((int) (java.lang.Math.round(maxDistance * yVector)));
    }

    private static double calculateMaxDistance(android.view.View sceneRoot, int focalX, int focalY) {
        int maxX = java.lang.Math.max(focalX, sceneRoot.getWidth() - focalX);
        int maxY = java.lang.Math.max(focalY, sceneRoot.getHeight() - focalY);
        return java.lang.Math.hypot(maxX, maxY);
    }
}

