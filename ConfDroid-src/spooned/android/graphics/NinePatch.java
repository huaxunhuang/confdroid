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
package android.graphics;


/**
 * The NinePatch class permits drawing a bitmap in nine or more sections.
 * Essentially, it allows the creation of custom graphics that will scale the
 * way that you define, when content added within the image exceeds the normal
 * bounds of the graphic. For a thorough explanation of a NinePatch image,
 * read the discussion in the
 * <a href="{@docRoot }guide/topics/graphics/2d-graphics.html#nine-patch">2D
 * Graphics</a> document.
 * <p>
 * The <a href="{@docRoot }guide/developing/tools/draw9patch.html">Draw 9-Patch</a>
 * tool offers an extremely handy way to create your NinePatch images,
 * using a WYSIWYG graphics editor.
 * </p>
 */
public class NinePatch {
    /**
     * Struct of inset information attached to a 9 patch bitmap.
     *
     * Present on a 9 patch bitmap if it optical insets were manually included,
     * or if outline insets were automatically included by aapt.
     *
     * @unknown 
     */
    public static class InsetStruct {
        // called from JNI
        @java.lang.SuppressWarnings({ "UnusedDeclaration" })
        @android.annotation.UnsupportedAppUsage
        InsetStruct(int opticalLeft, int opticalTop, int opticalRight, int opticalBottom, int outlineLeft, int outlineTop, int outlineRight, int outlineBottom, float outlineRadius, int outlineAlpha, float decodeScale) {
            opticalRect = new android.graphics.Rect(opticalLeft, opticalTop, opticalRight, opticalBottom);
            opticalRect.scale(decodeScale);
            outlineRect = android.graphics.NinePatch.InsetStruct.scaleInsets(outlineLeft, outlineTop, outlineRight, outlineBottom, decodeScale);
            this.outlineRadius = outlineRadius * decodeScale;
            this.outlineAlpha = outlineAlpha / 255.0F;
        }

        public final android.graphics.Rect opticalRect;

        public final android.graphics.Rect outlineRect;

        public final float outlineRadius;

        public final float outlineAlpha;

        /**
         * Scales up the rect by the given scale, ceiling values, so actual outline Rect
         * grows toward the inside.
         */
        public static android.graphics.Rect scaleInsets(int left, int top, int right, int bottom, float scale) {
            if (scale == 1.0F) {
                return new android.graphics.Rect(left, top, right, bottom);
            }
            android.graphics.Rect result = new android.graphics.Rect();
            result.left = ((int) (java.lang.Math.ceil(left * scale)));
            result.top = ((int) (java.lang.Math.ceil(top * scale)));
            result.right = ((int) (java.lang.Math.ceil(right * scale)));
            result.bottom = ((int) (java.lang.Math.ceil(bottom * scale)));
            return result;
        }
    }

    @android.annotation.UnsupportedAppUsage
    private final android.graphics.Bitmap mBitmap;

    /**
     * Used by native code. This pointer is an instance of Res_png_9patch*.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public long mNativeChunk;

    private android.graphics.Paint mPaint;

    private java.lang.String mSrcName;

    /**
     * Create a drawable projection from a bitmap to nine patches.
     *
     * @param bitmap
     * 		The bitmap describing the patches.
     * @param chunk
     * 		The 9-patch data chunk describing how the underlying bitmap
     * 		is split apart and drawn.
     */
    public NinePatch(android.graphics.Bitmap bitmap, byte[] chunk) {
        this(bitmap, chunk, null);
    }

    /**
     * Create a drawable projection from a bitmap to nine patches.
     *
     * @param bitmap
     * 		The bitmap describing the patches.
     * @param chunk
     * 		The 9-patch data chunk describing how the underlying
     * 		bitmap is split apart and drawn.
     * @param srcName
     * 		The name of the source for the bitmap. Might be null.
     */
    public NinePatch(android.graphics.Bitmap bitmap, byte[] chunk, java.lang.String srcName) {
        mBitmap = bitmap;
        mSrcName = srcName;
        mNativeChunk = android.graphics.NinePatch.validateNinePatchChunk(chunk);
    }

