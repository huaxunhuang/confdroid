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
 * Delegate implementing the native methods of android.graphics.Canvas
 *
 * Through the layoutlib_create tool, the original native methods of Canvas have been replaced
 * by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original Canvas class.
 *
 * @see DelegateManager
 */
public final class Canvas_Delegate extends android.graphics.BaseCanvas_Delegate {
    // ---- delegate manager ----
    private static long sFinalizer = -1;

    private android.graphics.DrawFilter_Delegate mDrawFilter = null;

    // ---- Public Helper methods ----
    /**
     * Returns the native delegate associated to a given {@link Canvas} object.
     */
    public static android.graphics.Canvas_Delegate getDelegate(android.graphics.Canvas canvas) {
        return ((android.graphics.Canvas_Delegate) (android.graphics.BaseCanvas_Delegate.sManager.getDelegate(canvas.getNativeCanvasWrapper())));
    }

    /**
     * Returns the native delegate associated to a given an int referencing a {@link Canvas} object.
     */
    public static android.graphics.Canvas_Delegate getDelegate(long native_canvas) {
        return ((android.graphics.Canvas_Delegate) (android.graphics.BaseCanvas_Delegate.sManager.getDelegate(native_canvas)));
    }

    /**
     * Returns the current {@link Graphics2D} used to draw.
     */
    public com.android.layoutlib.bridge.impl.GcSnapshot getSnapshot() {
        return mSnapshot;
    }

