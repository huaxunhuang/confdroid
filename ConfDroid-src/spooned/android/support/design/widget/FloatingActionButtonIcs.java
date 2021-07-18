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


class FloatingActionButtonIcs extends android.support.design.widget.FloatingActionButtonGingerbread {
    private float mRotation;

    FloatingActionButtonIcs(android.support.design.widget.VisibilityAwareImageButton view, android.support.design.widget.ShadowViewDelegate shadowViewDelegate, android.support.design.widget.ValueAnimatorCompat.Creator animatorCreator) {
        super(view, shadowViewDelegate, animatorCreator);
        mRotation = mView.getRotation();
    }

    @java.lang.Override
    boolean requirePreDrawListener() {
        return true;
    }

    @java.lang.Override
    void onPreDraw() {
        final float rotation = mView.getRotation();
        if (mRotation != rotation) {
            mRotation = rotation;
            updateFromViewRotation();
        }
    }

    @java.lang.Override
    void hide(@android.support.annotation.Nullable
    final android.support.design.widget.FloatingActionButtonImpl.InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (isOrWillBeHidden()) {
            // We either are or will soon be hidden, skip the call
            return;
        }
        mView.animate().cancel();
        if (shouldAnimateVisibilityChange()) {
            mAnimState = android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_HIDING;
            mView.animate().scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setDuration(android.support.design.widget.FloatingActionButtonImpl.SHOW_HIDE_ANIM_DURATION).setInterpolator(android.support.design.widget.AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener(new android.animation.AnimatorListenerAdapter() {
                private boolean mCancelled;

                @java.lang.Override
                public void onAnimationStart(android.animation.Animator animation) {
                    mView.internalSetVisibility(android.view.View.VISIBLE, fromUser);
                    mCancelled = false;
                }

                @java.lang.Override
                public void onAnimationCancel(android.animation.Animator animation) {
                    mCancelled = true;
                }

                @java.lang.Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    mAnimState = android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_NONE;
                    if (!mCancelled) {
                        mView.internalSetVisibility(android.view.View.GONE, fromUser);
                        if (listener != null) {
                            listener.onHidden();
                        }
                    }
                }
            });
        } else {
            // If the view isn't laid out, or we're in the editor, don't run the animation
            mView.internalSetVisibility(android.view.View.GONE, fromUser);
            if (listener != null) {
                listener.onHidden();
            }
        }
    }

    @java.lang.Override
    void show(@android.support.annotation.Nullable
    final android.support.design.widget.FloatingActionButtonImpl.InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (isOrWillBeShown()) {
            // We either are or will soon be visible, skip the call
            return;
        }
        mView.animate().cancel();
        if (shouldAnimateVisibilityChange()) {
            mAnimState = android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_SHOWING;
            if (mView.getVisibility() != android.view.View.VISIBLE) {
                // If the view isn't visible currently, we'll animate it from a single pixel
                mView.setAlpha(0.0F);
                mView.setScaleY(0.0F);
                mView.setScaleX(0.0F);
            }
            mView.animate().scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setDuration(android.support.design.widget.FloatingActionButtonImpl.SHOW_HIDE_ANIM_DURATION).setInterpolator(android.support.design.widget.AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener(new android.animation.AnimatorListenerAdapter() {
                @java.lang.Override
                public void onAnimationStart(android.animation.Animator animation) {
                    mView.internalSetVisibility(android.view.View.VISIBLE, fromUser);
                }

                @java.lang.Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    mAnimState = android.support.design.widget.FloatingActionButtonImpl.ANIM_STATE_NONE;
                    if (listener != null) {
                        listener.onShown();
                    }
                }
            });
        } else {
            mView.internalSetVisibility(android.view.View.VISIBLE, fromUser);
            mView.setAlpha(1.0F);
            mView.setScaleY(1.0F);
            mView.setScaleX(1.0F);
            if (listener != null) {
                listener.onShown();
            }
        }
    }

    private boolean shouldAnimateVisibilityChange() {
        return android.support.v4.view.ViewCompat.isLaidOut(mView) && (!mView.isInEditMode());
    }

    private void updateFromViewRotation() {
        if (android.os.Build.VERSION.SDK_INT == 19) {
            // KitKat seems to have an issue with views which are rotated with angles which are
            // not divisible by 90. Worked around by moving to software rendering in these cases.
            if ((mRotation % 90) != 0) {
                if (mView.getLayerType() != android.view.View.LAYER_TYPE_SOFTWARE) {
                    mView.setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null);
                }
            } else {
                if (mView.getLayerType() != android.view.View.LAYER_TYPE_NONE) {
                    mView.setLayerType(android.view.View.LAYER_TYPE_NONE, null);
                }
            }
        }
        // Offset any View rotation
        if (mShadowDrawable != null) {
            mShadowDrawable.setRotation(-mRotation);
        }
        if (mBorderDrawable != null) {
            mBorderDrawable.setRotation(-mRotation);
        }
    }
}

