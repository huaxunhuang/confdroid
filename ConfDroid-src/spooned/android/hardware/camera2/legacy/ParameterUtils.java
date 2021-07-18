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
 * Various utilities for dealing with camera API1 parameters.
 */
@java.lang.SuppressWarnings("deprecation")
public class ParameterUtils {
    /**
     * Upper/left minimal point of a normalized rectangle
     */
    public static final int NORMALIZED_RECTANGLE_MIN = -1000;

    /**
     * Lower/right maximal point of a normalized rectangle
     */
    public static final int NORMALIZED_RECTANGLE_MAX = 1000;

    /**
     * The default normalized rectangle spans the entire size of the preview viewport
     */
    public static final android.graphics.Rect NORMALIZED_RECTANGLE_DEFAULT = new android.graphics.Rect(android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MIN, android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MIN, android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MAX, android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MAX);

    /**
     * The default normalized area uses the default normalized rectangle with a weight=1
     */
    public static final android.hardware.Camera.Area CAMERA_AREA_DEFAULT = /* weight */
    new android.hardware.Camera.Area(new android.graphics.Rect(android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_DEFAULT), 1);

    /**
     * Empty rectangle {@code 0x0+0,0}
     */
    public static final android.graphics.Rect RECTANGLE_EMPTY = /* left */
    /* top */
    /* right */
    /* bottom */
    new android.graphics.Rect(0, 0, 0, 0);

    private static final double ASPECT_RATIO_TOLERANCE = 0.05F;

    /**
     * Calculate effective/reported zoom data from a user-specified crop region.
     */
    public static class ZoomData {
        /**
         * Zoom index used by {@link Camera.Parameters#setZoom}
         */
        public final int zoomIndex;

        /**
         * Effective crop-region given the zoom index, coordinates relative to active-array
         */
        public final android.graphics.Rect previewCrop;

        /**
         * Reported crop-region given the zoom index, coordinates relative to active-array
         */
        public final android.graphics.Rect reportedCrop;

        public ZoomData(int zoomIndex, android.graphics.Rect previewCrop, android.graphics.Rect reportedCrop) {
            this.zoomIndex = zoomIndex;
            this.previewCrop = previewCrop;
            this.reportedCrop = reportedCrop;
        }
    }

    /**
     * Calculate effective/reported metering data from a user-specified metering region.
     */
    public static class MeteringData {
        /**
         * The metering area scaled to the range of [-1000, 1000].
         * <p>Values outside of this range are clipped to be within the range.</p>
         */
        public final android.hardware.Camera.Area meteringArea;

        /**
         * Effective preview metering region, coordinates relative to active-array.
         *
         * <p>Clipped to fit inside of the (effective) preview crop region.</p>
         */
        public final android.graphics.Rect previewMetering;

        /**
         * Reported metering region, coordinates relative to active-array.
         *
         * <p>Clipped to fit inside of the (reported) resulting crop region.</p>
         */
        public final android.graphics.Rect reportedMetering;

        public MeteringData(android.hardware.Camera.Area meteringArea, android.graphics.Rect previewMetering, android.graphics.Rect reportedMetering) {
            this.meteringArea = meteringArea;
            this.previewMetering = previewMetering;
            this.reportedMetering = reportedMetering;
        }
    }

    /**
     * A weighted rectangle is an arbitrary rectangle (the coordinate system is unknown) with an
     * arbitrary weight.
     *
     * <p>The user of this class must know what the coordinate system ahead of time; it's
     * then possible to convert to a more concrete type such as a metering rectangle or a face.
     * </p>
     *
     * <p>When converting to a more concrete type, out-of-range values are clipped; this prevents
     * possible illegal argument exceptions being thrown at runtime.</p>
     */
    public static class WeightedRectangle {
        /**
         * Arbitrary rectangle (the range is user-defined); never {@code null}.
         */
        public final android.graphics.Rect rect;

        /**
         * Arbitrary weight (the range is user-defined).
         */
        public final int weight;

        /**
         * Create a new weighted-rectangle from a non-{@code null} rectangle; the {@code weight}
         * can be unbounded.
         */
        public WeightedRectangle(android.graphics.Rect rect, int weight) {
            this.rect = checkNotNull(rect, "rect must not be null");
            this.weight = weight;
        }

        /**
         * Convert to a metering rectangle, clipping any of the values to stay within range.
         *
         * <p>If values are clipped, a warning is printed to logcat.</p>
         *
         * @return a new metering rectangle
         */
        public android.hardware.camera2.params.MeteringRectangle toMetering() {
            int weight = android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle.clip(this.weight, android.hardware.camera2.params.MeteringRectangle.METERING_WEIGHT_MIN, android.hardware.camera2.params.MeteringRectangle.METERING_WEIGHT_MAX, rect, "weight");
            int x = /* lo */
            android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle.clipLower(rect.left, 0, rect, "left");
            int y = /* lo */
            android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle.clipLower(rect.top, 0, rect, "top");
            int w = /* lo */
            android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle.clipLower(rect.width(), 0, rect, "width");
            int h = /* lo */
            android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle.clipLower(rect.height(), 0, rect, "height");
            return new android.hardware.camera2.params.MeteringRectangle(x, y, w, h, weight);
        }

