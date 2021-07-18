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


abstract class FloatingActionButtonImpl {
    static final android.view.animation.Interpolator ANIM_INTERPOLATOR = android.support.design.widget.AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;

    static final long PRESSED_ANIM_DURATION = 100;

    static final long PRESSED_ANIM_DELAY = 100;

    static final int ANIM_STATE_NONE = 0;

    static final int ANIM_STATE_HIDING = 1;

    static final int ANIM_STATE_SHOWING = 2;

    int mAnimState = android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_NONE;

    android.graphics.drawable.Drawable mShapeDrawable;

    android.graphics.drawable.Drawable mRippleDrawable;

    android.support.design.widget.CircularBorderDrawable mBorderDrawable;

    android.graphics.drawable.Drawable mContentBackground;

    float mElevation;

    float mPressedTranslationZ;

    interface InternalVisibilityChangedListener {
        public void onShown();

        public void onHidden();
    }

    static final int SHOW_HIDE_ANIM_DURATION = 200;

    static final int[] PRESSED_ENABLED_STATE_SET = new int[]{ android.R.attr.state_pressed, android.R.attr.state_enabled };

    static final int[] FOCUSED_ENABLED_STATE_SET = new int[]{ android.R.attr.state_focused, android.R.attr.state_enabled };

    static final int[] ENABLED_STATE_SET = new int[]{ android.R.attr.state_enabled };

    static final int[] EMPTY_STATE_SET = new int[0];

    final android.support.design.widget.VisibilityAwareImageButton mView;

    final android.support.design.widget.ShadowViewDelegate mShadowViewDelegate;

    final android.support.design.widget.ValueAnimatorCompat.Creator mAnimatorCreator;

    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

    private android.view.ViewTreeObserver.OnPreDrawListener mPreDrawListener;

    FloatingActionButtonImpl(android.support.design.widget.VisibilityAwareImageButton view, android.support.design.widget.ShadowViewDelegate shadowViewDelegate, android.support.design.widget.ValueAnimatorCompat.Creator animatorCreator) {
        mView = view;
        mShadowViewDelegate = shadowViewDelegate;
        mAnimatorCreator = animatorCreator;
    }

    abstract void setBackgroundDrawable(android.content.res.ColorStateList backgroundTint, android.graphics.PorterDuff.Mode backgroundTintMode, int rippleColor, int borderWidth);

    abstract void setBackgroundTintList(android.content.res.ColorStateList tint);

    abstract void setBackgroundTintMode(android.graphics.PorterDuff.Mode tintMode);

    abstract void setRippleColor(int rippleColor);

    final void setElevation(float elevation) {
        if (mElevation != elevation) {
            mElevation = elevation;
            onElevationsChanged(elevation, mPressedTranslationZ);
        }
    }

    abstract float getElevation();

    final void setPressedTranslationZ(float translationZ) {
        if (mPressedTranslationZ != translationZ) {
            mPressedTranslationZ = translationZ;
            onElevationsChanged(mElevation, translationZ);
        }
    }

    abstract void onElevationsChanged(float elevation, float pressedTranslationZ);

    abstract void onDrawableStateChanged(int[] state);

    abstract void jumpDrawableToCurrentState();

    abstract void hide(@android.support.annotation.Nullable
    android.support.design.widget.FloatingActionButtonImpl.InternalVisibilityChangedListener listener, boolean fromUser);

    abstract void show(@android.support.annotation.Nullable
    android.support.design.widget.FloatingActionButtonImpl.InternalVisibilityChangedListener listener, boolean fromUser);

    final android.graphics.drawable.Drawable getContentBackground() {
        return mContentBackground;
    }

    abstract void onCompatShadowChanged();

    final void updatePadding() {
        android.graphics.Rect rect = mTmpRect;
        getPadding(rect);
        onPaddingUpdated(rect);
        mShadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    abstract void getPadding(android.graphics.Rect rect);

    void onPaddingUpdated(android.graphics.Rect padding) {
    }

    void onAttachedToWindow() {
        if (requirePreDrawListener()) {
            ensurePreDrawListener();
            mView.getViewTreeObserver().addOnPreDrawListener(mPreDrawListener);
        }
    }

    void onDetachedFromWindow() {
        if (mPreDrawListener != null) {
            mView.getViewTreeObserver().removeOnPreDrawListener(mPreDrawListener);
            mPreDrawListener = null;
        }
    }

    boolean requirePreDrawListener() {
        return false;
    }

    android.support.design.widget.CircularBorderDrawable createBorderDrawable(int borderWidth, android.content.res.ColorStateList backgroundTint) {
        final android.content.Context context = mView.getContext();
        android.support.design.widget.CircularBorderDrawable borderDrawable = newCircularDrawable();
        borderDrawable.setGradientColors(android.support.v4.content.ContextCompat.getColor(context, R.color.design_fab_stroke_top_outer_color), android.support.v4.content.ContextCompat.getColor(context, R.color.design_fab_stroke_top_inner_color), android.support.v4.content.ContextCompat.getColor(context, R.color.design_fab_stroke_end_inner_color), android.support.v4.content.ContextCompat.getColor(context, R.color.design_fab_stroke_end_outer_color));
        borderDrawable.setBorderWidth(borderWidth);
        borderDrawable.setBorderTint(backgroundTint);
        return borderDrawable;
    }

    android.support.design.widget.CircularBorderDrawable newCircularDrawable() {
        return new android.support.design.widget.CircularBorderDrawable();
    }

    void onPreDraw() {
    }

    private void ensurePreDrawListener() {
        if (mPreDrawListener == null) {
            mPreDrawListener = new android.view.ViewTreeObserver.OnPreDrawListener() {
                @java.lang.Override
                public boolean onPreDraw() {
                    android.support.design.widget.FloatingActionButtonImpl.this.onPreDraw();
                    return true;
                }
            };
        }
    }

    android.graphics.drawable.GradientDrawable createShapeDrawable() {
        android.graphics.drawable.GradientDrawable d = new android.graphics.drawable.GradientDrawable();
        d.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        d.setColor(android.graphics.Color.WHITE);
        return d;
    }

    boolean isOrWillBeShown() {
        if (mView.getVisibility() != android.view.View.VISIBLE) {
            // If we not currently visible, return true if we're animating to be shown
            return mAnimState == android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_SHOWING;
        } else {
            // Otherwise if we're visible, return true if we're not animating to be hidden
            return mAnimState != android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_HIDING;
        }
    }

    boolean isOrWillBeHidden() {
        if (mView.getVisibility() == android.view.View.VISIBLE) {
            // If we currently visible, return true if we're animating to be hidden
            return mAnimState == android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_HIDING;
        } else {
            // Otherwise if we're not visible, return true if we're not animating to be shown
            return mAnimState != android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_SHOWING;
        }
    }
}

