/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * A wrapper around ICameraDeviceUser.
 *
 * Mainly used to convert ServiceSpecificExceptions to the correct
 * checked / unchecked exception.
 *
 * @unknown 
 */
public class ICameraDeviceUserWrapper {
    private final android.hardware.camera2.ICameraDeviceUser mRemoteDevice;

    public ICameraDeviceUserWrapper(android.hardware.camera2.ICameraDeviceUser remoteDevice) {
        if (remoteDevice == null) {
            throw new java.lang.NullPointerException("Remote device may not be null");
        }
        mRemoteDevice = remoteDevice;
    }

    public void unlinkToDeath(android.os.IBinder.DeathRecipient recipient, int flags) {
        if (mRemoteDevice.asBinder() != null) {
            unlinkToDeath(recipient, flags);
        }
    }

    public void disconnect() {
        try {
            mRemoteDevice.disconnect();
        } catch (android.os.RemoteException t) {
            // ignore binder errors for disconnect
        }
    }

    public android.hardware.camera2.utils.SubmitInfo submitRequest(android.hardware.camera2.CaptureRequest request, boolean streaming) throws android.hardware.camera2.CameraAccessException {
        try {
            return mRemoteDevice.submitRequest(request, streaming);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public android.hardware.camera2.utils.SubmitInfo submitRequestList(android.hardware.camera2.CaptureRequest[] requestList, boolean streaming) throws android.hardware.camera2.CameraAccessException {
        try {
            return mRemoteDevice.submitRequestList(requestList, streaming);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public long cancelRequest(int requestId) throws android.hardware.camera2.CameraAccessException {
        try {
            return mRemoteDevice.cancelRequest(requestId);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public void beginConfigure() throws android.hardware.camera2.CameraAccessException {
        try {
            mRemoteDevice.beginConfigure();
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public void endConfigure(boolean isConstrainedHighSpeed) throws android.hardware.camera2.CameraAccessException {
        try {
            mRemoteDevice.endConfigure(isConstrainedHighSpeed);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public void deleteStream(int streamId) throws android.hardware.camera2.CameraAccessException {
        try {
            mRemoteDevice.deleteStream(streamId);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public int createStream(android.hardware.camera2.params.OutputConfiguration outputConfiguration) throws android.hardware.camera2.CameraAccessException {
        try {
            return mRemoteDevice.createStream(outputConfiguration);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public int createInputStream(int width, int height, int format) throws android.hardware.camera2.CameraAccessException {
        try {
            return mRemoteDevice.createInputStream(width, height, format);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public android.view.Surface getInputSurface() throws android.hardware.camera2.CameraAccessException {
        try {
            return mRemoteDevice.getInputSurface();
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public android.hardware.camera2.impl.CameraMetadataNative createDefaultRequest(int templateId) throws android.hardware.camera2.CameraAccessException {
        try {
            return mRemoteDevice.createDefaultRequest(templateId);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public android.hardware.camera2.impl.CameraMetadataNative getCameraInfo() throws android.hardware.camera2.CameraAccessException {
        try {
            return mRemoteDevice.getCameraInfo();
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public void waitUntilIdle() throws android.hardware.camera2.CameraAccessException {
        try {
            mRemoteDevice.waitUntilIdle();
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public long flush() throws android.hardware.camera2.CameraAccessException {
        try {
            return mRemoteDevice.flush();
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public void prepare(int streamId) throws android.hardware.camera2.CameraAccessException {
        try {
            mRemoteDevice.prepare(streamId);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public void tearDown(int streamId) throws android.hardware.camera2.CameraAccessException {
        try {
            mRemoteDevice.tearDown(streamId);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public void prepare2(int maxCount, int streamId) throws android.hardware.camera2.CameraAccessException {
        try {
            mRemoteDevice.prepare2(maxCount, streamId);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public void setDeferredConfiguration(int streamId, android.hardware.camera2.params.OutputConfiguration deferredConfig) throws android.hardware.camera2.CameraAccessException {
        try {
            mRemoteDevice.setDeferredConfiguration(streamId, deferredConfig);
        } catch (java.lang.Throwable t) {
            android.hardware.camera2.CameraManager.throwAsPublicException(t);
            throw new java.lang.UnsupportedOperationException("Unexpected exception", t);
        }
    }
}

