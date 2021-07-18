/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * Handle to an on-screen Surface managed by the system compositor. The SurfaceControl is
 * a combination of a buffer source, and metadata about how to display the buffers.
 * By constructing a {@link Surface} from this SurfaceControl you can submit buffers to be
 * composited. Using {@link SurfaceControl.Transaction} you can manipulate various
 * properties of how the buffer will be displayed on-screen. SurfaceControl's are
 * arranged into a scene-graph like hierarchy, and as such any SurfaceControl may have
 * a parent. Geometric properties like transform, crop, and Z-ordering will be inherited
 * from the parent, as if the child were content in the parents buffer stream.
 */
public final class SurfaceControl implements android.os.Parcelable {
    private static final java.lang.String TAG = "SurfaceControl";

    private static native long nativeCreate(android.view.SurfaceSession session, java.lang.String name, int w, int h, int format, int flags, long parentObject, android.os.Parcel metadata) throws android.view.Surface.OutOfResourcesException;

    private static native long nativeReadFromParcel(android.os.Parcel in);

    private static native long nativeCopyFromSurfaceControl(long nativeObject);

    private static native void nativeWriteToParcel(long nativeObject, android.os.Parcel out);

    private static native void nativeRelease(long nativeObject);

    private static native void nativeDestroy(long nativeObject);

    private static native void nativeDisconnect(long nativeObject);

    private static native android.view.SurfaceControl.ScreenshotGraphicBuffer nativeScreenshot(android.os.IBinder displayToken, android.graphics.Rect sourceCrop, int width, int height, boolean useIdentityTransform, int rotation, boolean captureSecureLayers);

    private static native android.view.SurfaceControl.ScreenshotGraphicBuffer nativeCaptureLayers(android.os.IBinder displayToken, android.os.IBinder layerHandleToken, android.graphics.Rect sourceCrop, float frameScale, android.os.IBinder[] excludeLayers);

    private static native long nativeCreateTransaction();

    private static native long nativeGetNativeTransactionFinalizer();

    private static native void nativeApplyTransaction(long transactionObj, boolean sync);

    private static native void nativeMergeTransaction(long transactionObj, long otherTransactionObj);

    private static native void nativeSetAnimationTransaction(long transactionObj);

    private static native void nativeSetEarlyWakeup(long transactionObj);

    private static native void nativeSetLayer(long transactionObj, long nativeObject, int zorder);

    private static native void nativeSetRelativeLayer(long transactionObj, long nativeObject, android.os.IBinder relativeTo, int zorder);

    private static native void nativeSetPosition(long transactionObj, long nativeObject, float x, float y);

    private static native void nativeSetGeometryAppliesWithResize(long transactionObj, long nativeObject);

    private static native void nativeSetSize(long transactionObj, long nativeObject, int w, int h);

    private static native void nativeSetTransparentRegionHint(long transactionObj, long nativeObject, android.graphics.Region region);

    private static native void nativeSetAlpha(long transactionObj, long nativeObject, float alpha);

    private static native void nativeSetMatrix(long transactionObj, long nativeObject, float dsdx, float dtdx, float dtdy, float dsdy);

    private static native void nativeSetColorTransform(long transactionObj, long nativeObject, float[] matrix, float[] translation);

    private static native void nativeSetColorSpaceAgnostic(long transactionObj, long nativeObject, boolean agnostic);

    private static native void nativeSetGeometry(long transactionObj, long nativeObject, android.graphics.Rect sourceCrop, android.graphics.Rect dest, long orientation);

    private static native void nativeSetColor(long transactionObj, long nativeObject, float[] color);

    private static native void nativeSetFlags(long transactionObj, long nativeObject, int flags, int mask);

    private static native void nativeSetWindowCrop(long transactionObj, long nativeObject, int l, int t, int r, int b);

    private static native void nativeSetCornerRadius(long transactionObj, long nativeObject, float cornerRadius);

    private static native void nativeSetLayerStack(long transactionObj, long nativeObject, int layerStack);

    private static native boolean nativeClearContentFrameStats(long nativeObject);

    private static native boolean nativeGetContentFrameStats(long nativeObject, android.view.WindowContentFrameStats outStats);

    private static native boolean nativeClearAnimationFrameStats();

    private static native boolean nativeGetAnimationFrameStats(android.view.WindowAnimationFrameStats outStats);

    private static native long[] nativeGetPhysicalDisplayIds();

    private static native android.os.IBinder nativeGetPhysicalDisplayToken(long physicalDisplayId);

    private static native android.os.IBinder nativeCreateDisplay(java.lang.String name, boolean secure);

    private static native void nativeDestroyDisplay(android.os.IBinder displayToken);

    private static native void nativeSetDisplaySurface(long transactionObj, android.os.IBinder displayToken, long nativeSurfaceObject);

    private static native void nativeSetDisplayLayerStack(long transactionObj, android.os.IBinder displayToken, int layerStack);

    private static native void nativeSetDisplayProjection(long transactionObj, android.os.IBinder displayToken, int orientation, int l, int t, int r, int b, int L, int T, int R, int B);

    private static native void nativeSetDisplaySize(long transactionObj, android.os.IBinder displayToken, int width, int height);

    private static native android.view.SurfaceControl.PhysicalDisplayInfo[] nativeGetDisplayConfigs(android.os.IBinder displayToken);

    private static native android.hardware.display.DisplayedContentSamplingAttributes nativeGetDisplayedContentSamplingAttributes(android.os.IBinder displayToken);

    private static native boolean nativeSetDisplayedContentSamplingEnabled(android.os.IBinder displayToken, boolean enable, int componentMask, int maxFrames);

    private static native android.hardware.display.DisplayedContentSample nativeGetDisplayedContentSample(android.os.IBinder displayToken, long numFrames, long timestamp);

    private static native int nativeGetActiveConfig(android.os.IBinder displayToken);

    private static native boolean nativeSetActiveConfig(android.os.IBinder displayToken, int id);

    private static native boolean nativeSetAllowedDisplayConfigs(android.os.IBinder displayToken, int[] allowedConfigs);

    private static native int[] nativeGetAllowedDisplayConfigs(android.os.IBinder displayToken);

    private static native int[] nativeGetDisplayColorModes(android.os.IBinder displayToken);

    private static native android.view.SurfaceControl.DisplayPrimaries nativeGetDisplayNativePrimaries(android.os.IBinder displayToken);

    private static native int[] nativeGetCompositionDataspaces();

    private static native int nativeGetActiveColorMode(android.os.IBinder displayToken);

    private static native boolean nativeSetActiveColorMode(android.os.IBinder displayToken, int colorMode);

    private static native void nativeSetDisplayPowerMode(android.os.IBinder displayToken, int mode);

    private static native void nativeDeferTransactionUntil(long transactionObj, long nativeObject, android.os.IBinder handle, long frame);

    private static native void nativeDeferTransactionUntilSurface(long transactionObj, long nativeObject, long surfaceObject, long frame);

    private static native void nativeReparentChildren(long transactionObj, long nativeObject, android.os.IBinder handle);

    private static native void nativeReparent(long transactionObj, long nativeObject, long newParentNativeObject);

    private static native void nativeSeverChildren(long transactionObj, long nativeObject);

    private static native void nativeSetOverrideScalingMode(long transactionObj, long nativeObject, int scalingMode);

    private static native android.os.IBinder nativeGetHandle(long nativeObject);

    private static native boolean nativeGetTransformToDisplayInverse(long nativeObject);

    private static native android.view.Display.HdrCapabilities nativeGetHdrCapabilities(android.os.IBinder displayToken);

    private static native void nativeSetInputWindowInfo(long transactionObj, long nativeObject, android.view.InputWindowHandle handle);

    private static native void nativeTransferTouchFocus(long transactionObj, android.os.IBinder fromToken, android.os.IBinder toToken);

    private static native boolean nativeGetProtectedContentSupport();

    private static native void nativeSetMetadata(long transactionObj, long nativeObject, int key, android.os.Parcel data);

    private static native void nativeSyncInputWindows(long transactionObj);

    private static native boolean nativeGetDisplayBrightnessSupport(android.os.IBinder displayToken);

    private static native boolean nativeSetDisplayBrightness(android.os.IBinder displayToken, float brightness);

    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

    private java.lang.String mName;

    long mNativeObject;// package visibility only for Surface.java access


    // TODO: Move this to native.
    private final java.lang.Object mSizeLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("mSizeLock")
    private int mWidth;

    @com.android.internal.annotations.GuardedBy("mSizeLock")
    private int mHeight;

    static android.view.SurfaceControl.Transaction sGlobalTransaction;

    static long sTransactionNestCount = 0;

    /* flags used in constructor (keep in sync with ISurfaceComposerClient.h) */
    /**
     * Surface creation flag: Surface is created hidden
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static final int HIDDEN = 0x4;

    /**
     * Surface creation flag: The surface contains secure content, special
     * measures will be taken to disallow the surface's content to be copied
     * from another process. In particular, screenshots and VNC servers will
     * be disabled, but other measures can take place, for instance the
     * surface might not be hardware accelerated.
     *
     * @unknown 
     */
    public static final int SECURE = 0x80;

    /**
     * Surface creation flag: Creates a surface where color components are interpreted
     * as "non pre-multiplied" by their alpha channel. Of course this flag is
     * meaningless for surfaces without an alpha channel. By default
     * surfaces are pre-multiplied, which means that each color component is
     * already multiplied by its alpha value. In this case the blending
     * equation used is:
     * <p>
     *    <code>DEST = SRC + DEST * (1-SRC_ALPHA)</code>
     * <p>
     * By contrast, non pre-multiplied surfaces use the following equation:
     * <p>
     *    <code>DEST = SRC * SRC_ALPHA * DEST * (1-SRC_ALPHA)</code>
     * <p>
     * pre-multiplied surfaces must always be used if transparent pixels are
     * composited on top of each-other into the surface. A pre-multiplied
     * surface can never lower the value of the alpha component of a given
     * pixel.
     * <p>
     * In some rare situations, a non pre-multiplied surface is preferable.
     *
     * @unknown 
     */
    public static final int NON_PREMULTIPLIED = 0x100;

