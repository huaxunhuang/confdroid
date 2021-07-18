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
 * Utility class containing exception handling used solely by the compatibility mode shim.
 */
public class LegacyExceptionUtils {
    private static final java.lang.String TAG = "LegacyExceptionUtils";

    public static final int NO_ERROR = 0;

    public static final int PERMISSION_DENIED = -android.system.OsConstants.EPERM;

    public static final int ALREADY_EXISTS = -android.system.OsConstants.EEXIST;

    public static final int BAD_VALUE = -android.system.OsConstants.EINVAL;

    public static final int DEAD_OBJECT = -android.system.OsConstants.ENOSYS;

    public static final int INVALID_OPERATION = -android.system.OsConstants.EPIPE;

    public static final int TIMED_OUT = -android.system.OsConstants.ETIMEDOUT;

    /**
     * Checked exception thrown when a BufferQueue has been abandoned by its consumer.
     */
    public static class BufferQueueAbandonedException extends android.util.AndroidException {
        public BufferQueueAbandonedException() {
        }

        public BufferQueueAbandonedException(java.lang.String name) {
            super(name);
        }

        public BufferQueueAbandonedException(java.lang.String name, java.lang.Throwable cause) {
            super(name, cause);
        }

        public BufferQueueAbandonedException(java.lang.Exception cause) {
            super(cause);
        }
    }

    /**
     * Throw error codes used by legacy device methods as exceptions.
     *
     * <p>Non-negative return values are passed through, negative return values are thrown as
     * exceptions.</p>
     *
     * @param errorFlag
     * 		error to throw as an exception.
     * @throws {@link
     * 		BufferQueueAbandonedException} for -ENODEV.
     * @throws {@link
     * 		UnsupportedOperationException} for an unknown negative error code.
     * @return {@code errorFlag} if the value was non-negative, throws otherwise.
     */
    public static int throwOnError(int errorFlag) throws android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException {
        if (errorFlag == android.hardware.camera2.legacy.LegacyExceptionUtils.NO_ERROR) {
            return android.hardware.camera2.legacy.LegacyExceptionUtils.NO_ERROR;
        } else
            if (errorFlag == (-android.system.OsConstants.ENODEV)) {
                throw new android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException();
            }

        if (errorFlag < 0) {
            throw new java.lang.UnsupportedOperationException("Unknown error " + errorFlag);
        }
        return errorFlag;
    }

    /**
     * Throw error codes returned by the camera service as exceptions.
     *
     * @param errorFlag
     * 		error to throw as an exception.
     */
    public static void throwOnServiceError(int errorFlag) {
        int errorCode = android.hardware.ICameraService.ERROR_INVALID_OPERATION;
        java.lang.String errorMsg;
        if (errorFlag >= android.hardware.camera2.legacy.LegacyExceptionUtils.NO_ERROR) {
            return;
        } else
            if (errorFlag == android.hardware.camera2.legacy.LegacyExceptionUtils.PERMISSION_DENIED) {
                errorCode = android.hardware.ICameraService.ERROR_PERMISSION_DENIED;
                errorMsg = "Lacking privileges to access camera service";
            } else
                if (errorFlag == android.hardware.camera2.legacy.LegacyExceptionUtils.ALREADY_EXISTS) {
                    // This should be handled at the call site. Typically this isn't bad,
                    // just means we tried to do an operation that already completed.
                    return;
                } else
                    if (errorFlag == android.hardware.camera2.legacy.LegacyExceptionUtils.BAD_VALUE) {
                        errorCode = android.hardware.ICameraService.ERROR_ILLEGAL_ARGUMENT;
                        errorMsg = "Bad argument passed to camera service";
                    } else
                        if (errorFlag == android.hardware.camera2.legacy.LegacyExceptionUtils.DEAD_OBJECT) {
                            errorCode = android.hardware.ICameraService.ERROR_DISCONNECTED;
                            errorMsg = "Camera service not available";
                        } else
                            if (errorFlag == android.hardware.camera2.legacy.LegacyExceptionUtils.TIMED_OUT) {
                                errorCode = android.hardware.ICameraService.ERROR_INVALID_OPERATION;
                                errorMsg = "Operation timed out in camera service";
                            } else
                                if (errorFlag == (-android.system.OsConstants.EACCES)) {
                                    errorCode = android.hardware.ICameraService.ERROR_DISABLED;
                                    errorMsg = "Camera disabled by policy";
                                } else
                                    if (errorFlag == (-android.system.OsConstants.EBUSY)) {
                                        errorCode = android.hardware.ICameraService.ERROR_CAMERA_IN_USE;
                                        errorMsg = "Camera already in use";
                                    } else
                                        if (errorFlag == (-android.system.OsConstants.EUSERS)) {
                                            errorCode = android.hardware.ICameraService.ERROR_MAX_CAMERAS_IN_USE;
                                            errorMsg = "Maximum number of cameras in use";
                                        } else
                                            if (errorFlag == (-android.system.OsConstants.ENODEV)) {
                                                errorCode = android.hardware.ICameraService.ERROR_DISCONNECTED;
                                                errorMsg = "Camera device not available";
                                            } else
                                                if (errorFlag == (-android.system.OsConstants.EOPNOTSUPP)) {
                                                    errorCode = android.hardware.ICameraService.ERROR_DEPRECATED_HAL;
                                                    errorMsg = "Deprecated camera HAL does not support this";
                                                } else
                                                    if (errorFlag == android.hardware.camera2.legacy.LegacyExceptionUtils.INVALID_OPERATION) {
                                                        errorCode = android.hardware.ICameraService.ERROR_INVALID_OPERATION;
                                                        errorMsg = "Illegal state encountered in camera service.";
                                                    } else {
                                                        errorCode = android.hardware.ICameraService.ERROR_INVALID_OPERATION;
                                                        errorMsg = "Unknown camera device error " + errorFlag;
                                                    }











        throw new android.os.ServiceSpecificException(errorCode, errorMsg);
    }

    private LegacyExceptionUtils() {
        throw new java.lang.AssertionError();
    }
}

