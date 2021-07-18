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
 * Provide legacy-specific implementations of camera2 metadata for legacy devices, such as the
 * camera characteristics.
 */
@java.lang.SuppressWarnings("deprecation")
public class LegacyMetadataMapper {
    private static final java.lang.String TAG = "LegacyMetadataMapper";

    private static final boolean DEBUG = false;

    private static final long NS_PER_MS = 1000000;

    // from graphics.h
    public static final int HAL_PIXEL_FORMAT_RGBA_8888 = android.graphics.PixelFormat.RGBA_8888;

    public static final int HAL_PIXEL_FORMAT_BGRA_8888 = 0x5;

    public static final int HAL_PIXEL_FORMAT_IMPLEMENTATION_DEFINED = 0x22;

    public static final int HAL_PIXEL_FORMAT_BLOB = 0x21;

    // for metadata
    private static final float LENS_INFO_MINIMUM_FOCUS_DISTANCE_FIXED_FOCUS = 0.0F;

    private static final int REQUEST_MAX_NUM_OUTPUT_STREAMS_COUNT_RAW = 0;// no raw support


    private static final int REQUEST_MAX_NUM_OUTPUT_STREAMS_COUNT_PROC = 3;// preview, video, cb


    private static final int REQUEST_MAX_NUM_OUTPUT_STREAMS_COUNT_PROC_STALL = 1;// 1 jpeg only


    private static final int REQUEST_MAX_NUM_INPUT_STREAMS_COUNT = 0;// no reprocessing


    /**
     * Assume 3 HAL1 stages: Exposure, Read-out, Post-Processing
     */
    private static final int REQUEST_PIPELINE_MAX_DEPTH_HAL1 = 3;

    /**
     * Assume 3 shim stages: Preview input, Split output, Format conversion for output
     */
    private static final int REQUEST_PIPELINE_MAX_DEPTH_OURS = 3;

    /* TODO: Update above maxDepth values once we do more performance measurements */
    // For approximating JPEG stall durations
    private static final long APPROXIMATE_CAPTURE_DELAY_MS = 200;// 200 milliseconds


    private static final long APPROXIMATE_SENSOR_AREA_PX = 1 << 23;// 8 megapixels


    private static final long APPROXIMATE_JPEG_ENCODE_TIME_MS = 600;// 600 milliseconds


    static final int UNKNOWN_MODE = -1;

    // Maximum difference between a preview size aspect ratio and a jpeg size aspect ratio
    private static final float PREVIEW_ASPECT_RATIO_TOLERANCE = 0.01F;

    /* Development hijinks: Lie about not supporting certain capabilities

    - Unblock some CTS tests from running whose main intent is not the metadata itself

    TODO: Remove these constants and strip out any code that previously relied on them
    being set to true.
     */
    static final boolean LIE_ABOUT_AE_STATE = false;

    static final boolean LIE_ABOUT_AE_MAX_REGIONS = false;

    static final boolean LIE_ABOUT_AF = false;

    static final boolean LIE_ABOUT_AF_MAX_REGIONS = false;

    static final boolean LIE_ABOUT_AWB_STATE = false;

    static final boolean LIE_ABOUT_AWB = false;

    /**
     * Create characteristics for a legacy device by mapping the {@code parameters}
     * and {@code info}
     *
     * @param parameters
     * 		A non-{@code null} parameters set
     * @param info
     * 		Camera info with camera facing direction and angle of orientation
     * @return static camera characteristics for a camera device
     * @throws NullPointerException
     * 		if any of the args were {@code null}
     */
    public static android.hardware.camera2.CameraCharacteristics createCharacteristics(android.hardware.Camera.Parameters parameters, android.hardware.Camera.CameraInfo info) {
        checkNotNull(parameters, "parameters must not be null");
        checkNotNull(info, "info must not be null");
        java.lang.String paramStr = parameters.flatten();
        android.hardware.CameraInfo outerInfo = new android.hardware.CameraInfo();
        outerInfo.info = info;
        return android.hardware.camera2.legacy.LegacyMetadataMapper.createCharacteristics(paramStr, outerInfo);
    }

