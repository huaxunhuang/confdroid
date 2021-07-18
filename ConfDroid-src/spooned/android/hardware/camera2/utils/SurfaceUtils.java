/**
 * Copyright 2015 The Android Open Source Project
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
package android.hardware.camera2.utils;


/**
 * Various Surface utilities.
 */
public class SurfaceUtils {
    /**
     * Check if a surface is for preview consumer based on consumer end point Gralloc usage flags.
     *
     * @param surface
     * 		The surface to be checked.
     * @return true if the surface is for preview consumer, false otherwise.
     */
    public static boolean isSurfaceForPreview(android.view.Surface surface) {
        return android.hardware.camera2.legacy.LegacyCameraDevice.isPreviewConsumer(surface);
    }

    /**
     * Check if the surface is for hardware video encoder consumer based on consumer end point
     * Gralloc usage flags.
     *
     * @param surface
     * 		The surface to be checked.
     * @return true if the surface is for hardware video encoder consumer, false otherwise.
     */
    public static boolean isSurfaceForHwVideoEncoder(android.view.Surface surface) {
        return android.hardware.camera2.legacy.LegacyCameraDevice.isVideoEncoderConsumer(surface);
    }

    /**
     * Get the Surface size.
     *
     * @param surface
     * 		The surface to be queried for size.
     * @return Size of the surface.
     * @throws IllegalArgumentException
     * 		if the surface is already abandoned.
     */
    public static android.util.Size getSurfaceSize(android.view.Surface surface) {
        try {
            return android.hardware.camera2.legacy.LegacyCameraDevice.getSurfaceSize(surface);
        } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new java.lang.IllegalArgumentException("Surface was abandoned", e);
        }
    }

    /**
     * Get the Surface format.
     *
     * @param surface
     * 		The surface to be queried for format.
     * @return format of the surface.
     * @throws IllegalArgumentException
     * 		if the surface is already abandoned.
     */
    public static int getSurfaceFormat(android.view.Surface surface) {
        try {
            return android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceType(surface);
        } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new java.lang.IllegalArgumentException("Surface was abandoned", e);
        }
    }

    /**
     * Get the Surface dataspace.
     *
     * @param surface
     * 		The surface to be queried for dataspace.
     * @return dataspace of the surface.
     * @throws IllegalArgumentException
     * 		if the surface is already abandoned.
     */
    public static int getSurfaceDataspace(android.view.Surface surface) {
        try {
            return android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceDataspace(surface);
        } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new java.lang.IllegalArgumentException("Surface was abandoned", e);
        }
    }

    /**
     * Return true is the consumer is one of the consumers that can accept
     * producer overrides of the default dimensions and format.
     */
    public static boolean isFlexibleConsumer(android.view.Surface output) {
        return android.hardware.camera2.legacy.LegacyCameraDevice.isFlexibleConsumer(output);
    }

    /**
     * A high speed output surface can only be preview or hardware encoder surface.
     *
     * @param surface
     * 		The high speed output surface to be checked.
     */
    private static void checkHighSpeedSurfaceFormat(android.view.Surface surface) {
        // TODO: remove this override since the default format should be
        // ImageFormat.PRIVATE. b/9487482
        final int HAL_FORMAT_RGB_START = 1;// HAL_PIXEL_FORMAT_RGBA_8888 from graphics.h

        final int HAL_FORMAT_RGB_END = 5;// HAL_PIXEL_FORMAT_BGRA_8888 from graphics.h

        int surfaceFormat = android.hardware.camera2.utils.SurfaceUtils.getSurfaceFormat(surface);
        if ((surfaceFormat >= HAL_FORMAT_RGB_START) && (surfaceFormat <= HAL_FORMAT_RGB_END)) {
            surfaceFormat = android.graphics.ImageFormat.PRIVATE;
        }
        if (surfaceFormat != android.graphics.ImageFormat.PRIVATE) {
            throw new java.lang.IllegalArgumentException((("Surface format(" + surfaceFormat) + ") is not") + " for preview or hardware video encoding!");
        }
    }

    /**
     * Verify that that the surfaces are valid for high-speed recording mode,
     * and that the FPS range is supported
     *
     * @param surfaces
     * 		the surfaces to verify as valid in terms of size and format
     * @param fpsRange
     * 		the target high-speed FPS range to validate
     * @param config
     * 		The stream configuration map for the device in question
     */
    public static void checkConstrainedHighSpeedSurfaces(java.util.Collection<android.view.Surface> surfaces, android.util.Range<java.lang.Integer> fpsRange, android.hardware.camera2.params.StreamConfigurationMap config) {
        if (((surfaces == null) || (surfaces.size() == 0)) || (surfaces.size() > 2)) {
            throw new java.lang.IllegalArgumentException("Output target surface list must not be null and" + " the size must be 1 or 2");
        }
        java.util.List<android.util.Size> highSpeedSizes = null;
        if (fpsRange == null) {
            highSpeedSizes = java.util.Arrays.asList(config.getHighSpeedVideoSizes());
        } else {
            // Check the FPS range first if provided
            android.util.Range<java.lang.Integer>[] highSpeedFpsRanges = config.getHighSpeedVideoFpsRanges();
            if (!java.util.Arrays.asList(highSpeedFpsRanges).contains(fpsRange)) {
                throw new java.lang.IllegalArgumentException(((("Fps range " + fpsRange.toString()) + " in the") + " request is not a supported high speed fps range ") + java.util.Arrays.toString(highSpeedFpsRanges));
            }
            highSpeedSizes = java.util.Arrays.asList(config.getHighSpeedVideoSizesFor(fpsRange));
        }
        for (android.view.Surface surface : surfaces) {
            android.hardware.camera2.utils.SurfaceUtils.checkHighSpeedSurfaceFormat(surface);
            // Surface size must be supported high speed sizes.
            android.util.Size surfaceSize = android.hardware.camera2.utils.SurfaceUtils.getSurfaceSize(surface);
            if (!highSpeedSizes.contains(surfaceSize)) {
                throw new java.lang.IllegalArgumentException(((("Surface size " + surfaceSize.toString()) + " is") + " not part of the high speed supported size list ") + java.util.Arrays.toString(highSpeedSizes.toArray()));
            }
            // Each output surface must be either preview surface or recording surface.
            if ((!android.hardware.camera2.utils.SurfaceUtils.isSurfaceForPreview(surface)) && (!android.hardware.camera2.utils.SurfaceUtils.isSurfaceForHwVideoEncoder(surface))) {
                throw new java.lang.IllegalArgumentException("This output surface is neither preview nor " + "hardware video encoding surface");
            }
            if (android.hardware.camera2.utils.SurfaceUtils.isSurfaceForPreview(surface) && android.hardware.camera2.utils.SurfaceUtils.isSurfaceForHwVideoEncoder(surface)) {
                throw new java.lang.IllegalArgumentException("This output surface can not be both preview" + " and hardware video encoding surface");
            }
        }
        // For 2 output surface case, they shouldn't be same type.
        if (surfaces.size() == 2) {
            // Up to here, each surface can only be either preview or recording.
            java.util.Iterator<android.view.Surface> iterator = surfaces.iterator();
            boolean isFirstSurfacePreview = android.hardware.camera2.utils.SurfaceUtils.isSurfaceForPreview(iterator.next());
            boolean isSecondSurfacePreview = android.hardware.camera2.utils.SurfaceUtils.isSurfaceForPreview(iterator.next());
            if (isFirstSurfacePreview == isSecondSurfacePreview) {
                throw new java.lang.IllegalArgumentException("The 2 output surfaces must have different" + " type");
            }
        }
    }
}

