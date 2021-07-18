/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * A resizeable bitmap, with stretchable areas that you define. This type of image
 * is defined in a .png file with a special format.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about how to use a NinePatchDrawable, read the
 * <a href="{@docRoot }guide/topics/graphics/2d-graphics.html#nine-patch">
 * Canvas and Drawables</a> developer guide. For information about creating a NinePatch image
 * file using the draw9patch tool, see the
 * <a href="{@docRoot }guide/developing/tools/draw9patch.html">Draw 9-patch</a> tool guide.</p></div>
 */
public class NinePatchDrawable extends android.graphics.drawable.Drawable {
    // dithering helps a lot, and is pretty cheap, so default is true
    private static final boolean DEFAULT_DITHER = false;

    /**
     * Temporary rect used for density scaling.
     */
    private android.graphics.Rect mTempRect;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.NinePatchDrawable.NinePatchState mNinePatchState;

    private android.graphics.BlendModeColorFilter mBlendModeFilter;

    private android.graphics.Rect mPadding;

    private android.graphics.Insets mOpticalInsets = android.graphics.Insets.NONE;

    private android.graphics.Rect mOutlineInsets;

    private float mOutlineRadius;

    private android.graphics.Paint mPaint;

    private boolean mMutated;

    private int mTargetDensity = android.util.DisplayMetrics.DENSITY_DEFAULT;

    // These are scaled to match the target density.
    private int mBitmapWidth = -1;

    private int mBitmapHeight = -1;

    NinePatchDrawable() {
        mNinePatchState = new android.graphics.drawable.NinePatchDrawable.NinePatchState();
    }

    /**
     * Create drawable from raw nine-patch data, not dealing with density.
     *
     * @deprecated Use {@link #NinePatchDrawable(Resources, Bitmap, byte[], Rect, String)}
    to ensure that the drawable has correctly set its target density.
     */
    @java.lang.Deprecated
    public NinePatchDrawable(android.graphics.Bitmap bitmap, byte[] chunk, android.graphics.Rect padding, java.lang.String srcName) {
        this(new android.graphics.drawable.NinePatchDrawable.NinePatchState(new android.graphics.NinePatch(bitmap, chunk, srcName), padding), null);
    }

    /**
     * Create drawable from raw nine-patch data, setting initial target density
     * based on the display metrics of the resources.
     */
    public NinePatchDrawable(android.content.res.Resources res, android.graphics.Bitmap bitmap, byte[] chunk, android.graphics.Rect padding, java.lang.String srcName) {
        this(new android.graphics.drawable.NinePatchDrawable.NinePatchState(new android.graphics.NinePatch(bitmap, chunk, srcName), padding), res);
    }

    /**
     * Create drawable from raw nine-patch data, setting initial target density
     * based on the display metrics of the resources.
     *
     * @unknown 
     */
    public NinePatchDrawable(android.content.res.Resources res, android.graphics.Bitmap bitmap, byte[] chunk, android.graphics.Rect padding, android.graphics.Rect opticalInsets, java.lang.String srcName) {
        this(new android.graphics.drawable.NinePatchDrawable.NinePatchState(new android.graphics.NinePatch(bitmap, chunk, srcName), padding, opticalInsets), res);
    }

    /**
     * Create drawable from existing nine-patch, not dealing with density.
     *
     * @deprecated Use {@link #NinePatchDrawable(Resources, NinePatch)}
    to ensure that the drawable has correctly set its target
    density.
     */
    @java.lang.Deprecated
    public NinePatchDrawable(@android.annotation.NonNull
    android.graphics.NinePatch patch) {
        this(new android.graphics.drawable.NinePatchDrawable.NinePatchState(patch, new android.graphics.Rect()), null);
    }

    /**
     * Create drawable from existing nine-patch, setting initial target density
     * based on the display metrics of the resources.
     */
    public NinePatchDrawable(@android.annotation.Nullable
    android.content.res.Resources res, @android.annotation.NonNull
    android.graphics.NinePatch patch) {
        this(new android.graphics.drawable.NinePatchDrawable.NinePatchState(patch, new android.graphics.Rect()), res);
    }

