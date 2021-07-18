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
 * Provide legacy-specific implementations of camera2 CaptureResult for legacy devices.
 */
@java.lang.SuppressWarnings("deprecation")
public class LegacyResultMapper {
    private static final java.lang.String TAG = "LegacyResultMapper";

    private static final boolean DEBUG = false;

    private android.hardware.camera2.legacy.LegacyRequest mCachedRequest = null;

    private android.hardware.camera2.impl.CameraMetadataNative mCachedResult = null;

    /**
     * Generate capture result metadata from the legacy camera request.
     *
     * <p>This method caches and reuses the result from the previous call to this method if
     * the {@code parameters} of the subsequent {@link LegacyRequest} passed to this method
     * have not changed.</p>
     *
     * @param legacyRequest
     * 		a non-{@code null} legacy request containing the latest parameters
     * @param timestamp
     * 		the timestamp to use for this result in nanoseconds.
     * @return {@link CameraMetadataNative} object containing result metadata.
     */
    public android.hardware.camera2.impl.CameraMetadataNative cachedConvertResultMetadata(android.hardware.camera2.legacy.LegacyRequest legacyRequest, long timestamp) {
        android.hardware.camera2.impl.CameraMetadataNative result;
        boolean cached;
        /* Attempt to look up the result from the cache if the parameters haven't changed */
        if (((mCachedRequest != null) && legacyRequest.parameters.same(mCachedRequest.parameters)) && legacyRequest.captureRequest.equals(mCachedRequest.captureRequest)) {
            result = new android.hardware.camera2.impl.CameraMetadataNative(mCachedResult);
            cached = true;
        } else {
            result = android.hardware.camera2.legacy.LegacyResultMapper.convertResultMetadata(legacyRequest);
            cached = false;
            // Always cache a *copy* of the metadata result,
            // since api2's client side takes ownership of it after it receives a result
            mCachedRequest = legacyRequest;
            mCachedResult = new android.hardware.camera2.impl.CameraMetadataNative(result);
        }
        /* Unconditionally set fields that change in every single frame */
        {
            // sensor.timestamp
            result.set(android.hardware.camera2.CaptureResult.SENSOR_TIMESTAMP, timestamp);
        }
        if (android.hardware.camera2.legacy.LegacyResultMapper.DEBUG) {
            android.util.Log.v(android.hardware.camera2.legacy.LegacyResultMapper.TAG, (("cachedConvertResultMetadata - cached? " + cached) + " timestamp = ") + timestamp);
            android.util.Log.v(android.hardware.camera2.legacy.LegacyResultMapper.TAG, "----- beginning of result dump ------");
            result.dumpToLog();
            android.util.Log.v(android.hardware.camera2.legacy.LegacyResultMapper.TAG, "----- end of result dump ------");
        }
        return result;
    }

