/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * A specialized Drawable that fills the Canvas with a specified color.
 * Note that a ColorDrawable ignores the ColorFilter.
 *
 * <p>It can be defined in an XML file with the <code>&lt;color></code> element.</p>
 *
 * @unknown ref android.R.styleable#ColorDrawable_color
 */
public class ColorDrawable extends android.graphics.drawable.Drawable {
    @android.annotation.UnsupportedAppUsage
    private final android.graphics.Paint mPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);

    @android.view.ViewDebug.ExportedProperty(deepExport = true, prefix = "state_")
    private android.graphics.drawable.ColorDrawable.ColorState mColorState;

    private android.graphics.BlendModeColorFilter mBlendModeColorFilter;

    private boolean mMutated;

    /**
     * Creates a new black ColorDrawable.
     */
    public ColorDrawable() {
        mColorState = new android.graphics.drawable.ColorDrawable.ColorState();
    }

    /**
     * Creates a new ColorDrawable with the specified color.
     *
     * @param color
     * 		The color to draw.
     */
    public ColorDrawable(@android.annotation.ColorInt
    int color) {
        mColorState = new android.graphics.drawable.ColorDrawable.ColorState();
        setColor(color);
    }

    @java.lang.Override
    @android.content.pm.ActivityInfo.Config
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | mColorState.getChangingConfigurations();
    }

    /**
     * A mutable BitmapDrawable still shares its Bitmap with any other Drawable
     * that comes from the same resource.
     *
     * @return This drawable.
     */
    @java.lang.Override
    public android.graphics.drawable.Drawable mutate() {
        if ((!mMutated) && (super.mutate() == this)) {
            mColorState = new android.graphics.drawable.ColorDrawable.ColorState(mColorState);
            mMutated = true;
        }
        return this;
    }

    /**
     *
     *
     * @unknown 
     */
    public void clearMutated() {
        super.clearMutated();
        mMutated = false;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        final android.graphics.ColorFilter colorFilter = mPaint.getColorFilter();
        if ((((mColorState.mUseColor >>> 24) != 0) || (colorFilter != null)) || (mBlendModeColorFilter != null)) {
            if (colorFilter == null) {
                mPaint.setColorFilter(mBlendModeColorFilter);
            }
            mPaint.setColor(mColorState.mUseColor);
            canvas.drawRect(getBounds(), mPaint);
            // Restore original color filter.
            mPaint.setColorFilter(colorFilter);
        }
    }

    /**
     * Gets the drawable's color value.
     *
     * @return int The color to draw.
     */
    @android.annotation.ColorInt
    public int getColor() {
        return mColorState.mUseColor;
    }

    /**
     * Sets the drawable's color value. This action will clobber the results of
     * prior calls to {@link #setAlpha(int)} on this object, which side-affected
     * the underlying color.
     *
     * @param color
     * 		The color to draw.
     */
    public void setColor(@android.annotation.ColorInt
    int color) {
        if ((mColorState.mBaseColor != color) || (mColorState.mUseColor != color)) {
            mColorState.mBaseColor = mColorState.mUseColor = color;
            invalidateSelf();
        }
    }

    /**
     * Returns the alpha value of this drawable's color. Note this may not be the same alpha value
     * provided in {@link Drawable#setAlpha(int)}. Instead this will return the alpha of the color
     * combined with the alpha provided by setAlpha
     *
     * @return A value between 0 and 255.
     * @see ColorDrawable#setAlpha(int)
     */
    @java.lang.Override
    public int getAlpha() {
        return mColorState.mUseColor >>> 24;
    }

    /**
     * Applies the given alpha to the underlying color. Note if the color already has
     * an alpha applied to it, this will apply this alpha to the existing value instead of
     * overwriting it.
     *
     * @param alpha
     * 		The alpha value to set, between 0 and 255.
     */
    @java.lang.Override
    public void setAlpha(int alpha) {
        alpha += alpha >> 7;// make it 0..256

        final int baseAlpha = mColorState.mBaseColor >>> 24;
        final int useAlpha = (baseAlpha * alpha) >> 8;
        final int useColor = ((mColorState.mBaseColor << 8) >>> 8) | (useAlpha << 24);
        if (mColorState.mUseColor != useColor) {
            mColorState.mUseColor = useColor;
            invalidateSelf();
        }
    }

    /**
     * Sets the color filter applied to this color.
     * <p>
     * Only supported on version {@link android.os.Build.VERSION_CODES#LOLLIPOP} and
     * above. Calling this method has no effect on earlier versions.
     *
     * @see android.graphics.drawable.Drawable#setColorFilter(ColorFilter)
     */
    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    /**
     * Returns the color filter applied to this color configured by
     * {@link #setColorFilter(ColorFilter)}
     *
     * @see android.graphics.drawable.Drawable#getColorFilter()
     */
    @java.lang.Override
    @android.annotation.Nullable
    public android.graphics.ColorFilter getColorFilter() {
        return mPaint.getColorFilter();
    }

    @java.lang.Override
    public void setTintList(android.content.res.ColorStateList tint) {
        mColorState.mTint = tint;
        mBlendModeColorFilter = updateBlendModeFilter(mBlendModeColorFilter, tint, mColorState.mBlendMode);
        invalidateSelf();
    }

    @java.lang.Override
    public void setTintBlendMode(@android.annotation.NonNull
    android.graphics.BlendMode blendMode) {
        mColorState.mBlendMode = blendMode;
        mBlendModeColorFilter = updateBlendModeFilter(mBlendModeColorFilter, mColorState.mTint, blendMode);
        invalidateSelf();
    }

    @java.lang.Override
    protected boolean onStateChange(int[] stateSet) {
        final android.graphics.drawable.ColorDrawable.ColorState state = mColorState;
        if ((state.mTint != null) && (state.mBlendMode != null)) {
            mBlendModeColorFilter = updateBlendModeFilter(mBlendModeColorFilter, state.mTint, state.mBlendMode);
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean isStateful() {
        return (mColorState.mTint != null) && mColorState.mTint.isStateful();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean hasFocusStateSpecified() {
        return (mColorState.mTint != null) && mColorState.mTint.hasFocusStateSpecified();
    }

    /**
     *
     *
     * @unknown 
     * @param mode
     * 		new transfer mode
     */
    @java.lang.Override
    public void setXfermode(@android.annotation.Nullable
    android.graphics.Xfermode mode) {
        mPaint.setXfermode(mode);
        invalidateSelf();
    }

    /**
     *
     *
     * @unknown 
     * @return current transfer mode
     */
    @android.annotation.TestApi
    public android.graphics.Xfermode getXfermode() {
        return mPaint.getXfermode();
    }

    @java.lang.Override
    public int getOpacity() {
        if ((mBlendModeColorFilter != null) || (mPaint.getColorFilter() != null)) {
            return android.graphics.PixelFormat.TRANSLUCENT;
        }
        switch (mColorState.mUseColor >>> 24) {
            case 255 :
                return android.graphics.PixelFormat.OPAQUE;
            case 0 :
                return android.graphics.PixelFormat.TRANSPARENT;
        }
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    @java.lang.Override
    public void getOutline(@android.annotation.NonNull
    android.graphics.Outline outline) {
        outline.setRect(getBounds());
        outline.setAlpha(getAlpha() / 255.0F);
    }

    @java.lang.Override
    public void inflate(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        super.inflate(r, parser, attrs, theme);
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.ColorDrawable);
        updateStateFromTypedArray(a);
        a.recycle();
        updateLocalState(r);
    }

    /**
     * Updates the constant state from the values in the typed array.
     */
    private void updateStateFromTypedArray(android.content.res.TypedArray a) {
        final android.graphics.drawable.ColorDrawable.ColorState state = mColorState;
        // Account for any configuration changes.
        state.mChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mBaseColor = a.getColor(R.styleable.ColorDrawable_color, state.mBaseColor);
        state.mUseColor = state.mBaseColor;
    }

    @java.lang.Override
    public boolean canApplyTheme() {
        return mColorState.canApplyTheme() || super.canApplyTheme();
    }

    @java.lang.Override
    public void applyTheme(android.content.res.Resources.Theme t) {
        super.applyTheme(t);
        final android.graphics.drawable.ColorDrawable.ColorState state = mColorState;
        if (state == null) {
            return;
        }
        if (state.mThemeAttrs != null) {
            final android.content.res.TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.ColorDrawable);
            updateStateFromTypedArray(a);
            a.recycle();
        }
        if ((state.mTint != null) && state.mTint.canApplyTheme()) {
            state.mTint = state.mTint.obtainForTheme(t);
        }
        updateLocalState(t.getResources());
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable.ConstantState getConstantState() {
        return mColorState;
    }

    static final class ColorState extends android.graphics.drawable.Drawable.ConstantState {
        int[] mThemeAttrs;

        int mBaseColor;// base color, independent of setAlpha()


        @android.view.ViewDebug.ExportedProperty
        @android.annotation.UnsupportedAppUsage
        int mUseColor;// basecolor modulated by setAlpha()


        @android.content.pm.ActivityInfo.Config
        int mChangingConfigurations;

        android.content.res.ColorStateList mTint = null;

        android.graphics.BlendMode mBlendMode = android.graphics.drawable.Drawable.DEFAULT_BLEND_MODE;

        ColorState() {
            // Empty constructor.
        }

        ColorState(android.graphics.drawable.ColorDrawable.ColorState state) {
            mThemeAttrs = state.mThemeAttrs;
            mBaseColor = state.mBaseColor;
            mUseColor = state.mUseColor;
            mChangingConfigurations = state.mChangingConfigurations;
            mTint = state.mTint;
            mBlendMode = state.mBlendMode;
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return (mThemeAttrs != null) || ((mTint != null) && mTint.canApplyTheme());
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.graphics.drawable.ColorDrawable(this, null);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.ColorDrawable(this, res);
        }

        @java.lang.Override
        @android.content.pm.ActivityInfo.Config
        public int getChangingConfigurations() {
            return mChangingConfigurations | (mTint != null ? mTint.getChangingConfigurations() : 0);
        }
    }

    private ColorDrawable(android.graphics.drawable.ColorDrawable.ColorState state, android.content.res.Resources res) {
        mColorState = state;
        updateLocalState(res);
    }

    /**
     * Initializes local dynamic properties from state. This should be called
     * after significant state changes, e.g. from the One True Constructor and
     * after inflating or applying a theme.
     */
    private void updateLocalState(android.content.res.Resources r) {
        mBlendModeColorFilter = updateBlendModeFilter(mBlendModeColorFilter, mColorState.mTint, mColorState.mBlendMode);
    }
}

