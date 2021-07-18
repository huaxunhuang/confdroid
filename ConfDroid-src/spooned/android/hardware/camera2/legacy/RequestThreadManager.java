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
 * This class executes requests to the {@link Camera}.
 *
 * <p>
 * The main components of this class are:
 * - A message queue of requests to the {@link Camera}.
 * - A thread that consumes requests to the {@link Camera} and executes them.
 * - A {@link GLThreadManager} that draws to the configured output {@link Surface}s.
 * - An {@link CameraDeviceState} state machine that manages the callbacks for various operations.
 * </p>
 */
@java.lang.SuppressWarnings("deprecation")
public class RequestThreadManager {
    private final java.lang.String TAG;

    private final int mCameraId;

    private final android.hardware.camera2.legacy.RequestHandlerThread mRequestThread;

    private static final boolean DEBUG = false;

    // For slightly more spammy messages that will get repeated every frame
    private static final boolean VERBOSE = false;

    private android.hardware.Camera mCamera;

    private final android.hardware.camera2.CameraCharacteristics mCharacteristics;

    private final android.hardware.camera2.legacy.CameraDeviceState mDeviceState;

    private final android.hardware.camera2.legacy.CaptureCollector mCaptureCollector;

    private final android.hardware.camera2.legacy.LegacyFocusStateMapper mFocusStateMapper;

    private final android.hardware.camera2.legacy.LegacyFaceDetectMapper mFaceDetectMapper;

    private static final int MSG_CONFIGURE_OUTPUTS = 1;

    private static final int MSG_SUBMIT_CAPTURE_REQUEST = 2;

    private static final int MSG_CLEANUP = 3;

    private static final int MAX_IN_FLIGHT_REQUESTS = 2;

    private static final int PREVIEW_FRAME_TIMEOUT = 1000;// ms


    private static final int JPEG_FRAME_TIMEOUT = 4000;// ms (same as CTS for API2)


    private static final int REQUEST_COMPLETE_TIMEOUT = android.hardware.camera2.legacy.RequestThreadManager.JPEG_FRAME_TIMEOUT;

    private static final float ASPECT_RATIO_TOLERANCE = 0.01F;

    private boolean mPreviewRunning = false;

    private final java.util.List<android.view.Surface> mPreviewOutputs = new java.util.ArrayList<>();

    private final java.util.List<android.view.Surface> mCallbackOutputs = new java.util.ArrayList<>();

    private android.hardware.camera2.legacy.GLThreadManager mGLThreadManager;

    private android.graphics.SurfaceTexture mPreviewTexture;

    private android.hardware.Camera.Parameters mParams;

    private final java.util.List<java.lang.Long> mJpegSurfaceIds = new java.util.ArrayList<>();

    private android.util.Size mIntermediateBufferSize;

    private final android.hardware.camera2.legacy.RequestQueue mRequestQueue = new android.hardware.camera2.legacy.RequestQueue(mJpegSurfaceIds);

    private android.hardware.camera2.legacy.LegacyRequest mLastRequest = null;

    private android.graphics.SurfaceTexture mDummyTexture;

    private android.view.Surface mDummySurface;

    private final java.lang.Object mIdleLock = new java.lang.Object();

    private final android.hardware.camera2.legacy.RequestThreadManager.FpsCounter mPrevCounter = new android.hardware.camera2.legacy.RequestThreadManager.FpsCounter("Incoming Preview");

    private final android.hardware.camera2.legacy.RequestThreadManager.FpsCounter mRequestCounter = new android.hardware.camera2.legacy.RequestThreadManager.FpsCounter("Incoming Requests");

    private final java.util.concurrent.atomic.AtomicBoolean mQuit = new java.util.concurrent.atomic.AtomicBoolean(false);

    // Stuff JPEGs into HAL_PIXEL_FORMAT_RGBA_8888 gralloc buffers to get around SW write
    // limitations for (b/17379185).
    private static final boolean USE_BLOB_FORMAT_OVERRIDE = true;

    /**
     * Container object for Configure messages.
     */
    private static class ConfigureHolder {
        public final android.os.ConditionVariable condition;

        public final java.util.Collection<android.util.Pair<android.view.Surface, android.util.Size>> surfaces;

        public ConfigureHolder(android.os.ConditionVariable condition, java.util.Collection<android.util.Pair<android.view.Surface, android.util.Size>> surfaces) {
            this.condition = condition;
            this.surfaces = surfaces;
        }
    }

    /**
     * Counter class used to calculate and log the current FPS of frame production.
     */
    public static class FpsCounter {
        // TODO: Hook this up to SystTrace?
        private static final java.lang.String TAG = "FpsCounter";

        private int mFrameCount = 0;

        private long mLastTime = 0;

        private long mLastPrintTime = 0;

        private double mLastFps = 0;

        private final java.lang.String mStreamType;

        private static final long NANO_PER_SECOND = 1000000000;// ns


        public FpsCounter(java.lang.String streamType) {
            mStreamType = streamType;
        }

        public synchronized void countFrame() {
            mFrameCount++;
            long nextTime = android.os.SystemClock.elapsedRealtimeNanos();
            if (mLastTime == 0) {
                mLastTime = nextTime;
            }
            if (nextTime > (mLastTime + android.hardware.camera2.legacy.RequestThreadManager.FpsCounter.NANO_PER_SECOND)) {
                long elapsed = nextTime - mLastTime;
                mLastFps = mFrameCount * (android.hardware.camera2.legacy.RequestThreadManager.FpsCounter.NANO_PER_SECOND / ((double) (elapsed)));
                mFrameCount = 0;
                mLastTime = nextTime;
            }
        }

        public synchronized double checkFps() {
            return mLastFps;
        }

