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
package android.hardware.camera2.impl;


/**
 * Proxy out invocations to the camera2 API callbacks into a {@link Dispatchable}.
 *
 * <p>Since abstract classes do not support Java's dynamic {@code Proxy}, we have to
 * to use our own proxy mechanism.</p>
 */
public class CallbackProxies {
    // TODO: replace with codegen
    public static class DeviceStateCallbackProxy extends android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK {
        private final android.hardware.camera2.dispatch.MethodNameInvoker<android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK> mProxy;

        public DeviceStateCallbackProxy(android.hardware.camera2.dispatch.Dispatchable<android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK> dispatchTarget) {
            dispatchTarget = checkNotNull(dispatchTarget, "dispatchTarget must not be null");
            mProxy = new android.hardware.camera2.dispatch.MethodNameInvoker<>(dispatchTarget, android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK.class);
        }

        @java.lang.Override
        public void onOpened(android.hardware.camera2.CameraDevice camera) {
            mProxy.invoke("onOpened", camera);
        }

        @java.lang.Override
        public void onDisconnected(android.hardware.camera2.CameraDevice camera) {
            mProxy.invoke("onDisconnected", camera);
        }

        @java.lang.Override
        public void onError(android.hardware.camera2.CameraDevice camera, int error) {
            mProxy.invoke("onError", camera, error);
        }

        @java.lang.Override
        public void onUnconfigured(android.hardware.camera2.CameraDevice camera) {
            mProxy.invoke("onUnconfigured", camera);
        }

        @java.lang.Override
        public void onActive(android.hardware.camera2.CameraDevice camera) {
            mProxy.invoke("onActive", camera);
        }

        @java.lang.Override
        public void onBusy(android.hardware.camera2.CameraDevice camera) {
            mProxy.invoke("onBusy", camera);
        }

        @java.lang.Override
        public void onClosed(android.hardware.camera2.CameraDevice camera) {
            mProxy.invoke("onClosed", camera);
        }

        @java.lang.Override
        public void onIdle(android.hardware.camera2.CameraDevice camera) {
            mProxy.invoke("onIdle", camera);
        }
    }

    @java.lang.SuppressWarnings("deprecation")
    public static class DeviceCaptureCallbackProxy extends android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback {
        private final android.hardware.camera2.dispatch.MethodNameInvoker<android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback> mProxy;

        public DeviceCaptureCallbackProxy(android.hardware.camera2.dispatch.Dispatchable<android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback> dispatchTarget) {
            dispatchTarget = checkNotNull(dispatchTarget, "dispatchTarget must not be null");
            mProxy = new android.hardware.camera2.dispatch.MethodNameInvoker<>(dispatchTarget, android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback.class);
        }

        @java.lang.Override
        public void onCaptureStarted(android.hardware.camera2.CameraDevice camera, android.hardware.camera2.CaptureRequest request, long timestamp, long frameNumber) {
            mProxy.invoke("onCaptureStarted", camera, request, timestamp, frameNumber);
        }

        @java.lang.Override
        public void onCapturePartial(android.hardware.camera2.CameraDevice camera, android.hardware.camera2.CaptureRequest request, android.hardware.camera2.CaptureResult result) {
            mProxy.invoke("onCapturePartial", camera, request, result);
        }

        @java.lang.Override
        public void onCaptureProgressed(android.hardware.camera2.CameraDevice camera, android.hardware.camera2.CaptureRequest request, android.hardware.camera2.CaptureResult partialResult) {
            mProxy.invoke("onCaptureProgressed", camera, request, partialResult);
        }

        @java.lang.Override
        public void onCaptureCompleted(android.hardware.camera2.CameraDevice camera, android.hardware.camera2.CaptureRequest request, android.hardware.camera2.TotalCaptureResult result) {
            mProxy.invoke("onCaptureCompleted", camera, request, result);
        }

        @java.lang.Override
        public void onCaptureFailed(android.hardware.camera2.CameraDevice camera, android.hardware.camera2.CaptureRequest request, android.hardware.camera2.CaptureFailure failure) {
            mProxy.invoke("onCaptureFailed", camera, request, failure);
        }

        @java.lang.Override
        public void onCaptureSequenceCompleted(android.hardware.camera2.CameraDevice camera, int sequenceId, long frameNumber) {
            mProxy.invoke("onCaptureSequenceCompleted", camera, sequenceId, frameNumber);
        }

        @java.lang.Override
        public void onCaptureSequenceAborted(android.hardware.camera2.CameraDevice camera, int sequenceId) {
            mProxy.invoke("onCaptureSequenceAborted", camera, sequenceId);
        }
    }

    public static class SessionStateCallbackProxy extends android.hardware.camera2.CameraCaptureSession.StateCallback {
        private final android.hardware.camera2.dispatch.MethodNameInvoker<android.hardware.camera2.CameraCaptureSession.StateCallback> mProxy;

        public SessionStateCallbackProxy(android.hardware.camera2.dispatch.Dispatchable<android.hardware.camera2.CameraCaptureSession.StateCallback> dispatchTarget) {
            dispatchTarget = checkNotNull(dispatchTarget, "dispatchTarget must not be null");
            mProxy = new android.hardware.camera2.dispatch.MethodNameInvoker<>(dispatchTarget, android.hardware.camera2.CameraCaptureSession.StateCallback.class);
        }

        @java.lang.Override
        public void onConfigured(android.hardware.camera2.CameraCaptureSession session) {
            mProxy.invoke("onConfigured", session);
        }

        @java.lang.Override
        public void onConfigureFailed(android.hardware.camera2.CameraCaptureSession session) {
            mProxy.invoke("onConfigureFailed", session);
        }

        @java.lang.Override
        public void onReady(android.hardware.camera2.CameraCaptureSession session) {
            mProxy.invoke("onReady", session);
        }

        @java.lang.Override
        public void onActive(android.hardware.camera2.CameraCaptureSession session) {
            mProxy.invoke("onActive", session);
        }

        @java.lang.Override
        public void onClosed(android.hardware.camera2.CameraCaptureSession session) {
            mProxy.invoke("onClosed", session);
        }

        @java.lang.Override
        public void onSurfacePrepared(android.hardware.camera2.CameraCaptureSession session, android.view.Surface surface) {
            mProxy.invoke("onSurfacePrepared", session, surface);
        }
    }

    private CallbackProxies() {
        throw new java.lang.AssertionError();
    }
}

