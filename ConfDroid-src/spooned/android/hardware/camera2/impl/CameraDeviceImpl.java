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
package android.hardware.camera2.impl;


/**
 * HAL2.1+ implementation of CameraDevice. Use CameraManager#open to instantiate
 */
public class CameraDeviceImpl extends android.hardware.camera2.CameraDevice implements android.os.IBinder.DeathRecipient {
    private final java.lang.String TAG;

    private final boolean DEBUG = false;

    private static final int REQUEST_ID_NONE = -1;

    // TODO: guard every function with if (!mRemoteDevice) check (if it was closed)
    private android.hardware.camera2.impl.ICameraDeviceUserWrapper mRemoteDevice;

    // Lock to synchronize cross-thread access to device public interface
    final java.lang.Object mInterfaceLock = new java.lang.Object();// access from this class and Session only!


    private final android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks mCallbacks = new android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks();

    private final android.hardware.camera2.CameraDevice.StateCallback mDeviceCallback;

    private volatile android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK mSessionStateCallback;

    private final android.os.Handler mDeviceHandler;

    private final java.util.concurrent.atomic.AtomicBoolean mClosing = new java.util.concurrent.atomic.AtomicBoolean();

    private boolean mInError = false;

    private boolean mIdle = true;

    /**
     * map request IDs to callback/request data
     */
    private final android.util.SparseArray<android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallbackHolder> mCaptureCallbackMap = new android.util.SparseArray<android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallbackHolder>();

    private int mRepeatingRequestId = android.hardware.camera2.impl.CameraDeviceImpl.REQUEST_ID_NONE;

    // Map stream IDs to input/output configurations
    private java.util.AbstractMap.SimpleEntry<java.lang.Integer, android.hardware.camera2.params.InputConfiguration> mConfiguredInput = new java.util.AbstractMap.SimpleEntry<>(android.hardware.camera2.impl.CameraDeviceImpl.REQUEST_ID_NONE, null);

    private final android.util.SparseArray<android.hardware.camera2.params.OutputConfiguration> mConfiguredOutputs = new android.util.SparseArray<>();

    private final java.lang.String mCameraId;

    private final android.hardware.camera2.CameraCharacteristics mCharacteristics;

    private final int mTotalPartialCount;

    /**
     * A list tracking request and its expected last regular frame number and last reprocess frame
     * number. Updated when calling ICameraDeviceUser methods.
     */
    private final java.util.List<android.hardware.camera2.impl.CameraDeviceImpl.RequestLastFrameNumbersHolder> mRequestLastFrameNumbersList = new java.util.ArrayList<>();

    /**
     * An object tracking received frame numbers.
     * Updated when receiving callbacks from ICameraDeviceCallbacks.
     */
    private final android.hardware.camera2.impl.CameraDeviceImpl.FrameNumberTracker mFrameNumberTracker = new android.hardware.camera2.impl.CameraDeviceImpl.FrameNumberTracker();

    private android.hardware.camera2.impl.CameraCaptureSessionCore mCurrentSession;

    private int mNextSessionId = 0;

