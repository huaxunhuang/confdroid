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
package android.view;


/**
 * This is a RenderThread driven backend for ViewPropertyAnimator.
 */
class ViewPropertyAnimatorRT {
    private static final android.view.animation.Interpolator sLinearInterpolator = new android.view.animation.LinearInterpolator();

    private final android.view.View mView;

    private android.view.RenderNodeAnimator[] mAnimators = new android.view.RenderNodeAnimator[android.view.RenderNodeAnimator.LAST_VALUE + 1];

    ViewPropertyAnimatorRT(android.view.View view) {
        mView = view;
    }

    /**
     *
     *
     * @return true if ViewPropertyAnimatorRT handled the animation,
    false if ViewPropertyAnimator needs to handle it
     */
    public boolean startAnimation(android.view.ViewPropertyAnimator parent) {
        cancelAnimators(parent.mPendingAnimations);
        if (!canHandleAnimator(parent)) {
            return false;
        }
        doStartAnimation(parent);
        return true;
    }

    public void cancelAll() {
        for (int i = 0; i < mAnimators.length; i++) {
            if (mAnimators[i] != null) {
                mAnimators[i].cancel();
                mAnimators[i] = null;
            }
        }
    }

    private void doStartAnimation(android.view.ViewPropertyAnimator parent) {
        int size = parent.mPendingAnimations.size();
        long startDelay = parent.getStartDelay();
        long duration = parent.getDuration();
        android.animation.TimeInterpolator interpolator = parent.getInterpolator();
        if (interpolator == null) {
            // Documented to be LinearInterpolator in ValueAnimator.setInterpolator
            interpolator = android.view.ViewPropertyAnimatorRT.sLinearInterpolator;
        }
        if (!android.view.RenderNodeAnimator.isNativeInterpolator(interpolator)) {
            interpolator = new com.android.internal.view.animation.FallbackLUTInterpolator(interpolator, duration);
        }
        for (int i = 0; i < size; i++) {
            android.view.ViewPropertyAnimator.NameValuesHolder holder = parent.mPendingAnimations.get(i);
            int property = android.view.RenderNodeAnimator.mapViewPropertyToRenderProperty(holder.mNameConstant);
            final float finalValue = holder.mFromValue + holder.mDeltaValue;
            android.view.RenderNodeAnimator animator = new android.view.RenderNodeAnimator(property, finalValue);
            animator.setStartDelay(startDelay);
            animator.setDuration(duration);
            animator.setInterpolator(interpolator);
            animator.setTarget(mView);
            animator.start();
            mAnimators[property] = animator;
        }
        parent.mPendingAnimations.clear();
    }

    private boolean canHandleAnimator(android.view.ViewPropertyAnimator parent) {
        // TODO: Can we eliminate this entirely?
        // If RenderNode.animatorProperties() can be toggled to point at staging
        // instead then RNA can be used as the animators for software as well
        // as the updateListener fallback paths. If this can be toggled
        // at the top level somehow, combined with requiresUiRedraw, we could
        // ensure that RT does not self-animate, allowing for safe driving of
        // the animators from the UI thread using the same mechanisms
        // ViewPropertyAnimator does, just with everything sitting on a single
        // animator subsystem instead of multiple.
        if (parent.getUpdateListener() != null) {
            return false;
        }
        if (parent.getListener() != null) {
            // TODO support
            return false;
        }
        if (!mView.isHardwareAccelerated()) {
            // TODO handle this maybe?
            return false;
        }
        if (parent.hasActions()) {
            return false;
        }
        // Here goes nothing...
        return true;
    }

    private void cancelAnimators(java.util.ArrayList<android.view.ViewPropertyAnimator.NameValuesHolder> mPendingAnimations) {
        int size = mPendingAnimations.size();
        for (int i = 0; i < size; i++) {
            android.view.ViewPropertyAnimator.NameValuesHolder holder = mPendingAnimations.get(i);
            int property = android.view.RenderNodeAnimator.mapViewPropertyToRenderProperty(holder.mNameConstant);
            if (mAnimators[property] != null) {
                mAnimators[property].cancel();
                mAnimators[property] = null;
            }
        }
    }
}