    /**
     * Set the density scale at which this drawable will be rendered. This
     * method assumes the drawable will be rendered at the same density as the
     * specified canvas.
     *
     * @param canvas
     * 		The Canvas from which the density scale must be obtained.
     * @see android.graphics.Bitmap#setDensity(int)
     * @see android.graphics.Bitmap#getDensity()
     */
    public void setTargetDensity(@android.annotation.NonNull
    android.graphics.Canvas canvas) {
        setTargetDensity(canvas.getDensity());
    }

    /**
     * Set the density scale at which this drawable will be rendered.
     *
     * @param metrics
     * 		The DisplayMetrics indicating the density scale for this drawable.
     * @see android.graphics.Bitmap#setDensity(int)
     * @see android.graphics.Bitmap#getDensity()
     */
    public void setTargetDensity(@android.annotation.NonNull
    android.util.DisplayMetrics metrics) {
        setTargetDensity(metrics.densityDpi);
    }

    /**
     * Set the density at which this drawable will be rendered.
     *
     * @param density
     * 		The density scale for this drawable.
     * @see android.graphics.Bitmap#setDensity(int)
     * @see android.graphics.Bitmap#getDensity()
     */
    public void setTargetDensity(int density) {
        if (density == 0) {
            density = android.util.DisplayMetrics.DENSITY_DEFAULT;
        }
        if (mTargetDensity != density) {
            mTargetDensity = density;
            computeBitmapSize();
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        final android.graphics.drawable.NinePatchDrawable.NinePatchState state = mNinePatchState;
        android.graphics.Rect bounds = getBounds();
        int restoreToCount = -1;
        final boolean clearColorFilter;
        if ((mBlendModeFilter != null) && (getPaint().getColorFilter() == null)) {
            mPaint.setColorFilter(mBlendModeFilter);
            clearColorFilter = true;
        } else {
            clearColorFilter = false;
        }
        final int restoreAlpha;
        if (state.mBaseAlpha != 1.0F) {
            restoreAlpha = getPaint().getAlpha();
            mPaint.setAlpha(((int) ((restoreAlpha * state.mBaseAlpha) + 0.5F)));
        } else {
            restoreAlpha = -1;
        }
        final boolean needsDensityScaling = (canvas.getDensity() == 0) && (android.graphics.Bitmap.DENSITY_NONE != state.mNinePatch.getDensity());
        if (needsDensityScaling) {
            restoreToCount = (restoreToCount >= 0) ? restoreToCount : canvas.save();
            // Apply density scaling.
            final float scale = mTargetDensity / ((float) (state.mNinePatch.getDensity()));
            final float px = bounds.left;
            final float py = bounds.top;
            canvas.scale(scale, scale, px, py);
            if (mTempRect == null) {
                mTempRect = new android.graphics.Rect();
            }
            // Scale the bounds to match.
            final android.graphics.Rect scaledBounds = mTempRect;
            scaledBounds.left = bounds.left;
            scaledBounds.top = bounds.top;
            scaledBounds.right = bounds.left + java.lang.Math.round(bounds.width() / scale);
            scaledBounds.bottom = bounds.top + java.lang.Math.round(bounds.height() / scale);
            bounds = scaledBounds;
        }
        final boolean needsMirroring = needsMirroring();
        if (needsMirroring) {
            restoreToCount = (restoreToCount >= 0) ? restoreToCount : canvas.save();
            // Mirror the 9patch.
            final float cx = (bounds.left + bounds.right) / 2.0F;
            final float cy = (bounds.top + bounds.bottom) / 2.0F;
            canvas.scale(-1.0F, 1.0F, cx, cy);
        }
        state.mNinePatch.draw(canvas, bounds, mPaint);
        if (restoreToCount >= 0) {
            canvas.restoreToCount(restoreToCount);
        }
        if (clearColorFilter) {
            mPaint.setColorFilter(null);
        }
        if (restoreAlpha >= 0) {
            mPaint.setAlpha(restoreAlpha);
        }
    }

    @java.lang.Override
    @android.content.pm.ActivityInfo.Config
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | mNinePatchState.getChangingConfigurations();
    }

    @java.lang.Override
    public boolean getPadding(@android.annotation.NonNull
    android.graphics.Rect padding) {
        if (mPadding != null) {
            padding.set(mPadding);
            return (((padding.left | padding.top) | padding.right) | padding.bottom) != 0;
        } else {
            return super.getPadding(padding);
        }
    }