    /**
     * Surface creation flag: Indicates that the surface must be considered opaque,
     * even if its pixel format contains an alpha channel. This can be useful if an
     * application needs full RGBA 8888 support for instance but will
     * still draw every pixel opaque.
     * <p>
     * This flag is ignored if setAlpha() is used to make the surface non-opaque.
     * Combined effects are (assuming a buffer format with an alpha channel):
     * <ul>
     * <li>OPAQUE + alpha(1.0) == opaque composition
     * <li>OPAQUE + alpha(0.x) == blended composition
     * <li>!OPAQUE + alpha(1.0) == blended composition
     * <li>!OPAQUE + alpha(0.x) == blended composition
     * </ul>
     * If the underlying buffer lacks an alpha channel, the OPAQUE flag is effectively
     * set automatically.
     *
     * @unknown 
     */
    public static final int OPAQUE = 0x400;

    /**
     * Surface creation flag: Application requires a hardware-protected path to an
     * external display sink. If a hardware-protected path is not available,
     * then this surface will not be displayed on the external sink.
     *
     * @unknown 
     */
    public static final int PROTECTED_APP = 0x800;

    // 0x1000 is reserved for an independent DRM protected flag in framework
    /**
     * Surface creation flag: Window represents a cursor glyph.
     *
     * @unknown 
     */
    public static final int CURSOR_WINDOW = 0x2000;

    /**
     * Surface creation flag: Creates a normal surface.
     * This is the default.
     *
     * @unknown 
     */
    public static final int FX_SURFACE_NORMAL = 0x0;

    /**
     * Surface creation flag: Creates a Dim surface.
     * Everything behind this surface is dimmed by the amount specified
     * in {@link #setAlpha}.  It is an error to lock a Dim surface, since it
     * doesn't have a backing store.
     *
     * @unknown 
     */
    public static final int FX_SURFACE_DIM = 0x20000;

    /**
     * Surface creation flag: Creates a container surface.
     * This surface will have no buffers and will only be used
     * as a container for other surfaces, or for its InputInfo.
     *
     * @unknown 
     */
    public static final int FX_SURFACE_CONTAINER = 0x80000;

    /**
     * Mask used for FX values above.
     *
     * @unknown 
     */
    public static final int FX_SURFACE_MASK = 0xf0000;

    /* flags used with setFlags() (keep in sync with ISurfaceComposer.h) */
    /**
     * Surface flag: Hide the surface.
     * Equivalent to calling hide().
     * Updates the value set during Surface creation (see {@link #HIDDEN}).
     */
    private static final int SURFACE_HIDDEN = 0x1;

    /**
     * Surface flag: composite without blending when possible.
     * Updates the value set during Surface creation (see {@link #OPAQUE}).
     */
    private static final int SURFACE_OPAQUE = 0x2;

    // Display power modes.
    /**
     * Display power mode off: used while blanking the screen.
     * Use only with {@link SurfaceControl#setDisplayPowerMode}.
     *
     * @unknown 
     */
    public static final int POWER_MODE_OFF = 0;

    /**
     * Display power mode doze: used while putting the screen into low power mode.
     * Use only with {@link SurfaceControl#setDisplayPowerMode}.
     *
     * @unknown 
     */
    public static final int POWER_MODE_DOZE = 1;

    /**
     * Display power mode normal: used while unblanking the screen.
     * Use only with {@link SurfaceControl#setDisplayPowerMode}.
     *
     * @unknown 
     */
    public static final int POWER_MODE_NORMAL = 2;

    /**
     * Display power mode doze: used while putting the screen into a suspended
     * low power mode.  Use only with {@link SurfaceControl#setDisplayPowerMode}.
     *
     * @unknown 
     */
    public static final int POWER_MODE_DOZE_SUSPEND = 3;

    /**
     * Display power mode on: used while putting the screen into a suspended
     * full power mode.  Use only with {@link SurfaceControl#setDisplayPowerMode}.
     *
     * @unknown 
     */
    public static final int POWER_MODE_ON_SUSPEND = 4;

    /**
     * A value for windowType used to indicate that the window should be omitted from screenshots
     * and display mirroring. A temporary workaround until we express such things with
     * the hierarchy.
     * TODO: b/64227542
     *
     * @unknown 
     */
    public static final int WINDOW_TYPE_DONT_SCREENSHOT = 441731;

    /**
     * internal representation of how to interpret pixel value, used only to convert to ColorSpace.
     */
    private static final int INTERNAL_DATASPACE_SRGB = 142671872;

    private static final int INTERNAL_DATASPACE_DISPLAY_P3 = 143261696;

    private static final int INTERNAL_DATASPACE_SCRGB = 411107328;

    private void assignNativeObject(long nativeObject) {
        if (mNativeObject != 0) {
            release();
        }
        mNativeObject = nativeObject;
    }

    /**
     *
     *
     * @unknown 
     */
    public void copyFrom(android.view.SurfaceControl other) {
        mName = other.mName;
        mWidth = other.mWidth;
        mHeight = other.mHeight;
        assignNativeObject(android.view.SurfaceControl.nativeCopyFromSurfaceControl(other.mNativeObject));
    }

    /**
     * owner UID.
     *
     * @unknown 
     */
    public static final int METADATA_OWNER_UID = 1;

    /**
     * Window type as per {@link WindowManager.LayoutParams}.
     *
     * @unknown 
     */
    public static final int METADATA_WINDOW_TYPE = 2;

    /**
     * Task id to allow association between surfaces and task.
     *
     * @unknown 
     */
    public static final int METADATA_TASK_ID = 3;

    /**
     * A wrapper around GraphicBuffer that contains extra information about how to
     * interpret the screenshot GraphicBuffer.
     *
     * @unknown 
     */
    public static class ScreenshotGraphicBuffer {
        private final android.graphics.GraphicBuffer mGraphicBuffer;

        private final android.graphics.ColorSpace mColorSpace;

        private final boolean mContainsSecureLayers;

        public ScreenshotGraphicBuffer(android.graphics.GraphicBuffer graphicBuffer, android.graphics.ColorSpace colorSpace, boolean containsSecureLayers) {
            mGraphicBuffer = graphicBuffer;
            mColorSpace = colorSpace;
            mContainsSecureLayers = containsSecureLayers;
        }

        /**
         * Create ScreenshotGraphicBuffer from existing native GraphicBuffer object.
         *
         * @param width
         * 		The width in pixels of the buffer
         * @param height
         * 		The height in pixels of the buffer
         * @param format
         * 		The format of each pixel as specified in {@link PixelFormat}
         * @param usage
         * 		Hint indicating how the buffer will be used
         * @param unwrappedNativeObject
         * 		The native object of GraphicBuffer
         * @param namedColorSpace
         * 		Integer value of a named color space {@link ColorSpace.Named}
         * @param containsSecureLayer
         * 		Indicates whether this graphic buffer contains captured contents
         * 		of secure layers, in which case the screenshot should not be persisted.
         */
        private static android.view.SurfaceControl.ScreenshotGraphicBuffer createFromNative(int width, int height, int format, int usage, long unwrappedNativeObject, int namedColorSpace, boolean containsSecureLayers) {
            android.graphics.GraphicBuffer graphicBuffer = android.graphics.GraphicBuffer.createFromExisting(width, height, format, usage, unwrappedNativeObject);
            android.graphics.ColorSpace colorSpace = android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.values()[namedColorSpace]);
            return new android.view.SurfaceControl.ScreenshotGraphicBuffer(graphicBuffer, colorSpace, containsSecureLayers);
        }

        public android.graphics.ColorSpace getColorSpace() {
            return mColorSpace;
        }

        public android.graphics.GraphicBuffer getGraphicBuffer() {
            return mGraphicBuffer;
        }

