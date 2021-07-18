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


public class SweepGradient extends android.graphics.Shader {
    @android.annotation.UnsupportedAppUsage
    private float mCx;

    @android.annotation.UnsupportedAppUsage
    private float mCy;

    @android.annotation.UnsupportedAppUsage
    private float[] mPositions;

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
     * A Shader that draws a sweep gradient around a center point.
     *
     * @param cx
     * 		The x-coordinate of the center
     * @param cy
     * 		The y-coordinate of the center
     * @param colors
     * 		The sRGB colors to be distributed between around the center.
     * 		There must be at least 2 colors in the array.
     * @param positions
     * 		May be NULL. The relative position of
     * 		each corresponding color in the colors array, beginning
     * 		with 0 and ending with 1.0. If the values are not
     * 		monotonic, the drawing may produce unexpected results.
     * 		If positions is NULL, then the colors are automatically
     * 		spaced evenly.
     */
    public SweepGradient(float cx, float cy, @android.annotation.NonNull
    @android.annotation.ColorInt
    int[] colors, @android.annotation.Nullable
    float[] positions) {
        this(cx, cy, android.graphics.Shader.convertColors(colors), positions, android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB));
    }

    /**
     * A Shader that draws a sweep gradient around a center point.
     *
     * @param cx
     * 		The x-coordinate of the center
     * @param cy
     * 		The y-coordinate of the center
     * @param colors
     * 		The colors to be distributed between around the center.
     * 		There must be at least 2 colors in the array.
     * @param positions
     * 		May be NULL. The relative position of
     * 		each corresponding color in the colors array, beginning
     * 		with 0 and ending with 1.0. If the values are not
     * 		monotonic, the drawing may produce unexpected results.
     * 		If positions is NULL, then the colors are automatically
     * 		spaced evenly.
     * @throws IllegalArgumentException
     * 		if there are less than two colors, the colors do
     * 		not share the same {@link ColorSpace} or do not use a valid one, or {@code positions}
     * 		is not {@code null} and has a different length from {@code colors}.
     */
    public SweepGradient(float cx, float cy, @android.annotation.NonNull
    @android.annotation.ColorLong
    long[] colors, @android.annotation.Nullable
    float[] positions) {
        this(cx, cy, colors.clone(), positions, android.graphics.Shader.detectColorSpace(colors));
    }

    /**
     * Base constructor. Assumes @param colors is a copy that this object can hold onto,
     * and all colors share @param colorSpace.
     */
    private SweepGradient(float cx, float cy, @android.annotation.NonNull
    @android.annotation.ColorLong
    long[] colors, @android.annotation.Nullable
    float[] positions, android.graphics.ColorSpace colorSpace) {
        super(colorSpace);
        if ((positions != null) && (colors.length != positions.length)) {
            throw new java.lang.IllegalArgumentException("color and position arrays must be of equal length");
        }
        mCx = cx;
        mCy = cy;
        mColorLongs = colors;
        mPositions = (positions != null) ? positions.clone() : null;
    }

    /**
     * A Shader that draws a sweep gradient around a center point.
     *
     * @param cx
     * 		The x-coordinate of the center
     * @param cy
     * 		The y-coordinate of the center
     * @param color0
     * 		The sRGB color to use at the start of the sweep
     * @param color1
     * 		The sRGB color to use at the end of the sweep
     */
    public SweepGradient(float cx, float cy, @android.annotation.ColorInt
    int color0, @android.annotation.ColorInt
    int color1) {
        this(cx, cy, android.graphics.Color.pack(color0), android.graphics.Color.pack(color1));
    }

    /**
     * A Shader that draws a sweep gradient around a center point.
     *
     * @param cx
     * 		The x-coordinate of the center
     * @param cy
     * 		The y-coordinate of the center
     * @param color0
     * 		The color to use at the start of the sweep
     * @param color1
     * 		The color to use at the end of the sweep
     * @throws IllegalArgumentException
     * 		if the colors do
     * 		not share the same {@link ColorSpace} or do not use a valid one.
     */
    public SweepGradient(float cx, float cy, @android.annotation.ColorLong
    long color0, @android.annotation.ColorLong
    long color1) {
        this(cx, cy, new long[]{ color0, color1 }, null);
    }

    @java.lang.Override
    long createNativeInstance(long nativeMatrix) {
        return android.graphics.SweepGradient.nativeCreate(nativeMatrix, mCx, mCy, mColorLongs, mPositions, colorSpace().getNativeInstance());
    }

    private static native long nativeCreate(long matrix, float x, float y, long[] colors, float[] positions, long colorSpaceHandle);
}

