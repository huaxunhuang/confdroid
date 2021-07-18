/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.graphics;


/**
 * Class for decoding images as {@link Bitmap}s or {@link Drawable}s.
 */
public final class ImageDecoder implements java.lang.AutoCloseable {
    /**
     * Source of the encoded image data.
     */
    public static abstract class Source {
        private Source() {
        }

        /* @hide */
        @android.annotation.Nullable
        android.content.res.Resources getResources() {
            return null;
        }

        /* @hide */
        int getDensity() {
            return android.graphics.Bitmap.DENSITY_NONE;
        }

        /* @hide */
        int computeDstDensity() {
            android.content.res.Resources res = getResources();
            if (res == null) {
                return android.graphics.Bitmap.getDefaultDensity();
            }
            return res.getDisplayMetrics().densityDpi;
        }

        /* @hide */
        @android.annotation.NonNull
        abstract android.graphics.ImageDecoder createImageDecoder() throws java.io.IOException;
    }

    private static class ByteArraySource extends android.graphics.ImageDecoder.Source {
        ByteArraySource(@android.annotation.NonNull
        byte[] data, int offset, int length) {
            mData = data;
            mOffset = offset;
            mLength = length;
        }

        private final byte[] mData;

        private final int mOffset;

        private final int mLength;

        @java.lang.Override
        public android.graphics.ImageDecoder createImageDecoder() throws java.io.IOException {
            return new android.graphics.ImageDecoder();
        }
    }

    private static class ByteBufferSource extends android.graphics.ImageDecoder.Source {
        ByteBufferSource(@android.annotation.NonNull
        java.nio.ByteBuffer buffer) {
            mBuffer = buffer;
        }

        private final java.nio.ByteBuffer mBuffer;

        @java.lang.Override
        public android.graphics.ImageDecoder createImageDecoder() throws java.io.IOException {
            return new android.graphics.ImageDecoder();
        }
    }

    private static class ContentResolverSource extends android.graphics.ImageDecoder.Source {
        ContentResolverSource(@android.annotation.NonNull
        android.content.ContentResolver resolver, @android.annotation.NonNull
        android.net.Uri uri) {
            mResolver = resolver;
            mUri = uri;
        }

        private final android.content.ContentResolver mResolver;

        private final android.net.Uri mUri;

        @java.lang.Override
        public android.graphics.ImageDecoder createImageDecoder() throws java.io.IOException {
            return new android.graphics.ImageDecoder();
        }
    }

    /**
     * For backwards compatibility, this does *not* close the InputStream.
     */
    private static class InputStreamSource extends android.graphics.ImageDecoder.Source {
        InputStreamSource(android.content.res.Resources res, java.io.InputStream is, int inputDensity) {
            if (is == null) {
                throw new java.lang.IllegalArgumentException("The InputStream cannot be null");
            }
            mResources = res;
            mInputStream = is;
            mInputDensity = (res != null) ? inputDensity : android.graphics.Bitmap.DENSITY_NONE;
        }

        final android.content.res.Resources mResources;

        java.io.InputStream mInputStream;

        final int mInputDensity;

        @java.lang.Override
        public android.content.res.Resources getResources() {
            return mResources;
        }

        @java.lang.Override
        public int getDensity() {
            return mInputDensity;
        }

        @java.lang.Override
        public android.graphics.ImageDecoder createImageDecoder() throws java.io.IOException {
            return new android.graphics.ImageDecoder();
        }
    }

    /**
     * Takes ownership of the AssetInputStream.
     *
     * @unknown 
     */
    public static class AssetInputStreamSource extends android.graphics.ImageDecoder.Source {
        public AssetInputStreamSource(@android.annotation.NonNull
        android.content.res.AssetManager.AssetInputStream ais, @android.annotation.NonNull
        android.content.res.Resources res, @android.annotation.NonNull
        android.util.TypedValue value) {
            mAssetInputStream = ais;
            mResources = res;
            if (value.density == android.util.TypedValue.DENSITY_DEFAULT) {
                mDensity = android.util.DisplayMetrics.DENSITY_DEFAULT;
            } else
                if (value.density != android.util.TypedValue.DENSITY_NONE) {
                    mDensity = value.density;
                } else {
                    mDensity = android.graphics.Bitmap.DENSITY_NONE;
                }

        }

        private android.content.res.AssetManager.AssetInputStream mAssetInputStream;

        private final android.content.res.Resources mResources;

        private final int mDensity;

        @java.lang.Override
        public android.content.res.Resources getResources() {
            return mResources;
        }