        public synchronized void staggeredLog() {
            if (mLastTime > (mLastPrintTime + (5 * android.hardware.camera2.legacy.RequestThreadManager.FpsCounter.NANO_PER_SECOND))) {
                mLastPrintTime = mLastTime;
                android.util.Log.d(android.hardware.camera2.legacy.RequestThreadManager.FpsCounter.TAG, (("FPS for " + mStreamType) + " stream: ") + mLastFps);
            }
        }

        public synchronized void countAndLog() {
            countFrame();
            staggeredLog();
        }
    }

    /**
     * Fake preview for jpeg captures when there is no active preview
     */
    private void createDummySurface() {
        if ((mDummyTexture == null) || (mDummySurface == null)) {
            mDummyTexture = /* ignored */
            new android.graphics.SurfaceTexture(0);
            // TODO: use smallest default sizes
            mDummyTexture.setDefaultBufferSize(640, 480);
            mDummySurface = new android.view.Surface(mDummyTexture);
        }
    }

    private final android.hardware.Camera.ErrorCallback mErrorCallback = new android.hardware.Camera.ErrorCallback() {
        @java.lang.Override
        public void onError(int i, android.hardware.Camera camera) {
            switch (i) {
                case android.hardware.Camera.CAMERA_ERROR_EVICTED :
                    {
                        flush();
                        mDeviceState.setError(ERROR_CAMERA_DISCONNECTED);
                    }
                    break;
                default :
                    {
                        android.util.Log.e(TAG, ("Received error " + i) + " from the Camera1 ErrorCallback");
                        mDeviceState.setError(ERROR_CAMERA_DEVICE);
                    }
                    break;
            }
        }
    };

    private final android.os.ConditionVariable mReceivedJpeg = new android.os.ConditionVariable(false);