    /**
     *
     *
     * @unknown 
     */
    public NinePatch(android.graphics.NinePatch patch) {
        mBitmap = patch.mBitmap;
        mSrcName = patch.mSrcName;
        if (patch.mPaint != null) {
            mPaint = new android.graphics.Paint(patch.mPaint);
        }
        // No need to validate the 9patch chunk again, it was done by
        // the instance we're copying from
        mNativeChunk = patch.mNativeChunk;
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            if (mNativeChunk != 0) {
                // only attempt to destroy correctly initilized chunks
                android.graphics.NinePatch.nativeFinalize(mNativeChunk);
                mNativeChunk = 0;
            }
        } finally {
            super.finalize();
        }
    }

    /**
     * Returns the name of this NinePatch object if one was specified
     * when calling the constructor.
     */
    public java.lang.String getName() {
        return mSrcName;
    }

    /**
     * Returns the paint used to draw this NinePatch. The paint can be null.
     *
     * @see #setPaint(Paint)
     * @see #draw(Canvas, Rect)
     * @see #draw(Canvas, RectF)
     */
    public android.graphics.Paint getPaint() {
        return mPaint;
    }

    /**
     * Sets the paint to use when drawing the NinePatch.
     *
     * @param p
     * 		The paint that will be used to draw this NinePatch.
     * @see #getPaint()
     * @see #draw(Canvas, Rect)
     * @see #draw(Canvas, RectF)
     */
    public void setPaint(android.graphics.Paint p) {
        mPaint = p;
    }

    /**
     * Returns the bitmap used to draw this NinePatch.
     */
    public android.graphics.Bitmap getBitmap() {
        return mBitmap;
    }

    /**
     * Draws the NinePatch. This method will use the paint returned by {@link #getPaint()}.
     *
     * @param canvas
     * 		A container for the current matrix and clip used to draw the NinePatch.
     * @param location
     * 		Where to draw the NinePatch.
     */
    public void draw(android.graphics.Canvas canvas, android.graphics.RectF location) {
        canvas.drawPatch(this, location, mPaint);
    }

    /**
     * Draws the NinePatch. This method will use the paint returned by {@link #getPaint()}.
     *
     * @param canvas
     * 		A container for the current matrix and clip used to draw the NinePatch.
     * @param location
     * 		Where to draw the NinePatch.
     */
    public void draw(android.graphics.Canvas canvas, android.graphics.Rect location) {
        canvas.drawPatch(this, location, mPaint);
    }

    /**
     * Draws the NinePatch. This method will ignore the paint returned
     * by {@link #getPaint()} and use the specified paint instead.
     *
     * @param canvas
     * 		A container for the current matrix and clip used to draw the NinePatch.
     * @param location
     * 		Where to draw the NinePatch.
     * @param paint
     * 		The Paint to draw through.
     */
    public void draw(android.graphics.Canvas canvas, android.graphics.Rect location, android.graphics.Paint paint) {
        canvas.drawPatch(this, location, paint);
    }

    /**
     * Return the underlying bitmap's density, as per
     * {@link Bitmap#getDensity() Bitmap.getDensity()}.
     */
    public int getDensity() {
        return mBitmap.mDensity;
    }

    /**
     * Returns the intrinsic width, in pixels, of this NinePatch. This is equivalent
     * to querying the width of the underlying bitmap returned by {@link #getBitmap()}.
     */
    public int getWidth() {
        return mBitmap.getWidth();
    }

    /**
     * Returns the intrinsic height, in pixels, of this NinePatch. This is equivalent
     * to querying the height of the underlying bitmap returned by {@link #getBitmap()}.
     */
    public int getHeight() {
        return mBitmap.getHeight();
    }

    /**
     * Indicates whether this NinePatch contains transparent or translucent pixels.
     * This is equivalent to calling <code>getBitmap().hasAlpha()</code> on this
     * NinePatch.
     */
    public final boolean hasAlpha() {
        return mBitmap.hasAlpha();
    }

    /**
     * Returns a {@link Region} representing the parts of the NinePatch that are
     * completely transparent.
     *
     * @param bounds
     * 		The location and size of the NinePatch.
     * @return null if the NinePatch has no transparent region to
    report, else a {@link Region} holding the parts of the specified bounds
    that are transparent.
     */
    public final android.graphics.Region getTransparentRegion(android.graphics.Rect bounds) {
        long r = android.graphics.NinePatch.nativeGetTransparentRegion(mBitmap.getNativeInstance(), mNativeChunk, bounds);
        return r != 0 ? new android.graphics.Region(r) : null;
    }

    /**
     * Verifies that the specified byte array is a valid 9-patch data chunk.
     *
     * @param chunk
     * 		A byte array representing a 9-patch data chunk.
     * @return True if the specified byte array represents a 9-patch data chunk,
    false otherwise.
     */
    public static native boolean isNinePatchChunk(byte[] chunk);

    /**
     * Validates the 9-patch chunk and throws an exception if the chunk is invalid.
     * If validation is successful, this method returns a native Res_png_9patch*
     * object used by the renderers.
     */
    private static native long validateNinePatchChunk(byte[] chunk);

    private static native void nativeFinalize(long chunk);

    private static native long nativeGetTransparentRegion(long bitmapHandle, long chunk, android.graphics.Rect location);
}

