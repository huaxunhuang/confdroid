/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * Handle onto a raw buffer that is being managed by the screen compositor.
 *
 * <p>A Surface is generally created by or from a consumer of image buffers (such as a
 * {@link android.graphics.SurfaceTexture}, {@link android.media.MediaRecorder}, or
 * {@link android.renderscript.Allocation}), and is handed to some kind of producer (such as
 * {@link android.opengl.EGL14#eglCreateWindowSurface(android.opengl.EGLDisplay,android.opengl.EGLConfig,java.lang.Object,int[],int) OpenGL},
 * {@link android.media.MediaPlayer#setSurface MediaPlayer}, or
 * {@link android.hardware.camera2.CameraDevice#createCaptureSession CameraDevice}) to draw
 * into.</p>
 *
 * <p><strong>Note:</strong> A Surface acts like a
 * {@link java.lang.ref.WeakReference weak reference} to the consumer it is associated with. By
 * itself it will not keep its parent consumer from being reclaimed.</p>
 */
public class Surface implements android.os.Parcelable {
    private static final java.lang.String TAG = "Surface";

    private static native long nativeCreateFromSurfaceTexture(android.graphics.SurfaceTexture surfaceTexture) throws android.view.Surface.OutOfResourcesException;

    private static native long nativeCreateFromSurfaceControl(long surfaceControlNativeObject);

    private static native long nativeGetFromSurfaceControl(long surfaceObject, long surfaceControlNativeObject);

    private static native long nativeLockCanvas(long nativeObject, android.graphics.Canvas canvas, android.graphics.Rect dirty) throws android.view.Surface.OutOfResourcesException;

    private static native void nativeUnlockCanvasAndPost(long nativeObject, android.graphics.Canvas canvas);

    @android.annotation.UnsupportedAppUsage
    private static native void nativeRelease(long nativeObject);

    private static native boolean nativeIsValid(long nativeObject);

    private static native boolean nativeIsConsumerRunningBehind(long nativeObject);

    private static native long nativeReadFromParcel(long nativeObject, android.os.Parcel source);

    private static native void nativeWriteToParcel(long nativeObject, android.os.Parcel dest);

    private static native void nativeAllocateBuffers(long nativeObject);

    private static native int nativeGetWidth(long nativeObject);

    private static native int nativeGetHeight(long nativeObject);

    private static native long nativeGetNextFrameNumber(long nativeObject);

    private static native int nativeSetScalingMode(long nativeObject, int scalingMode);

    private static native int nativeForceScopedDisconnect(long nativeObject);

    private static native int nativeAttachAndQueueBuffer(long nativeObject, android.graphics.GraphicBuffer buffer);

    private static native int nativeSetSharedBufferModeEnabled(long nativeObject, boolean enabled);

    private static native int nativeSetAutoRefreshEnabled(long nativeObject, boolean enabled);

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.Surface> CREATOR = new android.os.Parcelable.Creator<android.view.Surface>() {
        @java.lang.Override
        public android.view.Surface createFromParcel(android.os.Parcel source) {
            try {
                android.view.Surface s = new android.view.Surface();
                s.readFromParcel(source);
                return s;
            } catch ( e) {
                android.util.Log.e(android.view.Surface.TAG, "Exception creating surface from parcel", android.view.e);
                return null;
            }
        }

        @java.lang.Override
        public android.view.Surface[] newArray(int size) {
            return new android.view.Surface[size];
        }
    };

    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

    // Guarded state.
    @android.annotation.UnsupportedAppUsage
    final java.lang.Object mLock = new java.lang.Object();// protects the native state


    @android.annotation.UnsupportedAppUsage
    private java.lang.String mName;

    @android.annotation.UnsupportedAppUsage
    long mNativeObject;// package scope only for SurfaceControl access


    @android.annotation.UnsupportedAppUsage
    private long mLockedObject;

    private int mGenerationId;// incremented each time mNativeObject changes


    private final android.graphics.Canvas mCanvas = new android.view.Surface.CompatibleCanvas();

    // A matrix to scale the matrix set by application. This is set to null for
    // non compatibility mode.
    private android.graphics.Matrix mCompatibleMatrix;

    private android.view.Surface.HwuiContext mHwuiContext;

    private boolean mIsSingleBuffered;

    private boolean mIsSharedBufferModeEnabled;

    private boolean mIsAutoRefreshEnabled;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(prefix = { "SCALING_MODE_" }, value = { android.view.Surface.SCALING_MODE_FREEZE, android.view.Surface.SCALING_MODE_SCALE_TO_WINDOW, android.view.Surface.SCALING_MODE_SCALE_CROP, android.view.Surface.SCALING_MODE_NO_SCALE_CROP })
    public @interface ScalingMode {}

    // From system/window.h
    /**
     *
     *
     * @unknown 
     */
    public static final int SCALING_MODE_FREEZE = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int SCALING_MODE_SCALE_TO_WINDOW = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int SCALING_MODE_SCALE_CROP = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int SCALING_MODE_NO_SCALE_CROP = 3;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "ROTATION_" }, value = { android.view.Surface.ROTATION_0, android.view.Surface.ROTATION_90, android.view.Surface.ROTATION_180, android.view.Surface.ROTATION_270 })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Rotation {}

    /**
     * Rotation constant: 0 degree rotation (natural orientation)
     */
    public static final int ROTATION_0 = 0;

    /**
     * Rotation constant: 90 degree rotation.
     */
    public static final int ROTATION_90 = 1;

    /**
     * Rotation constant: 180 degree rotation.
     */
    public static final int ROTATION_180 = 2;

    /**
     * Rotation constant: 270 degree rotation.
     */
    public static final int ROTATION_270 = 3;

    /**
     * Create an empty surface, which will later be filled in by readFromParcel().
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public Surface() {
    }

    /**
     * Create a Surface assosciated with a given {@link SurfaceControl}. Buffers submitted to this
     * surface will be displayed by the system compositor according to the parameters
     * specified by the control. Multiple surfaces may be constructed from one SurfaceControl,
     * but only one can be connected (e.g. have an active EGL context) at a time.
     *
     * @param from
     * 		The SurfaceControl to assosciate this Surface with
     */
    public Surface(@android.annotation.NonNull
    android.view.SurfaceControl from) {
        copyFrom(from);
    }

    /**
     * Create Surface from a {@link SurfaceTexture}.
     *
     * Images drawn to the Surface will be made available to the {@link SurfaceTexture}, which can attach them to an OpenGL ES texture via {@link SurfaceTexture#updateTexImage}.
     *
     * Please note that holding onto the Surface created here is not enough to
     * keep the provided SurfaceTexture from being reclaimed.  In that sense,
     * the Surface will act like a
     * {@link java.lang.ref.WeakReference weak reference} to the SurfaceTexture.
     *
     * @param surfaceTexture
     * 		The {@link SurfaceTexture} that is updated by this
     * 		Surface.
     * @throws OutOfResourcesException
     * 		if the surface could not be created.
     */
    public Surface(android.graphics.SurfaceTexture surfaceTexture) {
        if (surfaceTexture == null) {
            throw new java.lang.IllegalArgumentException("surfaceTexture must not be null");
        }
        mIsSingleBuffered = surfaceTexture.isSingleBuffered();
        synchronized(mLock) {
            mName = surfaceTexture.toString();
            setNativeObjectLocked(android.view.Surface.nativeCreateFromSurfaceTexture(surfaceTexture));
        }
    }

    /* called from android_view_Surface_createFromIGraphicBufferProducer() */
    @android.annotation.UnsupportedAppUsage
    private Surface(long nativeObject) {
        synchronized(mLock) {
            setNativeObjectLocked(nativeObject);
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            if (mCloseGuard != null) {
                mCloseGuard.warnIfOpen();
            }
            release();
        } finally {
            super.finalize();
        }
    }

    /**
     * Release the local reference to the server-side surface.
     * Always call release() when you're done with a Surface.
     * This will make the surface invalid.
     */
    public void release() {
        synchronized(mLock) {
            if (mNativeObject != 0) {
                android.view.Surface.nativeRelease(mNativeObject);
                setNativeObjectLocked(0);
            }
            if (mHwuiContext != null) {
                mHwuiContext.destroy();
                mHwuiContext = null;
            }
        }
    }

    /**
     * Free all server-side state associated with this surface and
     * release this object's reference.  This method can only be
     * called from the process that created the service.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void destroy() {
        release();
    }

    /**
     * Destroys the HwuiContext without completely
     * releasing the Surface.
     *
     * @unknown 
     */
    public void hwuiDestroy() {
        if (mHwuiContext != null) {
            mHwuiContext.destroy();
            mHwuiContext = null;
        }
    }

    /**
     * Returns true if this object holds a valid surface.
     *
     * @return True if it holds a physical surface, so lockCanvas() will succeed.
    Otherwise returns false.
     */
    public boolean isValid() {
        synchronized(mLock) {
            if (mNativeObject == 0)
                return false;

            return android.view.Surface.nativeIsValid(mNativeObject);
        }
    }

    /**
     * Gets the generation number of this surface, incremented each time
     * the native surface contained within this object changes.
     *
     * @return The current generation number.
     * @unknown 
     */
    public int getGenerationId() {
        synchronized(mLock) {
            return mGenerationId;
        }
    }

    /**
     * Returns the next frame number which will be dequeued for rendering.
     * Intended for use with SurfaceFlinger's deferred transactions API.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public long getNextFrameNumber() {
        synchronized(mLock) {
            checkNotReleasedLocked();
            return android.view.Surface.nativeGetNextFrameNumber(mNativeObject);
        }
    }

    /**
     * Returns true if the consumer of this Surface is running behind the producer.
     *
     * @return True if the consumer is more than one buffer ahead of the producer.
     * @unknown 
     */
    public boolean isConsumerRunningBehind() {
        synchronized(mLock) {
            checkNotReleasedLocked();
            return android.view.Surface.nativeIsConsumerRunningBehind(mNativeObject);
        }
    }

    /**
     * Gets a {@link Canvas} for drawing into this surface.
     *
     * After drawing into the provided {@link Canvas}, the caller must
     * invoke {@link #unlockCanvasAndPost} to post the new contents to the surface.
     *
     * @param inOutDirty
     * 		A rectangle that represents the dirty region that the caller wants
     * 		to redraw.  This function may choose to expand the dirty rectangle if for example
     * 		the surface has been resized or if the previous contents of the surface were
     * 		not available.  The caller must redraw the entire dirty region as represented
     * 		by the contents of the inOutDirty rectangle upon return from this function.
     * 		The caller may also pass <code>null</code> instead, in the case where the
     * 		entire surface should be redrawn.
     * @return A canvas for drawing into the surface.
     * @throws IllegalArgumentException
     * 		If the inOutDirty rectangle is not valid.
     * @throws OutOfResourcesException
     * 		If the canvas cannot be locked.
     */
    public android.graphics.Canvas lockCanvas(android.graphics.Rect inOutDirty) throws android.view.Surface.OutOfResourcesException, java.lang.IllegalArgumentException {
        synchronized(mLock) {
            checkNotReleasedLocked();
            if (mLockedObject != 0) {
                // Ideally, nativeLockCanvas() would throw in this situation and prevent the
                // double-lock, but that won't happen if mNativeObject was updated.  We can't
                // abandon the old mLockedObject because it might still be in use, so instead
                // we just refuse to re-lock the Surface.
                throw new java.lang.IllegalArgumentException("Surface was already locked");
            }
            mLockedObject = android.view.Surface.nativeLockCanvas(mNativeObject, mCanvas, inOutDirty);
            return mCanvas;
        }
    }

    /**
     * Posts the new contents of the {@link Canvas} to the surface and
     * releases the {@link Canvas}.
     *
     * @param canvas
     * 		The canvas previously obtained from {@link #lockCanvas}.
     */
    public void unlockCanvasAndPost(android.graphics.Canvas canvas) {
        synchronized(mLock) {
            checkNotReleasedLocked();
            if (mHwuiContext != null) {
                mHwuiContext.unlockAndPost(canvas);
            } else {
                unlockSwCanvasAndPost(canvas);
            }
        }
    }

    private void unlockSwCanvasAndPost(android.graphics.Canvas canvas) {
        if (canvas != mCanvas) {
            throw new java.lang.IllegalArgumentException("canvas object must be the same instance that " + "was previously returned by lockCanvas");
        }
        if (mNativeObject != mLockedObject) {
            android.util.Log.w(android.view.Surface.TAG, ((("WARNING: Surface's mNativeObject (0x" + java.lang.Long.toHexString(mNativeObject)) + ") != mLockedObject (0x") + java.lang.Long.toHexString(mLockedObject)) + ")");
        }
        if (mLockedObject == 0) {
            throw new java.lang.IllegalStateException("Surface was not locked");
        }
        try {
            android.view.Surface.nativeUnlockCanvasAndPost(mLockedObject, canvas);
        } finally {
            android.view.Surface.nativeRelease(mLockedObject);
            mLockedObject = 0;
        }
    }

    /**
     * Gets a {@link Canvas} for drawing into this surface.
     *
     * After drawing into the provided {@link Canvas}, the caller must
     * invoke {@link #unlockCanvasAndPost} to post the new contents to the surface.
     *
     * Unlike {@link #lockCanvas(Rect)} this will return a hardware-accelerated
     * canvas. See the <a href="{@docRoot }guide/topics/graphics/hardware-accel.html#unsupported">
     * unsupported drawing operations</a> for a list of what is and isn't
     * supported in a hardware-accelerated canvas. It is also required to
     * fully cover the surface every time {@link #lockHardwareCanvas()} is
     * called as the buffer is not preserved between frames. Partial updates
     * are not supported.
     *
     * @return A canvas for drawing into the surface.
     * @throws IllegalStateException
     * 		If the canvas cannot be locked.
     */
    public android.graphics.Canvas lockHardwareCanvas() {
        synchronized(mLock) {
            checkNotReleasedLocked();
            if (mHwuiContext == null) {
                mHwuiContext = new android.view.Surface.HwuiContext(false);
            }
            return mHwuiContext.lockCanvas(android.view.Surface.nativeGetWidth(mNativeObject), android.view.Surface.nativeGetHeight(mNativeObject));
        }
    }

    /**
     * Gets a {@link Canvas} for drawing into this surface that supports wide color gamut.
     *
     * After drawing into the provided {@link Canvas}, the caller must
     * invoke {@link #unlockCanvasAndPost} to post the new contents to the surface.
     *
     * Unlike {@link #lockCanvas(Rect)} and {@link #lockHardwareCanvas()},
     * this will return a hardware-accelerated canvas that supports wide color gamut.
     * See the <a href="{@docRoot }guide/topics/graphics/hardware-accel.html#unsupported">
     * unsupported drawing operations</a> for a list of what is and isn't
     * supported in a hardware-accelerated canvas. It is also required to
     * fully cover the surface every time {@link #lockHardwareCanvas()} is
     * called as the buffer is not preserved between frames. Partial updates
     * are not supported.
     *
     * @return A canvas for drawing into the surface.
     * @throws IllegalStateException
     * 		If the canvas cannot be locked.
     * @unknown 
     */
    public android.graphics.Canvas lockHardwareWideColorGamutCanvas() {
        synchronized(mLock) {
            checkNotReleasedLocked();
            if ((mHwuiContext != null) && (!mHwuiContext.isWideColorGamut())) {
                mHwuiContext.destroy();
                mHwuiContext = null;
            }
            if (mHwuiContext == null) {
                mHwuiContext = new android.view.Surface.HwuiContext(true);
            }
            return mHwuiContext.lockCanvas(android.view.Surface.nativeGetWidth(mNativeObject), android.view.Surface.nativeGetHeight(mNativeObject));
        }
    }

    /**
     *
     *
     * @deprecated This API has been removed and is not supported.  Do not use.
     */
    @java.lang.Deprecated
    public void unlockCanvas(android.graphics.Canvas canvas) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * Sets the translator used to scale canvas's width/height in compatibility
     * mode.
     */
    void setCompatibilityTranslator(android.content.res.CompatibilityInfo.Translator translator) {
        if (translator != null) {
            float appScale = translator.applicationScale;
            mCompatibleMatrix = new android.graphics.Matrix();
            mCompatibleMatrix.setScale(appScale, appScale);
        }
    }

    /**
     * Copy another surface to this one.  This surface now holds a reference
     * to the same data as the original surface, and is -not- the owner.
     * This is for use by the window manager when returning a window surface
     * back from a client, converting it from the representation being managed
     * by the window manager to the representation the client uses to draw
     * in to it.
     *
     * @param other
     * 		{@link SurfaceControl} to copy from.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void copyFrom(android.view.SurfaceControl other) {
        if (other == null) {
            throw new java.lang.IllegalArgumentException("other must not be null");
        }
        long surfaceControlPtr = other.mNativeObject;
        if (surfaceControlPtr == 0) {
            throw new java.lang.NullPointerException("null SurfaceControl native object. Are you using a released SurfaceControl?");
        }
        long newNativeObject = android.view.Surface.nativeGetFromSurfaceControl(mNativeObject, surfaceControlPtr);
        synchronized(mLock) {
            if (newNativeObject == mNativeObject) {
                return;
            }
            if (mNativeObject != 0) {
                android.view.Surface.nativeRelease(mNativeObject);
            }
            setNativeObjectLocked(newNativeObject);
        }
    }

    /**
     * Gets a reference a surface created from this one.  This surface now holds a reference
     * to the same data as the original surface, and is -not- the owner.
     * This is for use by the window manager when returning a window surface
     * back from a client, converting it from the representation being managed
     * by the window manager to the representation the client uses to draw
     * in to it.
     *
     * @param other
     * 		{@link SurfaceControl} to create surface from.
     * @unknown 
     */
    public void createFrom(android.view.SurfaceControl other) {
        if (other == null) {
            throw new java.lang.IllegalArgumentException("other must not be null");
        }
        long surfaceControlPtr = other.mNativeObject;
        if (surfaceControlPtr == 0) {
            throw new java.lang.NullPointerException("null SurfaceControl native object. Are you using a released SurfaceControl?");
        }
        long newNativeObject = android.view.Surface.nativeCreateFromSurfaceControl(surfaceControlPtr);
        synchronized(mLock) {
            if (mNativeObject != 0) {
                android.view.Surface.nativeRelease(mNativeObject);
            }
            setNativeObjectLocked(newNativeObject);
        }
    }

    /**
     * This is intended to be used by {@link SurfaceView#updateWindow} only.
     *
     * @param other
     * 		access is not thread safe
     * @unknown 
     * @deprecated 
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    public void transferFrom(android.view.Surface other) {
        if (other == null) {
            throw new java.lang.IllegalArgumentException("other must not be null");
        }
        if (other != this) {
            final long newPtr;
            synchronized(other.mLock) {
                newPtr = other.mNativeObject;
                other.setNativeObjectLocked(0);
            }
            synchronized(mLock) {
                if (mNativeObject != 0) {
                    android.view.Surface.nativeRelease(mNativeObject);
                }
                setNativeObjectLocked(newPtr);
            }
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(android.os.Parcel source) {
        if (source == null) {
            throw new java.lang.IllegalArgumentException("source must not be null");
        }
        synchronized(mLock) {
            // nativeReadFromParcel() will either return mNativeObject, or
            // create a new native Surface and return it after reducing
            // the reference count on mNativeObject.  Either way, it is
            // not necessary to call nativeRelease() here.
            // NOTE: This must be kept synchronized with the native parceling code
            // in frameworks/native/libs/Surface.cpp
            mName = source.readString();
            mIsSingleBuffered = source.readInt() != 0;
            setNativeObjectLocked(android.view.Surface.nativeReadFromParcel(mNativeObject, source));
        }
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (dest == null) {
            throw new java.lang.IllegalArgumentException("dest must not be null");
        }
        synchronized(mLock) {
            // NOTE: This must be kept synchronized with the native parceling code
            // in frameworks/native/libs/Surface.cpp
            dest.writeString(mName);
            dest.writeInt(mIsSingleBuffered ? 1 : 0);
            android.view.Surface.nativeWriteToParcel(mNativeObject, dest);
        }
        if ((flags & android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE) != 0) {
            release();
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        synchronized(mLock) {
            return (("Surface(name=" + mName) + ")/@0x") + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this));
        }
    }

    private void setNativeObjectLocked(long ptr) {
        if (mNativeObject != ptr) {
            if ((mNativeObject == 0) && (ptr != 0)) {
                mCloseGuard.open("release");
            } else
                if ((mNativeObject != 0) && (ptr == 0)) {
                    mCloseGuard.close();
                }

            mNativeObject = ptr;
            mGenerationId += 1;
            if (mHwuiContext != null) {
                mHwuiContext.updateSurface();
            }
        }
    }

    private void checkNotReleasedLocked() {
        if (mNativeObject == 0) {
            throw new java.lang.IllegalStateException("Surface has already been released.");
        }
    }

    /**
     * Allocate buffers ahead of time to avoid allocation delays during rendering
     *
     * @unknown 
     */
    public void allocateBuffers() {
        synchronized(mLock) {
            checkNotReleasedLocked();
            android.view.Surface.nativeAllocateBuffers(mNativeObject);
        }
    }

    /**
     * Set the scaling mode to be used for this surfaces buffers
     *
     * @unknown 
     */
    void setScalingMode(@android.view.Surface.ScalingMode
    int scalingMode) {
        synchronized(mLock) {
            checkNotReleasedLocked();
            int err = android.view.Surface.nativeSetScalingMode(mNativeObject, scalingMode);
            if (err != 0) {
                throw new java.lang.IllegalArgumentException("Invalid scaling mode: " + scalingMode);
            }
        }
    }

    void forceScopedDisconnect() {
        synchronized(mLock) {
            checkNotReleasedLocked();
            int err = android.view.Surface.nativeForceScopedDisconnect(mNativeObject);
            if (err != 0) {
                throw new java.lang.RuntimeException("Failed to disconnect Surface instance (bad object?)");
            }
        }
    }

    /**
     * Transfer ownership of buffer and present it on the Surface.
     *
     * @unknown 
     */
    public void attachAndQueueBuffer(android.graphics.GraphicBuffer buffer) {
        synchronized(mLock) {
            checkNotReleasedLocked();
            int err = android.view.Surface.nativeAttachAndQueueBuffer(mNativeObject, buffer);
            if (err != 0) {
                throw new java.lang.RuntimeException("Failed to attach and queue buffer to Surface (bad object?)");
            }
        }
    }

    /**
     * Returns whether or not this Surface is backed by a single-buffered SurfaceTexture
     *
     * @unknown 
     */
    public boolean isSingleBuffered() {
        return mIsSingleBuffered;
    }

    /**
     * <p>The shared buffer mode allows both the application and the surface compositor
     * (SurfaceFlinger) to concurrently access this surface's buffer. While the
     * application is still required to issue a present request
     * (see {@link #unlockCanvasAndPost(Canvas)}) to the compositor when an update is required,
     * the compositor may trigger an update at any time. Since the surface's buffer is shared
     * between the application and the compositor, updates triggered by the compositor may
     * cause visible tearing.</p>
     *
     * <p>The shared buffer mode can be used with
     * {@link #setAutoRefreshEnabled(boolean) auto-refresh} to avoid the overhead of
     * issuing present requests.</p>
     *
     * <p>If the application uses the shared buffer mode to reduce latency, it is
     * recommended to use software rendering (see {@link #lockCanvas(Rect)} to ensure
     * the graphics workloads are not affected by other applications and/or the system
     * using the GPU. When using software rendering, the application should update the
     * smallest possible region of the surface required.</p>
     *
     * <p class="note">The shared buffer mode might not be supported by the underlying
     * hardware. Enabling shared buffer mode on hardware that does not support it will
     * not yield an error but the application will not benefit from lower latency (and
     * tearing will not be visible).</p>
     *
     * <p class="note">Depending on how many and what kind of surfaces are visible, the
     * surface compositor may need to copy the shared buffer before it is displayed. When
     * this happens, the latency benefits of shared buffer mode will be reduced.</p>
     *
     * @param enabled
     * 		True to enable the shared buffer mode on this surface, false otherwise
     * @see #isSharedBufferModeEnabled()
     * @see #setAutoRefreshEnabled(boolean)
     * @unknown 
     */
    public void setSharedBufferModeEnabled(boolean enabled) {
        if (mIsSharedBufferModeEnabled != enabled) {
            int error = android.view.Surface.nativeSetSharedBufferModeEnabled(mNativeObject, enabled);
            if (error != 0) {
                throw new java.lang.RuntimeException("Failed to set shared buffer mode on Surface (bad object?)");
            } else {
                mIsSharedBufferModeEnabled = enabled;
            }
        }
    }

    /**
     *
     *
     * @return True if shared buffer mode is enabled on this surface, false otherwise
     * @see #setSharedBufferModeEnabled(boolean)
     * @unknown 
     */
    public boolean isSharedBufferModeEnabled() {
        return mIsSharedBufferModeEnabled;
    }

    /**
     * <p>When auto-refresh is enabled, the surface compositor (SurfaceFlinger)
     * automatically updates the display on a regular refresh cycle. The application
     * can continue to issue present requests but it is not required. Enabling
     * auto-refresh may result in visible tearing.</p>
     *
     * <p>Auto-refresh has no effect if the {@link #setSharedBufferModeEnabled(boolean)
     * shared buffer mode} is not enabled.</p>
     *
     * <p>Because auto-refresh will trigger continuous updates of the display, it is
     * recommended to turn it on only when necessary. For example, in a drawing/painting
     * application auto-refresh should be enabled on finger/pen down and disabled on
     * finger/pen up.</p>
     *
     * @param enabled
     * 		True to enable auto-refresh on this surface, false otherwise
     * @see #isAutoRefreshEnabled()
     * @see #setSharedBufferModeEnabled(boolean)
     * @unknown 
     */
    public void setAutoRefreshEnabled(boolean enabled) {
        if (mIsAutoRefreshEnabled != enabled) {
            int error = android.view.Surface.nativeSetAutoRefreshEnabled(mNativeObject, enabled);
            if (error != 0) {
                throw new java.lang.RuntimeException("Failed to set auto refresh on Surface (bad object?)");
            } else {
                mIsAutoRefreshEnabled = enabled;
            }
        }
    }

    /**
     *
     *
     * @return True if auto-refresh is enabled on this surface, false otherwise
     * @unknown 
     */
    public boolean isAutoRefreshEnabled() {
        return mIsAutoRefreshEnabled;
    }

    /**
     * Exception thrown when a Canvas couldn't be locked with {@link Surface#lockCanvas}, or
     * when a SurfaceTexture could not successfully be allocated.
     */
    @java.lang.SuppressWarnings("serial")
    public static class OutOfResourcesException extends java.lang.RuntimeException {
        public OutOfResourcesException() {
        }

        public OutOfResourcesException(java.lang.String name) {
            super(name);
        }
    }

    /**
     * Returns a human readable representation of a rotation.
     *
     * @param rotation
     * 		The rotation.
     * @return The rotation symbolic name.
     * @unknown 
     */
    public static java.lang.String rotationToString(int rotation) {
        switch (rotation) {
            case android.view.Surface.ROTATION_0 :
                {
                    return "ROTATION_0";
                }
            case android.view.Surface.ROTATION_90 :
                {
                    return "ROTATION_90";
                }
            case android.view.Surface.ROTATION_180 :
                {
                    return "ROTATION_180";
                }
            case android.view.Surface.ROTATION_270 :
                {
                    return "ROTATION_270";
                }
            default :
                {
                    return java.lang.Integer.toString(rotation);
                }
        }
    }

    /**
     * A Canvas class that can handle the compatibility mode.
     * This does two things differently.
     * <ul>
     * <li>Returns the width and height of the target metrics, rather than
     * native. For example, the canvas returns 320x480 even if an app is running
     * in WVGA high density.
     * <li>Scales the matrix in setMatrix by the application scale, except if
     * the matrix looks like obtained from getMatrix. This is a hack to handle
     * the case that an application uses getMatrix to keep the original matrix,
     * set matrix of its own, then set the original matrix back. There is no
     * perfect solution that works for all cases, and there are a lot of cases
     * that this model does not work, but we hope this works for many apps.
     * </ul>
     */
    private final class CompatibleCanvas extends android.graphics.Canvas {
        // A temp matrix to remember what an application obtained via {@link getMatrix}
        private android.graphics.Matrix mOrigMatrix = null;

        @java.lang.Override
        public void setMatrix(android.graphics.Matrix matrix) {
            if (((mCompatibleMatrix == null) || (mOrigMatrix == null)) || mOrigMatrix.equals(matrix)) {
                // don't scale the matrix if it's not compatibility mode, or
                // the matrix was obtained from getMatrix.
                super.setMatrix(matrix);
            } else {
                android.graphics.Matrix m = new android.graphics.Matrix(mCompatibleMatrix);
                m.preConcat(matrix);
                super.setMatrix(m);
            }
        }

        @java.lang.SuppressWarnings("deprecation")
        @java.lang.Override
        public void getMatrix(android.graphics.Matrix m) {
            super.getMatrix(m);
            if (mOrigMatrix == null) {
                mOrigMatrix = new android.graphics.Matrix();
            }
            mOrigMatrix.set(m);
        }
    }

    private final class HwuiContext {
        private final android.graphics.RenderNode mRenderNode;

        private long mHwuiRenderer;

        private android.graphics.RecordingCanvas mCanvas;

        private final boolean mIsWideColorGamut;

        HwuiContext(boolean isWideColorGamut) {
            mRenderNode = android.graphics.RenderNode.create("HwuiCanvas", null);
            mRenderNode.setClipToBounds(false);
            mRenderNode.setForceDarkAllowed(false);
            mIsWideColorGamut = isWideColorGamut;
            mHwuiRenderer = android.view.Surface.nHwuiCreate(mRenderNode.mNativeRenderNode, mNativeObject, isWideColorGamut);
        }

        android.graphics.Canvas lockCanvas(int width, int height) {
            if (mCanvas != null) {
                throw new java.lang.IllegalStateException("Surface was already locked!");
            }
            mCanvas = mRenderNode.beginRecording(width, height);
            return mCanvas;
        }

        void unlockAndPost(android.graphics.Canvas canvas) {
            if (canvas != mCanvas) {
                throw new java.lang.IllegalArgumentException("canvas object must be the same instance that " + "was previously returned by lockCanvas");
            }
            mRenderNode.endRecording();
            mCanvas = null;
            android.view.Surface.nHwuiDraw(mHwuiRenderer);
        }

        void updateSurface() {
            android.view.Surface.nHwuiSetSurface(mHwuiRenderer, mNativeObject);
        }

        void destroy() {
            if (mHwuiRenderer != 0) {
                android.view.Surface.nHwuiDestroy(mHwuiRenderer);
                mHwuiRenderer = 0;
            }
        }

        boolean isWideColorGamut() {
            return mIsWideColorGamut;
        }
    }

    private static native long nHwuiCreate(long rootNode, long surface, boolean isWideColorGamut);

    private static native void nHwuiSetSurface(long renderer, long surface);

    private static native void nHwuiDraw(long renderer);

    private static native void nHwuiDestroy(long renderer);
}

