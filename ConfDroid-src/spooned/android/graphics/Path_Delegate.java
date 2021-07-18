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
 * Delegate implementing the native methods of android.graphics.Path
 *
 * Through the layoutlib_create tool, the original native methods of Path have been replaced
 * by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original Path class.
 *
 * @see DelegateManager
 */
public final class Path_Delegate {
    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.Path_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.Path_Delegate>(android.graphics.Path_Delegate.class);

    private static final float EPSILON = 1.0E-4F;

    private static long sFinalizer = -1;

    // ---- delegate data ----
    private android.graphics.Path.FillType mFillType = android.graphics.Path.FillType.WINDING;

    private java.awt.geom.Path2D mPath = new java.awt.geom.Path2D.Double();

    private float mLastX = 0;

    private float mLastY = 0;

    // true if the path contains does not contain a curve or line.
    private boolean mCachedIsEmpty = true;

    // ---- Public Helper methods ----
    public static android.graphics.Path_Delegate getDelegate(long nPath) {
        return android.graphics.Path_Delegate.sManager.getDelegate(nPath);
    }

    public java.awt.geom.Path2D getJavaShape() {
        return mPath;
    }

    public void setJavaShape(java.awt.Shape shape) {
        reset();
        /* connect */
        mPath.append(shape, false);
    }

    public void reset() {
        mPath.reset();
        mLastX = 0;
        mLastY = 0;
    }

