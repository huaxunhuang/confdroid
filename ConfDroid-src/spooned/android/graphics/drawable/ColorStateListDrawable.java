/**
 * Copyright 2018 The Android Open Source Project
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
 * A Drawable that manages a {@link ColorDrawable} to make it stateful and backed by a
 * {@link ColorStateList}.
 */
public class ColorStateListDrawable extends android.graphics.drawable.Drawable implements android.graphics.drawable.Drawable.Callback {
    private android.graphics.drawable.ColorDrawable mColorDrawable;

    private android.graphics.drawable.ColorStateListDrawable.ColorStateListDrawableState mState;

    private boolean mMutated = false;

    public ColorStateListDrawable() {
        mState = new android.graphics.drawable.ColorStateListDrawable.ColorStateListDrawableState();
        initializeColorDrawable();
    }

    public ColorStateListDrawable(@android.annotation.NonNull
    android.content.res.ColorStateList colorStateList) {
        mState = new android.graphics.drawable.ColorStateListDrawable.ColorStateListDrawableState();
        initializeColorDrawable();
        setColorStateList(colorStateList);
    }

    @java.lang.Override
    public void draw(@android.annotation.NonNull
    android.graphics.Canvas canvas) {
        mColorDrawable.draw(canvas);
    }

    @java.lang.Override
    @android.annotation.IntRange(from = 0, to = 255)
    public int getAlpha() {
        return mColorDrawable.getAlpha();
    }

    @java.lang.Override
    public boolean isStateful() {
        return mState.isStateful();
    }

    @java.lang.Override
    public boolean hasFocusStateSpecified() {
        return mState.hasFocusStateSpecified();
    }

    @java.lang.Override
    @android.annotation.NonNull
    public android.graphics.drawable.Drawable getCurrent() {
        return mColorDrawable;
    }

    @java.lang.Override
    public void applyTheme(@android.annotation.NonNull
    android.content.res.Resources.Theme t) {
        super.applyTheme(t);
        if (mState.mColor != null) {
            setColorStateList(mState.mColor.obtainForTheme(t));
        }
        if (mState.mTint != null) {
            setTintList(mState.mTint.obtainForTheme(t));
        }
    }

    @java.lang.Override
    public boolean canApplyTheme() {
        return super.canApplyTheme() || mState.canApplyTheme();
    }

    @java.lang.Override
    public void setAlpha(@android.annotation.IntRange(from = 0, to = 255)
    int alpha) {
        mState.mAlpha = alpha;
        onStateChange(getState());
    }

    /**
     * Remove the alpha override, reverting to the alpha defined on each color in the
     * {@link ColorStateList}.
     */
    public void clearAlpha() {
        mState.mAlpha = -1;
        onStateChange(getState());
    }

