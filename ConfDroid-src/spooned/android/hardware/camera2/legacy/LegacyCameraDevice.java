/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.hardware.camera2.legacy;


/**
 * This class emulates the functionality of a Camera2 device using a the old Camera class.
 *
 * <p>
 * There are two main components that are used to implement this:
 * - A state machine containing valid Camera2 device states ({@link CameraDeviceState}).
 * - A message-queue based pipeline that manages an old Camera class, and executes capture and
 *   configuration requests.
 * </p>
 */
public class LegacyCameraDevice implements java.lang.AutoCloseable {
    private final java.lang.String TAG;

    private static final boolean DEBUG = false;

    private final int mCameraId;

    private final android.hardware.camera2.CameraCharacteristics mStaticCharacteristics;

    private final android.hardware.camera2.ICameraDeviceCallbacks mDeviceCallbacks;

    private final android.hardware.camera2.legacy.CameraDeviceState mDeviceState = new android.hardware.camera2.legacy.CameraDeviceState();

    private android.util.SparseArray<android.view.Surface> mConfiguredSurfaces;

    private boolean mClosed = false;

    private final android.os.ConditionVariable mIdle = /* open */
    new android.os.ConditionVariable(true);

    private final android.os.HandlerThread mResultThread = new android.os.HandlerThread("ResultThread");

    private final android.os.HandlerThread mCallbackHandlerThread = new android.os.HandlerThread("CallbackThread");

    private final android.os.Handler mCallbackHandler;

    private final android.os.Handler mResultHandler;

    private static final int ILLEGAL_VALUE = -1;

    // Keep up to date with values in hardware/libhardware/include/hardware/gralloc.h
    private static final int GRALLOC_USAGE_RENDERSCRIPT = 0x100000;

    private static final int GRALLOC_USAGE_SW_READ_OFTEN = 0x3;

    private static final int GRALLOC_USAGE_HW_TEXTURE = 0x100;

    private static final int GRALLOC_USAGE_HW_COMPOSER = 0x800;

    private static final int GRALLOC_USAGE_HW_RENDER = 0x200;

    private static final int GRALLOC_USAGE_HW_VIDEO_ENCODER = 0x10000;

    public static final int MAX_DIMEN_FOR_ROUNDING = 1920;// maximum allowed width for rounding


    // Keep up to date with values in system/core/include/system/window.h
    public static final int NATIVE_WINDOW_SCALING_MODE_SCALE_TO_WINDOW = 1;

    private android.hardware.camera2.impl.CaptureResultExtras getExtrasFromRequest(android.hardware.camera2.legacy.RequestHolder holder) {
        return /* errorCode */
        /* errorArg */
        getExtrasFromRequest(holder, android.hardware.camera2.legacy.CameraDeviceState.NO_CAPTURE_ERROR, null);
    }

    private android.hardware.camera2.impl.CaptureResultExtras getExtrasFromRequest(android.hardware.camera2.legacy.RequestHolder holder, int errorCode, java.lang.Object errorArg) {
        int errorStreamId = -1;
        if (errorCode == ERROR_CAMERA_BUFFER) {
            android.view.Surface errorTarget = ((android.view.Surface) (errorArg));
            int indexOfTarget = mConfiguredSurfaces.indexOfValue(errorTarget);
            if (indexOfTarget < 0) {
                android.util.Log.e(TAG, "Buffer drop error reported for unknown Surface");
            } else {
                errorStreamId = mConfiguredSurfaces.keyAt(indexOfTarget);
            }
        }
        if (holder == null) {
            return new android.hardware.camera2.impl.CaptureResultExtras(android.hardware.camera2.legacy.LegacyCameraDevice.ILLEGAL_VALUE, android.hardware.camera2.legacy.LegacyCameraDevice.ILLEGAL_VALUE, android.hardware.camera2.legacy.LegacyCameraDevice.ILLEGAL_VALUE, android.hardware.camera2.legacy.LegacyCameraDevice.ILLEGAL_VALUE, android.hardware.camera2.legacy.LegacyCameraDevice.ILLEGAL_VALUE, android.hardware.camera2.legacy.LegacyCameraDevice.ILLEGAL_VALUE, android.hardware.camera2.legacy.LegacyCameraDevice.ILLEGAL_VALUE);
        }
        return /* afTriggerId */
        /* precaptureTriggerId */
        /* partialResultCount */
        new android.hardware.camera2.impl.CaptureResultExtras(holder.getRequestId(), holder.getSubsequeceId(), 0, 0, holder.getFrameNumber(), 1, errorStreamId);
    }

