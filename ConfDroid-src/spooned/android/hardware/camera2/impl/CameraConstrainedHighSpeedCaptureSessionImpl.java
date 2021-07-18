/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.hardware.camera2.impl;


/**
 * Standard implementation of CameraConstrainedHighSpeedCaptureSession.
 *
 * <p>
 * Mostly just forwards calls to an instance of CameraCaptureSessionImpl,
 * but implements the few necessary behavior changes and additional methods required
 * for the constrained high speed speed mode.
 * </p>
 */
public class CameraConstrainedHighSpeedCaptureSessionImpl extends android.hardware.camera2.CameraConstrainedHighSpeedCaptureSession implements android.hardware.camera2.impl.CameraCaptureSessionCore {
    private final android.hardware.camera2.CameraCharacteristics mCharacteristics;

    private final android.hardware.camera2.impl.CameraCaptureSessionImpl mSessionImpl;

    /**
     * Create a new CameraCaptureSession.
     *
     * <p>The camera device must already be in the {@code IDLE} state when this is invoked.
     * There must be no pending actions
     * (e.g. no pending captures, no repeating requests, no flush).</p>
     */
    CameraConstrainedHighSpeedCaptureSessionImpl(int id, java.util.List<android.view.Surface> outputs, android.hardware.camera2.CameraCaptureSession.StateCallback callback, android.os.Handler stateHandler, android.hardware.camera2.impl.CameraDeviceImpl deviceImpl, android.os.Handler deviceStateHandler, boolean configureSuccess, android.hardware.camera2.CameraCharacteristics characteristics) {
        mCharacteristics = characteristics;
        android.hardware.camera2.CameraCaptureSession.StateCallback wrapperCallback = new android.hardware.camera2.impl.CameraConstrainedHighSpeedCaptureSessionImpl.WrapperCallback(callback);
        mSessionImpl = /* input */
        new android.hardware.camera2.impl.CameraCaptureSessionImpl(id, null, outputs, wrapperCallback, stateHandler, deviceImpl, deviceStateHandler, configureSuccess);
    }

