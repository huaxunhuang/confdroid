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


/**
 * A color filter that transforms colors through a 4x5 color matrix. This filter
 * can be used to change the saturation of pixels, convert from YUV to RGB, etc.
 *
 * @see ColorMatrix
 */
public class ColorMatrixColorFilter extends android.graphics.ColorFilter {
    @android.annotation.UnsupportedAppUsage
    private final android.graphics.ColorMatrix mMatrix = new android.graphics.ColorMatrix();

    /**
     * Create a color filter that transforms colors through a 4x5 color matrix.
     *
     * @param matrix
     * 		4x5 matrix used to transform colors. It is copied into
     * 		the filter, so changes made to the matrix after the filter
     * 		is constructed will not be reflected in the filter.
     */
    public ColorMatrixColorFilter(@android.annotation.NonNull
    android.graphics.ColorMatrix matrix) {
        mMatrix.set(matrix);
    }

    /**
     * Create a color filter that transforms colors through a 4x5 color matrix.
     *
     * @param array
     * 		Array of floats used to transform colors, treated as a 4x5
     * 		matrix. The first 20 entries of the array are copied into
     * 		the filter. See ColorMatrix.
     */
    public ColorMatrixColorFilter(@android.annotation.NonNull
    float[] array) {
        if (array.length < 20) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        mMatrix.set(array);
    }

    /**
     * Copies the ColorMatrix from the filter into the passed ColorMatrix.
     *
     * @param colorMatrix
     * 		Set to the current value of the filter's ColorMatrix.
     */
    public void getColorMatrix(android.graphics.ColorMatrix colorMatrix) {
        colorMatrix.set(mMatrix);
    }

    /**
     * Copies the provided color matrix to be used by this filter.
     *
     * If the specified color matrix is null, this filter's color matrix will be reset to the
     * identity matrix.
     *
     * @param matrix
     * 		A {@link ColorMatrix} or null
     * @see #getColorMatrix(ColorMatrix)
     * @see #setColorMatrixArray(float[])
     * @see ColorMatrix#reset()
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setColorMatrix(@android.annotation.Nullable
    android.graphics.ColorMatrix matrix) {
        discardNativeInstance();
        if (matrix == null) {
            mMatrix.reset();
        } else {
            mMatrix.set(matrix);
        }
    }

    /**
     * Copies the provided color matrix to be used by this filter.
     *
     * If the specified color matrix is null, this filter's color matrix will be reset to the
     * identity matrix.
     *
     * @param array
     * 		Array of floats used to transform colors, treated as a 4x5
     * 		matrix. The first 20 entries of the array are copied into
     * 		the filter. See {@link ColorMatrix}.
     * @see #getColorMatrix(ColorMatrix)
     * @see #setColorMatrix(ColorMatrix)
     * @see ColorMatrix#reset()
     * @throws ArrayIndexOutOfBoundsException
     * 		if the specified array's
     * 		length is < 20
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setColorMatrixArray(@android.annotation.Nullable
    float[] array) {
        // called '...Array' so that passing null isn't ambiguous
        discardNativeInstance();
        if (array == null) {
            mMatrix.reset();
        } else {
            if (array.length < 20) {
                throw new java.lang.ArrayIndexOutOfBoundsException();
            }
            mMatrix.set(array);
        }
    }

    @java.lang.Override
    long createNativeInstance() {
        return android.graphics.ColorMatrixColorFilter.nativeColorMatrixFilter(mMatrix.getArray());
    }

    private static native long nativeColorMatrixFilter(float[] array);
}