        /**
         * Convert to a face; the rect is considered to be the bounds, and the weight
         * is considered to be the score.
         *
         * <p>If the score is out of range of {@value Face#SCORE_MIN}, {@value Face#SCORE_MAX},
         * the score is clipped first and a warning is printed to logcat.</p>
         *
         * <p>If the id is negative, the id is changed to 0 and a warning is printed to
         * logcat.</p>
         *
         * <p>All other parameters are passed-through as-is.</p>
         *
         * @return a new face with the optional features set
         */
        public android.hardware.camera2.params.Face toFace(int id, android.graphics.Point leftEyePosition, android.graphics.Point rightEyePosition, android.graphics.Point mouthPosition) {
            int idSafe = /* lo */
            android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle.clipLower(id, 0, rect, "id");
            int score = android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle.clip(weight, android.hardware.camera2.params.Face.SCORE_MIN, android.hardware.camera2.params.Face.SCORE_MAX, rect, "score");
            return new android.hardware.camera2.params.Face(rect, score, idSafe, leftEyePosition, rightEyePosition, mouthPosition);
        }

        /**
         * Convert to a face; the rect is considered to be the bounds, and the weight
         * is considered to be the score.
         *
         * <p>If the score is out of range of {@value Face#SCORE_MIN}, {@value Face#SCORE_MAX},
         * the score is clipped first and a warning is printed to logcat.</p>
         *
         * <p>All other parameters are passed-through as-is.</p>
         *
         * @return a new face without the optional features
         */
        public android.hardware.camera2.params.Face toFace() {
            int score = android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle.clip(weight, android.hardware.camera2.params.Face.SCORE_MIN, android.hardware.camera2.params.Face.SCORE_MAX, rect, "score");
            return new android.hardware.camera2.params.Face(rect, score);
        }

        private static int clipLower(int value, int lo, android.graphics.Rect rect, java.lang.String name) {
            return /* hi */
            android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle.clip(value, lo, java.lang.Integer.MAX_VALUE, rect, name);
        }

        private static int clip(int value, int lo, int hi, android.graphics.Rect rect, java.lang.String name) {
            if (value < lo) {
                android.util.Log.w(android.hardware.camera2.legacy.ParameterUtils.TAG, (((("toMetering - Rectangle " + rect) + " ") + name) + " too small, clip to ") + lo);
                value = lo;
            } else
                if (value > hi) {
                    android.util.Log.w(android.hardware.camera2.legacy.ParameterUtils.TAG, (((("toMetering - Rectangle " + rect) + " ") + name) + " too small, clip to ") + hi);
                    value = hi;
                }

            return value;
        }
    }

    private static final java.lang.String TAG = "ParameterUtils";

    private static final boolean DEBUG = false;

    /**
     * getZoomRatios stores zoom ratios in 1/100 increments, e.x. a zoom of 3.2 is 320
     */
    private static final int ZOOM_RATIO_MULTIPLIER = 100;

    /**
     * Convert a camera API1 size into a util size
     */
    public static android.util.Size convertSize(android.hardware.Camera.Size size) {
        checkNotNull(size, "size must not be null");
        return new android.util.Size(size.width, size.height);
    }

    /**
     * Convert a camera API1 list of sizes into a util list of sizes
     */
    public static java.util.List<android.util.Size> convertSizeList(java.util.List<android.hardware.Camera.Size> sizeList) {
        checkNotNull(sizeList, "sizeList must not be null");
        java.util.List<android.util.Size> sizes = new java.util.ArrayList<>(sizeList.size());
        for (android.hardware.Camera.Size s : sizeList) {
            sizes.add(new android.util.Size(s.width, s.height));
        }
        return sizes;
    }

    /**
     * Convert a camera API1 list of sizes into an array of sizes
     */
    public static android.util.Size[] convertSizeListToArray(java.util.List<android.hardware.Camera.Size> sizeList) {
        checkNotNull(sizeList, "sizeList must not be null");
        android.util.Size[] array = new android.util.Size[sizeList.size()];
        int ctr = 0;
        for (android.hardware.Camera.Size s : sizeList) {
            array[ctr++] = new android.util.Size(s.width, s.height);
        }
        return array;
    }

