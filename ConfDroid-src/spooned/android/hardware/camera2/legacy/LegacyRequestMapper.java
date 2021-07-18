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
 * Provide legacy-specific implementations of camera2 CaptureRequest for legacy devices.
 */
@java.lang.SuppressWarnings("deprecation")
public class LegacyRequestMapper {
    private static final java.lang.String TAG = "LegacyRequestMapper";

    private static final boolean DEBUG = false;

    /**
     * Default quality for android.jpeg.quality, android.jpeg.thumbnailQuality
     */
    private static final byte DEFAULT_JPEG_QUALITY = 85;

    /**
     * Set the legacy parameters using the {@link LegacyRequest legacy request}.
     *
     * <p>The legacy request's parameters are changed as a side effect of calling this
     * method.</p>
     *
     * @param legacyRequest
     * 		a non-{@code null} legacy request
     */
    public static void convertRequestMetadata(android.hardware.camera2.legacy.LegacyRequest legacyRequest) {
        android.hardware.camera2.CameraCharacteristics characteristics = legacyRequest.characteristics;
        android.hardware.camera2.CaptureRequest request = legacyRequest.captureRequest;
        android.util.Size previewSize = legacyRequest.previewSize;
        android.hardware.Camera.Parameters params = legacyRequest.parameters;
        android.graphics.Rect activeArray = characteristics.get(android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        /* scaler.cropRegion */
        android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData;
        {
            zoomData = android.hardware.camera2.legacy.ParameterUtils.convertScalerCropRegion(activeArray, request.get(android.hardware.camera2.CaptureRequest.SCALER_CROP_REGION), previewSize, params);
            if (params.isZoomSupported()) {
                params.setZoom(zoomData.zoomIndex);
            } else
                if (android.hardware.camera2.legacy.LegacyRequestMapper.DEBUG) {
                    android.util.Log.v(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "convertRequestToMetadata - zoom is not supported");
                }

        }
        /* colorCorrection.* */
        // colorCorrection.aberrationMode
        {
            int aberrationMode = /* defaultValue */
            android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, android.hardware.camera2.CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_FAST);
            if ((aberrationMode != android.hardware.camera2.CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_FAST) && (aberrationMode != android.hardware.camera2.CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY)) {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, ("convertRequestToMetadata - Ignoring unsupported " + "colorCorrection.aberrationMode = ") + aberrationMode);
            }
        }
        /* control.ae* */
        // control.aeAntibandingMode
        {
            java.lang.String legacyMode;
            java.lang.Integer antiBandingMode = request.get(android.hardware.camera2.CaptureRequest.CONTROL_AE_ANTIBANDING_MODE);
            if (antiBandingMode != null) {
                legacyMode = android.hardware.camera2.legacy.LegacyRequestMapper.convertAeAntiBandingModeToLegacy(antiBandingMode);
            } else {
                legacyMode = android.hardware.camera2.utils.ListUtils.listSelectFirstFrom(params.getSupportedAntibanding(), new java.lang.String[]{ android.hardware.Camera.Parameters.ANTIBANDING_AUTO, android.hardware.Camera.Parameters.ANTIBANDING_OFF, android.hardware.Camera.Parameters.ANTIBANDING_50HZ, android.hardware.Camera.Parameters.ANTIBANDING_60HZ });
            }
            if (legacyMode != null) {
                params.setAntibanding(legacyMode);
            }
        }
        /* control.aeRegions, afRegions */
        {
            // aeRegions
            {
                // Use aeRegions if available, fall back to using awbRegions if present
                android.hardware.camera2.params.MeteringRectangle[] aeRegions = request.get(android.hardware.camera2.CaptureRequest.CONTROL_AE_REGIONS);
                if (request.get(android.hardware.camera2.CaptureRequest.CONTROL_AWB_REGIONS) != null) {
                    android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "convertRequestMetadata - control.awbRegions setting is not " + "supported, ignoring value");
                }
                int maxNumMeteringAreas = params.getMaxNumMeteringAreas();
                java.util.List<android.hardware.Camera.Area> meteringAreaList = /* regionName */
                android.hardware.camera2.legacy.LegacyRequestMapper.convertMeteringRegionsToLegacy(activeArray, zoomData, aeRegions, maxNumMeteringAreas, "AE");
                // WAR: for b/17252693, some devices can't handle params.setFocusAreas(null).
                if (maxNumMeteringAreas > 0) {
                    params.setMeteringAreas(meteringAreaList);
                }
            }
            // afRegions
            {
                android.hardware.camera2.params.MeteringRectangle[] afRegions = request.get(android.hardware.camera2.CaptureRequest.CONTROL_AF_REGIONS);
                int maxNumFocusAreas = params.getMaxNumFocusAreas();
                java.util.List<android.hardware.Camera.Area> focusAreaList = /* regionName */
                android.hardware.camera2.legacy.LegacyRequestMapper.convertMeteringRegionsToLegacy(activeArray, zoomData, afRegions, maxNumFocusAreas, "AF");
                // WAR: for b/17252693, some devices can't handle params.setFocusAreas(null).
                if (maxNumFocusAreas > 0) {
                    params.setFocusAreas(focusAreaList);
                }
            }
        }
        // control.aeTargetFpsRange
        android.util.Range<java.lang.Integer> aeFpsRange = request.get(android.hardware.camera2.CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
        if (aeFpsRange != null) {
            int[] legacyFps = android.hardware.camera2.legacy.LegacyRequestMapper.convertAeFpsRangeToLegacy(aeFpsRange);
            int[] rangeToApply = null;
            for (int[] range : params.getSupportedPreviewFpsRange()) {
                // Round range up/down to integer FPS value
                int intRangeLow = ((int) (java.lang.Math.floor(range[0] / 1000.0))) * 1000;
                int intRangeHigh = ((int) (java.lang.Math.ceil(range[1] / 1000.0))) * 1000;
                if ((legacyFps[0] == intRangeLow) && (legacyFps[1] == intRangeHigh)) {
                    rangeToApply = range;
                    break;
                }
            }
            if (rangeToApply != null) {
                params.setPreviewFpsRange(rangeToApply[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX], rangeToApply[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX]);
            } else {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, ((("Unsupported FPS range set [" + legacyFps[0]) + ",") + legacyFps[1]) + "]");
            }
        }
        /* control */
        // control.aeExposureCompensation
        {
            android.util.Range<java.lang.Integer> compensationRange = characteristics.get(android.hardware.camera2.CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
            int compensation = /* defaultValue */
            android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, 0);
            if (!compensationRange.contains(compensation)) {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "convertRequestMetadata - control.aeExposureCompensation " + "is out of range, ignoring value");
                compensation = 0;
            }
            params.setExposureCompensation(compensation);
        }
        // control.aeLock
        {
            java.lang.Boolean aeLock = /* defaultValue */
            /* allowedValue */
            android.hardware.camera2.legacy.LegacyRequestMapper.getIfSupported(request, android.hardware.camera2.CaptureRequest.CONTROL_AE_LOCK, false, params.isAutoExposureLockSupported(), false);
            if (aeLock != null) {
                params.setAutoExposureLock(aeLock);
            }
            if (android.hardware.camera2.legacy.LegacyRequestMapper.DEBUG) {
                android.util.Log.v(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "convertRequestToMetadata - control.aeLock set to " + aeLock);
            }
            // TODO: Don't add control.aeLock to availableRequestKeys if it's not supported
        }
        // control.aeMode, flash.mode
        /* out */
        android.hardware.camera2.legacy.LegacyRequestMapper.mapAeAndFlashMode(request, params);
        // control.afMode
        {
            int afMode = /* defaultValue */
            android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.CONTROL_AF_MODE, android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_OFF);
            java.lang.String focusMode = android.hardware.camera2.legacy.LegacyMetadataMapper.convertAfModeToLegacy(afMode, params.getSupportedFocusModes());
            if (focusMode != null) {
                params.setFocusMode(focusMode);
            }
            if (android.hardware.camera2.legacy.LegacyRequestMapper.DEBUG) {
                android.util.Log.v(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, (("convertRequestToMetadata - control.afMode " + afMode) + " mapped to ") + focusMode);
            }
        }
        // control.awbMode
        {
            java.lang.Integer awbMode = /* defaultValue */
            /* allowedValue */
            android.hardware.camera2.legacy.LegacyRequestMapper.getIfSupported(request, android.hardware.camera2.CaptureRequest.CONTROL_AWB_MODE, android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_AUTO, params.getSupportedWhiteBalance() != null, android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_AUTO);
            java.lang.String whiteBalanceMode = null;
            if (awbMode != null) {
                // null iff AWB is not supported by camera1 api
                whiteBalanceMode = android.hardware.camera2.legacy.LegacyRequestMapper.convertAwbModeToLegacy(awbMode);
                params.setWhiteBalance(whiteBalanceMode);
            }
            if (android.hardware.camera2.legacy.LegacyRequestMapper.DEBUG) {
                android.util.Log.v(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, (("convertRequestToMetadata - control.awbMode " + awbMode) + " mapped to ") + whiteBalanceMode);
            }
        }
        // control.awbLock
        {
            java.lang.Boolean awbLock = /* defaultValue */
            /* allowedValue */
            android.hardware.camera2.legacy.LegacyRequestMapper.getIfSupported(request, android.hardware.camera2.CaptureRequest.CONTROL_AWB_LOCK, false, params.isAutoWhiteBalanceLockSupported(), false);
            if (awbLock != null) {
                params.setAutoWhiteBalanceLock(awbLock);
            }
            // TODO: Don't add control.awbLock to availableRequestKeys if it's not supported
        }
        // control.captureIntent
        {
            int captureIntent = /* defaultValue */
            android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.CONTROL_CAPTURE_INTENT, android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_PREVIEW);
            captureIntent = android.hardware.camera2.legacy.LegacyRequestMapper.filterSupportedCaptureIntent(captureIntent);
            params.setRecordingHint((captureIntent == android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_VIDEO_RECORD) || (captureIntent == android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_VIDEO_SNAPSHOT));
        }
        // control.videoStabilizationMode
        {
            java.lang.Integer stabMode = /* defaultValue */
            /* allowedValue */
            android.hardware.camera2.legacy.LegacyRequestMapper.getIfSupported(request, android.hardware.camera2.CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, android.hardware.camera2.CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_OFF, params.isVideoStabilizationSupported(), android.hardware.camera2.CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_OFF);
            if (stabMode != null) {
                params.setVideoStabilization(stabMode == android.hardware.camera2.CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_ON);
            }
        }
        // lens.focusDistance
        {
            boolean infinityFocusSupported = android.hardware.camera2.utils.ListUtils.listContains(params.getSupportedFocusModes(), android.hardware.Camera.Parameters.FOCUS_MODE_INFINITY);
            java.lang.Float focusDistance = /* defaultValue */
            /* allowedValue */
            android.hardware.camera2.legacy.LegacyRequestMapper.getIfSupported(request, android.hardware.camera2.CaptureRequest.LENS_FOCUS_DISTANCE, 0.0F, infinityFocusSupported, 0.0F);
            if ((focusDistance == null) || (focusDistance != 0.0F)) {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, ("convertRequestToMetadata - Ignoring android.lens.focusDistance " + infinityFocusSupported) + ", only 0.0f is supported");
            }
        }
        // control.sceneMode, control.mode
        {
            // TODO: Map FACE_PRIORITY scene mode to face detection.
            if (params.getSupportedSceneModes() != null) {
                int controlMode = /* defaultValue */
                android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.CONTROL_MODE, android.hardware.camera2.CameraMetadata.CONTROL_MODE_AUTO);
                java.lang.String modeToSet;
                switch (controlMode) {
                    case android.hardware.camera2.CameraMetadata.CONTROL_MODE_USE_SCENE_MODE :
                        {
                            int sceneMode = /* defaultValue */
                            android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.CONTROL_SCENE_MODE, android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_DISABLED);
                            java.lang.String legacySceneMode = android.hardware.camera2.legacy.LegacyMetadataMapper.convertSceneModeToLegacy(sceneMode);
                            if (legacySceneMode != null) {
                                modeToSet = legacySceneMode;
                            } else {
                                modeToSet = android.hardware.Camera.Parameters.SCENE_MODE_AUTO;
                                android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "Skipping unknown requested scene mode: " + sceneMode);
                            }
                            break;
                        }
                    case android.hardware.camera2.CameraMetadata.CONTROL_MODE_AUTO :
                        {
                            modeToSet = android.hardware.Camera.Parameters.SCENE_MODE_AUTO;
                            break;
                        }
                    default :
                        {
                            android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, ("Control mode " + controlMode) + " is unsupported, defaulting to AUTO");
                            modeToSet = android.hardware.Camera.Parameters.SCENE_MODE_AUTO;
                        }
                }
                params.setSceneMode(modeToSet);
            }
        }
        // control.effectMode
        {
            if (params.getSupportedColorEffects() != null) {
                int effectMode = /* defaultValue */
                android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.CONTROL_EFFECT_MODE, android.hardware.camera2.CameraMetadata.CONTROL_EFFECT_MODE_OFF);
                java.lang.String legacyEffectMode = android.hardware.camera2.legacy.LegacyMetadataMapper.convertEffectModeToLegacy(effectMode);
                if (legacyEffectMode != null) {
                    params.setColorEffect(legacyEffectMode);
                } else {
                    params.setColorEffect(android.hardware.Camera.Parameters.EFFECT_NONE);
                    android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "Skipping unknown requested effect mode: " + effectMode);
                }
            }
        }
        /* sensor */
        // sensor.testPattern
        {
            int testPatternMode = /* defaultValue */
            android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.SENSOR_TEST_PATTERN_MODE, android.hardware.camera2.CameraMetadata.SENSOR_TEST_PATTERN_MODE_OFF);
            if (testPatternMode != android.hardware.camera2.CameraMetadata.SENSOR_TEST_PATTERN_MODE_OFF) {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, ("convertRequestToMetadata - ignoring sensor.testPatternMode " + testPatternMode) + "; only OFF is supported");
            }
        }
        /* jpeg.* */
        // jpeg.gpsLocation
        {
            android.location.Location location = request.get(android.hardware.camera2.CaptureRequest.JPEG_GPS_LOCATION);
            if (location != null) {
                if (android.hardware.camera2.legacy.LegacyRequestMapper.checkForCompleteGpsData(location)) {
                    params.setGpsAltitude(location.getAltitude());
                    params.setGpsLatitude(location.getLatitude());
                    params.setGpsLongitude(location.getLongitude());
                    params.setGpsProcessingMethod(location.getProvider().toUpperCase());
                    params.setGpsTimestamp(location.getTime());
                } else {
                    android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "Incomplete GPS parameters provided in location " + location);
                }
            } else {
                params.removeGpsData();
            }
        }
        // jpeg.orientation
        {
            java.lang.Integer orientation = request.get(android.hardware.camera2.CaptureRequest.JPEG_ORIENTATION);
            params.setRotation(android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.JPEG_ORIENTATION, orientation == null ? 0 : orientation));
        }
        // jpeg.quality
        {
            params.setJpegQuality(0xff & android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.JPEG_QUALITY, android.hardware.camera2.legacy.LegacyRequestMapper.DEFAULT_JPEG_QUALITY));
        }
        // jpeg.thumbnailQuality
        {
            params.setJpegThumbnailQuality(0xff & android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.JPEG_THUMBNAIL_QUALITY, android.hardware.camera2.legacy.LegacyRequestMapper.DEFAULT_JPEG_QUALITY));
        }
        // jpeg.thumbnailSize
        {
            java.util.List<android.hardware.Camera.Size> sizes = params.getSupportedJpegThumbnailSizes();
            if ((sizes != null) && (sizes.size() > 0)) {
                android.util.Size s = request.get(android.hardware.camera2.CaptureRequest.JPEG_THUMBNAIL_SIZE);
                boolean invalidSize = (s == null) ? false : !android.hardware.camera2.legacy.ParameterUtils.containsSize(sizes, s.getWidth(), s.getHeight());
                if (invalidSize) {
                    android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, ("Invalid JPEG thumbnail size set " + s) + ", skipping thumbnail...");
                }
                if ((s == null) || invalidSize) {
                    // (0,0) = "no thumbnail" in Camera API 1
                    /* width */
                    /* height */
                    params.setJpegThumbnailSize(0, 0);
                } else {
                    params.setJpegThumbnailSize(s.getWidth(), s.getHeight());
                }
            }
        }
        /* noiseReduction.* */
        // noiseReduction.mode
        {
            int mode = /* defaultValue */
            android.hardware.camera2.utils.ParamsUtils.getOrDefault(request, android.hardware.camera2.CaptureRequest.NOISE_REDUCTION_MODE, android.hardware.camera2.CameraMetadata.NOISE_REDUCTION_MODE_FAST);
            if ((mode != android.hardware.camera2.CameraMetadata.NOISE_REDUCTION_MODE_FAST) && (mode != android.hardware.camera2.CameraMetadata.NOISE_REDUCTION_MODE_HIGH_QUALITY)) {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, ("convertRequestToMetadata - Ignoring unsupported " + "noiseReduction.mode = ") + mode);
            }
        }
    }

    private static boolean checkForCompleteGpsData(android.location.Location location) {
        return ((location != null) && (location.getProvider() != null)) && (location.getTime() != 0);
    }

    static int filterSupportedCaptureIntent(int captureIntent) {
        switch (captureIntent) {
            case android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_CUSTOM :
            case android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_PREVIEW :
            case android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_STILL_CAPTURE :
            case android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_VIDEO_RECORD :
            case android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_VIDEO_SNAPSHOT :
                break;
            case android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_ZERO_SHUTTER_LAG :
            case android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_MANUAL :
                captureIntent = android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_PREVIEW;
                android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, ("Unsupported control.captureIntent value " + captureIntent) + "; default to PREVIEW");
            default :
                captureIntent = android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_PREVIEW;
                android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, ("Unknown control.captureIntent value " + captureIntent) + "; default to PREVIEW");
        }
        return captureIntent;
    }

    private static java.util.List<android.hardware.Camera.Area> convertMeteringRegionsToLegacy(android.graphics.Rect activeArray, android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData, android.hardware.camera2.params.MeteringRectangle[] meteringRegions, int maxNumMeteringAreas, java.lang.String regionName) {
        if ((meteringRegions == null) || (maxNumMeteringAreas <= 0)) {
            if (maxNumMeteringAreas > 0) {
                return java.util.Arrays.asList(android.hardware.camera2.legacy.ParameterUtils.CAMERA_AREA_DEFAULT);
            } else {
                return null;
            }
        }
        // Add all non-zero weight regions to the list
        java.util.List<android.hardware.camera2.params.MeteringRectangle> meteringRectangleList = new java.util.ArrayList<>();
        for (android.hardware.camera2.params.MeteringRectangle rect : meteringRegions) {
            if (rect.getMeteringWeight() != android.hardware.camera2.params.MeteringRectangle.METERING_WEIGHT_DONT_CARE) {
                meteringRectangleList.add(rect);
            }
        }
        if (meteringRectangleList.size() == 0) {
            android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "Only received metering rectangles with weight 0.");
            return java.util.Arrays.asList(android.hardware.camera2.legacy.ParameterUtils.CAMERA_AREA_DEFAULT);
        }
        // Ignore any regions beyond our maximum supported count
        int countMeteringAreas = java.lang.Math.min(maxNumMeteringAreas, meteringRectangleList.size());
        java.util.List<android.hardware.Camera.Area> meteringAreaList = new java.util.ArrayList<>(countMeteringAreas);
        for (int i = 0; i < countMeteringAreas; ++i) {
            android.hardware.camera2.params.MeteringRectangle rect = meteringRectangleList.get(i);
            android.hardware.camera2.legacy.ParameterUtils.MeteringData meteringData = android.hardware.camera2.legacy.ParameterUtils.convertMeteringRectangleToLegacy(activeArray, rect, zoomData);
            meteringAreaList.add(meteringData.meteringArea);
        }
        if (maxNumMeteringAreas < meteringRectangleList.size()) {
            android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, (("convertMeteringRegionsToLegacy - Too many requested " + regionName) + " regions, ignoring all beyond the first ") + maxNumMeteringAreas);
        }
        if (android.hardware.camera2.legacy.LegacyRequestMapper.DEBUG) {
            android.util.Log.v(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, (("convertMeteringRegionsToLegacy - " + regionName) + " areas = ") + android.hardware.camera2.legacy.ParameterUtils.stringFromAreaList(meteringAreaList));
        }
        return meteringAreaList;
    }

    private static void mapAeAndFlashMode(android.hardware.camera2.CaptureRequest r, /* out */
    android.hardware.Camera.Parameters p) {
        int flashMode = android.hardware.camera2.utils.ParamsUtils.getOrDefault(r, android.hardware.camera2.CaptureRequest.FLASH_MODE, android.hardware.camera2.CameraMetadata.FLASH_MODE_OFF);
        int aeMode = android.hardware.camera2.utils.ParamsUtils.getOrDefault(r, android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE, android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON);
        java.util.List<java.lang.String> supportedFlashModes = p.getSupportedFlashModes();
        java.lang.String flashModeSetting = null;
        // Flash is OFF by default, on cameras that support flash
        if (android.hardware.camera2.utils.ListUtils.listContains(supportedFlashModes, android.hardware.Camera.Parameters.FLASH_MODE_OFF)) {
            flashModeSetting = android.hardware.Camera.Parameters.FLASH_MODE_OFF;
        }
        /* Map all of the control.aeMode* enums, but ignore AE_MODE_OFF since we never support it */
        // Ignore flash.mode controls unless aeMode == ON
        if (aeMode == android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON) {
            if (flashMode == android.hardware.camera2.CameraMetadata.FLASH_MODE_TORCH) {
                if (android.hardware.camera2.utils.ListUtils.listContains(supportedFlashModes, android.hardware.Camera.Parameters.FLASH_MODE_TORCH)) {
                    flashModeSetting = android.hardware.Camera.Parameters.FLASH_MODE_TORCH;
                } else {
                    android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "mapAeAndFlashMode - Ignore flash.mode == TORCH;" + "camera does not support it");
                }
            } else
                if (flashMode == android.hardware.camera2.CameraMetadata.FLASH_MODE_SINGLE) {
                    if (android.hardware.camera2.utils.ListUtils.listContains(supportedFlashModes, android.hardware.Camera.Parameters.FLASH_MODE_ON)) {
                        flashModeSetting = android.hardware.Camera.Parameters.FLASH_MODE_ON;
                    } else {
                        android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "mapAeAndFlashMode - Ignore flash.mode == SINGLE;" + "camera does not support it");
                    }
                } else {
                    // Use the default FLASH_MODE_OFF
                }

        } else
            if (aeMode == android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH) {
                if (android.hardware.camera2.utils.ListUtils.listContains(supportedFlashModes, android.hardware.Camera.Parameters.FLASH_MODE_ON)) {
                    flashModeSetting = android.hardware.Camera.Parameters.FLASH_MODE_ON;
                } else {
                    android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "mapAeAndFlashMode - Ignore control.aeMode == ON_ALWAYS_FLASH;" + "camera does not support it");
                }
            } else
                if (aeMode == android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH) {
                    if (android.hardware.camera2.utils.ListUtils.listContains(supportedFlashModes, android.hardware.Camera.Parameters.FLASH_MODE_AUTO)) {
                        flashModeSetting = android.hardware.Camera.Parameters.FLASH_MODE_AUTO;
                    } else {
                        android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "mapAeAndFlashMode - Ignore control.aeMode == ON_AUTO_FLASH;" + "camera does not support it");
                    }
                } else
                    if (aeMode == android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE) {
                        if (android.hardware.camera2.utils.ListUtils.listContains(supportedFlashModes, android.hardware.Camera.Parameters.FLASH_MODE_RED_EYE)) {
                            flashModeSetting = android.hardware.Camera.Parameters.FLASH_MODE_RED_EYE;
                        } else {
                            android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "mapAeAndFlashMode - Ignore control.aeMode == ON_AUTO_FLASH_REDEYE;" + "camera does not support it");
                        }
                    } else {
                        // Default to aeMode == ON, flash = OFF
                    }



        if (flashModeSetting != null) {
            p.setFlashMode(flashModeSetting);
        }
        if (android.hardware.camera2.legacy.LegacyRequestMapper.DEBUG) {
            android.util.Log.v(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, (((("mapAeAndFlashMode - set flash.mode (api1) to " + flashModeSetting) + ", requested (api2) ") + flashMode) + ", supported (api1) ") + android.hardware.camera2.utils.ListUtils.listToString(supportedFlashModes));
        }
    }

    /**
     * Returns null if the anti-banding mode enum is not supported.
     */
    private static java.lang.String convertAeAntiBandingModeToLegacy(int mode) {
        switch (mode) {
            case android.hardware.camera2.CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_OFF :
                {
                    return android.hardware.Camera.Parameters.ANTIBANDING_OFF;
                }
            case android.hardware.camera2.CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_50HZ :
                {
                    return android.hardware.Camera.Parameters.ANTIBANDING_50HZ;
                }
            case android.hardware.camera2.CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_60HZ :
                {
                    return android.hardware.Camera.Parameters.ANTIBANDING_60HZ;
                }
            case android.hardware.camera2.CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_AUTO :
                {
                    return android.hardware.Camera.Parameters.ANTIBANDING_AUTO;
                }
            default :
                {
                    return null;
                }
        }
    }

    private static int[] convertAeFpsRangeToLegacy(android.util.Range<java.lang.Integer> fpsRange) {
        int[] legacyFps = new int[2];
        legacyFps[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX] = fpsRange.getLower() * 1000;
        legacyFps[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX] = fpsRange.getUpper() * 1000;
        return legacyFps;
    }

    private static java.lang.String convertAwbModeToLegacy(int mode) {
        switch (mode) {
            case android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_AUTO :
                return android.hardware.Camera.Parameters.WHITE_BALANCE_AUTO;
            case android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_INCANDESCENT :
                return android.hardware.Camera.Parameters.WHITE_BALANCE_INCANDESCENT;
            case android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_FLUORESCENT :
                return android.hardware.Camera.Parameters.WHITE_BALANCE_FLUORESCENT;
            case android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_WARM_FLUORESCENT :
                return android.hardware.Camera.Parameters.WHITE_BALANCE_WARM_FLUORESCENT;
            case android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_DAYLIGHT :
                return android.hardware.Camera.Parameters.WHITE_BALANCE_DAYLIGHT;
            case android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_CLOUDY_DAYLIGHT :
                return android.hardware.Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT;
            case android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_TWILIGHT :
                return android.hardware.Camera.Parameters.WHITE_BALANCE_TWILIGHT;
            case android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_SHADE :
                return android.hardware.Camera.Parameters.WHITE_BALANCE_SHADE;
            default :
                android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, "convertAwbModeToLegacy - unrecognized control.awbMode" + mode);
                return android.hardware.Camera.Parameters.WHITE_BALANCE_AUTO;
        }
    }

    /**
     * Return {@code null} if the value is not supported, otherwise return the retrieved key's
     * value from the request (or the default value if it wasn't set).
     *
     * <p>If the fetched value in the request is equivalent to {@code allowedValue},
     * then omit the warning (e.g. turning off AF lock on a camera
     * that always has the AF lock turned off is a silent no-op), but still return {@code null}.</p>
     *
     * <p>Logs a warning to logcat if the key is not supported by api1 camera device.</p.
     */
    private static <T> T getIfSupported(android.hardware.camera2.CaptureRequest r, android.hardware.camera2.CaptureRequest.Key<T> key, T defaultValue, boolean isSupported, T allowedValue) {
        T val = android.hardware.camera2.utils.ParamsUtils.getOrDefault(r, key, defaultValue);
        if (!isSupported) {
            if (!java.util.Objects.equals(val, allowedValue)) {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyRequestMapper.TAG, (key.getName() + " is not supported; ignoring requested value ") + val);
            }
            return null;
        }
        return val;
    }
}

