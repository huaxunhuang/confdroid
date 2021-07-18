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
package android.hardware.camera2;


/**
 * <p>A system service manager for detecting, characterizing, and connecting to
 * {@link CameraDevice CameraDevices}.</p>
 *
 * <p>You can get an instance of this class by calling
 * {@link android.content.Context#getSystemService(String) Context.getSystemService()}.</p>
 *
 * <pre>CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);</pre>
 *
 * <p>For more details about communicating with camera devices, read the Camera
 * developer guide or the {@link android.hardware.camera2 camera2}
 * package documentation.</p>
 */
public final class CameraManager {
    private static final java.lang.String TAG = "CameraManager";

    private final boolean DEBUG = false;

    private static final int USE_CALLING_UID = -1;

    @java.lang.SuppressWarnings("unused")
    private static final int API_VERSION_1 = 1;

    private static final int API_VERSION_2 = 2;

    private static final int CAMERA_TYPE_BACKWARD_COMPATIBLE = 0;

    private static final int CAMERA_TYPE_ALL = 1;

    private java.util.ArrayList<java.lang.String> mDeviceIdList;

    private final android.content.Context mContext;

    private final java.lang.Object mLock = new java.lang.Object();

    /**
     *
     *
     * @unknown 
     */
    public CameraManager(android.content.Context context) {
        synchronized(mLock) {
            mContext = context;
        }
    }

    /**
     * Return the list of currently connected camera devices by identifier, including
     * cameras that may be in use by other camera API clients.
     *
     * <p>Non-removable cameras use integers starting at 0 for their
     * identifiers, while removable cameras have a unique identifier for each
     * individual device, even if they are the same model.</p>
     *
     * @return The list of currently connected camera devices.
     */
    @android.annotation.NonNull
    public java.lang.String[] getCameraIdList() throws android.hardware.camera2.CameraAccessException {
        synchronized(mLock) {
            // ID list creation handles various known failures in device enumeration, so only
            // exceptions it'll throw are unexpected, and should be propagated upward.
            return getOrCreateDeviceIdListLocked().toArray(new java.lang.String[0]);
        }
    }

    /**
     * Register a callback to be notified about camera device availability.
     *
     * <p>Registering the same callback again will replace the handler with the
     * new one provided.</p>
     *
     * <p>The first time a callback is registered, it is immediately called
     * with the availability status of all currently known camera devices.</p>
     *
     * <p>{@link AvailabilityCallback#onCameraUnavailable(String)} will be called whenever a camera
     * device is opened by any camera API client. As of API level 23, other camera API clients may
     * still be able to open such a camera device, evicting the existing client if they have higher
     * priority than the existing client of a camera device. See open() for more details.</p>
     *
     * <p>Since this callback will be registered with the camera service, remember to unregister it
     * once it is no longer needed; otherwise the callback will continue to receive events
     * indefinitely and it may prevent other resources from being released. Specifically, the
     * callbacks will be invoked independently of the general activity lifecycle and independently
     * of the state of individual CameraManager instances.</p>
     *
     * @param callback
     * 		the new callback to send camera availability notices to
     * @param handler
     * 		The handler on which the callback should be invoked, or {@code null} to use
     * 		the current thread's {@link android.os.Looper looper}.
     * @throws IllegalArgumentException
     * 		if the handler is {@code null} but the current thread has
     * 		no looper.
     */
    public void registerAvailabilityCallback(@android.annotation.NonNull
    android.hardware.camera2.CameraManager.AvailabilityCallback callback, @android.annotation.Nullable
    android.os.Handler handler) {
        if (handler == null) {
            android.os.Looper looper = android.os.Looper.myLooper();
            if (looper == null) {
                throw new java.lang.IllegalArgumentException("No handler given, and current thread has no looper!");
            }
            handler = new android.os.Handler(looper);
        }
        android.hardware.camera2.CameraManager.CameraManagerGlobal.get().registerAvailabilityCallback(callback, handler);
    }

    /**
     * Remove a previously-added callback; the callback will no longer receive connection and
     * disconnection callbacks.
     *
     * <p>Removing a callback that isn't registered has no effect.</p>
     *
     * @param callback
     * 		The callback to remove from the notification list
     */
    public void unregisterAvailabilityCallback(@android.annotation.NonNull
    android.hardware.camera2.CameraManager.AvailabilityCallback callback) {
        android.hardware.camera2.CameraManager.CameraManagerGlobal.get().unregisterAvailabilityCallback(callback);
    }

    /**
     * Register a callback to be notified about torch mode status.
     *
     * <p>Registering the same callback again will replace the handler with the
     * new one provided.</p>
     *
     * <p>The first time a callback is registered, it is immediately called
     * with the torch mode status of all currently known camera devices with a flash unit.</p>
     *
     * <p>Since this callback will be registered with the camera service, remember to unregister it
     * once it is no longer needed; otherwise the callback will continue to receive events
     * indefinitely and it may prevent other resources from being released. Specifically, the
     * callbacks will be invoked independently of the general activity lifecycle and independently
     * of the state of individual CameraManager instances.</p>
     *
     * @param callback
     * 		The new callback to send torch mode status to
     * @param handler
     * 		The handler on which the callback should be invoked, or {@code null} to use
     * 		the current thread's {@link android.os.Looper looper}.
     * @throws IllegalArgumentException
     * 		if the handler is {@code null} but the current thread has
     * 		no looper.
     */
    public void registerTorchCallback(@android.annotation.NonNull
    android.hardware.camera2.CameraManager.TorchCallback callback, @android.annotation.Nullable
    android.os.Handler handler) {
        if (handler == null) {
            android.os.Looper looper = android.os.Looper.myLooper();
            if (looper == null) {
                throw new java.lang.IllegalArgumentException("No handler given, and current thread has no looper!");
            }
            handler = new android.os.Handler(looper);
        }
        android.hardware.camera2.CameraManager.CameraManagerGlobal.get().registerTorchCallback(callback, handler);
    }