    /**
     * Listener for the camera device state machine.  Calls the appropriate
     * {@link ICameraDeviceCallbacks} for each state transition.
     */
    private final android.hardware.camera2.legacy.CameraDeviceState.CameraDeviceStateListener mStateListener = new android.hardware.camera2.legacy.CameraDeviceState.CameraDeviceStateListener() {
        @java.lang.Override
        public void onError(final int errorCode, final java.lang.Object errorArg, final android.hardware.camera2.legacy.RequestHolder holder) {
            if (android.hardware.camera2.legacy.LegacyCameraDevice.DEBUG) {
                android.util.Log.d(TAG, (("onError called, errorCode = " + errorCode) + ", errorArg = ") + errorArg);
            }
            switch (errorCode) {
                /* Only be considered idle if we hit a fatal error
                and no further requests can be processed.
                 */
                case ERROR_CAMERA_DISCONNECTED :
                case ERROR_CAMERA_SERVICE :
                case ERROR_CAMERA_DEVICE :
                    {
                        mIdle.open();
                        if (android.hardware.camera2.legacy.LegacyCameraDevice.DEBUG) {
                            android.util.Log.d(TAG, "onError - opening idle");
                        }
                    }
            }
            final android.hardware.camera2.impl.CaptureResultExtras extras = getExtrasFromRequest(holder, errorCode, errorArg);
            mResultHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (android.hardware.camera2.legacy.LegacyCameraDevice.DEBUG) {
                        android.util.Log.d(TAG, (("doing onError callback for request " + holder.getRequestId()) + ", with error code ") + errorCode);
                    }
                    try {
                        mDeviceCallbacks.onDeviceError(errorCode, extras);
                    } catch (android.os.RemoteException e) {
                        throw new java.lang.IllegalStateException("Received remote exception during onCameraError callback: ", e);
                    }
                }
            });
        }

        @java.lang.Override
        public void onConfiguring() {
            // Do nothing
            if (android.hardware.camera2.legacy.LegacyCameraDevice.DEBUG) {
                android.util.Log.d(TAG, "doing onConfiguring callback.");
            }
        }

        @java.lang.Override
        public void onIdle() {
            if (android.hardware.camera2.legacy.LegacyCameraDevice.DEBUG) {
                android.util.Log.d(TAG, "onIdle called");
            }
            mIdle.open();
            mResultHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (android.hardware.camera2.legacy.LegacyCameraDevice.DEBUG) {
                        android.util.Log.d(TAG, "doing onIdle callback.");
                    }
                    try {
                        mDeviceCallbacks.onDeviceIdle();
                    } catch (android.os.RemoteException e) {
                        throw new java.lang.IllegalStateException("Received remote exception during onCameraIdle callback: ", e);
                    }
                }
            });
        }

        @java.lang.Override
        public void onBusy() {
            mIdle.close();
            if (android.hardware.camera2.legacy.LegacyCameraDevice.DEBUG) {
                android.util.Log.d(TAG, "onBusy called");
            }
        }

        @java.lang.Override
        public void onCaptureStarted(final android.hardware.camera2.legacy.RequestHolder holder, final long timestamp) {
            final android.hardware.camera2.impl.CaptureResultExtras extras = getExtrasFromRequest(holder);
            mResultHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (android.hardware.camera2.legacy.LegacyCameraDevice.DEBUG) {
                        android.util.Log.d(TAG, "doing onCaptureStarted callback for request " + holder.getRequestId());
                    }
                    try {
                        mDeviceCallbacks.onCaptureStarted(extras, timestamp);
                    } catch (android.os.RemoteException e) {
                        throw new java.lang.IllegalStateException("Received remote exception during onCameraError callback: ", e);
                    }
                }
            });
        }

        @java.lang.Override
        public void onCaptureResult(final android.hardware.camera2.impl.CameraMetadataNative result, final android.hardware.camera2.legacy.RequestHolder holder) {
            final android.hardware.camera2.impl.CaptureResultExtras extras = getExtrasFromRequest(holder);
            mResultHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (android.hardware.camera2.legacy.LegacyCameraDevice.DEBUG) {
                        android.util.Log.d(TAG, "doing onCaptureResult callback for request " + holder.getRequestId());
                    }
                    try {
                        mDeviceCallbacks.onResultReceived(result, extras);
                    } catch (android.os.RemoteException e) {
                        throw new java.lang.IllegalStateException("Received remote exception during onCameraError callback: ", e);
                    }
                }
            });
        }

        @java.lang.Override
        public void onRepeatingRequestError(final long lastFrameNumber) {
            mResultHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (android.hardware.camera2.legacy.LegacyCameraDevice.DEBUG) {
                        android.util.Log.d(TAG, "doing onRepeatingRequestError callback.");
                    }
                    try {
                        mDeviceCallbacks.onRepeatingRequestError(lastFrameNumber);
                    } catch (android.os.RemoteException e) {
                        throw new java.lang.IllegalStateException("Received remote exception during onRepeatingRequestError " + "callback: ", e);
                    }
                }
            });
        }
    };

    private final android.hardware.camera2.legacy.RequestThreadManager mRequestThreadManager;

    /**
     * Check if a given surface uses {@link ImageFormat#YUV_420_888} or format that can be readily
     * converted to this; YV12 and NV21 are the two currently supported formats.
     *
     * @param s
     * 		the surface to check.
     * @return {@code true} if the surfaces uses {@link ImageFormat#YUV_420_888} or a compatible
    format.
     */
    static boolean needsConversion(android.view.Surface s) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        int nativeType = android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceType(s);
        return ((nativeType == android.graphics.ImageFormat.YUV_420_888) || (nativeType == android.graphics.ImageFormat.YV12)) || (nativeType == android.graphics.ImageFormat.NV21);
    }

    /**
     * Create a new emulated camera device from a given Camera 1 API camera.
     *
     * <p>
     * The {@link Camera} provided to this constructor must already have been successfully opened,
     * and ownership of the provided camera is passed to this object.  No further calls to the
     * camera methods should be made following this constructor.
     * </p>
     *
     * @param cameraId
     * 		the id of the camera.
     * @param camera
     * 		an open {@link Camera} device.
     * @param characteristics
     * 		the static camera characteristics for this camera device
     * @param callbacks
     * 		{@link ICameraDeviceCallbacks} callbacks to call for Camera2 API operations.
     */
    public LegacyCameraDevice(int cameraId, android.hardware.Camera camera, android.hardware.camera2.CameraCharacteristics characteristics, android.hardware.camera2.ICameraDeviceCallbacks callbacks) {
        mCameraId = cameraId;
        mDeviceCallbacks = callbacks;
        TAG = java.lang.String.format("CameraDevice-%d-LE", mCameraId);
        mResultThread.start();
        mResultHandler = new android.os.Handler(mResultThread.getLooper());
        mCallbackHandlerThread.start();
        mCallbackHandler = new android.os.Handler(mCallbackHandlerThread.getLooper());
        mDeviceState.setCameraDeviceCallbacks(mCallbackHandler, mStateListener);
        mStaticCharacteristics = characteristics;
        mRequestThreadManager = new android.hardware.camera2.legacy.RequestThreadManager(cameraId, camera, characteristics, mDeviceState);
        mRequestThreadManager.start();
    }

    /**
     * Configure the device with a set of output surfaces.
     *
     * <p>Using empty or {@code null} {@code outputs} is the same as unconfiguring.</p>
     *
     * <p>Every surface in {@code outputs} must be non-{@code null}.</p>
     *
     * @param outputs
     * 		a list of surfaces to set. LegacyCameraDevice will take ownership of this
     * 		list; it must not be modified by the caller once it's passed in.
     * @return an error code for this binder operation, or {@link NO_ERROR}
    on success.
     */
    public int configureOutputs(android.util.SparseArray<android.view.Surface> outputs) {
        java.util.List<android.util.Pair<android.view.Surface, android.util.Size>> sizedSurfaces = new java.util.ArrayList<>();
        if (outputs != null) {
            int count = outputs.size();
            for (int i = 0; i < count; i++) {
                android.view.Surface output = outputs.valueAt(i);
                if (output == null) {
                    android.util.Log.e(TAG, "configureOutputs - null outputs are not allowed");
                    return android.hardware.camera2.legacy.LegacyExceptionUtils.BAD_VALUE;
                }
                if (!output.isValid()) {
                    android.util.Log.e(TAG, "configureOutputs - invalid output surfaces are not allowed");
                    return android.hardware.camera2.legacy.LegacyExceptionUtils.BAD_VALUE;
                }
                android.hardware.camera2.params.StreamConfigurationMap streamConfigurations = mStaticCharacteristics.get(android.hardware.camera2.CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                // Validate surface size and format.
                try {
                    android.util.Size s = android.hardware.camera2.legacy.LegacyCameraDevice.getSurfaceSize(output);
                    int surfaceType = android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceType(output);
                    boolean flexibleConsumer = android.hardware.camera2.legacy.LegacyCameraDevice.isFlexibleConsumer(output);
                    android.util.Size[] sizes = streamConfigurations.getOutputSizes(surfaceType);
                    if (sizes == null) {
                        // WAR: Override default format to IMPLEMENTATION_DEFINED for b/9487482
                        if ((surfaceType >= android.hardware.camera2.legacy.LegacyMetadataMapper.HAL_PIXEL_FORMAT_RGBA_8888) && (surfaceType <= android.hardware.camera2.legacy.LegacyMetadataMapper.HAL_PIXEL_FORMAT_BGRA_8888)) {
                            // YUV_420_888 is always present in LEGACY for all
                            // IMPLEMENTATION_DEFINED output sizes, and is publicly visible in the
                            // API (i.e. {@code #getOutputSizes} works here).
                            sizes = streamConfigurations.getOutputSizes(android.graphics.ImageFormat.YUV_420_888);
                        } else
                            if (surfaceType == android.hardware.camera2.legacy.LegacyMetadataMapper.HAL_PIXEL_FORMAT_BLOB) {
                                sizes = streamConfigurations.getOutputSizes(android.graphics.ImageFormat.JPEG);
                            }

                    }
                    if (!android.hardware.camera2.utils.ArrayUtils.contains(sizes, s)) {
                        if (flexibleConsumer && ((s = android.hardware.camera2.legacy.LegacyCameraDevice.findClosestSize(s, sizes)) != null)) {
                            sizedSurfaces.add(new android.util.Pair<>(output, s));
                        } else {
                            java.lang.String reason = (sizes == null) ? "format is invalid." : "size not in valid set: " + java.util.Arrays.toString(sizes);
                            android.util.Log.e(TAG, java.lang.String.format("Surface with size (w=%d, h=%d) and format " + "0x%x is not valid, %s", s.getWidth(), s.getHeight(), surfaceType, reason));
                            return android.hardware.camera2.legacy.LegacyExceptionUtils.BAD_VALUE;
                        }
                    } else {
                        sizedSurfaces.add(new android.util.Pair<>(output, s));
                    }
                    // Lock down the size before configuration
                    android.hardware.camera2.legacy.LegacyCameraDevice.setSurfaceDimens(output, s.getWidth(), s.getHeight());
                } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
                    android.util.Log.e(TAG, "Surface bufferqueue is abandoned, cannot configure as output: ", e);
                    return android.hardware.camera2.legacy.LegacyExceptionUtils.BAD_VALUE;
                }
            }
        }
        boolean success = false;
        if (mDeviceState.setConfiguring()) {
            mRequestThreadManager.configure(sizedSurfaces);
            success = mDeviceState.setIdle();
        }
        if (success) {
            mConfiguredSurfaces = outputs;
        } else {
            return android.hardware.camera2.legacy.LegacyExceptionUtils.INVALID_OPERATION;
        }
        return android.hardware.camera2.legacy.LegacyExceptionUtils.NO_ERROR;
    }

    /**
     * Submit a burst of capture requests.
     *
     * @param requestList
     * 		a list of capture requests to execute.
     * @param repeating
     * 		{@code true} if this burst is repeating.
     * @return the submission info, including the new request id, and the last frame number, which
    contains either the frame number of the last frame that will be returned for this request,
    or the frame number of the last frame that will be returned for the current repeating
    request if this burst is set to be repeating.
     */
    public android.hardware.camera2.utils.SubmitInfo submitRequestList(android.hardware.camera2.CaptureRequest[] requestList, boolean repeating) {
        if ((requestList == null) || (requestList.length == 0)) {
            android.util.Log.e(TAG, "submitRequestList - Empty/null requests are not allowed");
            throw new android.os.ServiceSpecificException(android.hardware.camera2.legacy.LegacyExceptionUtils.BAD_VALUE, "submitRequestList - Empty/null requests are not allowed");
        }
        java.util.List<java.lang.Long> surfaceIds;
        try {
            surfaceIds = (mConfiguredSurfaces == null) ? new java.util.ArrayList<java.lang.Long>() : android.hardware.camera2.legacy.LegacyCameraDevice.getSurfaceIds(mConfiguredSurfaces);
        } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new android.os.ServiceSpecificException(android.hardware.camera2.legacy.LegacyExceptionUtils.BAD_VALUE, "submitRequestList - configured surface is abandoned.");
        }
        // Make sure that there all requests have at least 1 surface; all surfaces are non-null
        for (android.hardware.camera2.CaptureRequest request : requestList) {
            if (request.getTargets().isEmpty()) {
                android.util.Log.e(TAG, "submitRequestList - " + "Each request must have at least one Surface target");
                throw new android.os.ServiceSpecificException(android.hardware.camera2.legacy.LegacyExceptionUtils.BAD_VALUE, "submitRequestList - " + "Each request must have at least one Surface target");
            }
            for (android.view.Surface surface : request.getTargets()) {
                if (surface == null) {
                    android.util.Log.e(TAG, "submitRequestList - Null Surface targets are not allowed");
                    throw new android.os.ServiceSpecificException(android.hardware.camera2.legacy.LegacyExceptionUtils.BAD_VALUE, "submitRequestList - Null Surface targets are not allowed");
                } else
                    if (mConfiguredSurfaces == null) {
                        android.util.Log.e(TAG, "submitRequestList - must configure " + " device with valid surfaces before submitting requests");
                        throw new android.os.ServiceSpecificException(android.hardware.camera2.legacy.LegacyExceptionUtils.INVALID_OPERATION, "submitRequestList - must configure " + " device with valid surfaces before submitting requests");
                    } else
                        if (!android.hardware.camera2.legacy.LegacyCameraDevice.containsSurfaceId(surface, surfaceIds)) {
                            android.util.Log.e(TAG, "submitRequestList - cannot use a surface that wasn't configured");
                            throw new android.os.ServiceSpecificException(android.hardware.camera2.legacy.LegacyExceptionUtils.BAD_VALUE, "submitRequestList - cannot use a surface that wasn't configured");
                        }


            }
        }
        // TODO: further validation of request here
        mIdle.close();
        return mRequestThreadManager.submitCaptureRequests(requestList, repeating);
    }

    /**
     * Submit a single capture request.
     *
     * @param request
     * 		the capture request to execute.
     * @param repeating
     * 		{@code true} if this request is repeating.
     * @return the submission info, including the new request id, and the last frame number, which
    contains either the frame number of the last frame that will be returned for this request,
    or the frame number of the last frame that will be returned for the current repeating
    request if this burst is set to be repeating.
     */
    public android.hardware.camera2.utils.SubmitInfo submitRequest(android.hardware.camera2.CaptureRequest request, boolean repeating) {
        android.hardware.camera2.CaptureRequest[] requestList = new android.hardware.camera2.CaptureRequest[]{ request };
        return submitRequestList(requestList, repeating);
    }

    /**
     * Cancel the repeating request with the given request id.
     *
     * @param requestId
     * 		the request id of the request to cancel.
     * @return the last frame number to be returned from the HAL for the given repeating request, or
    {@code INVALID_FRAME} if none exists.
     */
    public long cancelRequest(int requestId) {
        return mRequestThreadManager.cancelRepeating(requestId);
    }

    /**
     * Block until the {@link ICameraDeviceCallbacks#onCameraIdle()} callback is received.
     */
    public void waitUntilIdle() {
        mIdle.block();
    }

    /**
     * Flush any pending requests.
     *
     * @return the last frame number.
     */
    public long flush() {
        long lastFrame = mRequestThreadManager.flush();
        waitUntilIdle();
        return lastFrame;
    }

    /**
     * Return {@code true} if the device has been closed.
     */
    public boolean isClosed() {
        return mClosed;
    }

    @java.lang.Override
    public void close() {
        mRequestThreadManager.quit();
        mCallbackHandlerThread.quitSafely();
        mResultThread.quitSafely();
        try {
            mCallbackHandlerThread.join();
        } catch (java.lang.InterruptedException e) {
            android.util.Log.e(TAG, java.lang.String.format("Thread %s (%d) interrupted while quitting.", mCallbackHandlerThread.getName(), mCallbackHandlerThread.getId()));
        }
        try {
            mResultThread.join();
        } catch (java.lang.InterruptedException e) {
            android.util.Log.e(TAG, java.lang.String.format("Thread %s (%d) interrupted while quitting.", mResultThread.getName(), mResultThread.getId()));
        }
        mClosed = true;
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            close();
        } catch (android.os.ServiceSpecificException e) {
            android.util.Log.e(TAG, "Got error while trying to finalize, ignoring: " + e.getMessage());
        } finally {
            super.finalize();
        }
    }

    static long findEuclidDistSquare(android.util.Size a, android.util.Size b) {
        long d0 = a.getWidth() - b.getWidth();
        long d1 = a.getHeight() - b.getHeight();
        return (d0 * d0) + (d1 * d1);
    }

    // Keep up to date with rounding behavior in
    // frameworks/av/services/camera/libcameraservice/api2/CameraDeviceClient.cpp
    static android.util.Size findClosestSize(android.util.Size size, android.util.Size[] supportedSizes) {
        if ((size == null) || (supportedSizes == null)) {
            return null;
        }
        android.util.Size bestSize = null;
        for (android.util.Size s : supportedSizes) {
            if (s.equals(size)) {
                return size;
            } else
                if ((s.getWidth() <= android.hardware.camera2.legacy.LegacyCameraDevice.MAX_DIMEN_FOR_ROUNDING) && ((bestSize == null) || (android.hardware.camera2.legacy.LegacyCameraDevice.findEuclidDistSquare(size, s) < android.hardware.camera2.legacy.LegacyCameraDevice.findEuclidDistSquare(bestSize, s)))) {
                    bestSize = s;
                }

        }
        return bestSize;
    }

    /**
     * Query the surface for its currently configured default buffer size.
     *
     * @param surface
     * 		a non-{@code null} {@code Surface}
     * @return the width and height of the surface
     * @throws NullPointerException
     * 		if the {@code surface} was {@code null}
     * @throws BufferQueueAbandonedException
     * 		if the {@code surface} was invalid
     */
    public static android.util.Size getSurfaceSize(android.view.Surface surface) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surface);
        int[] dimens = new int[2];
        android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(/* out */
        android.hardware.camera2.legacy.LegacyCameraDevice.nativeDetectSurfaceDimens(surface, dimens));
        return new android.util.Size(dimens[0], dimens[1]);
    }

    public static boolean isFlexibleConsumer(android.view.Surface output) {
        int usageFlags = android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceUsageFlags(output);
        // Keep up to date with allowed consumer types in
        // frameworks/av/services/camera/libcameraservice/api2/CameraDeviceClient.cpp
        int disallowedFlags = android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_HW_VIDEO_ENCODER | android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_RENDERSCRIPT;
        int allowedFlags = (android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_HW_TEXTURE | android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_SW_READ_OFTEN) | android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_HW_COMPOSER;
        boolean flexibleConsumer = ((usageFlags & disallowedFlags) == 0) && ((usageFlags & allowedFlags) != 0);
        return flexibleConsumer;
    }

    public static boolean isPreviewConsumer(android.view.Surface output) {
        int usageFlags = android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceUsageFlags(output);
        int disallowedFlags = (android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_HW_VIDEO_ENCODER | android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_RENDERSCRIPT) | android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_SW_READ_OFTEN;
        int allowedFlags = (android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_HW_TEXTURE | android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_HW_COMPOSER) | android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_HW_RENDER;
        boolean previewConsumer = ((usageFlags & disallowedFlags) == 0) && ((usageFlags & allowedFlags) != 0);
        int surfaceFormat = android.graphics.ImageFormat.UNKNOWN;
        try {
            surfaceFormat = android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceType(output);
        } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new java.lang.IllegalArgumentException("Surface was abandoned", e);
        }
        return previewConsumer;
    }

    public static boolean isVideoEncoderConsumer(android.view.Surface output) {
        int usageFlags = android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceUsageFlags(output);
        int disallowedFlags = ((android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_HW_TEXTURE | android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_HW_COMPOSER) | android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_RENDERSCRIPT) | android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_SW_READ_OFTEN;
        int allowedFlags = android.hardware.camera2.legacy.LegacyCameraDevice.GRALLOC_USAGE_HW_VIDEO_ENCODER;
        boolean videoEncoderConsumer = ((usageFlags & disallowedFlags) == 0) && ((usageFlags & allowedFlags) != 0);
        int surfaceFormat = android.graphics.ImageFormat.UNKNOWN;
        try {
            surfaceFormat = android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceType(output);
        } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new java.lang.IllegalArgumentException("Surface was abandoned", e);
        }
        return videoEncoderConsumer;
    }

    /**
     * Query the surface for its currently configured usage flags
     */
    static int detectSurfaceUsageFlags(android.view.Surface surface) {
        checkNotNull(surface);
        return android.hardware.camera2.legacy.LegacyCameraDevice.nativeDetectSurfaceUsageFlags(surface);
    }

    /**
     * Query the surface for its currently configured format
     */
    public static int detectSurfaceType(android.view.Surface surface) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surface);
        return android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(android.hardware.camera2.legacy.LegacyCameraDevice.nativeDetectSurfaceType(surface));
    }

    /**
     * Query the surface for its currently configured dataspace
     */
    public static int detectSurfaceDataspace(android.view.Surface surface) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surface);
        return android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(android.hardware.camera2.legacy.LegacyCameraDevice.nativeDetectSurfaceDataspace(surface));
    }

    static void connectSurface(android.view.Surface surface) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surface);
        android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(android.hardware.camera2.legacy.LegacyCameraDevice.nativeConnectSurface(surface));
    }

    static void disconnectSurface(android.view.Surface surface) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        if (surface == null)
            return;

        android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(android.hardware.camera2.legacy.LegacyCameraDevice.nativeDisconnectSurface(surface));
    }

    static void produceFrame(android.view.Surface surface, byte[] pixelBuffer, int width, int height, int pixelFormat) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surface);
        checkNotNull(pixelBuffer);
        checkArgumentPositive(width, "width must be positive.");
        checkArgumentPositive(height, "height must be positive.");
        android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(android.hardware.camera2.legacy.LegacyCameraDevice.nativeProduceFrame(surface, pixelBuffer, width, height, pixelFormat));
    }

    static void setSurfaceFormat(android.view.Surface surface, int pixelFormat) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surface);
        android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(android.hardware.camera2.legacy.LegacyCameraDevice.nativeSetSurfaceFormat(surface, pixelFormat));
    }

    static void setSurfaceDimens(android.view.Surface surface, int width, int height) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surface);
        checkArgumentPositive(width, "width must be positive.");
        checkArgumentPositive(height, "height must be positive.");
        android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(android.hardware.camera2.legacy.LegacyCameraDevice.nativeSetSurfaceDimens(surface, width, height));
    }

    static long getSurfaceId(android.view.Surface surface) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surface);
        try {
            return android.hardware.camera2.legacy.LegacyCameraDevice.nativeGetSurfaceId(surface);
        } catch (java.lang.IllegalArgumentException e) {
            throw new android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException();
        }
    }

    static java.util.List<java.lang.Long> getSurfaceIds(android.util.SparseArray<android.view.Surface> surfaces) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        if (surfaces == null) {
            throw new java.lang.NullPointerException("Null argument surfaces");
        }
        java.util.List<java.lang.Long> surfaceIds = new java.util.ArrayList<>();
        int count = surfaces.size();
        for (int i = 0; i < count; i++) {
            long id = android.hardware.camera2.legacy.LegacyCameraDevice.getSurfaceId(surfaces.valueAt(i));
            if (id == 0) {
                throw new java.lang.IllegalStateException("Configured surface had null native GraphicBufferProducer pointer!");
            }
            surfaceIds.add(id);
        }
        return surfaceIds;
    }

    static java.util.List<java.lang.Long> getSurfaceIds(java.util.Collection<android.view.Surface> surfaces) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        if (surfaces == null) {
            throw new java.lang.NullPointerException("Null argument surfaces");
        }
        java.util.List<java.lang.Long> surfaceIds = new java.util.ArrayList<>();
        for (android.view.Surface s : surfaces) {
            long id = android.hardware.camera2.legacy.LegacyCameraDevice.getSurfaceId(s);
            if (id == 0) {
                throw new java.lang.IllegalStateException("Configured surface had null native GraphicBufferProducer pointer!");
            }
            surfaceIds.add(id);
        }
        return surfaceIds;
    }

    static boolean containsSurfaceId(android.view.Surface s, java.util.Collection<java.lang.Long> ids) {
        long id = 0;
        try {
            id = android.hardware.camera2.legacy.LegacyCameraDevice.getSurfaceId(s);
        } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
            // If surface is abandoned, return false.
            return false;
        }
        return ids.contains(id);
    }

    static void setSurfaceOrientation(android.view.Surface surface, int facing, int sensorOrientation) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surface);
        android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(android.hardware.camera2.legacy.LegacyCameraDevice.nativeSetSurfaceOrientation(surface, facing, sensorOrientation));
    }

    static android.util.Size getTextureSize(android.graphics.SurfaceTexture surfaceTexture) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surfaceTexture);
        int[] dimens = new int[2];
        android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(/* out */
        android.hardware.camera2.legacy.LegacyCameraDevice.nativeDetectTextureDimens(surfaceTexture, dimens));
        return new android.util.Size(dimens[0], dimens[1]);
    }

    static void setNextTimestamp(android.view.Surface surface, long timestamp) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surface);
        android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(android.hardware.camera2.legacy.LegacyCameraDevice.nativeSetNextTimestamp(surface, timestamp));
    }

    static void setScalingMode(android.view.Surface surface, int mode) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        checkNotNull(surface);
        android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(android.hardware.camera2.legacy.LegacyCameraDevice.nativeSetScalingMode(surface, mode));
    }

    private static native int nativeDetectSurfaceType(android.view.Surface surface);

    private static native int nativeDetectSurfaceDataspace(android.view.Surface surface);

    private static native int nativeDetectSurfaceDimens(android.view.Surface surface, /* out */
    int[] dimens);

    private static native int nativeConnectSurface(android.view.Surface surface);

    private static native int nativeProduceFrame(android.view.Surface surface, byte[] pixelBuffer, int width, int height, int pixelFormat);

    private static native int nativeSetSurfaceFormat(android.view.Surface surface, int pixelFormat);

    private static native int nativeSetSurfaceDimens(android.view.Surface surface, int width, int height);

    private static native long nativeGetSurfaceId(android.view.Surface surface);

    private static native int nativeSetSurfaceOrientation(android.view.Surface surface, int facing, int sensorOrientation);

    private static native int nativeDetectTextureDimens(android.graphics.SurfaceTexture surfaceTexture, /* out */
    int[] dimens);

    private static native int nativeSetNextTimestamp(android.view.Surface surface, long timestamp);

    private static native int nativeDetectSurfaceUsageFlags(android.view.Surface surface);

    private static native int nativeSetScalingMode(android.view.Surface surface, int scalingMode);

    private static native int nativeDisconnectSurface(android.view.Surface surface);

    static native int nativeGetJpegFooterSize();
}

