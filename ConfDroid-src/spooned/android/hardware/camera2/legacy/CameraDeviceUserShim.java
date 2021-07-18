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
 * Compatibility implementation of the Camera2 API binder interface.
 *
 * <p>
 * This is intended to be called from the same process as client
 * {@link android.hardware.camera2.CameraDevice}, and wraps a
 * {@link android.hardware.camera2.legacy.LegacyCameraDevice} that emulates Camera2 service using
 * the Camera1 API.
 * </p>
 *
 * <p>
 * Keep up to date with ICameraDeviceUser.aidl.
 * </p>
 */
@java.lang.SuppressWarnings("deprecation")
public class CameraDeviceUserShim implements android.hardware.camera2.ICameraDeviceUser {
    private static final java.lang.String TAG = "CameraDeviceUserShim";

    private static final boolean DEBUG = false;

    private static final int OPEN_CAMERA_TIMEOUT_MS = 5000;// 5 sec (same as api1 cts timeout)


    private final android.hardware.camera2.legacy.LegacyCameraDevice mLegacyDevice;

    private final java.lang.Object mConfigureLock = new java.lang.Object();

    private int mSurfaceIdCounter;

    private boolean mConfiguring;

    private final android.util.SparseArray<android.view.Surface> mSurfaces;

    private final android.hardware.camera2.CameraCharacteristics mCameraCharacteristics;

    private final android.hardware.camera2.legacy.CameraDeviceUserShim.CameraLooper mCameraInit;

    private final android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread mCameraCallbacks;

    protected CameraDeviceUserShim(int cameraId, android.hardware.camera2.legacy.LegacyCameraDevice legacyCamera, android.hardware.camera2.CameraCharacteristics characteristics, android.hardware.camera2.legacy.CameraDeviceUserShim.CameraLooper cameraInit, android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread cameraCallbacks) {
        mLegacyDevice = legacyCamera;
        mConfiguring = false;
        mSurfaces = new android.util.SparseArray<android.view.Surface>();
        mCameraCharacteristics = characteristics;
        mCameraInit = cameraInit;
        mCameraCallbacks = cameraCallbacks;
        mSurfaceIdCounter = 0;
    }

    private static int translateErrorsFromCamera1(int errorCode) {
        if (errorCode == (-android.system.OsConstants.EACCES)) {
            return android.hardware.ICameraService.ERROR_PERMISSION_DENIED;
        }
        return errorCode;
    }

    /**
     * Create a separate looper/thread for the camera to run on; open the camera.
     *
     * <p>Since the camera automatically latches on to the current thread's looper,
     * it's important that we have our own thread with our own looper to guarantee
     * that the camera callbacks get correctly posted to our own thread.</p>
     */
    private static class CameraLooper implements java.lang.AutoCloseable , java.lang.Runnable {
        private final int mCameraId;

        private android.os.Looper mLooper;

        private volatile int mInitErrors;

        private final android.hardware.Camera mCamera = android.hardware.Camera.openUninitialized();

        private final android.os.ConditionVariable mStartDone = new android.os.ConditionVariable();

        private final java.lang.Thread mThread;

        /**
         * Spin up a new thread, immediately open the camera in the background.
         *
         * <p>Use {@link #waitForOpen} to block until the camera is finished opening.</p>
         *
         * @param cameraId
         * 		numeric camera Id
         * @see #waitForOpen
         */
        public CameraLooper(int cameraId) {
            mCameraId = cameraId;
            mThread = new java.lang.Thread(this);
            mThread.start();
        }

        public android.hardware.Camera getCamera() {
            return mCamera;
        }

        @java.lang.Override
        public void run() {
            // Set up a looper to be used by camera.
            android.os.Looper.prepare();
            // Save the looper so that we can terminate this thread
            // after we are done with it.
            mLooper = android.os.Looper.myLooper();
            mInitErrors = mCamera.cameraInitUnspecified(mCameraId);
            mStartDone.open();
            android.os.Looper.loop();// Blocks forever until #close is called.

        }

