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
 * Map legacy face detect callbacks into face detection results.
 */
@java.lang.SuppressWarnings("deprecation")
public class LegacyFaceDetectMapper {
    private static java.lang.String TAG = "LegacyFaceDetectMapper";

    private static final boolean DEBUG = false;

    private final android.hardware.Camera mCamera;

    /**
     * Is the camera capable of face detection?
     */
    private final boolean mFaceDetectSupported;

    /**
     * Is the camera is running face detection?
     */
    private boolean mFaceDetectEnabled = false;

    /**
     * Did the last request say to use SCENE_MODE = FACE_PRIORITY?
     */
    private boolean mFaceDetectScenePriority = false;

    /**
     * Did the last request enable the face detect mode to ON?
     */
    private boolean mFaceDetectReporting = false;

    /**
     * Synchronize access to all fields
     */
    private final java.lang.Object mLock = new java.lang.Object();

    private android.hardware.Camera.Face[] mFaces;

    private android.hardware.Camera.Face[] mFacesPrev;

    /**
     * Instantiate a new face detect mapper.
     *
     * @param camera
     * 		a non-{@code null} camera1 device
     * @param characteristics
     * 		a  non-{@code null} camera characteristics for that camera1
     * @throws NullPointerException
     * 		if any of the args were {@code null}
     */
    public LegacyFaceDetectMapper(android.hardware.Camera camera, android.hardware.camera2.CameraCharacteristics characteristics) {
        mCamera = checkNotNull(camera, "camera must not be null");
        checkNotNull(characteristics, "characteristics must not be null");
        mFaceDetectSupported = com.android.internal.util.ArrayUtils.contains(characteristics.get(android.hardware.camera2.CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES), android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_SIMPLE);
        if (!mFaceDetectSupported) {
            return;
        }
        mCamera.setFaceDetectionListener(new android.hardware.Camera.FaceDetectionListener() {
            @java.lang.Override
            public void onFaceDetection(android.hardware.Camera.Face[] faces, android.hardware.Camera camera) {
                int lengthFaces = (faces == null) ? 0 : faces.length;
                synchronized(mLock) {
                    if (mFaceDetectEnabled) {
                        mFaces = faces;
                    } else
                        if (lengthFaces > 0) {
                            // stopFaceDetectMode could race against the requests, print a debug log
                            android.util.Log.d(android.hardware.camera2.legacy.LegacyFaceDetectMapper.TAG, "onFaceDetection - Ignored some incoming faces since" + "face detection was disabled");
                        }

                }
                if (android.hardware.camera2.legacy.LegacyFaceDetectMapper.DEBUG) {
                    android.util.Log.v(android.hardware.camera2.legacy.LegacyFaceDetectMapper.TAG, ("onFaceDetection - read " + lengthFaces) + " faces");
                }
            }
        });
    }

    /**
     * Process the face detect mode from the capture request into an api1 face detect toggle.
     *
     * <p>This method should be called after the parameters are {@link LegacyRequestMapper mapped}
     * with the request.</p>
     *
     * <p>Callbacks are processed in the background, and the next call to {@link #mapResultTriggers}
     * will have the latest faces detected as reflected by the camera1 callbacks.</p>
     *
     * <p>None of the arguments will be mutated.</p>
     *
     * @param captureRequest
     * 		a non-{@code null} request
     * @param parameters
     * 		a non-{@code null} parameters corresponding to this request (read-only)
     */
    public void processFaceDetectMode(android.hardware.camera2.CaptureRequest captureRequest, android.hardware.Camera.Parameters parameters) {
        checkNotNull(captureRequest, "captureRequest must not be null");
        /* statistics.faceDetectMode */
        int fdMode = android.hardware.camera2.utils.ParamsUtils.getOrDefault(captureRequest, android.hardware.camera2.CaptureRequest.STATISTICS_FACE_DETECT_MODE, android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF);
        if ((fdMode != android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF) && (!mFaceDetectSupported)) {
            android.util.Log.w(android.hardware.camera2.legacy.LegacyFaceDetectMapper.TAG, "processFaceDetectMode - Ignoring statistics.faceDetectMode; " + "face detection is not available");
            return;
        }
        /* control.sceneMode */
        int sceneMode = android.hardware.camera2.utils.ParamsUtils.getOrDefault(captureRequest, android.hardware.camera2.CaptureRequest.CONTROL_SCENE_MODE, android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_DISABLED);
        if ((sceneMode == android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_FACE_PRIORITY) && (!mFaceDetectSupported)) {
            android.util.Log.w(android.hardware.camera2.legacy.LegacyFaceDetectMapper.TAG, "processFaceDetectMode - ignoring control.sceneMode == FACE_PRIORITY; " + "face detection is not available");
            return;
        }
        // Print some warnings out in case the values were wrong
        switch (fdMode) {
            case android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF :
            case android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_SIMPLE :
                break;
            case android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_FULL :
                android.util.Log.w(android.hardware.camera2.legacy.LegacyFaceDetectMapper.TAG, "processFaceDetectMode - statistics.faceDetectMode == FULL unsupported, " + "downgrading to SIMPLE");
                break;
            default :
                android.util.Log.w(android.hardware.camera2.legacy.LegacyFaceDetectMapper.TAG, "processFaceDetectMode - ignoring unknown statistics.faceDetectMode = " + fdMode);
                return;
        }
        boolean enableFaceDetect = (fdMode != android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF) || (sceneMode == android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_FACE_PRIORITY);
        synchronized(mLock) {
            // Enable/disable face detection if it's changed since last time
            if (enableFaceDetect != mFaceDetectEnabled) {
                if (enableFaceDetect) {
                    mCamera.startFaceDetection();
                    if (android.hardware.camera2.legacy.LegacyFaceDetectMapper.DEBUG) {
                        android.util.Log.v(android.hardware.camera2.legacy.LegacyFaceDetectMapper.TAG, "processFaceDetectMode - start face detection");
                    }
                } else {
                    mCamera.stopFaceDetection();
                    if (android.hardware.camera2.legacy.LegacyFaceDetectMapper.DEBUG) {
                        android.util.Log.v(android.hardware.camera2.legacy.LegacyFaceDetectMapper.TAG, "processFaceDetectMode - stop face detection");
                    }
                    mFaces = null;
                }
                mFaceDetectEnabled = enableFaceDetect;
                mFaceDetectScenePriority = sceneMode == android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_FACE_PRIORITY;
                mFaceDetectReporting = fdMode != android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF;
            }
        }
    }

