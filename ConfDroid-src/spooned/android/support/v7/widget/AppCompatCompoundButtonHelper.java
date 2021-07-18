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
package android.support.v7.widget;


class AppCompatCompoundButtonHelper {
    private final android.widget.CompoundButton mView;

    private android.content.res.ColorStateList mButtonTintList = null;

    private android.graphics.PorterDuff.Mode mButtonTintMode = null;

    private boolean mHasButtonTint = false;

    private boolean mHasButtonTintMode = false;

    private boolean mSkipNextApply;

    /**
     * Interface which allows us to directly set a button, bypass any calls back to ourselves.
     */
    interface DirectSetButtonDrawableInterface {
        void setButtonDrawable(android.graphics.drawable.Drawable buttonDrawable);
    }

    AppCompatCompoundButtonHelper(android.widget.CompoundButton view) {
        mView = view;
    }

    void loadFromAttributes(android.util.AttributeSet attrs, int defStyleAttr) {
        android.content.res.TypedArray a = mView.getContext().obtainStyledAttributes(attrs, R.styleable.CompoundButton, defStyleAttr, 0);
        try {
            if (a.hasValue(R.styleable.CompoundButton_android_button)) {
                final int resourceId = a.getResourceId(R.styleable.CompoundButton_android_button, 0);
                if (resourceId != 0) {
                    mView.setButtonDrawable(android.support.v7.content.res.AppCompatResources.getDrawable(mView.getContext(), resourceId));
                }
            }
            if (a.hasValue(R.styleable.CompoundButton_buttonTint)) {
                android.support.v4.widget.CompoundButtonCompat.setButtonTintList(mView, a.getColorStateList(R.styleable.CompoundButton_buttonTint));
            }
            if (a.hasValue(R.styleable.CompoundButton_buttonTintMode)) {
                android.support.v4.widget.CompoundButtonCompat.setButtonTintMode(mView, android.support.v7.widget.DrawableUtils.parseTintMode(a.getInt(R.styleable.CompoundButton_buttonTintMode, -1), null));
            }
        } finally {
            a.recycle();
        }
    }

    void setSupportButtonTintList(android.content.res.ColorStateList tint) {
        mButtonTintList = tint;
        mHasButtonTint = true;
        applyButtonTint();
    }

    android.content.res.ColorStateList getSupportButtonTintList() {
        return mButtonTintList;
    }

    void setSupportButtonTintMode(@android.support.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        mButtonTintMode = tintMode;
        mHasButtonTintMode = true;
        applyButtonTint();
    }

    android.graphics.PorterDuff.Mode getSupportButtonTintMode() {
        return mButtonTintMode;
    }

    void onSetButtonDrawable() {
        if (mSkipNextApply) {
            mSkipNextApply = false;
            return;
        }
        mSkipNextApply = true;
        applyButtonTint();
    }

    void applyButtonTint() {
        android.graphics.drawable.Drawable buttonDrawable = android.support.v4.widget.CompoundButtonCompat.getButtonDrawable(mView);
        if ((buttonDrawable != null) && (mHasButtonTint || mHasButtonTintMode)) {
            buttonDrawable = android.support.v4.graphics.drawable.DrawableCompat.wrap(buttonDrawable);
            buttonDrawable = buttonDrawable.mutate();
            if (mHasButtonTint) {
                android.support.v4.graphics.drawable.DrawableCompat.setTintList(buttonDrawable, mButtonTintList);
            }
            if (mHasButtonTintMode) {
                android.support.v4.graphics.drawable.DrawableCompat.setTintMode(buttonDrawable, mButtonTintMode);
            }
            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (buttonDrawable.isStateful()) {
                buttonDrawable.setState(mView.getDrawableState());
            }
            mView.setButtonDrawable(buttonDrawable);
        }
    }

    int getCompoundPaddingLeft(int superValue) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Before JB-MR1 the button drawable wasn't taken into account for padding. We'll
            // workaround that here
            android.graphics.drawable.Drawable buttonDrawable = android.support.v4.widget.CompoundButtonCompat.getButtonDrawable(mView);
            if (buttonDrawable != null) {
                superValue += buttonDrawable.getIntrinsicWidth();
            }
        }
        return superValue;
    }
}

