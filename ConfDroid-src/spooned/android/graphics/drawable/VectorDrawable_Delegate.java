/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.graphics.drawable;


/**
 * Delegate used to provide new implementation of a select few methods of {@link VectorDrawable}
 * <p>
 * Through the layoutlib_create tool, the original  methods of VectorDrawable have been replaced by
 * calls to methods of the same name in this delegate class.
 */
@java.lang.SuppressWarnings("unused")
public class VectorDrawable_Delegate {
    private static final java.lang.String LOGTAG = android.graphics.drawable.VectorDrawable_Delegate.class.getSimpleName();

    private static final boolean DBG_VECTOR_DRAWABLE = false;

    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.drawable.VectorDrawable_Delegate.VNativeObject> sPathManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.class);

    private static long addNativeObject(android.graphics.drawable.VectorDrawable_Delegate.VNativeObject object) {
        long ptr = android.graphics.drawable.VectorDrawable_Delegate.sPathManager.addNewDelegate(object);
        object.setNativePtr(ptr);
        return ptr;
    }

    /**
     * Obtains styled attributes from the theme, if available, or unstyled resources if the theme is
     * null.
     */
    private static android.content.res.TypedArray obtainAttributes(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    private static int applyAlpha(int color, float alpha) {
        int alphaBytes = android.graphics.Color.alpha(color);
        color &= 0xffffff;
        color |= ((int) (alphaBytes * alpha)) << 24;
        return color;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateTree(long rootGroupPtr) {
        return android.graphics.drawable.VectorDrawable_Delegate.addNativeObject(new android.graphics.drawable.VectorDrawable_Delegate.VPathRenderer_Delegate(rootGroupPtr));
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateTreeFromCopy(long rendererToCopyPtr, long rootGroupPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VPathRenderer_Delegate rendererToCopy = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(rendererToCopyPtr);
        return android.graphics.drawable.VectorDrawable_Delegate.addNativeObject(new android.graphics.drawable.VectorDrawable_Delegate.VPathRenderer_Delegate(rendererToCopy, rootGroupPtr));
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetRendererViewportSize(long rendererPtr, float viewportWidth, float viewportHeight) {
        android.graphics.drawable.VectorDrawable_Delegate.VPathRenderer_Delegate nativePathRenderer = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(rendererPtr);
        nativePathRenderer.mViewportWidth = viewportWidth;
        nativePathRenderer.mViewportHeight = viewportHeight;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetRootAlpha(long rendererPtr, float alpha) {
        android.graphics.drawable.VectorDrawable_Delegate.VPathRenderer_Delegate nativePathRenderer = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(rendererPtr);
        nativePathRenderer.setRootAlpha(alpha);
        return true;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetRootAlpha(long rendererPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VPathRenderer_Delegate nativePathRenderer = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(rendererPtr);
        return nativePathRenderer.getRootAlpha();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetAntiAlias(long rendererPtr, boolean aa) {
        android.graphics.drawable.VectorDrawable_Delegate.VPathRenderer_Delegate nativePathRenderer = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(rendererPtr);
        nativePathRenderer.setAntiAlias(aa);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetAllowCaching(long rendererPtr, boolean allowCaching) {
        // ignored
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nDraw(long rendererPtr, long canvasWrapperPtr, long colorFilterPtr, android.graphics.Rect bounds, boolean needsMirroring, boolean canReuseCache) {
        android.graphics.drawable.VectorDrawable_Delegate.VPathRenderer_Delegate nativePathRenderer = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(rendererPtr);
        android.graphics.Canvas_Delegate.nSave(canvasWrapperPtr, android.graphics.Canvas.MATRIX_SAVE_FLAG | android.graphics.Canvas.CLIP_SAVE_FLAG);
        android.graphics.Canvas_Delegate.nClipRect(canvasWrapperPtr, bounds.left, bounds.top, bounds.right, bounds.bottom, android.graphics.Region.Op.INTERSECT.nativeInt);
        android.graphics.Canvas_Delegate.nTranslate(canvasWrapperPtr, bounds.left, bounds.top);
        if (needsMirroring) {
            android.graphics.Canvas_Delegate.nTranslate(canvasWrapperPtr, bounds.width(), 0);
            android.graphics.Canvas_Delegate.nScale(canvasWrapperPtr, -1.0F, 1.0F);
        }
        // At this point, canvas has been translated to the right position.
        // And we use this bound for the destination rect for the drawBitmap, so
        // we offset to (0, 0);
        bounds.offsetTo(0, 0);
        nativePathRenderer.draw(canvasWrapperPtr, colorFilterPtr, bounds.width(), bounds.height());
        android.graphics.Canvas_Delegate.nRestore(canvasWrapperPtr);
        return bounds.width() * bounds.height();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateFullPath() {
        return android.graphics.drawable.VectorDrawable_Delegate.addNativeObject(new android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate());
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateFullPath(long nativeFullPathPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate original = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(nativeFullPathPtr);
        return android.graphics.drawable.VectorDrawable_Delegate.addNativeObject(new android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate(original));
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nGetFullPathProperties(long pathPtr, byte[] propertiesData, int length) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        java.nio.ByteBuffer properties = java.nio.ByteBuffer.wrap(propertiesData);
        properties.order(java.nio.ByteOrder.nativeOrder());
        properties.putFloat(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.STROKE_WIDTH_INDEX * 4, path.getStrokeWidth());
        properties.putInt(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.STROKE_COLOR_INDEX * 4, path.getStrokeColor());
        properties.putFloat(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.STROKE_ALPHA_INDEX * 4, path.getStrokeAlpha());
        properties.putInt(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.FILL_COLOR_INDEX * 4, path.getFillColor());
        properties.putFloat(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.FILL_ALPHA_INDEX * 4, path.getStrokeAlpha());
        properties.putFloat(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.TRIM_PATH_START_INDEX * 4, path.getTrimPathStart());
        properties.putFloat(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.TRIM_PATH_END_INDEX * 4, path.getTrimPathEnd());
        properties.putFloat(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.TRIM_PATH_OFFSET_INDEX * 4, path.getTrimPathOffset());
        properties.putInt(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.STROKE_LINE_CAP_INDEX * 4, path.getStrokeLineCap());
        properties.putInt(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.STROKE_LINE_JOIN_INDEX * 4, path.getStrokeLineJoin());
        properties.putFloat(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.STROKE_MITER_LIMIT_INDEX * 4, path.getStrokeMiterlimit());
        properties.putInt(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.FILL_TYPE_INDEX * 4, path.getFillType());
        return true;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nUpdateFullPathProperties(long pathPtr, float strokeWidth, int strokeColor, float strokeAlpha, int fillColor, float fillAlpha, float trimPathStart, float trimPathEnd, float trimPathOffset, float strokeMiterLimit, int strokeLineCap, int strokeLineJoin, int fillType) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setStrokeWidth(strokeWidth);
        path.setStrokeColor(strokeColor);
        path.setStrokeAlpha(strokeAlpha);
        path.setFillColor(fillColor);
        path.setFillAlpha(fillAlpha);
        path.setTrimPathStart(trimPathStart);
        path.setTrimPathEnd(trimPathEnd);
        path.setTrimPathOffset(trimPathOffset);
        path.setStrokeMiterlimit(strokeMiterLimit);
        path.setStrokeLineCap(strokeLineCap);
        path.setStrokeLineJoin(strokeLineJoin);
        path.setFillType(fillType);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nUpdateFullPathFillGradient(long pathPtr, long fillGradientPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setFillGradient(fillGradientPtr);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nUpdateFullPathStrokeGradient(long pathPtr, long strokeGradientPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setStrokeGradient(strokeGradientPtr);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateClipPath() {
        return android.graphics.drawable.VectorDrawable_Delegate.addNativeObject(new android.graphics.drawable.VectorDrawable_Delegate.VClipPath_Delegate());
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateClipPath(long clipPathPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VClipPath_Delegate original = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(clipPathPtr);
        return android.graphics.drawable.VectorDrawable_Delegate.addNativeObject(new android.graphics.drawable.VectorDrawable_Delegate.VClipPath_Delegate(original));
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateGroup() {
        return android.graphics.drawable.VectorDrawable_Delegate.addNativeObject(new android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate());
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateGroup(long groupPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate original = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        return android.graphics.drawable.VectorDrawable_Delegate.addNativeObject(new android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate(original, new android.util.ArrayMap()));
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetName(long nodePtr, java.lang.String name) {
        android.graphics.drawable.VectorDrawable_Delegate.VNativeObject group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(nodePtr);
        group.setName(name);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nGetGroupProperties(long groupPtr, float[] propertiesData, int length) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        java.nio.FloatBuffer properties = java.nio.FloatBuffer.wrap(propertiesData);
        properties.put(android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.ROTATE_INDEX, group.getRotation());
        properties.put(android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.PIVOT_X_INDEX, group.getPivotX());
        properties.put(android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.PIVOT_Y_INDEX, group.getPivotY());
        properties.put(android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.SCALE_X_INDEX, group.getScaleX());
        properties.put(android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.SCALE_Y_INDEX, group.getScaleY());
        properties.put(android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.TRANSLATE_X_INDEX, group.getTranslateX());
        properties.put(android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.TRANSLATE_Y_INDEX, group.getTranslateY());
        return true;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nUpdateGroupProperties(long groupPtr, float rotate, float pivotX, float pivotY, float scaleX, float scaleY, float translateX, float translateY) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        group.setRotation(rotate);
        group.setPivotX(pivotX);
        group.setPivotY(pivotY);
        group.setScaleX(scaleX);
        group.setScaleY(scaleY);
        group.setTranslateX(translateX);
        group.setTranslateY(translateY);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddChild(long groupPtr, long nodePtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        group.mChildren.add(android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(nodePtr));
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetPathString(long pathPtr, java.lang.String pathString, int length) {
        android.graphics.drawable.VectorDrawable_Delegate.VPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setPathData(android.util.PathParser_Delegate.createNodesFromPathData(pathString));
    }

    /**
     * The setters and getters below for paths and groups are here temporarily, and will be removed
     * once the animation in AVD is replaced with RenderNodeAnimator, in which case the animation
     * will modify these properties in native. By then no JNI hopping would be necessary for VD
     * during animation, and these setters and getters will be obsolete.
     */
    // Setters and getters during animation.
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetRotation(long groupPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        return group.getRotation();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetRotation(long groupPtr, float rotation) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        group.setRotation(rotation);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetPivotX(long groupPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        return group.getPivotX();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetPivotX(long groupPtr, float pivotX) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        group.setPivotX(pivotX);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetPivotY(long groupPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        return group.getPivotY();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetPivotY(long groupPtr, float pivotY) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        group.setPivotY(pivotY);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetScaleX(long groupPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        return group.getScaleX();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetScaleX(long groupPtr, float scaleX) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        group.setScaleX(scaleX);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetScaleY(long groupPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        return group.getScaleY();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetScaleY(long groupPtr, float scaleY) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        group.setScaleY(scaleY);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTranslateX(long groupPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        return group.getTranslateX();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTranslateX(long groupPtr, float translateX) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        group.setTranslateX(translateX);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTranslateY(long groupPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        return group.getTranslateY();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTranslateY(long groupPtr, float translateY) {
        android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate group = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(groupPtr);
        group.setTranslateY(translateY);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetPathData(long pathPtr, long pathDataPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setPathData(android.util.PathParser_Delegate.getDelegate(pathDataPtr).getPathDataNodes());
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetStrokeWidth(long pathPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        return path.getStrokeWidth();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetStrokeWidth(long pathPtr, float width) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setStrokeWidth(width);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetStrokeColor(long pathPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        return path.getStrokeColor();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetStrokeColor(long pathPtr, int strokeColor) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setStrokeColor(strokeColor);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetStrokeAlpha(long pathPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        return path.getStrokeAlpha();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetStrokeAlpha(long pathPtr, float alpha) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setStrokeAlpha(alpha);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetFillColor(long pathPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        return path.getFillColor();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetFillColor(long pathPtr, int fillColor) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setFillColor(fillColor);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetFillAlpha(long pathPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        return path.getFillAlpha();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetFillAlpha(long pathPtr, float fillAlpha) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setFillAlpha(fillAlpha);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTrimPathStart(long pathPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        return path.getTrimPathStart();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTrimPathStart(long pathPtr, float trimPathStart) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setTrimPathStart(trimPathStart);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTrimPathEnd(long pathPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        return path.getTrimPathEnd();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTrimPathEnd(long pathPtr, float trimPathEnd) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setTrimPathEnd(trimPathEnd);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTrimPathOffset(long pathPtr) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        return path.getTrimPathOffset();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTrimPathOffset(long pathPtr, float trimPathOffset) {
        android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate path = android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(pathPtr);
        path.setTrimPathOffset(trimPathOffset);
    }

    /**
     * Base class for all the internal Delegates that does two functions:
     * <ol>
     *     <li>Serves as base class to store all the delegates in one {@link DelegateManager}
     *     <li>Provides setName for all the classes. {@link VPathRenderer_Delegate} does actually
     *     not need it
     * </ol>
     */
    static abstract class VNativeObject {
        long mNativePtr = 0;

        @android.annotation.NonNull
        static <T> T getDelegate(long nativePtr) {
            // noinspection unchecked
            T vNativeObject = ((T) (android.graphics.drawable.VectorDrawable_Delegate.sPathManager.getDelegate(nativePtr)));
            assert vNativeObject != null;
            return vNativeObject;
        }

        abstract void setName(java.lang.String name);

        void setNativePtr(long nativePtr) {
            mNativePtr = nativePtr;
        }

        /**
         * Method to explicitly dispose native objects
         */
        void dispose() {
        }
    }

    private static class VClipPath_Delegate extends android.graphics.drawable.VectorDrawable_Delegate.VPath_Delegate {
        private VClipPath_Delegate() {
            // Empty constructor.
        }

        private VClipPath_Delegate(android.graphics.drawable.VectorDrawable_Delegate.VClipPath_Delegate copy) {
            super(copy);
        }

        @java.lang.Override
        public boolean isClipPath() {
            return true;
        }
    }

    static class VFullPath_Delegate extends android.graphics.drawable.VectorDrawable_Delegate.VPath_Delegate {
        // These constants need to be kept in sync with their values in VectorDrawable.VFullPath
        private static final int STROKE_WIDTH_INDEX = 0;

        private static final int STROKE_COLOR_INDEX = 1;

        private static final int STROKE_ALPHA_INDEX = 2;

        private static final int FILL_COLOR_INDEX = 3;

        private static final int FILL_ALPHA_INDEX = 4;

        private static final int TRIM_PATH_START_INDEX = 5;

        private static final int TRIM_PATH_END_INDEX = 6;

        private static final int TRIM_PATH_OFFSET_INDEX = 7;

        private static final int STROKE_LINE_CAP_INDEX = 8;

        private static final int STROKE_LINE_JOIN_INDEX = 9;

        private static final int STROKE_MITER_LIMIT_INDEX = 10;

        private static final int FILL_TYPE_INDEX = 11;

        private static final int LINECAP_BUTT = 0;

        private static final int LINECAP_ROUND = 1;

        private static final int LINECAP_SQUARE = 2;

        private static final int LINEJOIN_MITER = 0;

        private static final int LINEJOIN_ROUND = 1;

        private static final int LINEJOIN_BEVEL = 2;

        @android.annotation.NonNull
        public java.util.function.Consumer<java.lang.Float> getFloatPropertySetter(int propertyIdx) {
            switch (propertyIdx) {
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.STROKE_WIDTH_INDEX :
                    return this::setStrokeWidth;
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.STROKE_ALPHA_INDEX :
                    return this::setStrokeAlpha;
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.FILL_ALPHA_INDEX :
                    return this::setFillAlpha;
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.TRIM_PATH_START_INDEX :
                    return this::setTrimPathStart;
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.TRIM_PATH_END_INDEX :
                    return this::setTrimPathEnd;
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.TRIM_PATH_OFFSET_INDEX :
                    return this::setTrimPathOffset;
            }
            assert false : "Invalid VFullPath_Delegate property index " + propertyIdx;
            return ( t) -> {
            };
        }

        @android.annotation.NonNull
        public java.util.function.Consumer<java.lang.Integer> getIntPropertySetter(int propertyIdx) {
            switch (propertyIdx) {
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.STROKE_COLOR_INDEX :
                    return this::setStrokeColor;
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.FILL_COLOR_INDEX :
                    return this::setFillColor;
            }
            assert false : "Invalid VFullPath_Delegate property index " + propertyIdx;
            return ( t) -> {
            };
        }

        // ///////////////////////////////////////////////////
        // Variables below need to be copied (deep copy if applicable) for mutation.
        int mStrokeColor = android.graphics.Color.TRANSPARENT;

        float mStrokeWidth = 0;

        int mFillColor = android.graphics.Color.TRANSPARENT;

        long mStrokeGradient = 0;

        long mFillGradient = 0;

        float mStrokeAlpha = 1.0F;

        float mFillAlpha = 1.0F;

        float mTrimPathStart = 0;

        float mTrimPathEnd = 1;

        float mTrimPathOffset = 0;

        android.graphics.Paint.Cap mStrokeLineCap = android.graphics.Paint.Cap.BUTT;

        android.graphics.Paint.Join mStrokeLineJoin = android.graphics.Paint.Join.MITER;

        float mStrokeMiterlimit = 4;

        int mFillType = 0;// WINDING(0) is the default value. See Path.FillType


        private VFullPath_Delegate() {
            // Empty constructor.
        }

        private VFullPath_Delegate(android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate copy) {
            super(copy);
            mStrokeColor = copy.mStrokeColor;
            mStrokeWidth = copy.mStrokeWidth;
            mStrokeAlpha = copy.mStrokeAlpha;
            mFillColor = copy.mFillColor;
            mFillAlpha = copy.mFillAlpha;
            mTrimPathStart = copy.mTrimPathStart;
            mTrimPathEnd = copy.mTrimPathEnd;
            mTrimPathOffset = copy.mTrimPathOffset;
            mStrokeLineCap = copy.mStrokeLineCap;
            mStrokeLineJoin = copy.mStrokeLineJoin;
            mStrokeMiterlimit = copy.mStrokeMiterlimit;
            mStrokeGradient = copy.mStrokeGradient;
            mFillGradient = copy.mFillGradient;
            mFillType = copy.mFillType;
        }

        private int getStrokeLineCap() {
            switch (mStrokeLineCap) {
                case BUTT :
                    return android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINECAP_BUTT;
                case ROUND :
                    return android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINECAP_ROUND;
                case SQUARE :
                    return android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINECAP_SQUARE;
                default :
                    assert false;
            }
            return -1;
        }

        private void setStrokeLineCap(int cap) {
            switch (cap) {
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINECAP_BUTT :
                    mStrokeLineCap = android.graphics.Paint.Cap.BUTT;
                    break;
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINECAP_ROUND :
                    mStrokeLineCap = android.graphics.Paint.Cap.ROUND;
                    break;
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINECAP_SQUARE :
                    mStrokeLineCap = android.graphics.Paint.Cap.SQUARE;
                    break;
                default :
                    assert false;
            }
        }

        private int getStrokeLineJoin() {
            switch (mStrokeLineJoin) {
                case MITER :
                    return android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINEJOIN_MITER;
                case ROUND :
                    return android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINEJOIN_ROUND;
                case BEVEL :
                    return android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINEJOIN_BEVEL;
                default :
                    assert false;
            }
            return -1;
        }

        private void setStrokeLineJoin(int join) {
            switch (join) {
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINEJOIN_BEVEL :
                    mStrokeLineJoin = android.graphics.Paint.Join.BEVEL;
                    break;
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINEJOIN_MITER :
                    mStrokeLineJoin = android.graphics.Paint.Join.MITER;
                    break;
                case android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate.LINEJOIN_ROUND :
                    mStrokeLineJoin = android.graphics.Paint.Join.ROUND;
                    break;
                default :
                    assert false;
            }
        }

        private int getStrokeColor() {
            return mStrokeColor;
        }

        private void setStrokeColor(int strokeColor) {
            mStrokeColor = strokeColor;
        }

        private float getStrokeWidth() {
            return mStrokeWidth;
        }

        private void setStrokeWidth(float strokeWidth) {
            mStrokeWidth = strokeWidth;
        }

        private float getStrokeAlpha() {
            return mStrokeAlpha;
        }

        private void setStrokeAlpha(float strokeAlpha) {
            mStrokeAlpha = strokeAlpha;
        }

        private int getFillColor() {
            return mFillColor;
        }

        private void setFillColor(int fillColor) {
            mFillColor = fillColor;
        }

        private float getFillAlpha() {
            return mFillAlpha;
        }

        private void setFillAlpha(float fillAlpha) {
            mFillAlpha = fillAlpha;
        }

        private float getTrimPathStart() {
            return mTrimPathStart;
        }

        private void setTrimPathStart(float trimPathStart) {
            mTrimPathStart = trimPathStart;
        }

        private float getTrimPathEnd() {
            return mTrimPathEnd;
        }

        private void setTrimPathEnd(float trimPathEnd) {
            mTrimPathEnd = trimPathEnd;
        }

        private float getTrimPathOffset() {
            return mTrimPathOffset;
        }

        private void setTrimPathOffset(float trimPathOffset) {
            mTrimPathOffset = trimPathOffset;
        }

        private void setStrokeMiterlimit(float limit) {
            mStrokeMiterlimit = limit;
        }

        private float getStrokeMiterlimit() {
            return mStrokeMiterlimit;
        }

        private void setStrokeGradient(long gradientPtr) {
            mStrokeGradient = gradientPtr;
        }

        private void setFillGradient(long gradientPtr) {
            mFillGradient = gradientPtr;
        }

        private void setFillType(int fillType) {
            mFillType = fillType;
        }

        private int getFillType() {
            return mFillType;
        }
    }

    static class VGroup_Delegate extends android.graphics.drawable.VectorDrawable_Delegate.VNativeObject {
        // This constants need to be kept in sync with their definitions in VectorDrawable.Group
        private static final int ROTATE_INDEX = 0;

        private static final int PIVOT_X_INDEX = 1;

        private static final int PIVOT_Y_INDEX = 2;

        private static final int SCALE_X_INDEX = 3;

        private static final int SCALE_Y_INDEX = 4;

        private static final int TRANSLATE_X_INDEX = 5;

        private static final int TRANSLATE_Y_INDEX = 6;

        public java.util.function.Consumer<java.lang.Float> getPropertySetter(int propertyIdx) {
            switch (propertyIdx) {
                case android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.ROTATE_INDEX :
                    return this::setRotation;
                case android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.PIVOT_X_INDEX :
                    return this::setPivotX;
                case android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.PIVOT_Y_INDEX :
                    return this::setPivotY;
                case android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.SCALE_X_INDEX :
                    return this::setScaleX;
                case android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.SCALE_Y_INDEX :
                    return this::setScaleY;
                case android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.TRANSLATE_X_INDEX :
                    return this::setTranslateX;
                case android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate.TRANSLATE_Y_INDEX :
                    return this::setTranslateY;
            }
            assert false : "Invalid VGroup_Delegate property index " + propertyIdx;
            return ( t) -> {
            };
        }

        // ///////////////////////////////////////////////////
        // Variables below need to be copied (deep copy if applicable) for mutation.
        final java.util.ArrayList<java.lang.Object> mChildren = new java.util.ArrayList<>();

        // mStackedMatrix is only used temporarily when drawing, it combines all
        // the parents' local matrices with the current one.
        private final android.graphics.Matrix mStackedMatrix = new android.graphics.Matrix();

        // mLocalMatrix is updated based on the update of transformation information,
        // either parsed from the XML or by animation.
        private final android.graphics.Matrix mLocalMatrix = new android.graphics.Matrix();

        private float mRotate = 0;

        private float mPivotX = 0;

        private float mPivotY = 0;

        private float mScaleX = 1;

        private float mScaleY = 1;

        private float mTranslateX = 0;

        private float mTranslateY = 0;

        private int mChangingConfigurations;

        private java.lang.String mGroupName = null;

        private VGroup_Delegate(android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate copy, android.util.ArrayMap<java.lang.String, java.lang.Object> targetsMap) {
            mRotate = copy.mRotate;
            mPivotX = copy.mPivotX;
            mPivotY = copy.mPivotY;
            mScaleX = copy.mScaleX;
            mScaleY = copy.mScaleY;
            mTranslateX = copy.mTranslateX;
            mTranslateY = copy.mTranslateY;
            mGroupName = copy.mGroupName;
            mChangingConfigurations = copy.mChangingConfigurations;
            if (mGroupName != null) {
                targetsMap.put(mGroupName, this);
            }
            mLocalMatrix.set(copy.mLocalMatrix);
        }

        private VGroup_Delegate() {
        }

        private void updateLocalMatrix() {
            // The order we apply is the same as the
            // RenderNode.cpp::applyViewPropertyTransforms().
            mLocalMatrix.reset();
            mLocalMatrix.postTranslate(-mPivotX, -mPivotY);
            mLocalMatrix.postScale(mScaleX, mScaleY);
            mLocalMatrix.postRotate(mRotate, 0, 0);
            mLocalMatrix.postTranslate(mTranslateX + mPivotX, mTranslateY + mPivotY);
        }

        /* Setters and Getters, used by animator from AnimatedVectorDrawable. */
        private float getRotation() {
            return mRotate;
        }

        private void setRotation(float rotation) {
            if (rotation != mRotate) {
                mRotate = rotation;
                updateLocalMatrix();
            }
        }

        private float getPivotX() {
            return mPivotX;
        }

        private void setPivotX(float pivotX) {
            if (pivotX != mPivotX) {
                mPivotX = pivotX;
                updateLocalMatrix();
            }
        }

        private float getPivotY() {
            return mPivotY;
        }

        private void setPivotY(float pivotY) {
            if (pivotY != mPivotY) {
                mPivotY = pivotY;
                updateLocalMatrix();
            }
        }

        private float getScaleX() {
            return mScaleX;
        }

        private void setScaleX(float scaleX) {
            if (scaleX != mScaleX) {
                mScaleX = scaleX;
                updateLocalMatrix();
            }
        }

        private float getScaleY() {
            return mScaleY;
        }

        private void setScaleY(float scaleY) {
            if (scaleY != mScaleY) {
                mScaleY = scaleY;
                updateLocalMatrix();
            }
        }

        private float getTranslateX() {
            return mTranslateX;
        }

        private void setTranslateX(float translateX) {
            if (translateX != mTranslateX) {
                mTranslateX = translateX;
                updateLocalMatrix();
            }
        }

        private float getTranslateY() {
            return mTranslateY;
        }

        private void setTranslateY(float translateY) {
            if (translateY != mTranslateY) {
                mTranslateY = translateY;
                updateLocalMatrix();
            }
        }

        @java.lang.Override
        public void setName(java.lang.String name) {
            mGroupName = name;
        }

        @java.lang.Override
        protected void dispose() {
            mChildren.stream().filter(( child) -> child instanceof android.graphics.drawable.VectorDrawable_Delegate.VNativeObject).forEach(( child) -> {
                android.graphics.drawable.VectorDrawable_Delegate.VNativeObject nativeObject = ((android.graphics.drawable.VectorDrawable_Delegate.VNativeObject) (child));
                if (nativeObject.mNativePtr != 0) {
                    android.graphics.drawable.VectorDrawable_Delegate.sPathManager.removeJavaReferenceFor(nativeObject.mNativePtr);
                    nativeObject.mNativePtr = 0;
                }
                nativeObject.dispose();
            });
            mChildren.clear();
        }

        @java.lang.Override
        protected void finalize() throws java.lang.Throwable {
            super.finalize();
        }
    }

    public static class VPath_Delegate extends android.graphics.drawable.VectorDrawable_Delegate.VNativeObject {
        protected PathParser_Delegate.PathDataNode[] mNodes = null;

        java.lang.String mPathName;

        int mChangingConfigurations;

        public VPath_Delegate() {
            // Empty constructor.
        }

        public VPath_Delegate(android.graphics.drawable.VectorDrawable_Delegate.VPath_Delegate copy) {
            mPathName = copy.mPathName;
            mChangingConfigurations = copy.mChangingConfigurations;
            mNodes = (copy.mNodes != null) ? android.util.PathParser_Delegate.deepCopyNodes(copy.mNodes) : null;
        }

        public void toPath(android.graphics.Path path) {
            path.reset();
            if (mNodes != null) {
                PathParser_Delegate.PathDataNode.nodesToPath(mNodes, android.graphics.Path_Delegate.getDelegate(path.mNativePath));
            }
        }

        @java.lang.Override
        public void setName(java.lang.String name) {
            mPathName = name;
        }

        public boolean isClipPath() {
            return false;
        }

        private void setPathData(android.util.PathParser_Delegate[] nodes) {
            if (!android.util.PathParser_Delegate.canMorph(mNodes, nodes)) {
                // This should not happen in the middle of animation.
                mNodes = android.util.PathParser_Delegate.deepCopyNodes(nodes);
            } else {
                android.util.PathParser_Delegate.updateNodes(mNodes, nodes);
            }
        }

        @java.lang.Override
        void dispose() {
            mNodes = null;
        }
    }

    static class VPathRenderer_Delegate extends android.graphics.drawable.VectorDrawable_Delegate.VNativeObject {
        /* Right now the internal data structure is organized as a tree.
        Each node can be a group node, or a path.
        A group node can have groups or paths as children, but a path node has
        no children.
        One example can be:
                        Root Group
                       /    |     \
                  Group    Path    Group
                 /     \             |
                Path   Path         Path
         */
        // Variables that only used temporarily inside the draw() call, so there
        // is no need for deep copying.
        private final android.graphics.Path mPath;

        private final android.graphics.Path mRenderPath;

        private final android.graphics.Matrix mFinalPathMatrix = new android.graphics.Matrix();

        private final long mRootGroupPtr;

        private float mViewportWidth = 0;

        private float mViewportHeight = 0;

        private float mRootAlpha = 1.0F;

        private android.graphics.Paint mStrokePaint;

        private android.graphics.Paint mFillPaint;

        private android.graphics.PathMeasure mPathMeasure;

        private boolean mAntiAlias = true;

        private VPathRenderer_Delegate(long rootGroupPtr) {
            mRootGroupPtr = rootGroupPtr;
            mPath = new android.graphics.Path();
            mRenderPath = new android.graphics.Path();
        }

        private VPathRenderer_Delegate(android.graphics.drawable.VectorDrawable_Delegate.VPathRenderer_Delegate rendererToCopy, long rootGroupPtr) {
            this(rootGroupPtr);
            mViewportWidth = rendererToCopy.mViewportWidth;
            mViewportHeight = rendererToCopy.mViewportHeight;
            mRootAlpha = rendererToCopy.mRootAlpha;
        }

        private float getRootAlpha() {
            return mRootAlpha;
        }

        void setRootAlpha(float alpha) {
            mRootAlpha = alpha;
        }

        private void drawGroupTree(android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate currentGroup, android.graphics.Matrix currentMatrix, long canvasPtr, int w, int h, long filterPtr) {
            // Calculate current group's matrix by preConcat the parent's and
            // and the current one on the top of the stack.
            // Basically the Mfinal = Mviewport * M0 * M1 * M2;
            // Mi the local matrix at level i of the group tree.
            currentGroup.mStackedMatrix.set(currentMatrix);
            currentGroup.mStackedMatrix.preConcat(currentGroup.mLocalMatrix);
            // Save the current clip information, which is local to this group.
            android.graphics.Canvas_Delegate.nSave(canvasPtr, android.graphics.Canvas.MATRIX_SAVE_FLAG | android.graphics.Canvas.CLIP_SAVE_FLAG);
            // Draw the group tree in the same order as the XML file.
            for (int i = 0; i < currentGroup.mChildren.size(); i++) {
                java.lang.Object child = currentGroup.mChildren.get(i);
                if (child instanceof android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate) {
                    android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate childGroup = ((android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate) (child));
                    drawGroupTree(childGroup, currentGroup.mStackedMatrix, canvasPtr, w, h, filterPtr);
                } else
                    if (child instanceof android.graphics.drawable.VectorDrawable_Delegate.VPath_Delegate) {
                        android.graphics.drawable.VectorDrawable_Delegate.VPath_Delegate childPath = ((android.graphics.drawable.VectorDrawable_Delegate.VPath_Delegate) (child));
                        drawPath(currentGroup, childPath, canvasPtr, w, h, filterPtr);
                    }

            }
            android.graphics.Canvas_Delegate.nRestore(canvasPtr);
        }

        public void draw(long canvasPtr, long filterPtr, int w, int h) {
            // Traverse the tree in pre-order to draw.
            drawGroupTree(android.graphics.drawable.VectorDrawable_Delegate.VNativeObject.getDelegate(mRootGroupPtr), android.graphics.Matrix.IDENTITY_MATRIX, canvasPtr, w, h, filterPtr);
        }

        private void drawPath(android.graphics.drawable.VectorDrawable_Delegate.VGroup_Delegate VGroup, android.graphics.drawable.VectorDrawable_Delegate.VPath_Delegate VPath, long canvasPtr, int w, int h, long filterPtr) {
            final float scaleX = w / mViewportWidth;
            final float scaleY = h / mViewportHeight;
            final float minScale = java.lang.Math.min(scaleX, scaleY);
            final android.graphics.Matrix groupStackedMatrix = VGroup.mStackedMatrix;
            mFinalPathMatrix.set(groupStackedMatrix);
            mFinalPathMatrix.postScale(scaleX, scaleY);
            final float matrixScale = getMatrixScale(groupStackedMatrix);
            if (matrixScale == 0) {
                // When either x or y is scaled to 0, we don't need to draw anything.
                return;
            }
            VPath.toPath(mPath);
            final android.graphics.Path path = mPath;
            mRenderPath.reset();
            if (VPath.isClipPath()) {
                mRenderPath.setFillType(android.graphics.Path.FillType.WINDING);
                mRenderPath.addPath(path, mFinalPathMatrix);
                android.graphics.Canvas_Delegate.nClipPath(canvasPtr, mRenderPath.mNativePath, android.graphics.Region.Op.INTERSECT.nativeInt);
            } else {
                android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate fullPath = ((android.graphics.drawable.VectorDrawable_Delegate.VFullPath_Delegate) (VPath));
                if ((fullPath.mTrimPathStart != 0.0F) || (fullPath.mTrimPathEnd != 1.0F)) {
                    float start = (fullPath.mTrimPathStart + fullPath.mTrimPathOffset) % 1.0F;
                    float end = (fullPath.mTrimPathEnd + fullPath.mTrimPathOffset) % 1.0F;
                    if (mPathMeasure == null) {
                        mPathMeasure = new android.graphics.PathMeasure();
                    }
                    mPathMeasure.setPath(mPath, false);
                    float len = mPathMeasure.getLength();
                    start = start * len;
                    end = end * len;
                    path.reset();
                    if (start > end) {
                        mPathMeasure.getSegment(start, len, path, true);
                        mPathMeasure.getSegment(0.0F, end, path, true);
                    } else {
                        mPathMeasure.getSegment(start, end, path, true);
                    }
                    path.rLineTo(0, 0);// fix bug in measure

                }
                mRenderPath.addPath(path, mFinalPathMatrix);
                if (fullPath.mFillColor != android.graphics.Color.TRANSPARENT) {
                    if (mFillPaint == null) {
                        mFillPaint = new android.graphics.Paint();
                        mFillPaint.setStyle(android.graphics.Paint.Style.FILL);
                        mFillPaint.setAntiAlias(mAntiAlias);
                    }
                    final android.graphics.Paint fillPaint = mFillPaint;
                    fillPaint.setColor(android.graphics.drawable.VectorDrawable_Delegate.applyAlpha(android.graphics.drawable.VectorDrawable_Delegate.applyAlpha(fullPath.mFillColor, fullPath.mFillAlpha), getRootAlpha()));
                    android.graphics.Paint_Delegate fillPaintDelegate = android.graphics.Paint_Delegate.getDelegate(fillPaint.getNativeInstance());
                    // mFillPaint can not be null at this point so we will have a delegate
                    assert fillPaintDelegate != null;
                    fillPaintDelegate.setColorFilter(filterPtr);
                    android.graphics.Shader_Delegate shaderDelegate = android.graphics.Shader_Delegate.getDelegate(fullPath.mFillGradient);
                    if (shaderDelegate != null) {
                        // If there is a shader, apply the local transformation to make sure
                        // the gradient is transformed to match the viewport
                        shaderDelegate.setLocalMatrix(mFinalPathMatrix.native_instance);
                        shaderDelegate.setAlpha(fullPath.mFillAlpha);
                    }
                    fillPaintDelegate.setShader(fullPath.mFillGradient);
                    android.graphics.Path_Delegate.nSetFillType(mRenderPath.mNativePath, fullPath.mFillType);
                    android.graphics.BaseCanvas_Delegate.nDrawPath(canvasPtr, mRenderPath.mNativePath, fillPaint.getNativeInstance());
                    if (shaderDelegate != null) {
                        // Remove the local matrix
                        shaderDelegate.setLocalMatrix(0);
                    }
                }
                if (fullPath.mStrokeColor != android.graphics.Color.TRANSPARENT) {
                    if (mStrokePaint == null) {
                        mStrokePaint = new android.graphics.Paint();
                        mStrokePaint.setStyle(android.graphics.Paint.Style.STROKE);
                        mStrokePaint.setAntiAlias(mAntiAlias);
                    }
                    final android.graphics.Paint strokePaint = mStrokePaint;
                    if (fullPath.mStrokeLineJoin != null) {
                        strokePaint.setStrokeJoin(fullPath.mStrokeLineJoin);
                    }
                    if (fullPath.mStrokeLineCap != null) {
                        strokePaint.setStrokeCap(fullPath.mStrokeLineCap);
                    }
                    strokePaint.setStrokeMiter(fullPath.mStrokeMiterlimit);
                    strokePaint.setColor(android.graphics.drawable.VectorDrawable_Delegate.applyAlpha(android.graphics.drawable.VectorDrawable_Delegate.applyAlpha(fullPath.mStrokeColor, fullPath.mStrokeAlpha), getRootAlpha()));
                    android.graphics.Paint_Delegate strokePaintDelegate = android.graphics.Paint_Delegate.getDelegate(strokePaint.getNativeInstance());
                    // mStrokePaint can not be null at this point so we will have a delegate
                    assert strokePaintDelegate != null;
                    strokePaintDelegate.setColorFilter(filterPtr);
                    final float finalStrokeScale = minScale * matrixScale;
                    strokePaint.setStrokeWidth(fullPath.mStrokeWidth * finalStrokeScale);
                    android.graphics.Shader_Delegate strokeShaderDelegate = android.graphics.Shader_Delegate.getDelegate(fullPath.mStrokeGradient);
                    if (strokeShaderDelegate != null) {
                        strokeShaderDelegate.setAlpha(fullPath.mStrokeAlpha);
                    }
                    strokePaintDelegate.setShader(fullPath.mStrokeGradient);
                    android.graphics.BaseCanvas_Delegate.nDrawPath(canvasPtr, mRenderPath.mNativePath, strokePaint.getNativeInstance());
                }
            }
        }

        private float getMatrixScale(android.graphics.Matrix groupStackedMatrix) {
            // Given unit vectors A = (0, 1) and B = (1, 0).
            // After matrix mapping, we got A' and B'. Let theta = the angel b/t A' and B'.
            // Therefore, the final scale we want is min(|A'| * sin(theta), |B'| * sin(theta)),
            // which is (|A'| * |B'| * sin(theta)) / max (|A'|, |B'|);
            // If  max (|A'|, |B'|) = 0, that means either x or y has a scale of 0.
            // 
            // For non-skew case, which is most of the cases, matrix scale is computing exactly the
            // scale on x and y axis, and take the minimal of these two.
            // For skew case, an unit square will mapped to a parallelogram. And this function will
            // return the minimal height of the 2 bases.
            float[] unitVectors = new float[]{ 0, 1, 1, 0 };
            groupStackedMatrix.mapVectors(unitVectors);
            float scaleX = android.util.MathUtils.mag(unitVectors[0], unitVectors[1]);
            float scaleY = android.util.MathUtils.mag(unitVectors[2], unitVectors[3]);
            float crossProduct = android.util.MathUtils.cross(unitVectors[0], unitVectors[1], unitVectors[2], unitVectors[3]);
            float maxScale = android.util.MathUtils.max(scaleX, scaleY);
            float matrixScale = 0;
            if (maxScale > 0) {
                matrixScale = android.util.MathUtils.abs(crossProduct) / maxScale;
            }
            if (android.graphics.drawable.VectorDrawable_Delegate.DBG_VECTOR_DRAWABLE) {
                android.util.Log.d(android.graphics.drawable.VectorDrawable_Delegate.LOGTAG, (((("Scale x " + scaleX) + " y ") + scaleY) + " final ") + matrixScale);
            }
            return matrixScale;
        }

        private void setAntiAlias(boolean aa) {
            mAntiAlias = aa;
        }

        @java.lang.Override
        public void setName(java.lang.String name) {
        }

        @java.lang.Override
        protected void finalize() throws java.lang.Throwable {
            // The mRootGroupPtr is not explicitly freed by anything in the VectorDrawable so we
            // need to free it here.
            android.graphics.drawable.VectorDrawable_Delegate.VNativeObject nativeObject = android.graphics.drawable.VectorDrawable_Delegate.sPathManager.getDelegate(mRootGroupPtr);
            android.graphics.drawable.VectorDrawable_Delegate.sPathManager.removeJavaReferenceFor(mRootGroupPtr);
            assert nativeObject != null;
            nativeObject.dispose();
            super.finalize();
        }
    }
}

