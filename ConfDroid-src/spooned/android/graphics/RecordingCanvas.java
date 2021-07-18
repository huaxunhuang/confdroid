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
 * A Canvas implementation that records view system drawing operations for deferred rendering.
 * This is used in combination with RenderNode. This class keeps a list of all the Paint and
 * Bitmap objects that it draws, preventing the backing memory of Bitmaps from being released while
 * the RecordingCanvas is still holding a native reference to the memory.
 *
 * This is obtained by calling {@link RenderNode#beginRecording()} and is valid until the matching
 * {@link RenderNode#endRecording()} is called. It must not be retained beyond that as it is
 * internally reused.
 */
public final class RecordingCanvas extends android.view.DisplayListCanvas {
    // The recording canvas pool should be large enough to handle a deeply nested
    // view hierarchy because display lists are generated recursively.
    private static final int POOL_LIMIT = 25;

    /**
     *
     *
     * @unknown 
     */
    public static final int MAX_BITMAP_SIZE = (100 * 1024) * 1024;// 100 MB


    private static final android.util.Pools.SynchronizedPool<android.graphics.RecordingCanvas> sPool = new android.util.Pools.SynchronizedPool(android.graphics.RecordingCanvas.POOL_LIMIT);

    /**
     * TODO: Temporarily exposed for RenderNodeAnimator(Set)
     *
     * @unknown 
     */
    public android.graphics.RenderNode mNode;

    private int mWidth;

    private int mHeight;

    /**
     *
     *
     * @unknown 
     */
    static android.graphics.RecordingCanvas obtain(@android.annotation.NonNull
    android.graphics.RenderNode node, int width, int height) {
        if (node == null)
            throw new java.lang.IllegalArgumentException("node cannot be null");

        android.graphics.RecordingCanvas canvas = android.graphics.RecordingCanvas.sPool.acquire();
        if (canvas == null) {
            canvas = new android.graphics.RecordingCanvas(node, width, height);
        } else {
            android.graphics.RecordingCanvas.nResetDisplayListCanvas(canvas.mNativeCanvasWrapper, node.mNativeRenderNode, width, height);
        }
        canvas.mNode = node;
        canvas.mWidth = width;
        canvas.mHeight = height;
        return canvas;
    }

    /**
     *
     *
     * @unknown 
     */
    void recycle() {
        mNode = null;
        android.graphics.RecordingCanvas.sPool.release(this);
    }

    /**
     *
     *
     * @unknown 
     */
    long finishRecording() {
        return android.graphics.RecordingCanvas.nFinishRecording(mNativeCanvasWrapper);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean isRecordingFor(java.lang.Object o) {
        return o == mNode;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////
    /**
     *
     *
     * @unknown 
     */
    protected RecordingCanvas(@android.annotation.NonNull
    android.graphics.RenderNode node, int width, int height) {
        super(android.graphics.RecordingCanvas.nCreateDisplayListCanvas(node.mNativeRenderNode, width, height));
        mDensity = 0;// disable bitmap density scaling

    }

    // /////////////////////////////////////////////////////////////////////////
    // Canvas management
    // /////////////////////////////////////////////////////////////////////////
    @java.lang.Override
    public void setDensity(int density) {
        // drop silently, since RecordingCanvas doesn't perform density scaling
    }

    @java.lang.Override
    public boolean isHardwareAccelerated() {
        return true;
    }

    @java.lang.Override
    public void setBitmap(android.graphics.Bitmap bitmap) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean isOpaque() {
        return false;
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
        return android.graphics.RecordingCanvas.nGetMaximumTextureWidth();
    }

    @java.lang.Override
    public int getMaximumBitmapHeight() {
        return android.graphics.RecordingCanvas.nGetMaximumTextureHeight();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Setup
    // /////////////////////////////////////////////////////////////////////////
    @java.lang.Override
    public void enableZ() {
        android.graphics.RecordingCanvas.nInsertReorderBarrier(mNativeCanvasWrapper, true);
    }

    @java.lang.Override
    public void disableZ() {
        android.graphics.RecordingCanvas.nInsertReorderBarrier(mNativeCanvasWrapper, false);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Functor
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Records the functor specified with the drawGLFunction function pointer. This is
     * functionality used by webview for calling into their renderer from our display lists.
     *
     * @param drawGLFunction
     * 		A native function pointer
     * @unknown 
     * @deprecated Use {@link #drawWebViewFunctor(int)}
     */
    @java.lang.Deprecated
    public void callDrawGLFunction2(long drawGLFunction) {
        android.graphics.RecordingCanvas.nCallDrawGLFunction(mNativeCanvasWrapper, drawGLFunction, null);
    }

    /**
     * Records the functor specified with the drawGLFunction function pointer. This is
     * functionality used by webview for calling into their renderer from our display lists.
     *
     * @param drawGLFunctor
     * 		A native function pointer
     * @param releasedCallback
     * 		Called when the display list is destroyed, and thus
     * 		the functor is no longer referenced by this canvas's display list.
     * 		
     * 		NOTE: The callback does *not* necessarily mean that there are no longer
     * 		any references to the functor, just that the reference from this specific
     * 		canvas's display list has been released.
     * @unknown 
     * @deprecated Use {@link #drawWebViewFunctor(int)}
     */
    @java.lang.Deprecated
    public void drawGLFunctor2(long drawGLFunctor, @android.annotation.Nullable
    java.lang.Runnable releasedCallback) {
        android.graphics.RecordingCanvas.nCallDrawGLFunction(mNativeCanvasWrapper, drawGLFunctor, releasedCallback);
    }

    /**
     * Calls the provided functor that was created via WebViewFunctor_create()
     *
     * @unknown 
     */
    public void drawWebViewFunctor(int functor) {
        android.graphics.RecordingCanvas.nDrawWebViewFunctor(mNativeCanvasWrapper, functor);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Display list
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Draws the specified display list onto this canvas.
     *
     * @param renderNode
     * 		The RenderNode to draw.
     */
    @java.lang.Override
    public void drawRenderNode(@android.annotation.NonNull
    android.graphics.RenderNode renderNode) {
        android.graphics.RecordingCanvas.nDrawRenderNode(mNativeCanvasWrapper, renderNode.mNativeRenderNode);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Hardware layer
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Draws the specified layer onto this canvas.
     *
     * @param layer
     * 		The layer to composite on this canvas
     * @unknown 
     */
    public void drawTextureLayer(android.view.TextureLayer layer) {
        android.graphics.RecordingCanvas.nDrawTextureLayer(mNativeCanvasWrapper, layer.getLayerHandle());
    }

    // /////////////////////////////////////////////////////////////////////////
    // Drawing
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Draws a circle
     *
     * @param cx
     * 		
     * @param cy
     * 		
     * @param radius
     * 		
     * @param paint
     * 		
     * @unknown 
     */
    public void drawCircle(android.graphics.CanvasProperty<java.lang.Float> cx, android.graphics.CanvasProperty<java.lang.Float> cy, android.graphics.CanvasProperty<java.lang.Float> radius, android.graphics.CanvasProperty<android.graphics.Paint> paint) {
        android.graphics.RecordingCanvas.nDrawCircle(mNativeCanvasWrapper, cx.getNativeContainer(), cy.getNativeContainer(), radius.getNativeContainer(), paint.getNativeContainer());
    }

    /**
     * Draws a round rect
     *
     * @param left
     * 		
     * @param top
     * 		
     * @param right
     * 		
     * @param bottom
     * 		
     * @param rx
     * 		
     * @param ry
     * 		
     * @param paint
     * 		
     * @unknown 
     */
    public void drawRoundRect(android.graphics.CanvasProperty<java.lang.Float> left, android.graphics.CanvasProperty<java.lang.Float> top, android.graphics.CanvasProperty<java.lang.Float> right, android.graphics.CanvasProperty<java.lang.Float> bottom, android.graphics.CanvasProperty<java.lang.Float> rx, android.graphics.CanvasProperty<java.lang.Float> ry, android.graphics.CanvasProperty<android.graphics.Paint> paint) {
        android.graphics.RecordingCanvas.nDrawRoundRect(mNativeCanvasWrapper, left.getNativeContainer(), top.getNativeContainer(), right.getNativeContainer(), bottom.getNativeContainer(), rx.getNativeContainer(), ry.getNativeContainer(), paint.getNativeContainer());
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void throwIfCannotDraw(android.graphics.Bitmap bitmap) {
        super.throwIfCannotDraw(bitmap);
        int bitmapSize = bitmap.getByteCount();
        if (bitmapSize > android.graphics.RecordingCanvas.MAX_BITMAP_SIZE) {
            throw new java.lang.RuntimeException(("Canvas: trying to draw too large(" + bitmapSize) + "bytes) bitmap.");
        }
    }

    // ------------------ Fast JNI ------------------------
    @dalvik.annotation.optimization.FastNative
    private static native void nCallDrawGLFunction(long renderer, long drawGLFunction, java.lang.Runnable releasedCallback);

    // ------------------ Critical JNI ------------------------
    @dalvik.annotation.optimization.CriticalNative
    private static native long nCreateDisplayListCanvas(long node, int width, int height);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nResetDisplayListCanvas(long canvas, long node, int width, int height);

    @dalvik.annotation.optimization.CriticalNative
    private static native int nGetMaximumTextureWidth();

    @dalvik.annotation.optimization.CriticalNative
    private static native int nGetMaximumTextureHeight();

    @dalvik.annotation.optimization.CriticalNative
    private static native void nInsertReorderBarrier(long renderer, boolean enableReorder);

    @dalvik.annotation.optimization.CriticalNative
    private static native long nFinishRecording(long renderer);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nDrawRenderNode(long renderer, long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nDrawTextureLayer(long renderer, long layer);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nDrawCircle(long renderer, long propCx, long propCy, long propRadius, long propPaint);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nDrawRoundRect(long renderer, long propLeft, long propTop, long propRight, long propBottom, long propRx, long propRy, long propPaint);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nDrawWebViewFunctor(long canvas, int functor);
}

