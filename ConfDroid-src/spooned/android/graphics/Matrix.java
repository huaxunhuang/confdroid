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
 * The Matrix class holds a 3x3 matrix for transforming coordinates.
 */
public class Matrix {
    public static final int MSCALE_X = 0;// !< use with getValues/setValues


    public static final int MSKEW_X = 1;// !< use with getValues/setValues


    public static final int MTRANS_X = 2;// !< use with getValues/setValues


    public static final int MSKEW_Y = 3;// !< use with getValues/setValues


    public static final int MSCALE_Y = 4;// !< use with getValues/setValues


    public static final int MTRANS_Y = 5;// !< use with getValues/setValues


    public static final int MPERSP_0 = 6;// !< use with getValues/setValues


    public static final int MPERSP_1 = 7;// !< use with getValues/setValues


    public static final int MPERSP_2 = 8;// !< use with getValues/setValues


    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static final android.graphics.Matrix IDENTITY_MATRIX = new android.graphics.Matrix() {
        void oops() {
            throw new java.lang.IllegalStateException("Matrix can not be modified");
        }

        @java.lang.Override
        public void set(android.graphics.Matrix src) {
            oops();
        }

        @java.lang.Override
        public void reset() {
            oops();
        }

        @java.lang.Override
        public void setTranslate(float dx, float dy) {
            oops();
        }

        @java.lang.Override
        public void setScale(float sx, float sy, float px, float py) {
            oops();
        }

        @java.lang.Override
        public void setScale(float sx, float sy) {
            oops();
        }

        @java.lang.Override
        public void setRotate(float degrees, float px, float py) {
            oops();
        }

        @java.lang.Override
        public void setRotate(float degrees) {
            oops();
        }

        @java.lang.Override
        public void setSinCos(float sinValue, float cosValue, float px, float py) {
            oops();
        }

        @java.lang.Override
        public void setSinCos(float sinValue, float cosValue) {
            oops();
        }

        @java.lang.Override
        public void setSkew(float kx, float ky, float px, float py) {
            oops();
        }

        @java.lang.Override
        public void setSkew(float kx, float ky) {
            oops();
        }

        @java.lang.Override
        public boolean setConcat(android.graphics.Matrix a, android.graphics.Matrix b) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean preTranslate(float dx, float dy) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean preScale(float sx, float sy, float px, float py) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean preScale(float sx, float sy) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean preRotate(float degrees, float px, float py) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean preRotate(float degrees) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean preSkew(float kx, float ky, float px, float py) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean preSkew(float kx, float ky) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean preConcat(android.graphics.Matrix other) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean postTranslate(float dx, float dy) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean postScale(float sx, float sy, float px, float py) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean postScale(float sx, float sy) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean postRotate(float degrees, float px, float py) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean postRotate(float degrees) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean postSkew(float kx, float ky, float px, float py) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean postSkew(float kx, float ky) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean postConcat(android.graphics.Matrix other) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean setRectToRect(android.graphics.RectF src, android.graphics.RectF dst, android.graphics.Matrix.ScaleToFit stf) {
            oops();
            return false;
        }

        @java.lang.Override
        public boolean setPolyToPoly(float[] src, int srcIndex, float[] dst, int dstIndex, int pointCount) {
            oops();
            return false;
        }

        @java.lang.Override
        public void setValues(float[] values) {
            oops();
        }
    };

    private static class NoImagePreloadHolder {
        public static final libcore.util.NativeAllocationRegistry sRegistry = libcore.util.NativeAllocationRegistry.createMalloced(android.graphics.Matrix.class.getClassLoader(), android.graphics.Matrix.nGetNativeFinalizer());
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final long native_instance;

    /**
     * Create an identity matrix
     */
    public Matrix() {
        native_instance = android.graphics.Matrix.nCreate(0);
        android.graphics.Matrix.NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, native_instance);
    }

