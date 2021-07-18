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
 * A <code>TransitionPropagation</code> that propagates based on the distance to the side
 * and, orthogonally, the distance to epicenter. If the transitioning View is visible in
 * the start of the transition, then it will transition sooner when closer to the side and
 * later when farther. If the view is not visible in the start of the transition, then
 * it will transition later when closer to the side and sooner when farther from the edge.
 * This is the default TransitionPropagation used with {@link android.transition.Slide}.
 */
public class SidePropagation extends android.transition.VisibilityPropagation {
    private static final java.lang.String TAG = "SlidePropagation";

    private float mPropagationSpeed = 3.0F;

    private int mSide = android.view.Gravity.BOTTOM;

    /**
     * Sets the side that is used to calculate the transition propagation. If the transitioning
     * View is visible in the start of the transition, then it will transition sooner when
     * closer to the side and later when farther. If the view is not visible in the start of
     * the transition, then it will transition later when closer to the side and sooner when
     * farther from the edge. The default is {@link Gravity#BOTTOM}.
     *
     * @param side
     * 		The side that is used to calculate the transition propagation. Must be one of
     * 		{@link Gravity#LEFT}, {@link Gravity#TOP}, {@link Gravity#RIGHT},
     * 		{@link Gravity#BOTTOM}, {@link Gravity#START}, or {@link Gravity#END}.
     */
    public void setSide(@android.transition.Slide.GravityFlag
    int side) {
        mSide = side;
    }

    /**
     * Sets the speed at which transition propagation happens, relative to the duration of the
     * Transition. A <code>propagationSpeed</code> of 1 means that a View centered at the side
     * set in {@link #setSide(int)} and View centered at the opposite edge will have a difference
     * in start delay of approximately the duration of the Transition. A speed of 2 means the
     * start delay difference will be approximately half of the duration of the transition. A
     * value of 0 is illegal, but negative values will invert the propagation.
     *
     * @param propagationSpeed
     * 		The speed at which propagation occurs, relative to the duration
     * 		of the transition. A speed of 4 means it works 4 times as fast
     * 		as the duration of the transition. May not be 0.
     */
    public void setPropagationSpeed(float propagationSpeed) {
        if (propagationSpeed == 0) {
            throw new java.lang.IllegalArgumentException("propagationSpeed may not be 0");
        }
        mPropagationSpeed = propagationSpeed;
    }

    @java.lang.Override
    public long getStartDelay(android.view.ViewGroup sceneRoot, android.transition.Transition transition, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if ((startValues == null) && (endValues == null)) {
            return 0;
        }
        int directionMultiplier = 1;
        android.graphics.Rect epicenter = transition.getEpicenter();
        android.transition.TransitionValues positionValues;
        if ((endValues == null) || (getViewVisibility(startValues) == android.view.View.VISIBLE)) {
            positionValues = startValues;
            directionMultiplier = -1;
        } else {
            positionValues = endValues;
        }
        int viewCenterX = getViewX(positionValues);
        int viewCenterY = getViewY(positionValues);
        int[] loc = new int[2];
        sceneRoot.getLocationOnScreen(loc);
        int left = loc[0] + java.lang.Math.round(sceneRoot.getTranslationX());
        int top = loc[1] + java.lang.Math.round(sceneRoot.getTranslationY());
        int right = left + sceneRoot.getWidth();
        int bottom = top + sceneRoot.getHeight();
        int epicenterX;
        int epicenterY;
        if (epicenter != null) {
            epicenterX = epicenter.centerX();
            epicenterY = epicenter.centerY();
        } else {
            epicenterX = (left + right) / 2;
            epicenterY = (top + bottom) / 2;
        }
        float distance = distance(sceneRoot, viewCenterX, viewCenterY, epicenterX, epicenterY, left, top, right, bottom);
        float maxDistance = getMaxDistance(sceneRoot);
        float distanceFraction = distance / maxDistance;
        long duration = transition.getDuration();
        if (duration < 0) {
            duration = 300;
        }
        return java.lang.Math.round(((duration * directionMultiplier) / mPropagationSpeed) * distanceFraction);
    }

    private int distance(android.view.View sceneRoot, int viewX, int viewY, int epicenterX, int epicenterY, int left, int top, int right, int bottom) {
        final int side;
        if (mSide == android.view.Gravity.START) {
            final boolean isRtl = sceneRoot.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL;
            side = (isRtl) ? android.view.Gravity.RIGHT : android.view.Gravity.LEFT;
        } else
            if (mSide == android.view.Gravity.END) {
                final boolean isRtl = sceneRoot.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL;
                side = (isRtl) ? android.view.Gravity.LEFT : android.view.Gravity.RIGHT;
            } else {
                side = mSide;
            }

        int distance = 0;
        switch (side) {
            case android.view.Gravity.LEFT :
                distance = (right - viewX) + java.lang.Math.abs(epicenterY - viewY);
                break;
            case android.view.Gravity.TOP :
                distance = (bottom - viewY) + java.lang.Math.abs(epicenterX - viewX);
                break;
            case android.view.Gravity.RIGHT :
                distance = (viewX - left) + java.lang.Math.abs(epicenterY - viewY);
                break;
            case android.view.Gravity.BOTTOM :
                distance = (viewY - top) + java.lang.Math.abs(epicenterX - viewX);
                break;
        }
        return distance;
    }

    private int getMaxDistance(android.view.ViewGroup sceneRoot) {
        switch (mSide) {
            case android.view.Gravity.LEFT :
            case android.view.Gravity.RIGHT :
            case android.view.Gravity.START :
            case android.view.Gravity.END :
                return sceneRoot.getWidth();
            default :
                return sceneRoot.getHeight();
        }
    }
}