    @java.lang.Override
    public java.util.List<android.hardware.camera2.CaptureRequest> createHighSpeedRequestList(android.hardware.camera2.CaptureRequest request) throws android.hardware.camera2.CameraAccessException {
        if (request == null) {
            throw new java.lang.IllegalArgumentException("Input capture request must not be null");
        }
        java.util.Collection<android.view.Surface> outputSurfaces = request.getTargets();
        android.util.Range<java.lang.Integer> fpsRange = request.get(android.hardware.camera2.CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
        android.hardware.camera2.params.StreamConfigurationMap config = mCharacteristics.get(android.hardware.camera2.CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        android.hardware.camera2.utils.SurfaceUtils.checkConstrainedHighSpeedSurfaces(outputSurfaces, fpsRange, config);
        // Request list size: to limit the preview to 30fps, need use maxFps/30; to maximize
        // the preview frame rate, should use maxBatch size for that high speed stream
        // configuration. We choose the former for now.
        int requestListSize = fpsRange.getUpper() / 30;
        java.util.List<android.hardware.camera2.CaptureRequest> requestList = new java.util.ArrayList<android.hardware.camera2.CaptureRequest>();
        // Prepare the Request builders: need carry over the request controls.
        // First, create a request builder that will only include preview or recording target.
        android.hardware.camera2.impl.CameraMetadataNative requestMetadata = new android.hardware.camera2.impl.CameraMetadataNative(request.getNativeCopy());
        // Note that after this step, the requestMetadata is mutated (swapped) and can not be used
        // for next request builder creation.
        android.hardware.camera2.CaptureRequest.Builder singleTargetRequestBuilder = /* reprocess */
        new android.hardware.camera2.CaptureRequest.Builder(requestMetadata, false, android.hardware.camera2.CameraCaptureSession.SESSION_ID_NONE);
        // Overwrite the capture intent to make sure a good value is set.
        java.util.Iterator<android.view.Surface> iterator = outputSurfaces.iterator();
        android.view.Surface firstSurface = iterator.next();
        android.view.Surface secondSurface = null;
        if ((outputSurfaces.size() == 1) && android.hardware.camera2.utils.SurfaceUtils.isSurfaceForHwVideoEncoder(firstSurface)) {
            singleTargetRequestBuilder.set(android.hardware.camera2.CaptureRequest.CONTROL_CAPTURE_INTENT, android.hardware.camera2.CaptureRequest.CONTROL_CAPTURE_INTENT_PREVIEW);
        } else {
            // Video only, or preview + video
            singleTargetRequestBuilder.set(android.hardware.camera2.CaptureRequest.CONTROL_CAPTURE_INTENT, android.hardware.camera2.CaptureRequest.CONTROL_CAPTURE_INTENT_VIDEO_RECORD);
        }
        /* partOfCHSList */
        singleTargetRequestBuilder.setPartOfCHSRequestList(true);
        // Second, Create a request builder that will include both preview and recording targets.
        android.hardware.camera2.CaptureRequest.Builder doubleTargetRequestBuilder = null;
        if (outputSurfaces.size() == 2) {
            // Have to create a new copy, the original one was mutated after a new
            // CaptureRequest.Builder creation.
            requestMetadata = new android.hardware.camera2.impl.CameraMetadataNative(request.getNativeCopy());
            doubleTargetRequestBuilder = /* reprocess */
            new android.hardware.camera2.CaptureRequest.Builder(requestMetadata, false, android.hardware.camera2.CameraCaptureSession.SESSION_ID_NONE);
            doubleTargetRequestBuilder.set(android.hardware.camera2.CaptureRequest.CONTROL_CAPTURE_INTENT, android.hardware.camera2.CaptureRequest.CONTROL_CAPTURE_INTENT_VIDEO_RECORD);
            doubleTargetRequestBuilder.addTarget(firstSurface);
            secondSurface = iterator.next();
            doubleTargetRequestBuilder.addTarget(secondSurface);
            /* partOfCHSList */
            doubleTargetRequestBuilder.setPartOfCHSRequestList(true);
            // Make sure singleTargetRequestBuilder contains only recording surface for
            // preview + recording case.
            android.view.Surface recordingSurface = firstSurface;
            if (!android.hardware.camera2.utils.SurfaceUtils.isSurfaceForHwVideoEncoder(recordingSurface)) {
                recordingSurface = secondSurface;
            }
            singleTargetRequestBuilder.addTarget(recordingSurface);
        } else {
            // Single output case: either recording or preview.
            singleTargetRequestBuilder.addTarget(firstSurface);
        }
        // Generate the final request list.
        for (int i = 0; i < requestListSize; i++) {
            if ((i == 0) && (doubleTargetRequestBuilder != null)) {
                // First request should be recording + preview request
                requestList.add(doubleTargetRequestBuilder.build());
            } else {
                requestList.add(singleTargetRequestBuilder.build());
            }
        }
        return java.util.Collections.unmodifiableList(requestList);
    }

    private boolean isConstrainedHighSpeedRequestList(java.util.List<android.hardware.camera2.CaptureRequest> requestList) {
        checkCollectionNotEmpty(requestList, "High speed request list");
        for (android.hardware.camera2.CaptureRequest request : requestList) {
            if (!request.isPartOfCRequestList()) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    public android.hardware.camera2.CameraDevice getDevice() {
        return mSessionImpl.getDevice();
    }

    @java.lang.Override
    public void prepare(android.view.Surface surface) throws android.hardware.camera2.CameraAccessException {
        mSessionImpl.prepare(surface);
    }

    @java.lang.Override
    public void prepare(int maxCount, android.view.Surface surface) throws android.hardware.camera2.CameraAccessException {
        mSessionImpl.prepare(maxCount, surface);
    }

    @java.lang.Override
    public void tearDown(android.view.Surface surface) throws android.hardware.camera2.CameraAccessException {
        mSessionImpl.tearDown(surface);
    }

    @java.lang.Override
    public int capture(android.hardware.camera2.CaptureRequest request, android.hardware.camera2.CameraCaptureSession.CaptureCallback listener, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        throw new java.lang.UnsupportedOperationException("Constrained high speed session doesn't support" + " this method");
    }

    @java.lang.Override
    public int captureBurst(java.util.List<android.hardware.camera2.CaptureRequest> requests, android.hardware.camera2.CameraCaptureSession.CaptureCallback listener, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if (!isConstrainedHighSpeedRequestList(requests)) {
            throw new java.lang.IllegalArgumentException("Only request lists created by createHighSpeedRequestList() can be submitted to " + "a constrained high speed capture session");
        }
        return mSessionImpl.captureBurst(requests, listener, handler);
    }

    @java.lang.Override
    public int setRepeatingRequest(android.hardware.camera2.CaptureRequest request, android.hardware.camera2.CameraCaptureSession.CaptureCallback listener, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        throw new java.lang.UnsupportedOperationException("Constrained high speed session doesn't support" + " this method");
    }

    @java.lang.Override
    public int setRepeatingBurst(java.util.List<android.hardware.camera2.CaptureRequest> requests, android.hardware.camera2.CameraCaptureSession.CaptureCallback listener, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if (!isConstrainedHighSpeedRequestList(requests)) {
            throw new java.lang.IllegalArgumentException("Only request lists created by createHighSpeedRequestList() can be submitted to " + "a constrained high speed capture session");
        }
        return mSessionImpl.setRepeatingBurst(requests, listener, handler);
    }

    @java.lang.Override
    public void stopRepeating() throws android.hardware.camera2.CameraAccessException {
        mSessionImpl.stopRepeating();
    }

    @java.lang.Override
    public void abortCaptures() throws android.hardware.camera2.CameraAccessException {
        mSessionImpl.abortCaptures();
    }

    @java.lang.Override
    public android.view.Surface getInputSurface() {
        return null;
    }

    @java.lang.Override
    public void close() {
        mSessionImpl.close();
    }

    @java.lang.Override
    public boolean isReprocessable() {
        return false;
    }

    // Implementation of CameraCaptureSessionCore methods
    @java.lang.Override
    public void replaceSessionClose() {
        mSessionImpl.replaceSessionClose();
    }

    @java.lang.Override
    public android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK getDeviceStateCallback() {
        return mSessionImpl.getDeviceStateCallback();
    }

    @java.lang.Override
    public boolean isAborting() {
        return mSessionImpl.isAborting();
    }

    @java.lang.Override
    public void finishDeferredConfiguration(java.util.List<android.hardware.camera2.params.OutputConfiguration> deferredOutputConfigs) throws android.hardware.camera2.CameraAccessException {
        mSessionImpl.finishDeferredConfiguration(deferredOutputConfigs);
    }

    private class WrapperCallback extends android.hardware.camera2.CameraCaptureSession.StateCallback {
        private final android.hardware.camera2.CameraCaptureSession.StateCallback mCallback;

        public WrapperCallback(android.hardware.camera2.CameraCaptureSession.StateCallback callback) {
            mCallback = callback;
        }

        @java.lang.Override
        public void onConfigured(android.hardware.camera2.CameraCaptureSession session) {
            mCallback.onConfigured(android.hardware.camera2.impl.CameraConstrainedHighSpeedCaptureSessionImpl.this);
        }

        @java.lang.Override
        public void onConfigureFailed(android.hardware.camera2.CameraCaptureSession session) {
            mCallback.onConfigureFailed(android.hardware.camera2.impl.CameraConstrainedHighSpeedCaptureSessionImpl.this);
        }

        @java.lang.Override
        public void onReady(android.hardware.camera2.CameraCaptureSession session) {
            mCallback.onReady(android.hardware.camera2.impl.CameraConstrainedHighSpeedCaptureSessionImpl.this);
        }

        @java.lang.Override
        public void onActive(android.hardware.camera2.CameraCaptureSession session) {
            mCallback.onActive(android.hardware.camera2.impl.CameraConstrainedHighSpeedCaptureSessionImpl.this);
        }

        @java.lang.Override
        public void onClosed(android.hardware.camera2.CameraCaptureSession session) {
            mCallback.onClosed(android.hardware.camera2.impl.CameraConstrainedHighSpeedCaptureSessionImpl.this);
        }

        @java.lang.Override
        public void onSurfacePrepared(android.hardware.camera2.CameraCaptureSession session, android.view.Surface surface) {
            mCallback.onSurfacePrepared(android.hardware.camera2.impl.CameraConstrainedHighSpeedCaptureSessionImpl.this, surface);
        }
    }
}