    /**
     * Check if the camera API1 list of sizes contains a size with the given dimens.
     */
    public static boolean containsSize(java.util.List<android.hardware.Camera.Size> sizeList, int width, int height) {
        checkNotNull(sizeList, "sizeList must not be null");
        for (android.hardware.Camera.Size s : sizeList) {
            if ((s.height == height) && (s.width == width)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the largest supported picture size, as compared by its area.
     */
    public static android.util.Size getLargestSupportedJpegSizeByArea(android.hardware.Camera.Parameters params) {
        checkNotNull(params, "params must not be null");
        java.util.List<android.util.Size> supportedJpegSizes = android.hardware.camera2.legacy.ParameterUtils.convertSizeList(params.getSupportedPictureSizes());
        return android.hardware.camera2.utils.SizeAreaComparator.findLargestByArea(supportedJpegSizes);
    }

    /**
     * Convert a camera area into a human-readable string.
     */
    public static java.lang.String stringFromArea(android.hardware.Camera.Area area) {
        if (area == null) {
            return null;
        } else {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            android.graphics.Rect r = area.rect;
            sb.setLength(0);
            sb.append("([");
            sb.append(r.left);
            sb.append(',');
            sb.append(r.top);
            sb.append("][");
            sb.append(r.right);
            sb.append(',');
            sb.append(r.bottom);
            sb.append(']');
            sb.append(',');
            sb.append(area.weight);
            sb.append(')');
            return sb.toString();
        }
    }

    /**
     * Convert a camera area list into a human-readable string
     *
     * @param areaList
     * 		a list of areas (null is ok)
     */
    public static java.lang.String stringFromAreaList(java.util.List<android.hardware.Camera.Area> areaList) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (areaList == null) {
            return null;
        }
        int i = 0;
        for (android.hardware.Camera.Area area : areaList) {
            if (area == null) {
                sb.append("null");
            } else {
                sb.append(android.hardware.camera2.legacy.ParameterUtils.stringFromArea(area));
            }
            if (i != (areaList.size() - 1)) {
                sb.append(", ");
            }
            i++;
        }
        return sb.toString();
    }

    /**
     * Calculate the closest zoom index for the user-requested crop region by rounding
     * up to the closest (largest or equal) possible zoom crop.
     *
     * <p>If the requested crop region exceeds the size of the active array, it is
     * shrunk to fit inside of the active array first.</p>
     *
     * <p>Since all api1 camera devices only support a discrete set of zooms, we have
     * to translate the per-pixel-granularity requested crop region into a per-zoom-index
     * granularity.</p>
     *
     * <p>Furthermore, since the zoom index and zoom levels also depends on the field-of-view
     * of the preview, the current preview {@code streamSize} is also used.</p>
     *
     * <p>The calculated crop regions are then written to in-place to {@code reportedCropRegion}
     * and {@code previewCropRegion}, in coordinates relative to the active array.</p>
     *
     * @param params
     * 		non-{@code null} camera api1 parameters
     * @param activeArray
     * 		active array dimensions, in sensor space
     * @param streamSize
     * 		stream size dimensions, in pixels
     * @param cropRegion
     * 		user-specified crop region, in active array coordinates
     * @param reportedCropRegion
     * 		(out parameter) what the result for {@code cropRegion} looks like
     * @param previewCropRegion
     * 		(out parameter) what the visual preview crop is
     * @return the zoom index inclusively between 0 and {@code Parameters#getMaxZoom},
    where 0 means the camera is not zoomed
     * @throws NullPointerException
     * 		if any of the args were {@code null}
     */
    public static int getClosestAvailableZoomCrop(android.hardware.Camera.Parameters params, android.graphics.Rect activeArray, android.util.Size streamSize, android.graphics.Rect cropRegion, /* out */
    android.graphics.Rect reportedCropRegion, android.graphics.Rect previewCropRegion) {
        checkNotNull(params, "params must not be null");
        checkNotNull(activeArray, "activeArray must not be null");
        checkNotNull(streamSize, "streamSize must not be null");
        checkNotNull(reportedCropRegion, "reportedCropRegion must not be null");
        checkNotNull(previewCropRegion, "previewCropRegion must not be null");
        android.graphics.Rect actualCrop = new android.graphics.Rect(cropRegion);
        /* Shrink requested crop region to fit inside of the active array size */
        if (!actualCrop.intersect(activeArray)) {
            android.util.Log.w(android.hardware.camera2.legacy.ParameterUtils.TAG, "getClosestAvailableZoomCrop - Crop region out of range; " + "setting to active array size");
            actualCrop.set(activeArray);
        }
        android.graphics.Rect previewCrop = android.hardware.camera2.legacy.ParameterUtils.getPreviewCropRectangleUnzoomed(activeArray, streamSize);
        // Make the user-requested crop region the same aspect ratio as the preview stream size
        android.graphics.Rect cropRegionAsPreview = android.hardware.camera2.legacy.ParameterUtils.shrinkToSameAspectRatioCentered(previewCrop, actualCrop);
        if (android.hardware.camera2.legacy.ParameterUtils.DEBUG) {
            android.util.Log.v(android.hardware.camera2.legacy.ParameterUtils.TAG, "getClosestAvailableZoomCrop - actualCrop = " + actualCrop);
            android.util.Log.v(android.hardware.camera2.legacy.ParameterUtils.TAG, "getClosestAvailableZoomCrop - previewCrop = " + previewCrop);
            android.util.Log.v(android.hardware.camera2.legacy.ParameterUtils.TAG, "getClosestAvailableZoomCrop - cropRegionAsPreview = " + cropRegionAsPreview);
        }
        /* Iterate all available zoom rectangles and find the closest zoom index */
        android.graphics.Rect bestReportedCropRegion = null;
        android.graphics.Rect bestPreviewCropRegion = null;
        int bestZoomIndex = -1;
        java.util.List<android.graphics.Rect> availableReportedCropRegions = android.hardware.camera2.legacy.ParameterUtils.getAvailableZoomCropRectangles(params, activeArray);
        java.util.List<android.graphics.Rect> availablePreviewCropRegions = android.hardware.camera2.legacy.ParameterUtils.getAvailablePreviewZoomCropRectangles(params, activeArray, streamSize);
        if (android.hardware.camera2.legacy.ParameterUtils.DEBUG) {
            android.util.Log.v(android.hardware.camera2.legacy.ParameterUtils.TAG, "getClosestAvailableZoomCrop - availableReportedCropRegions = " + android.hardware.camera2.utils.ListUtils.listToString(availableReportedCropRegions));
            android.util.Log.v(android.hardware.camera2.legacy.ParameterUtils.TAG, "getClosestAvailableZoomCrop - availablePreviewCropRegions = " + android.hardware.camera2.utils.ListUtils.listToString(availablePreviewCropRegions));
        }
        if (availableReportedCropRegions.size() != availablePreviewCropRegions.size()) {
            throw new java.lang.AssertionError("available reported/preview crop region size mismatch");
        }
        for (int i = 0; i < availableReportedCropRegions.size(); ++i) {
            android.graphics.Rect currentPreviewCropRegion = availablePreviewCropRegions.get(i);
            android.graphics.Rect currentReportedCropRegion = availableReportedCropRegions.get(i);
            boolean isBest;
            if (bestZoomIndex == (-1)) {
                isBest = true;
            } else
                if ((currentPreviewCropRegion.width() >= cropRegionAsPreview.width()) && (currentPreviewCropRegion.height() >= cropRegionAsPreview.height())) {
                    isBest = true;
                } else {
                    isBest = false;
                }

            // Sizes are sorted largest-to-smallest, so once the available crop is too small,
            // we the rest are too small. Furthermore, this is the final best crop,
            // since its the largest crop that still fits the requested crop
            if (isBest) {
                bestPreviewCropRegion = currentPreviewCropRegion;
                bestReportedCropRegion = currentReportedCropRegion;
                bestZoomIndex = i;
            } else {
                break;
            }
        }
        if (bestZoomIndex == (-1)) {
            // Even in the worst case, we should always at least return 0 here
            throw new java.lang.AssertionError("Should've found at least one valid zoom index");
        }
        // Write the rectangles in-place
        reportedCropRegion.set(bestReportedCropRegion);
        previewCropRegion.set(bestPreviewCropRegion);
        return bestZoomIndex;
    }

    /**
     * Calculate the effective crop rectangle for this preview viewport;
     * assumes the preview is centered to the sensor and scaled to fit across one of the dimensions
     * without skewing.
     *
     * <p>The preview size must be a subset of the active array size; the resulting
     * rectangle will also be a subset of the active array rectangle.</p>
     *
     * <p>The unzoomed crop rectangle is calculated only.</p>
     *
     * @param activeArray
     * 		active array dimensions, in sensor space
     * @param previewSize
     * 		size of the preview buffer render target, in pixels (not in sensor space)
     * @return a rectangle which serves as the preview stream's effective crop region (unzoomed),
    in sensor space
     * @throws NullPointerException
     * 		if any of the args were {@code null}
     * @throws IllegalArgumentException
     * 		if {@code previewSize} is wider or taller than {@code activeArray}
     */
    private static android.graphics.Rect getPreviewCropRectangleUnzoomed(android.graphics.Rect activeArray, android.util.Size previewSize) {
        if (previewSize.getWidth() > activeArray.width()) {
            throw new java.lang.IllegalArgumentException("previewSize must not be wider than activeArray");
        } else
            if (previewSize.getHeight() > activeArray.height()) {
                throw new java.lang.IllegalArgumentException("previewSize must not be taller than activeArray");
            }

        float aspectRatioArray = (activeArray.width() * 1.0F) / activeArray.height();
        float aspectRatioPreview = (previewSize.getWidth() * 1.0F) / previewSize.getHeight();
        float cropH;
        float cropW;
        if (java.lang.Math.abs(aspectRatioPreview - aspectRatioArray) < android.hardware.camera2.legacy.ParameterUtils.ASPECT_RATIO_TOLERANCE) {
            cropH = activeArray.height();
            cropW = activeArray.width();
        } else
            if (aspectRatioPreview < aspectRatioArray) {
                // The new width must be smaller than the height, so scale the width by AR
                cropH = activeArray.height();
                cropW = cropH * aspectRatioPreview;
            } else {
                // The new height must be smaller (or equal) than the width, so scale the height by AR
                cropW = activeArray.width();
                cropH = cropW / aspectRatioPreview;
            }

        android.graphics.Matrix translateMatrix = new android.graphics.Matrix();
        android.graphics.RectF cropRect = /* left */
        /* top */
        new android.graphics.RectF(0, 0, cropW, cropH);
        // Now center the crop rectangle so its center is in the center of the active array
        translateMatrix.setTranslate(activeArray.exactCenterX(), activeArray.exactCenterY());
        translateMatrix.postTranslate(-cropRect.centerX(), -cropRect.centerY());
        /* inout */
        translateMatrix.mapRect(cropRect);
        // Round the rect corners towards the nearest integer values
        return android.hardware.camera2.utils.ParamsUtils.createRect(cropRect);
    }

    /**
     * Shrink the {@code shrinkTarget} rectangle to snugly fit inside of {@code reference};
     * the aspect ratio of {@code shrinkTarget} will change to be the same aspect ratio as
     * {@code reference}.
     *
     * <p>At most a single dimension will scale (down). Both dimensions will never be scaled.</p>
     *
     * @param reference
     * 		the rectangle whose aspect ratio will be used as the new aspect ratio
     * @param shrinkTarget
     * 		the rectangle which will be scaled down to have a new aspect ratio
     * @return a new rectangle, a subset of {@code shrinkTarget},
    whose aspect ratio will match that of {@code reference}
     */
    private static android.graphics.Rect shrinkToSameAspectRatioCentered(android.graphics.Rect reference, android.graphics.Rect shrinkTarget) {
        float aspectRatioReference = (reference.width() * 1.0F) / reference.height();
        float aspectRatioShrinkTarget = (shrinkTarget.width() * 1.0F) / shrinkTarget.height();
        float cropH;
        float cropW;
        if (aspectRatioShrinkTarget < aspectRatioReference) {
            // The new width must be smaller than the height, so scale the width by AR
            cropH = reference.height();
            cropW = cropH * aspectRatioShrinkTarget;
        } else {
            // The new height must be smaller (or equal) than the width, so scale the height by AR
            cropW = reference.width();
            cropH = cropW / aspectRatioShrinkTarget;
        }
        android.graphics.Matrix translateMatrix = new android.graphics.Matrix();
        android.graphics.RectF shrunkRect = new android.graphics.RectF(shrinkTarget);
        // Scale the rectangle down, but keep its center in the same place as before
        translateMatrix.setScale(cropW / reference.width(), cropH / reference.height(), shrinkTarget.exactCenterX(), shrinkTarget.exactCenterY());
        /* inout */
        translateMatrix.mapRect(shrunkRect);
        return android.hardware.camera2.utils.ParamsUtils.createRect(shrunkRect);
    }

    /**
     * Get the available 'crop' (zoom) rectangles for this camera that will be reported
     * via a {@code CaptureResult} when a zoom is requested.
     *
     * <p>These crops ignores the underlying preview buffer size, and will always be reported
     * the same values regardless of what configuration of outputs is used.</p>
     *
     * <p>When zoom is supported, this will return a list of {@code 1 + #getMaxZoom} size,
     * where each crop rectangle corresponds to a zoom ratio (and is centered at the middle).</p>
     *
     * <p>Each crop rectangle is changed to have the same aspect ratio as {@code streamSize},
     * by shrinking the rectangle if necessary.</p>
     *
     * <p>To get the reported crop region when applying a zoom to the sensor, use {@code streamSize}
     * = {@code activeArray size}.</p>
     *
     * @param params
     * 		non-{@code null} camera api1 parameters
     * @param activeArray
     * 		active array dimensions, in sensor space
     * @param streamSize
     * 		stream size dimensions, in pixels
     * @return a list of available zoom rectangles, sorted from least zoomed to most zoomed
     */
    public static java.util.List<android.graphics.Rect> getAvailableZoomCropRectangles(android.hardware.Camera.Parameters params, android.graphics.Rect activeArray) {
        checkNotNull(params, "params must not be null");
        checkNotNull(activeArray, "activeArray must not be null");
        return android.hardware.camera2.legacy.ParameterUtils.getAvailableCropRectangles(params, activeArray, android.hardware.camera2.utils.ParamsUtils.createSize(activeArray));
    }

    /**
     * Get the available 'crop' (zoom) rectangles for this camera.
     *
     * <p>This is the effective (real) crop that is applied by the camera api1 device
     * when projecting the zoom onto the intermediate preview buffer. Use this when
     * deciding which zoom ratio to apply.</p>
     *
     * <p>When zoom is supported, this will return a list of {@code 1 + #getMaxZoom} size,
     * where each crop rectangle corresponds to a zoom ratio (and is centered at the middle).</p>
     *
     * <p>Each crop rectangle is changed to have the same aspect ratio as {@code streamSize},
     * by shrinking the rectangle if necessary.</p>
     *
     * <p>To get the reported crop region when applying a zoom to the sensor, use {@code streamSize}
     * = {@code activeArray size}.</p>
     *
     * @param params
     * 		non-{@code null} camera api1 parameters
     * @param activeArray
     * 		active array dimensions, in sensor space
     * @param streamSize
     * 		stream size dimensions, in pixels
     * @return a list of available zoom rectangles, sorted from least zoomed to most zoomed
     */
    public static java.util.List<android.graphics.Rect> getAvailablePreviewZoomCropRectangles(android.hardware.Camera.Parameters params, android.graphics.Rect activeArray, android.util.Size previewSize) {
        checkNotNull(params, "params must not be null");
        checkNotNull(activeArray, "activeArray must not be null");
        checkNotNull(previewSize, "previewSize must not be null");
        return android.hardware.camera2.legacy.ParameterUtils.getAvailableCropRectangles(params, activeArray, previewSize);
    }

    /**
     * Get the available 'crop' (zoom) rectangles for this camera.
     *
     * <p>When zoom is supported, this will return a list of {@code 1 + #getMaxZoom} size,
     * where each crop rectangle corresponds to a zoom ratio (and is centered at the middle).</p>
     *
     * <p>Each crop rectangle is changed to have the same aspect ratio as {@code streamSize},
     * by shrinking the rectangle if necessary.</p>
     *
     * <p>To get the reported crop region when applying a zoom to the sensor, use {@code streamSize}
     * = {@code activeArray size}.</p>
     *
     * @param params
     * 		non-{@code null} camera api1 parameters
     * @param activeArray
     * 		active array dimensions, in sensor space
     * @param streamSize
     * 		stream size dimensions, in pixels
     * @return a list of available zoom rectangles, sorted from least zoomed to most zoomed
     */
    private static java.util.List<android.graphics.Rect> getAvailableCropRectangles(android.hardware.Camera.Parameters params, android.graphics.Rect activeArray, android.util.Size streamSize) {
        checkNotNull(params, "params must not be null");
        checkNotNull(activeArray, "activeArray must not be null");
        checkNotNull(streamSize, "streamSize must not be null");
        // TODO: change all uses of Rect activeArray to Size activeArray,
        // since we want the crop to be active-array relative, not pixel-array relative
        android.graphics.Rect unzoomedStreamCrop = android.hardware.camera2.legacy.ParameterUtils.getPreviewCropRectangleUnzoomed(activeArray, streamSize);
        if (!params.isZoomSupported()) {
            // Trivial case: No zoom -> only support the full size as the crop region
            return new java.util.ArrayList<>(java.util.Arrays.asList(unzoomedStreamCrop));
        }
        java.util.List<android.graphics.Rect> zoomCropRectangles = new java.util.ArrayList<>(params.getMaxZoom() + 1);
        android.graphics.Matrix scaleMatrix = new android.graphics.Matrix();
        android.graphics.RectF scaledRect = new android.graphics.RectF();
        for (int zoom : params.getZoomRatios()) {
            float shrinkRatio = (android.hardware.camera2.legacy.ParameterUtils.ZOOM_RATIO_MULTIPLIER * 1.0F) / zoom;// normalize to 1.0 and smaller

            // set scaledRect to unzoomedStreamCrop
            /* out */
            android.hardware.camera2.utils.ParamsUtils.convertRectF(unzoomedStreamCrop, scaledRect);
            scaleMatrix.setScale(shrinkRatio, shrinkRatio, activeArray.exactCenterX(), activeArray.exactCenterY());
            scaleMatrix.mapRect(scaledRect);
            android.graphics.Rect intRect = android.hardware.camera2.utils.ParamsUtils.createRect(scaledRect);
            // Round the rect corners towards the nearest integer values
            zoomCropRectangles.add(intRect);
        }
        return zoomCropRectangles;
    }

    /**
     * Get the largest possible zoom ratio (normalized to {@code 1.0f} and higher)
     * that the camera can support.
     *
     * <p>If the camera does not support zoom, it always returns {@code 1.0f}.</p>
     *
     * @param params
     * 		non-{@code null} camera api1 parameters
     * @return normalized max zoom ratio, at least {@code 1.0f}
     */
    public static float getMaxZoomRatio(android.hardware.Camera.Parameters params) {
        if (!params.isZoomSupported()) {
            return 1.0F;// no zoom

        }
        java.util.List<java.lang.Integer> zoomRatios = params.getZoomRatios();// sorted smallest->largest

        int zoom = zoomRatios.get(zoomRatios.size() - 1);// largest zoom ratio

        float zoomRatio = (zoom * 1.0F) / android.hardware.camera2.legacy.ParameterUtils.ZOOM_RATIO_MULTIPLIER;// normalize to 1.0 and smaller

        return zoomRatio;
    }

    /**
     * Returns the component-wise zoom ratio (each greater or equal than {@code 1.0});
     * largest values means more zoom.
     *
     * @param activeArraySize
     * 		active array size of the sensor (e.g. max jpeg size)
     * @param cropSize
     * 		size of the crop/zoom
     * @return {@link SizeF} with width/height being the component-wise zoom ratio
     * @throws NullPointerException
     * 		if any of the args were {@code null}
     * @throws IllegalArgumentException
     * 		if any component of {@code cropSize} was {@code 0}
     */
    private static android.util.SizeF getZoomRatio(android.util.Size activeArraySize, android.util.Size cropSize) {
        checkNotNull(activeArraySize, "activeArraySize must not be null");
        checkNotNull(cropSize, "cropSize must not be null");
        checkArgumentPositive(cropSize.getWidth(), "cropSize.width must be positive");
        checkArgumentPositive(cropSize.getHeight(), "cropSize.height must be positive");
        float zoomRatioWidth = (activeArraySize.getWidth() * 1.0F) / cropSize.getWidth();
        float zoomRatioHeight = (activeArraySize.getHeight() * 1.0F) / cropSize.getHeight();
        return new android.util.SizeF(zoomRatioWidth, zoomRatioHeight);
    }

    /**
     * Convert the user-specified crop region into zoom data; which can be used
     * to set the parameters to a specific zoom index, or to report back to the user what the
     * actual zoom was, or for other calculations requiring the current preview crop region.
     *
     * <p>None of the parameters are mutated.</p>
     *
     * @param activeArraySize
     * 		active array size of the sensor (e.g. max jpeg size)
     * @param cropRegion
     * 		the user-specified crop region
     * @param previewSize
     * 		the current preview size (in pixels)
     * @param params
     * 		the current camera parameters (not mutated)
     * @return the zoom index, and the effective/reported crop regions (relative to active array)
     */
    public static android.hardware.camera2.legacy.ParameterUtils.ZoomData convertScalerCropRegion(android.graphics.Rect activeArraySize, android.graphics.Rect cropRegion, android.util.Size previewSize, android.hardware.Camera.Parameters params) {
        android.graphics.Rect activeArraySizeOnly = /* left */
        /* top */
        new android.graphics.Rect(0, 0, activeArraySize.width(), activeArraySize.height());
        android.graphics.Rect userCropRegion = cropRegion;
        if (userCropRegion == null) {
            userCropRegion = activeArraySizeOnly;
        }
        if (android.hardware.camera2.legacy.ParameterUtils.DEBUG) {
            android.util.Log.v(android.hardware.camera2.legacy.ParameterUtils.TAG, "convertScalerCropRegion - user crop region was " + userCropRegion);
        }
        final android.graphics.Rect reportedCropRegion = new android.graphics.Rect();
        final android.graphics.Rect previewCropRegion = new android.graphics.Rect();
        final int zoomIdx = /* out */
        /* out */
        android.hardware.camera2.legacy.ParameterUtils.getClosestAvailableZoomCrop(params, activeArraySizeOnly, previewSize, userCropRegion, reportedCropRegion, previewCropRegion);
        if (android.hardware.camera2.legacy.ParameterUtils.DEBUG) {
            android.util.Log.v(android.hardware.camera2.legacy.ParameterUtils.TAG, ((((("convertScalerCropRegion - zoom calculated to: " + "zoomIndex = ") + zoomIdx) + ", reported crop region = ") + reportedCropRegion) + ", preview crop region = ") + previewCropRegion);
        }
        return new android.hardware.camera2.legacy.ParameterUtils.ZoomData(zoomIdx, previewCropRegion, reportedCropRegion);
    }

    /**
     * Calculate the actual/effective/reported normalized rectangle data from a metering
     * rectangle.
     *
     * <p>If any of the rectangles are out-of-range of their intended bounding box,
     * the {@link #RECTANGLE_EMPTY empty rectangle} is substituted instead
     * (with a weight of {@code 0}).</p>
     *
     * <p>The metering rectangle is bound by the crop region (effective/reported respectively).
     * The metering {@link Camera.Area area} is bound by {@code [-1000, 1000]}.</p>
     *
     * <p>No parameters are mutated; returns the new metering data.</p>
     *
     * @param activeArraySize
     * 		active array size of the sensor (e.g. max jpeg size)
     * @param meteringRect
     * 		the user-specified metering rectangle
     * @param zoomData
     * 		the calculated zoom data corresponding to this request
     * @return the metering area, the reported/effective metering rectangles
     */
    public static android.hardware.camera2.legacy.ParameterUtils.MeteringData convertMeteringRectangleToLegacy(android.graphics.Rect activeArray, android.hardware.camera2.params.MeteringRectangle meteringRect, android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData) {
        android.graphics.Rect previewCrop = zoomData.previewCrop;
        float scaleW = ((android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MAX - android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MIN) * 1.0F) / previewCrop.width();
        float scaleH = ((android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MAX - android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MIN) * 1.0F) / previewCrop.height();
        android.graphics.Matrix transform = new android.graphics.Matrix();
        // Move the preview crop so that top,left is at (0,0), otherwise after scaling
        // the corner bounds will be outside of [-1000, 1000]
        transform.setTranslate(-previewCrop.left, -previewCrop.top);
        // Scale into [0, 2000] range about the center of the preview
        transform.postScale(scaleW, scaleH);
        // Move so that top left of a typical rect is at [-1000, -1000]
        /* dx */
        /* dy */
        transform.postTranslate(android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MIN, android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MIN);
        /* Calculate the preview metering region (effective), and the camera1 api
        normalized metering region.
         */
        android.graphics.Rect normalizedRegionUnbounded = android.hardware.camera2.utils.ParamsUtils.mapRect(transform, meteringRect.getRect());
        /* Try to intersect normalized area with [-1000, 1000] rectangle; otherwise
        it's completely out of range
         */
        android.graphics.Rect normalizedIntersected = new android.graphics.Rect(normalizedRegionUnbounded);
        android.hardware.Camera.Area meteringArea;
        if (!normalizedIntersected.intersect(android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_DEFAULT)) {
            android.util.Log.w(android.hardware.camera2.legacy.ParameterUtils.TAG, "convertMeteringRectangleToLegacy - metering rectangle too small, " + "no metering will be done");
            normalizedIntersected.set(android.hardware.camera2.legacy.ParameterUtils.RECTANGLE_EMPTY);
            meteringArea = new android.hardware.Camera.Area(android.hardware.camera2.legacy.ParameterUtils.RECTANGLE_EMPTY, android.hardware.camera2.params.MeteringRectangle.METERING_WEIGHT_DONT_CARE);
        } else {
            meteringArea = new android.hardware.Camera.Area(normalizedIntersected, meteringRect.getMeteringWeight());
        }
        /* Calculate effective preview metering region */
        android.graphics.Rect previewMetering = meteringRect.getRect();
        if (!previewMetering.intersect(previewCrop)) {
            previewMetering.set(android.hardware.camera2.legacy.ParameterUtils.RECTANGLE_EMPTY);
        }
        /* Calculate effective reported metering region
        - Transform the calculated metering area back into active array space
        - Clip it to be a subset of the reported crop region
         */
        android.graphics.Rect reportedMetering;
        {
            android.hardware.Camera.Area normalizedAreaUnbounded = new android.hardware.Camera.Area(normalizedRegionUnbounded, meteringRect.getMeteringWeight());
            android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle reportedMeteringRect = /* usePreviewCrop */
            android.hardware.camera2.legacy.ParameterUtils.convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, normalizedAreaUnbounded, false);
            reportedMetering = reportedMeteringRect.rect;
        }
        if (android.hardware.camera2.legacy.ParameterUtils.DEBUG) {
            android.util.Log.v(android.hardware.camera2.legacy.ParameterUtils.TAG, java.lang.String.format("convertMeteringRectangleToLegacy - activeArray = %s, meteringRect = %s, " + ("previewCrop = %s, meteringArea = %s, previewMetering = %s, " + "reportedMetering = %s, normalizedRegionUnbounded = %s"), activeArray, meteringRect, previewCrop, android.hardware.camera2.legacy.ParameterUtils.stringFromArea(meteringArea), previewMetering, reportedMetering, normalizedRegionUnbounded));
        }
        return new android.hardware.camera2.legacy.ParameterUtils.MeteringData(meteringArea, previewMetering, reportedMetering);
    }

    /**
     * Convert the normalized camera area from [-1000, 1000] coordinate space
     * into the active array-based coordinate space.
     *
     * <p>Values out of range are clipped to be within the resulting (reported) crop
     * region. It is possible to have values larger than the preview crop.</p>
     *
     * <p>Weights out of range of [0, 1000] are clipped to be within the range.</p>
     *
     * @param activeArraySize
     * 		active array size of the sensor (e.g. max jpeg size)
     * @param zoomData
     * 		the calculated zoom data corresponding to this request
     * @param area
     * 		the normalized camera area
     * @return the weighed rectangle in active array coordinate space, with the weight
     */
    public static android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle convertCameraAreaToActiveArrayRectangle(android.graphics.Rect activeArray, android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData, android.hardware.Camera.Area area) {
        return /* usePreviewCrop */
        android.hardware.camera2.legacy.ParameterUtils.convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, area, true);
    }

