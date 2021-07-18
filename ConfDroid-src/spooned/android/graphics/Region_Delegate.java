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
 * Delegate implementing the native methods of android.graphics.Region
 *
 * Through the layoutlib_create tool, the original native methods of Region have been replaced
 * by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original Region class.
 *
 * This also serve as a base class for all Region delegate classes.
 *
 * @see DelegateManager
 */
// ---- Private delegate/helper methods ----
public class Region_Delegate {
    // ---- delegate manager ----
    protected static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.Region_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.Region_Delegate>(android.graphics.Region_Delegate.class);

    // ---- delegate helper data ----
    // ---- delegate data ----
    private java.awt.geom.Area mArea = new java.awt.geom.Area();

    // ---- Public Helper methods ----
    public static android.graphics.Region_Delegate getDelegate(long nativeShader) {
        return android.graphics.Region_Delegate.sManager.getDelegate(nativeShader);
    }

    public java.awt.geom.Area getJavaArea() {
        return mArea;
    }

    /**
     * Combines two {@link Shape} into another one (actually an {@link Area}), according
     * to the given {@link Region.Op}.
     *
     * If the Op is not one that combines two shapes, then this return null
     *
     * @param shape1
     * 		the firt shape to combine which can be null if there's no original clip.
     * @param shape2
     * 		the 2nd shape to combine
     * @param regionOp
     * 		the operande for the combine
     * @return a new area or null.
     */
    public static java.awt.geom.Area combineShapes(java.awt.Shape shape1, java.awt.Shape shape2, int regionOp) {
        if (regionOp == android.graphics.Region.Op.DIFFERENCE.nativeInt) {
            // if shape1 is null (empty), then the result is null.
            if (shape1 == null) {
                return null;
            }
            // result is always a new area.
            java.awt.geom.Area result = new java.awt.geom.Area(shape1);
            result.subtract(shape2 instanceof java.awt.geom.Area ? ((java.awt.geom.Area) (shape2)) : new java.awt.geom.Area(shape2));
            return result;
        } else
            if (regionOp == android.graphics.Region.Op.INTERSECT.nativeInt) {
                // if shape1 is null, then the result is simply shape2.
                if (shape1 == null) {
                    return new java.awt.geom.Area(shape2);
                }
                // result is always a new area.
                java.awt.geom.Area result = new java.awt.geom.Area(shape1);
                result.intersect(shape2 instanceof java.awt.geom.Area ? ((java.awt.geom.Area) (shape2)) : new java.awt.geom.Area(shape2));
                return result;
            } else
                if (regionOp == android.graphics.Region.Op.UNION.nativeInt) {
                    // if shape1 is null, then the result is simply shape2.
                    if (shape1 == null) {
                        return new java.awt.geom.Area(shape2);
                    }
                    // result is always a new area.
                    java.awt.geom.Area result = new java.awt.geom.Area(shape1);
                    result.add(shape2 instanceof java.awt.geom.Area ? ((java.awt.geom.Area) (shape2)) : new java.awt.geom.Area(shape2));
                    return result;
                } else
                    if (regionOp == android.graphics.Region.Op.XOR.nativeInt) {
                        // if shape1 is null, then the result is simply shape2
                        if (shape1 == null) {
                            return new java.awt.geom.Area(shape2);
                        }
                        // result is always a new area.
                        java.awt.geom.Area result = new java.awt.geom.Area(shape1);
                        result.exclusiveOr(shape2 instanceof java.awt.geom.Area ? ((java.awt.geom.Area) (shape2)) : new java.awt.geom.Area(shape2));
                        return result;
                    } else
                        if (regionOp == android.graphics.Region.Op.REVERSE_DIFFERENCE.nativeInt) {
                            // result is always a new area.
                            java.awt.geom.Area result = new java.awt.geom.Area(shape2);
                            if (shape1 != null) {
                                result.subtract(shape1 instanceof java.awt.geom.Area ? ((java.awt.geom.Area) (shape1)) : new java.awt.geom.Area(shape1));
                            }
                            return result;
                        }




        return null;
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean isEmpty(android.graphics.Region thisRegion) {
        android.graphics.Region_Delegate regionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(thisRegion.mNativeRegion);
        if (regionDelegate == null) {
            return true;
        }
        return regionDelegate.mArea.isEmpty();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean isRect(android.graphics.Region thisRegion) {
        android.graphics.Region_Delegate regionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(thisRegion.mNativeRegion);
        if (regionDelegate == null) {
            return true;
        }
        return regionDelegate.mArea.isRectangular();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean isComplex(android.graphics.Region thisRegion) {
        android.graphics.Region_Delegate regionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(thisRegion.mNativeRegion);
        if (regionDelegate == null) {
            return true;
        }
        return regionDelegate.mArea.isSingular() == false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean contains(android.graphics.Region thisRegion, int x, int y) {
        android.graphics.Region_Delegate regionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(thisRegion.mNativeRegion);
        if (regionDelegate == null) {
            return false;
        }
        return regionDelegate.mArea.contains(x, y);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean quickContains(android.graphics.Region thisRegion, int left, int top, int right, int bottom) {
        android.graphics.Region_Delegate regionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(thisRegion.mNativeRegion);
        if (regionDelegate == null) {
            return false;
        }
        return regionDelegate.mArea.isRectangular() && regionDelegate.mArea.contains(left, top, right - left, bottom - top);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean quickReject(android.graphics.Region thisRegion, int left, int top, int right, int bottom) {
        android.graphics.Region_Delegate regionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(thisRegion.mNativeRegion);
        if (regionDelegate == null) {
            return false;
        }
        return regionDelegate.mArea.isEmpty() || (regionDelegate.mArea.intersects(left, top, right - left, bottom - top) == false);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean quickReject(android.graphics.Region thisRegion, android.graphics.Region rgn) {
        android.graphics.Region_Delegate regionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(thisRegion.mNativeRegion);
        if (regionDelegate == null) {
            return false;
        }
        android.graphics.Region_Delegate targetRegionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(rgn.mNativeRegion);
        if (targetRegionDelegate == null) {
            return false;
        }
        return regionDelegate.mArea.isEmpty() || (regionDelegate.mArea.getBounds().intersects(targetRegionDelegate.mArea.getBounds()) == false);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void translate(android.graphics.Region thisRegion, int dx, int dy, android.graphics.Region dst) {
        android.graphics.Region_Delegate regionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(thisRegion.mNativeRegion);
        if (regionDelegate == null) {
            return;
        }
        android.graphics.Region_Delegate targetRegionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(dst.mNativeRegion);
        if (targetRegionDelegate == null) {
            return;
        }
        if (regionDelegate.mArea.isEmpty()) {
            targetRegionDelegate.mArea = new java.awt.geom.Area();
        } else {
            targetRegionDelegate.mArea = new java.awt.geom.Area(regionDelegate.mArea);
            java.awt.geom.AffineTransform mtx = new java.awt.geom.AffineTransform();
            mtx.translate(dx, dy);
            targetRegionDelegate.mArea.transform(mtx);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void scale(android.graphics.Region thisRegion, float scale, android.graphics.Region dst) {
        android.graphics.Region_Delegate regionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(thisRegion.mNativeRegion);
        if (regionDelegate == null) {
            return;
        }
        android.graphics.Region_Delegate targetRegionDelegate = android.graphics.Region_Delegate.sManager.getDelegate(dst.mNativeRegion);
        if (targetRegionDelegate == null) {
            return;
        }
        if (regionDelegate.mArea.isEmpty()) {
            targetRegionDelegate.mArea = new java.awt.geom.Area();
        } else {
            targetRegionDelegate.mArea = new java.awt.geom.Area(regionDelegate.mArea);
            java.awt.geom.AffineTransform mtx = new java.awt.geom.AffineTransform();
            mtx.scale(scale, scale);
            targetRegionDelegate.mArea.transform(mtx);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeConstructor() {
        android.graphics.Region_Delegate newDelegate = new android.graphics.Region_Delegate();
        return android.graphics.Region_Delegate.sManager.addNewDelegate(newDelegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeDestructor(long native_region) {
        android.graphics.Region_Delegate.sManager.removeJavaReferenceFor(native_region);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeSetRegion(long native_dst, long native_src) {
        android.graphics.Region_Delegate dstRegion = android.graphics.Region_Delegate.sManager.getDelegate(native_dst);
        if (dstRegion == null) {
            return;
        }
        android.graphics.Region_Delegate srcRegion = android.graphics.Region_Delegate.sManager.getDelegate(native_src);
        if (srcRegion == null) {
            return;
        }
        dstRegion.mArea.reset();
        dstRegion.mArea.add(srcRegion.mArea);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeSetRect(long native_dst, int left, int top, int right, int bottom) {
        android.graphics.Region_Delegate dstRegion = android.graphics.Region_Delegate.sManager.getDelegate(native_dst);
        if (dstRegion == null) {
            return true;
        }
        dstRegion.mArea = new java.awt.geom.Area(new java.awt.geom.Rectangle2D.Float(left, top, right - left, bottom - top));
        return dstRegion.mArea.getBounds().isEmpty() == false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeSetPath(long native_dst, long native_path, long native_clip) {
        android.graphics.Region_Delegate dstRegion = android.graphics.Region_Delegate.sManager.getDelegate(native_dst);
        if (dstRegion == null) {
            return true;
        }
        android.graphics.Path_Delegate path = android.graphics.Path_Delegate.getDelegate(native_path);
        if (path == null) {
            return true;
        }
        dstRegion.mArea = new java.awt.geom.Area(path.getJavaShape());
        android.graphics.Region_Delegate clip = android.graphics.Region_Delegate.sManager.getDelegate(native_clip);
        if (clip != null) {
            dstRegion.mArea.subtract(clip.getJavaArea());
        }
        return dstRegion.mArea.getBounds().isEmpty() == false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeGetBounds(long native_region, android.graphics.Rect rect) {
        android.graphics.Region_Delegate region = android.graphics.Region_Delegate.sManager.getDelegate(native_region);
        if (region == null) {
            return true;
        }
        java.awt.Rectangle bounds = region.mArea.getBounds();
        if (bounds.isEmpty()) {
            rect.left = rect.top = rect.right = rect.bottom = 0;
            return false;
        }
        rect.left = bounds.x;
        rect.top = bounds.y;
        rect.right = bounds.x + bounds.width;
        rect.bottom = bounds.y + bounds.height;
        return true;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeGetBoundaryPath(long native_region, long native_path) {
        android.graphics.Region_Delegate region = android.graphics.Region_Delegate.sManager.getDelegate(native_region);
        if (region == null) {
            return false;
        }
        android.graphics.Path_Delegate path = android.graphics.Path_Delegate.getDelegate(native_path);
        if (path == null) {
            return false;
        }
        if (region.mArea.isEmpty()) {
            path.reset();
            return false;
        }
        path.setPathIterator(region.mArea.getPathIterator(new java.awt.geom.AffineTransform()));
        return true;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeOp(long native_dst, int left, int top, int right, int bottom, int op) {
        android.graphics.Region_Delegate region = android.graphics.Region_Delegate.sManager.getDelegate(native_dst);
        if (region == null) {
            return false;
        }
        region.mArea = android.graphics.Region_Delegate.combineShapes(region.mArea, new java.awt.geom.Rectangle2D.Float(left, top, right - left, bottom - top), op);
        assert region.mArea != null;
        if (region.mArea != null) {
            region.mArea = new java.awt.geom.Area();
        }
        return region.mArea.getBounds().isEmpty() == false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeOp(long native_dst, android.graphics.Rect rect, long native_region, int op) {
        android.graphics.Region_Delegate region = android.graphics.Region_Delegate.sManager.getDelegate(native_dst);
        if (region == null) {
            return false;
        }
        region.mArea = android.graphics.Region_Delegate.combineShapes(region.mArea, new java.awt.geom.Rectangle2D.Float(rect.left, rect.top, rect.width(), rect.height()), op);
        assert region.mArea != null;
        if (region.mArea != null) {
            region.mArea = new java.awt.geom.Area();
        }
        return region.mArea.getBounds().isEmpty() == false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeOp(long native_dst, long native_region1, long native_region2, int op) {
        android.graphics.Region_Delegate dstRegion = android.graphics.Region_Delegate.sManager.getDelegate(native_dst);
        if (dstRegion == null) {
            return true;
        }
        android.graphics.Region_Delegate region1 = android.graphics.Region_Delegate.sManager.getDelegate(native_region1);
        if (region1 == null) {
            return false;
        }
        android.graphics.Region_Delegate region2 = android.graphics.Region_Delegate.sManager.getDelegate(native_region2);
        if (region2 == null) {
            return false;
        }
        dstRegion.mArea = android.graphics.Region_Delegate.combineShapes(region1.mArea, region2.mArea, op);
        assert dstRegion.mArea != null;
        if (dstRegion.mArea != null) {
            dstRegion.mArea = new java.awt.geom.Area();
        }
        return dstRegion.mArea.getBounds().isEmpty() == false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeCreateFromParcel(android.os.Parcel p) {
        // This is only called by Region.CREATOR (Parcelable.Creator<Region>), which is only
        // used during aidl call so really this should not be called.
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "AIDL is not suppored, and therefore Regions cannot be created from parcels.", null);
        return 0;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeWriteToParcel(long native_region, android.os.Parcel p) {
        // This is only called when sending a region through aidl, so really this should not
        // be called.
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "AIDL is not suppored, and therefore Regions cannot be written to parcels.", null);
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeEquals(long native_r1, long native_r2) {
        android.graphics.Region_Delegate region1 = android.graphics.Region_Delegate.sManager.getDelegate(native_r1);
        if (region1 == null) {
            return false;
        }
        android.graphics.Region_Delegate region2 = android.graphics.Region_Delegate.sManager.getDelegate(native_r2);
        if (region2 == null) {
            return false;
        }
        return region1.mArea.equals(region2.mArea);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String nativeToString(long native_region) {
        android.graphics.Region_Delegate region = android.graphics.Region_Delegate.sManager.getDelegate(native_region);
        if (region == null) {
            return "not found";
        }
        return region.mArea.toString();
    }
}