        @java.lang.Override
        public int getDensity() {
            return mDensity;
        }

        @java.lang.Override
        public android.graphics.ImageDecoder createImageDecoder() throws java.io.IOException {
            return new android.graphics.ImageDecoder();
        }
    }

    private static class ResourceSource extends android.graphics.ImageDecoder.Source {
        ResourceSource(@android.annotation.NonNull
        android.content.res.Resources res, int resId) {
            mResources = res;
            mResId = resId;
            mResDensity = android.graphics.Bitmap.DENSITY_NONE;
        }

        final android.content.res.Resources mResources;

        final int mResId;

        int mResDensity;

        @java.lang.Override
        public android.content.res.Resources getResources() {
            return mResources;
        }

        @java.lang.Override
        public int getDensity() {
            return mResDensity;
        }

        @java.lang.Override
        public android.graphics.ImageDecoder createImageDecoder() throws java.io.IOException {
            return new android.graphics.ImageDecoder();
        }
    }

    private static class FileSource extends android.graphics.ImageDecoder.Source {
        FileSource(@android.annotation.NonNull
        java.io.File file) {
            mFile = file;
        }

        private final java.io.File mFile;

        @java.lang.Override
        public android.graphics.ImageDecoder createImageDecoder() throws java.io.IOException {
            return new android.graphics.ImageDecoder();
        }
    }

    /**
     * Contains information about the encoded image.
     */
    public static class ImageInfo {
        private android.graphics.ImageDecoder mDecoder;

        private ImageInfo(@android.annotation.NonNull
        android.graphics.ImageDecoder decoder) {
            mDecoder = decoder;
        }

        /**
         * Size of the image, without scaling or cropping.
         */
        @android.annotation.NonNull
        public android.util.Size getSize() {
            return new android.util.Size(0, 0);
        }

        /**
         * The mimeType of the image.
         */
        @android.annotation.NonNull
        public java.lang.String getMimeType() {
            return "";
        }

        /**
         * Whether the image is animated.
         *
         * <p>Calling {@link #decodeDrawable} will return an
         * {@link AnimatedImageDrawable}.</p>
         */
        public boolean isAnimated() {
            return mDecoder.mAnimated;
        }
    }

    /**
     * Thrown if the provided data is incomplete.
     */
    public static class IncompleteException extends java.io.IOException {}

    /**
     * Optional listener supplied to {@link #decodeDrawable} or
     *  {@link #decodeBitmap}.
     */
    public interface OnHeaderDecodedListener {
        /**
         * Called when the header is decoded and the size is known.
         *
         * @param decoder
         * 		allows changing the default settings of the decode.
         * @param info
         * 		Information about the encoded image.
         * @param source
         * 		that created the decoder.
         */
        void onHeaderDecoded(@android.annotation.NonNull
        android.graphics.ImageDecoder decoder, @android.annotation.NonNull
        android.graphics.ImageDecoder.ImageInfo info, @android.annotation.NonNull
        android.graphics.ImageDecoder.Source source);
    }

    /**
     * An Exception was thrown reading the {@link Source}.
     */
    public static final int ERROR_SOURCE_EXCEPTION = 1;

    /**
     * The encoded data was incomplete.
     */
    public static final int ERROR_SOURCE_INCOMPLETE = 2;

    /**
     * The encoded data contained an error.
     */
    public static final int ERROR_SOURCE_ERROR = 3;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Error {}

    /**
     * Optional listener supplied to the ImageDecoder.
     *
     *  Without this listener, errors will throw {@link java.io.IOException}.
     */
    public interface OnPartialImageListener {
        /**
         * Called when there is only a partial image to display.
         *
         *  If decoding is interrupted after having decoded a partial image,
         *  this listener lets the client know that and allows them to
         *  optionally finish the rest of the decode/creation process to create
         *  a partial {@link Drawable}/{@link Bitmap}.
         *
         * @param error
         * 		indicating what interrupted the decode.
         * @param source
         * 		that had the error.
         * @return True to create and return a {@link Drawable}/{@link Bitmap}
        with partial data. False (which is the default) to abort the
        decode and throw {@link java.io.IOException}.
         */
        boolean onPartialImage(@android.graphics.ImageDecoder.Error
        int error, @android.annotation.NonNull
        android.graphics.ImageDecoder.Source source);
    }

    private boolean mAnimated;

    private android.graphics.Rect mOutPaddingRect;

    public ImageDecoder() {
        mAnimated = true;// This is too avoid throwing an exception in AnimatedImageDrawable

    }

