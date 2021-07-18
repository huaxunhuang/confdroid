/**
 * Copyright (C) 2007 The Android Open Source Project
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


public class LinearGradient extends android.graphics.Shader {
    @android.annotation.UnsupportedAppUsage
    private float mX0;

    @android.annotation.UnsupportedAppUsage
    private float mY0;

    @android.annotation.UnsupportedAppUsage
    private float mX1;

    @android.annotation.UnsupportedAppUsage
    private float mY1;

    @android.annotation.UnsupportedAppUsage
    private float[] mPositions;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.Shader.TileMode mTileMode;

    // @ColorInts are replaced by @ColorLongs, but these remain due to @UnsupportedAppUsage.
    @android.annotation.UnsupportedAppUsage
    @android.annotation.ColorInt
    private int[] mColors;

    @android.annotation.UnsupportedAppUsage
    @android.annotation.ColorInt
    private int mColor0;

    @android.annotation.UnsupportedAppUsage
    @android.annotation.ColorInt
    private int mColor1;

    @android.annotation.ColorLong
    private final long[] mColorLongs;

    /**
     * Create a shader that draws a linear gradient along a line.
     *
     * @param x0
     * 		The x-coordinate for the start of the gradient line
     * @param y0
     * 		The y-coordinate for the start of the gradient line
     * @param x1
     * 		The x-coordinate for the end of the gradient line
     * @param y1
     * 		The y-coordinate for the end of the gradient line
     * @param colors
     * 		The sRGB colors to be distributed along the gradient line
     * @param positions
     * 		May be null. The relative positions [0..1] of
     * 		each corresponding color in the colors array. If this is null,
     * 		the the colors are distributed evenly along the gradient line.
     * @param tile
     * 		The Shader tiling mode
     */
    public LinearGradient(float x0, float y0, float x1, float y1, @android.annotation.NonNull
    @android.annotation.ColorInt
    int[] colors, @android.annotation.Nullable
    float[] positions, @android.annotation.NonNull
    android.graphics.Shader.TileMode tile) {
        this(x0, y0, x1, y1, android.graphics.Shader.convertColors(colors), positions, tile, android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB));
    }

    /**
     * Create a shader that draws a linear gradient along a line.
     *
     * @param x0
     * 		The x-coordinate for the start of the gradient line
     * @param y0
     * 		The y-coordinate for the start of the gradient line
     * @param x1
     * 		The x-coordinate for the end of the gradient line
     * @param y1
     * 		The y-coordinate for the end of the gradient line
     * @param colors
     * 		The colors to be distributed along the gradient line
     * @param positions
     * 		May be null. The relative positions [0..1] of
     * 		each corresponding color in the colors array. If this is null,
     * 		the the colors are distributed evenly along the gradient line.
     * @param tile
     * 		The Shader tiling mode
     * @throws IllegalArgumentException
     * 		if there are less than two colors, the colors do
     * 		not share the same {@link ColorSpace} or do not use a valid one, or {@code positions}
     * 		is not {@code null} and has a different length from {@code colors}.
     */
    public LinearGradient(float x0, float y0, float x1, float y1, @android.annotation.NonNull
    @android.annotation.ColorLong
    long[] colors, @android.annotation.Nullable
    float[] positions, @android.annotation.NonNull
    android.graphics.Shader.TileMode tile) {
        this(x0, y0, x1, y1, colors.clone(), positions, tile, android.graphics.Shader.detectColorSpace(colors));
    }

    /**
     * Base constructor. Assumes @param colors is a copy that this object can hold onto,
     * and all colors share @param colorSpace.
     */
    private LinearGradient(float x0, float y0, float x1, float y1, @android.annotation.NonNull
    @android.annotation.ColorLong
    long[] colors, @android.annotation.Nullable
    float[] positions, @android.annotation.NonNull
    android.graphics.Shader.TileMode tile, @android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        super(colorSpace);
        if ((positions != null) && (colors.length != positions.length)) {
            throw new java.lang.IllegalArgumentException("color and position arrays must be of equal length");
        }
        mX0 = x0;
        mY0 = y0;
        mX1 = x1;
        mY1 = y1;
        mColorLongs = colors;
        mPositions = (positions != null) ? positions.clone() : null;
        mTileMode = tile;
    }

    /**
     * Create a shader that draws a linear gradient along a line.
     *
     * @param x0
     * 		The x-coordinate for the start of the gradient line
     * @param y0
     * 		The y-coordinate for the start of the gradient line
     * @param x1
     * 		The x-coordinate for the end of the gradient line
     * @param y1
     * 		The y-coordinate for the end of the gradient line
     * @param color0
     * 		The sRGB color at the start of the gradient line.
     * @param color1
     * 		The sRGB color at the end of the gradient line.
     * @param tile
     * 		The Shader tiling mode
     */
    public LinearGradient(float x0, float y0, float x1, float y1, @android.annotation.ColorInt
    int color0, @android.annotation.ColorInt
    int color1, @android.annotation.NonNull
    android.graphics.Shader.TileMode tile) {
        this(x0, y0, x1, y1, android.graphics.Color.pack(color0), android.graphics.Color.pack(color1), tile);
    }

    /**
     * Create a shader that draws a linear gradient along a line.
     *
     * @param x0
     * 		The x-coordinate for the start of the gradient line
     * @param y0
     * 		The y-coordinate for the start of the gradient line
     * @param x1
     * 		The x-coordinate for the end of the gradient line
     * @param y1
     * 		The y-coordinate for the end of the gradient line
     * @param color0
     * 		The color at the start of the gradient line.
     * @param color1
     * 		The color at the end of the gradient line.
     * @param tile
     * 		The Shader tiling mode
     * @throws IllegalArgumentException
     * 		if the colors do
     * 		not share the same {@link ColorSpace} or do not use a valid one.
     */
    public LinearGradient(float x0, float y0, float x1, float y1, @android.annotation.ColorLong
    long color0, @android.annotation.ColorLong
    long color1, @android.annotation.NonNull
    android.graphics.Shader.TileMode tile) {
        this(x0, y0, x1, y1, new long[]{ color0, color1 }, null, tile);
    }

    @java.lang.Override
    long createNativeInstance(long nativeMatrix) {
        return nativeCreate(nativeMatrix, mX0, mY0, mX1, mY1, mColorLongs, mPositions, mTileMode.nativeInt, colorSpace().getNativeInstance());
    }

    private native long nativeCreate(long matrix, float x0, float y0, float x1, float y1, long[] colors, float[] positions, int tileMode, long colorSpaceHandle);
}

