/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.design.widget;


class FloatingActionButtonGingerbread extends android.support.design.widget.FloatingActionButtonImpl {
    private final android.support.design.widget.StateListAnimator mStateListAnimator;

    android.support.design.widget.ShadowDrawableWrapper mShadowDrawable;

    FloatingActionButtonGingerbread(android.support.design.widget.VisibilityAwareImageButton view, android.support.design.widget.ShadowViewDelegate shadowViewDelegate, android.support.design.widget.ValueAnimatorCompat.Creator animatorCreator) {
        super(view, shadowViewDelegate, animatorCreator);
        mStateListAnimator = new android.support.design.widget.StateListAnimator();
        // Elevate with translationZ when pressed or focused
        mStateListAnimator.addState(android.support.design.widget.FloatingActionButtonImpl.PRESSED_ENABLED_STATE_SET, createAnimator(new android.support.design.widget.FloatingActionButtonGingerbread.ElevateToTranslationZAnimation()));
        mStateListAnimator.addState(android.support.design.widget.FloatingActionButtonImpl.FOCUSED_ENABLED_STATE_SET, createAnimator(new android.support.design.widget.FloatingActionButtonGingerbread.ElevateToTranslationZAnimation()));
        // Reset back to elevation by default
        mStateListAnimator.addState(android.support.design.widget.FloatingActionButtonImpl.ENABLED_STATE_SET, createAnimator(new android.support.design.widget.FloatingActionButtonGingerbread.ResetElevationAnimation()));
        // Set to 0 when disabled
        mStateListAnimator.addState(android.support.design.widget.FloatingActionButtonImpl.EMPTY_STATE_SET, createAnimator(new android.support.design.widget.FloatingActionButtonGingerbread.DisabledElevationAnimation()));
    }

    @java.lang.Override
    void setBackgroundDrawable(android.content.res.ColorStateList backgroundTint, android.graphics.PorterDuff.Mode backgroundTintMode, int rippleColor, int borderWidth) {
        // Now we need to tint the original background with the tint, using
        // an InsetDrawable if we have a border width
        mShapeDrawable = android.support.v4.graphics.drawable.DrawableCompat.wrap(createShapeDrawable());
        android.support.v4.graphics.drawable.DrawableCompat.setTintList(mShapeDrawable, backgroundTint);
        if (backgroundTintMode != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setTintMode(mShapeDrawable, backgroundTintMode);
        }
        // Now we created a mask Drawable which will be used for touch feedback.
        android.graphics.drawable.GradientDrawable touchFeedbackShape = createShapeDrawable();
        // We'll now wrap that touch feedback mask drawable with a ColorStateList. We do not need
        // to inset for any border here as LayerDrawable will nest the padding for us
        mRippleDrawable = android.support.v4.graphics.drawable.DrawableCompat.wrap(touchFeedbackShape);
        android.support.v4.graphics.drawable.DrawableCompat.setTintList(mRippleDrawable, android.support.design.widget.FloatingActionButtonGingerbread.createColorStateList(rippleColor));
        final android.graphics.drawable.Drawable[] layers;
        if (borderWidth > 0) {
            mBorderDrawable = createBorderDrawable(borderWidth, backgroundTint);
            layers = new android.graphics.drawable.Drawable[]{ mBorderDrawable, mShapeDrawable, mRippleDrawable };
        } else {
            mBorderDrawable = null;
            layers = new android.graphics.drawable.Drawable[]{ mShapeDrawable, mRippleDrawable };
        }
        mContentBackground = new android.graphics.drawable.LayerDrawable(layers);
        mShadowDrawable = new android.support.design.widget.ShadowDrawableWrapper(mView.getContext(), mContentBackground, mShadowViewDelegate.getRadius(), mElevation, mElevation + mPressedTranslationZ);
        mShadowDrawable.setAddPaddingForCorners(false);
        mShadowViewDelegate.setBackgroundDrawable(mShadowDrawable);
    }

    @java.lang.Override
    void setBackgroundTintList(android.content.res.ColorStateList tint) {
        if (mShapeDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setTintList(mShapeDrawable, tint);
        }
        if (mBorderDrawable != null) {
            mBorderDrawable.setBorderTint(tint);
        }
    }

    @java.lang.Override
    void setBackgroundTintMode(android.graphics.PorterDuff.Mode tintMode) {
        if (mShapeDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setTintMode(mShapeDrawable, tintMode);
        }
    }

    @java.lang.Override
    void setRippleColor(int rippleColor) {
        if (mRippleDrawable != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setTintList(mRippleDrawable, android.support.design.widget.FloatingActionButtonGingerbread.createColorStateList(rippleColor));
        }
    }

    @java.lang.Override
    float getElevation() {
        return mElevation;
    }

    @java.lang.Override
    void onElevationsChanged(float elevation, float pressedTranslationZ) {
        if (mShadowDrawable != null) {
            mShadowDrawable.setShadowSize(elevation, elevation + mPressedTranslationZ);
            updatePadding();
        }
    }

    @java.lang.Override
    void onDrawableStateChanged(int[] state) {
        mStateListAnimator.setState(state);
    }

    @java.lang.Override
    void jumpDrawableToCurrentState() {
        mStateListAnimator.jumpToCurrentState();
    }

