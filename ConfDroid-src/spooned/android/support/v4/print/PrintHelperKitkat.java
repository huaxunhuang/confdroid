/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.print;


/**
 * Kitkat specific PrintManager API implementation.
 */
class PrintHelperKitkat {
    private static final java.lang.String LOG_TAG = "PrintHelperKitkat";

    // will be <= 300 dpi on A4 (8.3×11.7) paper (worst case of 150 dpi)
    private static final int MAX_PRINT_SIZE = 3500;

    final android.content.Context mContext;

    android.graphics.BitmapFactory.Options mDecodeOptions = null;

    private final java.lang.Object mLock = new java.lang.Object();

    /**
     * image will be scaled but leave white space
     */
    public static final int SCALE_MODE_FIT = 1;

    /**
     * image will fill the paper and be cropped (default)
     */
    public static final int SCALE_MODE_FILL = 2;

    /**
     * select landscape (default)
     */
    public static final int ORIENTATION_LANDSCAPE = 1;

    /**
     * select portrait
     */
    public static final int ORIENTATION_PORTRAIT = 2;

    /**
     * this is a black and white image
     */
    public static final int COLOR_MODE_MONOCHROME = 1;

    /**
     * this is a color image (default)
     */
    public static final int COLOR_MODE_COLOR = 2;

    public interface OnPrintFinishCallback {
        public void onFinish();
    }

    /**
     * Whether the PrintActivity respects the suggested orientation
     */
    protected boolean mPrintActivityRespectsOrientation;

    /**
     * Whether the print subsystem handles min margins correctly. If not the print helper needs to
     * fake this.
     */
    protected boolean mIsMinMarginsHandlingCorrect;

    int mScaleMode = android.support.v4.print.PrintHelperKitkat.SCALE_MODE_FILL;

    int mColorMode = android.support.v4.print.PrintHelperKitkat.COLOR_MODE_COLOR;

    int mOrientation;

    PrintHelperKitkat(android.content.Context context) {
        mPrintActivityRespectsOrientation = true;
        mIsMinMarginsHandlingCorrect = true;
        mContext = context;
    }

    /**
     * Selects whether the image will fill the paper and be cropped
     * <p/>
     * {@link #SCALE_MODE_FIT}
     * or whether the image will be scaled but leave white space
     * {@link #SCALE_MODE_FILL}.
     *
     * @param scaleMode
     * 		{@link #SCALE_MODE_FIT} or
     * 		{@link #SCALE_MODE_FILL}
     */
    public void setScaleMode(int scaleMode) {
        mScaleMode = scaleMode;
    }

    /**
     * Returns the scale mode with which the image will fill the paper.
     *
     * @return The scale Mode: {@link #SCALE_MODE_FIT} or
    {@link #SCALE_MODE_FILL}
     */
    public int getScaleMode() {
        return mScaleMode;
    }

    /**
     * Sets whether the image will be printed in color (default)
     * {@link #COLOR_MODE_COLOR} or in back and white
     * {@link #COLOR_MODE_MONOCHROME}.
     *
     * @param colorMode
     * 		The color mode which is one of
     * 		{@link #COLOR_MODE_COLOR} and {@link #COLOR_MODE_MONOCHROME}.
     */
    public void setColorMode(int colorMode) {
        mColorMode = colorMode;
    }

    /**
     * Sets whether to select landscape (default), {@link #ORIENTATION_LANDSCAPE}
     * or portrait {@link #ORIENTATION_PORTRAIT}
     *
     * @param orientation
     * 		The page orientation which is one of
     * 		{@link #ORIENTATION_LANDSCAPE} or {@link #ORIENTATION_PORTRAIT}.
     */
    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    /**
     * Gets the page orientation with which the image will be printed.
     *
     * @return The preferred orientation which is one of
    {@link #ORIENTATION_LANDSCAPE} or {@link #ORIENTATION_PORTRAIT}
     */
    public int getOrientation() {
        // / Unset defaults to landscape but might turn image
        if (mOrientation == 0) {
            return android.support.v4.print.PrintHelperKitkat.ORIENTATION_LANDSCAPE;
        }
        return mOrientation;
    }