    /**
     * Create characteristics for a legacy device by mapping the {@code parameters}
     * and {@code info}
     *
     * @param parameters
     * 		A string parseable by {@link Camera.Parameters#unflatten}
     * @param info
     * 		Camera info with camera facing direction and angle of orientation
     * @return static camera characteristics for a camera device
     * @throws NullPointerException
     * 		if any of the args were {@code null}
     */
    public static android.hardware.camera2.CameraCharacteristics createCharacteristics(java.lang.String parameters, android.hardware.CameraInfo info) {
        checkNotNull(parameters, "parameters must not be null");
        checkNotNull(info, "info must not be null");
        checkNotNull(info.info, "info.info must not be null");
        android.hardware.camera2.impl.CameraMetadataNative m = new android.hardware.camera2.impl.CameraMetadataNative();
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapCharacteristicsFromInfo(m, info.info);
        android.hardware.Camera.Parameters params = android.hardware.Camera.getEmptyParameters();
        params.unflatten(parameters);
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapCharacteristicsFromParameters(m, params);
        if (android.hardware.camera2.legacy.LegacyMetadataMapper.DEBUG) {
            android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, "createCharacteristics metadata:");
            android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, "--------------------------------------------------- (start)");
            m.dumpToLog();
            android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, "--------------------------------------------------- (end)");
        }
        return new android.hardware.camera2.CameraCharacteristics(m);
    }

    private static void mapCharacteristicsFromInfo(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.CameraInfo i) {
        m.set(android.hardware.camera2.CameraCharacteristics.LENS_FACING, i.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK ? android.hardware.camera2.CameraMetadata.LENS_FACING_BACK : android.hardware.camera2.CameraMetadata.LENS_FACING_FRONT);
        m.set(android.hardware.camera2.CameraCharacteristics.SENSOR_ORIENTATION, i.orientation);
    }

    private static void mapCharacteristicsFromParameters(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        /* colorCorrection.* */
        m.set(android.hardware.camera2.CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES, new int[]{ android.hardware.camera2.CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_FAST, android.hardware.camera2.CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY });
        /* control.ae* */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapControlAe(m, p);
        /* control.af* */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapControlAf(m, p);
        /* control.awb* */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapControlAwb(m, p);
        /* control.*
        - Anything that doesn't have a set of related fields
         */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapControlOther(m, p);
        /* lens.* */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapLens(m, p);
        /* flash.* */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapFlash(m, p);
        /* jpeg.* */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapJpeg(m, p);
        /* noiseReduction.* */
        m.set(android.hardware.camera2.CameraCharacteristics.NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES, new int[]{ android.hardware.camera2.CameraMetadata.NOISE_REDUCTION_MODE_FAST, android.hardware.camera2.CameraMetadata.NOISE_REDUCTION_MODE_HIGH_QUALITY });
        /* scaler.* */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapScaler(m, p);
        /* sensor.* */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapSensor(m, p);
        /* statistics.* */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapStatistics(m, p);
        /* sync.* */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapSync(m, p);
        /* info.supportedHardwareLevel */
        m.set(android.hardware.camera2.CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL, android.hardware.camera2.CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY);
        /* scaler.availableStream*, scaler.available*Durations, sensor.info.maxFrameDuration */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapScalerStreamConfigs(m, p);
        // Order matters below: Put this last so that we can read the metadata set previously
        /* request.* */
        android.hardware.camera2.legacy.LegacyMetadataMapper.mapRequest(m, p);
    }

    private static void mapScalerStreamConfigs(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        java.util.ArrayList<android.hardware.camera2.params.StreamConfiguration> availableStreamConfigs = new java.util.ArrayList<>();
        /* Implementation-defined (preview, recording, etc) -> use camera1 preview sizes
        YUV_420_888 cpu callbacks -> use camera1 preview sizes
        Other preview callbacks (CPU) -> use camera1 preview sizes
        JPEG still capture -> use camera1 still capture sizes

        Use platform-internal format constants here, since StreamConfigurationMap does the
        remapping to public format constants.
         */
        java.util.List<android.hardware.Camera.Size> previewSizes = p.getSupportedPreviewSizes();
        java.util.List<android.hardware.Camera.Size> jpegSizes = p.getSupportedPictureSizes();
        /* Work-around for b/17589233:
        - Some HALs's largest preview size aspect ratio does not match the largest JPEG size AR
        - This causes a large amount of problems with focus/metering because it's relative to
          preview, making the difference between the JPEG and preview viewport inaccessible
        - This boils down to metering or focusing areas being "arbitrarily" cropped
          in the capture result.
        - Work-around the HAL limitations by removing all of the largest preview sizes
          until we get one with the same aspect ratio as the jpeg size.
         */
        {
            android.hardware.camera2.legacy.SizeAreaComparator areaComparator = new android.hardware.camera2.legacy.SizeAreaComparator();
            // Sort preview to min->max
            java.util.Collections.sort(previewSizes, areaComparator);
            android.hardware.Camera.Size maxJpegSize = android.hardware.camera2.legacy.SizeAreaComparator.findLargestByArea(jpegSizes);
            float jpegAspectRatio = (maxJpegSize.width * 1.0F) / maxJpegSize.height;
            if (android.hardware.camera2.legacy.LegacyMetadataMapper.DEBUG) {
                android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, java.lang.String.format("mapScalerStreamConfigs - largest JPEG area %dx%d, AR=%f", maxJpegSize.width, maxJpegSize.height, jpegAspectRatio));
            }
            // Now remove preview sizes from the end (largest->smallest) until aspect ratio matches
            while (!previewSizes.isEmpty()) {
                int index = previewSizes.size() - 1;// max is always at the end

                android.hardware.Camera.Size size = previewSizes.get(index);
                float previewAspectRatio = (size.width * 1.0F) / size.height;
                if (java.lang.Math.abs(jpegAspectRatio - previewAspectRatio) >= android.hardware.camera2.legacy.LegacyMetadataMapper.PREVIEW_ASPECT_RATIO_TOLERANCE) {
                    previewSizes.remove(index);// Assume removing from end is O(1)

                    if (android.hardware.camera2.legacy.LegacyMetadataMapper.DEBUG) {
                        android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, java.lang.String.format("mapScalerStreamConfigs - removed preview size %dx%d, AR=%f " + "was not the same", size.width, size.height, previewAspectRatio));
                    }
                } else {
                    break;
                }
            } 
            if (previewSizes.isEmpty()) {
                // Fall-back to the original faulty behavior, but at least work
                android.util.Log.w(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, ("mapScalerStreamConfigs - failed to find any preview size matching " + "JPEG aspect ratio ") + jpegAspectRatio);
                previewSizes = p.getSupportedPreviewSizes();
            }
            // Sort again, this time in descending order max->min
            java.util.Collections.sort(previewSizes, java.util.Collections.reverseOrder(areaComparator));
        }
        android.hardware.camera2.legacy.LegacyMetadataMapper.appendStreamConfig(availableStreamConfigs, android.hardware.camera2.legacy.LegacyMetadataMapper.HAL_PIXEL_FORMAT_IMPLEMENTATION_DEFINED, previewSizes);
        android.hardware.camera2.legacy.LegacyMetadataMapper.appendStreamConfig(availableStreamConfigs, android.graphics.ImageFormat.YUV_420_888, previewSizes);
        for (int format : p.getSupportedPreviewFormats()) {
            if (android.graphics.ImageFormat.isPublicFormat(format) && (format != android.graphics.ImageFormat.NV21)) {
                android.hardware.camera2.legacy.LegacyMetadataMapper.appendStreamConfig(availableStreamConfigs, format, previewSizes);
            } else
                if (android.hardware.camera2.legacy.LegacyMetadataMapper.DEBUG) {
                    /* Do not add any formats unknown to us
                    (since it would fail runtime checks in StreamConfigurationMap)
                     */
                    android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, java.lang.String.format("mapStreamConfigs - Skipping format %x", format));
                }

        }
        android.hardware.camera2.legacy.LegacyMetadataMapper.appendStreamConfig(availableStreamConfigs, android.hardware.camera2.legacy.LegacyMetadataMapper.HAL_PIXEL_FORMAT_BLOB, p.getSupportedPictureSizes());
        /* scaler.availableStreamConfigurations */
        m.set(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_STREAM_CONFIGURATIONS, availableStreamConfigs.toArray(new android.hardware.camera2.params.StreamConfiguration[0]));
        /* scaler.availableMinFrameDurations */
        // No frame durations available
        m.set(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_MIN_FRAME_DURATIONS, new android.hardware.camera2.params.StreamConfigurationDuration[0]);
        android.hardware.camera2.params.StreamConfigurationDuration[] jpegStalls = new android.hardware.camera2.params.StreamConfigurationDuration[jpegSizes.size()];
        int i = 0;
        long longestStallDuration = -1;
        for (android.hardware.Camera.Size s : jpegSizes) {
            long stallDuration = android.hardware.camera2.legacy.LegacyMetadataMapper.calculateJpegStallDuration(s);
            jpegStalls[i++] = new android.hardware.camera2.params.StreamConfigurationDuration(android.hardware.camera2.legacy.LegacyMetadataMapper.HAL_PIXEL_FORMAT_BLOB, s.width, s.height, stallDuration);
            if (longestStallDuration < stallDuration) {
                longestStallDuration = stallDuration;
            }
        }
        /* scaler.availableStallDurations */
        // Set stall durations for jpeg, other formats use default stall duration
        m.set(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_STALL_DURATIONS, jpegStalls);
        /* sensor.info.maxFrameDuration */
        m.set(android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_MAX_FRAME_DURATION, longestStallDuration);
    }

    @java.lang.SuppressWarnings({ "unchecked" })
    private static void mapControlAe(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        /* control.aeAvailableAntiBandingModes */
        java.util.List<java.lang.String> antiBandingModes = p.getSupportedAntibanding();
        if ((antiBandingModes != null) && (antiBandingModes.size() > 0)) {
            // antibanding is optional
            int[] modes = new int[antiBandingModes.size()];
            int j = 0;
            for (java.lang.String mode : antiBandingModes) {
                int convertedMode = android.hardware.camera2.legacy.LegacyMetadataMapper.convertAntiBandingMode(mode);
                if (android.hardware.camera2.legacy.LegacyMetadataMapper.DEBUG && (convertedMode == (-1))) {
                    android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, ("Antibanding mode " + (mode == null ? "NULL" : mode)) + " not supported, skipping...");
                } else {
                    modes[j++] = convertedMode;
                }
            }
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES, java.util.Arrays.copyOf(modes, j));
        } else {
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES, new int[0]);
        }
        /* control.aeAvailableTargetFpsRanges */
        {
            java.util.List<int[]> fpsRanges = p.getSupportedPreviewFpsRange();
            if (fpsRanges == null) {
                throw new java.lang.AssertionError("Supported FPS ranges cannot be null.");
            }
            int rangesSize = fpsRanges.size();
            if (rangesSize <= 0) {
                throw new java.lang.AssertionError("At least one FPS range must be supported.");
            }
            android.util.Range<java.lang.Integer>[] ranges = new android.util.Range[rangesSize];
            int i = 0;
            for (int[] r : fpsRanges) {
                ranges[i++] = android.util.Range.create(((int) (java.lang.Math.floor(r[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX] / 1000.0))), ((int) (java.lang.Math.ceil(r[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX] / 1000.0))));
            }
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES, ranges);
        }
        /* control.aeAvailableModes */
        {
            java.util.List<java.lang.String> flashModes = p.getSupportedFlashModes();
            java.lang.String[] flashModeStrings = new java.lang.String[]{ android.hardware.Camera.Parameters.FLASH_MODE_OFF, android.hardware.Camera.Parameters.FLASH_MODE_AUTO, android.hardware.Camera.Parameters.FLASH_MODE_ON, android.hardware.Camera.Parameters.FLASH_MODE_RED_EYE, // Map these manually
            android.hardware.Camera.Parameters.FLASH_MODE_TORCH };
            int[] flashModeInts = new int[]{ android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON, android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH, android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH, android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE };
            int[] aeAvail = android.hardware.camera2.utils.ArrayUtils.convertStringListToIntArray(flashModes, flashModeStrings, flashModeInts);
            // No flash control -> AE is always on
            if ((aeAvail == null) || (aeAvail.length == 0)) {
                aeAvail = new int[]{ android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON };
            }
            // Note that AE_MODE_OFF is never available.
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES, aeAvail);
        }
        /* control.aeCompensationRanges */
        {
            int min = p.getMinExposureCompensation();
            int max = p.getMaxExposureCompensation();
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE, android.util.Range.create(min, max));
        }
        /* control.aeCompensationStep */
        {
            float step = p.getExposureCompensationStep();
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP, android.hardware.camera2.utils.ParamsUtils.createRational(step));
        }
        /* control.aeLockAvailable */
        {
            boolean aeLockAvailable = p.isAutoExposureLockSupported();
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AE_LOCK_AVAILABLE, aeLockAvailable);
        }
    }

    @java.lang.SuppressWarnings({ "unchecked" })
    private static void mapControlAf(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        /* control.afAvailableModes */
        {
            java.util.List<java.lang.String> focusModes = p.getSupportedFocusModes();
            java.lang.String[] focusModeStrings = new java.lang.String[]{ android.hardware.Camera.Parameters.FOCUS_MODE_AUTO, android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE, android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO, android.hardware.Camera.Parameters.FOCUS_MODE_EDOF, android.hardware.Camera.Parameters.FOCUS_MODE_INFINITY, android.hardware.Camera.Parameters.FOCUS_MODE_MACRO, android.hardware.Camera.Parameters.FOCUS_MODE_FIXED };
            int[] focusModeInts = new int[]{ android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_AUTO, android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE, android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO, android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_EDOF, android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_OFF, android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_MACRO, android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_OFF };
            java.util.List<java.lang.Integer> afAvail = android.hardware.camera2.utils.ArrayUtils.convertStringListToIntList(focusModes, focusModeStrings, focusModeInts);
            // No AF modes supported? That's unpossible!
            if ((afAvail == null) || (afAvail.size() == 0)) {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, "No AF modes supported (HAL bug); defaulting to AF_MODE_OFF only");
                afAvail = /* capacity */
                new java.util.ArrayList<java.lang.Integer>(1);
                afAvail.add(android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_OFF);
            }
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES, android.hardware.camera2.utils.ArrayUtils.toIntArray(afAvail));
            if (android.hardware.camera2.legacy.LegacyMetadataMapper.DEBUG) {
                android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, "mapControlAf - control.afAvailableModes set to " + android.hardware.camera2.utils.ListUtils.listToString(afAvail));
            }
        }
    }

    private static void mapControlAwb(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        /* control.awbAvailableModes */
        {
            java.util.List<java.lang.String> wbModes = p.getSupportedWhiteBalance();
            java.lang.String[] wbModeStrings = new java.lang.String[]{ android.hardware.Camera.Parameters.WHITE_BALANCE_AUTO, android.hardware.Camera.Parameters.WHITE_BALANCE_INCANDESCENT, android.hardware.Camera.Parameters.WHITE_BALANCE_FLUORESCENT, android.hardware.Camera.Parameters.WHITE_BALANCE_WARM_FLUORESCENT, android.hardware.Camera.Parameters.WHITE_BALANCE_DAYLIGHT, android.hardware.Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT, android.hardware.Camera.Parameters.WHITE_BALANCE_TWILIGHT, android.hardware.Camera.Parameters.WHITE_BALANCE_SHADE };
            int[] wbModeInts = // Note that CONTROL_AWB_MODE_OFF is unsupported
            new int[]{ android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_AUTO, android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_INCANDESCENT, android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_FLUORESCENT, android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_WARM_FLUORESCENT, android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_DAYLIGHT, android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_CLOUDY_DAYLIGHT, android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_TWILIGHT, android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_SHADE }// Note that CONTROL_AWB_MODE_OFF is unsupported
            ;
            java.util.List<java.lang.Integer> awbAvail = android.hardware.camera2.utils.ArrayUtils.convertStringListToIntList(wbModes, wbModeStrings, wbModeInts);
            // No AWB modes supported? That's unpossible!
            if ((awbAvail == null) || (awbAvail.size() == 0)) {
                android.util.Log.w(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, "No AWB modes supported (HAL bug); defaulting to AWB_MODE_AUTO only");
                awbAvail = /* capacity */
                new java.util.ArrayList<java.lang.Integer>(1);
                awbAvail.add(android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_AUTO);
            }
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES, android.hardware.camera2.utils.ArrayUtils.toIntArray(awbAvail));
            if (android.hardware.camera2.legacy.LegacyMetadataMapper.DEBUG) {
                android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, "mapControlAwb - control.awbAvailableModes set to " + android.hardware.camera2.utils.ListUtils.listToString(awbAvail));
            }
            /* control.awbLockAvailable */
            {
                boolean awbLockAvailable = p.isAutoWhiteBalanceLockSupported();
                m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AWB_LOCK_AVAILABLE, awbLockAvailable);
            }
        }
    }

    private static void mapControlOther(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        /* android.control.availableVideoStabilizationModes */
        {
            int[] stabModes = (p.isVideoStabilizationSupported()) ? new int[]{ android.hardware.camera2.CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_OFF, android.hardware.camera2.CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_ON } : new int[]{ android.hardware.camera2.CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_OFF };
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES, stabModes);
        }
        final int AE = 0;
        final int AWB = 1;
        /* android.control.maxRegions */
        final int AF = 2;
        int[] maxRegions = new int[3];
        maxRegions[AE] = p.getMaxNumMeteringAreas();
        maxRegions[AWB] = 0;// AWB regions not supported in API1

        maxRegions[AF] = p.getMaxNumFocusAreas();
        if (android.hardware.camera2.legacy.LegacyMetadataMapper.LIE_ABOUT_AE_MAX_REGIONS) {
            maxRegions[AE] = 0;
        }
        if (android.hardware.camera2.legacy.LegacyMetadataMapper.LIE_ABOUT_AF_MAX_REGIONS) {
            maxRegions[AF] = 0;
        }
        m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_MAX_REGIONS, maxRegions);
        /* android.control.availableEffects */
        java.util.List<java.lang.String> effectModes = p.getSupportedColorEffects();
        int[] supportedEffectModes = (effectModes == null) ? new int[0] : android.hardware.camera2.utils.ArrayUtils.convertStringListToIntArray(effectModes, android.hardware.camera2.legacy.LegacyMetadataMapper.sLegacyEffectMode, android.hardware.camera2.legacy.LegacyMetadataMapper.sEffectModes);
        m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS, supportedEffectModes);
        /* android.control.availableSceneModes */
        int maxNumDetectedFaces = p.getMaxNumDetectedFaces();
        java.util.List<java.lang.String> sceneModes = p.getSupportedSceneModes();
        java.util.List<java.lang.Integer> supportedSceneModes = android.hardware.camera2.utils.ArrayUtils.convertStringListToIntList(sceneModes, android.hardware.camera2.legacy.LegacyMetadataMapper.sLegacySceneModes, android.hardware.camera2.legacy.LegacyMetadataMapper.sSceneModes);
        // Special case where the only scene mode listed is AUTO => no scene mode
        if (((sceneModes != null) && (sceneModes.size() == 1)) && (sceneModes.get(0) == android.hardware.Camera.Parameters.SCENE_MODE_AUTO)) {
            supportedSceneModes = null;
        }
        boolean sceneModeSupported = true;
        if ((supportedSceneModes == null) && (maxNumDetectedFaces == 0)) {
            sceneModeSupported = false;
        }
        if (sceneModeSupported) {
            if (supportedSceneModes == null) {
                supportedSceneModes = new java.util.ArrayList<java.lang.Integer>();
            }
            if (maxNumDetectedFaces > 0) {
                // always supports FACE_PRIORITY when face detecting
                supportedSceneModes.add(android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_FACE_PRIORITY);
            }
            // Remove all DISABLED occurrences
            if (supportedSceneModes.contains(android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_DISABLED)) {
                while (supportedSceneModes.remove(new java.lang.Integer(android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_DISABLED))) {
                } 
            }
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES, android.hardware.camera2.utils.ArrayUtils.toIntArray(supportedSceneModes));
        } else {
            m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES, new int[]{ android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_DISABLED });
        }
        /* android.control.availableModes */
        m.set(android.hardware.camera2.CameraCharacteristics.CONTROL_AVAILABLE_MODES, sceneModeSupported ? new int[]{ android.hardware.camera2.CameraMetadata.CONTROL_MODE_AUTO, android.hardware.camera2.CameraMetadata.CONTROL_MODE_USE_SCENE_MODE } : new int[]{ android.hardware.camera2.CameraMetadata.CONTROL_MODE_AUTO });
    }

    private static void mapLens(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        /* We can tell if the lens is fixed focus;
         but if it's not, we can't tell the minimum focus distance, so leave it null then.
         */
        if (android.hardware.camera2.legacy.LegacyMetadataMapper.DEBUG) {
            android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, ("mapLens - focus-mode='" + p.getFocusMode()) + "'");
        }
        if (android.hardware.Camera.Parameters.FOCUS_MODE_FIXED.equals(p.getFocusMode())) {
            /* lens.info.minimumFocusDistance */
            m.set(android.hardware.camera2.CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE, android.hardware.camera2.legacy.LegacyMetadataMapper.LENS_INFO_MINIMUM_FOCUS_DISTANCE_FIXED_FOCUS);
            if (android.hardware.camera2.legacy.LegacyMetadataMapper.DEBUG) {
                android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, "mapLens - lens.info.minimumFocusDistance = 0");
            }
        } else {
            if (android.hardware.camera2.legacy.LegacyMetadataMapper.DEBUG) {
                android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, "mapLens - lens.info.minimumFocusDistance is unknown");
            }
        }
        float[] focalLengths = new float[]{ p.getFocalLength() };
        m.set(android.hardware.camera2.CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS, focalLengths);
    }

    private static void mapFlash(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        boolean flashAvailable = false;
        java.util.List<java.lang.String> supportedFlashModes = p.getSupportedFlashModes();
        if (supportedFlashModes != null) {
            // If only 'OFF' is available, we don't really have flash support
            flashAvailable = !android.hardware.camera2.utils.ListUtils.listElementsEqualTo(supportedFlashModes, android.hardware.Camera.Parameters.FLASH_MODE_OFF);
        }
        /* flash.info.available */
        m.set(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE, flashAvailable);
    }

    private static void mapJpeg(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        java.util.List<android.hardware.Camera.Size> thumbnailSizes = p.getSupportedJpegThumbnailSizes();
        if (thumbnailSizes != null) {
            android.util.Size[] sizes = android.hardware.camera2.legacy.ParameterUtils.convertSizeListToArray(thumbnailSizes);
            java.util.Arrays.sort(sizes, new android.hardware.camera2.utils.SizeAreaComparator());
            m.set(android.hardware.camera2.CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES, sizes);
        }
    }

    private static void mapRequest(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        /* request.availableCapabilities */
        int[] capabilities = new int[]{ android.hardware.camera2.CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE };
        m.set(android.hardware.camera2.CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES, capabilities);
        /* request.availableCharacteristicsKeys */
        {
            // TODO: check if the underlying key is supported before listing a key as available
            // Note: We only list public keys. Native HALs should list ALL keys regardless of visibility.
            android.hardware.camera2.CameraCharacteristics.Key<?>[] availableKeys = new android.hardware.camera2.CameraCharacteristics.Key<?>[]{ android.hardware.camera2.CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES, android.hardware.camera2.CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES, android.hardware.camera2.CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES, android.hardware.camera2.CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES, android.hardware.camera2.CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE, android.hardware.camera2.CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP, android.hardware.camera2.CameraCharacteristics.CONTROL_AE_LOCK_AVAILABLE, android.hardware.camera2.CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES, android.hardware.camera2.CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS, android.hardware.camera2.CameraCharacteristics.CONTROL_AVAILABLE_MODES, android.hardware.camera2.CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES, android.hardware.camera2.CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES, android.hardware.camera2.CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES, android.hardware.camera2.CameraCharacteristics.CONTROL_AWB_LOCK_AVAILABLE, android.hardware.camera2.CameraCharacteristics.CONTROL_MAX_REGIONS, android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE, android.hardware.camera2.CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL, android.hardware.camera2.CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES, android.hardware.camera2.CameraCharacteristics.LENS_FACING, android.hardware.camera2.CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS, android.hardware.camera2.CameraCharacteristics.NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES, android.hardware.camera2.CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES, android.hardware.camera2.CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_STREAMS, android.hardware.camera2.CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT, android.hardware.camera2.CameraCharacteristics.REQUEST_PIPELINE_MAX_DEPTH, android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM, // CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP                 ,
            android.hardware.camera2.CameraCharacteristics.SCALER_CROPPING_TYPE, android.hardware.camera2.CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES, android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE, android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE, android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE, android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE, android.hardware.camera2.CameraCharacteristics.SENSOR_ORIENTATION, android.hardware.camera2.CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES, android.hardware.camera2.CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT, android.hardware.camera2.CameraCharacteristics.SYNC_MAX_LATENCY };
            java.util.List<android.hardware.camera2.CameraCharacteristics.Key<?>> characteristicsKeys = new java.util.ArrayList<>(java.util.Arrays.asList(availableKeys));
            /* Add the conditional keys */
            if (m.get(android.hardware.camera2.CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE) != null) {
                characteristicsKeys.add(android.hardware.camera2.CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
            }
            m.set(android.hardware.camera2.CameraCharacteristics.REQUEST_AVAILABLE_CHARACTERISTICS_KEYS, android.hardware.camera2.legacy.LegacyMetadataMapper.getTagsForKeys(characteristicsKeys.toArray(new android.hardware.camera2.CameraCharacteristics.Key<?>[0])));
        }
        /* request.availableRequestKeys */
        {
            android.hardware.camera2.CaptureRequest.Key<?>[] defaultAvailableKeys = new android.hardware.camera2.CaptureRequest.Key<?>[]{ android.hardware.camera2.CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, android.hardware.camera2.CaptureRequest.CONTROL_AE_ANTIBANDING_MODE, android.hardware.camera2.CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, android.hardware.camera2.CaptureRequest.CONTROL_AE_LOCK, android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE, android.hardware.camera2.CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, android.hardware.camera2.CaptureRequest.CONTROL_AF_MODE, android.hardware.camera2.CaptureRequest.CONTROL_AF_TRIGGER, android.hardware.camera2.CaptureRequest.CONTROL_AWB_LOCK, android.hardware.camera2.CaptureRequest.CONTROL_AWB_MODE, android.hardware.camera2.CaptureRequest.CONTROL_CAPTURE_INTENT, android.hardware.camera2.CaptureRequest.CONTROL_EFFECT_MODE, android.hardware.camera2.CaptureRequest.CONTROL_MODE, android.hardware.camera2.CaptureRequest.CONTROL_SCENE_MODE, android.hardware.camera2.CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, android.hardware.camera2.CaptureRequest.FLASH_MODE, android.hardware.camera2.CaptureRequest.JPEG_GPS_COORDINATES, android.hardware.camera2.CaptureRequest.JPEG_GPS_PROCESSING_METHOD, android.hardware.camera2.CaptureRequest.JPEG_GPS_TIMESTAMP, android.hardware.camera2.CaptureRequest.JPEG_ORIENTATION, android.hardware.camera2.CaptureRequest.JPEG_QUALITY, android.hardware.camera2.CaptureRequest.JPEG_THUMBNAIL_QUALITY, android.hardware.camera2.CaptureRequest.JPEG_THUMBNAIL_SIZE, android.hardware.camera2.CaptureRequest.LENS_FOCAL_LENGTH, android.hardware.camera2.CaptureRequest.NOISE_REDUCTION_MODE, android.hardware.camera2.CaptureRequest.SCALER_CROP_REGION, android.hardware.camera2.CaptureRequest.STATISTICS_FACE_DETECT_MODE };
            java.util.ArrayList<android.hardware.camera2.CaptureRequest.Key<?>> availableKeys = new java.util.ArrayList<android.hardware.camera2.CaptureRequest.Key<?>>(java.util.Arrays.asList(defaultAvailableKeys));
            if (p.getMaxNumMeteringAreas() > 0) {
                availableKeys.add(android.hardware.camera2.CaptureRequest.CONTROL_AE_REGIONS);
            }
            if (p.getMaxNumFocusAreas() > 0) {
                availableKeys.add(android.hardware.camera2.CaptureRequest.CONTROL_AF_REGIONS);
            }
            android.hardware.camera2.CaptureRequest.Key<?>[] availableRequestKeys = new android.hardware.camera2.CaptureRequest.Key<?>[availableKeys.size()];
            availableKeys.toArray(availableRequestKeys);
            m.set(android.hardware.camera2.CameraCharacteristics.REQUEST_AVAILABLE_REQUEST_KEYS, android.hardware.camera2.legacy.LegacyMetadataMapper.getTagsForKeys(availableRequestKeys));
        }
        /* request.availableResultKeys */
        {
            android.hardware.camera2.CaptureResult.Key<?>[] defaultAvailableKeys = // CaptureResult.STATISTICS_FACES                                 ,
            new android.hardware.camera2.CaptureResult.Key<?>[]{ android.hardware.camera2.CaptureResult.COLOR_CORRECTION_ABERRATION_MODE, android.hardware.camera2.CaptureResult.CONTROL_AE_ANTIBANDING_MODE, android.hardware.camera2.CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION, android.hardware.camera2.CaptureResult.CONTROL_AE_LOCK, android.hardware.camera2.CaptureResult.CONTROL_AE_MODE, android.hardware.camera2.CaptureResult.CONTROL_AF_MODE, android.hardware.camera2.CaptureResult.CONTROL_AF_STATE, android.hardware.camera2.CaptureResult.CONTROL_AWB_MODE, android.hardware.camera2.CaptureResult.CONTROL_AWB_LOCK, android.hardware.camera2.CaptureResult.CONTROL_MODE, android.hardware.camera2.CaptureResult.FLASH_MODE, android.hardware.camera2.CaptureResult.JPEG_GPS_COORDINATES, android.hardware.camera2.CaptureResult.JPEG_GPS_PROCESSING_METHOD, android.hardware.camera2.CaptureResult.JPEG_GPS_TIMESTAMP, android.hardware.camera2.CaptureResult.JPEG_ORIENTATION, android.hardware.camera2.CaptureResult.JPEG_QUALITY, android.hardware.camera2.CaptureResult.JPEG_THUMBNAIL_QUALITY, android.hardware.camera2.CaptureResult.LENS_FOCAL_LENGTH, android.hardware.camera2.CaptureResult.NOISE_REDUCTION_MODE, android.hardware.camera2.CaptureResult.REQUEST_PIPELINE_DEPTH, android.hardware.camera2.CaptureResult.SCALER_CROP_REGION, android.hardware.camera2.CaptureResult.SENSOR_TIMESTAMP, android.hardware.camera2.CaptureResult.STATISTICS_FACE_DETECT_MODE }// CaptureResult.STATISTICS_FACES                                 ,
            ;
            java.util.List<android.hardware.camera2.CaptureResult.Key<?>> availableKeys = new java.util.ArrayList<android.hardware.camera2.CaptureResult.Key<?>>(java.util.Arrays.asList(defaultAvailableKeys));
            if (p.getMaxNumMeteringAreas() > 0) {
                availableKeys.add(android.hardware.camera2.CaptureResult.CONTROL_AE_REGIONS);
            }
            if (p.getMaxNumFocusAreas() > 0) {
                availableKeys.add(android.hardware.camera2.CaptureResult.CONTROL_AF_REGIONS);
            }
            android.hardware.camera2.CaptureResult.Key<?>[] availableResultKeys = new android.hardware.camera2.CaptureResult.Key<?>[availableKeys.size()];
            availableKeys.toArray(availableResultKeys);
            m.set(android.hardware.camera2.CameraCharacteristics.REQUEST_AVAILABLE_RESULT_KEYS, android.hardware.camera2.legacy.LegacyMetadataMapper.getTagsForKeys(availableResultKeys));
        }
        /* request.maxNumOutputStreams */
        int[] outputStreams = new int[]{ /* RAW */
        android.hardware.camera2.legacy.LegacyMetadataMapper.REQUEST_MAX_NUM_OUTPUT_STREAMS_COUNT_RAW, /* Processed & Not-Stalling */
        android.hardware.camera2.legacy.LegacyMetadataMapper.REQUEST_MAX_NUM_OUTPUT_STREAMS_COUNT_PROC, /* Processed & Stalling */
        android.hardware.camera2.legacy.LegacyMetadataMapper.REQUEST_MAX_NUM_OUTPUT_STREAMS_COUNT_PROC_STALL };
        m.set(android.hardware.camera2.CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_STREAMS, outputStreams);
        /* request.maxNumInputStreams */
        m.set(android.hardware.camera2.CameraCharacteristics.REQUEST_MAX_NUM_INPUT_STREAMS, android.hardware.camera2.legacy.LegacyMetadataMapper.REQUEST_MAX_NUM_INPUT_STREAMS_COUNT);
        /* request.partialResultCount */
        m.set(android.hardware.camera2.CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT, 1);// No partial results supported

        /* request.pipelineMaxDepth */
        m.set(android.hardware.camera2.CameraCharacteristics.REQUEST_PIPELINE_MAX_DEPTH, ((byte) (android.hardware.camera2.legacy.LegacyMetadataMapper.REQUEST_PIPELINE_MAX_DEPTH_HAL1 + android.hardware.camera2.legacy.LegacyMetadataMapper.REQUEST_PIPELINE_MAX_DEPTH_OURS)));
    }

    private static void mapScaler(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        /* scaler.availableMaxDigitalZoom */
        m.set(android.hardware.camera2.CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM, android.hardware.camera2.legacy.ParameterUtils.getMaxZoomRatio(p));
        /* scaler.croppingType = CENTER_ONLY */
        m.set(android.hardware.camera2.CameraCharacteristics.SCALER_CROPPING_TYPE, android.hardware.camera2.CameraMetadata.SCALER_CROPPING_TYPE_CENTER_ONLY);
    }

    private static void mapSensor(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        // Use the largest jpeg size (by area) for both active array and pixel array
        android.util.Size largestJpegSize = android.hardware.camera2.legacy.ParameterUtils.getLargestSupportedJpegSizeByArea(p);
        /* sensor.info.activeArraySize */
        {
            android.graphics.Rect activeArrayRect = android.hardware.camera2.utils.ParamsUtils.createRect(largestJpegSize);
            m.set(android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE, activeArrayRect);
        }
        /* sensor.availableTestPatternModes */
        {
            // Only "OFF" test pattern mode is available
            m.set(android.hardware.camera2.CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES, new int[]{ android.hardware.camera2.CameraMetadata.SENSOR_TEST_PATTERN_MODE_OFF });
        }
        /* sensor.info.pixelArraySize */
        m.set(android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE, largestJpegSize);
        /* sensor.info.physicalSize */
        {
            /* Assume focal length is at infinity focus and that the lens is rectilinear. */
            float focalLength = p.getFocalLength();// in mm

            double angleHor = (p.getHorizontalViewAngle() * java.lang.Math.PI) / 180;// to radians

            double angleVer = (p.getVerticalViewAngle() * java.lang.Math.PI) / 180;// to radians

            float height = ((float) (java.lang.Math.abs((2 * focalLength) * java.lang.Math.tan(angleVer / 2))));
            float width = ((float) (java.lang.Math.abs((2 * focalLength) * java.lang.Math.tan(angleHor / 2))));
            m.set(android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE, new android.util.SizeF(width, height));// in mm

        }
        /* sensor.info.timestampSource */
        {
            m.set(android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE, android.hardware.camera2.CameraMetadata.SENSOR_INFO_TIMESTAMP_SOURCE_UNKNOWN);
        }
    }

    private static void mapStatistics(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        /* statistics.info.availableFaceDetectModes */
        int[] fdModes;
        if (p.getMaxNumDetectedFaces() > 0) {
            fdModes = // FULL is never-listed, since we have no way to query it statically
            new int[]{ android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF, android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_SIMPLE }// FULL is never-listed, since we have no way to query it statically
            ;
        } else {
            fdModes = new int[]{ android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF };
        }
        m.set(android.hardware.camera2.CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES, fdModes);
        /* statistics.info.maxFaceCount */
        m.set(android.hardware.camera2.CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT, p.getMaxNumDetectedFaces());
    }

    private static void mapSync(android.hardware.camera2.impl.CameraMetadataNative m, android.hardware.Camera.Parameters p) {
        /* sync.maxLatency */
        m.set(android.hardware.camera2.CameraCharacteristics.SYNC_MAX_LATENCY, android.hardware.camera2.CameraMetadata.SYNC_MAX_LATENCY_UNKNOWN);
    }

    private static void appendStreamConfig(java.util.ArrayList<android.hardware.camera2.params.StreamConfiguration> configs, int format, java.util.List<android.hardware.Camera.Size> sizes) {
        for (android.hardware.Camera.Size size : sizes) {
            android.hardware.camera2.params.StreamConfiguration config = /* input */
            new android.hardware.camera2.params.StreamConfiguration(format, size.width, size.height, false);
            configs.add(config);
        }
    }

    private static final java.lang.String[] sLegacySceneModes = new java.lang.String[]{ android.hardware.Camera.Parameters.SCENE_MODE_AUTO, android.hardware.Camera.Parameters.SCENE_MODE_ACTION, android.hardware.Camera.Parameters.SCENE_MODE_PORTRAIT, android.hardware.Camera.Parameters.SCENE_MODE_LANDSCAPE, android.hardware.Camera.Parameters.SCENE_MODE_NIGHT, android.hardware.Camera.Parameters.SCENE_MODE_NIGHT_PORTRAIT, android.hardware.Camera.Parameters.SCENE_MODE_THEATRE, android.hardware.Camera.Parameters.SCENE_MODE_BEACH, android.hardware.Camera.Parameters.SCENE_MODE_SNOW, android.hardware.Camera.Parameters.SCENE_MODE_SUNSET, android.hardware.Camera.Parameters.SCENE_MODE_STEADYPHOTO, android.hardware.Camera.Parameters.SCENE_MODE_FIREWORKS, android.hardware.Camera.Parameters.SCENE_MODE_SPORTS, android.hardware.Camera.Parameters.SCENE_MODE_PARTY, android.hardware.Camera.Parameters.SCENE_MODE_CANDLELIGHT, android.hardware.Camera.Parameters.SCENE_MODE_BARCODE, android.hardware.Camera.Parameters.SCENE_MODE_HDR };

    private static final int[] sSceneModes = new int[]{ android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_DISABLED, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_ACTION, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_PORTRAIT, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_LANDSCAPE, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_NIGHT, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_NIGHT_PORTRAIT, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_THEATRE, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_BEACH, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_SNOW, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_SUNSET, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_STEADYPHOTO, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_FIREWORKS, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_SPORTS, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_PARTY, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_CANDLELIGHT, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_BARCODE, android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_HDR };

    static int convertSceneModeFromLegacy(java.lang.String mode) {
        if (mode == null) {
            return android.hardware.camera2.CameraCharacteristics.CONTROL_SCENE_MODE_DISABLED;
        }
        int index = android.hardware.camera2.utils.ArrayUtils.getArrayIndex(android.hardware.camera2.legacy.LegacyMetadataMapper.sLegacySceneModes, mode);
        if (index < 0) {
            return android.hardware.camera2.legacy.LegacyMetadataMapper.UNKNOWN_MODE;
        }
        return android.hardware.camera2.legacy.LegacyMetadataMapper.sSceneModes[index];
    }

    static java.lang.String convertSceneModeToLegacy(int mode) {
        if (mode == android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_FACE_PRIORITY) {
            // OK: Let LegacyFaceDetectMapper handle turning face detection on/off
            return android.hardware.Camera.Parameters.SCENE_MODE_AUTO;
        }
        int index = android.hardware.camera2.utils.ArrayUtils.getArrayIndex(android.hardware.camera2.legacy.LegacyMetadataMapper.sSceneModes, mode);
        if (index < 0) {
            return null;
        }
        return android.hardware.camera2.legacy.LegacyMetadataMapper.sLegacySceneModes[index];
    }

    private static final java.lang.String[] sLegacyEffectMode = new java.lang.String[]{ android.hardware.Camera.Parameters.EFFECT_NONE, android.hardware.Camera.Parameters.EFFECT_MONO, android.hardware.Camera.Parameters.EFFECT_NEGATIVE, android.hardware.Camera.Parameters.EFFECT_SOLARIZE, android.hardware.Camera.Parameters.EFFECT_SEPIA, android.hardware.Camera.Parameters.EFFECT_POSTERIZE, android.hardware.Camera.Parameters.EFFECT_WHITEBOARD, android.hardware.Camera.Parameters.EFFECT_BLACKBOARD, android.hardware.Camera.Parameters.EFFECT_AQUA };

    private static final int[] sEffectModes = new int[]{ android.hardware.camera2.CameraCharacteristics.CONTROL_EFFECT_MODE_OFF, android.hardware.camera2.CameraCharacteristics.CONTROL_EFFECT_MODE_MONO, android.hardware.camera2.CameraCharacteristics.CONTROL_EFFECT_MODE_NEGATIVE, android.hardware.camera2.CameraCharacteristics.CONTROL_EFFECT_MODE_SOLARIZE, android.hardware.camera2.CameraCharacteristics.CONTROL_EFFECT_MODE_SEPIA, android.hardware.camera2.CameraCharacteristics.CONTROL_EFFECT_MODE_POSTERIZE, android.hardware.camera2.CameraCharacteristics.CONTROL_EFFECT_MODE_WHITEBOARD, android.hardware.camera2.CameraCharacteristics.CONTROL_EFFECT_MODE_BLACKBOARD, android.hardware.camera2.CameraCharacteristics.CONTROL_EFFECT_MODE_AQUA };

    static int convertEffectModeFromLegacy(java.lang.String mode) {
        if (mode == null) {
            return android.hardware.camera2.CameraCharacteristics.CONTROL_EFFECT_MODE_OFF;
        }
        int index = android.hardware.camera2.utils.ArrayUtils.getArrayIndex(android.hardware.camera2.legacy.LegacyMetadataMapper.sLegacyEffectMode, mode);
        if (index < 0) {
            return android.hardware.camera2.legacy.LegacyMetadataMapper.UNKNOWN_MODE;
        }
        return android.hardware.camera2.legacy.LegacyMetadataMapper.sEffectModes[index];
    }

    static java.lang.String convertEffectModeToLegacy(int mode) {
        int index = android.hardware.camera2.utils.ArrayUtils.getArrayIndex(android.hardware.camera2.legacy.LegacyMetadataMapper.sEffectModes, mode);
        if (index < 0) {
            return null;
        }
        return android.hardware.camera2.legacy.LegacyMetadataMapper.sLegacyEffectMode[index];
    }

    /**
     * Convert the ae antibanding mode from api1 into api2.
     *
     * @param mode
     * 		the api1 mode, {@code null} is allowed and will return {@code -1}.
     * @return The api2 value, or {@code -1} by default if conversion failed
     */
    private static int convertAntiBandingMode(java.lang.String mode) {
        if (mode == null) {
            return -1;
        }
        switch (mode) {
            case android.hardware.Camera.Parameters.ANTIBANDING_OFF :
                {
                    return android.hardware.camera2.CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_OFF;
                }
            case android.hardware.Camera.Parameters.ANTIBANDING_50HZ :
                {
                    return android.hardware.camera2.CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_50HZ;
                }
            case android.hardware.Camera.Parameters.ANTIBANDING_60HZ :
                {
                    return android.hardware.camera2.CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_60HZ;
                }
            case android.hardware.Camera.Parameters.ANTIBANDING_AUTO :
                {
                    return android.hardware.camera2.CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_AUTO;
                }
            default :
                {
                    android.util.Log.w(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, "convertAntiBandingMode - Unknown antibanding mode " + mode);
                    return -1;
                }
        }
    }

    /**
     * Convert the ae antibanding mode from api1 into api2.
     *
     * @param mode
     * 		the api1 mode, {@code null} is allowed and will return {@code MODE_OFF}.
     * @return The api2 value, or {@code MODE_OFF} by default if conversion failed
     */
    static int convertAntiBandingModeOrDefault(java.lang.String mode) {
        int antiBandingMode = android.hardware.camera2.legacy.LegacyMetadataMapper.convertAntiBandingMode(mode);
        if (antiBandingMode == (-1)) {
            return android.hardware.camera2.CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_OFF;
        }
        return antiBandingMode;
    }

    private static int[] convertAeFpsRangeToLegacy(android.util.Range<java.lang.Integer> fpsRange) {
        int[] legacyFps = new int[2];
        legacyFps[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX] = fpsRange.getLower();
        legacyFps[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX] = fpsRange.getUpper();
        return legacyFps;
    }

    /**
     * Return the stall duration for a given output jpeg size in nanoseconds.
     *
     * <p>An 8mp image is chosen to have a stall duration of 0.8 seconds.</p>
     */
    private static long calculateJpegStallDuration(android.hardware.Camera.Size size) {
        long baseDuration = android.hardware.camera2.legacy.LegacyMetadataMapper.APPROXIMATE_CAPTURE_DELAY_MS * android.hardware.camera2.legacy.LegacyMetadataMapper.NS_PER_MS;// 200ms for capture

        long area = size.width * ((long) (size.height));
        long stallPerArea = (android.hardware.camera2.legacy.LegacyMetadataMapper.APPROXIMATE_JPEG_ENCODE_TIME_MS * android.hardware.camera2.legacy.LegacyMetadataMapper.NS_PER_MS) / android.hardware.camera2.legacy.LegacyMetadataMapper.APPROXIMATE_SENSOR_AREA_PX;// 600ms stall for 8mp

        return baseDuration + (area * stallPerArea);
    }

    /**
     * Set the legacy parameters using the {@link LegacyRequest legacy request}.
     *
     * <p>The legacy request's parameters are changed as a side effect of calling this
     * method.</p>
     *
     * @param request
     * 		a non-{@code null} legacy request
     */
    public static void convertRequestMetadata(android.hardware.camera2.legacy.LegacyRequest request) {
        android.hardware.camera2.legacy.LegacyRequestMapper.convertRequestMetadata(request);
    }

    private static final int[] sAllowedTemplates = // Disallowed templates in legacy mode:
    // CameraDevice.TEMPLATE_VIDEO_SNAPSHOT,
    // CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG,
    // CameraDevice.TEMPLATE_MANUAL
    new int[]{ android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW, android.hardware.camera2.CameraDevice.TEMPLATE_STILL_CAPTURE, android.hardware.camera2.CameraDevice.TEMPLATE_RECORD }// Disallowed templates in legacy mode:
    // CameraDevice.TEMPLATE_VIDEO_SNAPSHOT,
    // CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG,
    // CameraDevice.TEMPLATE_MANUAL
    ;

    /**
     * Create a request template
     *
     * @param c
     * 		a non-{@code null} camera characteristics for this camera
     * @param templateId
     * 		a non-negative template ID
     * @return a non-{@code null} request template
     * @throws IllegalArgumentException
     * 		if {@code templateId} was invalid
     * @see android.hardware.camera2.CameraDevice#TEMPLATE_MANUAL
     */
    public static android.hardware.camera2.impl.CameraMetadataNative createRequestTemplate(android.hardware.camera2.CameraCharacteristics c, int templateId) {
        if (!android.hardware.camera2.utils.ArrayUtils.contains(android.hardware.camera2.legacy.LegacyMetadataMapper.sAllowedTemplates, templateId)) {
            throw new java.lang.IllegalArgumentException("templateId out of range");
        }
        android.hardware.camera2.impl.CameraMetadataNative m = new android.hardware.camera2.impl.CameraMetadataNative();
        /* NOTE: If adding new code here and it needs to query the static info,
        query the camera characteristics, so we can reuse this for api2 code later
        to create our own templates in the framework
         */
        /* control.* */
        // control.awbMode
        m.set(android.hardware.camera2.CaptureRequest.CONTROL_AWB_MODE, android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_AUTO);
        // AWB is always unconditionally available in API1 devices
        // control.aeAntibandingMode
        m.set(android.hardware.camera2.CaptureRequest.CONTROL_AE_ANTIBANDING_MODE, android.hardware.camera2.CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_AUTO);
        // control.aeExposureCompensation
        m.set(android.hardware.camera2.CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, 0);
        // control.aeLock
        m.set(android.hardware.camera2.CaptureRequest.CONTROL_AE_LOCK, false);
        // control.aePrecaptureTrigger
        m.set(android.hardware.camera2.CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, android.hardware.camera2.CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_IDLE);
        // control.afTrigger
        m.set(android.hardware.camera2.CaptureRequest.CONTROL_AF_TRIGGER, android.hardware.camera2.CameraMetadata.CONTROL_AF_TRIGGER_IDLE);
        // control.awbMode
        m.set(android.hardware.camera2.CaptureRequest.CONTROL_AWB_MODE, android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_AUTO);
        // control.awbLock
        m.set(android.hardware.camera2.CaptureRequest.CONTROL_AWB_LOCK, false);
        // control.aeRegions, control.awbRegions, control.afRegions
        {
            android.graphics.Rect activeArray = c.get(android.hardware.camera2.CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            android.hardware.camera2.params.MeteringRectangle[] activeRegions = new android.hardware.camera2.params.MeteringRectangle[]{ /* x */
            /* y */
            /* width */
            /* height */
            /* weight */
            new android.hardware.camera2.params.MeteringRectangle(0, 0, activeArray.width() - 1, activeArray.height() - 1, 0) };
            m.set(android.hardware.camera2.CaptureRequest.CONTROL_AE_REGIONS, activeRegions);
            m.set(android.hardware.camera2.CaptureRequest.CONTROL_AWB_REGIONS, activeRegions);
            m.set(android.hardware.camera2.CaptureRequest.CONTROL_AF_REGIONS, activeRegions);
        }
        // control.captureIntent
        {
            int captureIntent;
            switch (templateId) {
                case android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW :
                    captureIntent = android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_PREVIEW;
                    break;
                case android.hardware.camera2.CameraDevice.TEMPLATE_STILL_CAPTURE :
                    captureIntent = android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_STILL_CAPTURE;
                    break;
                case android.hardware.camera2.CameraDevice.TEMPLATE_RECORD :
                    captureIntent = android.hardware.camera2.CameraMetadata.CONTROL_CAPTURE_INTENT_VIDEO_RECORD;
                    break;
                default :
                    // Can't get anything else since it's guarded by the IAE check
                    throw new java.lang.AssertionError("Impossible; keep in sync with sAllowedTemplates");
            }
            m.set(android.hardware.camera2.CaptureRequest.CONTROL_CAPTURE_INTENT, captureIntent);
        }
        // control.aeMode
        m.set(android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE, android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON);
        // AE is always unconditionally available in API1 devices
        // control.mode
        m.set(android.hardware.camera2.CaptureRequest.CONTROL_MODE, android.hardware.camera2.CameraMetadata.CONTROL_MODE_AUTO);
        // control.afMode
        {
            java.lang.Float minimumFocusDistance = c.get(android.hardware.camera2.CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
            int afMode;
            if ((minimumFocusDistance != null) && (minimumFocusDistance == android.hardware.camera2.legacy.LegacyMetadataMapper.LENS_INFO_MINIMUM_FOCUS_DISTANCE_FIXED_FOCUS)) {
                // Cannot control auto-focus with fixed-focus cameras
                afMode = android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_OFF;
            } else {
                // If a minimum focus distance is reported; the camera must have AF
                afMode = android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_AUTO;
                if ((templateId == android.hardware.camera2.CameraDevice.TEMPLATE_RECORD) || (templateId == android.hardware.camera2.CameraDevice.TEMPLATE_VIDEO_SNAPSHOT)) {
                    if (android.hardware.camera2.utils.ArrayUtils.contains(c.get(android.hardware.camera2.CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES), android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO)) {
                        afMode = android.hardware.camera2.CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO;
                    }
                } else
                    if ((templateId == android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW) || (templateId == android.hardware.camera2.CameraDevice.TEMPLATE_STILL_CAPTURE)) {
                        if (android.hardware.camera2.utils.ArrayUtils.contains(c.get(android.hardware.camera2.CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES), android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE)) {
                            afMode = android.hardware.camera2.CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE;
                        }
                    }

            }
            if (android.hardware.camera2.legacy.LegacyMetadataMapper.DEBUG) {
                android.util.Log.v(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, ((((("createRequestTemplate (templateId=" + templateId) + "),") + " afMode=") + afMode) + ", minimumFocusDistance=") + minimumFocusDistance);
            }
            m.set(android.hardware.camera2.CaptureRequest.CONTROL_AF_MODE, afMode);
        }
        {
            // control.aeTargetFpsRange
            android.util.Range<java.lang.Integer>[] availableFpsRange = c.get(android.hardware.camera2.CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
            // Pick FPS range with highest max value, tiebreak on higher min value
            android.util.Range<java.lang.Integer> bestRange = availableFpsRange[0];
            for (android.util.Range<java.lang.Integer> r : availableFpsRange) {
                if (bestRange.getUpper() < r.getUpper()) {
                    bestRange = r;
                } else
                    if ((bestRange.getUpper() == r.getUpper()) && (bestRange.getLower() < r.getLower())) {
                        bestRange = r;
                    }

            }
            m.set(android.hardware.camera2.CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, bestRange);
        }
        // control.sceneMode -- DISABLED is always available
        m.set(android.hardware.camera2.CaptureRequest.CONTROL_SCENE_MODE, android.hardware.camera2.CameraMetadata.CONTROL_SCENE_MODE_DISABLED);
        /* statistics.* */
        // statistics.faceDetectMode
        m.set(android.hardware.camera2.CaptureRequest.STATISTICS_FACE_DETECT_MODE, android.hardware.camera2.CameraMetadata.STATISTICS_FACE_DETECT_MODE_OFF);
        /* flash.* */
        // flash.mode
        m.set(android.hardware.camera2.CaptureRequest.FLASH_MODE, android.hardware.camera2.CameraMetadata.FLASH_MODE_OFF);
        /* noiseReduction.* */
        if (templateId == android.hardware.camera2.CameraDevice.TEMPLATE_STILL_CAPTURE) {
            m.set(android.hardware.camera2.CaptureRequest.NOISE_REDUCTION_MODE, android.hardware.camera2.CameraMetadata.NOISE_REDUCTION_MODE_HIGH_QUALITY);
        } else {
            m.set(android.hardware.camera2.CaptureRequest.NOISE_REDUCTION_MODE, android.hardware.camera2.CameraMetadata.NOISE_REDUCTION_MODE_FAST);
        }
        /* colorCorrection.* */
        if (templateId == android.hardware.camera2.CameraDevice.TEMPLATE_STILL_CAPTURE) {
            m.set(android.hardware.camera2.CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, android.hardware.camera2.CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY);
        } else {
            m.set(android.hardware.camera2.CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, android.hardware.camera2.CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_FAST);
        }
        /* lens.* */
        // lens.focalLength
        m.set(android.hardware.camera2.CaptureRequest.LENS_FOCAL_LENGTH, c.get(android.hardware.camera2.CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)[0]);
        /* jpeg.* */
        // jpeg.thumbnailSize - set smallest non-zero size if possible
        android.util.Size[] sizes = c.get(android.hardware.camera2.CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES);
        m.set(android.hardware.camera2.CaptureRequest.JPEG_THUMBNAIL_SIZE, sizes.length > 1 ? sizes[1] : sizes[0]);
        // TODO: map other request template values
        return m;
    }

    private static int[] getTagsForKeys(android.hardware.camera2.CameraCharacteristics.Key<?>[] keys) {
        int[] tags = new int[keys.length];
        for (int i = 0; i < keys.length; ++i) {
            tags[i] = keys[i].getNativeKey().getTag();
        }
        return tags;
    }

    private static int[] getTagsForKeys(android.hardware.camera2.CaptureRequest.Key<?>[] keys) {
        int[] tags = new int[keys.length];
        for (int i = 0; i < keys.length; ++i) {
            tags[i] = keys[i].getNativeKey().getTag();
        }
        return tags;
    }

    private static int[] getTagsForKeys(android.hardware.camera2.CaptureResult.Key<?>[] keys) {
        int[] tags = new int[keys.length];
        for (int i = 0; i < keys.length; ++i) {
            tags[i] = keys[i].getNativeKey().getTag();
        }
        return tags;
    }

    /**
     * Convert the requested AF mode into its equivalent supported parameter.
     *
     * @param mode
     * 		{@code CONTROL_AF_MODE}
     * @param supportedFocusModes
     * 		list of camera1's supported focus modes
     * @return the stringified af mode, or {@code null} if its not supported
     */
    static java.lang.String convertAfModeToLegacy(int mode, java.util.List<java.lang.String> supportedFocusModes) {
        if ((supportedFocusModes == null) || supportedFocusModes.isEmpty()) {
            android.util.Log.w(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, "No focus modes supported; API1 bug");
            return null;
        }
        java.lang.String param = null;
        switch (mode) {
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_AUTO :
                param = android.hardware.Camera.Parameters.FOCUS_MODE_AUTO;
                break;
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE :
                param = android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
                break;
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO :
                param = android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO;
                break;
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_EDOF :
                param = android.hardware.Camera.Parameters.FOCUS_MODE_EDOF;
                break;
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_MACRO :
                param = android.hardware.Camera.Parameters.FOCUS_MODE_MACRO;
                break;
            case android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_OFF :
                if (supportedFocusModes.contains(android.hardware.Camera.Parameters.FOCUS_MODE_FIXED)) {
                    param = android.hardware.Camera.Parameters.FOCUS_MODE_FIXED;
                } else {
                    param = android.hardware.Camera.Parameters.FOCUS_MODE_INFINITY;
                }
        }
        if (!supportedFocusModes.contains(param)) {
            // Weed out bad user input by setting to the first arbitrary focus mode
            java.lang.String defaultMode = supportedFocusModes.get(0);
            android.util.Log.w(android.hardware.camera2.legacy.LegacyMetadataMapper.TAG, java.lang.String.format("convertAfModeToLegacy - ignoring unsupported mode %d, " + "defaulting to %s", mode, defaultMode));
            param = defaultMode;
        }
        return param;
    }
}

