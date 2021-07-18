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
 * Hardware accelerated canvas.
 *
 * @unknown 
 */
public abstract class HardwareCanvas extends android.graphics.Canvas {
    @java.lang.Override
    public boolean isHardwareAccelerated() {
        return true;
    }

    @java.lang.Override
    public void setBitmap(android.graphics.Bitmap bitmap) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * Invoked before any drawing operation is performed in this canvas.
     *
     * @param dirty
     * 		The dirty rectangle to update, can be null.
     * @return {@link RenderNode#STATUS_DREW} if anything was drawn (such as a call to clear
    the canvas).
     * @unknown 
     */
    public abstract int onPreDraw(android.graphics.Rect dirty);

    /**
     * Invoked after all drawing operation have been performed.
     *
     * @unknown 
     */
    public abstract void onPostDraw();

    /**
     * Draws the specified display list onto this canvas. The display list can only
     * be drawn if {@link android.view.RenderNode#isValid()} returns true.
     *
     * @param renderNode
     * 		The RenderNode to replay.
     */
    public void drawRenderNode(android.view.RenderNode renderNode) {
        drawRenderNode(renderNode, null, android.view.RenderNode.FLAG_CLIP_CHILDREN);
    }

    /**
     * Draws the specified display list onto this canvas.
     *
     * @param renderNode
     * 		The RenderNode to replay.
     * @param dirty
     * 		Ignored, can be null.
     * @param flags
     * 		Optional flags about drawing, see {@link RenderNode} for
     * 		the possible flags.
     * @return One of {@link RenderNode#STATUS_DONE} or {@link RenderNode#STATUS_DREW}
    if anything was drawn.
     * @unknown 
     */
    public abstract int drawRenderNode(android.view.RenderNode renderNode, android.graphics.Rect dirty, int flags);

    /**
     * Draws the specified layer onto this canvas.
     *
     * @param layer
     * 		The layer to composite on this canvas
     * @param x
     * 		The left coordinate of the layer
     * @param y
     * 		The top coordinate of the layer
     * @param paint
     * 		The paint used to draw the layer
     * @unknown 
     */
    abstract void drawHardwareLayer(android.view.HardwareLayer layer, float x, float y, android.graphics.Paint paint);

    /**
     * Calls the function specified with the drawGLFunction function pointer. This is
     * functionality used by webkit for calling into their renderer from our display lists.
     * This function may return true if an invalidation is needed after the call.
     *
     * @param drawGLFunction
     * 		A native function pointer
     * @return {@link RenderNode#STATUS_DONE}
     * @unknown 
     */
    public abstract int callDrawGLFunction2(long drawGLFunction);

    public abstract void drawCircle(android.graphics.CanvasProperty<java.lang.Float> cx, android.graphics.CanvasProperty<java.lang.Float> cy, android.graphics.CanvasProperty<java.lang.Float> radius, android.graphics.CanvasProperty<android.graphics.Paint> paint);

    public abstract void drawRoundRect(android.graphics.CanvasProperty<java.lang.Float> left, android.graphics.CanvasProperty<java.lang.Float> top, android.graphics.CanvasProperty<java.lang.Float> right, android.graphics.CanvasProperty<java.lang.Float> bottom, android.graphics.CanvasProperty<java.lang.Float> rx, android.graphics.CanvasProperty<java.lang.Float> ry, android.graphics.CanvasProperty<android.graphics.Paint> paint);

    public static void setProperty(java.lang.String name, java.lang.String value) {
        android.view.GLES20Canvas.setProperty(name, value);
    }
}

