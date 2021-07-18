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


class AppCompatBackgroundHelper {
    private final android.view.View mView;

    private final android.support.v7.widget.AppCompatDrawableManager mDrawableManager;

    private int mBackgroundResId = -1;

    private android.support.v7.widget.AppCompatBackgroundHelper.BackgroundTintInfo mInternalBackgroundTint;

    private android.support.v7.widget.AppCompatBackgroundHelper.BackgroundTintInfo mBackgroundTint;

    private android.support.v7.widget.AppCompatBackgroundHelper.BackgroundTintInfo mTmpInfo;

    AppCompatBackgroundHelper(android.view.View view) {
        mView = view;
        mDrawableManager = android.support.v7.widget.AppCompatDrawableManager.get();
    }

    void loadFromAttributes(android.util.AttributeSet attrs, int defStyleAttr) {
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(mView.getContext(), attrs, R.styleable.ViewBackgroundHelper, defStyleAttr, 0);
        try {
            if (a.hasValue(R.styleable.ViewBackgroundHelper_android_background)) {
                mBackgroundResId = a.getResourceId(R.styleable.ViewBackgroundHelper_android_background, -1);
                android.content.res.ColorStateList tint = mDrawableManager.getTintList(mView.getContext(), mBackgroundResId);
                if (tint != null) {
                    setInternalBackgroundTint(tint);
                }
            }
            if (a.hasValue(R.styleable.ViewBackgroundHelper_backgroundTint)) {
                android.support.v4.view.ViewCompat.setBackgroundTintList(mView, a.getColorStateList(R.styleable.ViewBackgroundHelper_backgroundTint));
            }
            if (a.hasValue(R.styleable.ViewBackgroundHelper_backgroundTintMode)) {
                android.support.v4.view.ViewCompat.setBackgroundTintMode(mView, android.support.v7.widget.DrawableUtils.parseTintMode(a.getInt(R.styleable.ViewBackgroundHelper_backgroundTintMode, -1), null));
            }
        } finally {
            a.recycle();
        }
    }

    void onSetBackgroundResource(int resId) {
        mBackgroundResId = resId;
        // Update the default background tint
        setInternalBackgroundTint(mDrawableManager != null ? mDrawableManager.getTintList(mView.getContext(), resId) : null);
        if (updateBackgroundTint()) {
            applySupportBackgroundTint();
        }
    }

    void onSetBackgroundDrawable(android.graphics.drawable.Drawable background) {
        mBackgroundResId = -1;
        // We don't know that this drawable is, so we need to clear the default background tint
        setInternalBackgroundTint(null);
        if (updateBackgroundTint()) {
            applySupportBackgroundTint();
        }
    }

    void setSupportBackgroundTintList(android.content.res.ColorStateList tint) {
        if (mBackgroundTint == null) {
            mBackgroundTint = new android.support.v7.widget.AppCompatBackgroundHelper.BackgroundTintInfo();
        }
        // Store the original tint and null out the applicable tint. updateBackgroundTint() will
        // set mTintList to the tint to actually use
        mBackgroundTint.mOriginalTintList = tint;
        mBackgroundTint.mTintList = null;
        mBackgroundTint.mHasTintList = true;
        if (updateBackgroundTint()) {
            applySupportBackgroundTint();
        }
    }

    /**
     * Updates the background tint state
     *
     * @return true if the state was changed and requires an apply
     */
    private boolean updateBackgroundTint() {
        if ((mBackgroundTint != null) && mBackgroundTint.mHasTintList) {
            if (mBackgroundResId >= 0) {
                // If we have a background resource id, lets see if we need to modify the tint
                // list to add any touch highlights in (for example, Button needs this)
                final android.content.res.ColorStateList updated = mDrawableManager.getTintList(mView.getContext(), mBackgroundResId, mBackgroundTint.mOriginalTintList);
                if (updated != null) {
                    mBackgroundTint.mTintList = updated;
                    return true;
                }
            }
            // If we reach here then we should just be using the original tint list. Check if we
            // need to set and apply
            if (mBackgroundTint.mTintList != mBackgroundTint.mOriginalTintList) {
                mBackgroundTint.mTintList = mBackgroundTint.mOriginalTintList;
                return true;
            }
        }
        return false;
    }

