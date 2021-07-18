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
package android.support.v4.graphics.drawable;


/**
 * Drawable which delegates all calls to it's wrapped {@link android.graphics.drawable.Drawable}.
 * <p>
 * Also allows backward compatible tinting via a color or {@link ColorStateList}.
 * This functionality is accessed via static methods in {@code DrawableCompat}.
 */
class DrawableWrapperGingerbread extends android.graphics.drawable.Drawable implements android.graphics.drawable.Drawable.Callback , android.support.v4.graphics.drawable.DrawableWrapper , android.support.v4.graphics.drawable.TintAwareDrawable {
    static final android.graphics.PorterDuff.Mode DEFAULT_TINT_MODE = android.graphics.PorterDuff.Mode.SRC_IN;

    private int mCurrentColor;

    private android.graphics.PorterDuff.Mode mCurrentMode;

    private boolean mColorFilterSet;

    android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState mState;

    private boolean mMutated;

    android.graphics.drawable.Drawable mDrawable;

    DrawableWrapperGingerbread(@android.support.annotation.NonNull
    android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState state, @android.support.annotation.Nullable
    android.content.res.Resources res) {
        mState = state;
        updateLocalState(res);
    }

    /**
     * Creates a new wrapper around the specified drawable.
     *
     * @param dr
     * 		the drawable to wrap
     */
    DrawableWrapperGingerbread(@android.support.annotation.Nullable
    android.graphics.drawable.Drawable dr) {
        mState = mutateConstantState();
        // Now set the drawable...
        setWrappedDrawable(dr);
    }

    /**
     * Initializes local dynamic properties from state. This should be called
     * after significant state changes, e.g. from the One True Constructor and
     * after inflating or applying a theme.
     */
    private void updateLocalState(@android.support.annotation.Nullable
    android.content.res.Resources res) {
        if ((mState != null) && (mState.mDrawableState != null)) {
            final android.graphics.drawable.Drawable dr = newDrawableFromState(mState.mDrawableState, res);
            setWrappedDrawable(dr);
        }
    }

    /**
     * Allows us to call ConstantState.newDrawable(*) is a API safe way
     */
    protected android.graphics.drawable.Drawable newDrawableFromState(@android.support.annotation.NonNull
    android.graphics.drawable.Drawable.ConstantState state, @android.support.annotation.Nullable
    android.content.res.Resources res) {
        return state.newDrawable(res);
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        mDrawable.draw(canvas);
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        if (mDrawable != null) {
            mDrawable.setBounds(bounds);
        }
    }

    @java.lang.Override
    public void setChangingConfigurations(int configs) {
        mDrawable.setChangingConfigurations(configs);
    }

    @java.lang.Override
    public int getChangingConfigurations() {
        return (super.getChangingConfigurations() | (mState != null ? mState.getChangingConfigurations() : 0)) | mDrawable.getChangingConfigurations();
    }

    @java.lang.Override
    public void setDither(boolean dither) {
        mDrawable.setDither(dither);
    }

    @java.lang.Override
    public void setFilterBitmap(boolean filter) {
        mDrawable.setFilterBitmap(filter);
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        mDrawable.setAlpha(alpha);
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter cf) {
        mDrawable.setColorFilter(cf);
    }

    @java.lang.Override
    public boolean isStateful() {
        final android.content.res.ColorStateList tintList = (isCompatTintEnabled() && (mState != null)) ? mState.mTint : null;
        return ((tintList != null) && tintList.isStateful()) || mDrawable.isStateful();
    }

    @java.lang.Override
    public boolean setState(final int[] stateSet) {
        boolean handled = mDrawable.setState(stateSet);
        handled = updateTint(stateSet) || handled;
        return handled;
    }