        public boolean containsSecureLayers() {
            return mContainsSecureLayers;
        }
    }

    /**
     * Builder class for {@link SurfaceControl} objects.
     *
     * By default the surface will be hidden, and have "unset" bounds, meaning it can
     * be as large as the bounds of its parent if a buffer or child so requires.
     *
     * It is necessary to set at least a name via {@link Builder#setName}
     */
    public static class Builder {
        private android.view.SurfaceSession mSession;

        private int mFlags = android.view.SurfaceControl.HIDDEN;

        private int mWidth;

        private int mHeight;

        private int mFormat = android.graphics.PixelFormat.OPAQUE;

        private java.lang.String mName;

        private android.view.SurfaceControl mParent;

        private android.util.SparseIntArray mMetadata;

        /**
         * Begin building a SurfaceControl with a given {@link SurfaceSession}.
         *
         * @param session
         * 		The {@link SurfaceSession} with which to eventually construct the surface.
         * @unknown 
         */
        public Builder(android.view.SurfaceSession session) {
            mSession = session;
        }

        /**
         * Begin building a SurfaceControl.
         */
        public Builder() {
        }

        /**
         * Construct a new {@link SurfaceControl} with the set parameters. The builder
         * remains valid.
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl build() {
            if ((mWidth < 0) || (mHeight < 0)) {
                throw new java.lang.IllegalStateException("width and height must be positive or unset");
            }
            if (((mWidth > 0) || (mHeight > 0)) && (isColorLayerSet() || isContainerLayerSet())) {
                throw new java.lang.IllegalStateException("Only buffer layers can set a valid buffer size.");
            }
            return new android.view.SurfaceControl(mSession, mName, mWidth, mHeight, mFormat, mFlags, mParent, mMetadata);
        }

        /**
         * Set a debugging-name for the SurfaceControl.
         *
         * @param name
         * 		A name to identify the Surface in debugging.
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Builder setName(@android.annotation.NonNull
        java.lang.String name) {
            mName = name;
            return this;
        }

        /**
         * Set the initial size of the controlled surface's buffers in pixels.
         *
         * @param width
         * 		The buffer width in pixels.
         * @param height
         * 		The buffer height in pixels.
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Builder setBufferSize(@android.annotation.IntRange(from = 0)
        int width, @android.annotation.IntRange(from = 0)
        int height) {
            if ((width < 0) || (height < 0)) {
                throw new java.lang.IllegalArgumentException("width and height must be positive");
            }
            mWidth = width;
            mHeight = height;
            // set this as a buffer layer since we are specifying a buffer size.
            return setFlags(android.view.SurfaceControl.FX_SURFACE_NORMAL, android.view.SurfaceControl.FX_SURFACE_MASK);
        }

        /**
         * Set the initial size of the controlled surface's buffers in pixels.
         */
        private void unsetBufferSize() {
            mWidth = 0;
            mHeight = 0;
        }

        /**
         * Set the pixel format of the controlled surface's buffers, using constants from
         * {@link android.graphics.PixelFormat}.
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Builder setFormat(@android.graphics.PixelFormat.Format
        int format) {
            mFormat = format;
            return this;
        }

        /**
         * Specify if the app requires a hardware-protected path to
         * an external display sync. If protected content is enabled, but
         * such a path is not available, then the controlled Surface will
         * not be displayed.
         *
         * @param protectedContent
         * 		Whether to require a protected sink.
         * @unknown 
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Builder setProtected(boolean protectedContent) {
            if (protectedContent) {
                mFlags |= android.view.SurfaceControl.PROTECTED_APP;
            } else {
                mFlags &= ~android.view.SurfaceControl.PROTECTED_APP;
            }
            return this;
        }

        /**
         * Specify whether the Surface contains secure content. If true, the system
         * will prevent the surfaces content from being copied by another process. In
         * particular screenshots and VNC servers will be disabled. This is however
         * not a complete prevention of readback as {@link #setProtected}.
         *
         * @unknown 
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Builder setSecure(boolean secure) {
            if (secure) {
                mFlags |= android.view.SurfaceControl.SECURE;
            } else {
                mFlags &= ~android.view.SurfaceControl.SECURE;
            }
            return this;
        }

        /**
         * Indicates whether the surface must be considered opaque,
         * even if its pixel format is set to translucent. This can be useful if an
         * application needs full RGBA 8888 support for instance but will
         * still draw every pixel opaque.
         * <p>
         * This flag only determines whether opacity will be sampled from the alpha channel.
         * Plane-alpha from calls to setAlpha() can still result in blended composition
         * regardless of the opaque setting.
         *
         * Combined effects are (assuming a buffer format with an alpha channel):
         * <ul>
         * <li>OPAQUE + alpha(1.0) == opaque composition
         * <li>OPAQUE + alpha(0.x) == blended composition
         * <li>OPAQUE + alpha(0.0) == no composition
         * <li>!OPAQUE + alpha(1.0) == blended composition
         * <li>!OPAQUE + alpha(0.x) == blended composition
         * <li>!OPAQUE + alpha(0.0) == no composition
         * </ul>
         * If the underlying buffer lacks an alpha channel, it is as if setOpaque(true)
         * were set automatically.
         *
         * @param opaque
         * 		Whether the Surface is OPAQUE.
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Builder setOpaque(boolean opaque) {
            if (opaque) {
                mFlags |= android.view.SurfaceControl.OPAQUE;
            } else {
                mFlags &= ~android.view.SurfaceControl.OPAQUE;
            }
            return this;
        }

        /**
         * Set a parent surface for our new SurfaceControl.
         *
         * Child surfaces are constrained to the onscreen region of their parent.
         * Furthermore they stack relatively in Z order, and inherit the transformation
         * of the parent.
         *
         * @param parent
         * 		The parent control.
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Builder setParent(@android.annotation.Nullable
        android.view.SurfaceControl parent) {
            mParent = parent;
            return this;
        }

        /**
         * Sets a metadata int.
         *
         * @param key
         * 		metadata key
         * @param data
         * 		associated data
         * @unknown 
         */
        public android.view.SurfaceControl.Builder setMetadata(int key, int data) {
            if (mMetadata == null) {
                mMetadata = new android.util.SparseIntArray();
            }
            mMetadata.put(key, data);
            return this;
        }

        /**
         * Indicate whether a 'ColorLayer' is to be constructed.
         *
         * Color layers will not have an associated BufferQueue and will instead always render a
         * solid color (that is, solid before plane alpha). Currently that color is black.
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Builder setColorLayer() {
            unsetBufferSize();
            return setFlags(android.view.SurfaceControl.FX_SURFACE_DIM, android.view.SurfaceControl.FX_SURFACE_MASK);
        }

        private boolean isColorLayerSet() {
            return (mFlags & android.view.SurfaceControl.FX_SURFACE_DIM) == android.view.SurfaceControl.FX_SURFACE_DIM;
        }

        /**
         * Indicates whether a 'ContainerLayer' is to be constructed.
         *
         * Container layers will not be rendered in any fashion and instead are used
         * as a parent of renderable layers.
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Builder setContainerLayer() {
            unsetBufferSize();
            return setFlags(android.view.SurfaceControl.FX_SURFACE_CONTAINER, android.view.SurfaceControl.FX_SURFACE_MASK);
        }

        private boolean isContainerLayerSet() {
            return (mFlags & android.view.SurfaceControl.FX_SURFACE_CONTAINER) == android.view.SurfaceControl.FX_SURFACE_CONTAINER;
        }

        /**
         * Set 'Surface creation flags' such as {@link #HIDDEN}, {@link #SECURE}.
         *
         * TODO: Finish conversion to individual builder methods?
         *
         * @param flags
         * 		The combined flags
         * @unknown 
         */
        public android.view.SurfaceControl.Builder setFlags(int flags) {
            mFlags = flags;
            return this;
        }

        private android.view.SurfaceControl.Builder setFlags(int flags, int mask) {
            mFlags = (mFlags & (~mask)) | flags;
            return this;
        }
    }

    /**
     * Create a surface with a name.
     * <p>
     * The surface creation flags specify what kind of surface to create and
     * certain options such as whether the surface can be assumed to be opaque
     * and whether it should be initially hidden.  Surfaces should always be
     * created with the {@link #HIDDEN} flag set to ensure that they are not
     * made visible prematurely before all of the surface's properties have been
     * configured.
     * <p>
     * Good practice is to first create the surface with the {@link #HIDDEN} flag
     * specified, open a transaction, set the surface layer, layer stack, alpha,
     * and position, call {@link #show} if appropriate, and close the transaction.
     * <p>
     * Bounds of the surface is determined by its crop and its buffer size. If the
     * surface has no buffer or crop, the surface is boundless and only constrained
     * by the size of its parent bounds.
     *
     * @param session
     * 		The surface session, must not be null.
     * @param name
     * 		The surface name, must not be null.
     * @param w
     * 		The surface initial width.
     * @param h
     * 		The surface initial height.
     * @param flags
     * 		The surface creation flags.  Should always include {@link #HIDDEN}
     * 		in the creation flags.
     * @param metadata
     * 		Initial metadata.
     * @throws throws
     * 		OutOfResourcesException If the SurfaceControl cannot be created.
     */
    private SurfaceControl(android.view.SurfaceSession session, java.lang.String name, int w, int h, int format, int flags, android.view.SurfaceControl parent, android.util.SparseIntArray metadata) throws android.view.Surface.OutOfResourcesException, java.lang.IllegalArgumentException {
        if (name == null) {
            throw new java.lang.IllegalArgumentException("name must not be null");
        }
        if ((flags & android.view.SurfaceControl.HIDDEN) == 0) {
            android.util.Log.w(android.view.SurfaceControl.TAG, ("Surfaces should always be created with the HIDDEN flag set " + ((("to ensure that they are not made visible prematurely before " + "all of the surface's properties have been configured.  ") + "Set the other properties and make the surface visible within ") + "a transaction.  New surface name: ")) + name, new java.lang.Throwable());
        }
        mName = name;
        mWidth = w;
        mHeight = h;
        android.os.Parcel metaParcel = android.os.Parcel.obtain();
        try {
            if ((metadata != null) && (metadata.size() > 0)) {
                metaParcel.writeInt(metadata.size());
                for (int i = 0; i < metadata.size(); ++i) {
                    metaParcel.writeInt(metadata.keyAt(i));
                    metaParcel.writeByteArray(java.nio.ByteBuffer.allocate(4).order(java.nio.ByteOrder.nativeOrder()).putInt(metadata.valueAt(i)).array());
                }
                metaParcel.setDataPosition(0);
            }
            mNativeObject = android.view.SurfaceControl.nativeCreate(session, name, w, h, format, flags, parent != null ? parent.mNativeObject : 0, metaParcel);
        } finally {
            metaParcel.recycle();
        }
        if (mNativeObject == 0) {
            throw new android.view.Surface.OutOfResourcesException("Couldn't allocate SurfaceControl native object");
        }
        mCloseGuard.open("release");
    }

    /**
     * This is a transfer constructor, useful for transferring a live SurfaceControl native
     * object to another Java wrapper which could have some different behavior, e.g.
     * event logging.
     *
     * @unknown 
     */
    public SurfaceControl(android.view.SurfaceControl other) {
        mName = other.mName;
        mWidth = other.mWidth;
        mHeight = other.mHeight;
        mNativeObject = other.mNativeObject;
        other.mCloseGuard.close();
        other.mNativeObject = 0;
        mCloseGuard.open("release");
    }

    private SurfaceControl(android.os.Parcel in) {
        readFromParcel(in);
        mCloseGuard.open("release");
    }

    /**
     *
     *
     * @unknown 
     */
    public SurfaceControl() {
        mCloseGuard.open("release");
    }

    public void readFromParcel(android.os.Parcel in) {
        if (in == null) {
            throw new java.lang.IllegalArgumentException("source must not be null");
        }
        mName = in.readString();
        mWidth = in.readInt();
        mHeight = in.readInt();
        long object = 0;
        if (in.readInt() != 0) {
            object = android.view.SurfaceControl.nativeReadFromParcel(in);
        }
        assignNativeObject(object);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
        if (mNativeObject == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
        }
        android.view.SurfaceControl.nativeWriteToParcel(mNativeObject, dest);
        if ((flags & android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE) != 0) {
            release();
        }
    }

    /**
     * Write to a protocol buffer output stream. Protocol buffer message definition is at {@link android.view.SurfaceControlProto}.
     *
     * @param proto
     * 		Stream to write the SurfaceControl object to.
     * @param fieldId
     * 		Field Id of the SurfaceControl as defined in the parent message.
     * @unknown 
     */
    public void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        final long token = proto.start(fieldId);
        proto.write(android.view.SurfaceControlProto.HASH_CODE, java.lang.System.identityHashCode(this));
        proto.write(android.view.SurfaceControlProto.NAME, mName);
        proto.end(token);
    }

    @android.annotation.NonNull
    public static final android.view.Creator<android.view.SurfaceControl> CREATOR = new android.view.Creator<android.view.SurfaceControl>() {
        public android.view.SurfaceControl createFromParcel(android.os.Parcel in) {
            return new android.view.SurfaceControl(in);
        }

        public android.view.SurfaceControl[] newArray(int size) {
            return new android.view.SurfaceControl[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            if (mCloseGuard != null) {
                mCloseGuard.warnIfOpen();
            }
            if (mNativeObject != 0) {
                android.view.SurfaceControl.nativeRelease(mNativeObject);
            }
        } finally {
            super.finalize();
        }
    }

    /**
     * Release the local reference to the server-side surface. The surface
     * may continue to exist on-screen as long as its parent continues
     * to exist. To explicitly remove a surface from the screen use
     * {@link Transaction#reparent} with a null-parent. After release,
     * {@link #isValid} will return false and other methods will throw
     * an exception.
     *
     * Always call release() when you're done with a SurfaceControl.
     */
    public void release() {
        if (mNativeObject != 0) {
            android.view.SurfaceControl.nativeRelease(mNativeObject);
            mNativeObject = 0;
        }
        mCloseGuard.close();
    }

    /**
     * Release the local resources like {@link #release} but also
     * remove the Surface from the screen.
     *
     * @unknown 
     */
    public void remove() {
        if (mNativeObject != 0) {
            android.view.SurfaceControl.nativeDestroy(mNativeObject);
            mNativeObject = 0;
        }
        mCloseGuard.close();
    }

    /**
     * Disconnect any client still connected to the surface.
     *
     * @unknown 
     */
    public void disconnect() {
        if (mNativeObject != 0) {
            android.view.SurfaceControl.nativeDisconnect(mNativeObject);
        }
    }

    private void checkNotReleased() {
        if (mNativeObject == 0)
            throw new java.lang.NullPointerException("mNativeObject is null. Have you called release() already?");

    }

    /**
     * Check whether this instance points to a valid layer with the system-compositor. For
     * example this may be false if construction failed, or the layer was released
     * ({@link #release}).
     *
     * @return Whether this SurfaceControl is valid.
     */
    public boolean isValid() {
        return mNativeObject != 0;
    }

    /* set surface parameters.
    needs to be inside open/closeTransaction block
     */
    /**
     * start a transaction
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static void openTransaction() {
        synchronized(android.view.SurfaceControl.class) {
            if (android.view.SurfaceControl.sGlobalTransaction == null) {
                android.view.SurfaceControl.sGlobalTransaction = new android.view.SurfaceControl.Transaction();
            }
            synchronized(android.view.SurfaceControl.class) {
                android.view.SurfaceControl.sTransactionNestCount++;
            }
        }
    }

    /**
     * Merge the supplied transaction in to the deprecated "global" transaction.
     * This clears the supplied transaction in an identical fashion to {@link Transaction#merge}.
     * <p>
     * This is a utility for interop with legacy-code and will go away with the Global Transaction.
     *
     * @unknown 
     */
    @java.lang.Deprecated
    public static void mergeToGlobalTransaction(android.view.SurfaceControl.Transaction t) {
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.merge(t);
        }
    }

    /**
     * end a transaction
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static void closeTransaction() {
        synchronized(android.view.SurfaceControl.class) {
            if (android.view.SurfaceControl.sTransactionNestCount == 0) {
                android.util.Log.e(android.view.SurfaceControl.TAG, "Call to SurfaceControl.closeTransaction without matching openTransaction");
            } else
                if ((--android.view.SurfaceControl.sTransactionNestCount) > 0) {
                    return;
                }

            android.view.SurfaceControl.sGlobalTransaction.apply();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void deferTransactionUntil(android.os.IBinder handle, long frame) {
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.deferTransactionUntil(this, handle, frame);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void deferTransactionUntil(android.view.Surface barrier, long frame) {
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.deferTransactionUntilSurface(this, barrier, frame);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void reparentChildren(android.os.IBinder newParentHandle) {
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.reparentChildren(this, newParentHandle);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void reparent(android.view.SurfaceControl newParent) {
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.reparent(this, newParent);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void detachChildren() {
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.detachChildren(this);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setOverrideScalingMode(int scalingMode) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setOverrideScalingMode(this, scalingMode);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public android.os.IBinder getHandle() {
        return android.view.SurfaceControl.nativeGetHandle(mNativeObject);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setAnimationTransaction() {
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setAnimationTransaction();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setLayer(int zorder) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setLayer(this, zorder);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setRelativeLayer(android.view.SurfaceControl relativeTo, int zorder) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setRelativeLayer(this, relativeTo, zorder);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setPosition(float x, float y) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setPosition(this, x, y);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setGeometryAppliesWithResize() {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setGeometryAppliesWithResize(this);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setBufferSize(int w, int h) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setBufferSize(this, w, h);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void hide() {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.hide(this);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void show() {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.show(this);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setTransparentRegionHint(android.graphics.Region region) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setTransparentRegionHint(this, region);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean clearContentFrameStats() {
        checkNotReleased();
        return android.view.SurfaceControl.nativeClearContentFrameStats(mNativeObject);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean getContentFrameStats(android.view.WindowContentFrameStats outStats) {
        checkNotReleased();
        return android.view.SurfaceControl.nativeGetContentFrameStats(mNativeObject, outStats);
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean clearAnimationFrameStats() {
        return android.view.SurfaceControl.nativeClearAnimationFrameStats();
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean getAnimationFrameStats(android.view.WindowAnimationFrameStats outStats) {
        return android.view.SurfaceControl.nativeGetAnimationFrameStats(outStats);
    }

    /**
     *
     *
     * @unknown 
     */
    public void setAlpha(float alpha) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setAlpha(this, alpha);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setColor(@android.annotation.Size(3)
    float[] color) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setColor(this, color);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setMatrix(float dsdx, float dtdx, float dtdy, float dsdy) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setMatrix(this, dsdx, dtdx, dtdy, dsdy);
        }
    }

    /**
     * Sets the transform and position of a {@link SurfaceControl} from a 3x3 transformation matrix.
     *
     * @param matrix
     * 		The matrix to apply.
     * @param float9
     * 		An array of 9 floats to be used to extract the values from the matrix.
     * @unknown 
     */
    public void setMatrix(android.graphics.Matrix matrix, float[] float9) {
        checkNotReleased();
        matrix.getValues(float9);
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setMatrix(this, float9[android.graphics.Matrix.MSCALE_X], float9[android.graphics.Matrix.MSKEW_Y], float9[android.graphics.Matrix.MSKEW_X], float9[android.graphics.Matrix.MSCALE_Y]);
            android.view.SurfaceControl.sGlobalTransaction.setPosition(this, float9[android.graphics.Matrix.MTRANS_X], float9[android.graphics.Matrix.MTRANS_Y]);
        }
    }

    /**
     * Sets the color transform for the Surface.
     *
     * @param matrix
     * 		A float array with 9 values represents a 3x3 transform matrix
     * @param translation
     * 		A float array with 3 values represents a translation vector
     * @unknown 
     */
    public void setColorTransform(@android.annotation.Size(9)
    float[] matrix, @android.annotation.Size(3)
    float[] translation) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setColorTransform(this, matrix, translation);
        }
    }

    /**
     * Sets the Surface to be color space agnostic. If a surface is color space agnostic,
     * the color can be interpreted in any color space.
     *
     * @param agnostic
     * 		A boolean to indicate whether the surface is color space agnostic
     * @unknown 
     */
    public void setColorSpaceAgnostic(boolean agnostic) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setColorSpaceAgnostic(this, agnostic);
        }
    }

    /**
     * Bounds the surface and its children to the bounds specified. Size of the surface will be
     * ignored and only the crop and buffer size will be used to determine the bounds of the
     * surface. If no crop is specified and the surface has no buffer, the surface bounds is only
     * constrained by the size of its parent bounds.
     *
     * @param crop
     * 		Bounds of the crop to apply.
     * @unknown 
     */
    public void setWindowCrop(android.graphics.Rect crop) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setWindowCrop(this, crop);
        }
    }

    /**
     * Same as {@link SurfaceControl#setWindowCrop(Rect)} but sets the crop rect top left at 0, 0.
     *
     * @param width
     * 		width of crop rect
     * @param height
     * 		height of crop rect
     * @unknown 
     */
    public void setWindowCrop(int width, int height) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setWindowCrop(this, width, height);
        }
    }

    /**
     * Sets the corner radius of a {@link SurfaceControl}.
     *
     * @param cornerRadius
     * 		Corner radius in pixels.
     * @unknown 
     */
    public void setCornerRadius(float cornerRadius) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setCornerRadius(this, cornerRadius);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setLayerStack(int layerStack) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setLayerStack(this, layerStack);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setOpaque(boolean isOpaque) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setOpaque(this, isOpaque);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setSecure(boolean isSecure) {
        checkNotReleased();
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setSecure(this, isSecure);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public int getWidth() {
        synchronized(mSizeLock) {
            return mWidth;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public int getHeight() {
        synchronized(mSizeLock) {
            return mHeight;
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (("Surface(name=" + mName) + ")/@0x") + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this));
    }

    /* set display parameters.
    needs to be inside open/closeTransaction block
     */
    /**
     * Describes the properties of a physical display known to surface flinger.
     *
     * @unknown 
     */
    public static final class PhysicalDisplayInfo {
        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public int width;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public int height;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public float refreshRate;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public float density;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public float xDpi;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public float yDpi;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public boolean secure;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public long appVsyncOffsetNanos;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public long presentationDeadlineNanos;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public PhysicalDisplayInfo() {
        }

        /**
         *
         *
         * @unknown 
         */
        public PhysicalDisplayInfo(android.view.SurfaceControl.PhysicalDisplayInfo other) {
            copyFrom(other);
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            return (o instanceof android.view.SurfaceControl.PhysicalDisplayInfo) && equals(((android.view.SurfaceControl.PhysicalDisplayInfo) (o)));
        }

        /**
         *
         *
         * @unknown 
         */
        public boolean equals(android.view.SurfaceControl.PhysicalDisplayInfo other) {
            return (((((((((other != null) && (width == other.width)) && (height == other.height)) && (refreshRate == other.refreshRate)) && (density == other.density)) && (xDpi == other.xDpi)) && (yDpi == other.yDpi)) && (secure == other.secure)) && (appVsyncOffsetNanos == other.appVsyncOffsetNanos)) && (presentationDeadlineNanos == other.presentationDeadlineNanos);
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public int hashCode() {
            return 0;// don't care

        }

        /**
         *
         *
         * @unknown 
         */
        public void copyFrom(android.view.SurfaceControl.PhysicalDisplayInfo other) {
            width = other.width;
            height = other.height;
            refreshRate = other.refreshRate;
            density = other.density;
            xDpi = other.xDpi;
            yDpi = other.yDpi;
            secure = other.secure;
            appVsyncOffsetNanos = other.appVsyncOffsetNanos;
            presentationDeadlineNanos = other.presentationDeadlineNanos;
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public java.lang.String toString() {
            return (((((((((((((((((("PhysicalDisplayInfo{" + width) + " x ") + height) + ", ") + refreshRate) + " fps, ") + "density ") + density) + ", ") + xDpi) + " x ") + yDpi) + " dpi, secure ") + secure) + ", appVsyncOffset ") + appVsyncOffsetNanos) + ", bufferDeadline ") + presentationDeadlineNanos) + "}";
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setDisplayPowerMode(android.os.IBinder displayToken, int mode) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        android.view.SurfaceControl.nativeSetDisplayPowerMode(displayToken, mode);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static android.view.SurfaceControl.PhysicalDisplayInfo[] getDisplayConfigs(android.os.IBinder displayToken) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return android.view.SurfaceControl.nativeGetDisplayConfigs(displayToken);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int getActiveConfig(android.os.IBinder displayToken) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return android.view.SurfaceControl.nativeGetActiveConfig(displayToken);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.hardware.display.DisplayedContentSamplingAttributes getDisplayedContentSamplingAttributes(android.os.IBinder displayToken) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return android.view.SurfaceControl.nativeGetDisplayedContentSamplingAttributes(displayToken);
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean setDisplayedContentSamplingEnabled(android.os.IBinder displayToken, boolean enable, int componentMask, int maxFrames) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        final int maxColorComponents = 4;
        if ((componentMask >> maxColorComponents) != 0) {
            throw new java.lang.IllegalArgumentException("invalid componentMask when enabling sampling");
        }
        return android.view.SurfaceControl.nativeSetDisplayedContentSamplingEnabled(displayToken, enable, componentMask, maxFrames);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.hardware.display.DisplayedContentSample getDisplayedContentSample(android.os.IBinder displayToken, long maxFrames, long timestamp) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return android.view.SurfaceControl.nativeGetDisplayedContentSample(displayToken, maxFrames, timestamp);
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean setActiveConfig(android.os.IBinder displayToken, int id) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return android.view.SurfaceControl.nativeSetActiveConfig(displayToken, id);
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean setAllowedDisplayConfigs(android.os.IBinder displayToken, int[] allowedConfigs) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        if (allowedConfigs == null) {
            throw new java.lang.IllegalArgumentException("allowedConfigs must not be null");
        }
        return android.view.SurfaceControl.nativeSetAllowedDisplayConfigs(displayToken, allowedConfigs);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int[] getAllowedDisplayConfigs(android.os.IBinder displayToken) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return android.view.SurfaceControl.nativeGetAllowedDisplayConfigs(displayToken);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int[] getDisplayColorModes(android.os.IBinder displayToken) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return android.view.SurfaceControl.nativeGetDisplayColorModes(displayToken);
    }

    /**
     * Color coordinates in CIE1931 XYZ color space
     *
     * @unknown 
     */
    public static final class CieXyz {
        /**
         *
         *
         * @unknown 
         */
        public float X;

        /**
         *
         *
         * @unknown 
         */
        public float Y;

        /**
         *
         *
         * @unknown 
         */
        public float Z;
    }

    /**
     * Contains a display's color primaries
     *
     * @unknown 
     */
    public static final class DisplayPrimaries {
        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.CieXyz red;

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.CieXyz green;

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.CieXyz blue;

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.CieXyz white;

        /**
         *
         *
         * @unknown 
         */
        public DisplayPrimaries() {
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.view.SurfaceControl.DisplayPrimaries getDisplayNativePrimaries(android.os.IBinder displayToken) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return android.view.SurfaceControl.nativeGetDisplayNativePrimaries(displayToken);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int getActiveColorMode(android.os.IBinder displayToken) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return android.view.SurfaceControl.nativeGetActiveColorMode(displayToken);
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean setActiveColorMode(android.os.IBinder displayToken, int colorMode) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return android.view.SurfaceControl.nativeSetActiveColorMode(displayToken, colorMode);
    }

    /**
     * Returns an array of color spaces with 2 elements. The first color space is the
     * default color space and second one is wide color gamut color space.
     *
     * @unknown 
     */
    public static android.graphics.ColorSpace[] getCompositionColorSpaces() {
        int[] dataspaces = android.view.SurfaceControl.nativeGetCompositionDataspaces();
        android.graphics.ColorSpace srgb = android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB);
        android.graphics.ColorSpace[] colorSpaces = new android.graphics.ColorSpace[]{ srgb, srgb };
        if (dataspaces.length == 2) {
            for (int i = 0; i < 2; ++i) {
                switch (dataspaces[i]) {
                    case android.view.SurfaceControl.INTERNAL_DATASPACE_DISPLAY_P3 :
                        colorSpaces[i] = android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.DISPLAY_P3);
                        break;
                    case android.view.SurfaceControl.INTERNAL_DATASPACE_SCRGB :
                        colorSpaces[i] = android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.EXTENDED_SRGB);
                        break;
                    case android.view.SurfaceControl.INTERNAL_DATASPACE_SRGB :
                        // Other dataspace is not recognized, use SRGB color space instead,
                        // the default value of the array is already SRGB, thus do nothing.
                    default :
                        break;
                }
            }
        }
        return colorSpaces;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static void setDisplayProjection(android.os.IBinder displayToken, int orientation, android.graphics.Rect layerStackRect, android.graphics.Rect displayRect) {
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setDisplayProjection(displayToken, orientation, layerStackRect, displayRect);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static void setDisplayLayerStack(android.os.IBinder displayToken, int layerStack) {
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setDisplayLayerStack(displayToken, layerStack);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static void setDisplaySurface(android.os.IBinder displayToken, android.view.Surface surface) {
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setDisplaySurface(displayToken, surface);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setDisplaySize(android.os.IBinder displayToken, int width, int height) {
        synchronized(android.view.SurfaceControl.class) {
            android.view.SurfaceControl.sGlobalTransaction.setDisplaySize(displayToken, width, height);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.view.Display.HdrCapabilities getHdrCapabilities(android.os.IBinder displayToken) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return android.view.SurfaceControl.nativeGetHdrCapabilities(displayToken);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static android.os.IBinder createDisplay(java.lang.String name, boolean secure) {
        if (name == null) {
            throw new java.lang.IllegalArgumentException("name must not be null");
        }
        return android.view.SurfaceControl.nativeCreateDisplay(name, secure);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static void destroyDisplay(android.os.IBinder displayToken) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        android.view.SurfaceControl.nativeDestroyDisplay(displayToken);
    }

    /**
     *
     *
     * @unknown 
     */
    public static long[] getPhysicalDisplayIds() {
        return android.view.SurfaceControl.nativeGetPhysicalDisplayIds();
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.os.IBinder getPhysicalDisplayToken(long physicalDisplayId) {
        return android.view.SurfaceControl.nativeGetPhysicalDisplayToken(physicalDisplayId);
    }

    /**
     * TODO(116025192): Remove this stopgap once framework is display-agnostic.
     *
     * @unknown 
     */
    public static android.os.IBinder getInternalDisplayToken() {
        final long[] physicalDisplayIds = android.view.SurfaceControl.getPhysicalDisplayIds();
        if (physicalDisplayIds.length == 0) {
            return null;
        }
        return android.view.SurfaceControl.getPhysicalDisplayToken(physicalDisplayIds[0]);
    }

    /**
     *
     *
     * @see SurfaceControl#screenshot(IBinder, Surface, Rect, int, int, boolean, int)
     * @unknown 
     */
    public static void screenshot(android.os.IBinder display, android.view.Surface consumer) {
        android.view.SurfaceControl.screenshot(display, consumer, new android.graphics.Rect(), 0, 0, false, 0);
    }

    /**
     * Copy the current screen contents into the provided {@link Surface}
     *
     * @param consumer
     * 		The {@link Surface} to take the screenshot into.
     * @see SurfaceControl#screenshotToBuffer(IBinder, Rect, int, int, boolean, int)
     * @unknown 
     */
    public static void screenshot(android.os.IBinder display, android.view.Surface consumer, android.graphics.Rect sourceCrop, int width, int height, boolean useIdentityTransform, int rotation) {
        if (consumer == null) {
            throw new java.lang.IllegalArgumentException("consumer must not be null");
        }
        final android.view.SurfaceControl.ScreenshotGraphicBuffer buffer = android.view.SurfaceControl.screenshotToBuffer(display, sourceCrop, width, height, useIdentityTransform, rotation);
        try {
            consumer.attachAndQueueBuffer(buffer.getGraphicBuffer());
        } catch (java.lang.RuntimeException e) {
            android.util.Log.w(android.view.SurfaceControl.TAG, "Failed to take screenshot - " + e.getMessage());
        }
    }

    /**
     *
     *
     * @see SurfaceControl#screenshot(Rect, int, int, boolean, int)}
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static android.graphics.Bitmap screenshot(android.graphics.Rect sourceCrop, int width, int height, int rotation) {
        return android.view.SurfaceControl.screenshot(sourceCrop, width, height, false, rotation);
    }

    /**
     * Copy the current screen contents into a hardware bitmap and return it.
     * Note: If you want to modify the Bitmap in software, you will need to copy the Bitmap into
     * a software Bitmap using {@link Bitmap#copy(Bitmap.Config, boolean)}
     *
     * CAVEAT: Versions of screenshot that return a {@link Bitmap} can be extremely slow; avoid use
     * unless absolutely necessary; prefer the versions that use a {@link Surface} such as
     * {@link SurfaceControl#screenshot(IBinder, Surface)} or {@link GraphicBuffer} such as
     * {@link SurfaceControl#screenshotToBuffer(IBinder, Rect, int, int, boolean, int)}.
     *
     * @see SurfaceControl#screenshotToBuffer(IBinder, Rect, int, int, boolean, int)}
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static android.graphics.Bitmap screenshot(android.graphics.Rect sourceCrop, int width, int height, boolean useIdentityTransform, int rotation) {
        // TODO: should take the display as a parameter
        final android.os.IBinder displayToken = android.view.SurfaceControl.getInternalDisplayToken();
        if (displayToken == null) {
            android.util.Log.w(android.view.SurfaceControl.TAG, "Failed to take screenshot because internal display is disconnected");
            return null;
        }
        if ((rotation == android.view.Surface.ROTATION_90) || (rotation == android.view.Surface.ROTATION_270)) {
            rotation = (rotation == android.view.Surface.ROTATION_90) ? android.view.Surface.ROTATION_270 : android.view.Surface.ROTATION_90;
        }
        android.view.SurfaceControl.rotateCropForSF(sourceCrop, rotation);
        final android.view.SurfaceControl.ScreenshotGraphicBuffer buffer = android.view.SurfaceControl.screenshotToBuffer(displayToken, sourceCrop, width, height, useIdentityTransform, rotation);
        if (buffer == null) {
            android.util.Log.w(android.view.SurfaceControl.TAG, "Failed to take screenshot");
            return null;
        }
        return android.graphics.Bitmap.wrapHardwareBuffer(buffer.getGraphicBuffer(), buffer.getColorSpace());
    }

    /**
     * Captures all the surfaces in a display and returns a {@link GraphicBuffer} with the content.
     *
     * @param display
     * 		The display to take the screenshot of.
     * @param sourceCrop
     * 		The portion of the screen to capture into the Bitmap; caller may
     * 		pass in 'new Rect()' if no cropping is desired.
     * @param width
     * 		The desired width of the returned bitmap; the raw screen will be
     * 		scaled down to this size; caller may pass in 0 if no scaling is
     * 		desired.
     * @param height
     * 		The desired height of the returned bitmap; the raw screen will
     * 		be scaled down to this size; caller may pass in 0 if no scaling
     * 		is desired.
     * @param useIdentityTransform
     * 		Replace whatever transformation (rotation, scaling, translation)
     * 		the surface layers are currently using with the identity
     * 		transformation while taking the screenshot.
     * @param rotation
     * 		Apply a custom clockwise rotation to the screenshot, i.e.
     * 		Surface.ROTATION_0,90,180,270. SurfaceFlinger will always take
     * 		screenshots in its native portrait orientation by default, so
     * 		this is useful for returning screenshots that are independent of
     * 		device orientation.
     * @return Returns a GraphicBuffer that contains the captured content.
     * @unknown 
     */
    public static android.view.SurfaceControl.ScreenshotGraphicBuffer screenshotToBuffer(android.os.IBinder display, android.graphics.Rect sourceCrop, int width, int height, boolean useIdentityTransform, int rotation) {
        if (display == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return /* captureSecureLayers */
        android.view.SurfaceControl.nativeScreenshot(display, sourceCrop, width, height, useIdentityTransform, rotation, false);
    }

    /**
     * Like screenshotToBuffer, but if the caller is AID_SYSTEM, allows
     * for the capture of secure layers. This is used for the screen rotation
     * animation where the system server takes screenshots but does
     * not persist them or allow them to leave the server. However in other
     * cases in the system server, we mostly want to omit secure layers
     * like when we take a screenshot on behalf of the assistant.
     *
     * @unknown 
     */
    public static android.view.SurfaceControl.ScreenshotGraphicBuffer screenshotToBufferWithSecureLayersUnsafe(android.os.IBinder display, android.graphics.Rect sourceCrop, int width, int height, boolean useIdentityTransform, int rotation) {
        if (display == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        return /* captureSecureLayers */
        android.view.SurfaceControl.nativeScreenshot(display, sourceCrop, width, height, useIdentityTransform, rotation, true);
    }

    private static void rotateCropForSF(android.graphics.Rect crop, int rot) {
        if ((rot == android.view.Surface.ROTATION_90) || (rot == android.view.Surface.ROTATION_270)) {
            int tmp = crop.top;
            crop.top = crop.left;
            crop.left = tmp;
            tmp = crop.right;
            crop.right = crop.bottom;
            crop.bottom = tmp;
        }
    }

    /**
     * Captures a layer and its children and returns a {@link GraphicBuffer} with the content.
     *
     * @param layerHandleToken
     * 		The root layer to capture.
     * @param sourceCrop
     * 		The portion of the root surface to capture; caller may pass in 'new
     * 		Rect()' or null if no cropping is desired.
     * @param frameScale
     * 		The desired scale of the returned buffer; the raw
     * 		screen will be scaled up/down.
     * @return Returns a GraphicBuffer that contains the layer capture.
     * @unknown 
     */
    public static android.view.SurfaceControl.ScreenshotGraphicBuffer captureLayers(android.os.IBinder layerHandleToken, android.graphics.Rect sourceCrop, float frameScale) {
        final android.os.IBinder displayToken = android.view.SurfaceControl.getInternalDisplayToken();
        return android.view.SurfaceControl.nativeCaptureLayers(displayToken, layerHandleToken, sourceCrop, frameScale, null);
    }

    /**
     * Like {@link captureLayers} but with an array of layer handles to exclude.
     *
     * @unknown 
     */
    public static android.view.SurfaceControl.ScreenshotGraphicBuffer captureLayersExcluding(android.os.IBinder layerHandleToken, android.graphics.Rect sourceCrop, float frameScale, android.os.IBinder[] exclude) {
        final android.os.IBinder displayToken = android.view.SurfaceControl.getInternalDisplayToken();
        return android.view.SurfaceControl.nativeCaptureLayers(displayToken, layerHandleToken, sourceCrop, frameScale, exclude);
    }

    /**
     * Returns whether protected content is supported in GPU composition.
     *
     * @unknown 
     */
    public static boolean getProtectedContentSupport() {
        return android.view.SurfaceControl.nativeGetProtectedContentSupport();
    }

    /**
     * Returns whether brightness operations are supported on a display.
     *
     * @param displayToken
     * 		The token for the display.
     * @return Whether brightness operations are supported on the display.
     * @unknown 
     */
    public static boolean getDisplayBrightnessSupport(android.os.IBinder displayToken) {
        return android.view.SurfaceControl.nativeGetDisplayBrightnessSupport(displayToken);
    }

    /**
     * Sets the brightness of a display.
     *
     * @param displayToken
     * 		The token for the display whose brightness is set.
     * @param brightness
     * 		A number between 0.0f (minimum brightness) and 1.0f (maximum brightness), or -1.0f to
     * 		turn the backlight off.
     * @return Whether the method succeeded or not.
     * @throws IllegalArgumentException
     * 		if:
     * 		- displayToken is null;
     * 		- brightness is NaN or greater than 1.0f.
     * @unknown 
     */
    public static boolean setDisplayBrightness(android.os.IBinder displayToken, float brightness) {
        java.util.Objects.requireNonNull(displayToken);
        if ((java.lang.Float.isNaN(brightness) || (brightness > 1.0F)) || ((brightness < 0.0F) && (brightness != (-1.0F)))) {
            throw new java.lang.IllegalArgumentException("brightness must be a number between 0.0f and 1.0f," + " or -1 to turn the backlight off.");
        }
        return android.view.SurfaceControl.nativeSetDisplayBrightness(displayToken, brightness);
    }

    /**
     * An atomic set of changes to a set of SurfaceControl.
     */
    public static class Transaction implements java.io.Closeable {
        /**
         *
         *
         * @unknown 
         */
        public static final libcore.util.NativeAllocationRegistry sRegistry = new libcore.util.NativeAllocationRegistry(android.view.SurfaceControl.Transaction.class.getClassLoader(), android.view.SurfaceControl.nativeGetNativeTransactionFinalizer(), 512);

        private long mNativeObject;

        private final android.util.ArrayMap<android.view.SurfaceControl, android.graphics.Point> mResizedSurfaces = new android.util.ArrayMap();

        java.lang.Runnable mFreeNativeResources;

        /**
         * Open a new transaction object. The transaction may be filed with commands to
         * manipulate {@link SurfaceControl} instances, and then applied atomically with
         * {@link #apply}. Eventually the user should invoke {@link #close}, when the object
         * is no longer required. Note however that re-using a transaction after a call to apply
         * is allowed as a convenience.
         */
        public Transaction() {
            mNativeObject = android.view.SurfaceControl.nativeCreateTransaction();
            mFreeNativeResources = android.view.SurfaceControl.Transaction.sRegistry.registerNativeAllocation(this, mNativeObject);
        }

        /**
         * Apply the transaction, clearing it's state, and making it usable
         * as a new transaction.
         */
        public void apply() {
            apply(false);
        }

        /**
         * Release the native transaction object, without applying it.
         */
        @java.lang.Override
        public void close() {
            mFreeNativeResources.run();
            mNativeObject = 0;
        }

        /**
         * Jankier version of apply. Avoid use (b/28068298).
         *
         * @unknown 
         */
        public void apply(boolean sync) {
            applyResizedSurfaces();
            android.view.SurfaceControl.nativeApplyTransaction(mNativeObject, sync);
        }

        private void applyResizedSurfaces() {
            for (int i = mResizedSurfaces.size() - 1; i >= 0; i--) {
                final android.graphics.Point size = mResizedSurfaces.valueAt(i);
                final android.view.SurfaceControl surfaceControl = mResizedSurfaces.keyAt(i);
                synchronized(surfaceControl.mSizeLock) {
                    surfaceControl.mWidth = size.x;
                    surfaceControl.mHeight = size.y;
                }
            }
            mResizedSurfaces.clear();
        }

        /**
         * Toggle the visibility of a given Layer and it's sub-tree.
         *
         * @param sc
         * 		The SurfaceControl for which to set the visibility
         * @param visible
         * 		The new visibility
         * @return This transaction object.
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Transaction setVisibility(@android.annotation.NonNull
        android.view.SurfaceControl sc, boolean visible) {
            sc.checkNotReleased();
            if (visible) {
                return show(sc);
            } else {
                return hide(sc);
            }
        }

        /**
         * Request that a given surface and it's sub-tree be shown.
         *
         * @param sc
         * 		The surface to show.
         * @return This transaction.
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public android.view.SurfaceControl.Transaction show(android.view.SurfaceControl sc) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetFlags(mNativeObject, sc.mNativeObject, 0, android.view.SurfaceControl.SURFACE_HIDDEN);
            return this;
        }

        /**
         * Request that a given surface and it's sub-tree be hidden.
         *
         * @param sc
         * 		The surface to hidden.
         * @return This transaction.
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public android.view.SurfaceControl.Transaction hide(android.view.SurfaceControl sc) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetFlags(mNativeObject, sc.mNativeObject, android.view.SurfaceControl.SURFACE_HIDDEN, android.view.SurfaceControl.SURFACE_HIDDEN);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public android.view.SurfaceControl.Transaction setPosition(android.view.SurfaceControl sc, float x, float y) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetPosition(mNativeObject, sc.mNativeObject, x, y);
            return this;
        }

        /**
         * Set the default buffer size for the SurfaceControl, if there is a
         * {@link Surface} associated with the control, then
         * this will be the default size for buffers dequeued from it.
         *
         * @param sc
         * 		The surface to set the buffer size for.
         * @param w
         * 		The default width
         * @param h
         * 		The default height
         * @return This Transaction
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Transaction setBufferSize(@android.annotation.NonNull
        android.view.SurfaceControl sc, @android.annotation.IntRange(from = 0)
        int w, @android.annotation.IntRange(from = 0)
        int h) {
            sc.checkNotReleased();
            mResizedSurfaces.put(sc, new android.graphics.Point(w, h));
            android.view.SurfaceControl.nativeSetSize(mNativeObject, sc.mNativeObject, w, h);
            return this;
        }

        /**
         * Set the Z-order for a given SurfaceControl, relative to it's siblings.
         * If two siblings share the same Z order the ordering is undefined. Surfaces
         * with a negative Z will be placed below the parent surface.
         *
         * @param sc
         * 		The SurfaceControl to set the Z order on
         * @param z
         * 		The Z-order
         * @return This Transaction.
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Transaction setLayer(@android.annotation.NonNull
        android.view.SurfaceControl sc, @android.annotation.IntRange(from = java.lang.Integer.MIN_VALUE, to = java.lang.Integer.MAX_VALUE)
        int z) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetLayer(mNativeObject, sc.mNativeObject, z);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setRelativeLayer(android.view.SurfaceControl sc, android.view.SurfaceControl relativeTo, int z) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetRelativeLayer(mNativeObject, sc.mNativeObject, relativeTo.getHandle(), z);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setTransparentRegionHint(android.view.SurfaceControl sc, android.graphics.Region transparentRegion) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetTransparentRegionHint(mNativeObject, sc.mNativeObject, transparentRegion);
            return this;
        }

        /**
         * Set the alpha for a given surface. If the alpha is non-zero the SurfaceControl
         * will be blended with the Surfaces under it according to the specified ratio.
         *
         * @param sc
         * 		The given SurfaceControl.
         * @param alpha
         * 		The alpha to set.
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Transaction setAlpha(@android.annotation.NonNull
        android.view.SurfaceControl sc, @android.annotation.FloatRange(from = 0.0, to = 1.0)
        float alpha) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetAlpha(mNativeObject, sc.mNativeObject, alpha);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setInputWindowInfo(android.view.SurfaceControl sc, android.view.InputWindowHandle handle) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetInputWindowInfo(mNativeObject, sc.mNativeObject, handle);
            return this;
        }

        /**
         * Transfers touch focus from one window to another. It is possible for multiple windows to
         * have touch focus if they support split touch dispatch
         * {@link android.view.WindowManager.LayoutParams#FLAG_SPLIT_TOUCH} but this
         * method only transfers touch focus of the specified window without affecting
         * other windows that may also have touch focus at the same time.
         *
         * @param fromToken
         * 		The token of a window that currently has touch focus.
         * @param toToken
         * 		The token of the window that should receive touch focus in
         * 		place of the first.
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction transferTouchFocus(android.os.IBinder fromToken, android.os.IBinder toToken) {
            android.view.SurfaceControl.nativeTransferTouchFocus(mNativeObject, fromToken, toToken);
            return this;
        }

        /**
         * Waits until any changes to input windows have been sent from SurfaceFlinger to
         * InputFlinger before returning.
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction syncInputWindows() {
            android.view.SurfaceControl.nativeSyncInputWindows(mNativeObject);
            return this;
        }

        /**
         * Specify how the buffer assosciated with this Surface is mapped in to the
         * parent coordinate space. The source frame will be scaled to fit the destination
         * frame, after being rotated according to the orientation parameter.
         *
         * @param sc
         * 		The SurfaceControl to specify the geometry of
         * @param sourceCrop
         * 		The source rectangle in buffer space. Or null for the entire buffer.
         * @param destFrame
         * 		The destination rectangle in parent space. Or null for the source frame.
         * @param orientation
         * 		The buffer rotation
         * @return This transaction object.
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Transaction setGeometry(@android.annotation.NonNull
        android.view.SurfaceControl sc, @android.annotation.Nullable
        android.graphics.Rect sourceCrop, @android.annotation.Nullable
        android.graphics.Rect destFrame, @android.view.Surface.Rotation
        int orientation) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetGeometry(mNativeObject, sc.mNativeObject, sourceCrop, destFrame, orientation);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public android.view.SurfaceControl.Transaction setMatrix(android.view.SurfaceControl sc, float dsdx, float dtdx, float dtdy, float dsdy) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetMatrix(mNativeObject, sc.mNativeObject, dsdx, dtdx, dtdy, dsdy);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public android.view.SurfaceControl.Transaction setMatrix(android.view.SurfaceControl sc, android.graphics.Matrix matrix, float[] float9) {
            matrix.getValues(float9);
            setMatrix(sc, float9[android.graphics.Matrix.MSCALE_X], float9[android.graphics.Matrix.MSKEW_Y], float9[android.graphics.Matrix.MSKEW_X], float9[android.graphics.Matrix.MSCALE_Y]);
            setPosition(sc, float9[android.graphics.Matrix.MTRANS_X], float9[android.graphics.Matrix.MTRANS_Y]);
            return this;
        }

        /**
         * Sets the color transform for the Surface.
         *
         * @param matrix
         * 		A float array with 9 values represents a 3x3 transform matrix
         * @param translation
         * 		A float array with 3 values represents a translation vector
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setColorTransform(android.view.SurfaceControl sc, @android.annotation.Size(9)
        float[] matrix, @android.annotation.Size(3)
        float[] translation) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetColorTransform(mNativeObject, sc.mNativeObject, matrix, translation);
            return this;
        }

        /**
         * Sets the Surface to be color space agnostic. If a surface is color space agnostic,
         * the color can be interpreted in any color space.
         *
         * @param agnostic
         * 		A boolean to indicate whether the surface is color space agnostic
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setColorSpaceAgnostic(android.view.SurfaceControl sc, boolean agnostic) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetColorSpaceAgnostic(mNativeObject, sc.mNativeObject, agnostic);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public android.view.SurfaceControl.Transaction setWindowCrop(android.view.SurfaceControl sc, android.graphics.Rect crop) {
            sc.checkNotReleased();
            if (crop != null) {
                android.view.SurfaceControl.nativeSetWindowCrop(mNativeObject, sc.mNativeObject, crop.left, crop.top, crop.right, crop.bottom);
            } else {
                android.view.SurfaceControl.nativeSetWindowCrop(mNativeObject, sc.mNativeObject, 0, 0, 0, 0);
            }
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setWindowCrop(android.view.SurfaceControl sc, int width, int height) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetWindowCrop(mNativeObject, sc.mNativeObject, 0, 0, width, height);
            return this;
        }

        /**
         * Sets the corner radius of a {@link SurfaceControl}.
         *
         * @param sc
         * 		SurfaceControl
         * @param cornerRadius
         * 		Corner radius in pixels.
         * @return Itself.
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public android.view.SurfaceControl.Transaction setCornerRadius(android.view.SurfaceControl sc, float cornerRadius) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetCornerRadius(mNativeObject, sc.mNativeObject, cornerRadius);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.O)
        public android.view.SurfaceControl.Transaction setLayerStack(android.view.SurfaceControl sc, int layerStack) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetLayerStack(mNativeObject, sc.mNativeObject, layerStack);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public android.view.SurfaceControl.Transaction deferTransactionUntil(android.view.SurfaceControl sc, android.os.IBinder handle, long frameNumber) {
            if (frameNumber < 0) {
                return this;
            }
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeDeferTransactionUntil(mNativeObject, sc.mNativeObject, handle, frameNumber);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public android.view.SurfaceControl.Transaction deferTransactionUntilSurface(android.view.SurfaceControl sc, android.view.Surface barrierSurface, long frameNumber) {
            if (frameNumber < 0) {
                return this;
            }
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeDeferTransactionUntilSurface(mNativeObject, sc.mNativeObject, barrierSurface.mNativeObject, frameNumber);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction reparentChildren(android.view.SurfaceControl sc, android.os.IBinder newParentHandle) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeReparentChildren(mNativeObject, sc.mNativeObject, newParentHandle);
            return this;
        }

        /**
         * Re-parents a given layer to a new parent. Children inherit transform (position, scaling)
         * crop, visibility, and Z-ordering from their parents, as if the children were pixels within the
         * parent Surface.
         *
         * @param sc
         * 		The SurfaceControl to reparent
         * @param newParent
         * 		The new parent for the given control.
         * @return This Transaction
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Transaction reparent(@android.annotation.NonNull
        android.view.SurfaceControl sc, @android.annotation.Nullable
        android.view.SurfaceControl newParent) {
            sc.checkNotReleased();
            long otherObject = 0;
            if (newParent != null) {
                newParent.checkNotReleased();
                otherObject = newParent.mNativeObject;
            }
            android.view.SurfaceControl.nativeReparent(mNativeObject, sc.mNativeObject, otherObject);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction detachChildren(android.view.SurfaceControl sc) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSeverChildren(mNativeObject, sc.mNativeObject);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setOverrideScalingMode(android.view.SurfaceControl sc, int overrideScalingMode) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetOverrideScalingMode(mNativeObject, sc.mNativeObject, overrideScalingMode);
            return this;
        }

        /**
         * Sets a color for the Surface.
         *
         * @param color
         * 		A float array with three values to represent r, g, b in range [0..1]
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public android.view.SurfaceControl.Transaction setColor(android.view.SurfaceControl sc, @android.annotation.Size(3)
        float[] color) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetColor(mNativeObject, sc.mNativeObject, color);
            return this;
        }

        /**
         * If the buffer size changes in this transaction, position and crop updates specified
         * in this transaction will not complete until a buffer of the new size
         * arrives. As transform matrix and size are already frozen in this fashion,
         * this enables totally freezing the surface until the resize has completed
         * (at which point the geometry influencing aspects of this transaction will then occur)
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setGeometryAppliesWithResize(android.view.SurfaceControl sc) {
            sc.checkNotReleased();
            android.view.SurfaceControl.nativeSetGeometryAppliesWithResize(mNativeObject, sc.mNativeObject);
            return this;
        }

        /**
         * Sets the security of the surface.  Setting the flag is equivalent to creating the
         * Surface with the {@link #SECURE} flag.
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setSecure(android.view.SurfaceControl sc, boolean isSecure) {
            sc.checkNotReleased();
            if (isSecure) {
                android.view.SurfaceControl.nativeSetFlags(mNativeObject, sc.mNativeObject, android.view.SurfaceControl.SECURE, android.view.SurfaceControl.SECURE);
            } else {
                android.view.SurfaceControl.nativeSetFlags(mNativeObject, sc.mNativeObject, 0, android.view.SurfaceControl.SECURE);
            }
            return this;
        }

        /**
         * Sets the opacity of the surface.  Setting the flag is equivalent to creating the
         * Surface with the {@link #OPAQUE} flag.
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setOpaque(android.view.SurfaceControl sc, boolean isOpaque) {
            sc.checkNotReleased();
            if (isOpaque) {
                android.view.SurfaceControl.nativeSetFlags(mNativeObject, sc.mNativeObject, android.view.SurfaceControl.SURFACE_OPAQUE, android.view.SurfaceControl.SURFACE_OPAQUE);
            } else {
                android.view.SurfaceControl.nativeSetFlags(mNativeObject, sc.mNativeObject, 0, android.view.SurfaceControl.SURFACE_OPAQUE);
            }
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setDisplaySurface(android.os.IBinder displayToken, android.view.Surface surface) {
            if (displayToken == null) {
                throw new java.lang.IllegalArgumentException("displayToken must not be null");
            }
            if (surface != null) {
                synchronized(surface.mLock) {
                    android.view.SurfaceControl.nativeSetDisplaySurface(mNativeObject, displayToken, surface.mNativeObject);
                }
            } else {
                android.view.SurfaceControl.nativeSetDisplaySurface(mNativeObject, displayToken, 0);
            }
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setDisplayLayerStack(android.os.IBinder displayToken, int layerStack) {
            if (displayToken == null) {
                throw new java.lang.IllegalArgumentException("displayToken must not be null");
            }
            android.view.SurfaceControl.nativeSetDisplayLayerStack(mNativeObject, displayToken, layerStack);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setDisplayProjection(android.os.IBinder displayToken, int orientation, android.graphics.Rect layerStackRect, android.graphics.Rect displayRect) {
            if (displayToken == null) {
                throw new java.lang.IllegalArgumentException("displayToken must not be null");
            }
            if (layerStackRect == null) {
                throw new java.lang.IllegalArgumentException("layerStackRect must not be null");
            }
            if (displayRect == null) {
                throw new java.lang.IllegalArgumentException("displayRect must not be null");
            }
            android.view.SurfaceControl.nativeSetDisplayProjection(mNativeObject, displayToken, orientation, layerStackRect.left, layerStackRect.top, layerStackRect.right, layerStackRect.bottom, displayRect.left, displayRect.top, displayRect.right, displayRect.bottom);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setDisplaySize(android.os.IBinder displayToken, int width, int height) {
            if (displayToken == null) {
                throw new java.lang.IllegalArgumentException("displayToken must not be null");
            }
            if ((width <= 0) || (height <= 0)) {
                throw new java.lang.IllegalArgumentException("width and height must be positive");
            }
            android.view.SurfaceControl.nativeSetDisplaySize(mNativeObject, displayToken, width, height);
            return this;
        }

        /**
         * flag the transaction as an animation
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setAnimationTransaction() {
            android.view.SurfaceControl.nativeSetAnimationTransaction(mNativeObject);
            return this;
        }

        /**
         * Indicate that SurfaceFlinger should wake up earlier than usual as a result of this
         * transaction. This should be used when the caller thinks that the scene is complex enough
         * that it's likely to hit GL composition, and thus, SurfaceFlinger needs to more time in
         * order not to miss frame deadlines.
         * <p>
         * Corresponds to setting ISurfaceComposer::eEarlyWakeup
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setEarlyWakeup() {
            android.view.SurfaceControl.nativeSetEarlyWakeup(mNativeObject);
            return this;
        }

        /**
         * Sets an arbitrary piece of metadata on the surface. This is a helper for int data.
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setMetadata(android.view.SurfaceControl sc, int key, int data) {
            android.os.Parcel parcel = android.os.Parcel.obtain();
            parcel.writeInt(data);
            try {
                setMetadata(sc, key, parcel);
            } finally {
                parcel.recycle();
            }
            return this;
        }

        /**
         * Sets an arbitrary piece of metadata on the surface.
         *
         * @unknown 
         */
        public android.view.SurfaceControl.Transaction setMetadata(android.view.SurfaceControl sc, int key, android.os.Parcel data) {
            android.view.SurfaceControl.nativeSetMetadata(mNativeObject, sc.mNativeObject, key, data);
            return this;
        }

        /**
         * Merge the other transaction into this transaction, clearing the
         * other transaction as if it had been applied.
         *
         * @param other
         * 		The transaction to merge in to this one.
         * @return This transaction.
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Transaction merge(@android.annotation.NonNull
        android.view.SurfaceControl.Transaction other) {
            if (this == other) {
                return this;
            }
            mResizedSurfaces.putAll(other.mResizedSurfaces);
            other.mResizedSurfaces.clear();
            android.view.SurfaceControl.nativeMergeTransaction(mNativeObject, other.mNativeObject);
            return this;
        }

        /**
         * Equivalent to reparent with a null parent, in that it removes
         * the SurfaceControl from the scene, but it also releases
         * the local resources (by calling {@link SurfaceControl#release})
         * after this method returns, {@link SurfaceControl#isValid} will return
         * false for the argument.
         *
         * @param sc
         * 		The surface to remove and release.
         * @return This transaction
         * @unknown 
         */
        @android.annotation.NonNull
        public android.view.SurfaceControl.Transaction remove(@android.annotation.NonNull
        android.view.SurfaceControl sc) {
            reparent(sc, null);
            sc.release();
            return this;
        }
    }
}

