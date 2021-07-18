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
package android.media;


/**
 * Package private utility class for hosting commonly used Image related methods.
 */
class ImageUtils {
    /**
     * Only a subset of the formats defined in
     * {@link android.graphics.ImageFormat ImageFormat} and
     * {@link android.graphics.PixelFormat PixelFormat} are supported by
     * ImageReader. When reading RGB data from a surface, the formats defined in
     * {@link android.graphics.PixelFormat PixelFormat} can be used; when
     * reading YUV, JPEG or raw sensor data (for example, from the camera or video
     * decoder), formats from {@link android.graphics.ImageFormat ImageFormat}
     * are used.
     */
    public static int getNumPlanesForFormat(int format) {
        switch (format) {
            case android.graphics.ImageFormat.YV12 :
            case android.graphics.ImageFormat.YUV_420_888 :
            case android.graphics.ImageFormat.NV21 :
                return 3;
            case android.graphics.ImageFormat.NV16 :
                return 2;
            case android.graphics.PixelFormat.RGB_565 :
            case android.graphics.PixelFormat.RGBA_8888 :
            case android.graphics.PixelFormat.RGBX_8888 :
            case android.graphics.PixelFormat.RGB_888 :
            case android.graphics.ImageFormat.JPEG :
            case android.graphics.ImageFormat.YUY2 :
            case android.graphics.ImageFormat.Y8 :
            case android.graphics.ImageFormat.Y16 :
            case android.graphics.ImageFormat.RAW_SENSOR :
            case android.graphics.ImageFormat.RAW_PRIVATE :
            case android.graphics.ImageFormat.RAW10 :
            case android.graphics.ImageFormat.RAW12 :
            case android.graphics.ImageFormat.DEPTH16 :
            case android.graphics.ImageFormat.DEPTH_POINT_CLOUD :
                return 1;
            case android.graphics.ImageFormat.PRIVATE :
                return 0;
            default :
                throw new java.lang.UnsupportedOperationException(java.lang.String.format("Invalid format specified %d", format));
        }
    }

    /**
     * <p>
     * Copy source image data to destination Image.
     * </p>
     * <p>
     * Only support the copy between two non-{@link ImageFormat#PRIVATE PRIVATE} format
     * images with same properties (format, size, etc.). The data from the
     * source image will be copied to the byteBuffers from the destination Image
     * starting from position zero, and the destination image will be rewound to
     * zero after copy is done.
     * </p>
     *
     * @param src
     * 		The source image to be copied from.
     * @param dst
     * 		The destination image to be copied to.
     * @throws IllegalArgumentException
     * 		If the source and destination images
     * 		have different format, or one of the images is not copyable.
     */
    public static void imageCopy(android.media.Image src, android.media.Image dst) {
        if ((src == null) || (dst == null)) {
            throw new java.lang.IllegalArgumentException("Images should be non-null");
        }
        if (src.getFormat() != dst.getFormat()) {
            throw new java.lang.IllegalArgumentException("Src and dst images should have the same format");
        }
        if ((src.getFormat() == android.graphics.ImageFormat.PRIVATE) || (dst.getFormat() == android.graphics.ImageFormat.PRIVATE)) {
            throw new java.lang.IllegalArgumentException("PRIVATE format images are not copyable");
        }
        if (src.getFormat() == android.graphics.ImageFormat.RAW_PRIVATE) {
            throw new java.lang.IllegalArgumentException("Copy of RAW_OPAQUE format has not been implemented");
        }
        if (!(dst.getOwner() instanceof android.media.ImageWriter)) {
            throw new java.lang.IllegalArgumentException("Destination image is not from ImageWriter. Only" + " the images from ImageWriter are writable");
        }
        android.util.Size srcSize = new android.util.Size(src.getWidth(), src.getHeight());
        android.util.Size dstSize = new android.util.Size(dst.getWidth(), dst.getHeight());
        if (!srcSize.equals(dstSize)) {
            throw new java.lang.IllegalArgumentException((((("source image size " + srcSize) + " is different") + " with ") + "destination image size ") + dstSize);
        }
        android.media.Image.Plane[] srcPlanes = src.getPlanes();
        android.media.Image.Plane[] dstPlanes = dst.getPlanes();
        java.nio.ByteBuffer srcBuffer = null;
        java.nio.ByteBuffer dstBuffer = null;
        for (int i = 0; i < srcPlanes.length; i++) {
            int srcRowStride = srcPlanes[i].getRowStride();
            int dstRowStride = dstPlanes[i].getRowStride();
            srcBuffer = srcPlanes[i].getBuffer();
            dstBuffer = dstPlanes[i].getBuffer();
            if (!(srcBuffer.isDirect() && dstBuffer.isDirect())) {
                throw new java.lang.IllegalArgumentException("Source and destination ByteBuffers must be" + " direct byteBuffer!");
            }
            if (srcPlanes[i].getPixelStride() != dstPlanes[i].getPixelStride()) {
                throw new java.lang.IllegalArgumentException((("Source plane image pixel stride " + srcPlanes[i].getPixelStride()) + " must be same as destination image pixel stride ") + dstPlanes[i].getPixelStride());
            }
            int srcPos = srcBuffer.position();
            srcBuffer.rewind();
            dstBuffer.rewind();
            if (srcRowStride == dstRowStride) {
                // Fast path, just copy the content if the byteBuffer all together.
                dstBuffer.put(srcBuffer);
            } else {
                // Source and destination images may have different alignment requirements,
                // therefore may have different strides. Copy row by row for such case.
                int srcOffset = srcBuffer.position();
                int dstOffset = dstBuffer.position();
                android.util.Size effectivePlaneSize = android.media.ImageUtils.getEffectivePlaneSizeForImage(src, i);
                int srcByteCount = effectivePlaneSize.getWidth() * srcPlanes[i].getPixelStride();
                for (int row = 0; row < effectivePlaneSize.getHeight(); row++) {
                    if (row == (effectivePlaneSize.getHeight() - 1)) {
                        // Special case for NV21 backed YUV420_888: need handle the last row
                        // carefully to avoid memory corruption. Check if we have enough bytes to
                        // copy.
                        int remainingBytes = srcBuffer.remaining() - srcOffset;
                        if (srcByteCount > remainingBytes) {
                            srcByteCount = remainingBytes;
                        }
                    }
                    android.media.ImageUtils.directByteBufferCopy(srcBuffer, srcOffset, dstBuffer, dstOffset, srcByteCount);
                    srcOffset += srcRowStride;
                    dstOffset += dstRowStride;
                }
            }
            srcBuffer.position(srcPos);
            dstBuffer.rewind();
        }
    }