    /**
     * Create a new {@link Source} from an asset.
     *
     * @unknown 
     * @param res
     * 		the {@link Resources} object containing the image data.
     * @param resId
     * 		resource ID of the image data.
     * 		// FIXME: Can be an @DrawableRes?
     * @return a new Source object, which can be passed to
    {@link #decodeDrawable} or {@link #decodeBitmap}.
     */
    @android.annotation.NonNull
    public static android.graphics.ImageDecoder.Source createSource(@android.annotation.NonNull
    android.content.res.Resources res, int resId) {
        return new android.graphics.ImageDecoder.ResourceSource(res, resId);
    }

    /**
     * Create a new {@link Source} from a {@link android.net.Uri}.
     *
     * @param cr
     * 		to retrieve from.
     * @param uri
     * 		of the image file.
     * @return a new Source object, which can be passed to
    {@link #decodeDrawable} or {@link #decodeBitmap}.
     */
    @android.annotation.NonNull
    public static android.graphics.ImageDecoder.Source createSource(@android.annotation.NonNull
    android.content.ContentResolver cr, @android.annotation.NonNull
    android.net.Uri uri) {
        return new android.graphics.ImageDecoder.ContentResolverSource(cr, uri);
    }

    /**
     * Create a new {@link Source} from a byte array.
     *
     * @param data
     * 		byte array of compressed image data.
     * @param offset
     * 		offset into data for where the decoder should begin
     * 		parsing.
     * @param length
     * 		number of bytes, beginning at offset, to parse.
     * @throws NullPointerException
     * 		if data is null.
     * @throws ArrayIndexOutOfBoundsException
     * 		if offset and length are
     * 		not within data.
     * @unknown 
     */
    @android.annotation.NonNull
    public static android.graphics.ImageDecoder.Source createSource(@android.annotation.NonNull
    byte[] data, int offset, int length) throws java.lang.ArrayIndexOutOfBoundsException {
        if ((((offset < 0) || (length < 0)) || (offset >= data.length)) || ((offset + length) > data.length)) {
            throw new java.lang.ArrayIndexOutOfBoundsException("invalid offset/length!");
        }
        return new android.graphics.ImageDecoder.ByteArraySource(data, offset, length);
    }

    /**
     * See {@link #createSource(byte[], int, int).
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public static android.graphics.ImageDecoder.Source createSource(@android.annotation.NonNull
    byte[] data) {
        return android.graphics.ImageDecoder.createSource(data, 0, data.length);
    }

    /**
     * Create a new {@link Source} from a {@link java.nio.ByteBuffer}.
     *
     * <p>The returned {@link Source} effectively takes ownership of the
     * {@link java.nio.ByteBuffer}; i.e. no other code should modify it after
     * this call.</p>
     *
     * Decoding will start from {@link java.nio.ByteBuffer#position()}. The
     * position after decoding is undefined.
     */
    @android.annotation.NonNull
    public static android.graphics.ImageDecoder.Source createSource(@android.annotation.NonNull
    java.nio.ByteBuffer buffer) {
        return new android.graphics.ImageDecoder.ByteBufferSource(buffer);
    }

    /**
     * Internal API used to generate bitmaps for use by Drawables (i.e. BitmapDrawable)
     *
     * @unknown 
     */
    public static android.graphics.ImageDecoder.Source createSource(android.content.res.Resources res, java.io.InputStream is) {
        return new android.graphics.ImageDecoder.InputStreamSource(res, is, android.graphics.Bitmap.getDefaultDensity());
    }

    /**
     * Internal API used to generate bitmaps for use by Drawables (i.e. BitmapDrawable)
     *
     * @unknown 
     */
    public static android.graphics.ImageDecoder.Source createSource(android.content.res.Resources res, java.io.InputStream is, int density) {
        return new android.graphics.ImageDecoder.InputStreamSource(res, is, density);
    }

    /**
     * Create a new {@link Source} from a {@link java.io.File}.
     */
    @android.annotation.NonNull
    public static android.graphics.ImageDecoder.Source createSource(@android.annotation.NonNull
    java.io.File file) {
        return new android.graphics.ImageDecoder.FileSource(file);
    }

