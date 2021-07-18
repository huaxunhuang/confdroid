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
package android.graphics.pdf;


/**
 * Class for editing PDF files.
 *
 * @unknown 
 */
public final class PdfEditor {
    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

    private long mNativeDocument;

    private int mPageCount;

    private android.os.ParcelFileDescriptor mInput;

    /**
     * Creates a new instance.
     * <p>
     * <strong>Note:</strong> The provided file descriptor must be <strong>seekable</strong>,
     * i.e. its data being randomly accessed, e.g. pointing to a file. After finishing
     * with this class you must call {@link #close()}.
     * </p>
     * <p>
     * <strong>Note:</strong> This class takes ownership of the passed in file descriptor
     * and is responsible for closing it when the editor is closed.
     * </p>
     *
     * @param input
     * 		Seekable file descriptor to read from.
     * @throws java.io.IOException
     * 		If an error occurs while reading the file.
     * @throws java.lang.SecurityException
     * 		If the file requires a password or
     * 		the security scheme is not supported.
     * @see #close()
     */
    public PdfEditor(@android.annotation.NonNull
    android.os.ParcelFileDescriptor input) throws java.io.IOException {
        if (input == null) {
            throw new java.lang.NullPointerException("input cannot be null");
        }
        final long size;
        try {
            android.system.Os.lseek(input.getFileDescriptor(), 0, OsConstants.SEEK_SET);
            size = android.system.Os.fstat(input.getFileDescriptor()).st_size;
        } catch (android.system.ErrnoException ee) {
            throw new java.lang.IllegalArgumentException("file descriptor not seekable");
        }
        mInput = input;
        synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
            mNativeDocument = android.graphics.pdf.PdfEditor.nativeOpen(mInput.getFd(), size);
            try {
                mPageCount = android.graphics.pdf.PdfEditor.nativeGetPageCount(mNativeDocument);
            } catch (java.lang.Throwable t) {
                android.graphics.pdf.PdfEditor.nativeClose(mNativeDocument);
                mNativeDocument = 0;
                throw t;
            }
        }
        mCloseGuard.open("close");
    }

    /**
     * Gets the number of pages in the document.
     *
     * @return The page count.
     */
    public int getPageCount() {
        throwIfClosed();
        return mPageCount;
    }

    /**
     * Removes the page with a given index.
     *
     * @param pageIndex
     * 		The page to remove.
     */
    public void removePage(int pageIndex) {
        throwIfClosed();
        throwIfPageNotInDocument(pageIndex);
        synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
            mPageCount = android.graphics.pdf.PdfEditor.nativeRemovePage(mNativeDocument, pageIndex);
        }
    }

    /**
     * Sets a transformation and clip for a given page. The transformation matrix if
     * non-null must be affine as per {@link android.graphics.Matrix#isAffine()}. If
     * the clip is null, then no clipping is performed.
     *
     * @param pageIndex
     * 		The page whose transform to set.
     * @param transform
     * 		The transformation to apply.
     * @param clip
     * 		The clip to apply.
     */
    public void setTransformAndClip(int pageIndex, @android.annotation.Nullable
    android.graphics.Matrix transform, @android.annotation.Nullable
    android.graphics.Rect clip) {
        throwIfClosed();
        throwIfPageNotInDocument(pageIndex);
        throwIfNotNullAndNotAfine(transform);
        if (transform == null) {
            transform = android.graphics.Matrix.IDENTITY_MATRIX;
        }
        if (clip == null) {
            android.graphics.Point size = new android.graphics.Point();
            getPageSize(pageIndex, size);
            synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
                android.graphics.pdf.PdfEditor.nativeSetTransformAndClip(mNativeDocument, pageIndex, transform.native_instance, 0, 0, size.x, size.y);
            }
        } else {
            synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
                android.graphics.pdf.PdfEditor.nativeSetTransformAndClip(mNativeDocument, pageIndex, transform.native_instance, clip.left, clip.top, clip.right, clip.bottom);
            }
        }
    }

    /**
     * Gets the size of a given page in mils (1/72").
     *
     * @param pageIndex
     * 		The page index.
     * @param outSize
     * 		The size output.
     */
    public void getPageSize(int pageIndex, @android.annotation.NonNull
    android.graphics.Point outSize) {
        throwIfClosed();
        throwIfOutSizeNull(outSize);
        throwIfPageNotInDocument(pageIndex);
        synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
            android.graphics.pdf.PdfEditor.nativeGetPageSize(mNativeDocument, pageIndex, outSize);
        }
    }

    /**
     * Gets the media box of a given page in mils (1/72").
     *
     * @param pageIndex
     * 		The page index.
     * @param outMediaBox
     * 		The media box output.
     */
    public boolean getPageMediaBox(int pageIndex, @android.annotation.NonNull
    android.graphics.Rect outMediaBox) {
        throwIfClosed();
        throwIfOutMediaBoxNull(outMediaBox);
        throwIfPageNotInDocument(pageIndex);
        synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
            return android.graphics.pdf.PdfEditor.nativeGetPageMediaBox(mNativeDocument, pageIndex, outMediaBox);
        }
    }

    /**
     * Sets the media box of a given page in mils (1/72").
     *
     * @param pageIndex
     * 		The page index.
     * @param mediaBox
     * 		The media box.
     */
    public void setPageMediaBox(int pageIndex, @android.annotation.NonNull
    android.graphics.Rect mediaBox) {
        throwIfClosed();
        throwIfMediaBoxNull(mediaBox);
        throwIfPageNotInDocument(pageIndex);
        synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
            android.graphics.pdf.PdfEditor.nativeSetPageMediaBox(mNativeDocument, pageIndex, mediaBox);
        }
    }

    /**
     * Gets the crop box of a given page in mils (1/72").
     *
     * @param pageIndex
     * 		The page index.
     * @param outCropBox
     * 		The crop box output.
     */
    public boolean getPageCropBox(int pageIndex, @android.annotation.NonNull
    android.graphics.Rect outCropBox) {
        throwIfClosed();
        throwIfOutCropBoxNull(outCropBox);
        throwIfPageNotInDocument(pageIndex);
        synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
            return android.graphics.pdf.PdfEditor.nativeGetPageCropBox(mNativeDocument, pageIndex, outCropBox);
        }
    }

    /**
     * Sets the crop box of a given page in mils (1/72").
     *
     * @param pageIndex
     * 		The page index.
     * @param cropBox
     * 		The crop box.
     */
    public void setPageCropBox(int pageIndex, @android.annotation.NonNull
    android.graphics.Rect cropBox) {
        throwIfClosed();
        throwIfCropBoxNull(cropBox);
        throwIfPageNotInDocument(pageIndex);
        synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
            android.graphics.pdf.PdfEditor.nativeSetPageCropBox(mNativeDocument, pageIndex, cropBox);
        }
    }

    /**
     * Gets whether the document prefers to be scaled for printing.
     *
     * @return Whether to scale the document.
     */
    public boolean shouldScaleForPrinting() {
        throwIfClosed();
        synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
            return android.graphics.pdf.PdfEditor.nativeScaleForPrinting(mNativeDocument);
        }
    }

    /**
     * Writes the PDF file to the provided destination.
     * <p>
     * <strong>Note:</strong> This method takes ownership of the passed in file
     * descriptor and is responsible for closing it when writing completes.
     * </p>
     *
     * @param output
     * 		The destination.
     */
    public void write(android.os.ParcelFileDescriptor output) throws java.io.IOException {
        try {
            throwIfClosed();
            synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
                android.graphics.pdf.PdfEditor.nativeWrite(mNativeDocument, output.getFd());
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(output);
        }
    }

    /**
     * Closes this editor. You should not use this instance
     * after this method is called.
     */
    public void close() {
        throwIfClosed();
        doClose();
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            if (mCloseGuard != null) {
                mCloseGuard.warnIfOpen();
            }
            doClose();
        } finally {
            super.finalize();
        }
    }

    private void doClose() {
        if (mNativeDocument != 0) {
            synchronized(android.graphics.pdf.PdfRenderer.sPdfiumLock) {
                android.graphics.pdf.PdfEditor.nativeClose(mNativeDocument);
            }
            mNativeDocument = 0;
        }
        if (mInput != null) {
            libcore.io.IoUtils.closeQuietly(mInput);
            mInput = null;
        }
        mCloseGuard.close();
    }

    private void throwIfClosed() {
        if (mInput == null) {
            throw new java.lang.IllegalStateException("Already closed");
        }
    }

    private void throwIfPageNotInDocument(int pageIndex) {
        if ((pageIndex < 0) || (pageIndex >= mPageCount)) {
            throw new java.lang.IllegalArgumentException("Invalid page index");
        }
    }

    private void throwIfNotNullAndNotAfine(android.graphics.Matrix matrix) {
        if ((matrix != null) && (!matrix.isAffine())) {
            throw new java.lang.IllegalStateException("Matrix must be afine");
        }
    }

    private void throwIfOutSizeNull(android.graphics.Point outSize) {
        if (outSize == null) {
            throw new java.lang.NullPointerException("outSize cannot be null");
        }
    }

    private void throwIfOutMediaBoxNull(android.graphics.Rect outMediaBox) {
        if (outMediaBox == null) {
            throw new java.lang.NullPointerException("outMediaBox cannot be null");
        }
    }

    private void throwIfMediaBoxNull(android.graphics.Rect mediaBox) {
        if (mediaBox == null) {
            throw new java.lang.NullPointerException("mediaBox cannot be null");
        }
    }

    private void throwIfOutCropBoxNull(android.graphics.Rect outCropBox) {
        if (outCropBox == null) {
            throw new java.lang.NullPointerException("outCropBox cannot be null");
        }
    }

    private void throwIfCropBoxNull(android.graphics.Rect cropBox) {
        if (cropBox == null) {
            throw new java.lang.NullPointerException("cropBox cannot be null");
        }
    }

    private static native long nativeOpen(int fd, long size);

    private static native void nativeClose(long documentPtr);

    private static native int nativeGetPageCount(long documentPtr);

    private static native int nativeRemovePage(long documentPtr, int pageIndex);

    private static native void nativeWrite(long documentPtr, int fd);

    private static native void nativeSetTransformAndClip(long documentPtr, int pageIndex, long transformPtr, int clipLeft, int clipTop, int clipRight, int clipBottom);

    private static native void nativeGetPageSize(long documentPtr, int pageIndex, android.graphics.Point outSize);

    private static native boolean nativeGetPageMediaBox(long documentPtr, int pageIndex, android.graphics.Rect outMediaBox);

    private static native void nativeSetPageMediaBox(long documentPtr, int pageIndex, android.graphics.Rect mediaBox);

    private static native boolean nativeGetPageCropBox(long documentPtr, int pageIndex, android.graphics.Rect outMediaBox);

    private static native void nativeSetPageCropBox(long documentPtr, int pageIndex, android.graphics.Rect mediaBox);

    private static native boolean nativeScaleForPrinting(long documentPtr);
}