    /**
     * Return the estimated native allocation size in bytes based on width, height, format,
     * and number of images.
     *
     * <p>This is a very rough estimation and should only be used for native allocation
     * registration in VM so it can be accounted for during GC.</p>
     *
     * @param width
     * 		The width of the images.
     * @param height
     * 		The height of the images.
     * @param format
     * 		The format of the images.
     * @param numImages
     * 		The number of the images.
     */
    public static int getEstimatedNativeAllocBytes(int width, int height, int format, int numImages) {
        double estimatedBytePerPixel;
        switch (format) {
            // 10x compression from RGB_888
            case android.graphics.ImageFormat.JPEG :
            case android.graphics.ImageFormat.DEPTH_POINT_CLOUD :
                estimatedBytePerPixel = 0.3;
                break;
            case android.graphics.ImageFormat.Y8 :
                estimatedBytePerPixel = 1.0;
                break;
            case android.graphics.ImageFormat.RAW10 :
                estimatedBytePerPixel = 1.25;
                break;
            case android.graphics.ImageFormat.YV12 :
            case android.graphics.ImageFormat.YUV_420_888 :
            case android.graphics.ImageFormat.NV21 :
            case android.graphics.ImageFormat.RAW12 :
            case android.graphics.ImageFormat.PRIVATE :
                // A rough estimate because the real size is unknown.
                estimatedBytePerPixel = 1.5;
                break;
            case android.graphics.ImageFormat.NV16 :
            case android.graphics.PixelFormat.RGB_565 :
            case android.graphics.ImageFormat.YUY2 :
            case android.graphics.ImageFormat.Y16 :
            case android.graphics.ImageFormat.RAW_SENSOR :
            case android.graphics.ImageFormat.RAW_PRIVATE :
                // round estimate, real size is unknown
            case android.graphics.ImageFormat.DEPTH16 :
                estimatedBytePerPixel = 2.0;
                break;
            case android.graphics.PixelFormat.RGB_888 :
                estimatedBytePerPixel = 3.0;
                break;
            case android.graphics.PixelFormat.RGBA_8888 :
            case android.graphics.PixelFormat.RGBX_8888 :
                estimatedBytePerPixel = 4.0;
                break;
            default :
                throw new java.lang.UnsupportedOperationException(java.lang.String.format("Invalid format specified %d", format));
        }
        return ((int) (((width * height) * estimatedBytePerPixel) * numImages));
    }

    private static android.util.Size getEffectivePlaneSizeForImage(android.media.Image image, int planeIdx) {
        switch (image.getFormat()) {
            case android.graphics.ImageFormat.YV12 :
            case android.graphics.ImageFormat.YUV_420_888 :
            case android.graphics.ImageFormat.NV21 :
                if (planeIdx == 0) {
                    return new android.util.Size(image.getWidth(), image.getHeight());
                } else {
                    return new android.util.Size(image.getWidth() / 2, image.getHeight() / 2);
                }
            case android.graphics.ImageFormat.NV16 :
                if (planeIdx == 0) {
                    return new android.util.Size(image.getWidth(), image.getHeight());
                } else {
                    return new android.util.Size(image.getWidth(), image.getHeight() / 2);
                }
            case android.graphics.PixelFormat.RGB_565 :
            case android.graphics.PixelFormat.RGBA_8888 :
            case android.graphics.PixelFormat.RGBX_8888 :
            case android.graphics.PixelFormat.RGB_888 :
            case android.graphics.ImageFormat.JPEG :
            case android.graphics.ImageFormat.YUY2 :
            case android.graphics.ImageFormat.Y8 :
            case android.graphics.ImageFormat.Y16 :
            case android.graphics.ImageFormat.RAW_SENSOR :
            case android.graphics.ImageFormat.RAW10 :
            case android.graphics.ImageFormat.RAW12 :
                return new android.util.Size(image.getWidth(), image.getHeight());
            case android.graphics.ImageFormat.PRIVATE :
                return new android.util.Size(0, 0);
            default :
                throw new java.lang.UnsupportedOperationException(java.lang.String.format("Invalid image format %d", image.getFormat()));
        }
    }

    private static void directByteBufferCopy(java.nio.ByteBuffer srcBuffer, int srcOffset, java.nio.ByteBuffer dstBuffer, int dstOffset, int srcByteCount) {
        libcore.io.Memory.memmove(dstBuffer, dstOffset, srcBuffer, srcOffset, srcByteCount);
    }
}