    // Runnables for all state transitions, except error, which needs the
    // error code argument
    private final java.lang.Runnable mCallOnOpened = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK sessionCallback = null;
            synchronized(mInterfaceLock) {
                if (mRemoteDevice == null)
                    return;
                // Camera already closed

                sessionCallback = mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onOpened(android.hardware.camera2.impl.CameraDeviceImpl.this);
            }
            mDeviceCallback.onOpened(android.hardware.camera2.impl.CameraDeviceImpl.this);
        }
    };

    private final java.lang.Runnable mCallOnUnconfigured = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK sessionCallback = null;
            synchronized(mInterfaceLock) {
                if (mRemoteDevice == null)
                    return;
                // Camera already closed

                sessionCallback = mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onUnconfigured(android.hardware.camera2.impl.CameraDeviceImpl.this);
            }
        }
    };

    private final java.lang.Runnable mCallOnActive = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK sessionCallback = null;
            synchronized(mInterfaceLock) {
                if (mRemoteDevice == null)
                    return;
                // Camera already closed

                sessionCallback = mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onActive(android.hardware.camera2.impl.CameraDeviceImpl.this);
            }
        }
    };

    private final java.lang.Runnable mCallOnBusy = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK sessionCallback = null;
            synchronized(mInterfaceLock) {
                if (mRemoteDevice == null)
                    return;
                // Camera already closed

                sessionCallback = mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onBusy(android.hardware.camera2.impl.CameraDeviceImpl.this);
            }
        }
    };

    private final java.lang.Runnable mCallOnClosed = new java.lang.Runnable() {
        private boolean mClosedOnce = false;

        @java.lang.Override
        public void run() {
            if (mClosedOnce) {
                throw new java.lang.AssertionError("Don't post #onClosed more than once");
            }
            android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK sessionCallback = null;
            synchronized(mInterfaceLock) {
                sessionCallback = mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onClosed(android.hardware.camera2.impl.CameraDeviceImpl.this);
            }
            mDeviceCallback.onClosed(android.hardware.camera2.impl.CameraDeviceImpl.this);
            mClosedOnce = true;
        }
    };

    private final java.lang.Runnable mCallOnIdle = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK sessionCallback = null;
            synchronized(mInterfaceLock) {
                if (mRemoteDevice == null)
                    return;
                // Camera already closed

                sessionCallback = mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onIdle(android.hardware.camera2.impl.CameraDeviceImpl.this);
            }
        }
    };

    private final java.lang.Runnable mCallOnDisconnected = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK sessionCallback = null;
            synchronized(mInterfaceLock) {
                if (mRemoteDevice == null)
                    return;
                // Camera already closed

                sessionCallback = mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onDisconnected(android.hardware.camera2.impl.CameraDeviceImpl.this);
            }
            mDeviceCallback.onDisconnected(android.hardware.camera2.impl.CameraDeviceImpl.this);
        }
    };

    public CameraDeviceImpl(java.lang.String cameraId, android.hardware.camera2.CameraDevice.StateCallback callback, android.os.Handler handler, android.hardware.camera2.CameraCharacteristics characteristics) {
        if ((((cameraId == null) || (callback == null)) || (handler == null)) || (characteristics == null)) {
            throw new java.lang.IllegalArgumentException("Null argument given");
        }
        mCameraId = cameraId;
        mDeviceCallback = callback;
        mDeviceHandler = handler;
        mCharacteristics = characteristics;
        final int MAX_TAG_LEN = 23;
        java.lang.String tag = java.lang.String.format("CameraDevice-JV-%s", mCameraId);
        if (tag.length() > MAX_TAG_LEN) {
            tag = tag.substring(0, MAX_TAG_LEN);
        }
        TAG = tag;
        java.lang.Integer partialCount = mCharacteristics.get(android.hardware.camera2.CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT);
        if (partialCount == null) {
            // 1 means partial result is not supported.
            mTotalPartialCount = 1;
        } else {
            mTotalPartialCount = partialCount;
        }
    }

    public android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks getCallbacks() {
        return mCallbacks;
    }

    /**
     * Set remote device, which triggers initial onOpened/onUnconfigured callbacks
     *
     * <p>This function may post onDisconnected and throw CAMERA_DISCONNECTED if remoteDevice dies
     * during setup.</p>
     */
    public void setRemoteDevice(android.hardware.camera2.ICameraDeviceUser remoteDevice) throws android.hardware.camera2.CameraAccessException {
        synchronized(mInterfaceLock) {
            // TODO: Move from decorator to direct binder-mediated exceptions
            // If setRemoteFailure already called, do nothing
            if (mInError)
                return;

            mRemoteDevice = new android.hardware.camera2.impl.ICameraDeviceUserWrapper(remoteDevice);
            android.os.IBinder remoteDeviceBinder = remoteDevice.asBinder();
            // For legacy camera device, remoteDevice is in the same process, and
            // asBinder returns NULL.
            if (remoteDeviceBinder != null) {
                try {
                    /* flag */
                    remoteDeviceBinder.linkToDeath(this, 0);
                } catch (android.os.RemoteException e) {
                    this.mDeviceHandler.post(mCallOnDisconnected);
                    throw new android.hardware.camera2.CameraAccessException(android.hardware.camera2.CameraAccessException.CAMERA_DISCONNECTED, "The camera device has encountered a serious error");
                }
            }
            mDeviceHandler.post(mCallOnOpened);
            mDeviceHandler.post(mCallOnUnconfigured);
        }
    }

    /**
     * Call to indicate failed connection to a remote camera device.
     *
     * <p>This places the camera device in the error state and informs the callback.
     * Use in place of setRemoteDevice() when startup fails.</p>
     */
    public void setRemoteFailure(final android.os.ServiceSpecificException failure) {
        int failureCode = android.hardware.camera2.CameraDevice.StateCallback.ERROR_CAMERA_DEVICE;
        boolean failureIsError = true;
        switch (failure.errorCode) {
            case android.hardware.ICameraService.ERROR_CAMERA_IN_USE :
                failureCode = android.hardware.camera2.CameraDevice.StateCallback.ERROR_CAMERA_IN_USE;
                break;
            case android.hardware.ICameraService.ERROR_MAX_CAMERAS_IN_USE :
                failureCode = android.hardware.camera2.CameraDevice.StateCallback.ERROR_MAX_CAMERAS_IN_USE;
                break;
            case android.hardware.ICameraService.ERROR_DISABLED :
                failureCode = android.hardware.camera2.CameraDevice.StateCallback.ERROR_CAMERA_DISABLED;
                break;
            case android.hardware.ICameraService.ERROR_DISCONNECTED :
                failureIsError = false;
                break;
            case android.hardware.ICameraService.ERROR_INVALID_OPERATION :
                failureCode = android.hardware.camera2.CameraDevice.StateCallback.ERROR_CAMERA_DEVICE;
                break;
            default :
                android.util.Log.e(TAG, ("Unexpected failure in opening camera device: " + failure.errorCode) + failure.getMessage());
                break;
        }
        final int code = failureCode;
        final boolean isError = failureIsError;
        synchronized(mInterfaceLock) {
            mInError = true;
            mDeviceHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (isError) {
                        mDeviceCallback.onError(android.hardware.camera2.impl.CameraDeviceImpl.this, code);
                    } else {
                        mDeviceCallback.onDisconnected(android.hardware.camera2.impl.CameraDeviceImpl.this);
                    }
                }
            });
        }
    }

    @java.lang.Override
    public java.lang.String getId() {
        return mCameraId;
    }

    public void configureOutputs(java.util.List<android.view.Surface> outputs) throws android.hardware.camera2.CameraAccessException {
        // Leave this here for backwards compatibility with older code using this directly
        java.util.ArrayList<android.hardware.camera2.params.OutputConfiguration> outputConfigs = new java.util.ArrayList<>(outputs.size());
        for (android.view.Surface s : outputs) {
            outputConfigs.add(new android.hardware.camera2.params.OutputConfiguration(s));
        }
        /* inputConfig */
        /* isConstrainedHighSpeed */
        configureStreamsChecked(null, outputConfigs, false);
    }

    /**
     * Attempt to configure the input and outputs; the device goes to idle and then configures the
     * new input and outputs if possible.
     *
     * <p>The configuration may gracefully fail, if input configuration is not supported,
     * if there are too many outputs, if the formats are not supported, or if the sizes for that
     * format is not supported. In this case this function will return {@code false} and the
     * unconfigured callback will be fired.</p>
     *
     * <p>If the configuration succeeds (with 1 or more outputs with or without an input),
     * then the idle callback is fired. Unconfiguring the device always fires the idle callback.</p>
     *
     * @param inputConfig
     * 		input configuration or {@code null} for no input
     * @param outputs
     * 		a list of one or more surfaces, or {@code null} to unconfigure
     * @param isConstrainedHighSpeed
     * 		If the streams configuration is for constrained high speed output.
     * @return whether or not the configuration was successful
     * @throws CameraAccessException
     * 		if there were any unexpected problems during configuration
     */
    public boolean configureStreamsChecked(android.hardware.camera2.params.InputConfiguration inputConfig, java.util.List<android.hardware.camera2.params.OutputConfiguration> outputs, boolean isConstrainedHighSpeed) throws android.hardware.camera2.CameraAccessException {
        // Treat a null input the same an empty list
        if (outputs == null) {
            outputs = new java.util.ArrayList<android.hardware.camera2.params.OutputConfiguration>();
        }
        if ((outputs.size() == 0) && (inputConfig != null)) {
            throw new java.lang.IllegalArgumentException("cannot configure an input stream without " + "any output streams");
        }
        checkInputConfiguration(inputConfig);
        boolean success = false;
        synchronized(mInterfaceLock) {
            checkIfCameraClosedOrInError();
            // Streams to create
            java.util.HashSet<android.hardware.camera2.params.OutputConfiguration> addSet = new java.util.HashSet<android.hardware.camera2.params.OutputConfiguration>(outputs);
            // Streams to delete
            java.util.List<java.lang.Integer> deleteList = new java.util.ArrayList<java.lang.Integer>();
            // Determine which streams need to be created, which to be deleted
            for (int i = 0; i < mConfiguredOutputs.size(); ++i) {
                int streamId = mConfiguredOutputs.keyAt(i);
                android.hardware.camera2.params.OutputConfiguration outConfig = mConfiguredOutputs.valueAt(i);
                if ((!outputs.contains(outConfig)) || outConfig.isDeferredConfiguration()) {
                    // Always delete the deferred output configuration when the session
                    // is created, as the deferred output configuration doesn't have unique surface
                    // related identifies.
                    deleteList.add(streamId);
                } else {
                    addSet.remove(outConfig);// Don't create a stream previously created

                }
            }
            mDeviceHandler.post(mCallOnBusy);
            stopRepeating();
            try {
                waitUntilIdle();
                mRemoteDevice.beginConfigure();
                // reconfigure the input stream if the input configuration is different.
                android.hardware.camera2.params.InputConfiguration currentInputConfig = mConfiguredInput.getValue();
                if ((inputConfig != currentInputConfig) && ((inputConfig == null) || (!inputConfig.equals(currentInputConfig)))) {
                    if (currentInputConfig != null) {
                        mRemoteDevice.deleteStream(mConfiguredInput.getKey());
                        mConfiguredInput = new java.util.AbstractMap.SimpleEntry<java.lang.Integer, android.hardware.camera2.params.InputConfiguration>(android.hardware.camera2.impl.CameraDeviceImpl.REQUEST_ID_NONE, null);
                    }
                    if (inputConfig != null) {
                        int streamId = mRemoteDevice.createInputStream(inputConfig.getWidth(), inputConfig.getHeight(), inputConfig.getFormat());
                        mConfiguredInput = new java.util.AbstractMap.SimpleEntry<java.lang.Integer, android.hardware.camera2.params.InputConfiguration>(streamId, inputConfig);
                    }
                }
                // Delete all streams first (to free up HW resources)
                for (java.lang.Integer streamId : deleteList) {
                    mRemoteDevice.deleteStream(streamId);
                    mConfiguredOutputs.delete(streamId);
                }
                // Add all new streams
                for (android.hardware.camera2.params.OutputConfiguration outConfig : outputs) {
                    if (addSet.contains(outConfig)) {
                        int streamId = mRemoteDevice.createStream(outConfig);
                        mConfiguredOutputs.put(streamId, outConfig);
                    }
                }
                mRemoteDevice.endConfigure(isConstrainedHighSpeed);
                success = true;
            } catch (java.lang.IllegalArgumentException e) {
                // OK. camera service can reject stream config if it's not supported by HAL
                // This is only the result of a programmer misusing the camera2 api.
                android.util.Log.w(TAG, "Stream configuration failed due to: " + e.getMessage());
                return false;
            } catch (android.hardware.camera2.CameraAccessException e) {
                if (e.getReason() == android.hardware.camera2.CameraAccessException.CAMERA_IN_USE) {
                    throw new java.lang.IllegalStateException("The camera is currently busy." + " You must wait until the previous operation completes.", e);
                }
                throw e;
            } finally {
                if (success && (outputs.size() > 0)) {
                    mDeviceHandler.post(mCallOnIdle);
                } else {
                    // Always return to the 'unconfigured' state if we didn't hit a fatal error
                    mDeviceHandler.post(mCallOnUnconfigured);
                }
            }
        }
        return success;
    }

    @java.lang.Override
    public void createCaptureSession(java.util.List<android.view.Surface> outputs, android.hardware.camera2.CameraCaptureSession.StateCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        java.util.List<android.hardware.camera2.params.OutputConfiguration> outConfigurations = new java.util.ArrayList<>(outputs.size());
        for (android.view.Surface surface : outputs) {
            outConfigurations.add(new android.hardware.camera2.params.OutputConfiguration(surface));
        }
        /* isConstrainedHighSpeed */
        createCaptureSessionInternal(null, outConfigurations, callback, handler, false);
    }

    @java.lang.Override
    public void createCaptureSessionByOutputConfigurations(java.util.List<android.hardware.camera2.params.OutputConfiguration> outputConfigurations, android.hardware.camera2.CameraCaptureSession.StateCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if (DEBUG) {
            android.util.Log.d(TAG, "createCaptureSessionByOutputConfiguration");
        }
        // OutputConfiguration objects are immutable, but need to have our own array
        java.util.List<android.hardware.camera2.params.OutputConfiguration> currentOutputs = new java.util.ArrayList<>(outputConfigurations);
        /* isConstrainedHighSpeed */
        createCaptureSessionInternal(null, currentOutputs, callback, handler, false);
    }

    @java.lang.Override
    public void createReprocessableCaptureSession(android.hardware.camera2.params.InputConfiguration inputConfig, java.util.List<android.view.Surface> outputs, android.hardware.camera2.CameraCaptureSession.StateCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if (DEBUG) {
            android.util.Log.d(TAG, "createReprocessableCaptureSession");
        }
        if (inputConfig == null) {
            throw new java.lang.IllegalArgumentException("inputConfig cannot be null when creating a " + "reprocessable capture session");
        }
        java.util.List<android.hardware.camera2.params.OutputConfiguration> outConfigurations = new java.util.ArrayList<>(outputs.size());
        for (android.view.Surface surface : outputs) {
            outConfigurations.add(new android.hardware.camera2.params.OutputConfiguration(surface));
        }
        /* isConstrainedHighSpeed */
        createCaptureSessionInternal(inputConfig, outConfigurations, callback, handler, false);
    }

    @java.lang.Override
    public void createReprocessableCaptureSessionByConfigurations(android.hardware.camera2.params.InputConfiguration inputConfig, java.util.List<android.hardware.camera2.params.OutputConfiguration> outputs, android.hardware.camera2.CameraCaptureSession.StateCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if (DEBUG) {
            android.util.Log.d(TAG, "createReprocessableCaptureSessionWithConfigurations");
        }
        if (inputConfig == null) {
            throw new java.lang.IllegalArgumentException("inputConfig cannot be null when creating a " + "reprocessable capture session");
        }
        if (outputs == null) {
            throw new java.lang.IllegalArgumentException("Output configurations cannot be null when " + "creating a reprocessable capture session");
        }
        // OutputConfiguration objects aren't immutable, make a copy before using.
        java.util.List<android.hardware.camera2.params.OutputConfiguration> currentOutputs = new java.util.ArrayList<android.hardware.camera2.params.OutputConfiguration>();
        for (android.hardware.camera2.params.OutputConfiguration output : outputs) {
            currentOutputs.add(new android.hardware.camera2.params.OutputConfiguration(output));
        }
        /* isConstrainedHighSpeed */
        createCaptureSessionInternal(inputConfig, currentOutputs, callback, handler, false);
    }

    @java.lang.Override
    public void createConstrainedHighSpeedCaptureSession(java.util.List<android.view.Surface> outputs, android.hardware.camera2.CameraCaptureSession.StateCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if (((outputs == null) || (outputs.size() == 0)) || (outputs.size() > 2)) {
            throw new java.lang.IllegalArgumentException("Output surface list must not be null and the size must be no more than 2");
        }
        android.hardware.camera2.params.StreamConfigurationMap config = getCharacteristics().get(android.hardware.camera2.CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        /* fpsRange */
        android.hardware.camera2.utils.SurfaceUtils.checkConstrainedHighSpeedSurfaces(outputs, null, config);
        java.util.List<android.hardware.camera2.params.OutputConfiguration> outConfigurations = new java.util.ArrayList<>(outputs.size());
        for (android.view.Surface surface : outputs) {
            outConfigurations.add(new android.hardware.camera2.params.OutputConfiguration(surface));
        }
        /* isConstrainedHighSpeed */
        createCaptureSessionInternal(null, outConfigurations, callback, handler, true);
    }

    private void createCaptureSessionInternal(android.hardware.camera2.params.InputConfiguration inputConfig, java.util.List<android.hardware.camera2.params.OutputConfiguration> outputConfigurations, android.hardware.camera2.CameraCaptureSession.StateCallback callback, android.os.Handler handler, boolean isConstrainedHighSpeed) throws android.hardware.camera2.CameraAccessException {
        synchronized(mInterfaceLock) {
            if (DEBUG) {
                android.util.Log.d(TAG, "createCaptureSessionInternal");
            }
            checkIfCameraClosedOrInError();
            if (isConstrainedHighSpeed && (inputConfig != null)) {
                throw new java.lang.IllegalArgumentException("Constrained high speed session doesn't support" + " input configuration yet.");
            }
            // Notify current session that it's going away, before starting camera operations
            // After this call completes, the session is not allowed to call into CameraDeviceImpl
            if (mCurrentSession != null) {
                mCurrentSession.replaceSessionClose();
            }
            // TODO: dont block for this
            boolean configureSuccess = true;
            android.hardware.camera2.CameraAccessException pendingException = null;
            android.view.Surface input = null;
            try {
                // configure streams and then block until IDLE
                configureSuccess = configureStreamsChecked(inputConfig, outputConfigurations, isConstrainedHighSpeed);
                if ((configureSuccess == true) && (inputConfig != null)) {
                    input = mRemoteDevice.getInputSurface();
                }
            } catch (android.hardware.camera2.CameraAccessException e) {
                configureSuccess = false;
                pendingException = e;
                input = null;
                if (DEBUG) {
                    android.util.Log.v(TAG, "createCaptureSession - failed with exception ", e);
                }
            }
            java.util.List<android.view.Surface> outSurfaces = new java.util.ArrayList<>(outputConfigurations.size());
            for (android.hardware.camera2.params.OutputConfiguration config : outputConfigurations) {
                outSurfaces.add(config.getSurface());
            }
            // Fire onConfigured if configureOutputs succeeded, fire onConfigureFailed otherwise.
            android.hardware.camera2.impl.CameraCaptureSessionCore newSession = null;
            if (isConstrainedHighSpeed) {
                newSession = new android.hardware.camera2.impl.CameraConstrainedHighSpeedCaptureSessionImpl(mNextSessionId++, outSurfaces, callback, handler, this, mDeviceHandler, configureSuccess, mCharacteristics);
            } else {
                newSession = new android.hardware.camera2.impl.CameraCaptureSessionImpl(mNextSessionId++, input, outSurfaces, callback, handler, this, mDeviceHandler, configureSuccess);
            }
            // TODO: wait until current session closes, then create the new session
            mCurrentSession = newSession;
            if (pendingException != null) {
                throw pendingException;
            }
            mSessionStateCallback = mCurrentSession.getDeviceStateCallback();
        }
    }

    /**
     * For use by backwards-compatibility code only.
     */
    public void setSessionListener(android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK sessionCallback) {
        synchronized(mInterfaceLock) {
            mSessionStateCallback = sessionCallback;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.CaptureRequest.Builder createCaptureRequest(int templateType) throws android.hardware.camera2.CameraAccessException {
        synchronized(mInterfaceLock) {
            checkIfCameraClosedOrInError();
            android.hardware.camera2.impl.CameraMetadataNative templatedRequest = null;
            templatedRequest = mRemoteDevice.createDefaultRequest(templateType);
            android.hardware.camera2.CaptureRequest.Builder builder = /* reprocess */
            new android.hardware.camera2.CaptureRequest.Builder(templatedRequest, false, android.hardware.camera2.CameraCaptureSession.SESSION_ID_NONE);
            return builder;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.CaptureRequest.Builder createReprocessCaptureRequest(android.hardware.camera2.TotalCaptureResult inputResult) throws android.hardware.camera2.CameraAccessException {
        synchronized(mInterfaceLock) {
            checkIfCameraClosedOrInError();
            android.hardware.camera2.impl.CameraMetadataNative resultMetadata = new android.hardware.camera2.impl.CameraMetadataNative(inputResult.getNativeCopy());
            return /* reprocess */
            new android.hardware.camera2.CaptureRequest.Builder(resultMetadata, true, inputResult.getSessionId());
        }
    }

    public void prepare(android.view.Surface surface) throws android.hardware.camera2.CameraAccessException {
        if (surface == null)
            throw new java.lang.IllegalArgumentException("Surface is null");

        synchronized(mInterfaceLock) {
            int streamId = -1;
            for (int i = 0; i < mConfiguredOutputs.size(); i++) {
                if (surface == mConfiguredOutputs.valueAt(i).getSurface()) {
                    streamId = mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == (-1)) {
                throw new java.lang.IllegalArgumentException("Surface is not part of this session");
            }
            mRemoteDevice.prepare(streamId);
        }
    }

    public void prepare(int maxCount, android.view.Surface surface) throws android.hardware.camera2.CameraAccessException {
        if (surface == null)
            throw new java.lang.IllegalArgumentException("Surface is null");

        if (maxCount <= 0)
            throw new java.lang.IllegalArgumentException("Invalid maxCount given: " + maxCount);

        synchronized(mInterfaceLock) {
            int streamId = -1;
            for (int i = 0; i < mConfiguredOutputs.size(); i++) {
                if (surface == mConfiguredOutputs.valueAt(i).getSurface()) {
                    streamId = mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == (-1)) {
                throw new java.lang.IllegalArgumentException("Surface is not part of this session");
            }
            mRemoteDevice.prepare2(maxCount, streamId);
        }
    }

    public void tearDown(android.view.Surface surface) throws android.hardware.camera2.CameraAccessException {
        if (surface == null)
            throw new java.lang.IllegalArgumentException("Surface is null");

        synchronized(mInterfaceLock) {
            int streamId = -1;
            for (int i = 0; i < mConfiguredOutputs.size(); i++) {
                if (surface == mConfiguredOutputs.valueAt(i).getSurface()) {
                    streamId = mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == (-1)) {
                throw new java.lang.IllegalArgumentException("Surface is not part of this session");
            }
            mRemoteDevice.tearDown(streamId);
        }
    }

    public void finishDeferredConfig(java.util.List<android.hardware.camera2.params.OutputConfiguration> deferredConfigs) throws android.hardware.camera2.CameraAccessException {
        if ((deferredConfigs == null) || (deferredConfigs.size() == 0)) {
            throw new java.lang.IllegalArgumentException("deferred config is null or empty");
        }
        synchronized(mInterfaceLock) {
            for (android.hardware.camera2.params.OutputConfiguration config : deferredConfigs) {
                int streamId = -1;
                for (int i = 0; i < mConfiguredOutputs.size(); i++) {
                    // Have to use equal here, as createCaptureSessionByOutputConfigurations() and
                    // createReprocessableCaptureSessionByConfigurations() do a copy of the configs.
                    if (config.equals(mConfiguredOutputs.valueAt(i))) {
                        streamId = mConfiguredOutputs.keyAt(i);
                        break;
                    }
                }
                if (streamId == (-1)) {
                    throw new java.lang.IllegalArgumentException("Deferred config is not part of this " + "session");
                }
                if (config.getSurface() == null) {
                    throw new java.lang.IllegalArgumentException(("The deferred config for stream " + streamId) + " must have a non-null surface");
                }
                mRemoteDevice.setDeferredConfiguration(streamId, config);
            }
        }
    }

    public int capture(android.hardware.camera2.CaptureRequest request, android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if (DEBUG) {
            android.util.Log.d(TAG, "calling capture");
        }
        java.util.List<android.hardware.camera2.CaptureRequest> requestList = new java.util.ArrayList<android.hardware.camera2.CaptureRequest>();
        requestList.add(request);
        return /* streaming */
        submitCaptureRequest(requestList, callback, handler, false);
    }

    public int captureBurst(java.util.List<android.hardware.camera2.CaptureRequest> requests, android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if ((requests == null) || requests.isEmpty()) {
            throw new java.lang.IllegalArgumentException("At least one request must be given");
        }
        return /* streaming */
        submitCaptureRequest(requests, callback, handler, false);
    }

    /**
     * This method checks lastFrameNumber returned from ICameraDeviceUser methods for
     * starting and stopping repeating request and flushing.
     *
     * <p>If lastFrameNumber is NO_FRAMES_CAPTURED, it means that the request was never
     * sent to HAL. Then onCaptureSequenceAborted is immediately triggered.
     * If lastFrameNumber is non-negative, then the requestId and lastFrameNumber as the last
     * regular frame number will be added to the list mRequestLastFrameNumbersList.</p>
     *
     * @param requestId
     * 		the request ID of the current repeating request.
     * @param lastFrameNumber
     * 		last frame number returned from binder.
     */
    private void checkEarlyTriggerSequenceComplete(final int requestId, final long lastFrameNumber) {
        // lastFrameNumber being equal to NO_FRAMES_CAPTURED means that the request
        // was never sent to HAL. Should trigger onCaptureSequenceAborted immediately.
        if (lastFrameNumber == android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback.NO_FRAMES_CAPTURED) {
            final android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallbackHolder holder;
            int index = mCaptureCallbackMap.indexOfKey(requestId);
            holder = (index >= 0) ? mCaptureCallbackMap.valueAt(index) : null;
            if (holder != null) {
                mCaptureCallbackMap.removeAt(index);
                if (DEBUG) {
                    android.util.Log.v(TAG, java.lang.String.format("remove holder for requestId %d, " + "because lastFrame is %d.", requestId, lastFrameNumber));
                }
            }
            if (holder != null) {
                if (DEBUG) {
                    android.util.Log.v(TAG, "immediately trigger onCaptureSequenceAborted because" + " request did not reach HAL");
                }
                java.lang.Runnable resultDispatch = new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (!android.hardware.camera2.impl.CameraDeviceImpl.this.isClosed()) {
                            if (DEBUG) {
                                android.util.Log.d(TAG, java.lang.String.format("early trigger sequence complete for request %d", requestId));
                            }
                            holder.getCallback().onCaptureSequenceAborted(android.hardware.camera2.impl.CameraDeviceImpl.this, requestId);
                        }
                    }
                };
                holder.getHandler().post(resultDispatch);
            } else {
                android.util.Log.w(TAG, java.lang.String.format("did not register callback to request %d", requestId));
            }
        } else {
            // This function is only called for regular request so lastFrameNumber is the last
            // regular frame number.
            mRequestLastFrameNumbersList.add(new android.hardware.camera2.impl.CameraDeviceImpl.RequestLastFrameNumbersHolder(requestId, lastFrameNumber));
            // It is possible that the last frame has already arrived, so we need to check
            // for sequence completion right away
            checkAndFireSequenceComplete();
        }
    }

    private int submitCaptureRequest(java.util.List<android.hardware.camera2.CaptureRequest> requestList, android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback callback, android.os.Handler handler, boolean repeating) throws android.hardware.camera2.CameraAccessException {
        // Need a valid handler, or current thread needs to have a looper, if
        // callback is valid
        handler = android.hardware.camera2.impl.CameraDeviceImpl.checkHandler(handler, callback);
        // Make sure that there all requests have at least 1 surface; all surfaces are non-null
        for (android.hardware.camera2.CaptureRequest request : requestList) {
            if (request.getTargets().isEmpty()) {
                throw new java.lang.IllegalArgumentException("Each request must have at least one Surface target");
            }
            for (android.view.Surface surface : request.getTargets()) {
                if (surface == null) {
                    throw new java.lang.IllegalArgumentException("Null Surface targets are not allowed");
                }
            }
        }
        synchronized(mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (repeating) {
                stopRepeating();
            }
            android.hardware.camera2.utils.SubmitInfo requestInfo;
            android.hardware.camera2.CaptureRequest[] requestArray = requestList.toArray(new android.hardware.camera2.CaptureRequest[requestList.size()]);
            requestInfo = mRemoteDevice.submitRequestList(requestArray, repeating);
            if (DEBUG) {
                android.util.Log.v(TAG, "last frame number " + requestInfo.getLastFrameNumber());
            }
            if (callback != null) {
                mCaptureCallbackMap.put(requestInfo.getRequestId(), new android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallbackHolder(callback, requestList, handler, repeating, mNextSessionId - 1));
            } else {
                if (DEBUG) {
                    android.util.Log.d(TAG, ("Listen for request " + requestInfo.getRequestId()) + " is null");
                }
            }
            if (repeating) {
                if (mRepeatingRequestId != android.hardware.camera2.impl.CameraDeviceImpl.REQUEST_ID_NONE) {
                    checkEarlyTriggerSequenceComplete(mRepeatingRequestId, requestInfo.getLastFrameNumber());
                }
                mRepeatingRequestId = requestInfo.getRequestId();
            } else {
                mRequestLastFrameNumbersList.add(new android.hardware.camera2.impl.CameraDeviceImpl.RequestLastFrameNumbersHolder(requestList, requestInfo));
            }
            if (mIdle) {
                mDeviceHandler.post(mCallOnActive);
            }
            mIdle = false;
            return requestInfo.getRequestId();
        }
    }

    public int setRepeatingRequest(android.hardware.camera2.CaptureRequest request, android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        java.util.List<android.hardware.camera2.CaptureRequest> requestList = new java.util.ArrayList<android.hardware.camera2.CaptureRequest>();
        requestList.add(request);
        return /* streaming */
        submitCaptureRequest(requestList, callback, handler, true);
    }

    public int setRepeatingBurst(java.util.List<android.hardware.camera2.CaptureRequest> requests, android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if ((requests == null) || requests.isEmpty()) {
            throw new java.lang.IllegalArgumentException("At least one request must be given");
        }
        return /* streaming */
        submitCaptureRequest(requests, callback, handler, true);
    }

    public void stopRepeating() throws android.hardware.camera2.CameraAccessException {
        synchronized(mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (mRepeatingRequestId != android.hardware.camera2.impl.CameraDeviceImpl.REQUEST_ID_NONE) {
                int requestId = mRepeatingRequestId;
                mRepeatingRequestId = android.hardware.camera2.impl.CameraDeviceImpl.REQUEST_ID_NONE;
                long lastFrameNumber;
                try {
                    lastFrameNumber = mRemoteDevice.cancelRequest(requestId);
                } catch (java.lang.IllegalArgumentException e) {
                    if (DEBUG) {
                        android.util.Log.v(TAG, "Repeating request was already stopped for request " + requestId);
                    }
                    // Repeating request was already stopped. Nothing more to do.
                    return;
                }
                checkEarlyTriggerSequenceComplete(requestId, lastFrameNumber);
            }
        }
    }

    private void waitUntilIdle() throws android.hardware.camera2.CameraAccessException {
        synchronized(mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (mRepeatingRequestId != android.hardware.camera2.impl.CameraDeviceImpl.REQUEST_ID_NONE) {
                throw new java.lang.IllegalStateException("Active repeating request ongoing");
            }
            mRemoteDevice.waitUntilIdle();
        }
    }

    public void flush() throws android.hardware.camera2.CameraAccessException {
        synchronized(mInterfaceLock) {
            checkIfCameraClosedOrInError();
            mDeviceHandler.post(mCallOnBusy);
            // If already idle, just do a busy->idle transition immediately, don't actually
            // flush.
            if (mIdle) {
                mDeviceHandler.post(mCallOnIdle);
                return;
            }
            long lastFrameNumber = mRemoteDevice.flush();
            if (mRepeatingRequestId != android.hardware.camera2.impl.CameraDeviceImpl.REQUEST_ID_NONE) {
                checkEarlyTriggerSequenceComplete(mRepeatingRequestId, lastFrameNumber);
                mRepeatingRequestId = android.hardware.camera2.impl.CameraDeviceImpl.REQUEST_ID_NONE;
            }
        }
    }

    @java.lang.Override
    public void close() {
        synchronized(mInterfaceLock) {
            if (mClosing.getAndSet(true)) {
                return;
            }
            if (mRemoteDevice != null) {
                mRemoteDevice.disconnect();
                /* flags */
                mRemoteDevice.unlinkToDeath(this, 0);
            }
            // Only want to fire the onClosed callback once;
            // either a normal close where the remote device is valid
            // or a close after a startup error (no remote device but in error state)
            if ((mRemoteDevice != null) || mInError) {
                mDeviceHandler.post(mCallOnClosed);
            }
            mRemoteDevice = null;
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private void checkInputConfiguration(android.hardware.camera2.params.InputConfiguration inputConfig) {
        if (inputConfig != null) {
            android.hardware.camera2.params.StreamConfigurationMap configMap = mCharacteristics.get(android.hardware.camera2.CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            int[] inputFormats = configMap.getInputFormats();
            boolean validFormat = false;
            for (int format : inputFormats) {
                if (format == inputConfig.getFormat()) {
                    validFormat = true;
                }
            }
            if (validFormat == false) {
                throw new java.lang.IllegalArgumentException(("input format " + inputConfig.getFormat()) + " is not valid");
            }
            boolean validSize = false;
            android.util.Size[] inputSizes = configMap.getInputSizes(inputConfig.getFormat());
            for (android.util.Size s : inputSizes) {
                if ((inputConfig.getWidth() == s.getWidth()) && (inputConfig.getHeight() == s.getHeight())) {
                    validSize = true;
                }
            }
            if (validSize == false) {
                throw new java.lang.IllegalArgumentException(((("input size " + inputConfig.getWidth()) + "x") + inputConfig.getHeight()) + " is not valid");
            }
        }
    }

    /**
     * <p>A callback for tracking the progress of a {@link CaptureRequest}
     * submitted to the camera device.</p>
     */
    public static abstract class CaptureCallback {
        /**
         * This constant is used to indicate that no images were captured for
         * the request.
         *
         * @unknown 
         */
        public static final int NO_FRAMES_CAPTURED = -1;

        /**
         * This method is called when the camera device has started capturing
         * the output image for the request, at the beginning of image exposure.
         *
         * @see android.media.MediaActionSound
         */
        public void onCaptureStarted(android.hardware.camera2.CameraDevice camera, android.hardware.camera2.CaptureRequest request, long timestamp, long frameNumber) {
            // default empty implementation
        }

        /**
         * This method is called when some results from an image capture are
         * available.
         *
         * @unknown 
         */
        public void onCapturePartial(android.hardware.camera2.CameraDevice camera, android.hardware.camera2.CaptureRequest request, android.hardware.camera2.CaptureResult result) {
            // default empty implementation
        }

        /**
         * This method is called when an image capture makes partial forward progress; some
         * (but not all) results from an image capture are available.
         */
        public void onCaptureProgressed(android.hardware.camera2.CameraDevice camera, android.hardware.camera2.CaptureRequest request, android.hardware.camera2.CaptureResult partialResult) {
            // default empty implementation
        }

        /**
         * This method is called when an image capture has fully completed and all the
         * result metadata is available.
         */
        public void onCaptureCompleted(android.hardware.camera2.CameraDevice camera, android.hardware.camera2.CaptureRequest request, android.hardware.camera2.TotalCaptureResult result) {
            // default empty implementation
        }

        /**
         * This method is called instead of {@link #onCaptureCompleted} when the
         * camera device failed to produce a {@link CaptureResult} for the
         * request.
         */
        public void onCaptureFailed(android.hardware.camera2.CameraDevice camera, android.hardware.camera2.CaptureRequest request, android.hardware.camera2.CaptureFailure failure) {
            // default empty implementation
        }

        /**
         * This method is called independently of the others in CaptureCallback,
         * when a capture sequence finishes and all {@link CaptureResult}
         * or {@link CaptureFailure} for it have been returned via this callback.
         */
        public void onCaptureSequenceCompleted(android.hardware.camera2.CameraDevice camera, int sequenceId, long frameNumber) {
            // default empty implementation
        }

        /**
         * This method is called independently of the others in CaptureCallback,
         * when a capture sequence aborts before any {@link CaptureResult}
         * or {@link CaptureFailure} for it have been returned via this callback.
         */
        public void onCaptureSequenceAborted(android.hardware.camera2.CameraDevice camera, int sequenceId) {
            // default empty implementation
        }

        public void onCaptureBufferLost(android.hardware.camera2.CameraDevice camera, android.hardware.camera2.CaptureRequest request, android.view.Surface target, long frameNumber) {
            // default empty implementation
        }
    }

    /**
     * A callback for notifications about the state of a camera device, adding in the callbacks that
     * were part of the earlier KK API design, but now only used internally.
     */
    public static abstract class StateCallbackKK extends android.hardware.camera2.CameraDevice.StateCallback {
        /**
         * The method called when a camera device has no outputs configured.
         */
        public void onUnconfigured(android.hardware.camera2.CameraDevice camera) {
            // Default empty implementation
        }

        /**
         * The method called when a camera device begins processing
         * {@link CaptureRequest capture requests}.
         */
        public void onActive(android.hardware.camera2.CameraDevice camera) {
            // Default empty implementation
        }

        /**
         * The method called when a camera device is busy.
         */
        public void onBusy(android.hardware.camera2.CameraDevice camera) {
            // Default empty implementation
        }

        /**
         * The method called when a camera device has finished processing all
         * submitted capture requests and has reached an idle state.
         */
        public void onIdle(android.hardware.camera2.CameraDevice camera) {
            // Default empty implementation
        }

        /**
         * The method called when the camera device has finished preparing
         * an output Surface
         */
        public void onSurfacePrepared(android.view.Surface surface) {
            // Default empty implementation
        }
    }

    static class CaptureCallbackHolder {
        private final boolean mRepeating;

        private final android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback mCallback;

        private final java.util.List<android.hardware.camera2.CaptureRequest> mRequestList;

        private final android.os.Handler mHandler;

        private final int mSessionId;

        CaptureCallbackHolder(android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback callback, java.util.List<android.hardware.camera2.CaptureRequest> requestList, android.os.Handler handler, boolean repeating, int sessionId) {
            if ((callback == null) || (handler == null)) {
                throw new java.lang.UnsupportedOperationException("Must have a valid handler and a valid callback");
            }
            mRepeating = repeating;
            mHandler = handler;
            mRequestList = new java.util.ArrayList<android.hardware.camera2.CaptureRequest>(requestList);
            mCallback = callback;
            mSessionId = sessionId;
        }

        public boolean isRepeating() {
            return mRepeating;
        }

        public android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback getCallback() {
            return mCallback;
        }

        public android.hardware.camera2.CaptureRequest getRequest(int subsequenceId) {
            if (subsequenceId >= mRequestList.size()) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Requested subsequenceId %d is larger than request list size %d.", subsequenceId, mRequestList.size()));
            } else {
                if (subsequenceId < 0) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Requested subsequenceId %d is negative", subsequenceId));
                } else {
                    return mRequestList.get(subsequenceId);
                }
            }
        }

        public android.hardware.camera2.CaptureRequest getRequest() {
            return getRequest(0);
        }

        public android.os.Handler getHandler() {
            return mHandler;
        }

        public int getSessionId() {
            return mSessionId;
        }
    }

    /**
     * This class holds a capture ID and its expected last regular frame number and last reprocess
     * frame number.
     */
    static class RequestLastFrameNumbersHolder {
        // request ID
        private final int mRequestId;

        // The last regular frame number for this request ID. It's
        // CaptureCallback.NO_FRAMES_CAPTURED if the request ID has no regular request.
        private final long mLastRegularFrameNumber;

        // The last reprocess frame number for this request ID. It's
        // CaptureCallback.NO_FRAMES_CAPTURED if the request ID has no reprocess request.
        private final long mLastReprocessFrameNumber;

        /**
         * Create a request-last-frame-numbers holder with a list of requests, request ID, and
         * the last frame number returned by camera service.
         */
        public RequestLastFrameNumbersHolder(java.util.List<android.hardware.camera2.CaptureRequest> requestList, android.hardware.camera2.utils.SubmitInfo requestInfo) {
            long lastRegularFrameNumber = android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback.NO_FRAMES_CAPTURED;
            long lastReprocessFrameNumber = android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback.NO_FRAMES_CAPTURED;
            long frameNumber = requestInfo.getLastFrameNumber();
            if (requestInfo.getLastFrameNumber() < (requestList.size() - 1)) {
                throw new java.lang.IllegalArgumentException(((((("lastFrameNumber: " + requestInfo.getLastFrameNumber()) + " should be at least ") + (requestList.size() - 1)) + " for the number of ") + " requests in the list: ") + requestList.size());
            }
            // find the last regular frame number and the last reprocess frame number
            for (int i = requestList.size() - 1; i >= 0; i--) {
                android.hardware.camera2.CaptureRequest request = requestList.get(i);
                if (request.isReprocess() && (lastReprocessFrameNumber == android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback.NO_FRAMES_CAPTURED)) {
                    lastReprocessFrameNumber = frameNumber;
                } else
                    if ((!request.isReprocess()) && (lastRegularFrameNumber == android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback.NO_FRAMES_CAPTURED)) {
                        lastRegularFrameNumber = frameNumber;
                    }

                if ((lastReprocessFrameNumber != android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback.NO_FRAMES_CAPTURED) && (lastRegularFrameNumber != android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback.NO_FRAMES_CAPTURED)) {
                    break;
                }
                frameNumber--;
            }
            mLastRegularFrameNumber = lastRegularFrameNumber;
            mLastReprocessFrameNumber = lastReprocessFrameNumber;
            mRequestId = requestInfo.getRequestId();
        }

        /**
         * Create a request-last-frame-numbers holder with a request ID and last regular frame
         * number.
         */
        public RequestLastFrameNumbersHolder(int requestId, long lastRegularFrameNumber) {
            mLastRegularFrameNumber = lastRegularFrameNumber;
            mLastReprocessFrameNumber = android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback.NO_FRAMES_CAPTURED;
            mRequestId = requestId;
        }

        /**
         * Return the last regular frame number. Return CaptureCallback.NO_FRAMES_CAPTURED if
         * it contains no regular request.
         */
        public long getLastRegularFrameNumber() {
            return mLastRegularFrameNumber;
        }

        /**
         * Return the last reprocess frame number. Return CaptureCallback.NO_FRAMES_CAPTURED if
         * it contains no reprocess request.
         */
        public long getLastReprocessFrameNumber() {
            return mLastReprocessFrameNumber;
        }

        /**
         * Return the last frame number overall.
         */
        public long getLastFrameNumber() {
            return java.lang.Math.max(mLastRegularFrameNumber, mLastReprocessFrameNumber);
        }

        /**
         * Return the request ID.
         */
        public int getRequestId() {
            return mRequestId;
        }
    }

    /**
     * This class tracks the last frame number for submitted requests.
     */
    public class FrameNumberTracker {
        private long mCompletedFrameNumber = android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback.NO_FRAMES_CAPTURED;

        private long mCompletedReprocessFrameNumber = android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback.NO_FRAMES_CAPTURED;

        /**
         * the skipped frame numbers that belong to regular results
         */
        private final java.util.LinkedList<java.lang.Long> mSkippedRegularFrameNumbers = new java.util.LinkedList<java.lang.Long>();

        /**
         * the skipped frame numbers that belong to reprocess results
         */
        private final java.util.LinkedList<java.lang.Long> mSkippedReprocessFrameNumbers = new java.util.LinkedList<java.lang.Long>();

        /**
         * frame number -> is reprocess
         */
        private final java.util.TreeMap<java.lang.Long, java.lang.Boolean> mFutureErrorMap = new java.util.TreeMap<java.lang.Long, java.lang.Boolean>();

        /**
         * Map frame numbers to list of partial results
         */
        private final java.util.HashMap<java.lang.Long, java.util.List<android.hardware.camera2.CaptureResult>> mPartialResults = new java.util.HashMap<>();

        private void update() {
            java.util.Iterator iter = mFutureErrorMap.entrySet().iterator();
            while (iter.hasNext()) {
                android.hardware.camera2.impl.TreeMap.Entry pair = ((android.hardware.camera2.impl.TreeMap.Entry) (iter.next()));
                java.lang.Long errorFrameNumber = ((java.lang.Long) (pair.getKey()));
                java.lang.Boolean reprocess = ((java.lang.Boolean) (pair.getValue()));
                java.lang.Boolean removeError = true;
                if (reprocess) {
                    if (errorFrameNumber == (mCompletedReprocessFrameNumber + 1)) {
                        mCompletedReprocessFrameNumber = errorFrameNumber;
                    } else
                        if ((mSkippedReprocessFrameNumbers.isEmpty() != true) && (errorFrameNumber == mSkippedReprocessFrameNumbers.element())) {
                            mCompletedReprocessFrameNumber = errorFrameNumber;
                            mSkippedReprocessFrameNumbers.remove();
                        } else {
                            removeError = false;
                        }

                } else {
                    if (errorFrameNumber == (mCompletedFrameNumber + 1)) {
                        mCompletedFrameNumber = errorFrameNumber;
                    } else
                        if ((mSkippedRegularFrameNumbers.isEmpty() != true) && (errorFrameNumber == mSkippedRegularFrameNumbers.element())) {
                            mCompletedFrameNumber = errorFrameNumber;
                            mSkippedRegularFrameNumbers.remove();
                        } else {
                            removeError = false;
                        }

                }
                if (removeError) {
                    iter.remove();
                }
            } 
        }

        /**
         * This function is called every time when a result or an error is received.
         *
         * @param frameNumber
         * 		the frame number corresponding to the result or error
         * @param isError
         * 		true if it is an error, false if it is not an error
         * @param isReprocess
         * 		true if it is a reprocess result, false if it is a regular result.
         */
        public void updateTracker(long frameNumber, boolean isError, boolean isReprocess) {
            if (isError) {
                mFutureErrorMap.put(frameNumber, isReprocess);
            } else {
                try {
                    if (isReprocess) {
                        updateCompletedReprocessFrameNumber(frameNumber);
                    } else {
                        updateCompletedFrameNumber(frameNumber);
                    }
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Log.e(TAG, e.getMessage());
                }
            }
            update();
        }

        /**
         * This function is called every time a result has been completed.
         *
         * <p>It keeps a track of all the partial results already created for a particular
         * frame number.</p>
         *
         * @param frameNumber
         * 		the frame number corresponding to the result
         * @param result
         * 		the total or partial result
         * @param partial
         * 		{@true } if the result is partial, {@code false} if total
         * @param isReprocess
         * 		true if it is a reprocess result, false if it is a regular result.
         */
        public void updateTracker(long frameNumber, android.hardware.camera2.CaptureResult result, boolean partial, boolean isReprocess) {
            if (!partial) {
                // Update the total result's frame status as being successful
                /* isError */
                updateTracker(frameNumber, false, isReprocess);
                // Don't keep a list of total results, we don't need to track them
                return;
            }
            if (result == null) {
                // Do not record blank results; this also means there will be no total result
                // so it doesn't matter that the partials were not recorded
                return;
            }
            // Partial results must be aggregated in-order for that frame number
            java.util.List<android.hardware.camera2.CaptureResult> partials = mPartialResults.get(frameNumber);
            if (partials == null) {
                partials = new java.util.ArrayList<>();
                mPartialResults.put(frameNumber, partials);
            }
            partials.add(result);
        }

        /**
         * Attempt to pop off all of the partial results seen so far for the {@code frameNumber}.
         *
         * <p>Once popped-off, the partial results are forgotten (unless {@code updateTracker}
         * is called again with new partials for that frame number).</p>
         *
         * @param frameNumber
         * 		the frame number corresponding to the result
         * @return a list of partial results for that frame with at least 1 element,
        or {@code null} if there were no partials recorded for that frame
         */
        public java.util.List<android.hardware.camera2.CaptureResult> popPartialResults(long frameNumber) {
            return mPartialResults.remove(frameNumber);
        }

        public long getCompletedFrameNumber() {
            return mCompletedFrameNumber;
        }

        public long getCompletedReprocessFrameNumber() {
            return mCompletedReprocessFrameNumber;
        }

        /**
         * Update the completed frame number for regular results.
         *
         * It validates that all previous frames have arrived except for reprocess frames.
         *
         * If there is a gap since previous regular frame number, assume the frames in the gap are
         * reprocess frames and store them in the skipped reprocess frame number queue to check
         * against when reprocess frames arrive.
         */
        private void updateCompletedFrameNumber(long frameNumber) throws java.lang.IllegalArgumentException {
            if (frameNumber <= mCompletedFrameNumber) {
                throw new java.lang.IllegalArgumentException(("frame number " + frameNumber) + " is a repeat");
            } else
                if (frameNumber <= mCompletedReprocessFrameNumber) {
                    // if frame number is smaller than completed reprocess frame number,
                    // it must be the head of mSkippedRegularFrameNumbers
                    if ((mSkippedRegularFrameNumbers.isEmpty() == true) || (frameNumber < mSkippedRegularFrameNumbers.element())) {
                        throw new java.lang.IllegalArgumentException(("frame number " + frameNumber) + " is a repeat");
                    } else
                        if (frameNumber > mSkippedRegularFrameNumbers.element()) {
                            throw new java.lang.IllegalArgumentException((("frame number " + frameNumber) + " comes out of order. Expecting ") + mSkippedRegularFrameNumbers.element());
                        }

                    // frame number matches the head of the skipped frame number queue.
                    mSkippedRegularFrameNumbers.remove();
                } else {
                    // there is a gap of unseen frame numbers which should belong to reprocess result
                    // put all the skipped frame numbers in the queue
                    for (long i = java.lang.Math.max(mCompletedFrameNumber, mCompletedReprocessFrameNumber) + 1; i < frameNumber; i++) {
                        mSkippedReprocessFrameNumbers.add(i);
                    }
                }

            mCompletedFrameNumber = frameNumber;
        }

        /**
         * Update the completed frame number for reprocess results.
         *
         * It validates that all previous frames have arrived except for regular frames.
         *
         * If there is a gap since previous reprocess frame number, assume the frames in the gap are
         * regular frames and store them in the skipped regular frame number queue to check
         * against when regular frames arrive.
         */
        private void updateCompletedReprocessFrameNumber(long frameNumber) throws java.lang.IllegalArgumentException {
            if (frameNumber < mCompletedReprocessFrameNumber) {
                throw new java.lang.IllegalArgumentException(("frame number " + frameNumber) + " is a repeat");
            } else
                if (frameNumber < mCompletedFrameNumber) {
                    // if reprocess frame number is smaller than completed regular frame number,
                    // it must be the head of the skipped reprocess frame number queue.
                    if ((mSkippedReprocessFrameNumbers.isEmpty() == true) || (frameNumber < mSkippedReprocessFrameNumbers.element())) {
                        throw new java.lang.IllegalArgumentException(("frame number " + frameNumber) + " is a repeat");
                    } else
                        if (frameNumber > mSkippedReprocessFrameNumbers.element()) {
                            throw new java.lang.IllegalArgumentException((("frame number " + frameNumber) + " comes out of order. Expecting ") + mSkippedReprocessFrameNumbers.element());
                        }

                    // frame number matches the head of the skipped frame number queue.
                    mSkippedReprocessFrameNumbers.remove();
                } else {
                    // put all the skipped frame numbers in the queue
                    for (long i = java.lang.Math.max(mCompletedFrameNumber, mCompletedReprocessFrameNumber) + 1; i < frameNumber; i++) {
                        mSkippedRegularFrameNumbers.add(i);
                    }
                }

            mCompletedReprocessFrameNumber = frameNumber;
        }
    }

    private void checkAndFireSequenceComplete() {
        long completedFrameNumber = mFrameNumberTracker.getCompletedFrameNumber();
        long completedReprocessFrameNumber = mFrameNumberTracker.getCompletedReprocessFrameNumber();
        boolean isReprocess = false;
        java.util.Iterator<android.hardware.camera2.impl.CameraDeviceImpl.RequestLastFrameNumbersHolder> iter = mRequestLastFrameNumbersList.iterator();
        while (iter.hasNext()) {
            final android.hardware.camera2.impl.CameraDeviceImpl.RequestLastFrameNumbersHolder requestLastFrameNumbers = iter.next();
            boolean sequenceCompleted = false;
            final int requestId = requestLastFrameNumbers.getRequestId();
            final android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallbackHolder holder;
            synchronized(mInterfaceLock) {
                if (mRemoteDevice == null) {
                    android.util.Log.w(TAG, "Camera closed while checking sequences");
                    return;
                }
                int index = mCaptureCallbackMap.indexOfKey(requestId);
                holder = (index >= 0) ? mCaptureCallbackMap.valueAt(index) : null;
                if (holder != null) {
                    long lastRegularFrameNumber = requestLastFrameNumbers.getLastRegularFrameNumber();
                    long lastReprocessFrameNumber = requestLastFrameNumbers.getLastReprocessFrameNumber();
                    // check if it's okay to remove request from mCaptureCallbackMap
                    if ((lastRegularFrameNumber <= completedFrameNumber) && (lastReprocessFrameNumber <= completedReprocessFrameNumber)) {
                        sequenceCompleted = true;
                        mCaptureCallbackMap.removeAt(index);
                        if (DEBUG) {
                            android.util.Log.v(TAG, java.lang.String.format("Remove holder for requestId %d, because lastRegularFrame %d " + "is <= %d and lastReprocessFrame %d is <= %d", requestId, lastRegularFrameNumber, completedFrameNumber, lastReprocessFrameNumber, completedReprocessFrameNumber));
                        }
                    }
                }
            }
            // If no callback is registered for this requestId or sequence completed, remove it
            // from the frame number->request pair because it's not needed anymore.
            if ((holder == null) || sequenceCompleted) {
                iter.remove();
            }
            // Call onCaptureSequenceCompleted
            if (sequenceCompleted) {
                java.lang.Runnable resultDispatch = new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (!android.hardware.camera2.impl.CameraDeviceImpl.this.isClosed()) {
                            if (DEBUG) {
                                android.util.Log.d(TAG, java.lang.String.format("fire sequence complete for request %d", requestId));
                            }
                            holder.getCallback().onCaptureSequenceCompleted(android.hardware.camera2.impl.CameraDeviceImpl.this, requestId, requestLastFrameNumbers.getLastFrameNumber());
                        }
                    }
                };
                holder.getHandler().post(resultDispatch);
            }
        } 
    }

    public class CameraDeviceCallbacks extends android.hardware.camera2.ICameraDeviceCallbacks.Stub {
        @java.lang.Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @java.lang.Override
        public void onDeviceError(final int errorCode, android.hardware.camera2.impl.CaptureResultExtras resultExtras) {
            if (DEBUG) {
                android.util.Log.d(TAG, java.lang.String.format("Device error received, code %d, frame number %d, request ID %d, subseq ID %d", errorCode, resultExtras.getFrameNumber(), resultExtras.getRequestId(), resultExtras.getSubsequenceId()));
            }
            synchronized(mInterfaceLock) {
                if (mRemoteDevice == null) {
                    return;// Camera already closed

                }
                switch (errorCode) {
                    case ERROR_CAMERA_DISCONNECTED :
                        android.hardware.camera2.impl.CameraDeviceImpl.this.mDeviceHandler.post(mCallOnDisconnected);
                        break;
                    default :
                        android.util.Log.e(TAG, "Unknown error from camera device: " + errorCode);
                        // no break
                    case ERROR_CAMERA_DEVICE :
                    case ERROR_CAMERA_SERVICE :
                        mInError = true;
                        final int publicErrorCode = (errorCode == ERROR_CAMERA_DEVICE) ? android.hardware.camera2.CameraDevice.StateCallback.ERROR_CAMERA_DEVICE : android.hardware.camera2.CameraDevice.StateCallback.ERROR_CAMERA_SERVICE;
                        java.lang.Runnable r = new java.lang.Runnable() {
                            @java.lang.Override
                            public void run() {
                                if (!android.hardware.camera2.impl.CameraDeviceImpl.this.isClosed()) {
                                    mDeviceCallback.onError(android.hardware.camera2.impl.CameraDeviceImpl.this, publicErrorCode);
                                }
                            }
                        };
                        android.hardware.camera2.impl.CameraDeviceImpl.this.mDeviceHandler.post(r);
                        break;
                    case ERROR_CAMERA_REQUEST :
                    case ERROR_CAMERA_RESULT :
                    case ERROR_CAMERA_BUFFER :
                        onCaptureErrorLocked(errorCode, resultExtras);
                        break;
                }
            }
        }

        @java.lang.Override
        public void onRepeatingRequestError(long lastFrameNumber) {
            if (DEBUG) {
                android.util.Log.d(TAG, "Repeating request error received. Last frame number is " + lastFrameNumber);
            }
            synchronized(mInterfaceLock) {
                // Camera is already closed or no repeating request is present.
                if ((mRemoteDevice == null) || (mRepeatingRequestId == android.hardware.camera2.impl.CameraDeviceImpl.REQUEST_ID_NONE)) {
                    return;// Camera already closed

                }
                checkEarlyTriggerSequenceComplete(mRepeatingRequestId, lastFrameNumber);
                mRepeatingRequestId = android.hardware.camera2.impl.CameraDeviceImpl.REQUEST_ID_NONE;
            }
        }

        @java.lang.Override
        public void onDeviceIdle() {
            if (DEBUG) {
                android.util.Log.d(TAG, "Camera now idle");
            }
            synchronized(mInterfaceLock) {
                if (mRemoteDevice == null)
                    return;
                // Camera already closed

                if (!android.hardware.camera2.impl.CameraDeviceImpl.this.mIdle) {
                    android.hardware.camera2.impl.CameraDeviceImpl.this.mDeviceHandler.post(mCallOnIdle);
                }
                android.hardware.camera2.impl.CameraDeviceImpl.this.mIdle = true;
            }
        }

        @java.lang.Override
        public void onCaptureStarted(final android.hardware.camera2.impl.CaptureResultExtras resultExtras, final long timestamp) {
            int requestId = resultExtras.getRequestId();
            final long frameNumber = resultExtras.getFrameNumber();
            if (DEBUG) {
                android.util.Log.d(TAG, (("Capture started for id " + requestId) + " frame number ") + frameNumber);
            }
            final android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallbackHolder holder;
            synchronized(mInterfaceLock) {
                if (mRemoteDevice == null)
                    return;
                // Camera already closed

                // Get the callback for this frame ID, if there is one
                holder = android.hardware.camera2.impl.CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
                if (holder == null) {
                    return;
                }
                if (isClosed())
                    return;

                // Dispatch capture start notice
                holder.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (!android.hardware.camera2.impl.CameraDeviceImpl.this.isClosed()) {
                            holder.getCallback().onCaptureStarted(android.hardware.camera2.impl.CameraDeviceImpl.this, holder.getRequest(resultExtras.getSubsequenceId()), timestamp, frameNumber);
                        }
                    }
                });
            }
        }

        @java.lang.Override
        public void onResultReceived(android.hardware.camera2.impl.CameraMetadataNative result, android.hardware.camera2.impl.CaptureResultExtras resultExtras) throws android.os.RemoteException {
            int requestId = resultExtras.getRequestId();
            long frameNumber = resultExtras.getFrameNumber();
            if (DEBUG) {
                android.util.Log.v(TAG, (("Received result frame " + frameNumber) + " for id ") + requestId);
            }
            synchronized(mInterfaceLock) {
                if (mRemoteDevice == null)
                    return;
                // Camera already closed

                // TODO: Handle CameraCharacteristics access from CaptureResult correctly.
                result.set(android.hardware.camera2.CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE, getCharacteristics().get(android.hardware.camera2.CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE));
                final android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallbackHolder holder = android.hardware.camera2.impl.CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
                final android.hardware.camera2.CaptureRequest request = holder.getRequest(resultExtras.getSubsequenceId());
                boolean isPartialResult = resultExtras.getPartialResultCount() < mTotalPartialCount;
                boolean isReprocess = request.isReprocess();
                // Check if we have a callback for this
                if (holder == null) {
                    if (DEBUG) {
                        android.util.Log.d(TAG, "holder is null, early return at frame " + frameNumber);
                    }
                    /* result */
                    mFrameNumberTracker.updateTracker(frameNumber, null, isPartialResult, isReprocess);
                    return;
                }
                if (isClosed()) {
                    if (DEBUG) {
                        android.util.Log.d(TAG, "camera is closed, early return at frame " + frameNumber);
                    }
                    /* result */
                    mFrameNumberTracker.updateTracker(frameNumber, null, isPartialResult, isReprocess);
                    return;
                }
                java.lang.Runnable resultDispatch = null;
                android.hardware.camera2.CaptureResult finalResult;
                // Either send a partial result or the final capture completed result
                if (isPartialResult) {
                    final android.hardware.camera2.CaptureResult resultAsCapture = new android.hardware.camera2.CaptureResult(result, request, resultExtras);
                    // Partial result
                    resultDispatch = new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            if (!android.hardware.camera2.impl.CameraDeviceImpl.this.isClosed()) {
                                holder.getCallback().onCaptureProgressed(android.hardware.camera2.impl.CameraDeviceImpl.this, request, resultAsCapture);
                            }
                        }
                    };
                    finalResult = resultAsCapture;
                } else {
                    java.util.List<android.hardware.camera2.CaptureResult> partialResults = mFrameNumberTracker.popPartialResults(frameNumber);
                    final android.hardware.camera2.TotalCaptureResult resultAsCapture = new android.hardware.camera2.TotalCaptureResult(result, request, resultExtras, partialResults, holder.getSessionId());
                    // Final capture result
                    resultDispatch = new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            if (!android.hardware.camera2.impl.CameraDeviceImpl.this.isClosed()) {
                                holder.getCallback().onCaptureCompleted(android.hardware.camera2.impl.CameraDeviceImpl.this, request, resultAsCapture);
                            }
                        }
                    };
                    finalResult = resultAsCapture;
                }
                holder.getHandler().post(resultDispatch);
                // Collect the partials for a total result; or mark the frame as totally completed
                mFrameNumberTracker.updateTracker(frameNumber, finalResult, isPartialResult, isReprocess);
                // Fire onCaptureSequenceCompleted
                if (!isPartialResult) {
                    checkAndFireSequenceComplete();
                }
            }
        }

        @java.lang.Override
        public void onPrepared(int streamId) {
            final android.hardware.camera2.params.OutputConfiguration output;
            final android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK sessionCallback;
            if (DEBUG) {
                android.util.Log.v(TAG, ("Stream " + streamId) + " is prepared");
            }
            synchronized(mInterfaceLock) {
                output = mConfiguredOutputs.get(streamId);
                sessionCallback = mSessionStateCallback;
            }
            if (sessionCallback == null)
                return;

            if (output == null) {
                android.util.Log.w(TAG, "onPrepared invoked for unknown output Surface");
                return;
            }
            final android.view.Surface surface = output.getSurface();
            sessionCallback.onSurfacePrepared(surface);
        }

        /**
         * Called by onDeviceError for handling single-capture failures.
         */
        private void onCaptureErrorLocked(int errorCode, android.hardware.camera2.impl.CaptureResultExtras resultExtras) {
            final int requestId = resultExtras.getRequestId();
            final int subsequenceId = resultExtras.getSubsequenceId();
            final long frameNumber = resultExtras.getFrameNumber();
            final android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallbackHolder holder = android.hardware.camera2.impl.CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
            final android.hardware.camera2.CaptureRequest request = holder.getRequest(subsequenceId);
            java.lang.Runnable failureDispatch = null;
            if (errorCode == ERROR_CAMERA_BUFFER) {
                final android.view.Surface outputSurface = mConfiguredOutputs.get(resultExtras.getErrorStreamId()).getSurface();
                if (DEBUG) {
                    android.util.Log.v(TAG, java.lang.String.format("Lost output buffer reported for frame %d, target %s", frameNumber, outputSurface));
                }
                failureDispatch = new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (!android.hardware.camera2.impl.CameraDeviceImpl.this.isClosed()) {
                            holder.getCallback().onCaptureBufferLost(android.hardware.camera2.impl.CameraDeviceImpl.this, request, outputSurface, frameNumber);
                        }
                    }
                };
            } else {
                boolean mayHaveBuffers = errorCode == ERROR_CAMERA_RESULT;
                // This is only approximate - exact handling needs the camera service and HAL to
                // disambiguate between request failures to due abort and due to real errors.  For
                // now, assume that if the session believes we're mid-abort, then the error is due
                // to abort.
                int reason = ((mCurrentSession != null) && mCurrentSession.isAborting()) ? android.hardware.camera2.CaptureFailure.REASON_FLUSHED : android.hardware.camera2.CaptureFailure.REASON_ERROR;
                final android.hardware.camera2.CaptureFailure failure = /* dropped */
                new android.hardware.camera2.CaptureFailure(request, reason, mayHaveBuffers, requestId, frameNumber);
                failureDispatch = new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (!android.hardware.camera2.impl.CameraDeviceImpl.this.isClosed()) {
                            holder.getCallback().onCaptureFailed(android.hardware.camera2.impl.CameraDeviceImpl.this, request, failure);
                        }
                    }
                };
                // Fire onCaptureSequenceCompleted if appropriate
                if (DEBUG) {
                    android.util.Log.v(TAG, java.lang.String.format("got error frame %d", frameNumber));
                }
                /* error */
                mFrameNumberTracker.updateTracker(frameNumber, true, request.isReprocess());
                checkAndFireSequenceComplete();
            }
            // Dispatch the failure callback
            holder.getHandler().post(failureDispatch);
        }
    }

    /**
     * Default handler management.
     *
     * <p>
     * If handler is null, get the current thread's
     * Looper to create a Handler with. If no looper exists, throw {@code IllegalArgumentException}.
     * </p>
     */
    static android.os.Handler checkHandler(android.os.Handler handler) {
        if (handler == null) {
            android.os.Looper looper = android.os.Looper.myLooper();
            if (looper == null) {
                throw new java.lang.IllegalArgumentException("No handler given, and current thread has no looper!");
            }
            handler = new android.os.Handler(looper);
        }
        return handler;
    }

    /**
     * Default handler management, conditional on there being a callback.
     *
     * <p>If the callback isn't null, check the handler, otherwise pass it through.</p>
     */
    static <T> android.os.Handler checkHandler(android.os.Handler handler, T callback) {
        if (callback != null) {
            return android.hardware.camera2.impl.CameraDeviceImpl.checkHandler(handler);
        }
        return handler;
    }

    private void checkIfCameraClosedOrInError() throws android.hardware.camera2.CameraAccessException {
        if (mRemoteDevice == null) {
            throw new java.lang.IllegalStateException("CameraDevice was already closed");
        }
        if (mInError) {
            throw new android.hardware.camera2.CameraAccessException(android.hardware.camera2.CameraAccessException.CAMERA_ERROR, "The camera device has encountered a serious error");
        }
    }

    /**
     * Whether the camera device has started to close (may not yet have finished)
     */
    private boolean isClosed() {
        return mClosing.get();
    }

    private android.hardware.camera2.CameraCharacteristics getCharacteristics() {
        return mCharacteristics;
    }

    /**
     * Listener for binder death.
     *
     * <p> Handle binder death for ICameraDeviceUser. Trigger onError.</p>
     */
    @java.lang.Override
    public void binderDied() {
        android.util.Log.w(TAG, ("CameraDevice " + mCameraId) + " died unexpectedly");
        if (mRemoteDevice == null) {
            return;// Camera already closed

        }
        mInError = true;
        java.lang.Runnable r = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                if (!isClosed()) {
                    mDeviceCallback.onError(android.hardware.camera2.impl.CameraDeviceImpl.this, android.hardware.camera2.CameraDevice.StateCallback.ERROR_CAMERA_SERVICE);
                }
            }
        };
        this.mDeviceHandler.post(r);
    }
}

