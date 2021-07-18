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


@android.annotation.TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
class FloatingActionButtonLollipop extends android.support.design.widget.FloatingActionButtonIcs {
    private android.graphics.drawable.InsetDrawable mInsetDrawable;

    FloatingActionButtonLollipop(android.support.design.widget.VisibilityAwareImageButton view, android.support.design.widget.ShadowViewDelegate shadowViewDelegate, android.support.design.widget.ValueAnimatorCompat.Creator animatorCreator) {
        super(view, shadowViewDelegate, animatorCreator);
    }

    @java.lang.Override
    void setBackgroundDrawable(android.content.res.ColorStateList backgroundTint, android.graphics.PorterDuff.Mode backgroundTintMode, int rippleColor, int borderWidth) {
        // Now we need to tint the shape background with the tint
        mShapeDrawable = android.support.v4.graphics.drawable.DrawableCompat.wrap(createShapeDrawable());
        android.support.v4.graphics.drawable.DrawableCompat.setTintList(mShapeDrawable, backgroundTint);
        if (backgroundTintMode != null) {
            android.support.v4.graphics.drawable.DrawableCompat.setTintMode(mShapeDrawable, backgroundTintMode);
        }
        final android.graphics.drawable.Drawable rippleContent;
        if (borderWidth > 0) {
            mBorderDrawable = createBorderDrawable(borderWidth, backgroundTint);
            rippleContent = new android.graphics.drawable.LayerDrawable(new android.graphics.drawable.Drawable[]{ mBorderDrawable, mShapeDrawable });
        } else {
            mBorderDrawable = null;
            rippleContent = mShapeDrawable;
        }
        mRippleDrawable = new android.graphics.drawable.RippleDrawable(android.content.res.ColorStateList.valueOf(rippleColor), rippleContent, null);
        mContentBackground = mRippleDrawable;
        mShadowViewDelegate.setBackgroundDrawable(mRippleDrawable);
    }

    @java.lang.Override
    void setRippleColor(int rippleColor) {
        if (mRippleDrawable instanceof android.graphics.drawable.RippleDrawable) {
            ((android.graphics.drawable.RippleDrawable) (mRippleDrawable)).setColor(android.content.res.ColorStateList.valueOf(rippleColor));
        } else {
            super.setRippleColor(rippleColor);
        }
    }

    @java.lang.Override
    void onElevationsChanged(final float elevation, final float pressedTranslationZ) {
        final android.animation.StateListAnimator stateListAnimator = new android.animation.StateListAnimator();
        // Animate elevation and translationZ to our values when pressed
        android.animation.AnimatorSet set = new android.animation.AnimatorSet();
        set.play(android.animation.ObjectAnimator.ofFloat(mView, "elevation", elevation).setDuration(0)).with(android.animation.ObjectAnimator.ofFloat(mView, android.view.View.TRANSLATION_Z, pressedTranslationZ).setDuration(android.support.design.widget.FloatingActionButtonImpl.PRESSED_ANIM_DURATION));
        set.setInterpolator(android.support.design.widget.FloatingActionButtonImpl.ANIM_INTERPOLATOR);
        stateListAnimator.addState(android.support.design.widget.FloatingActionButtonImpl.PRESSED_ENABLED_STATE_SET, set);
        // Same deal for when we're focused
        set = new android.animation.AnimatorSet();
        set.play(android.animation.ObjectAnimator.ofFloat(mView, "elevation", elevation).setDuration(0)).with(android.animation.ObjectAnimator.ofFloat(mView, android.view.View.TRANSLATION_Z, pressedTranslationZ).setDuration(android.support.design.widget.FloatingActionButtonImpl.PRESSED_ANIM_DURATION));
        set.setInterpolator(android.support.design.widget.FloatingActionButtonImpl.ANIM_INTERPOLATOR);
        stateListAnimator.addState(android.support.design.widget.FloatingActionButtonImpl.FOCUSED_ENABLED_STATE_SET, set);
        // Animate translationZ to 0 if not pressed
        set = new android.animation.AnimatorSet();
        // Use an AnimatorSet to set a start delay since there is a bug with ValueAnimator that
        // prevents it from being cancelled properly when used with a StateListAnimator.
        android.animation.AnimatorSet anim = new android.animation.AnimatorSet();
        anim.play(android.animation.ObjectAnimator.ofFloat(mView, android.view.View.TRANSLATION_Z, 0.0F).setDuration(android.support.design.widget.FloatingActionButtonImpl.PRESSED_ANIM_DURATION)).after(android.support.design.widget.FloatingActionButtonImpl.PRESSED_ANIM_DURATION);
        set.play(android.animation.ObjectAnimator.ofFloat(mView, "elevation", elevation).setDuration(0)).with(anim);
        set.setInterpolator(android.support.design.widget.FloatingActionButtonImpl.ANIM_INTERPOLATOR);
        stateListAnimator.addState(android.support.design.widget.FloatingActionButtonImpl.ENABLED_STATE_SET, set);
        // Animate everything to 0 when disabled
        set = new android.animation.AnimatorSet();
        set.play(android.animation.ObjectAnimator.ofFloat(mView, "elevation", 0.0F).setDuration(0)).with(android.animation.ObjectAnimator.ofFloat(mView, android.view.View.TRANSLATION_Z, 0.0F).setDuration(0));
        set.setInterpolator(android.support.design.widget.FloatingActionButtonImpl.ANIM_INTERPOLATOR);
        stateListAnimator.addState(android.support.design.widget.FloatingActionButtonImpl.EMPTY_STATE_SET, set);
        mView.setStateListAnimator(stateListAnimator);
        if (mShadowViewDelegate.isCompatPaddingEnabled()) {
            updatePadding();
        }
    }

    @java.lang.Override
    public float getElevation() {
        return mView.getElevation();
    }

    @java.lang.Override
    void onCompatShadowChanged() {
        updatePadding();
    }

    @java.lang.Override
    void onPaddingUpdated(android.graphics.Rect padding) {
        if (mShadowViewDelegate.isCompatPaddingEnabled()) {
            mInsetDrawable = new android.graphics.drawable.InsetDrawable(mRippleDrawable, padding.left, padding.top, padding.right, padding.bottom);
            mShadowViewDelegate.setBackgroundDrawable(mInsetDrawable);
        } else {
            mShadowViewDelegate.setBackgroundDrawable(mRippleDrawable);
        }
    }

    @java.lang.Override
    void onDrawableStateChanged(int[] state) {
        // no-op
    }

    @java.lang.Override
    void jumpDrawableToCurrentState() {
        // no-op
    }

    @java.lang.Override
    boolean requirePreDrawListener() {
        return false;
    }

    @java.lang.Override
    android.support.design.widget.CircularBorderDrawable newCircularDrawable() {
        return new android.support.design.widget.CircularBorderDrawableLollipop();
    }

    @java.lang.Override
    void getPadding(android.graphics.Rect rect) {
        if (mShadowViewDelegate.isCompatPaddingEnabled()) {
            final float radius = mShadowViewDelegate.getRadius();
            final float maxShadowSize = getElevation() + mPressedTranslationZ;
            final int hPadding = ((int) (java.lang.Math.ceil(android.support.design.widget.ShadowDrawableWrapper.calculateHorizontalPadding(maxShadowSize, radius, false))));
            final int vPadding = ((int) (java.lang.Math.ceil(android.support.design.widget.ShadowDrawableWrapper.calculateVerticalPadding(maxShadowSize, radius, false))));
            rect.set(hPadding, vPadding, hPadding, vPadding);
        } else {
            rect.set(0, 0, 0, 0);
        }
    }
}