    /**
     * Convert an api1 face into an active-array based api2 face.
     *
     * <p>Out-of-ranges scores and ids will be clipped to be within range (with a warning).</p>
     *
     * @param face
     * 		a non-{@code null} api1 face
     * @param activeArraySize
     * 		active array size of the sensor (e.g. max jpeg size)
     * @param zoomData
     * 		the calculated zoom data corresponding to this request
     * @return a non-{@code null} api2 face
     * @throws NullPointerException
     * 		if the {@code face} was {@code null}
     */
    public static android.hardware.camera2.params.Face convertFaceFromLegacy(android.hardware.Camera.Face face, android.graphics.Rect activeArray, android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData) {
        checkNotNull(face, "face must not be null");
        android.hardware.camera2.params.Face api2Face;
        android.hardware.Camera.Area fakeArea = /* weight */
        new android.hardware.Camera.Area(face.rect, 1);
        android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle faceRect = android.hardware.camera2.legacy.ParameterUtils.convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, fakeArea);
        android.graphics.Point leftEye = face.leftEye;
        android.graphics.Point rightEye = face.rightEye;
        android.graphics.Point mouth = face.mouth;
        if (((((((((leftEye != null) && (rightEye != null)) && (mouth != null)) && (leftEye.x != (-2000))) && (leftEye.y != (-2000))) && (rightEye.x != (-2000))) && (rightEye.y != (-2000))) && (mouth.x != (-2000))) && (mouth.y != (-2000))) {
            leftEye = /* usePreviewCrop */
            android.hardware.camera2.legacy.ParameterUtils.convertCameraPointToActiveArrayPoint(activeArray, zoomData, leftEye, true);
            rightEye = /* usePreviewCrop */
            android.hardware.camera2.legacy.ParameterUtils.convertCameraPointToActiveArrayPoint(activeArray, zoomData, leftEye, true);
            mouth = /* usePreviewCrop */
            android.hardware.camera2.legacy.ParameterUtils.convertCameraPointToActiveArrayPoint(activeArray, zoomData, leftEye, true);
            api2Face = faceRect.toFace(face.id, leftEye, rightEye, mouth);
        } else {
            api2Face = faceRect.toFace();
        }
        return api2Face;
    }

    private static android.graphics.Point convertCameraPointToActiveArrayPoint(android.graphics.Rect activeArray, android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData, android.graphics.Point point, boolean usePreviewCrop) {
        android.graphics.Rect pointedRect = new android.graphics.Rect(point.x, point.y, point.x, point.y);
        android.hardware.Camera.Area pointedArea = /* weight */
        new android.hardware.Camera.Area(pointedRect, 1);
        android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle adjustedRect = android.hardware.camera2.legacy.ParameterUtils.convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, pointedArea, usePreviewCrop);
        android.graphics.Point transformedPoint = new android.graphics.Point(adjustedRect.rect.left, adjustedRect.rect.top);
        return transformedPoint;
    }

    private static android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle convertCameraAreaToActiveArrayRectangle(android.graphics.Rect activeArray, android.hardware.camera2.legacy.ParameterUtils.ZoomData zoomData, android.hardware.Camera.Area area, boolean usePreviewCrop) {
        android.graphics.Rect previewCrop = zoomData.previewCrop;
        android.graphics.Rect reportedCrop = zoomData.reportedCrop;
        float scaleW = (previewCrop.width() * 1.0F) / (android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MAX - android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MIN);
        float scaleH = (previewCrop.height() * 1.0F) / (android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MAX - android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MIN);
        /* Calculate the reported metering region from the non-intersected normalized region
        by scaling and translating back into active array-relative coordinates.
         */
        android.graphics.Matrix transform = new android.graphics.Matrix();
        // Move top left from (-1000, -1000) to (0, 0)
        /* dx */
        /* dy */
        transform.setTranslate(android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MAX, android.hardware.camera2.legacy.ParameterUtils.NORMALIZED_RECTANGLE_MAX);
        // Scale from [0, 2000] back into the preview rectangle
        transform.postScale(scaleW, scaleH);
        // Move the rect so that the [-1000,-1000] point ends up at the preview [left, top]
        transform.postTranslate(previewCrop.left, previewCrop.top);
        android.graphics.Rect cropToIntersectAgainst = (usePreviewCrop) ? previewCrop : reportedCrop;
        // Now apply the transformation backwards to get the reported metering region
        android.graphics.Rect reportedMetering = android.hardware.camera2.utils.ParamsUtils.mapRect(transform, area.rect);
        // Intersect it with the crop region, to avoid reporting out-of-bounds
        // metering regions
        if (!reportedMetering.intersect(cropToIntersectAgainst)) {
            reportedMetering.set(android.hardware.camera2.legacy.ParameterUtils.RECTANGLE_EMPTY);
        }
        int weight = area.weight;
        if (weight < android.hardware.camera2.params.MeteringRectangle.METERING_WEIGHT_MIN) {
            android.util.Log.w(android.hardware.camera2.legacy.ParameterUtils.TAG, ("convertCameraAreaToMeteringRectangle - rectangle " + android.hardware.camera2.legacy.ParameterUtils.stringFromArea(area)) + " has too small weight, clip to 0");
            weight = 0;
        }
        return new android.hardware.camera2.legacy.ParameterUtils.WeightedRectangle(reportedMetering, area.weight);
    }

    private ParameterUtils() {
        throw new java.lang.AssertionError();
    }
}

