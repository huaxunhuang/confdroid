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
 * <p>A display list records a series of graphics related operations and can replay
 * them later. Display lists are usually built by recording operations on a
 * {@link DisplayListCanvas}. Replaying the operations from a display list avoids
 * executing application code on every frame, and is thus much more efficient.</p>
 *
 * <p>Display lists are used internally for all views by default, and are not
 * typically used directly. One reason to consider using a display is a custom
 * {@link View} implementation that needs to issue a large number of drawing commands.
 * When the view invalidates, all the drawing commands must be reissued, even if
 * large portions of the drawing command stream stay the same frame to frame, which
 * can become a performance bottleneck. To solve this issue, a custom View might split
 * its content into several display lists. A display list is updated only when its
 * content, and only its content, needs to be updated.</p>
 *
 * <p>A text editor might for instance store each paragraph into its own display list.
 * Thus when the user inserts or removes characters, only the display list of the
 * affected paragraph needs to be recorded again.</p>
 *
 * <h3>Hardware acceleration</h3>
 * <p>Display lists can only be replayed using a {@link DisplayListCanvas}. They are not
 * supported in software. Always make sure that the {@link android.graphics.Canvas}
 * you are using to render a display list is hardware accelerated using
 * {@link android.graphics.Canvas#isHardwareAccelerated()}.</p>
 *
 * <h3>Creating a display list</h3>
 * <pre class="prettyprint">
 *     ThreadedRenderer renderer = myView.getThreadedRenderer();
 *     if (renderer != null) {
 *         DisplayList displayList = renderer.createDisplayList();
 *         DisplayListCanvas canvas = displayList.start(width, height);
 *         try {
 *             // Draw onto the canvas
 *             // For instance: canvas.drawBitmap(...);
 *         } finally {
 *             displayList.end();
 *         }
 *     }
 * </pre>
 *
 * <h3>Rendering a display list on a View</h3>
 * <pre class="prettyprint">
 *     protected void onDraw(Canvas canvas) {
 *         if (canvas.isHardwareAccelerated()) {
 *             DisplayListCanvas displayListCanvas = (DisplayListCanvas) canvas;
 *             displayListCanvas.drawDisplayList(mDisplayList);
 *         }
 *     }
 * </pre>
 *
 * <h3>Releasing resources</h3>
 * <p>This step is not mandatory but recommended if you want to release resources
 * held by a display list as soon as possible.</p>
 * <pre class="prettyprint">
 *     // Mark this display list invalid, it cannot be used for drawing anymore,
 *     // and release resources held by this display list
 *     displayList.clear();
 * </pre>
 *
 * <h3>Properties</h3>
 * <p>In addition, a display list offers several properties, such as
 * {@link #setScaleX(float)} or {@link #setLeft(int)}, that can be used to affect all
 * the drawing commands recorded within. For instance, these properties can be used
 * to move around a large number of images without re-issuing all the individual
 * <code>drawBitmap()</code> calls.</p>
 *
 * <pre class="prettyprint">
 *     private void createDisplayList() {
 *         mDisplayList = DisplayList.create("MyDisplayList");
 *         DisplayListCanvas canvas = mDisplayList.start(width, height);
 *         try {
 *             for (Bitmap b : mBitmaps) {
 *                 canvas.drawBitmap(b, 0.0f, 0.0f, null);
 *                 canvas.translate(0.0f, b.getHeight());
 *             }
 *         } finally {
 *             displayList.end();
 *         }
 *     }
 *
 *     protected void onDraw(Canvas canvas) {
 *         if (canvas.isHardwareAccelerated()) {
 *             DisplayListCanvas displayListCanvas = (DisplayListCanvas) canvas;
 *             displayListCanvas.drawDisplayList(mDisplayList);
 *         }
 *     }
 *
 *     private void moveContentBy(int x) {
 *          // This will move all the bitmaps recorded inside the display list
 *          // by x pixels to the right and redraw this view. All the commands
 *          // recorded in createDisplayList() won't be re-issued, only onDraw()
 *          // will be invoked and will execute very quickly
 *          mDisplayList.offsetLeftAndRight(x);
 *          invalidate();
 *     }
 * </pre>
 *
 * <h3>Threading</h3>
 * <p>Display lists must be created on and manipulated from the UI thread only.</p>
 *
 * @unknown 
 */
public class RenderNode {
    // Use a Holder to allow static initialization in the boot image.
    private static class NoImagePreloadHolder {
        public static final libcore.util.NativeAllocationRegistry sRegistry = new libcore.util.NativeAllocationRegistry(android.view.RenderNode.class.getClassLoader(), android.view.RenderNode.nGetNativeFinalizer(), 1024);
    }

    // Do not access directly unless you are ThreadedRenderer
    final long mNativeRenderNode;

    private final android.view.View mOwningView;

    private RenderNode(java.lang.String name, android.view.View owningView) {
        mNativeRenderNode = android.view.RenderNode.nCreate(name);
        android.view.RenderNode.NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, mNativeRenderNode);
        mOwningView = owningView;
    }

    /**
     *
     *
     * @see RenderNode#adopt(long)
     */
    private RenderNode(long nativePtr) {
        mNativeRenderNode = nativePtr;
        android.view.RenderNode.NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, mNativeRenderNode);
        mOwningView = null;
    }

    /**
     * Immediately destroys the RenderNode
     * Only suitable for testing/benchmarking where waiting for the GC/finalizer
     * is not feasible.
     */
    public void destroy() {
        // TODO: Removed temporarily
    }

    /**
     * Creates a new RenderNode that can be used to record batches of
     * drawing operations, and store / apply render properties when drawn.
     *
     * @param name
     * 		The name of the RenderNode, used for debugging purpose. May be null.
     * @return A new RenderNode.
     */
    public static android.view.RenderNode create(java.lang.String name, @android.annotation.Nullable
    android.view.View owningView) {
        return new android.view.RenderNode(name, owningView);
    }

    /**
     * Adopts an existing native render node.
     *
     * Note: This will *NOT* incRef() on the native object, however it will
     * decRef() when it is destroyed. The caller should have already incRef'd it
     */
    public static android.view.RenderNode adopt(long nativePtr) {
        return new android.view.RenderNode(nativePtr);
    }

    /**
     * Enable callbacks for position changes.
     */
    public void requestPositionUpdates(android.view.SurfaceView view) {
        android.view.RenderNode.nRequestPositionUpdates(mNativeRenderNode, view);
    }

    /**
     * Starts recording a display list for the render node. All
     * operations performed on the returned canvas are recorded and
     * stored in this display list.
     *
     * Calling this method will mark the render node invalid until
     * {@link #end(DisplayListCanvas)} is called.
     * Only valid render nodes can be replayed.
     *
     * @param width
     * 		The width of the recording viewport
     * @param height
     * 		The height of the recording viewport
     * @return A canvas to record drawing operations.
     * @see #end(DisplayListCanvas)
     * @see #isValid()
     */
    public android.view.DisplayListCanvas start(int width, int height) {
        return android.view.DisplayListCanvas.obtain(this, width, height);
    }

    /**
     * Ends the recording for this display list. A display list cannot be
     * replayed if recording is not finished. Calling this method marks
     * the display list valid and {@link #isValid()} will return true.
     *
     * @see #start(int, int)
     * @see #isValid()
     */
    public void end(android.view.DisplayListCanvas canvas) {
        long displayList = canvas.finishRecording();
        android.view.RenderNode.nSetDisplayList(mNativeRenderNode, displayList);
        canvas.recycle();
    }

    /**
     * Reset native resources. This is called when cleaning up the state of display lists
     * during destruction of hardware resources, to ensure that we do not hold onto
     * obsolete resources after related resources are gone.
     */
    public void discardDisplayList() {
        android.view.RenderNode.nSetDisplayList(mNativeRenderNode, 0);
    }

    /**
     * Returns whether the RenderNode's display list content is currently usable.
     * If this returns false, the display list should be re-recorded prior to replaying it.
     *
     * @return boolean true if the display list is able to be replayed, false otherwise.
     */
    public boolean isValid() {
        return android.view.RenderNode.nIsValid(mNativeRenderNode);
    }

    long getNativeDisplayList() {
        if (!isValid()) {
            throw new java.lang.IllegalStateException("The display list is not valid.");
        }
        return mNativeRenderNode;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Matrix manipulation
    // /////////////////////////////////////////////////////////////////////////
    public boolean hasIdentityMatrix() {
        return android.view.RenderNode.nHasIdentityMatrix(mNativeRenderNode);
    }

    public void getMatrix(@android.annotation.NonNull
    android.graphics.Matrix outMatrix) {
        android.view.RenderNode.nGetTransformMatrix(mNativeRenderNode, outMatrix.native_instance);
    }

    public void getInverseMatrix(@android.annotation.NonNull
    android.graphics.Matrix outMatrix) {
        android.view.RenderNode.nGetInverseTransformMatrix(mNativeRenderNode, outMatrix.native_instance);
    }

    // /////////////////////////////////////////////////////////////////////////
    // RenderProperty Setters
    // /////////////////////////////////////////////////////////////////////////
    public boolean setLayerType(int layerType) {
        return android.view.RenderNode.nSetLayerType(mNativeRenderNode, layerType);
    }

    public boolean setLayerPaint(@android.annotation.Nullable
    android.graphics.Paint paint) {
        return android.view.RenderNode.nSetLayerPaint(mNativeRenderNode, paint != null ? paint.getNativeInstance() : 0);
    }

    public boolean setClipBounds(@android.annotation.Nullable
    android.graphics.Rect rect) {
        if (rect == null) {
            return android.view.RenderNode.nSetClipBoundsEmpty(mNativeRenderNode);
        } else {
            return android.view.RenderNode.nSetClipBounds(mNativeRenderNode, rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    /**
     * Set whether the Render node should clip itself to its bounds. This property is controlled by
     * the view's parent.
     *
     * @param clipToBounds
     * 		true if the display list should clip to its bounds
     */
    public boolean setClipToBounds(boolean clipToBounds) {
        return android.view.RenderNode.nSetClipToBounds(mNativeRenderNode, clipToBounds);
    }

    /**
     * Sets whether the display list should be drawn immediately after the
     * closest ancestor display list containing a projection receiver.
     *
     * @param shouldProject
     * 		true if the display list should be projected onto a
     * 		containing volume.
     */
    public boolean setProjectBackwards(boolean shouldProject) {
        return android.view.RenderNode.nSetProjectBackwards(mNativeRenderNode, shouldProject);
    }

    /**
     * Sets whether the display list is a projection receiver - that its parent
     * DisplayList should draw any descendent DisplayLists with
     * ProjectBackwards=true directly on top of it. Default value is false.
     */
    public boolean setProjectionReceiver(boolean shouldRecieve) {
        return android.view.RenderNode.nSetProjectionReceiver(mNativeRenderNode, shouldRecieve);
    }

    /**
     * Sets the outline, defining the shape that casts a shadow, and the path to
     * be clipped if setClipToOutline is set.
     *
     * Deep copies the data into native to simplify reference ownership.
     */
    public boolean setOutline(@android.annotation.Nullable
    android.graphics.Outline outline) {
        if (outline == null) {
            return android.view.RenderNode.nSetOutlineNone(mNativeRenderNode);
        }
        switch (outline.mMode) {
            case android.graphics.Outline.MODE_EMPTY :
                return android.view.RenderNode.nSetOutlineEmpty(mNativeRenderNode);
            case android.graphics.Outline.MODE_ROUND_RECT :
                return android.view.RenderNode.nSetOutlineRoundRect(mNativeRenderNode, outline.mRect.left, outline.mRect.top, outline.mRect.right, outline.mRect.bottom, outline.mRadius, outline.mAlpha);
            case android.graphics.Outline.MODE_CONVEX_PATH :
                return android.view.RenderNode.nSetOutlineConvexPath(mNativeRenderNode, outline.mPath.mNativePath, outline.mAlpha);
        }
        throw new java.lang.IllegalArgumentException("Unrecognized outline?");
    }

    public boolean hasShadow() {
        return android.view.RenderNode.nHasShadow(mNativeRenderNode);
    }

    /**
     * setSpotShadowColor
     */
    public boolean setSpotShadowColor(int color) {
        return android.view.RenderNode.nSetSpotShadowColor(mNativeRenderNode, color);
    }

    /**
     * setAmbientShadowColor
     */
    public boolean setAmbientShadowColor(int color) {
        return android.view.RenderNode.nSetAmbientShadowColor(mNativeRenderNode, color);
    }

    /**
     * getSpotShadowColor
     */
    public int getSpotShadowColor() {
        return android.view.RenderNode.nGetSpotShadowColor(mNativeRenderNode);
    }

    /**
     * getAmbientShadowColor
     */
    public int getAmbientShadowColor() {
        return android.view.RenderNode.nGetAmbientShadowColor(mNativeRenderNode);
    }

    /**
     * Enables or disables clipping to the outline.
     *
     * @param clipToOutline
     * 		true if clipping to the outline.
     */
    public boolean setClipToOutline(boolean clipToOutline) {
        return android.view.RenderNode.nSetClipToOutline(mNativeRenderNode, clipToOutline);
    }

    public boolean getClipToOutline() {
        return android.view.RenderNode.nGetClipToOutline(mNativeRenderNode);
    }

    /**
     * Controls the RenderNode's circular reveal clip.
     */
    public boolean setRevealClip(boolean shouldClip, float x, float y, float radius) {
        return android.view.RenderNode.nSetRevealClip(mNativeRenderNode, shouldClip, x, y, radius);
    }

    /**
     * Set the static matrix on the display list. The specified matrix is combined with other
     * transforms (such as {@link #setScaleX(float)}, {@link #setRotation(float)}, etc.)
     *
     * @param matrix
     * 		A transform matrix to apply to this display list
     */
    public boolean setStaticMatrix(android.graphics.Matrix matrix) {
        return android.view.RenderNode.nSetStaticMatrix(mNativeRenderNode, matrix.native_instance);
    }

    /**
     * Set the Animation matrix on the display list. This matrix exists if an Animation is
     * currently playing on a View, and is set on the display list during at draw() time. When
     * the Animation finishes, the matrix should be cleared by sending <code>null</code>
     * for the matrix parameter.
     *
     * @param matrix
     * 		The matrix, null indicates that the matrix should be cleared.
     */
    public boolean setAnimationMatrix(android.graphics.Matrix matrix) {
        return android.view.RenderNode.nSetAnimationMatrix(mNativeRenderNode, matrix != null ? matrix.native_instance : 0);
    }

    /**
     * Sets the translucency level for the display list.
     *
     * @param alpha
     * 		The translucency of the display list, must be a value between 0.0f and 1.0f
     * @see View#setAlpha(float)
     * @see #getAlpha()
     */
    public boolean setAlpha(float alpha) {
        return android.view.RenderNode.nSetAlpha(mNativeRenderNode, alpha);
    }

    /**
     * Returns the translucency level of this display list.
     *
     * @return A value between 0.0f and 1.0f
     * @see #setAlpha(float)
     */
    public float getAlpha() {
        return android.view.RenderNode.nGetAlpha(mNativeRenderNode);
    }

    /**
     * Sets whether the display list renders content which overlaps. Non-overlapping rendering
     * can use a fast path for alpha that avoids rendering to an offscreen buffer. By default
     * display lists consider they do not have overlapping content.
     *
     * @param hasOverlappingRendering
     * 		False if the content is guaranteed to be non-overlapping,
     * 		true otherwise.
     * @see android.view.View#hasOverlappingRendering()
     * @see #hasOverlappingRendering()
     */
    public boolean setHasOverlappingRendering(boolean hasOverlappingRendering) {
        return android.view.RenderNode.nSetHasOverlappingRendering(mNativeRenderNode, hasOverlappingRendering);
    }

    /**
     * Indicates whether the content of this display list overlaps.
     *
     * @return True if this display list renders content which overlaps, false otherwise.
     * @see #setHasOverlappingRendering(boolean)
     */
    public boolean hasOverlappingRendering() {
        // noinspection SimplifiableIfStatement
        return android.view.RenderNode.nHasOverlappingRendering(mNativeRenderNode);
    }

    public boolean setElevation(float lift) {
        return android.view.RenderNode.nSetElevation(mNativeRenderNode, lift);
    }

    public float getElevation() {
        return android.view.RenderNode.nGetElevation(mNativeRenderNode);
    }

    /**
     * Sets the translation value for the display list on the X axis.
     *
     * @param translationX
     * 		The X axis translation value of the display list, in pixels
     * @see View#setTranslationX(float)
     * @see #getTranslationX()
     */
    public boolean setTranslationX(float translationX) {
        return android.view.RenderNode.nSetTranslationX(mNativeRenderNode, translationX);
    }

    /**
     * Returns the translation value for this display list on the X axis, in pixels.
     *
     * @see #setTranslationX(float)
     */
    public float getTranslationX() {
        return android.view.RenderNode.nGetTranslationX(mNativeRenderNode);
    }

    /**
     * Sets the translation value for the display list on the Y axis.
     *
     * @param translationY
     * 		The Y axis translation value of the display list, in pixels
     * @see View#setTranslationY(float)
     * @see #getTranslationY()
     */
    public boolean setTranslationY(float translationY) {
        return android.view.RenderNode.nSetTranslationY(mNativeRenderNode, translationY);
    }

    /**
     * Returns the translation value for this display list on the Y axis, in pixels.
     *
     * @see #setTranslationY(float)
     */
    public float getTranslationY() {
        return android.view.RenderNode.nGetTranslationY(mNativeRenderNode);
    }

    /**
     * Sets the translation value for the display list on the Z axis.
     *
     * @see View#setTranslationZ(float)
     * @see #getTranslationZ()
     */
    public boolean setTranslationZ(float translationZ) {
        return android.view.RenderNode.nSetTranslationZ(mNativeRenderNode, translationZ);
    }

    /**
     * Returns the translation value for this display list on the Z axis.
     *
     * @see #setTranslationZ(float)
     */
    public float getTranslationZ() {
        return android.view.RenderNode.nGetTranslationZ(mNativeRenderNode);
    }

    /**
     * Sets the rotation value for the display list around the Z axis.
     *
     * @param rotation
     * 		The rotation value of the display list, in degrees
     * @see View#setRotation(float)
     * @see #getRotation()
     */
    public boolean setRotation(float rotation) {
        return android.view.RenderNode.nSetRotation(mNativeRenderNode, rotation);
    }

    /**
     * Returns the rotation value for this display list around the Z axis, in degrees.
     *
     * @see #setRotation(float)
     */
    public float getRotation() {
        return android.view.RenderNode.nGetRotation(mNativeRenderNode);
    }

    /**
     * Sets the rotation value for the display list around the X axis.
     *
     * @param rotationX
     * 		The rotation value of the display list, in degrees
     * @see View#setRotationX(float)
     * @see #getRotationX()
     */
    public boolean setRotationX(float rotationX) {
        return android.view.RenderNode.nSetRotationX(mNativeRenderNode, rotationX);
    }

    /**
     * Returns the rotation value for this display list around the X axis, in degrees.
     *
     * @see #setRotationX(float)
     */
    public float getRotationX() {
        return android.view.RenderNode.nGetRotationX(mNativeRenderNode);
    }

    /**
     * Sets the rotation value for the display list around the Y axis.
     *
     * @param rotationY
     * 		The rotation value of the display list, in degrees
     * @see View#setRotationY(float)
     * @see #getRotationY()
     */
    public boolean setRotationY(float rotationY) {
        return android.view.RenderNode.nSetRotationY(mNativeRenderNode, rotationY);
    }

    /**
     * Returns the rotation value for this display list around the Y axis, in degrees.
     *
     * @see #setRotationY(float)
     */
    public float getRotationY() {
        return android.view.RenderNode.nGetRotationY(mNativeRenderNode);
    }

    /**
     * Sets the scale value for the display list on the X axis.
     *
     * @param scaleX
     * 		The scale value of the display list
     * @see View#setScaleX(float)
     * @see #getScaleX()
     */
    public boolean setScaleX(float scaleX) {
        return android.view.RenderNode.nSetScaleX(mNativeRenderNode, scaleX);
    }

    /**
     * Returns the scale value for this display list on the X axis.
     *
     * @see #setScaleX(float)
     */
    public float getScaleX() {
        return android.view.RenderNode.nGetScaleX(mNativeRenderNode);
    }

    /**
     * Sets the scale value for the display list on the Y axis.
     *
     * @param scaleY
     * 		The scale value of the display list
     * @see View#setScaleY(float)
     * @see #getScaleY()
     */
    public boolean setScaleY(float scaleY) {
        return android.view.RenderNode.nSetScaleY(mNativeRenderNode, scaleY);
    }

    /**
     * Returns the scale value for this display list on the Y axis.
     *
     * @see #setScaleY(float)
     */
    public float getScaleY() {
        return android.view.RenderNode.nGetScaleY(mNativeRenderNode);
    }

    /**
     * Sets the pivot value for the display list on the X axis
     *
     * @param pivotX
     * 		The pivot value of the display list on the X axis, in pixels
     * @see View#setPivotX(float)
     * @see #getPivotX()
     */
    public boolean setPivotX(float pivotX) {
        return android.view.RenderNode.nSetPivotX(mNativeRenderNode, pivotX);
    }

    /**
     * Returns the pivot value for this display list on the X axis, in pixels.
     *
     * @see #setPivotX(float)
     */
    public float getPivotX() {
        return android.view.RenderNode.nGetPivotX(mNativeRenderNode);
    }

    /**
     * Sets the pivot value for the display list on the Y axis
     *
     * @param pivotY
     * 		The pivot value of the display list on the Y axis, in pixels
     * @see View#setPivotY(float)
     * @see #getPivotY()
     */
    public boolean setPivotY(float pivotY) {
        return android.view.RenderNode.nSetPivotY(mNativeRenderNode, pivotY);
    }

    /**
     * Returns the pivot value for this display list on the Y axis, in pixels.
     *
     * @see #setPivotY(float)
     */
    public float getPivotY() {
        return android.view.RenderNode.nGetPivotY(mNativeRenderNode);
    }

    public boolean isPivotExplicitlySet() {
        return android.view.RenderNode.nIsPivotExplicitlySet(mNativeRenderNode);
    }

    /**
     * lint
     */
    public boolean resetPivot() {
        return android.view.RenderNode.nResetPivot(mNativeRenderNode);
    }

    /**
     * Sets the camera distance for the display list. Refer to
     * {@link View#setCameraDistance(float)} for more information on how to
     * use this property.
     *
     * @param distance
     * 		The distance in Z of the camera of the display list
     * @see View#setCameraDistance(float)
     * @see #getCameraDistance()
     */
    public boolean setCameraDistance(float distance) {
        return android.view.RenderNode.nSetCameraDistance(mNativeRenderNode, distance);
    }

    /**
     * Returns the distance in Z of the camera of the display list.
     *
     * @see #setCameraDistance(float)
     */
    public float getCameraDistance() {
        return android.view.RenderNode.nGetCameraDistance(mNativeRenderNode);
    }

    /**
     * Sets the left position for the display list.
     *
     * @param left
     * 		The left position, in pixels, of the display list
     * @see View#setLeft(int)
     */
    public boolean setLeft(int left) {
        return android.view.RenderNode.nSetLeft(mNativeRenderNode, left);
    }

    /**
     * Sets the top position for the display list.
     *
     * @param top
     * 		The top position, in pixels, of the display list
     * @see View#setTop(int)
     */
    public boolean setTop(int top) {
        return android.view.RenderNode.nSetTop(mNativeRenderNode, top);
    }

    /**
     * Sets the right position for the display list.
     *
     * @param right
     * 		The right position, in pixels, of the display list
     * @see View#setRight(int)
     */
    public boolean setRight(int right) {
        return android.view.RenderNode.nSetRight(mNativeRenderNode, right);
    }

    /**
     * Sets the bottom position for the display list.
     *
     * @param bottom
     * 		The bottom position, in pixels, of the display list
     * @see View#setBottom(int)
     */
    public boolean setBottom(int bottom) {
        return android.view.RenderNode.nSetBottom(mNativeRenderNode, bottom);
    }

    /**
     * Sets the left and top positions for the display list
     *
     * @param left
     * 		The left position of the display list, in pixels
     * @param top
     * 		The top position of the display list, in pixels
     * @param right
     * 		The right position of the display list, in pixels
     * @param bottom
     * 		The bottom position of the display list, in pixels
     * @see View#setLeft(int)
     * @see View#setTop(int)
     * @see View#setRight(int)
     * @see View#setBottom(int)
     */
    public boolean setLeftTopRightBottom(int left, int top, int right, int bottom) {
        return android.view.RenderNode.nSetLeftTopRightBottom(mNativeRenderNode, left, top, right, bottom);
    }

    /**
     * Offsets the left and right positions for the display list
     *
     * @param offset
     * 		The amount that the left and right positions of the display
     * 		list are offset, in pixels
     * @see View#offsetLeftAndRight(int)
     */
    public boolean offsetLeftAndRight(int offset) {
        return android.view.RenderNode.nOffsetLeftAndRight(mNativeRenderNode, offset);
    }

    /**
     * Offsets the top and bottom values for the display list
     *
     * @param offset
     * 		The amount that the top and bottom positions of the display
     * 		list are offset, in pixels
     * @see View#offsetTopAndBottom(int)
     */
    public boolean offsetTopAndBottom(int offset) {
        return android.view.RenderNode.nOffsetTopAndBottom(mNativeRenderNode, offset);
    }

    /**
     * Outputs the display list to the log. This method exists for use by
     * tools to output display lists for selected nodes to the log.
     */
    public void output() {
        android.view.RenderNode.nOutput(mNativeRenderNode);
    }

    /**
     * Gets the size of the DisplayList for debug purposes.
     */
    public int getDebugSize() {
        return android.view.RenderNode.nGetDebugSize(mNativeRenderNode);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Animations
    // /////////////////////////////////////////////////////////////////////////
    public void addAnimator(android.view.RenderNodeAnimator animator) {
        if ((mOwningView == null) || (mOwningView.mAttachInfo == null)) {
            throw new java.lang.IllegalStateException("Cannot start this animator on a detached view!");
        }
        android.view.RenderNode.nAddAnimator(mNativeRenderNode, animator.getNativeAnimator());
        mOwningView.mAttachInfo.mViewRootImpl.registerAnimatingRenderNode(this);
    }

    public boolean isAttached() {
        return (mOwningView != null) && (mOwningView.mAttachInfo != null);
    }

    public void registerVectorDrawableAnimator(android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT animatorSet) {
        if ((mOwningView == null) || (mOwningView.mAttachInfo == null)) {
            throw new java.lang.IllegalStateException("Cannot start this animator on a detached view!");
        }
        mOwningView.mAttachInfo.mViewRootImpl.registerVectorDrawableAnimator(animatorSet);
    }

    public void endAllAnimators() {
        android.view.RenderNode.nEndAllAnimators(mNativeRenderNode);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Regular JNI methods
    // /////////////////////////////////////////////////////////////////////////
    private static native long nCreate(java.lang.String name);

    private static native long nGetNativeFinalizer();

    private static native void nOutput(long renderNode);

    private static native int nGetDebugSize(long renderNode);

    private static native void nRequestPositionUpdates(long renderNode, android.view.SurfaceView callback);

    // Animations
    private static native void nAddAnimator(long renderNode, long animatorPtr);

    private static native void nEndAllAnimators(long renderNode);

    // /////////////////////////////////////////////////////////////////////////
    // @FastNative methods
    // /////////////////////////////////////////////////////////////////////////
    @dalvik.annotation.optimization.FastNative
    private static native void nSetDisplayList(long renderNode, long newData);

    // /////////////////////////////////////////////////////////////////////////
    // @CriticalNative methods
    // /////////////////////////////////////////////////////////////////////////
    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nIsValid(long renderNode);

    // Matrix
    @dalvik.annotation.optimization.CriticalNative
    private static native void nGetTransformMatrix(long renderNode, long nativeMatrix);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nGetInverseTransformMatrix(long renderNode, long nativeMatrix);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nHasIdentityMatrix(long renderNode);

    // Properties
    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nOffsetTopAndBottom(long renderNode, int offset);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nOffsetLeftAndRight(long renderNode, int offset);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetLeftTopRightBottom(long renderNode, int left, int top, int right, int bottom);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetBottom(long renderNode, int bottom);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetRight(long renderNode, int right);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetTop(long renderNode, int top);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetLeft(long renderNode, int left);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetCameraDistance(long renderNode, float distance);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetPivotY(long renderNode, float pivotY);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetPivotX(long renderNode, float pivotX);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nResetPivot(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetLayerType(long renderNode, int layerType);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetLayerPaint(long renderNode, long paint);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetClipToBounds(long renderNode, boolean clipToBounds);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetClipBounds(long renderNode, int left, int top, int right, int bottom);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetClipBoundsEmpty(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetProjectBackwards(long renderNode, boolean shouldProject);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetProjectionReceiver(long renderNode, boolean shouldRecieve);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetOutlineRoundRect(long renderNode, int left, int top, int right, int bottom, float radius, float alpha);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetOutlineConvexPath(long renderNode, long nativePath, float alpha);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetOutlineEmpty(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetOutlineNone(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nHasShadow(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetSpotShadowColor(long renderNode, int color);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetAmbientShadowColor(long renderNode, int color);

    @dalvik.annotation.optimization.CriticalNative
    private static native int nGetSpotShadowColor(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native int nGetAmbientShadowColor(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetClipToOutline(long renderNode, boolean clipToOutline);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetRevealClip(long renderNode, boolean shouldClip, float x, float y, float radius);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetAlpha(long renderNode, float alpha);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetHasOverlappingRendering(long renderNode, boolean hasOverlappingRendering);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetElevation(long renderNode, float lift);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetTranslationX(long renderNode, float translationX);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetTranslationY(long renderNode, float translationY);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetTranslationZ(long renderNode, float translationZ);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetRotation(long renderNode, float rotation);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetRotationX(long renderNode, float rotationX);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetRotationY(long renderNode, float rotationY);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetScaleX(long renderNode, float scaleX);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetScaleY(long renderNode, float scaleY);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetStaticMatrix(long renderNode, long nativeMatrix);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nSetAnimationMatrix(long renderNode, long animationMatrix);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nHasOverlappingRendering(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nGetClipToOutline(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetAlpha(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetCameraDistance(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetScaleX(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetScaleY(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetElevation(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetTranslationX(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetTranslationY(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetTranslationZ(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetRotation(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetRotationX(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetRotationY(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native boolean nIsPivotExplicitlySet(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetPivotX(long renderNode);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetPivotY(long renderNode);
}