    @java.lang.Override
    public void getOutline(@android.annotation.NonNull
    android.graphics.Outline outline) {
        final android.graphics.Rect bounds = getBounds();
        if (bounds.isEmpty()) {
            return;
        }
        if ((mNinePatchState != null) && (mOutlineInsets != null)) {
            final android.graphics.NinePatch.InsetStruct insets = mNinePatchState.mNinePatch.getBitmap().getNinePatchInsets();
            if (insets != null) {
                outline.setRoundRect(bounds.left + mOutlineInsets.left, bounds.top + mOutlineInsets.top, bounds.right - mOutlineInsets.right, bounds.bottom - mOutlineInsets.bottom, mOutlineRadius);
                outline.setAlpha(insets.outlineAlpha * (getAlpha() / 255.0F));
                return;
            }
        }
        super.getOutline(outline);
    }

    @java.lang.Override
    public android.graphics.Insets getOpticalInsets() {
        final android.graphics.Insets opticalInsets = mOpticalInsets;
        if (needsMirroring()) {
            return android.graphics.Insets.of(opticalInsets.right, opticalInsets.top, opticalInsets.left, opticalInsets.bottom);
        } else {
            return opticalInsets;
        }
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        if ((mPaint == null) && (alpha == 0xff)) {
            // Fast common case -- leave at normal alpha.
            return;
        }
        getPaint().setAlpha(alpha);
        invalidateSelf();
    }

    @java.lang.Override
    public int getAlpha() {
        if (mPaint == null) {
            // Fast common case -- normal alpha.
            return 0xff;
        }
        return getPaint().getAlpha();
    }

    @java.lang.Override
    public void setColorFilter(@android.annotation.Nullable
    android.graphics.ColorFilter colorFilter) {
        if ((mPaint == null) && (colorFilter == null)) {
            // Fast common case -- leave at no color filter.
            return;
        }
        getPaint().setColorFilter(colorFilter);
        invalidateSelf();
    }