    /**
     * Update the {@code result} camera metadata map with the new value for the
     * {@code statistics.faces} and {@code statistics.faceDetectMode}.
     *
     * <p>Face detect callbacks are processed in the background, and each call to
     * {@link #mapResultFaces} will have the latest faces as reflected by the camera1 callbacks.</p>
     *
     * <p>If the scene mode was set to {@code FACE_PRIORITY} but face detection is disabled,
     * the camera will still run face detection in the background, but no faces will be reported
     * in the capture result.</p>
     *
     * @param result
     * 		a non-{@code null} result
     * @param legacyRequest
     * 		a non-{@code null} request (read-only)
     */
    public void mapResultFaces(android.hardware.camera2.impl.CameraMetadataNative result, android.hardware.camera2.legacy.LegacyRequest legacyRequest) {
        checkNotNull(result, "result must not be null");
        checkNotNull(legacyRequest, "legacyRequest must not be null");
        android.hardware.Camera.Face[] faces;
        android.hardware.Camera.Face[] previousFaces;
        int fdMode;
        boolean fdScenePriority;
        synchronized(mLock) {
            fdMode = (mFaceDetectReporting) ? android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_SIMPLE : android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF;
            if (mFaceDetectReporting) {
                faces = mFaces;
            } else {
                faces = null;
            }
            fdScenePriority = mFaceDetectScenePriority;
            previousFaces = mFacesPrev;
            mFacesPrev = faces;
        }
        android.hardware.camera2.CameraCharacteristics characteristics = legacyRequest.characteristics;
        android.hardware.camera2.CaptureRequest request = legacyRequest.captureRequest;
        android.util.Size previewSize = legacyRequest.previewSize;
        android.hardware.Camera.Parameters params = legacyRequest.parameters;
        android.graphics.Rect activeArray = characteristics.get(android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData = android.hardware.camera2.legacy.ParameterUtils.convertScalerCropRegion(activeArray, request.get(android.hardware.camera2.CaptureRequest.SCALER_CROP_REGION), previewSize, params);
        java.util.List<android.hardware.camera2.params.Face> convertedFaces = new java.util.ArrayList<>();
        if (faces != null) {
            for (android.hardware.Camera.Face face : faces) {
                if (face != null) {
                    convertedFaces.add(android.hardware.camera2.legacy.ParameterUtils.convertFaceFromLegacy(face, activeArray, zoomData));
                } else {
                    android.util.Log.w(android.hardware.camera2.legacy.LegacyFaceDetectMapper.TAG, "mapResultFaces - read NULL face from camera1 device");
                }
            }
        }
        if (android.hardware.camera2.legacy.LegacyFaceDetectMapper.DEBUG && (previousFaces != faces)) {
            // Log only in verbose and IF the faces changed
            android.util.Log.v(android.hardware.camera2.legacy.LegacyFaceDetectMapper.TAG, "mapResultFaces - changed to " + android.hardware.camera2.utils.ListUtils.listToString(convertedFaces));
        }
        result.set(android.hardware.camera2.CaptureResult.STATISTICS_FACES, convertedFaces.toArray(new android.hardware.camera2.params.Face[0]));
        result.set(android.hardware.camera2.CaptureResult.STATISTICS_FACE_DETECT_MODE, fdMode);
        // Override scene mode with FACE_PRIORITY if the request was using FACE_PRIORITY
        if (fdScenePriority) {
            result.set(android.hardware.camera2.CaptureResult.CONTROL_SCENE_MODE, android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_FACE_PRIORITY);
        }
    }
}

