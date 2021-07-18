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
package android.graphics.drawable;


/**
 * Drawable container with only one child element.
 */
public abstract class DrawableWrapper extends android.graphics.drawable.Drawable implements android.graphics.drawable.Drawable.Callback {
    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.DrawableWrapper.DrawableWrapperState mState;

    private android.graphics.drawable.Drawable mDrawable;

    private boolean mMutated;

    DrawableWrapper(android.graphics.drawable.DrawableWrapper.DrawableWrapperState state, android.content.res.Resources res) {
        mState = state;
        updateLocalState(res);
    }

    /**
     * Creates a new wrapper around the specified drawable.
     *
     * @param dr
     * 		the drawable to wrap
     */
    public DrawableWrapper(@android.annotation.Nullable
    android.graphics.drawable.Drawable dr) {
        mState = null;
        setDrawable(dr);
    }

    /**
     * Initializes local dynamic properties from state. This should be called
     * after significant state changes, e.g. from the One True Constructor and
     * after inflating or applying a theme.
     */
    private void updateLocalState(android.content.res.Resources res) {
        if ((mState != null) && (mState.mDrawableState != null)) {
            final android.graphics.drawable.Drawable dr = mState.mDrawableState.newDrawable(res);
            setDrawable(dr);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void setXfermode(android.graphics.Xfermode mode) {
        if (mDrawable != null) {
            mDrawable.setXfermode(mode);
        }
    }

    /**
     * Sets the wrapped drawable.
     *
     * @param dr
     * 		the wrapped drawable
     */
    public void setDrawable(@android.annotation.Nullable
    android.graphics.drawable.Drawable dr) {
        if (mDrawable != null) {
            mDrawable.setCallback(null);
        }
        mDrawable = dr;
        if (dr != null) {
            dr.setCallback(this);
            // Only call setters for data that's stored in the base Drawable.
            dr.setVisible(isVisible(), true);
            dr.setState(getState());
            dr.setLevel(getLevel());
            dr.setBounds(getBounds());
            dr.setLayoutDirection(getLayoutDirection());
            if (mState != null) {
                mState.mDrawableState = dr.getConstantState();
            }
        }
        invalidateSelf();
    }

    /**
     *
     *
     * @return the wrapped drawable
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getDrawable() {
        return mDrawable;
    }

    @java.lang.Override
    public void inflate(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        super.inflate(r, parser, attrs, theme);
        final android.graphics.drawable.DrawableWrapper.DrawableWrapperState state = mState;
        if (state == null) {
            return;
        }
        // The density may have changed since the last update. This will
        // apply scaling to any existing constant state properties.
        final int densityDpi = r.getDisplayMetrics().densityDpi;
        final int targetDensity = (densityDpi == 0) ? android.util.DisplayMetrics.DENSITY_DEFAULT : densityDpi;
        state.setDensity(targetDensity);
        state.mSrcDensityOverride = mSrcDensityOverride;
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.DrawableWrapper);
        updateStateFromTypedArray(a);
        a.recycle();
        inflateChildDrawable(r, parser, attrs, theme);
    }

    @java.lang.Override
    public void applyTheme(@android.annotation.NonNull
    android.content.res.Resources.Theme t) {
        super.applyTheme(t);
        // If we load the drawable later as part of updating from the typed
        // array, it will already be themed correctly. So, we can theme the
        // local drawable first.
        if ((mDrawable != null) && mDrawable.canApplyTheme()) {
            mDrawable.applyTheme(t);
        }
        final android.graphics.drawable.DrawableWrapper.DrawableWrapperState state = mState;
        if (state == null) {
            return;
        }
        final int densityDpi = t.getResources().getDisplayMetrics().densityDpi;
        final int density = (densityDpi == 0) ? android.util.DisplayMetrics.DENSITY_DEFAULT : densityDpi;
        state.setDensity(density);
        if (state.mThemeAttrs != null) {
            final android.content.res.TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.DrawableWrapper);
            updateStateFromTypedArray(a);
            a.recycle();
        }
    }

    /**
     * Updates constant state properties from the provided typed array.
     * <p>
     * Implementing subclasses should call through to the super method first.
     *
     * @param a
     * 		the typed array rom which properties should be read
     */
    private void updateStateFromTypedArray(@android.annotation.NonNull
    android.content.res.TypedArray a) {
        final android.graphics.drawable.DrawableWrapper.DrawableWrapperState state = mState;
        if (state == null) {
            return;
        }
        // Account for any configuration changes.
        state.mChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        state.mThemeAttrs = a.extractThemeAttrs();
        if (a.hasValueOrEmpty(R.styleable.DrawableWrapper_drawable)) {
            setDrawable(a.getDrawable(R.styleable.DrawableWrapper_drawable));
        }
    }

    @java.lang.Override
    public boolean canApplyTheme() {
        return ((mState != null) && mState.canApplyTheme()) || super.canApplyTheme();
    }

    @java.lang.Override
    public void invalidateDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who) {
        final android.graphics.drawable.Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @java.lang.Override
    public void scheduleDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who, @android.annotation.NonNull
    java.lang.Runnable what, long when) {
        final android.graphics.drawable.Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    @java.lang.Override
    public void unscheduleDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who, @android.annotation.NonNull
    java.lang.Runnable what) {
        final android.graphics.drawable.Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    @java.lang.Override
    public void draw(@android.annotation.NonNull
    android.graphics.Canvas canvas) {
        if (mDrawable != null) {
            mDrawable.draw(canvas);
        }
    }

    @java.lang.Override
    @android.content.pm.ActivityInfo.Config
    public int getChangingConfigurations() {
        return (super.getChangingConfigurations() | (mState != null ? mState.getChangingConfigurations() : 0)) | mDrawable.getChangingConfigurations();
    }

    @java.lang.Override
    public boolean getPadding(@android.annotation.NonNull
    android.graphics.Rect padding) {
        return (mDrawable != null) && mDrawable.getPadding(padding);
    }

    @java.lang.Override
    public android.graphics.Insets getOpticalInsets() {
        return mDrawable != null ? mDrawable.getOpticalInsets() : android.graphics.Insets.NONE;
    }

    @java.lang.Override
    public void setHotspot(float x, float y) {
        if (mDrawable != null) {
            mDrawable.setHotspot(x, y);
        }
    }

    @java.lang.Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        if (mDrawable != null) {
            mDrawable.setHotspotBounds(left, top, right, bottom);
        }
    }

    @java.lang.Override
    public void getHotspotBounds(@android.annotation.NonNull
    android.graphics.Rect outRect) {
        if (mDrawable != null) {
            mDrawable.getHotspotBounds(outRect);
        } else {
            outRect.set(getBounds());
        }
    }

    @java.lang.Override
    public boolean setVisible(boolean visible, boolean restart) {
        final boolean superChanged = super.setVisible(visible, restart);
        final boolean changed = (mDrawable != null) && mDrawable.setVisible(visible, restart);
        return superChanged | changed;
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        if (mDrawable != null) {
            mDrawable.setAlpha(alpha);
        }
    }

    @java.lang.Override
    public int getAlpha() {
        return mDrawable != null ? mDrawable.getAlpha() : 255;
    }

    @java.lang.Override
    public void setColorFilter(@android.annotation.Nullable
    android.graphics.ColorFilter colorFilter) {
        if (mDrawable != null) {
            mDrawable.setColorFilter(colorFilter);
        }
    }

    @java.lang.Override
    public android.graphics.ColorFilter getColorFilter() {
        final android.graphics.drawable.Drawable drawable = getDrawable();
        if (drawable != null) {
            return drawable.getColorFilter();
        }
        return super.getColorFilter();
    }

    @java.lang.Override
    public void setTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        if (mDrawable != null) {
            mDrawable.setTintList(tint);
        }
    }

