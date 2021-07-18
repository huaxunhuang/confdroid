/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.graphics.drawable;


/**
 * Draws a ripple background.
 */
class RippleBackground extends android.graphics.drawable.RippleComponent {
    private static final android.animation.TimeInterpolator LINEAR_INTERPOLATOR = new android.view.animation.LinearInterpolator();

    private static final int OPACITY_DURATION = 80;

    private android.animation.ObjectAnimator mAnimator;

    private float mOpacity = 0;

    /**
     * Whether this ripple is bounded.
     */
    private boolean mIsBounded;

    private boolean mFocused = false;

    private boolean mHovered = false;

    public RippleBackground(android.graphics.drawable.RippleDrawable owner, android.graphics.Rect bounds, boolean isBounded) {
        super(owner, bounds);
        mIsBounded = isBounded;
    }

    public boolean isVisible() {
        return mOpacity > 0;
    }

    public void draw(android.graphics.Canvas c, android.graphics.Paint p) {
        final int origAlpha = p.getAlpha();
        final int alpha = java.lang.Math.min(((int) ((origAlpha * mOpacity) + 0.5F)), 255);
        if (alpha > 0) {
            p.setAlpha(alpha);
            c.drawCircle(0, 0, mTargetRadius, p);
            p.setAlpha(origAlpha);
        }
    }

    public void setState(boolean focused, boolean hovered, boolean pressed) {
        if (!mFocused) {
            focused = focused && (!pressed);
        }
        if (!mHovered) {
            hovered = hovered && (!pressed);
        }
        if ((mHovered != hovered) || (mFocused != focused)) {
            mHovered = hovered;
            mFocused = focused;
            onStateChanged();
        }
    }

    private void onStateChanged() {
        // Hover             = .2 * alpha
        // Focus             = .6 * alpha
        // Focused + Hovered = .6 * alpha
        float newOpacity = (mFocused) ? 0.6F : mHovered ? 0.2F : 0.0F;
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
        mAnimator = android.animation.ObjectAnimator.ofFloat(this, android.graphics.drawable.RippleBackground.OPACITY, newOpacity);
        mAnimator.setDuration(android.graphics.drawable.RippleBackground.OPACITY_DURATION);
        mAnimator.setInterpolator(android.graphics.drawable.RippleBackground.LINEAR_INTERPOLATOR);
        mAnimator.start();
    }

    public void jumpToFinal() {
        if (mAnimator != null) {
            mAnimator.end();
            mAnimator = null;
        }
    }

    private static abstract class BackgroundProperty extends android.util.FloatProperty<android.graphics.drawable.RippleBackground> {
        public BackgroundProperty(java.lang.String name) {
            super(name);
        }
    }

    private static final android.graphics.drawable.RippleBackground.BackgroundProperty OPACITY = new android.graphics.drawable.RippleBackground.BackgroundProperty("opacity") {
        @java.lang.Override
        public void setValue(android.graphics.drawable.RippleBackground object, float value) {
            object.mOpacity = value;
            object.invalidateSelf();
        }

        @java.lang.Override
        public java.lang.Float get(android.graphics.drawable.RippleBackground object) {
            return object.mOpacity;
        }
    };
}

