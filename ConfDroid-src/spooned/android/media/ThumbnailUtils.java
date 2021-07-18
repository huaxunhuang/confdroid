/**
 * Copyright (C) 2009 The Android Open Source Project
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
 * Thumbnail generation routines for media provider.
 */
public class ThumbnailUtils {
    private static final java.lang.String TAG = "ThumbnailUtils";

    /* Maximum pixels size for created bitmap. */
    private static final int MAX_NUM_PIXELS_THUMBNAIL = 512 * 384;

    private static final int MAX_NUM_PIXELS_MICRO_THUMBNAIL = 160 * 120;

    private static final int UNCONSTRAINED = -1;

    /* Options used internally. */
    private static final int OPTIONS_NONE = 0x0;

    private static final int OPTIONS_SCALE_UP = 0x1;

    /**
     * Constant used to indicate we should recycle the input in
     * {@link #extractThumbnail(Bitmap, int, int, int)} unless the output is the input.
     */
    public static final int OPTIONS_RECYCLE_INPUT = 0x2;

    /**
     * Constant used to indicate the dimension of mini thumbnail.
     *
     * @unknown Only used by media framework and media provider internally.
     */
    public static final int TARGET_SIZE_MINI_THUMBNAIL = 320;

    /**
     * Constant used to indicate the dimension of micro thumbnail.
     *
     * @unknown Only used by media framework and media provider internally.
     */
    public static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;

