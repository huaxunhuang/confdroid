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
 * A Drawable that wraps a bitmap and can be tiled, stretched, or aligned. You can create a
 * BitmapDrawable from a file path, an input stream, through XML inflation, or from
 * a {@link android.graphics.Bitmap} object.
 * <p>It can be defined in an XML file with the <code>&lt;bitmap></code> element.  For more
 * information, see the guide to <a
 * href="{@docRoot }guide/topics/resources/drawable-resource.html">Drawable Resources</a>.</p>
 * <p>
 * Also see the {@link android.graphics.Bitmap} class, which handles the management and
 * transformation of raw bitmap graphics, and should be used when drawing to a
 * {@link android.graphics.Canvas}.
 * </p>
 *
 * @unknown ref android.R.styleable#BitmapDrawable_src
 * @unknown ref android.R.styleable#BitmapDrawable_antialias
 * @unknown ref android.R.styleable#BitmapDrawable_filter
 * @unknown ref android.R.styleable#BitmapDrawable_dither
 * @unknown ref android.R.styleable#BitmapDrawable_gravity
 * @unknown ref android.R.styleable#BitmapDrawable_mipMap
 * @unknown ref android.R.styleable#BitmapDrawable_tileMode
 */
public class BitmapDrawable extends android.graphics.drawable.Drawable {
    private static final int DEFAULT_PAINT_FLAGS = android.graphics.Paint.FILTER_BITMAP_FLAG | android.graphics.Paint.DITHER_FLAG;

    // Constants for {@link android.R.styleable#BitmapDrawable_tileMode}.
    private static final int TILE_MODE_UNDEFINED = -2;

    private static final int TILE_MODE_DISABLED = -1;

    private static final int TILE_MODE_CLAMP = 0;

    private static final int TILE_MODE_REPEAT = 1;

    private static final int TILE_MODE_MIRROR = 2;

    private final android.graphics.Rect mDstRect = new android.graphics.Rect();// #updateDstRectAndInsetsIfDirty() sets this


    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.BitmapDrawable.BitmapState mBitmapState;

    private android.graphics.BlendModeColorFilter mBlendModeFilter;

    @android.annotation.UnsupportedAppUsage
    private int mTargetDensity = android.util.DisplayMetrics.DENSITY_DEFAULT;

    private boolean mDstRectAndInsetsDirty = true;

    private boolean mMutated;

    // These are scaled to match the target density.
    private int mBitmapWidth;

    private int mBitmapHeight;

    /**
     * Optical insets due to gravity.
     */
    private android.graphics.Insets mOpticalInsets = android.graphics.Insets.NONE;

    // Mirroring matrix for using with Shaders
    private android.graphics.Matrix mMirrorMatrix;

    /**
     * Create an empty drawable, not dealing with density.
     *
     * @deprecated Use {@link #BitmapDrawable(android.content.res.Resources, android.graphics.Bitmap)}
    instead to specify a bitmap to draw with and ensure the correct density is set.
     */
    @java.lang.Deprecated
    public BitmapDrawable() {
        init(new android.graphics.drawable.BitmapDrawable.BitmapState(((android.graphics.Bitmap) (null))), null);
    }

    /**
     * Create an empty drawable, setting initial target density based on
     * the display metrics of the resources.
     *
     * @deprecated Use {@link #BitmapDrawable(android.content.res.Resources, android.graphics.Bitmap)}
    instead to specify a bitmap to draw with.
     */
    @java.lang.SuppressWarnings("unused")
    @java.lang.Deprecated
    public BitmapDrawable(android.content.res.Resources res) {
        init(new android.graphics.drawable.BitmapDrawable.BitmapState(((android.graphics.Bitmap) (null))), res);
    }

    /**
     * Create drawable from a bitmap, not dealing with density.
     *
     * @deprecated Use {@link #BitmapDrawable(Resources, Bitmap)} to ensure
    that the drawable has correctly set its target density.
     */
    @java.lang.Deprecated
    public BitmapDrawable(android.graphics.Bitmap bitmap) {
        init(new android.graphics.drawable.BitmapDrawable.BitmapState(bitmap), null);
    }

    /**
     * Create drawable from a bitmap, setting initial target density based on
     * the display metrics of the resources.
     */
    public BitmapDrawable(android.content.res.Resources res, android.graphics.Bitmap bitmap) {
        init(new android.graphics.drawable.BitmapDrawable.BitmapState(bitmap), res);
    }

    /**
     * Create a drawable by opening a given file path and decoding the bitmap.
     *
     * @deprecated Use {@link #BitmapDrawable(Resources, String)} to ensure
    that the drawable has correctly set its target density.
     */
    @java.lang.Deprecated
    public BitmapDrawable(java.lang.String filepath) {
        this(null, filepath);
    }