    @java.lang.Override
    public int[] getState() {
        return mDrawable.getState();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getCurrent() {
        return mDrawable.getCurrent();
    }

    @java.lang.Override
    public boolean setVisible(boolean visible, boolean restart) {
        return super.setVisible(visible, restart) || mDrawable.setVisible(visible, restart);
    }

    @java.lang.Override
    public int getOpacity() {
        return mDrawable.getOpacity();
    }

    @java.lang.Override
    public android.graphics.Region getTransparentRegion() {
        return mDrawable.getTransparentRegion();
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        return mDrawable.getIntrinsicWidth();
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        return mDrawable.getIntrinsicHeight();
    }

    @java.lang.Override
    public int getMinimumWidth() {
        return mDrawable.getMinimumWidth();
    }

    @java.lang.Override
    public int getMinimumHeight() {
        return mDrawable.getMinimumHeight();
    }

    @java.lang.Override
    public boolean getPadding(android.graphics.Rect padding) {
        return mDrawable.getPadding(padding);
    }

    @java.lang.Override
    @android.support.annotation.Nullable
    public android.graphics.drawable.Drawable.ConstantState getConstantState() {
        if ((mState != null) && mState.canConstantState()) {
            mState.mChangingConfigurations = getChangingConfigurations();
            return mState;
        }
        return null;
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable mutate() {
        if ((!mMutated) && (super.mutate() == this)) {
            mState = mutateConstantState();
            if (mDrawable != null) {
                mDrawable.mutate();
            }
            if (mState != null) {
                mState.mDrawableState = (mDrawable != null) ? mDrawable.getConstantState() : null;
            }
            mMutated = true;
        }
        return this;
    }

    /**
     * Mutates the constant state and returns the new state.
     * <p>
     * This method should never call the super implementation; it should always
     * mutate and return its own constant state.
     *
     * @return the new state
     */
    @android.support.annotation.NonNull
    android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState mutateConstantState() {
        return new android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperStateBase(mState, null);
    }

    /**
     * {@inheritDoc }
     */
    public void invalidateDrawable(android.graphics.drawable.Drawable who) {
        invalidateSelf();
    }

    /**
     * {@inheritDoc }
     */
    public void scheduleDrawable(android.graphics.drawable.Drawable who, java.lang.Runnable what, long when) {
        scheduleSelf(what, when);
    }

    /**
     * {@inheritDoc }
     */
    public void unscheduleDrawable(android.graphics.drawable.Drawable who, java.lang.Runnable what) {
        unscheduleSelf(what);
    }

    @java.lang.Override
    protected boolean onLevelChange(int level) {
        return mDrawable.setLevel(level);
    }

    @java.lang.Override
    public void setTint(int tint) {
        setTintList(android.content.res.ColorStateList.valueOf(tint));
    }

    @java.lang.Override
    public void setTintList(android.content.res.ColorStateList tint) {
        mState.mTint = tint;
        updateTint(getState());
    }

    @java.lang.Override
    public void setTintMode(android.graphics.PorterDuff.Mode tintMode) {
        mState.mTintMode = tintMode;
        updateTint(getState());
    }

    private boolean updateTint(int[] state) {
        if (!isCompatTintEnabled()) {
            // If compat tinting is not enabled, fail fast
            return false;
        }
        final android.content.res.ColorStateList tintList = mState.mTint;
        final android.graphics.PorterDuff.Mode tintMode = mState.mTintMode;
        if ((tintList != null) && (tintMode != null)) {
            final int color = tintList.getColorForState(state, tintList.getDefaultColor());
            if (((!mColorFilterSet) || (color != mCurrentColor)) || (tintMode != mCurrentMode)) {
                setColorFilter(color, tintMode);
                mCurrentColor = color;
                mCurrentMode = tintMode;
                mColorFilterSet = true;
                return true;
            }
        } else {
            mColorFilterSet = false;
            clearColorFilter();
        }
        return false;
    }

    /**
     * Returns the wrapped {@link Drawable}
     */
    public final android.graphics.drawable.Drawable getWrappedDrawable() {
        return mDrawable;
    }

    /**
     * Sets the current wrapped {@link Drawable}
     */
    public final void setWrappedDrawable(android.graphics.drawable.Drawable dr) {
        if (mDrawable != null) {
            mDrawable.setCallback(null);
        }
        mDrawable = dr;
        if (dr != null) {
            dr.setCallback(this);
            // Only call setters for data that's stored in the base Drawable.
            setVisible(dr.isVisible(), true);
            setState(dr.getState());
            setLevel(dr.getLevel());
            setBounds(dr.getBounds());
            if (mState != null) {
                mState.mDrawableState = dr.getConstantState();
            }
        }
        invalidateSelf();
    }

    protected boolean isCompatTintEnabled() {
        // It's enabled by default on Gingerbread
        return true;
    }

    protected static abstract class DrawableWrapperState extends android.graphics.drawable.Drawable.ConstantState {
        int mChangingConfigurations;

        android.graphics.drawable.Drawable.ConstantState mDrawableState;

        android.content.res.ColorStateList mTint = null;

        android.graphics.PorterDuff.Mode mTintMode = android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DEFAULT_TINT_MODE;

        DrawableWrapperState(@android.support.annotation.Nullable
        android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState orig, @android.support.annotation.Nullable
        android.content.res.Resources res) {
            if (orig != null) {
                mChangingConfigurations = orig.mChangingConfigurations;
                mDrawableState = orig.mDrawableState;
                mTint = orig.mTint;
                mTintMode = orig.mTintMode;
            }
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return newDrawable(null);
        }

        public abstract android.graphics.drawable.Drawable newDrawable(@android.support.annotation.Nullable
        android.content.res.Resources res);

        @java.lang.Override
        public int getChangingConfigurations() {
            return mChangingConfigurations | (mDrawableState != null ? mDrawableState.getChangingConfigurations() : 0);
        }

        boolean canConstantState() {
            return mDrawableState != null;
        }
    }

    private static class DrawableWrapperStateBase extends android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState {
        DrawableWrapperStateBase(@android.support.annotation.Nullable
        android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState orig, @android.support.annotation.Nullable
        android.content.res.Resources res) {
            super(orig, res);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(@android.support.annotation.Nullable
        android.content.res.Resources res) {
            return new android.support.v4.graphics.drawable.DrawableWrapperGingerbread(this, res);
        }
    }
}

