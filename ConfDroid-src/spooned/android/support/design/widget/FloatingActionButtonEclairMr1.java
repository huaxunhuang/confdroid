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


class FloatingActionButtonEclairMr1 extends android.support.design.widget.FloatingActionButtonImpl {
    private int mAnimationDuration;

    private android.support.design.widget.StateListAnimator mStateListAnimator;

    private boolean mIsHiding;

    android.support.design.widget.ShadowDrawableWrapper mShadowDrawable;

    FloatingActionButtonEclairMr1(android.support.design.widget.VisibilityAwareImageButton view, android.support.design.widget.ShadowViewDelegate shadowViewDelegate) {
        super(view, shadowViewDelegate);
        mAnimationDuration = view.getResources().getInteger(android.R.integer.config_shortAnimTime);
        mStateListAnimator = new android.support.design.widget.StateListAnimator();
        mStateListAnimator.setTarget(view);
        // Elevate with translationZ when pressed or focused
        mStateListAnimator.addState(android.support.design.widget.FloatingActionButtonImpl.PRESSED_ENABLED_STATE_SET, setupAnimation(new android.support.design.widget.FloatingActionButtonEclairMr1.ElevateToTranslationZAnimation()));
        mStateListAnimator.addState(android.support.design.widget.FloatingActionButtonImpl.FOCUSED_ENABLED_STATE_SET, setupAnimation(new android.support.design.widget.FloatingActionButtonEclairMr1.ElevateToTranslationZAnimation()));
        // Reset back to elevation by default
        mStateListAnimator.addState(android.support.design.widget.FloatingActionButtonImpl.EMPTY_STATE_SET, setupAnimation(new android.support.design.widget.FloatingActionButtonEclairMr1.ResetElevationAnimation()));
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
        android.support.v4.graphics.drawable.DrawableCompat.setTintList(mRippleDrawable, android.support.design.widget.FloatingActionButtonEclairMr1.createColorStateList(rippleColor));
        final android.graphics.drawable.Drawable[] layers;
        if (borderWidth > 0) {
            mBorderDrawable = createBorderDrawable(borderWidth, backgroundTint);
            layers = new android.graphics.drawable.Drawable[]{ mBorderDrawable, mShapeDrawable, mRippleDrawable };
        } else {
            mBorderDrawable = null;
            layers = new android.graphics.drawable.Drawable[]{ mShapeDrawable, mRippleDrawable };
        }
        mContentBackground = new android.graphics.drawable.LayerDrawable(layers);
        mShadowDrawable = new android.support.design.widget.ShadowDrawableWrapper(mView.getResources(), mContentBackground, mShadowViewDelegate.getRadius(), mElevation, mElevation + mPressedTranslationZ);
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
            android.support.v4.graphics.drawable.DrawableCompat.setTintList(mRippleDrawable, android.support.design.widget.FloatingActionButtonEclairMr1.createColorStateList(rippleColor));
        }
    }

    @java.lang.Override
    float getElevation() {
        return mElevation;
    }

    @java.lang.Override
    void onElevationChanged(float elevation) {
        if (mShadowDrawable != null) {
            mShadowDrawable.setShadowSize(elevation, elevation + mPressedTranslationZ);
            updatePadding();
        }
    }

    @java.lang.Override
    void onTranslationZChanged(float translationZ) {
        if (mShadowDrawable != null) {
            mShadowDrawable.setMaxShadowSize(mElevation + translationZ);
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
        if (mIsHiding || (mView.getVisibility() != android.view.View.VISIBLE)) {
            // A hide animation is in progress, or we're already hidden. Skip the call
            if (listener != null) {
                listener.onHidden();
            }
            return;
        }
        android.view.animation.Animation anim = android.view.animation.AnimationUtils.loadAnimation(mView.getContext(), R.anim.design_fab_out);
        anim.setInterpolator(android.support.design.widget.AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
        anim.setDuration(android.support.design.widget.FloatingActionButtonImpl.SHOW_HIDE_ANIM_DURATION);
        anim.setAnimationListener(new android.support.design.widget.AnimationUtils.AnimationListenerAdapter() {
            @java.lang.Override
            public void onAnimationStart(android.view.animation.Animation animation) {
                mIsHiding = true;
            }

            @java.lang.Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                mIsHiding = false;
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
        if ((mView.getVisibility() != android.view.View.VISIBLE) || mIsHiding) {
            // If the view is not visible, or is visible and currently being hidden, run
            // the show animation
            mView.clearAnimation();
            mView.internalSetVisibility(android.view.View.VISIBLE, fromUser);
            android.view.animation.Animation anim = android.view.animation.AnimationUtils.loadAnimation(mView.getContext(), R.anim.design_fab_in);
            anim.setDuration(android.support.design.widget.FloatingActionButtonImpl.SHOW_HIDE_ANIM_DURATION);
            anim.setInterpolator(android.support.design.widget.AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
            anim.setAnimationListener(new android.support.design.widget.AnimationUtils.AnimationListenerAdapter() {
                @java.lang.Override
                public void onAnimationEnd(android.view.animation.Animation animation) {
                    if (listener != null) {
                        listener.onShown();
                    }
                }
            });
            mView.startAnimation(anim);
        } else {
            if (listener != null) {
                listener.onShown();
            }
        }
    }

    @java.lang.Override
    void onCompatShadowChanged() {
        // Ignore pre-v21
    }

    void getPadding(android.graphics.Rect rect) {
        mShadowDrawable.getPadding(rect);
    }

    private android.view.animation.Animation setupAnimation(android.view.animation.Animation animation) {
        animation.setInterpolator(android.support.design.widget.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animation.setDuration(mAnimationDuration);
        return animation;
    }

    private abstract class BaseShadowAnimation extends android.view.animation.Animation {
        private float mShadowSizeStart;

        private float mShadowSizeDiff;

        @java.lang.Override
        public void reset() {
            super.reset();
            mShadowSizeStart = mShadowDrawable.getShadowSize();
            mShadowSizeDiff = getTargetShadowSize() - mShadowSizeStart;
        }

        @java.lang.Override
        protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
            mShadowDrawable.setShadowSize(mShadowSizeStart + (mShadowSizeDiff * interpolatedTime));
        }

        /**
         *
         *
         * @return the shadow size we want to animate to.
         */
        protected abstract float getTargetShadowSize();
    }

    private class ResetElevationAnimation extends android.support.design.widget.FloatingActionButtonEclairMr1.BaseShadowAnimation {
        @java.lang.Override
        protected float getTargetShadowSize() {
            return mElevation;
        }
    }

    private class ElevateToTranslationZAnimation extends android.support.design.widget.FloatingActionButtonEclairMr1.BaseShadowAnimation {
        @java.lang.Override
        protected float getTargetShadowSize() {
            return mElevation + mPressedTranslationZ;
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