        /**
         * Quit the looper safely; then join until the thread shuts down.
         */
        @java.lang.Override
        public void close() {
            if (mLooper == null) {
                return;
            }
            mLooper.quitSafely();
            try {
                mThread.join();
            } catch (java.lang.InterruptedException e) {
                throw new java.lang.AssertionError(e);
            }
            mLooper = null;
        }

        /**
         * Block until the camera opens; then return its initialization error code (if any).
         *
         * @param timeoutMs
         * 		timeout in milliseconds
         * @return int error code
         * @throws ServiceSpecificException
         * 		if the camera open times out with ({@code CAMERA_ERROR})
         */
        public int waitForOpen(int timeoutMs) {
            // Block until the camera is open asynchronously
            if (!mStartDone.block(timeoutMs)) {
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, ("waitForOpen - Camera failed to open after timeout of " + android.hardware.camera2.legacy.CameraDeviceUserShim.OPEN_CAMERA_TIMEOUT_MS) + " ms");
                try {
                    mCamera.release();
                } catch (java.lang.RuntimeException e) {
                    android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "connectBinderShim - Failed to release camera after timeout ", e);
                }
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION);
            }
            return mInitErrors;
        }
    }

    /**
     * A thread to process callbacks to send back to the camera client.
     *
     * <p>This effectively emulates one-way binder semantics when in the same process as the
     * callee.</p>
     */
    private static class CameraCallbackThread implements android.hardware.camera2.ICameraDeviceCallbacks {
        private static final int CAMERA_ERROR = 0;

        private static final int CAMERA_IDLE = 1;

        private static final int CAPTURE_STARTED = 2;

        private static final int RESULT_RECEIVED = 3;

        private static final int PREPARED = 4;

        private static final int REPEATING_REQUEST_ERROR = 5;

        private final android.os.HandlerThread mHandlerThread;

        private android.os.Handler mHandler;

        private final android.hardware.camera2.ICameraDeviceCallbacks mCallbacks;

        public CameraCallbackThread(android.hardware.camera2.ICameraDeviceCallbacks callbacks) {
            mCallbacks = callbacks;
            mHandlerThread = new android.os.HandlerThread("LegacyCameraCallback");
            mHandlerThread.start();
        }

        public void close() {
            mHandlerThread.quitSafely();
        }

        @java.lang.Override
        public void onDeviceError(final int errorCode, final android.hardware.camera2.impl.CaptureResultExtras resultExtras) {
            android.os.Message msg = /* arg1 */
            /* arg2 */
            /* obj */
            getHandler().obtainMessage(android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.CAMERA_ERROR, errorCode, 0, resultExtras);
            getHandler().sendMessage(msg);
        }

        @java.lang.Override
        public void onDeviceIdle() {
            android.os.Message msg = getHandler().obtainMessage(android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.CAMERA_IDLE);
            getHandler().sendMessage(msg);
        }

        @java.lang.Override
        public void onCaptureStarted(final android.hardware.camera2.impl.CaptureResultExtras resultExtras, final long timestamp) {
            android.os.Message msg = /* arg1 */
            /* arg2 */
            /* obj */
            getHandler().obtainMessage(android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.CAPTURE_STARTED, ((int) (timestamp & 0xffffffffL)), ((int) ((timestamp >> 32) & 0xffffffffL)), resultExtras);
            getHandler().sendMessage(msg);
        }

        @java.lang.Override
        public void onResultReceived(final android.hardware.camera2.impl.CameraMetadataNative result, final android.hardware.camera2.impl.CaptureResultExtras resultExtras) {
            java.lang.Object[] resultArray = new java.lang.Object[]{ result, resultExtras };
            android.os.Message msg = /* obj */
            getHandler().obtainMessage(android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.RESULT_RECEIVED, resultArray);
            getHandler().sendMessage(msg);
        }

        @java.lang.Override
        public void onPrepared(int streamId) {
            android.os.Message msg = /* arg1 */
            /* arg2 */
            getHandler().obtainMessage(android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.PREPARED, streamId, 0);
            getHandler().sendMessage(msg);
        }

        @java.lang.Override
        public void onRepeatingRequestError(long lastFrameNumber) {
            android.os.Message msg = /* arg1 */
            /* arg2 */
            getHandler().obtainMessage(android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.REPEATING_REQUEST_ERROR, ((int) (lastFrameNumber & 0xffffffffL)), ((int) ((lastFrameNumber >> 32) & 0xffffffffL)));
            getHandler().sendMessage(msg);
        }

        @java.lang.Override
        public android.os.IBinder asBinder() {
            // This is solely intended to be used for in-process binding.
            return null;
        }

        private android.os.Handler getHandler() {
            if (mHandler == null) {
                mHandler = new android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.CallbackHandler(mHandlerThread.getLooper());
            }
            return mHandler;
        }

        private class CallbackHandler extends android.os.Handler {
            public CallbackHandler(android.os.Looper l) {
                super(l);
            }

            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                try {
                    switch (msg.what) {
                        case android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.CAMERA_ERROR :
                            {
                                int errorCode = msg.arg1;
                                android.hardware.camera2.impl.CaptureResultExtras resultExtras = ((android.hardware.camera2.impl.CaptureResultExtras) (msg.obj));
                                mCallbacks.onDeviceError(errorCode, resultExtras);
                                break;
                            }
                        case android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.CAMERA_IDLE :
                            mCallbacks.onDeviceIdle();
                            break;
                        case android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.CAPTURE_STARTED :
                            {
                                long timestamp = msg.arg2 & 0xffffffffL;
                                timestamp = (timestamp << 32) | (msg.arg1 & 0xffffffffL);
                                android.hardware.camera2.impl.CaptureResultExtras resultExtras = ((android.hardware.camera2.impl.CaptureResultExtras) (msg.obj));
                                mCallbacks.onCaptureStarted(resultExtras, timestamp);
                                break;
                            }
                        case android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.RESULT_RECEIVED :
                            {
                                java.lang.Object[] resultArray = ((java.lang.Object[]) (msg.obj));
                                android.hardware.camera2.impl.CameraMetadataNative result = ((android.hardware.camera2.impl.CameraMetadataNative) (resultArray[0]));
                                android.hardware.camera2.impl.CaptureResultExtras resultExtras = ((android.hardware.camera2.impl.CaptureResultExtras) (resultArray[1]));
                                mCallbacks.onResultReceived(result, resultExtras);
                                break;
                            }
                        case android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.PREPARED :
                            {
                                int streamId = msg.arg1;
                                mCallbacks.onPrepared(streamId);
                                break;
                            }
                        case android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread.REPEATING_REQUEST_ERROR :
                            {
                                long lastFrameNumber = msg.arg2 & 0xffffffffL;
                                lastFrameNumber = (lastFrameNumber << 32) | (msg.arg1 & 0xffffffffL);
                                mCallbacks.onRepeatingRequestError(lastFrameNumber);
                                break;
                            }
                        default :
                            throw new java.lang.IllegalArgumentException("Unknown callback message " + msg.what);
                    }
                } catch (android.os.RemoteException e) {
                    throw new java.lang.IllegalStateException("Received remote exception during camera callback " + msg.what, e);
                }
            }
        }
    }

    public static android.hardware.camera2.legacy.CameraDeviceUserShim connectBinderShim(android.hardware.camera2.ICameraDeviceCallbacks callbacks, int cameraId) {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "Opening shim Camera device");
        }
        /* Put the camera open on a separate thread with its own looper; otherwise
        if the main thread is used then the callbacks might never get delivered
        (e.g. in CTS which run its own default looper only after tests)
         */
        android.hardware.camera2.legacy.CameraDeviceUserShim.CameraLooper init = new android.hardware.camera2.legacy.CameraDeviceUserShim.CameraLooper(cameraId);
        android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread threadCallbacks = new android.hardware.camera2.legacy.CameraDeviceUserShim.CameraCallbackThread(callbacks);
        // TODO: Make this async instead of blocking
        int initErrors = init.waitForOpen(android.hardware.camera2.legacy.CameraDeviceUserShim.OPEN_CAMERA_TIMEOUT_MS);
        android.hardware.Camera legacyCamera = init.getCamera();
        // Check errors old HAL initialization
        android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnServiceError(initErrors);
        // Disable shutter sounds (this will work unconditionally) for api2 clients
        legacyCamera.disableShutterSound();
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        android.hardware.Camera.Parameters legacyParameters = null;
        try {
            legacyParameters = legacyCamera.getParameters();
        } catch (java.lang.RuntimeException e) {
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, "Unable to get initial parameters: " + e.getMessage());
        }
        android.hardware.camera2.CameraCharacteristics characteristics = android.hardware.camera2.legacy.LegacyMetadataMapper.createCharacteristics(legacyParameters, info);
        android.hardware.camera2.legacy.LegacyCameraDevice device = new android.hardware.camera2.legacy.LegacyCameraDevice(cameraId, legacyCamera, characteristics, threadCallbacks);
        return new android.hardware.camera2.legacy.CameraDeviceUserShim(cameraId, device, characteristics, init, threadCallbacks);
    }

    @java.lang.Override
    public void disconnect() {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "disconnect called.");
        }
        if (mLegacyDevice.isClosed()) {
            android.util.Log.w(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "Cannot disconnect, device has already been closed.");
        }
        try {
            mLegacyDevice.close();
        } finally {
            mCameraInit.close();
            mCameraCallbacks.close();
        }
    }

    @java.lang.Override
    public android.hardware.camera2.utils.SubmitInfo submitRequest(android.hardware.camera2.CaptureRequest request, boolean streaming) {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "submitRequest called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot submit request, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        synchronized(mConfigureLock) {
            if (mConfiguring) {
                java.lang.String err = "Cannot submit request, configuration change in progress.";
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
            }
        }
        return mLegacyDevice.submitRequest(request, streaming);
    }

    @java.lang.Override
    public android.hardware.camera2.utils.SubmitInfo submitRequestList(android.hardware.camera2.CaptureRequest[] request, boolean streaming) {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "submitRequestList called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot submit request list, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        synchronized(mConfigureLock) {
            if (mConfiguring) {
                java.lang.String err = "Cannot submit request, configuration change in progress.";
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
            }
        }
        return mLegacyDevice.submitRequestList(request, streaming);
    }

    @java.lang.Override
    public long cancelRequest(int requestId) {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "cancelRequest called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot cancel request, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        synchronized(mConfigureLock) {
            if (mConfiguring) {
                java.lang.String err = "Cannot cancel request, configuration change in progress.";
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
            }
        }
        return mLegacyDevice.cancelRequest(requestId);
    }

    @java.lang.Override
    public void beginConfigure() {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "beginConfigure called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot begin configure, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        synchronized(mConfigureLock) {
            if (mConfiguring) {
                java.lang.String err = "Cannot begin configure, configuration change already in progress.";
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
            }
            mConfiguring = true;
        }
    }

    @java.lang.Override
    public void endConfigure(boolean isConstrainedHighSpeed) {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "endConfigure called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot end configure, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        android.util.SparseArray<android.view.Surface> surfaces = null;
        synchronized(mConfigureLock) {
            if (!mConfiguring) {
                java.lang.String err = "Cannot end configure, no configuration change in progress.";
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
            }
            if (mSurfaces != null) {
                surfaces = mSurfaces.clone();
            }
            mConfiguring = false;
        }
        mLegacyDevice.configureOutputs(surfaces);
    }

    @java.lang.Override
    public void deleteStream(int streamId) {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "deleteStream called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot delete stream, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        synchronized(mConfigureLock) {
            if (!mConfiguring) {
                java.lang.String err = "Cannot delete stream, no configuration change in progress.";
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
            }
            int index = mSurfaces.indexOfKey(streamId);
            if (index < 0) {
                java.lang.String err = ("Cannot delete stream, stream id " + streamId) + " doesn't exist.";
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_ILLEGAL_ARGUMENT, err);
            }
            mSurfaces.removeAt(index);
        }
    }

    @java.lang.Override
    public int createStream(android.hardware.camera2.params.OutputConfiguration outputConfiguration) {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "createStream called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot create stream, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        synchronized(mConfigureLock) {
            if (!mConfiguring) {
                java.lang.String err = "Cannot create stream, beginConfigure hasn't been called yet.";
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
            }
            if (outputConfiguration.getRotation() != android.hardware.camera2.params.OutputConfiguration.ROTATION_0) {
                java.lang.String err = "Cannot create stream, stream rotation is not supported.";
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_ILLEGAL_ARGUMENT, err);
            }
            int id = ++mSurfaceIdCounter;
            mSurfaces.put(id, outputConfiguration.getSurface());
            return id;
        }
    }

    @java.lang.Override
    public void setDeferredConfiguration(int steamId, android.hardware.camera2.params.OutputConfiguration config) {
        java.lang.String err = "Set deferred configuration is not supported on legacy devices";
        android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
        throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
    }

    @java.lang.Override
    public int createInputStream(int width, int height, int format) {
        java.lang.String err = "Creating input stream is not supported on legacy devices";
        android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
        throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
    }

    @java.lang.Override
    public android.view.Surface getInputSurface() {
        java.lang.String err = "Getting input surface is not supported on legacy devices";
        android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
        throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
    }

    @java.lang.Override
    public android.hardware.camera2.impl.CameraMetadataNative createDefaultRequest(int templateId) {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "createDefaultRequest called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot create default request, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        android.hardware.camera2.impl.CameraMetadataNative template;
        try {
            template = android.hardware.camera2.legacy.LegacyMetadataMapper.createRequestTemplate(mCameraCharacteristics, templateId);
        } catch (java.lang.IllegalArgumentException e) {
            java.lang.String err = "createDefaultRequest - invalid templateId specified";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_ILLEGAL_ARGUMENT, err);
        }
        return template;
    }

    @java.lang.Override
    public android.hardware.camera2.impl.CameraMetadataNative getCameraInfo() {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "getCameraInfo called.");
        }
        // TODO: implement getCameraInfo.
        android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "getCameraInfo unimplemented.");
        return null;
    }

    @java.lang.Override
    public void waitUntilIdle() throws android.os.RemoteException {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "waitUntilIdle called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot wait until idle, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        synchronized(mConfigureLock) {
            if (mConfiguring) {
                java.lang.String err = "Cannot wait until idle, configuration change in progress.";
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
            }
        }
        mLegacyDevice.waitUntilIdle();
    }

    @java.lang.Override
    public long flush() {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "flush called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot flush, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        synchronized(mConfigureLock) {
            if (mConfiguring) {
                java.lang.String err = "Cannot flush, configuration change in progress.";
                android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
                throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_INVALID_OPERATION, err);
            }
        }
        return mLegacyDevice.flush();
    }

    public void prepare(int streamId) {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "prepare called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot prepare stream, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        // LEGACY doesn't support actual prepare, just signal success right away
        mCameraCallbacks.onPrepared(streamId);
    }

    public void prepare2(int maxCount, int streamId) {
        // We don't support this in LEGACY mode.
        prepare(streamId);
    }

    public void tearDown(int streamId) {
        if (android.hardware.camera2.legacy.CameraDeviceUserShim.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, "tearDown called.");
        }
        if (mLegacyDevice.isClosed()) {
            java.lang.String err = "Cannot tear down stream, device has been closed.";
            android.util.Log.e(android.hardware.camera2.legacy.CameraDeviceUserShim.TAG, err);
            throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, err);
        }
        // LEGACY doesn't support actual teardown, so just a no-op
    }

    @java.lang.Override
    public android.os.IBinder asBinder() {
        // This is solely intended to be used for in-process binding.
        return null;
    }
}

