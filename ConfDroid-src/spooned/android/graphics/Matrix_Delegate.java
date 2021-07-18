/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * Delegate implementing the native methods of android.graphics.Matrix
 *
 * Through the layoutlib_create tool, the original native methods of Matrix have been replaced
 * by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original Matrix class.
 *
 * @see DelegateManager
 */
public final class Matrix_Delegate {
    private static final int MATRIX_SIZE = 9;

    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.Matrix_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.Matrix_Delegate>(android.graphics.Matrix_Delegate.class);

    private static long sFinalizer = -1;

    // ---- delegate data ----
    private float[] mValues = new float[android.graphics.Matrix_Delegate.MATRIX_SIZE];

    // ---- Public Helper methods ----
    public static android.graphics.Matrix_Delegate getDelegate(long native_instance) {
        return android.graphics.Matrix_Delegate.sManager.getDelegate(native_instance);
    }

    /**
     * Returns an {@link AffineTransform} matching the given Matrix.
     */
    public static java.awt.geom.AffineTransform getAffineTransform(android.graphics.Matrix m) {
        android.graphics.Matrix_Delegate delegate = android.graphics.Matrix_Delegate.sManager.getDelegate(m.native_instance);
        if (delegate == null) {
            return null;
        }
        return delegate.getAffineTransform();
    }

    public static boolean hasPerspective(android.graphics.Matrix m) {
        android.graphics.Matrix_Delegate delegate = android.graphics.Matrix_Delegate.sManager.getDelegate(m.native_instance);
        if (delegate == null) {
            return false;
        }
        return delegate.hasPerspective();
    }

    /**
     * Sets the content of the matrix with the content of another matrix.
     */
    public void set(android.graphics.Matrix_Delegate matrix) {
        java.lang.System.arraycopy(matrix.mValues, 0, mValues, 0, android.graphics.Matrix_Delegate.MATRIX_SIZE);
    }

    /**
     * Sets the content of the matrix with the content of another matrix represented as an array
     * of values.
     */
    public void set(float[] values) {
        java.lang.System.arraycopy(values, 0, mValues, 0, android.graphics.Matrix_Delegate.MATRIX_SIZE);
    }

    /**
     * Resets the matrix to be the identity matrix.
     */
    public void reset() {
        android.graphics.Matrix_Delegate.reset(mValues);
    }

