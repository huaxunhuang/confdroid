/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * Delegate implementing the native methods of {@link android.graphics.PathMeasure}
 * <p/>
 * Through the layoutlib_create tool, the original native methods of PathMeasure have been
 * replaced by
 * calls to methods of the same name in this delegate class.
 * <p/>
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between it
 * and the original PathMeasure class.
 *
 * @see DelegateManager
 */
public final class PathMeasure_Delegate {
    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.PathMeasure_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.PathMeasure_Delegate>(android.graphics.PathMeasure_Delegate.class);

    // ---- delegate data ----
    private com.android.layoutlib.bridge.util.CachedPathIteratorFactory mOriginalPathIterator;

    private long mNativePath;

    private PathMeasure_Delegate(long native_path, boolean forceClosed) {
        mNativePath = native_path;
        if (native_path != 0) {
            if (forceClosed) {
                // Copy the path and call close
                native_path = android.graphics.Path_Delegate.nInit(native_path);
                android.graphics.Path_Delegate.nClose(native_path);
            }
            android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.getDelegate(native_path);
            mOriginalPathIterator = new com.android.layoutlib.bridge.util.CachedPathIteratorFactory(pathDelegate.getJavaShape().getPathIterator(null));
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long native_create(long native_path, boolean forceClosed) {
        return android.graphics.PathMeasure_Delegate.sManager.addNewDelegate(new android.graphics.PathMeasure_Delegate(native_path, forceClosed));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void native_destroy(long native_instance) {
        android.graphics.PathMeasure_Delegate.sManager.removeJavaReferenceFor(native_instance);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean native_getPosTan(long native_instance, float distance, float[] pos, float[] tan) {
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "PathMeasure.getPostTan is not supported.", null, null);
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean native_getMatrix(long native_instance, float distance, long native_matrix, int flags) {
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "PathMeasure.getMatrix is not supported.", null, null);
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean native_nextContour(long native_instance) {
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "PathMeasure.nextContour is not supported.", null, null);
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void native_setPath(long native_instance, long native_path, boolean forceClosed) {
        android.graphics.PathMeasure_Delegate pathMeasure = android.graphics.PathMeasure_Delegate.sManager.getDelegate(native_instance);
        assert pathMeasure != null;
        if (native_path != 0) {
            if (forceClosed) {
                // Copy the path and call close
                native_path = android.graphics.Path_Delegate.nInit(native_path);
                android.graphics.Path_Delegate.nClose(native_path);
            }
            android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.getDelegate(native_path);
            pathMeasure.mOriginalPathIterator = new com.android.layoutlib.bridge.util.CachedPathIteratorFactory(pathDelegate.getJavaShape().getPathIterator(null));
        }
        pathMeasure.mNativePath = native_path;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float native_getLength(long native_instance) {
        android.graphics.PathMeasure_Delegate pathMeasure = android.graphics.PathMeasure_Delegate.sManager.getDelegate(native_instance);
        assert pathMeasure != null;
        if (pathMeasure.mOriginalPathIterator == null) {
            return 0;
        }
        return pathMeasure.mOriginalPathIterator.iterator().getTotalLength();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean native_isClosed(long native_instance) {
        android.graphics.PathMeasure_Delegate pathMeasure = android.graphics.PathMeasure_Delegate.sManager.getDelegate(native_instance);
        assert pathMeasure != null;
        android.graphics.Path_Delegate path = android.graphics.Path_Delegate.getDelegate(pathMeasure.mNativePath);
        if (path == null) {
            return false;
        }
        int type = 0;
        float[] segment = new float[6];
        for (java.awt.geom.PathIterator pi = path.getJavaShape().getPathIterator(null); !pi.isDone(); pi.next()) {
            type = pi.currentSegment(segment);
        }
        // A path is a closed path if the last element is SEG_CLOSE
        return type == java.awt.geom.PathIterator.SEG_CLOSE;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean native_getSegment(long native_instance, float startD, float stopD, long native_dst_path, boolean startWithMoveTo) {
        if (startD < 0) {
            startD = 0;
        }
        if (startD >= stopD) {
            return false;
        }
        android.graphics.PathMeasure_Delegate pathMeasure = android.graphics.PathMeasure_Delegate.sManager.getDelegate(native_instance);
        assert pathMeasure != null;
        com.android.layoutlib.bridge.util.CachedPathIteratorFactory.CachedPathIterator iterator = pathMeasure.mOriginalPathIterator.iterator();
        float accLength = startD;
        boolean isZeroLength = true;// Whether the output has zero length or not

        float[] points = new float[6];
        iterator.jumpToSegment(accLength);
        while ((!iterator.isDone()) && ((stopD - accLength) > 0.1F)) {
            int type = iterator.currentSegment(points, stopD - accLength);
            if ((accLength - iterator.getCurrentSegmentLength()) <= stopD) {
                if (startWithMoveTo) {
                    startWithMoveTo = false;
                    // If this segment is a MOVETO, then we just use that one. If not, then we issue
                    // a first moveto
                    if (type != java.awt.geom.PathIterator.SEG_MOVETO) {
                        float[] lastPoint = new float[2];
                        iterator.getCurrentSegmentEnd(lastPoint);
                        android.graphics.Path_Delegate.nMoveTo(native_dst_path, lastPoint[0], lastPoint[1]);
                    }
                }
                isZeroLength = isZeroLength && (iterator.getCurrentSegmentLength() > 0);
                switch (type) {
                    case java.awt.geom.PathIterator.SEG_MOVETO :
                        android.graphics.Path_Delegate.nMoveTo(native_dst_path, points[0], points[1]);
                        break;
                    case java.awt.geom.PathIterator.SEG_LINETO :
                        android.graphics.Path_Delegate.nLineTo(native_dst_path, points[0], points[1]);
                        break;
                    case java.awt.geom.PathIterator.SEG_CLOSE :
                        android.graphics.Path_Delegate.nClose(native_dst_path);
                        break;
                    case java.awt.geom.PathIterator.SEG_CUBICTO :
                        android.graphics.Path_Delegate.nCubicTo(native_dst_path, points[0], points[1], points[2], points[3], points[4], points[5]);
                        break;
                    case java.awt.geom.PathIterator.SEG_QUADTO :
                        android.graphics.Path_Delegate.nQuadTo(native_dst_path, points[0], points[1], points[2], points[3]);
                        break;
                    default :
                        assert false;
                }
            }
            accLength += iterator.getCurrentSegmentLength();
            iterator.next();
        } 
        return !isZeroLength;
    }
}