    @java.lang.Override
    public void setTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        mState.mTint = tint;
        mColorDrawable.setTintList(tint);
        onStateChange(getState());
    }

    @java.lang.Override
    public void setTintBlendMode(@android.annotation.NonNull
    android.graphics.BlendMode blendMode) {
        mState.mBlendMode = blendMode;
        mColorDrawable.setTintBlendMode(blendMode);
        onStateChange(getState());
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.graphics.ColorFilter getColorFilter() {
        return mColorDrawable.getColorFilter();
    }

    @java.lang.Override
    public void setColorFilter(@android.annotation.Nullable
    android.graphics.ColorFilter colorFilter) {
        mColorDrawable.setColorFilter(colorFilter);
    }

    @java.lang.Override
    @android.graphics.PixelFormat.Opacity
    public int getOpacity() {
        return mColorDrawable.getOpacity();
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        super.onBoundsChange(bounds);
        mColorDrawable.setBounds(bounds);
    }

    @java.lang.Override
    protected boolean onStateChange(int[] state) {
        if (mState.mColor != null) {
            int color = mState.mColor.getColorForState(state, mState.mColor.getDefaultColor());
            if (mState.mAlpha != (-1)) {
                color = (color & 0xffffff) | (android.util.MathUtils.constrain(mState.mAlpha, 0, 255) << 24);
            }
            if (color != mColorDrawable.getColor()) {
                mColorDrawable.setColor(color);
                mColorDrawable.setState(state);
                return true;
            } else {
                return mColorDrawable.setState(state);
            }
        } else {
            return false;
        }
    }

    @java.lang.Override
    public void invalidateDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who) {
        if ((who == mColorDrawable) && (getCallback() != null)) {
            getCallback().invalidateDrawable(this);
        }
    }

    @java.lang.Override
    public void scheduleDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who, @android.annotation.NonNull
    java.lang.Runnable what, long when) {
        if ((who == mColorDrawable) && (getCallback() != null)) {
            getCallback().scheduleDrawable(this, what, when);
        }
    }

    @java.lang.Override
    public void unscheduleDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who, @android.annotation.NonNull
    java.lang.Runnable what) {
        if ((who == mColorDrawable) && (getCallback() != null)) {
            getCallback().unscheduleDrawable(this, what);
        }
    }

    @java.lang.Override
    @android.annotation.NonNull
    public android.graphics.drawable.Drawable.ConstantState getConstantState() {
        mState.mChangingConfigurations = mState.mChangingConfigurations | (getChangingConfigurations() & (~mState.getChangingConfigurations()));
        return mState;
    }

    /**
     * Returns the ColorStateList backing this Drawable, or a new ColorStateList of the default
     * ColorDrawable color if one hasn't been defined yet.
     *
     * @return a ColorStateList
     */
    @android.annotation.NonNull
    public android.content.res.ColorStateList getColorStateList() {
        if (mState.mColor == null) {
            return android.content.res.ColorStateList.valueOf(mColorDrawable.getColor());
        } else {
            return mState.mColor;
        }
    }

    @java.lang.Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | mState.getChangingConfigurations();
    }

    @java.lang.Override
    @android.annotation.NonNull
    public android.graphics.drawable.Drawable mutate() {
        if ((!mMutated) && (super.mutate() == this)) {
            mState = new android.graphics.drawable.ColorStateListDrawable.ColorStateListDrawableState(mState);
            mMutated = true;
        }
        return this;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void clearMutated() {
        super.clearMutated();
        mMutated = false;
    }

    /**
     * Replace this Drawable's ColorStateList. It is not copied, so changes will propagate on the
     * next call to {@link #setState(int[])}.
     *
     * @param colorStateList
     * 		A color state list to attach.
     */
    public void setColorStateList(@android.annotation.NonNull
    android.content.res.ColorStateList colorStateList) {
        mState.mColor = colorStateList;
        onStateChange(getState());
    }

    static final class ColorStateListDrawableState extends android.graphics.drawable.Drawable.ConstantState {
        android.content.res.ColorStateList mColor = null;

        android.content.res.ColorStateList mTint = null;

        int mAlpha = -1;

        android.graphics.BlendMode mBlendMode = android.graphics.drawable.Drawable.DEFAULT_BLEND_MODE;

        @android.content.pm.ActivityInfo.Config
        int mChangingConfigurations = 0;

        ColorStateListDrawableState() {
        }

        ColorStateListDrawableState(android.graphics.drawable.ColorStateListDrawable.ColorStateListDrawableState state) {
            mColor = state.mColor;
            mTint = state.mTint;
            mAlpha = state.mAlpha;
            mBlendMode = state.mBlendMode;
            mChangingConfigurations = state.mChangingConfigurations;
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.graphics.drawable.ColorStateListDrawable(this);
        }

        @java.lang.Override
        @android.content.pm.ActivityInfo.Config
        public int getChangingConfigurations() {
            return (mChangingConfigurations | (mColor != null ? mColor.getChangingConfigurations() : 0)) | (mTint != null ? mTint.getChangingConfigurations() : 0);
        }

        public boolean isStateful() {
            return ((mColor != null) && mColor.isStateful()) || ((mTint != null) && mTint.isStateful());
        }

        public boolean hasFocusStateSpecified() {
            return ((mColor != null) && mColor.hasFocusStateSpecified()) || ((mTint != null) && mTint.hasFocusStateSpecified());
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return ((mColor != null) && mColor.canApplyTheme()) || ((mTint != null) && mTint.canApplyTheme());
        }
    }

    private ColorStateListDrawable(@android.annotation.NonNull
    android.graphics.drawable.ColorStateListDrawable.ColorStateListDrawableState state) {
        mState = state;
        initializeColorDrawable();
    }

    private void initializeColorDrawable() {
        mColorDrawable = new android.graphics.drawable.ColorDrawable();
        mColorDrawable.setCallback(this);
        if (mState.mTint != null) {
            mColorDrawable.setTintList(mState.mTint);
        }
        if (mState.mBlendMode != android.graphics.drawable.Drawable.DEFAULT_BLEND_MODE) {
            mColorDrawable.setTintBlendMode(mState.mBlendMode);
        }
    }
}

