/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * <p>A TextureView can be used to display a content stream. Such a content
 * stream can for instance be a video or an OpenGL scene. The content stream
 * can come from the application's process as well as a remote process.</p>
 *
 * <p>TextureView can only be used in a hardware accelerated window. When
 * rendered in software, TextureView will draw nothing.</p>
 *
 * <p>Unlike {@link SurfaceView}, TextureView does not create a separate
 * window but behaves as a regular View. This key difference allows a
 * TextureView to be moved, transformed, animated, etc. For instance, you
 * can make a TextureView semi-translucent by calling
 * <code>myView.setAlpha(0.5f)</code>.</p>
 *
 * <p>Using a TextureView is simple: all you need to do is get its
 * {@link SurfaceTexture}. The {@link SurfaceTexture} can then be used to
 * render content. The following example demonstrates how to render the
 * camera preview into a TextureView:</p>
 *
 * <pre>
 *  public class LiveCameraActivity extends Activity implements TextureView.SurfaceTextureListener {
 *      private Camera mCamera;
 *      private TextureView mTextureView;
 *
 *      protected void onCreate(Bundle savedInstanceState) {
 *          super.onCreate(savedInstanceState);
 *
 *          mTextureView = new TextureView(this);
 *          mTextureView.setSurfaceTextureListener(this);
 *
 *          setContentView(mTextureView);
 *      }
 *
 *      public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
 *          mCamera = Camera.open();
 *
 *          try {
 *              mCamera.setPreviewTexture(surface);
 *              mCamera.startPreview();
 *          } catch (IOException ioe) {
 *              // Something bad happened
 *          }
 *      }
 *
 *      public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
 *          // Ignored, Camera does all the work for us
 *      }
 *
 *      public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
 *          mCamera.stopPreview();
 *          mCamera.release();
 *          return true;
 *      }
 *
 *      public void onSurfaceTextureUpdated(SurfaceTexture surface) {
 *          // Invoked every time there's a new Camera preview frame
 *      }
 *  }
 * </pre>
 *
 * <p>A TextureView's SurfaceTexture can be obtained either by invoking
 * {@link #getSurfaceTexture()} or by using a {@link SurfaceTextureListener}.
 * It is important to know that a SurfaceTexture is available only after the
 * TextureView is attached to a window (and {@link #onAttachedToWindow()} has
 * been invoked.) It is therefore highly recommended you use a listener to
 * be notified when the SurfaceTexture becomes available.</p>
 *
 * <p>It is important to note that only one producer can use the TextureView.
 * For instance, if you use a TextureView to display the camera preview, you
 * cannot use {@link #lockCanvas()} to draw onto the TextureView at the same
 * time.</p>
 *
 * @see SurfaceView
 * @see SurfaceTexture
 */
public class TextureView extends android.view.View {
    private static final java.lang.String LOG_TAG = "TextureView";

    @android.annotation.UnsupportedAppUsage
    private android.view.TextureLayer mLayer;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.SurfaceTexture mSurface;

    private android.view.TextureView.SurfaceTextureListener mListener;

    private boolean mHadSurface;

    @android.annotation.UnsupportedAppUsage
    private boolean mOpaque = true;

    private final android.graphics.Matrix mMatrix = new android.graphics.Matrix();

    private boolean mMatrixChanged;

    private final java.lang.Object[] mLock = new java.lang.Object[0];

    private boolean mUpdateLayer;

    @android.annotation.UnsupportedAppUsage
    private boolean mUpdateSurface;

    private android.graphics.Canvas mCanvas;

    private int mSaveCount;

    private final java.lang.Object[] mNativeWindowLock = new java.lang.Object[0];

    // Set by native code, do not write!
    @android.annotation.UnsupportedAppUsage
    private long mNativeWindow;

    /**
     * Creates a new TextureView.
     *
     * @param context
     * 		The context to associate this view with.
     */
    public TextureView(android.content.Context context) {
        super(context);
    }

    /**
     * Creates a new TextureView.
     *
     * @param context
     * 		The context to associate this view with.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view.
     */
    public TextureView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Creates a new TextureView.
     *
     * @param context
     * 		The context to associate this view with.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     */
    public TextureView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Creates a new TextureView.
     *
     * @param context
     * 		The context to associate this view with.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     * @param defStyleRes
     * 		A resource identifier of a style resource that
     * 		supplies default values for the view, used only if
     * 		defStyleAttr is 0 or can not be found in the theme. Can be 0
     * 		to not look for defaults.
     */
    public TextureView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public boolean isOpaque() {
        return mOpaque;
    }

    /**
     * Indicates whether the content of this TextureView is opaque. The
     * content is assumed to be opaque by default.
     *
     * @param opaque
     * 		True if the content of this TextureView is opaque,
     * 		false otherwise
     */
    public void setOpaque(boolean opaque) {
        if (opaque != mOpaque) {
            mOpaque = opaque;
            if (mLayer != null) {
                updateLayerAndInvalidate();
            }
        }
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isHardwareAccelerated()) {
            android.util.Log.w(android.view.TextureView.LOG_TAG, "A TextureView or a subclass can only be " + "used with hardware acceleration enabled.");
        }
        if (mHadSurface) {
            invalidate(true);
            mHadSurface = false;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    protected void onDetachedFromWindowInternal() {
        destroyHardwareLayer();
        releaseSurfaceTexture();
        super.onDetachedFromWindowInternal();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    protected void destroyHardwareResources() {
        super.destroyHardwareResources();
        destroyHardwareLayer();
    }

    @android.annotation.UnsupportedAppUsage
    private void destroyHardwareLayer() {
        if (mLayer != null) {
            mLayer.detachSurfaceTexture();
            mLayer.destroy();
            mLayer = null;
            mMatrixChanged = true;
        }
    }

    private void releaseSurfaceTexture() {
        if (mSurface != null) {
            boolean shouldRelease = true;
            if (mListener != null) {
                shouldRelease = mListener.onSurfaceTextureDestroyed(mSurface);
            }
            synchronized(mNativeWindowLock) {
                nDestroyNativeWindow();
            }
            if (shouldRelease) {
                mSurface.release();
            }
            mSurface = null;
            mHadSurface = true;
        }
    }

    /**
     * The layer type of a TextureView is ignored since a TextureView is always
     * considered to act as a hardware layer. The optional paint supplied to this
     * method will however be taken into account when rendering the content of
     * this TextureView.
     *
     * @param layerType
     * 		The type of layer to use with this view, must be one of
     * 		{@link #LAYER_TYPE_NONE}, {@link #LAYER_TYPE_SOFTWARE} or
     * 		{@link #LAYER_TYPE_HARDWARE}
     * @param paint
     * 		The paint used to compose the layer. This argument is optional
     * 		and can be null. It is ignored when the layer type is
     * 		{@link #LAYER_TYPE_NONE}
     */
    @java.lang.Override
    public void setLayerType(int layerType, @android.annotation.Nullable
    android.graphics.Paint paint) {
        setLayerPaint(paint);
    }

    @java.lang.Override
    public void setLayerPaint(@android.annotation.Nullable
    android.graphics.Paint paint) {
        if (paint != mLayerPaint) {
            mLayerPaint = paint;
            invalidate();
        }
    }

    /**
     * Always returns {@link #LAYER_TYPE_HARDWARE}.
     */
    @java.lang.Override
    public int getLayerType() {
        return android.view.View.LAYER_TYPE_HARDWARE;
    }

    /**
     * Calling this method has no effect.
     */
    @java.lang.Override
    public void buildLayer() {
    }

    @java.lang.Override
    public void setForeground(android.graphics.drawable.Drawable foreground) {
        if ((foreground != null) && (!android.view.View.sTextureViewIgnoresDrawableSetters)) {
            throw new java.lang.UnsupportedOperationException("TextureView doesn't support displaying a foreground drawable");
        }
    }

    @java.lang.Override
    public void setBackgroundDrawable(android.graphics.drawable.Drawable background) {
        if ((background != null) && (!android.view.View.sTextureViewIgnoresDrawableSetters)) {
            throw new java.lang.UnsupportedOperationException("TextureView doesn't support displaying a background drawable");
        }
    }

    /**
     * Subclasses of TextureView cannot do their own rendering
     * with the {@link Canvas} object.
     *
     * @param canvas
     * 		The Canvas to which the View is rendered.
     */
    @java.lang.Override
    public final void draw(android.graphics.Canvas canvas) {
        // NOTE: Maintain this carefully (see View#draw)
        mPrivateFlags = (mPrivateFlags & (~android.view.View.PFLAG_DIRTY_MASK)) | android.view.View.PFLAG_DRAWN;
        /* Simplify drawing to guarantee the layer is the only thing drawn - so e.g. no background,
        scrolling, or fading edges. This guarantees all drawing is in the layer, so drawing
        properties (alpha, layer paint) affect all of the content of a TextureView.
         */
        if (canvas.isHardwareAccelerated()) {
            android.graphics.RecordingCanvas recordingCanvas = ((android.graphics.RecordingCanvas) (canvas));
            android.view.TextureLayer layer = getTextureLayer();
            if (layer != null) {
                applyUpdate();
                applyTransformMatrix();
                mLayer.setLayerPaint(mLayerPaint);// ensure layer paint is up to date

                recordingCanvas.drawTextureLayer(layer);
            }
        }
    }

    /**
     * Subclasses of TextureView cannot do their own rendering
     * with the {@link Canvas} object.
     *
     * @param canvas
     * 		The Canvas to which the View is rendered.
     */
    @java.lang.Override
    protected final void onDraw(android.graphics.Canvas canvas) {
    }

    @java.lang.Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mSurface != null) {
            mSurface.setDefaultBufferSize(getWidth(), getHeight());
            updateLayer();
            if (mListener != null) {
                mListener.onSurfaceTextureSizeChanged(mSurface, getWidth(), getHeight());
            }
        }
    }

    android.view.TextureLayer getTextureLayer() {
        if (mLayer == null) {
            if ((mAttachInfo == null) || (mAttachInfo.mThreadedRenderer == null)) {
                return null;
            }
            mLayer = mAttachInfo.mThreadedRenderer.createTextureLayer();
            boolean createNewSurface = mSurface == null;
            if (createNewSurface) {
                // Create a new SurfaceTexture for the layer.
                mSurface = new android.graphics.SurfaceTexture(false);
                nCreateNativeWindow(mSurface);
            }
            mLayer.setSurfaceTexture(mSurface);
            mSurface.setDefaultBufferSize(getWidth(), getHeight());
            mSurface.setOnFrameAvailableListener(mUpdateListener, mAttachInfo.mHandler);
            if ((mListener != null) && createNewSurface) {
                mListener.onSurfaceTextureAvailable(mSurface, getWidth(), getHeight());
            }
            mLayer.setLayerPaint(mLayerPaint);
        }
        if (mUpdateSurface) {
            // Someone has requested that we use a specific SurfaceTexture, so
            // tell mLayer about it and set the SurfaceTexture to use the
            // current view size.
            mUpdateSurface = false;
            // Since we are updating the layer, force an update to ensure its
            // parameters are correct (width, height, transform, etc.)
            updateLayer();
            mMatrixChanged = true;
            mLayer.setSurfaceTexture(mSurface);
            mSurface.setDefaultBufferSize(getWidth(), getHeight());
        }
        return mLayer;
    }

    @java.lang.Override
    protected void onVisibilityChanged(android.view.View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mSurface != null) {
            // When the view becomes invisible, stop updating it, it's a waste of CPU
            // To cancel updates, the easiest thing to do is simply to remove the
            // updates listener
            if (visibility == android.view.View.VISIBLE) {
                if (mLayer != null) {
                    mSurface.setOnFrameAvailableListener(mUpdateListener, mAttachInfo.mHandler);
                }
                updateLayerAndInvalidate();
            } else {
                mSurface.setOnFrameAvailableListener(null);
            }
        }
    }

    private void updateLayer() {
        synchronized(mLock) {
            mUpdateLayer = true;
        }
    }

    private void updateLayerAndInvalidate() {
        synchronized(mLock) {
            mUpdateLayer = true;
        }
        invalidate();
    }

    private void applyUpdate() {
        if (mLayer == null) {
            return;
        }
        synchronized(mLock) {
            if (mUpdateLayer) {
                mUpdateLayer = false;
            } else {
                return;
            }
        }
        mLayer.prepare(getWidth(), getHeight(), mOpaque);
        mLayer.updateSurfaceTexture();
        if (mListener != null) {
            mListener.onSurfaceTextureUpdated(mSurface);
        }
    }

    /**
     * <p>Sets the transform to associate with this texture view.
     * The specified transform applies to the underlying surface
     * texture and does not affect the size or position of the view
     * itself, only of its content.</p>
     *
     * <p>Some transforms might prevent the content from drawing
     * all the pixels contained within this view's bounds. In such
     * situations, make sure this texture view is not marked opaque.</p>
     *
     * @param transform
     * 		The transform to apply to the content of
     * 		this view.
     * @see #getTransform(android.graphics.Matrix)
     * @see #isOpaque()
     * @see #setOpaque(boolean)
     */
    public void setTransform(android.graphics.Matrix transform) {
        mMatrix.set(transform);
        mMatrixChanged = true;
        invalidateParentIfNeeded();
    }

    /**
     * Returns the transform associated with this texture view.
     *
     * @param transform
     * 		The {@link Matrix} in which to copy the current
     * 		transform. Can be null.
     * @return The specified matrix if not null or a new {@link Matrix}
    instance otherwise.
     * @see #setTransform(android.graphics.Matrix)
     */
    public android.graphics.Matrix getTransform(android.graphics.Matrix transform) {
        if (transform == null) {
            transform = new android.graphics.Matrix();
        }
        transform.set(mMatrix);
        return transform;
    }

    private void applyTransformMatrix() {
        if (mMatrixChanged && (mLayer != null)) {
            mLayer.setTransform(mMatrix);
            mMatrixChanged = false;
        }
    }

    /**
     * <p>Returns a {@link android.graphics.Bitmap} representation of the content
     * of the associated surface texture. If the surface texture is not available,
     * this method returns null.</p>
     *
     * <p>The bitmap returned by this method uses the {@link Bitmap.Config#ARGB_8888}
     * pixel format and its dimensions are the same as this view's.</p>
     *
     * <p><strong>Do not</strong> invoke this method from a drawing method
     * ({@link #onDraw(android.graphics.Canvas)} for instance).</p>
     *
     * <p>If an error occurs during the copy, an empty bitmap will be returned.</p>
     *
     * @return A valid {@link Bitmap.Config#ARGB_8888} bitmap, or null if the surface
    texture is not available or the width &lt;= 0 or the height &lt;= 0
     * @see #isAvailable()
     * @see #getBitmap(android.graphics.Bitmap)
     * @see #getBitmap(int, int)
     */
    public android.graphics.Bitmap getBitmap() {
        return getBitmap(getWidth(), getHeight());
    }

    /**
     * <p>Returns a {@link android.graphics.Bitmap} representation of the content
     * of the associated surface texture. If the surface texture is not available,
     * this method returns null.</p>
     *
     * <p>The bitmap returned by this method uses the {@link Bitmap.Config#ARGB_8888}
     * pixel format.</p>
     *
     * <p><strong>Do not</strong> invoke this method from a drawing method
     * ({@link #onDraw(android.graphics.Canvas)} for instance).</p>
     *
     * <p>If an error occurs during the copy, an empty bitmap will be returned.</p>
     *
     * @param width
     * 		The width of the bitmap to create
     * @param height
     * 		The height of the bitmap to create
     * @return A valid {@link Bitmap.Config#ARGB_8888} bitmap, or null if the surface
    texture is not available or width is &lt;= 0 or height is &lt;= 0
     * @see #isAvailable()
     * @see #getBitmap(android.graphics.Bitmap)
     * @see #getBitmap()
     */
    public android.graphics.Bitmap getBitmap(int width, int height) {
        if ((isAvailable() && (width > 0)) && (height > 0)) {
            return getBitmap(android.graphics.Bitmap.createBitmap(getResources().getDisplayMetrics(), width, height, android.graphics.Bitmap.Config.ARGB_8888));
        }
        return null;
    }

    /**
     * <p>Copies the content of this view's surface texture into the specified
     * bitmap. If the surface texture is not available, the copy is not executed.
     * The content of the surface texture will be scaled to fit exactly inside
     * the specified bitmap.</p>
     *
     * <p><strong>Do not</strong> invoke this method from a drawing method
     * ({@link #onDraw(android.graphics.Canvas)} for instance).</p>
     *
     * <p>If an error occurs, the bitmap is left unchanged.</p>
     *
     * @param bitmap
     * 		The bitmap to copy the content of the surface texture into,
     * 		cannot be null, all configurations are supported
     * @return The bitmap specified as a parameter
     * @see #isAvailable()
     * @see #getBitmap(int, int)
     * @see #getBitmap()
     * @throws IllegalStateException
     * 		if the hardware rendering context cannot be
     * 		acquired to capture the bitmap
     */
    public android.graphics.Bitmap getBitmap(android.graphics.Bitmap bitmap) {
        if ((bitmap != null) && isAvailable()) {
            applyUpdate();
            applyTransformMatrix();
            // This case can happen if the app invokes setSurfaceTexture() before
            // we are able to create the hardware layer. We can safely initialize
            // the layer here thanks to the validate() call at the beginning of
            // this method
            if ((mLayer == null) && mUpdateSurface) {
                getTextureLayer();
            }
            if (mLayer != null) {
                mLayer.copyInto(bitmap);
            }
        }
        return bitmap;
    }

    /**
     * Returns true if the {@link SurfaceTexture} associated with this
     * TextureView is available for rendering. When this method returns
     * true, {@link #getSurfaceTexture()} returns a valid surface texture.
     */
    public boolean isAvailable() {
        return mSurface != null;
    }

    /**
     * <p>Start editing the pixels in the surface.  The returned Canvas can be used
     * to draw into the surface's bitmap.  A null is returned if the surface has
     * not been created or otherwise cannot be edited. You will usually need
     * to implement
     * {@link SurfaceTextureListener#onSurfaceTextureAvailable(android.graphics.SurfaceTexture, int, int)}
     * to find out when the Surface is available for use.</p>
     *
     * <p>The content of the Surface is never preserved between unlockCanvas()
     * and lockCanvas(), for this reason, every pixel within the Surface area
     * must be written. The only exception to this rule is when a dirty
     * rectangle is specified, in which case, non-dirty pixels will be
     * preserved.</p>
     *
     * <p>This method can only be used if the underlying surface is not already
     * owned by another producer. For instance, if the TextureView is being used
     * to render the camera's preview you cannot invoke this method.</p>
     *
     * @return A Canvas used to draw into the surface.
     * @see #lockCanvas(android.graphics.Rect)
     * @see #unlockCanvasAndPost(android.graphics.Canvas)
     */
    public android.graphics.Canvas lockCanvas() {
        return lockCanvas(null);
    }

    /**
     * Just like {@link #lockCanvas()} but allows specification of a dirty
     * rectangle. Every pixel within that rectangle must be written; however
     * pixels outside the dirty rectangle will be preserved by the next call
     * to lockCanvas().
     *
     * This method can return null if the underlying surface texture is not
     * available (see {@link #isAvailable()} or if the surface texture is
     * already connected to an image producer (for instance: the camera,
     * OpenGL, a media player, etc.)
     *
     * @param dirty
     * 		Area of the surface that will be modified.
     * @return A Canvas used to draw into the surface.
     * @see #lockCanvas()
     * @see #unlockCanvasAndPost(android.graphics.Canvas)
     * @see #isAvailable()
     */
    public android.graphics.Canvas lockCanvas(android.graphics.Rect dirty) {
        if (!isAvailable())
            return null;

        if (mCanvas == null) {
            mCanvas = new android.graphics.Canvas();
        }
        synchronized(mNativeWindowLock) {
            if (!android.view.TextureView.nLockCanvas(mNativeWindow, mCanvas, dirty)) {
                return null;
            }
        }
        mSaveCount = mCanvas.save();
        return mCanvas;
    }

    /**
     * Finish editing pixels in the surface. After this call, the surface's
     * current pixels will be shown on the screen, but its content is lost,
     * in particular there is no guarantee that the content of the Surface
     * will remain unchanged when lockCanvas() is called again.
     *
     * @param canvas
     * 		The Canvas previously returned by lockCanvas()
     * @see #lockCanvas()
     * @see #lockCanvas(android.graphics.Rect)
     */
    public void unlockCanvasAndPost(android.graphics.Canvas canvas) {
        if ((mCanvas != null) && (canvas == mCanvas)) {
            canvas.restoreToCount(mSaveCount);
            mSaveCount = 0;
            synchronized(mNativeWindowLock) {
                android.view.TextureView.nUnlockCanvasAndPost(mNativeWindow, mCanvas);
            }
        }
    }

    /**
     * Returns the {@link SurfaceTexture} used by this view. This method
     * may return null if the view is not attached to a window or if the surface
     * texture has not been initialized yet.
     *
     * @see #isAvailable()
     */
    public android.graphics.SurfaceTexture getSurfaceTexture() {
        return mSurface;
    }

    /**
     * Set the {@link SurfaceTexture} for this view to use. If a {@link SurfaceTexture} is already being used by this view, it is immediately
     * released and not usable any more.  The {@link SurfaceTextureListener#onSurfaceTextureDestroyed} callback is <b>not</b>
     * called for the previous {@link SurfaceTexture}.  Similarly, the {@link SurfaceTextureListener#onSurfaceTextureAvailable} callback is <b>not</b>
     * called for the {@link SurfaceTexture} passed to setSurfaceTexture.
     *
     * The {@link SurfaceTexture} object must be detached from all OpenGL ES
     * contexts prior to calling this method.
     *
     * @param surfaceTexture
     * 		The {@link SurfaceTexture} that the view should use.
     * @see SurfaceTexture#detachFromGLContext()
     */
    public void setSurfaceTexture(android.graphics.SurfaceTexture surfaceTexture) {
        if (surfaceTexture == null) {
            throw new java.lang.NullPointerException("surfaceTexture must not be null");
        }
        if (surfaceTexture == mSurface) {
            throw new java.lang.IllegalArgumentException("Trying to setSurfaceTexture to " + "the same SurfaceTexture that's already set.");
        }
        if (surfaceTexture.isReleased()) {
            throw new java.lang.IllegalArgumentException("Cannot setSurfaceTexture to a " + "released SurfaceTexture");
        }
        if (mSurface != null) {
            nDestroyNativeWindow();
            mSurface.release();
        }
        mSurface = surfaceTexture;
        nCreateNativeWindow(mSurface);
        /* If the view is visible and we already made a layer, update the
        listener in the new surface to use the existing listener in the view.
        Otherwise this will be called when the view becomes visible or the
        layer is created
         */
        if (((mViewFlags & android.view.View.VISIBILITY_MASK) == android.view.View.VISIBLE) && (mLayer != null)) {
            mSurface.setOnFrameAvailableListener(mUpdateListener, mAttachInfo.mHandler);
        }
        mUpdateSurface = true;
        invalidateParentIfNeeded();
    }

    /**
     * Returns the {@link SurfaceTextureListener} currently associated with this
     * texture view.
     *
     * @see #setSurfaceTextureListener(android.view.TextureView.SurfaceTextureListener)
     * @see SurfaceTextureListener
     */
    public android.view.TextureView.SurfaceTextureListener getSurfaceTextureListener() {
        return mListener;
    }

    /**
     * Sets the {@link SurfaceTextureListener} used to listen to surface
     * texture events.
     *
     * @see #getSurfaceTextureListener()
     * @see SurfaceTextureListener
     */
    public void setSurfaceTextureListener(android.view.TextureView.SurfaceTextureListener listener) {
        mListener = listener;
    }

    @android.annotation.UnsupportedAppUsage
    private final android.graphics.SurfaceTexture.OnFrameAvailableListener mUpdateListener = new android.graphics.SurfaceTexture.OnFrameAvailableListener() {
        @java.lang.Override
        public void onFrameAvailable(android.graphics.SurfaceTexture surfaceTexture) {
            updateLayer();
            invalidate();
        }
    };

    /**
     * This listener can be used to be notified when the surface texture
     * associated with this texture view is available.
     */
    public static interface SurfaceTextureListener {
        /**
         * Invoked when a {@link TextureView}'s SurfaceTexture is ready for use.
         *
         * @param surface
         * 		The surface returned by
         * 		{@link android.view.TextureView#getSurfaceTexture()}
         * @param width
         * 		The width of the surface
         * @param height
         * 		The height of the surface
         */
        public void onSurfaceTextureAvailable(android.graphics.SurfaceTexture surface, int width, int height);

        /**
         * Invoked when the {@link SurfaceTexture}'s buffers size changed.
         *
         * @param surface
         * 		The surface returned by
         * 		{@link android.view.TextureView#getSurfaceTexture()}
         * @param width
         * 		The new width of the surface
         * @param height
         * 		The new height of the surface
         */
        public void onSurfaceTextureSizeChanged(android.graphics.SurfaceTexture surface, int width, int height);

        /**
         * Invoked when the specified {@link SurfaceTexture} is about to be destroyed.
         * If returns true, no rendering should happen inside the surface texture after this method
         * is invoked. If returns false, the client needs to call {@link SurfaceTexture#release()}.
         * Most applications should return true.
         *
         * @param surface
         * 		The surface about to be destroyed
         */
        public boolean onSurfaceTextureDestroyed(android.graphics.SurfaceTexture surface);

        /**
         * Invoked when the specified {@link SurfaceTexture} is updated through
         * {@link SurfaceTexture#updateTexImage()}.
         *
         * @param surface
         * 		The surface just updated
         */
        public void onSurfaceTextureUpdated(android.graphics.SurfaceTexture surface);
    }

    @android.annotation.UnsupportedAppUsage
    private native void nCreateNativeWindow(android.graphics.SurfaceTexture surface);

    @android.annotation.UnsupportedAppUsage
    private native void nDestroyNativeWindow();

    private static native boolean nLockCanvas(long nativeWindow, android.graphics.Canvas canvas, android.graphics.Rect dirty);

    private static native void nUnlockCanvasAndPost(long nativeWindow, android.graphics.Canvas canvas);
}