    @java.lang.Override
    void hide(@android.support.annotation.Nullable
    final android.support.design.widget.FloatingActionButtonImpl.InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (isOrWillBeHidden()) {
            // We either are or will soon be hidden, skip the call
            return;
        }
        mAnimState = android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_HIDING;
        android.view.animation.Animation anim = android.view.animation.AnimationUtils.loadAnimation(mView.getContext(), R.anim.design_fab_out);
        anim.setInterpolator(android.support.design.widget.AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
        anim.setDuration(android.support.design.widget.FloatingActionButtonImpl.SHOW_HIDE_ANIM_DURATION);
        anim.setAnimationListener(new android.support.design.widget.AnimationUtils.AnimationListenerAdapter() {
            @java.lang.Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                mAnimState = android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_NONE;
                mView.internalSetVisibility(android.view.View.GONE, fromUser);
                if (listener != null) {
                    listener.onHidden();
                }
            }
        });
        mView.startAnimation(anim);
    }

    @java.lang.Override
    void show(@android.support.annotation.Nullable
    final android.support.design.widget.FloatingActionButtonImpl.InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (isOrWillBeShown()) {
            // We either are or will soon be visible, skip the call
            return;
        }
        mAnimState = android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_SHOWING;
        mView.internalSetVisibility(android.view.View.VISIBLE, fromUser);
        android.view.animation.Animation anim = android.view.animation.AnimationUtils.loadAnimation(mView.getContext(), R.anim.design_fab_in);
        anim.setDuration(android.support.design.widget.FloatingActionButtonImpl.SHOW_HIDE_ANIM_DURATION);
        anim.setInterpolator(android.support.design.widget.AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        anim.setAnimationListener(new android.support.design.widget.AnimationUtils.AnimationListenerAdapter() {
            @java.lang.Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                mAnimState = android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_NONE;
                if (listener != null) {
                    listener.onShown();
                }
            }
        });
        mView.startAnimation(anim);
    }

    @java.lang.Override
    void onCompatShadowChanged() {
        // Ignore pre-v21
    }

    @java.lang.Override
    void getPadding(android.graphics.Rect rect) {
        mShadowDrawable.getPadding(rect);
    }

    private android.support.design.widget.ValueAnimatorCompat createAnimator(@android.support.annotation.NonNull
    android.support.design.widget.FloatingActionButtonGingerbread.ShadowAnimatorImpl impl) {
        final android.support.design.widget.ValueAnimatorCompat animator = mAnimatorCreator.createAnimator();
        animator.setInterpolator(android.support.design.widget.FloatingActionButtonImpl.ANIM_INTERPOLATOR);
        animator.setDuration(android.support.design.widget.FloatingActionButtonImpl.PRESSED_ANIM_DURATION);
        animator.addListener(impl);
        animator.addUpdateListener(impl);
        animator.setFloatValues(0, 1);
        return animator;
    }

    private abstract class ShadowAnimatorImpl extends android.support.design.widget.ValueAnimatorCompat.AnimatorListenerAdapter implements android.support.design.widget.ValueAnimatorCompat.AnimatorUpdateListener {
        private boolean mValidValues;

        private float mShadowSizeStart;

        private float mShadowSizeEnd;

        @java.lang.Override
        public void onAnimationUpdate(android.support.design.widget.ValueAnimatorCompat animator) {
            if (!mValidValues) {
                mShadowSizeStart = mShadowDrawable.getShadowSize();
                mShadowSizeEnd = getTargetShadowSize();
                mValidValues = true;
            }
            mShadowDrawable.setShadowSize(mShadowSizeStart + ((mShadowSizeEnd - mShadowSizeStart) * animator.getAnimatedFraction()));
        }

        @java.lang.Override
        public void onAnimationEnd(android.support.design.widget.ValueAnimatorCompat animator) {
            mShadowDrawable.setShadowSize(mShadowSizeEnd);
            mValidValues = false;
        }

        /**
         *
         *
         * @return the shadow size we want to animate to.
         */
        protected abstract float getTargetShadowSize();
    }

    private class ResetElevationAnimation extends android.support.design.widget.FloatingActionButtonGingerbread.ShadowAnimatorImpl {
        ResetElevationAnimation() {
        }

        @java.lang.Override
        protected float getTargetShadowSize() {
            return mElevation;
        }
    }

    private class ElevateToTranslationZAnimation extends android.support.design.widget.FloatingActionButtonGingerbread.ShadowAnimatorImpl {
        ElevateToTranslationZAnimation() {
        }

        @java.lang.Override
        protected float getTargetShadowSize() {
            return mElevation + mPressedTranslationZ;
        }
    }

    private class DisabledElevationAnimation extends android.support.design.widget.FloatingActionButtonGingerbread.ShadowAnimatorImpl {
        DisabledElevationAnimation() {
        }

        @java.lang.Override
        protected float getTargetShadowSize() {
            return 0.0F;
        }
    }

    private static android.content.res.ColorStateList createColorStateList(int selectedColor) {
        final int[][] states = new int[3][];
        final int[] colors = new int[3];
        int i = 0;
        states[i] = android.support.design.widget.FloatingActionButtonImpl.FOCUSED_ENABLED_STATE_SET;
        colors[i] = selectedColor;
        i++;
        states[i] = android.support.design.widget.FloatingActionButtonImpl.PRESSED_ENABLED_STATE_SET;
        colors[i] = selectedColor;
        i++;
        // Default enabled state
        states[i] = new int[0];
        colors[i] = android.graphics.Color.TRANSPARENT;
        i++;
        return new android.content.res.ColorStateList(states, colors);
    }
}