    /**
     * Create a drawable by opening a given file path and decoding the bitmap.
     */
    @java.lang.SuppressWarnings({ "unused", "ChainingConstructorIgnoresParameter" })
    public BitmapDrawable(android.content.res.Resources res, java.lang.String filepath) {
        android.graphics.Bitmap bitmap = null;
        try (java.io.FileInputStream stream = new java.io.FileInputStream(filepath)) {
            bitmap = android.graphics.ImageDecoder.decodeBitmap(android.graphics.ImageDecoder.createSource(res, stream), ( decoder, info, src) -> {
                decoder.setAllocator(android.graphics.ImageDecoder.ALLOCATOR_SOFTWARE);
            });
        } catch (java.lang.Exception e) {
            /* do nothing. This matches the behavior of BitmapFactory.decodeFile()
            If the exception happened on decode, mBitmapState.mBitmap will be null.
             */
        } finally {
            init(new android.graphics.drawable.BitmapDrawable.BitmapState(bitmap), res);
            if (mBitmapState.mBitmap == null) {
                android.graphics.drawable.android.util.Log.w("BitmapDrawable", "BitmapDrawable cannot decode " + filepath);
            }
        }
    }

    /**
     * Create a drawable by decoding a bitmap from the given input stream.
     *
     * @deprecated Use {@link #BitmapDrawable(Resources, java.io.InputStream)} to ensure
    that the drawable has correctly set its target density.
     */
    @java.lang.Deprecated
    public BitmapDrawable(java.io.InputStream is) {
        this(null, is);
    }

    /**
     * Create a drawable by decoding a bitmap from the given input stream.
     */
    @java.lang.SuppressWarnings({ "unused", "ChainingConstructorIgnoresParameter" })
    public BitmapDrawable(android.content.res.Resources res, java.io.InputStream is) {
        android.graphics.Bitmap bitmap = null;
        try {
            bitmap = android.graphics.ImageDecoder.decodeBitmap(android.graphics.ImageDecoder.createSource(res, is), ( decoder, info, src) -> {
                decoder.setAllocator(android.graphics.ImageDecoder.ALLOCATOR_SOFTWARE);
            });
        } catch (java.lang.Exception e) {
            /* do nothing. This matches the behavior of BitmapFactory.decodeStream()
            If the exception happened on decode, mBitmapState.mBitmap will be null.
             */
        } finally {
            init(new android.graphics.drawable.BitmapDrawable.BitmapState(bitmap), res);
            if (mBitmapState.mBitmap == null) {
                android.graphics.drawable.android.util.Log.w("BitmapDrawable", "BitmapDrawable cannot decode " + is);
            }
        }
    }

    /**
     * Returns the paint used to render this drawable.
     */
    public final android.graphics.Paint getPaint() {
        return mBitmapState.mPaint;
    }

    /**
     * Returns the bitmap used by this drawable to render. May be null.
     */
    public final android.graphics.Bitmap getBitmap() {
        return mBitmapState.mBitmap;
    }

