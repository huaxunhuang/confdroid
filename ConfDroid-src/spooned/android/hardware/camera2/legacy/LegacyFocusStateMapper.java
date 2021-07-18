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
 * Map capture request data into legacy focus state transitions.
 *
 * <p>This object will asynchronously process auto-focus changes, so no interaction
 * with it is necessary beyond reading the current state and updating with the latest trigger.</p>
 */
@java.lang.SuppressWarnings("deprecation")
public class LegacyFocusStateMapper {
    private static java.lang.String TAG = "LegacyFocusStateMapper";

    private static final boolean DEBUG = false;

    private final android.hardware.Camera mCamera;

    private int mAfStatePrevious = android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_INACTIVE;

    private java.lang.String mAfModePrevious = null;

    /**
     * Guard mAfRun and mAfState
     */
    private final java.lang.Object mLock = new java.lang.Object();

    /**
     * Guard access with mLock
     */
    private int mAfRun = 0;

    /**
     * Guard access with mLock
     */
    private int mAfState = android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_INACTIVE;

    /**
     * Instantiate a new focus state mapper.
     *
     * @param camera
     * 		a non-{@code null} camera1 device
     * @throws NullPointerException
     * 		if any of the args were {@code null}
     */
    public LegacyFocusStateMapper(android.hardware.Camera camera) {
        mCamera = checkNotNull(camera, "camera must not be null");
    }

    /**
     * Process the AF triggers from the request as a camera1 autofocus routine.
     *
     * <p>This method should be called after the parameters are {@link LegacyRequestMapper mapped}
     * with the request.</p>
     *
     * <p>Callbacks are processed in the background, and the next call to {@link #mapResultTriggers}
     * will have the latest AF state as reflected by the camera1 callbacks.</p>
     *
     * <p>None of the arguments will be mutated.</p>
     *
     * @param captureRequest
     * 		a non-{@code null} request
     * @param parameters
     * 		a non-{@code null} parameters corresponding to this request (read-only)
     */
    public void processRequestTriggers(android.hardware.camera2.CaptureRequest captureRequest, android.hardware.Camera.Parameters parameters) {
        checkNotNull(captureRequest, "captureRequest must not be null");
        /* control.afTrigger */
        int afTrigger = android.hardware.camera2.utils.ParamsUtils.getOrDefault(captureRequest, android.hardware.camera2.CaptureRequest.CONTROL_AF_TRIGGER, android.hardware.camera2.CameraMetadata.CONTROL_AF_TRIGGER_IDLE);
        final java.lang.String afMode = parameters.getFocusMode();
        if (!java.util.Objects.equals(mAfModePrevious, afMode)) {
            if (android.hardware.camera2.legacy.LegacyFocusStateMapper.DEBUG) {
                android.util.Log.v(android.hardware.camera2.legacy.LegacyFocusStateMapper.TAG, (("processRequestTriggers - AF mode switched from " + mAfModePrevious) + " to ") + afMode);
            }
            // Switching modes always goes back to INACTIVE; ignore callbacks from previous modes
            synchronized(mLock) {
                ++mAfRun;
                mAfState = android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_INACTIVE;
            }
            mCamera.cancelAutoFocus();
        }
        mAfModePrevious = afMode;
        // Passive AF Scanning
        {
            final int currentAfRun;
            synchronized(mLock) {
                currentAfRun = mAfRun;
            }
            android.hardware.Camera.AutoFocusMoveCallback afMoveCallback = new android.hardware.Camera.AutoFocusMoveCallback() {
                @java.lang.Override
                public void onAutoFocusMoving(boolean start, android.hardware.Camera camera) {
                    synchronized(mLock) {
                        int latestAfRun = mAfRun;
                        if (android.hardware.camera2.legacy.LegacyFocusStateMapper.DEBUG) {
                            android.util.Log.v(android.hardware.camera2.legacy.LegacyFocusStateMapper.TAG, (((("onAutoFocusMoving - start " + start) + " latest AF run ") + latestAfRun) + ", last AF run ") + currentAfRun);
                        }
                        if (currentAfRun != latestAfRun) {
                            android.util.Log.d(android.hardware.camera2.legacy.LegacyFocusStateMapper.TAG, "onAutoFocusMoving - ignoring move callbacks from old af run" + currentAfRun);
                            return;
                        }
                        int newAfState = (start) ? android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_PASSIVE_SCAN : android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_PASSIVE_FOCUSED;
                        // We never send CONTROL_AF_STATE_PASSIVE_UNFOCUSED
                        switch (afMode) {
                            case android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE :
                            case android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO :
                                break;
                                // This callback should never be sent in any other AF mode
                            default :
                                android.util.Log.w(android.hardware.camera2.legacy.LegacyFocusStateMapper.TAG, "onAutoFocus - got unexpected onAutoFocus in mode " + afMode);
                        }
                        mAfState = newAfState;
                    }
                }
            };
            // Only set move callback if we can call autofocus.
            switch (afMode) {
                case android.hardware.Camera.Parameters.FOCUS_MODE_AUTO :
                case android.hardware.Camera.Parameters.FOCUS_MODE_MACRO :
                case android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE :
                case android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO :
                    mCamera.setAutoFocusMoveCallback(afMoveCallback);
            }
        }
        // AF Locking
        switch (afTrigger) {
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_TRIGGER_START :
                int afStateAfterStart;
                switch (afMode) {
                    case android.hardware.Camera.Parameters.FOCUS_MODE_AUTO :
                    case android.hardware.Camera.Parameters.FOCUS_MODE_MACRO :
                        afStateAfterStart = android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_ACTIVE_SCAN;
                        break;
                    case android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE :
                    case android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO :
                        afStateAfterStart = android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_PASSIVE_SCAN;
                        break;
                    default :
                        // EDOF, INFINITY
                        afStateAfterStart = android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_INACTIVE;
                }
                final int currentAfRun;
                synchronized(mLock) {
                    currentAfRun = ++mAfRun;
                    mAfState = afStateAfterStart;
                }
                if (android.hardware.camera2.legacy.LegacyFocusStateMapper.DEBUG) {
                    android.util.Log.v(android.hardware.camera2.legacy.LegacyFocusStateMapper.TAG, ("processRequestTriggers - got AF_TRIGGER_START, " + "new AF run is ") + currentAfRun);
                }
                // Avoid calling autofocus unless we are in a state that supports calling this.
                if (afStateAfterStart == android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_INACTIVE) {
                    break;
                }
                mCamera.autoFocus(new android.hardware.Camera.AutoFocusCallback() {
                    @java.lang.Override
                    public void onAutoFocus(boolean success, android.hardware.Camera camera) {
                        synchronized(mLock) {
                            int latestAfRun = mAfRun;
                            if (android.hardware.camera2.legacy.LegacyFocusStateMapper.DEBUG) {
                                android.util.Log.v(android.hardware.camera2.legacy.LegacyFocusStateMapper.TAG, (((("onAutoFocus - success " + success) + " latest AF run ") + latestAfRun) + ", last AF run ") + currentAfRun);
                            }
                            // Ignore old auto-focus results, since another trigger was requested
                            if (latestAfRun != currentAfRun) {
                                android.util.Log.d(android.hardware.camera2.legacy.LegacyFocusStateMapper.TAG, java.lang.String.format("onAutoFocus - ignoring AF callback " + "(old run %d, new run %d)", currentAfRun, latestAfRun));
                                return;
                            }
                            int newAfState = (success) ? android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_FOCUSED_LOCKED : android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED;
                            switch (afMode) {
                                case android.hardware.Camera.Parameters.FOCUS_MODE_AUTO :
                                case android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE :
                                case android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO :
                                case android.hardware.Camera.Parameters.FOCUS_MODE_MACRO :
                                    break;
                                    // This callback should never be sent in any other AF mode
                                default :
                                    android.util.Log.w(android.hardware.camera2.legacy.LegacyFocusStateMapper.TAG, "onAutoFocus - got unexpected onAutoFocus in mode " + afMode);
                            }
                            mAfState = newAfState;
                        }
                    }
                });
                break;
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_TRIGGER_CANCEL :
                synchronized(mLock) {
                    int updatedAfRun;
                    synchronized(mLock) {
                        updatedAfRun = ++mAfRun;
                        mAfState = android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_INACTIVE;
                    }
                    mCamera.cancelAutoFocus();
                    if (android.hardware.camera2.legacy.LegacyFocusStateMapper.DEBUG) {
                        android.util.Log.v(android.hardware.camera2.legacy.LegacyFocusStateMapper.TAG, ("processRequestTriggers - got AF_TRIGGER_CANCEL, " + "new AF run is ") + updatedAfRun);
                    }
                }
                break;
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_TRIGGER_IDLE :
                // No action necessary. The callbacks will handle transitions.
                break;
            default :
                android.util.Log.w(android.hardware.camera2.legacy.LegacyFocusStateMapper.TAG, "processRequestTriggers - ignoring unknown control.afTrigger = " + afTrigger);
        }
    }

