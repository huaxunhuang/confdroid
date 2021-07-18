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
package android.view;


/**
 * An implementation of Canvas on top of OpenGL ES 2.0.
 */
class GLES20Canvas extends android.view.HardwareCanvas {
    private final boolean mOpaque;

    protected long mRenderer;

    // The native renderer will be destroyed when this object dies.
    // DO NOT overwrite this reference once it is set.
    @java.lang.SuppressWarnings({ "unused", "FieldCanBeLocal" })
    private android.view.GLES20Canvas.CanvasFinalizer mFinalizer;

    private int mWidth;

    private int mHeight;

    private float[] mPoint;

    private float[] mLine;

    private android.graphics.Rect mClipBounds;

    private android.graphics.RectF mPathBounds;

    private android.graphics.DrawFilter mFilter;

    // /////////////////////////////////////////////////////////////////////////
    // JNI
    // /////////////////////////////////////////////////////////////////////////
    private static native boolean nIsAvailable();

    private static boolean sIsAvailable = android.view.GLES20Canvas.nIsAvailable();

    static boolean isAvailable() {
        return android.view.GLES20Canvas.sIsAvailable;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////
    // TODO: Merge with GLES20RecordingCanvas
    protected GLES20Canvas() {
        mOpaque = false;
        mRenderer = android.view.GLES20Canvas.nCreateDisplayListRenderer();
        setupFinalizer();
    }

    private void setupFinalizer() {
        if (mRenderer == 0) {
            throw new java.lang.IllegalStateException("Could not create GLES20Canvas renderer");
        } else {
            mFinalizer = new android.view.GLES20Canvas.CanvasFinalizer(mRenderer);
        }
    }

    private static native long nCreateDisplayListRenderer();

    private static native void nResetDisplayListRenderer(long renderer);

    private static native void nDestroyRenderer(long renderer);

    private static final class CanvasFinalizer {
        private final long mRenderer;

        public CanvasFinalizer(long renderer) {
            mRenderer = renderer;
        }

        @java.lang.Override
        protected void finalize() throws java.lang.Throwable {
            try {
                android.view.GLES20Canvas.nDestroyRenderer(mRenderer);
            } finally {
                super.finalize();
            }
        }
    }

    public static void setProperty(java.lang.String name, java.lang.String value) {
        android.view.GLES20Canvas.nSetProperty(name, value);
    }

    private static native void nSetProperty(java.lang.String name, java.lang.String value);

    // /////////////////////////////////////////////////////////////////////////
    // Canvas management
    // /////////////////////////////////////////////////////////////////////////
    @java.lang.Override
    public boolean isOpaque() {
        return mOpaque;
    }

    @java.lang.Override
    public int getWidth() {
        return mWidth;
    }

    @java.lang.Override
    public int getHeight() {
        return mHeight;
    }

    @java.lang.Override
    public int getMaximumBitmapWidth() {
        return android.view.GLES20Canvas.nGetMaximumTextureWidth();
    }

    @java.lang.Override
    public int getMaximumBitmapHeight() {
        return android.view.GLES20Canvas.nGetMaximumTextureHeight();
    }

    private static native int nGetMaximumTextureWidth();

    private static native int nGetMaximumTextureHeight();

    /**
     * Returns the native OpenGLRenderer object.
     */
    long getRenderer() {
        return mRenderer;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Setup
    // /////////////////////////////////////////////////////////////////////////
    @java.lang.Override
    public void setViewport(int width, int height) {
        mWidth = width;
        mHeight = height;
        android.view.GLES20Canvas.nSetViewport(mRenderer, width, height);
    }

    private static native void nSetViewport(long renderer, int width, int height);

    @java.lang.Override
    public void setHighContrastText(boolean highContrastText) {
        android.view.GLES20Canvas.nSetHighContrastText(mRenderer, highContrastText);
    }

    private static native void nSetHighContrastText(long renderer, boolean highContrastText);

    @java.lang.Override
    public void insertReorderBarrier() {
        android.view.GLES20Canvas.nInsertReorderBarrier(mRenderer, true);
    }

    @java.lang.Override
    public void insertInorderBarrier() {
        android.view.GLES20Canvas.nInsertReorderBarrier(mRenderer, false);
    }

    private static native void nInsertReorderBarrier(long renderer, boolean enableReorder);

    @java.lang.Override
    public int onPreDraw(android.graphics.Rect dirty) {
        if (dirty != null) {
            return android.view.GLES20Canvas.nPrepareDirty(mRenderer, dirty.left, dirty.top, dirty.right, dirty.bottom, mOpaque);
        } else {
            return android.view.GLES20Canvas.nPrepare(mRenderer, mOpaque);
        }
    }

    private static native int nPrepare(long renderer, boolean opaque);

    private static native int nPrepareDirty(long renderer, int left, int top, int right, int bottom, boolean opaque);

    @java.lang.Override
    public void onPostDraw() {
        android.view.GLES20Canvas.nFinish(mRenderer);
    }

    private static native void nFinish(long renderer);

    // /////////////////////////////////////////////////////////////////////////
    // Functor
    // /////////////////////////////////////////////////////////////////////////
    @java.lang.Override
    public int callDrawGLFunction2(long drawGLFunction) {
        return android.view.GLES20Canvas.nCallDrawGLFunction(mRenderer, drawGLFunction);
    }

    private static native int nCallDrawGLFunction(long renderer, long drawGLFunction);

    // /////////////////////////////////////////////////////////////////////////
    // Display list
    // /////////////////////////////////////////////////////////////////////////
    protected static native long nFinishRecording(long renderer);

    @java.lang.Override
    public int drawRenderNode(android.view.RenderNode renderNode, android.graphics.Rect dirty, int flags) {
        return android.view.GLES20Canvas.nDrawRenderNode(mRenderer, renderNode.getNativeDisplayList(), dirty, flags);
    }

    private static native int nDrawRenderNode(long renderer, long renderNode, android.graphics.Rect dirty, int flags);

    // /////////////////////////////////////////////////////////////////////////
    // Hardware layer
    // /////////////////////////////////////////////////////////////////////////
    void drawHardwareLayer(android.view.HardwareLayer layer, float x, float y, android.graphics.Paint paint) {
        layer.setLayerPaint(paint);
        android.view.GLES20Canvas.nDrawLayer(mRenderer, layer.getLayerHandle(), x, y);
    }

    private static native void nDrawLayer(long renderer, long layer, float x, float y);

    // /////////////////////////////////////////////////////////////////////////
    // Support
    // /////////////////////////////////////////////////////////////////////////
    private android.graphics.Rect getInternalClipBounds() {
        if (mClipBounds == null)
            mClipBounds = new android.graphics.Rect();

        return mClipBounds;
    }

    private android.graphics.RectF getPathBounds() {
        if (mPathBounds == null)
            mPathBounds = new android.graphics.RectF();

        return mPathBounds;
    }

    private float[] getPointStorage() {
        if (mPoint == null)
            mPoint = new float[2];

        return mPoint;
    }

    private float[] getLineStorage() {
        if (mLine == null)
            mLine = new float[4];

        return mLine;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Clipping
    // /////////////////////////////////////////////////////////////////////////
    @java.lang.Override
    public boolean clipPath(android.graphics.Path path) {
        return android.view.GLES20Canvas.nClipPath(mRenderer, path.mNativePath, android.graphics.Region.Op.INTERSECT.nativeInt);
    }

    @java.lang.Override
    public boolean clipPath(android.graphics.Path path, android.graphics.Region.Op op) {
        return android.view.GLES20Canvas.nClipPath(mRenderer, path.mNativePath, op.nativeInt);
    }

    private static native boolean nClipPath(long renderer, long path, int op);

    @java.lang.Override
    public boolean clipRect(float left, float top, float right, float bottom) {
        return android.view.GLES20Canvas.nClipRect(mRenderer, left, top, right, bottom, android.graphics.Region.Op.INTERSECT.nativeInt);
    }

    private static native boolean nClipRect(long renderer, float left, float top, float right, float bottom, int op);

    @java.lang.Override
    public boolean clipRect(float left, float top, float right, float bottom, android.graphics.Region.Op op) {
        return android.view.GLES20Canvas.nClipRect(mRenderer, left, top, right, bottom, op.nativeInt);
    }

    @java.lang.Override
    public boolean clipRect(int left, int top, int right, int bottom) {
        return android.view.GLES20Canvas.nClipRect(mRenderer, left, top, right, bottom, android.graphics.Region.Op.INTERSECT.nativeInt);
    }

    private static native boolean nClipRect(long renderer, int left, int top, int right, int bottom, int op);

    @java.lang.Override
    public boolean clipRect(android.graphics.Rect rect) {
        return android.view.GLES20Canvas.nClipRect(mRenderer, rect.left, rect.top, rect.right, rect.bottom, android.graphics.Region.Op.INTERSECT.nativeInt);
    }

    @java.lang.Override
    public boolean clipRect(android.graphics.Rect rect, android.graphics.Region.Op op) {
        return android.view.GLES20Canvas.nClipRect(mRenderer, rect.left, rect.top, rect.right, rect.bottom, op.nativeInt);
    }

    @java.lang.Override
    public boolean clipRect(android.graphics.RectF rect) {
        return android.view.GLES20Canvas.nClipRect(mRenderer, rect.left, rect.top, rect.right, rect.bottom, android.graphics.Region.Op.INTERSECT.nativeInt);
    }

    @java.lang.Override
    public boolean clipRect(android.graphics.RectF rect, android.graphics.Region.Op op) {
        return android.view.GLES20Canvas.nClipRect(mRenderer, rect.left, rect.top, rect.right, rect.bottom, op.nativeInt);
    }

    @java.lang.Override
    public boolean clipRegion(android.graphics.Region region) {
        return android.view.GLES20Canvas.nClipRegion(mRenderer, region.mNativeRegion, android.graphics.Region.Op.INTERSECT.nativeInt);
    }

    @java.lang.Override
    public boolean clipRegion(android.graphics.Region region, android.graphics.Region.Op op) {
        return android.view.GLES20Canvas.nClipRegion(mRenderer, region.mNativeRegion, op.nativeInt);
    }

    private static native boolean nClipRegion(long renderer, long region, int op);

    @java.lang.Override
    public boolean getClipBounds(android.graphics.Rect bounds) {
        return android.view.GLES20Canvas.nGetClipBounds(mRenderer, bounds);
    }

    private static native boolean nGetClipBounds(long renderer, android.graphics.Rect bounds);

    @java.lang.Override
    public boolean quickReject(float left, float top, float right, float bottom, android.graphics.Canvas.EdgeType type) {
        return android.view.GLES20Canvas.nQuickReject(mRenderer, left, top, right, bottom);
    }

    private static native boolean nQuickReject(long renderer, float left, float top, float right, float bottom);

    @java.lang.Override
    public boolean quickReject(android.graphics.Path path, android.graphics.Canvas.EdgeType type) {
        android.graphics.RectF pathBounds = getPathBounds();
        path.computeBounds(pathBounds, true);
        return android.view.GLES20Canvas.nQuickReject(mRenderer, pathBounds.left, pathBounds.top, pathBounds.right, pathBounds.bottom);
    }

    @java.lang.Override
    public boolean quickReject(android.graphics.RectF rect, android.graphics.Canvas.EdgeType type) {
        return android.view.GLES20Canvas.nQuickReject(mRenderer, rect.left, rect.top, rect.right, rect.bottom);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Transformations
    // /////////////////////////////////////////////////////////////////////////
    @java.lang.Override
    public void translate(float dx, float dy) {
        if ((dx != 0.0F) || (dy != 0.0F))
            android.view.GLES20Canvas.nTranslate(mRenderer, dx, dy);

    }

    private static native void nTranslate(long renderer, float dx, float dy);

    @java.lang.Override
    public void skew(float sx, float sy) {
        android.view.GLES20Canvas.nSkew(mRenderer, sx, sy);
    }

    private static native void nSkew(long renderer, float sx, float sy);

    @java.lang.Override
    public void rotate(float degrees) {
        android.view.GLES20Canvas.nRotate(mRenderer, degrees);
    }

    private static native void nRotate(long renderer, float degrees);

    @java.lang.Override
    public void scale(float sx, float sy) {
        android.view.GLES20Canvas.nScale(mRenderer, sx, sy);
    }

    private static native void nScale(long renderer, float sx, float sy);

    @java.lang.Override
    public void setMatrix(android.graphics.Matrix matrix) {
        android.view.GLES20Canvas.nSetMatrix(mRenderer, matrix == null ? 0 : matrix.native_instance);
    }

    private static native void nSetMatrix(long renderer, long matrix);

    @java.lang.SuppressWarnings("deprecation")
    @java.lang.Override
    public void getMatrix(android.graphics.Matrix matrix) {
        android.view.GLES20Canvas.nGetMatrix(mRenderer, matrix.native_instance);
    }

    private static native void nGetMatrix(long renderer, long matrix);

    @java.lang.Override
    public void concat(android.graphics.Matrix matrix) {
        if (matrix != null)
            android.view.GLES20Canvas.nConcatMatrix(mRenderer, matrix.native_instance);

    }

    private static native void nConcatMatrix(long renderer, long matrix);

    // /////////////////////////////////////////////////////////////////////////
    // State management
    // /////////////////////////////////////////////////////////////////////////
    @java.lang.Override
    public int save() {
        return android.view.GLES20Canvas.nSave(mRenderer, android.graphics.Canvas.CLIP_SAVE_FLAG | android.graphics.Canvas.MATRIX_SAVE_FLAG);
    }

    @java.lang.Override
    public int save(int saveFlags) {
        return android.view.GLES20Canvas.nSave(mRenderer, saveFlags);
    }

    private static native int nSave(long renderer, int flags);

    @java.lang.Override
    public int saveLayer(android.graphics.RectF bounds, android.graphics.Paint paint, int saveFlags) {
        if (bounds != null) {
            return saveLayer(bounds.left, bounds.top, bounds.right, bounds.bottom, paint, saveFlags);
        }
        final long nativePaint = (paint == null) ? 0 : paint.mNativePaint;
        return android.view.GLES20Canvas.nSaveLayer(mRenderer, nativePaint, saveFlags);
    }

    private static native int nSaveLayer(long renderer, long paint, int saveFlags);

    @java.lang.Override
    public int saveLayer(float left, float top, float right, float bottom, android.graphics.Paint paint, int saveFlags) {
        if ((left < right) && (top < bottom)) {
            final long nativePaint = (paint == null) ? 0 : paint.mNativePaint;
            return android.view.GLES20Canvas.nSaveLayer(mRenderer, left, top, right, bottom, nativePaint, saveFlags);
        }
        return save(saveFlags);
    }

    private static native int nSaveLayer(long renderer, float left, float top, float right, float bottom, long paint, int saveFlags);

    @java.lang.Override
    public int saveLayerAlpha(android.graphics.RectF bounds, int alpha, int saveFlags) {
        if (bounds != null) {
            return saveLayerAlpha(bounds.left, bounds.top, bounds.right, bounds.bottom, alpha, saveFlags);
        }
        return android.view.GLES20Canvas.nSaveLayerAlpha(mRenderer, alpha, saveFlags);
    }

    private static native int nSaveLayerAlpha(long renderer, int alpha, int saveFlags);

    @java.lang.Override
    public int saveLayerAlpha(float left, float top, float right, float bottom, int alpha, int saveFlags) {
        if ((left < right) && (top < bottom)) {
            return android.view.GLES20Canvas.nSaveLayerAlpha(mRenderer, left, top, right, bottom, alpha, saveFlags);
        }
        return save(saveFlags);
    }

    private static native int nSaveLayerAlpha(long renderer, float left, float top, float right, float bottom, int alpha, int saveFlags);

    @java.lang.Override
    public void restore() {
        android.view.GLES20Canvas.nRestore(mRenderer);
    }

    private static native void nRestore(long renderer);

    @java.lang.Override
    public void restoreToCount(int saveCount) {
        android.view.GLES20Canvas.nRestoreToCount(mRenderer, saveCount);
    }

    private static native void nRestoreToCount(long renderer, int saveCount);

    @java.lang.Override
    public int getSaveCount() {
        return android.view.GLES20Canvas.nGetSaveCount(mRenderer);
    }

    private static native int nGetSaveCount(long renderer);

    // /////////////////////////////////////////////////////////////////////////
    // Filtering
    // /////////////////////////////////////////////////////////////////////////
    @java.lang.Override
    public void setDrawFilter(android.graphics.DrawFilter filter) {
        mFilter = filter;
        if (filter == null) {
            android.view.GLES20Canvas.nResetPaintFilter(mRenderer);
        } else
            if (filter instanceof android.graphics.PaintFlagsDrawFilter) {
                android.graphics.PaintFlagsDrawFilter flagsFilter = ((android.graphics.PaintFlagsDrawFilter) (filter));
                android.view.GLES20Canvas.nSetupPaintFilter(mRenderer, flagsFilter.clearBits, flagsFilter.setBits);
            }

    }

    private static native void nResetPaintFilter(long renderer);

    private static native void nSetupPaintFilter(long renderer, int clearBits, int setBits);

    @java.lang.Override
    public android.graphics.DrawFilter getDrawFilter() {
        return mFilter;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Drawing
    // /////////////////////////////////////////////////////////////////////////
    @java.lang.Override
    public void drawArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean useCenter, android.graphics.Paint paint) {
        android.view.GLES20Canvas.nDrawArc(mRenderer, left, top, right, bottom, startAngle, sweepAngle, useCenter, paint.mNativePaint);
    }

    private static native void nDrawArc(long renderer, float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean useCenter, long paint);

    @java.lang.Override
    public void drawARGB(int a, int r, int g, int b) {
        drawColor(((((a & 0xff) << 24) | ((r & 0xff) << 16)) | ((g & 0xff) << 8)) | (b & 0xff));
    }

    @java.lang.Override
    public void drawPatch(android.graphics.NinePatch patch, android.graphics.Rect dst, android.graphics.Paint paint) {
        android.graphics.Bitmap bitmap = patch.getBitmap();
        android.graphics.Canvas.throwIfCannotDraw(bitmap);
        final long nativePaint = (paint == null) ? 0 : paint.mNativePaint;
        android.view.GLES20Canvas.nDrawPatch(mRenderer, bitmap.mNativeBitmap, patch.mNativeChunk, dst.left, dst.top, dst.right, dst.bottom, nativePaint);
    }

    @java.lang.Override
    public void drawPatch(android.graphics.NinePatch patch, android.graphics.RectF dst, android.graphics.Paint paint) {
        android.graphics.Bitmap bitmap = patch.getBitmap();
        android.graphics.Canvas.throwIfCannotDraw(bitmap);
        final long nativePaint = (paint == null) ? 0 : paint.mNativePaint;
        android.view.GLES20Canvas.nDrawPatch(mRenderer, bitmap.mNativeBitmap, patch.mNativeChunk, dst.left, dst.top, dst.right, dst.bottom, nativePaint);
    }

    private static native void nDrawPatch(long renderer, long bitmap, long chunk, float left, float top, float right, float bottom, long paint);

    @java.lang.Override
    public void drawBitmap(android.graphics.Bitmap bitmap, float left, float top, android.graphics.Paint paint) {
        android.graphics.Canvas.throwIfCannotDraw(bitmap);
        final long nativePaint = (paint == null) ? 0 : paint.mNativePaint;
        android.view.GLES20Canvas.nDrawBitmap(mRenderer, bitmap.mNativeBitmap, left, top, nativePaint);
    }

    private static native void nDrawBitmap(long renderer, long bitmap, float left, float top, long paint);

    @java.lang.Override
    public void drawBitmap(android.graphics.Bitmap bitmap, android.graphics.Matrix matrix, android.graphics.Paint paint) {
        android.graphics.Canvas.throwIfCannotDraw(bitmap);
        final long nativePaint = (paint == null) ? 0 : paint.mNativePaint;
        android.view.GLES20Canvas.nDrawBitmap(mRenderer, bitmap.mNativeBitmap, matrix.native_instance, nativePaint);
    }

    private static native void nDrawBitmap(long renderer, long bitmap, long matrix, long paint);

    @java.lang.Override
    public void drawBitmap(android.graphics.Bitmap bitmap, android.graphics.Rect src, android.graphics.Rect dst, android.graphics.Paint paint) {
        android.graphics.Canvas.throwIfCannotDraw(bitmap);
        final long nativePaint = (paint == null) ? 0 : paint.mNativePaint;
        int left;
        int top;
        int right;
        int bottom;
        if (src == null) {
            left = top = 0;
            right = bitmap.getWidth();
            bottom = bitmap.getHeight();
        } else {
            left = src.left;
            right = src.right;
            top = src.top;
            bottom = src.bottom;
        }
        android.view.GLES20Canvas.nDrawBitmap(mRenderer, bitmap.mNativeBitmap, left, top, right, bottom, dst.left, dst.top, dst.right, dst.bottom, nativePaint);
    }

    @java.lang.Override
    public void drawBitmap(android.graphics.Bitmap bitmap, android.graphics.Rect src, android.graphics.RectF dst, android.graphics.Paint paint) {
        android.graphics.Canvas.throwIfCannotDraw(bitmap);
        final long nativePaint = (paint == null) ? 0 : paint.mNativePaint;
        float left;
        float top;
        float right;
        float bottom;
        if (src == null) {
            left = top = 0;
            right = bitmap.getWidth();
            bottom = bitmap.getHeight();
        } else {
            left = src.left;
            right = src.right;
            top = src.top;
            bottom = src.bottom;
        }
        android.view.GLES20Canvas.nDrawBitmap(mRenderer, bitmap.mNativeBitmap, left, top, right, bottom, dst.left, dst.top, dst.right, dst.bottom, nativePaint);
    }

    private static native void nDrawBitmap(long renderer, long bitmap, float srcLeft, float srcTop, float srcRight, float srcBottom, float left, float top, float right, float bottom, long paint);

    @java.lang.Override
    public void drawBitmap(int[] colors, int offset, int stride, float x, float y, int width, int height, boolean hasAlpha, android.graphics.Paint paint) {
        if (width < 0) {
            throw new java.lang.IllegalArgumentException("width must be >= 0");
        }
        if (height < 0) {
            throw new java.lang.IllegalArgumentException("height must be >= 0");
        }
        if (java.lang.Math.abs(stride) < width) {
            throw new java.lang.IllegalArgumentException("abs(stride) must be >= width");
        }
        int lastScanline = offset + ((height - 1) * stride);
        int length = colors.length;
        if ((((offset < 0) || ((offset + width) > length)) || (lastScanline < 0)) || ((lastScanline + width) > length)) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        final long nativePaint = (paint == null) ? 0 : paint.mNativePaint;
        android.view.GLES20Canvas.nDrawBitmap(mRenderer, colors, offset, stride, x, y, width, height, hasAlpha, nativePaint);
    }

    private static native void nDrawBitmap(long renderer, int[] colors, int offset, int stride, float x, float y, int width, int height, boolean hasAlpha, long nativePaint);

    @java.lang.Override
    public void drawBitmap(int[] colors, int offset, int stride, int x, int y, int width, int height, boolean hasAlpha, android.graphics.Paint paint) {
        drawBitmap(colors, offset, stride, ((float) (x)), ((float) (y)), width, height, hasAlpha, paint);
    }

    @java.lang.Override
    public void drawBitmapMesh(android.graphics.Bitmap bitmap, int meshWidth, int meshHeight, float[] verts, int vertOffset, int[] colors, int colorOffset, android.graphics.Paint paint) {
        android.graphics.Canvas.throwIfCannotDraw(bitmap);
        if ((((meshWidth < 0) || (meshHeight < 0)) || (vertOffset < 0)) || (colorOffset < 0)) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        if ((meshWidth == 0) || (meshHeight == 0)) {
            return;
        }
        final int count = (meshWidth + 1) * (meshHeight + 1);
        android.graphics.Canvas.checkRange(verts.length, vertOffset, count * 2);
        if (colors != null) {
            android.graphics.Canvas.checkRange(colors.length, colorOffset, count);
        }
        final long nativePaint = (paint == null) ? 0 : paint.mNativePaint;
        android.view.GLES20Canvas.nDrawBitmapMesh(mRenderer, bitmap.mNativeBitmap, meshWidth, meshHeight, verts, vertOffset, colors, colorOffset, nativePaint);
    }

    private static native void nDrawBitmapMesh(long renderer, long bitmap, int meshWidth, int meshHeight, float[] verts, int vertOffset, int[] colors, int colorOffset, long paint);

    @java.lang.Override
    public void drawCircle(float cx, float cy, float radius, android.graphics.Paint paint) {
        android.view.GLES20Canvas.nDrawCircle(mRenderer, cx, cy, radius, paint.mNativePaint);
    }

    private static native void nDrawCircle(long renderer, float cx, float cy, float radius, long paint);

    @java.lang.Override
    public void drawCircle(android.graphics.CanvasProperty<java.lang.Float> cx, android.graphics.CanvasProperty<java.lang.Float> cy, android.graphics.CanvasProperty<java.lang.Float> radius, android.graphics.CanvasProperty<android.graphics.Paint> paint) {
        android.view.GLES20Canvas.nDrawCircle(mRenderer, cx.getNativeContainer(), cy.getNativeContainer(), radius.getNativeContainer(), paint.getNativeContainer());
    }

    private static native void nDrawCircle(long renderer, long propCx, long propCy, long propRadius, long propPaint);

    @java.lang.Override
    public void drawRoundRect(android.graphics.CanvasProperty<java.lang.Float> left, android.graphics.CanvasProperty<java.lang.Float> top, android.graphics.CanvasProperty<java.lang.Float> right, android.graphics.CanvasProperty<java.lang.Float> bottom, android.graphics.CanvasProperty<java.lang.Float> rx, android.graphics.CanvasProperty<java.lang.Float> ry, android.graphics.CanvasProperty<android.graphics.Paint> paint) {
        android.view.GLES20Canvas.nDrawRoundRect(mRenderer, left.getNativeContainer(), top.getNativeContainer(), right.getNativeContainer(), bottom.getNativeContainer(), rx.getNativeContainer(), ry.getNativeContainer(), paint.getNativeContainer());
    }

    private static native void nDrawRoundRect(long renderer, long propLeft, long propTop, long propRight, long propBottom, long propRx, long propRy, long propPaint);

    @java.lang.Override
    public void drawColor(int color) {
        drawColor(color, android.graphics.PorterDuff.Mode.SRC_OVER);
    }

    @java.lang.Override
    public void drawColor(int color, android.graphics.PorterDuff.Mode mode) {
        android.view.GLES20Canvas.nDrawColor(mRenderer, color, mode.nativeInt);
    }

    private static native void nDrawColor(long renderer, int color, int mode);

    @java.lang.Override
    public void drawLine(float startX, float startY, float stopX, float stopY, android.graphics.Paint paint) {
        float[] line = getLineStorage();
        line[0] = startX;
        line[1] = startY;
        line[2] = stopX;
        line[3] = stopY;
        drawLines(line, 0, 4, paint);
    }

    @java.lang.Override
    public void drawLines(float[] pts, int offset, int count, android.graphics.Paint paint) {
        if (count < 4)
            return;

        if (((offset | count) < 0) || ((offset + count) > pts.length)) {
            throw new java.lang.IllegalArgumentException("The lines array must contain 4 elements per line.");
        }
        android.view.GLES20Canvas.nDrawLines(mRenderer, pts, offset, count, paint.mNativePaint);
    }

    private static native void nDrawLines(long renderer, float[] points, int offset, int count, long paint);

    @java.lang.Override
    public void drawLines(float[] pts, android.graphics.Paint paint) {
        drawLines(pts, 0, pts.length, paint);
    }

    @java.lang.Override
    public void drawOval(float left, float top, float right, float bottom, android.graphics.Paint paint) {
        android.view.GLES20Canvas.nDrawOval(mRenderer, left, top, right, bottom, paint.mNativePaint);
    }

    private static native void nDrawOval(long renderer, float left, float top, float right, float bottom, long paint);

    @java.lang.Override
    public void drawPaint(android.graphics.Paint paint) {
        final android.graphics.Rect r = getInternalClipBounds();
        android.view.GLES20Canvas.nGetClipBounds(mRenderer, r);
        drawRect(r.left, r.top, r.right, r.bottom, paint);
    }

    @java.lang.Override
    public void drawPath(android.graphics.Path path, android.graphics.Paint paint) {
        if (path.isSimplePath) {
            if (path.rects != null) {
                android.view.GLES20Canvas.nDrawRects(mRenderer, path.rects.mNativeRegion, paint.mNativePaint);
            }
        } else {
            android.view.GLES20Canvas.nDrawPath(mRenderer, path.mNativePath, paint.mNativePaint);
        }
    }

    private static native void nDrawPath(long renderer, long path, long paint);

    private static native void nDrawRects(long renderer, long region, long paint);

    @java.lang.Override
    public void drawPicture(android.graphics.Picture picture) {
        picture.endRecording();
        // TODO: Implement rendering
    }

    @java.lang.Override
    public void drawPoint(float x, float y, android.graphics.Paint paint) {
        float[] point = getPointStorage();
        point[0] = x;
        point[1] = y;
        drawPoints(point, 0, 2, paint);
    }

    @java.lang.Override
    public void drawPoints(float[] pts, android.graphics.Paint paint) {
        drawPoints(pts, 0, pts.length, paint);
    }

    @java.lang.Override
    public void drawPoints(float[] pts, int offset, int count, android.graphics.Paint paint) {
        if (count < 2)
            return;

        android.view.GLES20Canvas.nDrawPoints(mRenderer, pts, offset, count, paint.mNativePaint);
    }

    private static native void nDrawPoints(long renderer, float[] points, int offset, int count, long paint);

    // Note: drawPosText just uses implementation in Canvas
    @java.lang.Override
    public void drawRect(float left, float top, float right, float bottom, android.graphics.Paint paint) {
        if ((left == right) || (top == bottom))
            return;

        android.view.GLES20Canvas.nDrawRect(mRenderer, left, top, right, bottom, paint.mNativePaint);
    }

    private static native void nDrawRect(long renderer, float left, float top, float right, float bottom, long paint);

    @java.lang.Override
    public void drawRect(android.graphics.Rect r, android.graphics.Paint paint) {
        drawRect(r.left, r.top, r.right, r.bottom, paint);
    }

    @java.lang.Override
    public void drawRect(android.graphics.RectF r, android.graphics.Paint paint) {
        drawRect(r.left, r.top, r.right, r.bottom, paint);
    }

    @java.lang.Override
    public void drawRGB(int r, int g, int b) {
        drawColor(((0xff000000 | ((r & 0xff) << 16)) | ((g & 0xff) << 8)) | (b & 0xff));
    }

    @java.lang.Override
    public void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, android.graphics.Paint paint) {
        android.view.GLES20Canvas.nDrawRoundRect(mRenderer, left, top, right, bottom, rx, ry, paint.mNativePaint);
    }

    private static native void nDrawRoundRect(long renderer, float left, float top, float right, float bottom, float rx, float y, long paint);

    @java.lang.Override
    public void drawText(char[] text, int index, int count, float x, float y, android.graphics.Paint paint) {
        if ((((index | count) | (index + count)) | ((text.length - index) - count)) < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        android.view.GLES20Canvas.nDrawText(mRenderer, text, index, count, x, y, paint.mBidiFlags, paint.mNativePaint, paint.mNativeTypeface);
    }

    private static native void nDrawText(long renderer, char[] text, int index, int count, float x, float y, int bidiFlags, long paint, long typeface);

    @java.lang.Override
    public void drawText(java.lang.CharSequence text, int start, int end, float x, float y, android.graphics.Paint paint) {
        if ((((start | end) | (end - start)) | (text.length() - end)) < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (((text instanceof java.lang.String) || (text instanceof android.text.SpannedString)) || (text instanceof android.text.SpannableString)) {
            android.view.GLES20Canvas.nDrawText(mRenderer, text.toString(), start, end, x, y, paint.mBidiFlags, paint.mNativePaint, paint.mNativeTypeface);
        } else
            if (text instanceof android.text.GraphicsOperations) {
                drawText(this, start, end, x, y, paint);
            } else {
                char[] buf = android.graphics.TemporaryBuffer.obtain(end - start);
                android.text.TextUtils.getChars(text, start, end, buf, 0);
                android.view.GLES20Canvas.nDrawText(mRenderer, buf, 0, end - start, x, y, paint.mBidiFlags, paint.mNativePaint, paint.mNativeTypeface);
                android.graphics.TemporaryBuffer.recycle(buf);
            }

    }

    @java.lang.Override
    public void drawText(java.lang.String text, int start, int end, float x, float y, android.graphics.Paint paint) {
        if ((((start | end) | (end - start)) | (text.length() - end)) < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        android.view.GLES20Canvas.nDrawText(mRenderer, text, start, end, x, y, paint.mBidiFlags, paint.mNativePaint, paint.mNativeTypeface);
    }

    private static native void nDrawText(long renderer, java.lang.String text, int start, int end, float x, float y, int bidiFlags, long paint, long typeface);

    @java.lang.Override
    public void drawText(java.lang.String text, float x, float y, android.graphics.Paint paint) {
        android.view.GLES20Canvas.nDrawText(mRenderer, text, 0, text.length(), x, y, paint.mBidiFlags, paint.mNativePaint, paint.mNativeTypeface);
    }

    @java.lang.Override
    public void drawTextOnPath(char[] text, int index, int count, android.graphics.Path path, float hOffset, float vOffset, android.graphics.Paint paint) {
        if ((index < 0) || ((index + count) > text.length)) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        android.view.GLES20Canvas.nDrawTextOnPath(mRenderer, text, index, count, path.mNativePath, hOffset, vOffset, paint.mBidiFlags, paint.mNativePaint, paint.mNativeTypeface);
    }

    private static native void nDrawTextOnPath(long renderer, char[] text, int index, int count, long path, float hOffset, float vOffset, int bidiFlags, long nativePaint, long typeface);

    @java.lang.Override
    public void drawTextOnPath(java.lang.String text, android.graphics.Path path, float hOffset, float vOffset, android.graphics.Paint paint) {
        if (text.length() == 0)
            return;

        android.view.GLES20Canvas.nDrawTextOnPath(mRenderer, text, 0, text.length(), path.mNativePath, hOffset, vOffset, paint.mBidiFlags, paint.mNativePaint, paint.mNativeTypeface);
    }

    private static native void nDrawTextOnPath(long renderer, java.lang.String text, int start, int end, long path, float hOffset, float vOffset, int bidiFlags, long nativePaint, long typeface);

    @java.lang.Override
    public void drawTextRun(char[] text, int index, int count, int contextIndex, int contextCount, float x, float y, boolean isRtl, android.graphics.Paint paint) {
        if (((index | count) | ((text.length - index) - count)) < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        android.view.GLES20Canvas.nDrawTextRun(mRenderer, text, index, count, contextIndex, contextCount, x, y, isRtl, paint.mNativePaint, paint.mNativeTypeface);
    }

    private static native void nDrawTextRun(long renderer, char[] text, int index, int count, int contextIndex, int contextCount, float x, float y, boolean isRtl, long nativePaint, long nativeTypeface);

    @java.lang.Override
    public void drawTextRun(java.lang.CharSequence text, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, android.graphics.Paint paint) {
        if ((((start | end) | (end - start)) | (text.length() - end)) < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (((text instanceof java.lang.String) || (text instanceof android.text.SpannedString)) || (text instanceof android.text.SpannableString)) {
            android.view.GLES20Canvas.nDrawTextRun(mRenderer, text.toString(), start, end, contextStart, contextEnd, x, y, isRtl, paint.mNativePaint, paint.mNativeTypeface);
        } else
            if (text instanceof android.text.GraphicsOperations) {
                drawTextRun(this, start, end, contextStart, contextEnd, x, y, isRtl, paint);
            } else {
                int contextLen = contextEnd - contextStart;
                int len = end - start;
                char[] buf = android.graphics.TemporaryBuffer.obtain(contextLen);
                android.text.TextUtils.getChars(text, contextStart, contextEnd, buf, 0);
                android.view.GLES20Canvas.nDrawTextRun(mRenderer, buf, start - contextStart, len, 0, contextLen, x, y, isRtl, paint.mNativePaint, paint.mNativeTypeface);
                android.graphics.TemporaryBuffer.recycle(buf);
            }

    }

    private static native void nDrawTextRun(long renderer, java.lang.String text, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, long nativePaint, long nativeTypeface);

    @java.lang.Override
    public void drawVertices(android.graphics.Canvas.VertexMode mode, int vertexCount, float[] verts, int vertOffset, float[] texs, int texOffset, int[] colors, int colorOffset, short[] indices, int indexOffset, int indexCount, android.graphics.Paint paint) {
        // TODO: Implement
    }
}