    /**
     * Create a matrix that is a (deep) copy of src
     *
     * @param src
     * 		The matrix to copy into this matrix
     */
    public Matrix(android.graphics.Matrix src) {
        native_instance = android.graphics.Matrix.nCreate(src != null ? src.native_instance : 0);
        android.graphics.Matrix.NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, native_instance);
    }

    /**
     * Returns true if the matrix is identity. This maybe faster than testing if (getType() == 0)
     */
    public boolean isIdentity() {
        return android.graphics.Matrix.nIsIdentity(native_instance);
    }

    /**
     * Gets whether this matrix is affine. An affine matrix preserves straight lines and has no
     * perspective.
     *
     * @return Whether the matrix is affine.
     */
    public boolean isAffine() {
        return android.graphics.Matrix.nIsAffine(native_instance);
    }

    /**
     * Returns true if will map a rectangle to another rectangle. This can be true if the matrix is
     * identity, scale-only, or rotates a multiple of 90 degrees.
     */
    public boolean rectStaysRect() {
        return android.graphics.Matrix.nRectStaysRect(native_instance);
    }

    /**
     * (deep) copy the src matrix into this matrix. If src is null, reset this matrix to the
     * identity matrix.
     */
    public void set(android.graphics.Matrix src) {
        if (src == null) {
            reset();
        } else {
            android.graphics.Matrix.nSet(native_instance, src.native_instance);
        }
    }

    /**
     * Returns true iff obj is a Matrix and its values equal our values.
     */
    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        // if (obj == this) return true; -- NaN value would mean matrix != itself
        if (!(obj instanceof android.graphics.Matrix)) {
            return false;
        }
        return android.graphics.Matrix.nEquals(native_instance, ((android.graphics.Matrix) (obj)).native_instance);
    }

    @java.lang.Override
    public int hashCode() {
        // This should generate the hash code by performing some arithmetic operation on all
        // the matrix elements -- our equals() does an element-by-element comparison, and we
        // need to ensure that the hash code for two equal objects is the same. We're not
        // really using this at the moment, so we take the easy way out.
        return 44;
    }

    /**
     * Set the matrix to identity
     */
    public void reset() {
        android.graphics.Matrix.nReset(native_instance);
    }

    /**
     * Set the matrix to translate by (dx, dy).
     */
    public void setTranslate(float dx, float dy) {
        android.graphics.Matrix.nSetTranslate(native_instance, dx, dy);
    }

    /**
     * Set the matrix to scale by sx and sy, with a pivot point at (px, py). The pivot point is the
     * coordinate that should remain unchanged by the specified transformation.
     */
    public void setScale(float sx, float sy, float px, float py) {
        android.graphics.Matrix.nSetScale(native_instance, sx, sy, px, py);
    }

    /**
     * Set the matrix to scale by sx and sy.
     */
    public void setScale(float sx, float sy) {
        android.graphics.Matrix.nSetScale(native_instance, sx, sy);
    }

    /**
     * Set the matrix to rotate by the specified number of degrees, with a pivot point at (px, py).
     * The pivot point is the coordinate that should remain unchanged by the specified
     * transformation.
     */
    public void setRotate(float degrees, float px, float py) {
        android.graphics.Matrix.nSetRotate(native_instance, degrees, px, py);
    }

    /**
     * Set the matrix to rotate about (0,0) by the specified number of degrees.
     */
    public void setRotate(float degrees) {
        android.graphics.Matrix.nSetRotate(native_instance, degrees);
    }

    /**
     * Set the matrix to rotate by the specified sine and cosine values, with a pivot point at (px,
     * py). The pivot point is the coordinate that should remain unchanged by the specified
     * transformation.
     */
    public void setSinCos(float sinValue, float cosValue, float px, float py) {
        android.graphics.Matrix.nSetSinCos(native_instance, sinValue, cosValue, px, py);
    }

    /**
     * Set the matrix to rotate by the specified sine and cosine values.
     */
    public void setSinCos(float sinValue, float cosValue) {
        android.graphics.Matrix.nSetSinCos(native_instance, sinValue, cosValue);
    }

    /**
     * Set the matrix to skew by sx and sy, with a pivot point at (px, py). The pivot point is the
     * coordinate that should remain unchanged by the specified transformation.
     */
    public void setSkew(float kx, float ky, float px, float py) {
        android.graphics.Matrix.nSetSkew(native_instance, kx, ky, px, py);
    }

    /**
     * Set the matrix to skew by sx and sy.
     */
    public void setSkew(float kx, float ky) {
        android.graphics.Matrix.nSetSkew(native_instance, kx, ky);
    }

    /**
     * Set the matrix to the concatenation of the two specified matrices and return true.
     * <p>
     * Either of the two matrices may also be the target matrix, that is
     * <code>matrixA.setConcat(matrixA, matrixB);</code> is valid.
     * </p>
     * <p class="note">
     * In {@link android.os.Build.VERSION_CODES#GINGERBREAD_MR1} and below, this function returns
     * true only if the result can be represented. In
     * {@link android.os.Build.VERSION_CODES#HONEYCOMB} and above, it always returns true.
     * </p>
     */
    public boolean setConcat(android.graphics.Matrix a, android.graphics.Matrix b) {
        android.graphics.Matrix.nSetConcat(native_instance, a.native_instance, b.native_instance);
        return true;
    }

    /**
     * Preconcats the matrix with the specified translation. M' = M * T(dx, dy)
     */
    public boolean preTranslate(float dx, float dy) {
        android.graphics.Matrix.nPreTranslate(native_instance, dx, dy);
        return true;
    }

    /**
     * Preconcats the matrix with the specified scale. M' = M * S(sx, sy, px, py)
     */
    public boolean preScale(float sx, float sy, float px, float py) {
        android.graphics.Matrix.nPreScale(native_instance, sx, sy, px, py);
        return true;
    }

    /**
     * Preconcats the matrix with the specified scale. M' = M * S(sx, sy)
     */
    public boolean preScale(float sx, float sy) {
        android.graphics.Matrix.nPreScale(native_instance, sx, sy);
        return true;
    }

    /**
     * Preconcats the matrix with the specified rotation. M' = M * R(degrees, px, py)
     */
    public boolean preRotate(float degrees, float px, float py) {
        android.graphics.Matrix.nPreRotate(native_instance, degrees, px, py);
        return true;
    }

    /**
     * Preconcats the matrix with the specified rotation. M' = M * R(degrees)
     */
    public boolean preRotate(float degrees) {
        android.graphics.Matrix.nPreRotate(native_instance, degrees);
        return true;
    }

    /**
     * Preconcats the matrix with the specified skew. M' = M * K(kx, ky, px, py)
     */
    public boolean preSkew(float kx, float ky, float px, float py) {
        android.graphics.Matrix.nPreSkew(native_instance, kx, ky, px, py);
        return true;
    }

    /**
     * Preconcats the matrix with the specified skew. M' = M * K(kx, ky)
     */
    public boolean preSkew(float kx, float ky) {
        android.graphics.Matrix.nPreSkew(native_instance, kx, ky);
        return true;
    }

    /**
     * Preconcats the matrix with the specified matrix. M' = M * other
     */
    public boolean preConcat(android.graphics.Matrix other) {
        android.graphics.Matrix.nPreConcat(native_instance, other.native_instance);
        return true;
    }

    /**
     * Postconcats the matrix with the specified translation. M' = T(dx, dy) * M
     */
    public boolean postTranslate(float dx, float dy) {
        android.graphics.Matrix.nPostTranslate(native_instance, dx, dy);
        return true;
    }

    /**
     * Postconcats the matrix with the specified scale. M' = S(sx, sy, px, py) * M
     */
    public boolean postScale(float sx, float sy, float px, float py) {
        android.graphics.Matrix.nPostScale(native_instance, sx, sy, px, py);
        return true;
    }

    /**
     * Postconcats the matrix with the specified scale. M' = S(sx, sy) * M
     */
    public boolean postScale(float sx, float sy) {
        android.graphics.Matrix.nPostScale(native_instance, sx, sy);
        return true;
    }

    /**
     * Postconcats the matrix with the specified rotation. M' = R(degrees, px, py) * M
     */
    public boolean postRotate(float degrees, float px, float py) {
        android.graphics.Matrix.nPostRotate(native_instance, degrees, px, py);
        return true;
    }

    /**
     * Postconcats the matrix with the specified rotation. M' = R(degrees) * M
     */
    public boolean postRotate(float degrees) {
        android.graphics.Matrix.nPostRotate(native_instance, degrees);
        return true;
    }

    /**
     * Postconcats the matrix with the specified skew. M' = K(kx, ky, px, py) * M
     */
    public boolean postSkew(float kx, float ky, float px, float py) {
        android.graphics.Matrix.nPostSkew(native_instance, kx, ky, px, py);
        return true;
    }

    /**
     * Postconcats the matrix with the specified skew. M' = K(kx, ky) * M
     */
    public boolean postSkew(float kx, float ky) {
        android.graphics.Matrix.nPostSkew(native_instance, kx, ky);
        return true;
    }

    /**
     * Postconcats the matrix with the specified matrix. M' = other * M
     */
    public boolean postConcat(android.graphics.Matrix other) {
        android.graphics.Matrix.nPostConcat(native_instance, other.native_instance);
        return true;
    }

    /**
     * Controlls how the src rect should align into the dst rect for setRectToRect().
     */
    public enum ScaleToFit {

        /**
         * Scale in X and Y independently, so that src matches dst exactly. This may change the
         * aspect ratio of the src.
         */
        FILL(0),
        /**
         * Compute a scale that will maintain the original src aspect ratio, but will also ensure
         * that src fits entirely inside dst. At least one axis (X or Y) will fit exactly. START
         * aligns the result to the left and top edges of dst.
         */
        START(1),
        /**
         * Compute a scale that will maintain the original src aspect ratio, but will also ensure
         * that src fits entirely inside dst. At least one axis (X or Y) will fit exactly. The
         * result is centered inside dst.
         */
        CENTER(2),
        /**
         * Compute a scale that will maintain the original src aspect ratio, but will also ensure
         * that src fits entirely inside dst. At least one axis (X or Y) will fit exactly. END
         * aligns the result to the right and bottom edges of dst.
         */
        END(3);
        // the native values must match those in SkMatrix.h
        ScaleToFit(int nativeInt) {
            this.nativeInt = nativeInt;
        }

        final int nativeInt;
    }

    /**
     * Set the matrix to the scale and translate values that map the source rectangle to the
     * destination rectangle, returning true if the the result can be represented.
     *
     * @param src
     * 		the source rectangle to map from.
     * @param dst
     * 		the destination rectangle to map to.
     * @param stf
     * 		the ScaleToFit option
     * @return true if the matrix can be represented by the rectangle mapping.
     */
    public boolean setRectToRect(android.graphics.RectF src, android.graphics.RectF dst, android.graphics.Matrix.ScaleToFit stf) {
        if ((dst == null) || (src == null)) {
            throw new java.lang.NullPointerException();
        }
        return android.graphics.Matrix.nSetRectToRect(native_instance, src, dst, stf.nativeInt);
    }

    // private helper to perform range checks on arrays of "points"
    private static void checkPointArrays(float[] src, int srcIndex, float[] dst, int dstIndex, int pointCount) {
        // check for too-small and too-big indices
        int srcStop = srcIndex + (pointCount << 1);
        int dstStop = dstIndex + (pointCount << 1);
        if (((((((pointCount | srcIndex) | dstIndex) | srcStop) | dstStop) < 0) || (srcStop > src.length)) || (dstStop > dst.length)) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Set the matrix such that the specified src points would map to the specified dst points. The
     * "points" are represented as an array of floats, order [x0, y0, x1, y1, ...], where each
     * "point" is 2 float values.
     *
     * @param src
     * 		The array of src [x,y] pairs (points)
     * @param srcIndex
     * 		Index of the first pair of src values
     * @param dst
     * 		The array of dst [x,y] pairs (points)
     * @param dstIndex
     * 		Index of the first pair of dst values
     * @param pointCount
     * 		The number of pairs/points to be used. Must be [0..4]
     * @return true if the matrix was set to the specified transformation
     */
    public boolean setPolyToPoly(float[] src, int srcIndex, float[] dst, int dstIndex, int pointCount) {
        if (pointCount > 4) {
            throw new java.lang.IllegalArgumentException();
        }
        android.graphics.Matrix.checkPointArrays(src, srcIndex, dst, dstIndex, pointCount);
        return android.graphics.Matrix.nSetPolyToPoly(native_instance, src, srcIndex, dst, dstIndex, pointCount);
    }

    /**
     * If this matrix can be inverted, return true and if inverse is not null, set inverse to be the
     * inverse of this matrix. If this matrix cannot be inverted, ignore inverse and return false.
     */
    public boolean invert(android.graphics.Matrix inverse) {
        return android.graphics.Matrix.nInvert(native_instance, inverse.native_instance);
    }

    /**
     * Apply this matrix to the array of 2D points specified by src, and write the transformed
     * points into the array of points specified by dst. The two arrays represent their "points" as
     * pairs of floats [x, y].
     *
     * @param dst
     * 		The array of dst points (x,y pairs)
     * @param dstIndex
     * 		The index of the first [x,y] pair of dst floats
     * @param src
     * 		The array of src points (x,y pairs)
     * @param srcIndex
     * 		The index of the first [x,y] pair of src floats
     * @param pointCount
     * 		The number of points (x,y pairs) to transform
     */
    public void mapPoints(float[] dst, int dstIndex, float[] src, int srcIndex, int pointCount) {
        android.graphics.Matrix.checkPointArrays(src, srcIndex, dst, dstIndex, pointCount);
        android.graphics.Matrix.nMapPoints(native_instance, dst, dstIndex, src, srcIndex, pointCount, true);
    }

    /**
     * Apply this matrix to the array of 2D vectors specified by src, and write the transformed
     * vectors into the array of vectors specified by dst. The two arrays represent their "vectors"
     * as pairs of floats [x, y]. Note: this method does not apply the translation associated with
     * the matrix. Use {@link Matrix#mapPoints(float[], int, float[], int, int)} if you want the
     * translation to be applied.
     *
     * @param dst
     * 		The array of dst vectors (x,y pairs)
     * @param dstIndex
     * 		The index of the first [x,y] pair of dst floats
     * @param src
     * 		The array of src vectors (x,y pairs)
     * @param srcIndex
     * 		The index of the first [x,y] pair of src floats
     * @param vectorCount
     * 		The number of vectors (x,y pairs) to transform
     */
    public void mapVectors(float[] dst, int dstIndex, float[] src, int srcIndex, int vectorCount) {
        android.graphics.Matrix.checkPointArrays(src, srcIndex, dst, dstIndex, vectorCount);
        android.graphics.Matrix.nMapPoints(native_instance, dst, dstIndex, src, srcIndex, vectorCount, false);
    }

    /**
     * Apply this matrix to the array of 2D points specified by src, and write the transformed
     * points into the array of points specified by dst. The two arrays represent their "points" as
     * pairs of floats [x, y].
     *
     * @param dst
     * 		The array of dst points (x,y pairs)
     * @param src
     * 		The array of src points (x,y pairs)
     */
    public void mapPoints(float[] dst, float[] src) {
        if (dst.length != src.length) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        mapPoints(dst, 0, src, 0, dst.length >> 1);
    }

    /**
     * Apply this matrix to the array of 2D vectors specified by src, and write the transformed
     * vectors into the array of vectors specified by dst. The two arrays represent their "vectors"
     * as pairs of floats [x, y]. Note: this method does not apply the translation associated with
     * the matrix. Use {@link Matrix#mapPoints(float[], float[])} if you want the translation to be
     * applied.
     *
     * @param dst
     * 		The array of dst vectors (x,y pairs)
     * @param src
     * 		The array of src vectors (x,y pairs)
     */
    public void mapVectors(float[] dst, float[] src) {
        if (dst.length != src.length) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        mapVectors(dst, 0, src, 0, dst.length >> 1);
    }

    /**
     * Apply this matrix to the array of 2D points, and write the transformed points back into the
     * array
     *
     * @param pts
     * 		The array [x0, y0, x1, y1, ...] of points to transform.
     */
    public void mapPoints(float[] pts) {
        mapPoints(pts, 0, pts, 0, pts.length >> 1);
    }

    /**
     * Apply this matrix to the array of 2D vectors, and write the transformed vectors back into the
     * array. Note: this method does not apply the translation associated with the matrix. Use
     * {@link Matrix#mapPoints(float[])} if you want the translation to be applied.
     *
     * @param vecs
     * 		The array [x0, y0, x1, y1, ...] of vectors to transform.
     */
    public void mapVectors(float[] vecs) {
        mapVectors(vecs, 0, vecs, 0, vecs.length >> 1);
    }

    /**
     * Apply this matrix to the src rectangle, and write the transformed rectangle into dst. This is
     * accomplished by transforming the 4 corners of src, and then setting dst to the bounds of
     * those points.
     *
     * @param dst
     * 		Where the transformed rectangle is written.
     * @param src
     * 		The original rectangle to be transformed.
     * @return the result of calling rectStaysRect()
     */
    public boolean mapRect(android.graphics.RectF dst, android.graphics.RectF src) {
        if ((dst == null) || (src == null)) {
            throw new java.lang.NullPointerException();
        }
        return android.graphics.Matrix.nMapRect(native_instance, dst, src);
    }

    /**
     * Apply this matrix to the rectangle, and write the transformed rectangle back into it. This is
     * accomplished by transforming the 4 corners of rect, and then setting it to the bounds of
     * those points
     *
     * @param rect
     * 		The rectangle to transform.
     * @return the result of calling rectStaysRect()
     */
    public boolean mapRect(android.graphics.RectF rect) {
        return mapRect(rect, rect);
    }

    /**
     * Return the mean radius of a circle after it has been mapped by this matrix. NOTE: in
     * perspective this value assumes the circle has its center at the origin.
     */
    public float mapRadius(float radius) {
        return android.graphics.Matrix.nMapRadius(native_instance, radius);
    }

    /**
     * Copy 9 values from the matrix into the array.
     */
    public void getValues(float[] values) {
        if (values.length < 9) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        android.graphics.Matrix.nGetValues(native_instance, values);
    }

    /**
     * Copy 9 values from the array into the matrix. Depending on the implementation of Matrix,
     * these may be transformed into 16.16 integers in the Matrix, such that a subsequent call to
     * getValues() will not yield exactly the same values.
     */
    public void setValues(float[] values) {
        if (values.length < 9) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        android.graphics.Matrix.nSetValues(native_instance, values);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(64);
        sb.append("Matrix{");
        toShortString(sb);
        sb.append('}');
        return sb.toString();
    }

    public java.lang.String toShortString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(64);
        toShortString(sb);
        return sb.toString();
    }

    /**
     *
     *
     * @unknown 
     */
    public void toShortString(java.lang.StringBuilder sb) {
        float[] values = new float[9];
        getValues(values);
        sb.append('[');
        sb.append(values[0]);
        sb.append(", ");
        sb.append(values[1]);
        sb.append(", ");
        sb.append(values[2]);
        sb.append("][");
        sb.append(values[3]);
        sb.append(", ");
        sb.append(values[4]);
        sb.append(", ");
        sb.append(values[5]);
        sb.append("][");
        sb.append(values[6]);
        sb.append(", ");
        sb.append(values[7]);
        sb.append(", ");
        sb.append(values[8]);
        sb.append(']');
    }

    /**
     * Print short string, to optimize dumping.
     *
     * @unknown 
     */
    public void printShortString(java.io.PrintWriter pw) {
        float[] values = new float[9];
        getValues(values);
        pw.print('[');
        pw.print(values[0]);
        pw.print(", ");
        pw.print(values[1]);
        pw.print(", ");
        pw.print(values[2]);
        pw.print("][");
        pw.print(values[3]);
        pw.print(", ");
        pw.print(values[4]);
        pw.print(", ");
        pw.print(values[5]);
        pw.print("][");
        pw.print(values[6]);
        pw.print(", ");
        pw.print(values[7]);
        pw.print(", ");
        pw.print(values[8]);
        pw.print(']');
    }

    /**
     *
     *
     * @unknown 
     */
    public final long ni() {
        return native_instance;
    }

    // ------------------ Regular JNI ------------------------
    private static native long nCreate(long nSrc_or_zero);

    private static native long nGetNativeFinalizer();

    // ------------------ Fast JNI ------------------------
    @dalvik.annotation.optimization.FastNative
    private static native boolean nSetRectToRect(long nObject, android.graphics.RectF src, android.graphics.RectF dst, int stf);

    @dalvik.annotation.optimization.FastNative
    private static native boolean nSetPolyToPoly(long nObject, float[] src, int srcIndex, float[] dst, int dstIndex, int pointCount);

    @dalvik.annotation.optimization.FastNative
    private static native void nMapPoints(long nObject, float[] dst, int dstIndex, float[] src, int srcIndex, int ptCount, boolean isPts);

    @dalvik.annotation.optimization.FastNative
    private static native boolean nMapRect(long nObject, android.graphics.RectF dst, android.graphics.RectF src);

    @dalvik.annotation.optimization.FastNative
    private static native void nGetValues(long nObject, float[] values);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetValues(long nObject, float[] values);

    // ------------------ Critical JNI ------------------------
    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nIsIdentity(long nObject);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nIsAffine(long nObject);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nRectStaysRect(long nObject);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nReset(long nObject);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nSet(long nObject, long nOther);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nSetTranslate(long nObject, float dx, float dy);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nSetScale(long nObject, float sx, float sy, float px, float py);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nSetScale(long nObject, float sx, float sy);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nSetRotate(long nObject, float degrees, float px, float py);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nSetRotate(long nObject, float degrees);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nSetSinCos(long nObject, float sinValue, float cosValue, float px, float py);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nSetSinCos(long nObject, float sinValue, float cosValue);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nSetSkew(long nObject, float kx, float ky, float px, float py);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nSetSkew(long nObject, float kx, float ky);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nSetConcat(long nObject, long nA, long nB);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPreTranslate(long nObject, float dx, float dy);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPreScale(long nObject, float sx, float sy, float px, float py);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPreScale(long nObject, float sx, float sy);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPreRotate(long nObject, float degrees, float px, float py);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPreRotate(long nObject, float degrees);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPreSkew(long nObject, float kx, float ky, float px, float py);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPreSkew(long nObject, float kx, float ky);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPreConcat(long nObject, long nOther_matrix);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPostTranslate(long nObject, float dx, float dy);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPostScale(long nObject, float sx, float sy, float px, float py);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPostScale(long nObject, float sx, float sy);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPostRotate(long nObject, float degrees, float px, float py);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPostRotate(long nObject, float degrees);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPostSkew(long nObject, float kx, float ky, float px, float py);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPostSkew(long nObject, float kx, float ky);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nPostConcat(long nObject, long nOther_matrix);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nInvert(long nObject, long nInverse);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nMapRadius(long nObject, float radius);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nEquals(long nA, long nB);
}