    @java.lang.Override
    public void setTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        mNinePatchState.mTint = tint;
        mBlendModeFilter = updateBlendModeFilter(mBlendModeFilter, tint, mNinePatchState.mBlendMode);
        invalidateSelf();
    }

    @java.lang.Override
    public void setTintBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode blendMode) {
        mNinePatchState.mBlendMode = blendMode;
        mBlendModeFilter = updateBlendModeFilter(mBlendModeFilter, mNinePatchState.mTint, blendMode);
        invalidateSelf();
    }

    @java.lang.Override
    public void setDither(boolean dither) {
        // noinspection PointlessBooleanExpression
        if ((mPaint == null) && (dither == android.graphics.drawable.NinePatchDrawable.DEFAULT_DITHER)) {
            // Fast common case -- leave at default dither.
            return;
        }
        getPaint().setDither(dither);
        invalidateSelf();
    }

    @java.lang.Override
    public void setAutoMirrored(boolean mirrored) {
        mNinePatchState.mAutoMirrored = mirrored;
    }

    private boolean needsMirroring() {
        return isAutoMirrored() && (getLayoutDirection() == android.util.LayoutDirection.RTL);
    }

    @java.lang.Override
    public boolean isAutoMirrored() {
        return mNinePatchState.mAutoMirrored;
    }

    @java.lang.Override
    public void setFilterBitmap(boolean filter) {
        getPaint().setFilterBitmap(filter);
        invalidateSelf();
    }

    @java.lang.Override
    public boolean isFilterBitmap() {
        return (mPaint != null) && getPaint().isFilterBitmap();
    }

    @java.lang.Override
    public void inflate(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        super.inflate(r, parser, attrs, theme);
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.NinePatchDrawable);
        updateStateFromTypedArray(a);
        a.recycle();
        updateLocalState(r);
    }

    /**
     * Updates the constant state from the values in the typed array.
     */
    private void updateStateFromTypedArray(@android.annotation.NonNull
    android.content.res.TypedArray a) throws org.xmlpull.v1.XmlPullParserException {
        final android.content.res.Resources r = a.getResources();
        final android.graphics.drawable.NinePatchDrawable.NinePatchState state = mNinePatchState;
        // Account for any configuration changes.
        state.mChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mDither = a.getBoolean(R.styleable.NinePatchDrawable_dither, state.mDither);
        final int srcResId = a.getResourceId(R.styleable.NinePatchDrawable_src, 0);
        if (srcResId != 0) {
            final android.graphics.Rect padding = new android.graphics.Rect();
            final android.graphics.Rect opticalInsets = new android.graphics.Rect();
            android.graphics.Bitmap bitmap = null;
            try {
                final android.util.TypedValue value = new android.util.TypedValue();
                final java.io.InputStream is = r.openRawResource(srcResId, value);
                int density = android.graphics.Bitmap.DENSITY_NONE;
                if (value.density == android.util.TypedValue.DENSITY_DEFAULT) {
                    density = android.util.DisplayMetrics.DENSITY_DEFAULT;
                } else
                    if (value.density != android.util.TypedValue.DENSITY_NONE) {
                        density = value.density;
                    }

                android.graphics.ImageDecoder.Source source = android.graphics.ImageDecoder.createSource(r, is, density);
                bitmap = android.graphics.ImageDecoder.decodeBitmap(source, ( decoder, info, src) -> {
                    decoder.setOutPaddingRect(padding);
                    decoder.setAllocator(android.graphics.ImageDecoder.ALLOCATOR_SOFTWARE);
                });
                is.close();
            } catch (java.io.IOException e) {
                // Ignore
            }
            if (bitmap == null) {
                throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + ": <nine-patch> requires a valid src attribute");
            } else
                if (bitmap.getNinePatchChunk() == null) {
                    throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + ": <nine-patch> requires a valid 9-patch source image");
                }

            bitmap.getOpticalInsets(opticalInsets);
            state.mNinePatch = new android.graphics.NinePatch(bitmap, bitmap.getNinePatchChunk());
            state.mPadding = padding;
            state.mOpticalInsets = android.graphics.Insets.of(opticalInsets);
        }
        state.mAutoMirrored = a.getBoolean(R.styleable.NinePatchDrawable_autoMirrored, state.mAutoMirrored);
        state.mBaseAlpha = a.getFloat(R.styleable.NinePatchDrawable_alpha, state.mBaseAlpha);
        final int tintMode = a.getInt(R.styleable.NinePatchDrawable_tintMode, -1);
        if (tintMode != (-1)) {
            state.mBlendMode = android.graphics.drawable.Drawable.parseBlendMode(tintMode, android.graphics.BlendMode.SRC_IN);
        }
        final android.content.res.ColorStateList tint = a.getColorStateList(R.styleable.NinePatchDrawable_tint);
        if (tint != null) {
            state.mTint = tint;
        }
    }

    @java.lang.Override
    public void applyTheme(@android.annotation.NonNull
    android.content.res.Resources.Theme t) {
        super.applyTheme(t);
        final android.graphics.drawable.NinePatchDrawable.NinePatchState state = mNinePatchState;
        if (state == null) {
            return;
        }
        if (state.mThemeAttrs != null) {
            final android.content.res.TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.NinePatchDrawable);
            try {
                updateStateFromTypedArray(a);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                android.graphics.drawable.Drawable.rethrowAsRuntimeException(e);
            } finally {
                a.recycle();
            }
        }
        if ((state.mTint != null) && state.mTint.canApplyTheme()) {
            state.mTint = state.mTint.obtainForTheme(t);
        }
        updateLocalState(t.getResources());
    }

    @java.lang.Override
    public boolean canApplyTheme() {
        return (mNinePatchState != null) && mNinePatchState.canApplyTheme();
    }

    @android.annotation.NonNull
    public android.graphics.Paint getPaint() {
        if (mPaint == null) {
            mPaint = new android.graphics.Paint();
            mPaint.setDither(android.graphics.drawable.NinePatchDrawable.DEFAULT_DITHER);
        }
        return mPaint;
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    @java.lang.Override
    public int getOpacity() {
        return mNinePatchState.mNinePatch.hasAlpha() || ((mPaint != null) && (mPaint.getAlpha() < 255)) ? android.graphics.PixelFormat.TRANSLUCENT : android.graphics.PixelFormat.OPAQUE;
    }

    @java.lang.Override
    public android.graphics.Region getTransparentRegion() {
        return mNinePatchState.mNinePatch.getTransparentRegion(getBounds());
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable.ConstantState getConstantState() {
        mNinePatchState.mChangingConfigurations = getChangingConfigurations();
        return mNinePatchState;
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable mutate() {
        if ((!mMutated) && (super.mutate() == this)) {
            mNinePatchState = new android.graphics.drawable.NinePatchDrawable.NinePatchState(mNinePatchState);
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
    protected boolean onStateChange(int[] stateSet) {
        final android.graphics.drawable.NinePatchDrawable.NinePatchState state = mNinePatchState;
        if ((state.mTint != null) && (state.mBlendMode != null)) {
            mBlendModeFilter = updateBlendModeFilter(mBlendModeFilter, state.mTint, state.mBlendMode);
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean isStateful() {
        final android.graphics.drawable.NinePatchDrawable.NinePatchState s = mNinePatchState;
        return super.isStateful() || ((s.mTint != null) && s.mTint.isStateful());
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean hasFocusStateSpecified() {
        return (mNinePatchState.mTint != null) && mNinePatchState.mTint.hasFocusStateSpecified();
    }

    static final class NinePatchState extends android.graphics.drawable.Drawable.ConstantState {
        @android.content.pm.ActivityInfo.Config
        int mChangingConfigurations;

        // Values loaded during inflation.
        @android.annotation.UnsupportedAppUsage
        android.graphics.NinePatch mNinePatch = null;

        android.content.res.ColorStateList mTint = null;

        android.graphics.BlendMode mBlendMode = android.graphics.drawable.Drawable.DEFAULT_BLEND_MODE;

        android.graphics.Rect mPadding = null;

        android.graphics.Insets mOpticalInsets = android.graphics.Insets.NONE;

        float mBaseAlpha = 1.0F;

        boolean mDither = android.graphics.drawable.NinePatchDrawable.DEFAULT_DITHER;

        boolean mAutoMirrored = false;

        int[] mThemeAttrs;

        NinePatchState() {
            // Empty constructor.
        }

        NinePatchState(@android.annotation.NonNull
        android.graphics.NinePatch ninePatch, @android.annotation.Nullable
        android.graphics.Rect padding) {
            this(ninePatch, padding, null, android.graphics.drawable.NinePatchDrawable.DEFAULT_DITHER, false);
        }

        NinePatchState(@android.annotation.NonNull
        android.graphics.NinePatch ninePatch, @android.annotation.Nullable
        android.graphics.Rect padding, @android.annotation.Nullable
        android.graphics.Rect opticalInsets) {
            this(ninePatch, padding, opticalInsets, android.graphics.drawable.NinePatchDrawable.DEFAULT_DITHER, false);
        }

        NinePatchState(@android.annotation.NonNull
        android.graphics.NinePatch ninePatch, @android.annotation.Nullable
        android.graphics.Rect padding, @android.annotation.Nullable
        android.graphics.Rect opticalInsets, boolean dither, boolean autoMirror) {
            mNinePatch = ninePatch;
            mPadding = padding;
            mOpticalInsets = android.graphics.Insets.of(opticalInsets);
            mDither = dither;
            mAutoMirrored = autoMirror;
        }

        NinePatchState(@android.annotation.NonNull
        android.graphics.drawable.NinePatchDrawable.NinePatchState orig) {
            mChangingConfigurations = orig.mChangingConfigurations;
            mNinePatch = orig.mNinePatch;
            mTint = orig.mTint;
            mBlendMode = orig.mBlendMode;
            mPadding = orig.mPadding;
            mOpticalInsets = orig.mOpticalInsets;
            mBaseAlpha = orig.mBaseAlpha;
            mDither = orig.mDither;
            mAutoMirrored = orig.mAutoMirrored;
            mThemeAttrs = orig.mThemeAttrs;
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return ((mThemeAttrs != null) || ((mTint != null) && mTint.canApplyTheme())) || super.canApplyTheme();
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.graphics.drawable.NinePatchDrawable(this, null);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.NinePatchDrawable(this, res);
        }

        @java.lang.Override
        @android.content.pm.ActivityInfo.Config
        public int getChangingConfigurations() {
            return mChangingConfigurations | (mTint != null ? mTint.getChangingConfigurations() : 0);
        }
    }

    private void computeBitmapSize() {
        final android.graphics.NinePatch ninePatch = mNinePatchState.mNinePatch;
        if (ninePatch == null) {
            return;
        }
        final int targetDensity = mTargetDensity;
        final int sourceDensity = (ninePatch.getDensity() == android.graphics.Bitmap.DENSITY_NONE) ? targetDensity : ninePatch.getDensity();
        final android.graphics.Insets sourceOpticalInsets = mNinePatchState.mOpticalInsets;
        if (sourceOpticalInsets != android.graphics.Insets.NONE) {
            final int left = android.graphics.drawable.Drawable.scaleFromDensity(sourceOpticalInsets.left, sourceDensity, targetDensity, true);
            final int top = android.graphics.drawable.Drawable.scaleFromDensity(sourceOpticalInsets.top, sourceDensity, targetDensity, true);
            final int right = android.graphics.drawable.Drawable.scaleFromDensity(sourceOpticalInsets.right, sourceDensity, targetDensity, true);
            final int bottom = android.graphics.drawable.Drawable.scaleFromDensity(sourceOpticalInsets.bottom, sourceDensity, targetDensity, true);
            mOpticalInsets = android.graphics.Insets.of(left, top, right, bottom);
        } else {
            mOpticalInsets = android.graphics.Insets.NONE;
        }
        final android.graphics.Rect sourcePadding = mNinePatchState.mPadding;
        if (sourcePadding != null) {
            if (mPadding == null) {
                mPadding = new android.graphics.Rect();
            }
            mPadding.left = android.graphics.drawable.Drawable.scaleFromDensity(sourcePadding.left, sourceDensity, targetDensity, true);
            mPadding.top = android.graphics.drawable.Drawable.scaleFromDensity(sourcePadding.top, sourceDensity, targetDensity, true);
            mPadding.right = android.graphics.drawable.Drawable.scaleFromDensity(sourcePadding.right, sourceDensity, targetDensity, true);
            mPadding.bottom = android.graphics.drawable.Drawable.scaleFromDensity(sourcePadding.bottom, sourceDensity, targetDensity, true);
        } else {
            mPadding = null;
        }
        mBitmapHeight = android.graphics.drawable.Drawable.scaleFromDensity(ninePatch.getHeight(), sourceDensity, targetDensity, true);
        mBitmapWidth = android.graphics.drawable.Drawable.scaleFromDensity(ninePatch.getWidth(), sourceDensity, targetDensity, true);
        final android.graphics.NinePatch.InsetStruct insets = ninePatch.getBitmap().getNinePatchInsets();
        if (insets != null) {
            android.graphics.Rect outlineRect = insets.outlineRect;
            mOutlineInsets = android.graphics.NinePatch.InsetStruct.scaleInsets(outlineRect.left, outlineRect.top, outlineRect.right, outlineRect.bottom, targetDensity / ((float) (sourceDensity)));
            mOutlineRadius = android.graphics.drawable.Drawable.scaleFromDensity(insets.outlineRadius, sourceDensity, targetDensity);
        } else {
            mOutlineInsets = null;
        }
    }

    /**
     * The one constructor to rule them all. This is called by all public
     * constructors to set the state and initialize local properties.
     *
     * @param state
     * 		constant state to assign to the new drawable
     */
    private NinePatchDrawable(@android.annotation.NonNull
    android.graphics.drawable.NinePatchDrawable.NinePatchState state, @android.annotation.Nullable
    android.content.res.Resources res) {
        mNinePatchState = state;
        updateLocalState(res);
    }

    /**
     * Initializes local dynamic properties from state.
     */
    private void updateLocalState(@android.annotation.Nullable
    android.content.res.Resources res) {
        final android.graphics.drawable.NinePatchDrawable.NinePatchState state = mNinePatchState;
        // If we can, avoid calling any methods that initialize Paint.
        if (state.mDither != android.graphics.drawable.NinePatchDrawable.DEFAULT_DITHER) {
            setDither(state.mDither);
        }
        // The nine-patch may have been created without a Resources object, in
        // which case we should try to match the density of the nine patch (if
        // available).
        if ((res == null) && (state.mNinePatch != null)) {
            mTargetDensity = state.mNinePatch.getDensity();
        } else {
            mTargetDensity = android.graphics.drawable.Drawable.resolveDensity(res, mTargetDensity);
        }
        mBlendModeFilter = updateBlendModeFilter(mBlendModeFilter, state.mTint, state.mBlendMode);
        computeBitmapSize();
    }
}