    /**
     * Returns whether or not the matrix is identity.
     */
    public boolean isIdentity() {
        for (int i = 0, k = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++ , k++) {
                if (mValues[k] != (i == j ? 1 : 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static float[] setValues(java.awt.geom.AffineTransform matrix, float[] values) {
        values[0] = ((float) (matrix.getScaleX()));
        values[1] = ((float) (matrix.getShearX()));
        values[2] = ((float) (matrix.getTranslateX()));
        values[3] = ((float) (matrix.getShearY()));
        values[4] = ((float) (matrix.getScaleY()));
        values[5] = ((float) (matrix.getTranslateY()));
        values[6] = 0.0F;
        values[7] = 0.0F;
        values[8] = 1.0F;
        return values;
    }

    public static float[] makeValues(java.awt.geom.AffineTransform matrix) {
        return android.graphics.Matrix_Delegate.setValues(matrix, new float[android.graphics.Matrix_Delegate.MATRIX_SIZE]);
    }

    public static android.graphics.Matrix_Delegate make(java.awt.geom.AffineTransform matrix) {
        return new android.graphics.Matrix_Delegate(android.graphics.Matrix_Delegate.makeValues(matrix));
    }

    public boolean mapRect(android.graphics.RectF dst, android.graphics.RectF src) {
        // array with 4 corners
        float[] corners = new float[]{ src.left, src.top, src.right, src.top, src.right, src.bottom, src.left, src.bottom };
        // apply the transform to them.
        mapPoints(corners);
        // now put the result in the rect. We take the min/max of Xs and min/max of Ys
        dst.left = java.lang.Math.min(java.lang.Math.min(corners[0], corners[2]), java.lang.Math.min(corners[4], corners[6]));
        dst.right = java.lang.Math.max(java.lang.Math.max(corners[0], corners[2]), java.lang.Math.max(corners[4], corners[6]));
        dst.top = java.lang.Math.min(java.lang.Math.min(corners[1], corners[3]), java.lang.Math.min(corners[5], corners[7]));
        dst.bottom = java.lang.Math.max(java.lang.Math.max(corners[1], corners[3]), java.lang.Math.max(corners[5], corners[7]));
        return (computeTypeMask() & android.graphics.Matrix_Delegate.kRectStaysRect_Mask) != 0;
    }

    /**
     * Returns an {@link AffineTransform} matching the matrix.
     */
    public java.awt.geom.AffineTransform getAffineTransform() {
        return android.graphics.Matrix_Delegate.getAffineTransform(mValues);
    }

    public boolean hasPerspective() {
        return ((mValues[6] != 0) || (mValues[7] != 0)) || (mValues[8] != 1);
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreate(long native_src_or_zero) {
        // create the delegate
        android.graphics.Matrix_Delegate newDelegate = new android.graphics.Matrix_Delegate();
        // copy from values if needed.
        if (native_src_or_zero > 0) {
            android.graphics.Matrix_Delegate oldDelegate = android.graphics.Matrix_Delegate.sManager.getDelegate(native_src_or_zero);
            if (oldDelegate != null) {
                java.lang.System.arraycopy(oldDelegate.mValues, 0, newDelegate.mValues, 0, android.graphics.Matrix_Delegate.MATRIX_SIZE);
            }
        }
        return android.graphics.Matrix_Delegate.sManager.addNewDelegate(newDelegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nIsIdentity(long native_object) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return false;
        }
        return d.isIdentity();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nIsAffine(long native_object) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return true;
        }
        return (d.computeTypeMask() & android.graphics.Matrix_Delegate.kPerspective_Mask) == 0;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nRectStaysRect(long native_object) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return true;
        }
        return (d.computeTypeMask() & android.graphics.Matrix_Delegate.kRectStaysRect_Mask) != 0;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nReset(long native_object) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        android.graphics.Matrix_Delegate.reset(d.mValues);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSet(long native_object, long other) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        android.graphics.Matrix_Delegate src = android.graphics.Matrix_Delegate.sManager.getDelegate(other);
        if (src == null) {
            return;
        }
        java.lang.System.arraycopy(src.mValues, 0, d.mValues, 0, android.graphics.Matrix_Delegate.MATRIX_SIZE);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTranslate(long native_object, float dx, float dy) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        android.graphics.Matrix_Delegate.setTranslate(d.mValues, dx, dy);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetScale(long native_object, float sx, float sy, float px, float py) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        d.mValues = android.graphics.Matrix_Delegate.getScale(sx, sy, px, py);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetScale(long native_object, float sx, float sy) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        d.mValues[0] = sx;
        d.mValues[1] = 0;
        d.mValues[2] = 0;
        d.mValues[3] = 0;
        d.mValues[4] = sy;
        d.mValues[5] = 0;
        d.mValues[6] = 0;
        d.mValues[7] = 0;
        d.mValues[8] = 1;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetRotate(long native_object, float degrees, float px, float py) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        d.mValues = android.graphics.Matrix_Delegate.getRotate(degrees, px, py);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetRotate(long native_object, float degrees) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        android.graphics.Matrix_Delegate.setRotate(d.mValues, degrees);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetSinCos(long native_object, float sinValue, float cosValue, float px, float py) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        // TODO: do it in one pass
        // translate so that the pivot is in 0,0
        android.graphics.Matrix_Delegate.setTranslate(d.mValues, -px, -py);
        // scale
        d.postTransform(android.graphics.Matrix_Delegate.getRotate(sinValue, cosValue));
        // translate back the pivot
        d.postTransform(android.graphics.Matrix_Delegate.getTranslate(px, py));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetSinCos(long native_object, float sinValue, float cosValue) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        android.graphics.Matrix_Delegate.setRotate(d.mValues, sinValue, cosValue);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetSkew(long native_object, float kx, float ky, float px, float py) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        d.mValues = android.graphics.Matrix_Delegate.getSkew(kx, ky, px, py);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetSkew(long native_object, float kx, float ky) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        d.mValues[0] = 1;
        d.mValues[1] = kx;
        d.mValues[2] = -0;
        d.mValues[3] = ky;
        d.mValues[4] = 1;
        d.mValues[5] = 0;
        d.mValues[6] = 0;
        d.mValues[7] = 0;
        d.mValues[8] = 1;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetConcat(long native_object, long a, long b) {
        if (a == native_object) {
            android.graphics.Matrix_Delegate.nPreConcat(native_object, b);
            return;
        } else
            if (b == native_object) {
                android.graphics.Matrix_Delegate.nPostConcat(native_object, a);
                return;
            }

        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        android.graphics.Matrix_Delegate a_mtx = android.graphics.Matrix_Delegate.sManager.getDelegate(a);
        android.graphics.Matrix_Delegate b_mtx = android.graphics.Matrix_Delegate.sManager.getDelegate(b);
        if (((d != null) && (a_mtx != null)) && (b_mtx != null)) {
            android.graphics.Matrix_Delegate.multiply(d.mValues, a_mtx.mValues, b_mtx.mValues);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPreTranslate(long native_object, float dx, float dy) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.preTransform(android.graphics.Matrix_Delegate.getTranslate(dx, dy));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPreScale(long native_object, float sx, float sy, float px, float py) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.preTransform(android.graphics.Matrix_Delegate.getScale(sx, sy, px, py));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPreScale(long native_object, float sx, float sy) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.preTransform(android.graphics.Matrix_Delegate.getScale(sx, sy));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPreRotate(long native_object, float degrees, float px, float py) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.preTransform(android.graphics.Matrix_Delegate.getRotate(degrees, px, py));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPreRotate(long native_object, float degrees) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            double rad = java.lang.Math.toRadians(degrees);
            float sin = ((float) (java.lang.Math.sin(rad)));
            float cos = ((float) (java.lang.Math.cos(rad)));
            d.preTransform(android.graphics.Matrix_Delegate.getRotate(sin, cos));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPreSkew(long native_object, float kx, float ky, float px, float py) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.preTransform(android.graphics.Matrix_Delegate.getSkew(kx, ky, px, py));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPreSkew(long native_object, float kx, float ky) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.preTransform(android.graphics.Matrix_Delegate.getSkew(kx, ky));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPreConcat(long native_object, long other_matrix) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        android.graphics.Matrix_Delegate other = android.graphics.Matrix_Delegate.sManager.getDelegate(other_matrix);
        if ((d != null) && (other != null)) {
            d.preTransform(other.mValues);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPostTranslate(long native_object, float dx, float dy) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.postTransform(android.graphics.Matrix_Delegate.getTranslate(dx, dy));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPostScale(long native_object, float sx, float sy, float px, float py) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.postTransform(android.graphics.Matrix_Delegate.getScale(sx, sy, px, py));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPostScale(long native_object, float sx, float sy) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.postTransform(android.graphics.Matrix_Delegate.getScale(sx, sy));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPostRotate(long native_object, float degrees, float px, float py) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.postTransform(android.graphics.Matrix_Delegate.getRotate(degrees, px, py));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPostRotate(long native_object, float degrees) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.postTransform(android.graphics.Matrix_Delegate.getRotate(degrees));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPostSkew(long native_object, float kx, float ky, float px, float py) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.postTransform(android.graphics.Matrix_Delegate.getSkew(kx, ky, px, py));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPostSkew(long native_object, float kx, float ky) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d != null) {
            d.postTransform(android.graphics.Matrix_Delegate.getSkew(kx, ky));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nPostConcat(long native_object, long other_matrix) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        android.graphics.Matrix_Delegate other = android.graphics.Matrix_Delegate.sManager.getDelegate(other_matrix);
        if ((d != null) && (other != null)) {
            d.postTransform(other.mValues);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetRectToRect(long native_object, android.graphics.RectF src, android.graphics.RectF dst, int stf) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return false;
        }
        if (src.isEmpty()) {
            android.graphics.Matrix_Delegate.reset(d.mValues);
            return false;
        }
        if (dst.isEmpty()) {
            d.mValues[0] = d.mValues[1] = d.mValues[2] = d.mValues[3] = d.mValues[4] = d.mValues[5] = d.mValues[6] = d.mValues[7] = 0;
            d.mValues[8] = 1;
        } else {
            float tx;
            float sx = dst.width() / src.width();
            float ty;
            float sy = dst.height() / src.height();
            boolean xLarger = false;
            if (stf != android.graphics.Matrix.ScaleToFit.FILL.nativeInt) {
                if (sx > sy) {
                    xLarger = true;
                    sx = sy;
                } else {
                    sy = sx;
                }
            }
            tx = dst.left - (src.left * sx);
            ty = dst.top - (src.top * sy);
            if ((stf == android.graphics.Matrix.ScaleToFit.CENTER.nativeInt) || (stf == android.graphics.Matrix.ScaleToFit.END.nativeInt)) {
                float diff;
                if (xLarger) {
                    diff = dst.width() - (src.width() * sy);
                } else {
                    diff = dst.height() - (src.height() * sy);
                }
                if (stf == android.graphics.Matrix.ScaleToFit.CENTER.nativeInt) {
                    diff = diff / 2;
                }
                if (xLarger) {
                    tx += diff;
                } else {
                    ty += diff;
                }
            }
            d.mValues[0] = sx;
            d.mValues[4] = sy;
            d.mValues[2] = tx;
            d.mValues[5] = ty;
            d.mValues[1] = d.mValues[3] = d.mValues[6] = d.mValues[7] = 0;
        }
        // shared cleanup
        d.mValues[8] = 1;
        return true;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetPolyToPoly(long native_object, float[] src, int srcIndex, float[] dst, int dstIndex, int pointCount) {
        // FIXME
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Matrix.setPolyToPoly is not supported.", null, null);
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nInvert(long native_object, long inverse) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return false;
        }
        android.graphics.Matrix_Delegate inv_mtx = android.graphics.Matrix_Delegate.sManager.getDelegate(inverse);
        if (inv_mtx == null) {
            return false;
        }
        float det = ((d.mValues[0] * ((d.mValues[4] * d.mValues[8]) - (d.mValues[5] * d.mValues[7]))) + (d.mValues[1] * ((d.mValues[5] * d.mValues[6]) - (d.mValues[3] * d.mValues[8])))) + (d.mValues[2] * ((d.mValues[3] * d.mValues[7]) - (d.mValues[4] * d.mValues[6])));
        if (det == 0.0) {
            return false;
        }
        inv_mtx.mValues[0] = ((d.mValues[4] * d.mValues[8]) - (d.mValues[5] * d.mValues[7])) / det;
        inv_mtx.mValues[1] = ((d.mValues[2] * d.mValues[7]) - (d.mValues[1] * d.mValues[8])) / det;
        inv_mtx.mValues[2] = ((d.mValues[1] * d.mValues[5]) - (d.mValues[2] * d.mValues[4])) / det;
        inv_mtx.mValues[3] = ((d.mValues[5] * d.mValues[6]) - (d.mValues[3] * d.mValues[8])) / det;
        inv_mtx.mValues[4] = ((d.mValues[0] * d.mValues[8]) - (d.mValues[2] * d.mValues[6])) / det;
        inv_mtx.mValues[5] = ((d.mValues[2] * d.mValues[3]) - (d.mValues[0] * d.mValues[5])) / det;
        inv_mtx.mValues[6] = ((d.mValues[3] * d.mValues[7]) - (d.mValues[4] * d.mValues[6])) / det;
        inv_mtx.mValues[7] = ((d.mValues[1] * d.mValues[6]) - (d.mValues[0] * d.mValues[7])) / det;
        inv_mtx.mValues[8] = ((d.mValues[0] * d.mValues[4]) - (d.mValues[1] * d.mValues[3])) / det;
        return true;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nMapPoints(long native_object, float[] dst, int dstIndex, float[] src, int srcIndex, int ptCount, boolean isPts) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        if (isPts) {
            d.mapPoints(dst, dstIndex, src, srcIndex, ptCount);
        } else {
            d.mapVectors(dst, dstIndex, src, srcIndex, ptCount);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nMapRect(long native_object, android.graphics.RectF dst, android.graphics.RectF src) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return false;
        }
        return d.mapRect(dst, src);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nMapRadius(long native_object, float radius) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return 0.0F;
        }
        float[] src = new float[]{ radius, 0.0F, 0.0F, radius };
        d.mapVectors(src, 0, src, 0, 2);
        float l1 = ((float) (java.lang.Math.hypot(src[0], src[1])));
        float l2 = ((float) (java.lang.Math.hypot(src[2], src[3])));
        return ((float) (java.lang.Math.sqrt(l1 * l2)));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nGetValues(long native_object, float[] values) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        java.lang.System.arraycopy(d.mValues, 0, values, 0, android.graphics.Matrix_Delegate.MATRIX_SIZE);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetValues(long native_object, float[] values) {
        android.graphics.Matrix_Delegate d = android.graphics.Matrix_Delegate.sManager.getDelegate(native_object);
        if (d == null) {
            return;
        }
        java.lang.System.arraycopy(values, 0, d.mValues, 0, android.graphics.Matrix_Delegate.MATRIX_SIZE);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nEquals(long native_a, long native_b) {
        android.graphics.Matrix_Delegate a = android.graphics.Matrix_Delegate.sManager.getDelegate(native_a);
        if (a == null) {
            return false;
        }
        android.graphics.Matrix_Delegate b = android.graphics.Matrix_Delegate.sManager.getDelegate(native_b);
        if (b == null) {
            return false;
        }
        for (int i = 0; i < android.graphics.Matrix_Delegate.MATRIX_SIZE; i++) {
            if (a.mValues[i] != b.mValues[i]) {
                return false;
            }
        }
        return true;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetNativeFinalizer() {
        synchronized(android.graphics.Matrix_Delegate.class) {
            if (android.graphics.Matrix_Delegate.sFinalizer == (-1)) {
                android.graphics.Matrix_Delegate.sFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.Matrix_Delegate.sManager::removeJavaReferenceFor);
            }
        }
        return android.graphics.Matrix_Delegate.sFinalizer;
    }

    // ---- Private helper methods ----
    /* package */
    static java.awt.geom.AffineTransform getAffineTransform(float[] matrix) {
        // the AffineTransform constructor takes the value in a different order
        // for a matrix [ 0 1 2 ]
        // [ 3 4 5 ]
        // the order is 0, 3, 1, 4, 2, 5...
        return new java.awt.geom.AffineTransform(matrix[0], matrix[3], matrix[1], matrix[4], matrix[2], matrix[5]);
    }

    /**
     * Reset a matrix to the identity
     */
    private static void reset(float[] mtx) {
        for (int i = 0, k = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++ , k++) {
                mtx[k] = (i == j) ? 1 : 0;
            }
        }
    }

    @java.lang.SuppressWarnings("unused")
    private static final int kIdentity_Mask = 0;

    private static final int kTranslate_Mask = 0x1;// !< set if the matrix has translation


    private static final int kScale_Mask = 0x2;// !< set if the matrix has X or Y scale


    private static final int kAffine_Mask = 0x4;// !< set if the matrix skews or rotates


    private static final int kPerspective_Mask = 0x8;// !< set if the matrix is in perspective


    private static final int kRectStaysRect_Mask = 0x10;

    @java.lang.SuppressWarnings("unused")
    private static final int kUnknown_Mask = 0x80;

    @java.lang.SuppressWarnings("unused")
    private static final int kAllMasks = (((android.graphics.Matrix_Delegate.kTranslate_Mask | android.graphics.Matrix_Delegate.kScale_Mask) | android.graphics.Matrix_Delegate.kAffine_Mask) | android.graphics.Matrix_Delegate.kPerspective_Mask) | android.graphics.Matrix_Delegate.kRectStaysRect_Mask;

    // these guys align with the masks, so we can compute a mask from a variable 0/1
    @java.lang.SuppressWarnings("unused")
    private static final int kTranslate_Shift = 0;

    @java.lang.SuppressWarnings("unused")
    private static final int kScale_Shift = 1;

    @java.lang.SuppressWarnings("unused")
    private static final int kAffine_Shift = 2;

    @java.lang.SuppressWarnings("unused")
    private static final int kPerspective_Shift = 3;

    private static final int kRectStaysRect_Shift = 4;

    private int computeTypeMask() {
        int mask = 0;
        if (((mValues[6] != 0.0) || (mValues[7] != 0.0)) || (mValues[8] != 1.0)) {
            mask |= android.graphics.Matrix_Delegate.kPerspective_Mask;
        }
        if ((mValues[2] != 0.0) || (mValues[5] != 0.0)) {
            mask |= android.graphics.Matrix_Delegate.kTranslate_Mask;
        }
        float m00 = mValues[0];
        float m01 = mValues[1];
        float m10 = mValues[3];
        float m11 = mValues[4];
        if ((m01 != 0.0) || (m10 != 0.0)) {
            mask |= android.graphics.Matrix_Delegate.kAffine_Mask;
        }
        if ((m00 != 1.0) || (m11 != 1.0)) {
            mask |= android.graphics.Matrix_Delegate.kScale_Mask;
        }
        if ((mask & android.graphics.Matrix_Delegate.kPerspective_Mask) == 0) {
            // map non-zero to 1
            int im00 = (m00 != 0) ? 1 : 0;
            int im01 = (m01 != 0) ? 1 : 0;
            int im10 = (m10 != 0) ? 1 : 0;
            int im11 = (m11 != 0) ? 1 : 0;
            // record if the (p)rimary and (s)econdary diagonals are all 0 or
            // all non-zero (answer is 0 or 1)
            int dp0 = (im00 | im11) ^ 1;// true if both are 0

            int dp1 = im00 & im11;// true if both are 1

            int ds0 = (im01 | im10) ^ 1;// true if both are 0

            int ds1 = im01 & im10;// true if both are 1

            // return 1 if primary is 1 and secondary is 0 or
            // primary is 0 and secondary is 1
            mask |= ((dp0 & ds1) | (dp1 & ds0)) << android.graphics.Matrix_Delegate.kRectStaysRect_Shift;
        }
        return mask;
    }

    private Matrix_Delegate() {
        reset();
    }

    private Matrix_Delegate(float[] values) {
        java.lang.System.arraycopy(values, 0, mValues, 0, android.graphics.Matrix_Delegate.MATRIX_SIZE);
    }

    /**
     * Adds the given transformation to the current Matrix
     * <p/>This in effect does this = this*matrix
     *
     * @param matrix
     * 		
     */
    private void postTransform(float[] matrix) {
        float[] tmp = new float[9];
        android.graphics.Matrix_Delegate.multiply(tmp, mValues, matrix);
        mValues = tmp;
    }

    /**
     * Adds the given transformation to the current Matrix
     * <p/>This in effect does this = matrix*this
     *
     * @param matrix
     * 		
     */
    private void preTransform(float[] matrix) {
        float[] tmp = new float[9];
        android.graphics.Matrix_Delegate.multiply(tmp, matrix, mValues);
        mValues = tmp;
    }

    /**
     * Apply this matrix to the array of 2D points specified by src, and write
     * the transformed points into the array of points specified by dst. The
     * two arrays represent their "points" as pairs of floats [x, y].
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
    private void mapPoints(float[] dst, int dstIndex, float[] src, int srcIndex, int pointCount) {
        final int count = pointCount * 2;
        float[] tmpDest = dst;
        boolean inPlace = dst == src;
        if (inPlace) {
            tmpDest = new float[dstIndex + count];
        }
        for (int i = 0; i < count; i += 2) {
            // just in case we are doing in place, we better put this in temp vars
            float x = ((mValues[0] * src[i + srcIndex]) + (mValues[1] * src[(i + srcIndex) + 1])) + mValues[2];
            float y = ((mValues[3] * src[i + srcIndex]) + (mValues[4] * src[(i + srcIndex) + 1])) + mValues[5];
            tmpDest[i + dstIndex] = x;
            tmpDest[(i + dstIndex) + 1] = y;
        }
        if (inPlace) {
            java.lang.System.arraycopy(tmpDest, dstIndex, dst, dstIndex, count);
        }
    }

    /**
     * Apply this matrix to the array of 2D points, and write the transformed
     * points back into the array
     *
     * @param pts
     * 		The array [x0, y0, x1, y1, ...] of points to transform.
     */
    private void mapPoints(float[] pts) {
        mapPoints(pts, 0, pts, 0, pts.length >> 1);
    }

    private void mapVectors(float[] dst, int dstIndex, float[] src, int srcIndex, int ptCount) {
        if (hasPerspective()) {
            // transform the (0,0) point
            float[] origin = new float[]{ 0.0F, 0.0F };
            mapPoints(origin);
            // translate the vector data as points
            mapPoints(dst, dstIndex, src, srcIndex, ptCount);
            // then substract the transformed origin.
            final int count = ptCount * 2;
            for (int i = 0; i < count; i += 2) {
                dst[dstIndex + i] = dst[dstIndex + i] - origin[0];
                dst[(dstIndex + i) + 1] = dst[(dstIndex + i) + 1] - origin[1];
            }
        } else {
            // make a copy of the matrix
            android.graphics.Matrix_Delegate copy = new android.graphics.Matrix_Delegate(mValues);
            // remove the translation
            android.graphics.Matrix_Delegate.setTranslate(copy.mValues, 0, 0);
            // map the content as points.
            copy.mapPoints(dst, dstIndex, src, srcIndex, ptCount);
        }
    }

    /**
     * multiply two matrices and store them in a 3rd.
     * <p/>This in effect does dest = a*b
     * dest cannot be the same as a or b.
     */
    /* package */
    static void multiply(float[] dest, float[] a, float[] b) {
        // first row
        dest[0] = ((b[0] * a[0]) + (b[1] * a[3])) + (b[2] * a[6]);
        dest[1] = ((b[0] * a[1]) + (b[1] * a[4])) + (b[2] * a[7]);
        dest[2] = ((b[0] * a[2]) + (b[1] * a[5])) + (b[2] * a[8]);
        // 2nd row
        dest[3] = ((b[3] * a[0]) + (b[4] * a[3])) + (b[5] * a[6]);
        dest[4] = ((b[3] * a[1]) + (b[4] * a[4])) + (b[5] * a[7]);
        dest[5] = ((b[3] * a[2]) + (b[4] * a[5])) + (b[5] * a[8]);
        // 3rd row
        dest[6] = ((b[6] * a[0]) + (b[7] * a[3])) + (b[8] * a[6]);
        dest[7] = ((b[6] * a[1]) + (b[7] * a[4])) + (b[8] * a[7]);
        dest[8] = ((b[6] * a[2]) + (b[7] * a[5])) + (b[8] * a[8]);
    }

    /**
     * Returns a matrix that represents a given translate
     *
     * @param dx
     * 		
     * @param dy
     * 		
     * @return 
     */
    /* package */
    static float[] getTranslate(float dx, float dy) {
        return android.graphics.Matrix_Delegate.setTranslate(new float[9], dx, dy);
    }

    /* package */
    static float[] setTranslate(float[] dest, float dx, float dy) {
        dest[0] = 1;
        dest[1] = 0;
        dest[2] = dx;
        dest[3] = 0;
        dest[4] = 1;
        dest[5] = dy;
        dest[6] = 0;
        dest[7] = 0;
        dest[8] = 1;
        return dest;
    }

    /* package */
    static float[] getScale(float sx, float sy) {
        return new float[]{ sx, 0, 0, 0, sy, 0, 0, 0, 1 };
    }

    /**
     * Returns a matrix that represents the given scale info.
     *
     * @param sx
     * 		
     * @param sy
     * 		
     * @param px
     * 		
     * @param py
     * 		
     */
    /* package */
    static float[] getScale(float sx, float sy, float px, float py) {
        float[] tmp = new float[9];
        float[] tmp2 = new float[9];
        // TODO: do it in one pass
        // translate tmp so that the pivot is in 0,0
        android.graphics.Matrix_Delegate.setTranslate(tmp, -px, -py);
        // scale into tmp2
        android.graphics.Matrix_Delegate.multiply(tmp2, tmp, android.graphics.Matrix_Delegate.getScale(sx, sy));
        // translate back the pivot back into tmp
        android.graphics.Matrix_Delegate.multiply(tmp, tmp2, android.graphics.Matrix_Delegate.getTranslate(px, py));
        return tmp;
    }

    /* package */
    static float[] getRotate(float degrees) {
        double rad = java.lang.Math.toRadians(degrees);
        float sin = ((float) (java.lang.Math.sin(rad)));
        float cos = ((float) (java.lang.Math.cos(rad)));
        return android.graphics.Matrix_Delegate.getRotate(sin, cos);
    }

    /* package */
    static float[] getRotate(float sin, float cos) {
        return android.graphics.Matrix_Delegate.setRotate(new float[9], sin, cos);
    }

    /* package */
    static float[] setRotate(float[] dest, float degrees) {
        double rad = java.lang.Math.toRadians(degrees);
        float sin = ((float) (java.lang.Math.sin(rad)));
        float cos = ((float) (java.lang.Math.cos(rad)));
        return android.graphics.Matrix_Delegate.setRotate(dest, sin, cos);
    }

    /* package */
    static float[] setRotate(float[] dest, float sin, float cos) {
        dest[0] = cos;
        dest[1] = -sin;
        dest[2] = 0;
        dest[3] = sin;
        dest[4] = cos;
        dest[5] = 0;
        dest[6] = 0;
        dest[7] = 0;
        dest[8] = 1;
        return dest;
    }

    /* package */
    static float[] getRotate(float degrees, float px, float py) {
        float[] tmp = new float[9];
        float[] tmp2 = new float[9];
        // TODO: do it in one pass
        // translate so that the pivot is in 0,0
        android.graphics.Matrix_Delegate.setTranslate(tmp, -px, -py);
        // rotate into tmp2
        double rad = java.lang.Math.toRadians(degrees);
        float cos = ((float) (java.lang.Math.cos(rad)));
        float sin = ((float) (java.lang.Math.sin(rad)));
        android.graphics.Matrix_Delegate.multiply(tmp2, tmp, android.graphics.Matrix_Delegate.getRotate(sin, cos));
        // translate back the pivot back into tmp
        android.graphics.Matrix_Delegate.multiply(tmp, tmp2, android.graphics.Matrix_Delegate.getTranslate(px, py));
        return tmp;
    }

    /* package */
    static float[] getSkew(float kx, float ky) {
        return new float[]{ 1, kx, 0, ky, 1, 0, 0, 0, 1 };
    }

    /* package */
    static float[] getSkew(float kx, float ky, float px, float py) {
        float[] tmp = new float[9];
        float[] tmp2 = new float[9];
        // TODO: do it in one pass
        // translate so that the pivot is in 0,0
        android.graphics.Matrix_Delegate.setTranslate(tmp, -px, -py);
        // skew into tmp2
        android.graphics.Matrix_Delegate.multiply(tmp2, tmp, new float[]{ 1, kx, 0, ky, 1, 0, 0, 0, 1 });
        // translate back the pivot back into tmp
        android.graphics.Matrix_Delegate.multiply(tmp, tmp2, android.graphics.Matrix_Delegate.getTranslate(px, py));
        return tmp;
    }
}