    /**
     * Generate capture result metadata from the legacy camera request.
     *
     * @param legacyRequest
     * 		a non-{@code null} legacy request containing the latest parameters
     * @return a {@link CameraMetadataNative} object containing result metadata.
     */
    private static android.hardware.camera2.impl.CameraMetadataNative convertResultMetadata(android.hardware.camera2.legacy.LegacyRequest legacyRequest) {
        android.hardware.camera2.CameraCharacteristics characteristics = legacyRequest.characteristics;
        android.hardware.camera2.CaptureRequest request = legacyRequest.captureRequest;
        android.util.Size previewSize = legacyRequest.previewSize;
        android.hardware.Camera.Parameters params = legacyRequest.parameters;
        android.hardware.camera2.impl.CameraMetadataNative result = new android.hardware.camera2.impl.CameraMetadataNative();
        android.graphics.Rect activeArraySize = characteristics.get(android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData = android.hardware.camera2.legacy.ParameterUtils.convertScalerCropRegion(activeArraySize, request.get(android.hardware.camera2.CaptureRequest.SCALER_CROP_REGION), previewSize, params);
        /* colorCorrection */
        // colorCorrection.aberrationMode
        {
            result.set(android.hardware.camera2.CaptureResult.COLOR_CORRECTION_ABERRATION_MODE, request.get(android.hardware.camera2.CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE));
        }
        /* control */
        /* control.ae* */
        /* out */
        android.hardware.camera2.legacy.LegacyResultMapper.mapAe(result, characteristics, request, activeArraySize, zoomData, params);
        /* control.af* */
        /* out */
        android.hardware.camera2.legacy.LegacyResultMapper.mapAf(result, activeArraySize, zoomData, params);
        /* control.awb* */
        /* out */
        android.hardware.camera2.legacy.LegacyResultMapper.mapAwb(result, params);
        /* control.captureIntent */
        {
            int captureIntent = /* defaultValue */
            android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.CONTROL_CAPTURE_INTENT, android.hardware.camera2.CaptureRequest.CONTROL_CAPTURE_INTENT_PREVIEW);
            captureIntent = android.hardware.camera2.legacy.LegacyRequestMapper.filterSupportedCaptureIntent(captureIntent);
            result.set(android.hardware.camera2.CaptureResult.CONTROL_CAPTURE_INTENT, captureIntent);
        }
        /* control.mode */
        {
            int controlMode = android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.CONTROL_MODE, android.hardware.camera2.CameraMetadata.CONTROL_MODE_AUTO);
            if (controlMode == android.hardware.camera2.CaptureResult.CONTROL_MODE_USE_SCENE_MODE) {
                result.set(android.hardware.camera2.CaptureResult.CONTROL_MODE, android.hardware.camera2.CameraMetadata.CONTROL_MODE_USE_SCENE_MODE);
            } else {
                result.set(android.hardware.camera2.CaptureResult.CONTROL_MODE, android.hardware.camera2.CameraMetadata.CONTROL_MODE_AUTO);
            }
        }
        /* control.sceneMode */
        {
            java.lang.String legacySceneMode = params.getSceneMode();
            int mode = android.hardware.camera2.legacy.LegacyMetadataMapper.convertSceneModeFromLegacy(legacySceneMode);
            if (mode != android.hardware.camera2.legacy.LegacyMetadataMapper.UNKNOWN_MODE) {
                result.set(android.hardware.camera2.CaptureResult.CONTROL_SCENE_MODE, mode);
                // In case of SCENE_MODE == FACE_PRIORITY, LegacyFaceDetectMapper will override
                // the result to say SCENE_MODE == FACE_PRIORITY.
            } else {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyResultMapper.TAG, ("Unknown scene mode " + legacySceneMode) + " returned by camera HAL, setting to disabled.");
                result.set(android.hardware.camera2.CaptureResult.CONTROL_SCENE_MODE, android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_DISABLED);
            }
        }
        /* control.effectMode */
        {
            java.lang.String legacyEffectMode = params.getColorEffect();
            int mode = android.hardware.camera2.legacy.LegacyMetadataMapper.convertEffectModeFromLegacy(legacyEffectMode);
            if (mode != android.hardware.camera2.legacy.LegacyMetadataMapper.UNKNOWN_MODE) {
                result.set(android.hardware.camera2.CaptureResult.CONTROL_EFFECT_MODE, mode);
            } else {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyResultMapper.TAG, ("Unknown effect mode " + legacyEffectMode) + " returned by camera HAL, setting to off.");
                result.set(android.hardware.camera2.CaptureResult.CONTROL_EFFECT_MODE, android.hardware.camera2.CameraMetadata.CONTROL_EFFECT_MODE_OFF);
            }
        }
        // control.videoStabilizationMode
        {
            int stabMode = (params.isVideoStabilizationSupported() && params.getVideoStabilization()) ? android.hardware.camera2.CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_ON : android.hardware.camera2.CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_OFF;
            result.set(android.hardware.camera2.CaptureResult.CONTROL_VIDEO_STABILIZATION_MODE, stabMode);
        }
        /* flash */
        {
            // flash.mode, flash.state mapped in mapAeAndFlashMode
        }
        /* lens */
        // lens.focusDistance
        {
            if (android.hardware.Camera.Parameters.FOCUS_MODE_INFINITY.equals(params.getFocusMode())) {
                result.set(android.hardware.camera2.CaptureResult.LENS_FOCUS_DISTANCE, 0.0F);
            }
        }
        // lens.focalLength
        result.set(android.hardware.camera2.CaptureResult.LENS_FOCAL_LENGTH, params.getFocalLength());
        /* request */
        // request.pipelineDepth
        result.set(android.hardware.camera2.CaptureResult.REQUEST_PIPELINE_DEPTH, characteristics.get(android.hardware.camera2.CameraCharacteristics.REQUEST_PIPELINE_MAX_DEPTH));
        /* scaler */
        /* out */
        android.hardware.camera2.legacy.LegacyResultMapper.mapScaler(result, zoomData, params);
        /* sensor */
        // sensor.timestamp varies every frame; mapping is done in #cachedConvertResultMetadata
        {
            // Unconditionally no test patterns
            result.set(android.hardware.camera2.CaptureResult.SENSOR_TEST_PATTERN_MODE, android.hardware.camera2.CameraMetadata.SENSOR_TEST_PATTERN_MODE_OFF);
        }
        /* jpeg */
        // jpeg.gpsLocation
        result.set(android.hardware.camera2.CaptureResult.JPEG_GPS_LOCATION, request.get(android.hardware.camera2.CaptureRequest.JPEG_GPS_LOCATION));
        // jpeg.orientation
        result.set(android.hardware.camera2.CaptureResult.JPEG_ORIENTATION, request.get(android.hardware.camera2.CaptureRequest.JPEG_ORIENTATION));
        // jpeg.quality
        result.set(android.hardware.camera2.CaptureResult.JPEG_QUALITY, ((byte) (params.getJpegQuality())));
        // jpeg.thumbnailQuality
        result.set(android.hardware.camera2.CaptureResult.JPEG_THUMBNAIL_QUALITY, ((byte) (params.getJpegThumbnailQuality())));
        // jpeg.thumbnailSize
        android.hardware.Camera.Size s = params.getJpegThumbnailSize();
        if (s != null) {
            result.set(android.hardware.camera2.CaptureResult.JPEG_THUMBNAIL_SIZE, android.hardware.camera2.legacy.ParameterUtils.convertSize(s));
        } else {
            android.util.Log.w(android.hardware.camera2.legacy.LegacyResultMapper.TAG, "Null thumbnail size received from parameters.");
        }
        /* noiseReduction.* */
        // noiseReduction.mode
        result.set(android.hardware.camera2.CaptureResult.NOISE_REDUCTION_MODE, request.get(android.hardware.camera2.CaptureRequest.NOISE_REDUCTION_MODE));
        return result;
    }

    private static void mapAe(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.camera2.CameraCharacteristics characteristics, android.hardware.camera2.CaptureRequest request, android.graphics.Rect activeArray, android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData, /* out */
    android.hardware.Camera.Parameters p) {
        // control.aeAntiBandingMode
        {
            int antiBandingMode = android.hardware.camera2.legacy.LegacyMetadataMapper.convertAntiBandingModeOrDefault(p.getAntibanding());
            m.set(android.hardware.camera2.CaptureResult.CONTROL_AE_ANTIBANDING_MODE, antiBandingMode);
        }
        // control.aeExposureCompensation
        {
            m.set(android.hardware.camera2.CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION, p.getExposureCompensation());
        }
        // control.aeLock
        {
            boolean lock = (p.isAutoExposureLockSupported()) ? p.getAutoExposureLock() : false;
            m.set(android.hardware.camera2.CaptureResult.CONTROL_AE_LOCK, lock);
            if (android.hardware.camera2.legacy.LegacyResultMapper.DEBUG) {
                android.util.Log.v(android.hardware.camera2.legacy.LegacyResultMapper.TAG, (("mapAe - android.control.aeLock = " + lock) + ", supported = ") + p.isAutoExposureLockSupported());
            }
            java.lang.Boolean requestLock = request.get(android.hardware.camera2.CaptureRequest.CONTROL_AE_LOCK);
            if ((requestLock != null) && (requestLock != lock)) {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyResultMapper.TAG, (("mapAe - android.control.aeLock was requested to " + requestLock) + " but resulted in ") + lock);
            }
        }
        // control.aeMode, flash.mode, flash.state
        android.hardware.camera2.legacy.LegacyResultMapper.mapAeAndFlashMode(m, characteristics, p);
        // control.aeState
        if (android.hardware.camera2.legacy.LegacyMetadataMapper.LIE_ABOUT_AE_STATE) {
            // Lie to pass CTS temporarily.
            // TODO: Implement precapture trigger, after which we can report CONVERGED ourselves
            m.set(android.hardware.camera2.CaptureResult.CONTROL_AE_STATE, android.hardware.camera2.CameraMetadata.CONTROL_AE_STATE_CONVERGED);
        }
        // control.aeRegions
        if (p.getMaxNumMeteringAreas() > 0) {
            if (android.hardware.camera2.legacy.LegacyResultMapper.DEBUG) {
                java.lang.String meteringAreas = p.get("metering-areas");
                android.util.Log.v(android.hardware.camera2.legacy.LegacyResultMapper.TAG, "mapAe - parameter dump; metering-areas: " + meteringAreas);
            }
            android.hardware.camera2.params.MeteringRectangle[] meteringRectArray = android.hardware.camera2.legacy.LegacyResultMapper.getMeteringRectangles(activeArray, zoomData, p.getMeteringAreas(), "AE");
            m.set(android.hardware.camera2.CaptureResult.CONTROL_AE_REGIONS, meteringRectArray);
        }
    }

    private static void mapAf(android.hardware.camera2.impl.CameraMetadataNative m, android.graphics.Rect activeArray, android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData, android.hardware.Camera.Parameters p) {
        // control.afMode
        m.set(android.hardware.camera2.CaptureResult.CONTROL_AF_MODE, android.hardware.camera2.legacy.LegacyResultMapper.convertLegacyAfMode(p.getFocusMode()));
        // control.afRegions
        if (p.getMaxNumFocusAreas() > 0) {
            if (android.hardware.camera2.legacy.LegacyResultMapper.DEBUG) {
                java.lang.String focusAreas = p.get("focus-areas");
                android.util.Log.v(android.hardware.camera2.legacy.LegacyResultMapper.TAG, "mapAe - parameter dump; focus-areas: " + focusAreas);
            }
            android.hardware.camera2.params.MeteringRectangle[] meteringRectArray = android.hardware.camera2.legacy.LegacyResultMapper.getMeteringRectangles(activeArray, zoomData, p.getFocusAreas(), "AF");
            m.set(android.hardware.camera2.CaptureResult.CONTROL_AF_REGIONS, meteringRectArray);
        }
    }

    private static void mapAwb(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        // control.awbLock
        {
            boolean lock = (p.isAutoWhiteBalanceLockSupported()) ? p.getAutoWhiteBalanceLock() : false;
            m.set(android.hardware.camera2.CaptureResult.CONTROL_AWB_LOCK, lock);
        }
        // control.awbMode
        {
            int awbMode = android.hardware.camera2.legacy.LegacyResultMapper.convertLegacyAwbMode(p.getWhiteBalance());
            m.set(android.hardware.camera2.CaptureResult.CONTROL_AWB_MODE, awbMode);
        }
    }

    private static android.hardware.camera2.params.MeteringRectangle[] getMeteringRectangles(android.graphics.Rect activeArray, android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData, java.util.List<android.hardware.Camera.Area> meteringAreaList, java.lang.String regionName) {
        java.util.List<android.hardware.camera2.params.MeteringRectangle> meteringRectList = new java.util.ArrayList<>();
        if (meteringAreaList != null) {
            for (android.hardware.Camera.Area area : meteringAreaList) {
                android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle rect = android.hardware.camera2.legacy.ParameterUtils.convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, area);
                meteringRectList.add(rect.toMetering());
            }
        }
        if (android.hardware.camera2.legacy.LegacyResultMapper.DEBUG) {
            android.util.Log.v(android.hardware.camera2.legacy.LegacyResultMapper.TAG, (("Metering rectangles for " + regionName) + ": ") + android.hardware.camera2.utils.ListUtils.listToString(meteringRectList));
        }
        return meteringRectList.toArray(new android.hardware.camera2.params.MeteringRectangle[0]);
    }

    /**
     * Map results for control.aeMode, flash.mode, flash.state
     */
    private static void mapAeAndFlashMode(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.camera2.CameraCharacteristics characteristics, android.hardware.Camera.Parameters p) {
        // Default: AE mode on but flash never fires
        int flashMode = android.hardware.camera2.CameraMetadata.FLASH_MODE_OFF;
        // If there is no flash on this camera, the state is always unavailable
        // , otherwise it's only known for TORCH/SINGLE modes
        java.lang.Integer flashState = (characteristics.get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE)) ? null : android.hardware.camera2.CameraMetadata.FLASH_STATE_UNAVAILABLE;
        int aeMode = android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON;
        java.lang.String flashModeSetting = p.getFlashMode();
        if (flashModeSetting != null) {
            switch (flashModeSetting) {
                case android.hardware.Camera.Parameters.FLASH_MODE_OFF :
                    break;// ok, using default

                case android.hardware.Camera.Parameters.FLASH_MODE_AUTO :
                    aeMode = android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH;
                    break;
                case android.hardware.Camera.Parameters.FLASH_MODE_ON :
                    // flashMode = SINGLE + aeMode = ON is indistinguishable from ON_ALWAYS_FLASH
                    flashMode = android.hardware.camera2.CameraMetadata.FLASH_MODE_SINGLE;
                    aeMode = android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH;
                    flashState = android.hardware.camera2.CameraMetadata.FLASH_STATE_FIRED;
                    break;
                case android.hardware.Camera.Parameters.FLASH_MODE_RED_EYE :
                    aeMode = android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE;
                    break;
                case android.hardware.Camera.Parameters.FLASH_MODE_TORCH :
                    flashMode = android.hardware.camera2.CameraMetadata.FLASH_MODE_TORCH;
                    flashState = android.hardware.camera2.CameraMetadata.FLASH_STATE_FIRED;
                    break;
                default :
                    android.util.Log.w(android.hardware.camera2.legacy.LegacyResultMapper.TAG, "mapAeAndFlashMode - Ignoring unknown flash mode " + p.getFlashMode());
            }
        }
        // flash.state
        m.set(android.hardware.camera2.CaptureResult.FLASH_STATE, flashState);
        // flash.mode
        m.set(android.hardware.camera2.CaptureResult.FLASH_MODE, flashMode);
        // control.aeMode
        m.set(android.hardware.camera2.CaptureResult.CONTROL_AE_MODE, aeMode);
    }

    private static int convertLegacyAfMode(java.lang.String mode) {
        if (mode == null) {
            android.util.Log.w(android.hardware.camera2.legacy.LegacyResultMapper.TAG, "convertLegacyAfMode - no AF mode, default to OFF");
            return android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_OFF;
        }
        switch (mode) {
            case android.hardware.Camera.Parameters.FOCUS_MODE_AUTO :
                return android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_AUTO;
            case android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE :
                return android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE;
            case android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO :
                return android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO;
            case android.hardware.Camera.Parameters.FOCUS_MODE_EDOF :
                return android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_EDOF;
            case android.hardware.Camera.Parameters.FOCUS_MODE_MACRO :
                return android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_MACRO;
            case android.hardware.Camera.Parameters.FOCUS_MODE_FIXED :
                return android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_OFF;
            case android.hardware.Camera.Parameters.FOCUS_MODE_INFINITY :
                return android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_OFF;
            default :
                android.util.Log.w(android.hardware.camera2.legacy.LegacyResultMapper.TAG, ("convertLegacyAfMode - unknown mode " + mode) + " , ignoring");
                return android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_OFF;
        }
    }

    private static int convertLegacyAwbMode(java.lang.String mode) {
        if (mode == null) {
            // OK: camera1 api may not support changing WB modes; assume AUTO
            return android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_AUTO;
        }
        switch (mode) {
            case android.hardware.Camera.Parameters.WHITE_BALANCE_AUTO :
                return android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_AUTO;
            case android.hardware.Camera.Parameters.WHITE_BALANCE_INCANDESCENT :
                return android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_INCANDESCENT;
            case android.hardware.Camera.Parameters.WHITE_BALANCE_FLUORESCENT :
                return android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_FLUORESCENT;
            case android.hardware.Camera.Parameters.WHITE_BALANCE_WARM_FLUORESCENT :
                return android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_WARM_FLUORESCENT;
            case android.hardware.Camera.Parameters.WHITE_BALANCE_DAYLIGHT :
                return android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_DAYLIGHT;
            case android.hardware.Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT :
                return android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_CLOUDY_DAYLIGHT;
            case android.hardware.Camera.Parameters.WHITE_BALANCE_TWILIGHT :
                return android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_TWILIGHT;
            case android.hardware.Camera.Parameters.WHITE_BALANCE_SHADE :
                return android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_SHADE;
            default :
                android.util.Log.w(android.hardware.camera2.legacy.LegacyResultMapper.TAG, "convertAwbMode - unrecognized WB mode " + mode);
                return android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_AUTO;
        }
    }

    /**
     * Map results for scaler.*
     */
    private static void mapScaler(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData, /* out */
    android.hardware.Camera.Parameters p) {
        /* scaler.cropRegion */
        {
            m.set(android.hardware.camera2.CaptureResult.SCALER_CROP_REGION, zoomData.reportedCrop);
        }
    }
}