    /**
     * This method first examines if the thumbnail embedded in EXIF is bigger than our target
     * size. If not, then it'll create a thumbnail from original image. Due to efficiency
     * consideration, we want to let MediaThumbRequest avoid calling this method twice for
     * both kinds, so it only requests for MICRO_KIND and set saveImage to true.
     *
     * This method always returns a "square thumbnail" for MICRO_KIND thumbnail.
     *
     * @param filePath
     * 		the path of image file
     * @param kind
     * 		could be MINI_KIND or MICRO_KIND
     * @return Bitmap, or null on failures
     * @unknown This method is only used by media framework and media provider internally.
     */
    public static android.graphics.Bitmap createImageThumbnail(java.lang.String filePath, int kind) {
        boolean wantMini = kind == android.provider.MediaStore.Images.Thumbnails.MINI_KIND;
        int targetSize = (wantMini) ? android.media.ThumbnailUtils.TARGET_SIZE_MINI_THUMBNAIL : android.media.ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL;
        int maxPixels = (wantMini) ? android.media.ThumbnailUtils.MAX_NUM_PIXELS_THUMBNAIL : android.media.ThumbnailUtils.MAX_NUM_PIXELS_MICRO_THUMBNAIL;
        android.media.ThumbnailUtils.SizedThumbnailBitmap sizedThumbnailBitmap = new android.media.ThumbnailUtils.SizedThumbnailBitmap();
        android.graphics.Bitmap bitmap = null;
        android.media.MediaFile.MediaFileType fileType = android.media.MediaFile.getFileType(filePath);
        if ((fileType != null) && ((fileType.fileType == android.media.MediaFile.FILE_TYPE_JPEG) || android.media.MediaFile.isRawImageFileType(fileType.fileType))) {
            android.media.ThumbnailUtils.createThumbnailFromEXIF(filePath, targetSize, maxPixels, sizedThumbnailBitmap);
            bitmap = sizedThumbnailBitmap.mBitmap;
        }
        if (bitmap == null) {
            java.io.FileInputStream stream = null;
            try {
                stream = new java.io.FileInputStream(filePath);
                java.io.FileDescriptor fd = stream.getFD();
                android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
                options.inSampleSize = 1;
                options.inJustDecodeBounds = true;
                android.graphics.BitmapFactory.decodeFileDescriptor(fd, null, options);
                if ((options.mCancel || (options.outWidth == (-1))) || (options.outHeight == (-1))) {
                    return null;
                }
                options.inSampleSize = android.media.ThumbnailUtils.computeSampleSize(options, targetSize, maxPixels);
                options.inJustDecodeBounds = false;
                options.inDither = false;
                options.inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888;
                bitmap = android.graphics.BitmapFactory.decodeFileDescriptor(fd, null, options);
            } catch (java.io.IOException ex) {
                android.util.Log.e(android.media.ThumbnailUtils.TAG, "", ex);
            } catch (java.lang.OutOfMemoryError oom) {
                android.util.Log.e(android.media.ThumbnailUtils.TAG, ("Unable to decode file " + filePath) + ". OutOfMemoryError.", oom);
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (java.io.IOException ex) {
                    android.util.Log.e(android.media.ThumbnailUtils.TAG, "", ex);
                }
            }
        }
        if (kind == android.provider.MediaStore.Images.Thumbnails.MICRO_KIND) {
            // now we make it a "square thumbnail" for MICRO_KIND thumbnail
            bitmap = android.media.ThumbnailUtils.extractThumbnail(bitmap, android.media.ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL, android.media.ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL, android.media.ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    /**
     * Create a video thumbnail for a video. May return null if the video is
     * corrupt or the format is not supported.
     *
     * @param filePath
     * 		the path of video file
     * @param kind
     * 		could be MINI_KIND or MICRO_KIND
     */
    public static android.graphics.Bitmap createVideoThumbnail(java.lang.String filePath, int kind) {
        android.graphics.Bitmap bitmap = null;
        android.media.MediaMetadataRetriever retriever = new android.media.MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(-1);
        } catch (java.lang.IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (java.lang.RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (java.lang.RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (bitmap == null)
            return null;

        if (kind == android.provider.MediaStore.Images.Thumbnails.MINI_KIND) {
            // Scale down the bitmap if it's too large.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = java.lang.Math.max(width, height);
            if (max > 512) {
                float scale = 512.0F / max;
                int w = java.lang.Math.round(scale * width);
                int h = java.lang.Math.round(scale * height);
                bitmap = android.graphics.Bitmap.createScaledBitmap(bitmap, w, h, true);
            }
        } else
            if (kind == android.provider.MediaStore.Images.Thumbnails.MICRO_KIND) {
                bitmap = android.media.ThumbnailUtils.extractThumbnail(bitmap, android.media.ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL, android.media.ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL, android.media.ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            }

        return bitmap;
    }

    /**
     * Creates a centered bitmap of the desired size.
     *
     * @param source
     * 		original bitmap source
     * @param width
     * 		targeted width
     * @param height
     * 		targeted height
     */
    public static android.graphics.Bitmap extractThumbnail(android.graphics.Bitmap source, int width, int height) {
        return android.media.ThumbnailUtils.extractThumbnail(source, width, height, android.media.ThumbnailUtils.OPTIONS_NONE);
    }

    /**
     * Creates a centered bitmap of the desired size.
     *
     * @param source
     * 		original bitmap source
     * @param width
     * 		targeted width
     * @param height
     * 		targeted height
     * @param options
     * 		options used during thumbnail extraction
     */
    public static android.graphics.Bitmap extractThumbnail(android.graphics.Bitmap source, int width, int height, int options) {
        if (source == null) {
            return null;
        }
        float scale;
        if (source.getWidth() < source.getHeight()) {
            scale = width / ((float) (source.getWidth()));
        } else {
            scale = height / ((float) (source.getHeight()));
        }
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setScale(scale, scale);
        android.graphics.Bitmap thumbnail = android.media.ThumbnailUtils.transform(matrix, source, width, height, android.media.ThumbnailUtils.OPTIONS_SCALE_UP | options);
        return thumbnail;
    }

    /* Compute the sample size as a function of minSideLength
    and maxNumOfPixels.
    minSideLength is used to specify that minimal width or height of a
    bitmap.
    maxNumOfPixels is used to specify the maximal size in pixels that is
    tolerable in terms of memory usage.

    The function returns a sample size based on the constraints.
    Both size and minSideLength can be passed in as IImage.UNCONSTRAINED,
    which indicates no care of the corresponding constraint.
    The functions prefers returning a sample size that
    generates a smaller bitmap, unless minSideLength = IImage.UNCONSTRAINED.

    Also, the function rounds up the sample size to a power of 2 or multiple
    of 8 because BitmapFactory only honors sample size this way.
    For example, BitmapFactory downsamples an image by 2 even though the
    request is 3. So we round up the sample size to avoid OOM.
     */
    private static int computeSampleSize(android.graphics.BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = android.media.ThumbnailUtils.computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            } 
        } else {
            roundedSize = ((initialSize + 7) / 8) * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(android.graphics.BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == android.media.ThumbnailUtils.UNCONSTRAINED) ? 1 : ((int) (java.lang.Math.ceil(java.lang.Math.sqrt((w * h) / maxNumOfPixels))));
        int upperBound = (minSideLength == android.media.ThumbnailUtils.UNCONSTRAINED) ? 128 : ((int) (java.lang.Math.min(java.lang.Math.floor(w / minSideLength), java.lang.Math.floor(h / minSideLength))));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == android.media.ThumbnailUtils.UNCONSTRAINED) && (minSideLength == android.media.ThumbnailUtils.UNCONSTRAINED)) {
            return 1;
        } else
            if (minSideLength == android.media.ThumbnailUtils.UNCONSTRAINED) {
                return lowerBound;
            } else {
                return upperBound;
            }

    }

    /**
     * Make a bitmap from a given Uri, minimal side length, and maximum number of pixels.
     * The image data will be read from specified pfd if it's not null, otherwise
     * a new input stream will be created using specified ContentResolver.
     *
     * Clients are allowed to pass their own BitmapFactory.Options used for bitmap decoding. A
     * new BitmapFactory.Options will be created if options is null.
     */
    private static android.graphics.Bitmap makeBitmap(int minSideLength, int maxNumOfPixels, android.net.Uri uri, android.content.ContentResolver cr, android.os.ParcelFileDescriptor pfd, android.graphics.BitmapFactory.Options options) {
        android.graphics.Bitmap b = null;
        try {
            if (pfd == null)
                pfd = android.media.ThumbnailUtils.makeInputStream(uri, cr);

            if (pfd == null)
                return null;

            if (options == null)
                options = new android.graphics.BitmapFactory.Options();

            java.io.FileDescriptor fd = pfd.getFileDescriptor();
            options.inSampleSize = 1;
            options.inJustDecodeBounds = true;
            android.graphics.BitmapFactory.decodeFileDescriptor(fd, null, options);
            if ((options.mCancel || (options.outWidth == (-1))) || (options.outHeight == (-1))) {
                return null;
            }
            options.inSampleSize = android.media.ThumbnailUtils.computeSampleSize(options, minSideLength, maxNumOfPixels);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888;
            b = android.graphics.BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (java.lang.OutOfMemoryError ex) {
            android.util.Log.e(android.media.ThumbnailUtils.TAG, "Got oom exception ", ex);
            return null;
        } finally {
            android.media.ThumbnailUtils.closeSilently(pfd);
        }
        return b;
    }

    private static void closeSilently(android.os.ParcelFileDescriptor c) {
        if (c == null)
            return;

        try {
            c.close();
        } catch (java.lang.Throwable t) {
            // do nothing
        }
    }

    private static android.os.ParcelFileDescriptor makeInputStream(android.net.Uri uri, android.content.ContentResolver cr) {
        try {
            return cr.openFileDescriptor(uri, "r");
        } catch (java.io.IOException ex) {
            return null;
        }
    }

    /**
     * Transform source Bitmap to targeted width and height.
     */
    private static android.graphics.Bitmap transform(android.graphics.Matrix scaler, android.graphics.Bitmap source, int targetWidth, int targetHeight, int options) {
        boolean scaleUp = (options & android.media.ThumbnailUtils.OPTIONS_SCALE_UP) != 0;
        boolean recycle = (options & android.media.ThumbnailUtils.OPTIONS_RECYCLE_INPUT) != 0;
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if ((!scaleUp) && ((deltaX < 0) || (deltaY < 0))) {
            /* In this case the bitmap is smaller, at least in one dimension,
            than the target.  Transform it by placing as much of the image
            as possible into the target and leaving the top/bottom or
            left/right (or both) black.
             */
            android.graphics.Bitmap b2 = android.graphics.Bitmap.createBitmap(targetWidth, targetHeight, android.graphics.Bitmap.Config.ARGB_8888);
            android.graphics.Canvas c = new android.graphics.Canvas(b2);
            int deltaXHalf = java.lang.Math.max(0, deltaX / 2);
            int deltaYHalf = java.lang.Math.max(0, deltaY / 2);
            android.graphics.Rect src = new android.graphics.Rect(deltaXHalf, deltaYHalf, deltaXHalf + java.lang.Math.min(targetWidth, source.getWidth()), deltaYHalf + java.lang.Math.min(targetHeight, source.getHeight()));
            int dstX = (targetWidth - src.width()) / 2;
            int dstY = (targetHeight - src.height()) / 2;
            android.graphics.Rect dst = new android.graphics.Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY);
            c.drawBitmap(source, src, dst, null);
            if (recycle) {
                source.recycle();
            }
            c.setBitmap(null);
            return b2;
        }
        float bitmapWidthF = source.getWidth();
        float bitmapHeightF = source.getHeight();
        float bitmapAspect = bitmapWidthF / bitmapHeightF;
        float viewAspect = ((float) (targetWidth)) / targetHeight;
        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeightF;
            if ((scale < 0.9F) || (scale > 1.0F)) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        } else {
            float scale = targetWidth / bitmapWidthF;
            if ((scale < 0.9F) || (scale > 1.0F)) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        }
        android.graphics.Bitmap b1;
        if (scaler != null) {
            // this is used for minithumb and crop, so we want to filter here.
            b1 = android.graphics.Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
        } else {
            b1 = source;
        }
        if (recycle && (b1 != source)) {
            source.recycle();
        }
        int dx1 = java.lang.Math.max(0, b1.getWidth() - targetWidth);
        int dy1 = java.lang.Math.max(0, b1.getHeight() - targetHeight);
        android.graphics.Bitmap b2 = android.graphics.Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth, targetHeight);
        if (b2 != b1) {
            if (recycle || (b1 != source)) {
                b1.recycle();
            }
        }
        return b2;
    }

    /**
     * SizedThumbnailBitmap contains the bitmap, which is downsampled either from
     * the thumbnail in exif or the full image.
     * mThumbnailData, mThumbnailWidth and mThumbnailHeight are set together only if mThumbnail
     * is not null.
     *
     * The width/height of the sized bitmap may be different from mThumbnailWidth/mThumbnailHeight.
     */
    private static class SizedThumbnailBitmap {
        public byte[] mThumbnailData;

        public android.graphics.Bitmap mBitmap;

        public int mThumbnailWidth;

        public int mThumbnailHeight;
    }

    /**
     * Creates a bitmap by either downsampling from the thumbnail in EXIF or the full image.
     * The functions returns a SizedThumbnailBitmap,
     * which contains a downsampled bitmap and the thumbnail data in EXIF if exists.
     */
    private static void createThumbnailFromEXIF(java.lang.String filePath, int targetSize, int maxPixels, android.media.ThumbnailUtils.SizedThumbnailBitmap sizedThumbBitmap) {
        if (filePath == null)
            return;

        android.media.ExifInterface exif = null;
        byte[] thumbData = null;
        try {
            exif = new android.media.ExifInterface(filePath);
            thumbData = exif.getThumbnail();
        } catch (java.io.IOException ex) {
            android.util.Log.w(android.media.ThumbnailUtils.TAG, ex);
        }
        android.graphics.BitmapFactory.Options fullOptions = new android.graphics.BitmapFactory.Options();
        android.graphics.BitmapFactory.Options exifOptions = new android.graphics.BitmapFactory.Options();
        int exifThumbWidth = 0;
        int fullThumbWidth = 0;
        // Compute exifThumbWidth.
        if (thumbData != null) {
            exifOptions.inJustDecodeBounds = true;
            android.graphics.BitmapFactory.decodeByteArray(thumbData, 0, thumbData.length, exifOptions);
            exifOptions.inSampleSize = android.media.ThumbnailUtils.computeSampleSize(exifOptions, targetSize, maxPixels);
            exifThumbWidth = exifOptions.outWidth / exifOptions.inSampleSize;
        }
        // Compute fullThumbWidth.
        fullOptions.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeFile(filePath, fullOptions);
        fullOptions.inSampleSize = android.media.ThumbnailUtils.computeSampleSize(fullOptions, targetSize, maxPixels);
        fullThumbWidth = fullOptions.outWidth / fullOptions.inSampleSize;
        // Choose the larger thumbnail as the returning sizedThumbBitmap.
        if ((thumbData != null) && (exifThumbWidth >= fullThumbWidth)) {
            int width = exifOptions.outWidth;
            int height = exifOptions.outHeight;
            exifOptions.inJustDecodeBounds = false;
            sizedThumbBitmap.mBitmap = android.graphics.BitmapFactory.decodeByteArray(thumbData, 0, thumbData.length, exifOptions);
            if (sizedThumbBitmap.mBitmap != null) {
                sizedThumbBitmap.mThumbnailData = thumbData;
                sizedThumbBitmap.mThumbnailWidth = width;
                sizedThumbBitmap.mThumbnailHeight = height;
            }
        } else {
            fullOptions.inJustDecodeBounds = false;
            sizedThumbBitmap.mBitmap = android.graphics.BitmapFactory.decodeFile(filePath, fullOptions);
        }
    }
}