    public void setPathIterator(java.awt.geom.PathIterator iterator) {
        reset();
        /* connect */
        mPath.append(iterator, false);
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nInit() {
        // create the delegate
        android.graphics.Path_Delegate newDelegate = new android.graphics.Path_Delegate();
        return android.graphics.Path_Delegate.sManager.addNewDelegate(newDelegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nInit(long nPath) {
        // create the delegate
        android.graphics.Path_Delegate newDelegate = new android.graphics.Path_Delegate();
        // get the delegate to copy, which could be null if nPath is 0
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate != null) {
            newDelegate.set(pathDelegate);
        }
        return android.graphics.Path_Delegate.sManager.addNewDelegate(newDelegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nReset(long nPath) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.reset();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nRewind(long nPath) {
        // call out to reset since there's nothing to optimize in
        // terms of data structs.
        android.graphics.Path_Delegate.nReset(nPath);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSet(long native_dst, long nSrc) {
        android.graphics.Path_Delegate pathDstDelegate = android.graphics.Path_Delegate.sManager.getDelegate(native_dst);
        if (pathDstDelegate == null) {
            return;
        }
        android.graphics.Path_Delegate pathSrcDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nSrc);
        if (pathSrcDelegate == null) {
            return;
        }
        pathDstDelegate.set(pathSrcDelegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nIsConvex(long nPath) {
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Path.isConvex is not supported.", null, null);
        return true;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetFillType(long nPath) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return 0;
        }
        return pathDelegate.mFillType.nativeInt;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nSetFillType(long nPath, int ft) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.setFillType(android.graphics.Path.sFillTypeArray[ft]);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nIsEmpty(long nPath) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        return (pathDelegate == null) || pathDelegate.isEmpty();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nIsRect(long nPath, android.graphics.RectF rect) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return false;
        }
        // create an Area that can test if the path is a rect
        java.awt.geom.Area area = new java.awt.geom.Area(pathDelegate.mPath);
        if (area.isRectangular()) {
            if (rect != null) {
                pathDelegate.fillBounds(rect);
            }
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nComputeBounds(long nPath, android.graphics.RectF bounds) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.fillBounds(bounds);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nIncReserve(long nPath, int extraPtCount) {
        // since we use a java2D path, there's no way to pre-allocate new points,
        // so we do nothing.
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nMoveTo(long nPath, float x, float y) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.moveTo(x, y);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nRMoveTo(long nPath, float dx, float dy) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.rMoveTo(dx, dy);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nLineTo(long nPath, float x, float y) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.lineTo(x, y);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nRLineTo(long nPath, float dx, float dy) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.rLineTo(dx, dy);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nQuadTo(long nPath, float x1, float y1, float x2, float y2) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.quadTo(x1, y1, x2, y2);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nRQuadTo(long nPath, float dx1, float dy1, float dx2, float dy2) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.rQuadTo(dx1, dy1, dx2, dy2);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nCubicTo(long nPath, float x1, float y1, float x2, float y2, float x3, float y3) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.cubicTo(x1, y1, x2, y2, x3, y3);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nRCubicTo(long nPath, float x1, float y1, float x2, float y2, float x3, float y3) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.rCubicTo(x1, y1, x2, y2, x3, y3);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nArcTo(long nPath, float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.arcTo(left, top, right, bottom, startAngle, sweepAngle, forceMoveTo);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nClose(long nPath) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.close();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddRect(long nPath, float left, float top, float right, float bottom, int dir) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.addRect(left, top, right, bottom, dir);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddOval(long nPath, float left, float top, float right, float bottom, int dir) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.mPath.append(new java.awt.geom.Ellipse2D.Float(left, top, right - left, bottom - top), false);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddCircle(long nPath, float x, float y, float radius, int dir) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        // because x/y is the center of the circle, need to offset this by the radius
        pathDelegate.mPath.append(new java.awt.geom.Ellipse2D.Float(x - radius, y - radius, radius * 2, radius * 2), false);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddArc(long nPath, float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        // because x/y is the center of the circle, need to offset this by the radius
        pathDelegate.mPath.append(new java.awt.geom.Arc2D.Float(left, top, right - left, bottom - top, -startAngle, -sweepAngle, java.awt.geom.Arc2D.OPEN), false);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddRoundRect(long nPath, float left, float top, float right, float bottom, float rx, float ry, int dir) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.mPath.append(new java.awt.geom.RoundRectangle2D.Float(left, top, right - left, bottom - top, rx * 2, ry * 2), false);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddRoundRect(long nPath, float left, float top, float right, float bottom, float[] radii, int dir) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        float[] cornerDimensions = new float[radii.length];
        for (int i = 0; i < radii.length; i++) {
            cornerDimensions[i] = 2 * radii[i];
        }
        pathDelegate.mPath.append(new android.graphics.RoundRectangle(left, top, right - left, bottom - top, cornerDimensions), false);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddPath(long nPath, long src, float dx, float dy) {
        android.graphics.Path_Delegate.addPath(nPath, src, java.awt.geom.AffineTransform.getTranslateInstance(dx, dy));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddPath(long nPath, long src) {
        /* transform */
        android.graphics.Path_Delegate.addPath(nPath, src, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddPath(long nPath, long src, long matrix) {
        android.graphics.Matrix_Delegate matrixDelegate = android.graphics.Matrix_Delegate.getDelegate(matrix);
        if (matrixDelegate == null) {
            return;
        }
        android.graphics.Path_Delegate.addPath(nPath, src, matrixDelegate.getAffineTransform());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nOffset(long nPath, float dx, float dy) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.offset(dx, dy);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetLastPoint(long nPath, float dx, float dy) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        pathDelegate.mLastX = dx;
        pathDelegate.mLastY = dy;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nTransform(long nPath, long matrix, long dst_path) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return;
        }
        android.graphics.Matrix_Delegate matrixDelegate = android.graphics.Matrix_Delegate.getDelegate(matrix);
        if (matrixDelegate == null) {
            return;
        }
        // this can be null if dst_path is 0
        android.graphics.Path_Delegate dstDelegate = android.graphics.Path_Delegate.sManager.getDelegate(dst_path);
        pathDelegate.transform(matrixDelegate, dstDelegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nTransform(long nPath, long matrix) {
        android.graphics.Path_Delegate.nTransform(nPath, matrix, 0);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nOp(long nPath1, long nPath2, int op, long result) {
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "Path.op() not supported", null);
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetFinalizer() {
        synchronized(android.graphics.Path_Delegate.class) {
            if (android.graphics.Path_Delegate.sFinalizer == (-1)) {
                android.graphics.Path_Delegate.sFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.Path_Delegate.sManager::removeJavaReferenceFor);
            }
        }
        return android.graphics.Path_Delegate.sFinalizer;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float[] nApproximate(long nPath, float error) {
        android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(nPath);
        if (pathDelegate == null) {
            return null;
        }
        // Get a FlatteningIterator
        java.awt.geom.PathIterator iterator = pathDelegate.getJavaShape().getPathIterator(null, error);
        float[] segment = new float[6];
        float totalLength = 0;
        java.util.ArrayList<java.awt.geom.Point2D.Float> points = new java.util.ArrayList<java.awt.geom.Point2D.Float>();
        java.awt.geom.Point2D.Float previousPoint = null;
        while (!iterator.isDone()) {
            int type = iterator.currentSegment(segment);
            java.awt.geom.Point2D.Float currentPoint = new java.awt.geom.Point2D.Float(segment[0], segment[1]);
            // MoveTo shouldn't affect the length
            if ((previousPoint != null) && (type != java.awt.geom.PathIterator.SEG_MOVETO)) {
                totalLength += currentPoint.distance(previousPoint);
            }
            previousPoint = currentPoint;
            points.add(currentPoint);
            iterator.next();
        } 
        int nPoints = points.size();
        float[] result = new float[nPoints * 3];
        previousPoint = null;
        // Distance that we've covered so far. Used to calculate the fraction of the path that
        // we've covered up to this point.
        float walkedDistance = 0.0F;
        for (int i = 0; i < nPoints; i++) {
            java.awt.geom.Point2D.Float point = points.get(i);
            float distance = (previousPoint != null) ? ((float) (previousPoint.distance(point))) : 0.0F;
            walkedDistance += distance;
            result[i * 3] = walkedDistance / totalLength;
            result[(i * 3) + 1] = point.x;
            result[(i * 3) + 2] = point.y;
            previousPoint = point;
        }
        return result;
    }

    // ---- Private helper methods ----
    private void set(android.graphics.Path_Delegate delegate) {
        mPath.reset();
        setFillType(delegate.mFillType);
        /* connect */
        mPath.append(delegate.mPath, false);
    }

    private void setFillType(android.graphics.Path.FillType fillType) {
        mFillType = fillType;
        mPath.setWindingRule(android.graphics.Path_Delegate.getWindingRule(fillType));
    }

    /**
     * Returns the Java2D winding rules matching a given Android {@link FillType}.
     *
     * @param type
     * 		the android fill type
     * @return the matching java2d winding rule.
     */
    private static int getWindingRule(android.graphics.Path.FillType type) {
        switch (type) {
            case WINDING :
            case INVERSE_WINDING :
                return java.awt.geom.GeneralPath.WIND_NON_ZERO;
            case EVEN_ODD :
            case INVERSE_EVEN_ODD :
                return java.awt.geom.GeneralPath.WIND_EVEN_ODD;
            default :
                assert false;
                return java.awt.geom.GeneralPath.WIND_NON_ZERO;
        }
    }

    @android.annotation.NonNull
    private static android.graphics.Path.Direction getDirection(int direction) {
        for (android.graphics.Path.Direction d : android.graphics.Path.Direction.values()) {
            if (direction == d.nativeInt) {
                return d;
            }
        }
        assert false;
        return null;
    }

    public static void addPath(long destPath, long srcPath, java.awt.geom.AffineTransform transform) {
        android.graphics.Path_Delegate destPathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(destPath);
        if (destPathDelegate == null) {
            return;
        }
        android.graphics.Path_Delegate srcPathDelegate = android.graphics.Path_Delegate.sManager.getDelegate(srcPath);
        if (srcPathDelegate == null) {
            return;
        }
        if (transform != null) {
            destPathDelegate.mPath.append(srcPathDelegate.mPath.getPathIterator(transform), false);
        } else {
            destPathDelegate.mPath.append(srcPathDelegate.mPath, false);
        }
    }

    /**
     * Returns whether the path already contains any points.
     * Note that this is different to
     * {@link #isEmpty} because if all elements are {@link PathIterator#SEG_MOVETO},
     * {@link #isEmpty} will return true while hasPoints will return false.
     */
    public boolean hasPoints() {
        return !mPath.getPathIterator(null).isDone();
    }

    /**
     * Returns whether the path is empty (contains no lines or curves).
     *
     * @see Path#isEmpty
     */
    public boolean isEmpty() {
        if (!mCachedIsEmpty) {
            return false;
        }
        float[] coords = new float[6];
        mCachedIsEmpty = java.lang.Boolean.TRUE;
        for (java.awt.geom.PathIterator it = mPath.getPathIterator(null); !it.isDone(); it.next()) {
            int type = it.currentSegment(coords);
            if (type != java.awt.geom.PathIterator.SEG_MOVETO) {
                // Once we know that the path is not empty, we do not need to check again unless
                // Path#reset is called.
                mCachedIsEmpty = false;
                return false;
            }
        }
        return true;
    }

    /**
     * Fills the given {@link RectF} with the path bounds.
     *
     * @param bounds
     * 		the RectF to be filled.
     */
    public void fillBounds(android.graphics.RectF bounds) {
        java.awt.geom.Rectangle2D rect = mPath.getBounds2D();
        bounds.left = ((float) (rect.getMinX()));
        bounds.right = ((float) (rect.getMaxX()));
        bounds.top = ((float) (rect.getMinY()));
        bounds.bottom = ((float) (rect.getMaxY()));
    }

    /**
     * Set the beginning of the next contour to the point (x,y).
     *
     * @param x
     * 		The x-coordinate of the start of a new contour
     * @param y
     * 		The y-coordinate of the start of a new contour
     */
    public void moveTo(float x, float y) {
        mPath.moveTo(mLastX = x, mLastY = y);
    }

    /**
     * Set the beginning of the next contour relative to the last point on the
     * previous contour. If there is no previous contour, this is treated the
     * same as moveTo().
     *
     * @param dx
     * 		The amount to add to the x-coordinate of the end of the
     * 		previous contour, to specify the start of a new contour
     * @param dy
     * 		The amount to add to the y-coordinate of the end of the
     * 		previous contour, to specify the start of a new contour
     */
    public void rMoveTo(float dx, float dy) {
        dx += mLastX;
        dy += mLastY;
        mPath.moveTo(mLastX = dx, mLastY = dy);
    }

    /**
     * Add a line from the last point to the specified point (x,y).
     * If no moveTo() call has been made for this contour, the first point is
     * automatically set to (0,0).
     *
     * @param x
     * 		The x-coordinate of the end of a line
     * @param y
     * 		The y-coordinate of the end of a line
     */
    public void lineTo(float x, float y) {
        if (!hasPoints()) {
            mPath.moveTo(mLastX = 0, mLastY = 0);
        }
        mPath.lineTo(mLastX = x, mLastY = y);
    }

    /**
     * Same as lineTo, but the coordinates are considered relative to the last
     * point on this contour. If there is no previous point, then a moveTo(0,0)
     * is inserted automatically.
     *
     * @param dx
     * 		The amount to add to the x-coordinate of the previous point on
     * 		this contour, to specify a line
     * @param dy
     * 		The amount to add to the y-coordinate of the previous point on
     * 		this contour, to specify a line
     */
    public void rLineTo(float dx, float dy) {
        if (!hasPoints()) {
            mPath.moveTo(mLastX = 0, mLastY = 0);
        }
        if ((java.lang.Math.abs(dx) < android.graphics.Path_Delegate.EPSILON) && (java.lang.Math.abs(dy) < android.graphics.Path_Delegate.EPSILON)) {
            // The delta is so small that this shouldn't generate a line
            return;
        }
        dx += mLastX;
        dy += mLastY;
        mPath.lineTo(mLastX = dx, mLastY = dy);
    }

    /**
     * Add a quadratic bezier from the last point, approaching control point
     * (x1,y1), and ending at (x2,y2). If no moveTo() call has been made for
     * this contour, the first point is automatically set to (0,0).
     *
     * @param x1
     * 		The x-coordinate of the control point on a quadratic curve
     * @param y1
     * 		The y-coordinate of the control point on a quadratic curve
     * @param x2
     * 		The x-coordinate of the end point on a quadratic curve
     * @param y2
     * 		The y-coordinate of the end point on a quadratic curve
     */
    public void quadTo(float x1, float y1, float x2, float y2) {
        mPath.quadTo(x1, y1, mLastX = x2, mLastY = y2);
    }

    /**
     * Same as quadTo, but the coordinates are considered relative to the last
     * point on this contour. If there is no previous point, then a moveTo(0,0)
     * is inserted automatically.
     *
     * @param dx1
     * 		The amount to add to the x-coordinate of the last point on
     * 		this contour, for the control point of a quadratic curve
     * @param dy1
     * 		The amount to add to the y-coordinate of the last point on
     * 		this contour, for the control point of a quadratic curve
     * @param dx2
     * 		The amount to add to the x-coordinate of the last point on
     * 		this contour, for the end point of a quadratic curve
     * @param dy2
     * 		The amount to add to the y-coordinate of the last point on
     * 		this contour, for the end point of a quadratic curve
     */
    public void rQuadTo(float dx1, float dy1, float dx2, float dy2) {
        if (!hasPoints()) {
            mPath.moveTo(mLastX = 0, mLastY = 0);
        }
        dx1 += mLastX;
        dy1 += mLastY;
        dx2 += mLastX;
        dy2 += mLastY;
        mPath.quadTo(dx1, dy1, mLastX = dx2, mLastY = dy2);
    }

    /**
     * Add a cubic bezier from the last point, approaching control points
     * (x1,y1) and (x2,y2), and ending at (x3,y3). If no moveTo() call has been
     * made for this contour, the first point is automatically set to (0,0).
     *
     * @param x1
     * 		The x-coordinate of the 1st control point on a cubic curve
     * @param y1
     * 		The y-coordinate of the 1st control point on a cubic curve
     * @param x2
     * 		The x-coordinate of the 2nd control point on a cubic curve
     * @param y2
     * 		The y-coordinate of the 2nd control point on a cubic curve
     * @param x3
     * 		The x-coordinate of the end point on a cubic curve
     * @param y3
     * 		The y-coordinate of the end point on a cubic curve
     */
    public void cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        if (!hasPoints()) {
            mPath.moveTo(0, 0);
        }
        mPath.curveTo(x1, y1, x2, y2, mLastX = x3, mLastY = y3);
    }

    /**
     * Same as cubicTo, but the coordinates are considered relative to the
     * current point on this contour. If there is no previous point, then a
     * moveTo(0,0) is inserted automatically.
     */
    public void rCubicTo(float dx1, float dy1, float dx2, float dy2, float dx3, float dy3) {
        if (!hasPoints()) {
            mPath.moveTo(mLastX = 0, mLastY = 0);
        }
        dx1 += mLastX;
        dy1 += mLastY;
        dx2 += mLastX;
        dy2 += mLastY;
        dx3 += mLastX;
        dy3 += mLastY;
        mPath.curveTo(dx1, dy1, dx2, dy2, mLastX = dx3, mLastY = dy3);
    }

    /**
     * Append the specified arc to the path as a new contour. If the start of
     * the path is different from the path's current last point, then an
     * automatic lineTo() is added to connect the current contour to the
     * start of the arc. However, if the path is empty, then we call moveTo()
     * with the first point of the arc. The sweep angle is tread mod 360.
     *
     * @param left
     * 		The left of oval defining shape and size of the arc
     * @param top
     * 		The top of oval defining shape and size of the arc
     * @param right
     * 		The right of oval defining shape and size of the arc
     * @param bottom
     * 		The bottom of oval defining shape and size of the arc
     * @param startAngle
     * 		Starting angle (in degrees) where the arc begins
     * @param sweepAngle
     * 		Sweep angle (in degrees) measured clockwise, treated
     * 		mod 360.
     * @param forceMoveTo
     * 		If true, always begin a new contour with the arc
     */
    public void arcTo(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo) {
        java.awt.geom.Arc2D arc = new java.awt.geom.Arc2D.Float(left, top, right - left, bottom - top, -startAngle, -sweepAngle, java.awt.geom.Arc2D.OPEN);
        /* connect */
        mPath.append(arc, true);
        resetLastPointFromPath();
    }

    /**
     * Close the current contour. If the current point is not equal to the
     * first point of the contour, a line segment is automatically added.
     */
    public void close() {
        mPath.closePath();
    }

    private void resetLastPointFromPath() {
        java.awt.geom.Point2D last = mPath.getCurrentPoint();
        mLastX = ((float) (last.getX()));
        mLastY = ((float) (last.getY()));
    }

    /**
     * Add a closed rectangle contour to the path
     *
     * @param left
     * 		The left side of a rectangle to add to the path
     * @param top
     * 		The top of a rectangle to add to the path
     * @param right
     * 		The right side of a rectangle to add to the path
     * @param bottom
     * 		The bottom of a rectangle to add to the path
     * @param dir
     * 		The direction to wind the rectangle's contour
     */
    public void addRect(float left, float top, float right, float bottom, int dir) {
        moveTo(left, top);
        android.graphics.Path.Direction direction = android.graphics.Path_Delegate.getDirection(dir);
        switch (direction) {
            case CW :
                lineTo(right, top);
                lineTo(right, bottom);
                lineTo(left, bottom);
                break;
            case CCW :
                lineTo(left, bottom);
                lineTo(right, bottom);
                lineTo(right, top);
                break;
        }
        close();
        resetLastPointFromPath();
    }

    /**
     * Offset the path by (dx,dy), returning true on success
     *
     * @param dx
     * 		The amount in the X direction to offset the entire path
     * @param dy
     * 		The amount in the Y direction to offset the entire path
     */
    public void offset(float dx, float dy) {
        java.awt.geom.GeneralPath newPath = new java.awt.geom.GeneralPath();
        java.awt.geom.PathIterator iterator = mPath.getPathIterator(new java.awt.geom.AffineTransform(0, 0, dx, 0, 0, dy));
        /* connect */
        newPath.append(iterator, false);
        mPath = newPath;
    }

    /**
     * Transform the points in this path by matrix, and write the answer
     * into dst. If dst is null, then the the original path is modified.
     *
     * @param matrix
     * 		The matrix to apply to the path
     * @param dst
     * 		The transformed path is written here. If dst is null,
     * 		then the the original path is modified
     */
    public void transform(android.graphics.Matrix_Delegate matrix, android.graphics.Path_Delegate dst) {
        if (matrix.hasPerspective()) {
            assert false;
            /* data */
            com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_MATRIX_AFFINE, "android.graphics.Path#transform() only " + "supports affine transformations.", null, null);
        }
        java.awt.geom.GeneralPath newPath = new java.awt.geom.GeneralPath();
        java.awt.geom.PathIterator iterator = mPath.getPathIterator(matrix.getAffineTransform());
        /* connect */
        newPath.append(iterator, false);
        if (dst != null) {
            dst.mPath = newPath;
        } else {
            mPath = newPath;
        }
    }
}

