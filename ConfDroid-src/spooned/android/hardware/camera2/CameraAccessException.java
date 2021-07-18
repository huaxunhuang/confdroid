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
 * <p><code>CameraAccessException</code> is thrown if a camera device could not
 * be queried or opened by the {@link CameraManager}, or if the connection to an
 * opened {@link CameraDevice} is no longer valid.</p>
 *
 * @see CameraManager
 * @see CameraDevice
 */
public class CameraAccessException extends android.util.AndroidException {
    /**
     * The camera device is in use already.
     */
    public static final int CAMERA_IN_USE = 4;

    /**
     * The system-wide limit for number of open cameras or camera resources has
     * been reached, and more camera devices cannot be opened or torch mode
     * cannot be turned on until previous instances are closed.
     */
    public static final int MAX_CAMERAS_IN_USE = 5;

    /**
     * The camera is disabled due to a device policy, and cannot be opened.
     *
     * @see android.app.admin.DevicePolicyManager#setCameraDisabled(android.content.ComponentName, boolean)
     */
    public static final int CAMERA_DISABLED = 1;

    /**
     * The camera device is removable and has been disconnected from the Android
     * device, or the camera id used with {@link android.hardware.camera2.CameraManager#openCamera}
     * is no longer valid, or the camera service has shut down the connection due to a
     * higher-priority access request for the camera device.
     */
    public static final int CAMERA_DISCONNECTED = 2;

    /**
     * The camera device is currently in the error state.
     *
     * <p>The camera has failed to open or has failed at a later time
     * as a result of some non-user interaction. Refer to
     * {@link CameraDevice.StateCallback#onError} for the exact
     * nature of the error.</p>
     *
     * <p>No further calls to the camera will succeed. Clean up
     * the camera with {@link CameraDevice#close} and try
     * handling the error in order to successfully re-open the camera.
     * </p>
     */
    public static final int CAMERA_ERROR = 3;

    /**
     * A deprecated HAL version is in use.
     *
     * @unknown 
     */
    public static final int CAMERA_DEPRECATED_HAL = 1000;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.hardware.camera2.CameraAccessException.CAMERA_IN_USE, android.hardware.camera2.CameraAccessException.MAX_CAMERAS_IN_USE, android.hardware.camera2.CameraAccessException.CAMERA_DISABLED, android.hardware.camera2.CameraAccessException.CAMERA_DISCONNECTED, android.hardware.camera2.CameraAccessException.CAMERA_ERROR })
    public @interface AccessError {}

    // Make the eclipse warning about serializable exceptions go away
    private static final long serialVersionUID = 5630338637471475675L;// randomly generated


    private final int mReason;

    /**
     * The reason for the failure to access the camera.
     *
     * @see #CAMERA_DISABLED
     * @see #CAMERA_DISCONNECTED
     * @see #CAMERA_ERROR
     */
    @android.hardware.camera2.CameraAccessException.AccessError
    public final int getReason() {
        return mReason;
    }

    public CameraAccessException(@android.hardware.camera2.CameraAccessException.AccessError
    int problem) {
        super(android.hardware.camera2.CameraAccessException.getDefaultMessage(problem));
        mReason = problem;
    }

    public CameraAccessException(@android.hardware.camera2.CameraAccessException.AccessError
    int problem, java.lang.String message) {
        super(android.hardware.camera2.CameraAccessException.getCombinedMessage(problem, message));
        mReason = problem;
    }

    public CameraAccessException(@android.hardware.camera2.CameraAccessException.AccessError
    int problem, java.lang.String message, java.lang.Throwable cause) {
        super(android.hardware.camera2.CameraAccessException.getCombinedMessage(problem, message), cause);
        mReason = problem;
    }

    public CameraAccessException(@android.hardware.camera2.CameraAccessException.AccessError
    int problem, java.lang.Throwable cause) {
        super(android.hardware.camera2.CameraAccessException.getDefaultMessage(problem), cause);
        mReason = problem;
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String getDefaultMessage(@android.hardware.camera2.CameraAccessException.AccessError
    int problem) {
        switch (problem) {
            case android.hardware.camera2.CameraAccessException.CAMERA_IN_USE :
                return "The camera device is in use already";
            case android.hardware.camera2.CameraAccessException.MAX_CAMERAS_IN_USE :
                return "The system-wide limit for number of open cameras has been reached, " + ("and more camera devices cannot be opened until previous instances " + "are closed.");
            case android.hardware.camera2.CameraAccessException.CAMERA_DISCONNECTED :
                return "The camera device is removable and has been disconnected from the " + ("Android device, or the camera service has shut down the connection due " + "to a higher-priority access request for the camera device.");
            case android.hardware.camera2.CameraAccessException.CAMERA_DISABLED :
                return "The camera is disabled due to a device policy, and cannot be opened.";
            case android.hardware.camera2.CameraAccessException.CAMERA_ERROR :
                return "The camera device is currently in the error state; " + "no further calls to it will succeed.";
        }
        return null;
    }

    private static java.lang.String getCombinedMessage(@android.hardware.camera2.CameraAccessException.AccessError
    int problem, java.lang.String message) {
        java.lang.String problemString = android.hardware.camera2.CameraAccessException.getProblemString(problem);
        return java.lang.String.format("%s (%d): %s", problemString, problem, message);
    }

    private static java.lang.String getProblemString(int problem) {
        java.lang.String problemString;
        switch (problem) {
            case android.hardware.camera2.CameraAccessException.CAMERA_IN_USE :
                problemString = "CAMERA_IN_USE";
                break;
            case android.hardware.camera2.CameraAccessException.MAX_CAMERAS_IN_USE :
                problemString = "MAX_CAMERAS_IN_USE";
                break;
            case android.hardware.camera2.CameraAccessException.CAMERA_DISCONNECTED :
                problemString = "CAMERA_DISCONNECTED";
                break;
            case android.hardware.camera2.CameraAccessException.CAMERA_DISABLED :
                problemString = "CAMERA_DISABLED";
                break;
            case android.hardware.camera2.CameraAccessException.CAMERA_ERROR :
                problemString = "CAMERA_ERROR";
                break;
            case android.hardware.camera2.CameraAccessException.CAMERA_DEPRECATED_HAL :
                problemString = "CAMERA_DEPRECATED_HAL";
                break;
            default :
                problemString = "<UNKNOWN ERROR>";
        }
        return problemString;
    }
}