    private final android.hardware.Camera.PictureCallback mJpegCallback = new android.hardware.Camera.PictureCallback() {
        @java.lang.Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
            android.util.Log.i(TAG, "Received jpeg.");
            android.util.Pair<android.hardware.camera2.legacy.RequestHolder, java.lang.Long> captureInfo = mCaptureCollector.jpegProduced();
            if ((captureInfo == null) || (captureInfo.first == null)) {
                android.util.Log.e(TAG, "Dropping jpeg frame.");
                return;
            }
            android.hardware.camera2.legacy.RequestHolder holder = captureInfo.first;
            long timestamp = captureInfo.second;
            for (android.view.Surface s : holder.getHolderTargets()) {
                try {
                    if (android.hardware.camera2.legacy.LegacyCameraDevice.containsSurfaceId(s, mJpegSurfaceIds)) {
                        android.util.Log.i(TAG, "Producing jpeg buffer...");
                        int totalSize = data.length + android.hardware.camera2.legacy.LegacyCameraDevice.nativeGetJpegFooterSize();
                        totalSize = (totalSize + 3) & (~0x3);// round up to nearest octonibble

                        android.hardware.camera2.legacy.LegacyCameraDevice.setNextTimestamp(s, timestamp);
                        if (android.hardware.camera2.legacy.RequestThreadManager.USE_BLOB_FORMAT_OVERRIDE) {
                            // Override to RGBA_8888 format.
                            android.hardware.camera2.legacy.LegacyCameraDevice.setSurfaceFormat(s, android.hardware.camera2.legacy.LegacyMetadataMapper.HAL_PIXEL_FORMAT_RGBA_8888);
                            int dimen = ((int) (java.lang.Math.ceil(java.lang.Math.sqrt(totalSize))));
                            dimen = (dimen + 0xf) & (~0xf);// round up to nearest multiple of 16

                            android.hardware.camera2.legacy.LegacyCameraDevice.setSurfaceDimens(s, dimen, dimen);
                            android.hardware.camera2.legacy.LegacyCameraDevice.produceFrame(s, data, dimen, dimen, android.hardware.camera2.impl.CameraMetadataNative.NATIVE_JPEG_FORMAT);
                        } else {
                            /* height */
                            android.hardware.camera2.legacy.LegacyCameraDevice.setSurfaceDimens(s, totalSize, 1);
                            /* height */
                            android.hardware.camera2.legacy.LegacyCameraDevice.produceFrame(s, data, totalSize, 1, android.hardware.camera2.impl.CameraMetadataNative.NATIVE_JPEG_FORMAT);
                        }
                    }
                } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
                    android.util.Log.w(TAG, "Surface abandoned, dropping frame. ", e);
                }
            }
            mReceivedJpeg.open();
        }
    };

    private final android.hardware.Camera.ShutterCallback mJpegShutterCallback = new android.hardware.Camera.ShutterCallback() {
        @java.lang.Override
        public void onShutter() {
            mCaptureCollector.jpegCaptured(android.os.SystemClock.elapsedRealtimeNanos());
        }
    };

    private final android.graphics.SurfaceTexture.OnFrameAvailableListener mPreviewCallback = new android.graphics.SurfaceTexture.OnFrameAvailableListener() {
        @java.lang.Override
        public void onFrameAvailable(android.graphics.SurfaceTexture surfaceTexture) {
            if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG) {
                mPrevCounter.countAndLog();
            }
            mGLThreadManager.queueNewFrame();
        }
    };

    private void stopPreview() {
        if (android.hardware.camera2.legacy.RequestThreadManager.VERBOSE) {
            android.util.Log.v(TAG, "stopPreview - preview running? " + mPreviewRunning);
        }
        if (mPreviewRunning) {
            mCamera.stopPreview();
            mPreviewRunning = false;
        }
    }

    private void startPreview() {
        if (android.hardware.camera2.legacy.RequestThreadManager.VERBOSE) {
            android.util.Log.v(TAG, "startPreview - preview running? " + mPreviewRunning);
        }
        if (!mPreviewRunning) {
            // XX: CameraClient:;startPreview is not getting called after a stop
            mCamera.startPreview();
            mPreviewRunning = true;
        }
    }

    private void doJpegCapturePrepare(android.hardware.camera2.legacy.RequestHolder request) throws java.io.IOException {
        if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG)
            android.util.Log.d(TAG, "doJpegCapturePrepare - preview running? " + mPreviewRunning);

        if (!mPreviewRunning) {
            if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG)
                android.util.Log.d(TAG, "doJpegCapture - create fake surface");

            createDummySurface();
            mCamera.setPreviewTexture(mDummyTexture);
            startPreview();
        }
    }

    private void doJpegCapture(android.hardware.camera2.legacy.RequestHolder request) {
        if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG)
            android.util.Log.d(TAG, "doJpegCapturePrepare");

        /* raw */
        mCamera.takePicture(mJpegShutterCallback, null, mJpegCallback);
        mPreviewRunning = false;
    }

    private void doPreviewCapture(android.hardware.camera2.legacy.RequestHolder request) throws java.io.IOException {
        if (android.hardware.camera2.legacy.RequestThreadManager.VERBOSE) {
            android.util.Log.v(TAG, "doPreviewCapture - preview running? " + mPreviewRunning);
        }
        if (mPreviewRunning) {
            return;// Already running

        }
        if (mPreviewTexture == null) {
            throw new java.lang.IllegalStateException("Preview capture called with no preview surfaces configured.");
        }
        mPreviewTexture.setDefaultBufferSize(mIntermediateBufferSize.getWidth(), mIntermediateBufferSize.getHeight());
        mCamera.setPreviewTexture(mPreviewTexture);
        startPreview();
    }

    private void configureOutputs(java.util.Collection<android.util.Pair<android.view.Surface, android.util.Size>> outputs) {
        if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG) {
            java.lang.String outputsStr = (outputs == null) ? "null" : outputs.size() + " surfaces";
            android.util.Log.d(TAG, "configureOutputs with " + outputsStr);
        }
        try {
            stopPreview();
        } catch (java.lang.RuntimeException e) {
            android.util.Log.e(TAG, "Received device exception in configure call: ", e);
            mDeviceState.setError(ERROR_CAMERA_DEVICE);
            return;
        }
        /* Try to release the previous preview's surface texture earlier if we end up
        using a different one; this also reduces the likelihood of getting into a deadlock
        when disconnecting from the old previous texture at a later time.
         */
        try {
            /* surfaceTexture */
            mCamera.setPreviewTexture(null);
        } catch (java.io.IOException e) {
            android.util.Log.w(TAG, "Failed to clear prior SurfaceTexture, may cause GL deadlock: ", e);
        } catch (java.lang.RuntimeException e) {
            android.util.Log.e(TAG, "Received device exception in configure call: ", e);
            mDeviceState.setError(ERROR_CAMERA_DEVICE);
            return;
        }
        if (mGLThreadManager != null) {
            mGLThreadManager.waitUntilStarted();
            mGLThreadManager.ignoreNewFrames();
            mGLThreadManager.waitUntilIdle();
        }
        resetJpegSurfaceFormats(mCallbackOutputs);
        for (android.view.Surface s : mCallbackOutputs) {
            try {
                android.hardware.camera2.legacy.LegacyCameraDevice.disconnectSurface(s);
            } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
                android.util.Log.w(TAG, "Surface abandoned, skipping...", e);
            }
        }
        mPreviewOutputs.clear();
        mCallbackOutputs.clear();
        mJpegSurfaceIds.clear();
        mPreviewTexture = null;
        java.util.List<android.util.Size> previewOutputSizes = new java.util.ArrayList<>();
        java.util.List<android.util.Size> callbackOutputSizes = new java.util.ArrayList<>();
        int facing = mCharacteristics.get(android.hardware.camera2.CameraCharacteristics.LENS_FACING);
        int orientation = mCharacteristics.get(android.hardware.camera2.CameraCharacteristics.SENSOR_ORIENTATION);
        if (outputs != null) {
            for (android.util.Pair<android.view.Surface, android.util.Size> outPair : outputs) {
                android.view.Surface s = outPair.first;
                android.util.Size outSize = outPair.second;
                try {
                    int format = android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceType(s);
                    android.hardware.camera2.legacy.LegacyCameraDevice.setSurfaceOrientation(s, facing, orientation);
                    switch (format) {
                        case android.hardware.camera2.impl.CameraMetadataNative.NATIVE_JPEG_FORMAT :
                            if (android.hardware.camera2.legacy.RequestThreadManager.USE_BLOB_FORMAT_OVERRIDE) {
                                // Override to RGBA_8888 format.
                                android.hardware.camera2.legacy.LegacyCameraDevice.setSurfaceFormat(s, android.hardware.camera2.legacy.LegacyMetadataMapper.HAL_PIXEL_FORMAT_RGBA_8888);
                            }
                            mJpegSurfaceIds.add(android.hardware.camera2.legacy.LegacyCameraDevice.getSurfaceId(s));
                            mCallbackOutputs.add(s);
                            callbackOutputSizes.add(outSize);
                            // LegacyCameraDevice is the producer of JPEG output surfaces
                            // so LegacyCameraDevice needs to connect to the surfaces.
                            android.hardware.camera2.legacy.LegacyCameraDevice.connectSurface(s);
                            break;
                        default :
                            android.hardware.camera2.legacy.LegacyCameraDevice.setScalingMode(s, android.hardware.camera2.legacy.LegacyCameraDevice.NATIVE_WINDOW_SCALING_MODE_SCALE_TO_WINDOW);
                            mPreviewOutputs.add(s);
                            previewOutputSizes.add(outSize);
                            break;
                    }
                } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
                    android.util.Log.w(TAG, "Surface abandoned, skipping...", e);
                }
            }
        }
        try {
            mParams = mCamera.getParameters();
        } catch (java.lang.RuntimeException e) {
            android.util.Log.e(TAG, "Received device exception: ", e);
            mDeviceState.setError(ERROR_CAMERA_DEVICE);
            return;
        }
        java.util.List<int[]> supportedFpsRanges = mParams.getSupportedPreviewFpsRange();
        int[] bestRange = getPhotoPreviewFpsRange(supportedFpsRanges);
        if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG) {
            android.util.Log.d(TAG, ((("doPreviewCapture - Selected range [" + bestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX]) + ",") + bestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX]) + "]");
        }
        mParams.setPreviewFpsRange(bestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX], bestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX]);
        android.util.Size smallestSupportedJpegSize = calculatePictureSize(mCallbackOutputs, callbackOutputSizes, mParams);
        if (previewOutputSizes.size() > 0) {
            android.util.Size largestOutput = android.hardware.camera2.utils.SizeAreaComparator.findLargestByArea(previewOutputSizes);
            // Find largest jpeg dimension - assume to have the same aspect ratio as sensor.
            android.util.Size largestJpegDimen = android.hardware.camera2.legacy.ParameterUtils.getLargestSupportedJpegSizeByArea(mParams);
            android.util.Size chosenJpegDimen = (smallestSupportedJpegSize != null) ? smallestSupportedJpegSize : largestJpegDimen;
            java.util.List<android.util.Size> supportedPreviewSizes = android.hardware.camera2.legacy.ParameterUtils.convertSizeList(mParams.getSupportedPreviewSizes());
            // Use smallest preview dimension with same aspect ratio as sensor that is >= than all
            // of the configured output dimensions.  If none exists, fall back to using the largest
            // supported preview size.
            long largestOutputArea = largestOutput.getHeight() * ((long) (largestOutput.getWidth()));
            android.util.Size bestPreviewDimen = android.hardware.camera2.utils.SizeAreaComparator.findLargestByArea(supportedPreviewSizes);
            for (android.util.Size s : supportedPreviewSizes) {
                long currArea = s.getWidth() * s.getHeight();
                long bestArea = bestPreviewDimen.getWidth() * bestPreviewDimen.getHeight();
                if (android.hardware.camera2.legacy.RequestThreadManager.checkAspectRatiosMatch(chosenJpegDimen, s) && ((currArea < bestArea) && (currArea >= largestOutputArea))) {
                    bestPreviewDimen = s;
                }
            }
            mIntermediateBufferSize = bestPreviewDimen;
            mParams.setPreviewSize(mIntermediateBufferSize.getWidth(), mIntermediateBufferSize.getHeight());
            if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG) {
                android.util.Log.d(TAG, "Intermediate buffer selected with dimens: " + bestPreviewDimen.toString());
            }
        } else {
            mIntermediateBufferSize = null;
            if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG) {
                android.util.Log.d(TAG, "No Intermediate buffer selected, no preview outputs were configured");
            }
        }
        if (smallestSupportedJpegSize != null) {
            /* Set takePicture size to the smallest supported JPEG size large enough
            to scale/crop out of for the bounding rectangle of the configured JPEG sizes.
             */
            android.util.Log.i(TAG, "configureOutputs - set take picture size to " + smallestSupportedJpegSize);
            mParams.setPictureSize(smallestSupportedJpegSize.getWidth(), smallestSupportedJpegSize.getHeight());
        }
        // TODO: Detect and optimize single-output paths here to skip stream teeing.
        if (mGLThreadManager == null) {
            mGLThreadManager = new android.hardware.camera2.legacy.GLThreadManager(mCameraId, facing, mDeviceState);
            mGLThreadManager.start();
        }
        mGLThreadManager.waitUntilStarted();
        java.util.List<android.util.Pair<android.view.Surface, android.util.Size>> previews = new java.util.ArrayList<>();
        java.util.Iterator<android.util.Size> previewSizeIter = previewOutputSizes.iterator();
        for (android.view.Surface p : mPreviewOutputs) {
            previews.add(new android.util.Pair<>(p, previewSizeIter.next()));
        }
        mGLThreadManager.setConfigurationAndWait(previews, mCaptureCollector);
        mGLThreadManager.allowNewFrames();
        mPreviewTexture = mGLThreadManager.getCurrentSurfaceTexture();
        if (mPreviewTexture != null) {
            mPreviewTexture.setOnFrameAvailableListener(mPreviewCallback);
        }
        try {
            mCamera.setParameters(mParams);
        } catch (java.lang.RuntimeException e) {
            android.util.Log.e(TAG, "Received device exception while configuring: ", e);
            mDeviceState.setError(ERROR_CAMERA_DEVICE);
        }
    }

    private void resetJpegSurfaceFormats(java.util.Collection<android.view.Surface> surfaces) {
        if ((!android.hardware.camera2.legacy.RequestThreadManager.USE_BLOB_FORMAT_OVERRIDE) || (surfaces == null)) {
            return;
        }
        for (android.view.Surface s : surfaces) {
            if ((s == null) || (!s.isValid())) {
                android.util.Log.w(TAG, "Jpeg surface is invalid, skipping...");
                continue;
            }
            try {
                android.hardware.camera2.legacy.LegacyCameraDevice.setSurfaceFormat(s, android.hardware.camera2.legacy.LegacyMetadataMapper.HAL_PIXEL_FORMAT_BLOB);
            } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
                android.util.Log.w(TAG, "Surface abandoned, skipping...", e);
            }
        }
    }

    /**
     * Find a JPEG size (that is supported by the legacy camera device) which is equal to or larger
     * than all of the configured {@code JPEG} outputs (by both width and height).
     *
     * <p>If multiple supported JPEG sizes are larger, select the smallest of them which
     * still satisfies the above constraint.</p>
     *
     * <p>As a result, the returned size is guaranteed to be usable without needing
     * to upscale any of the outputs. If only one {@code JPEG} surface is used,
     * then no scaling/cropping is necessary between the taken picture and
     * the {@code JPEG} output surface.</p>
     *
     * @param callbackOutputs
     * 		a non-{@code null} list of {@code Surface}s with any image formats
     * @param params
     * 		api1 parameters (used for reading only)
     * @return a size large enough to fit all of the configured {@code JPEG} outputs, or
    {@code null} if the {@code callbackOutputs} did not have any {@code JPEG}
    surfaces.
     */
    private android.util.Size calculatePictureSize(java.util.List<android.view.Surface> callbackOutputs, java.util.List<android.util.Size> callbackSizes, android.hardware.Camera.Parameters params) {
        /* Find the largest JPEG size (if any), from the configured outputs:
        - the api1 picture size should be set to the smallest legal size that's at least as large
          as the largest configured JPEG size
         */
        if (callbackOutputs.size() != callbackSizes.size()) {
            throw new java.lang.IllegalStateException("Input collections must be same length");
        }
        java.util.List<android.util.Size> configuredJpegSizes = new java.util.ArrayList<>();
        java.util.Iterator<android.util.Size> sizeIterator = callbackSizes.iterator();
        for (android.view.Surface callbackSurface : callbackOutputs) {
            android.util.Size jpegSize = sizeIterator.next();
            if (!android.hardware.camera2.legacy.LegacyCameraDevice.containsSurfaceId(callbackSurface, mJpegSurfaceIds)) {
                continue;// Ignore non-JPEG callback formats

            }
            configuredJpegSizes.add(jpegSize);
        }
        if (!configuredJpegSizes.isEmpty()) {
            /* Find the largest configured JPEG width, and height, independently
            of the rest.

            The rest of the JPEG streams can be cropped out of this smallest bounding
            rectangle.
             */
            int maxConfiguredJpegWidth = -1;
            int maxConfiguredJpegHeight = -1;
            for (android.util.Size jpegSize : configuredJpegSizes) {
                maxConfiguredJpegWidth = (jpegSize.getWidth() > maxConfiguredJpegWidth) ? jpegSize.getWidth() : maxConfiguredJpegWidth;
                maxConfiguredJpegHeight = (jpegSize.getHeight() > maxConfiguredJpegHeight) ? jpegSize.getHeight() : maxConfiguredJpegHeight;
            }
            android.util.Size smallestBoundJpegSize = new android.util.Size(maxConfiguredJpegWidth, maxConfiguredJpegHeight);
            java.util.List<android.util.Size> supportedJpegSizes = android.hardware.camera2.legacy.ParameterUtils.convertSizeList(params.getSupportedPictureSizes());
            /* Find the smallest supported JPEG size that can fit the smallest bounding
            rectangle for the configured JPEG sizes.
             */
            java.util.List<android.util.Size> candidateSupportedJpegSizes = new java.util.ArrayList<>();
            for (android.util.Size supportedJpegSize : supportedJpegSizes) {
                if ((supportedJpegSize.getWidth() >= maxConfiguredJpegWidth) && (supportedJpegSize.getHeight() >= maxConfiguredJpegHeight)) {
                    candidateSupportedJpegSizes.add(supportedJpegSize);
                }
            }
            if (candidateSupportedJpegSizes.isEmpty()) {
                throw new java.lang.AssertionError("Could not find any supported JPEG sizes large enough to fit " + smallestBoundJpegSize);
            }
            android.util.Size smallestSupportedJpegSize = java.util.Collections.min(candidateSupportedJpegSizes, new android.hardware.camera2.utils.SizeAreaComparator());
            if (!smallestSupportedJpegSize.equals(smallestBoundJpegSize)) {
                android.util.Log.w(TAG, java.lang.String.format("configureOutputs - Will need to crop picture %s into " + "smallest bound size %s", smallestSupportedJpegSize, smallestBoundJpegSize));
            }
            return smallestSupportedJpegSize;
        }
        return null;
    }

    private static boolean checkAspectRatiosMatch(android.util.Size a, android.util.Size b) {
        float aAspect = a.getWidth() / ((float) (a.getHeight()));
        float bAspect = b.getWidth() / ((float) (b.getHeight()));
        return java.lang.Math.abs(aAspect - bAspect) < android.hardware.camera2.legacy.RequestThreadManager.ASPECT_RATIO_TOLERANCE;
    }

    // Calculate the highest FPS range supported
    private int[] getPhotoPreviewFpsRange(java.util.List<int[]> frameRates) {
        if (frameRates.size() == 0) {
            android.util.Log.e(TAG, "No supported frame rates returned!");
            return null;
        }
        int bestMin = 0;
        int bestMax = 0;
        int bestIndex = 0;
        int index = 0;
        for (int[] rate : frameRates) {
            int minFps = rate[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX];
            int maxFps = rate[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX];
            if ((maxFps > bestMax) || ((maxFps == bestMax) && (minFps > bestMin))) {
                bestMin = minFps;
                bestMax = maxFps;
                bestIndex = index;
            }
            index++;
        }
        return frameRates.get(bestIndex);
    }

    private final android.os.Handler.Callback mRequestHandlerCb = new android.os.Handler.Callback() {
        private boolean mCleanup = false;

        private final android.hardware.camera2.legacy.LegacyResultMapper mMapper = new android.hardware.camera2.legacy.LegacyResultMapper();

        @java.lang.Override
        public boolean handleMessage(android.os.Message msg) {
            if (mCleanup) {
                return true;
            }
            if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG) {
                android.util.Log.d(TAG, "Request thread handling message:" + msg.what);
            }
            long startTime = 0;
            if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG) {
                startTime = android.os.SystemClock.elapsedRealtimeNanos();
            }
            switch (msg.what) {
                case android.hardware.camera2.legacy.RequestThreadManager.MSG_CONFIGURE_OUTPUTS :
                    android.hardware.camera2.legacy.RequestThreadManager.ConfigureHolder config = ((android.hardware.camera2.legacy.RequestThreadManager.ConfigureHolder) (msg.obj));
                    int sizes = (config.surfaces != null) ? config.surfaces.size() : 0;
                    android.util.Log.i(TAG, ("Configure outputs: " + sizes) + " surfaces configured.");
                    try {
                        boolean success = mCaptureCollector.waitForEmpty(android.hardware.camera2.legacy.RequestThreadManager.JPEG_FRAME_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
                        if (!success) {
                            android.util.Log.e(TAG, "Timed out while queueing configure request.");
                            mCaptureCollector.failAll();
                        }
                    } catch (java.lang.InterruptedException e) {
                        android.util.Log.e(TAG, "Interrupted while waiting for requests to complete.");
                        mDeviceState.setError(ERROR_CAMERA_DEVICE);
                        break;
                    }
                    configureOutputs(config.surfaces);
                    config.condition.open();
                    if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG) {
                        long totalTime = android.os.SystemClock.elapsedRealtimeNanos() - startTime;
                        android.util.Log.d(TAG, ("Configure took " + totalTime) + " ns");
                    }
                    break;
                case android.hardware.camera2.legacy.RequestThreadManager.MSG_SUBMIT_CAPTURE_REQUEST :
                    android.os.Handler handler = android.hardware.camera2.legacy.RequestThreadManager.this.mRequestThread.getHandler();
                    boolean anyRequestOutputAbandoned = false;
                    // Get the next burst from the request queue.
                    android.util.Pair<android.hardware.camera2.legacy.BurstHolder, java.lang.Long> nextBurst = mRequestQueue.getNext();
                    if (nextBurst == null) {
                        // If there are no further requests queued, wait for any currently executing
                        // requests to complete, then switch to idle state.
                        try {
                            boolean success = mCaptureCollector.waitForEmpty(android.hardware.camera2.legacy.RequestThreadManager.JPEG_FRAME_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
                            if (!success) {
                                android.util.Log.e(TAG, "Timed out while waiting for prior requests to complete.");
                                mCaptureCollector.failAll();
                            }
                        } catch (java.lang.InterruptedException e) {
                            android.util.Log.e(TAG, "Interrupted while waiting for requests to complete: ", e);
                            mDeviceState.setError(ERROR_CAMERA_DEVICE);
                            break;
                        }
                        synchronized(mIdleLock) {
                            // Retry the the request queue.
                            nextBurst = mRequestQueue.getNext();
                            // If we still have no queued requests, go idle.
                            if (nextBurst == null) {
                                mDeviceState.setIdle();
                                break;
                            }
                        }
                    }
                    if (nextBurst != null) {
                        // Queue another capture if we did not get the last burst.
                        handler.sendEmptyMessage(android.hardware.camera2.legacy.RequestThreadManager.MSG_SUBMIT_CAPTURE_REQUEST);
                    }
                    // Complete each request in the burst
                    java.util.List<android.hardware.camera2.legacy.RequestHolder> requests = nextBurst.first.produceRequestHolders(nextBurst.second);
                    for (android.hardware.camera2.legacy.RequestHolder holder : requests) {
                        android.hardware.camera2.CaptureRequest request = holder.getRequest();
                        boolean paramsChanged = false;
                        // Only update parameters if the request has changed
                        if ((mLastRequest == null) || (mLastRequest.captureRequest != request)) {
                            // The intermediate buffer is sometimes null, but we always need
                            // the Camera1 API configured preview size
                            android.util.Size previewSize = android.hardware.camera2.legacy.ParameterUtils.convertSize(mParams.getPreviewSize());
                            android.hardware.camera2.legacy.LegacyRequest legacyRequest = new android.hardware.camera2.legacy.LegacyRequest(mCharacteristics, request, previewSize, mParams);// params are copied

                            // Parameters are mutated as a side-effect
                            /* inout */
                            android.hardware.camera2.legacy.LegacyMetadataMapper.convertRequestMetadata(legacyRequest);
                            // If the parameters have changed, set them in the Camera1 API.
                            if (!mParams.same(legacyRequest.parameters)) {
                                try {
                                    mCamera.setParameters(legacyRequest.parameters);
                                } catch (java.lang.RuntimeException e) {
                                    // If setting the parameters failed, report a request error to
                                    // the camera client, and skip any further work for this request
                                    android.util.Log.e(TAG, "Exception while setting camera parameters: ", e);
                                    holder.failRequest();
                                    /* timestamp */
                                    mDeviceState.setCaptureStart(holder, 0, ERROR_CAMERA_REQUEST);
                                    continue;
                                }
                                paramsChanged = true;
                                mParams = legacyRequest.parameters;
                            }
                            mLastRequest = legacyRequest;
                        }
                        try {
                            boolean success = mCaptureCollector.queueRequest(holder, mLastRequest, android.hardware.camera2.legacy.RequestThreadManager.JPEG_FRAME_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
                            if (!success) {
                                // Report a request error if we timed out while queuing this.
                                android.util.Log.e(TAG, "Timed out while queueing capture request.");
                                holder.failRequest();
                                /* timestamp */
                                mDeviceState.setCaptureStart(holder, 0, ERROR_CAMERA_REQUEST);
                                continue;
                            }
                            // Starting the preview needs to happen before enabling
                            // face detection or auto focus
                            if (holder.hasPreviewTargets()) {
                                doPreviewCapture(holder);
                            }
                            if (holder.hasJpegTargets()) {
                                while (!mCaptureCollector.waitForPreviewsEmpty(android.hardware.camera2.legacy.RequestThreadManager.PREVIEW_FRAME_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                                    // Fail preview requests until the queue is empty.
                                    android.util.Log.e(TAG, "Timed out while waiting for preview requests to " + "complete.");
                                    mCaptureCollector.failNextPreview();
                                } 
                                mReceivedJpeg.close();
                                doJpegCapturePrepare(holder);
                            }
                            /* Do all the actions that require a preview to have been started */
                            // Toggle face detection on/off
                            // - do this before AF to give AF a chance to use faces
                            /* in */
                            mFaceDetectMapper.processFaceDetectMode(request, mParams);
                            // Unconditionally process AF triggers, since they're non-idempotent
                            // - must be done after setting the most-up-to-date AF mode
                            mFocusStateMapper.processRequestTriggers(request, mParams);
                            if (holder.hasJpegTargets()) {
                                doJpegCapture(holder);
                                if (!mReceivedJpeg.block(android.hardware.camera2.legacy.RequestThreadManager.JPEG_FRAME_TIMEOUT)) {
                                    android.util.Log.e(TAG, "Hit timeout for jpeg callback!");
                                    mCaptureCollector.failNextJpeg();
                                }
                            }
                        } catch (java.io.IOException e) {
                            android.util.Log.e(TAG, "Received device exception during capture call: ", e);
                            mDeviceState.setError(ERROR_CAMERA_DEVICE);
                            break;
                        } catch (java.lang.InterruptedException e) {
                            android.util.Log.e(TAG, "Interrupted during capture: ", e);
                            mDeviceState.setError(ERROR_CAMERA_DEVICE);
                            break;
                        } catch (java.lang.RuntimeException e) {
                            android.util.Log.e(TAG, "Received device exception during capture call: ", e);
                            mDeviceState.setError(ERROR_CAMERA_DEVICE);
                            break;
                        }
                        if (paramsChanged) {
                            if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG) {
                                android.util.Log.d(TAG, "Params changed -- getting new Parameters from HAL.");
                            }
                            try {
                                mParams = mCamera.getParameters();
                            } catch (java.lang.RuntimeException e) {
                                android.util.Log.e(TAG, "Received device exception: ", e);
                                mDeviceState.setError(ERROR_CAMERA_DEVICE);
                                break;
                            }
                            // Update parameters to the latest that we think the camera is using
                            mLastRequest.setParameters(mParams);
                        }
                        android.util.MutableLong timestampMutable = /* value */
                        new android.util.MutableLong(0L);
                        try {
                            boolean success = /* out */
                            mCaptureCollector.waitForRequestCompleted(holder, android.hardware.camera2.legacy.RequestThreadManager.REQUEST_COMPLETE_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS, timestampMutable);
                            if (!success) {
                                android.util.Log.e(TAG, "Timed out while waiting for request to complete.");
                                mCaptureCollector.failAll();
                            }
                        } catch (java.lang.InterruptedException e) {
                            android.util.Log.e(TAG, "Interrupted waiting for request completion: ", e);
                            mDeviceState.setError(ERROR_CAMERA_DEVICE);
                            break;
                        }
                        android.hardware.camera2.impl.CameraMetadataNative result = mMapper.cachedConvertResultMetadata(mLastRequest, timestampMutable.value);
                        /* Order matters: The default result mapper is state-less; the
                        other mappers carry state and may override keys set by the default
                        mapper with their own values.
                         */
                        // Update AF state
                        mFocusStateMapper.mapResultTriggers(result);
                        // Update face-related results
                        mFaceDetectMapper.mapResultFaces(result, mLastRequest);
                        if (!holder.requestFailed()) {
                            mDeviceState.setCaptureResult(holder, result);
                        }
                        if (holder.isOutputAbandoned()) {
                            anyRequestOutputAbandoned = true;
                        }
                    }
                    // Stop the repeating request if any of its output surfaces is abandoned.
                    if (anyRequestOutputAbandoned && nextBurst.first.isRepeating()) {
                        long lastFrameNumber = cancelRepeating(nextBurst.first.getRequestId());
                        if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG) {
                            android.util.Log.d(TAG, "Stopped repeating request. Last frame number is " + lastFrameNumber);
                        }
                        mDeviceState.setRepeatingRequestError(lastFrameNumber);
                    }
                    if (android.hardware.camera2.legacy.RequestThreadManager.DEBUG) {
                        long totalTime = android.os.SystemClock.elapsedRealtimeNanos() - startTime;
                        android.util.Log.d(TAG, ("Capture request took " + totalTime) + " ns");
                        mRequestCounter.countAndLog();
                    }
                    break;
                case android.hardware.camera2.legacy.RequestThreadManager.MSG_CLEANUP :
                    mCleanup = true;
                    try {
                        boolean success = mCaptureCollector.waitForEmpty(android.hardware.camera2.legacy.RequestThreadManager.JPEG_FRAME_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
                        if (!success) {
                            android.util.Log.e(TAG, "Timed out while queueing cleanup request.");
                            mCaptureCollector.failAll();
                        }
                    } catch (java.lang.InterruptedException e) {
                        android.util.Log.e(TAG, "Interrupted while waiting for requests to complete: ", e);
                        mDeviceState.setError(ERROR_CAMERA_DEVICE);
                    }
                    if (mGLThreadManager != null) {
                        mGLThreadManager.quit();
                        mGLThreadManager = null;
                    }
                    if (mCamera != null) {
                        mCamera.release();
                        mCamera = null;
                    }
                    resetJpegSurfaceFormats(mCallbackOutputs);
                    break;
                case android.hardware.camera2.legacy.RequestHandlerThread.MSG_POKE_IDLE_HANDLER :
                    // OK: Ignore message.
                    break;
                default :
                    throw new java.lang.AssertionError(("Unhandled message " + msg.what) + " on RequestThread.");
            }
            return true;
        }
    };

    /**
     * Create a new RequestThreadManager.
     *
     * @param cameraId
     * 		the id of the camera to use.
     * @param camera
     * 		an open camera object.  The RequestThreadManager takes ownership of this camera
     * 		object, and is responsible for closing it.
     * @param characteristics
     * 		the static camera characteristics corresponding to this camera device
     * @param deviceState
     * 		a {@link CameraDeviceState} state machine.
     */
    public RequestThreadManager(int cameraId, android.hardware.Camera camera, android.hardware.camera2.CameraCharacteristics characteristics, android.hardware.camera2.legacy.CameraDeviceState deviceState) {
        mCamera = checkNotNull(camera, "camera must not be null");
        mCameraId = cameraId;
        mCharacteristics = checkNotNull(characteristics, "characteristics must not be null");
        java.lang.String name = java.lang.String.format("RequestThread-%d", cameraId);
        TAG = name;
        mDeviceState = checkNotNull(deviceState, "deviceState must not be null");
        mFocusStateMapper = new android.hardware.camera2.legacy.LegacyFocusStateMapper(mCamera);
        mFaceDetectMapper = new android.hardware.camera2.legacy.LegacyFaceDetectMapper(mCamera, mCharacteristics);
        mCaptureCollector = new android.hardware.camera2.legacy.CaptureCollector(android.hardware.camera2.legacy.RequestThreadManager.MAX_IN_FLIGHT_REQUESTS, mDeviceState);
        mRequestThread = new android.hardware.camera2.legacy.RequestHandlerThread(name, mRequestHandlerCb);
        mCamera.setErrorCallback(mErrorCallback);
    }

    /**
     * Start the request thread.
     */
    public void start() {
        mRequestThread.start();
    }

    /**
     * Flush any pending requests.
     *
     * @return the last frame number.
     */
    public long flush() {
        android.util.Log.i(TAG, "Flushing all pending requests.");
        long lastFrame = mRequestQueue.stopRepeating();
        mCaptureCollector.failAll();
        return lastFrame;
    }

    /**
     * Quit the request thread, and clean up everything.
     */
    public void quit() {
        if (!mQuit.getAndSet(true)) {
            // Avoid sending messages on dead thread's handler.
            android.os.Handler handler = mRequestThread.waitAndGetHandler();
            handler.sendMessageAtFrontOfQueue(handler.obtainMessage(android.hardware.camera2.legacy.RequestThreadManager.MSG_CLEANUP));
            mRequestThread.quitSafely();
            try {
                mRequestThread.join();
            } catch (java.lang.InterruptedException e) {
                android.util.Log.e(TAG, java.lang.String.format("Thread %s (%d) interrupted while quitting.", mRequestThread.getName(), mRequestThread.getId()));
            }
        }
    }

    /**
     * Submit the given burst of requests to be captured.
     *
     * <p>If the burst is repeating, replace the current repeating burst.</p>
     *
     * @param requests
     * 		the burst of requests to add to the queue.
     * @param repeating
     * 		true if the burst is repeating.
     * @return the submission info, including the new request id, and the last frame number, which
    contains either the frame number of the last frame that will be returned for this request,
    or the frame number of the last frame that will be returned for the current repeating
    request if this burst is set to be repeating.
     */
    public android.hardware.camera2.utils.SubmitInfo submitCaptureRequests(android.hardware.camera2.CaptureRequest[] requests, boolean repeating) {
        android.os.Handler handler = mRequestThread.waitAndGetHandler();
        android.hardware.camera2.utils.SubmitInfo info;
        synchronized(mIdleLock) {
            info = mRequestQueue.submit(requests, repeating);
            handler.sendEmptyMessage(android.hardware.camera2.legacy.RequestThreadManager.MSG_SUBMIT_CAPTURE_REQUEST);
        }
        return info;
    }

    /**
     * Cancel a repeating request.
     *
     * @param requestId
     * 		the id of the repeating request to cancel.
     * @return the last frame to be returned from the HAL for the given repeating request, or
    {@code INVALID_FRAME} if none exists.
     */
    public long cancelRepeating(int requestId) {
        return mRequestQueue.stopRepeating(requestId);
    }

    /**
     * Configure with the current list of output Surfaces.
     *
     * <p>
     * This operation blocks until the configuration is complete.
     * </p>
     *
     * <p>Using a {@code null} or empty {@code outputs} list is the equivalent of unconfiguring.</p>
     *
     * @param outputs
     * 		a {@link java.util.Collection} of outputs to configure.
     */
    public void configure(java.util.Collection<android.util.Pair<android.view.Surface, android.util.Size>> outputs) {
        android.os.Handler handler = mRequestThread.waitAndGetHandler();
        final android.os.ConditionVariable condition = /* closed */
        new android.os.ConditionVariable(false);
        android.hardware.camera2.legacy.RequestThreadManager.ConfigureHolder holder = new android.hardware.camera2.legacy.RequestThreadManager.ConfigureHolder(condition, outputs);
        handler.sendMessage(handler.obtainMessage(android.hardware.camera2.legacy.RequestThreadManager.MSG_CONFIGURE_OUTPUTS, 0, 0, holder));
        condition.block();
    }
}