    private void computeBitmapSize() {
        final android.graphics.Bitmap bitmap = mBitmapState.mBitmap;
        if (bitmap != null) {
            mBitmapWidth = bitmap.getScaledWidth(mTargetDensity);
            mBitmapHeight = bitmap.getScaledHeight(mTargetDensity);
        } else {
            mBitmapWidth = mBitmapHeight = -1;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setBitmap(android.graphics.Bitmap bitmap) {
        if (mBitmapState.mBitmap != bitmap) {
            mBitmapState.mBitmap = bitmap;
            computeBitmapSize();
            invalidateSelf();
        }
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
    public void setTargetDensity(android.graphics.Canvas canvas) {
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
    public void setTargetDensity(android.util.DisplayMetrics metrics) {
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
        if (mTargetDensity != density) {
            mTargetDensity = (density == 0) ? android.util.DisplayMetrics.DENSITY_DEFAULT : density;
            if (mBitmapState.mBitmap != null) {
                computeBitmapSize();
            }
            invalidateSelf();
        }
    }

    /**
     * Get the gravity used to position/stretch the bitmap within its bounds.
     * See android.view.Gravity
     *
     * @return the gravity applied to the bitmap
     */
    public int getGravity() {
        return mBitmapState.mGravity;
    }

    /**
     * Set the gravity used to position/stretch the bitmap within its bounds.
     * See android.view.Gravity
     *
     * @param gravity
     * 		the gravity
     */
    public void setGravity(int gravity) {
        if (mBitmapState.mGravity != gravity) {
            mBitmapState.mGravity = gravity;
            mDstRectAndInsetsDirty = true;
            invalidateSelf();
        }
    }

    /**
     * Enables or disables the mipmap hint for this drawable's bitmap.
     * See {@link Bitmap#setHasMipMap(boolean)} for more information.
     *
     * If the bitmap is null calling this method has no effect.
     *
     * @param mipMap
     * 		True if the bitmap should use mipmaps, false otherwise.
     * @see #hasMipMap()
     */
    public void setMipMap(boolean mipMap) {
        if (mBitmapState.mBitmap != null) {
            mBitmapState.mBitmap.setHasMipMap(mipMap);
            invalidateSelf();
        }
    }

    /**
     * Indicates whether the mipmap hint is enabled on this drawable's bitmap.
     *
     * @return True if the mipmap hint is set, false otherwise. If the bitmap
    is null, this method always returns false.
     * @see #setMipMap(boolean)
     * @unknown ref android.R.styleable#BitmapDrawable_mipMap
     */
    public boolean hasMipMap() {
        return (mBitmapState.mBitmap != null) && mBitmapState.mBitmap.hasMipMap();
    }

    /**
     * Enables or disables anti-aliasing for this drawable. Anti-aliasing affects
     * the edges of the bitmap only so it applies only when the drawable is rotated.
     *
     * @param aa
     * 		True if the bitmap should be anti-aliased, false otherwise.
     * @see #hasAntiAlias()
     */
    public void setAntiAlias(boolean aa) {
        mBitmapState.mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    /**
     * Indicates whether anti-aliasing is enabled for this drawable.
     *
     * @return True if anti-aliasing is enabled, false otherwise.
     * @see #setAntiAlias(boolean)
     */
    public boolean hasAntiAlias() {
        return mBitmapState.mPaint.isAntiAlias();
    }

    @java.lang.Override
    public void setFilterBitmap(boolean filter) {
        mBitmapState.mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @java.lang.Override
    public boolean isFilterBitmap() {
        return mBitmapState.mPaint.isFilterBitmap();
    }

    @java.lang.Override
    public void setDither(boolean dither) {
        mBitmapState.mPaint.setDither(dither);
        invalidateSelf();
    }

    /**
     * Indicates the repeat behavior of this drawable on the X axis.
     *
     * @return {@link android.graphics.Shader.TileMode#CLAMP} if the bitmap does not repeat,
    {@link android.graphics.Shader.TileMode#REPEAT} or
    {@link android.graphics.Shader.TileMode#MIRROR} otherwise.
     */
    public android.graphics.Shader.TileMode getTileModeX() {
        return mBitmapState.mTileModeX;
    }

    /**
     * Indicates the repeat behavior of this drawable on the Y axis.
     *
     * @return {@link android.graphics.Shader.TileMode#CLAMP} if the bitmap does not repeat,
    {@link android.graphics.Shader.TileMode#REPEAT} or
    {@link android.graphics.Shader.TileMode#MIRROR} otherwise.
     */
    public android.graphics.Shader.TileMode getTileModeY() {
        return mBitmapState.mTileModeY;
    }

    /**
     * Sets the repeat behavior of this drawable on the X axis. By default, the drawable
     * does not repeat its bitmap. Using {@link android.graphics.Shader.TileMode#REPEAT} or
     * {@link android.graphics.Shader.TileMode#MIRROR} the bitmap can be repeated (or tiled)
     * if the bitmap is smaller than this drawable.
     *
     * @param mode
     * 		The repeat mode for this drawable.
     * @see #setTileModeY(android.graphics.Shader.TileMode)
     * @see #setTileModeXY(android.graphics.Shader.TileMode, android.graphics.Shader.TileMode)
     * @unknown ref android.R.styleable#BitmapDrawable_tileModeX
     */
    public void setTileModeX(android.graphics.Shader.TileMode mode) {
        setTileModeXY(mode, mBitmapState.mTileModeY);
    }

    /**
     * Sets the repeat behavior of this drawable on the Y axis. By default, the drawable
     * does not repeat its bitmap. Using {@link android.graphics.Shader.TileMode#REPEAT} or
     * {@link android.graphics.Shader.TileMode#MIRROR} the bitmap can be repeated (or tiled)
     * if the bitmap is smaller than this drawable.
     *
     * @param mode
     * 		The repeat mode for this drawable.
     * @see #setTileModeX(android.graphics.Shader.TileMode)
     * @see #setTileModeXY(android.graphics.Shader.TileMode, android.graphics.Shader.TileMode)
     * @unknown ref android.R.styleable#BitmapDrawable_tileModeY
     */
    public final void setTileModeY(android.graphics.Shader.TileMode mode) {
        setTileModeXY(mBitmapState.mTileModeX, mode);
    }

    /**
     * Sets the repeat behavior of this drawable on both axis. By default, the drawable
     * does not repeat its bitmap. Using {@link android.graphics.Shader.TileMode#REPEAT} or
     * {@link android.graphics.Shader.TileMode#MIRROR} the bitmap can be repeated (or tiled)
     * if the bitmap is smaller than this drawable.
     *
     * @param xmode
     * 		The X repeat mode for this drawable.
     * @param ymode
     * 		The Y repeat mode for this drawable.
     * @see #setTileModeX(android.graphics.Shader.TileMode)
     * @see #setTileModeY(android.graphics.Shader.TileMode)
     */
    public void setTileModeXY(android.graphics.Shader.TileMode xmode, android.graphics.Shader.TileMode ymode) {
        final android.graphics.drawable.BitmapDrawable.BitmapState state = mBitmapState;
        if ((state.mTileModeX != xmode) || (state.mTileModeY != ymode)) {
            state.mTileModeX = xmode;
            state.mTileModeY = ymode;
            state.mRebuildShader = true;
            mDstRectAndInsetsDirty = true;
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void setAutoMirrored(boolean mirrored) {
        if (mBitmapState.mAutoMirrored != mirrored) {
            mBitmapState.mAutoMirrored = mirrored;
            invalidateSelf();
        }
    }

    @java.lang.Override
    public final boolean isAutoMirrored() {
        return mBitmapState.mAutoMirrored;
    }

    @java.lang.Override
    @android.content.pm.ActivityInfo.Config
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | mBitmapState.getChangingConfigurations();
    }

    private boolean needMirroring() {
        return isAutoMirrored() && (getLayoutDirection() == android.util.LayoutDirection.RTL);
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        mDstRectAndInsetsDirty = true;
        final android.graphics.Bitmap bitmap = mBitmapState.mBitmap;
        final android.graphics.Shader shader = mBitmapState.mPaint.getShader();
        if ((bitmap != null) && (shader != null)) {
            updateShaderMatrix(bitmap, mBitmapState.mPaint, shader, needMirroring());
        }
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        final android.graphics.Bitmap bitmap = mBitmapState.mBitmap;
        if (bitmap == null) {
            return;
        }
        final android.graphics.drawable.BitmapDrawable.BitmapState state = mBitmapState;
        final android.graphics.Paint paint = state.mPaint;
        if (state.mRebuildShader) {
            final android.graphics.Shader.TileMode tmx = state.mTileModeX;
            final android.graphics.Shader.TileMode tmy = state.mTileModeY;
            if ((tmx == null) && (tmy == null)) {
                paint.setShader(null);
            } else {
                paint.setShader(new android.graphics.BitmapShader(bitmap, tmx == null ? android.graphics.Shader.TileMode.CLAMP : tmx, tmy == null ? android.graphics.Shader.TileMode.CLAMP : tmy));
            }
            state.mRebuildShader = false;
        }
        final int restoreAlpha;
        if (state.mBaseAlpha != 1.0F) {
            final android.graphics.Paint p = getPaint();
            restoreAlpha = p.getAlpha();
            p.setAlpha(((int) ((restoreAlpha * state.mBaseAlpha) + 0.5F)));
        } else {
            restoreAlpha = -1;
        }
        final boolean clearColorFilter;
        if ((mBlendModeFilter != null) && (paint.getColorFilter() == null)) {
            paint.setColorFilter(mBlendModeFilter);
            clearColorFilter = true;
        } else {
            clearColorFilter = false;
        }
        updateDstRectAndInsetsIfDirty();
        final android.graphics.Shader shader = paint.getShader();
        final boolean needMirroring = needMirroring();
        if (shader == null) {
            if (needMirroring) {
                canvas.save();
                // Mirror the bitmap
                canvas.translate(mDstRect.right - mDstRect.left, 0);
                canvas.scale(-1.0F, 1.0F);
            }
            canvas.drawBitmap(bitmap, null, mDstRect, paint);
            if (needMirroring) {
                canvas.restore();
            }
        } else {
            updateShaderMatrix(bitmap, paint, shader, needMirroring);
            canvas.drawRect(mDstRect, paint);
        }
        if (clearColorFilter) {
            paint.setColorFilter(null);
        }
        if (restoreAlpha >= 0) {
            paint.setAlpha(restoreAlpha);
        }
    }

    /**
     * Updates the {@code paint}'s shader matrix to be consistent with the
     * destination size and layout direction.
     *
     * @param bitmap
     * 		the bitmap to be drawn
     * @param paint
     * 		the paint used to draw the bitmap
     * @param shader
     * 		the shader to set on the paint
     * @param needMirroring
     * 		whether the bitmap should be mirrored
     */
    private void updateShaderMatrix(@android.annotation.NonNull
    android.graphics.Bitmap bitmap, @android.annotation.NonNull
    android.graphics.Paint paint, @android.annotation.NonNull
    android.graphics.Shader shader, boolean needMirroring) {
        final int sourceDensity = bitmap.getDensity();
        final int targetDensity = mTargetDensity;
        final boolean needScaling = (sourceDensity != 0) && (sourceDensity != targetDensity);
        if (needScaling || needMirroring) {
            final android.graphics.Matrix matrix = getOrCreateMirrorMatrix();
            matrix.reset();
            if (needMirroring) {
                final int dx = mDstRect.right - mDstRect.left;
                matrix.setTranslate(dx, 0);
                matrix.setScale(-1, 1);
            }
            if (needScaling) {
                final float densityScale = targetDensity / ((float) (sourceDensity));
                matrix.postScale(densityScale, densityScale);
            }
            shader.setLocalMatrix(matrix);
        } else {
            mMirrorMatrix = null;
            shader.setLocalMatrix(android.graphics.Matrix.IDENTITY_MATRIX);
        }
        paint.setShader(shader);
    }

    private android.graphics.Matrix getOrCreateMirrorMatrix() {
        if (mMirrorMatrix == null) {
            mMirrorMatrix = new android.graphics.Matrix();
        }
        return mMirrorMatrix;
    }

    private void updateDstRectAndInsetsIfDirty() {
        if (mDstRectAndInsetsDirty) {
            if ((mBitmapState.mTileModeX == null) && (mBitmapState.mTileModeY == null)) {
                final android.graphics.Rect bounds = getBounds();
                final int layoutDirection = getLayoutDirection();
                android.view.Gravity.apply(mBitmapState.mGravity, mBitmapWidth, mBitmapHeight, bounds, mDstRect, layoutDirection);
                final int left = mDstRect.left - bounds.left;
                final int top = mDstRect.top - bounds.top;
                final int right = bounds.right - mDstRect.right;
                final int bottom = bounds.bottom - mDstRect.bottom;
                mOpticalInsets = android.graphics.Insets.of(left, top, right, bottom);
            } else {
                copyBounds(mDstRect);
                mOpticalInsets = android.graphics.Insets.NONE;
            }
        }
        mDstRectAndInsetsDirty = false;
    }

    @java.lang.Override
    public android.graphics.Insets getOpticalInsets() {
        updateDstRectAndInsetsIfDirty();
        return mOpticalInsets;
    }

    @java.lang.Override
    public void getOutline(@android.annotation.NonNull
    android.graphics.Outline outline) {
        updateDstRectAndInsetsIfDirty();
        outline.setRect(mDstRect);
        // Only opaque Bitmaps can report a non-0 alpha,
        // since only they are guaranteed to fill their bounds
        boolean opaqueOverShape = (mBitmapState.mBitmap != null) && (!mBitmapState.mBitmap.hasAlpha());
        outline.setAlpha(opaqueOverShape ? getAlpha() / 255.0F : 0.0F);
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        final int oldAlpha = mBitmapState.mPaint.getAlpha();
        if (alpha != oldAlpha) {
            mBitmapState.mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @java.lang.Override
    public int getAlpha() {
        return mBitmapState.mPaint.getAlpha();
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        mBitmapState.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @java.lang.Override
    public android.graphics.ColorFilter getColorFilter() {
        return mBitmapState.mPaint.getColorFilter();
    }

    @java.lang.Override
    public void setTintList(android.content.res.ColorStateList tint) {
        final android.graphics.drawable.BitmapDrawable.BitmapState state = mBitmapState;
        if (state.mTint != tint) {
            state.mTint = tint;
            mBlendModeFilter = updateBlendModeFilter(mBlendModeFilter, tint, mBitmapState.mBlendMode);
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void setTintBlendMode(@android.annotation.NonNull
    android.graphics.BlendMode blendMode) {
        final android.graphics.drawable.BitmapDrawable.BitmapState state = mBitmapState;
        if (state.mBlendMode != blendMode) {
            state.mBlendMode = blendMode;
            mBlendModeFilter = updateBlendModeFilter(mBlendModeFilter, mBitmapState.mTint, blendMode);
            invalidateSelf();
        }
    }

    /**
     *
     *
     * @unknown only needed by a hack within ProgressBar
     */
    @android.annotation.UnsupportedAppUsage
    public android.content.res.ColorStateList getTint() {
        return mBitmapState.mTint;
    }

    /**
     *
     *
     * @unknown only needed by a hack within ProgressBar
     */
    @android.annotation.UnsupportedAppUsage
    public android.graphics.PorterDuff.Mode getTintMode() {
        return android.graphics.BlendMode.blendModeToPorterDuffMode(mBitmapState.mBlendMode);
    }

    /**
     *
     *
     * @unknown Candidate for future API inclusion
     */
    @java.lang.Override
    public void setXfermode(android.graphics.Xfermode xfermode) {
        mBitmapState.mPaint.setXfermode(xfermode);
        invalidateSelf();
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
            mBitmapState = new android.graphics.drawable.BitmapDrawable.BitmapState(mBitmapState);
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
        final android.graphics.drawable.BitmapDrawable.BitmapState state = mBitmapState;
        if ((state.mTint != null) && (state.mBlendMode != null)) {
            mBlendModeFilter = updateBlendModeFilter(mBlendModeFilter, state.mTint, state.mBlendMode);
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean isStateful() {
        return ((mBitmapState.mTint != null) && mBitmapState.mTint.isStateful()) || super.isStateful();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean hasFocusStateSpecified() {
        return (mBitmapState.mTint != null) && mBitmapState.mTint.hasFocusStateSpecified();
    }

    @java.lang.Override
    public void inflate(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        super.inflate(r, parser, attrs, theme);
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.BitmapDrawable);
        updateStateFromTypedArray(a, mSrcDensityOverride);
        verifyRequiredAttributes(a);
        a.recycle();
        // Update local properties.
        updateLocalState(r);
    }

    /**
     * Ensures all required attributes are set.
     *
     * @throws XmlPullParserException
     * 		if any required attributes are missing
     */
    private void verifyRequiredAttributes(android.content.res.TypedArray a) throws org.xmlpull.v1.XmlPullParserException {
        // If we're not waiting on a theme, verify required attributes.
        final android.graphics.drawable.BitmapDrawable.BitmapState state = mBitmapState;
        if ((state.mBitmap == null) && ((state.mThemeAttrs == null) || (state.mThemeAttrs[R.styleable.BitmapDrawable_src] == 0))) {
            throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + ": <bitmap> requires a valid 'src' attribute");
        }
    }

    /**
     * Updates the constant state from the values in the typed array.
     */
    private void updateStateFromTypedArray(android.content.res.TypedArray a, int srcDensityOverride) throws org.xmlpull.v1.XmlPullParserException {
        final android.content.res.Resources r = a.getResources();
        final android.graphics.drawable.BitmapDrawable.BitmapState state = mBitmapState;
        // Account for any configuration changes.
        state.mChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mSrcDensityOverride = srcDensityOverride;
        state.mTargetDensity = android.graphics.drawable.Drawable.resolveDensity(r, 0);
        final int srcResId = a.getResourceId(R.styleable.BitmapDrawable_src, 0);
        if (srcResId != 0) {
            final android.util.TypedValue value = new android.util.TypedValue();
            r.getValueForDensity(srcResId, srcDensityOverride, value, true);
            // Pretend the requested density is actually the display density. If
            // the drawable returned is not the requested density, then force it
            // to be scaled later by dividing its density by the ratio of
            // requested density to actual device density. Drawables that have
            // undefined density or no density don't need to be handled here.
            if (((srcDensityOverride > 0) && (value.density > 0)) && (value.density != android.util.TypedValue.DENSITY_NONE)) {
                if (value.density == srcDensityOverride) {
                    value.density = r.getDisplayMetrics().densityDpi;
                } else {
                    value.density = (value.density * r.getDisplayMetrics().densityDpi) / srcDensityOverride;
                }
            }
            int density = android.graphics.Bitmap.DENSITY_NONE;
            if (value.density == android.util.TypedValue.DENSITY_DEFAULT) {
                density = android.util.DisplayMetrics.DENSITY_DEFAULT;
            } else
                if (value.density != android.util.TypedValue.DENSITY_NONE) {
                    density = value.density;
                }

            android.graphics.Bitmap bitmap = null;
            try (java.io.InputStream is = r.openRawResource(srcResId, value)) {
                android.graphics.ImageDecoder.Source source = android.graphics.ImageDecoder.createSource(r, is, density);
                bitmap = android.graphics.ImageDecoder.decodeBitmap(source, ( decoder, info, src) -> {
                    decoder.setAllocator(android.graphics.ImageDecoder.ALLOCATOR_SOFTWARE);
                });
            } catch (java.lang.Exception e) {
                // Do nothing and pick up the error below.
            }
            if (bitmap == null) {
                throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + ": <bitmap> requires a valid 'src' attribute");
            }
            state.mBitmap = bitmap;
        }
        final boolean defMipMap = (state.mBitmap != null) ? state.mBitmap.hasMipMap() : false;
        setMipMap(a.getBoolean(R.styleable.BitmapDrawable_mipMap, defMipMap));
        state.mAutoMirrored = a.getBoolean(R.styleable.BitmapDrawable_autoMirrored, state.mAutoMirrored);
        state.mBaseAlpha = a.getFloat(R.styleable.BitmapDrawable_alpha, state.mBaseAlpha);
        final int tintMode = a.getInt(R.styleable.BitmapDrawable_tintMode, -1);
        if (tintMode != (-1)) {
            state.mBlendMode = android.graphics.drawable.Drawable.parseBlendMode(tintMode, android.graphics.BlendMode.SRC_IN);
        }
        final android.content.res.ColorStateList tint = a.getColorStateList(R.styleable.BitmapDrawable_tint);
        if (tint != null) {
            state.mTint = tint;
        }
        final android.graphics.Paint paint = mBitmapState.mPaint;
        paint.setAntiAlias(a.getBoolean(R.styleable.BitmapDrawable_antialias, paint.isAntiAlias()));
        paint.setFilterBitmap(a.getBoolean(R.styleable.BitmapDrawable_filter, paint.isFilterBitmap()));
        paint.setDither(a.getBoolean(R.styleable.BitmapDrawable_dither, paint.isDither()));
        setGravity(a.getInt(R.styleable.BitmapDrawable_gravity, state.mGravity));
        final int tileMode = a.getInt(R.styleable.BitmapDrawable_tileMode, android.graphics.drawable.BitmapDrawable.TILE_MODE_UNDEFINED);
        if (tileMode != android.graphics.drawable.BitmapDrawable.TILE_MODE_UNDEFINED) {
            final android.graphics.Shader.TileMode mode = android.graphics.drawable.BitmapDrawable.parseTileMode(tileMode);
            setTileModeXY(mode, mode);
        }
        final int tileModeX = a.getInt(R.styleable.BitmapDrawable_tileModeX, android.graphics.drawable.BitmapDrawable.TILE_MODE_UNDEFINED);
        if (tileModeX != android.graphics.drawable.BitmapDrawable.TILE_MODE_UNDEFINED) {
            setTileModeX(android.graphics.drawable.BitmapDrawable.parseTileMode(tileModeX));
        }
        final int tileModeY = a.getInt(R.styleable.BitmapDrawable_tileModeY, android.graphics.drawable.BitmapDrawable.TILE_MODE_UNDEFINED);
        if (tileModeY != android.graphics.drawable.BitmapDrawable.TILE_MODE_UNDEFINED) {
            setTileModeY(android.graphics.drawable.BitmapDrawable.parseTileMode(tileModeY));
        }
    }

    @java.lang.Override
    public void applyTheme(android.content.res.Resources.Theme t) {
        super.applyTheme(t);
        final android.graphics.drawable.BitmapDrawable.BitmapState state = mBitmapState;
        if (state == null) {
            return;
        }
        if (state.mThemeAttrs != null) {
            final android.content.res.TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.BitmapDrawable);
            try {
                updateStateFromTypedArray(a, state.mSrcDensityOverride);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                android.graphics.drawable.Drawable.rethrowAsRuntimeException(e);
            } finally {
                a.recycle();
            }
        }
        // Apply theme to contained color state list.
        if ((state.mTint != null) && state.mTint.canApplyTheme()) {
            state.mTint = state.mTint.obtainForTheme(t);
        }
        // Update local properties.
        updateLocalState(t.getResources());
    }

    private static android.graphics.Shader.TileMode parseTileMode(int tileMode) {
        switch (tileMode) {
            case android.graphics.drawable.BitmapDrawable.TILE_MODE_CLAMP :
                return android.graphics.Shader.TileMode.CLAMP;
            case android.graphics.drawable.BitmapDrawable.TILE_MODE_REPEAT :
                return android.graphics.Shader.TileMode.REPEAT;
            case android.graphics.drawable.BitmapDrawable.TILE_MODE_MIRROR :
                return android.graphics.Shader.TileMode.MIRROR;
            default :
                return null;
        }
    }

    @java.lang.Override
    public boolean canApplyTheme() {
        return (mBitmapState != null) && mBitmapState.canApplyTheme();
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
        if (mBitmapState.mGravity != android.view.Gravity.FILL) {
            return android.graphics.PixelFormat.TRANSLUCENT;
        }
        final android.graphics.Bitmap bitmap = mBitmapState.mBitmap;
        return ((bitmap == null) || bitmap.hasAlpha()) || (mBitmapState.mPaint.getAlpha() < 255) ? android.graphics.PixelFormat.TRANSLUCENT : android.graphics.PixelFormat.OPAQUE;
    }

    @java.lang.Override
    public final android.graphics.drawable.Drawable.ConstantState getConstantState() {
        mBitmapState.mChangingConfigurations |= getChangingConfigurations();
        return mBitmapState;
    }

    static final class BitmapState extends android.graphics.drawable.Drawable.ConstantState {
        final android.graphics.Paint mPaint;

        // Values loaded during inflation.
        int[] mThemeAttrs = null;

        android.graphics.Bitmap mBitmap = null;

        android.content.res.ColorStateList mTint = null;

        android.graphics.BlendMode mBlendMode = android.graphics.drawable.Drawable.DEFAULT_BLEND_MODE;

        int mGravity = android.view.Gravity.FILL;

        float mBaseAlpha = 1.0F;

        android.graphics.Shader.TileMode mTileModeX = null;

        android.graphics.Shader.TileMode mTileModeY = null;

        // The density to use when looking up the bitmap in Resources. A value of 0 means use
        // the system's density.
        int mSrcDensityOverride = 0;

        // The density at which to render the bitmap.
        int mTargetDensity = android.util.DisplayMetrics.DENSITY_DEFAULT;

        boolean mAutoMirrored = false;

        @android.content.pm.ActivityInfo.Config
        int mChangingConfigurations;

        boolean mRebuildShader;

        BitmapState(android.graphics.Bitmap bitmap) {
            mBitmap = bitmap;
            mPaint = new android.graphics.Paint(android.graphics.drawable.BitmapDrawable.DEFAULT_PAINT_FLAGS);
        }

        BitmapState(android.graphics.drawable.BitmapDrawable.BitmapState bitmapState) {
            mBitmap = bitmapState.mBitmap;
            mTint = bitmapState.mTint;
            mBlendMode = bitmapState.mBlendMode;
            mThemeAttrs = bitmapState.mThemeAttrs;
            mChangingConfigurations = bitmapState.mChangingConfigurations;
            mGravity = bitmapState.mGravity;
            mTileModeX = bitmapState.mTileModeX;
            mTileModeY = bitmapState.mTileModeY;
            mSrcDensityOverride = bitmapState.mSrcDensityOverride;
            mTargetDensity = bitmapState.mTargetDensity;
            mBaseAlpha = bitmapState.mBaseAlpha;
            mPaint = new android.graphics.Paint(bitmapState.mPaint);
            mRebuildShader = bitmapState.mRebuildShader;
            mAutoMirrored = bitmapState.mAutoMirrored;
        }

        @java.lang.Override
        public boolean canApplyTheme() {
            return (mThemeAttrs != null) || ((mTint != null) && mTint.canApplyTheme());
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable() {
            return new android.graphics.drawable.BitmapDrawable(this, null);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.BitmapDrawable(this, res);
        }

        @java.lang.Override
        @android.content.pm.ActivityInfo.Config
        public int getChangingConfigurations() {
            return mChangingConfigurations | (mTint != null ? mTint.getChangingConfigurations() : 0);
        }
    }

    private BitmapDrawable(android.graphics.drawable.BitmapDrawable.BitmapState state, android.content.res.Resources res) {
        init(state, res);
    }

    /**
     * The one helper to rule them all. This is called by all public & private
     * constructors to set the state and initialize local properties.
     */
    private void init(android.graphics.drawable.BitmapDrawable.BitmapState state, android.content.res.Resources res) {
        mBitmapState = state;
        updateLocalState(res);
        if ((mBitmapState != null) && (res != null)) {
            mBitmapState.mTargetDensity = mTargetDensity;
        }
    }

    /**
     * Initializes local dynamic properties from state. This should be called
     * after significant state changes, e.g. from the One True Constructor and
     * after inflating or applying a theme.
     */
    private void updateLocalState(android.content.res.Resources res) {
        mTargetDensity = android.graphics.drawable.Drawable.resolveDensity(res, mBitmapState.mTargetDensity);
        mBlendModeFilter = updateBlendModeFilter(mBlendModeFilter, mBitmapState.mTint, mBitmapState.mBlendMode);
        computeBitmapSize();
    }
}