    /**
     * Update the {@code result} camera metadata map with the new value for the
     * {@code control.afState}.
     *
     * <p>AF callbacks are processed in the background, and each call to {@link #mapResultTriggers}
     * will have the latest AF state as reflected by the camera1 callbacks.</p>
     *
     * @param result
     * 		a non-{@code null} result
     */
    public void mapResultTriggers(android.hardware.camera2.impl.CameraMetadataNative result) {
        checkNotNull(result, "result must not be null");
        int newAfState;
        synchronized(mLock) {
            newAfState = mAfState;
        }
        if (android.hardware.camera2.legacy.LegacyFocusStateMapper.DEBUG && (newAfState != mAfStatePrevious)) {
            android.util.Log.v(android.hardware.camera2.legacy.LegacyFocusStateMapper.TAG, java.lang.String.format("mapResultTriggers - afState changed from %s to %s", android.hardware.camera2.legacy.LegacyFocusStateMapper.afStateToString(mAfStatePrevious), android.hardware.camera2.legacy.LegacyFocusStateMapper.afStateToString(newAfState)));
        }
        result.set(android.hardware.camera2.CaptureResult.CONTROL_AF_STATE, newAfState);
        mAfStatePrevious = newAfState;
    }

    private static java.lang.String afStateToString(int afState) {
        switch (afState) {
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_ACTIVE_SCAN :
                return "ACTIVE_SCAN";
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_FOCUSED_LOCKED :
                return "FOCUSED_LOCKED";
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_INACTIVE :
                return "INACTIVE";
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED :
                return "NOT_FOCUSED_LOCKED";
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_PASSIVE_FOCUSED :
                return "PASSIVE_FOCUSED";
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_PASSIVE_SCAN :
                return "PASSIVE_SCAN";
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_PASSIVE_UNFOCUSED :
                return "PASSIVE_UNFOCUSED";
            default :
                return ("UNKNOWN(" + afState) + ")";
        }
    }
}