    /**
     * Remove a previously-added callback; the callback will no longer receive torch mode status
     * callbacks.
     *
     * <p>Removing a callback that isn't registered has no effect.</p>
     *
     * @param callback
     * 		The callback to remove from the notification list
     */
    public void unregisterTorchCallback(@android.annotation.NonNull
    android.hardware.camera2.CameraManager.TorchCallback callback) {
        android.hardware.camera2.CameraManager.CameraManagerGlobal.get().unregisterTorchCallback(callback);
    }

    /**
     * <p>Query the capabilities of a camera device. These capabilities are
     * immutable for a given camera.</p>
     *
     * @param cameraId
     * 		The id of the camera device to query
     * @return The properties of the given camera
     * @throws IllegalArgumentException
     * 		if the cameraId does not match any
     * 		known camera device.
     * @throws CameraAccessException
     * 		if the camera device has been disconnected.
     * @see #getCameraIdList
     * @see android.app.admin.DevicePolicyManager#setCameraDisabled
     */
    @android.annotation.NonNull
    public android.hardware.camera2.CameraCharacteristics getCameraCharacteristics(@android.annotation.NonNull
    java.lang.String cameraId) throws android.hardware.camera2.CameraAccessException {
        android.hardware.camera2.CameraCharacteristics characteristics = null;
        synchronized(mLock) {
            if (!getOrCreateDeviceIdListLocked().contains(cameraId)) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Camera id %s does not match any" + " currently connected camera device", cameraId));
            }
            int id = java.lang.Integer.parseInt(cameraId);
            /* Get the camera characteristics from the camera service directly if it supports it,
            otherwise get them from the legacy shim instead.
             */
            android.hardware.ICameraService cameraService = android.hardware.camera2.CameraManager.CameraManagerGlobal.get().getCameraService();
            if (cameraService == null) {
                throw new android.hardware.camera2.CameraAccessException(android.hardware.camera2.CameraAccessException.CAMERA_DISCONNECTED, "Camera service is currently unavailable");
            }
            try {
                if (!supportsCamera2ApiLocked(cameraId)) {
                    // Legacy backwards compatibility path; build static info from the camera
                    // parameters
                    java.lang.String parameters = cameraService.getLegacyParameters(id);
                    android.hardware.CameraInfo info = cameraService.getCameraInfo(id);
                    characteristics = android.hardware.camera2.legacy.LegacyMetadataMapper.createCharacteristics(parameters, info);
                } else {
                    // Normal path: Get the camera characteristics directly from the camera service
                    android.hardware.camera2.impl.CameraMetadataNative info = cameraService.getCameraCharacteristics(id);
                    characteristics = new android.hardware.camera2.CameraCharacteristics(info);
                }
            } catch (android.os.ServiceSpecificException e) {
                android.hardware.camera2.CameraManager.throwAsPublicException(e);
            } catch (android.os.RemoteException e) {
                // Camera service died - act as if the camera was disconnected
                throw new android.hardware.camera2.CameraAccessException(android.hardware.camera2.CameraAccessException.CAMERA_DISCONNECTED, "Camera service is currently unavailable", e);
            }
        }
        return characteristics;
    }

    /**
     * Helper for opening a connection to a camera with the given ID.
     *
     * @param cameraId
     * 		The unique identifier of the camera device to open
     * @param callback
     * 		The callback for the camera. Must not be null.
     * @param handler
     * 		The handler to invoke the callback on. Must not be null.
     * @param uid
     * 		The UID of the application actually opening the camera.
     * 		Must be USE_CALLING_UID unless the caller is a service
     * 		that is trusted to open the device on behalf of an
     * 		application and to forward the real UID.
     * @throws CameraAccessException
     * 		if the camera is disabled by device policy,
     * 		too many camera devices are already open, or the cameraId does not match
     * 		any currently available camera device.
     * @throws SecurityException
     * 		if the application does not have permission to
     * 		access the camera
     * @throws IllegalArgumentException
     * 		if callback or handler is null.
     * @return A handle to the newly-created camera device.
     * @see #getCameraIdList
     * @see android.app.admin.DevicePolicyManager#setCameraDisabled
     */
    private android.hardware.camera2.CameraDevice openCameraDeviceUserAsync(java.lang.String cameraId, android.hardware.camera2.CameraDevice.StateCallback callback, android.os.Handler handler, final int uid) throws android.hardware.camera2.CameraAccessException {
        android.hardware.camera2.CameraCharacteristics characteristics = getCameraCharacteristics(cameraId);
        android.hardware.camera2.CameraDevice device = null;
        synchronized(mLock) {
            android.hardware.camera2.ICameraDeviceUser cameraUser = null;
            android.hardware.camera2.impl.CameraDeviceImpl deviceImpl = new android.hardware.camera2.impl.CameraDeviceImpl(cameraId, callback, handler, characteristics);
            android.hardware.camera2.ICameraDeviceCallbacks callbacks = deviceImpl.getCallbacks();
            int id;
            try {
                id = java.lang.Integer.parseInt(cameraId);
            } catch (java.lang.NumberFormatException e) {
                throw new java.lang.IllegalArgumentException("Expected cameraId to be numeric, but it was: " + cameraId);
            }
            try {
                if (supportsCamera2ApiLocked(cameraId)) {
                    // Use cameraservice's cameradeviceclient implementation for HAL3.2+ devices
                    android.hardware.ICameraService cameraService = android.hardware.camera2.CameraManager.CameraManagerGlobal.get().getCameraService();
                    if (cameraService == null) {
                        throw new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, "Camera service is currently unavailable");
                    }
                    cameraUser = cameraService.connectDevice(callbacks, id, mContext.getOpPackageName(), uid);
                } else {
                    // Use legacy camera implementation for HAL1 devices
                    android.util.Log.i(android.hardware.camera2.CameraManager.TAG, "Using legacy camera HAL.");
                    cameraUser = android.hardware.camera2.legacy.CameraDeviceUserShim.connectBinderShim(callbacks, id);
                }
            } catch (android.os.ServiceSpecificException e) {
                if (e.errorCode == android.hardware.ICameraService.ERROR_DEPRECATED_HAL) {
                    throw new java.lang.AssertionError("Should've gone down the shim path");
                } else
                    if (((((e.errorCode == android.hardware.ICameraService.ERROR_CAMERA_IN_USE) || (e.errorCode == android.hardware.ICameraService.ERROR_MAX_CAMERAS_IN_USE)) || (e.errorCode == android.hardware.ICameraService.ERROR_DISABLED)) || (e.errorCode == android.hardware.ICameraService.ERROR_DISCONNECTED)) || (e.errorCode == android.hardware.ICameraService.ERROR_INVALID_OPERATION)) {
                        // Received one of the known connection errors
                        // The remote camera device cannot be connected to, so
                        // set the local camera to the startup error state
                        deviceImpl.setRemoteFailure(e);
                        if (((e.errorCode == android.hardware.ICameraService.ERROR_DISABLED) || (e.errorCode == android.hardware.ICameraService.ERROR_DISCONNECTED)) || (e.errorCode == android.hardware.ICameraService.ERROR_CAMERA_IN_USE)) {
                            // Per API docs, these failures call onError and throw
                            android.hardware.camera2.CameraManager.throwAsPublicException(e);
                        }
                    } else {
                        // Unexpected failure - rethrow
                        android.hardware.camera2.CameraManager.throwAsPublicException(e);
                    }

            } catch (android.os.RemoteException e) {
                // Camera service died - act as if it's a CAMERA_DISCONNECTED case
                android.os.ServiceSpecificException sse = new android.os.ServiceSpecificException(android.hardware.ICameraService.ERROR_DISCONNECTED, "Camera service is currently unavailable");
                deviceImpl.setRemoteFailure(sse);
                android.hardware.camera2.CameraManager.throwAsPublicException(sse);
            }
            // TODO: factor out callback to be non-nested, then move setter to constructor
            // For now, calling setRemoteDevice will fire initial
            // onOpened/onUnconfigured callbacks.
            // This function call may post onDisconnected and throw CAMERA_DISCONNECTED if
            // cameraUser dies during setup.
            deviceImpl.setRemoteDevice(cameraUser);
            device = deviceImpl;
        }
        return device;
    }

    /**
     * Open a connection to a camera with the given ID.
     *
     * <p>Use {@link #getCameraIdList} to get the list of available camera
     * devices. Note that even if an id is listed, open may fail if the device
     * is disconnected between the calls to {@link #getCameraIdList} and
     * {@link #openCamera}, or if a higher-priority camera API client begins using the
     * camera device.</p>
     *
     * <p>As of API level 23, devices for which the
     * {@link AvailabilityCallback#onCameraUnavailable(String)} callback has been called due to the
     * device being in use by a lower-priority, background camera API client can still potentially
     * be opened by calling this method when the calling camera API client has a higher priority
     * than the current camera API client using this device.  In general, if the top, foreground
     * activity is running within your application process, your process will be given the highest
     * priority when accessing the camera, and this method will succeed even if the camera device is
     * in use by another camera API client. Any lower-priority application that loses control of the
     * camera in this way will receive an
     * {@link android.hardware.camera2.CameraDevice.StateCallback#onDisconnected} callback.</p>
     *
     * <p>Once the camera is successfully opened, {@link CameraDevice.StateCallback#onOpened} will
     * be invoked with the newly opened {@link CameraDevice}. The camera device can then be set up
     * for operation by calling {@link CameraDevice#createCaptureSession} and
     * {@link CameraDevice#createCaptureRequest}</p>
     *
     * <!--
     * <p>Since the camera device will be opened asynchronously, any asynchronous operations done
     * on the returned CameraDevice instance will be queued up until the device startup has
     * completed and the callback's {@link CameraDevice.StateCallback#onOpened onOpened} method is
     * called. The pending operations are then processed in order.</p>
     * -->
     * <p>If the camera becomes disconnected during initialization
     * after this function call returns,
     * {@link CameraDevice.StateCallback#onDisconnected} with a
     * {@link CameraDevice} in the disconnected state (and
     * {@link CameraDevice.StateCallback#onOpened} will be skipped).</p>
     *
     * <p>If opening the camera device fails, then the device callback's
     * {@link CameraDevice.StateCallback#onError onError} method will be called, and subsequent
     * calls on the camera device will throw a {@link CameraAccessException}.</p>
     *
     * @param cameraId
     * 		The unique identifier of the camera device to open
     * @param callback
     * 		The callback which is invoked once the camera is opened
     * @param handler
     * 		The handler on which the callback should be invoked, or
     * 		{@code null} to use the current thread's {@link android.os.Looper looper}.
     * @throws CameraAccessException
     * 		if the camera is disabled by device policy,
     * 		has been disconnected, or is being used by a higher-priority camera API client.
     * @throws IllegalArgumentException
     * 		if cameraId or the callback was null,
     * 		or the cameraId does not match any currently or previously available
     * 		camera device.
     * @throws SecurityException
     * 		if the application does not have permission to
     * 		access the camera
     * @see #getCameraIdList
     * @see android.app.admin.DevicePolicyManager#setCameraDisabled
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.CAMERA)
    public void openCamera(@android.annotation.NonNull
    java.lang.String cameraId, @android.annotation.NonNull
    final android.hardware.camera2.CameraDevice.StateCallback callback, @android.annotation.Nullable
    android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        openCameraForUid(cameraId, callback, handler, android.hardware.camera2.CameraManager.USE_CALLING_UID);
    }

    /**
     * Open a connection to a camera with the given ID, on behalf of another application
     * specified by clientUid.
     *
     * <p>The behavior of this method matches that of {@link #openCamera}, except that it allows
     * the caller to specify the UID to use for permission/etc verification. This can only be
     * done by services trusted by the camera subsystem to act on behalf of applications and
     * to forward the real UID.</p>
     *
     * @param clientUid
     * 		The UID of the application on whose behalf the camera is being opened.
     * 		Must be USE_CALLING_UID unless the caller is a trusted service.
     * @unknown 
     */
    public void openCameraForUid(@android.annotation.NonNull
    java.lang.String cameraId, @android.annotation.NonNull
    final android.hardware.camera2.CameraDevice.StateCallback callback, @android.annotation.Nullable
    android.os.Handler handler, int clientUid) throws android.hardware.camera2.CameraAccessException {
        if (cameraId == null) {
            throw new java.lang.IllegalArgumentException("cameraId was null");
        } else
            if (callback == null) {
                throw new java.lang.IllegalArgumentException("callback was null");
            } else
                if (handler == null) {
                    if (android.os.Looper.myLooper() != null) {
                        handler = new android.os.Handler();
                    } else {
                        throw new java.lang.IllegalArgumentException("Handler argument is null, but no looper exists in the calling thread");
                    }
                }


        openCameraDeviceUserAsync(cameraId, callback, handler, clientUid);
    }

    /**
     * Set the flash unit's torch mode of the camera of the given ID without opening the camera
     * device.
     *
     * <p>Use {@link #getCameraIdList} to get the list of available camera devices and use
     * {@link #getCameraCharacteristics} to check whether the camera device has a flash unit.
     * Note that even if a camera device has a flash unit, turning on the torch mode may fail
     * if the camera device or other camera resources needed to turn on the torch mode are in use.
     * </p>
     *
     * <p> If {@link #setTorchMode} is called to turn on or off the torch mode successfully,
     * {@link CameraManager.TorchCallback#onTorchModeChanged} will be invoked.
     * However, even if turning on the torch mode is successful, the application does not have the
     * exclusive ownership of the flash unit or the camera device. The torch mode will be turned
     * off and becomes unavailable when the camera device that the flash unit belongs to becomes
     * unavailable or when other camera resources to keep the torch on become unavailable (
     * {@link CameraManager.TorchCallback#onTorchModeUnavailable} will be invoked). Also,
     * other applications are free to call {@link #setTorchMode} to turn off the torch mode (
     * {@link CameraManager.TorchCallback#onTorchModeChanged} will be invoked). If the latest
     * application that turned on the torch mode exits, the torch mode will be turned off.
     *
     * @param cameraId
     * 		The unique identifier of the camera device that the flash unit belongs to.
     * @param enabled
     * 		The desired state of the torch mode for the target camera device. Set to
     * 		{@code true} to turn on the torch mode. Set to {@code false} to turn off the
     * 		torch mode.
     * @throws CameraAccessException
     * 		if it failed to access the flash unit.
     * 		{@link CameraAccessException#CAMERA_IN_USE} will be thrown if the camera device
     * 		is in use. {@link CameraAccessException#MAX_CAMERAS_IN_USE} will be thrown if
     * 		other camera resources needed to turn on the torch mode are in use.
     * 		{@link CameraAccessException#CAMERA_DISCONNECTED} will be thrown if camera
     * 		service is not available.
     * @throws IllegalArgumentException
     * 		if cameraId was null, cameraId doesn't match any currently
     * 		or previously available camera device, or the camera device doesn't have a
     * 		flash unit.
     */
    public void setTorchMode(@android.annotation.NonNull
    java.lang.String cameraId, boolean enabled) throws android.hardware.camera2.CameraAccessException {
        android.hardware.camera2.CameraManager.CameraManagerGlobal.get().setTorchMode(cameraId, enabled);
    }

    /**
     * A callback for camera devices becoming available or unavailable to open.
     *
     * <p>Cameras become available when they are no longer in use, or when a new
     * removable camera is connected. They become unavailable when some
     * application or service starts using a camera, or when a removable camera
     * is disconnected.</p>
     *
     * <p>Extend this callback and pass an instance of the subclass to
     * {@link CameraManager#registerAvailabilityCallback} to be notified of such availability
     * changes.</p>
     *
     * @see #registerAvailabilityCallback
     */
    public static abstract class AvailabilityCallback {
        /**
         * A new camera has become available to use.
         *
         * <p>The default implementation of this method does nothing.</p>
         *
         * @param cameraId
         * 		The unique identifier of the new camera.
         */
        public void onCameraAvailable(@android.annotation.NonNull
        java.lang.String cameraId) {
            // default empty implementation
        }

        /**
         * A previously-available camera has become unavailable for use.
         *
         * <p>If an application had an active CameraDevice instance for the
         * now-disconnected camera, that application will receive a
         * {@link CameraDevice.StateCallback#onDisconnected disconnection error}.</p>
         *
         * <p>The default implementation of this method does nothing.</p>
         *
         * @param cameraId
         * 		The unique identifier of the disconnected camera.
         */
        public void onCameraUnavailable(@android.annotation.NonNull
        java.lang.String cameraId) {
            // default empty implementation
        }
    }

    /**
     * A callback for camera flash torch modes becoming unavailable, disabled, or enabled.
     *
     * <p>The torch mode becomes unavailable when the camera device it belongs to becomes
     * unavailable or other camera resources it needs become busy due to other higher priority
     * camera activities. The torch mode becomes disabled when it was turned off or when the camera
     * device it belongs to is no longer in use and other camera resources it needs are no longer
     * busy. A camera's torch mode is turned off when an application calls {@link #setTorchMode} to
     * turn off the camera's torch mode, or when an application turns on another camera's torch mode
     * if keeping multiple torch modes on simultaneously is not supported. The torch mode becomes
     * enabled when it is turned on via {@link #setTorchMode}.</p>
     *
     * <p>The torch mode is available to set via {@link #setTorchMode} only when it's in a disabled
     * or enabled state.</p>
     *
     * <p>Extend this callback and pass an instance of the subclass to
     * {@link CameraManager#registerTorchCallback} to be notified of such status changes.
     * </p>
     *
     * @see #registerTorchCallback
     */
    public static abstract class TorchCallback {
        /**
         * A camera's torch mode has become unavailable to set via {@link #setTorchMode}.
         *
         * <p>If torch mode was previously turned on by calling {@link #setTorchMode}, it will be
         * turned off before {@link CameraManager.TorchCallback#onTorchModeUnavailable} is
         * invoked. {@link #setTorchMode} will fail until the torch mode has entered a disabled or
         * enabled state again.</p>
         *
         * <p>The default implementation of this method does nothing.</p>
         *
         * @param cameraId
         * 		The unique identifier of the camera whose torch mode has become
         * 		unavailable.
         */
        public void onTorchModeUnavailable(@android.annotation.NonNull
        java.lang.String cameraId) {
            // default empty implementation
        }

        /**
         * A camera's torch mode has become enabled or disabled and can be changed via
         * {@link #setTorchMode}.
         *
         * <p>The default implementation of this method does nothing.</p>
         *
         * @param cameraId
         * 		The unique identifier of the camera whose torch mode has been changed.
         * @param enabled
         * 		The state that the torch mode of the camera has been changed to.
         * 		{@code true} when the torch mode has become on and available to be turned
         * 		off. {@code false} when the torch mode has becomes off and available to
         * 		be turned on.
         */
        public void onTorchModeChanged(@android.annotation.NonNull
        java.lang.String cameraId, boolean enabled) {
            // default empty implementation
        }
    }

    /**
     * Convert ServiceSpecificExceptions and Binder RemoteExceptions from camera binder interfaces
     * into the correct public exceptions.
     *
     * @unknown 
     */
    public static void throwAsPublicException(java.lang.Throwable t) throws android.hardware.camera2.CameraAccessException {
        if (t instanceof android.os.ServiceSpecificException) {
            android.os.ServiceSpecificException e = ((android.os.ServiceSpecificException) (t));
            int reason = android.hardware.camera2.CameraAccessException.CAMERA_ERROR;
            switch (e.errorCode) {
                case android.hardware.ICameraService.ERROR_DISCONNECTED :
                    reason = android.hardware.camera2.CameraAccessException.CAMERA_DISCONNECTED;
                    break;
                case android.hardware.ICameraService.ERROR_DISABLED :
                    reason = android.hardware.camera2.CameraAccessException.CAMERA_DISABLED;
                    break;
                case android.hardware.ICameraService.ERROR_CAMERA_IN_USE :
                    reason = android.hardware.camera2.CameraAccessException.CAMERA_IN_USE;
                    break;
                case android.hardware.ICameraService.ERROR_MAX_CAMERAS_IN_USE :
                    reason = android.hardware.camera2.CameraAccessException.MAX_CAMERAS_IN_USE;
                    break;
                case android.hardware.ICameraService.ERROR_DEPRECATED_HAL :
                    reason = android.hardware.camera2.CameraAccessException.CAMERA_DEPRECATED_HAL;
                    break;
                case android.hardware.ICameraService.ERROR_ILLEGAL_ARGUMENT :
                case android.hardware.ICameraService.ERROR_ALREADY_EXISTS :
                    throw new java.lang.IllegalArgumentException(e.getMessage(), e);
                case android.hardware.ICameraService.ERROR_PERMISSION_DENIED :
                    throw new java.lang.SecurityException(e.getMessage(), e);
                case android.hardware.ICameraService.ERROR_TIMED_OUT :
                case android.hardware.ICameraService.ERROR_INVALID_OPERATION :
                default :
                    reason = android.hardware.camera2.CameraAccessException.CAMERA_ERROR;
            }
            throw new android.hardware.camera2.CameraAccessException(reason, e.getMessage(), e);
        } else
            if (t instanceof android.os.DeadObjectException) {
                throw new android.hardware.camera2.CameraAccessException(android.hardware.camera2.CameraAccessException.CAMERA_DISCONNECTED, "Camera service has died unexpectedly", t);
            } else
                if (t instanceof android.os.RemoteException) {
                    throw new java.lang.UnsupportedOperationException("An unknown RemoteException was thrown" + " which should never happen.", t);
                } else
                    if (t instanceof java.lang.RuntimeException) {
                        java.lang.RuntimeException e = ((java.lang.RuntimeException) (t));
                        throw e;
                    }



    }

    /**
     * Return or create the list of currently connected camera devices.
     *
     * <p>In case of errors connecting to the camera service, will return an empty list.</p>
     */
    private java.util.ArrayList<java.lang.String> getOrCreateDeviceIdListLocked() throws android.hardware.camera2.CameraAccessException {
        if (mDeviceIdList == null) {
            int numCameras = 0;
            android.hardware.ICameraService cameraService = android.hardware.camera2.CameraManager.CameraManagerGlobal.get().getCameraService();
            java.util.ArrayList<java.lang.String> deviceIdList = new java.util.ArrayList<>();
            // If no camera service, then no devices
            if (cameraService == null) {
                return deviceIdList;
            }
            try {
                numCameras = cameraService.getNumberOfCameras(android.hardware.camera2.CameraManager.CAMERA_TYPE_ALL);
            } catch (android.os.ServiceSpecificException e) {
                android.hardware.camera2.CameraManager.throwAsPublicException(e);
            } catch (android.os.RemoteException e) {
                // camera service just died - if no camera service, then no devices
                return deviceIdList;
            }
            for (int i = 0; i < numCameras; ++i) {
                // Non-removable cameras use integers starting at 0 for their
                // identifiers
                boolean isDeviceSupported = false;
                try {
                    android.hardware.camera2.impl.CameraMetadataNative info = cameraService.getCameraCharacteristics(i);
                    if (!info.isEmpty()) {
                        isDeviceSupported = true;
                    } else {
                        throw new java.lang.AssertionError("Expected to get non-empty characteristics");
                    }
                } catch (android.os.ServiceSpecificException e) {
                    // DISCONNECTED means that the HAL reported an low-level error getting the
                    // device info; ILLEGAL_ARGUMENT means that this devices is not supported.
                    // Skip listing the device.  Other errors,
                    // propagate exception onward
                    if ((e.errorCode != android.hardware.ICameraService.ERROR_DISCONNECTED) || (e.errorCode != android.hardware.ICameraService.ERROR_ILLEGAL_ARGUMENT)) {
                        android.hardware.camera2.CameraManager.throwAsPublicException(e);
                    }
                } catch (android.os.RemoteException e) {
                    // Camera service died - no devices to list
                    deviceIdList.clear();
                    return deviceIdList;
                }
                if (isDeviceSupported) {
                    deviceIdList.add(java.lang.String.valueOf(i));
                } else {
                    android.util.Log.w(android.hardware.camera2.CameraManager.TAG, ("Error querying camera device " + i) + " for listing.");
                }
            }
            mDeviceIdList = deviceIdList;
        }
        return mDeviceIdList;
    }

    /**
     * Queries the camera service if it supports the camera2 api directly, or needs a shim.
     *
     * @param cameraId
     * 		a non-{@code null} camera identifier
     * @return {@code false} if the legacy shim needs to be used, {@code true} otherwise.
     */
    private boolean supportsCamera2ApiLocked(java.lang.String cameraId) {
        return supportsCameraApiLocked(cameraId, android.hardware.camera2.CameraManager.API_VERSION_2);
    }

    /**
     * Queries the camera service if it supports a camera api directly, or needs a shim.
     *
     * @param cameraId
     * 		a non-{@code null} camera identifier
     * @param apiVersion
     * 		the version, i.e. {@code API_VERSION_1} or {@code API_VERSION_2}
     * @return {@code true} if connecting will work for that device version.
     */
    private boolean supportsCameraApiLocked(java.lang.String cameraId, int apiVersion) {
        int id = java.lang.Integer.parseInt(cameraId);
        /* Possible return values:
        - NO_ERROR => CameraX API is supported
        - CAMERA_DEPRECATED_HAL => CameraX API is *not* supported (thrown as an exception)
        - Remote exception => If the camera service died

        Anything else is an unexpected error we don't want to recover from.
         */
        try {
            android.hardware.ICameraService cameraService = android.hardware.camera2.CameraManager.CameraManagerGlobal.get().getCameraService();
            // If no camera service, no support
            if (cameraService == null)
                return false;

            return cameraService.supportsCameraApi(id, apiVersion);
        } catch (android.os.RemoteException e) {
            // Camera service is now down, no support for any API level
        }
        return false;
    }

    /**
     * A per-process global camera manager instance, to retain a connection to the camera service,
     * and to distribute camera availability notices to API-registered callbacks
     */
    private static final class CameraManagerGlobal extends android.hardware.ICameraServiceListener.Stub implements android.os.IBinder.DeathRecipient {
        private static final java.lang.String TAG = "CameraManagerGlobal";

        private final boolean DEBUG = false;

        private final int CAMERA_SERVICE_RECONNECT_DELAY_MS = 1000;

        // Singleton instance
        private static final android.hardware.camera2.CameraManager.CameraManagerGlobal gCameraManager = new android.hardware.camera2.CameraManager.CameraManagerGlobal();

        /**
         * This must match the ICameraService definition
         */
        private static final java.lang.String CAMERA_SERVICE_BINDER_NAME = "media.camera";

        // Camera ID -> Status map
        private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mDeviceStatus = new android.util.ArrayMap<java.lang.String, java.lang.Integer>();

        // Registered availablility callbacks and their handlers
        private final android.util.ArrayMap<android.hardware.camera2.CameraManager.AvailabilityCallback, android.os.Handler> mCallbackMap = new android.util.ArrayMap<android.hardware.camera2.CameraManager.AvailabilityCallback, android.os.Handler>();

        // torch client binder to set the torch mode with.
        private android.os.Binder mTorchClientBinder = new android.os.Binder();

        // Camera ID -> Torch status map
        private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mTorchStatus = new android.util.ArrayMap<java.lang.String, java.lang.Integer>();

        // Registered torch callbacks and their handlers
        private final android.util.ArrayMap<android.hardware.camera2.CameraManager.TorchCallback, android.os.Handler> mTorchCallbackMap = new android.util.ArrayMap<android.hardware.camera2.CameraManager.TorchCallback, android.os.Handler>();

        private final java.lang.Object mLock = new java.lang.Object();

        // Access only through getCameraService to deal with binder death
        private android.hardware.ICameraService mCameraService;

        // Singleton, don't allow construction
        private CameraManagerGlobal() {
        }

        public static android.hardware.camera2.CameraManager.CameraManagerGlobal get() {
            return android.hardware.camera2.CameraManager.CameraManagerGlobal.gCameraManager;
        }

        @java.lang.Override
        public android.os.IBinder asBinder() {
            return this;
        }

        /**
         * Return a best-effort ICameraService.
         *
         * <p>This will be null if the camera service is not currently available. If the camera
         * service has died since the last use of the camera service, will try to reconnect to the
         * service.</p>
         */
        public android.hardware.ICameraService getCameraService() {
            synchronized(mLock) {
                connectCameraServiceLocked();
                if (mCameraService == null) {
                    android.util.Log.e(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, "Camera service is unavailable");
                }
                return mCameraService;
            }
        }

        /**
         * Connect to the camera service if it's available, and set up listeners.
         * If the service is already connected, do nothing.
         *
         * <p>Sets mCameraService to a valid pointer or null if the connection does not succeed.</p>
         */
        private void connectCameraServiceLocked() {
            // Only reconnect if necessary
            if (mCameraService != null)
                return;

            android.util.Log.i(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, "Connecting to camera service");
            android.os.IBinder cameraServiceBinder = android.os.ServiceManager.getService(android.hardware.camera2.CameraManager.CameraManagerGlobal.CAMERA_SERVICE_BINDER_NAME);
            if (cameraServiceBinder == null) {
                // Camera service is now down, leave mCameraService as null
                return;
            }
            try {
                /* flags */
                cameraServiceBinder.linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                // Camera service is now down, leave mCameraService as null
                return;
            }
            android.hardware.ICameraService cameraService = ICameraService.Stub.asInterface(cameraServiceBinder);
            try {
                android.hardware.camera2.impl.CameraMetadataNative.setupGlobalVendorTagDescriptor();
            } catch (android.os.ServiceSpecificException e) {
                handleRecoverableSetupErrors(e);
            }
            try {
                cameraService.addListener(this);
                mCameraService = cameraService;
            } catch (android.os.ServiceSpecificException e) {
                // Unexpected failure
                throw new java.lang.IllegalStateException("Failed to register a camera service listener", e);
            } catch (android.os.RemoteException e) {
                // Camera service is now down, leave mCameraService as null
            }
        }

        public void setTorchMode(java.lang.String cameraId, boolean enabled) throws android.hardware.camera2.CameraAccessException {
            synchronized(mLock) {
                if (cameraId == null) {
                    throw new java.lang.IllegalArgumentException("cameraId was null");
                }
                android.hardware.ICameraService cameraService = getCameraService();
                if (cameraService == null) {
                    throw new android.hardware.camera2.CameraAccessException(android.hardware.camera2.CameraAccessException.CAMERA_DISCONNECTED, "Camera service is currently unavailable");
                }
                try {
                    cameraService.setTorchMode(cameraId, enabled, mTorchClientBinder);
                } catch (android.os.ServiceSpecificException e) {
                    android.hardware.camera2.CameraManager.throwAsPublicException(e);
                } catch (android.os.RemoteException e) {
                    throw new android.hardware.camera2.CameraAccessException(android.hardware.camera2.CameraAccessException.CAMERA_DISCONNECTED, "Camera service is currently unavailable");
                }
            }
        }

        private void handleRecoverableSetupErrors(android.os.ServiceSpecificException e) {
            switch (e.errorCode) {
                case android.hardware.ICameraService.ERROR_DISCONNECTED :
                    android.util.Log.w(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, e.getMessage());
                    break;
                default :
                    throw new java.lang.IllegalStateException(e);
            }
        }

        private boolean isAvailable(int status) {
            switch (status) {
                case android.hardware.ICameraServiceListener.STATUS_PRESENT :
                    return true;
                default :
                    return false;
            }
        }

        private boolean validStatus(int status) {
            switch (status) {
                case android.hardware.ICameraServiceListener.STATUS_NOT_PRESENT :
                case android.hardware.ICameraServiceListener.STATUS_PRESENT :
                case android.hardware.ICameraServiceListener.STATUS_ENUMERATING :
                case android.hardware.ICameraServiceListener.STATUS_NOT_AVAILABLE :
                    return true;
                default :
                    return false;
            }
        }

        private boolean validTorchStatus(int status) {
            switch (status) {
                case android.hardware.ICameraServiceListener.TORCH_STATUS_NOT_AVAILABLE :
                case android.hardware.ICameraServiceListener.TORCH_STATUS_AVAILABLE_ON :
                case android.hardware.ICameraServiceListener.TORCH_STATUS_AVAILABLE_OFF :
                    return true;
                default :
                    return false;
            }
        }

        private void postSingleUpdate(final android.hardware.camera2.CameraManager.AvailabilityCallback callback, final android.os.Handler handler, final java.lang.String id, final int status) {
            if (isAvailable(status)) {
                handler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onCameraAvailable(id);
                    }
                });
            } else {
                handler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onCameraUnavailable(id);
                    }
                });
            }
        }

        private void postSingleTorchUpdate(final android.hardware.camera2.CameraManager.TorchCallback callback, final android.os.Handler handler, final java.lang.String id, final int status) {
            switch (status) {
                case android.hardware.ICameraServiceListener.TORCH_STATUS_AVAILABLE_ON :
                case android.hardware.ICameraServiceListener.TORCH_STATUS_AVAILABLE_OFF :
                    handler.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            callback.onTorchModeChanged(id, status == android.hardware.ICameraServiceListener.TORCH_STATUS_AVAILABLE_ON);
                        }
                    });
                    break;
                default :
                    handler.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            callback.onTorchModeUnavailable(id);
                        }
                    });
                    break;
            }
        }

        /**
         * Send the state of all known cameras to the provided listener, to initialize
         * the listener's knowledge of camera state.
         */
        private void updateCallbackLocked(android.hardware.camera2.CameraManager.AvailabilityCallback callback, android.os.Handler handler) {
            for (int i = 0; i < mDeviceStatus.size(); i++) {
                java.lang.String id = mDeviceStatus.keyAt(i);
                java.lang.Integer status = mDeviceStatus.valueAt(i);
                postSingleUpdate(callback, handler, id, status);
            }
        }

        private void onStatusChangedLocked(int status, java.lang.String id) {
            if (DEBUG) {
                android.util.Log.v(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, java.lang.String.format("Camera id %s has status changed to 0x%x", id, status));
            }
            if (!validStatus(status)) {
                android.util.Log.e(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, java.lang.String.format("Ignoring invalid device %s status 0x%x", id, status));
                return;
            }
            java.lang.Integer oldStatus = mDeviceStatus.put(id, status);
            if ((oldStatus != null) && (oldStatus == status)) {
                if (DEBUG) {
                    android.util.Log.v(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, java.lang.String.format("Device status changed to 0x%x, which is what it already was", status));
                }
                return;
            }
            // TODO: consider abstracting out this state minimization + transition
            // into a separate
            // more easily testable class
            // i.e. (new State()).addState(STATE_AVAILABLE)
            // .addState(STATE_NOT_AVAILABLE)
            // .addTransition(STATUS_PRESENT, STATE_AVAILABLE),
            // .addTransition(STATUS_NOT_PRESENT, STATE_NOT_AVAILABLE)
            // .addTransition(STATUS_ENUMERATING, STATE_NOT_AVAILABLE);
            // .addTransition(STATUS_NOT_AVAILABLE, STATE_NOT_AVAILABLE);
            // Translate all the statuses to either 'available' or 'not available'
            // available -> available         => no new update
            // not available -> not available => no new update
            if ((oldStatus != null) && (isAvailable(status) == isAvailable(oldStatus))) {
                if (DEBUG) {
                    android.util.Log.v(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, java.lang.String.format("Device status was previously available (%b), " + (" and is now again available (%b)" + "so no new client visible update will be sent"), isAvailable(oldStatus), isAvailable(status)));
                }
                return;
            }
            final int callbackCount = mCallbackMap.size();
            for (int i = 0; i < callbackCount; i++) {
                android.os.Handler handler = mCallbackMap.valueAt(i);
                final android.hardware.camera2.CameraManager.AvailabilityCallback callback = mCallbackMap.keyAt(i);
                postSingleUpdate(callback, handler, id, status);
            }
        }// onStatusChangedLocked


        private void updateTorchCallbackLocked(android.hardware.camera2.CameraManager.TorchCallback callback, android.os.Handler handler) {
            for (int i = 0; i < mTorchStatus.size(); i++) {
                java.lang.String id = mTorchStatus.keyAt(i);
                java.lang.Integer status = mTorchStatus.valueAt(i);
                postSingleTorchUpdate(callback, handler, id, status);
            }
        }

        private void onTorchStatusChangedLocked(int status, java.lang.String id) {
            if (DEBUG) {
                android.util.Log.v(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, java.lang.String.format("Camera id %s has torch status changed to 0x%x", id, status));
            }
            if (!validTorchStatus(status)) {
                android.util.Log.e(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, java.lang.String.format("Ignoring invalid device %s torch status 0x%x", id, status));
                return;
            }
            java.lang.Integer oldStatus = mTorchStatus.put(id, status);
            if ((oldStatus != null) && (oldStatus == status)) {
                if (DEBUG) {
                    android.util.Log.v(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, java.lang.String.format("Torch status changed to 0x%x, which is what it already was", status));
                }
                return;
            }
            final int callbackCount = mTorchCallbackMap.size();
            for (int i = 0; i < callbackCount; i++) {
                final android.os.Handler handler = mTorchCallbackMap.valueAt(i);
                final android.hardware.camera2.CameraManager.TorchCallback callback = mTorchCallbackMap.keyAt(i);
                postSingleTorchUpdate(callback, handler, id, status);
            }
        }// onTorchStatusChangedLocked


        /**
         * Register a callback to be notified about camera device availability with the
         * global listener singleton.
         *
         * @param callback
         * 		the new callback to send camera availability notices to
         * @param handler
         * 		The handler on which the callback should be invoked. May not be null.
         */
        public void registerAvailabilityCallback(android.hardware.camera2.CameraManager.AvailabilityCallback callback, android.os.Handler handler) {
            synchronized(mLock) {
                connectCameraServiceLocked();
                android.os.Handler oldHandler = mCallbackMap.put(callback, handler);
                // For new callbacks, provide initial availability information
                if (oldHandler == null) {
                    updateCallbackLocked(callback, handler);
                }
                // If not connected to camera service, schedule a reconnect to camera service.
                if (mCameraService == null) {
                    scheduleCameraServiceReconnectionLocked();
                }
            }
        }

        /**
         * Remove a previously-added callback; the callback will no longer receive connection and
         * disconnection callbacks, and is no longer referenced by the global listener singleton.
         *
         * @param callback
         * 		The callback to remove from the notification list
         */
        public void unregisterAvailabilityCallback(android.hardware.camera2.CameraManager.AvailabilityCallback callback) {
            synchronized(mLock) {
                mCallbackMap.remove(callback);
            }
        }

        public void registerTorchCallback(android.hardware.camera2.CameraManager.TorchCallback callback, android.os.Handler handler) {
            synchronized(mLock) {
                connectCameraServiceLocked();
                android.os.Handler oldHandler = mTorchCallbackMap.put(callback, handler);
                // For new callbacks, provide initial torch information
                if (oldHandler == null) {
                    updateTorchCallbackLocked(callback, handler);
                }
                // If not connected to camera service, schedule a reconnect to camera service.
                if (mCameraService == null) {
                    scheduleCameraServiceReconnectionLocked();
                }
            }
        }

        public void unregisterTorchCallback(android.hardware.camera2.CameraManager.TorchCallback callback) {
            synchronized(mLock) {
                mTorchCallbackMap.remove(callback);
            }
        }

        /**
         * Callback from camera service notifying the process about camera availability changes
         */
        @java.lang.Override
        public void onStatusChanged(int status, int cameraId) throws android.os.RemoteException {
            synchronized(mLock) {
                onStatusChangedLocked(status, java.lang.String.valueOf(cameraId));
            }
        }

        @java.lang.Override
        public void onTorchStatusChanged(int status, java.lang.String cameraId) throws android.os.RemoteException {
            synchronized(mLock) {
                onTorchStatusChangedLocked(status, cameraId);
            }
        }

        /**
         * Try to connect to camera service after some delay if any client registered camera
         * availability callback or torch status callback.
         */
        private void scheduleCameraServiceReconnectionLocked() {
            final android.os.Handler handler;
            if (mCallbackMap.size() > 0) {
                handler = mCallbackMap.valueAt(0);
            } else
                if (mTorchCallbackMap.size() > 0) {
                    handler = mTorchCallbackMap.valueAt(0);
                } else {
                    // Not necessary to reconnect camera service if no client registers a callback.
                    return;
                }

            if (DEBUG) {
                android.util.Log.v(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, ("Reconnecting Camera Service in " + CAMERA_SERVICE_RECONNECT_DELAY_MS) + " ms");
            }
            handler.postDelayed(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    android.hardware.ICameraService cameraService = getCameraService();
                    if (cameraService == null) {
                        synchronized(mLock) {
                            if (DEBUG) {
                                android.util.Log.v(android.hardware.camera2.CameraManager.CameraManagerGlobal.TAG, "Reconnecting Camera Service failed.");
                            }
                            scheduleCameraServiceReconnectionLocked();
                        }
                    }
                }
            }, CAMERA_SERVICE_RECONNECT_DELAY_MS);
        }

        /**
         * Listener for camera service death.
         *
         * <p>The camera service isn't supposed to die under any normal circumstances, but can be
         * turned off during debug, or crash due to bugs.  So detect that and null out the interface
         * object, so that the next calls to the manager can try to reconnect.</p>
         */
        public void binderDied() {
            synchronized(mLock) {
                // Only do this once per service death
                if (mCameraService == null)
                    return;

                mCameraService = null;
                // Tell listeners that the cameras and torch modes are unavailable and schedule a
                // reconnection to camera service. When camera service is reconnected, the camera
                // and torch statuses will be updated.
                for (int i = 0; i < mDeviceStatus.size(); i++) {
                    java.lang.String cameraId = mDeviceStatus.keyAt(i);
                    onStatusChangedLocked(ICameraServiceListener.STATUS_NOT_PRESENT, cameraId);
                }
                for (int i = 0; i < mTorchStatus.size(); i++) {
                    java.lang.String cameraId = mTorchStatus.keyAt(i);
                    onTorchStatusChangedLocked(ICameraServiceListener.TORCH_STATUS_NOT_AVAILABLE, cameraId);
                }
                scheduleCameraServiceReconnectionLocked();
            }
        }
    }
}