    @java.lang.Override
    public void setTintBlendMode(@android.annotation.NonNull
    android.graphics.BlendMode blendMode) {
        if (mDrawable != null) {
            mDrawable.setTintBlendMode(blendMode);
        }
    }

    @java.lang.Override
    public boolean onLayoutDirectionChanged(@android.view.View.ResolvedLayoutDir
    int layoutDirection) {
        return (mDrawable != null) && mDrawable.setLayoutDirection(layoutDirection);
    }

    @java.lang.Override
    public int getOpacity() {
        return mDrawable != null ? mDrawable.getOpacity() : android.graphics.PixelFormat.TRANSPARENT;
    }

    @java.lang.Override
    public boolean isStateful() {
        return (mDrawable != null) && mDrawable.isStateful();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean hasFocusStateSpecified() {
        return (mDrawable != null) && mDrawable.hasFocusStateSpecified();
    }

    @java.lang.Override
    protected boolean onStateChange(int[] state) {
        if ((mDrawable != null) && mDrawable.isStateful()) {
            final boolean changed = mDrawable.setState(state);
            if (changed) {
                onBoundsChange(getBounds());
            }
            return changed;
        }
        return false;
    }

    @java.lang.Override
    protected boolean onLevelChange(int level) {
        return (mDrawable != null) && mDrawable.setLevel(level);
    }

    @java.lang.Override
    protected void onBoundsChange(@android.annotation.NonNull
    android.graphics.Rect bounds) {
        if (mDrawable != null) {
            mDrawable.setBounds(bounds);
        }
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        return mDrawable != null ? mDrawable.getIntrinsicWidth() : -1;
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        return mDrawable != null ? mDrawable.getIntrinsicHeight() : -1;
    }

    @java.lang.Override
    public void getOutline(@android.annotation.NonNull
    android.graphics.Outline outline) {
        if (mDrawable != null) {
            mDrawable.getOutline(outline);
        } else {
            super.getOutline(outline);
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable.ConstantState getConstantState() {
        if ((mState != null) && mState.canConstantState()) {
            mState.mChangingConfigurations = getChangingConfigurations();
            return mState;
        }
        return null;
    }

    @java.lang.Override
    @android.annotation.NonNull
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
     * Mutates the constant state and returns the new state. Responsible for
     * updating any local copy.
     * <p>
     * This method should never call the super implementation; it should always
     * mutate and return its own constant state.
     *
     * @return the new state
     */
    android.graphics.drawable.DrawableWrapper.DrawableWrapperState mutateConstantState() {
        return mState;
    }

    /**
     *
     *
     * @unknown Only used by the framework for pre-loading resources.
     */
    public void clearMutated() {
        super.clearMutated();
        if (mDrawable != null) {
            mDrawable.clearMutated();
        }
        mMutated = false;
    }

    /**
     * Called during inflation to inflate the child element. The last valid
     * child element will take precedence over any other child elements or
     * explicit drawable attribute.
     */
    private void inflateChildDrawable(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        // Seek to the first child element.
        android.graphics.drawable.Drawable dr = null;
        int type;
        final int outerDepth = parser.getDepth();
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if (type == org.xmlpull.v1.XmlPullParser.START_TAG) {
                dr = android.graphics.drawable.Drawable.createFromXmlInnerForDensity(r, parser, attrs, mState.mSrcDensityOverride, theme);
            }
        } 
        if (dr != null) {
            setDrawable(dr);
        }
    }

    static abstract class DrawableWrapperState extends android.graphics.drawable.Drawable.ConstantState {
        private int[] mThemeAttrs;

        @android.content.pm.ActivityInfo.Config
        int mChangingConfigurations;

        int mDensity = android.util.DisplayMetrics.DENSITY_DEFAULT;

        /**
         * The density to use when looking up resources from
         * {@link Resources#getDrawableForDensity(int, int, Theme)}.
         * A value of 0 means there is no override and the system density will be used.
         *
         * @unknown 
         */
        int mSrcDensityOverride = 0;

        android.graphics.drawable.Drawable.ConstantState mDrawableState;

        DrawableWrapperState(@android.annotation.Nullable
        android.graphics.drawable.DrawableWrapper.DrawableWrapperState orig, @android.annotation.Nullable
        android.content.res.Resources res) {
            if (orig != null) {
                mThemeAttrs = orig.mThemeAttrs;
                mChangingConfigurations = orig.mChangingConfigurations;
                mDrawableState = orig.mDrawableState;
                mSrcDensityOverride = orig.mSrcDensityOverride;
            }
            final int density;
            if (res != null) {
                density = res.getDisplayMetrics().densityDpi;
            } else
                if (orig != null) {
                    density = orig.mDensity;
                } else {
                    density = 0;
                }

            mDensity = (density == 0) ? android.util.DisplayMetrics.DENSITY_DEFAULT : density;
        }

        /**
         * Sets the constant state density.
         * <p>
         * If the density has been previously set, dispatches the change to
         * subclasses so that density-dependent properties may be scaled as
         * necessary.
         *
         * @param targetDensity
         * 		the new constant state density
         */
        public final void setDensity(int targetDensity) {
            if (mDensity != targetDensity) {
                final int sourceDensity = mDensity;
                mDensity = targetDensity;
                onDensityChanged(sourceDensity, targetDensity);
            }
        }

        /**
         * Called when the constant state density changes.
         * <p>
         * Subclasses with density-dependent constant state properties should
         * override this method and scale their properties as necessary.
         *
         * @param sourceDensity
         * 		the previous constant state density
         * @param targetDensity
         * 		the new constant state density
         */
        void onDensityChanged(int sourceDensity, int targetDensity) {
            // Stub method.
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return ((mThemeAttrs != null) || ((mDrawableState != null) && mDrawableState.canApplyTheme())) || super.canApplyTheme();
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return newDrawable(null);
        }

        @java.lang.Override
        public abstract android.graphics.drawable.Drawable newDrawable(@android.annotation.Nullable
        android.content.res.Resources res);

        @java.lang.Override
        @android.content.pm.ActivityInfo.Config
        public int getChangingConfigurations() {
            return mChangingConfigurations | (mDrawableState != null ? mDrawableState.getChangingConfigurations() : 0);
        }

        public boolean canConstantState() {
            return mDrawableState != null;
        }
    }
}

