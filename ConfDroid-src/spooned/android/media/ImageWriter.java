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
 * <p>
 * The ImageWriter class allows an application to produce Image data into a
 * {@link android.view.Surface}, and have it be consumed by another component
 * like {@link android.hardware.camera2.CameraDevice CameraDevice}.
 * </p>
 * <p>
 * Several Android API classes can provide input {@link android.view.Surface
 * Surface} objects for ImageWriter to produce data into, including
 * {@link MediaCodec MediaCodec} (encoder),
 * {@link android.hardware.camera2.CameraCaptureSession CameraCaptureSession}
 * (reprocessing input), {@link ImageReader}, etc.
 * </p>
 * <p>
 * The input Image data is encapsulated in {@link Image} objects. To produce
 * Image data into a destination {@link android.view.Surface Surface}, the
 * application can get an input Image via {@link #dequeueInputImage} then write
 * Image data into it. Multiple such {@link Image} objects can be dequeued at
 * the same time and queued back in any order, up to the number specified by the
 * {@code maxImages} constructor parameter.
 * </p>
 * <p>
 * If the application already has an Image from {@link ImageReader}, the
 * application can directly queue this Image into ImageWriter (via
 * {@link #queueInputImage}), potentially with zero buffer copies. For the
 * {@link ImageFormat#PRIVATE PRIVATE} format Images produced by
 * {@link ImageReader}, this is the only way to send Image data to ImageWriter,
 * as the Image data aren't accessible by the application.
 * </p>
 * Once new input Images are queued into an ImageWriter, it's up to the
 * downstream components (e.g. {@link ImageReader} or
 * {@link android.hardware.camera2.CameraDevice}) to consume the Images. If the
 * downstream components cannot consume the Images at least as fast as the
 * ImageWriter production rate, the {@link #dequeueInputImage} call will
 * eventually block and the application will have to drop input frames.
 * </p>
 * <p>
 * If the consumer component that provided the input {@link android.view.Surface Surface}
 * abandons the {@link android.view.Surface Surface}, {@link #queueInputImage queueing}
 * or {@link #dequeueInputImage dequeueing} an {@link Image} will throw an
 * {@link IllegalStateException}.
 * </p>
 */
public class ImageWriter implements java.lang.AutoCloseable {
    private final java.lang.Object mListenerLock = new java.lang.Object();

    private android.media.ImageWriter.OnImageReleasedListener mListener;

    private android.media.ImageWriter.ListenerHandler mListenerHandler;

    private long mNativeContext;

    // Field below is used by native code, do not access or modify.
    private int mWriterFormat;

    private final int mMaxImages;

    // Keep track of the currently dequeued Image. This need to be thread safe as the images
    // could be closed by different threads (e.g., application thread and GC thread).
    private java.util.List<android.media.Image> mDequeuedImages = new java.util.concurrent.CopyOnWriteArrayList<android.media.Image>();

    private int mEstimatedNativeAllocBytes;

    /**
     * <p>
     * Create a new ImageWriter.
     * </p>
     * <p>
     * The {@code maxImages} parameter determines the maximum number of
     * {@link Image} objects that can be be dequeued from the
     * {@code ImageWriter} simultaneously. Requesting more buffers will use up
     * more memory, so it is important to use only the minimum number necessary.
     * </p>
     * <p>
     * The input Image size and format depend on the Surface that is provided by
     * the downstream consumer end-point.
     * </p>
     *
     * @param surface
     * 		The destination Surface this writer produces Image data
     * 		into.
     * @param maxImages
     * 		The maximum number of Images the user will want to
     * 		access simultaneously for producing Image data. This should be
     * 		as small as possible to limit memory use. Once maxImages
     * 		Images are dequeued by the user, one of them has to be queued
     * 		back before a new Image can be dequeued for access via
     * 		{@link #dequeueInputImage()}.
     * @return a new ImageWriter instance.
     */
    public static android.media.ImageWriter newInstance(android.view.Surface surface, int maxImages) {
        return new android.media.ImageWriter(surface, maxImages);
    }

    /**
     *
     *
     * @unknown 
     */
    protected ImageWriter(android.view.Surface surface, int maxImages) {
        if ((surface == null) || (maxImages < 1)) {
            throw new java.lang.IllegalArgumentException((("Illegal input argument: surface " + surface) + ", maxImages: ") + maxImages);
        }
        mMaxImages = maxImages;
        // Note that the underlying BufferQueue is working in synchronous mode
        // to avoid dropping any buffers.
        mNativeContext = nativeInit(new java.lang.ref.WeakReference<android.media.ImageWriter>(this), surface, maxImages);
        // Estimate the native buffer allocation size and register it so it gets accounted for
        // during GC. Note that this doesn't include the buffers required by the buffer queue
        // itself and the buffers requested by the producer.
        // Only include memory for 1 buffer, since actually accounting for the memory used is
        // complex, and 1 buffer is enough for the VM to treat the ImageWriter as being of some
        // size.
        android.util.Size surfSize = android.hardware.camera2.utils.SurfaceUtils.getSurfaceSize(surface);
        int format = android.hardware.camera2.utils.SurfaceUtils.getSurfaceFormat(surface);
        mEstimatedNativeAllocBytes = /* buffer count */
        android.media.ImageUtils.getEstimatedNativeAllocBytes(surfSize.getWidth(), surfSize.getHeight(), format, 1);
        dalvik.system.VMRuntime.getRuntime().registerNativeAllocation(mEstimatedNativeAllocBytes);
    }

    /**
     * <p>
     * Maximum number of Images that can be dequeued from the ImageWriter
     * simultaneously (for example, with {@link #dequeueInputImage()}).
     * </p>
     * <p>
     * An Image is considered dequeued after it's returned by
     * {@link #dequeueInputImage()} from ImageWriter, and until the Image is
     * sent back to ImageWriter via {@link #queueInputImage}, or
     * {@link Image#close()}.
     * </p>
     * <p>
     * Attempting to dequeue more than {@code maxImages} concurrently will
     * result in the {@link #dequeueInputImage()} function throwing an
     * {@link IllegalStateException}.
     * </p>
     *
     * @return Maximum number of Images that can be dequeued from this
    ImageWriter.
     * @see #dequeueInputImage
     * @see #queueInputImage
     * @see Image#close
     */
    public int getMaxImages() {
        return mMaxImages;
    }

    /**
     * <p>
     * Dequeue the next available input Image for the application to produce
     * data into.
     * </p>
     * <p>
     * This method requests a new input Image from ImageWriter. The application
     * owns this Image after this call. Once the application fills the Image
     * data, it is expected to return this Image back to ImageWriter for
     * downstream consumer components (e.g.
     * {@link android.hardware.camera2.CameraDevice}) to consume. The Image can
     * be returned to ImageWriter via {@link #queueInputImage} or
     * {@link Image#close()}.
     * </p>
     * <p>
     * This call will block if all available input images have been queued by
     * the application and the downstream consumer has not yet consumed any.
     * When an Image is consumed by the downstream consumer and released, an
     * {@link OnImageReleasedListener#onImageReleased} callback will be fired,
     * which indicates that there is one input Image available. For non-
     * {@link ImageFormat#PRIVATE PRIVATE} formats (
     * {@link ImageWriter#getFormat()} != {@link ImageFormat#PRIVATE}), it is
     * recommended to dequeue the next Image only after this callback is fired,
     * in the steady state.
     * </p>
     * <p>
     * If the format of ImageWriter is {@link ImageFormat#PRIVATE PRIVATE} (
     * {@link ImageWriter#getFormat()} == {@link ImageFormat#PRIVATE}), the
     * image buffer is inaccessible to the application, and calling this method
     * will result in an {@link IllegalStateException}. Instead, the application
     * should acquire images from some other component (e.g. an
     * {@link ImageReader}), and queue them directly to this ImageWriter via the
     * {@link ImageWriter#queueInputImage queueInputImage()} method.
     * </p>
     *
     * @return The next available input Image from this ImageWriter.
     * @throws IllegalStateException
     * 		if {@code maxImages} Images are currently
     * 		dequeued, or the ImageWriter format is
     * 		{@link ImageFormat#PRIVATE PRIVATE}, or the input
     * 		{@link android.view.Surface Surface} has been abandoned by the
     * 		consumer component that provided the {@link android.view.Surface Surface}.
     * @see #queueInputImage
     * @see Image#close
     */
    public android.media.Image dequeueInputImage() {
        if (mWriterFormat == android.graphics.ImageFormat.PRIVATE) {
            throw new java.lang.IllegalStateException("PRIVATE format ImageWriter doesn't support this operation since the images are" + " inaccessible to the application!");
        }
        if (mDequeuedImages.size() >= mMaxImages) {
            throw new java.lang.IllegalStateException("Already dequeued max number of Images " + mMaxImages);
        }
        android.media.ImageWriter.WriterSurfaceImage newImage = new android.media.ImageWriter.WriterSurfaceImage(this);
        nativeDequeueInputImage(mNativeContext, newImage);
        mDequeuedImages.add(newImage);
        newImage.mIsImageValid = true;
        return newImage;
    }

    /**
     * <p>
     * Queue an input {@link Image} back to ImageWriter for the downstream
     * consumer to access.
     * </p>
     * <p>
     * The input {@link Image} could be from ImageReader (acquired via
     * {@link ImageReader#acquireNextImage} or
     * {@link ImageReader#acquireLatestImage}), or from this ImageWriter
     * (acquired via {@link #dequeueInputImage}). In the former case, the Image
     * data will be moved to this ImageWriter. Note that the Image properties
     * (size, format, strides, etc.) must be the same as the properties of the
     * images dequeued from this ImageWriter, or this method will throw an
     * {@link IllegalArgumentException}. In the latter case, the application has
     * filled the input image with data. This method then passes the filled
     * buffer to the downstream consumer. In both cases, it's up to the caller
     * to ensure that the Image timestamp (in nanoseconds) is correctly set, as
     * the downstream component may want to use it to indicate the Image data
     * capture time.
     * </p>
     * <p>
     * After this method is called and the downstream consumer consumes and
     * releases the Image, an {@link OnImageReleasedListener#onImageReleased}
     * callback will fire. The application can use this callback to avoid
     * sending Images faster than the downstream consumer processing rate in
     * steady state.
     * </p>
     * <p>
     * Passing in an Image from some other component (e.g. an
     * {@link ImageReader}) requires a free input Image from this ImageWriter as
     * the destination. In this case, this call will block, as
     * {@link #dequeueInputImage} does, if there are no free Images available.
     * To avoid blocking, the application should ensure that there is at least
     * one free Image available in this ImageWriter before calling this method.
     * </p>
     * <p>
     * After this call, the input Image is no longer valid for further access,
     * as if the Image is {@link Image#close closed}. Attempting to access the
     * {@link ByteBuffer ByteBuffers} returned by an earlier
     * {@link Image.Plane#getBuffer Plane#getBuffer} call will result in an
     * {@link IllegalStateException}.
     * </p>
     *
     * @param image
     * 		The Image to be queued back to ImageWriter for future
     * 		consumption.
     * @throws IllegalStateException
     * 		if the image was already queued previously,
     * 		or the image was aborted previously, or the input
     * 		{@link android.view.Surface Surface} has been abandoned by the
     * 		consumer component that provided the
     * 		{@link android.view.Surface Surface}.
     * @see #dequeueInputImage()
     */
    public void queueInputImage(android.media.Image image) {
        if (image == null) {
            throw new java.lang.IllegalArgumentException("image shouldn't be null");
        }
        boolean ownedByMe = isImageOwnedByMe(image);
        if (ownedByMe && (!((android.media.ImageWriter.WriterSurfaceImage) (image)).mIsImageValid)) {
            throw new java.lang.IllegalStateException("Image from ImageWriter is invalid");
        }
        // For images from other components, need to detach first, then attach.
        if (!ownedByMe) {
            if (!(image.getOwner() instanceof android.media.ImageReader)) {
                throw new java.lang.IllegalArgumentException("Only images from ImageReader can be queued to" + " ImageWriter, other image source is not supported yet!");
            }
            android.media.ImageReader prevOwner = ((android.media.ImageReader) (image.getOwner()));
            // Only do the image attach for PRIVATE format images for now. Do the image
            // copy for other formats. TODO: use attach for other formats to
            // improve the performance, and fall back to copy when attach/detach
            // fails. Right now, detach is guaranteed to fail as the buffer is
            // locked when ImageReader#acquireNextImage is called. See bug 19962027.
            if (image.getFormat() == android.graphics.ImageFormat.PRIVATE) {
                prevOwner.detachImage(image);
                attachAndQueueInputImage(image);
                // This clears the native reference held by the original owner.
                // When this Image is detached later by this ImageWriter, the
                // native memory won't be leaked.
                image.close();
                return;
            } else {
                android.media.Image inputImage = dequeueInputImage();
                inputImage.setTimestamp(image.getTimestamp());
                inputImage.setCropRect(image.getCropRect());
                android.media.ImageUtils.imageCopy(image, inputImage);
                image.close();
                image = inputImage;
                ownedByMe = true;
            }
        }
        android.graphics.Rect crop = image.getCropRect();
        nativeQueueInputImage(mNativeContext, image, image.getTimestamp(), crop.left, crop.top, crop.right, crop.bottom);
        /**
         * Only remove and cleanup the Images that are owned by this
         * ImageWriter. Images detached from other owners are only temporarily
         * owned by this ImageWriter and will be detached immediately after they
         * are released by downstream consumers, so there is no need to keep
         * track of them in mDequeuedImages.
         */
        if (ownedByMe) {
            mDequeuedImages.remove(image);
            // Do not call close here, as close is essentially cancel image.
            android.media.ImageWriter.WriterSurfaceImage wi = ((android.media.ImageWriter.WriterSurfaceImage) (image));
            wi.clearSurfacePlanes();
            wi.mIsImageValid = false;
        }
    }

    /**
     * Get the ImageWriter format.
     * <p>
     * This format may be different than the Image format returned by
     * {@link Image#getFormat()}. However, if the ImageWriter format is
     * {@link ImageFormat#PRIVATE PRIVATE}, calling {@link #dequeueInputImage()}
     * will result in an {@link IllegalStateException}.
     * </p>
     *
     * @return The ImageWriter format.
     */
    public int getFormat() {
        return mWriterFormat;
    }

    /**
     * ImageWriter callback interface, used to to asynchronously notify the
     * application of various ImageWriter events.
     */
    public interface OnImageReleasedListener {
        /**
         * <p>
         * Callback that is called when an input Image is released back to
         * ImageWriter after the data consumption.
         * </p>
         * <p>
         * The client can use this callback to be notified that an input Image
         * has been consumed and released by the downstream consumer. More
         * specifically, this callback will be fired for below cases:
         * <li>The application dequeues an input Image via the
         * {@link ImageWriter#dequeueInputImage dequeueInputImage()} method,
         * uses it, and then queues it back to this ImageWriter via the
         * {@link ImageWriter#queueInputImage queueInputImage()} method. After
         * the downstream consumer uses and releases this image to this
         * ImageWriter, this callback will be fired. This image will be
         * available to be dequeued after this callback.</li>
         * <li>The application obtains an Image from some other component (e.g.
         * an {@link ImageReader}), uses it, and then queues it to this
         * ImageWriter via {@link ImageWriter#queueInputImage queueInputImage()}.
         * After the downstream consumer uses and releases this image to this
         * ImageWriter, this callback will be fired.</li>
         * </p>
         *
         * @param writer
         * 		the ImageWriter the callback is associated with.
         * @see ImageWriter
         * @see Image
         */
        void onImageReleased(android.media.ImageWriter writer);
    }

    /**
     * Register a listener to be invoked when an input Image is returned to the
     * ImageWriter.
     *
     * @param listener
     * 		The listener that will be run.
     * @param handler
     * 		The handler on which the listener should be invoked, or
     * 		null if the listener should be invoked on the calling thread's
     * 		looper.
     * @throws IllegalArgumentException
     * 		If no handler specified and the calling
     * 		thread has no looper.
     */
    public void setOnImageReleasedListener(android.media.ImageWriter.OnImageReleasedListener listener, android.os.Handler handler) {
        synchronized(mListenerLock) {
            if (listener != null) {
                android.os.Looper looper = (handler != null) ? handler.getLooper() : android.os.Looper.myLooper();
                if (looper == null) {
                    throw new java.lang.IllegalArgumentException("handler is null but the current thread is not a looper");
                }
                if ((mListenerHandler == null) || (mListenerHandler.getLooper() != looper)) {
                    mListenerHandler = new android.media.ImageWriter.ListenerHandler(looper);
                }
                mListener = listener;
            } else {
                mListener = null;
                mListenerHandler = null;
            }
        }
    }

    /**
     * Free up all the resources associated with this ImageWriter.
     * <p>
     * After calling this method, this ImageWriter cannot be used. Calling any
     * methods on this ImageWriter and Images previously provided by
     * {@link #dequeueInputImage()} will result in an
     * {@link IllegalStateException}, and attempting to write into
     * {@link ByteBuffer ByteBuffers} returned by an earlier
     * {@link Image.Plane#getBuffer Plane#getBuffer} call will have undefined
     * behavior.
     * </p>
     */
    @java.lang.Override
    public void close() {
        setOnImageReleasedListener(null, null);
        for (android.media.Image image : mDequeuedImages) {
            image.close();
        }
        mDequeuedImages.clear();
        nativeClose(mNativeContext);
        mNativeContext = 0;
        if (mEstimatedNativeAllocBytes > 0) {
            dalvik.system.VMRuntime.getRuntime().registerNativeFree(mEstimatedNativeAllocBytes);
            mEstimatedNativeAllocBytes = 0;
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    /**
     * <p>
     * Attach and queue input Image to this ImageWriter.
     * </p>
     * <p>
     * When the format of an Image is {@link ImageFormat#PRIVATE PRIVATE}, or
     * the source Image is so large that copying its data is too expensive, this
     * method can be used to migrate the source Image into ImageWriter without a
     * data copy, and then queue it to this ImageWriter. The source Image must
     * be detached from its previous owner already, or this call will throw an
     * {@link IllegalStateException}.
     * </p>
     * <p>
     * After this call, the ImageWriter takes ownership of this Image. This
     * ownership will automatically be removed from this writer after the
     * consumer releases this Image, that is, after
     * {@link OnImageReleasedListener#onImageReleased}. The caller is responsible for
     * closing this Image through {@link Image#close()} to free up the resources
     * held by this Image.
     * </p>
     *
     * @param image
     * 		The source Image to be attached and queued into this
     * 		ImageWriter for downstream consumer to use.
     * @throws IllegalStateException
     * 		if the Image is not detached from its
     * 		previous owner, or the Image is already attached to this
     * 		ImageWriter, or the source Image is invalid.
     */
    private void attachAndQueueInputImage(android.media.Image image) {
        if (image == null) {
            throw new java.lang.IllegalArgumentException("image shouldn't be null");
        }
        if (isImageOwnedByMe(image)) {
            throw new java.lang.IllegalArgumentException("Can not attach an image that is owned ImageWriter already");
        }
        /**
         * Throw ISE if the image is not attachable, which means that it is
         * either owned by other entity now, or completely non-attachable (some
         * stand-alone images are not backed by native gralloc buffer, thus not
         * attachable).
         */
        if (!image.isAttachable()) {
            throw new java.lang.IllegalStateException("Image was not detached from last owner, or image " + " is not detachable");
        }
        // TODO: what if attach failed, throw RTE or detach a slot then attach?
        // need do some cleanup to make sure no orphaned
        // buffer caused leak.
        android.graphics.Rect crop = image.getCropRect();
        nativeAttachAndQueueImage(mNativeContext, image.getNativeContext(), image.getFormat(), image.getTimestamp(), crop.left, crop.top, crop.right, crop.bottom);
    }

    /**
     * This custom handler runs asynchronously so callbacks don't get queued
     * behind UI messages.
     */
    private final class ListenerHandler extends android.os.Handler {
        public ListenerHandler(android.os.Looper looper) {
            /* async */
            super(looper, null, true);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.media.ImageWriter.OnImageReleasedListener listener;
            synchronized(mListenerLock) {
                listener = mListener;
            }
            if (listener != null) {
                listener.onImageReleased(android.media.ImageWriter.this);
            }
        }
    }

    /**
     * Called from Native code when an Event happens. This may be called from an
     * arbitrary Binder thread, so access to the ImageWriter must be
     * synchronized appropriately.
     */
    private static void postEventFromNative(java.lang.Object selfRef) {
        @java.lang.SuppressWarnings("unchecked")
        java.lang.ref.WeakReference<android.media.ImageWriter> weakSelf = ((java.lang.ref.WeakReference<android.media.ImageWriter>) (selfRef));
        final android.media.ImageWriter iw = weakSelf.get();
        if (iw == null) {
            return;
        }
        final android.os.Handler handler;
        synchronized(iw.mListenerLock) {
            handler = iw.mListenerHandler;
        }
        if (handler != null) {
            handler.sendEmptyMessage(0);
        }
    }

    /**
     * <p>
     * Abort the Images that were dequeued from this ImageWriter, and return
     * them to this writer for reuse.
     * </p>
     * <p>
     * This method is used for the cases where the application dequeued the
     * Image, may have filled the data, but does not want the downstream
     * component to consume it. The Image will be returned to this ImageWriter
     * for reuse after this call, and the ImageWriter will immediately have an
     * Image available to be dequeued. This aborted Image will be invisible to
     * the downstream consumer, as if nothing happened.
     * </p>
     *
     * @param image
     * 		The Image to be aborted.
     * @see #dequeueInputImage()
     * @see Image#close()
     */
    private void abortImage(android.media.Image image) {
        if (image == null) {
            throw new java.lang.IllegalArgumentException("image shouldn't be null");
        }
        if (!mDequeuedImages.contains(image)) {
            throw new java.lang.IllegalStateException("It is illegal to abort some image that is not" + " dequeued yet");
        }
        android.media.ImageWriter.WriterSurfaceImage wi = ((android.media.ImageWriter.WriterSurfaceImage) (image));
        if (!wi.mIsImageValid) {
            return;
        }
        /**
         * We only need abort Images that are owned and dequeued by ImageWriter.
         * For attached Images, no need to abort, as there are only two cases:
         * attached + queued successfully, and attach failed. Neither of the
         * cases need abort.
         */
        cancelImage(mNativeContext, image);
        mDequeuedImages.remove(image);
        wi.clearSurfacePlanes();
        wi.mIsImageValid = false;
    }

    private boolean isImageOwnedByMe(android.media.Image image) {
        if (!(image instanceof android.media.ImageWriter.WriterSurfaceImage)) {
            return false;
        }
        android.media.ImageWriter.WriterSurfaceImage wi = ((android.media.ImageWriter.WriterSurfaceImage) (image));
        if (wi.getOwner() != this) {
            return false;
        }
        return true;
    }

    private static class WriterSurfaceImage extends android.media.Image {
        private android.media.ImageWriter mOwner;

        // This field is used by native code, do not access or modify.
        private long mNativeBuffer;

        private int mNativeFenceFd = -1;

        private android.media.ImageWriter.WriterSurfaceImage.SurfacePlane[] mPlanes;

        private int mHeight = -1;

        private int mWidth = -1;

        private int mFormat = -1;

        // When this default timestamp is used, timestamp for the input Image
        // will be generated automatically when queueInputBuffer is called.
        private final long DEFAULT_TIMESTAMP = java.lang.Long.MIN_VALUE;

        private long mTimestamp = DEFAULT_TIMESTAMP;

        public WriterSurfaceImage(android.media.ImageWriter writer) {
            mOwner = writer;
        }

        @java.lang.Override
        public int getFormat() {
            throwISEIfImageIsInvalid();
            if (mFormat == (-1)) {
                mFormat = nativeGetFormat();
            }
            return mFormat;
        }

        @java.lang.Override
        public int getWidth() {
            throwISEIfImageIsInvalid();
            if (mWidth == (-1)) {
                mWidth = nativeGetWidth();
            }
            return mWidth;
        }

        @java.lang.Override
        public int getHeight() {
            throwISEIfImageIsInvalid();
            if (mHeight == (-1)) {
                mHeight = nativeGetHeight();
            }
            return mHeight;
        }

        @java.lang.Override
        public long getTimestamp() {
            throwISEIfImageIsInvalid();
            return mTimestamp;
        }

        @java.lang.Override
        public void setTimestamp(long timestamp) {
            throwISEIfImageIsInvalid();
            mTimestamp = timestamp;
        }

        @java.lang.Override
        public android.media.Image.Plane[] getPlanes() {
            throwISEIfImageIsInvalid();
            if (mPlanes == null) {
                int numPlanes = android.media.ImageUtils.getNumPlanesForFormat(getFormat());
                mPlanes = nativeCreatePlanes(numPlanes, getOwner().getFormat());
            }
            return mPlanes.clone();
        }

        @java.lang.Override
        boolean isAttachable() {
            throwISEIfImageIsInvalid();
            // Don't allow Image to be detached from ImageWriter for now, as no
            // detach API is exposed.
            return false;
        }

        @java.lang.Override
        android.media.ImageWriter getOwner() {
            throwISEIfImageIsInvalid();
            return mOwner;
        }

        @java.lang.Override
        long getNativeContext() {
            throwISEIfImageIsInvalid();
            return mNativeBuffer;
        }

        @java.lang.Override
        public void close() {
            if (mIsImageValid) {
                getOwner().abortImage(this);
            }
        }

        @java.lang.Override
        protected final void finalize() throws java.lang.Throwable {
            try {
                close();
            } finally {
                super.finalize();
            }
        }

        private void clearSurfacePlanes() {
            if (mIsImageValid && (mPlanes != null)) {
                for (int i = 0; i < mPlanes.length; i++) {
                    if (mPlanes[i] != null) {
                        mPlanes[i].clearBuffer();
                        mPlanes[i] = null;
                    }
                }
            }
        }

        private class SurfacePlane extends android.media.Image.Plane {
            private java.nio.ByteBuffer mBuffer;

            private final int mPixelStride;

            private final int mRowStride;

            // SurfacePlane instance is created by native code when SurfaceImage#getPlanes() is
            // called
            private SurfacePlane(int rowStride, int pixelStride, java.nio.ByteBuffer buffer) {
                mRowStride = rowStride;
                mPixelStride = pixelStride;
                mBuffer = buffer;
                /**
                 * Set the byteBuffer order according to host endianness (native
                 * order), otherwise, the byteBuffer order defaults to
                 * ByteOrder.BIG_ENDIAN.
                 */
                mBuffer.order(java.nio.ByteOrder.nativeOrder());
            }

            @java.lang.Override
            public int getRowStride() {
                throwISEIfImageIsInvalid();
                return mRowStride;
            }

            @java.lang.Override
            public int getPixelStride() {
                throwISEIfImageIsInvalid();
                return mPixelStride;
            }

            @java.lang.Override
            public java.nio.ByteBuffer getBuffer() {
                throwISEIfImageIsInvalid();
                return mBuffer;
            }

            private void clearBuffer() {
                // Need null check first, as the getBuffer() may not be called
                // before an Image is closed.
                if (mBuffer == null) {
                    return;
                }
                if (mBuffer.isDirect()) {
                    java.nio.NioUtils.freeDirectBuffer(mBuffer);
                }
                mBuffer = null;
            }
        }

        // Create the SurfacePlane object and fill the information
        private native synchronized android.media.ImageWriter.WriterSurfaceImage.SurfacePlane[] nativeCreatePlanes(int numPlanes, int writerFmt);

        private native synchronized int nativeGetWidth();

        private native synchronized int nativeGetHeight();

        private native synchronized int nativeGetFormat();
    }

    // Native implemented ImageWriter methods.
    private native synchronized long nativeInit(java.lang.Object weakSelf, android.view.Surface surface, int maxImgs);

    private native synchronized void nativeClose(long nativeCtx);

    private native synchronized void nativeDequeueInputImage(long nativeCtx, android.media.Image wi);

    private native synchronized void nativeQueueInputImage(long nativeCtx, android.media.Image image, long timestampNs, int left, int top, int right, int bottom);

    private native synchronized int nativeAttachAndQueueImage(long nativeCtx, long imageNativeBuffer, int imageFormat, long timestampNs, int left, int top, int right, int bottom);

    private native synchronized void cancelImage(long nativeCtx, android.media.Image image);

    /**
     * We use a class initializer to allow the native code to cache some field
     * offsets.
     */
    private static native void nativeClassInit();

    static {
        java.lang.System.loadLibrary("media_jni");
        android.media.ImageWriter.nativeClassInit();
    }
}