    /**
     * Gets the color mode with which the image will be printed.
     *
     * @return The color mode which is one of {@link #COLOR_MODE_COLOR}
    and {@link #COLOR_MODE_MONOCHROME}.
     */
    public int getColorMode() {
        return mColorMode;
    }

    /**
     * Check if the supplied bitmap should best be printed on a portrait orientation paper.
     *
     * @param bitmap
     * 		The bitmap to be printed.
     * @return true iff the picture should best be printed on a portrait orientation paper.
     */
    private static boolean isPortrait(android.graphics.Bitmap bitmap) {
        if (bitmap.getWidth() <= bitmap.getHeight()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create a build with a copy from the other print attributes.
     *
     * @param other
     * 		The other print attributes
     * @return A builder that will build print attributes that match the other attributes
     */
    protected android.print.PrintAttributes.Builder copyAttributes(android.print.PrintAttributes other) {
        android.print.PrintAttributes.Builder b = new android.print.PrintAttributes.Builder().setMediaSize(other.getMediaSize()).setResolution(other.getResolution()).setMinMargins(other.getMinMargins());
        if (other.getColorMode() != 0) {
            b.setColorMode(other.getColorMode());
        }
        return b;
    }

    /**
     * Prints a bitmap.
     *
     * @param jobName
     * 		The print job name.
     * @param bitmap
     * 		The bitmap to print.
     * @param callback
     * 		Optional callback to observe when printing is finished.
     */
    public void printBitmap(final java.lang.String jobName, final android.graphics.Bitmap bitmap, final android.support.v4.print.PrintHelperKitkat.OnPrintFinishCallback callback) {
        if (bitmap == null) {
            return;
        }
        final int fittingMode = mScaleMode;// grab the fitting mode at time of call

        android.print.PrintManager printManager = ((android.print.PrintManager) (mContext.getSystemService(android.content.Context.PRINT_SERVICE)));
        android.print.PrintAttributes.MediaSize mediaSize;
        if (android.support.v4.print.PrintHelperKitkat.isPortrait(bitmap)) {
            mediaSize = android.print.PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
        } else {
            mediaSize = android.print.PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
        }
        android.print.PrintAttributes attr = new android.print.PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(mColorMode).build();
        printManager.print(jobName, new android.print.PrintDocumentAdapter() {
            private android.print.PrintAttributes mAttributes;

            @java.lang.Override
            public void onLayout(android.print.PrintAttributes oldPrintAttributes, android.print.PrintAttributes newPrintAttributes, android.os.CancellationSignal cancellationSignal, android.print.PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, android.os.Bundle bundle) {
                mAttributes = newPrintAttributes;
                android.print.PrintDocumentInfo info = new android.print.PrintDocumentInfo.Builder(jobName).setContentType(android.print.PrintDocumentInfo.CONTENT_TYPE_PHOTO).setPageCount(1).build();
                boolean changed = !newPrintAttributes.equals(oldPrintAttributes);
                layoutResultCallback.onLayoutFinished(info, changed);
            }

            @java.lang.Override
            public void onWrite(android.print.PageRange[] pageRanges, android.os.ParcelFileDescriptor fileDescriptor, android.os.CancellationSignal cancellationSignal, android.print.PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
                writeBitmap(mAttributes, fittingMode, bitmap, fileDescriptor, cancellationSignal, writeResultCallback);
            }

            @java.lang.Override
            public void onFinish() {
                if (callback != null) {
                    callback.onFinish();
                }
            }
        }, attr);
    }

    /**
     * Calculates the transform the print an Image to fill the page
     *
     * @param imageWidth
     * 		with of bitmap
     * @param imageHeight
     * 		height of bitmap
     * @param content
     * 		The output page dimensions
     * @param fittingMode
     * 		The mode of fitting {@link #SCALE_MODE_FILL} vs {@link #SCALE_MODE_FIT}
     * @return Matrix to be used in canvas.drawBitmap(bitmap, matrix, null) call
     */
    private android.graphics.Matrix getMatrix(int imageWidth, int imageHeight, android.graphics.RectF content, int fittingMode) {
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        // Compute and apply scale to fill the page.
        float scale = content.width() / imageWidth;
        if (fittingMode == android.support.v4.print.PrintHelperKitkat.SCALE_MODE_FILL) {
            scale = java.lang.Math.max(scale, content.height() / imageHeight);
        } else {
            scale = java.lang.Math.min(scale, content.height() / imageHeight);
        }
        matrix.postScale(scale, scale);
        // Center the content.
        final float translateX = (content.width() - (imageWidth * scale)) / 2;
        final float translateY = (content.height() - (imageHeight * scale)) / 2;
        matrix.postTranslate(translateX, translateY);
        return matrix;
    }

    /**
     * Write a bitmap for a PDF document.
     *
     * @param attributes
     * 		The print attributes
     * @param fittingMode
     * 		How to fit the bitmap
     * @param bitmap
     * 		The bitmap to write
     * @param fileDescriptor
     * 		The file to write to
     * @param cancellationSignal
     * 		Signal cancelling operation
     * @param writeResultCallback
     * 		Callback to call once written
     */
    private void writeBitmap(final android.print.PrintAttributes attributes, final int fittingMode, final android.graphics.Bitmap bitmap, final android.os.ParcelFileDescriptor fileDescriptor, final android.os.CancellationSignal cancellationSignal, final android.print.PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
        final android.print.PrintAttributes pdfAttributes;
        if (mIsMinMarginsHandlingCorrect) {
            pdfAttributes = attributes;
        } else {
            // If the handling of any margin != 0 is broken, strip the margins and add them to the
            // bitmap later
            pdfAttributes = copyAttributes(attributes).setMinMargins(new android.print.PrintAttributes.Margins(0, 0, 0, 0)).build();
        }
        new android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Throwable>() {
            @java.lang.Override
            protected java.lang.Throwable doInBackground(java.lang.Void... params) {
                try {
                    if (cancellationSignal.isCanceled()) {
                        return null;
                    }
                    android.print.pdf.PrintedPdfDocument pdfDocument = new android.print.pdf.PrintedPdfDocument(mContext, pdfAttributes);
                    android.graphics.Bitmap maybeGrayscale = convertBitmapForColorMode(bitmap, pdfAttributes.getColorMode());
                    if (cancellationSignal.isCanceled()) {
                        return null;
                    }
                    try {
                        android.graphics.pdf.PdfDocument.Page page = pdfDocument.startPage(1);
                        android.graphics.RectF contentRect;
                        if (mIsMinMarginsHandlingCorrect) {
                            contentRect = new android.graphics.RectF(page.getInfo().getContentRect());
                        } else {
                            // Create dummy doc that has the margins to compute correctly sized
                            // content rectangle
                            android.print.pdf.PrintedPdfDocument dummyDocument = new android.print.pdf.PrintedPdfDocument(mContext, attributes);
                            android.graphics.pdf.PdfDocument.Page dummyPage = dummyDocument.startPage(1);
                            contentRect = new android.graphics.RectF(dummyPage.getInfo().getContentRect());
                            dummyDocument.finishPage(dummyPage);
                            dummyDocument.close();
                        }
                        // Resize bitmap
                        android.graphics.Matrix matrix = getMatrix(maybeGrayscale.getWidth(), maybeGrayscale.getHeight(), contentRect, fittingMode);
                        if (mIsMinMarginsHandlingCorrect) {
                            // The pdfDocument takes care of the positioning and margins
                        } else {
                            // Move it to the correct position.
                            matrix.postTranslate(contentRect.left, contentRect.top);
                            // Cut off margins
                            page.getCanvas().clipRect(contentRect);
                        }
                        // Draw the bitmap.
                        page.getCanvas().drawBitmap(maybeGrayscale, matrix, null);
                        // Finish the page.
                        pdfDocument.finishPage(page);
                        if (cancellationSignal.isCanceled()) {
                            return null;
                        }
                        // Write the document.
                        pdfDocument.writeTo(new java.io.FileOutputStream(fileDescriptor.getFileDescriptor()));
                        return null;
                    } finally {
                        pdfDocument.close();
                        if (fileDescriptor != null) {
                            try {
                                fileDescriptor.close();
                            } catch (java.io.IOException ioe) {
                                // ignore
                            }
                        }
                        // If we created a new instance for grayscaling, then recycle it here.
                        if (maybeGrayscale != bitmap) {
                            maybeGrayscale.recycle();
                        }
                    }
                } catch (java.lang.Throwable t) {
                    return t;
                }
            }

            @java.lang.Override
            protected void onPostExecute(java.lang.Throwable throwable) {
                if (cancellationSignal.isCanceled()) {
                    // Cancelled.
                    writeResultCallback.onWriteCancelled();
                } else
                    if (throwable == null) {
                        // Done.
                        writeResultCallback.onWriteFinished(new android.print.PageRange[]{ android.print.PageRange.ALL_PAGES });
                    } else {
                        // Failed.
                        android.util.Log.e(android.support.v4.print.PrintHelperKitkat.LOG_TAG, "Error writing printed content", throwable);
                        writeResultCallback.onWriteFailed(null);
                    }

            }
        }.execute();
    }

    /**
     * Prints an image located at the Uri. Image types supported are those of
     * <code>BitmapFactory.decodeStream</code> (JPEG, GIF, PNG, BMP, WEBP)
     *
     * @param jobName
     * 		The print job name.
     * @param imageFile
     * 		The <code>Uri</code> pointing to an image to print.
     * @param callback
     * 		Optional callback to observe when printing is finished.
     * @throws FileNotFoundException
     * 		if <code>Uri</code> is not pointing to a valid image.
     */
    public void printBitmap(final java.lang.String jobName, final android.net.Uri imageFile, final android.support.v4.print.PrintHelperKitkat.OnPrintFinishCallback callback) throws java.io.FileNotFoundException {
        final int fittingMode = mScaleMode;
        android.print.PrintDocumentAdapter printDocumentAdapter = new android.print.PrintDocumentAdapter() {
            private android.print.PrintAttributes mAttributes;

            android.os.AsyncTask<android.net.Uri, java.lang.Boolean, android.graphics.Bitmap> mLoadBitmap;

            android.graphics.Bitmap mBitmap = null;

            @java.lang.Override
            public void onLayout(final android.print.PrintAttributes oldPrintAttributes, final android.print.PrintAttributes newPrintAttributes, final android.os.CancellationSignal cancellationSignal, final android.print.PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, android.os.Bundle bundle) {
                synchronized(this) {
                    mAttributes = newPrintAttributes;
                }
                if (cancellationSignal.isCanceled()) {
                    layoutResultCallback.onLayoutCancelled();
                    return;
                }
                // we finished the load
                if (mBitmap != null) {
                    android.print.PrintDocumentInfo info = new android.print.PrintDocumentInfo.Builder(jobName).setContentType(android.print.PrintDocumentInfo.CONTENT_TYPE_PHOTO).setPageCount(1).build();
                    boolean changed = !newPrintAttributes.equals(oldPrintAttributes);
                    layoutResultCallback.onLayoutFinished(info, changed);
                    return;
                }
                mLoadBitmap = new android.os.AsyncTask<android.net.Uri, java.lang.Boolean, android.graphics.Bitmap>() {
                    @java.lang.Override
                    protected void onPreExecute() {
                        // First register for cancellation requests.
                        cancellationSignal.setOnCancelListener(new android.os.CancellationSignal.OnCancelListener() {
                            @java.lang.Override
                            public void onCancel() {
                                // on different thread
                                cancelLoad();
                                cancel(false);
                            }
                        });
                    }

                    @java.lang.Override
                    protected android.graphics.Bitmap doInBackground(android.net.Uri... uris) {
                        try {
                            return loadConstrainedBitmap(imageFile, android.support.v4.print.PrintHelperKitkat.MAX_PRINT_SIZE);
                        } catch (java.io.FileNotFoundException e) {
                            /* ignore */
                        }
                        return null;
                    }

                    @java.lang.Override
                    protected void onPostExecute(android.graphics.Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        // If orientation was not set by the caller, try to fit the bitmap on
                        // the current paper by potentially rotating the bitmap by 90 degrees.
                        if ((bitmap != null) && ((!mPrintActivityRespectsOrientation) || (mOrientation == 0))) {
                            android.print.PrintAttributes.MediaSize mediaSize;
                            synchronized(this) {
                                mediaSize = mAttributes.getMediaSize();
                            }
                            if (mediaSize != null) {
                                if (mediaSize.isPortrait() != android.support.v4.print.PrintHelperKitkat.isPortrait(bitmap)) {
                                    android.graphics.Matrix rotation = new android.graphics.Matrix();
                                    rotation.postRotate(90);
                                    bitmap = android.graphics.Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotation, true);
                                }
                            }
                        }
                        mBitmap = bitmap;
                        if (bitmap != null) {
                            android.print.PrintDocumentInfo info = new android.print.PrintDocumentInfo.Builder(jobName).setContentType(android.print.PrintDocumentInfo.CONTENT_TYPE_PHOTO).setPageCount(1).build();
                            boolean changed = !newPrintAttributes.equals(oldPrintAttributes);
                            layoutResultCallback.onLayoutFinished(info, changed);
                        } else {
                            layoutResultCallback.onLayoutFailed(null);
                        }
                        mLoadBitmap = null;
                    }

                    @java.lang.Override
                    protected void onCancelled(android.graphics.Bitmap result) {
                        // Task was cancelled, report that.
                        layoutResultCallback.onLayoutCancelled();
                        mLoadBitmap = null;
                    }
                }.execute();
            }

            private void cancelLoad() {
                synchronized(mLock) {
                    // prevent race with set null below
                    if (mDecodeOptions != null) {
                        mDecodeOptions.requestCancelDecode();
                        mDecodeOptions = null;
                    }
                }
            }

            @java.lang.Override
            public void onFinish() {
                super.onFinish();
                cancelLoad();
                if (mLoadBitmap != null) {
                    mLoadBitmap.cancel(true);
                }
                if (callback != null) {
                    callback.onFinish();
                }
                if (mBitmap != null) {
                    mBitmap.recycle();
                    mBitmap = null;
                }
            }

            @java.lang.Override
            public void onWrite(android.print.PageRange[] pageRanges, android.os.ParcelFileDescriptor fileDescriptor, android.os.CancellationSignal cancellationSignal, android.print.PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
                writeBitmap(mAttributes, fittingMode, mBitmap, fileDescriptor, cancellationSignal, writeResultCallback);
            }
        };
        android.print.PrintManager printManager = ((android.print.PrintManager) (mContext.getSystemService(android.content.Context.PRINT_SERVICE)));
        android.print.PrintAttributes.Builder builder = new android.print.PrintAttributes.Builder();
        builder.setColorMode(mColorMode);
        if ((mOrientation == android.support.v4.print.PrintHelperKitkat.ORIENTATION_LANDSCAPE) || (mOrientation == 0)) {
            builder.setMediaSize(android.print.PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
        } else
            if (mOrientation == android.support.v4.print.PrintHelperKitkat.ORIENTATION_PORTRAIT) {
                builder.setMediaSize(android.print.PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
            }

        android.print.PrintAttributes attr = builder.build();
        printManager.print(jobName, printDocumentAdapter, attr);
    }

    /**
     * Loads a bitmap while limiting its size
     *
     * @param uri
     * 		location of a valid image
     * @param maxSideLength
     * 		the maximum length of a size
     * @return the Bitmap
     * @throws FileNotFoundException
     * 		if the Uri does not point to an image
     */
    private android.graphics.Bitmap loadConstrainedBitmap(android.net.Uri uri, int maxSideLength) throws java.io.FileNotFoundException {
        if (((maxSideLength <= 0) || (uri == null)) || (mContext == null)) {
            throw new java.lang.IllegalArgumentException("bad argument to getScaledBitmap");
        }
        // Get width and height of stored bitmap
        android.graphics.BitmapFactory.Options opt = new android.graphics.BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        loadBitmap(uri, opt);
        int w = opt.outWidth;
        int h = opt.outHeight;
        // If bitmap cannot be decoded, return null
        if ((w <= 0) || (h <= 0)) {
            return null;
        }
        // Find best downsampling size
        int imageSide = java.lang.Math.max(w, h);
        int sampleSize = 1;
        while (imageSide > maxSideLength) {
            imageSide >>>= 1;
            sampleSize <<= 1;
        } 
        // Make sure sample size is reasonable
        if ((sampleSize <= 0) || (0 >= ((int) (java.lang.Math.min(w, h) / sampleSize)))) {
            return null;
        }
        android.graphics.BitmapFactory.Options decodeOptions = null;
        synchronized(mLock) {
            // prevent race with set null below
            mDecodeOptions = new android.graphics.BitmapFactory.Options();
            mDecodeOptions.inMutable = true;
            mDecodeOptions.inSampleSize = sampleSize;
            decodeOptions = mDecodeOptions;
        }
        try {
            return loadBitmap(uri, decodeOptions);
        } finally {
            synchronized(mLock) {
                mDecodeOptions = null;
            }
        }
    }

    /**
     * Returns the bitmap from the given uri loaded using the given options.
     * Returns null on failure.
     */
    private android.graphics.Bitmap loadBitmap(android.net.Uri uri, android.graphics.BitmapFactory.Options o) throws java.io.FileNotFoundException {
        if ((uri == null) || (mContext == null)) {
            throw new java.lang.IllegalArgumentException("bad argument to loadBitmap");
        }
        java.io.InputStream is = null;
        try {
            is = mContext.getContentResolver().openInputStream(uri);
            return android.graphics.BitmapFactory.decodeStream(is, null, o);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (java.io.IOException t) {
                    android.util.Log.w(android.support.v4.print.PrintHelperKitkat.LOG_TAG, "close fail ", t);
                }
            }
        }
    }

    private android.graphics.Bitmap convertBitmapForColorMode(android.graphics.Bitmap original, int colorMode) {
        if (colorMode != android.support.v4.print.PrintHelperKitkat.COLOR_MODE_MONOCHROME) {
            return original;
        }
        // Create a grayscale bitmap
        android.graphics.Bitmap grayscale = android.graphics.Bitmap.createBitmap(original.getWidth(), original.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Canvas c = new android.graphics.Canvas(grayscale);
        android.graphics.Paint p = new android.graphics.Paint();
        android.graphics.ColorMatrix cm = new android.graphics.ColorMatrix();
        cm.setSaturation(0);
        android.graphics.ColorMatrixColorFilter f = new android.graphics.ColorMatrixColorFilter(cm);
        p.setColorFilter(f);
        c.drawBitmap(original, 0, 0, p);
        c.setBitmap(null);
        return grayscale;
    }
}