    /**
     * Return the width and height of a given sample size.
     *
     *  <p>This takes an input that functions like
     *  {@link BitmapFactory.Options#inSampleSize}. It returns a width and
     *  height that can be acheived by sampling the encoded image. Other widths
     *  and heights may be supported, but will require an additional (internal)
     *  scaling step. Such internal scaling is *not* supported with
     *  {@link #setRequireUnpremultiplied} set to {@code true}.</p>
     *
     * @param sampleSize
     * 		Sampling rate of the encoded image.
     * @return {@link android.util.Size} of the width and height after
    sampling.
     */
    @android.annotation.NonNull
    public android.util.Size getSampledSize(int sampleSize) {
        return new android.util.Size(0, 0);
    }

    // Modifiers
    /**
     * Resize the output to have the following size.
     *
     * @param width
     * 		must be greater than 0.
     * @param height
     * 		must be greater than 0.
     */
    public void setResize(int width, int height) {
    }

    /**
     * Resize based on a sample size.
     *
     *  <p>This has the same effect as passing the result of
     *  {@link #getSampledSize} to {@link #setResize(int, int)}.</p>
     *
     * @param sampleSize
     * 		Sampling rate of the encoded image.
     */
    public void setResize(int sampleSize) {
    }

    // These need to stay in sync with ImageDecoder.cpp's Allocator enum.
    /**
     * Use the default allocation for the pixel memory.
     *
     *  Will typically result in a {@link Bitmap.Config#HARDWARE}
     *  allocation, but may be software for small images. In addition, this will
     *  switch to software when HARDWARE is incompatible, e.g.
     *  {@link #setMutable}, {@link #setAsAlphaMask}.
     */
    public static final int ALLOCATOR_DEFAULT = 0;

    /**
     * Use a software allocation for the pixel memory.
     *
     *  Useful for drawing to a software {@link Canvas} or for
     *  accessing the pixels on the final output.
     */
    public static final int ALLOCATOR_SOFTWARE = 1;

    /**
     * Use shared memory for the pixel memory.
     *
     *  Useful for sharing across processes.
     */
    public static final int ALLOCATOR_SHARED_MEMORY = 2;

    /**
     * Require a {@link Bitmap.Config#HARDWARE} {@link Bitmap}.
     *
     *  When this is combined with incompatible options, like
     *  {@link #setMutable} or {@link #setAsAlphaMask}, {@link #decodeDrawable}
     *  / {@link #decodeBitmap} will throw an
     *  {@link java.lang.IllegalStateException}.
     */
    public static final int ALLOCATOR_HARDWARE = 3;

    /**
     *
     *
     * @unknown *
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Allocator {}

    /**
     * Choose the backing for the pixel memory.
     *
     *  This is ignored for animated drawables.
     *
     * @param allocator
     * 		Type of allocator to use.
     */
    public void setAllocator(@android.graphics.ImageDecoder.Allocator
    int allocator) {
    }

    /**
     * Specify whether the {@link Bitmap} should have unpremultiplied pixels.
     *
     *  By default, ImageDecoder will create a {@link Bitmap} with
     *  premultiplied pixels, which is required for drawing with the
     *  {@link android.view.View} system (i.e. to a {@link Canvas}). Calling
     *  this method with a value of {@code true} will result in
     *  {@link #decodeBitmap} returning a {@link Bitmap} with unpremultiplied
     *  pixels. See {@link Bitmap#isPremultiplied}. This is incompatible with
     *  {@link #decodeDrawable}; attempting to decode an unpremultiplied
     *  {@link Drawable} will throw an {@link java.lang.IllegalStateException}.
     */
    public android.graphics.ImageDecoder setRequireUnpremultiplied(boolean requireUnpremultiplied) {
        return this;
    }

    /**
     * Modify the image after decoding and scaling.
     *
     *  <p>This allows adding effects prior to returning a {@link Drawable} or
     *  {@link Bitmap}. For a {@code Drawable} or an immutable {@code Bitmap},
     *  this is the only way to process the image after decoding.</p>
     *
     *  <p>If set on a nine-patch image, the nine-patch data is ignored.</p>
     *
     *  <p>For an animated image, the drawing commands drawn on the
     *  {@link Canvas} will be recorded immediately and then applied to each
     *  frame.</p>
     */
    public void setPostProcessor(@android.annotation.Nullable
    android.graphics.PostProcessor p) {
    }

    /**
     * Set (replace) the {@link OnPartialImageListener} on this object.
     *
     *  Will be called if there is an error in the input. Without one, a
     *  partial {@link Bitmap} will be created.
     */
    public void setOnPartialImageListener(@android.annotation.Nullable
    android.graphics.ImageDecoder.OnPartialImageListener l) {
    }