    /**
     * Returns the {@link DrawFilter} delegate or null if none have been set.
     *
     * @return the delegate or null.
     */
    public android.graphics.DrawFilter_Delegate getDrawFilter() {
        return mDrawFilter;
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nFreeCaches() {
        // nothing to be done here.
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nFreeTextLayoutCaches() {
        // nothing to be done here yet.
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nInitRaster(long bitmapHandle) {
        if (bitmapHandle > 0) {
            // get the Bitmap from the int
            android.graphics.Bitmap_Delegate bitmapDelegate = android.graphics.Bitmap_Delegate.getDelegate(bitmapHandle);
            // create a new Canvas_Delegate with the given bitmap and return its new native int.
            android.graphics.Canvas_Delegate newDelegate = new android.graphics.Canvas_Delegate(bitmapDelegate);
            return android.graphics.BaseCanvas_Delegate.sManager.addNewDelegate(newDelegate);
        }
        // create a new Canvas_Delegate and return its new native int.
        android.graphics.Canvas_Delegate newDelegate = new android.graphics.Canvas_Delegate();
        return android.graphics.BaseCanvas_Delegate.sManager.addNewDelegate(newDelegate);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nSetBitmap(long canvas, long bitmapHandle) {
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(canvas);
        android.graphics.Bitmap_Delegate bitmapDelegate = android.graphics.Bitmap_Delegate.getDelegate(bitmapHandle);
        if ((canvasDelegate == null) || (bitmapDelegate == null)) {
            return;
        }
        canvasDelegate.mBitmap = bitmapDelegate;
        canvasDelegate.mSnapshot = com.android.layoutlib.bridge.impl.GcSnapshot.createDefaultSnapshot(bitmapDelegate);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static boolean nIsOpaque(long nativeCanvas) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return false;
        }
        return canvasDelegate.mBitmap.getConfig() == android.graphics.Bitmap.Config.RGB_565;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static int nGetWidth(long nativeCanvas) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return 0;
        }
        return canvasDelegate.mBitmap.getImage().getWidth();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static int nGetHeight(long nativeCanvas) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return 0;
        }
        return canvasDelegate.mBitmap.getImage().getHeight();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static int nSave(long nativeCanvas, int saveFlags) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return 0;
        }
        return canvasDelegate.save(saveFlags);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static int nSaveLayer(long nativeCanvas, float l, float t, float r, float b, long paint, int layerFlags) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return 0;
        }
        android.graphics.Paint_Delegate paintDelegate = android.graphics.Paint_Delegate.getDelegate(paint);
        return canvasDelegate.saveLayer(new android.graphics.RectF(l, t, r, b), paintDelegate, layerFlags);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static int nSaveUnclippedLayer(long nativeCanvas, int l, int t, int r, int b) {
        return android.graphics.Canvas_Delegate.nSaveLayer(nativeCanvas, l, t, r, b, 0, 0);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static int nSaveLayerAlpha(long nativeCanvas, float l, float t, float r, float b, int alpha, int layerFlags) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return 0;
        }
        return canvasDelegate.saveLayerAlpha(new android.graphics.RectF(l, t, r, b), alpha, layerFlags);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static boolean nRestore(long nativeCanvas) {
        // FIXME: implement throwOnUnderflow.
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return false;
        }
        canvasDelegate.restore();
        return true;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nRestoreToCount(long nativeCanvas, int saveCount) {
        // FIXME: implement throwOnUnderflow.
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return;
        }
        canvasDelegate.restoreTo(saveCount);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static int nGetSaveCount(long nativeCanvas) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return 0;
        }
        return canvasDelegate.getSnapshot().size();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nTranslate(long nativeCanvas, float dx, float dy) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return;
        }
        canvasDelegate.getSnapshot().translate(dx, dy);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nScale(long nativeCanvas, float sx, float sy) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return;
        }
        canvasDelegate.getSnapshot().scale(sx, sy);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nRotate(long nativeCanvas, float degrees) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return;
        }
        canvasDelegate.getSnapshot().rotate(java.lang.Math.toRadians(degrees));
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nSkew(long nativeCanvas, float kx, float ky) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return;
        }
        // get the current top graphics2D object.
        com.android.layoutlib.bridge.impl.GcSnapshot g = canvasDelegate.getSnapshot();
        // get its current matrix
        java.awt.geom.AffineTransform currentTx = g.getTransform();
        // get the AffineTransform for the given skew.
        float[] mtx = android.graphics.Matrix_Delegate.getSkew(kx, ky);
        java.awt.geom.AffineTransform matrixTx = android.graphics.Matrix_Delegate.getAffineTransform(mtx);
        // combine them so that the given matrix is applied after.
        currentTx.preConcatenate(matrixTx);
        // give it to the graphics2D as a new matrix replacing all previous transform
        g.setTransform(currentTx);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nConcat(long nCanvas, long nMatrix) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nCanvas);
        if (canvasDelegate == null) {
            return;
        }
        android.graphics.Matrix_Delegate matrixDelegate = android.graphics.Matrix_Delegate.getDelegate(nMatrix);
        if (matrixDelegate == null) {
            return;
        }
        // get the current top graphics2D object.
        com.android.layoutlib.bridge.impl.GcSnapshot snapshot = canvasDelegate.getSnapshot();
        // get its current matrix
        java.awt.geom.AffineTransform currentTx = snapshot.getTransform();
        // get the AffineTransform of the given matrix
        java.awt.geom.AffineTransform matrixTx = matrixDelegate.getAffineTransform();
        // combine them so that the given matrix is applied after.
        currentTx.concatenate(matrixTx);
        // give it to the graphics2D as a new matrix replacing all previous transform
        snapshot.setTransform(currentTx);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nSetMatrix(long nCanvas, long nMatrix) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nCanvas);
        if (canvasDelegate == null) {
            return;
        }
        android.graphics.Matrix_Delegate matrixDelegate = android.graphics.Matrix_Delegate.getDelegate(nMatrix);
        if (matrixDelegate == null) {
            return;
        }
        // get the current top graphics2D object.
        com.android.layoutlib.bridge.impl.GcSnapshot snapshot = canvasDelegate.getSnapshot();
        // get the AffineTransform of the given matrix
        java.awt.geom.AffineTransform matrixTx = matrixDelegate.getAffineTransform();
        // give it to the graphics2D as a new matrix replacing all previous transform
        snapshot.setTransform(matrixTx);
        if (matrixDelegate.hasPerspective()) {
            assert false;
            /* data */
            com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_MATRIX_AFFINE, "android.graphics.Canvas#setMatrix(android.graphics.Matrix) only " + "supports affine transformations.", null, null);
        }
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static boolean nClipRect(long nCanvas, float left, float top, float right, float bottom, int regionOp) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nCanvas);
        if (canvasDelegate == null) {
            return false;
        }
        return canvasDelegate.clipRect(left, top, right, bottom, regionOp);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static boolean nClipPath(long nativeCanvas, long nativePath, int regionOp) {
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return true;
        }
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.getDelegate(nativePath);
        if (pathDelegate == null) {
            return true;
        }
        return canvasDelegate.mSnapshot.clip(pathDelegate.getJavaShape(), regionOp);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nSetDrawFilter(long nativeCanvas, long nativeFilter) {
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return;
        }
        canvasDelegate.mDrawFilter = android.graphics.DrawFilter_Delegate.getDelegate(nativeFilter);
        if ((canvasDelegate.mDrawFilter != null) && (!canvasDelegate.mDrawFilter.isSupported())) {
            /* data */
            com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_DRAWFILTER, canvasDelegate.mDrawFilter.getSupportMessage(), null, null);
        }
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static boolean nGetClipBounds(long nativeCanvas, android.graphics.Rect bounds) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return false;
        }
        java.awt.Rectangle rect = canvasDelegate.getSnapshot().getClip().getBounds();
        if ((rect != null) && (!rect.isEmpty())) {
            bounds.left = rect.x;
            bounds.top = rect.y;
            bounds.right = rect.x + rect.width;
            bounds.bottom = rect.y + rect.height;
            return true;
        }
        return false;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nGetMatrix(long canvas, long matrix) {
        // get the delegate from the native int.
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(canvas);
        if (canvasDelegate == null) {
            return;
        }
        android.graphics.Matrix_Delegate matrixDelegate = android.graphics.Matrix_Delegate.getDelegate(matrix);
        if (matrixDelegate == null) {
            return;
        }
        java.awt.geom.AffineTransform transform = canvasDelegate.getSnapshot().getTransform();
        matrixDelegate.set(android.graphics.Matrix_Delegate.makeValues(transform));
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static boolean nQuickReject(long nativeCanvas, long path) {
        // FIXME properly implement quickReject
        return false;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static boolean nQuickReject(long nativeCanvas, float left, float top, float right, float bottom) {
        // FIXME properly implement quickReject
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetNativeFinalizer() {
        synchronized(android.graphics.Canvas_Delegate.class) {
            if (android.graphics.Canvas_Delegate.sFinalizer == (-1)) {
                android.graphics.Canvas_Delegate.sFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(( nativePtr) -> {
                    android.graphics.Canvas_Delegate delegate = android.graphics.Canvas_Delegate.getDelegate(nativePtr);
                    if (delegate != null) {
                        delegate.dispose();
                    }
                    android.graphics.BaseCanvas_Delegate.sManager.removeJavaReferenceFor(nativePtr);
                });
            }
        }
        return android.graphics.Canvas_Delegate.sFinalizer;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetCompatibilityVersion(int apiLevel) {
        // Unsupported by layoutlib, do nothing
    }

    private Canvas_Delegate(android.graphics.Bitmap_Delegate bitmap) {
        super(bitmap);
    }

    private Canvas_Delegate() {
        super();
    }
}