    android.content.res.ColorStateList getSupportBackgroundTintList() {
        return mBackgroundTint != null ? mBackgroundTint.mTintList : null;
    }

    void setSupportBackgroundTintMode(android.graphics.PorterDuff.Mode tintMode) {
        if (mBackgroundTint == null) {
            mBackgroundTint = new android.support.v7.widget.AppCompatBackgroundHelper.BackgroundTintInfo();
        }
        mBackgroundTint.mTintMode = tintMode;
        mBackgroundTint.mHasTintMode = true;
        applySupportBackgroundTint();
    }

    android.graphics.PorterDuff.Mode getSupportBackgroundTintMode() {
        return mBackgroundTint != null ? mBackgroundTint.mTintMode : null;
    }

    void applySupportBackgroundTint() {
        final android.graphics.drawable.Drawable background = mView.getBackground();
        if (background != null) {
            if (shouldApplyFrameworkTintUsingColorFilter() && applyFrameworkTintUsingColorFilter(background)) {
                // This needs to be called before the internal tints below so it takes
                // effect on any widgets using the compat tint on API 21 (EditText)
                return;
            }
            if (mBackgroundTint != null) {
                android.support.v7.widget.AppCompatDrawableManager.tintDrawable(background, mBackgroundTint, mView.getDrawableState());
            } else
                if (mInternalBackgroundTint != null) {
                    android.support.v7.widget.AppCompatDrawableManager.tintDrawable(background, mInternalBackgroundTint, mView.getDrawableState());
                }

        }
    }

    void setInternalBackgroundTint(android.content.res.ColorStateList tint) {
        if (tint != null) {
            if (mInternalBackgroundTint == null) {
                mInternalBackgroundTint = new android.support.v7.widget.AppCompatBackgroundHelper.BackgroundTintInfo();
            }
            mInternalBackgroundTint.mTintList = tint;
            mInternalBackgroundTint.mHasTintList = true;
        } else {
            mInternalBackgroundTint = null;
        }
        applySupportBackgroundTint();
    }

    private boolean shouldApplyFrameworkTintUsingColorFilter() {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < 21) {
            // API 19 and below doesn't have framework tint
            return false;
        } else
            if (sdk == 21) {
                // GradientDrawable doesn't implement setTintList on API 21, and since there is
                // no nice way to unwrap DrawableContainers we have to blanket apply this
                // on API 21
                return true;
            } else {
                // On API 22+, if we're using an internal compat background tint, we're also
                // responsible for applying any custom tint set via the framework impl
                return mInternalBackgroundTint != null;
            }

    }

    /**
     * Applies the framework background tint to a view, but using the compat method (ColorFilter)
     *
     * @return true if a tint was applied
     */
    private boolean applyFrameworkTintUsingColorFilter(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable background) {
        if (mTmpInfo == null) {
            mTmpInfo = new android.support.v7.widget.AppCompatBackgroundHelper.BackgroundTintInfo();
        }
        final android.support.v7.widget.TintInfo info = mTmpInfo;
        info.clear();
        final android.content.res.ColorStateList tintList = android.support.v4.view.ViewCompat.getBackgroundTintList(mView);
        if (tintList != null) {
            info.mHasTintList = true;
            info.mTintList = tintList;
        }
        final android.graphics.PorterDuff.Mode mode = android.support.v4.view.ViewCompat.getBackgroundTintMode(mView);
        if (mode != null) {
            info.mHasTintMode = true;
            info.mTintMode = mode;
        }
        if (info.mHasTintList || info.mHasTintMode) {
            android.support.v7.widget.AppCompatDrawableManager.tintDrawable(background, info, mView.getDrawableState());
            return true;
        }
        return false;
    }

    private static class BackgroundTintInfo extends android.support.v7.widget.TintInfo {
        // The original tint list given to the call. We need this distinction because create a
        // modified for actual tinting purposes
        public android.content.res.ColorStateList mOriginalTintList;

        BackgroundTintInfo() {
        }

        @java.lang.Override
        void clear() {
            super.clear();
            mOriginalTintList = null;
        }
    }
}