    /**
     * Crop the output to {@code subset} of the (possibly) scaled image.
     *
     *  <p>{@code subset} must be contained within the size set by
     *  {@link #setResize} or the bounds of the image if setResize was not
     *  called. Otherwise an {@link IllegalStateException} will be thrown by
     *  {@link #decodeDrawable}/{@link #decodeBitmap}.</p>
     *
     *  <p>NOT intended as a replacement for
     *  {@link BitmapRegionDecoder#decodeRegion}. This supports all formats,
     *  but merely crops the output.</p>
     */
    public void setCrop(@android.annotation.Nullable
    android.graphics.Rect subset) {
    }

    /**
     * Set a Rect for retrieving nine patch padding.
     *
     *  If the image is a nine patch, this Rect will be set to the padding
     *  rectangle during decode. Otherwise it will not be modified.
     *
     * @unknown 
     */
    public void setOutPaddingRect(@android.annotation.NonNull
    android.graphics.Rect outPadding) {
        mOutPaddingRect = outPadding;
    }

    /**
     * Specify whether the {@link Bitmap} should be mutable.
     *
     *  <p>By default, a {@link Bitmap} created will be immutable, but that can
     *  be changed with this call.</p>
     *
     *  <p>Mutable Bitmaps are incompatible with {@link #ALLOCATOR_HARDWARE},
     *  because {@link Bitmap.Config#HARDWARE} Bitmaps cannot be mutable.
     *  Attempting to combine them will throw an
     *  {@link java.lang.IllegalStateException}.</p>
     *
     *  <p>Mutable Bitmaps are also incompatible with {@link #decodeDrawable},
     *  which would require retrieving the Bitmap from the returned Drawable in
     *  order to modify. Attempting to decode a mutable {@link Drawable} will
     *  throw an {@link java.lang.IllegalStateException}.</p>
     */
    public android.graphics.ImageDecoder setMutable(boolean mutable) {
        return this;
    }

    /**
     * Specify whether to potentially save RAM at the expense of quality.
     *
     *  Setting this to {@code true} may result in a {@link Bitmap} with a
     *  denser {@link Bitmap.Config}, depending on the image. For example, for
     *  an opaque {@link Bitmap}, this may result in a {@link Bitmap.Config}
     *  with no alpha information.
     */
    public android.graphics.ImageDecoder setPreferRamOverQuality(boolean preferRamOverQuality) {
        return this;
    }

    /**
     * Specify whether to potentially treat the output as an alpha mask.
     *
     *  <p>If this is set to {@code true} and the image is encoded in a format
     *  with only one channel, treat that channel as alpha. Otherwise this call has
     *  no effect.</p>
     *
     *  <p>setAsAlphaMask is incompatible with {@link #ALLOCATOR_HARDWARE}. Trying to
     *  combine them will result in {@link #decodeDrawable}/
     *  {@link #decodeBitmap} throwing an
     *  {@link java.lang.IllegalStateException}.</p>
     */
    public android.graphics.ImageDecoder setAsAlphaMask(boolean asAlphaMask) {
        return this;
    }

    @java.lang.Override
    public void close() {
    }

    /**
     * Create a {@link Drawable} from a {@code Source}.
     *
     * @param src
     * 		representing the encoded image.
     * @param listener
     * 		for learning the {@link ImageInfo} and changing any
     * 		default settings on the {@code ImageDecoder}. If not {@code null},
     * 		this will be called on the same thread as {@code decodeDrawable}
     * 		before that method returns.
     * @return Drawable for displaying the image.
     * @throws IOException
     * 		if {@code src} is not found, is an unsupported
     * 		format, or cannot be decoded for any reason.
     */
    @android.annotation.NonNull
    public static android.graphics.drawable.Drawable decodeDrawable(@android.annotation.NonNull
    android.graphics.ImageDecoder.Source src, @android.annotation.Nullable
    android.graphics.ImageDecoder.OnHeaderDecodedListener listener) throws java.io.IOException {
        android.graphics.Bitmap bitmap = android.graphics.ImageDecoder.decodeBitmap(src, listener);
        return new android.graphics.drawable.BitmapDrawable(src.getResources(), bitmap);
    }

    /**
     * See {@link #decodeDrawable(Source, OnHeaderDecodedListener)}.
     */
    @android.annotation.NonNull
    public static android.graphics.drawable.Drawable decodeDrawable(@android.annotation.NonNull
    android.graphics.ImageDecoder.Source src) throws java.io.IOException {
        return android.graphics.ImageDecoder.decodeDrawable(src, null);
    }

    /**
     * Create a {@link Bitmap} from a {@code Source}.
     *
     * @param src
     * 		representing the encoded image.
     * @param listener
     * 		for learning the {@link ImageInfo} and changing any
     * 		default settings on the {@code ImageDecoder}. If not {@code null},
     * 		this will be called on the same thread as {@code decodeBitmap}
     * 		before that method returns.
     * @return Bitmap containing the image.
     * @throws IOException
     * 		if {@code src} is not found, is an unsupported
     * 		format, or cannot be decoded for any reason.
     */
    @android.annotation.NonNull
    public static android.graphics.Bitmap decodeBitmap(@android.annotation.NonNull
    android.graphics.ImageDecoder.Source src, @android.annotation.Nullable
    android.graphics.ImageDecoder.OnHeaderDecodedListener listener) throws java.io.IOException {
        android.util.TypedValue value = new android.util.TypedValue();
        value.density = src.getDensity();
        android.graphics.ImageDecoder decoder = src.createImageDecoder();
        if (listener != null) {
            listener.onHeaderDecoded(decoder, new android.graphics.ImageDecoder.ImageInfo(decoder), src);
        }
        return android.graphics.BitmapFactory.decodeResourceStream(src.getResources(), value, ((android.graphics.ImageDecoder.InputStreamSource) (src)).mInputStream, decoder.mOutPaddingRect, null);
    }

    /**
     * See {@link #decodeBitmap(Source, OnHeaderDecodedListener)}.
     */
    @android.annotation.NonNull
    public static android.graphics.Bitmap decodeBitmap(@android.annotation.NonNull
    android.graphics.ImageDecoder.Source src) throws java.io.IOException {
        return android.graphics.ImageDecoder.decodeBitmap(src, null);
    }

    public static final class DecodeException extends java.io.IOException {
        /**
         * An Exception was thrown reading the {@link Source}.
         */
        public static final int SOURCE_EXCEPTION = 1;

        /**
         * The encoded data was incomplete.
         */
        public static final int SOURCE_INCOMPLETE = 2;

        /**
         * The encoded data contained an error.
         */
        public static final int SOURCE_MALFORMED_DATA = 3;

        @android.graphics.ImageDecoder.Error
        final int mError;

        @android.annotation.NonNull
        final android.graphics.ImageDecoder.Source mSource;

        DecodeException(@android.graphics.ImageDecoder.Error
        int error, @android.annotation.Nullable
        java.lang.Throwable cause, @android.annotation.NonNull
        android.graphics.ImageDecoder.Source source) {
            super(android.graphics.ImageDecoder.DecodeException.errorMessage(error, cause), cause);
            mError = error;
            mSource = source;
        }

        /**
         * Private method called by JNI.
         */
        @java.lang.SuppressWarnings("unused")
        DecodeException(@android.graphics.ImageDecoder.Error
        int error, @android.annotation.Nullable
        java.lang.String msg, @android.annotation.Nullable
        java.lang.Throwable cause, @android.annotation.NonNull
        android.graphics.ImageDecoder.Source source) {
            super(msg + android.graphics.ImageDecoder.DecodeException.errorMessage(error, cause), cause);
            mError = error;
            mSource = source;
        }

        /**
         * Retrieve the reason that decoding was interrupted.
         *
         *  <p>If the error is {@link #SOURCE_EXCEPTION}, the underlying
         *  {@link java.lang.Throwable} can be retrieved with
         *  {@link java.lang.Throwable#getCause}.</p>
         */
        @android.graphics.ImageDecoder.Error
        public int getError() {
            return mError;
        }

        /**
         * Retrieve the {@link Source Source} that was interrupted.
         *
         *  <p>This can be used for equality checking to find the Source which
         *  failed to completely decode.</p>
         */
        @android.annotation.NonNull
        public android.graphics.ImageDecoder.Source getSource() {
            return mSource;
        }

        private static java.lang.String errorMessage(@android.graphics.ImageDecoder.Error
        int error, @android.annotation.Nullable
        java.lang.Throwable cause) {
            switch (error) {
                case android.graphics.ImageDecoder.DecodeException.SOURCE_EXCEPTION :
                    return "Exception in input: " + cause;
                case android.graphics.ImageDecoder.DecodeException.SOURCE_INCOMPLETE :
                    return "Input was incomplete.";
                case android.graphics.ImageDecoder.DecodeException.SOURCE_MALFORMED_DATA :
                    return "Input contained an error.";
                default :
                    return "";
            }
        }
    }
}

